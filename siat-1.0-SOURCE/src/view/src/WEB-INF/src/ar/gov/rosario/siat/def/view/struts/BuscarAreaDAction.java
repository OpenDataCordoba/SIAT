//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.AreaSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarAreaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAreaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			AreaSearchPage areaSearchPageVO = DefServiceLocator.getConfiguracionService().getAreaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (areaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSearchPageVO.infoString()); 
				saveDemodaErrors(request, areaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSearchPage.NAME, areaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (areaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSearchPage.NAME, areaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AreaSearchPage.NAME, areaSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_AREA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AreaSearchPage.NAME);
		
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
			AreaSearchPage areaSearchPageVO = (AreaSearchPage) userSession.get(AreaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(areaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				areaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (areaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSearchPageVO.infoString()); 
				saveDemodaErrors(request, areaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSearchPage.NAME, areaSearchPageVO);
			}
				
			// Llamada al servicio	
			areaSearchPageVO = DefServiceLocator.getConfiguracionService().getAreaSearchPageResult(userSession, areaSearchPageVO);			

			// Tiene errores recuperables
			if (areaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSearchPageVO.infoString()); 
				saveDemodaErrors(request, areaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSearchPage.NAME, areaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (areaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSearchPage.NAME, areaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AreaSearchPage.NAME, areaSearchPageVO);
			// Nuleo el list result
			//areaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AreaSearchPage.NAME, areaSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_AREA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, AreaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AreaSearchPage.NAME);	
	}
		
	public ActionForward admOficina(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
				SegConstants.ACTION_BUSCAR_OFICINA, BaseConstants.ACT_BUSCAR);
	}
	
	public ActionForward admRecursoArea(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_AREA, 
				DefConstants.ACT_ADM_RECURSOAREA_ADAPTER);				
	}
	
}
