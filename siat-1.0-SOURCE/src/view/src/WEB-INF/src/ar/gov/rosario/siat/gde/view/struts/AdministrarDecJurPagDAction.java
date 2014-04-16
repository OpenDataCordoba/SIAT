//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.DecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurDetAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurPagAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDecJurPagDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarDecJurPagDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		DecJurPagAdapter decJurPagAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;

		try {
			
			CommonKey selectedId = null;
			if (!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
				selectedId= new CommonKey(navModel.getSelectedId());
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDecJurDetAdapterForView(userSession, atributoCommonKey)";
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurPagAdapterForView(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURPAG_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDecJurDetAdapterForUpdate(userSession, atributoCommonKey)";
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurPagAdapterForUpdate(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURPAG_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDecJurDetAdapterForView(userSession, atributoCommonKey)";
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurPagAdapterForView(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURPAG_VIEW_ADAPTER);
				decJurPagAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DECJURPAG_TIPOPAGO_LABEL);									
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDecJurDetAdapterForCreate(userSession, idDecJur)";
				Long idDecJur = new Long(request.getParameter("idDecJur"));
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurPagAdapterForCreate(userSession, idDecJur);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURPAG_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + decJurPagAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			decJurPagAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DecJurPagAdapter.NAME + ": "+ decJurPagAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DecJurPagAdapter.NAME, decJurPagAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DecJurPagAdapter.NAME, decJurPagAdapterVO);
			
			saveDemodaMessages(request, decJurPagAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurPagAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.ABM_DECJUR, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DecJurAdapter decJurAdapterVO = (DecJurAdapter) userSession.get(DecJurAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (decJurAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DecJurAdapter.NAME); 
				}
				DemodaUtil.populateVO(decJurAdapterVO, request);
				
				// llamada al servicio
			//	decJurAdapterVO = GdeServiceLocator.getGestionDeudaService().createDecJur( userSession, decJurAdapterVO);
				
	            // Tiene errores recuperables
				if (decJurAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + decJurAdapterVO.infoString()); 
					saveDemodaErrors(request, decJurAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DecJurAdapter.NAME, decJurAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (decJurAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurAdapter.NAME, decJurAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DecJurAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DecJurAdapter.NAME);
			}
		}
	
		
		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DecJurPagAdapter.NAME);
		}
		
		public ActionForward agregarDecJurPag (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DecJurPagAdapter decJurPagAdapterVO= (DecJurPagAdapter)userSession.get(DecJurPagAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (decJurPagAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DecJurPagAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DecJurPagAdapter.NAME); 
				}
				DemodaUtil.populateVO(decJurPagAdapterVO, request);
				
				// llamada al servicio
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().createDecJurPag( userSession, decJurPagAdapterVO);
				
	            // Tiene errores recuperablesDe
				if (decJurPagAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + decJurPagAdapterVO.infoString()); 
					saveDemodaErrors(request, decJurPagAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurPagAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				
				
				
				// Tiene errores no recuperables
				if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurPagAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				
				DecJurAdapter decJurAdapterVO = new DecJurAdapter();
				decJurAdapterVO.setDecJur(decJurPagAdapterVO.getDecJurPag().getDecJur());
				
				//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el create
				userSession.put(DecJurPagAdapter.NAME, null);
				
				userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
				request.setAttribute(DecJurAdapter.NAME , decJurAdapterVO);
				// Fue Exitoso
				return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DecJurPagAdapter.NAME);
			}
			 
		}
		
		public ActionForward eliminar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
			String funcName=DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			try{
				DecJurPagAdapter decJurPagAdapterVO = (DecJurPagAdapter)userSession.get(DecJurPagAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (decJurPagAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DecJurPagAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DecJurPagAdapter.NAME); 
				}
				DemodaUtil.populateVO(decJurPagAdapterVO, request);
				
				// llamada al servicio
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().deleteDecJurPag( userSession, decJurPagAdapterVO);
				 // Tiene errores recuperablesDe
				if (decJurPagAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + decJurPagAdapterVO.infoString()); 
					saveDemodaErrors(request, decJurPagAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurPagAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				log.debug("ID DECJUR= "+decJurPagAdapterVO.getDecJurPag().getDecJur().getId());
				DecJurAdapter decJurAdapterVO = new DecJurAdapter();
				decJurAdapterVO.setDecJur(decJurPagAdapterVO.getDecJurPag().getDecJur());
				// Tiene errores no recuperables
				if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurPagAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el delete
				userSession.put(DecJurDetAdapter.NAME, null);
				
				userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
				request.setAttribute(DecJurAdapter.NAME , decJurAdapterVO);
				// Fue Exitoso
				return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
				
			}catch(Exception exception){
				return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
			}
			
		}
		
		
		public ActionForward modificarDecJurPag (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DecJurPagAdapter decJurPagAdapterVO= (DecJurPagAdapter)userSession.get(DecJurPagAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (decJurPagAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DecJurPagAdapter.NAME); 
				}
				DemodaUtil.populateVO(decJurPagAdapterVO, request);
				
				// llamada al servicio
				decJurPagAdapterVO = GdeServiceLocator.getGestionDeudaService().updateDecJurPag( userSession, decJurPagAdapterVO);
				
	            // Tiene errores recuperablesDe
				if (decJurPagAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + decJurPagAdapterVO.infoString()); 
					saveDemodaErrors(request, decJurPagAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurPagAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				
				DecJurAdapter decJurAdapterVO = new DecJurAdapter();
				decJurAdapterVO.setDecJur(decJurPagAdapterVO.getDecJurPag().getDecJur());
				
				// Tiene errores no recuperables
				if (decJurPagAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + decJurPagAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DecJurPagAdapter.NAME, decJurPagAdapterVO);
				}
				//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el create
				userSession.put(decJurPagAdapterVO.NAME, null);
				
				userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
				request.setAttribute(DecJurAdapter.NAME , decJurAdapterVO);
				// Fue Exitoso
				return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
			}
			 
		}
		
		
}
