package alone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
 * ¿Cómo correr esto?
 * Ir a: ~/<WORKSPACE>/siat-msf/tools/alone
 * Ejecutar alone.sh PGSqlAlterSequences sequence
 */
public class PGSqlAlterSequences {

	private final String POSTGRESQL_DRIVER = "PostgreSQL";
	private final String INFORMIX_DRIVER = "Informix";

	private final String url="jdbc:postgresql://dbsrv1:5432/siatmsf?user=sat&password=pachitea24";
	
//	private final String url="jdbc:informix-sqli://192.168.1.5:1500/siatr5:INFORMIXSERVER=demo_on;IFX_LOCK_MODE_WAIT=20;user=informix;password=paCHitea24";
	
	
	public static void main(String[] args) throws Exception {
		
		System.out.println("PGSqlAlterSequences:");
		PGSqlAlterSequences alterSequences = new PGSqlAlterSequences();
		
		if (args.length>1 && "sequence".equalsIgnoreCase(args[1])) {
			alterSequences.updateSequences();
		}else {
			alterSequences.fkScriptGenerator();
		}
		
	}
	
	private void updateSequences() throws Exception {
		
		// Get a connection to the database
		Connection cn = DriverManager.getConnection(url);	
		// Get the name of this database.
		String drvName = cn.getMetaData().getDatabaseProductName();
		
		SQLResultsPostgresImpl sqlResult = null;
		if (drvName.equals(POSTGRESQL_DRIVER)) {
			sqlResult = new SQLResultsPostgresImpl();
		}else{
			System.err.println("The program doesn't support the driver: "+drvName);
			return;
		}
		
		Integer maxId;
		List<String> tablesName = sqlResult.getListTableName(cn);
		for (String tName : tablesName) {
			// Get a connection to the database
			cn = DriverManager.getConnection(url);	
			// Obtain de Max Id for de table
			maxId = sqlResult.getMaxId(cn, tName);
			if (sqlResult.updateSequence(cn,tName, maxId+1)) 
				System.out.println(tName + "_id_seq "+"actualizada OK");		
		}
	}
	
	
	private void fkScriptGenerator()throws Exception {
		
		Class.forName("com.informix.jdbc.IfxDriver");
		
		// Get a connection to the database
		Connection cn = DriverManager.getConnection(url);	
		
		// Get the name of this database.
		String drvName = cn.getMetaData().getDatabaseProductName();
		
		ISQLResults sqlResult = null;
		if (drvName.equals(POSTGRESQL_DRIVER)) {
			sqlResult = new SQLResultsPostgresImpl();
		}else if (drvName.contains(INFORMIX_DRIVER)){
			sqlResult = new SQLResultsInformixImpl();
		}else{
			System.err.println("The program doesn't support the driver: "+drvName);
			return;
		}
		
		List<String> tablesName = sqlResult.getListTableName(cn);
		for (String tableName : tablesName) {
			
			if (!tableName.contains("_") || tableName.contains("_sq")) continue;
			// Get a connection to the database
			cn = DriverManager.getConnection(url);
			// Map with imported keys
			Map<String,String> mapImpKey = sqlResult.getMapImportedKey(cn,tableName);
			// Map with indexes
			Map<String,String> mapIdx = sqlResult.getMapIndexName(cn,tableName);
			//List with columns name 
			List<String> columnsName = sqlResult.getListColumnName(cn, tableName);
			// Close connection
			cn.close();
			int fkIndex = mapImpKey.size();
			for (String col : columnsName) {
				boolean createIdx = false;
				// The column is 'id' or not start with word 'id'
				if (col.equals("id") || !col.startsWith("id")) continue;

				// The column hasn't index
				if (null == mapIdx.get(col)) createIdx = true;
				
				// The column hasn't foreing key
				if (null == mapImpKey.get(col)){
					fkIndex++;
					//Generate the 'alter' to create the foreing key
					String alterStr = generateAlterFKLine(tableName, col, tablesName,fkIndex);
					System.out.println(alterStr);
					
					// Create fk index only in PostGre database. 
					createIdx = drvName.equalsIgnoreCase(POSTGRESQL_DRIVER);
				}
				
				if (createIdx) {
					//Generate the 'create' to new index
					String indexStr = generateCreateIdxLine(tableName, col);
					System.out.println(indexStr);
				}
			}
		}
	}
	
	private String generateAlterFKLine(String tableName,String column,List<String>tables, int index){
		
		String alterStr = "ALTER TABLE "+tableName+" ADD CONSTRAINT "+tableName+"_fk"+index+"_"+column+" FOREIGN KEY ("+column+") REFERENCES ";
		List<String> refs = new ArrayList<String>();
		for (String tName : tables) {
			if (tName.substring(4).equals(column.substring(2))) refs.add(tName);
		}
		if (refs.size() == 0) {
			//Hasn't reference
			alterStr="--NR: "+alterStr;
		}else if (refs.size() == 1) {
			//Has reference
			alterStr+=refs.get(0)+"(id);";
		}else {
			//Has double reference
			for (String tabRef : refs) 
				alterStr="--DR: "+alterStr+tabRef+"(id);";
		}
		
		return alterStr;
	}
	
	private String generateCreateIdxLine(String tableName,String column){
		String indexStr = "CREATE INDEX "+tableName+"_idx_"+column+" ON "+tableName+"("+column+");";

		return indexStr;
	}

}
