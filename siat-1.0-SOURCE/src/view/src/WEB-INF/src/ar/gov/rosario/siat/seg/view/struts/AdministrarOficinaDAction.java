//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.view.struts;

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
import ar.gov.rosario.siat.seg.iface.model.OficinaAdapter;
import ar.gov.rosario.siat.seg.iface.model.OficinaVO;
import ar.gov.rosario.siat.seg.iface.service.SegServiceLocator;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarOficinaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOficinaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OficinaAdapter oficinaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getOficinaAdapterForView(userSession, commonKey)";
				oficinaAdapterVO = SegServiceLocator.getSeguridadService().getOficinaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getOficinaAdapterForUpdate(userSession, commonKey)";
				oficinaAdapterVO = SegServiceLocator.getSeguridadService().getOficinaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SegConstants.FWD_OFICINA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getOficinaAdapterForView(userSession, commonKey)";
				oficinaAdapterVO = SegServiceLocator.getSeguridadService().getOficinaAdapterForView(userSession, commonKey);				
				oficinaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, SegError.OFICINA_LABEL);
				actionForward = mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getOficinaAdapterForCreate(userSession)";
				oficinaAdapterVO = SegServiceLocator.getSeguridadService().getOficinaAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(SegConstants.FWD_OFICINA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getOficinaAdapterForView(userSession)";
				oficinaAdapterVO = SegServiceLocator.getSeguridadService().getOficinaAdapterForView(userSession, commonKey);
				oficinaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, SegError.OFICINA_LABEL);
				actionForward = mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getOficinaAdapterForView(userSession)";
				oficinaAdapterVO = SegServiceLocator.getSeguridadService().getOficinaAdapterForView(userSession, commonKey);
				oficinaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, SegError.OFICINA_LABEL);
				actionForward = mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (oficinaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + oficinaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			oficinaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OficinaAdapter.NAME + ": "+ oficinaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OficinaAdapter.NAME, oficinaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OficinaAdapter.NAME, oficinaAdapterVO);
			 
			saveDemodaMessages(request, oficinaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OficinaAdapter oficinaAdapterVO = (OficinaAdapter) userSession.get(OficinaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (oficinaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OficinaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OficinaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(oficinaAdapterVO, request);
			
            // Tiene errores recuperables
			if (oficinaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaAdapterVO.infoString()); 
				saveDemodaErrors(request, oficinaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// llamada al servicio
			OficinaVO oficinaVO = SegServiceLocator.getSeguridadService().createOficina(userSession, oficinaAdapterVO.getOficina());
			
            // Tiene errores recuperables
			if (oficinaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaVO.infoString()); 
				saveDemodaErrors(request, oficinaVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (oficinaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OficinaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OficinaAdapter oficinaAdapterVO = (OficinaAdapter) userSession.get(OficinaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (oficinaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OficinaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OficinaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(oficinaAdapterVO, request);
			
            // Tiene errores recuperables
			if (oficinaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaAdapterVO.infoString()); 
				saveDemodaErrors(request, oficinaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// llamada al servicio
			OficinaVO oficinaVO = SegServiceLocator.getSeguridadService().updateOficina(userSession, oficinaAdapterVO.getOficina());
			
            // Tiene errores recuperables
			if (oficinaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaAdapterVO.infoString()); 
				saveDemodaErrors(request, oficinaVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (oficinaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OficinaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OficinaAdapter oficinaAdapterVO = (OficinaAdapter) userSession.get(OficinaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (oficinaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OficinaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OficinaAdapter.NAME); 
			}

			// llamada al servicio
			OficinaVO oficinaVO = SegServiceLocator.getSeguridadService().deleteOficina
				(userSession, oficinaAdapterVO.getOficina());
			
            // Tiene errores recuperables
			if (oficinaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaAdapterVO.infoString());
				saveDemodaErrors(request, oficinaVO);				
				request.setAttribute(OficinaAdapter.NAME, oficinaAdapterVO);
				return mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (oficinaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OficinaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OficinaAdapter oficinaAdapterVO = (OficinaAdapter) userSession.get(OficinaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (oficinaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OficinaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OficinaAdapter.NAME); 
			}

			// llamada al servicio
			OficinaVO oficinaVO = SegServiceLocator.getSeguridadService().activarOficina
				(userSession, oficinaAdapterVO.getOficina());
			
            // Tiene errores recuperables
			if (oficinaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaAdapterVO.infoString());
				saveDemodaErrors(request, oficinaVO);				
				request.setAttribute(OficinaAdapter.NAME, oficinaAdapterVO);
				return mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (oficinaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OficinaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OficinaAdapter oficinaAdapterVO = (OficinaAdapter) userSession.get(OficinaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (oficinaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OficinaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OficinaAdapter.NAME); 
			}

			// llamada al servicio
			OficinaVO oficinaVO = SegServiceLocator.getSeguridadService().desactivarOficina
				(userSession, oficinaAdapterVO.getOficina());
			
            // Tiene errores recuperables
			if (oficinaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaAdapterVO.infoString());
				saveDemodaErrors(request, oficinaVO);				
				request.setAttribute(OficinaAdapter.NAME, oficinaAdapterVO);
				return mapping.findForward(SegConstants.FWD_OFICINA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (oficinaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaAdapter.NAME, oficinaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OficinaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OficinaAdapter.NAME);	
	}		
}
