package alone;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLResultsInformixImpl implements ISQLResults {

	/**
	 * Retrieves a List<String> with name of Tables found in the Database.
	 */
	public List<String> getListTableName(Connection cn) throws Exception {

		List<String> listTableName = new ArrayList<String>();
		
		//Prepare the query
		String queryStr="SELECT tabname FROM systables WHERE version > 80000";
		
		// Get a statement from the connection
		Statement st = cn.createStatement();	
		// Execute the query
		ResultSet rs = st.executeQuery(queryStr);		
		
		while (rs.next()) 
			listTableName.add(rs.getString(1));
		
		cn.close();
		
		return listTableName;
	}
	
	
	/**
	 * Retrieves a List<String> with name of Columns for a TableName passed as parameter.
	 */
	public List<String> getListColumnName(Connection cn,String tName) throws Exception{
		
		List<String> listColumnName = new ArrayList<String>();
		
		//Prepare the query
		String queryStr="SELECT FIRST 1 * FROM "+tName;
		
		// Get a statement from the connection
		Statement st = cn.createStatement();	
		// Execute the query
		ResultSet rs = st.executeQuery(queryStr);	
		
		// Get the metadata
		ResultSetMetaData md = rs.getMetaData();
		
		for (int i=1; i<=md.getColumnCount(); i++) {
			listColumnName.add(md.getColumnName(i));
		}
		
		return listColumnName;
	}
	
	/**
	 * Retrieves a Map<String,String> formed with <FKCOLUMN_NAME,PKTABLE_NAME> 
	 * for a TableName passed as parameter.
	 */
	public Map<String,String> getMapImportedKey(Connection cn,String tName) throws Exception{
		
		Map<String,String> mapIK = new HashMap<String, String>(); 
		
		DatabaseMetaData dbmd = cn.getMetaData();
		ResultSet rs = dbmd.getImportedKeys(null, null, tName);
		
		while (rs.next()) {
			mapIK.put(rs.getString("FKCOLUMN_NAME"),
					  rs.getString("PKTABLE_NAME"));
		}
		
		return mapIK;
	}
	
	/**
	 * Retrieves a Map<String,String> formed with <COLUMN_NAME,INDEX_NAME> 
	 * for a TableName passed as parameter.
	 */
	public Map<String,String> getMapIndexName(Connection cn, String tName) throws Exception {

		Map<String,String> mapIdx = new HashMap<String, String>(); 
		
		DatabaseMetaData dbmd = cn.getMetaData();
		ResultSet rs = dbmd.getIndexInfo(null, null, tName, false, true);
		
		while (rs.next()) {
			mapIdx.put(rs.getString("COLUMN_NAME"),
					   rs.getString("INDEX_NAME"));
		}
		
		return mapIdx;
	}
}
