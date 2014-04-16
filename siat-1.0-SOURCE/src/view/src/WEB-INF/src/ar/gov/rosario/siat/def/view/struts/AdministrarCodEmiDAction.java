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
import ar.gov.rosario.siat.def.iface.model.CodEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.CodEmiVO;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCodEmiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCodEmiDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CODEMI, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CodEmiAdapter codEmiAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCodEmiAdapterForView(userSession, commonKey)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCodEmiAdapterForUpdate(userSession, commonKey)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCodEmiAdapterForDelete(userSession, commonKey)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForView
					(userSession, commonKey);
				codEmiAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.CODEMI_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCodEmiAdapterForView(userSession)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForView
					(userSession, commonKey);
				codEmiAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.CODEMI_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCodEmiAdapterForView(userSession)";
				codEmiAdapterVO = DefServiceLocator.getEmisionService().getCodEmiAdapterForView
					(userSession, commonKey);
				codEmiAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.CODEMI_LABEL);			
				actionForward = mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (codEmiAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			codEmiAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				CodEmiAdapter.NAME + ": " + codEmiAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CodEmiAdapter.NAME, codEmiAdapterVO);

			// Subo el apdater al userSession
			userSession.put(CodEmiAdapter.NAME, codEmiAdapterVO);
			
			saveDemodaMessages(request, codEmiAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			DefConstants.ACTION_ADMINISTRAR_ENC_CODEMI, BaseConstants.ACT_MODIFICAR);
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CODEMI, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.NAME); 
			}

			// llamada al servicio
			CodEmiVO codEmiVO = DefServiceLocator.getEmisionService().deleteCodEmi
				(userSession, codEmiAdapterVO.getCodEmi());
			
            // Tiene errores recuperables
			if (codEmiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString());
				saveDemodaErrors(request, codEmiVO);				
				request.setAttribute(CodEmiAdapter.NAME, codEmiAdapterVO);
				return mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (codEmiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CODEMI, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.NAME); 
			}

			// llamada al servicio
			CodEmiVO codEmiVO = DefServiceLocator.getEmisionService().activarCodEmi
				(userSession, codEmiAdapterVO.getCodEmi());
			
            // Tiene errores recuperables
			if (codEmiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString());
				saveDemodaErrors(request, codEmiVO);				
				request.setAttribute(CodEmiAdapter.NAME, codEmiAdapterVO);
				return mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (codEmiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CODEMI, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.NAME); 
			}

			// llamada al servicio
			CodEmiVO codEmiVO = DefServiceLocator.getEmisionService().desactivarCodEmi
				(userSession, codEmiAdapterVO.getCodEmi());
			
            // Tiene errores recuperables
			if (codEmiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString());
				saveDemodaErrors(request, codEmiVO);				
				request.setAttribute(CodEmiAdapter.NAME, codEmiAdapterVO);
				return mapping.findForward(DefConstants.FWD_CODEMI_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (codEmiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CodEmiAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CodEmiAdapter.NAME);
		
	}
	
	public ActionForward guardar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_CODEMI, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (codEmiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CodEmiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(codEmiAdapterVO, request);
			
            // Tiene errores recuperables
			if (codEmiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// llamada al servicio
			CodEmiVO codEmiVO = DefServiceLocator.getEmisionService().updateCodEmi(userSession, codEmiAdapterVO.getCodEmi());
			
            // Tiene errores recuperables
			if (codEmiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
				saveDemodaErrors(request, codEmiVO);
				return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (codEmiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.NAME, codEmiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CodEmiAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CodEmiAdapter.NAME);
		}
	}
	
	public ActionForward testCode(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_CODEMI, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CodEmiAdapter codEmiAdapterVO = (CodEmiAdapter) userSession.get(CodEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (codEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CodEmiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CodEmiAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(codEmiAdapterVO, request);
				
				// populado de atributos
				for (GenericAtrDefinition genericAtrDefinition:
					codEmiAdapterVO.getCodEmi().getRecAtrCueEmiDefinition().getListGenericAtrDefinition()) {
					DefinitionUtil.populateAtrVal4Edit(genericAtrDefinition, request);				
				}

				// Tiene errores recuperables
				if (codEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, codEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.NAME, codEmiAdapterVO);
				}
				
				// llamada al servicio
				 codEmiAdapterVO = DefServiceLocator.getEmisionService().testCodEmi(userSession, codEmiAdapterVO);
				
								 
	            // Tiene errores recuperables
				if (codEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + codEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, codEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CodEmiAdapter.NAME, codEmiAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (codEmiAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + codEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CodEmiAdapter.NAME, codEmiAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(CodEmiAdapter.NAME, codEmiAdapterVO);

				// Subo el apdater al userSession
				userSession.put(CodEmiAdapter.NAME, codEmiAdapterVO);
				
				saveDemodaMessages(request, codEmiAdapterVO);			

				// Fue Exitoso
				return mapping.findForward(DefConstants.FWD_CODEMI_ADAPTER);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CodEmiAdapter.NAME);
			}
		}
}
	
