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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.VencimientoSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarVencimientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarVencimientoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			VencimientoSearchPage vencimientoSearchPageVO = DefServiceLocator.getGravamenService().getVencimientoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (vencimientoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoSearchPageVO.infoString()); 
				saveDemodaErrors(request, vencimientoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, VencimientoSearchPage.NAME, vencimientoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (vencimientoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoSearchPage.NAME, vencimientoSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , VencimientoSearchPage.NAME, vencimientoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_VENCIMIENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, VencimientoSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, VencimientoSearchPage.NAME);
			
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
				VencimientoSearchPage vencimientoSearchPageVO = (VencimientoSearchPage) userSession.get(VencimientoSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (vencimientoSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + VencimientoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, VencimientoSearchPage.NAME); 
				}
				// si el buscar diparado desde la pagina de busqueda
				if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(vencimientoSearchPageVO, request);
					// Setea el PageNumber del PageModel				
					vencimientoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				}
				 // Tiene errores recuperables
				if (vencimientoSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + vencimientoSearchPageVO.infoString()); 
					saveDemodaErrors(request, vencimientoSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, VencimientoSearchPage.NAME, vencimientoSearchPageVO);
				}
				// Llamada al servicio	
				vencimientoSearchPageVO = DefServiceLocator.getGravamenService().getVencimientoSearchPageResult(userSession, vencimientoSearchPageVO);			

				// Tiene errores recuperables
				if (vencimientoSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + vencimientoSearchPageVO.infoString()); 
					saveDemodaErrors(request, vencimientoSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, VencimientoSearchPage.NAME, vencimientoSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (vencimientoSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + vencimientoSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoSearchPage.NAME, vencimientoSearchPageVO);
				}
				// Envio el VO al request
				request.setAttribute(VencimientoSearchPage.NAME, vencimientoSearchPageVO);
				// Nuleo el list result
				//vencimientoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
				// Subo en el el searchPage al userSession
				userSession.put(VencimientoSearchPage.NAME, vencimientoSearchPageVO);
				
				return mapping.findForward(DefConstants.FWD_VENCIMIENTO_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, VencimientoSearchPage.NAME);
			}
	}
	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VENCIMIENTO);

		}
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VENCIMIENTO);
			
		}
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VENCIMIENTO);

		}

		public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VENCIMIENTO);

		}
		
		public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VENCIMIENTO);			
		}
		
		public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_VENCIMIENTO);
		}
		
		public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, VencimientoSearchPage.NAME);
			
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, VencimientoSearchPage.NAME);
			
		}

	
}
