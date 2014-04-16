//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.DetAjuDocSopAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuDocSopVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuAdapter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDetAjuDocSopDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDetAjuDocSopDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJUDOCSOP, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DetAjuDocSopAdapter detAjuDocSopAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDetAjuDocSopAdapterForView(userSession, commonKey)";
				detAjuDocSopAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuDocSopAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJUDOCSOP_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDetAjuDocSopAdapterForUpdate(userSession, commonKey)";
				detAjuDocSopAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuDocSopAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJUDOCSOP_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDetAjuDocSopAdapterForView(userSession, commonKey)";
				detAjuDocSopAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuDocSopAdapterForView(userSession, commonKey);				
				detAjuDocSopAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.DETAJUDOCSOP_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJUDOCSOP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDetAjuDocSopAdapterForCreate(userSession)";
				detAjuDocSopAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuDocSopAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJUDOCSOP_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (detAjuDocSopAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + detAjuDocSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			detAjuDocSopAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DetAjuDocSopAdapter.NAME + ": "+ detAjuDocSopAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			 
			saveDemodaMessages(request, detAjuDocSopAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuDocSopAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DetAjuDocSopAdapter detAjuDocSopAdapterVO = (DetAjuDocSopAdapter) userSession.get(DetAjuDocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (detAjuDocSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DetAjuDocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuDocSopAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(detAjuDocSopAdapterVO, request);
			
            // Tiene errores recuperables
			if (detAjuDocSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuDocSopAdapterVO.infoString()); 
				saveDemodaErrors(request, detAjuDocSopAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// llamada al servicio
			DetAjuDocSopVO detAjuDocSopVO = EfServiceLocator.getFiscalizacionService().createDetAjuDocSop(userSession, detAjuDocSopAdapterVO.getDetAjuDocSop());
			
            // Tiene errores recuperables
			if (detAjuDocSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuDocSopVO.infoString()); 
				saveDemodaErrors(request, detAjuDocSopVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (detAjuDocSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuDocSopVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DetAjuDocSopAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuDocSopAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DetAjuDocSopAdapter detAjuDocSopAdapterVO = (DetAjuDocSopAdapter) userSession.get(DetAjuDocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (detAjuDocSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DetAjuDocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuDocSopAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(detAjuDocSopAdapterVO, request);
			
            // Tiene errores recuperables
			if (detAjuDocSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuDocSopAdapterVO.infoString()); 
				saveDemodaErrors(request, detAjuDocSopAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// llamada al servicio
			DetAjuDocSopVO detAjuDocSopVO = EfServiceLocator.getFiscalizacionService().updateDetAjuDocSop(userSession, detAjuDocSopAdapterVO.getDetAjuDocSop());
			
            // Tiene errores recuperables
			if (detAjuDocSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuDocSopAdapterVO.infoString()); 
				saveDemodaErrors(request, detAjuDocSopVO);
				return forwardErrorRecoverable(mapping, request, userSession, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (detAjuDocSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuDocSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DetAjuDocSopAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuDocSopAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJUDOCSOP, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DetAjuDocSopAdapter detAjuDocSopAdapterVO = (DetAjuDocSopAdapter) userSession.get(DetAjuDocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (detAjuDocSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DetAjuDocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuDocSopAdapter.NAME); 
			}

			// llamada al servicio
			DetAjuDocSopVO detAjuDocSopVO = EfServiceLocator.getFiscalizacionService().deleteDetAjuDocSop
				(userSession, detAjuDocSopAdapterVO.getDetAjuDocSop());
			
            // Tiene errores recuperables
			if (detAjuDocSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuDocSopAdapterVO.infoString());
				saveDemodaErrors(request, detAjuDocSopVO);				
				request.setAttribute(DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
				return mapping.findForward(EfConstants.FWD_DETAJUDOCSOP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (detAjuDocSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuDocSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DetAjuDocSopAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuDocSopAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DetAjuDocSopAdapter.NAME);
		
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
				//NavModel navModel = userSession.getNavModel();
				
				String fileName = request.getParameter("fileParam");	
		
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetAjuDocSopAdapter.NAME);
			}
	}
	
	public ActionForward paramDocumentacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				DetAjuDocSopAdapter detAjuDocSopAdapterVO =  (DetAjuDocSopAdapter) userSession.get(DetAjuDocSopAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detAjuDocSopAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetAjuDocSopAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuDocSopAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
					return mapping.findForward(EfConstants.FWD_DETAJUDOCSOP_EDIT_ADAPTER); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(detAjuDocSopAdapterVO, request);
				
				// llamo al param del servicio
				detAjuDocSopAdapterVO = EfServiceLocator.getFiscalizacionService().cambiarDocumentacionParam(userSession, detAjuDocSopAdapterVO);

	            // Tiene errores recuperables
				if (detAjuDocSopAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuDocSopAdapterVO.infoString()); 
					saveDemodaErrors(request, detAjuDocSopAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (detAjuDocSopAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + detAjuDocSopAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, detAjuDocSopAdapterVO);

				// Envio el VO al request
				request.setAttribute(DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DetAjuDocSopAdapter.NAME, detAjuDocSopAdapterVO);

				return forwardModificarAdapter(mapping, request, funcName, EfConstants.FWD_DETAJUDOCSOP_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConstanciaDeuAdapter.NAME);
			}
	}
	
	
}
