//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.EstCueSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarEstCueDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEstCueDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_ESTADOCUENTA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			EstCueSearchPage estadoCuentaSearchPageVO = PadServiceLocator.getCuentaService().getEstCueSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (estadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_ESTADOCUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstCueSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EstCueSearchPage.NAME);
		
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
			EstCueSearchPage estadoCuentaSearchPageVO = (EstCueSearchPage) userSession.get(EstCueSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EstCueSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstCueSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(estadoCuentaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				estadoCuentaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			}
				
			// Llamada al servicio	
			estadoCuentaSearchPageVO = PadServiceLocator.getCuentaService().getEstCueSearchPageResult(userSession, estadoCuentaSearchPageVO);			

			// Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			// Nuleo el list result
			//estadoCuentaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(EstCueSearchPage.NAME, estadoCuentaSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_ESTADOCUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstCueSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ESTADOCUENTA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ESTADOCUENTA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ESTADOCUENTA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ESTADOCUENTA);

	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EstCueSearchPage.NAME);
		
	}
		
	
}
