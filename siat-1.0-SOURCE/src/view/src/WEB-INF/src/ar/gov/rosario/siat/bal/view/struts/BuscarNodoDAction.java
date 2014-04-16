//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.NodoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarNodoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarNodoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_NODO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		try {
		
			NodoSearchPage nodoSearchPageVO = BalServiceLocator.getClasificacionService().getNodoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (nodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, nodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NodoSearchPage.NAME, nodoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (nodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + nodoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, NodoSearchPage.NAME, nodoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , NodoSearchPage.NAME, nodoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_NODO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, NodoSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, NodoSearchPage.NAME);

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
			NodoSearchPage nodoSearchPageVO = (NodoSearchPage) userSession.get(NodoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (nodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + NodoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, NodoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(nodoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				nodoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
				// Verificamos si se tildo el checkList para 'Checkear Consistencia de Nodos'
				String checkearConsistencia = request.getParameter("checkearConsistencia");
				if(checkearConsistencia != null)
					nodoSearchPageVO.setCheckearConsistencia(true);
				else
					nodoSearchPageVO.setCheckearConsistencia(false);
			}
			
            // Tiene errores recuperables
			if (nodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, nodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NodoSearchPage.NAME, nodoSearchPageVO);
			}

			// Llamada al servicio	
			nodoSearchPageVO = BalServiceLocator.getClasificacionService().getNodoSearchPageResult(userSession, nodoSearchPageVO);			

			// Tiene errores recuperables
			if (nodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, nodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NodoSearchPage.NAME, nodoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (nodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + nodoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, NodoSearchPage.NAME, nodoSearchPageVO);
			}
			
			CommonKey idNodoAAbrir = (CommonKey) userSession.get("idNodoAAbrir");
			if(idNodoAAbrir != null ){
				boolean expandirNodo = false;
				String expandir = (String) userSession.get("expandir");
				if(expandir != null && "true".equals(expandir)){
					expandirNodo = true;
				}
				nodoSearchPageVO = BalServiceLocator.getClasificacionService().getNodoSearchPageForVolver(userSession, nodoSearchPageVO, idNodoAAbrir.getId(), expandirNodo);
				// Tiene errores recuperables
				if (nodoSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString()); 
					saveDemodaErrors(request, nodoSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, NodoSearchPage.NAME, nodoSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (nodoSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + nodoSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NodoSearchPage.NAME, nodoSearchPageVO);
				}
				userSession.put("idNodoAAbrir", null);
				userSession.put("expandir", null);
			}
			
			// Envio el VO al request
			request.setAttribute(NodoSearchPage.NAME, nodoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(NodoSearchPage.NAME, nodoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_NODO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, NodoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);		
			if (userSession == null) return forwardErrorSession(request);
			try {	
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				// Subo en el el searchPage al userSession
				userSession.put("idNodoAAbrir", commonKey);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoSearchPage.NAME);
			}
			
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_NODO);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
	
			UserSession userSession = getCurrentUserSession(request, mapping);		
			if (userSession == null) return forwardErrorSession(request);
			try {	
				NavModel navModel = userSession.getNavModel();
				
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					CommonKey commonKey = new CommonKey(navModel.getSelectedId());
					
					// Subo en el el searchPage al userSession
					userSession.put("idNodoAAbrir", commonKey);
					userSession.put("expandir", "true");
				
					// Bajo el searchPage del userSession
					NodoSearchPage nodoSearchPageVO = (NodoSearchPage) userSession.get(NodoSearchPage.NAME);
					
					// Si es nulo no se puede continuar
					if (nodoSearchPageVO == null) {
						log.error("error en: "  + funcName + ": " + NodoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, NodoSearchPage.NAME); 
					}
					userSession.put("idClasificador", nodoSearchPageVO.getNodo().getClasificador().getId().toString());
				}
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoSearchPage.NAME);
			}
			
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENCNODO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
	
			UserSession userSession = getCurrentUserSession(request, mapping);		
			if (userSession == null) return forwardErrorSession(request);
			try {	
				NavModel navModel = userSession.getNavModel();
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
	
				// Subo en el el searchPage al userSession
				userSession.put("idNodoAAbrir", commonKey);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoSearchPage.NAME);
			}
			
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_NODO);	
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_NODO);
			
		}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, NodoSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, NodoSearchPage.NAME);
			
	}
	
	public ActionForward expandirContraer(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);
		
		try {	
			// Bajo el searchPage del userSession
			NodoSearchPage nodoSearchPageVO = (NodoSearchPage) userSession.get(NodoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (nodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + NodoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, NodoSearchPage.NAME); 
			}
			
			NavModel navModel = userSession.getNavModel();
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
			// Llamada al servicio	
			nodoSearchPageVO = BalServiceLocator.getClasificacionService().getNodoSearchPageForTree(userSession, nodoSearchPageVO, commonKey.getId());			

			// Tiene errores recuperables
			if (nodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, nodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NodoSearchPage.NAME, nodoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (nodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + nodoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, NodoSearchPage.NAME, nodoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(NodoSearchPage.NAME, nodoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(NodoSearchPage.NAME, nodoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_NODO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, NodoSearchPage.NAME);
		}
	}
	
	
	public ActionForward imprimirReportFromSearchPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String name = NodoSearchPage.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			NodoSearchPage nodoSearchPageVO = (NodoSearchPage) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (nodoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
		
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(nodoSearchPageVO, request);
			
			// Verificamos si se tildo el checkList para 'Checkear Consistencia de Nodos'
			String checkearConsistencia = request.getParameter("checkearConsistencia");
			if(checkearConsistencia != null)
				nodoSearchPageVO.setCheckearConsistencia(true);
			else
				nodoSearchPageVO.setCheckearConsistencia(false);
			
            // Tiene errores recuperables
			if (nodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString()); 
				saveDemodaErrors(request, nodoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NodoSearchPage.NAME, nodoSearchPageVO);
			}

			// llamada al servicio que genera el reporte
			nodoSearchPageVO = BalServiceLocator.getClasificacionService().imprimirArbolDeClasificacion(userSession, nodoSearchPageVO);

			// limpia la lista de reports y la lista de tablas
			nodoSearchPageVO.getReport().getListReport().clear();
			nodoSearchPageVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (nodoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + nodoSearchPageVO.infoString());
				saveDemodaErrors(request, nodoSearchPageVO);				
				request.setAttribute(name, nodoSearchPageVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (nodoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + nodoSearchPageVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, nodoSearchPageVO);
			
			// obtenemos el nombre del nodo seleccionado
			String fileName = nodoSearchPageVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
}
