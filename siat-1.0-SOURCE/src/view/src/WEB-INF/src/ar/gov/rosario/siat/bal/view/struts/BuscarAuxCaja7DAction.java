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

import ar.gov.rosario.siat.bal.iface.model.AuxCaja7SearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarAuxCaja7DAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAuxCaja7DAction.class);
	public final static String AUXCAJA7_SEARCHPAGE_FILTRO = "auxCaja7SPFiltro";
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			AuxCaja7SearchPage auxCaja7SearchPageFiltro = (AuxCaja7SearchPage) userSession.getNavModel().getParameter(BuscarAuxCaja7DAction.AUXCAJA7_SEARCHPAGE_FILTRO);
			
			AuxCaja7SearchPage auxCaja7SearchPageVO = BalServiceLocator.getBalanceService().getAuxCaja7SearchPageInit(userSession, auxCaja7SearchPageFiltro);
			
			// Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
				saveDemodaErrors(request, auxCaja7SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			} 

			// Tiene errores no recuperables
			if (auxCaja7SearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7SearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			
			if(auxCaja7SearchPageFiltro != null){
				// Setea el PageNumber del PageModel				
				auxCaja7SearchPageVO.setPageNumber(1L);
				
				// Llamada al servicio	
				auxCaja7SearchPageVO = BalServiceLocator.getBalanceService().getAuxCaja7SearchPageResult(userSession, auxCaja7SearchPageVO);			

				// Tiene errores recuperables
				if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
					saveDemodaErrors(request, auxCaja7SearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (auxCaja7SearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + auxCaja7SearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
			
			return mapping.findForward(BalConstants.FWD_AUXCAJA7_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7SearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AuxCaja7SearchPage.NAME);
		
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
			AuxCaja7SearchPage auxCaja7SearchPageVO = (AuxCaja7SearchPage) userSession.get(AuxCaja7SearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (auxCaja7SearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AuxCaja7SearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AuxCaja7SearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(auxCaja7SearchPageVO, request);
				// Setea el PageNumber del PageModel				
				auxCaja7SearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
				saveDemodaErrors(request, auxCaja7SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
				
			// Llamada al servicio	
			auxCaja7SearchPageVO = BalServiceLocator.getBalanceService().getAuxCaja7SearchPageResult(userSession, auxCaja7SearchPageVO);			

			// Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
				saveDemodaErrors(request, auxCaja7SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (auxCaja7SearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7SearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
	
			// Subo en el el searchPage al userSession
			userSession.put(AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_AUXCAJA7_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7SearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_AUXCAJA7);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
	
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_AUXCAJA7);

	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_AUXCAJA7);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_AUXCAJA7);

	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, AuxCaja7SearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AuxCaja7SearchPage.NAME);
		
	}
		
	public ActionForward incluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, BaseSecurityConstants.INCLUIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			AuxCaja7SearchPage auxCaja7SearchPageVO = (AuxCaja7SearchPage) userSession.get(AuxCaja7SearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (auxCaja7SearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AuxCaja7SearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AuxCaja7SearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(auxCaja7SearchPageVO, request);
			
            // Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
				saveDemodaErrors(request, auxCaja7SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
			
			// llamada al servicio
			auxCaja7SearchPageVO = BalServiceLocator.getBalanceService().incluirAuxCaja7(userSession, auxCaja7SearchPageVO);
			
            // Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
				saveDemodaErrors(request, auxCaja7SearchPageVO);
				request.setAttribute(AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (auxCaja7SearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7SearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AuxCaja7SearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7SearchPage.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_AUXCAJA7);			
	}

	public ActionForward imprimirReportFromSearchPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String name = AuxCaja7SearchPage.NAME;
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
			AuxCaja7SearchPage auxCaja7SearchPageVO = (AuxCaja7SearchPage) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (auxCaja7SearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(auxCaja7SearchPageVO, request);
		
	        // Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString()); 
				saveDemodaErrors(request, auxCaja7SearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7SearchPage.NAME, auxCaja7SearchPageVO);
			}
		
			// llamada al servicio que genera el reporte
			auxCaja7SearchPageVO = BalServiceLocator.getBalanceService().imprimirAuxCaja7(userSession, auxCaja7SearchPageVO);

			// limpia la lista de reports y la lista de tablas
			auxCaja7SearchPageVO.getReport().getListReport().clear();
			auxCaja7SearchPageVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (auxCaja7SearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7SearchPageVO.infoString());
				saveDemodaErrors(request, auxCaja7SearchPageVO);				
				request.setAttribute(name, auxCaja7SearchPageVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (auxCaja7SearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7SearchPageVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, auxCaja7SearchPageVO);
			
			// obtenemos el nombre del auxCaja7 seleccionado
			String fileName = auxCaja7SearchPageVO.getReport().getReportFileName();

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
