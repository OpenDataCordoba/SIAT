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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.CategoriaAdapter;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCategoriaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCategoriaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CategoriaAdapter categoriaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCategoriaAdapterForView(userSession, commonKey)";
				categoriaAdapterVO = DefServiceLocator.getGravamenService().getCategoriaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCategoriaAdapterForUpdate(userSession, commonKey)";
				categoriaAdapterVO = DefServiceLocator.getGravamenService().getCategoriaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CATEGORIA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCategoriaAdapterForView(userSession, commonKey)";
				categoriaAdapterVO = DefServiceLocator.getGravamenService().getCategoriaAdapterForView(userSession, commonKey);
				categoriaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.CATEGORIA_LABEL);					
				actionForward = mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCategoriaAdapterForCreate(userSession)";
				categoriaAdapterVO = DefServiceLocator.getGravamenService().getCategoriaAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_CATEGORIA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCategoriaAdapterForView(userSession)";
				categoriaAdapterVO = DefServiceLocator.getGravamenService().getCategoriaAdapterForView(userSession, commonKey);
				categoriaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.CATEGORIA_LABEL);					
				actionForward = mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCategoriaAdapterForView(userSession)";
				categoriaAdapterVO = DefServiceLocator.getGravamenService().getCategoriaAdapterForView(userSession, commonKey);
				categoriaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.CATEGORIA_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);				
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (categoriaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + categoriaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			categoriaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CategoriaAdapter.NAME + ": "+ categoriaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CategoriaAdapter.NAME, categoriaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CategoriaAdapter.NAME, categoriaAdapterVO);
			
			saveDemodaMessages(request, categoriaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaAdapter.NAME);
		}
	}
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CategoriaAdapter categoriaAdapterVO = (CategoriaAdapter) userSession.get(CategoriaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (categoriaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CategoriaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CategoriaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(categoriaAdapterVO, request);

            // Tiene errores recuperables
			if (categoriaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaAdapterVO.infoString()); 
				saveDemodaErrors(request, categoriaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// llamada al servicio
			CategoriaVO categoriaVO = DefServiceLocator.getGravamenService().createCategoria(userSession, categoriaAdapterVO.getCategoria());
			
            // Tiene errores recuperables
			if (categoriaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaVO.infoString()); 
				saveDemodaErrors(request, categoriaVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (categoriaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CategoriaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CategoriaAdapter categoriaAdapterVO = (CategoriaAdapter) userSession.get(CategoriaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (categoriaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CategoriaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CategoriaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(categoriaAdapterVO, request);
			
            // Tiene errores recuperables
			if (categoriaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaAdapterVO.infoString()); 
				saveDemodaErrors(request, categoriaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// llamada al servicio
			CategoriaVO categoriaVO = DefServiceLocator.getGravamenService().updateCategoria(userSession, categoriaAdapterVO.getCategoria());
			
            // Tiene errores recuperables
			if (categoriaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaAdapterVO.infoString()); 
				saveDemodaErrors(request, categoriaVO);
				return forwardErrorRecoverable(mapping, request, userSession, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (categoriaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CategoriaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CategoriaAdapter categoriaAdapterVO = (CategoriaAdapter) userSession.get(CategoriaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (categoriaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CategoriaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CategoriaAdapter.NAME); 
			}

			// llamada al servicio
			CategoriaVO categoriaVO = DefServiceLocator.getGravamenService().deleteCategoria
				(userSession, categoriaAdapterVO.getCategoria());
			
            // Tiene errores recuperables
			if (categoriaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaAdapterVO.infoString());
				saveDemodaErrors(request, categoriaVO);				
				request.setAttribute(CategoriaAdapter.NAME, categoriaAdapterVO);
				return mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (categoriaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CategoriaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CategoriaAdapter categoriaAdapterVO = (CategoriaAdapter) userSession.get(CategoriaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (categoriaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CategoriaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CategoriaAdapter.NAME); 
			}

			// llamada al servicio
			CategoriaVO categoriaVO = DefServiceLocator.getGravamenService().activarCategoria
				(userSession, categoriaAdapterVO.getCategoria());
			
            // Tiene errores recuperables
			if (categoriaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaAdapterVO.infoString());
				saveDemodaErrors(request, categoriaVO);				
				request.setAttribute(CategoriaAdapter.NAME, categoriaAdapterVO);
				return mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (categoriaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CategoriaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaAdapter.NAME);
		}	
	}
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CATEGORIA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CategoriaAdapter categoriaAdapterVO = (CategoriaAdapter) userSession.get(CategoriaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (categoriaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CategoriaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CategoriaAdapter.NAME); 
			}

			// llamada al servicio
			CategoriaVO categoriaVO = DefServiceLocator.getGravamenService().desactivarCategoria
				(userSession, categoriaAdapterVO.getCategoria());
			
            // Tiene errores recuperables
			if (categoriaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + categoriaAdapterVO.infoString());
				saveDemodaErrors(request, categoriaVO);				
				request.setAttribute(CategoriaAdapter.NAME, categoriaAdapterVO);
				return mapping.findForward(DefConstants.FWD_CATEGORIA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (categoriaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + categoriaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CategoriaAdapter.NAME, categoriaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CategoriaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CategoriaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CategoriaAdapter.NAME);
			
	}
	


}
	
