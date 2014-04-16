//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package alone; 

import java.sql.Connection;
import java.sql.ResultSet;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class SweDump {

	static synchronized private Connection connection(String dsn) throws Exception {
		Connection result = null;
		try {
			Context initialContext = new InitialContext();
			DataSource datasource = (DataSource)initialContext.lookup(dsn);
			if (datasource != null) {
				result = datasource.getConnection();
			    result.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
			} 
			return result;
		} catch (Exception e) {
			if (result != null) result.close();
			throw e;
		}
	}

	public static void main(String[] args) throws Exception {
		System.out.printf("SweDump\n\n");
		SweDump main = new SweDump();
		
		Alone.init(false, false);
		
		main.dumpMenu();
	}

	private String makesql(Long padre, boolean leaf) {
		String clause = "";
		String qry = "";
		
		if (leaf) {
			qry = "select im.*, ac.nombreaccion, ac.nombremetodo " +
			" from swe_itemmenu im, swe_accmodapl ac " +
			" where " +
			" im.idaplicacion = 2 " +
			" and (im.idaccmodapl = ac.id)" +
			" and url is not null " +
			" %s " + 
			" order by im.nroorden, ac.nombreaccion, ac.nombremetodo ";
		} else {
			qry = "select im.*" +
			" from swe_itemmenu im" +
			" where " +
			" im.idaplicacion = 2 " +
			" and idaccmodapl is null " +
			" %s " + 
			" order by im.nroorden, im.id ";
		} 
		
		if (padre == null) {
			clause = " and im.iditemmenupadre is null";
		} else {
			clause = " and im.iditemmenupadre = " + padre;			
		}
		
		String sql = String.format(qry, clause);
		return sql;
	}
		
	private String trunk(String s, int n) {
		if (s.length() > n) {
			return s.substring(0,n);
		}
		return s;
	}
	private void dumpMenu() throws Exception {
		Connection cn = connection("java:comp/env/ds/swe");
		
		//nivel 0
		String sql0 = makesql(null, false);
		ResultSet rs0 = cn.createStatement().executeQuery(sql0);
		while (rs0.next() ) {
			String titulo0 = rs0.getString("titulo"); 
			//nivel 1
			String sql1 = makesql(rs0.getLong("id"), false);
			ResultSet rs1 = cn.createStatement().executeQuery(sql1);
			while (rs1.next() ) {
				String titulo1 = rs1.getString("titulo"); 
				//nivel 2
				String sql2 = makesql(rs1.getLong("id"), true);
				ResultSet rs2 = cn.createStatement().executeQuery(sql2);
				while (rs2.next()) {
					String titulo2 = rs2.getString("titulo");

					System.out.printf("%-20s|%-25s|%-30s|%-36s|%s\n",
							trunk(titulo0, 20), trunk(titulo1, 25), trunk(titulo2, 30),
							rs2.getString("nombreaccion") + ":" + rs2.getString("nombremetodo"),
							rs2.getString("url"));
				}
				rs2.close();
			}
			rs1.close();
		}
		rs0.close();
		cn.close();
	}
	
}
