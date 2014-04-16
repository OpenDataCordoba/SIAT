//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoSearchPage;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncRecursoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncRecursoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession us=getCurrentUserSession(request, mapping);
			
			NavModel navModel = us.getNavModel();
			
			RecursoSearchPage recursoSearchPageVO=(RecursoSearchPage)us.get(RecursoSearchPage.NAME);
			
			boolean esNoTrib=false;
			
			if (recursoSearchPageVO!=null && recursoSearchPageVO.isEsNoTrib())
				esNoTrib = true;
			
			UserSession userSession=null;
			
			if (esNoTrib)
				userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO_NOTRIB, act);
			else
				userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO_ENC, act);		

			
			if (userSession == null) return forwardErrorSession(request);
			
			
			
			RecursoAdapter recursoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecursoAdapterForUpdate(userSession, commonKey)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForUpdate(userSession, commonKey);
					if(!esNoTrib)
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
					else
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_NOTRIB_ENC_EDITADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRecursoAdapterForCreate(userSession)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForCreate(userSession, esNoTrib);
					if(!esNoTrib)
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
					else
						actionForward = mapping.findForward(DefConstants.FWD_RECURSO_NOTRIB_ENC_EDITADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recursoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RecursoAdapter.ENC_NAME + ": "+ recursoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECURSO_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				RecursoVO recursoVO = DefServiceLocator.getGravamenService().createRecurso(userSession, recursoAdapterVO);
				
	            // Tiene errores recuperables
				if (recursoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoVO.infoString());
					String forward;
					
					if (!recursoAdapterVO.isEsNoTrib())
						forward = DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER;
					else
						forward=DefConstants.FWD_RECURSO_NOTRIB_ENC_EDITADAPTER;
					
					saveDemodaErrors(request, recursoVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO, forward);
				}
				
				// Tiene errores no recuperables
				if (recursoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}

				// Si tiene permiso lo dirijo al adapter de modificacion, 
				// sino vuelve al searchPage
				if (hasAccess(userSession, DefSecurityConstants.ABM_RECURSO, 
					BaseSecurityConstants.MODIFICAR)) {
					
					// seteo el id para que lo use el siguiente action 
					userSession.getNavModel().setSelectedId(recursoVO.getId().toString());

					// lo dirijo al adapter de modificacion
					return forwardConfirmarOk(mapping, request, funcName, RecursoAdapter.ENC_NAME, 
						DefConstants.PATH_ADMINISTRAR_RECURSO, BaseConstants.METHOD_INICIALIZAR, 
						BaseConstants.ACT_MODIFICAR);
				} else {
					
					// lo dirijo al searchPage				
					return forwardConfirmarOk(mapping, request, funcName, RecursoAdapter.ENC_NAME);
					
				}
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECURSO_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				RecursoVO recursoVO = DefServiceLocator.getGravamenService().updateRecurso(userSession, recursoAdapterVO.getRecurso());
				
	            // Tiene errores recuperables
				if (recursoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoVO);
					request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);

					if(!recursoAdapterVO.isEsNoTrib())
						return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
					else
						return mapping.findForward(DefConstants.FWD_RECURSO_NOTRIB_ENC_EDITADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recursoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecursoAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RecursoAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, RecursoAdapter.ENC_NAME);
	}
		
	public ActionForward paramTipObjImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
		
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}
	
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamTipObjImp(userSession, recursoAdapterVO);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}
		
	public ActionForward paramEsPrincipal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamEsPrincipal(userSession, recursoAdapterVO);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}

	public ActionForward paramEnviaJudicial(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recursoAdapterVO, request);
			
            // Tiene errores recuperables
			if (recursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
			}
			
			// llamada al servicio
			recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamEnviaJudicial(userSession, recursoAdapterVO);
			
            // Tiene errores recuperables
			if (recursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recursoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
			// Subo el adapter al userSession
			userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
			
			return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
		}
	}

	public ActionForward paramPerEmiDeuMas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recursoAdapterVO, request);
			
            // Tiene errores recuperables
			if (recursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
			}
			
			// llamada al servicio
			recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamPerEmiDeuMas(userSession, recursoAdapterVO);
			
            // Tiene errores recuperables
			if (recursoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recursoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
			// Subo el adapter al userSession
			userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
			
			return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward paramPerImpMasDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamPerImpMasDeu(userSession, recursoAdapterVO);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}

	public ActionForward paramGenNotImpMas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamGenNotImpMas(userSession, recursoAdapterVO);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}
	

	public ActionForward paramGenPadFir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (recursoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecursoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// llamada al servicio
				recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterParamGenPadFir(userSession, recursoAdapterVO);
				
	            // Tiene errores recuperables
				if (recursoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoAdapterVO.infoString()); 
					saveDemodaErrors(request, recursoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.ENC_NAME, recursoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecursoAdapter.ENC_NAME, recursoAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECURSO_ENC_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.ENC_NAME);
			}
		}
	
}
