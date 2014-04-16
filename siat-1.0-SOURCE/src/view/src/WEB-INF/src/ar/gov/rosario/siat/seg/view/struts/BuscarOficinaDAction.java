//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.seg.iface.model.OficinaSearchPage;
import ar.gov.rosario.siat.seg.iface.service.SegServiceLocator;
import ar.gov.rosario.siat.seg.iface.util.SegSecurityConstants;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarOficinaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOficinaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, SegSecurityConstants.ABM_OFICINA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			NavModel navModel = userSession.getNavModel();
			CommonKey areaKey = new CommonKey(navModel.getSelectedId());

			OficinaSearchPage oficinaSearchPageVO = SegServiceLocator.getSeguridadService().getOficinaSearchPageInit(userSession, areaKey);
			
			// Tiene errores recuperables
			if (oficinaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaSearchPageVO.infoString()); 
				saveDemodaErrors(request, oficinaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaSearchPage.NAME, oficinaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (oficinaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaSearchPage.NAME, oficinaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OficinaSearchPage.NAME, oficinaSearchPageVO);
			
			oficinaSearchPageVO.setPageNumber(new Long(1));
			oficinaSearchPageVO.setMaxRegistros(30L);
			return buscar(mapping, form, request, response);

			
			//return mapping.findForward(SegConstants.FWD_OFICINA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OficinaSearchPage.NAME);
		
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
			OficinaSearchPage oficinaSearchPageVO = (OficinaSearchPage) userSession.get(OficinaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (oficinaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OficinaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OficinaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(oficinaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				oficinaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (oficinaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaSearchPageVO.infoString()); 
				saveDemodaErrors(request, oficinaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaSearchPage.NAME, oficinaSearchPageVO);
			}
				
			// Llamada al servicio	
			oficinaSearchPageVO = SegServiceLocator.getSeguridadService().getOficinaSearchPageResult(userSession, oficinaSearchPageVO);			

			// Tiene errores recuperables
			if (oficinaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + oficinaSearchPageVO.infoString()); 
				saveDemodaErrors(request, oficinaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OficinaSearchPage.NAME, oficinaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (oficinaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + oficinaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OficinaSearchPage.NAME, oficinaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OficinaSearchPage.NAME, oficinaSearchPageVO);
			// Nuleo el list result
			//oficinaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(OficinaSearchPage.NAME, oficinaSearchPageVO);
			
			return mapping.findForward(SegConstants.FWD_OFICINA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OficinaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_OFICINA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_OFICINA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_OFICINA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_OFICINA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_OFICINA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, SegConstants.ACTION_ADMINISTRAR_OFICINA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, OficinaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OficinaSearchPage.NAME);
		
	}
		
	
}
