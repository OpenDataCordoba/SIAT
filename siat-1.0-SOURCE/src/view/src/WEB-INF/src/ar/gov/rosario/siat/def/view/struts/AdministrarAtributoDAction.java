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

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.AtributoAdapter;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarAtributoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAtributoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AtributoAdapter atributoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAtributoAdapterForView(userSession, commonKey)";
				atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAtributoAdapterForUpdate(userSession, commonKey)";
				atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_ATRIBUTO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAtributoAdapterForView(userSession, commonKey)";
				atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterForView(userSession, commonKey);
				atributoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.ATRIBUTO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAtributoAdapterForCreate(userSession)";
				atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_ATRIBUTO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAtributoAdapterForView(userSession)";
				atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterForView(userSession, commonKey);				
				atributoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.ATRIBUTO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAtributoAdapterForView(userSession)";
				atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterForView(userSession, commonKey);					
				atributoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.ATRIBUTO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (atributoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			atributoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AtributoAdapter.NAME + ": "+ atributoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AtributoAdapter.NAME, atributoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AtributoAdapter.NAME, atributoAdapterVO);
			 
			saveDemodaMessages(request, atributoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(atributoAdapterVO, request);

            // Tiene errores recuperables
			if (atributoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// llamada al servicio
			AtributoVO atributoVO = DefServiceLocator.getAtributoService().createAtributo(userSession, atributoAdapterVO.getAtributo());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoVO.infoString()); 
				saveDemodaErrors(request, atributoVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AtributoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(atributoAdapterVO, request);
			
            // Tiene errores recuperables
			if (atributoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// llamada al servicio
			AtributoVO atributoVO = DefServiceLocator.getAtributoService().updateAtributo(userSession, atributoAdapterVO.getAtributo());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AtributoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoAdapter.NAME); 
			}

			// llamada al servicio
			AtributoVO atributoVO = DefServiceLocator.getAtributoService().deleteAtributo
				(userSession, atributoAdapterVO.getAtributo());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString());
				saveDemodaErrors(request, atributoVO);				
				request.setAttribute(AtributoAdapter.NAME, atributoAdapterVO);
				return mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AtributoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoAdapter.NAME); 
			}

			// llamada al servicio
			AtributoVO atributoVO = DefServiceLocator.getAtributoService().activarAtributo
				(userSession, atributoAdapterVO.getAtributo());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString());
				saveDemodaErrors(request, atributoVO);				
				request.setAttribute(AtributoAdapter.NAME, atributoAdapterVO);
				return mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AtributoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_ATRIBUTO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoAdapter.NAME); 
			}

			// llamada al servicio
			AtributoVO atributoVO = DefServiceLocator.getAtributoService().desactivarAtributo
				(userSession, atributoAdapterVO.getAtributo());
			
            // Tiene errores recuperables
			if (atributoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString());
				saveDemodaErrors(request, atributoVO);				
				request.setAttribute(AtributoAdapter.NAME, atributoAdapterVO);
				return mapping.findForward(DefConstants.FWD_ATRIBUTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (atributoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AtributoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AtributoAdapter.NAME);
		
	}
	
	public ActionForward paramTipoAtributo (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (atributoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AtributoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AtributoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(atributoAdapterVO, request);
			
            // Tiene errores recuperables
			if (atributoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// llamada al servicio
			atributoAdapterVO = DefServiceLocator.getAtributoService().getAtributoAdapterParamTipoAtributo(userSession, atributoAdapterVO);
			
            // Tiene errores recuperables
			if (atributoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + atributoAdapterVO.infoString()); 
				saveDemodaErrors(request, atributoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (atributoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + atributoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AtributoAdapter.NAME, atributoAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AtributoAdapter.NAME, atributoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AtributoAdapter.NAME, atributoAdapterVO);
			
			return mapping.findForward(DefConstants.FWD_ATRIBUTO_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AtributoAdapter.NAME);
		}
	}
	
	public ActionForward imprimirAtributo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
				
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
						
		// Bajo el adapter del userSession
		AtributoAdapter atributoAdapterVO = (AtributoAdapter) userSession.get(AtributoAdapter.NAME);		
		
		
/*
		PrintModel print0 = new PrintModel();
		print0.setRenderer(PrintModel.RENDER_PDF);
		print0.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		print0.setExcludeFileName("/publico/general/reportes/exclude.xml");
		print0.setXmlFileName("/publico/general/reportes/atributo.xml-" + userSession.getUserName());
		print0.setXslString("formularioVO.getXsl()");
		print0.setXmlData("formularioVO.getXml()");
		
		baseResponsePrintModel(response, print0);
*/      
		
		PrintModel print = new PrintModel();
		print.putCabecera("FileSharePath", SiatParam.getString("FileSharePath"));
		print.putFormulario("", "", ""); //cada for camp

		print.setExcludeFileName("/publico/general/reportes/exclude.xml");
						
		// Parametros para la renderizacion
		print.setTopeProfundidad(5);
		print.setData(atributoAdapterVO.getAtributo());
		
				
		byte[] content = print.getByteArray();
		response.setContentType("application/pdf");
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();
        
		return mapping.findForward(DefConstants.FWD_ATRIBUTO_EDIT_ADAPTER);
	}
}
