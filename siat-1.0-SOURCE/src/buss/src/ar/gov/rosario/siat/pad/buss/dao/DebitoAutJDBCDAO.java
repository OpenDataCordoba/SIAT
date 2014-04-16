//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class DebitoAutJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/debitoaut";
       
    private Logger log = Logger.getLogger(DebitoAutJDBCDAO.class);
	
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public DebitoAutJDBCDAO() {
		
	}

    /**
     * 
     * @param  nroSistema
     * @param  nroCuenta
     * @return String
     * @throws Exception
     */
    public String getTipoAdhe(String nroCuenta) throws Exception {
        
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	String tipoAdhe = null;
        
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	try {
        	String nroSistema = "2";
        	String sSql = "SELECT tipo_adhe FROM adhesiones " +
        		" WHERE id_sistema = " + nroSistema +    
        		" AND  cuenta =" + nroCuenta;

        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

        	// conecta con la base de debitoAut via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);

        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sSql);
        	rs = pstmt.executeQuery();

        	while (rs.next()) {        		
        		tipoAdhe = rs.getString("tipo_adhe");
        	}

        	rs.close();
        	pstmt.close();
        	con.close();

        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tipoAdhe; 
		} catch (Exception e) {
			log.error("Error: ",  e);
			throw new Exception(e);
		}finally{
			con.close();
		}
    }
    
    /**
     * Obtiene el mapa formado por la clave como el nro de cuenta y valor por el tipo de adhesion
     * @return Map<Long,String>
     * @throws Exception
     */
    public Map<String,String> getMapaTipoAdhe() throws Exception {
        
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
        Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	Map<String, String> tiposAdhe = new HashMap<String, String>();
    	
    	try {
        	String nroSistema = "2";
        	
        	String strQry = "";
        	strQry += "select cuenta, tipo_adhe from adhesiones ";
        	strQry += "where id_sistema = " + nroSistema;
        	
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + strQry);

        	// conecta con la base de debitoAut via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);

        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(strQry);
        	rs = pstmt.executeQuery();

        	while (rs.next()) {
        		String tipoAdhe = rs.getString("tipo_adhe");
        		String nroCuenta = rs.getString("cuenta");
        		tiposAdhe.put(nroCuenta, tipoAdhe);
        	}

        	rs.close();
        	pstmt.close();

        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return tiposAdhe; 
		
    	} catch (Exception e) {
			log.error("Error: ",  e);
			throw new Exception(e);
		} finally {
			con.close();
		}
    }

}
