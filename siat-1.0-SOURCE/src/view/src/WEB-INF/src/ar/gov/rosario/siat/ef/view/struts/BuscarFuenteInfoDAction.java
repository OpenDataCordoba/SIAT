//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarFuenteInfoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarFuenteInfoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			FuenteInfoSearchPage fuenteInfoSearchPageVO = EfServiceLocator.getDefinicionService().getFuenteInfoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (fuenteInfoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoSearchPageVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (fuenteInfoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_FUENTEINFO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, FuenteInfoSearchPage.NAME);
		
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
			FuenteInfoSearchPage fuenteInfoSearchPageVO = (FuenteInfoSearchPage) userSession.get(FuenteInfoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FuenteInfoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(fuenteInfoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				fuenteInfoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (fuenteInfoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoSearchPageVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			}
				
			// Llamada al servicio	
			fuenteInfoSearchPageVO = EfServiceLocator.getDefinicionService().getFuenteInfoSearchPageResult(userSession, fuenteInfoSearchPageVO);			

			// Tiene errores recuperables
			if (fuenteInfoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoSearchPageVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			// Nuleo el list result
			//fuenteInfoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(FuenteInfoSearchPage.NAME, fuenteInfoSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_FUENTEINFO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_FUENTEINFO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
	
		return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_FUENTEINFO);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_FUENTEINFO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_FUENTEINFO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_FUENTEINFO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_FUENTEINFO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, FuenteInfoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FuenteInfoSearchPage.NAME);
		
	}
		
	
}
