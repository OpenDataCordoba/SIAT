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
import ar.gov.rosario.siat.def.iface.model.RecursoSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarRecursoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarRecursoDAction.class);
	
	private static final Long COD_NO_TRIB = -2L;
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");
			
			UserSession us=getCurrentUserSession(request, mapping);
			
			NavModel navModel=us.getNavModel();
			
			CommonKey commonKey=null;
			if (!StringUtil.isNullOrEmpty(navModel.getSelectedId()))
				commonKey= new CommonKey(navModel.getSelectedId());
			
			UserSession userSession;
			
			boolean esNoTrib = false;
			
			if (!StringUtil.isNullOrEmpty(navModel.getSelectedId()) && commonKey!=null && commonKey.getId().longValue()== (COD_NO_TRIB.longValue()))
				esNoTrib=true;
			
			if (esNoTrib)
				userSession = canAccess(request, mapping , DefSecurityConstants.ABM_RECURSO_NOTRIB, act);
			else
				userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO, act);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
					
				
				RecursoSearchPage recursoSearchPageVO = DefServiceLocator.getGravamenService().getRecursoSearchPageInit(userSession, esNoTrib);
				
				
				
				// Tiene errores recuperables
				if (recursoSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recursoSearchPageVO.infoString()); 
					saveDemodaErrors(request, recursoSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecursoSearchPage.NAME, recursoSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (recursoSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recursoSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoSearchPage.NAME, recursoSearchPageVO);
				}
				
				// Si no tiene error
				baseInicializarSearchPage(mapping, request, userSession , RecursoSearchPage.NAME, recursoSearchPageVO);
				
				return mapping.findForward(DefConstants.FWD_RECURSO_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoSearchPage.NAME);
			}
		}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, RecursoSearchPage.NAME);

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
			RecursoSearchPage recursoSearchPageVO = (RecursoSearchPage) userSession.get(RecursoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recursoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				recursoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (recursoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoSearchPageVO.infoString()); 
				saveDemodaErrors(request, recursoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoSearchPage.NAME, recursoSearchPageVO);
			}

			// Llamada al servicio	
			recursoSearchPageVO = DefServiceLocator.getGravamenService().getRecursoSearchPageResult(userSession, recursoSearchPageVO);			

			// Tiene errores recuperables
			if (recursoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + recursoSearchPageVO.infoString()); 
				saveDemodaErrors(request, recursoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, RecursoSearchPage.NAME, recursoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (recursoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + recursoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, RecursoSearchPage.NAME, recursoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(RecursoSearchPage.NAME, recursoSearchPageVO);
			// Nuleo el list result
			//recursoSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(RecursoSearchPage.NAME, recursoSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_RECURSO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecursoSearchPage.NAME);
		}
	}
	
	
	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSO);
		}

		public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENCRECURSO);
		}

		public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			
			RecursoSearchPage recursoSearchPageVO = (RecursoSearchPage) userSession.get(RecursoSearchPage.NAME);
			
			String act=DefConstants.ACTION_ADMINISTRAR_RECURSO;
			
			
			
			return forwardModificarSearchPage(mapping, request, funcName, act);	
		}

		public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSO);
			
		}
		
		public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSO);			
		}
		
		public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSO);
		}

		public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, RecursoSearchPage.NAME);
		}	

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, RecursoSearchPage.NAME);
			
		}
		
		public ActionForward paramTipoCategoria (ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {
					// Bajo el SearchPage del userSession
					RecursoSearchPage recursoSearchPageVO = (RecursoSearchPage) userSession.get(RecursoSearchPage.NAME);
					
					// Si es nulo no se puede continuar
					if (recursoSearchPageVO == null) {
						log.error("error en: "  + funcName + ": " + RecursoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, RecursoSearchPage.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(recursoSearchPageVO, request);
					
		            // Tiene errores recuperables
					if (recursoSearchPageVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + recursoSearchPageVO.infoString()); 
						saveDemodaErrors(request, recursoSearchPageVO);
						return forwardErrorRecoverable(mapping, request, userSession, RecursoSearchPage.NAME, recursoSearchPageVO);
					}
					
					// llamada al servicio
					recursoSearchPageVO = DefServiceLocator.getGravamenService().getRecursoSearchPageParamTipoCategoria(userSession, recursoSearchPageVO);
					
		            // Tiene errores recuperables
					if (recursoSearchPageVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + recursoSearchPageVO.infoString()); 
						saveDemodaErrors(request, recursoSearchPageVO);
						return forwardErrorRecoverable(mapping, request, userSession, RecursoSearchPage.NAME, recursoSearchPageVO);
					}
					
					// Tiene errores no recuperables
					if (recursoSearchPageVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + recursoSearchPageVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, RecursoSearchPage.NAME, recursoSearchPageVO);
					}
					
					// Vuelvo el PAGENUMBER a 0 para evitar que el viewresult sea false
					recursoSearchPageVO.setPageNumber(new Long(0)); 
					// Envio el VO al request
					request.setAttribute(RecursoSearchPage.NAME, recursoSearchPageVO);
					// Subo el searchpage al userSession
					userSession.put(RecursoSearchPage.NAME, recursoSearchPageVO);
					
					return mapping.findForward(DefConstants.FWD_RECURSO_SEARCHPAGE);
				
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, RecursoSearchPage.NAME);
				}
			}
	
		// Recurso AutoLiquidable
		public ActionForward modificarAutoLiquidable(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSO_AUTOLIQUIDABLE);	
		}
		

}
