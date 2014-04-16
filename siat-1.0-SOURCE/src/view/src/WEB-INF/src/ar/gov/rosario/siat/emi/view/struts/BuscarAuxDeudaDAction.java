//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.AuxDeudaSearchPage;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarAuxDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAuxDeudaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug(funcName + " navModel" + navModel.infoString());
			
			Long idEmision = (Long) navModel.getParameter(EmisionVO.ADP_PARAM_ID);
			AuxDeudaSearchPage auxDeudaSearchPageVO = EmiServiceLocator.getEmisionService()
						.getAuxDeudaSearchPageInit(userSession, idEmision);
			
			// Tiene errores recuperables
			if (auxDeudaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxDeudaSearchPageVO.infoString()); 
				saveDemodaErrors(request, auxDeudaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (auxDeudaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxDeudaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_AUXDEUDA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxDeudaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AuxDeudaSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			AuxDeudaSearchPage auxDeudaSearchPageVO = (AuxDeudaSearchPage) userSession.get(AuxDeudaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (auxDeudaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AuxDeudaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AuxDeudaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(auxDeudaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				auxDeudaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (auxDeudaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxDeudaSearchPageVO.infoString()); 
				saveDemodaErrors(request, auxDeudaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			}
				
			// Llamada al servicio	
			auxDeudaSearchPageVO = EmiServiceLocator.getEmisionService()
					.getAuxDeudaSearchPageResult(userSession, auxDeudaSearchPageVO);			

			// Tiene errores recuperables
			if (auxDeudaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxDeudaSearchPageVO.infoString()); 
				saveDemodaErrors(request, auxDeudaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (auxDeudaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxDeudaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(AuxDeudaSearchPage.NAME, auxDeudaSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_AUXDEUDA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxDeudaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_AUXDEUDA);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AuxDeudaSearchPage.NAME);
		
	}
	
}
