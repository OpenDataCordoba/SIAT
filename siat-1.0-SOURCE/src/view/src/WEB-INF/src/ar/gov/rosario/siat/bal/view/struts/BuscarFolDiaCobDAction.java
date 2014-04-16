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

import ar.gov.rosario.siat.bal.iface.model.FolDiaCobSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

/**
 *  Consulta de Dias de Cobranza de varios Folios con totales
 * 
 * @author pgp
 *
 */
public class BuscarFolDiaCobDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarFolDiaCobDAction.class);
	public final static String FOLDIACOB_SEARCHPAGE_FILTRO = "folDiaCobSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLDIACOB, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
		
			FolDiaCobSearchPage folioSearchPageVO = BalServiceLocator.getFolioTesoreriaService().getFolDiaCobSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (folioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioSearchPageVO.infoString()); 
				saveDemodaErrors(request, folioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobSearchPage.NAME, folioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (folioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FolDiaCobSearchPage.NAME, folioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , FolDiaCobSearchPage.NAME, folioSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_FOLDIACOB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolDiaCobSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, FolDiaCobSearchPage.NAME);

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
			FolDiaCobSearchPage folioSearchPageVO = (FolDiaCobSearchPage) userSession.get(FolDiaCobSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (folioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FolDiaCobSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolDiaCobSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(folioSearchPageVO, request);
				// Setea el PageModel sin paginacion				
				folioSearchPageVO.setPaged(false);
			}
			
            // Tiene errores recuperables
			if (folioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioSearchPageVO.infoString()); 
				saveDemodaErrors(request, folioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobSearchPage.NAME, folioSearchPageVO);
			}

			// Llamada al servicio	
			folioSearchPageVO = BalServiceLocator.getFolioTesoreriaService().getFolDiaCobSearchPageResult(userSession, folioSearchPageVO);			

			// Tiene errores recuperables
			if (folioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folioSearchPageVO.infoString()); 
				saveDemodaErrors(request, folioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobSearchPage.NAME, folioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (folioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, FolDiaCobSearchPage.NAME, folioSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(FolDiaCobSearchPage.NAME, folioSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(FolDiaCobSearchPage.NAME, folioSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_FOLDIACOB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolDiaCobSearchPage.NAME);
		}
	}



	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, FolDiaCobSearchPage.NAME);
			
	}
	
	public ActionForward guardar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_FOLDIACOB, BaseSecurityConstants.GUARDAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			FolDiaCobSearchPage folDiaCobSearchPageVO = (FolDiaCobSearchPage) userSession.get(FolDiaCobSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (folDiaCobSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + FolDiaCobSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FolDiaCobSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(folDiaCobSearchPageVO, request);
			// Verificamos el caso de ninguno seleccionado para vaciar el array.
			if(request.getParameter("listIdFolDiaCobConciliado") == null){
				folDiaCobSearchPageVO.setListIdFolDiaCobConciliado(new String[0]);
			}
			
            // Tiene errores recuperables
			if (folDiaCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folDiaCobSearchPageVO.infoString()); 
				saveDemodaErrors(request, folDiaCobSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobSearchPage.NAME, folDiaCobSearchPageVO);
			}
			
			// llamada al servicio
			folDiaCobSearchPageVO = BalServiceLocator.getFolioTesoreriaService().conciliarFolDiaCob(userSession, folDiaCobSearchPageVO);
			
            // Tiene errores recuperables
			if (folDiaCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folDiaCobSearchPageVO.infoString()); 
				saveDemodaErrors(request, folDiaCobSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobSearchPage.NAME, folDiaCobSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (folDiaCobSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folDiaCobSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FolDiaCobSearchPage.NAME, folDiaCobSearchPageVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FolDiaCobSearchPage.NAME);
			//return forwardConfirmarOk(mapping, request, funcName, FolDiaCobSearchPage.NAME,BalConstants.PATH_BUSCAR_FOLDIACOB, BaseConstants.ACT_BUSCAR,BaseConstants.ACT_BUSCAR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FolDiaCobSearchPage.NAME);
		}
	}
	
	public ActionForward imprimirReportFromSearchPage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String name = FolDiaCobSearchPage.NAME;
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
			FolDiaCobSearchPage folDiaCobSearchPageVO = (FolDiaCobSearchPage) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (folDiaCobSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
		
			// llamada al servicio que genera el reporte
			folDiaCobSearchPageVO = BalServiceLocator.getFolioTesoreriaService().imprimirFolDiaCobPDF(userSession, folDiaCobSearchPageVO);

			// limpia la lista de reports y la lista de tablas
			folDiaCobSearchPageVO.getReport().getListReport().clear();
			folDiaCobSearchPageVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (folDiaCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + folDiaCobSearchPageVO.infoString());
				saveDemodaErrors(request, folDiaCobSearchPageVO);				
				request.setAttribute(name, folDiaCobSearchPageVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (folDiaCobSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + folDiaCobSearchPageVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, folDiaCobSearchPageVO);
			
			// obtenemos el nombre del reporte
			String fileName = folDiaCobSearchPageVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}

	public ActionForward generarPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
			
				String name = FolDiaCobSearchPage.NAME;
				
				// Bajo el adapter del userSession
				FolDiaCobSearchPage folDiaCobSearchPageVO = (FolDiaCobSearchPage) userSession.get(name);
				
				// Si es nulo no se puede continuar
				if (folDiaCobSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + FolDiaCobSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, FolDiaCobSearchPage.NAME); 
				}
				
				// Llamar a Servicio que genere el archivo y devuelva el fileName con el Path y nombre de la planilla generada
				folDiaCobSearchPageVO  = BalServiceLocator.getFolioTesoreriaService().generarPlanilla(userSession, folDiaCobSearchPageVO);
				
				  // Tiene errores recuperables
				if (folDiaCobSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + folDiaCobSearchPageVO.infoString()); 
					saveDemodaErrors(request, folDiaCobSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, FolDiaCobSearchPage.NAME, folDiaCobSearchPageVO);
				}
				
				baseResponseFile(response,folDiaCobSearchPageVO.getPlanillaFileName());

				log.debug("exit: " + funcName);
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, FolDiaCobSearchPage.NAME);
			}
	}
}
