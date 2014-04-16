//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs.sql;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import coop.tecso.grs.Grs;
import coop.tecso.grs.GrsEngine;
import coop.tecso.grs.GrsMap;

public class Sql {
	String dsname;
	static final char OptionsStart = '[';
	static final char OptionsEnd = ']';
	static final String OptionsSep = ";";
	static final String OptionId = "id";
	static final String OptionNoerr = "noerr";
	static Pattern paramPattern = Pattern.compile("#[msifdo]");
	private Connection privcn = null; //please dont use directly, use cn() method
	
	public Sql(String ds) {
		this.dsname = ds;
	}

	public List<Map<String, Object>> list(String sql, Object... params) throws Exception {    	
		ArrayList<Map<String, Object>> ret;
		Cursor cr = null;
		
		try {
			cr = (Cursor) exec(sql, params);
			ret = new ArrayList<Map<String, Object>>();
			GrsMap row;
			while ((row = cr.read()) != null) {
				ret.add(row);
			}
			return ret;
    	} finally {
    		if (cr != null) cr.close();
    	}
	}

	/**
	 * row execute a sql and return the first row of resultset in a map
	 * where each column name is the key of map.
	 * @return null if sql is an empty result set.
	 */
	public Map<String, Object> row(String sql, Object... params) throws Exception {
		Cursor cr = null;
		try {
			cr = cursor(sql, params);
			Map<String, Object> ret = cr.read();
			return ret;
    	} finally {
    		if (cr != null) cr.close();
    	}
	}

	/**
	 * value ejecuta el sql, y retorna un valor del primer renglon.
	 * sql tiene que ser un query de una sola columna, si retorna más de una columna
	 * el resultado es indeterminado.
	 * 
	 * @return null si el sql no retorna registros o el valor esta en null
	 */
	public Object value(String sql, Object... params) throws Exception {
		Cursor cr = null;
		try {
			cr = cursor(sql, params);
			Map<String, Object> row = cr.read();
			cr.close();

			Object ret = null;
			if (row != null) {
				for(Object value : row.values()) {
					ret = value;
					break; //get first
				}
			}
			return ret;
		} finally {
			if (cr != null) cr.close();
		}
	}
	
	/**
	 * Reemplaza la parte pre from del sql por 'select count(*) '
	 * Ojo: si el query tiene distincts o modificadores similares
	 * estos seran reeplazados tambien.
	 * <p>Bug: en la parte extraida de 'sql' no puede haber parametros
	 * @return el valor de select count(*) from ...
	 */
	public Object count(String sql, Object... params) throws Exception {
		int posFrom = sql.toLowerCase().indexOf("from");
		if (posFrom > 1){
			sql = sql.substring(posFrom);
		}
		
		int posOrder = sql.toLowerCase().indexOf("order by");
		if (posOrder >= 0) {
			sql = sql.substring(0, posOrder);
		}
		
		sql = "select count(*) " + sql;
		return value(sql, params);
	}
	
	public Cursor cursor(String sql, Object... params) throws Exception {
		return (Cursor) exec(sql, params);
	}

	/**
	 * exec execute sql, and return a Cursor or an Long.
	 * @return Cursor or update count depending on result of the sql statement.
	 *         IMPORTANT: if return a Cursor, please close() it after use.
	 */
	public Object exec(String sql, Object... params) throws Exception {
		Connection cn = null;
    	PreparedStatement st = null;
    	Object ret = null;
    	
    	try {
    		cn = cn();
    		st = statement(cn, sql, params);
    		//XXX must handle multiple results?
    		if (st.execute()) {
    			ret = new Cursor(st.getResultSet(), st, cn);
    		} else {
    			ret = getGenkey(st);
    			if (ret == null) {
    				ret = Long.valueOf(st.getUpdateCount());
    			}
    			Sql.closeall(null, st);
    		}
    	} catch (Exception e) {
    		Sql.closeall(null, st);
    		
    		if (sql.indexOf("[noerr]") == -1)
    			throw e;
    	}
    	return ret;
	}
		
