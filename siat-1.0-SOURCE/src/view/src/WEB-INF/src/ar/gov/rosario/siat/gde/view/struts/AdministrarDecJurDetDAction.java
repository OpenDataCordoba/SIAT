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
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;



public final class AdministrarDecJurDetDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarDecJurDetDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		DecJurDetAdapter decJurDetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
	
		try {
			
			CommonKey selectedId = null;
			if (!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
				selectedId= new CommonKey(navModel.getSelectedId());
			}
			
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDecJurDetAdapterForView(userSession, atributoCommonKey)";
				decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurDetAdapterForView(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURDET_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDecJurDetAdapterForUpdate(userSession, atributoCommonKey)";
				decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurDetAdapterForUpdate(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURDET_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDecJurDetAdapterForView(userSession, atributoCommonKey)";
				decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurDetAdapterForView(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURDET_VIEW_ADAPTER);
				decJurDetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DECJURDET_ACTIVIDAD_LABEL);									
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDecJurDetAdapterForCreate(userSession, idDecJur)";
				Long idDecJur = new Long(request.getParameter("idDecJur"));
				decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurDetAdapterForCreate(userSession, idDecJur);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJURDET_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			decJurDetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DecJurDetAdapter.NAME + ": "+ decJurDetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DecJurDetAdapter.NAME, decJurDetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DecJurDetAdapter.NAME, decJurDetAdapterVO);
			
			saveDemodaMessages(request, decJurDetAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
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
		
		return baseVolver(mapping, form, request, response, DecJurDetAdapter.NAME);
	}
		
	public ActionForward agregarDecJurDet (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurDetAdapter decJurDetAdapterVO= (DecJurDetAdapter)userSession.get(DecJurDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurDetAdapter.NAME); 
			}
			DemodaUtil.populateVO(decJurDetAdapterVO, request);
			
			// llamada al servicio
			decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().createDecJurDet( userSession, decJurDetAdapterVO);
			
            // Tiene errores recuperablesDe
			if (decJurDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurDetAdapterVO.infoString()); 
				saveDemodaErrors(request, decJurDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			DecJurAdapter decJurAdapterVO = new DecJurAdapter();
			decJurAdapterVO.setDecJur(decJurDetAdapterVO.getDecJurDet().getDecJur());
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el create
			userSession.put(DecJurDetAdapter.NAME, null);
			
			userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
			request.setAttribute(DecJurAdapter.NAME , decJurAdapterVO);
			// Fue Exitoso
			return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
		}
		 
	}
		
	public ActionForward eliminar(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try{
			DecJurDetAdapter decJurDetAdapterVO = (DecJurDetAdapter)userSession.get(DecJurDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurDetAdapter.NAME); 
			}
			DemodaUtil.populateVO(decJurDetAdapterVO, request);
			
			// llamada al servicio
			decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().deleteDecJurDet( userSession, decJurDetAdapterVO);
			 // Tiene errores recuperablesDe
			if (decJurDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurDetAdapterVO.infoString()); 
				saveDemodaErrors(request, decJurDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			log.debug("ID DECJUR= "+decJurDetAdapterVO.getDecJurDet().getDecJur().getId());
			DecJurAdapter decJurAdapterVO = new DecJurAdapter();
			decJurAdapterVO.setDecJur(decJurDetAdapterVO.getDecJurDet().getDecJur());
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
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
		
		
	public ActionForward modificarDecJurDet (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurDetAdapter decJurDetAdapterVO= (DecJurDetAdapter)userSession.get(DecJurDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurDetAdapter.NAME); 
			}
			DemodaUtil.populateVO(decJurDetAdapterVO, request);
			
			// llamada al servicio
			decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().updateDecJurDet( userSession, decJurDetAdapterVO);
			
            // Tiene errores recuperablesDe
			if (decJurDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurDetAdapterVO.infoString()); 
				saveDemodaErrors(request, decJurDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			DecJurAdapter decJurAdapterVO = new DecJurAdapter();
			decJurAdapterVO.setDecJur(decJurDetAdapterVO.getDecJurDet().getDecJur());
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el create
			userSession.put(DecJurDetAdapter.NAME, null);
			
			userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
			request.setAttribute(DecJurAdapter.NAME , decJurAdapterVO);
			// Fue Exitoso
			return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DECJUD);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
		}
		 
	}
		
		
	public ActionForward paramUnidad(ActionMapping mapping, ActionForm form,  HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurDetAdapter decJurDetAdapterVO= (DecJurDetAdapter)userSession.get(DecJurDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurDetAdapter.NAME); 
			}
			DemodaUtil.populateVO(decJurDetAdapterVO, request);
			
			// llamada al servicio
			decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurDetParamUnidad( userSession, decJurDetAdapterVO);
			
            // Tiene errores recuperablesDe
			if (decJurDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurDetAdapterVO.infoString()); 
				saveDemodaErrors(request, decJurDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			DecJurAdapter decJurAdapterVO = new DecJurAdapter();
			decJurAdapterVO.setDecJur(decJurDetAdapterVO.getDecJurDet().getDecJur());
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el create
			userSession.put(DecJurDetAdapter.NAME, decJurDetAdapterVO);
			
			
			request.setAttribute(DecJurDetAdapter.NAME , decJurDetAdapterVO);
			// Fue Exitoso
			return mapping.findForward(GdeConstants.FWD_DECJURDET_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
		}
	}
		
		
	public ActionForward paramTipUni(ActionMapping mapping, ActionForm form,  HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurDetAdapter decJurDetAdapterVO= (DecJurDetAdapter)userSession.get(DecJurDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurDetAdapter.NAME); 
			}
			DemodaUtil.populateVO(decJurDetAdapterVO, request);
			
			// llamada al servicio
			decJurDetAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurDetParamTipUni( userSession, decJurDetAdapterVO);
			
            // Tiene errores recuperablesDe
			if (decJurDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurDetAdapterVO.infoString()); 
				saveDemodaErrors(request, decJurDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			
			DecJurAdapter decJurAdapterVO = new DecJurAdapter();
			decJurAdapterVO.setDecJur(decJurDetAdapterVO.getDecJurDet().getDecJur());
			
			// Tiene errores no recuperables
			if (decJurDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurDetAdapter.NAME, decJurDetAdapterVO);
			}
			//Nuleo el decJurDet para que el F5 de decJur no ejecute de nuevo el create
			userSession.put(DecJurDetAdapter.NAME, decJurDetAdapterVO);
			
			
			request.setAttribute(DecJurDetAdapter.NAME , decJurDetAdapterVO);
			// Fue Exitoso
			return mapping.findForward(GdeConstants.FWD_DECJURDET_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurDetAdapter.NAME);
		}
	}
		
		
}
