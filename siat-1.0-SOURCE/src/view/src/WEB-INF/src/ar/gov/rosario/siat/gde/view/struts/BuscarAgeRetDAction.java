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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.AgeRetSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarAgeRetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAgeRetDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_AGERET, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			AgeRetSearchPage ageRetSearchPageVO = GdeServiceLocator.getDefinicionService().getAgeRetSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (ageRetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetSearchPageVO.infoString()); 
				saveDemodaErrors(request, ageRetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetSearchPage.NAME, ageRetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (ageRetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetSearchPage.NAME, ageRetSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AgeRetSearchPage.NAME, ageRetSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_AGERET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AgeRetSearchPage.NAME);
		
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
			AgeRetSearchPage ageRetSearchPageVO = (AgeRetSearchPage) userSession.get(AgeRetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ageRetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AgeRetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AgeRetSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ageRetSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				ageRetSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (ageRetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetSearchPageVO.infoString()); 
				saveDemodaErrors(request, ageRetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetSearchPage.NAME, ageRetSearchPageVO);
			}
				
			// Llamada al servicio	
			ageRetSearchPageVO = GdeServiceLocator.getDefinicionService().getAgeRetSearchPageResult(userSession, ageRetSearchPageVO);			

			// Tiene errores recuperables
			if (ageRetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ageRetSearchPageVO.infoString()); 
				saveDemodaErrors(request, ageRetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AgeRetSearchPage.NAME, ageRetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (ageRetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ageRetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AgeRetSearchPage.NAME, ageRetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AgeRetSearchPage.NAME, ageRetSearchPageVO);
			// Nuleo el list result
			//ageRetSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AgeRetSearchPage.NAME, ageRetSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_AGERET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AgeRetSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_AGERET);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_AGERET);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_AGERET);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_AGERET);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_AGERET);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_AGERET);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, AgeRetSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AgeRetSearchPage.NAME);
		
	}
		
	
}
