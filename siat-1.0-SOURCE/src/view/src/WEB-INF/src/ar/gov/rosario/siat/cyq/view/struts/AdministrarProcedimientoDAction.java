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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarProcedimientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcedimientoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcedimientoAdapter procedimientoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProcedimientoAdapterForView(userSession, commonKey)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProcedimientoAdapterForUpdate(userSession, commonKey)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProcedimientoAdapterForDelete(userSession, commonKey)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForView
					(userSession, commonKey);
				procedimientoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CyqError.PROCEDIMIENTO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getProcedimientoAdapterForView(userSession)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForView
					(userSession, commonKey);
				procedimientoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, CyqError.PROCEDIMIENTO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getProcedimientoAdapterForView(userSession)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForView
					(userSession, commonKey);
				procedimientoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, CyqError.PROCEDIMIENTO_LABEL);			
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.NAME, procedimientoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procedimientoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ProcedimientoAdapter.NAME + ": " + procedimientoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.NAME, procedimientoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcedimientoAdapter.NAME, procedimientoAdapterVO);
			
			saveDemodaMessages(request, procedimientoAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			CyqConstants.ACTION_ADMINISTRAR_ENC_PROCEDIMIENTO, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward baja(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			CyqConstants.ACTION_ADMINISTRAR_BAJA_PROCEDIMIENTO, CyqConstants.ACT_BAJA);

	}

	public ActionForward conversion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			CyqConstants.ACTION_ADMINISTRAR_CONVERSION_PROCEDIMIENTO, CyqConstants.ACT_CONVERSION);

	}
	
	public ActionForward informe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			CyqConstants.ACTION_ADMINISTRAR_INFORME_PROCEDIMIENTO, CyqConstants.ACT_INFORME);

	}
	
	public ActionForward agregarDeudaAdmin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProcedimientoAdapter procedimientoAdapter = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			CommonKey procedimientoKey = new CommonKey(procedimientoAdapter.getProcedimiento().getId());
			
			userSession.getNavModel().putParameter(AdministrarDeudaCyqDAction.PROCEDIMIENTO_KEY, procedimientoKey);
			
			return baseForwardAdapter(mapping, request, funcName, CyqConstants.ACTION_BUSCAR_DEUDA, BaseConstants.ACT_INICIALIZAR);
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	public ActionForward quitarDeudaAdmin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapter4EnvioDeudaCyq()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			procedimientoAdapterVO.clearErrorMessages();
			
			if (request.getParameterValues("listIdDeudaSelected") != null) {
			
				// cargo los ids de deuda seleccionadas.
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
					
				procedimientoAdapterVO.setListIdDeudaSelected(listIdDeudaSelected);
				
				if (listIdDeudaSelected != null){
					for (int i=0; i < listIdDeudaSelected.length; i++){
						log.debug(funcName + " idDeuda Selected=" + listIdDeudaSelected[i]);
					}
				}
			
			} else {
				procedimientoAdapterVO.addRecoverableValueError("Debe seleccionar al menos un registro de deuda a quitar");
			}
			
			// Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				request.setAttribute(ProcedimientoAdapter.NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ADAPTER);
			}
						
			// Llamo al Service de Cyq que haga el init de la lista de procedimientos.
			procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().quitarDeudaCyq(userSession, procedimientoAdapterVO);
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procedimientoAdapterVO.errorString());
				
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.NAME, procedimientoAdapterVO);
			}
			
			userSession.getNavModel().setConfAction(CyqConstants.PATH_ADMINISTRAR_PROCEDIMIENTO);
			userSession.getNavModel().setConfActionParameter(BaseConstants.ACT_REFILL);
				
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	public ActionForward agregarDeudaJudicial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProcedimientoAdapter procedimientoAdapter = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			CommonKey procedimientoKey = new CommonKey(procedimientoAdapter.getProcedimiento().getId());
			
			userSession.getNavModel().putParameter(AdministrarDeudaCyqDAction.PROCEDIMIENTO_KEY, procedimientoKey);
			
			return baseForwardAdapter(mapping, request, funcName, CyqConstants.ACTION_BUSCAR_DEUDA, BaseConstants.ACT_INICIALIZAR);
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	
	public ActionForward quitarDeudaJudicial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ADM_DEUDA_CYQ, funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapter4EnvioDeudaCyq()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			procedimientoAdapterVO.clearErrorMessages();
			
			if (request.getParameterValues("listIdDeudaSelected") != null) {
			
				// cargo los ids de deuda seleccionadas.
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
					
				procedimientoAdapterVO.setListIdDeudaSelected(listIdDeudaSelected);
				
				if (listIdDeudaSelected != null){
					for (int i=0; i < listIdDeudaSelected.length; i++){
						log.debug(funcName + " idDeuda Selected=" + listIdDeudaSelected[i]);
					}
				}
			
			} else {
				procedimientoAdapterVO.addRecoverableValueError("Debe seleccionar al menos un registro de deuda a quitar");
			}
			
			// Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				request.setAttribute(ProcedimientoAdapter.NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_ADAPTER);
			}
						
			// Llamo al Service de Cyq que haga el init de la lista de procedimientos.
			procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().quitarDeudaCyq(userSession, procedimientoAdapterVO);
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procedimientoAdapterVO.errorString());
				
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.NAME, procedimientoAdapterVO);
			}
			
			userSession.getNavModel().setConfAction(CyqConstants.PATH_ADMINISTRAR_PROCEDIMIENTO);
			userSession.getNavModel().setConfActionParameter(BaseConstants.ACT_REFILL);
					
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	public ActionForward imprimirDeudaAdmin(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			procedimientoAdapterVO.clearErrorMessages();
			
			if (request.getParameterValues("listIdDeudaSelected") != null) {
			
				// cargo los ids de deuda seleccionadas.
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
					
				procedimientoAdapterVO.setListIdDeudaSelected(listIdDeudaSelected);
				
				if (listIdDeudaSelected != null){
					for (int i=0; i < listIdDeudaSelected.length; i++){
						log.debug(funcName + " idDeuda Selected=" + listIdDeudaSelected[i]);
					}
				}
			}else{
				procedimientoAdapterVO.setListIdDeudaSelected(null);
			}
			
			PrintModel print = CyqServiceLocator.getConcursoyQuiebraService().imprimirDeudaAdminCyq(userSession, procedimientoAdapterVO);
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	
	public ActionForward imprimirDeudaJudicial(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			procedimientoAdapterVO.clearErrorMessages();
			
			if (request.getParameterValues("listIdDeudaSelected") != null) {
			
				// cargo los ids de deuda seleccionadas.
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
					
				procedimientoAdapterVO.setListIdDeudaSelected(listIdDeudaSelected);
				
				if (listIdDeudaSelected != null){
					for (int i=0; i < listIdDeudaSelected.length; i++){
						log.debug(funcName + " idDeuda Selected=" + listIdDeudaSelected[i]);
					}
				}
			}else{
				procedimientoAdapterVO.setListIdDeudaSelected(null);
			}
			
			PrintModel print = CyqServiceLocator.getConcursoyQuiebraService().imprimirDeudaJudicialCyq(userSession, procedimientoAdapterVO);
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_CyQ, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.NAME); 
			}

			// llamada al servicio
			ProcedimientoVO procedimientoVO = CyqServiceLocator.getConcursoyQuiebraService().deleteProcedimiento
				(userSession, procedimientoAdapterVO.getProcedimiento());
			
            // Tiene errores recuperables
			if (procedimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString());
				saveDemodaErrors(request, procedimientoVO);				
				request.setAttribute(ProcedimientoAdapter.NAME, procedimientoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (procedimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.NAME, procedimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcedimientoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcedimientoAdapter.NAME);
		
	}


	public ActionForward imprimirCaratula(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.NAME); 
			}
			
			// Llamada al servicio
			PrintModel print = CyqServiceLocator.getConcursoyQuiebraService().imprimirCaratula(userSession, procedimientoAdapterVO);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
	}
	
	
	// Metodos relacionados ProCueNoDeu
	public ActionForward verProCueNoDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_PROCUENODEU);

	}

	public ActionForward modificarProCueNoDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_PROCUENODEU);

	}

	public ActionForward eliminarProCueNoDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_PROCUENODEU);

	}
	
	public ActionForward agregarProCueNoDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProcedimientoAdapter procedimientoAdapter = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.NAME);
			
			CommonKey procedimientoKey = new CommonKey(procedimientoAdapter.getProcedimiento().getId());
			
			userSession.getNavModel().putParameter(AdministrarDeudaCyqDAction.PROCEDIMIENTO_KEY, procedimientoKey);
			
			return baseForwardAdapter(mapping, request, funcName, CyqConstants.ACTION_BUSCAR_DEUDA, BaseConstants.ACT_INICIALIZAR);
	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.NAME);
		}
		
	}
}