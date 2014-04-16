//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

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
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDeudaCyqDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaCyqDAction.class);
	
	public static String PROCEDIMIENTO_KEY = "procedimientoKey";
	
	/**
	 * Metodo iniciliziza para el grupo de usuarios perntenecientes a Gestion de Recurso.
	 * 
	 * @throws Exception
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ADM_DEUDA_CYQ, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDeudaAdapter liqDeudaAdapterVO = null;
		String stringServicio = "getLiqDeudaAdapterForCyqInit()";
		try {
			
			CommonKey procedimientoKey = (CommonKey) userSession.getNavModel().getParameter(AdministrarDeudaCyqDAction.PROCEDIMIENTO_KEY);
			
			liqDeudaAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getLiqDeudaAdapterForCyqInit(userSession, procedimientoKey);
						
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_CUENTA_SEARCH);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Validacion de periodos de deuda.
	 * Busca la cuenta por recurso y numero de cuenta.
	 * 
	 * @throws Exception
	 */
	public ActionForward validar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		//UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ADM_DEUDA_CYQ, CyqSecurityConstants.MTD_AGREGAR_DEUDA_ADMIN);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "validarCuentaEnvioCyQ()";
		try {
			
			// Bajo el charango de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
						
			liqDeudaAdapterVO.clearError();
			
			String numeroCuenta = "";
			Long idRecurso = null;
						
			// Recuperamos datos del request en el vo		
			log.debug(funcName + " numeroCuenta: " + request.getParameter("numeroCuenta"));
			log.debug(funcName + " idRecurso: " + request.getParameter("idRecurso"));
			
			if (request.getParameter("numeroCuenta") != null ){
				numeroCuenta = request.getParameter("numeroCuenta");
			}
			
			if ( request.getParameter("idRecurso") != null && request.getParameter("idRecurso") != "-1"){
				idRecurso = new Long(request.getParameter("idRecurso"));
			}

			liqDeudaAdapterVO.setNumeroCuenta(numeroCuenta);
			liqDeudaAdapterVO.setIdRecurso(idRecurso);
						
			// Validamos requeridos y existencia de cuenta.
			liqDeudaAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().validarCuentaEnvioCyQ(userSession, liqDeudaAdapterVO);
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				liqDeudaAdapterVO.setNumeroCuenta(numeroCuenta);
				liqDeudaAdapterVO.setIdRecurso(idRecurso);
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				
				return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_CUENTA_SEARCH);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			liqDeudaAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().validarDeudaEnvioCyQ(userSession, liqDeudaAdapterVO);	
						
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaErrors(request, liqDeudaAdapterVO);
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_MSG_VALIDAR_DEDUDA_CYQ_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}

	
	public ActionForward continuar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		//UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ADM_DEUDA_CYQ, CyqSecurityConstants.MTD_AGREGAR_DEUDA_ADMIN);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapterForEnvioCyq()";
		try {
			
			// Bajo el charango de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
						
			liqDeudaAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getLiqDeudaAdapterForEnvioCyq(userSession, liqDeudaAdapterVO);	
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				
				return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_CUENTA_SEARCH);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
						
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaErrors(request, liqDeudaAdapterVO);
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 *  Captura la seleccion de deuda submitida y forwardea al jsp para confirmar el envio a cyq
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ADM_DEUDA_CYQ, CyqSecurityConstants.MTD_AGREGAR_DEUDA_ADMIN);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapter4EnvioDeudaCyq()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			// Bajo el cachorro de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			log.debug(funcName + " Session NroCuenta: " + liqDeudaAdapterVO.getNumeroCuenta());
			log.debug(funcName + " Session idRecurso: " + liqDeudaAdapterVO.getIdRecurso());
			log.debug(funcName + " listIdDeudaSelected: " + request.getParameterValues("listIdDeudaSelected"));
			
			liqDeudaAdapterVO.clearError();
			
			/* 
			 * 
			 *  TODO: Quitar comentario cuando se implenete cyq completamente
			
			if (liqDeudaAdapterVO.isSeleccionarDeuda4Cyq()){
				
				liqDeudaAdapterVO.setContinuaGesViaOri(SiNo.getById(new Integer(request.getParameter("continuaGesViaOri.id"))));
				
				if (liqDeudaAdapterVO.getContinuaGesViaOri().getBussId() == null){
					liqDeudaAdapterVO.addRecoverableValueError("Desea que las cobranzas puedan seguir gestionando la deuda de esta cuenta");
				}				
			}
			
			*/
			
			if (request.getParameterValues("listIdDeudaSelected") != null) {
			
				// cargo los ids de deuda seleccionadas.
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
					
				liqDeudaAdapterVO.setListIdDeudaSelected(listIdDeudaSelected);
				
				if (listIdDeudaSelected != null){
					for (int i=0; i < listIdDeudaSelected.length; i++){
						log.debug(funcName + " idDeuda Selected=" + listIdDeudaSelected[i]);
					}
				}
			
			} else {
				liqDeudaAdapterVO.addRecoverableValueError("Debe seleccionar al menos un registro de deuda a enviar");
			}
						
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				saveDemodaMessages(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_ADAPTER);
			}
						
			// Llamo al Service de Cyq que haga el init de la lista de procedimientos.
			liqDeudaAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().enviarDeudaCyq(userSession, liqDeudaAdapterVO);
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			userSession.getNavModel().setConfAction(CyqConstants.PATH_ADMINISTRAR_PROCEDIMIENTO);
			userSession.getNavModel().setConfActionParameter(BaseConstants.ACT_REFILL);
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			userSession.remove(LiqDeudaAdapter.NAME);
			
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward seleccionarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ADM_DEUDA_CYQ, funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapter4EnvioDeudaCyq()";
		try {
			
			// Bajo el cachorro de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			log.debug(funcName + " Session idCuenta: " + liqDeudaAdapterVO.getIdCuenta());
			log.debug(funcName + " Session NroCuenta: " + liqDeudaAdapterVO.getNumeroCuenta());
			log.debug(funcName + " Session idRecurso: " + liqDeudaAdapterVO.getIdRecurso());
			
			liqDeudaAdapterVO.clearErrorMessages();
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_ADAPTER);
			}
						
			// Llamo al Service de Cyq que haga el init de la lista de procedimientos.
			liqDeudaAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().agregarCuentaNoDeu(userSession, liqDeudaAdapterVO);
			
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(CyqConstants.FWD_DEDUDA_CYQ_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			userSession.getNavModel().setConfAction(CyqConstants.PATH_ADMINISTRAR_PROCEDIMIENTO);
			userSession.getNavModel().setConfActionParameter(BaseConstants.ACT_REFILL);
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			userSession.remove(LiqDeudaAdapter.NAME);
			
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return mapping.findForward(CyqConstants.ACTION_ADMINISTRAR_PROCEDIMIENTO);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, LiqDeudaAdapter.NAME);
		
	}

}