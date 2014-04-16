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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.rec.iface.model.ObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncObraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_OBRA_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObraAdapter obraAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getObraAdapterForUpdate(userSession, commonKey)";
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_OBRA_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getObraAdapterForCreate(userSession)";
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_OBRA_ENC_EDIT_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (obraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + obraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.ENC_NAME, obraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			obraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ObraAdapter.ENC_NAME + ": "+ obraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObraAdapter.ENC_NAME, obraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObraAdapter.ENC_NAME, obraAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_OBRA_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (obraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(obraAdapterVO, request);
			
            // Tiene errores recuperables
			if (obraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
				saveDemodaErrors(request, obraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
			}
			
			// llamada al servicio
			ObraVO obraVO = RecServiceLocator.getCdmService().createObra(userSession, obraAdapterVO.getObra());
			
            // Tiene errores recuperables
			if (obraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraVO.infoString()); 
				saveDemodaErrors(request, obraVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (obraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.ENC_NAME, obraAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, RecSecurityConstants.ABM_OBRA, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(obraVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, ObraAdapter.ENC_NAME, 
					RecConstants.PATH_ADMINISTRAR_OBRA, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, ObraAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_OBRA_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (obraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(obraAdapterVO, request);
			
            // Tiene errores recuperables
			if (obraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
				saveDemodaErrors(request, obraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
			}
			
			// llamada al servicio
			ObraVO obraVO = RecServiceLocator.getCdmService().updateObra(userSession, obraAdapterVO.getObra());
			
            // Tiene errores recuperables
			if (obraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
				saveDemodaErrors(request, obraVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (obraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.ENC_NAME, obraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObraAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObraAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ObraAdapter.ENC_NAME);
		
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
			ObraAdapter adapterVO = (ObraAdapter)userSession.get(ObraAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getObra().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getObra()); 
			
			adapterVO.getObra().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(ObraAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward( RecConstants.FWD_OBRA_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObraAdapter.ENC_NAME);
		}	
	}

	public ActionForward paramValuacion (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (obraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ObraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(obraAdapterVO, request);
				
	            // Tiene errores recuperables
				if (obraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
					saveDemodaErrors(request, obraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
				}
				
				// llamada al servicio
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterParamValuacion
																			(userSession, obraAdapterVO);

	            // Tiene errores recuperables
				if (obraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
					saveDemodaErrors(request, obraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (obraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.ENC_NAME, obraAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ObraAdapter.ENC_NAME, obraAdapterVO);

				// Subo el apdater al userSession
				userSession.put(ObraAdapter.ENC_NAME, obraAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_OBRA_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ObraAdapter.ENC_NAME);
			}
		}
	
	public ActionForward paramEsCostoEsp (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ObraAdapter obraAdapterVO = (ObraAdapter) userSession.get(ObraAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (obraAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ObraAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(obraAdapterVO, request);
				
	            // Tiene errores recuperables
				if (obraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
					saveDemodaErrors(request, obraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
				}
				
				// llamada al servicio
				obraAdapterVO = RecServiceLocator.getCdmService().getObraAdapterParamEsCostoEsp
																			(userSession, obraAdapterVO);

	            // Tiene errores recuperables
				if (obraAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + obraAdapterVO.infoString()); 
					saveDemodaErrors(request, obraAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ObraAdapter.ENC_NAME, obraAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (obraAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + obraAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.ENC_NAME, obraAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ObraAdapter.ENC_NAME, obraAdapterVO);

				// Subo el apdater al userSession
				userSession.put(ObraAdapter.ENC_NAME, obraAdapterVO);
				
				return mapping.findForward(RecConstants.FWD_OBRA_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ObraAdapter.ENC_NAME);
			}
		}

}
	
