//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.def.buss.bean.IndiceAct;
import ar.gov.rosario.siat.pad.iface.model.LetraCuit;
import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 * Fachada al sistema general 
 * @author Tecso Coop. Ltda.
 */
public class GeneralFacade {

	private static Logger log = Logger.getLogger(GeneralFacade.class);
	private static final GeneralFacade INSTANCE = new GeneralFacade();
	private static final String dsName = "java:comp/env/ds/generaldb";
	
	/**
	 * Constructor privado
	 */
	private GeneralFacade() {
	}

	/**
	 * Devuelve unica instancia
	 */
	public static GeneralFacade getInstance() {
		return INSTANCE;
	}
	
	/**
	 * Recupera la lista de todos los indices menusales cargados
	 * @return
	 * @throws Exception
	 */
	public List<IndiceAct> getListInidiceMensual() throws Exception {
		String funcName = "getListInidiceMensual()";
		List<IndiceAct> ret = new ArrayList<IndiceAct>();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Connection cn = JDBCConnManager.getConnection(dsName);
		String sql = "select * from indices";
		PreparedStatement ps = cn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			IndiceAct indice = new IndiceAct();
			indice.setMensualAnioApli(rs.getInt("anio_apli"));
			indice.setMensualMesApli(rs.getInt("mes_apli"));
			indice.setMensualAnioVen(rs.getInt("anio_ven"));
			indice.setMensualMesVen(rs.getInt("mes_ven"));
			indice.setMensualIndiceMin(rs.getDouble("indice_min"));
			indice.setMensualIndiceMax(rs.getDouble("indice_max"));
			ret.add(indice);
		}
		rs.close();
		ps.close();
		cn.close();
		return ret;
	} 

	/**
	 * Recupera la lista de todos los indices diarios ordenados por fecha_desde
	 * @return
	 * @throws Exception
	 */
	public List<IndiceAct> getListInidiceDiario() throws Exception {
		String funcName = "getListInidiceDiario()";
		List<IndiceAct> ret = new ArrayList<IndiceAct>();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		Connection cn = JDBCConnManager.getConnection(dsName);
        cn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
		String sql = "select * from indices_diario order by fecha_desde";
		PreparedStatement ps = cn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			IndiceAct indice = new IndiceAct();
			indice.setDiarioFechaDesde(rs.getTimestamp("fecha_desde"));
			indice.setDiarioFechaHasta(rs.getTimestamp("fecha_hasta"));
			if (rs.wasNull()) {
				indice.setDiarioFechaHasta(IndiceAct.MAX_DATE);
			}
			indice.setDiarioCoeficiente(rs.getDouble("coeficientes"));
			ret.add(indice);
		}
		rs.close();
		ps.close();
		cn.close();
		return ret;
	}

	/**
	 * Obtiene los datos minimos de una persona: id, nombre, opellido o razon social.
	 * Si no encuentra persona fisica o juridica, devuelve null
	 * @return Persona con los datos de arriba cargados.
	 * @throws Exception
	 */
	public Persona getPersonaByIdLight(Long id) throws Exception {
		String funcName = "getPersonaByIdLight()";
    	
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
        
		return Persona.getByIdNull(id);
	} 
	
	/**
	 * Recupera la lista de todos los id de persona
	 * @return
	 * @throws Exception
	 */
	public List<Integer> getListIdPersona(Connection cn) throws Exception {
		String funcName = "getListInidiceMensual()";
		List<Integer> ret = new ArrayList<Integer>();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		String sql = "select id_persona from persona_id";
		PreparedStatement ps = cn.prepareStatement(sql);

		ResultSet rs = ps.executeQuery();
		while (rs.next()) {
			ret.add(new Integer(rs.getInt("id_persona")));
		}
		rs.close();
		ps.close();

		return ret;
	} 
	
	/**
	 * Obtiene un mapa de la persona con: Clave=id Value=CUIT.
	 * 
	 * @return mapa de personas.
	 * @throws Exception
	 */
	public Map<Long, String> getMapaPersona(Connection con) throws Exception {
     	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	Map<Long, String> mapPersona = new HashMap<Long, String>();
    	
        try {
    		
            String sSql = "select pid.id_persona, " +
				            "pid.cuit00, pid.cuit01, pid.cuit02, pid.cuit03 from persona_id pid "; 

	        pstmt = con.prepareStatement(sSql);
        	rs = pstmt.executeQuery();
        	
        	// Si encuentro resultado.
        	while(rs.next()) {
        		String cuit = String.format("%s-%s-%s-%s",
        						rs.getString("cuit00").charAt(0),
        						rs.getString("cuit01"),
        						rs.getString("cuit02"),
        						rs.getString("cuit03"));
        		
        		mapPersona.put(rs.getLong("id_persona"), cuit);       	
        	} 
        	
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	
        	return mapPersona; 

        } catch (Exception e) {
        	e.printStackTrace();
        	
        	try {rs.close();}    catch (Exception ex) {}
        	try {pstmt.close();} catch (Exception ex) {}
        	
        	throw new DemodaServiceException(e);
        }
	}
	
	
	/**
	 * Obtiene el numero de comercio para uno nuevo 
	 * @return
	 * @throws Exception
	 */
	 public static synchronized int getNroDeComercio(String tabla, String columna) throws Exception {
		 //XXX siatgpl:implement
		 return 1;
	 }

	
    /**
     * Construye un mapa con las descripciones de las localidades,
     * con clave codPostal codSubpostal.
     * 
     * @return Map<String,String>
     * @throws Exception
     */
    public Map<String,String> getMapaLocalidades() throws Exception {
       	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
        Connection con = null;
    	PreparedStatement ps = null;
    	ResultSet rs = null;
    	Map<String, String> localidades = new HashMap<String, String>();
    	
    	try {
        	String strQuery = "";
        	strQuery += "select cod_postal, sub_postal, desc_postal from localidades";

           	if (log.isDebugEnabled()) log.debug(funcName + " Query: " + strQuery);

        	// conecta con la base de debitoAut via jdbc
           	con = JDBCConnManager.getConnection(dsName);
           	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

        	ps = con.prepareStatement(strQuery);
        	rs = ps.executeQuery();

        	while (rs.next()) {
	    		String codPostal = rs.getString("cod_postal");
	    		String codSubPostal = rs.getString("sub_postal");
	    		String descPostal = rs.getString("desc_postal");
	    		localidades.put(codPostal + codSubPostal, descPostal);
        	}

        	rs.close();
        	ps.close();

        	if (log.isDebugEnabled()) log.debug(funcName + ": exit");
        	return localidades; 
		
    	} catch (Exception e) {
			log.error("Error: ",  e);
			throw new Exception(e);
		} finally {
			con.close();
		}
    }

}
