//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hibernate.Session;

import ar.gov.rosario.siat.bal.buss.bean.CierreBanco;
import ar.gov.rosario.siat.bal.buss.bean.CierreSucursal;
import ar.gov.rosario.siat.bal.buss.bean.Conciliacion;
import ar.gov.rosario.siat.bal.buss.bean.DetalleDJ;
import ar.gov.rosario.siat.bal.buss.bean.DetallePago;
import ar.gov.rosario.siat.bal.buss.bean.EnvNotObl;
import ar.gov.rosario.siat.bal.buss.bean.EnvioOsiris;
import ar.gov.rosario.siat.bal.buss.bean.EstDetDJ;
import ar.gov.rosario.siat.bal.buss.bean.EstDetPago;
import ar.gov.rosario.siat.bal.buss.bean.EstTranAfip;
import ar.gov.rosario.siat.bal.buss.bean.EstadoEnvio;
import ar.gov.rosario.siat.bal.buss.bean.NotaConc;
import ar.gov.rosario.siat.bal.buss.bean.NotaImpto;
import ar.gov.rosario.siat.bal.buss.bean.NovedadEnvio;
import ar.gov.rosario.siat.bal.buss.bean.TipoOperacion;
import ar.gov.rosario.siat.bal.buss.bean.TranAfip;
import ar.gov.rosario.siat.bal.iface.model.DetallePagoVO;
import ar.gov.rosario.siat.base.buss.dao.SiatHibernateUtil;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.dao.JDBCConnManager;
import coop.tecso.demoda.buss.dao.JdbcUtil;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.FormularioAfip;
import coop.tecso.demoda.iface.model.ImpuestoAfip;
import coop.tecso.demoda.iface.model.SiNo;

public class MulatorJDBCDAO {

	private static final String DS_NAME = "java:comp/env/ds/osirisEnvio";
       
    private Logger log = Logger.getLogger(MulatorJDBCDAO.class);
	
    private HashMap<String, Long> recursoCodId = new HashMap<String, Long>(); // tabla codigo recurso, idrecurso
    
	/**
	 * Constructor
	 * Pasa nombre del archivo de propiedades a la super de Demoda
	 */
    public MulatorJDBCDAO() {
		
	}
    
    /**
     * Dado un codigo de recurso retorna una idRecurso,
     * utilizando un cache interno para acceso rapido.
     * @param codRecurso
     * @return
     */
    private Long findIdRecurso(String codRecurso) throws Exception {
    	Long ret = recursoCodId.get(codRecurso);
    	if (ret == null) {
    		Recurso recurso = Recurso.getByCodigo(codRecurso);
    		if (recurso != null) {
    			recursoCodId.put(recurso.getCodRecurso(), recurso.getId());
    			ret = recurso.getId();
    		}
    	}

    	return ret;
    }
    
    /**
     * Lee como un string el campo.
     * Importante: Si valor esta almacenado en contenido_n, lo trunca y lo pasa a String
     */
    public String readCampoString(ResultSet rs) throws Exception {
    	String ret = "";
		String tipoContenido = rs.getString("tipo_contenido");
		
    	// Se toma el valor del numero de cuenta del campo nro 4 
    	if (!StringUtil.isNullOrEmpty(tipoContenido) && tipoContenido.toUpperCase().equals("N")) {
    		ret = String.valueOf(rs.getLong("contenido_n"));
    	} else {
    		ret = rs.getString("contenido_c");
    	}
    	
    	return ret;
    }
    
    
    public List<EnvioOsiris> getListNuevosEnviosByFechaMayorIgual(Date fecha) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	List<EnvioOsiris>listEnvioOsiris = new ArrayList<EnvioOsiris>();
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	
    	String sSql = "select e.envio_id as idEnvio, e.org_recaud_id as idOrgRec, e.fecha_Registro as fecha, e.cant_cierre_banco as canCieBan  from envio e";
    	
