//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Calendar;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.gde.iface.model.LiqReclamoVO;
import ar.gov.rosario.siat.gde.iface.model.LiqTramiteWeb;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import ar.gov.rosario.siat.pad.iface.service.IPadVariosWebFacade;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Manejador del m&oacute;dulo de Varios Web
 * 
 * @author tecso
 *
 */
public class VariosWebFacade implements IPadVariosWebFacade {
		
	private static Logger log = Logger.getLogger(VariosWebFacade.class);
	
	public static final int COD_CONSULTA_RECURSOCUENTA = 1; 	 // 01 - Consulta por Rec/Cta.
	public static final int COD_REIMPRESION_DEUDA = 2; 		 	 // 02 - Reimpresion de deuda.
	public static final int COD_RECLAMO_ASENTAMIENTO_DEUDA = 3;  // 03 - Reclamo de asentamiento de deuda.
	public static final int COD_CONSULTA_PLAN_PAGO = 19; 		 // 19 - Consulta de plan de pago de TGI
	public static final int COD_IMPRIMIR_LIQUIDACION_DEUDA = 20; // 20 - Liquidacion de TGI y MEJORAS
	public static final int COD_RECONFECCION_CUOTA_PLAN = 21; 	 // 21 - Reconfeccion de Cuotas de plan de pago de TGI
	public static final int COD_RECLAMO_ASENTAMIENTO_CUOTA = 48; // 48 - Reclamo de asentamiento de cuota
	public static final int COD_CAMBIO_DOMICILIO_WEB = 18; 		 // 18 - Cambio domicilio web 
	
	private static final VariosWebFacade INSTANCE = new VariosWebFacade();
	
	/**
	 * Constructor 
	 */
	public VariosWebFacade() {
		
	}

	/**
	 * Devuelve unica instancia
	 */
	public static VariosWebFacade getInstance() {
		return INSTANCE;
	}	
	
	/**
	 * 
	 * Regristra en varios web la operacion de cambio de domicilio
	 * 
	 * @param camDomWeb
	 * @return
	 * @throws Exception
	 */
	public int createOperacionAuditable(CamDomWeb camDomWeb) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		String idObjeto = camDomWeb.getCuenta().getRecurso().getCodRecurso() + "-" +
			camDomWeb.getCuenta().getNumeroCuenta();
		String nombre = camDomWeb.getNomSolicitante();
		String apellido = camDomWeb.getApeSolicitante();
		String direccion = camDomWeb.getDomNue().getViewDomicilio();
		String telefono = "";
		String email = camDomWeb.getMail();
		String tipoDoc = null;
		if (!StringUtil.isNullOrEmpty(camDomWeb.getAbrev_doc())) {
			tipoDoc = camDomWeb.getAbrev_doc();
		}
		
		String nroDoc = null;
		if (camDomWeb.getNumDoc() != null && camDomWeb.getNumDoc().intValue() != 0) {
			nroDoc = camDomWeb.getNumDoc().toString();
		}
		String ip = DemodaUtil.currentUserContext().getIpRequest();

