//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.iface.helper.DateUtil;

public class VariosWebJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/variosweb";
       
    private Logger log = Logger.getLogger(VariosWebJDBCDAO.class);
	
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public VariosWebJDBCDAO() {
		
	}
    
    /**
     * Checkea existencia de un reclamo a registrar.
     * 
     * 
     * @param tipoTramite
     * @param tributo
     * @param cuenta
     * @param anio
     * @param periodo
     * @param fechaPago
     * @return boolean
     * @throws Exception
     */
    public boolean validateReclamoWeb(int tipoTramite,
						    		String tributo, 
						    		String cuenta,
						    		int anio, 
						    		int periodo,
						    		Calendar fechaPago) throws Exception {
    	
    	String funcName = "validateReclamoWeb()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	boolean reclamoValido = true;
    	
    	String sSql = "Select count(*) as c from reclamos " +
					"where tipo_tramite = " + tipoTramite +
					" and tributo = '" + tributo + "'"+
					" and cuenta = " + cuenta +
					" and anio = " + anio + 
					" and periodo = " + periodo +
					" and fecha_pago = '" + DateUtil.formatDate(fechaPago.getTime(), DateUtil.yyyy_MM_dd_MASK) + "'";
    	
    	
    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

    	// conecta con la base de indet via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	pstmt = con.prepareStatement(sSql);
    	rs = pstmt.executeQuery();

    	while (rs.next()) {        		
    		if (rs.getInt("c") > 0 )
    			reclamoValido = false;
    		else 
    			reclamoValido = true;
    	}

    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}

    	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    	return reclamoValido; 
    }
  
}