    	if (fecha != null)
    		sSql+=" where e.fecha_Registro >= TO_DATE('" + DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK) + "','%d/%m/%Y')";  
    		
    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

    	// conecta con la base de mulator via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	pstmt = con.prepareStatement(sSql);
    	
    	rs = pstmt.executeQuery();
    	
    	log.debug("RS: empieza");
    	
    	//recorro la lista de resultados devuelta
    	while (rs.next()){
    		
    		Long idEnvio=rs.getLong("idEnvio");
    		Long idOrgRec = rs.getLong("idOrgRec");
    		log.debug("RS: 1");
    		log.debug("idEnvio: "+idEnvio);
    		
    		if (EnvioOsiris.getByIdEnvioAfip(idEnvio)==null){
    		
	    		EnvioOsiris envioOsiris = new EnvioOsiris();
	    		envioOsiris.setFechaRegistroMulat(rs.getDate("fecha"));
	    		envioOsiris.setIdEnvioAfip(idEnvio);
	    		envioOsiris.setIdOrgRecAfip(idOrgRec);
	    		envioOsiris.setFechaRecepcion(new Date());
	    		envioOsiris.setCanDecJur(0);
	    		envioOsiris.setCantidadPagos(0);
	    		envioOsiris.setImportePagos(0D);
	    		log.debug("va a validar consistencia");
	    		
	    		listEnvioOsiris.add(envioOsiris);
    		}
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return listEnvioOsiris;
    	
    	
    }
    
    /**
     * Verifica la consistencia de un envio osiris en la base de mulator
     * @param envioOsiris
     * @return
     */
    public EnvioOsiris validateConsistenciaMulator(EnvioOsiris envioOsiris) throws Exception{
    	String funcName=DemodaUtil.currentMethodName();

    	if(log.isDebugEnabled())log.debug(funcName+" :enter");
    	Connection con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet rs = null;
    	String sql;
    	Integer canCieBan = null;
    	Long idEnvio = envioOsiris.getIdEnvioAfip();
    	Long idOrgRec = envioOsiris.getIdOrgRecAfip();
    	Integer canTransacciones = 0;
    	EstadoEnvio estadoEnvioInconsistente=EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_INCONSISTENTE);

    	try {
    		envioOsiris.setObservacion("");

    		// conecta con la base de mulator via jdbc
    		con = JDBCConnManager.getConnection(DS_NAME);
    		con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    		sql = "select e.envio_id as idEnvio, e.org_recaud_id as idOrgRec, e.fecha_Registro as fecha, e.cant_cierre_banco as canCieBan  from envio e";
    		sql+= " where e.envio_id = " + envioOsiris.getIdEnvioAfip() + " AND e.org_recaud_id = "+envioOsiris.getIdOrgRecAfip();
    		if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sql);

    		pstmt = con.prepareStatement(sql);
    		rs = pstmt.executeQuery();
    		if (rs.next()){
    			canCieBan = rs.getInt("canCieBan");
    			if (rs.wasNull()) canCieBan = null;
    		} else {
    			envioOsiris.setObservacion("El Envio fue eliminado de la base Mulator");
    			if( envioOsiris.getEstadoEnvio() == null || envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE) {
    				envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    			}
    			return envioOsiris;    		
    		}

    		boolean estaCompleto=false;
    		//1-Verifico si el envio esta completo 
    		//1.1-La cantidad de cierre banco declarada en el envio es correcta 
    		if (canCieBan != null) {
    			String sqlCountCieBan = "select count(cierre_banco) as canCieBan, sum(cant_transaccion) as canTransaccion from envio_cierre_banco " 
    					+ "WHERE org_recaud_id = " + idOrgRec
    					+ " AND envio_id = "+idEnvio + " group by org_recaud_id, envio_id";

    			log.debug("Query canCieBan: "+ sqlCountCieBan);

        		try { rs.close(); } catch (Exception ex) {} //close previous rs
    			try { pstmt.close(); } catch (Exception e) {} //close previous stmt
    			pstmt = con.prepareStatement(sqlCountCieBan);
    			rs = pstmt.executeQuery();

    			if (rs.next() && canCieBan.intValue() == rs.getInt("canCieBan")) {
    				estaCompleto = true;
    				canTransacciones = rs.getInt("canTransaccion");
    				
    			} else {
    				envioOsiris.setObservacion("La cantidad de Cierres de Banco informada en el envio no coincide con los cierres bancos");
    				if (envioOsiris.getEstadoEnvio() == null || envioOsiris.getEstadoEnvio().equals(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PENDIENTE))) {
    					envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    				}
    			}

    			//1.2-La cantidad de transacciones es igual a la suma de las declaradas en cada cierre banco
    			if (estaCompleto) {
    				String sqlCanTran = "select count(t.transaccion_afip) as canTran, sum(t.cant_pago) as canPago, sum(t.cant_detalle) as canDetalle" + 
    				" , sum(t.cant_campo) as canCampo FROM transaccion t , envio_cierre_banco ecb WHERE t.cierre_banco = ecb.cierre_banco "+
    				" AND t.banco = ecb.banco and ecb.envio_id = "+idEnvio+" and ecb.org_recaud_id = "+idOrgRec;

    				log.debug("Query canTran: "+sqlCanTran);

            		try { rs.close(); } catch (Exception ex) {} //close previous rs
            		try { pstmt.close(); } catch (Exception e) {} //close previous stmt
    				pstmt = con.prepareStatement(sqlCanTran);
    				rs = pstmt.executeQuery();

    				if (rs.next() && canTransacciones == rs.getInt("canTran")){
    					Integer canPago = rs.getInt("canPago");
    					Integer canDetalle = rs.getInt("canDetalle");
    					Integer canCampo = rs.getInt("canCampo");

    					String sqlIdTransaccion="select t.transaccion_afip FROM transaccion t, envio_cierre_banco ecb WHERE t.cierre_banco = ecb.cierre_banco";
    						   sqlIdTransaccion+= " AND t.banco = ecb.banco and ecb.envio_id = "+idEnvio+" and ecb.org_recaud_id = "+idOrgRec;

    					String sqlCantidad="";
    					//1.3-la cantidad de transacciones de pagos coinciden con la sumatoria de las declaradas en las transacciones
    					if (canPago!=null && canPago>0 && estaCompleto){
    						sqlCantidad = "select count (transaccion_afip) as canPago from fmlr_pago where transaccion_afip in ("+sqlIdTransaccion+")";

    	            		log.debug("Query cantidad pagos: "+ sqlCantidad);
    	            		
    	            		try { rs.close(); } catch (Exception ex) {} //close previous rs
    	            		try { pstmt.close(); } catch (Exception e) {} //close previous stmt
    						pstmt = con.prepareStatement(sqlCantidad);
    						rs = pstmt.executeQuery();

    						if (rs.next() && canPago != rs.getInt("canPago")){
    							estaCompleto=false;
    							if(envioOsiris.getEstadoEnvio()==null || envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE){
    								envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    							}
    							envioOsiris.setObservacion("La cantidad de pagos no es igual a la descripta en las transacciones");
    						}
    					}

    					//1.4-la cantidad de campos coinciden con la sumatoria de los declaradas en las transacciones
    					if (estaCompleto){
    						sqlCantidad = "select count (transaccion_afip) as canCampo from fmlr_campo where transaccion_afip in ("+sqlIdTransaccion+")";

    						log.debug("Query cantidad campos: "+sqlCantidad);
    	            		try { rs.close(); } catch (Exception ex) {} //close previous rs
    	            		try { pstmt.close(); } catch (Exception e) {} //close previous stmt
    						pstmt = con.prepareStatement(sqlCantidad);
    						rs = pstmt.executeQuery();

    						if (rs.next() && canCampo != rs.getInt("canCampo")){
    							estaCompleto=false;
    							if(envioOsiris.getEstadoEnvio()==null || envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE){
    								envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    							}
    							envioOsiris.setObservacion("La cantidad de campos no es igual a la descripta en las transacciones");
    						}
    					}

    					//1.5-la cantidad de detalles coinciden con la sumatoria de los declaradas en las transacciones
    					if(canDetalle!=null && canDetalle > 0 && estaCompleto){
    						sqlCantidad = "select count (transaccion_afip) as canDetalle from fmlr_disquete where transaccion_afip in ("+sqlIdTransaccion+")";

    						log.debug("Query cantidad detalles: "+sqlCantidad);
    	            		try { rs.close(); } catch (Exception ex) {} //close previous rs
    	            		try { pstmt.close(); } catch (Exception e) {} //close previous stmt
    						pstmt = con.prepareStatement(sqlCantidad);
    						rs = pstmt.executeQuery();

    						if (rs.next() && canDetalle != rs.getInt("canDetalle")){
    							estaCompleto=false;
    							if(envioOsiris.getEstadoEnvio()==null || envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE){
    								envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    							}
    							envioOsiris.setObservacion("La cantidad de detalles de DDJJ no es igual a la descripta en las transacciones");
    						}
    					}
    				} else {
    					estaCompleto=false;
    					if (envioOsiris.getEstadoEnvio() == null || envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE) {
    						envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    					}
    					envioOsiris.setObservacion("La cantidad de transacciones no coincide con la detallada en los cierres bancos");
    				}
    			}	
    		} else {
    			if (envioOsiris.getEstadoEnvio() == null || envioOsiris.getEstadoEnvio().getId().longValue() == EstadoEnvio.ID_ESTADO_PENDIENTE) {
    				envioOsiris.setEstadoEnvio(estadoEnvioInconsistente);
    			}
    			envioOsiris.setObservacion("No se detallaron cierres bancos");
    		}

    		if (estaCompleto) {
    			envioOsiris.setEstadoEnvio(EstadoEnvio.getById(EstadoEnvio.ID_ESTADO_PENDIENTE));
    			envioOsiris.setObservacion("Validaciones realizadas con Exito");
    		}

    	} catch (Exception e) {
    		throw e;
    	} finally {
    		try {rs.close();}    catch (Exception ex) {}
    		try {pstmt.close();} catch (Exception ex) {}
    		try {con.close();}   catch (Exception ex) {}
    	}

    	return envioOsiris;
    }
    
    
    public EnvioOsiris getEnvioCompleto(EnvioOsiris envioOsiris) throws Exception{
    	Session session = SiatHibernateUtil.currentSession();
    	
    	// Obtener Cierres de Banco para el Envio
    	createListCierreBanco(envioOsiris);
    	session.flush();
    	
    	// Obtener Transaccion del Envio
    	if(!envioOsiris.hasError()){
    		createListTranAfip(envioOsiris);
    		session.flush();
    	}
    	
    	// Obtener detalle de pagos para Transacciones del Envio
    	if(!envioOsiris.hasError()){
    		createListDetallePago(envioOsiris);
    		session.flush();
    	}
    	
    	// Obtener detalle de DJ para Transacciones del Envio    	
    	if(!envioOsiris.hasError()){
    		createListDetalleDJ(envioOsiris);	
    		session.flush();
    	}
    	 
    	// Obtener datos de Conciliacion
    	if(!envioOsiris.hasError()){
    		createListNotaImpto(envioOsiris);
    		session.flush();
    	}
    	
    	// Obtener Cierres de Sucursal
    	if(!envioOsiris.hasError()){
    		createListCierreSucursal(envioOsiris);	
    		session.flush();
    	}
    	
    	// Obtener Notas de Abono
    	if(!envioOsiris.hasError()){
    		createListNovedad(envioOsiris);
    		session.flush();
    	}

    	// Obtener lista de registros de Conciliacion
    	if(!envioOsiris.hasError()){
    		createListConciliacion(envioOsiris);
    		session.flush();
    	}
    	
    	// Obtener Notas Conciliadas
    	if(!envioOsiris.hasError()){
    		createListNotaConc(envioOsiris);
    		session.flush();
    	}
    	
    	// Obtener lista de Nota de Obligacion
    	if(!envioOsiris.hasError()){
    		createListNotaObl(envioOsiris);
    		session.flush();
    	}
    	
    	return envioOsiris;
    }
    
    
    public EnvioOsiris createListCierreBanco(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	String sSql = "select banco as banco, cant_transaccion as cantTransaccion, total_importe_neto as importeTotal, cierre_banco  from envio_cierre_banco";
    		   sSql+=" where envio_id = "+envioOsiris.getIdEnvioAfip();  
    		
    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

    	// conecta con la base de mulator via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);
    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	pstmt = con.prepareStatement(sSql);
    	rs = pstmt.executeQuery();
    	
    	List<CierreBanco> listCierreBanco=new ArrayList<CierreBanco>();
    	
    	log.debug("RS: empieza");    	
    	//recorro la lista de resultados devuelta
    	while (rs.next()){
    		CierreBanco cierreBanco = new CierreBanco();
    		cierreBanco.setConciliado(SiNo.NO.getId());
    		cierreBanco.setEnvioOsiris(envioOsiris);
    		cierreBanco.setBanco(rs.getInt("banco"));
    		cierreBanco.setNroCierreBanco(rs.getInt("cierre_banco"));
    		cierreBanco.setCantTransaccion(rs.getLong("cantTransaccion"));
    		cierreBanco.setImporteTotal(rs.getDouble("importeTotal"));
    		if(cierreBanco.getImporteTotal() != null && cierreBanco.getImporteTotal().doubleValue() == 0D){
    			cierreBanco.setConciliado(SiNo.SI.getId());
    		}else{
    			cierreBanco.setConciliado(SiNo.NO.getId());
    		}
    		
    		String sqlCierre ="select fecha_registro as fechaCierre from cierre_banco where cierre_banco = "+cierreBanco.getNroCierreBanco();
			   	   sqlCierre+=" and banco = "+cierreBanco.getBanco();
			   
    		PreparedStatement psCierre=con.prepareStatement(sqlCierre);
    		ResultSet rsCierre= psCierre.executeQuery();
    		
    		if(rsCierre.next())
    			cierreBanco.setFechaCierre(rsCierre.getDate("fechaCierre"));
    		
    		rsCierre.close();
    		psCierre.close();
		
    		if(cierreBanco.validateCreate()){
    			BalDAOFactory.getCierreBancoDAO().update(cierreBanco);
    			listCierreBanco.add(cierreBanco);
    		}else{
    			cierreBanco.passErrorMessages(envioOsiris);
    			break;
    		}    		
    	}
    	
    	if(!envioOsiris.hasError()){
    		envioOsiris.setListCierreBanco(listCierreBanco);
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    
    public EnvioOsiris createListTranAfip(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	List<TranAfip> listTranAfip = new ArrayList<TranAfip>();
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	
    	for(CierreBanco cierreBanco: envioOsiris.getListCierreBanco()){
    		String sqlTran = "select * from transaccion where cierre_banco = "+ cierreBanco.getNroCierreBanco() + " and banco = "+ cierreBanco.getBanco();
    		pstmt = con.prepareStatement(sqlTran);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			TranAfip tranAfip = new TranAfip();
    			tranAfip.setEnvioOsiris(envioOsiris);
    			tranAfip.setCierreBanco(cierreBanco);
    			tranAfip.setIdTransaccionAfip(rs.getLong("transaccion_afip"));
    			tranAfip.setTipoOperacion(TipoOperacion.getById(rs.getLong("tipo_operacion")));
    			tranAfip.setFormulario(rs.getInt("formulario"));
    			tranAfip.setFechaProceso(rs.getDate("fecha_proceso"));
    			tranAfip.setFechaAnulacion(rs.getDate("fecha_anulacion"));
    			tranAfip.setTotMontoIngresado(rs.getDouble("total_monto_ingresado"));
    			tranAfip.setCuit(String.valueOf((rs.getLong("cuit"))));
    			tranAfip.setCanPago(rs.getInt("cant_pago"));
    			tranAfip.setCanDecJur(rs.getInt("cant_detalle"));
    			tranAfip.setVep(rs.getString("nro_vep"));
    			tranAfip.setNroTran(rs.getLong("transaccion"));
    			//tranAfip.setNroTranPres(new Long(rs.getInt("presentacion_nro_transaccion"))); Nombre campo muy largo..
    			log.debug("<----------- NroTran="+tranAfip.getNroTran()+", NroTranPres="+tranAfip.getNroTranPres());
    			
    			
    			// read Cumur en campo = 3
    			if (tranAfip.getFormulario() == FormularioAfip.RS_6057.getId()) {
    				String cumur = StringUtil.formatLong(readCampoLong(con, tranAfip.getIdTransaccionAfip(), 3));
    				tranAfip.setCumur(cumur);
    			}
    			
    			// validar si existe algun registro para esta transaccion en la tabla FMLR_ERROR
    			if(tranAfip.getFechaAnulacion() != null) {
    				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_ANULADA));
    			} else {
    				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_PENDIENTE));    				
    			}
    			
    			// verificar errores
    			if (readErrorVerificar(con, tranAfip.getIdTransaccionAfip())) {
    				// Marca la transaccion con Error
    				tranAfip.setEstTranAfip(EstTranAfip.getById(EstTranAfip.ID_CON_ERROR));
    			}
    			
    			// obtengo los datos del cheque
    			String sql = String.format("select * from fmlr_cheque where transaccion_afip = %d", tranAfip.getIdTransaccionAfip());
    			Map<String, Object> m = JdbcUtil.queryRow(con, sql);  
    			if (!m.isEmpty()) {
    				tranAfip.setNroCheque((Integer)m.get("numero_cheque"));
    				tranAfip.setBancoCheque((Integer)m.get("banco_cheque"));
    				tranAfip.setSucursalCheque((Integer)m.get("sucursal_cheque"));
    				tranAfip.setCodPostalCheque((String)m.get("cod_postal"));
    				tranAfip.setCtaCteCheque((Integer)m.get("cta_cte"));
    				tranAfip.setFechaAcredCheque((Date)m.get("fecha_acreditacion"));
				}
    			
    			if(tranAfip.validateCreate()){
    				BalDAOFactory.getTranAfipDAO().update(tranAfip);
    				listTranAfip.add(tranAfip);
    			}else{
    				tranAfip.passErrorMessages(envioOsiris);
    				break;
    			}
    			
    		}
    		
    		if(envioOsiris.hasError())
    			break;
    		
    	}
    	
    	
    	
    	if(!envioOsiris.hasError()){
    		envioOsiris.setListTranAfip(listTranAfip);
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris; 	
    }
   
    
    /**
     * Verifica si existe algun registro de error en fmlr_error para este idTransaccionAfip
     * @param cn conexion jdbc
     * @param idTransaccionAfip
     * @return Si encuentra algun registro de error, retorna  True, indicando que afip informo error
     */
    private boolean readErrorVerificar(Connection cn, Long idTransaccionAfip) throws Exception {
    	String sql;
    	
    	sql = "select * from fmlr_error where transaccion_afip = " + idTransaccionAfip;
    	return !JdbcUtil.queryRow(cn, sql).isEmpty();
	}

    
    
	private Long readCampoLong(Connection cn, Long idTransaccionAfip, int campo) throws Exception {
		String sql;
		Long v;
		
		sql  = "select * from fmlr_campo where campo = " + campo + " and transaccion_afip = " + idTransaccionAfip;
		v = campoLong(JdbcUtil.queryRow(cn, sql));
		return v;
	}
	
	private Date readCampoDate(Connection cn, Long idTransaccionAfip, int campo) throws Exception {
		String sql;
		Date v;
		
		sql  = "select * from fmlr_campo where campo = " + campo + " and transaccion_afip = " + idTransaccionAfip;
		v = campoDate(JdbcUtil.queryRow(cn, sql));
		return v;
	}

    /**
     * Dado un row de la tabla fmlr_campo, obtiene el valor, 
     * leyenda de contenido_n o _c segun el tipo_contenido.
     * el valor encontrado es truncado a Long.
     * @return Este metodo retorna null si no se encuentran valores en contenido_* 
     */
	private Long campoLong(Map<String, Object> m) {
		String tipo = (String) m.get("tipo_contenido");
		Long ret;
		
		if (m == null || m.size() == 0)
			return null;
		
		if ("N".equalsIgnoreCase(tipo)) {
			Double v = (Double) m.get("contenido_n");
			if (v == null) return null;
			ret = v.longValue();
		} else {
			String v = (String) m.get("contenido_c");
			if (StringUtil.isNullOrEmpty(v)) return null;
			ret = Long.valueOf(v);
		}
		
		return ret;
	}
	
	
    /**
     * Dado un row de la tabla fmlr_campo, obtiene el valor, 
     * leyenda de contenido_n si el tipo_contenido es D.
     * el valor encontrado es Date.
     * @return Este metodo retorna null si no se encuentran valores en contenido_n 
     */
	private Date campoDate(Map<String, Object> m) {
		String tipo = (String) m.get("tipo_contenido");
		Date ret = null;
		
		if (m == null || m.size() == 0)
			return null;
		
		if ("D".equalsIgnoreCase(tipo)) {
			String v = String.valueOf(m.get("contenido_n"));
			if (v == null) return null;
			
			int anio = NumberUtil.getInt(v.substring(0, 4));
			int mes  = NumberUtil.getInt(v.substring(4, 6));
			int dia  = NumberUtil.getInt(v.substring(6, 8));
			
			ret = DateUtil.getDate(anio, mes, dia);
		}
	
		return ret;
	}

	public EnvioOsiris createListDetallePago(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
 	
    	Connection con = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	log.debug("lista de transacciones: "+envioOsiris.getListTranAfip().size());
    	int canPagos=0;
    	for(TranAfip tranAfip: envioOsiris.getListTranAfip()){
    		int formulario = tranAfip.getFormulario();

    		if (formulario == FormularioAfip.RS_6057.getId()) {
    			canPagos += detallePagoRs(con, tranAfip);
    	
    		} else if (formulario == FormularioAfip.DREI_SOLO_PAGO_6052.getId() || 
    				formulario == FormularioAfip.ETUR_SOLO_PAGO_6056.getId()) {
    			canPagos += detallePagoSolo(con, tranAfip, 4, 33);
    			
    		} else if (formulario == FormularioAfip.DREI_PRES_Y_PAGO_WEB_6058.getId() ||
    			formulario == FormularioAfip.ETUR_PRES_Y_PAGO_WEB_6059.getId()) {
    			canPagos += detallePagoWeb(con, tranAfip);
    		
    		} else if (formulario == FormularioAfip.DREI_MULTAS_6053.getId() ||
    				formulario == FormularioAfip.ETUR_MULTAS_6061.getId()) {
    			canPagos += detallePagoMulta(con, tranAfip);
    			
    		} else if (formulario == FormularioAfip.DREI_PRES_Y_PAGO_6050.getId() ||
    				formulario == FormularioAfip.ETUR_PRES_Y_PAGO_6054.getId()) {
				canPagos += detallePresYPago(con, tranAfip);
				
			} else if (formulario == FormularioAfip.DREI_SOLO_PAGO_6063_BETA.getId() || 
    				formulario == FormularioAfip.ETUR_SOLO_PAGO_6064_BETA.getId()){
				canPagos += detalleSoloPagoBeta(con, tranAfip);
			}
    		
    		if(tranAfip.hasError()){
    			tranAfip.passErrorMessages(envioOsiris);
    			break;
    		}
    	}

    	envioOsiris.setCantidadPagos(canPagos);

    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
 
	/**
	 * Procesa los detalle Pago Multa. (formularios 6053, 6061)
	 * @param cn
	 * @param tranAfip
	 * @return la cantida de pagos procesados
	 * @throws Exception
	 */
	private int detallePagoMulta(Connection cn, TranAfip tranAfip) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter (formularios 6053, 6061)");

		// obtenemos anio y periodo del primer registro de fmlr_obligacion
		String sql = String.format("select * from fmlr_obligacion where transaccion_Afip = %d", tranAfip.getIdTransaccionAfip());
		Map<String, Object> m = JdbcUtil.queryRow(cn, sql);    	

		DetallePago detallePago = new DetallePago();
		detallePago.setTranAfip(tranAfip);
		detallePago.setFechaPago(tranAfip.getFechaProceso());
		detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
		detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
		
		// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
		String periodoAnio = m.get("periodo_fiscal").toString();
		detallePago.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
		detallePago.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
		
		detallePago.setImpuesto((Integer) m.get("impuesto"));

		// obtenemos el importe pago de fmlr_pago
		sql = String.format("select * from fmlr_pago where transaccion_Afip = %d", tranAfip.getIdTransaccionAfip());
		m = JdbcUtil.queryRow(cn, sql);   
		detallePago.setImportePago((Double) m.get("importe")); //fmlr_pago

		Long codRefPag = readCampoLong(cn, tranAfip.getIdTransaccionAfip(), 5); 
		
		detallePago.setNumeroCuenta(StringUtil.formatLong(codRefPag));
		
		if (detallePago.validateCreate()) {
			BalDAOFactory.getDetallePagoDAO().update(detallePago);
			log.debug("Se crea el detalle de pago");
		} else {
			detallePago.passErrorMessages(tranAfip);
		}
		
		return 1; //un pago procesado
	}

	/**
	 * Procesa los detalle Drei Presentación y Pago.  (formulario 6050)
	 * @param cn
	 * @param tranAfip
	 * @return la cantida de pagos procesados
	 * @throws Exception
	 */
	private int detallePresYPago(Connection cn, TranAfip tranAfip) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter (formulario 6050,6054)");
				
		String sql = null;
		int canPagos = 0;
		if (tranAfip.getTotMontoIngresado() > 0) {
		
			sql = String.format("select contenido_c from fmlr_disquete where registro = 96 and transaccion_afip = " + tranAfip.getIdTransaccionAfip());
			List<Map<String, Object>> listcon = JdbcUtil.queryList(cn, sql);
			
			//puedo obtener impuesto capital (6050,6053) junto con interés (impuesto 6059,6054) 
			for (Map<String, Object> mcampo : listcon) {
				
				DetallePago detallePago = new DetallePago();
				detallePago.setTranAfip(tranAfip);
				detallePago.setFechaPago(tranAfip.getFechaProceso());
				detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
				detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
				
				Long numeroCuenta = 0L;
				Integer impuesto = 0;
				Double importePago = 0.0D;
				
				String cc = (String) mcampo.get("contenido_c");
				if (!StringUtil.isNullOrEmpty(cc)) {
					numeroCuenta = Long.valueOf(cc.substring(0, 9));
					impuesto = Integer.valueOf(cc.substring(9,13));
					importePago = Double.valueOf(cc.substring(13, 26) + "." + cc.substring(26, 28));
				} 
				
				fillCuenta(detallePago, findIdRecurso(tranAfip.getCodRecursoSegunFormulario()), numeroCuenta);
				
				detallePago.setImportePago(importePago);
				detallePago.setImpuesto(impuesto);
				
				sql = String.format("select * from fmlr_obligacion where transaccion_Afip = %d", tranAfip.getIdTransaccionAfip());
				Map<String, Object> m = JdbcUtil.queryRow(cn, sql);  
				
				// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
				String periodoAnio = m.get("periodo_fiscal").toString();
				detallePago.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePago.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));
				
				if (detallePago.validateCreate()) {
					BalDAOFactory.getDetallePagoDAO().update(detallePago);
					log.debug("Se crea el detalle de pago");
				} else {
					detallePago.passErrorMessages(tranAfip);
					break;
				}
				
				canPagos++;
			}
					
		}
		
		return canPagos;
	}
	
	/**
	 * Dado un nc, (Numero de cuenta) completa los datos de cuenta del detalle de pago.
	 * Si nc o recurso es null, pone un 0 en numerocuenta y null en idCuenta
	 * @param dp detalle de pago
	 * @param recurso objeto del recurso del nc. Puede ser null.
	 * @param nc numero de cuenta
	 */
	private void fillCuenta(DetallePago dp, Long idRecurso, Long nc) throws Exception {
		log.debug("fillCuenta: enter");
		//numero de cuenta
		if (nc == null || nc == 0L || idRecurso == null) {
			dp.setNumeroCuenta("0");
			dp.setCuenta(null);
			
		} else {
			String numeroCuenta = StringUtil.formatLong(nc);
			dp.setNumeroCuenta(numeroCuenta);
			// Durante las pruebas: surgio que hibernate en envios largos, causaba problemas de performance
			// al llamar al metodo: Cuenta.getByIdRecursoYNumeroCuenta(), por eso lo reemplazamos por estas dos llamadas.
			Long idCuenta = Cuenta.getIdCuenta(idRecurso, numeroCuenta);
			Cuenta cuenta = idCuenta == null ? null : Cuenta.getById(idCuenta);
			dp.setCuenta(cuenta);
		}		
		log.debug("fillCuenta: exit");
	}
	
	/**
	 * Procesa los detalle Pago Web. (formularios 6058, 6059)
	 * @param cn
	 * @param tranAfip
	 * @return la cantida de pagos procesados
	 * @throws Exception
	 */
	private int detallePagoWeb(Connection cn, TranAfip tranAfip) throws Exception {
    	String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter (formularios 6058, 6059)");
		
		String sql = "select * from fmlr_obligacion where transaccion_afip = " + tranAfip.getIdTransaccionAfip();
		List<Map<String, Object>> listOblig = JdbcUtil.queryList(cn,sql);
		
    	Integer canPagos = 0;
		Long numeroCuenta = readCampoLong(cn, tranAfip.getIdTransaccionAfip(), 2);
		
		//utilizo esta lista para setear impuesto e importePago
		List<DetallePagoVO> listDetallePagoVO = new ArrayList<DetallePagoVO>();
		
		//se trata de una cuenta contributiva
		if (null == numeroCuenta || StringUtil.formatLong(numeroCuenta).length() > 6) {
			for(Map<String, Object> moblig : listOblig) {
				DetallePagoVO detallePagoVO = new DetallePagoVO();
				
				// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
				String periodoAnio = moblig.get("periodo_fiscal").toString();
				detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
				
				detallePagoVO.setImportePago((Double) moblig.get("monto_ingresado"));
				detallePagoVO.setImpuesto((Integer) moblig.get("impuesto"));
				
				detallePagoVO.setNumeroCuenta(StringUtil.formatLong(numeroCuenta));
				listDetallePagoVO.add(detallePagoVO);
			}
		}else{
			//Obtengo el primer elemento de la lista de campos
			Map<String, Object> moblig = listOblig.get(0);
			
			// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
			String periodoAnio = moblig.get("periodo_fiscal").toString();
			
			Integer formulario; 
			//Solo pago ETuR
			if (tranAfip.getFormulario().equals(FormularioAfip.ETUR_PRES_Y_PAGO_WEB_6059.getId())) {
				formulario = FormularioAfip.ETUR_PRES_Y_PAGO_6054.getId();
			} else {
				//Solo pago DReI
				formulario = FormularioAfip.DREI_PRES_Y_PAGO_6050.getId();
			}
			
			//casos de nros verificadores de DJs relacionadas
			String querySql = "select t.transaccion_afip from transaccion t, fmlr_disquete d";
				   querySql+= " where t.transaccion_afip = d.transaccion_afip and t.formulario ="+ formulario;
				   querySql+= " and t.cuit = " + tranAfip.getCuit();
				   querySql+= " and d.registro = 1 and d.c14n = " + numeroCuenta;
				   
			//busco la DJ relacionada (DReI = 6050, Etur = 6054)
			List<Map<String, Object>>  camposDJ = JdbcUtil.queryList(cn, querySql);
			
			//si recupera solo un registro
			if (camposDJ.size() == 1) {
				//obtengo el primer elemento para determinar la transaccion_afip
				Map<String, Object> cpo = camposDJ.get(0);
				Integer transaccionAfip = (Integer) cpo.get("transaccion_afip"); 
				 
				//busco las cuentas e importes relacionados a la transaccion
				List<Map<String, Object>> camposCeI = JdbcUtil.queryList(cn, "select contenido_c from fmlr_disquete where registro = 96 and transaccion_afip = " + transaccionAfip);
				
				for (Map<String, Object> mcampo : camposCeI) {
					DetallePagoVO detallePagoVO = new DetallePagoVO();
					
					detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
					detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
					
					String cc = (String) mcampo.get("contenido_c");
					if (!StringUtil.isNullOrEmpty(cc)) {
						detallePagoVO.setNumeroCuenta(cc.substring(0, 9));
						detallePagoVO.setImportePago(Double.valueOf(cc.substring(13, 26) + "." + cc.substring(26, 28)));
						detallePagoVO.setImpuesto(Integer.valueOf(cc.substring(9,13)));
					} 
					
					listDetallePagoVO.add(detallePagoVO);
				}
			} else {
			    //si recuperó mas de 1 o ningun registro, el caso debe ir a indeterminados
				DetallePagoVO detallePagoVO = new DetallePagoVO();
				
				detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));
				detallePagoVO.setImportePago(tranAfip.getTotMontoIngresado());
				detallePagoVO.setImpuesto((Integer) moblig.get("impuesto"));
				
				// en este caso numeroCuenta es un nro verificador
				detallePagoVO.setNumeroCuenta(StringUtil.formatLong(numeroCuenta));
			
				listDetallePagoVO.add(detallePagoVO);
			}
		}
    	
    	//Determino el Recurso asociado a la transaccion
    	Long idRecurso = findIdRecurso(tranAfip.getCodRecursoSegunFormulario());
		for (DetallePagoVO detallePagoVO : listDetallePagoVO) {
			
			DetallePago detallePago = new DetallePago();
			detallePago.setTranAfip(tranAfip);
			detallePago.setFechaPago(tranAfip.getFechaProceso());
			detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
			detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
			
			detallePago.setAnio(detallePagoVO.getAnio());
			detallePago.setPeriodo(detallePagoVO.getPeriodo());						

			if (!StringUtil.isNullOrEmpty(detallePagoVO.getNumeroCuenta())) {
				numeroCuenta = Long.valueOf(detallePagoVO.getNumeroCuenta()); 
			}
			
			// pasamos numerocuenta, importes y impuesto al detallepago
			fillCuenta(detallePago,idRecurso, numeroCuenta);
			detallePago.setImportePago(detallePagoVO.getImportePago());
			detallePago.setImpuesto(detallePagoVO.getImpuesto());
			
			if (detallePago.validateCreate()) {
				BalDAOFactory.getDetallePagoDAO().update(detallePago);
				log.debug("Se crea el detalle de pago");
			} else {
				detallePago.passErrorMessages(tranAfip);
				break;
			}
			canPagos++;
		}

		return canPagos;
	}

	/**
	 * Procesa los detalle Solo Pago. (formularios 6052, 6056)
	 * @param cn
	 * @param tranAfip
	 * @return la cantida de pagos procesados
	 * @throws Exception
	 */
	private int detallePagoSolo(Connection cn, TranAfip tranAfip, int campoDesde, int campoHasta) throws Exception {
    	String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter (formularios 6052, 6056)");

		int canPagos = 0;
		if (tranAfip.getVep() == null || Integer.parseInt(tranAfip.getVep()) == 0) {
			String sql = "select * from fmlr_obligacion where transaccion_Afip = " + tranAfip.getIdTransaccionAfip();
			
			//cuando Vep: es 0, no se usan nada de los campos de fmlr_campo
			canPagos = createPagoSoloVEP0(cn, tranAfip, JdbcUtil.queryList(cn,sql));
		} else {
			String sql = String.format("select * from fmlr_campo where transaccion_Afip = %s and campo >= %s and campo <= %s ", 
					tranAfip.getIdTransaccionAfip(), campoDesde, campoHasta);
			List<Map<String, Object>> campos = JdbcUtil.queryList(cn, sql);
			for(Map<String, Object> mcampo : campos) {
				DetallePago detallePago = new DetallePago();
				detallePago.setTranAfip(tranAfip);
				detallePago.setFechaPago(tranAfip.getFechaProceso());
				detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
				detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
				
				// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
				Map<String, Object> moblig = JdbcUtil.queryRow(cn, "select * from fmlr_obligacion where transaccion_Afip = " + tranAfip.getIdTransaccionAfip());
				String periodoAnio = moblig.get("periodo_fiscal").toString();
				detallePago.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePago.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));						
							
				// detetrmina numerocuenta, impuesto e importePago
				Long numeroCuenta = 0L;
				Integer impuesto = 0;
				Double importePago = 0.0D;
				String cc = (String) mcampo.get("contenido_c");
				if (!StringUtil.isNullOrEmpty(cc)) {
					numeroCuenta = Long.valueOf(cc.substring(0, 9));
					impuesto = Integer.valueOf(cc.substring(9,13));
					importePago = Double.valueOf(cc.substring(13, 26) + "." + cc.substring(26, 28));
				} 

				// pasamos numerocuenta, importes y impuesto al detallepago
				fillCuenta(detallePago, findIdRecurso(tranAfip.getCodRecursoSegunFormulario()), numeroCuenta);
				detallePago.setImportePago(importePago);
				detallePago.setImpuesto(impuesto);
				
				if (detallePago.validateCreate()) {
					BalDAOFactory.getDetallePagoDAO().update(detallePago);
					log.debug("Se crea el detalle de pago");
				} else {
					detallePago.passErrorMessages(tranAfip);
					break;
				}
				canPagos++;
			}
		}
		
		return canPagos;
	}

	/**
	 * Procesa los detalle Pago de Regimen simplificado. (formularios 6057)
	 * @param cn
	 * @param tranAfip
	 * @return la cantida de pagos procesados
	 * @throws Exception
	 */
    private int detallePagoRs(Connection cn, TranAfip tranAfip) throws Exception {
    	String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter (formularios 6057)");
    	
    	String sql = "select * from fmlr_obligacion where transaccion_Afip = "+ tranAfip.getIdTransaccionAfip();
		List<Map<String, Object>> r = JdbcUtil.queryList(cn, sql);

		int canPagos = 0;
		for(Map<String, Object> moblig : r) {
			DetallePago detallePago = new DetallePago();
			detallePago.setTranAfip(tranAfip);
			detallePago.setFechaPago(tranAfip.getFechaProceso());
			detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
			String periodoAnio = moblig.get("periodo_fiscal").toString();
			detallePago.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
			detallePago.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));			
			detallePago.setImportePago((Double) moblig.get("monto_ingresado"));
			detallePago.setImpuesto((Integer) moblig.get("impuesto"));
			detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
			
			// campo 4 = numerocuenta
			Long nc = readCampoLong(cn, tranAfip.getIdTransaccionAfip(), 4);
			
			/*- Mantis #5869 (nota:0015646) 
			 *  La busqueda del recurso debe hacerse por impuesto, ya que 
			 *  el formulario es el mismo (6057) para DReI y ETuR.
			 */
			if (ImpuestoAfip.RS_DREI.getId().equals(detallePago.getImpuesto())) {
				// Recurso DReI - Impuesto 6056
				fillCuenta(detallePago, findIdRecurso(Recurso.COD_RECURSO_DReI), nc);
			}else {
				// Recurso ETuR - Impuesto 6057
				fillCuenta(detallePago, findIdRecurso(Recurso.COD_RECURSO_ETuR), nc);
			}

			if (detallePago.validateCreate()) {
				BalDAOFactory.getDetallePagoDAO().update(detallePago);
			} else {
				detallePago.passErrorMessages(tranAfip);
				break;
			}
			canPagos++;
		}
		
    	return canPagos;
    }
    
    
    /**
	 * Procesa los detalles Solo Pago BETA. (formularios 6063, 6064)
	 * @param cn
	 * @param tranAfip
	 * @return la cantida de pagos procesados
	 * @throws Exception
	 */
	private int detalleSoloPagoBeta(Connection cn, TranAfip tranAfip) throws Exception {
    	String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter (formularios 6063, 6064)");
		
		String sql = "select * from fmlr_obligacion where transaccion_afip = " + tranAfip.getIdTransaccionAfip();
		List<Map<String, Object>> listOblig = JdbcUtil.queryList(cn,sql);
		
    	Integer canPagos = 0;
		Long numeroCuenta = readCampoLong(cn, tranAfip.getIdTransaccionAfip(), 3);
		
		//utilizo esta lista para setear impuesto e importePago
		List<DetallePagoVO> listDetallePagoVO = new ArrayList<DetallePagoVO>();
		
		//se trata de una cuenta contributiva
		if (null == numeroCuenta || StringUtil.formatLong(numeroCuenta).length() > 6) {
			for(Map<String, Object> moblig : listOblig) {
				DetallePagoVO detallePagoVO = new DetallePagoVO();
				
				// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
				String periodoAnio = moblig.get("periodo_fiscal").toString();
				detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
				
				detallePagoVO.setImportePago((Double) moblig.get("monto_ingresado"));
				detallePagoVO.setImpuesto((Integer) moblig.get("impuesto"));
				
				detallePagoVO.setNumeroCuenta(StringUtil.formatLong(numeroCuenta));
				listDetallePagoVO.add(detallePagoVO);
			}
		}else{
			//Obtengo el primer elemento de la lista de campos
			Map<String, Object> moblig = listOblig.get(0);
			
			// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
			String periodoAnio = moblig.get("periodo_fiscal").toString();
			
			Integer formulario; 
			//Solo pago ETuR
			if (tranAfip.getFormulario().equals(FormularioAfip.ETUR_SOLO_PAGO_6064_BETA.getId())) {
				formulario = FormularioAfip.ETUR_PRES_Y_PAGO_6054.getId();
			} else {
				//Solo pago DReI
				formulario = FormularioAfip.DREI_PRES_Y_PAGO_6050.getId();
			}
			
			//casos de nros verificadores de DJs relacionadas
			String querySql = "select t.transaccion_afip from transaccion t, fmlr_disquete d";
				   querySql+= " where t.transaccion_afip = d.transaccion_afip and t.formulario ="+ formulario;
				   querySql+= " and t.cuit = " + tranAfip.getCuit();
				   querySql+= " and d.registro = 1 and d.c14n = " + numeroCuenta;
				   
			//busco la DJ relacionada (DReI = 6050, Etur = 6054)
			List<Map<String, Object>>  camposDJ = JdbcUtil.queryList(cn, querySql);
			
			//si recupera solo un registro
			if (camposDJ.size() == 1) {
				//obtengo el primer elemento para determinar la transaccion_afip
				Map<String, Object> cpo = camposDJ.get(0);
				Integer transaccionAfip = (Integer) cpo.get("transaccion_afip"); 
				 
				//busco las cuentas e importes relacionados a la transaccion
				List<Map<String, Object>> camposCeI = JdbcUtil.queryList(cn, "select contenido_c from fmlr_disquete where registro = 96 and transaccion_afip = " + transaccionAfip);
				
				for (Map<String, Object> mcampo : camposCeI) {
					DetallePagoVO detallePagoVO = new DetallePagoVO();
					
					detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
					detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
					
					String cc = (String) mcampo.get("contenido_c");
					if (!StringUtil.isNullOrEmpty(cc)) {
						detallePagoVO.setNumeroCuenta(cc.substring(0, 9));
						detallePagoVO.setImportePago(Double.valueOf(cc.substring(13, 26) + "." + cc.substring(26, 28)));
						detallePagoVO.setImpuesto(Integer.valueOf(cc.substring(9,13)));
					} 
					
					listDetallePagoVO.add(detallePagoVO);
				}
			} else {
			    //si recuperó mas de 1 o ningun registro, el caso debe ir a indeterminados
				DetallePagoVO detallePagoVO = new DetallePagoVO();
				
				detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));
				detallePagoVO.setImportePago(tranAfip.getTotMontoIngresado());
				detallePagoVO.setImpuesto((Integer) moblig.get("impuesto"));
				
				// en este caso numeroCuenta es un nro verificador
				detallePagoVO.setNumeroCuenta(StringUtil.formatLong(numeroCuenta));
			
				listDetallePagoVO.add(detallePagoVO);
			}
		}
    	
    	//Determino el Recurso asociado a la transaccion
    	Long idRecurso = findIdRecurso(tranAfip.getCodRecursoSegunFormulario());
		for (DetallePagoVO detallePagoVO : listDetallePagoVO) {
			
			DetallePago detallePago = new DetallePago();
			detallePago.setTranAfip(tranAfip);
			detallePago.setFechaPago(tranAfip.getFechaProceso());
			detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
			detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
			
			detallePago.setAnio(detallePagoVO.getAnio());
			detallePago.setPeriodo(detallePagoVO.getPeriodo());						

			if (!StringUtil.isNullOrEmpty(detallePagoVO.getNumeroCuenta())) {
				numeroCuenta = Long.valueOf(detallePagoVO.getNumeroCuenta()); 
			}
			
			// pasamos numerocuenta, importes y impuesto al detallepago
			fillCuenta(detallePago,idRecurso, numeroCuenta);
			detallePago.setImportePago(detallePagoVO.getImportePago());
			detallePago.setImpuesto(detallePagoVO.getImpuesto());
			
			if (detallePago.validateCreate()) {
				BalDAOFactory.getDetallePagoDAO().update(detallePago);
				log.debug("Se crea el detalle de pago");
			} else {
				detallePago.passErrorMessages(tranAfip);
				break;
			}
			canPagos++;
		}

		return canPagos;
	}


	public EnvioOsiris createListDetalleDJ(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	log.info(funcName + ": enter");
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);
    	log.info(funcName + ": conexion a: " + DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	log.info(funcName + ": por pedir lista tranafip");
    	log.info(funcName + ": lista de transacciones: " + envioOsiris.getListTranAfip().size());
    	log.info(funcName + ": listo!");

    	int canDetDJ=0;
    	for(TranAfip tranAfip: envioOsiris.getListTranAfip()){
    		
    		String sqlTran = "select * from fmlr_disquete where transaccion_Afip = "+ tranAfip.getIdTransaccionAfip();
    		pstmt = con.prepareStatement(sqlTran);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			//log.debug("detalle de dj nro: "+(canDetDJ+1));
    			DetalleDJ detalleDJ = new DetalleDJ();
    			detalleDJ.setTranAfip(tranAfip);
    			detalleDJ.setFechaProceso(tranAfip.getFechaProceso());
    			
    			detalleDJ.setRegistro(rs.getInt("registro"));    			
    			detalleDJ.setFila(rs.getInt("fila"));
    			
    			String contenido = rs.getString("contenido_c");
    			if (!StringUtil.isNullOrEmpty(contenido)){
    				contenido = contenido.replace("|", " ");
				}
    			detalleDJ.setContenido(contenido);
    			
    			
    			detalleDJ.setC01n(rs.getDouble("c01n"));
    			detalleDJ.setC02n(rs.getDouble("c02n"));
    			detalleDJ.setC03n(rs.getDouble("c03n"));
    			detalleDJ.setC04n(rs.getDouble("c04n"));
    			detalleDJ.setC05n(rs.getDouble("c05n"));
    			detalleDJ.setC06n(rs.getDouble("c06n"));
    			detalleDJ.setC07n(rs.getDouble("c07n"));
    			detalleDJ.setC08n(rs.getDouble("c08n"));
    			detalleDJ.setC09n(rs.getDouble("c09n"));
    			detalleDJ.setC10n(rs.getDouble("c10n"));
    			detalleDJ.setC11n(rs.getDouble("c11n"));
    			detalleDJ.setC12n(rs.getDouble("c12n"));
    			detalleDJ.setC13n(rs.getDouble("c13n"));
    			detalleDJ.setC14n(rs.getDouble("c14n"));
    			detalleDJ.setC15n(rs.getDouble("c15n"));
    			detalleDJ.setC16n(rs.getDouble("c16n"));
    			detalleDJ.setC17n(rs.getDouble("c17n"));
    			detalleDJ.setC18n(rs.getDouble("c18n"));
    			detalleDJ.setC19n(rs.getDouble("c19n"));
    			detalleDJ.setC20n(rs.getDouble("c20n"));
    			detalleDJ.setC21n(rs.getDouble("c21n"));
    			detalleDJ.setC22n(rs.getDouble("c22n"));
    			detalleDJ.setC23n(rs.getDouble("c23n"));
    			detalleDJ.setC24n(rs.getDouble("c24n"));
    			detalleDJ.setC25n(rs.getDouble("c25n"));
    			detalleDJ.setC26n(rs.getDouble("c26n"));
    			detalleDJ.setC27n(rs.getDouble("c27n"));
    			detalleDJ.setC28n(rs.getDouble("c28n"));
    			detalleDJ.setC29n(rs.getDouble("c29n"));
    			detalleDJ.setC30n(rs.getDouble("c30n"));
    			
    			detalleDJ.setEstDetDJ(EstDetDJ.getById(EstDetDJ.ID_PENDIENTE));
    			
    			/*log.debug("==================> c01: "+detalleDJ.getC01n()
    					+" c02: "+detalleDJ.getC02n()
    					+" c03: "+detalleDJ.getC03n()
    					+" c04: "+detalleDJ.getC04n()
    					+" c05: "+detalleDJ.getC05n()
    					+" c06: "+detalleDJ.getC06n()
    					+" c07: "+detalleDJ.getC07n()
    					+" c08: "+detalleDJ.getC08n()
    					+" c09: "+detalleDJ.getC09n()
    					+" c10: "+detalleDJ.getC10n()
    					+" c11: "+detalleDJ.getC11n()
    					+" c12: "+detalleDJ.getC12n()
    					+" c13: "+detalleDJ.getC13n()
    					+" c14: "+detalleDJ.getC14n()
    					+" c15: "+detalleDJ.getC15n()
    					+" c16: "+detalleDJ.getC16n()
    					+" c17: "+detalleDJ.getC17n()
    					+" c18: "+detalleDJ.getC18n()
    					+" c19: "+detalleDJ.getC19n()
    					+" c20: "+detalleDJ.getC20n()
    					+" c21: "+detalleDJ.getC21n()
    					+" c22: "+detalleDJ.getC22n()
    					+" c23: "+detalleDJ.getC23n()
    					+" c24: "+detalleDJ.getC24n()
    					+" c25: "+detalleDJ.getC25n()
    					+" c26: "+detalleDJ.getC26n()
    					+" c27: "+detalleDJ.getC27n()
    					+" c28: "+detalleDJ.getC28n()
    					+" c29: "+detalleDJ.getC29n()
    					+" c30: "+detalleDJ.getC30n()
    					+" <=====================");*/
    			if(detalleDJ.validateCreate()){
    				BalDAOFactory.getDetalleDJDAO().update(detalleDJ);
    				log.debug("Se crea el detalle de DJ");
    			}else{
    				detalleDJ.passErrorMessages(envioOsiris);
    				break;
    			}
    			canDetDJ++;
    		}
    		
    		if(envioOsiris.hasError()){
    			break;
    		}
    	}
    	
    	envioOsiris.setCanDecJur(canDetDJ);
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    
    /**
     *  Obtiene la lista de registros de Conciliacion incluidos en el Envio
     * 
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public EnvioOsiris createListConciliacion(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	String sSql = "select envio_id as envioId, banco as banco, cierre_banco as cierreBanco, fecha_conciliacion as fechaConciliacion, cant_nota as cantNota, total_conciliado as totalConciliado  from envio_conciliacion";
    	
    	sSql+=" where envio_id = "+envioOsiris.getIdEnvioAfip();  
    		
    	if (log.isDebugEnabled()) log.debug(funcName + " - Query: " + sSql);

    	// conecta con la base de mulator via jdbc
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);

    	pstmt = con.prepareStatement(sSql);
    	
    	rs = pstmt.executeQuery();
    	
    	List<Conciliacion> listConciliacion=new ArrayList<Conciliacion>();
    	
    	log.debug("RS: empieza");
    	
    	//recorro la lista de resultados devuelta
    	while (rs.next()){
    		Conciliacion conciliacion = new Conciliacion();
    		conciliacion.setEnvioOsiris(envioOsiris);
    		conciliacion.setIdEnvioAfip(rs.getLong("envioId"));
    		conciliacion.setBanco(rs.getLong("banco"));
    		conciliacion.setNroCierreBanco(rs.getLong("cierreBanco"));
    		conciliacion.setFechaConciliacion(rs.getDate("fechaConciliacion"));
    		conciliacion.setCantNota(rs.getLong("cantNota"));
    		conciliacion.setTotalConciliado(rs.getDouble("totalConciliado"));
    		
    		if(conciliacion.validateCreate()){
    			BalDAOFactory.getConciliacionDAO().update(conciliacion);
    			listConciliacion.add(conciliacion);
    		}else{
    			conciliacion.passErrorMessages(envioOsiris);
    			break;
    		}
    	}
    	
    	if(!envioOsiris.hasError()){
    		envioOsiris.setListConciliacion(listConciliacion);
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    	
    	
    }
    
    /**
     *  Obtiene las Notas de Obligacion asociadas a los registros de Envio_Conciliacion
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public EnvioOsiris createListNotaObl(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	log.debug("lista de cierreBanco: "+envioOsiris.getListCierreBanco().size());
    	for(CierreBanco cierreBanco: envioOsiris.getListCierreBanco()){
    		
    		String queryImto ="select t.banco, t.cierre_banco, n.comision_recaud, n.comision_x_tran, n.comision_caja, n.iva, n.retencion_iva "; 
    			   queryImto+="	from nota_obligacion n,  transaccion t ";
    		       queryImto+="	where t.transaccion_afip = n.transaccion_afip";
        		   queryImto+=" and n.banco="+cierreBanco.getBanco()+" and n.cierre_banco="+cierreBanco.getNroCierreBanco();
    		
			//busco importes relacionados al banco y cierre banco
			List<Map<String, Object>> camposImto = JdbcUtil.queryList(con, queryImto);
			
			//Mapa con los importes sumarizados por banco y cierre banco
			Map<String,Double> mapImpto = new HashMap<String, Double>();
			
			for (Map<String, Object> mcampo : camposImto) {

				//obtengo banco y cierreBanco
				String strBco = mcampo.get("banco").toString();
				String strCierreBco = mcampo.get("cierre_banco").toString();
				
				Double comRec =	(Double) mcampo.get("comision_recaud");
				if(null==comRec) comRec=0D;
				
				Double comTran = (Double) mcampo.get("comision_x_tran");
				if(null==comTran) comTran=0D;
				
				Double comCaj = (Double) mcampo.get("comision_caja");
				if(null==comCaj) comCaj=0D;
				
				Double iva = (Double) mcampo.get("iva");
				if(null==iva) iva=0D;
				
				Double retIva = (Double) mcampo.get("retencion_iva");
				if(null == retIva) retIva =0D;
				
				//sum(comision_recaud + comision_x_tran + comision_caja + iva - retencion_iva)
				Double sumValue = comRec + comTran + comCaj + iva - retIva;
				
				//Key para buscar en el map: "banco-cierreBanco"
				String key = strBco+"-"+strCierreBco;
				
				//determino el valor en el map para esa la clave banco-cierreBanco
				Double valueMap = mapImpto.get(key);
				if(null == valueMap){
					//no existe ese banco-cierreBanco en el map, lo debo crear
					mapImpto.put(key, sumValue);
				}else{
					//sumo los valores para ese banco y cierreBanco
					mapImpto.put(key, valueMap + sumValue);
				}
				
			}
    		
    		String subQuery = "select distinct transaccion_afip from nota_obligacion"; 
    			   subQuery+= " where banco= "+cierreBanco.getBanco()+" and cierre_banco = "+cierreBanco.getNroCierreBanco();

    		String sqlQuery = "select banco, cierre_banco, sum(total_monto_ingresado) as total"; 
    			   sqlQuery+= " from transaccion  where transaccion_afip in ("+subQuery+") group by 1,2";
    			   
    		pstmt = con.prepareStatement(sqlQuery);
    		rs = pstmt.executeQuery();
    		
     		while(rs.next()){
    			
    			EnvNotObl envNotObl = new EnvNotObl();
    			envNotObl.setEnvioOsiris(envioOsiris);
    			envNotObl.setBancoOriginal(cierreBanco.getBanco()); //Banco Original
    			envNotObl.setNroCieBanOrig(cierreBanco.getNroCierreBanco()); //CiereBanco Original
    			envNotObl.setFechaRegistro(envioOsiris.getFechaRegistroMulat());
    			envNotObl.setBanco(rs.getInt("banco"));
    			envNotObl.setNroCierreBanco(rs.getInt("cierre_banco"));
    			envNotObl.setEstado(Estado.ACTIVO.getId());
    			envNotObl.setTotalCredito(rs.getDouble("total"));
    			
    			//determino el total acreditado buscando en el mapa el valor por key "banco-cierreBanco"
    			Double valRestar = mapImpto.get(rs.getString("banco")+"-"+rs.getString("cierre_banco"));
    			if(null == valRestar) valRestar = 0D;
    			envNotObl.setTotalAcreditado(rs.getDouble("total") - valRestar);
    			
    			if(envNotObl.validateCreate()){
    				BalDAOFactory.getEnvNovOblDAO().update(envNotObl);
    			}else{
    				envNotObl.passErrorMessages(envioOsiris);
    				break;
    			}
    		}
    		
    		if(envioOsiris.hasError()){
    			break;
    		}
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    
    /**
     *  Obtiene las Notas Conciliadas asociadas a los registros de Envio_Conciliacion
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public EnvioOsiris createListNotaConc(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	log.debug("lista de conciliacion: "+envioOsiris.getListConciliacion().size());
    	for(Conciliacion conciliacion: envioOsiris.getListConciliacion()){
    		
    		
    		String sqlTran = "select * from conciliacion_nota where banco = "+ conciliacion.getBanco()+ " and cierre_banco =  "+conciliacion.getNroCierreBanco();
    		pstmt = con.prepareStatement(sqlTran);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			NotaConc notaConc = new NotaConc();
    			notaConc.setConciliacion(conciliacion);
    			notaConc.setFechaConciliacion(rs.getDate("fecha_conciliacion"));
    			notaConc.setNroCuenta(rs.getString("cuenta"));
    			notaConc.setImpuesto(rs.getLong("impuesto"));
    			notaConc.setMoneda(rs.getInt("moneda"));
    			notaConc.setTipoNota(rs.getInt("tipo_nota"));
    			notaConc.setImporte(rs.getDouble("importe_nota"));
    			notaConc.setFechaAcredit(rs.getDate("fecha_acreditacion"));
    			notaConc.setEstado(Estado.ACTIVO.getId());
    			
    			if(notaConc.validateCreate()){
    				BalDAOFactory.getNotaConcDAO().update(notaConc);
    			}else{
    				notaConc.passErrorMessages(envioOsiris);
    				break;
    			}
    		}
    		
    		if(envioOsiris.hasError()){
    			break;
    		}
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    
    
    /**
     *  Obtiene las Notas de Abono asociadas a los registros de Cierre_Banco
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public EnvioOsiris createListNotaImpto(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	log.debug("lista de cierreBanco: "+envioOsiris.getListCierreBanco().size());
    	for(CierreBanco cierreBanco: envioOsiris.getListCierreBanco()){
    		
    		
    		String sqlTran = "select * from nota_impto where banco = "+ cierreBanco.getBanco()+ " and cierre_banco =  "+cierreBanco.getNroCierreBanco();
    		pstmt = con.prepareStatement(sqlTran);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			NotaImpto notaImpto = new NotaImpto();
    			notaImpto.setCierreBanco(cierreBanco);
    			notaImpto.setTipoNota(rs.getInt("tipo_nota"));
    			notaImpto.setImpuesto(rs.getLong("impuesto"));
    			notaImpto.setMoneda(rs.getInt("moneda"));
    			notaImpto.setNroCuenta(rs.getString("cuenta"));
    			notaImpto.setImporte(rs.getDouble("importe"));
    			notaImpto.setImporteIVA(rs.getDouble("importe_iva"));
    			notaImpto.setEstado(Estado.ACTIVO.getId());
    			
    			if(notaImpto.validateCreate()){
    				BalDAOFactory.getNotaImptoDAO().update(notaImpto);
    			}else{
    				notaImpto.passErrorMessages(envioOsiris);
    				break;
    			}
    		}
    		
    		if(envioOsiris.hasError()){
    			break;
    		}
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    
    /**
     *  Obtiene las Notas de Abono asociadas a los registros de Cierre_Banco
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public EnvioOsiris createListNovedad(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	log.debug("lista de cierreBanco: "+envioOsiris.getListCierreBanco().size());
    	for(CierreBanco cierreBanco: envioOsiris.getListCierreBanco()){
    		
    		
    		String sqlTran = "select * from novedad where banco = "+ cierreBanco.getBanco()+ " and cierre_banco =  "+cierreBanco.getNroCierreBanco();
    		pstmt = con.prepareStatement(sqlTran);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			NovedadEnvio novedadEnvio = new NovedadEnvio();
    			novedadEnvio.setCierreBanco(cierreBanco);
    			novedadEnvio.setIdTransaccionAfip(rs.getLong("transaccion_afip"));
    			novedadEnvio.setFormaPago(rs.getInt("forma_pago"));
    			novedadEnvio.setSucursal(rs.getLong("sucursal"));
    			novedadEnvio.setTipoSucursal(rs.getInt("tipo_sucursal"));
    			novedadEnvio.setTipoOperacion(rs.getInt("tipo_operacion"));
    			int aceptada = 0;
    			if("S".equals(rs.getString("aceptada").toUpperCase()))
    				aceptada = 1;
    			novedadEnvio.setAceptada(aceptada);
    			int deOficio = 0;
    			if("S".equals(rs.getString("de_oficio").toUpperCase()))
    				deOficio = 1;
    			novedadEnvio.setDeOficio(deOficio);
    			novedadEnvio.setFechaNovedad(rs.getDate("fecha_novedad"));
    			novedadEnvio.setFechaRegistro(rs.getDate("fecha_registro"));
    			novedadEnvio.setEstado(Estado.ACTIVO.getId());
    			
    			if(novedadEnvio.validateCreate()){
    				BalDAOFactory.getNovedadEnvioDAO().update(novedadEnvio);
    			}else{
    				novedadEnvio.passErrorMessages(envioOsiris);
    				break;
    			}
    		}
    		
    		if(envioOsiris.hasError()){
    			break;
    		}
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    
    /**
     *  Obtiene los Cierre de Sucursal asociadas a los registros de Cierre_Banco
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public EnvioOsiris createListCierreSucursal(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);

    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	log.debug("lista de cierreBanco: "+envioOsiris.getListCierreBanco().size());
    	for(CierreBanco cierreBanco: envioOsiris.getListCierreBanco()){
    		
    		
    		String sqlTran = "select * from cierre_sucursal where banco = "+ cierreBanco.getBanco()+ " and cierre_banco =  "+cierreBanco.getNroCierreBanco();
    		pstmt = con.prepareStatement(sqlTran);
    		rs = pstmt.executeQuery();
    		
    		while(rs.next()){
    			CierreSucursal cierreSucursal = new CierreSucursal();
    			cierreSucursal.setCierreBanco(cierreBanco);
    			
    			cierreSucursal.setNroCieSuc(rs.getLong("cierre_sucursal"));
    			cierreSucursal.setSucursal(rs.getLong("sucursal"));
    			cierreSucursal.setTipoSucursal(rs.getInt("tipo_sucursal"));
    			cierreSucursal.setFechaRegistro(rs.getDate("fecha_registro"));
    			cierreSucursal.setFechaCierre(rs.getDate("fecha_cierre"));
    			cierreSucursal.setEstado(Estado.ACTIVO.getId());
    			
    			if(cierreSucursal.validateCreate()){
    				BalDAOFactory.getCierreSucursalDAO().update(cierreSucursal);
    			}else{
    				cierreSucursal.passErrorMessages(envioOsiris);
    				break;
    			}
    		}
    		
    		if(envioOsiris.hasError()){
    			break;
    		}
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return envioOsiris;
    }
    

    /**
     * Crea los DetallePago para el caso que VEP = 0 o VEP = null, devuelve la cantidad de pagos creados.
     * Se trabaja con lista de VO, ya que para una transaccion y registro = 96 pueden obtenerse varias cuentas e importes. 
     * 
     * @param cn
     * @param tranAfip
     * @param campos
     * @return la cantida de pagos procesados
     * @throws Exception 
     */
    private Integer createPagoSoloVEP0(Connection cn, TranAfip tranAfip ,List<Map<String, Object>> listOblig) throws Exception {
    	if (listOblig.size() == 0) return 0; 

    	Integer canPagos = 0;
		Integer impuesto = 0;
		Double importePago = 0.0D;
		Long numeroCuenta = readCampoLong(cn, tranAfip.getIdTransaccionAfip(), 37);
		
		//utilizo esta lista para setear impuesto e importePago
		List<DetallePagoVO> listDetallePagoVO = new ArrayList<DetallePagoVO>();
		
		//se trata de una cuenta contributiva
		if (null == numeroCuenta || StringUtil.formatLong(numeroCuenta).length() > 6) {
			
			for(Map<String, Object> moblig : listOblig) {
				// determino impuesto e importePago
				importePago = (Double) moblig.get("monto_ingresado");
				impuesto = (Integer) moblig.get("impuesto");
				
				// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
				String periodoAnio = moblig.get("periodo_fiscal").toString();
				
				DetallePagoVO detallePagoVO = new DetallePagoVO();
				
				detallePagoVO.setNumeroCuenta(StringUtil.formatLong(numeroCuenta));
				detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
				detallePagoVO.setImportePago(importePago);
				detallePagoVO.setImpuesto(impuesto);
				
				listDetallePagoVO.add(detallePagoVO);
			}
			
		}else{
			//Obtengo el primer elemento de la lista de campos
			Map<String, Object> moblig = listOblig.get(0);
			
			// anio y periodo de : el primer registro de fmlr_obligacion el campo periodo_fiscal
			String periodoAnio = moblig.get("periodo_fiscal").toString();
			
			int formulario; 
			//Solo pago ETuR
			if (tranAfip.getFormulario().intValue() == FormularioAfip.ETUR_SOLO_PAGO_6056.getId().intValue() ||
					tranAfip.getFormulario().intValue() == FormularioAfip.ETUR_SOLO_PAGO_6064_BETA.getId().intValue()) {
				formulario = FormularioAfip.ETUR_PRES_Y_PAGO_6054.getId();
			} else {
				//Solo pago DReI
				formulario = FormularioAfip.DREI_PRES_Y_PAGO_6050.getId();
			}
			
			//casos de nros verificadores de DJs relacionadas
			String querySql = "select t.transaccion_afip from transaccion t, fmlr_disquete d";
				   querySql+= " where t.transaccion_afip = d.transaccion_afip and t.formulario ="+ formulario;
				   querySql+= " and t.cuit = " + tranAfip.getCuit();
				   querySql+= " and d.registro = 1 and d.c14n = " + numeroCuenta;
				   
			//busco la DJ relacionada (DReI = 6050, Etur = 6054)
			List<Map<String, Object>>  camposDJ = JdbcUtil.queryList(cn, querySql);
			
			//si recupera solo un registro
			if (camposDJ.size() == 1) {
				//obtengo el primer elemento para determinar la transaccion_afip
				Map<String, Object> cpo = camposDJ.get(0);
				Integer transaccionAfip = (Integer) cpo.get("transaccion_afip"); 
				 
				//busco las cuentas e importes relacionados a la transaccion
				List<Map<String, Object>> camposCeI = JdbcUtil.queryList(cn, "select contenido_c from fmlr_disquete where registro = 96 and transaccion_afip = " + transaccionAfip);
				
				for (Map<String, Object> mcampo : camposCeI) {
					DetallePagoVO detallePagoVO = new DetallePagoVO();
					
					detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
					detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));	
					
					String cc = (String) mcampo.get("contenido_c");
					if (!StringUtil.isNullOrEmpty(cc)) {
						detallePagoVO.setNumeroCuenta(cc.substring(0, 9));
						detallePagoVO.setImportePago(Double.valueOf(cc.substring(13, 26) + "." + cc.substring(26, 28)));
						detallePagoVO.setImpuesto(Integer.valueOf(cc.substring(9,13)));
					} 
					
					listDetallePagoVO.add(detallePagoVO);
				}
			} else {
			    //si recuperó mas de 1 o ningun registro, el caso debe ir a indeterminados
				DetallePagoVO detallePagoVO = new DetallePagoVO();
				importePago = tranAfip.getTotMontoIngresado();//ponemos el monto total para que no genere inconsistencias en el envio
				impuesto = (Integer) moblig.get("impuesto");
				
				detallePagoVO.setAnio(Integer.valueOf(periodoAnio.substring(0, 4)));
				detallePagoVO.setPeriodo(Integer.valueOf(periodoAnio.substring(4,6)));
				
				
				detallePagoVO.setImportePago(importePago);
				detallePagoVO.setImpuesto(impuesto);
				
				// en este caso numeroCuenta es un nro verificador
				detallePagoVO.setNumeroCuenta(StringUtil.formatLong(numeroCuenta));
			
				listDetallePagoVO.add(detallePagoVO);
			}
		}
    	
    	//Determino el Recurso asociado a la transaccion
    	Long idRecurso = findIdRecurso(tranAfip.getCodRecursoSegunFormulario());
		for (DetallePagoVO detallePagoVO : listDetallePagoVO) {
			
			DetallePago detallePago = new DetallePago();
			detallePago.setTranAfip(tranAfip);
			detallePago.setFechaPago(tranAfip.getFechaProceso());
			detallePago.setCaja(tranAfip.getCierreBanco().getBanco());
			detallePago.setEstDetPago(EstDetPago.getById(EstDetPago.ID_PENDIENTE));
			
			detallePago.setAnio(detallePagoVO.getAnio());
			detallePago.setPeriodo(detallePagoVO.getPeriodo());						

			numeroCuenta = null;
			if (!StringUtil.isNullOrEmpty(detallePagoVO.getNumeroCuenta())) {
				numeroCuenta = Long.valueOf(detallePagoVO.getNumeroCuenta()); 
			}
			
			// pasamos numerocuenta, importes y impuesto al detallepago
			fillCuenta(detallePago,idRecurso, numeroCuenta);
			detallePago.setImportePago(detallePagoVO.getImportePago());
			detallePago.setImpuesto(detallePagoVO.getImpuesto());
			
			if (detallePago.validateCreate()) {
				BalDAOFactory.getDetallePagoDAO().update(detallePago);
				log.debug("Se crea el detalle de pago");
			} else {
				detallePago.passErrorMessages(tranAfip);
				break;
			}
			canPagos++;
		}
    
		return canPagos;
    }
    
    
    /**
     * Obtiene la sumatoria de monto total ingresado en las transacciones asociadas al EnvioOsiris.
     *  
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public Double getMontoTotalEnvio(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	Double montoTotal = 0D;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);
    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	
    	String sqlTran = "select sum(t.total_monto_ingresado) as total from transaccion t,envio_cierre_banco e ";
    		   sqlTran+= " where t.banco=e.banco and t.cierre_banco=e.cierre_banco and e.envio_id=" + envioOsiris.getIdEnvioAfip();
    	
    	pstmt = con.prepareStatement(sqlTran);
    	rs = pstmt.executeQuery();
   	
    	while(rs.next()){
    		montoTotal = rs.getDouble("total");
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	if (null == montoTotal) return 0D;
    	
    	return montoTotal;
    }
    
    /**
     * Obtiene un Map<idTransaccionAfip,totalMontoIngresado> para todas las transacciones asociadas al EnvioOsiris
     * 
     * @param envioOsiris
     * @return
     * @throws Exception
     */
    public Map<Long,Double> getMapTransaccionMonto(EnvioOsiris envioOsiris) throws Exception{
    	String funcName = DemodaUtil.currentMethodName();
    	if (log.isDebugEnabled()) log.debug(funcName + ": enter");
    	
    	Map<Long,Double> mapValues = new HashMap<Long, Double>();
    	
    	Connection        con   = null;
    	PreparedStatement pstmt = null;
    	ResultSet         rs    = null;
    	
    	// conecta con la base
    	con = JDBCConnManager.getConnection(DS_NAME);
    	con.setTransactionIsolation(java.sql.Connection.TRANSACTION_READ_UNCOMMITTED);
    	
    	String sqlTran = "select t.transaccion_afip , t.total_monto_ingresado from transaccion t,envio_cierre_banco e ";
    		   sqlTran+= " where t.banco=e.banco and t.cierre_banco=e.cierre_banco and e.envio_id=" + envioOsiris.getIdEnvioAfip();
    	
    	pstmt = con.prepareStatement(sqlTran);
    	rs = pstmt.executeQuery();
   	
    	while(rs.next()){
    		mapValues.put(rs.getLong(1), rs.getDouble(2));
    	}
    	
    	try {rs.close();}    catch (Exception ex) {}
    	try {pstmt.close();} catch (Exception ex) {}
    	try {con.close();}   catch (Exception ex) {}
    	
    	return mapValues;
    }
}
