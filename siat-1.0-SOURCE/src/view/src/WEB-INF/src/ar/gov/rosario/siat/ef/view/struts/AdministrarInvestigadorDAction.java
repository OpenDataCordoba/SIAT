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
import ar.gov.rosario.siat.ef.iface.model.InvestigadorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarInvestigadorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarInvestigadorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INVESTIGADOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		InvestigadorAdapter investigadorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getInvestigadorAdapterForView(userSession, commonKey)";
				investigadorAdapterVO = EfServiceLocator.getDefinicionService().getInvestigadorAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INVESTIGADOR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getInvestigadorAdapterForUpdate(userSession, commonKey)";
				investigadorAdapterVO = EfServiceLocator.getDefinicionService().getInvestigadorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INVESTIGADOR_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getInvestigadorAdapterForView(userSession, commonKey)";
				investigadorAdapterVO = EfServiceLocator.getDefinicionService().getInvestigadorAdapterForView(userSession, commonKey);				
				investigadorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.INVESTIGADOR_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_INVESTIGADOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getInvestigadorAdapterForCreate(userSession)";
				investigadorAdapterVO = EfServiceLocator.getDefinicionService().getInvestigadorAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_INVESTIGADOR_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (investigadorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + investigadorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			investigadorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + InvestigadorAdapter.NAME + ": "+ investigadorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(InvestigadorAdapter.NAME, investigadorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(InvestigadorAdapter.NAME, investigadorAdapterVO);
			 
			saveDemodaMessages(request, investigadorAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InvestigadorAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INVESTIGADOR, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InvestigadorAdapter investigadorAdapterVO = (InvestigadorAdapter) userSession.get(InvestigadorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (investigadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InvestigadorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InvestigadorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(investigadorAdapterVO, request);
			
            // Tiene errores recuperables
			if (investigadorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorAdapterVO.infoString()); 
				saveDemodaErrors(request, investigadorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// llamada al servicio
			InvestigadorVO investigadorVO = EfServiceLocator.getDefinicionService().createInvestigador(userSession, investigadorAdapterVO.getInvestigador());
			
            // Tiene errores recuperables
			if (investigadorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorVO.infoString()); 
				saveDemodaErrors(request, investigadorVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (investigadorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + investigadorVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InvestigadorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InvestigadorAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INVESTIGADOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InvestigadorAdapter investigadorAdapterVO = (InvestigadorAdapter) userSession.get(InvestigadorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (investigadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InvestigadorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InvestigadorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(investigadorAdapterVO, request);
			
            // Tiene errores recuperables
			if (investigadorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorAdapterVO.infoString()); 
				saveDemodaErrors(request, investigadorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// llamada al servicio
			InvestigadorVO investigadorVO = EfServiceLocator.getDefinicionService().updateInvestigador(userSession, investigadorAdapterVO.getInvestigador());
			
            // Tiene errores recuperables
			if (investigadorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorAdapterVO.infoString()); 
				saveDemodaErrors(request, investigadorVO);
				return forwardErrorRecoverable(mapping, request, userSession, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (investigadorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + investigadorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InvestigadorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InvestigadorAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INVESTIGADOR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InvestigadorAdapter investigadorAdapterVO = (InvestigadorAdapter) userSession.get(InvestigadorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (investigadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InvestigadorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InvestigadorAdapter.NAME); 
			}

			// llamada al servicio
			InvestigadorVO investigadorVO = EfServiceLocator.getDefinicionService().deleteInvestigador
				(userSession, investigadorAdapterVO.getInvestigador());
			
            // Tiene errores recuperables
			if (investigadorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + investigadorAdapterVO.infoString());
				saveDemodaErrors(request, investigadorVO);				
				request.setAttribute(InvestigadorAdapter.NAME, investigadorAdapterVO);
				return mapping.findForward(EfConstants.FWD_INVESTIGADOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (investigadorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + investigadorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InvestigadorAdapter.NAME, investigadorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InvestigadorAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InvestigadorAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, InvestigadorAdapter.NAME);
		
	}

	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);		
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				InvestigadorAdapter investigadorAdapterVO =  (InvestigadorAdapter) userSession.get(InvestigadorAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (investigadorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + InvestigadorAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, InvestigadorAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(InvestigadorAdapter.NAME, investigadorAdapterVO);
					return mapping.findForward(EfConstants.FWD_INVESTIGADOR_EDIT_ADAPTER); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				investigadorAdapterVO.getInvestigador().setIdPersona(new Long(selectedId));
				
				// llamo al param del servicio
				investigadorAdapterVO = EfServiceLocator.getDefinicionService().getInvestigadorAdapterParamPersona(userSession, investigadorAdapterVO);

	            // Tiene errores recuperables
				if (investigadorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + investigadorAdapterVO.infoString()); 
					saveDemodaErrors(request, investigadorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							InvestigadorAdapter.NAME, investigadorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (investigadorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + investigadorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							InvestigadorAdapter.NAME, investigadorAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, investigadorAdapterVO);

				// Envio el VO al request
				request.setAttribute(InvestigadorAdapter.NAME, investigadorAdapterVO);
				// Subo el apdater al userSession
				userSession.put(InvestigadorAdapter.NAME, investigadorAdapterVO);

				return mapping.findForward(EfConstants.FWD_INVESTIGADOR_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, InvestigadorAdapter.NAME);
			}
	}
		
}