	public Map<String, Object> get(String table, Long id) throws Exception {
		String sql = String.format("select * from %s where id = #i", table);
		return row(sql, id);
	}

	public Long remove(String table, Long id) throws Exception {
		String sql = String.format("delete from %s where id = #i", table);
		return (Long) exec(sql, id);
	}

	public Long save(String table, Map<String, Object> row) throws Exception {
		Object oid = row.get("id");
		Long id = null;
		
		Query q = Query.parse(table, new Object[0]);
    	String opts = "[" + Grs.paramToString(q.options) + "]";
    	table = q.sql;
    	
		if (oid != null)
			id = ((Number) row.get("id")).longValue();
		
		Object[] values = new Object[row.size()]; //ignore id column
		String sql;
		
		if (id == null || id <= 0) {
			//insert
			StringBuilder columns = new StringBuilder();
			StringBuilder valuesSign = new StringBuilder();
			int i = 0;
			
			for (Entry<String, Object> e : row.entrySet()) {
				if ("id".equals(e.getKey()))
					continue;
				columns.append(",").append(e.getKey());
				valuesSign.append(",#o");
				values[i++] = e.getValue();
			}
			
			sql = String.format("%s insert into %s (%s) values (%s)",
					opts, table, columns.substring(1), valuesSign.substring(1));
		} else {
			//update
			StringBuilder columns = new StringBuilder();
			int i = 0;
			
			for (Entry<String, Object> e : row.entrySet()) {
				if ("id".equals(e.getKey()))
					continue;
				columns.append(",").append(e.getKey()).append("=#o");
				values[i++] = e.getValue();
			}
			
			sql = String.format("%s update %s set %s where id=%s",
					opts, table, columns.substring(1), id);
		}
		
		//finally exec sql
		Long ret = (Long) exec(sql, values);
		
		return ret;
	}
	
	/**
	 * getGenKey get then first generated key.
	 * Note: a more generic way is needed
	 * @return null if cannot get generated key or there is none.
	 */
	private Long getGenkey(Statement st) {
		Long ret = null;
		try {
			ResultSet gk = st.getGeneratedKeys();
			if (gk.next())
				ret = gk.getLong(1);
			return ret;
		} catch (Exception e) {
			return null;
		}
	}
	
