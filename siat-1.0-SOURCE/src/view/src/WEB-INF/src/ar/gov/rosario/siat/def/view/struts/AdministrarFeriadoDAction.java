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
import ar.gov.rosario.siat.def.iface.model.FeriadoAdapter;
import ar.gov.rosario.siat.def.iface.model.FeriadoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarFeriadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFeriadoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_FERIADO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		FeriadoAdapter feriadoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getFeriadoAdapterForView(userSession, commonKey)";
				feriadoAdapterVO = DefServiceLocator.getGravamenService().getFeriadoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_FERIADO_VIEW_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getFeriadoAdapterForCreate(userSession)";
				feriadoAdapterVO = DefServiceLocator.getGravamenService().getFeriadoAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_FERIADO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getFeriadoAdapterForView(userSession)";
				feriadoAdapterVO = DefServiceLocator.getGravamenService().getFeriadoAdapterForView(userSession, commonKey);
				feriadoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.FERIADO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_FERIADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getFeriadoAdapterForView(userSession)";
				feriadoAdapterVO = DefServiceLocator.getGravamenService().getFeriadoAdapterForView(userSession, commonKey);
				feriadoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.FERIADO_LABEL); 				
				actionForward = mapping.findForward(DefConstants.FWD_FERIADO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (feriadoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + feriadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FeriadoAdapter.NAME, feriadoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			feriadoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + FeriadoAdapter.NAME + ": "+ feriadoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(FeriadoAdapter.NAME, feriadoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FeriadoAdapter.NAME, feriadoAdapterVO);
			 
			saveDemodaMessages(request, feriadoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FeriadoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_FERIADO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FeriadoAdapter feriadoAdapterVO = (FeriadoAdapter) userSession.get(FeriadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (feriadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FeriadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FeriadoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(feriadoAdapterVO, request);
			
            // Tiene errores recuperables
			if (feriadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoAdapterVO.infoString()); 
				saveDemodaErrors(request, feriadoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FeriadoAdapter.NAME, feriadoAdapterVO);
			}
			
			// llamada al servicio
			FeriadoVO feriadoVO = DefServiceLocator.getGravamenService().createFeriado(userSession, feriadoAdapterVO.getFeriado());
			
            // Tiene errores recuperables
			if (feriadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoVO.infoString()); 
				saveDemodaErrors(request, feriadoVO);
				return forwardErrorRecoverable(mapping, request, userSession, FeriadoAdapter.NAME, feriadoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (feriadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + feriadoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FeriadoAdapter.NAME, feriadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FeriadoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FeriadoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_FERIADO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FeriadoAdapter feriadoAdapterVO = (FeriadoAdapter) userSession.get(FeriadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (feriadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FeriadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FeriadoAdapter.NAME); 
			}

			// llamada al servicio
			FeriadoVO feriadoVO = DefServiceLocator.getGravamenService().activarFeriado
				(userSession, feriadoAdapterVO.getFeriado());
			
            // Tiene errores recuperables
			if (feriadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoAdapterVO.infoString());
				saveDemodaErrors(request, feriadoVO);				
				request.setAttribute(FeriadoAdapter.NAME, feriadoAdapterVO);
				return mapping.findForward(DefConstants.FWD_FERIADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (feriadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + feriadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FeriadoAdapter.NAME, feriadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FeriadoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FeriadoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_FERIADO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FeriadoAdapter feriadoAdapterVO = (FeriadoAdapter) userSession.get(FeriadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (feriadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FeriadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FeriadoAdapter.NAME); 
			}

			// llamada al servicio
			FeriadoVO feriadoVO = DefServiceLocator.getGravamenService().desactivarFeriado
				(userSession, feriadoAdapterVO.getFeriado());
			
            // Tiene errores recuperables
			if (feriadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + feriadoAdapterVO.infoString());
				saveDemodaErrors(request, feriadoVO);				
				request.setAttribute(FeriadoAdapter.NAME, feriadoAdapterVO);
				return mapping.findForward(DefConstants.FWD_FERIADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (feriadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + feriadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FeriadoAdapter.NAME, feriadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FeriadoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FeriadoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FeriadoAdapter.NAME);
		
	}
		
}
