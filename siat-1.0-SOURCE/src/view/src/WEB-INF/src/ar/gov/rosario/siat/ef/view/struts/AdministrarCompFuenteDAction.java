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
import ar.gov.rosario.siat.ef.iface.model.CompFuenteAdapter;
import ar.gov.rosario.siat.ef.iface.model.CompFuenteVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCompFuenteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCompFuenteDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPFUENTE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CompFuenteAdapter compFuenteAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCompFuenteAdapterForView(userSession, commonKey)";
				compFuenteAdapterVO = EfServiceLocator.getFiscalizacionService().getCompFuenteAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMPFUENTE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCompFuenteAdapterForView(userSession, commonKey)";
				compFuenteAdapterVO = EfServiceLocator.getFiscalizacionService().getCompFuenteAdapterForView(userSession, commonKey);				
				compFuenteAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.COMPFUENTE_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_COMPFUENTE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCompFuenteAdapterForCreate(userSession)";
				compFuenteAdapterVO = EfServiceLocator.getFiscalizacionService().getCompFuenteAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMPFUENTE_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCompFuenteAdapterForView(userSession)";
				compFuenteAdapterVO = EfServiceLocator.getFiscalizacionService().getCompFuenteAdapterForView(userSession, commonKey);
				compFuenteAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EfError.COMPFUENTE_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_COMPFUENTE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCompFuenteAdapterForView(userSession)";
				compFuenteAdapterVO = EfServiceLocator.getFiscalizacionService().getCompFuenteAdapterForView(userSession, commonKey);
				compFuenteAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EfError.COMPFUENTE_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_COMPFUENTE_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (compFuenteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + compFuenteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			compFuenteAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CompFuenteAdapter.NAME + ": "+ compFuenteAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CompFuenteAdapter.NAME, compFuenteAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CompFuenteAdapter.NAME, compFuenteAdapterVO);
			 
			saveDemodaMessages(request, compFuenteAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompFuenteAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPFUENTE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompFuenteAdapter compFuenteAdapterVO = (CompFuenteAdapter) userSession.get(CompFuenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (compFuenteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompFuenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompFuenteAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(compFuenteAdapterVO, request);
			
            // Tiene errores recuperables
			if (compFuenteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compFuenteAdapterVO.infoString()); 
				saveDemodaErrors(request, compFuenteAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// llamada al servicio
			CompFuenteVO compFuenteVO = EfServiceLocator.getFiscalizacionService().createCompFuente(userSession, compFuenteAdapterVO.getCompFuente());
			
            // Tiene errores recuperables
			if (compFuenteVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compFuenteVO.infoString()); 
				saveDemodaErrors(request, compFuenteVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (compFuenteVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compFuenteVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CompFuenteAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompFuenteAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName+"       selectedId:"+request.getParameter("selectedId"));
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPFUENTE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompFuenteAdapter compFuenteAdapterVO = (CompFuenteAdapter) userSession.get(CompFuenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (compFuenteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompFuenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompFuenteAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(compFuenteAdapterVO, request);

			// llamada al servicio
			CompFuenteVO compFuenteVO = EfServiceLocator.getFiscalizacionService().deleteCompFuente
				(userSession, compFuenteAdapterVO.getCompFuente());
			
            // Tiene errores recuperables
			if (compFuenteVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compFuenteAdapterVO.infoString());
				saveDemodaErrors(request, compFuenteVO);				
				request.setAttribute(CompFuenteAdapter.NAME, compFuenteAdapterVO);
				return mapping.findForward(EfConstants.FWD_COMPFUENTE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (compFuenteVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compFuenteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CompFuenteAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompFuenteAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CompFuenteAdapter.NAME);
		
	}
	
	public ActionForward paramFuenteInfo (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompFuenteAdapter compFuenteAdapterVO = (CompFuenteAdapter) userSession.get(CompFuenteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (compFuenteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompFuenteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompFuenteAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(compFuenteAdapterVO, request);
			
            // Tiene errores recuperables
			if (compFuenteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compFuenteAdapterVO.infoString()); 
				saveDemodaErrors(request, compFuenteAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// llamada al servicio
			compFuenteAdapterVO = EfServiceLocator.getFiscalizacionService().getCompFuenteAdapterParamPlaFueDat(userSession, compFuenteAdapterVO);
			
            // Tiene errores recuperables
			if (compFuenteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compFuenteAdapterVO.infoString()); 
				saveDemodaErrors(request, compFuenteAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (compFuenteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compFuenteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompFuenteAdapter.NAME, compFuenteAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CompFuenteAdapter.NAME, compFuenteAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CompFuenteAdapter.NAME, compFuenteAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_COMPFUENTE_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompFuenteAdapter.NAME);
		}
	}
			
}
