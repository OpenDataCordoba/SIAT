//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.ProcesoSearchPage;
import ar.gov.rosario.siat.pro.iface.service.ProServiceLocator;
import ar.gov.rosario.siat.pro.iface.util.ProSecurityConstants;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarProcesoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarProcesoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, ProSecurityConstants.ABM_PROCESO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProcesoSearchPage procesoSearchPageVO = ProServiceLocator.getConsultaService().getProcesoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (procesoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoSearchPageVO.infoString()); 
				saveDemodaErrors(request, procesoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoSearchPage.NAME, procesoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (procesoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoSearchPage.NAME, procesoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ProcesoSearchPage.NAME, procesoSearchPageVO);
			
			return mapping.findForward(ProConstants.FWD_PROCESO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ProcesoSearchPage.NAME);
		
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
			ProcesoSearchPage procesoSearchPageVO = (ProcesoSearchPage) userSession.get(ProcesoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (procesoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ProcesoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcesoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(procesoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				procesoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (procesoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoSearchPageVO.infoString()); 
				saveDemodaErrors(request, procesoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoSearchPage.NAME, procesoSearchPageVO);
			}
				
			// Llamada al servicio	
			procesoSearchPageVO = ProServiceLocator.getConsultaService().getProcesoSearchPageResult(userSession, procesoSearchPageVO);			

			// Tiene errores recuperables
			if (procesoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procesoSearchPageVO.infoString()); 
				saveDemodaErrors(request, procesoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcesoSearchPage.NAME, procesoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (procesoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procesoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcesoSearchPage.NAME, procesoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ProcesoSearchPage.NAME, procesoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ProcesoSearchPage.NAME, procesoSearchPageVO);
			
			return mapping.findForward(ProConstants.FWD_PROCESO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcesoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_PROCESO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_PROCESO);	
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_PROCESO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_PROCESO);

	}
	
/*	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_PROCESO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, ProConstants.ACTION_ADMINISTRAR_PROCESO);
	}
	*/
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ProcesoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcesoSearchPage.NAME);
		
	}
		
	
	
}
