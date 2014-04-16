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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.AccionMasivaConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class AdministrarAccionConvenioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAccionConvenioDAction.class);
	
	/**
	 * Permite ir al adapter de ejecucion de una accion masiva de convenios.
	 * 
	 * @author 
	 * @return ActionForward
	 * @throws Exception
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ACCION_MASIVA_CONVENIOS, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		
		
		
		try {
			
			
			
			AccionMasivaConvenioAdapter accionMasivaAdapter=GdeServiceLocator.getGdePlanPagoService().getAccionMasivaConvenioAdapterForInit(userSession);
					
			// Envio el VO al request y subo al userSession
			request.setAttribute(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			userSession.put(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			
			saveDemodaMessages(request, accionMasivaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_ACCION_CONVENIO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionMasivaConvenioAdapter.NAME);
		}
	}
	
	
	
	public ActionForward ejecutar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		AccionMasivaConvenioAdapter accionMasivaAdapter=(AccionMasivaConvenioAdapter)userSession.get(AccionMasivaConvenioAdapter.NAME);
		
		DemodaUtil.populateVO(accionMasivaAdapter, request);
		
		try {			
			
			accionMasivaAdapter=GdeServiceLocator.getGdePlanPagoService().createAccionMasivaConvenio(accionMasivaAdapter, userSession);
					
			if (accionMasivaAdapter.hasErrorRecoverable()){
				request.setAttribute(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
				userSession.put(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
				saveDemodaErrors(request, accionMasivaAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			}
			// Envio el VO al request y subo al userSession
			request.setAttribute(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			userSession.put(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			
			saveDemodaMessages(request, accionMasivaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_ACCION_CONVENIO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionMasivaConvenioAdapter.NAME);
		}
	}
	
	
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return new ActionForward(BaseConstants.FWD_SIAT_BUILD_MENU);
		}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		AccionMasivaConvenioAdapter accionMasivaAdapter=(AccionMasivaConvenioAdapter)userSession.get(AccionMasivaConvenioAdapter.NAME);
		
		if (accionMasivaAdapter == null){
			return forwardErrorSessionNullObject(mapping, request, funcName, AccionMasivaConvenioAdapter.NAME);
		}
		DemodaUtil.populateVO(accionMasivaAdapter, request);
		
		try {
			
			
			
			accionMasivaAdapter=GdeServiceLocator.getGdePlanPagoService().getAccionMasivaConvenioAdapterForView(userSession,accionMasivaAdapter);
					
			// Envio el VO al request y subo al userSession
			request.setAttribute(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			userSession.put(AccionMasivaConvenioAdapter.NAME, accionMasivaAdapter);
			
			saveDemodaMessages(request, accionMasivaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_ACCION_CONVENIO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AccionMasivaConvenioAdapter.NAME);
		}
	}
	
}