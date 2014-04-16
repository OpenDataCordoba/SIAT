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
import ar.gov.rosario.siat.def.iface.model.DomAtrValAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrValVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDomAtrValDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDomAtrValDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		DomAtrValAdapter domAtrValAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDomAtrValAdapterForView(userSession, commonKey)";
				domAtrValAdapterVO = DefServiceLocator.getAtributoService().getDomAtrValAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATRVAL_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDomAtrValAdapterForUpdate(userSession, commonKey)";
				domAtrValAdapterVO = DefServiceLocator.getAtributoService().getDomAtrValAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATRVAL_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDomAtrValAdapterForDelete(userSession, commonKey)";
				domAtrValAdapterVO = DefServiceLocator.getAtributoService().getDomAtrValAdapterForView
					(userSession, commonKey);
				domAtrValAdapterVO.addMessage(BaseError.MSG_ELIMINAR ,DefError.DOMATRVAL_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_DOMATRVAL_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDomAtrValAdapterForCreate(userSession)";
				domAtrValAdapterVO = DefServiceLocator.getAtributoService().getDomAtrValAdapterForCreate
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATRVAL_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (domAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + domAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			domAtrValAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DomAtrValAdapter.NAME + ": "+ domAtrValAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DomAtrValAdapter.NAME, domAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DomAtrValAdapter.NAME, domAtrValAdapterVO);
			
			saveDemodaMessages(request, domAtrValAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrValAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,
			DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrValAdapter domAtrValAdapterVO = (DomAtrValAdapter) userSession.get(DomAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (domAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, domAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// llamada al servicio
			DomAtrValVO domAtrValVO = DefServiceLocator.getAtributoService().createDomAtrVal(userSession, domAtrValAdapterVO.getDomAtrVal());
			
            // Tiene errores recuperables
			if (domAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrValVO.infoString()); 
				saveDemodaErrors(request, domAtrValVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrValVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrValAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.AGREGAR);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrValAdapter domAtrValAdapterVO = (DomAtrValAdapter) userSession.get(DomAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrValAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (domAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, domAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// llamada al servicio
			DomAtrValVO domAtrValVO = DefServiceLocator.getAtributoService().updateDomAtrVal
				(userSession, domAtrValAdapterVO.getDomAtrVal());
			
            // Tiene errores recuperables
			if (domAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, domAtrValVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrValAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_VALOR, BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrValAdapter domAtrValAdapterVO = (DomAtrValAdapter) userSession.get(DomAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrValAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrValAdapter.NAME); 
			}

			// llamada al servicio
			DomAtrValVO domAtrValVO = DefServiceLocator.getAtributoService().deleteDomAtrVal
				(userSession, domAtrValAdapterVO.getDomAtrVal());
			
            // Tiene errores recuperables
			if (domAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrValAdapterVO.infoString());
				saveDemodaErrors(request, domAtrValVO);				
				request.setAttribute(DomAtrValAdapter.NAME, domAtrValAdapterVO);
				return mapping.findForward(DefConstants.FWD_DOMATRVAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (domAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					DomAtrValAdapter.NAME, domAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrValAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrValAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DomAtrValAdapter.NAME);
	}
	
}