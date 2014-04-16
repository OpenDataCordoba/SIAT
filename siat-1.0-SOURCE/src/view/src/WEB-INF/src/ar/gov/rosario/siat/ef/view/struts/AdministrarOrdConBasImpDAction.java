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
import ar.gov.rosario.siat.ef.iface.model.OrdConBasImpAdapter;
import ar.gov.rosario.siat.ef.iface.model.PlaFueDatAdapter;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarOrdConBasImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOrdConBasImpDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OrdConBasImpAdapter ordConBasImpAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getOrdConBasImpAdapterForCreate(userSession)";
				ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDCONBASIMP_EDIT_ADAPTER);				
			}
			if (act.equals(EfConstants.ACT_CARGAR_AJUSTES)) {
				stringServicio = "getOrdConBasImpAdapterForUpdate(userSession, commonKey)";
				ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterForAjustes(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ADAPTER);
			}			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getOrdConBasImpAdapterForView(userSession, commonKey)";
				ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterForAjustes(userSession, commonKey);				
				ordConBasImpAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.ORDCONBASIMP_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_ORDCONBASIMP_VIEW_ADAPTER);				
			}
			if (act.equals(EfConstants.ACT_CARGAR_ALICUOTAS)) {
				stringServicio = "getOrdConBasImpAdapterForAlicuotas(userSession, commonKey)";
				ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterForAlicuotas(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ordConBasImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			ordConBasImpAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OrdConBasImpAdapter.NAME + ": "+ ordConBasImpAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			 
			saveDemodaMessages(request, ordConBasImpAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordConBasImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// llamada al servicio
			ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().createOrdConBasImp(userSession, ordConBasImpAdapterVO);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdConBasImpAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordConBasImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// llamada al servicio
			ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().deleteOrdConBasImp(userSession, ordConBasImpAdapterVO);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString());
				// Envio el VO al request
				request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);

				saveDemodaErrors(request, ordConBasImpAdapterVO);
				
				return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdConBasImpAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OrdConBasImpAdapter.NAME);
		
	}
	
	public ActionForward paramCompFuenteRes (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordConBasImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// llamada al servicio
			ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterParamCompFuenteRes(userSession, ordConBasImpAdapterVO);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			request.setAttribute("irA", "seccionFuentes");
			
			// Envio el VO al request
			request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
		}
	}		

	public ActionForward paramSelecFuente (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (ordConBasImpAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
				
	            // Tiene errores recuperables
				if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
					saveDemodaErrors(request, ordConBasImpAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				}
				
				// llamada al servicio
				ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterParamSelecFuente(userSession, ordConBasImpAdapterVO);
				
	            // Tiene errores recuperables
				if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
					saveDemodaErrors(request, ordConBasImpAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				}
				
				request.setAttribute("irA", "seccionVigencia");
				
				// Envio el VO al request
				request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				
				return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
			}
		}		

	public ActionForward irCargarAjustes(ActionMapping mapping, ActionForm form,
	HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_CARGAR_AJUSTES); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {			
			// Bajo el adapter del userSession
			OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (ordConBasImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));
			
			// llamada al servicio
			ordConBasImpAdapterVO= EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapter4UpdateAjustes(userSession, commonKey, ordConBasImpAdapterVO);
			
		    // Tiene errores recuperables
			if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
				saveDemodaErrors(request, ordConBasImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			}
			
			// seteo para el volver
			ordConBasImpAdapterVO.setPrevAction("/ef/AdministrarOrdConBasImp");
			ordConBasImpAdapterVO.setPrevActionParameter("inicializar");
			ordConBasImpAdapterVO.setAct(EfConstants.ACT_CARGAR_AJUSTES);
			
			// Envio el VO al request
			request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_AJUSTES_EDIT_ADAPTER);

			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
		}
	}

	public ActionForward updateAjustes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_CARGAR_AJUSTES); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {			
					// Bajo el adapter del userSession
					OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
					
					// Si es nulo no se puede continuar
					if (ordConBasImpAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
					
		            // Tiene errores recuperables
					if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
						saveDemodaErrors(request, ordConBasImpAdapterVO);
						userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_AJUSTES_EDIT_ADAPTER);
						//return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// llamada al servicio
					ordConBasImpAdapterVO= EfServiceLocator.getFiscalizacionService().updateAjustes(userSession, ordConBasImpAdapterVO);
					
				    // Tiene errores recuperables
					if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
						saveDemodaErrors(request, ordConBasImpAdapterVO);
						userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_AJUSTES_EDIT_ADAPTER);
						//return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// Tiene errores no recuperables
					if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// Envio el VO al request
					request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					// Subo el apdater al userSession
					userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					
					return new ActionForward("/ef/AdministrarOrdConBasImp.do?method=inicializar&selectedId="+
							ordConBasImpAdapterVO.getOrdConBasImp().getId());
					
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
				}
			}
				
	public ActionForward irAjustarPeriodos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_AJUSTAR_PERIODOS); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {			
					// Bajo el adapter del userSession
					OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
					
					// Si es nulo no se puede continuar
					if (ordConBasImpAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
					
		            // Tiene errores recuperables
					if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
						saveDemodaErrors(request, ordConBasImpAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// llamada al servicio
					ordConBasImpAdapterVO= EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapter4AjustarPeriodo(userSession, ordConBasImpAdapterVO);
					
				    // Tiene errores recuperables
					if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
						saveDemodaErrors(request, ordConBasImpAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// Tiene errores no recuperables
					if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// seteo para el volver
					ordConBasImpAdapterVO.setPrevAction("/ef/AdministrarOrdConBasImp");
					ordConBasImpAdapterVO.setPrevActionParameter("inicializar");
					ordConBasImpAdapterVO.setAct(EfConstants.ACT_CARGAR_AJUSTES);
					
					// Envio el VO al request
					request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					// Subo el apdater al userSession
					userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					
					return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_AJUSTAR_PERIODOS_ADAPTER);

					
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
				}
			}

	public ActionForward ajustarPeriodos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_AJUSTAR_PERIODOS); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {			
					// Bajo el adapter del userSession
					OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
					
					// Si es nulo no se puede continuar
					if (ordConBasImpAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
					
		            // Tiene errores recuperables
					if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
						saveDemodaErrors(request, ordConBasImpAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// llamada al servicio
					ordConBasImpAdapterVO= EfServiceLocator.getFiscalizacionService().ajustarPeriodos(userSession, ordConBasImpAdapterVO);
					
				    // Tiene errores recuperables
					if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
						saveDemodaErrors(request, ordConBasImpAdapterVO);

						// Envio el VO al request
						request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						// Subo el apdater al userSession
						userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);

						return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_AJUSTAR_PERIODOS_ADAPTER);
					}
					
					// Tiene errores no recuperables
					if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					}
					
					// Envio el VO al request
					request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					// Subo el apdater al userSession
					userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					
					return new ActionForward("/ef/AdministrarOrdConBasImp.do?method=inicializar&selectedId="+
							ordConBasImpAdapterVO.getOrdConBasImp().getId());
					
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
				}
			}
				
	public ActionForward irModificarAlicuota(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, 
																EfSecurityConstants.ACT_CARGAR_ALICUOTAS);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (ordConBasImpAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
				}

				ordConBasImpAdapterVO.getPlaFueDatCom().setId(new Long(request.getParameter("selectedId")));
				
				// llamada al servicio
				ordConBasImpAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdConBasImpAdapterForUpdateAlicuota(userSession, ordConBasImpAdapterVO);
				
	            // Tiene errores recuperables
				if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString());
					saveDemodaErrors(request, ordConBasImpAdapterVO);				
					request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
					return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				}
				
				request.setAttribute("irA", "listaPlaFueDatCom");
				
				// Envio el VO al request
				request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				
				saveDemodaMessages(request, ordConBasImpAdapterVO);			

				return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
			}
		}

		public ActionForward modificarAlicuotas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_PLAFUEDATDET,BaseSecurityConstants.MODIFICAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OrdConBasImpAdapter plaFueDatAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (plaFueDatAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaFueDatAdapterVO, request);
				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString()); 
					// Envio el VO al request
					request.setAttribute(OrdConBasImpAdapter.NAME, plaFueDatAdapterVO);
					// Subo el apdater al userSession
					userSession.put(OrdConBasImpAdapter.NAME, plaFueDatAdapterVO);
					saveDemodaErrors(request, plaFueDatAdapterVO);				
					return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
				}
				
				// llamada al servicio
				plaFueDatAdapterVO = EfServiceLocator.getFiscalizacionService().updateAlicuotas(userSession, plaFueDatAdapterVO);
				
	            // Tiene errores recuperables
				if (plaFueDatAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaFueDatAdapterVO.infoString());
					saveDemodaErrors(request, plaFueDatAdapterVO);				
					request.setAttribute(OrdConBasImpAdapter.NAME, plaFueDatAdapterVO);
					return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (plaFueDatAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaFueDatAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, plaFueDatAdapterVO);
				}

				request.setAttribute("irA", "listaPlaFueDatCom");
				
				// Envio el VO al request
				request.setAttribute(OrdConBasImpAdapter.NAME, plaFueDatAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OrdConBasImpAdapter.NAME, plaFueDatAdapterVO);
				
				saveDemodaMessages(request, plaFueDatAdapterVO);			

				return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
			}
		}

		public ActionForward imprimir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled())
				log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ACTA, EfSecurityConstants.IMPRIMIR);

			if (userSession == null)
				return forwardErrorSession(request);
			String stringServicio = "imprimir";
			try {
				// Bajo el adapter del userSession
				OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);

				// Si es nulo no se puede continuar
				if (ordConBasImpAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlaFueDatAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaFueDatAdapter.NAME); 
				}
				CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));
				
			
				PrintModel	 print  = EfServiceLocator.getFiscalizacionService().imprimirOrdConBasImp(userSession, commonKey);
			
				
				// Tiene errores recuperables
				if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
					saveDemodaErrors(request, ordConBasImpAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				}

				// Tiene errores no recuperables
				if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
				}

				if (log.isDebugEnabled()) log.debug(funcName + ": " + OrdConBasImpAdapter.NAME + ": "+ ordConBasImpAdapterVO.infoString());
				baseResponsePrintModel(response, print);
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception,
						OrdConBasImpAdapter.NAME);
			}
		}

		public ActionForward modificarCoeficientes(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

					String funcName = DemodaUtil.currentMethodName();
					if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
					
					UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ORDCONBASIMP, EfSecurityConstants.ACT_AJUSTAR_PERIODOS); 
					if (userSession==null) return forwardErrorSession(request);
					
					try {			
						// Bajo el adapter del userSession
						OrdConBasImpAdapter ordConBasImpAdapterVO = (OrdConBasImpAdapter) userSession.get(OrdConBasImpAdapter.NAME);
						
						// Si es nulo no se puede continuar
						if (ordConBasImpAdapterVO == null) {
							log.error("error en: "  + funcName + ": " + OrdConBasImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
							return forwardErrorSessionNullObject(mapping, request, funcName, OrdConBasImpAdapter.NAME); 
						}

						// Recuperamos datos del form en el vo
						DemodaUtil.populateVO(ordConBasImpAdapterVO, request);
						
			            // Tiene errores recuperables
						if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
							log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
							saveDemodaErrors(request, ordConBasImpAdapterVO);
							return forwardErrorRecoverable(mapping, request, userSession, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						}
						
						// llamada al servicio
						ordConBasImpAdapterVO= EfServiceLocator.getFiscalizacionService().updateMasivoAlicuotas(userSession, ordConBasImpAdapterVO);
						
					    // Tiene errores recuperables
						if (ordConBasImpAdapterVO.hasErrorRecoverable()) {
							log.error("recoverable error en: "  + funcName + ": " + ordConBasImpAdapterVO.infoString()); 
							saveDemodaErrors(request, ordConBasImpAdapterVO);

							// Envio el VO al request
							request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
							// Subo el apdater al userSession
							userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);

							return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
						}
						
						// Tiene errores no recuperables
						if (ordConBasImpAdapterVO.hasErrorNonRecoverable()) {
							log.error("error en: "  + funcName + ": " + ordConBasImpAdapterVO.errorString()); 
							return forwardErrorNonRecoverable(mapping, request, funcName, OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						}
						
						// Envio el VO al request
						request.setAttribute(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						// Subo el apdater al userSession
						userSession.put(OrdConBasImpAdapter.NAME, ordConBasImpAdapterVO);
						
						return mapping.findForward(EfConstants.FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER);
						
						/*return new ActionForward("/ef/AdministrarOrdConBasImp.do?method=inicializar&selectedId="+
								ordConBasImpAdapterVO.getOrdConBasImp().getId());
						*/
					} catch (Exception exception) {
						return baseException(mapping, request, funcName, exception, OrdConBasImpAdapter.NAME);
					}
				}
	
}
