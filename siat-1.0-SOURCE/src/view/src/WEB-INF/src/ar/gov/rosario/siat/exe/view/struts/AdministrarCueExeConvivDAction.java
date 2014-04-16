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
import ar.gov.rosario.siat.exe.iface.model.ConvivienteVO;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeConvivAdapter;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCueExeConvivDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCueExeConvivDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExeConvivAdapter cueExeConvivAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCueExeAdapterForView(userSession, commonKey)";
				cueExeConvivAdapterVO = ExeServiceLocator.getExencionService().getCueExeConvivAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CUEEXECONVIV_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExeConvivAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExeConvivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeConvivAdapter.NAME, cueExeConvivAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExeConvivAdapterVO.setValuesFromNavModel(navModel);
								
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExeConvivAdapter.NAME + ": "+ cueExeConvivAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExeConvivAdapter.NAME, cueExeConvivAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeConvivAdapter.NAME, cueExeConvivAdapterVO);
			 
			saveDemodaMessages(request, cueExeConvivAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.NAME);
		}
	}
	
	public ActionForward irAgregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeConvivAdapter == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeConvivAdapter, request);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExeConvivAdapter.setValuesFromNavModel(userSession.getNavModel());
			cueExeConvivAdapter.setAct(BaseSecurityConstants.AGREGAR);
			
			// Envio el VO al request
			request.setAttribute(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			// Subo el apdater al userSession
			userSession.put(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			 
			saveDemodaMessages(request, cueExeConvivAdapter);

			return mapping.findForward(ExeConstants.FWD_CUEEXECONVIV_EDITADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeConvivAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeConvivAdapter == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeConvivAdapter, request);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// llamada al servicio
			cueExeConvivAdapter = ExeServiceLocator.getExencionService().createCueExeConviv(userSession, cueExeConvivAdapter);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Tiene errores no recuperables
			if (cueExeConvivAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeConvivAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			setearValores4Volver(userSession, cueExeConvivAdapter);			

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeConvivAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeConvivAdapter.NAME);
		}
	}

	public ActionForward irModificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeConvivAdapter == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeConvivAdapter, request);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Obtiene el id del conviviente seleccionado
			String id = request.getParameter("selectedId");
			CommonKey commonKey = new CommonKey(id);
			
			// llamada al servicio
			cueExeConvivAdapter = ExeServiceLocator.getExencionService().getCueExeConvivAdapterForUpdate(userSession, commonKey);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Tiene errores no recuperables
			if (cueExeConvivAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeConvivAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExeConvivAdapter.setValuesFromNavModel(userSession.getNavModel());
			cueExeConvivAdapter.setAct(BaseSecurityConstants.MODIFICAR);
			cueExeConvivAdapter.setPrevAction(ExeConstants.ACTION_ADMINISTRAR_CUEEXE_CONVIV);
			cueExeConvivAdapter.setPrevActionParameter(BaseConstants.METHOD_INICIALIZAR);
			
			// Envio el VO al request
			request.setAttribute(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			// Subo el apdater al userSession
			userSession.put(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			 
			saveDemodaMessages(request, cueExeConvivAdapter);

			return mapping.findForward(ExeConstants.FWD_CUEEXECONVIV_EDITADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeConvivAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeConvivAdapter == null) {
				log.error("error en: "  + funcName + ": " + CueExeConvivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeConvivAdapter, request);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// llamada al servicio
			ConvivienteVO convivienteVO = ExeServiceLocator.getExencionService().updateCueExeConviv(userSession, cueExeConvivAdapter.getConviviente());
			
			cueExeConvivAdapter.setConviviente(convivienteVO);
			convivienteVO.passErrorMessages(cueExeConvivAdapter);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Tiene errores no recuperables
			if (cueExeConvivAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeConvivAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// setea los valores para volver a la vista de convivientes
			setearValores4Volver(userSession, cueExeConvivAdapter);			
			
			// Envio el VO al request
			request.setAttribute(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			// Subo el apdater al userSession
			userSession.put(CueExeConvivAdapter.NAME, cueExeConvivAdapter);

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeConvivAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeConvivAdapter.NAME);
		}
	}

	private void setearValores4Volver(UserSession userSession,
			CueExeConvivAdapter cueExeConvivAdapter) {
		cueExeConvivAdapter.setPrevAction("/exe/AdministrarCueExeConviv");
		cueExeConvivAdapter.setPrevActionParameter("inicializar");
		cueExeConvivAdapter.setAct(BaseSecurityConstants.VER);
		userSession.getNavModel().setSelectedId(String.valueOf(cueExeConvivAdapter.getCueExe().getId().longValue()));
	}

	public ActionForward irEliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeConvivAdapter == null) {
				log.error("error en: "  + funcName + ": " + CueExeConvivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeConvivAdapter, request);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Obtiene el id del conviviente seleccionado
			String id = request.getParameter("selectedId");
			CommonKey commonKey = new CommonKey(id);
			
			// llamada al servicio
			ConvivienteVO convivienteVO = ExeServiceLocator.getExencionService().getCueExeConvivAdapterForDelete(userSession, commonKey);
			
			cueExeConvivAdapter.setConviviente(convivienteVO);
			convivienteVO.passErrorMessages(cueExeConvivAdapter);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Tiene errores no recuperables
			if (cueExeConvivAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeConvivAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Setea el msg
			cueExeConvivAdapter.addMessage(BaseError.MSG_ELIMINAR, ExeError.CUEEXECONVIVIENTE_LABEL);
			saveDemodaMessages(request, cueExeConvivAdapter);

			cueExeConvivAdapter.setAct(BaseSecurityConstants.ELIMINAR);
			
			// Envio el VO al request
			request.setAttribute(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			// Subo el apdater al userSession
			userSession.put(CueExeConvivAdapter.NAME, cueExeConvivAdapter);

			return mapping.findForward(ExeConstants.FWD_CUEEXECONVIV_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeConvivAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeConvivAdapter == null) {
				log.error("error en: "  + funcName + ": " + CueExeConvivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeConvivAdapter, request);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
						
			// llamada al servicio
			ConvivienteVO convivienteVO = ExeServiceLocator.getExencionService().deleteCueExeConviv(userSession, cueExeConvivAdapter);
			
			cueExeConvivAdapter.setConviviente(convivienteVO);
			convivienteVO.passErrorMessages(cueExeConvivAdapter);
			
            // Tiene errores recuperables
			if (cueExeConvivAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeConvivAdapter.infoString()); 
				saveDemodaErrors(request, cueExeConvivAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			// Tiene errores no recuperables
			if (cueExeConvivAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeConvivAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			}
			
			setearValores4Volver(userSession, cueExeConvivAdapter);			
			
			
			// Envio el VO al request
			request.setAttribute(CueExeConvivAdapter.NAME, cueExeConvivAdapter);
			// Subo el apdater al userSession
			userSession.put(CueExeConvivAdapter.NAME, cueExeConvivAdapter);

			// Fue Exitoso			
			return forwardConfirmarOk(mapping, request, funcName, CueExeConvivAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeConvivAdapter.NAME);
		}
	}

	public ActionForward volverAVer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXECONVIV, BaseSecurityConstants.VER); 
		if (userSession == null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		CueExeConvivAdapter cueExeConvivAdapter = (CueExeConvivAdapter) userSession.get(CueExeConvivAdapter.NAME);
		
		
		String path = "/exe/AdministrarCueExeConviv.do?method=inicializar&selectedId="+String.valueOf(cueExeConvivAdapter.getCueExe().getId().longValue());		
		return new ActionForward(path);
			
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExeConvivAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeAdapter.NAME);
		
	}
	

}
