//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAnulacionObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAnulacionObraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_ANULACIONOBRA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AnulacionObraAdapter anulacionObraAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAnulacionObraAdapterForView(userSession, commonKey)";
				anulacionObraAdapterVO = RecServiceLocator.getCdmService().getAnulacionObraAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_ANULACIONOBRA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAnulacionObraAdapterForUpdate(userSession, commonKey)";
				anulacionObraAdapterVO = RecServiceLocator.getCdmService().getAnulacionObraAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_ANULACIONOBRA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAnulacionObraAdapterForView(userSession, commonKey)";
				anulacionObraAdapterVO = RecServiceLocator.getCdmService().getAnulacionObraAdapterForView(userSession, commonKey);				
				anulacionObraAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.ANULACIONOBRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_ANULACIONOBRA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAnulacionObraAdapterForCreate(userSession)";
				anulacionObraAdapterVO = RecServiceLocator.getCdmService().getAnulacionObraAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_ANULACIONOBRA_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (anulacionObraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + anulacionObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			anulacionObraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AnulacionObraAdapter.NAME + ": "+ anulacionObraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			 
			saveDemodaMessages(request, anulacionObraAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_ANULACIONOBRA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AnulacionObraAdapter anulacionObraAdapterVO = (AnulacionObraAdapter) userSession.get(AnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (anulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(anulacionObraAdapterVO, request);
			
            // Tiene errores recuperables
			if (anulacionObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
				saveDemodaErrors(request, anulacionObraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// llamada al servicio
			AnulacionObraVO anulacionObraVO = RecServiceLocator.getCdmService().createAnulacionObra(userSession, anulacionObraAdapterVO.getAnulacionObra());
			
            // Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraVO.infoString()); 
				saveDemodaErrors(request, anulacionObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AnulacionObraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_ANULACIONOBRA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AnulacionObraAdapter anulacionObraAdapterVO = (AnulacionObraAdapter) userSession.get(AnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (anulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(anulacionObraAdapterVO, request);
			
            // Tiene errores recuperables
			if (anulacionObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
				saveDemodaErrors(request, anulacionObraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// llamada al servicio
			AnulacionObraVO anulacionObraVO = RecServiceLocator.getCdmService().updateAnulacionObra(userSession, anulacionObraAdapterVO.getAnulacionObra());
			
            // Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
				saveDemodaErrors(request, anulacionObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AnulacionObraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_ANULACIONOBRA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AnulacionObraAdapter anulacionObraAdapterVO = (AnulacionObraAdapter) userSession.get(AnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (anulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AnulacionObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraAdapter.NAME); 
			}

			// llamada al servicio
			AnulacionObraVO anulacionObraVO = RecServiceLocator.getCdmService().deleteAnulacionObra
				(userSession, anulacionObraAdapterVO.getAnulacionObra());
			
            // Tiene errores recuperables
			if (anulacionObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString());
				saveDemodaErrors(request, anulacionObraVO);				
				request.setAttribute(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
				return mapping.findForward(RecConstants.FWD_ANULACIONOBRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AnulacionObraAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AnulacionObraAdapter.NAME);
		
	}
	
	public ActionForward paramObra (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AnulacionObraAdapter anulacionObraAdapterVO = (AnulacionObraAdapter) userSession.get(AnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (anulacionObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AnulacionObraAdapter.NAME 
							+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(anulacionObraAdapterVO, request);
			
            // Tiene errores recuperables
			if (anulacionObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
				saveDemodaErrors(request, anulacionObraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME,
							anulacionObraAdapterVO);
			}
			
			// llamada al servicio
			anulacionObraAdapterVO = RecServiceLocator.getCdmService()
										.getAnulacionObraAdapterParamObra(userSession, anulacionObraAdapterVO);
			
            // Tiene errores recuperables
			if (anulacionObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
				saveDemodaErrors(request, anulacionObraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, 
							anulacionObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraAdapter.NAME, 
							anulacionObraAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
			
			return mapping.findForward(RecConstants.FWD_ANULACIONOBRA_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
		}
	}

	public ActionForward paramPlanillaCuadra (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AnulacionObraAdapter anulacionObraAdapterVO = (AnulacionObraAdapter) userSession.get(AnulacionObraAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (anulacionObraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AnulacionObraAdapter.NAME 
							+ " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(anulacionObraAdapterVO, request);
				
	            // Tiene errores recuperables
				if (anulacionObraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
					saveDemodaErrors(request, anulacionObraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, 
							anulacionObraAdapterVO);
				}
				
				// llamada al servicio
				anulacionObraAdapterVO = RecServiceLocator.getCdmService().
						getAnulacionObraAdapterParamPlanillaCuadra(userSession, anulacionObraAdapterVO);
				
	            // Tiene errores recuperables
				if (anulacionObraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + anulacionObraAdapterVO.infoString()); 
					saveDemodaErrors(request, anulacionObraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraAdapter.NAME, 
							anulacionObraAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (anulacionObraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + anulacionObraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraAdapter.NAME, 
							anulacionObraAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
				// Subo el apdater al userSession
				userSession.put(AnulacionObraAdapter.NAME, anulacionObraAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_ANULACIONOBRA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
			}
		}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AnulacionObraAdapter adapterVO = (AnulacionObraAdapter)userSession.get(AnulacionObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + AnulacionObraAdapter.NAME 
								+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getAnulacionObra().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getAnulacionObra()); 
			
			adapterVO.getAnulacionObra().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(AnulacionObraAdapter.NAME, adapterVO);
			
			return mapping.findForward(RecConstants.FWD_ANULACIONOBRA_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraAdapter.NAME);
		}	
	}

}
