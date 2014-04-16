package alone;

import java.sql.Connection;
import java.util.List;
import java.util.Map;

public interface ISQLResults {

	/**
	 * Retrieves a List<String> with name of Tables found in the Database.
	 */
	public List<String> getListTableName(Connection cn) throws Exception;
	
	/**
	 * Retrieves a List<String> with name of Columns for a TableName passed as parameter.
	 */
	public List<String> getListColumnName(Connection cn,String tName) throws Exception;
	
	/**
	 * Retrieves a Map<String,String> formed with <FKCOLUMN_NAME,PKTABLE_NAME> 
	 * for a TableName passed as parameter.
	 */
	public Map<String,String> getMapImportedKey(Connection cn,String tName) throws Exception;
	
	/**
	 * Retrieves a Map<String,String> formed with <COLUMN_NAME,INDEX_NAME> 
	 * for a TableName passed as parameter.
	 */
	public Map<String,String> getMapIndexName(Connection cn,String tName) throws Exception;
	
	
}