    private PreparedStatement statement(Connection cn, String sql, Object... params) throws SQLException {
    	int retid = Statement.NO_GENERATED_KEYS;

    	Query q = Query.parse(sql, params);
    	
    	// prepare statement and set it
    	if (q.option(OptionId) != null)
    		retid = Statement.RETURN_GENERATED_KEYS;

    	if (Grs.debugflg > 0)
    		Grs.debug("Grs Sql: opts:[%s] sql=%s\n", Grs.paramToString(q.options), q.sql);
    	
    	PreparedStatement st = cn.prepareStatement(q.sql, retid);
    	
    	Grs.debug("Grs Sql: Params: ");
    	for(int i = 0, np = 1; i < q.params.length; i++) {
    		Object value = q.params[i];
    		String type = q.cmds[i];
    		    		
    		//may be NativeDate or others
    		if (value != null && value instanceof org.mozilla.javascript.ScriptableObject) {
    			value = GrsEngine.jsToJvm(value);
    			if (value instanceof java.util.Date) {
    				value = new Timestamp(((Date) value).getTime());
    			}
    		}
    		
    		if (Grs.debugflg > 0 && i > 0)
    			Grs.debug(", ");
    		
    		if (value == null) {
    			st.setObject(np++, null);
        		Grs.debug("%s", "null");
    			//object handle
        		
    		} else if (type.equals("#o")) {
    			if (value instanceof Date)
        			value = new Timestamp(((java.util.Date) value).getTime());
    			st.setObject(np++, value);
        		Grs.debug("%s", value);
    			
        	//string handle
    		} else if (type.equals("#s")) {
    			st.setString(np++, value.toString());
        		Grs.debug("'%s'", value);
    			
            //integer handle
    		} else if (type.equals("#i") && value instanceof Number) {
    			st.setLong(np++, ((Number) value).longValue());
        		Grs.debug("%s", value);

    		} else if (type.equals("#i") && value instanceof String) {
    			Object o = Grs.paramToObject((String) value);
    			if (!(o instanceof Long))
        			throw new SQLException(String.format("Not supported conversion of type %s value '%s' of parameter %d to '%s'", value.getClass().getName(), value, i+1, type));

    			Long l = (Long) Grs.paramToObject((String)value);
    			st.setLong(np++, l.longValue());
    			Grs.debug("%s", value);
    		
    		//float handle
    		} else if (type.equals("#f") && value instanceof Number) {
    			st.setDouble(np++, ((Number) value).doubleValue());
    			Grs.debug("%s", value);

    		} else if (type.equals("#f") && value instanceof String) {
    			Object o = Grs.paramToObject((String) value);
    			if (!(o instanceof Double))
        			throw new SQLException(String.format("Not supported conversion of type %s value '%s' of parameter %d to '%s'", value.getClass().getName(), value, i+1, type));

    			Double d = (Double) Grs.paramToObject((String)value);
    			st.setDouble(np++, d.doubleValue());
    			Grs.debug("%s", value);
    		
        	//date handle
    		} else if (type.equals("#d") && value instanceof Timestamp) { 
    			st.setTimestamp(np++, (Timestamp) value);
    			Grs.debug("%tF", value);
    		} else if (type.equals("#d") && value instanceof Date) { 
    			st.setTimestamp(np++, new Timestamp(((java.util.Date) value).getTime()));
    			Grs.debug("%tF", value);
    		} else if (type.equals("#d") && value instanceof String) {
    			Object o = Grs.paramToObject((String) value);
    			if (!(o instanceof Date))
        			throw new SQLException(String.format("Not supported conversion of type %s value '%s' of parameter %d to '%s'", value.getClass().getName(), value, i+1, type));
    				
    			st.setTimestamp(np++, new Timestamp(((Date)o).getTime()));
    			Grs.debug("%F", o);
    		//unknow handle
    		} else {
    			throw new SQLException(String.format("Not supported conversion of type %s value '%s' of parameter %d to '%s'", value.getClass().getName(), value, i+1, type));
    		}
    	}
		Grs.debug("\n");
    	
    	return st;
    }
	
	private Connection cn() throws Exception {
		//this method may be change internally, 
		//please use this method to get db conections.
		if (privcn == null) {
			privcn = Sql.connect(dsname);
		}
		
		return this.privcn;
	}
	
	public void close() {
		try { if (privcn != null) { privcn.close(); privcn = null;} } catch (Exception e) {}
	}

	//util
	static Connection connect(String dsname) throws Exception {
		Connection result = null;
		Context initialContext = new InitialContext();
		DataSource datasource = (DataSource)initialContext.lookup(dsname);
		result = datasource.getConnection();
		result.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		return result;
	}
	
    static void loadmap(ResultSet rs, Map<String, Object> m) throws SQLException {
		ResultSetMetaData md = rs.getMetaData();
		for(int i=1, count = md.getColumnCount(); i <= count; i++) {
			Object o = rs.getObject(i);
			
			//Coerce BigDecimal to Double
			if (o instanceof BigDecimal) o = Double.valueOf(((BigDecimal) o).doubleValue());
			if (o instanceof Timestamp) o = new Date(((Timestamp)o).getTime());
			
			m.put(md.getColumnName(i), o);
		}
    }

    static void closeall(ResultSet rs, Statement st) {
		try { if (rs != null) rs.close(); } catch (Exception e) {} 
		try { if (st != null) st.close(); } catch (Exception e) {} 
	}
	
}