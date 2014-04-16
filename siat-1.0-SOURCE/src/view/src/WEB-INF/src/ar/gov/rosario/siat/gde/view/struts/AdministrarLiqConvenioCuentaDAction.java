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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import ar.gov.rosario.siat.gde.iface.model.ConvenioConsistenciaAdapter;
import ar.gov.rosario.siat.gde.iface.model.ConvenioEstadosAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioPagoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioSalPorCadAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReclamoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReconfeccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqTramiteWeb;
import ar.gov.rosario.siat.gde.iface.model.RescateIndividualAdapter;
import ar.gov.rosario.siat.gde.iface.model.VerPagosConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import coop.tecso.demoda.iface.DemodaServiceException;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarLiqConvenioCuentaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqConvenioCuentaDAction.class);
	
	/**
	 * Permite ir a ver el detalle de un convenio de Pago.
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward verConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_CONVENIOCUENTA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqConvenioCuentaInit()";
		LiqConvenioCuentaAdapter liqConvenioCuenta = new LiqConvenioCuentaAdapter();
		
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idConvenio: " + request.getParameter("selectedId"));
			
			if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
				liqConvenioCuenta.getConvenio().setIdConvenio(new Long(request.getParameter("selectedId")));
				
			}else{
				LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO = (LiqConvenioCuentaAdapter) userSession.get(LiqConvenioCuentaAdapter.NAME);
			 	liqConvenioCuenta.getConvenio().setIdConvenio(liqConvenioCuentaAdapterVO.getConvenio().getIdConvenio().longValue());
			}
			
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			liqConvenioCuenta.setCuentaFilter(liqCuentaFiltro);
			
			liqConvenioCuenta = GdeServiceLocator.getGestionDeudaService().getLiqConvenioCuentaInit(userSession, liqConvenioCuenta);
	
			// Si se utiliza volverAConvenio desde la Reclamar Asentamiento *****************************************************************
			LiqReclamoAdapter liqReclamoAdapterVO = (LiqReclamoAdapter) userSession.get(LiqReclamoAdapter.NAME);
			
			if (liqReclamoAdapterVO != null &&
					liqReclamoAdapterVO.hasErrorOrMessage()){
				log.debug(funcName + ": liqReclamoAdapterVO ErrorRecoverable Encontrado " );
				liqReclamoAdapterVO.passErrorMessages(liqConvenioCuenta);
			}

			userSession.remove(LiqReclamoAdapter.NAME);
						
			// Tiene errores no recuperables
			if (liqConvenioCuenta.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqConvenioCuenta.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqConvenioCuentaAdapter.NAME + ": "+ liqConvenioCuenta.infoString());
			
			// Envio el VO al request y subo al userSession
			request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
			userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
			
			saveDemodaMessages(request, liqConvenioCuenta);
			saveDemodaErrors(request, liqConvenioCuenta);
			
			return mapping.findForward(GdeConstants.FWD_LIQCONVENIOCUENTA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}

	
	/**
	 * Utilizado para volver desde la formalizacion de convenio 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverACuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		
		try {
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			Long selectedId = new Long(request.getParameter("selectedId"));
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}

	}
	
	/**
	 * Obtiene y muestra un detalle de un convenio recibiendo un selectedId 
	 * 
	 * Vuelve a donde le hallan indicado volver.
	 *  
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_CONVENIOCUENTA,	
				GdeSecurityConstants.MTD_VER_CONVENIO);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqConvenioCuentaInit()";
		LiqConvenioCuentaAdapter liqConvenioCuentaVO = new LiqConvenioCuentaAdapter();
		
		try {
			
			String selectedId = userSession.getNavModel().getSelectedId();
			
			userSession.getNavModel().setAct(GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);
	
			if (StringUtil.isNullOrEmpty(selectedId)){
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqConvenioCuentaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);				
			} else {
				liqConvenioCuentaVO.getConvenio().setIdConvenio(new Long(selectedId));
			}
		
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			liqConvenioCuentaVO.setCuentaFilter(liqCuentaFiltro);
			
			liqConvenioCuentaVO = GdeServiceLocator.getGestionDeudaService().getLiqConvenioCuentaInit(userSession, liqConvenioCuentaVO);
			String vieneDe = (String) request.getAttribute("vieneDe");
			Long idCuenta = liqConvenioCuentaVO.getCuenta().getIdCuenta();
			if ("liqDeuda".equals(vieneDe)){
				userSession.put("verConvenioVolverA", GdeConstants.PATH_VER_CUENTA + idCuenta);
			} else if ("liqDeudaCyq".equals(vieneDe)){
				Long idProced = liqConvenioCuentaVO.getProcedimiento().getId();
				userSession.put("verConvenioVolverA", CyqConstants.PATH_VER_LIQDEUDACYQ + idProced);	
			}else if ("buscarConvenio".equals(vieneDe)){
				userSession.put("verConvenioVolverA", GdeConstants.PATH_BUSCAR_CONVENIO + idCuenta);
			}else if ("estadoCuenta".equals(vieneDe)){
				userSession.put ("verConvenioVolverA",GdeConstants.PATH_INGRESO_ESTADOCUENTA_ID + idCuenta);
			}else if ("salPorCad".equals(vieneDe)){
				userSession.put("verConvenioVolverA", GdeConstants.PATH_SALPORCADSELECCIONAR+request.getAttribute("idSalPorCad"));
			}else if ("rescate".equals(vieneDe)){
				userSession.put("verConvenioVolverA", GdeConstants.PATH_RESCATE_ADMINISTRAR + request.getAttribute("idRescate"));
			}else {
				log.error("No se especifico el atributo en el request 'vieneDe'.para usar esta funcion hay que modificar el codigo del action");
				return forwardErrorNonRecoverable (mapping, request, funcName, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			}
			// Seteo los valores de navegacion en el adapter
			liqConvenioCuentaVO.setValuesFromNavModel(userSession.getNavModel());
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqConvenioCuentaAdapter.NAME + ": "+ liqConvenioCuentaVO.infoString());
			
			// Si el usuario es anomimo grabamos el tramite web
			if(userSession.getIsAnonimo()){
				// Grabamos el tramite web ******************************************************************
				LiqTramiteWeb liqTramiteWeb = new LiqTramiteWeb();
				
				liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_CONSULTA_PLAN_PAGO);
				liqTramiteWeb.setIdObjeto(liqConvenioCuentaVO.getCuenta().getCodRecurso() +  
										  liqConvenioCuentaVO.getCuenta().getNroCuenta());
				
				PadServiceLocator.getVariosWebFacade().grabarTramiteWeb(liqTramiteWeb);
				// Fin tramite web **************************************************************************
			}
			
			
			// Envio el VO al request
			request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			
			userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			
			saveDemodaMessages(request, liqConvenioCuentaVO);
			
			return mapping.findForward(GdeConstants.FWD_LIQCONVENIOCUENTA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqConvenioCuentaAdapter.NAME);
		}
	}
	
	/**
	 * Volver que se ejecuta cuando este action es incluido por otro distinto al AdministrarLiqDeuda. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		UserSession userSession = getCurrentUserSession(request, mapping);
		String volverA=userSession.get("verConvenioVolverA").toString();
		userSession.remove("verConvenioVolverA");
		log.debug("volviendo a "+volverA);
		return new ActionForward (volverA);			
	}

	
	/**
	 *	Forward a la jsp liqFormConvenioImpForm 
	 *  la cual llama a ImprimirConvenio con el metod getFormPDF().
	 * 
	 * @author Cristian
	 * @throws Exception
	 */
	public ActionForward printForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqFormConvenioAdapter liqFormConvenioAdapterVO = new LiqFormConvenioAdapter(); 
		try{
		
			if (request.getParameter("selectedId") != null ){
				liqFormConvenioAdapterVO.getConvenio().setIdConvenio(new Long(request.getParameter("selectedId")));				
			}
			
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			liqFormConvenioAdapterVO.setCuentaFilter(liqCuentaFiltro);
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getConvenioFormalizado(userSession, liqFormConvenioAdapterVO);
	        
			// Parameter para el volver del ImprimirConvenio			
			if (userSession.getNavModel().getAct() != null &&
					userSession.getNavModel().getAct().equals(GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO)){
				liqFormConvenioAdapterVO.setPrevActionParameter(GdeConstants.ACTION_VER_CONVENIO_VER);				
			} else { 
				liqFormConvenioAdapterVO.setPrevActionParameter(GdeConstants.ACTION_VER_CONVENIO_VER);
			}
						
			// Subo el liqFormConvenioAdapter a la session para que lo tome el ImprimirConvenio
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward("liqFormConvenioImpForm");
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	/**
	 *	Forward a la jsp liqFormConvenioImpRecibos 
	 *  la cual llama a ImprimirConvenio getRecibosPDF.
	 * 
	 * @author Cristian
	 * @throws Exception
	 */
	public ActionForward printRecibos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_RECIBOS_CONVENIO);
		if (userSession == null) return forwardErrorSession(request);
		
		 
		try{
			
			log.debug(funcName + ": listIdCuotaSelected" +request.getParameterValues("listIdCuotaSelected"));
			
			LiqConvenioCuentaAdapter liqConvenioCuenta = (LiqConvenioCuentaAdapter) userSession.get(LiqConvenioCuentaAdapter.NAME);
			liqConvenioCuenta.clearErrorMessages();
			if (request.getParameterValues("listIdCuotaSelected") == null){
				liqConvenioCuenta.addRecoverableValueError("Debe seleccionar al menos una cuota a reimprimir.");
			}

				// Tiene errores no recuperables
			if (liqConvenioCuenta.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: : " + liqConvenioCuenta.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
			}
			
			if (liqConvenioCuenta.hasErrorRecoverable()){
				
				
				// Envio el VO al request
				request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
				
				saveDemodaErrors(request, liqConvenioCuenta);
				saveDemodaMessages(request, liqConvenioCuenta);
				
				return mapping.findForward(GdeConstants.FWD_LIQCONVENIOCUENTA_ADAPTER);
			}
			
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = new LiqFormConvenioAdapter();
			
			liqFormConvenioAdapterVO.setListIdCuotaSelected(request.getParameterValues("listIdCuotaSelected"));
			
			
			if (request.getParameter("selectedId") != null ){
				liqFormConvenioAdapterVO.getConvenio().setIdConvenio(new Long(request.getParameter("selectedId")));				
			}
	        LiqReconfeccionAdapter liqReconfAdapterVO= GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getReconfeccionCuota(userSession, liqFormConvenioAdapterVO);
	        
	        if (liqReconfAdapterVO.hasErrorRecoverable()){
	        	liqReconfAdapterVO.passErrorMessages(liqConvenioCuenta);
	        	userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
	        	request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
	        	saveDemodaErrors(request, liqConvenioCuenta);
				saveDemodaMessages(request, liqConvenioCuenta);
				
				return mapping.findForward(GdeConstants.FWD_LIQCONVENIOCUENTA_ADAPTER);
	        }
	        
	        
			// Si el usuario es anomimo grabamos el tramite web
			if(userSession.getIsAnonimo()){
				// Grabamos el tramite web ******************************************************************
				LiqTramiteWeb liqTramiteWeb = new LiqTramiteWeb();
				
				liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_RECONFECCION_CUOTA_PLAN);
				liqTramiteWeb.setIdObjeto(liqReconfAdapterVO.getCuenta().getCodRecurso() +  
										  liqReconfAdapterVO.getCuenta().getNroCuenta());
				
				PadServiceLocator.getVariosWebFacade().grabarTramiteWeb(liqTramiteWeb);
				// Fin tramite web **************************************************************************
			}
	        
	        
        	//seteo que es reconfeccion o reimpresion de cuotas
        	liqReconfAdapterVO.setEsReimpresionCuotas(true);
	        if (!liqReconfAdapterVO.isTieneDeudaVencida()){
	        	LiqReconfeccionAdapter liqReconfeccionAdapterVO = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().reconfeccionar(userSession, liqReconfAdapterVO);
				
		        // Tiene errores recuperables
				if (liqReconfeccionAdapterVO.hasErrorRecoverable()) {
					saveDemodaErrors(request, liqReconfeccionAdapterVO);
					return mapping.getInputForward();
				}
				
				// Tiene errores no recuperables
				if (liqReconfeccionAdapterVO.hasErrorNonRecoverable()) {
 
					return forwardErrorNonRecoverable(mapping, request, funcName, LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfeccionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
				userSession.put(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
				
				saveDemodaMessages(request, liqReconfeccionAdapterVO);	
				return mapping.findForward("imprimir");
	        }else{
	        	liqReconfAdapterVO.setPrevActionParameter(GdeConstants.ACTION_VER_CONVENIO_VER);

	        	request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
	        	userSession.put(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
	        	return mapping.findForward("liqReconfeccionAdapter");
	        }
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	

	/*
	 * 				Metodo Dummuy para crear archivos de transaccion.
	 * 
	 * */
	public ActionForward crearTransaccionDummy(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)return forwardErrorSession(request);
		
		try {
			
			DemodaUtil.printRequest(request);
			
			LiqConvenioCuentaAdapter liqConvenioCuentaAdapterSess = (LiqConvenioCuentaAdapter) userSession.get(LiqConvenioCuentaAdapter.NAME);
			
			liqConvenioCuentaAdapterSess.setListIdCuotaSelected(request.getParameterValues("listIdCuotaSelected"));
			
			// Llamada al servicio
			GdeServiceLocator.getFormConvenioDeudaService().crearTransaccionDummy(userSession, liqConvenioCuentaAdapterSess);
	        
			
			request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaAdapterSess);
			
			return mapping.findForward(GdeConstants.FWD_LIQCONVENIOCUENTA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqConvenioCuentaAdapter.NAME);
		}
	}
	
	public ActionForward reconfeccionar(ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		
		return mapping.findForward(GdeConstants.ACTION_RECONFECCIONAR);
	
	}
	
	public ActionForward salPorCadIndividualInit (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_GENERAR_SALDO_X_CADUCIDAD);
		log.debug("userSession " +userSession);
		if (userSession == null)return forwardErrorSession(request);
		
		LiqConvenioCuentaAdapter liqConvenioCuenta = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		
		LiqConvenioCuentaAdapter liqConvenioCuentaVO = new LiqConvenioCuentaAdapter();
		
		String selectedId = (String ) request.getParameter("selectedId");
		log.debug("SelectedID: "+ selectedId);
		liqConvenioCuentaVO.getConvenio().setIdConvenio(new Long(selectedId));
		try {

			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			liqConvenioCuentaVO.setCuentaFilter(liqCuentaFiltro);
			
			liqConvenioCuentaVO=GdeServiceLocator.getGestionDeudaService().getLiqConvenioCuentaInit(userSession, liqConvenioCuentaVO);
			log.debug("IdConvenio: "+ liqConvenioCuentaVO.getConvenio().getDesEstadoConvenio());
			
			LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO = GdeServiceLocator.getGdePlanPagoService().
						getSalPorCadAdapterForView(userSession, liqConvenioCuentaVO);
			NavModel navModel = userSession.getNavModel();
			liqConvenioSalPorCadAdapterVO.setEsAnulacion(liqConvenioCuenta.isAnulaConvenio());
				
			if(liqConvenioSalPorCadAdapterVO.hasErrorRecoverable()){
				liqConvenioSalPorCadAdapterVO.passErrorMessages(liqConvenioCuentaVO);
				request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
				userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
				saveDemodaErrors(request, liqConvenioSalPorCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			}
			
			liqConvenioSalPorCadAdapterVO.setPrevAction(navModel.getPrevAction());
			liqConvenioSalPorCadAdapterVO.setPrevActionParameter(navModel.getPrevActionParameter());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqConvenioSalPorCadAdapter.NAME + ": "+ liqConvenioCuentaVO.infoString());
			request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			
			userSession.put(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			
			if(liqConvenioSalPorCadAdapterVO.isEsAnulacion()){
				liqConvenioSalPorCadAdapterVO.addMessage(GdeError.ANULAR_CONVENIO_MSG);
				liqConvenioSalPorCadAdapterVO.addMessage(GdeError.ANULAR_CONVENIO1_MSG);
			}
			
			saveDemodaMessages(request, liqConvenioSalPorCadAdapterVO);

			return baseForward(mapping,request,funcName,GdeConstants.ACTION_VER_CONVENIO_CUENTA,GdeConstants.FWD_LIQCONVENIOSALPORCAD_ADAPTER,GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);

		} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, LiqConvenioCuentaAdapter.NAME);
		}
	}
	

	
	public ActionForward ejecutarSalPorCad (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_GENERAR_SALDO_X_CADUCIDAD);		
		if (userSession == null)return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO = (LiqConvenioSalPorCadAdapter) userSession.get(LiqConvenioSalPorCadAdapter.NAME);
		DemodaUtil.populateVO(liqConvenioSalPorCadAdapterVO, request);
		
		try{
			liqConvenioSalPorCadAdapterVO = GdeServiceLocator.getGdePlanPagoService().createSaldoPorCaducidad(userSession, liqConvenioSalPorCadAdapterVO);

			
			if (liqConvenioSalPorCadAdapterVO.hasError()) {
				log.error("error en: "  + funcName + ": " + liqConvenioSalPorCadAdapterVO.infoString());
				saveDemodaErrors(request, liqConvenioSalPorCadAdapterVO);
				userSession.put(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
				request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession,LiqConvenioSalPorCadAdapter.NAME,liqConvenioSalPorCadAdapterVO,GdeConstants.FWD_LIQCONVENIOSALPORCAD_ADAPTER);
			}
			
			request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			userSession.put(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			saveDemodaErrors(request, liqConvenioSalPorCadAdapterVO);
			
			//se indica al Message adonde volver
			liqConvenioSalPorCadAdapterVO.setPrevAction(navModel.getPrevAction());
			liqConvenioSalPorCadAdapterVO.setPrevActionParameter(navModel.getPrevActionParameter());
			liqConvenioSalPorCadAdapterVO.setSelectedId(request.getParameter("selectedId"));


			return forwardConfirmarOk(mapping, request, 
				funcName, LiqConvenioSalPorCadAdapter.NAME);
			
		}catch (Exception e){
			return baseException(mapping, request, funcName, e, LiqConvenioSalPorCadAdapter.NAME);
		}
	}
	
	public ActionForward anularConvenioInit (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		LiqConvenioCuentaAdapter liqConvenioCuenta = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		
		liqConvenioCuenta.setAnulaConvenio(true);
		
		userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
		
		return salPorCadIndividualInit(mapping, form, request, response);
	}
	
	public ActionForward vueltaAtrasSalPorCadInit (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_ATRAS_SALDO_X_CADUCIDAD);
		
		LiqConvenioCuentaAdapter liqConvenioCuentaVO = new LiqConvenioCuentaAdapter();
		
		String selectedId = (String ) request.getParameter("selectedId");
		
		liqConvenioCuentaVO.getConvenio().setIdConvenio(new Long(selectedId));
		liqConvenioCuentaVO.clearError();
		
		
		// Pasamos el filtro de la liquidacion de deuda.
		LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
		if (liqCuentaFiltro==null){
			liqCuentaFiltro = new LiqCuentaVO();
		}
		liqConvenioCuentaVO.setCuentaFilter(liqCuentaFiltro);
		
		liqConvenioCuentaVO=GdeServiceLocator.getGestionDeudaService().getLiqConvenioCuentaInit(userSession, liqConvenioCuentaVO);

		liqConvenioCuentaVO.setValuesFromNavModel(userSession.getNavModel());
		
		LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO =  GdeServiceLocator.getGdePlanPagoService().getVueltaAtrasSalPorCadForView(userSession, liqConvenioCuentaVO);
		
		if(liqConvenioSalPorCadAdapterVO.hasErrorRecoverable()){
			liqConvenioSalPorCadAdapterVO.passErrorMessages(liqConvenioCuentaVO);
			userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			saveDemodaErrors(request, liqConvenioCuentaVO);
			return forwardErrorRecoverable(mapping, request, userSession, 
					LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
		}
		//seteo la vuelta atras para que el JSP muestre bien los titles
		liqConvenioSalPorCadAdapterVO.setEsVueltaAtras(true);
		
		try {
			liqConvenioSalPorCadAdapterVO.setValuesFromNavModel(userSession.getNavModel());
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqConvenioSalPorCadAdapter.NAME + ": "+ liqConvenioCuentaVO.infoString());
			request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			
			userSession.put(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			
			saveDemodaMessages(request, liqConvenioSalPorCadAdapterVO);

			return baseForward(mapping,request,funcName,GdeConstants.ACTION_VER_CONVENIO_CUENTA,GdeConstants.FWD_LIQCONVENIOSALPORCAD_ADAPTER,GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);

		} catch (Exception exception) {
			log.error("ERROR EN ACTION ", exception);
			return baseException(mapping, request, funcName, exception, LiqConvenioCuentaAdapter.NAME);
		}
	}
	
	public ActionForward vueltaAtrasSalPorCad (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_ATRAS_SALDO_X_CADUCIDAD);
		
		if (userSession == null)return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		LiqConvenioSalPorCadAdapter liqConvenioSalPorCadAdapterVO = (LiqConvenioSalPorCadAdapter) userSession.get(LiqConvenioSalPorCadAdapter.NAME);
		DemodaUtil.populateVO(liqConvenioSalPorCadAdapterVO, request);

		try{
			liqConvenioSalPorCadAdapterVO.setValuesFromNavModel(navModel);
			liqConvenioSalPorCadAdapterVO = GdeServiceLocator.getGdePlanPagoService().createVueltaAtrasSalPorCad(userSession, liqConvenioSalPorCadAdapterVO);
					
			if (liqConvenioSalPorCadAdapterVO.hasError()) {
				log.error("error en: "  + funcName + ": " + liqConvenioSalPorCadAdapterVO.infoString());
				saveDemodaErrors(request, liqConvenioSalPorCadAdapterVO);
				userSession.put(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
				request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession,LiqConvenioSalPorCadAdapter.NAME,liqConvenioSalPorCadAdapterVO,GdeConstants.FWD_LIQCONVENIOSALPORCAD_ADAPTER);
			}

			request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			userSession.put(LiqConvenioSalPorCadAdapter.NAME, liqConvenioSalPorCadAdapterVO);
			//saveDemodaErrors(request, liqConvenioSalPorCadAdapterVO);
			return forwardConfirmarOk(mapping, request, 
				funcName, LiqConvenioSalPorCadAdapter.NAME);
			
		}catch (Exception e){
			return baseException(mapping, request, funcName, e, LiqConvenioSalPorCadAdapter.NAME);
		}
	}
	
	public ActionForward cuotaSaldoInit (ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_GENERAR_CUOTA_SALDO);
		NavModel navModel = userSession.getNavModel();
		//recupero el adapter
		LiqConvenioCuentaAdapter liqConvenioCuentaVO = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		//llamo al servicio para obtener Cuota Saldo Adapter
		LiqConvenioCuotaSaldoAdapter liqConvenioCuotaSaldoVO = GdeServiceLocator.getGdePlanPagoService().getCuotaSaldoAdapterForInit(userSession, liqConvenioCuentaVO);
		
		if (liqConvenioCuotaSaldoVO.getTieneCuotasVencidas() && userSession.getEsAnonimo()){
			liqConvenioCuotaSaldoVO.addRecoverableError(GdeError.CUOTASALDO_CUOTASVENCIDAS);
		}
		if (liqConvenioCuotaSaldoVO.hasErrorRecoverable()){
			liqConvenioCuotaSaldoVO.passErrorMessages(liqConvenioCuentaVO);
			userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			saveDemodaErrors(request, liqConvenioCuentaVO);
			saveDemodaMessages(request, liqConvenioCuentaVO);
			return forwardErrorRecoverable(mapping, request, userSession, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
		}
		liqConvenioCuotaSaldoVO.setPrevAction(navModel.getPrevAction());
		liqConvenioCuotaSaldoVO.setPrevActionParameter(navModel.getPrevActionParameter());
		userSession.put(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO);
		request.setAttribute(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO);
		
		return baseForwardAdapter(mapping, request, funcName, GdeConstants.FWD_LIQCONVENIOCUOTASALDO_ADAPTER , GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);
	}
	
	public ActionForward generarCuotaSaldo (ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_GENERAR_CUOTA_SALDO);
		LiqConvenioCuotaSaldoAdapter liqConvenioCuotaSaldoVO = (LiqConvenioCuotaSaldoAdapter)userSession.get(LiqConvenioCuotaSaldoAdapter.NAME);
		DemodaUtil.populateVO(liqConvenioCuotaSaldoVO, request);
		// si el usuario no era CMD no ingreso cuota desde, para eso la seteo desde la cuota 1
		if (userSession.getEsAnonimo()){
			liqConvenioCuotaSaldoVO.setCuotaDesde(1);
		}
		
		LiqReconfeccionAdapter liqReconfeccionAdapter = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getCuotaSaldo(userSession, liqConvenioCuotaSaldoVO);
		if (liqReconfeccionAdapter.hasErrorRecoverable()){
			liqReconfeccionAdapter.passErrorMessages(liqConvenioCuotaSaldoVO);
		}
		if (liqConvenioCuotaSaldoVO.hasErrorRecoverable()){
			userSession.put(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO);
			request.setAttribute(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO);
			saveDemodaErrors(request, liqConvenioCuotaSaldoVO);
			saveDemodaMessages(request, liqConvenioCuotaSaldoVO);
			return forwardErrorRecoverable(mapping, request, userSession, LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO,GdeConstants.FWD_LIQCONVENIOCUOTASALDO_ADAPTER);
		}
		
		request.setAttribute(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO);
    	userSession.put(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldoVO);
		
		
		request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapter);
    	userSession.put(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapter);
    	return mapping.findForward("reconfeccionar");
		
	}
	
	public ActionForward aplicarPagoACuentaInit (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en "+funcName);
		
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_APLICAR_PAGO_ACUENTA);
		LiqConvenioCuentaAdapter liqConvenioCuentaVO = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		liqConvenioCuentaVO.setValuesFromNavModel(userSession.getNavModel());
		LiqConvenioPagoCuentaAdapter liqConvenioPagoCuentaVO = GdeServiceLocator.getGdePlanPagoService().getPagoCuentaAdapterForInit(userSession, liqConvenioCuentaVO);
		
		if (liqConvenioPagoCuentaVO.hasError()){
			liqConvenioPagoCuentaVO.passErrorMessages(liqConvenioCuentaVO);
			userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			saveDemodaErrors(request, liqConvenioCuentaVO);
			saveDemodaMessages(request,liqConvenioCuentaVO);
			return forwardErrorRecoverable(mapping, request, userSession, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
		}
		
		userSession.put(LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO);
		request.setAttribute(LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO);
		
		return baseForward(mapping,request,funcName,GdeConstants.ACTION_VER_CONVENIO_CUENTA,GdeConstants.FWD_PAGOACUENTAADAPTER,GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);

	}
	
	public ActionForward aplicarPagoACuenta (ActionMapping mapping, ActionForm form,HttpServletRequest request, HttpServletResponse response)
		throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug("entrando en "+funcName);
		
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_APLICAR_PAGO_ACUENTA);
		NavModel navModel = userSession.getNavModel();
		LiqConvenioPagoCuentaAdapter liqConvenioPagoCuentaVO = (LiqConvenioPagoCuentaAdapter)userSession.get(LiqConvenioPagoCuentaAdapter.NAME);
		try{
			liqConvenioPagoCuentaVO = GdeServiceLocator.getGdePlanPagoService().createAplicarPagoCuenta(userSession, liqConvenioPagoCuentaVO);
			
			if (liqConvenioPagoCuentaVO.hasError()){
				userSession.put(LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO);
				request.setAttribute(LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO);
				saveDemodaErrors(request, liqConvenioPagoCuentaVO);
				saveDemodaMessages(request,liqConvenioPagoCuentaVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO,GdeConstants.FWD_PAGOACUENTAADAPTER);
			}
			
			request.setAttribute(LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO);
			userSession.put(LiqConvenioPagoCuentaAdapter.NAME, liqConvenioPagoCuentaVO);
			saveDemodaErrors(request, liqConvenioPagoCuentaVO);
			//se indica al Message adonde volver
			liqConvenioPagoCuentaVO.setPrevAction(navModel.getPrevAction());
			liqConvenioPagoCuentaVO.setPrevActionParameter(navModel.getPrevActionParameter());
			liqConvenioPagoCuentaVO.setSelectedId(request.getParameter("selectedId"));
			return forwardConfirmarOk(mapping, request, 
					funcName, LiqConvenioPagoCuentaAdapter.NAME);
			
			
		}catch (Exception e){
			return baseException(mapping, request, funcName, e, LiqConvenioPagoCuentaAdapter.NAME);
		}
	}
	
	/**
	 * Validacion de Caso para el saldo por caducidad 
	 * 
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqConvenioSalPorCadAdapter adapterVO = (LiqConvenioSalPorCadAdapter)userSession.get(LiqConvenioSalPorCadAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				RescateIndividualAdapter adapterVO1 = (RescateIndividualAdapter)userSession.get(RescateIndividualAdapter.NAME);
				DemodaUtil.populateVO(adapterVO1, request);
				
				log.debug( funcName + " " + adapterVO1.getCasoContainer().getCaso().infoString());
				
				// llamada al servicio
				CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO1.getCasoContainer()); 
				
				adapterVO1.getCasoContainer().passErrorMessages(adapterVO1);
			    
			    saveDemodaMessages(request, adapterVO1);
			    saveDemodaErrors(request, adapterVO1);
			    
				request.setAttribute(RescateIndividualAdapter.NAME, adapterVO1);
				
				return mapping.findForward( GdeConstants.FWD_RESCATEINDIV_INIT);
				//log.error("error en: "  + funcName + ": " + LiqConvenioSalPorCadAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				//return forwardErrorSessionNullObject(mapping, request, funcName, LiqConvenioSalPorCadAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCasoContainer().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCasoContainer()); 
			
			adapterVO.getCasoContainer().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(LiqConvenioSalPorCadAdapter.NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_LIQCONVENIOSALPORCAD_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqConvenioSalPorCadAdapter.NAME);
		}	
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
	
	public ActionForward verPagosConvenio(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		LiqConvenioCuentaAdapter liqConvenioCuentaAdapter = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		VerPagosConvenioAdapter verPagosAdapter = new VerPagosConvenioAdapter();
		try{
			verPagosAdapter = GdeServiceLocator.getGdePlanPagoService().getPagosConvenio(liqConvenioCuentaAdapter, userSession);
			
			if (verPagosAdapter.hasError()){
				verPagosAdapter.passErrorMessages(liqConvenioCuentaAdapter);
				userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaAdapter);
				request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaAdapter);
				saveDemodaErrors(request, liqConvenioCuentaAdapter);
				return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_CUENTA);
			}
			
			userSession.put(VerPagosConvenioAdapter.NAME, verPagosAdapter);
			request.setAttribute(VerPagosConvenioAdapter.NAME, verPagosAdapter);
			
			return mapping.findForward(GdeConstants.FWD_VERPAGOSCONVENIOS);
			
		} catch (Exception exception){
			verPagosAdapter.passErrorMessages(liqConvenioCuentaAdapter);
			userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaAdapter);
			request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaAdapter);
			saveDemodaErrors(request, liqConvenioCuentaAdapter);
			return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_CUENTA);
		}
	}
	
	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = LiqConvenioCuentaAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			LiqConvenioCuentaAdapter liqConvenioCuentaAdapterVO = (LiqConvenioCuentaAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (liqConvenioCuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, LiqConvenioCuentaAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			liqConvenioCuentaAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			liqConvenioCuentaAdapterVO = GdeServiceLocator.getGestionDeudaService().imprimirLiqConvenioCuenta(userSession, liqConvenioCuentaAdapterVO);

			// limpia la lista de reports y la lista de tablas
			liqConvenioCuentaAdapterVO.getReport().clear();
			
            // Tiene errores recuperables
			if (liqConvenioCuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqConvenioCuentaAdapterVO.infoString());
				saveDemodaErrors(request, liqConvenioCuentaAdapterVO);				
				request.setAttribute(name, liqConvenioCuentaAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (liqConvenioCuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqConvenioCuentaAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, liqConvenioCuentaAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = liqConvenioCuentaAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward verHistorial (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		LiqConvenioCuentaAdapter liqConvenioCuenta = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		
		try{
			ConvenioEstadosAdapter convenioEstados = GdeServiceLocator.getGdePlanPagoService().getConvenioEstados(userSession, liqConvenioCuenta.getConvenio());
			if (convenioEstados.hasError()){
				convenioEstados.passErrorMessages(liqConvenioCuenta);
				userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
				request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
				saveDemodaErrors(request, liqConvenioCuenta);
				forwardErrorRecoverable(mapping, request, userSession, LiqConvenioCuentaAdapter.NAME, liqConvenioCuenta);
			}
			
			userSession.put(ConvenioEstadosAdapter.NAME, convenioEstados);
			request.setAttribute(ConvenioEstadosAdapter.NAME, convenioEstados);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIOESTADOSADAPTER);
		}catch (Exception exception){
			
			log.error("ocurrio un error en un action: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}
	}
	
	public ActionForward rescateIndividualInit (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_GENERAR_RESCATE_IND);
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		LiqConvenioCuentaAdapter liqConvenioCuentaVO = (LiqConvenioCuentaAdapter)userSession.get(LiqConvenioCuentaAdapter.NAME);
		
		try{
			RescateIndividualAdapter rescateIndAdapter = GdeServiceLocator.getGdePlanPagoService().getRescateIndAdapterForInit(userSession, liqConvenioCuentaVO);
			if (rescateIndAdapter.hasErrorRecoverable()){
				rescateIndAdapter.passErrorMessages(liqConvenioCuentaVO);
				userSession.put(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
				request.setAttribute(LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
				saveDemodaErrors(request, liqConvenioCuentaVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqConvenioCuentaAdapter.NAME, liqConvenioCuentaVO);
			}
			userSession.put(RescateIndividualAdapter.NAME, rescateIndAdapter);
			request.setAttribute(RescateIndividualAdapter.NAME, rescateIndAdapter);
			rescateIndAdapter.setPrevAction(navModel.getPrevAction());
			rescateIndAdapter.setPrevActionParameter(navModel.getPrevActionParameter());
			
			
			return baseForward(mapping, request, funcName, GdeConstants.ACTION_VER_CONVENIO_CUENTA, GdeConstants.FWD_RESCATEINDIV_INIT, GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);
			
		}catch (Exception exception){
			log.error("ocurrio un error en un action: "+funcName,exception);
			throw new DemodaServiceException(exception);
		}
		
		
	}
	
	public ActionForward createRescateIndividual (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		UserSession userSession = getCurrentUserSession(request, mapping);
		NavModel navModel = userSession.getNavModel();
		RescateIndividualAdapter rescateIndAdapter = (RescateIndividualAdapter)userSession.get(RescateIndividualAdapter.NAME);
		
		try{
			DemodaUtil.populateVO(rescateIndAdapter, request);
			rescateIndAdapter = GdeServiceLocator.getGdePlanPagoService().createRescateIndividual(userSession, rescateIndAdapter);
			
			if (rescateIndAdapter.hasErrorRecoverable()){
				userSession.put(RescateIndividualAdapter.NAME, rescateIndAdapter);
				request.setAttribute(RescateIndividualAdapter.NAME, rescateIndAdapter);
				saveDemodaErrors(request, rescateIndAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, RescateIndividualAdapter.NAME, rescateIndAdapter, GdeConstants.FWD_RESCATEINDIV_INIT);
			}
			
			rescateIndAdapter.setPrevAction(navModel.getPrevAction());
			rescateIndAdapter.setPrevActionParameter(navModel.getPrevActionParameter());
			rescateIndAdapter.setSelectedId(request.getParameter("selectedId"));
			log.debug("PREV ACTION "+rescateIndAdapter.getPrevAction());
			log.debug("PREV ACTION PARAM: "+rescateIndAdapter.getPrevActionParameter());
			userSession.put(RescateIndividualAdapter.NAME, rescateIndAdapter);
			request.setAttribute(RescateIndividualAdapter.NAME, rescateIndAdapter);
			
			return forwardConfirmarOk(mapping, request, 
					funcName, RescateIndividualAdapter.NAME);
			
		}catch (Exception exception){
			log.error("ocurrio un error en un Action: "+ funcName, exception);
			throw new DemodaServiceException(exception);
		}
	}
	
	public ActionForward verificarConsistencia (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		try{
						
			NavModel navModel = userSession.getNavModel();
			
			CommonKey selectedId = new CommonKey (navModel.getSelectedId());
			
			ConvenioConsistenciaAdapter convConsistAdapter = GdeServiceLocator.getGdePlanPagoService().getConvenioConsistenciaAdapter(userSession, selectedId);
			
			userSession.put(ConvenioConsistenciaAdapter.NAME, convConsistAdapter);
			request.setAttribute(ConvenioConsistenciaAdapter.NAME, convConsistAdapter);
			//saveDemodaMessages(request, convConsistAdapter);
			
			return baseForward(mapping, request, funcName, GdeConstants.ACTION_VER_CONVENIO_CUENTA, GdeConstants.FWD_CONVENIO_CONSIST, GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);
			
			
		}catch (Exception exception){
			log.error("ocurrio un error en un Action: "+ funcName, exception);
			throw new DemodaServiceException(exception);
		}
		
		
	}
	
	public ActionForward createMoverDeuda (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		try{
						
			NavModel navModel = userSession.getNavModel();
			
			CommonKey selectedId = new CommonKey (navModel.getSelectedId());
			
			ConvenioConsistenciaAdapter convConsistAdapter = GdeServiceLocator.getGdePlanPagoService().createMoverDeuda(userSession, selectedId);
			
			userSession.put(ConvenioConsistenciaAdapter.NAME, convConsistAdapter);
			request.setAttribute(ConvenioConsistenciaAdapter.NAME, convConsistAdapter);
			//saveDemodaMessages(request, convConsistAdapter);
			
			return baseForward(mapping, request, funcName, GdeConstants.ACTION_VER_CONVENIO_CUENTA, GdeConstants.FWD_CONVENIO_CONSIST, GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO);
			
			
		}catch (Exception exception){
			log.error("ocurrio un error en un Action: "+ funcName, exception);
			throw new DemodaServiceException(exception);
		}
		
		
	}

}