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
import ar.gov.rosario.siat.gde.iface.model.CambioPlanCDMAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.BrocheAdapter;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarCambioPlanCDMDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCambioPlanCDMDAction.class);
	
	/**
	 * Permite ver el detalle de un registro de Deuda con 
	 * sus conceptos y pagos asociados.
	 * 
	 * @author Cristian
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward cambioPlanCDM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "validarDeudaCDMVencida()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			CommonKey cuentaKey = new CommonKey(request.getParameter("selectedId"));
			
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = RecServiceLocator.getCdmService().validarDeudaCDMNoVencida(userSession, cuentaKey);
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			}
			
			// tiene errores recuperables
			if (cambioPlanCDMAdapter.hasErrorRecoverable()) {
				log.debug("ErrorRecoverable encontrado...");
				userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				return this.volverACuenta(mapping, form, request, response);
			}
			
			// Si no posee deuda no vencida
			if (!cambioPlanCDMAdapter.isPoseeDeudaNoVencida()){
				cambioPlanCDMAdapter.addMessage(GdeError.MSG_DEUDA_NO_VENCIDA);
				userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				log.debug("No posee deuda no vencida");
				return this.volverACuenta(mapping, form, request, response);
			}
			
			cambioPlanCDMAdapter = RecServiceLocator.getCdmService().validarDeudaCDMVencida(userSession, cuentaKey);
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			}
			
			// Si no posee deuda vencida
			if (!cambioPlanCDMAdapter.isPoseeDeudaVencida()){
				return this.continuarCambioPlan(mapping, form, request, response);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapter.infoString());
			
			cambioPlanCDMAdapter.addMessage(GdeError.MSG_DEUDA_VENCIDA);
			saveDemodaMessages(request, cambioPlanCDMAdapter);
			
			// Envio el VO al request
			request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			
			return mapping.findForward(GdeConstants.FWD_DEUDACDMVENCIDA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	/**
	 * Continua con el cambio de plan despues de la pantalla de visualizacion de deuda vencida que pueda existir. 
	 * 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward continuarCambioPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CAMBIOPLAN_CDM);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getCambioPlanCDMAdapterInit()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			CommonKey cuentaKey = new CommonKey(request.getParameter("selectedId"));
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = RecServiceLocator.getCdmService().getCambioPlanCDMAdapterInit(
					userSession, cuentaKey);
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapter.infoString());
			
			
			if (!cambioPlanCDMAdapter.getPoseeFormaPagoSeleccionable()){
				
				cambioPlanCDMAdapter.addMessageValue("No Existen planes seleccionables para realizar el cambio.");
				
			}
			
			// Envio el VO al request
			request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			
			saveDemodaMessages(request, cambioPlanCDMAdapter);
			
			return mapping.findForward(GdeConstants.FWD_CAMBIOPLANCDM_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}

	/**
	 * Realiza el cambio de plan y forwardea a la pantalla de impresion. 
	 * 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward cambiarPlanCDM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CAMBIOPLAN_CDM);
		if (userSession == null) return forwardErrorSession(request);
	
		try {
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
			
			cambioPlanCDMAdapter.clearError();
			
			log.debug(funcName + " idPlan: " + request.getParameter("idPlan"));
			
			if (request.getParameter("idPlan") != null){
				cambioPlanCDMAdapter.setIdPlanSelected(new Long(request.getParameter("idPlan")));
			} else {
				cambioPlanCDMAdapter.addRecoverableError(GdeError.MSG_PLANCDM_REQUERIDO);
			}
		
			if (cambioPlanCDMAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapter.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapter);
				request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				
				return mapping.findForward(GdeConstants.FWD_CAMBIOPLANCDM_ADAPTER);
			}
			
			// Llamamos al servicio para hacer el cambio de plan
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = RecServiceLocator.getCdmService().cambiarPlanCDM(
					userSession, cambioPlanCDMAdapter);
			
			if (cambioPlanCDMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapterVO.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapterVO);
				request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_CAMBIOPLANCDM_ADAPTER);
			}
			
			userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			
			NavModel navModel = userSession.getNavModel();
			
			//le seteo la accion a donde ir al navModel
			navModel.setConfAction("/gde/AdministrarCambioPlanCDM");
			navModel.setConfActionParameter("getPrintAdapter");
			
			// Me dirije al mensaje de confirmacion OK
			return this.forwardMessage(mapping, navModel, 
				NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	
	public ActionForward getPrintAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CAMBIOPLAN_CDM);
		if (userSession == null) return forwardErrorSession(request);
	
		try {
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter;
			
			if (request.getParameter("id") != null){
				cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
				cambioPlanCDMAdapter.getCuenta().setIdCuenta(new Long(request.getParameter("id")));
			} else {
				cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
			}
			
			if (cambioPlanCDMAdapter == null) {
				log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
			}
			
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = RecServiceLocator.getCdmService().getUltimoCambioPlan(userSession, 
					cambioPlanCDMAdapter);
			
			userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			
			request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_CAMBIOPLANCDM_PRINT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	
	public ActionForward printRecibo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CAMBIOPLAN_CDM);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);		
		cambioPlanCDMAdapter.setPrevActionParameter(GdeConstants.ACTION_CAMBIO_PLAN_PRINT_ADAPTER);
		
		return mapping.findForward(GdeConstants.FWD_CAMBIOPLANCDM_IMP_RECIBOS);		
	}
	
	
	
	public ActionForward printForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CAMBIOPLAN_CDM);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);		
		cambioPlanCDMAdapter.setPrevActionParameter(GdeConstants.ACTION_CAMBIO_PLAN_PRINT_ADAPTER);
		
		return mapping.findForward(GdeConstants.FWD_CAMBIOPLANCDM_IMP_FORM);		
	}
	
	
	public ActionForward getReciboPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPrintForm";
		try {
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print = RecServiceLocator.getCdmService().getPrintRecibo(userSession, cambioPlanCDMAdapterVO);
			
	        // Tiene errores recuperables
			if (cambioPlanCDMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapterVO.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					CambioPlanCDMAdapter.NAME);
		}
	}
	
	
	public ActionForward getFormPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPrintForm";
		try {
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print = RecServiceLocator.getCdmService().getPrintForm(userSession, cambioPlanCDMAdapterVO);
			
	        // Tiene errores recuperables
			if (cambioPlanCDMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapterVO.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					CambioPlanCDMAdapter.NAME);
		}
	}
	
	/**
	 * Utilizado para volver desde el el detalle de una Deuda 
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
			
			Long selectedId = null;
						
			if (StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
				CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
				if (cambioPlanCDMAdapter == null) return forwardErrorSession(request);
				selectedId = cambioPlanCDMAdapter.getCuenta().getIdCuenta();
			} else {
				selectedId = new Long(request.getParameter("selectedId"));
			}
			
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}		
	}
	
	public ActionForward cuotaSaldoCDM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "validarDeudaCDMVencida()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			CommonKey cuentaKey = new CommonKey(request.getParameter("selectedId"));
			
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = RecServiceLocator.getCdmService().validarDeudaCDMNoVencida(
					userSession, cuentaKey);
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			}
			
			// Si no posee deuda no vencida
			if (!cambioPlanCDMAdapter.isPoseeDeudaNoVencida()){
				cambioPlanCDMAdapter.addMessage(GdeError.MSG_DEUDA_NO_VENCIDA);
				userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				log.debug("No posee deuda no vencida");
				return this.volverACuenta(mapping, form, request, response);
				
			}
			
			cambioPlanCDMAdapter = RecServiceLocator.getCdmService().validarDeudaCDMVencida(
					userSession, cuentaKey);
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			}
			
			// Si no posee deuda vencida
			if (!cambioPlanCDMAdapter.isPoseeDeudaVencida()){
				return this.continuarCuotaSaldo(mapping, form, request, response);
			}
			
			// Si el usuario es anonimo y posee deuda vencida, no puede continuar.
			if (userSession.getEsAnonimo()){
				cambioPlanCDMAdapter.addMessage(GdeError.MSG_CUOTASALDO_CDM_DEUDA_VENCIDA_USRANONIMO);
				userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				log.debug("Si el usuario es anonimo y posee deuda vencida, no puede continuar.");
				return this.volverACuenta(mapping, form, request, response);
			}
			
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapter.infoString());
			
			cambioPlanCDMAdapter.addMessage(GdeError.MSG_DEUDA_VENCIDA);
			saveDemodaMessages(request, cambioPlanCDMAdapter);
			
			cambioPlanCDMAdapter.setEsCuotaSaldo(true);
			
			// Envio el VO al request
			request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			
			return mapping.findForward(GdeConstants.FWD_DEUDACDMVENCIDA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	public ActionForward continuarCuotaSaldo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CUOTASALDO_CDM);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getCuotaSaldoCDMAdapterInit()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			CommonKey cuentaKey = new CommonKey(request.getParameter("selectedId"));
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = RecServiceLocator.getCdmService().getCuotaSaldoCDMAdapterInit(
					userSession, cuentaKey);
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapter.infoString());

			// tiene errores recuperables
			if (cambioPlanCDMAdapter.hasErrorRecoverable()) {
				userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				log.debug("tiene errores recuperables");
				return this.volverACuenta(mapping, form, request, response);
			}
			
			cambioPlanCDMAdapter.addMessage(GdeError.MSG_CUOTASALDO_CDM_ANULACION_DEUDA);
			saveDemodaMessages(request, cambioPlanCDMAdapter);
			
			// Envio el VO al request
			request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
			
			
			return mapping.findForward(GdeConstants.FWD_CUOTASALDOCDM_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	
	public ActionForward generarCuotaSaldoCDM(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CUOTASALDO_CDM);
		if (userSession == null) return forwardErrorSession(request);
	
		try {
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
			
			cambioPlanCDMAdapter.clearError();
			
			if (cambioPlanCDMAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapter.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapter);
				request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapter);
				
				return mapping.findForward(GdeConstants.FWD_CUOTASALDOCDM_ADAPTER);
			}
			
			// Llamamos al servicio para hacer el cambio de plan
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = RecServiceLocator.getCdmService().generarCuotaSaldoCDM(
					userSession, cambioPlanCDMAdapter);
			
			if (cambioPlanCDMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapterVO.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapterVO);
				request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_CUOTASALDOCDM_ADAPTER);
			}
			
			userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			
			NavModel navModel = userSession.getNavModel();
			
			//le seteo la accion a donde ir al navModel
			navModel.setConfAction("/gde/AdministrarCambioPlanCDM");
			navModel.setConfActionParameter("getCuotaSaldoPrintAdapter");
			
			// Me dirije al mensaje de confirmacion OK
			return this.forwardMessage(mapping, navModel, 
				NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	
	public ActionForward getCuotaSaldoPrintAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CUOTASALDO_CDM);
		if (userSession == null) return forwardErrorSession(request);
	
		try {
			
			CambioPlanCDMAdapter cambioPlanCDMAdapter;
			
			if (request.getParameter("id") != null){
				cambioPlanCDMAdapter = new CambioPlanCDMAdapter();
				cambioPlanCDMAdapter.getCuenta().setIdCuenta(new Long(request.getParameter("id")));
			} else {
				cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
			}
			
			if (cambioPlanCDMAdapter == null) {
				log.error("error en: "  + funcName + ": " + BrocheAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BrocheAdapter.NAME); 
			}
			
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = RecServiceLocator.getCdmService().getUltimaCuotaSaldo(userSession, 
					cambioPlanCDMAdapter);
			
			userSession.put(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			
			request.setAttribute(CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_CUOTASALDOCDM_PRINT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CambioPlanCDMAdapter.NAME);
		}
	}
	
	
	
	public ActionForward printReciboCuotaSaldo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_CUOTASALDO_CDM);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		CambioPlanCDMAdapter cambioPlanCDMAdapter = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);		
		cambioPlanCDMAdapter.setPrevActionParameter(GdeConstants.ACTION_CUOTASALDOCDM_PRINT_ADAPTER);
		
		return mapping.findForward(GdeConstants.FWD_CUOTASALDOCDM_IMP_RECIBOS);		
	}
	
	
	public ActionForward getReciboCuotaSaldoPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_CUOTASALDO_CDM);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPrintForm";
		try {
			CambioPlanCDMAdapter cambioPlanCDMAdapterVO = (CambioPlanCDMAdapter) userSession.get(CambioPlanCDMAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print = RecServiceLocator.getCdmService().getPrintReciboCuotaSaldo(userSession, cambioPlanCDMAdapterVO);
			
	        // Tiene errores recuperables
			if (cambioPlanCDMAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cambioPlanCDMAdapterVO.infoString()); 
				saveDemodaErrors(request, cambioPlanCDMAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (cambioPlanCDMAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cambioPlanCDMAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CambioPlanCDMAdapter.NAME, cambioPlanCDMAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CambioPlanCDMAdapter.NAME + ": "+ cambioPlanCDMAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					CambioPlanCDMAdapter.NAME);
		}
	}
	

}