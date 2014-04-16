//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import ar.gov.rosario.siat.exe.view.struts.BuscarCueExeDAction;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.gde.iface.model.CambioPlanCDMAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesgloseAjusteAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReconfeccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqTramiteWeb;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.gde.view.util.LiqDeudaUtil;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import ar.gov.rosario.siat.rec.view.struts.BuscarNovedadRSDAction;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarLiqDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqDeudaDAction.class);
	
	/**
	 * Metodo inicializa para el grupo de usuarios perntenecientes a Gestion de Recurso.
	 * 
	 * @throws Exception
	 */
	public ActionForward inicializarGr(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDeudaAdapter liqDeudaAdapterVO = null;
		String stringServicio = "getLiqDeudaAdapterGRInit";
		try {
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterGRInit(userSession);
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQDEUDA_INGRESO_GR_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Inicializar para el contribuyente desde la web y para el recurso que llegue como parametro
	 * 
	 * @throws Exception
	 */
	public ActionForward inicializarContr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName + " web ###### ");
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, "inicializarContrTGI");
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDeudaAdapter liqDeudaAdapterVO = null;
		String stringServicio = "getLiqDeudaAdapterContrInit";
		try {
			
			log.debug(funcName + " id:" + request.getParameter("id"));
			
			CommonKey idRecurso = new CommonKey(request.getParameter("id"));
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterContrInit(userSession, idRecurso);
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			userSession.setIdRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getId().toString());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQDEUDA_INGRESO_CONTR_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
	/**
	 * Ingreso para el usuario Gestion de Recurso
	 * Busca la cuenta por recurso y numero de cuenta. 
	 * y permite param por recurso, si es autoliquidable, puede recibe estado periodo, clasificacion deuda, y 
	 * fecha vencimiento desde - hasta.
	 * 
	 * @throws Exception
	 */
	public ActionForward ingresarLiqDeudaGr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		String stringServicio="getIdDeudaByRecursoNroCuenta";
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();

		try {
			
			// Mantenemos la navegacion Segun valor de session, request o default
			String liqDeudaVuelveA = (String) userSession.get("liqDeudaVuelveA");
			String liqDeudaVieneDe = "recursoCuenta";
			
			// Si viene navegando de otro lado respetamos eso.
			if (StringUtil.isNullOrEmpty(liqDeudaVuelveA)){

				String vuelveReq = (String) request.getParameter("liqDeudaVieneDe");
				
				if (!StringUtil.isNullOrEmpty(vuelveReq))
					liqDeudaVieneDe = vuelveReq;

				request.setAttribute("liqDeudaVieneDe", liqDeudaVieneDe);				
			} 
			
			log.debug(funcName + " liqDeudaVieneDe: " + liqDeudaVieneDe);
			log.debug(funcName + " liqDeudaVuelveA: " + liqDeudaVuelveA);
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " numeroCuenta: " + request.getParameter("cuenta.numeroCuenta"));
			if (request.getParameter("cuenta.numeroCuenta") != null ){ 			
				liqDeudaAdapterVO.getCuenta().setNumeroCuenta(request.getParameter("cuenta.numeroCuenta"));
			}
			
			log.debug(funcName + " idRecurso: " + request.getParameter("cuenta.idRecurso"));
			if ( request.getParameter("cuenta.idRecurso") != null &&			
					request.getParameter("cuenta.idRecurso") != "-1"){
				liqDeudaAdapterVO.getCuenta().setIdRecurso(new Long(request.getParameter("cuenta.idRecurso")));
			}
			
			// Mantenemos los filtros iniciales durante toda la navegacion
			LiqDeudaUtil.populateCuentaFilters(liqDeudaAdapterVO.getCuenta(), request, "cuenta.");
			
			// Seteamos el filtro para mantenerlo durante la navegacion 
			userSession.put("liqCuentaFilter", liqDeudaAdapterVO.getCuenta());
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getIdCuentaByRecursoNroCuenta(userSession, liqDeudaAdapterVO);
			
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_LIQDEUDA_INGRESO_GR_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}

			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			Long selectedId = liqDeudaAdapterVO.getCuenta().getIdCuenta();
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId;
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 *  Ingreso Para el usuario web
	 *  Busca la cuenta por Recurso (hidden en el jsp), Numero de Cuenta y Codigo de Gestion Personal  
	 * 
	 * @throws Exception
	 */
	public ActionForward ingresarLiqDeudaContr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		DemodaTimer dt = new DemodaTimer();
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
		String stringServicio = "getLiqDeudaAdapterByNroCuentaCodGesPer()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " numeroCuenta: " + request.getParameter("cuenta.numeroCuenta"));
			if (request.getParameter("cuenta.numeroCuenta") != null ){ 			
				liqDeudaAdapterVO.getCuenta().setNumeroCuenta(request.getParameter("cuenta.numeroCuenta"));
			}
			
			log.debug(funcName + " idRecurso: " + request.getParameter("cuenta.idRecurso"));
			if ( request.getParameter("cuenta.idRecurso") != null &&			
					request.getParameter("cuenta.idRecurso") != "-1"){
				liqDeudaAdapterVO.getCuenta().setIdRecurso(new Long(request.getParameter("cuenta.idRecurso")));
			}

			// Verificamos en la lista de IdRecurso del parametro para no requerir el codigo de gestion personal (Solicitado en mantis 5105)
			String listIdRecSinCodGesPer = null;
			try{ listIdRecSinCodGesPer = SiatParam.getString(SiatParam.LISTA_ID_REC_SIN_COD_GES_PER); }catch (Exception e) {}
			String idRecursoStr = "|" + liqDeudaAdapterVO.getCuenta().getIdRecurso().toString() + "|";
			if (!StringUtil.isNullOrEmpty(listIdRecSinCodGesPer) && listIdRecSinCodGesPer.indexOf(idRecursoStr) >= 0){
				liqDeudaAdapterVO.setCodGesPerRequerido(false);
				log.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> CODGESPER NO REQUERIDO 2 <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<");
			}else{
				liqDeudaAdapterVO.setCodGesPerRequerido(true);
			}
			
			if(liqDeudaAdapterVO.getCodGesPerRequerido()){
				log.debug(funcName + " codGesPer: " + request.getParameter("cuenta.codGesPer"));
				if (request.getParameter("cuenta.numeroCuenta") != null ){ 			
					liqDeudaAdapterVO.getCuenta().setCodGesPer(request.getParameter("cuenta.codGesPer"));
				}				
			}else{
				log.debug(funcName + " codGesPer no solicitado.");
			}
			
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getIdCuentaByNroCuentaCodGesPer(userSession, 
																											liqDeudaAdapterVO);
			log.info(dt.stop(" Tiempo total en recuperar cuenta Anonima " + liqDeudaAdapterVO.getCuenta().getNumeroCuenta()));

			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_LIQDEUDA_INGRESO_CONTR_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			// Grabamos el tramite web ******************************************************************
			//if(userSession.getIsAnonimo()){ //Nuevo
				LiqTramiteWeb liqTramiteWeb = new LiqTramiteWeb();
				
				liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_CONSULTA_RECURSOCUENTA);
				liqTramiteWeb.setIdObjeto(liqDeudaAdapterVO.getCuenta().getCodRecurso() + 
										  liqDeudaAdapterVO.getCuenta().getNroCuenta());
				
				PadServiceLocator.getVariosWebFacade().grabarTramiteWeb(liqTramiteWeb);
			//}
			// Fin tramite web **************************************************************************
			
			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());

			// Si no es autoliquidable, reseteamos el filtro
			LiqCuentaVO cuentaFilter = new LiqCuentaVO();
			
			if (liqDeudaAdapterVO.getCuenta().getEsRecursoAutoliquidable()){
				cuentaFilter.setFechaVtoDesde(DateUtil.getDate("01/02/2009", DateUtil.ddSMMSYYYY_MASK));
			}
			userSession.put("liqCuentaFilter", cuentaFilter);

			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			Long selectedId = liqDeudaAdapterVO.getCuenta().getIdCuenta();
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId;
			
			if (liqDeudaAdapterVO.getCuenta().getEsRecursoAutoliquidable()){
				pathVerCuenta+="&validAuto=true";
			}
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
			
			
		} catch (Exception exception) {
			exception.printStackTrace();
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
	@Deprecated
	public ActionForward ingresarLiqDeudaContrFilter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);

		try {
			
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			// Mantenemos la navegacion Segun valor de session, request o default
			String liqDeudaVuelveA = (String) userSession.get("liqDeudaVuelveA");
			String liqDeudaVieneDe = "recursoCuenta";
			
			// Si viene navegando de otro lado respetamos eso.
			if (StringUtil.isNullOrEmpty(liqDeudaVuelveA)){

				String vuelveReq = (String) request.getParameter("liqDeudaVieneDe");
				
				if (!StringUtil.isNullOrEmpty(vuelveReq))
					liqDeudaVieneDe = vuelveReq;

				request.setAttribute("liqDeudaVieneDe", liqDeudaVieneDe);				
			} 
			
			log.debug(funcName + " liqDeudaVieneDe: " + liqDeudaVieneDe);
			log.debug(funcName + " liqDeudaVuelveA: " + liqDeudaVuelveA);
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " numeroCuenta: " + request.getParameter("cuenta.numeroCuenta"));
			if (request.getParameter("cuenta.numeroCuenta") != null ){ 			
				liqDeudaAdapterVO.getCuenta().setNumeroCuenta(request.getParameter("cuenta.numeroCuenta"));
			}
			
			log.debug(funcName + " idRecurso: " + request.getParameter("cuenta.idRecurso"));
			if ( request.getParameter("cuenta.idRecurso") != null &&			
					request.getParameter("cuenta.idRecurso") != "-1"){
				liqDeudaAdapterVO.getCuenta().setIdRecurso(new Long(request.getParameter("cuenta.idRecurso")));
			}
			
			// Mantenemos los filtros iniciales durante toda la navegacion
			LiqDeudaUtil.populateCuentaFilters(liqDeudaAdapterVO.getCuenta(), request, "cuenta.");
			
			// Seteamos el filtro para mantenerlo durante la navegacion 
			userSession.put("liqCuentaFilter", liqDeudaAdapterVO.getCuenta());
			
			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			Long selectedId = liqDeudaAdapterVO.getCuenta().getIdCuenta();
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId;
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			userSession.remove(LiqDeudaAdapter.NAME);
			
			return  new ActionForward (pathVerCuenta);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}

	
	
	/**
	 *  Cuando se elije un recurso, se setean los parametros de busqueda de deuda segun sea o no autoliquidable
	 * 
	 * @return
	 * @throws Exception
	 */
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		String stringServicio="getLiqDeudaAdapterParamRecurso";
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, "ingresarLiqDeudaGr");
		if (userSession == null) return forwardErrorSession(request);
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();

		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " numeroCuenta: " + request.getParameter("cuenta.numeroCuenta"));
			if (request.getParameter("cuenta.numeroCuenta") != null ){ 			
				liqDeudaAdapterVO.getCuenta().setNumeroCuenta(request.getParameter("cuenta.numeroCuenta"));
			}
			
			log.debug(funcName + " idRecurso: " + request.getParameter("cuenta.idRecurso"));
			if ( request.getParameter("cuenta.idRecurso") != null &&			
					request.getParameter("cuenta.idRecurso") != "-1"){
				liqDeudaAdapterVO.getCuenta().setIdRecurso(new Long(request.getParameter("cuenta.idRecurso")));
			}
			
		    // Mantenemos los filtros iniciales durante toda la navegacion
			LiqDeudaUtil.populateCuentaFilters(liqDeudaAdapterVO.getCuenta(), request);
		    
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterParamRecurso(userSession,liqDeudaAdapterVO);

			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_LIQDEUDA_INGRESO_GR_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			request.setAttribute("liqDeudaVieneDe", "recursoCuenta");
			
			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQDEUDA_INGRESO_GR_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Permite ver una cuenta dado su id submitido, 
	 * Recarga el mismo LiqDeudaAdapter con la nueva cuenta
	 * Utilizado desde la "Liquidacion de Deuda por Contribuyente"
	 * 
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
		
		try {
			
			boolean validarAutoliquidable = true;
			
			// Mantenemos la navegacion Segun valor de session, request o default
			String liqDeudaVuelveA = (String) userSession.get("liqDeudaVuelveA");
			String liqDeudaVieneDe = "recursoCuenta";
			
			// Si viene navegando de otro lado respetamos eso.
			if (StringUtil.isNullOrEmpty(liqDeudaVuelveA)){

				String vuelveReq = (String) request.getAttribute("liqDeudaVieneDe");
				
				if (!StringUtil.isNullOrEmpty(vuelveReq))
					liqDeudaVieneDe = vuelveReq;

				request.setAttribute("liqDeudaVieneDe", liqDeudaVieneDe);				
			} 
				
			// Si no viene seteado, Seteamos los valores para saber a donde hay que volver
			if (userSession.get("liqDeudaVuelveA") == null)
				setearValoresVolver(userSession, request);

			
			log.debug(funcName + " liqDeudaVieneDe: " + liqDeudaVieneDe);
			log.debug(funcName + " liqDeudaVuelveA: " + liqDeudaVuelveA);
			
			// Recuperamos datos del form en el vo para comprobar si es autoliquidable.
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			log.debug(funcName + " validAuto: " + request.getParameter("validAuto"));
			log.debug(funcName + " idCuentaTitular: " + request.getParameter("idCuentaTitular"));
			
			if (request.getParameter("selectedId") != null ){ 			
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("selectedId")));
			}
			
			if (request.getParameter("validAuto") != null && request.getParameter("validAuto").equals("false")){
				validarAutoliquidable = false;
			}
			
			if (request.getParameter("idCuentaTitular") != null){
				liqDeudaAdapterVO.getCuenta().setIdCuentaTitular(new Long(request.getParameter("idCuentaTitular")));
			}
			
			// Logica para que al usuario anonimo no le aparezca la pantalla de filtros.
			if (validarAutoliquidable && userSession.getEsAnonimo()){
				
				// Param Cuenta para saber si saltamos a un autoliquidable o no.
				liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterParamCuenta(userSession, liqDeudaAdapterVO);
				
				LiqCuentaVO cuentaFilter = new LiqCuentaVO();
								
				if (liqDeudaAdapterVO.getCuenta().getEsRecursoAutoliquidable()){
					cuentaFilter.setFechaVtoDesde(DateUtil.getDate("01/02/2009", DateUtil.ddSMMSYYYY_MASK));
				}
				
				log.debug("valida y anonimo");
				
				userSession.put("liqCuentaFilter", cuentaFilter);
				
				// Envio el VO al request
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				
			} else if(validarAutoliquidable){

				liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterParamCuenta(userSession, liqDeudaAdapterVO);
				
				// Tiene errores recuperables
				if (liqDeudaAdapterVO.hasErrorRecoverable()) {
					
					log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
					saveDemodaErrors(request, liqDeudaAdapterVO);
					request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
					return mapping.getInputForward();
				}
				
				// Tiene errores no recuperables
				if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: getLiqDeudaAdapterParamCuenta() : " + liqDeudaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				}

				if (liqDeudaAdapterVO.getCuenta().getEsRecursoAutoliquidable()){
					
					// Seteamos el Codigo de Recurso para obtener la frase correcta
					userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
					
					if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
					
					// Envio el VO al request
					request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
					
					saveDemodaMessages(request, liqDeudaAdapterVO);
					
					return mapping.findForward(GdeConstants.FWD_LIQDEUDA_FILTROCUENTAAUTO_ADAPTER);	
				} else {
					// Si no es autoliquidable, reseteamos el filtro
					LiqCuentaVO cuentaFilter = new LiqCuentaVO();
					
					cuentaFilter.setIdCuentaTitular(liqDeudaAdapterVO.getCuenta().getIdCuentaTitular());
					cuentaFilter.setDeudaSigueTitular(liqDeudaAdapterVO.getCuenta().getDeudaSigueTitular());
					
					userSession.put("liqCuentaFilter", cuentaFilter);
					return fwdDeudaAdapter(mapping, request, userSession);
				}
			}
			
			log.debug("es Auto: "+liqDeudaAdapterVO.getCuenta().getEsRecursoAutoliquidable());
			
			return fwdDeudaAdapter(mapping, request, userSession);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}	
	}
	
	/**
	 * Permite ver una cuenta dado su id submitido, para el caso de "Unificaciones/Desgloses"  
	 * Recarga el mismo LiqDeudaAdapter con la nueva cuenta
	 * 
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verCuentaDesgUnif(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			Long selectedId = new Long(request.getParameter("selectedId"));
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=true";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}

	}
	
	/**
	 * Permite ver una cuenta dado su id submitido, para el caso de "Cuentas Relacionadas" por medio del Objeto Imponible.  
	 * Recarga el mismo LiqDeudaAdapter con la nueva cuenta
	 * 
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verCuentaRel(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			Long selectedId = new Long(request.getParameter("selectedId"));
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=true";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
		
	}
	
	/**
	 * Permite ver la deuda de todas las cuentas del contribuyente seleccionado. 
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verDeudaContrib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DEUDA_CONTRIB, BaseConstants.ACT_BUSCAR);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			CommonKey contribuyenteKey = null;
				
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idTitular: " + request.getParameter("id"));
			if (request.getParameter("id") != null ){ 			
				contribuyenteKey = new CommonKey(request.getParameter("id"));
			}
			
			userSession.getNavModel().putParameter(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY, contribuyenteKey);
			userSession.getNavModel().setAct(BaseConstants.ACT_BUSCAR);
			request.setAttribute("vieneDe", "liqDeuda");
			
			return mapping.findForward(GdeConstants.ACTION_INICIALIZAR_DEUDACONTRIB);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 *  Forward a ver Convenio
	 *  
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward verConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setAttribute("vieneDe", "liqDeuda");
		return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_CUENTA);
		
	}
	
	/**
	 * Forward a ver Detalle Deuda
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward verDetalleDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_VER_DETALLE_DEUDA);
	}

	
	/**
	 *  Forward a Reclamar Acentamiento 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward reclamarAcent(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_RECLAMAR_ACENT);
		
	}
	
	
	/**
	 * Permite ir al caso de uso que muestra los datos del Caso segun sea Expediente, Nota, Etc.
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCaso: " + request.getParameter("id"));
			
			return mapping.findForward(GdeConstants.FWD_LIQDEUDA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Permite ver el historico de las Acciones de las Solicitudes de Exencion de la Cuenta.
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verHistoricoExe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			CommonKey idCuenta = new CommonKey(request.getParameter("selectedId"));
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			
			if (request.getParameter("selectedId") != null)
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("selectedId")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			liqDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
			liqDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			userSession.put(BuscarCueExeDAction.VER_CUEEXE_ID_CUENTA, idCuenta.getId());

			// seteo el act a ejecutar en el accion al cual me dirijo		
			userSession.getNavModel().setAct(BaseConstants.ACT_INICIALIZAR);

			return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, ExeConstants.ACTION_BUSCAR_CUEEXE, 
					BaseConstants.ACT_INICIALIZAR); 

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward irImprimirLiqDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_IMPRIMIR_INFORME_DEUDA);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			log.debug(funcName+"  idCuenta: "+request.getParameter("selectedId"));
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			DemodaUtil.populateVO(liqDeudaAdapterVO, request);
			
			liqDeudaAdapterVO.getCuenta().setIdCuenta(Long.parseLong(request.getParameter("selectedId")));
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);

			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			// Subo un liqReconfeccionAdapter con el id de Cuenta para que funcione el volver
			LiqReconfeccionAdapter liqReconfAdapter = new LiqReconfeccionAdapter();
			liqReconfAdapter.setCuenta(new LiqCuentaVO());
			liqReconfAdapter.getCuenta().setIdCuenta(liqDeudaAdapterVO.getCuenta().getIdCuenta());
			
			userSession.put(LiqReconfeccionAdapter.NAME, liqReconfAdapter);
			return mapping.findForward(GdeConstants.FWD_LIQDEUDA_IMPRIMIR_INFORME_DEUDA);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward imprimirLiqDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_IMPRIMIR_INFORME_DEUDA);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			// Si el usuario es anomimo grabamos el tramite web
			if(userSession.getIsAnonimo()){
				// Grabamos el tramite web ******************************************************************
				LiqTramiteWeb liqTramiteWeb = new LiqTramiteWeb();
				
				liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterByIdCuenta(userSession, liqDeudaAdapterVO);
				
				liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_IMPRIMIR_LIQUIDACION_DEUDA);
				liqTramiteWeb.setIdObjeto(liqDeudaAdapterVO.getCuenta().getCodRecurso() + 
										  liqDeudaAdapterVO.getCuenta().getNroCuenta());
				
				PadServiceLocator.getVariosWebFacade().grabarTramiteWeb(liqTramiteWeb);
				// Fin tramite web **************************************************************************
			}
			
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
				
			liqCuentaFiltro.setIdCuenta(liqDeudaAdapterVO.getCuenta().getIdCuenta());
			
			PrintModel print = GdeServiceLocator.getGestionDeudaService().imprimirInformeLiqDeuda(userSession, 
					liqCuentaFiltro, null);
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward reconfeccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECONFECCIONAR);
		if (userSession == null) return forwardErrorSession(request);
		
		userSession.getNavModel().putParameter("esReconfEspecial", false);

		return mapping.findForward(GdeConstants.ACTION_RECONFECCIONAR);
		
	}
	
	public ActionForward reconfeccionarEsp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECONFECCIONAR_ESP);
		if (userSession == null) return forwardErrorSession(request);
		
		userSession.getNavModel().putParameter("esReconfEspecial", true);
		
		return mapping.findForward(GdeConstants.ACTION_RECONFECCIONAR);
		
	}
	
	/**
	 * Forward al action que administra la Formalizacion de Convenio de pago.
	 * 
	 *  
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward formalizarConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_FORMCONVENIO);		
	}
	
	/**
	 * Forward al action que administra el Desglose de ajuste.
	 * 
	 *  
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward desglosarAjuste(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_DESGLOSE_AJUSTE);		
	}
	
	public ActionForward formalizarConvenioEsp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_FORMCONVENIOESP);		
	}
	
	/**
	 * Volver al menu principal   
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverAlMenu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return new ActionForward (BaseConstants.FWD_SIAT_BUILD_MENU);
		
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = "volver";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
		
			userSession.getNavModel().setAct("volver");
			
			String volverA=userSession.get("liqDeudaVuelveA").toString();
			userSession.remove("liqDeudaVuelveA");
			
			userSession.remove("liqCuentaFilter");
			
			log.debug("Volver A..."+volverA);
			return new ActionForward(volverA);
		
		} catch (Exception e) {
			e.printStackTrace();
			return baseException(mapping, request, funcName, e, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward volverAnt(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = "volver";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();		
		try {
			
			String actForward = "";
			
			// Si alguien me clavo un navModel con el nombre "volver", vuelvo a ese lugar
			NavModel navModelVolver = (NavModel) navModel.getParameter(BaseConstants.ACT_VOLVER); 
			
			if (navModelVolver != null) {
				actForward = StringUtil.getActionPath(navModelVolver.getPrevAction(), navModelVolver.getPrevActionParameter());
				navModel.clearParametersMap();
			} else {
				// Seteo de valores para volver al Ingreso GR
				actForward = StringUtil.getActionPath(GdeConstants.PATH_INICIALIZAR_GR, GdeConstants.ACTION_INICIALIZAR_GR);
			} 
			
			log.debug(funcName + ": intentando volver a :" + actForward);
			
			return new ActionForward (actForward);
		} catch (Exception e) {
			log.error("Exception - ", e);
			e.printStackTrace();
			// falta definir llamada o no a logout 
			return forwardErrorSession(mapping, request);
		}
	}

	
	/**
	 * Factorizacion del forward al Adapter de Deuda. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	private ActionForward fwdDeudaAdapter(ActionMapping mapping, HttpServletRequest request, UserSession userSession)
			throws Exception {
		
		DemodaTimer dt = new DemodaTimer();
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
		String stringServicio = "getLiqDeudaAdapterByIdCuenta";
		try {
			
			// Mantenemos los filtros iniciales durante toda la navegacion
			LiqCuentaVO liqCuentaFilter = (LiqCuentaVO) userSession.get("liqCuentaFilter");
			if (liqCuentaFilter == null) return forwardErrorSession(request);
			liqDeudaAdapterVO.setCuenta(liqCuentaFilter); 

			// Recuperamos datos del form en el vo		
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			if (request.getParameter("selectedId") != null ){ 			
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("selectedId")));
			}
			
	        // Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);

				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.getInputForward();
			}
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterByIdCuenta(userSession, liqDeudaAdapterVO);
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			// Si se utiliza volverACuenta desde la formalizacion de convenio ***************************************************
			LiqFormConvenioAdapter liqFormConvenioAdapterVO =  (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);
			
			if (liqFormConvenioAdapterVO != null &&
					liqFormConvenioAdapterVO.hasErrorOrMessage()){
				log.debug(funcName + ": liqFormConvenioAdapter ErrorRecoverable Encontrado " );
				liqFormConvenioAdapterVO.passErrorMessages(liqDeudaAdapterVO);
			}

			userSession.remove(LiqFormConvenioAdapter.NAME);

			// Si se utiliza volverACuenta desde la reconfeccion *****************************************************************
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter) userSession.get(LiqReconfeccionAdapter.NAME);
			
			if (liqReconfeccionAdapterVO != null &&
					liqReconfeccionAdapterVO.hasErrorOrMessage()){
				log.debug(funcName + ": liqReconfeccionAdapter ErrorRecoverable Encontrado " );
				liqReconfeccionAdapterVO.passErrorMessages(liqDeudaAdapterVO);
			}

			userSession.remove(LiqReconfeccionAdapter.NAME);
			
			// Si se utiliza volverACuenta desde la Desglosar Ajuste *****************************************************************
			DesgloseAjusteAdapter desgloseAjusteAdapterVO = (DesgloseAjusteAdapter) userSession.get(DesgloseAjusteAdapter.NAME);
			
			if (desgloseAjusteAdapterVO != null &&
					desgloseAjusteAdapterVO.hasErrorOrMessage()){
				log.debug(funcName + ": desgloseAjusteAdapterVO ErrorRecoverable Encontrado " );
				desgloseAjusteAdapterVO.passErrorMessages(liqDeudaAdapterVO);
			}

			userSession.remove(DesgloseAjusteAdapter.NAME);
			
			// Si se utiliza volverACuenta desde cambio plan CDM *****************************************************************
			CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);

			if (cambioPlanCDMAdapter != null &&
					cambioPlanCDMAdapter.hasErrorOrMessage()){
				log.debug(funcName + ": cambioPlanCDMAdapter Error or Message Encontrado " );
				cambioPlanCDMAdapter.passErrorMessages(liqDeudaAdapterVO);
			}

			userSession.remove(CambioPlanCDMAdapter.NAME);
			
			// Si se utiliza volverACuenta desde la Declaracion Jurada Masiva *****************************************************************
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.get(LiqDecJurAdapter.NAME);
			
			if (liqDecJurAdapterVO != null &&
					liqDecJurAdapterVO.hasErrorOrMessage()){
				log.debug(funcName + ": liqDecJurAdapterVO ErrorRecoverable Encontrado " );
				liqDecJurAdapterVO.passErrorMessages(liqDeudaAdapterVO);
			}

			userSession.remove(LiqDecJurAdapter.NAME);
			
			// Si se utiliza volverACuenta desde la Reclamar Asentamiento *****************************************************************
			LiqReclamoAdapter liqReclamoAdapterVO = (LiqReclamoAdapter) userSession.get(LiqReclamoAdapter.NAME);
			
			if (liqReclamoAdapterVO != null &&
					liqReclamoAdapterVO.hasErrorOrMessage()){
				log.debug(funcName + ": liqReclamoAdapterVO ErrorRecoverable Encontrado " );
				liqReclamoAdapterVO.passErrorMessages(liqDeudaAdapterVO);
			}

			userSession.remove(LiqReclamoAdapter.NAME);
			
			// Fin recupero adapters de session con error **************************************************************************
			
			saveDemodaErrors(request, liqDeudaAdapterVO);
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			// Seteamos el Codigo de Recurso para obtener la frase correcta
			userSession.setCodRecurso(liqDeudaAdapterVO.getCuenta().getRecurso().getCodRecurso());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			// Seteamos los valores para saber a donde hay que volver
			setearValoresVolver(userSession, request);
			
			log.info(dt.stop(" Tiempo total en recuperar cuenta " + liqDeudaAdapterVO.getCuenta().getNumeroCuenta()));
			
			return mapping.findForward(GdeConstants.FWD_LIQDEUDA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Setea los valores de navegacion para volver. 
	 * 
	 * @param userSession
	 * @param request
	 */
	private void setearValoresVolver(UserSession userSession, HttpServletRequest request){
		
		String funcName = DemodaUtil.currentMethodName();
		log.debug("entrando en " + funcName);
		
		String liqDeudaViendeDe= (String) request.getAttribute("liqDeudaVieneDe");
		log.debug("liqDeudaVieneDe...."+liqDeudaViendeDe);
		
		if ("recursoCuenta".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", GdeConstants.PATH_BUSCARDEUDA_REC_CUENTA);
		} else if ("deudaContrib".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", GdeConstants.PATH_VER_CUENTACONTRIB + request.getParameter("idContrib"));
			log.debug("url volver..."+userSession.get("liqDeudaVuelveA"));
		} else if ("cuentaObjImp".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", GdeConstants.PATH_BUSCAR_CUENTA_OBJIMP_ID + request.getParameter("idObjImp"));
		} else if ("cuentaProcurador".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", GdeConstants.PATH_BUSCAR_CUENTAS_PROCURADOR_ID);
		} else if ("modifOpeInvCon".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", EfConstants.PATH_MODIFICAR_OPEINVCON);
		} else if ("ordenControlSearchPage".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", EfConstants.PATH_ORDENCONTROL_SEARCHPAGE);
		} else if ("ordenControlContrSearchPage".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", EfConstants.PATH_ORDENCONTROLCONTR_SEARCHPAGE);
		} else if ("admOrdenControlFis".equals(liqDeudaViendeDe)){
			userSession.put("liqDeudaVuelveA", EfConstants.PATH_ORDENCONTROL_VIEWADAPTER);
		}
		
		log.debug("saliendo de " + funcName);
	}
	
	
	/**
	 * Forward a Ver Objeto Imponible
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward verDetalleObjImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("cuenta.idCuenta"));
			log.debug(funcName + " idObjImp: " + request.getParameter("selectedId"));
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			
			if (request.getParameter("cuenta.idCuenta") != null)
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("cuenta.idCuenta")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			liqDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
			liqDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, GdeConstants.FWD_VER_DETALLE_OBJIMP , BaseConstants.ACT_VER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Forward a ver Detalle de Cuenta, donde se muestran todos los titulares de la misma. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward verHistoricoContrib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			
			if (request.getParameter("selectedId") != null)
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("selectedId")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			liqDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
			liqDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, GdeConstants.FWD_VER_HISTORICO_CONTRIB, BaseConstants.ACT_VER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 *  Refill para recargar el Adapter de Deuda desde ObjImp, Cuenta, etc. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		// Si viene de otro CU
		if(userSession.getNavModel().getParameter("vieneDe")!=null){
			if(StringUtil.iguales((String) userSession.getNavModel().getParameter("vieneDe"), GdeConstants.ESTADOCUENTA))
				return new ActionForward(GdeConstants.PATH_INGRESO_ESTADOCUENTA_ID);			
		}
		
		try {

			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			userSession = canAccess(request, mapping, 
					liqDeudaAdapterVO.getPrevAction(),
					liqDeudaAdapterVO.getPrevActionParameter());
			
			if (userSession == null) return forwardErrorSession(request);
			
			// Si es nulo no se puede continuar
			if (liqDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqDeudaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqDeudaAdapter.NAME); 
			}
			
			Long selectedId = liqDeudaAdapterVO.getCuenta().getIdCuenta();
			log.debug("selectedId "+ liqDeudaAdapterVO.getCuenta().getIdCuenta());
			
			userSession.remove(LiqDeudaAdapter.NAME);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}

	
	public ActionForward infDeudaEscribano(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
		
		return new ActionForward (GdeConstants.PATH_INFORMEESCRIBANO);
		
	}


	public ActionForward cambioPlanCDM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_CAMBIO_PLAN_CDM);
	}
	
	
	public ActionForward cuotaSaldoCDM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_CUOTASALDO_CDM);
	}
	
	public ActionForward cierreComercio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_CIERRE_COMERCIO);
	}
	
	public ActionForward buzonCambios(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setAttribute("vieneDe", "liqDeuda");
		return new ActionForward(PadConstants.PATH_ADMINISTRAR_BUZON_CAMBIOS_INICIALIZAR);
		
	}
	
	public ActionForward imprimirCierre(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_IMPRIMIR_CIERRE_COMERCIO);
	
		
		try {
			
			Long cuentaId = new Long(request.getParameter("cuentaId"));
			
			PrintModel print = GdeServiceLocator.getGestionDeudaService().imprimirCierreComercio(userSession, cuentaId);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
	public ActionForward decJurMasiva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(GdeConstants.ACTION_DECJURMASIVA);		
	}

	
	public ActionForward verPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("cuentaId"));
			log.debug(funcName + " idPlanilla: " + request.getParameter("selectedId"));
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			
			if (request.getParameter("cuentaId") != null)
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("cuentaId")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			liqDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
			liqDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, GdeConstants.FWD_VER_PLANILLA , BaseConstants.ACT_VER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}

	/**
	 *  Declaracion Jurada de Entradas Vendidas para Espectaculos Publicos habilitados
	 * 
	 */
	public ActionForward ddjjEntVen(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Recuperamos datos del form en el vo		
				log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
				
				LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
				
				if (request.getParameter("selectedId") != null)
					liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("selectedId")));
				
				// Traspaso de permisos al adapter que el elevado a la session.
				liqDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
				liqDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
				
				userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
				return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, GdeConstants.ACTION_DDJJ_ENTVEN ,  EspConstants.MTD_ADM_DDJJENTVEN); 
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
			}
	}
	
	/**
	 * Consulta de Novedades de Regimen Simplificado para la Cuenta
	 * 
	 */
	public ActionForward verNovedadesRS(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			
			if (request.getParameter("selectedId") != null)
				liqDeudaAdapterVO.getCuenta().setIdCuenta(new Long(request.getParameter("selectedId")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			liqDeudaAdapterVO.setPrevAction(userSession.getAccionSWE());
			liqDeudaAdapterVO.setPrevActionParameter(userSession.getMetodoSWE());
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
		
			if(liqDeudaAdapterVO.getCuenta().getIdCuenta() != null){
				NovedadRSSearchPage novedadRSSPFiltro = new NovedadRSSearchPage();
				CuentaVO cuentaVO = new CuentaVO();
				cuentaVO.setId(liqDeudaAdapterVO.getCuenta().getIdCuenta());
				novedadRSSPFiltro.getNovedadRS().setCuentaDRei(cuentaVO);
				userSession.getNavModel().putParameter(BuscarNovedadRSDAction.NOVEDADRS_SEARCHPAGE_FILTRO, novedadRSSPFiltro);
			}
			
			return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, GdeConstants.ACTION_VER_NOVEDADES_RS ,  BaseConstants.ACT_BUSCAR); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
}
	
	public ActionForward volantePagoIntRS(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_VOLANTEPAGOINTRS);
		if (userSession == null) return forwardErrorSession(request);
		
		return mapping.findForward(GdeConstants.ACTION_VOLANTEPAGOINTRS);
		
	}
	
}
