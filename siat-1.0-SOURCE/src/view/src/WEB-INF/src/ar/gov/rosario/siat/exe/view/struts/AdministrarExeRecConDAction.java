//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.exe.iface.model.ExeRecConAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExeRecConVO;
import ar.gov.rosario.siat.exe.iface.model.ExencionAdapter;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarExeRecConDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarExeRecConDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXERECCON, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ExeRecConAdapter exeRecConAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			ExencionAdapter exencionAdapter =  (ExencionAdapter) userSession.get(ExencionAdapter.NAME);
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getExeRecConAdapterForUpdate(userSession, commonKey)";
				exeRecConAdapterVO = ExeServiceLocator.getDefinicionService().getExeRecConAdapterForUpdate(userSession, commonKey);				
				actionForward = mapping.findForward(ExeConstants.FWD_EXERECCON_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getExeRecConAdapterForView(userSession, commonKey)";
				exeRecConAdapterVO = ExeServiceLocator.getDefinicionService().getExeRecConAdapterForView(userSession, commonKey);				
				exeRecConAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.EXERECCON_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_EXERECCON_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getExeRecConAdapterForCreate(userSession)";
				exeRecConAdapterVO = new ExeRecConAdapter();
				
				for(ExeRecConVO item:exencionAdapter.getExencion().getListExeRecCon()){
					exeRecConAdapterVO.getListVOExcluidos().add(item.getRecCon());
				}
				
				exeRecConAdapterVO.getExeRecCon().getExencion().setId(commonKey.getId());
				
				exeRecConAdapterVO = ExeServiceLocator.getDefinicionService().getExeRecConAdapterForCreate(userSession, exeRecConAdapterVO);
				actionForward = mapping.findForward(ExeConstants.FWD_EXERECCON_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (exeRecConAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + exeRecConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			exeRecConAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ExeRecConAdapter.NAME + ": "+ exeRecConAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ExeRecConAdapter.NAME, exeRecConAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ExeRecConAdapter.NAME, exeRecConAdapterVO);
			 
			saveDemodaMessages(request, exeRecConAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExeRecConAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExeRecConAdapter exeRecConAdapterVO = (ExeRecConAdapter) userSession.get(ExeRecConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (exeRecConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExeRecConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExeRecConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(exeRecConAdapterVO, request);
			
            // Tiene errores recuperables
			if (exeRecConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exeRecConAdapterVO.infoString()); 
				saveDemodaErrors(request, exeRecConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// llamada al servicio
			ExeRecConVO exeRecConVO = ExeServiceLocator.getDefinicionService().createExeRecCon(userSession, exeRecConAdapterVO.getExeRecCon());
			
            // Tiene errores recuperables
			if (exeRecConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exeRecConVO.infoString()); 
				saveDemodaErrors(request, exeRecConVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (exeRecConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exeRecConVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExeRecConAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExeRecConAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExeRecConAdapter exeRecConAdapterVO = (ExeRecConAdapter) userSession.get(ExeRecConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (exeRecConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExeRecConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExeRecConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(exeRecConAdapterVO, request);
			
            // Tiene errores recuperables
			if (exeRecConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exeRecConAdapterVO.infoString()); 
				saveDemodaErrors(request, exeRecConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// llamada al servicio
			ExeRecConVO exeRecConVO = ExeServiceLocator.getDefinicionService().updateExeRecCon(userSession, exeRecConAdapterVO.getExeRecCon());
			
            // Tiene errores recuperables
			if (exeRecConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exeRecConAdapterVO.infoString()); 
				saveDemodaErrors(request, exeRecConVO);
				return forwardErrorRecoverable(mapping, request, userSession, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (exeRecConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exeRecConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExeRecConAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExeRecConAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXERECCON, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExeRecConAdapter exeRecConAdapterVO = (ExeRecConAdapter) userSession.get(ExeRecConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (exeRecConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExeRecConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExeRecConAdapter.NAME); 
			}

			// llamada al servicio
			ExeRecConVO exeRecConVO = ExeServiceLocator.getDefinicionService().deleteExeRecCon
				(userSession, exeRecConAdapterVO.getExeRecCon());
			
            // Tiene errores recuperables
			if (exeRecConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exeRecConAdapterVO.infoString());
				saveDemodaErrors(request, exeRecConVO);				
				request.setAttribute(ExeRecConAdapter.NAME, exeRecConAdapterVO);
				return mapping.findForward(ExeConstants.FWD_EXERECCON_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (exeRecConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exeRecConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExeRecConAdapter.NAME, exeRecConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExeRecConAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExeRecConAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ExeRecConAdapter.NAME);
		
	}
		
}
