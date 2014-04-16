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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.CobranzaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarCobranzaDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarCobranzaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_COBRANZA, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		CobranzaAdapter cobranzaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		
		
		
		try {
			
			CommonKey selectedId = null;
			String idSelected = request.getParameter(BaseConstants.SELECTED_ID);
			log.debug("SELECTED ID: "+idSelected);
			
			//Si viene de un refill tomo el atributo selectedId
			if (StringUtil.isNullOrEmpty(idSelected))
				idSelected=(String)request.getAttribute(BaseConstants.SELECTED_ID);
				
			if (!StringUtil.isNullOrEmpty(idSelected)){
				selectedId= new CommonKey(Long.parseLong(idSelected));
			}
			
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				
				
				cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().getCobranzaAdapterForUpdate(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_COBRANZA_VIEW_ADAPTER);
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
								
				cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().getCobranzaAdapterForUpdate(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_COBRANZA_ADAPTER);
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_ASIGNAR)) {
				
				cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().getCobranzaAdapterForAsign(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_COBRANZA_ASIGN_ADAPTER);
			}
			
			//Este caso es para cuando vuelve de la liquidacion de deuda
			if(cobranzaAdapterVO==null){
				cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().getCobranzaAdapterForUpdate(userSession, selectedId);
				actionForward = mapping.findForward(GdeConstants.FWD_COBRANZA_ADAPTER);
			}
			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cobranzaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + cobranzaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cobranzaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CobranzaAdapter.NAME + ": "+ cobranzaAdapterVO.infoString());
			
			cobranzaAdapterVO.setPrevAction(GdeConstants.PATH_BUSCAR_COBRANZA);
			
			cobranzaAdapterVO.setPrevActionParameter(BaseConstants.ACT_BUSCAR);
			
			// Envio el VO al request
			request.setAttribute(CobranzaAdapter.NAME, cobranzaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CobranzaAdapter.NAME, cobranzaAdapterVO);
			
			saveDemodaMessages(request, cobranzaAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}
	}
	
	
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			UserSession userSession = getCurrentUserSession(request, mapping);
			CobranzaAdapter cobranzaAdapterVO= (CobranzaAdapter)userSession.get(CobranzaAdapter.NAME_ENC);
			
			userSession.put(CobranzaAdapter.NAME, cobranzaAdapterVO);
			if(cobranzaAdapterVO != null){
				request.setAttribute(BaseConstants.SELECTED_ID, cobranzaAdapterVO.getCobranza().getId().toString());
				log.debug("SELECTED_ID: "+cobranzaAdapterVO.getCobranza().getId());
			}

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CobranzaAdapter.NAME);
			
	}
	
	public ActionForward emitirAjustes(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,
			GdeSecurityConstants.ABM_COBRANZA, BaseSecurityConstants.EMITIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaAdapter cobranzaAdapterVO = (CobranzaAdapter) userSession.get(CobranzaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaAdapter.NAME); 
			}
			DemodaUtil.populateVO(cobranzaAdapterVO, request);
			
			// llamada al servicio
			cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().createEmisionAjustes(userSession, cobranzaAdapterVO);
            // Tiene errores recuperables
			if (cobranzaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaAdapterVO.infoString()); 
				saveDemodaErrors(request, cobranzaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cobranzaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			cobranzaAdapterVO.setPrevAction(GdeConstants.PATH_COBRANZA_ADAPTER);
			
			cobranzaAdapterVO.setPrevActionParameter(BaseConstants.ACT_REFILL);
			
			cobranzaAdapterVO.setSelectedId(cobranzaAdapterVO.getCobranza().getId().toString());
			
			
			userSession.put(CobranzaAdapter.NAME_ENC, cobranzaAdapterVO);
			
			
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CobranzaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}
	}
	
	public ActionForward agregarGesCob(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaAdapter cobranzaAdapterVO = (CobranzaAdapter) userSession.get(CobranzaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaAdapter.NAME); 
			}
			DemodaUtil.populateVO(cobranzaAdapterVO, request);
			
			// llamada al servicio
			cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().createGesCob(userSession, cobranzaAdapterVO);
            // Tiene errores recuperables
			if (cobranzaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaAdapterVO.infoString()); 
				saveDemodaErrors(request, cobranzaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cobranzaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			cobranzaAdapterVO.setPrevAction(GdeConstants.PATH_COBRANZA_ADAPTER);
			
			cobranzaAdapterVO.setPrevActionParameter(userSession.getNavModel().getPrevActionParameter());
			
			cobranzaAdapterVO.setSelectedId(cobranzaAdapterVO.getCobranza().getId().toString());
			
			userSession.put(CobranzaAdapter.NAME_ENC, cobranzaAdapterVO);
			
			
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CobranzaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CobranzaAdapter.NAME);
		}
	
	public ActionForward asignarGesCob(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,
			GdeSecurityConstants.ABM_COBRANZA, BaseSecurityConstants.EMITIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaAdapter cobranzaAdapterVO = (CobranzaAdapter) userSession.get(CobranzaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaAdapter.NAME); 
			}
			DemodaUtil.populateVO(cobranzaAdapterVO, request);
			
			// llamada al servicio
			cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().asignarPerCob(userSession, cobranzaAdapterVO);
            // Tiene errores recuperables
			if (cobranzaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cobranzaAdapterVO.infoString()); 
				saveDemodaErrors(request, cobranzaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cobranzaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cobranzaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CobranzaAdapter.NAME, cobranzaAdapterVO);
			}
			
			
			
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CobranzaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}
	}
	
	
	public ActionForward irALiqDeuda(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaAdapter cobranzaAdapterVO = (CobranzaAdapter) userSession.get(CobranzaAdapter.NAME);
			
			Long selectedId=Long.parseLong(request.getParameter(BaseConstants.SELECTED_ID));
			
			// Si es nulo no se puede continuar
			if (cobranzaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaAdapter.NAME); 
			}
			DemodaUtil.populateVO(cobranzaAdapterVO, request);
			
			String url="/gde/AdministrarLiqDeuda.do?method=verCuenta&selectedId="+selectedId;
			
			String vuelveA="/gde/AdministrarCobranza.do?method=inicializar&selectedId="+cobranzaAdapterVO.getCobranza().getId();
			
			userSession.put("liqDeudaVuelveA", vuelveA);
			
			log.debug("URL LIQDEUDA: "+url);
			
			// Fue Exitoso
			return new ActionForward(url);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaAdapter cobranzaAdapterVO = (CobranzaAdapter) userSession.get(CobranzaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(cobranzaAdapterVO, request);
			
			
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, cobranzaAdapterVO.getCobranza()); 
			
			cobranzaAdapterVO.getCobranza().passErrorMessages(cobranzaAdapterVO);
		    
		    saveDemodaMessages(request, cobranzaAdapterVO);
		    saveDemodaErrors(request, cobranzaAdapterVO);
		    
			request.setAttribute(CobranzaAdapter.NAME, cobranzaAdapterVO);
			
			return mapping.findForward( GdeConstants.FWD_COBRANZA_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}	
	}
	
	public ActionForward quitarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CobranzaAdapter cobranzaAdapterVO = (CobranzaAdapter) userSession.get(CobranzaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cobranzaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CobranzaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CobranzaAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(cobranzaAdapterVO, request);
			
			
			
			// llamada al servicio
			cobranzaAdapterVO = GdeServiceLocator.getGdeGCobranzaService().quitarCaso(userSession, cobranzaAdapterVO); 
			
			cobranzaAdapterVO.getCobranza().passErrorMessages(cobranzaAdapterVO);
		    
		    saveDemodaMessages(request, cobranzaAdapterVO);
		    saveDemodaErrors(request, cobranzaAdapterVO);
		    
			request.setAttribute(CobranzaAdapter.NAME, cobranzaAdapterVO);
			
			return mapping.findForward( GdeConstants.FWD_COBRANZA_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CobranzaAdapter.NAME);
		}	
	}

	
}
