//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

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
import ar.gov.rosario.siat.def.buss.bean.TipoEmision;
import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;
import ar.gov.rosario.siat.emi.iface.model.EmisionAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiError;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEmisionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionDAction.class);

	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		// Obtenemos el Tipo de Emision del request
		Long idTipoEmision = Long.parseLong(request.getParameter(TipoEmisionVO.ID_TIPOEMISION));
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.getById(idTipoEmision), act); 
		if (userSession == null) return forwardErrorSession(request);
		
		// Guardamos el id del tipo de Emision en el User Session
		userSession.put(TipoEmisionVO.ID_TIPOEMISION, idTipoEmision);
		
		NavModel navModel = userSession.getNavModel();
		EmisionAdapter emisionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEmisionAdapterForView(userSession, commonKey)";
				emisionAdapterVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision)
					.getEmisionAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISION_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEmisionAdapterForUpdate(userSession, commonKey)";
				emisionAdapterVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision)
					.getEmisionAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISION_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEmisionAdapterForView(userSession, commonKey)";
				emisionAdapterVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision)
					.getEmisionAdapterForView(userSession, commonKey);				
				emisionAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EmiError.EMISION_LABEL);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISION_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEmisionAdapterForCreate(userSession)";
				emisionAdapterVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision)
					.getEmisionAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISION_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emisionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionAdapter.NAME + ": "+ emisionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionAdapter.NAME, emisionAdapterVO);
			
			// Envio el Tipo de Emision al request
			request.setAttribute(TipoEmisionVO.ID_TIPOEMISION, idTipoEmision);

			// Subo el apdater al userSession
			userSession.put(EmisionAdapter.NAME, emisionAdapterVO);
			 
			saveDemodaMessages(request, emisionAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		// Determinamos el Tipo de Emision pasado como parametro desde el Menu
		Long idTipoEmisionFromReq = new Long(request.getParameter(TipoEmisionVO.ID_TIPOEMISION));
		if (log.isDebugEnabled()) log.debug("Tipo de Emision del Request " + idTipoEmisionFromReq);
		
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.getById(idTipoEmisionFromReq), BaseSecurityConstants.AGREGAR); 
		if (userSession == null) return forwardErrorSession(request);

		try {
			
			//Determinamos el Tipo de Emision
			Long idTipoEmision = (Long) userSession.get(TipoEmisionVO.ID_TIPOEMISION);
			// Bajo el adapter del userSession
			EmisionAdapter emisionAdapterVO = (EmisionAdapter) userSession.get(EmisionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionAdapter.NAME); 
			}

			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionAdapterVO.infoString());
				saveDemodaErrors(request, emisionAdapterVO);
				// Envio el VO al request
				request.setAttribute(EmisionAdapter.NAME, emisionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(EmisionAdapter.NAME, emisionAdapterVO);

				return forwardErrorRecoverable(mapping, request, userSession, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision).createEmision
				(userSession, emisionAdapterVO);
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//Determinamos el Tipo de Emision pasado como parametro desde el Menu
		Long idTipoEmisionFromReq = new Long(request.getParameter(TipoEmisionVO.ID_TIPOEMISION));
		if (log.isDebugEnabled()) log.debug("Tipo de Emision del Request " + idTipoEmisionFromReq);
		
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.getById(idTipoEmisionFromReq), BaseSecurityConstants.MODIFICAR); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			//Determinamos el Tipo de Emision
			Long idTipoEmision = (Long) userSession.get(TipoEmisionVO.ID_TIPOEMISION);
			
			// Bajo el adapter del userSession
			EmisionAdapter emisionAdapterVO = (EmisionAdapter) userSession.get(EmisionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision).updateEmision
				(userSession, emisionAdapterVO);
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		//Determinamos el Tipo de Emision pasado como parametro desde el Menu
		Long idTipoEmisionFromReq = new Long(request.getParameter(TipoEmisionVO.ID_TIPOEMISION));
		if (log.isDebugEnabled()) log.debug("Tipo de Emision del Request " + idTipoEmisionFromReq);
		
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.getById(idTipoEmisionFromReq), BaseSecurityConstants.ELIMINAR); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {

			//Determinamos el Tipo de Emision
			Long idTipoEmision = (Long) userSession.get(TipoEmisionVO.ID_TIPOEMISION);
			
			// Bajo el adapter del userSession
			EmisionAdapter emisionAdapterVO = (EmisionAdapter) userSession.get(EmisionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionAdapter.NAME); 
			}

			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision).deleteEmision
				(userSession, emisionAdapterVO.getEmision());
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionAdapterVO.infoString());
				saveDemodaErrors(request, emisionVO);				
				request.setAttribute(EmisionAdapter.NAME, emisionAdapterVO);
				return mapping.findForward(EmiConstants.FWD_EMISION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionAdapter.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//Determinamos el Tipo de Emision pasado como parametro desde el Menu
		Long idTipoEmisionFromReq = new Long(request.getParameter(TipoEmision.ID_TIPOEMISION));
		if (log.isDebugEnabled()) log.debug("Tipo de Emision del Request " + idTipoEmisionFromReq);
		
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.getById(idTipoEmisionFromReq), BaseSecurityConstants.AGREGAR); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//Determinamos el Tipo de Emision
			Long idTipoEmision = (Long) userSession.get(TipoEmision.ID_TIPOEMISION);
			// Bajo el adapter del userSession
			EmisionAdapter emisionAdapterVO = (EmisionAdapter) userSession.get(EmisionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// llamada al servicio
			emisionAdapterVO = EmiServiceLocator.getEmisionServiceBy(idTipoEmision).paramRecurso
				(userSession, emisionAdapterVO);
			
            // Tiene errores recuperables
			if (emisionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionAdapter.NAME, emisionAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EmisionAdapter.NAME, emisionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionAdapter.NAME, emisionAdapterVO);
			 
			saveDemodaMessages(request, emisionAdapterVO);

			// Fue Exitoso
			return mapping.findForward(EmiConstants.FWD_EMISION_EDIT_ADAPTER);	
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)  throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionAdapter.NAME);
	}


}
