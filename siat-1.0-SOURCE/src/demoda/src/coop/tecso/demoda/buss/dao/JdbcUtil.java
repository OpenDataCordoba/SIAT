//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.buss.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class JdbcUtil {
	
	static Logger log = Logger.getLogger(JdbcUtil.class);
	
    static private void loadRs(ResultSet rs, Map<String, Object> m) throws Exception {
		ResultSetMetaData md = rs.getMetaData();
		for(int i=1, count = md.getColumnCount(); i <= count; i++) {
			Object o = rs.getObject(i);
			
			//Coerce BigDecimal to Double
			if (o instanceof BigDecimal) o = Double.valueOf(((BigDecimal) o).doubleValue());
			
			m.put(md.getColumnName(i), o);
		}    	
    }

    /**
     * Retorna una lista de mapa string->Object, cada mapa de la lista
     * representa un row resultado del query.
     * Si el query no retorna rows, entonces la funcion retorna una lista vacia.
	 * IMPORTANTE: Si el query trae muchos registros, esta funcion puede agotar la memoria del JVM
     */
    static public List<Map<String, Object>> queryList(Connection con, String sql) throws Exception {
    	ResultSet rs    = null;
    	Statement st = null;
    	List<Map<String, Object>> ret = new ArrayList<Map<String, Object>>();
    	
    	st = con.createStatement();
    	rs = st.executeQuery(sql);
    	log.debug("executed: " + sql);
    	
    	while (rs.next()) {
    		Map<String, Object> r = new HashMap<String, Object>();
    		loadRs(rs, r);
    		ret.add(r);
    	}
    	
    	rs.close();
    	st.close();
    	log.debug("fetched: " + sql);
    	
    	return ret;
    }

    
    /**
     * Si el query no trae resultados retorna un mapa instanciado pero vacio.
     * Si el query trae valores, informa el primer row, y coloca el valor
     * de cada columna en el mapa.
     */
    static public Map<String, Object> queryRow(Connection con, String sql) throws Exception {
    	List<Map<String, Object>> ret;
    	
    	ret = queryList(con, sql);
    	
    	return ret.size() == 0 ? new HashMap<String, Object>() : ret.get(0);
    }
}
