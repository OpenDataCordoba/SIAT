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
import ar.gov.rosario.siat.frm.iface.model.ForCamVO;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcesoMasivoVO;
import ar.gov.rosario.siat.gde.iface.model.TipProMasVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProcesoMasivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcesoMasivoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
									 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcesoMasivoAdapter procesoMasivoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProcesoMasivoAdapterForView(userSession, commonKey)";
				procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_VIEW_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {

				// Tipo de Proceso masivo cargado en el userSession, en el inicializar de la busqueda
				CommonKey tipProMasKey = null;
				String idTipProMasFromReq = (String) userSession.get(TipProMasVO.ID_TIPOPROMAS);
				if(idTipProMasFromReq != null){
					tipProMasKey = new CommonKey(idTipProMasFromReq);
				}
				
				stringServicio = "getProcesoMasivoAdapterForCreate(userSession)";
				procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdapterForCreate(userSession,tipProMasKey );
				actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_EDIT_ADAPTER);				
			}
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProcesoMasivoAdapterForUpdate(userSession)";
				procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProcesoMasivoAdapterForView(userSession, commonKey)";
				procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdapterForView(userSession, commonKey);
				procesoMasivoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PROCESO_MASIVO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_VIEW_ADAPTER);				
			}
			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procesoMasivoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procesoMasivoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcesoMasivoAdapter.NAME + ": "+ procesoMasivoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			 
			saveDemodaMessages(request, procesoMasivoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
											 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el adapter del usserSession
			ProcesoMasivoAdapter procesoMasivoAdapterVO =  (ProcesoMasivoAdapter) userSession.get(ProcesoMasivoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoMasivoAdapterVO, request);
				
			// Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}

			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}

			procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdapterParamFecEnvRec(userSession, procesoMasivoAdapterVO);
				
			// Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (procesoMasivoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, procesoMasivoAdapterVO);
				
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);

			return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}
	}
	
	public ActionForward paramUtCri(ActionMapping mapping, ActionForm form,
									HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el adapter del usserSession
			ProcesoMasivoAdapter procesoMasivoAdapterVO =  (ProcesoMasivoAdapter) userSession.get(ProcesoMasivoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoMasivoAdapterVO, request);
				
			// Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}

			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}

			procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getProcesoMasivoAdapterParamUtCri(userSession, procesoMasivoAdapterVO);
				
			// Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (procesoMasivoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, procesoMasivoAdapterVO);
				
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);

			return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}
	}
	
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
								 HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcesoMasivoAdapter procesoMasivoAdapterVO = (ProcesoMasivoAdapter) userSession.get(ProcesoMasivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoMasivoAdapterVO, request);
			
            // Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
			
			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}
			
			// llamada al servicio
			ProcesoMasivoVO procesoMasivoVO = GdeServiceLocator.getGestionDeudaJudicialService().createProcesoMasivo(userSession, procesoMasivoAdapterVO.getProcesoMasivo());
			
            // Tiene errores recuperables
			if (procesoMasivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoMasivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcesoMasivoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
								   HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
			
		try {
			// Bajo el adapter del userSession
			ProcesoMasivoAdapter procesoMasivoAdapterVO = (ProcesoMasivoAdapter) userSession.get(ProcesoMasivoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoMasivoAdapterVO, request);
				
			// Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}

			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}
			
			// llamada al servicio
			ProcesoMasivoVO procesoMasivoVO = GdeServiceLocator.getGestionDeudaJudicialService().updateProcesoMasivo(userSession, procesoMasivoAdapterVO.getProcesoMasivo());
				
			// Tiene errores recuperables
			if (procesoMasivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// Tiene errores no recuperables
			if (procesoMasivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcesoMasivoAdapter.NAME);
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
								  HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
			
		try {
			// Bajo el adapter del userSession
			ProcesoMasivoAdapter procesoMasivoAdapterVO = (ProcesoMasivoAdapter) userSession.get(ProcesoMasivoAdapter.NAME);
				
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}

			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}			
			
			// llamada al servicio
			ProcesoMasivoVO procesoMasivoVO = GdeServiceLocator.getGestionDeudaJudicialService().deleteProcesoMasivo(userSession, procesoMasivoAdapterVO.getProcesoMasivo());
				
			// Tiene errores recuperables
			if (procesoMasivoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoVO);
				request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
				// no usamos forwardErrorRecoverable porque no forwardeamos al inputForward del struts-config.
				return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_VIEW_ADAPTER);
			}
				
			// Tiene errores no recuperables
			if (procesoMasivoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcesoMasivoAdapter.NAME);
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
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
			ProcesoMasivoAdapter procesoMasivoAdapterVO = (ProcesoMasivoAdapter)userSession.get(ProcesoMasivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(procesoMasivoAdapterVO, request);
			
			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}
			
			log.debug( funcName + " " + procesoMasivoAdapterVO.getProcesoMasivo().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, procesoMasivoAdapterVO.getProcesoMasivo()); 
			
			procesoMasivoAdapterVO.getProcesoMasivo().passErrorMessages(procesoMasivoAdapterVO);
		    
		    saveDemodaMessages(request, procesoMasivoAdapterVO);
		    saveDemodaErrors(request, procesoMasivoAdapterVO);
		    
			request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}	
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
								HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoMasivoAdapter.NAME);
		
	}
		
	public ActionForward paramFormulario(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			ProcesoMasivoAdapter procesoMasivoAdapterVO = (ProcesoMasivoAdapter) userSession.get(ProcesoMasivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoMasivoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoMasivoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoMasivoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procesoMasivoAdapterVO, request);
			
            // Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}

			// Si es un proceso con reportes parametriazados	
			if(procesoMasivoAdapterVO.getProcesoMasivo().getSeleccionFormularioEnabled()
				&& procesoMasivoAdapterVO.getProcesoMasivo().getFormulario() != null) {
 				for(ForCamVO campo: procesoMasivoAdapterVO.getProcesoMasivo().getFormulario().getListForCam()){
					campo.setValorDefecto(request.getParameter(campo.getCodForCam()));
				}
			}

			// llamada al servicio
			procesoMasivoAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().paramFormulario
					(userSession, procesoMasivoAdapterVO);
			
            // Tiene errores recuperables
			if (procesoMasivoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoMasivoAdapterVO.infoString()); 
				saveDemodaErrors(request, procesoMasivoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procesoMasivoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoMasivoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcesoMasivoAdapter.NAME, procesoMasivoAdapterVO);
			 
			saveDemodaMessages(request, procesoMasivoAdapterVO);

			// Fue Exitoso
			return mapping.findForward(GdeConstants.FWD_PROCESO_MASIVO_EDIT_ADAPTER);	
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoMasivoAdapter.NAME);
		}
	}

}
