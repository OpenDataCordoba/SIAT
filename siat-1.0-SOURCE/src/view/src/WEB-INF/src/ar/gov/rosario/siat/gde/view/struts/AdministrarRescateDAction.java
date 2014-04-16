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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;
import ar.gov.rosario.siat.gde.iface.model.RescateVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRescateDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRescateDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		if (act.equals(GdeConstants.ACT_INCLUDE_VERDETALLE_CONVENIO)){
			act = BaseConstants.ACT_SELECCIONAR;
		}
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RescateAdapter rescateAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRescateAdapterForView(userSession, commonKey)";
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForView(userSession, commonKey);
				rescateAdapterVO.setAct(BaseConstants.ACT_VER);
				userSession.put(RescateAdapter.NAME, rescateAdapterVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER); 
			}
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRescateAdapterForCreate(userSession)";
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForCreate(userSession);
				rescateAdapterVO.setAct(BaseConstants.ACT_AGREGAR);
				userSession.put(RescateAdapter.NAME, rescateAdapterVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER); 				
			}
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRescateAdapterForUpdate(userSession)";
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForUpdate(userSession, commonKey);
				rescateAdapterVO.setAct(BaseConstants.ACT_MODIFICAR);
				userSession.put(RescateAdapter.NAME, rescateAdapterVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER);				
			}
			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRescateAdapterForView(userSession, commonKey)";
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForView(userSession, commonKey);
				rescateAdapterVO.setAct(BaseConstants.ACT_ELIMINAR);
				userSession.put(RescateAdapter.NAME, rescateAdapterVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				rescateAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.RESCATE_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER);				
			}
			
			if(act.equals(BaseConstants.ACT_SELECCIONAR)){
				rescateAdapterVO=new RescateAdapter();
				rescateAdapterVO.setPageNumber(1L);
				rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForSeleccion(userSession, commonKey.getId(), rescateAdapterVO);
				rescateAdapterVO.setAct(BaseConstants.ACT_SELECCIONAR);
				userSession.put(RescateAdapter.NAME, rescateAdapterVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				actionForward = mapping.findForward(GdeConstants.FWD_RESCATE_SELADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (rescateAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + rescateAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.NAME, rescateAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			rescateAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RescateAdapter.NAME + ": "+ rescateAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RescateAdapter.NAME, rescateAdapterVO);
			 
			saveDemodaMessages(request, rescateAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el adapter del usserSession
			RescateAdapter rescateAdapterVO =  (RescateAdapter) userSession.get(RescateAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rescateAdapterVO, request);
				
			// Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.NAME, rescateAdapterVO);
			}

			rescateAdapterVO = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterParamRecurso(userSession, rescateAdapterVO);
				
			// Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   RescateAdapter.NAME, rescateAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (rescateAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  RescateAdapter.NAME, rescateAdapterVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, rescateAdapterVO);
				
			// Envio el VO al request
			request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);

			return mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, 
				BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RescateAdapter rescateAdapterVO = (RescateAdapter) userSession.get(RescateAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rescateAdapterVO, request);
			
            // Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.NAME, rescateAdapterVO);
			}
			
			// llamada al servicio
			RescateVO rescateVO = GdeServiceLocator.getGdePlanPagoService().createRescate(userSession, rescateAdapterVO.getRescate());
			
            // Tiene errores recuperables
			if (rescateVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateVO.infoString()); 
				rescateVO.addErrorMessages(rescateAdapterVO);
				userSession.put(RescateAdapter.NAME, rescateAdapterVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.NAME, rescateAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (rescateVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.NAME, rescateAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RescateAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
								   HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, 
				BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
			
		try {
			// Bajo el adapter del userSession
			RescateAdapter rescateAdapterVO = (RescateAdapter) userSession.get(RescateAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(rescateAdapterVO, request);
				
			// Tiene errores recuperables
			if (rescateAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateAdapterVO.infoString()); 
				saveDemodaErrors(request, rescateAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.NAME, rescateAdapterVO);
			}
				
			// llamada al servicio
			RescateVO rescateVO = GdeServiceLocator.getGdePlanPagoService().updateRescate(userSession, rescateAdapterVO.getRescate());
				
			// Tiene errores recuperables
			if (rescateVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateVO.infoString()); 
				saveDemodaErrors(request, rescateVO);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.NAME, rescateAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (rescateVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.NAME, rescateAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RescateAdapter.NAME);
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
								  HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_RESCATE, 
				BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
			
		try {
			// Bajo el adapter del userSession
			RescateAdapter rescateAdapterVO = (RescateAdapter) userSession.get(RescateAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (rescateAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.NAME); 
			}

			// llamada al servicio
			RescateVO rescateVO = GdeServiceLocator.getGdePlanPagoService().
				deleteRescate(userSession, rescateAdapterVO.getRescate());
				
			// Tiene errores recuperables
			if (rescateVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + rescateVO.infoString()); 
				saveDemodaErrors(request, rescateVO);
				request.setAttribute(RescateAdapter.NAME, rescateAdapterVO);
				// no usamos forwardErrorRecoverable porque no forwardeamos al inputForward del struts-config.
				return mapping.findForward(GdeConstants.FWD_RESCATE_VIEW_ADAPTER);
			}
				
			// Tiene errores no recuperables
			if (rescateVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + rescateVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RescateAdapter.NAME, rescateAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RescateAdapter.NAME);
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}
	}
	
	public ActionForward verConvenio (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		UserSession userSession = getCurrentUserSession(request, mapping);
		RescateAdapter rescateAdapterVO = (RescateAdapter)userSession.get(RescateAdapter.NAME);
		userSession.put(RescateAdapter.NAME, rescateAdapterVO);
		request.setAttribute("vieneDe", "rescate");
		request.setAttribute("idRescate", rescateAdapterVO.getRescate().getId());
		userSession.put("iniciarEnPagina",rescateAdapterVO.getPageNumber().toString());
		log.debug("SUBO PAGINA: "+rescateAdapterVO.getPageNumber());
		
		return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_CUENTA);
	}

	public ActionForward seleccionar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession= getCurrentUserSession(request, mapping);
		
		RescateAdapter rescateAdapter = (RescateAdapter)userSession.get(RescateAdapter.NAME);
		
		if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))&&!(Long.parseLong(request.getParameter("selectedId"))==0L)){
			rescateAdapter.getRescate().setId(Long.parseLong((String) request.getParameter("selectedId")));
			if (userSession.get("iniciarEnPagina")!=null){
				log.debug("PAGINA DE VUELTA: "+(String) userSession.get("iniciarEnPagina"));
				rescateAdapter.setPageNumber(Long.parseLong(((String)userSession.get("iniciarEnPagina"))));
				userSession.put("iniciarEnPagina", null);
			}
		}else{
			DemodaUtil.populateVO(rescateAdapter, request);
			rescateAdapter.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
		}
		rescateAdapter = GdeServiceLocator.getGdePlanPagoService().getRescateAdapterForSeleccion(userSession, rescateAdapter.getRescate().getId(), rescateAdapter);
		
		userSession.put(RescateAdapter.NAME, rescateAdapter);
		request.setAttribute(RescateAdapter.NAME, rescateAdapter);
		
		return mapping.findForward(GdeConstants.FWD_RESCATE_SELADAPTER);
	}
	public ActionForward volver(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		return new ActionForward (GdeConstants.PATH_BUSCARRESCATE);
	}
	
	public ActionForward quitarConvenio (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		Long selectedId=Long.parseLong(request.getParameter("selectedId"));
		RescateAdapter rescateAdapter = (RescateAdapter)userSession.get(RescateAdapter.NAME);
		
		try{
			rescateAdapter = GdeServiceLocator.getGdePlanPagoService().getRescateQuitarConvenio(selectedId, rescateAdapter, userSession);
			if (rescateAdapter.hasError()){
				userSession.put(RescateAdapter.NAME, rescateAdapter);
				request.setAttribute(RescateAdapter.NAME, rescateAdapter);
				saveDemodaErrors(request, rescateAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, RescateAdapter.NAME, rescateAdapter);
			}
			
			userSession.put(RescateAdapter.NAME, rescateAdapter);
			request.setAttribute(RescateAdapter.NAME, rescateAdapter);
			return mapping.findForward(GdeConstants.FWD_RESCATE_SELADAPTER);
		}catch (Exception exception){
			log.error(rescateAdapter, exception);
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}
	}

	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RescateAdapter adapterVO = (RescateAdapter) userSession.get(RescateAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + RescateAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RescateAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getRescate().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getRescate()); 
			
			adapterVO.getRescate().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(RescateAdapter.NAME, adapterVO);
			
			return mapping.findForward(GdeConstants.FWD_RESCATE_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RescateAdapter.NAME);
		}	
	}

}
