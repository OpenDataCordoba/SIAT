//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoSearchPage;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarJuzgadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarJuzgadoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			JuzgadoSearchPage juzgadoSearchPageVO = CyqServiceLocator.getDefinicionService().getJuzgadoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (juzgadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, juzgadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (juzgadoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			
			return mapping.findForward(CyqConstants.FWD_JUZGADO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, JuzgadoSearchPage.NAME);
		
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
			JuzgadoSearchPage juzgadoSearchPageVO = (JuzgadoSearchPage) userSession.get(JuzgadoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (juzgadoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + JuzgadoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, JuzgadoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(juzgadoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				juzgadoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (juzgadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, juzgadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			}
				
			// Llamada al servicio	
			juzgadoSearchPageVO = CyqServiceLocator.getDefinicionService().getJuzgadoSearchPageResult(userSession, juzgadoSearchPageVO);			

			// Tiene errores recuperables
			if (juzgadoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoSearchPageVO.infoString()); 
				saveDemodaErrors(request, juzgadoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (juzgadoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			// Nuleo el list result
			//juzgadoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(JuzgadoSearchPage.NAME, juzgadoSearchPageVO);
			
			return mapping.findForward(CyqConstants.FWD_JUZGADO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_JUZGADO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_JUZGADO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_JUZGADO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_JUZGADO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_JUZGADO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_JUZGADO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, JuzgadoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, JuzgadoSearchPage.NAME);
		
	}
		
	
}
