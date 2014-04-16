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

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarSaldoPorCaducidadDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarSaldoPorCaducidadDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_SALDOPORCADUCIDAD, act); 
		if (userSession==null) return forwardErrorSession(request);

		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		NavModel navModel = userSession.getNavModel();
		navModel.setAct(getCurrentAct(request));
		
		try {
			
			SaldoPorCaducidadSearchPage saldoSearchPage = GdeServiceLocator.getGdePlanPagoService().getSalPorCadSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (saldoSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoSearchPage.infoString()); 
				saveDemodaErrors(request, saldoSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
			} 

			// Tiene errores no recuperables
			if (saldoSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
			
			return mapping.findForward(GdeConstants.FWD_ABMSALPORCAD);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoPorCaducidadSearchPage.NAME);
		}
	}
	
	public ActionForward buscar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+ " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		SaldoPorCaducidadSearchPage saldoSearchPage = (SaldoPorCaducidadSearchPage)userSession.get(SaldoPorCaducidadSearchPage.NAME);
		DemodaUtil.populateVO(saldoSearchPage, request);
		
		saldoSearchPage = GdeServiceLocator.getGdePlanPagoService().getSalPorCadSearchPageResults(saldoSearchPage,userSession);
		
		if (saldoSearchPage.hasErrorRecoverable()){
			userSession.put(SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
			request.setAttribute(SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
			saveDemodaErrors(request, saldoSearchPage);
			
			return forwardErrorRecoverable(mapping, request, userSession, SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
		}
		
		userSession.put(SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
		request.setAttribute(SaldoPorCaducidadSearchPage.NAME, saldoSearchPage);
		
		return mapping.findForward(GdeConstants.FWD_ABMSALPORCAD);
		
	}
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		SaldoPorCaducidadSearchPage salPorCadSearchPage = (SaldoPorCaducidadSearchPage) userSession.get(SaldoPorCaducidadSearchPage.NAME);
		
		DemodaUtil.populateVO(salPorCadSearchPage, request);
		
		salPorCadSearchPage = GdeServiceLocator.getGdePlanPagoService().getSalPorCadSearchPageParamRec(userSession, salPorCadSearchPage);
		
		request.setAttribute(SaldoPorCaducidadSearchPage.NAME, salPorCadSearchPage);
		
		userSession.put(SaldoPorCaducidadSearchPage.NAME, salPorCadSearchPage);
		
		return mapping.findForward(GdeConstants.FWD_ABMSALPORCAD);
		
			
		
	}
	
	public ActionForward ver (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADVER);
	}
	
	public ActionForward modificar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADMODIFICAR);
	}
	
	public ActionForward administrar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADADMIN);
	}
	
	public ActionForward eliminar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADELIMINAR);
	}
	
	public ActionForward seleccionar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADSELECCIONAR);
	}

	
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, SaldoPorCaducidadSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SaldoPorCaducidadSearchPage.NAME);
	}
	
	public ActionForward agregar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		String act = userSession.getNavModel().getAct();
		
		return baseForwardSearchPage(mapping, request, funcName, GdeConstants.FWD_ADDSALPORCAD, act);
	
	}
	
	
		
	
}
