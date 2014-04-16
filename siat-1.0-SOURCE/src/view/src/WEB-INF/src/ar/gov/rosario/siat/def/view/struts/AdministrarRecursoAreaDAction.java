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
import ar.gov.rosario.siat.def.iface.model.RecursoAreaAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoAreaVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecursoAreaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecursoAreaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSOAREA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RecursoAreaAdapter recursoAreaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRecursoAreaAdapterForView(userSession, commonKey)";
				recursoAreaAdapterVO = DefServiceLocator.getConfiguracionService().getRecursoAreaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECURSOAREA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecursoAreaAdapterForUpdate(userSession, commonKey)";
				recursoAreaAdapterVO = DefServiceLocator.getConfiguracionService().getRecursoAreaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECURSOAREA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecursoAreaAdapterForView(userSession, commonKey)";
				recursoAreaAdapterVO = DefServiceLocator.getConfiguracionService().getRecursoAreaAdapterForView(userSession, commonKey);				
				recursoAreaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECURSOAREA_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_RECURSOAREA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRecursoAreaAdapterForCreate(userSession)";
				recursoAreaAdapterVO = DefServiceLocator.getConfiguracionService().getRecursoAreaAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECURSOAREA_EDIT_ADAPTER);				
			}


			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recursoAreaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + recursoAreaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recursoAreaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecursoAreaAdapter.NAME + ": "+ recursoAreaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			 
			saveDemodaMessages(request, recursoAreaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAreaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecursoAreaAdapter recursoAreaAdapterVO = (RecursoAreaAdapter) userSession.get(RecursoAreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAreaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAreaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recursoAreaAdapterVO, request);
			
            // Tiene errores recuperables
			if (recursoAreaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAreaAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAreaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			// llamada al servicio
			RecursoAreaVO recursoAreaVO = DefServiceLocator.getConfiguracionService().createRecursoArea(userSession, recursoAreaAdapterVO.getRecursoArea());
			
            // Tiene errores recuperables
			if (recursoAreaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAreaVO.infoString()); 
				saveDemodaErrors(request, recursoAreaVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recursoAreaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoAreaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecursoAreaAdapter.NAME, 
					DefConstants.PATH_ADMINISTRAR_AREA, 
					BaseConstants.ACT_INICIALIZAR, 
					DefConstants.ACT_ADM_RECURSOAREA_ADAPTER);
			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAreaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecursoAreaAdapter recursoAreaAdapterVO = (RecursoAreaAdapter) userSession.get(RecursoAreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAreaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAreaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(recursoAreaAdapterVO, request);
			
            // Tiene errores recuperables
			if (recursoAreaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAreaAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAreaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			// llamada al servicio
			RecursoAreaVO recursoAreaVO = DefServiceLocator.getConfiguracionService().updateRecursoArea(userSession, recursoAreaAdapterVO.getRecursoArea());
			
            // Tiene errores recuperables
			if (recursoAreaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAreaAdapterVO.infoString()); 
				saveDemodaErrors(request, recursoAreaVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (recursoAreaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoAreaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			userSession.getNavModel().setSelectedId(""+ recursoAreaAdapterVO.getRecursoArea().getArea().getId());
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecursoAreaAdapter.NAME, 
					DefConstants.PATH_ADMINISTRAR_AREA, 
					BaseConstants.ACT_INICIALIZAR, 
					DefConstants.ACT_ADM_RECURSOAREA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAreaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSOAREA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			RecursoAreaAdapter recursoAreaAdapterVO = (RecursoAreaAdapter) userSession.get(RecursoAreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAreaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAreaAdapter.NAME); 
			}

			// llamada al servicio
			RecursoAreaVO recursoAreaVO = DefServiceLocator.getConfiguracionService().deleteRecursoArea(userSession, recursoAreaAdapterVO.getRecursoArea());
			
            // Tiene errores recuperables
			if (recursoAreaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoAreaAdapterVO.infoString());
				saveDemodaErrors(request, recursoAreaVO);				
				request.setAttribute(RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
				return mapping.findForward(DefConstants.FWD_RECURSOAREA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (recursoAreaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoAreaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAreaAdapter.NAME, recursoAreaAdapterVO);
			}
			
			
			userSession.getNavModel().setSelectedId(""+ recursoAreaAdapterVO.getRecursoArea().getArea().getId());
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, RecursoAreaAdapter.NAME, 
					DefConstants.PATH_ADMINISTRAR_AREA, 
					BaseConstants.ACT_INICIALIZAR, 
					DefConstants.ACT_ADM_RECURSOAREA_ADAPTER);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoAreaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = "volver";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
				
		try {
		
			userSession.remove(RecursoAreaAdapter.NAME);
			NavModel navModel = userSession.getNavModel();
			
			navModel.setPrevAction("/def/BuscarArea");
			navModel.setPrevActionParameter("buscar");
			
			navModel.setAct(DefConstants.ACT_ADM_RECURSOAREA_ADAPTER);
			return mapping.findForward(DefConstants.ACTION_ADMINISTRAR_AREA);
			
		} catch (Exception e) {
			log.error("Exception - ", e);
			e.printStackTrace();
			// falta definir llamada o no a logout 
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
	
}
