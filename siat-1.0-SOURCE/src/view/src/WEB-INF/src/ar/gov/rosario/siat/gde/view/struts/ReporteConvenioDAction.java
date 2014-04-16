//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ConvenioReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.TipoReporte;

public final class ReporteConvenioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteConvenioDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CONVENIO_REPORT, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConvenioReport convenioReportVO = GdeServiceLocator.getReporteService().getConvenioReportInit(userSession);
			
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			} 

			// Tiene errores no recuperables
			if (convenioReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioReport.NAME, convenioReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConvenioReport.NAME, convenioReportVO);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIO_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConvenioReport convenioReportVO =  (ConvenioReport) userSession.get(ConvenioReport.NAME);
				
			// Si es nulo no se puede continuar
			if (convenioReportVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioReport.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(convenioReportVO, request); request.getParameter("idTipoReporte");
			// carga el tipo de reporte seleccionado porque es una enumeracion que no tiene setters
			convenioReportVO.setTipoReporte(TipoReporte.getById(Integer.valueOf(convenioReportVO.getIdTipoReporte())));
			
			// no visualizo la lista de resultados
			convenioReportVO.setPageNumber(0L);
			
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			}

			convenioReportVO = GdeServiceLocator.getReporteService().getConvenioReportParamRecurso(userSession, convenioReportVO);
				
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   ConvenioReport.NAME, convenioReportVO);
			}
				
			// Tiene errores no recuperables
			if (convenioReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
												  ConvenioReport.NAME, convenioReportVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, convenioReportVO);
				
			// Envio el VO al request
			request.setAttribute(ConvenioReport.NAME, convenioReportVO);

			return mapping.findForward(GdeConstants.FWD_CONVENIO_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
		}
	}

	public ActionForward paramPlan(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConvenioReport convenioReportVO =  (ConvenioReport) userSession.get(ConvenioReport.NAME);
				
			// Si es nulo no se puede continuar
			if (convenioReportVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(convenioReportVO, request);
			// carga el tipo de reporte seleccionado porque es una enumeracion que no tiene setters
			convenioReportVO.setTipoReporte(TipoReporte.getById(Integer.valueOf(convenioReportVO.getIdTipoReporte())));
			
			// no visualizo la lista de resultados
			convenioReportVO.setPageNumber(0L);
				
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			}

			// llamar al servicio
			convenioReportVO = GdeServiceLocator.getReporteService().getConvenioReportParamPlan(userSession, convenioReportVO);
			
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			} 

			// Tiene errores no recuperables
			if (convenioReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioReport.NAME, convenioReportVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, convenioReportVO);
				
			// Envio el VO al request
			request.setAttribute(ConvenioReport.NAME, convenioReportVO);

			return mapping.findForward(GdeConstants.FWD_CONVENIO_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
		}
	}

	public ActionForward paramViaDeuda(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
			
		try {
			//bajo el report del usserSession
			ConvenioReport convenioReportVO =  (ConvenioReport) userSession.get(ConvenioReport.NAME);
				
			// Si es nulo no se puede continuar
			if (convenioReportVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(convenioReportVO, request);
			// carga el tipo de reporte seleccionado porque es una enumeracion que no tiene setters
			convenioReportVO.setTipoReporte(TipoReporte.getById(Integer.valueOf(convenioReportVO.getIdTipoReporte())));
			
			// no visualizo la lista de resultados
			convenioReportVO.setPageNumber(0L);
				
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			}

			// llamar al servicio
			convenioReportVO = GdeServiceLocator.getReporteService().getConvenioReportParamViaDeuda(userSession, convenioReportVO);
			
			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			} 

			// Tiene errores no recuperables
			if (convenioReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioReport.NAME, convenioReportVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, convenioReportVO);
				
			// Envio el VO al request
			request.setAttribute(ConvenioReport.NAME, convenioReportVO);

			return mapping.findForward(GdeConstants.FWD_CONVENIO_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
		}
	}


	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ConvenioReport convenioReportVO = (ConvenioReport) userSession.get(ConvenioReport.NAME);
			
			// Si es nulo no se puede continuar
			if (convenioReportVO == null) {
				log.error("error en: "  + funcName + ": " + ConvenioReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConvenioReport.NAME); 
			}
			
			// siempre el buscar es diparado desde la pagina de busqueda
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(convenioReportVO, request);

			// carga el tipo de reporte seleccionado porque es una enumeracion que no tiene setters
			convenioReportVO.setTipoReporte(TipoReporte.getById(Integer.valueOf(convenioReportVO.getIdTipoReporte())));
			
			// Setea el PageNumber del PageModel				
			convenioReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));

			
            // Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			}
				
			// Llamada al servicio	
			convenioReportVO = GdeServiceLocator.getReporteService().getConvenioReportResult(userSession, convenioReportVO);			

			// Tiene errores recuperables
			if (convenioReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + convenioReportVO.infoString()); 
				saveDemodaErrors(request, convenioReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, ConvenioReport.NAME, convenioReportVO);
			}
			
			// Tiene errores no recuperables
			if (convenioReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + convenioReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConvenioReport.NAME, convenioReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConvenioReport.NAME, convenioReportVO);
			// Nuleo el list result
			// Subo en el el searchPage al userSession
			userSession.put(ConvenioReport.NAME, convenioReportVO);
			
			return mapping.findForward(GdeConstants.FWD_CONVENIO_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
		}
	}
	
	public ActionForward downloadFile(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			try {
				
				// obtenemos el nombre del archivo seleccionado
				String fileName = request.getParameter("fileParam");
				
				baseResponseFile(response,fileName);

				log.debug("exit: " + funcName);
				
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConvenioReport.NAME);
			}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, ConvenioReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ConvenioReport.NAME);
	}
		
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ConvenioReport.NAME);
	}

}