		//XXX siatgpl:implement

		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		return 1;
	}
	
	
	/**
	 * Crea un ReclamoWeb a partir de un LiqReclamoVO.
	 * 
	 * @author Cristian
	 * @param liqReclamoVO
	 * @return
	 * @throws Exception
	 */
	public int createReclamoWeb(LiqReclamoVO liqReclamoVO) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		int tipoTramite = liqReclamoVO.getTipoTramite();
		String idObjeto = liqReclamoVO.getIdObjeto();
		String nombre = liqReclamoVO.getNombre(); 
		String apellido = liqReclamoVO.getApellido(); 
		String direccion = null; 
		String telefono = liqReclamoVO.getTelefono(); 
		String email = liqReclamoVO.getEmail(); 
		String tipoDoc = liqReclamoVO.getTipoDoc();
		String nroDoc = liqReclamoVO.getNroDoc(); 
		String ip = DemodaUtil.currentUserContext().getIpRequest();
		int idUsuario = 0;
		String grupo = null;
		
		//XXX siatgpl:implement
		
		String tributo = liqReclamoVO.getCodRecurso(); 
		int sistema = liqReclamoVO.getSistema(); 
		String cuenta = liqReclamoVO.getNumeroCuenta();
		int anio = liqReclamoVO.getAnio(); 
		int periodo = liqReclamoVO.getPeriodo();
		int ajuste = liqReclamoVO.getResto(); 
		Calendar fechaPago = Calendar.getInstance();
		fechaPago.setTime(DateUtil.getDate(liqReclamoVO.getFechaPagoView())); 
		double importePagado = new Double(liqReclamoVO.getImportePagadoView()); 
		String banco = liqReclamoVO.getBanco();
		String observacion = liqReclamoVO.getObservacion();
		
		//XXX siatgpl:implement
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");
		
		return 1;
	}
	
	/**
	 * Graba los tramites web realizados por usuarios anomimos
	 * 
	 * 
	 * @param liqTramiteWeb
	 * @throws Exception
	 */
	public void grabarTramiteWeb(LiqTramiteWeb liqTramiteWeb) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter -> tipoTramite: " + liqTramiteWeb.getTipoTramite());
		
		int tipoTramite = liqTramiteWeb.getTipoTramite();
		String idObjeto = liqTramiteWeb.getIdObjeto();
		String nombre = liqTramiteWeb.getNombre(); 
		String apellido = liqTramiteWeb.getApellido(); 
		String direccion = liqTramiteWeb.getDireccion(); 
		String telefono = liqTramiteWeb.getTelefono(); 
		String email = liqTramiteWeb.getEmail(); 
		String tipoDoc = liqTramiteWeb.getTipoDoc();
		String nroDoc = liqTramiteWeb.getNroDoc(); 
		String ip = DemodaUtil.currentUserContext().getIpRequest();
		int idUsuario = 0;
		String grupo = null;
		
		try {
			//XXX siatgpl:implement
		} catch (Exception e) {
			log.warn("Error al intentar actualizar info en tramites web. tipoTramite: " + tipoTramite, e);
		}
	}	
	
	/*
				01 - Consulta por Rec/Cta.
					* AdministrarLiqDeuda -> ingresarContr  #
				
				02 - Reimpresión de deuda.
					* AdministrarLiqDeuda -> reimprimir(reconfeccionar) # 
						
				03 - Reclamo de asentamiento de deuda.
					* GdeGDeudaServiceHbmImpl -> createReclamo # (nombre, apellido, direccion, telefono, email)
				
				19 - Consulta de plan de pago de TGI
					* AdministrarLiqConvenioCuentaDAction -> inicializar/verConvenio #
				
				20 - Liquidacion de TGI y MEJORAS
					* AdministrarLiqDeuda -> imprimir liquidacion deuda  #
					
				21 - Reconfección de Cuotas de plan de pago de TGI
					* AdministrarLiqConvenioCuentaDAction -> reconfeccionar #
				
				48 - Reclamo de asentamiento de cuota
					* GdeGDeudaServiceHbmImpl -> createReclamoCuota # (nombre, apellido, direccion, telefono, email)
	 * */
	
	/**
	 * 
	 * 
	 * 
	 */
	public boolean validateReclamoWeb(LiqReclamoVO liqReclamoVO) throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		int tipoTramite = liqReclamoVO.getTipoTramite();
		String tributo = liqReclamoVO.getCodRecurso(); 
		String cuenta = liqReclamoVO.getNumeroCuenta();
		int anio = liqReclamoVO.getAnio(); 
		int periodo = liqReclamoVO.getPeriodo();
		Calendar fechaPago = Calendar.getInstance();
		fechaPago.setTime(DateUtil.getDate(liqReclamoVO.getFechaPagoView())); 
		
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");

		return PadDAOFactory.getVariosWebJDBCDAO().validateReclamoWeb(tipoTramite, tributo, cuenta, anio, periodo, fechaPago);
		
	}

}