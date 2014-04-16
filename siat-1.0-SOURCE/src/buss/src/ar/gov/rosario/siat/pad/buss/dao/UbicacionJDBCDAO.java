//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class UbicacionJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/gisdb";
       
    private Logger log = Logger.getLogger(UbicacionJDBCDAO.class);
	
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public UbicacionJDBCDAO() {
		
	}

    /** Obtiene las cuatro manzanas que 
     *  rodean al punto (x,y) pasado como parametro
     *  El valor se devuelve como: "seccion/manzana" ej: (2/41)
     * 
     * @param ejeX
     * @param ejeY
     * @return
     * @throws Exception
     */
    public List<String> getManzanasCircundantes (Double ejeX, Double ejeY) throws Exception {
        
    	String funcName = DemodaUtil.currentMethodName();
    	List<String> manzanasCircundantes = new ArrayList<String>();
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;

    	String sSql = "SELECT DISTINCT seccion, muni_manza from MANZANAS " +
    				  "WHERE st_distance(the_geom, st_pointfromtext " +
    				  "('POINT(" + ejeX + " " +  ejeY + ")',0)) < 30 " +
    				  "ORDER BY 1,2"; 

    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	pstmt = con.prepareStatement(sSql);
    	rs = pstmt.executeQuery();

    	while (rs.next()) {
    		Long idSeccion = rs.getLong("seccion");    		
    		Long idManzana = rs.getLong("muni_manza");
    		manzanasCircundantes.add(idSeccion + "/" + idManzana); 
    	}

    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	return manzanasCircundantes; 
    }
}
