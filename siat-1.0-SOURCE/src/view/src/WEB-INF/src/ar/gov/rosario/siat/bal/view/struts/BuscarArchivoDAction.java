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

import ar.gov.rosario.siat.bal.iface.model.ArchivoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarArchivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarArchivoDAction.class);
	public final static String ARCHIVO_SEARCHPAGE_FILTRO = "archivoSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		try {

			ArchivoSearchPage archivoSearchPageFiltro = (ArchivoSearchPage) userSession.getNavModel().getParameter(BuscarArchivoDAction.ARCHIVO_SEARCHPAGE_FILTRO);

			ArchivoSearchPage archivoSearchPageVO = BalServiceLocator.getArchivosBancoService().getArchivoSearchPageInit(userSession, archivoSearchPageFiltro);
			
			// Tiene errores recuperables
			if (archivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, archivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ArchivoSearchPage.NAME, archivoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (archivoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ArchivoSearchPage.NAME, archivoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ARCHIVO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, ArchivoSearchPage.NAME);

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
			ArchivoSearchPage archivoSearchPageVO = (ArchivoSearchPage) userSession.get(ArchivoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (archivoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ArchivoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ArchivoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(archivoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				archivoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (archivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, archivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}

			
			// Llamada al servicio	
			archivoSearchPageVO = BalServiceLocator.getArchivosBancoService().getArchivoSearchPageResult(userSession, archivoSearchPageVO);			

			// Tiene errores recuperables
			if (archivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, archivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (archivoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ArchivoSearchPage.NAME, archivoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ArchivoSearchPage.NAME, archivoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ARCHIVO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ARCHIVO);
	}

	public ActionForward anular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ARCHIVO, BaseConstants.ACT_ANULAR);	
	}
	
	public ActionForward aceptar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ARCHIVO, BaseConstants.ACT_ACEPTAR);	
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ARCHIVO);
			
		}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
	
			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, ArchivoSearchPage.NAME);
	}	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
			return baseVolver(mapping, form, request, response, ArchivoSearchPage.NAME);
			
	}

	public ActionForward incluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ARCHIVO, BaseSecurityConstants.INCLUIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			ArchivoSearchPage archivoSearchPageVO = (ArchivoSearchPage) userSession.get(ArchivoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (archivoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ArchivoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ArchivoSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(archivoSearchPageVO, request);
			
            // Tiene errores recuperables
			if (archivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, archivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}
			
			// llamada al servicio
			archivoSearchPageVO = BalServiceLocator.getBalanceService().incluirArchivo(userSession, archivoSearchPageVO);
			
            // Tiene errores recuperables
			if (archivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoSearchPageVO.infoString()); 
				saveDemodaErrors(request, archivoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (archivoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ArchivoSearchPage.NAME, archivoSearchPageVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ArchivoSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ArchivoSearchPage.NAME);
		}
	}

	public ActionForward imprimirReportFromSearchPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String name = ArchivoSearchPage.NAME;
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
			ArchivoSearchPage archivoSearchPageVO = (ArchivoSearchPage) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (archivoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
		
			// llamada al servicio que genera el reporte
			archivoSearchPageVO = BalServiceLocator.getArchivosBancoService().imprimirArchivos(userSession, archivoSearchPageVO);

			// limpia la lista de reports y la lista de tablas
			archivoSearchPageVO.getReport().getListReport().clear();
			archivoSearchPageVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (archivoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + archivoSearchPageVO.infoString());
				saveDemodaErrors(request, archivoSearchPageVO);				
				request.setAttribute(name, archivoSearchPageVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (archivoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + archivoSearchPageVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, archivoSearchPageVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = archivoSearchPageVO.getReport().getReportFileName();

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
