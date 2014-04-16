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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.DomAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.DomAtrVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncDomAtrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncDomAtrDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DomAtrAdapter domAtrAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDomAtrAdapterForUpdate(userSession, commonKey)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDomAtrAdapterForCreate(userSession)";
				domAtrAdapterVO = DefServiceLocator.getAtributoService().getDomAtrAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_DOMATR_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (domAtrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + domAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			domAtrAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DomAtrAdapter.ENC_NAME + ": "+ domAtrAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DomAtrAdapter.ENC_NAME, domAtrAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrAdapter domAtrAdapterVO = (DomAtrAdapter) userSession.get(DomAtrAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domAtrAdapterVO, request);
			
            // Tiene errores recuperables
			if (domAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, domAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}
			
			// llamada al servicio
			DomAtrVO domAtrVO = DefServiceLocator.getAtributoService().createDomAtr(userSession, domAtrAdapterVO.getDomAtr());
			
            // Tiene errores recuperables
			if (domAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrVO.infoString()); 
				saveDemodaErrors(request, domAtrVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(domAtrVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, DomAtrAdapter.ENC_NAME, 
					DefConstants.PATH_ADMINISTRAR_DOMATR, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, DomAtrAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			DefSecurityConstants.ABM_DOMINIO_ATRIBUTO_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DomAtrAdapter domAtrAdapterVO = (DomAtrAdapter) userSession.get(DomAtrAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (domAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DomAtrAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomAtrAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(domAtrAdapterVO, request);
			
            // Tiene errores recuperables
			if (domAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, domAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}
			
			// llamada al servicio
			DomAtrVO domAtrVO = DefServiceLocator.getAtributoService().updateDomAtr(userSession, domAtrAdapterVO.getDomAtr());
			
            // Tiene errores recuperables
			if (domAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, domAtrVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (domAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomAtrAdapter.ENC_NAME, domAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DomAtrAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomAtrAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DomAtrAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DomAtrAdapter.ENC_NAME);
		
	}
	
}
	
