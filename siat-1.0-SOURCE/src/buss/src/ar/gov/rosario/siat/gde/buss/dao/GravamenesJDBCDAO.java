//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.gde.buss.bean.TipoTramite;
import ar.gov.rosario.siat.gde.iface.model.TramiteAdapter;
import coop.tecso.demoda.buss.dao.JDBCConnManager;

public class GravamenesJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/gravamenes";
       
    private Logger log = Logger.getLogger(GravamenesJDBCDAO.class);
	
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public GravamenesJDBCDAO() {
		
	}
      
    /**
     * Recupera los Tipos de Tramites de la tabla identificaciones done id_sistema sea = 25. 
     * 
     * 
     */
    public List<TipoTramite> getListTipoTramite() throws Exception {
        
    	String funcName = "getListTipoTramite()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
        Connection        con   = null;
        PreparedStatement pstmt = null;
        ResultSet         rs    = null;
        try {

        	String sql = "SELECT cod_id, descripcion FROM identificaciones  WHERE id_sistema = 25";
        	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

        	// conecta con la base de indet via jdbc
        	con = JDBCConnManager.getConnection(DS_NAME);
        	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	pstmt = con.prepareStatement(sql);
        	rs = pstmt.executeQuery();

        	List<TipoTramite> listTipoTramite = new ArrayList<TipoTramite>();

        	while (rs.next()) {
        		TipoTramite tt = new TipoTramite(rs.getString("cod_id"), rs.getString("descripcion"));
        		listTipoTramite.add(tt);
        	}

        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return listTipoTramite;
        } catch (Exception ex) {
        	throw ex;
        } finally {
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	try {con.close();}   catch (Exception ex) {}
        }
    }
    
    /**
     * Chequea contra el sistema de gravamenes que se trate de un recibo valido.
     * 
     * Consultando por cod_id, nro_recibo y anio_recibo.
     * 
     * 
     * @author Cristian
     * @param tramiteSearchPage
     * @return
     * @throws Exception
     */
    public boolean getEsReciboValido(TramiteAdapter tramiteAdapter)throws Exception {

    	String funcName = "getEsReciboValido()";
    	boolean ret = false;

    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");

    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	try {

    		String sql = "SELECT COUNT(*) cant " +
    		" FROM detalle_rec det, valores_emi val " +
    		" WHERE det.nro_deuda = val.nro_deuda " +
    		" AND det.anio_deuda = val.anio_deuda " +
    		" AND det.nro_recibo = " + tramiteAdapter.getTramite().getNroRecibo() + 
    		" AND det.anio_recibo = " + tramiteAdapter.getTramite().getAnioRecibo() +
    		" AND val.cod_id = " + tramiteAdapter.getTramite().getTipoTramite().getCodTipoTramite();   	

    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

    		// conecta con la base de indet via jdbc
    		con = JDBCConnManager.getConnection(DS_NAME);
    		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    		pstmt = con.prepareStatement(sql);
    		rs = pstmt.executeQuery();

    		while (rs.next()) {
    			Integer cantidad = rs.getInt("cant");

    			if (cantidad >= 1){
    				ret = true;
    			} else {
    				ret = false;
    			}
    		}

    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
    		return ret;
    	} catch (Exception ex) {
    		throw ex;
    	} finally {
    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    		try {con.close();}   catch (Exception ex) {}
    	}
    } 	
}
