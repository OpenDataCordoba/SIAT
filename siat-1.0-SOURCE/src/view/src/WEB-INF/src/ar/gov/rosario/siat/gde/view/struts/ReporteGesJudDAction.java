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

import ar.gov.rosario.siat.bal.iface.model.ProcesoAsentamientoAdapter;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudReport;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class ReporteGesJudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(ReporteGesJudDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GEJUD_REPORT, act);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			GesJudReport gesJudReportVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudReportInit(userSession);
			
			// Tiene errores recuperables
			if (gesJudReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudReportVO.infoString()); 
				saveDemodaErrors(request, gesJudReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudReport.NAME, gesJudReportVO);
			} 

			// Tiene errores no recuperables
			if (gesJudReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudReport.NAME, gesJudReportVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , GesJudReport.NAME, gesJudReportVO);
			
			return mapping.findForward(GdeConstants.FWD_GESJUD_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudReport.NAME);
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
			GesJudReport GesJudReportVO =  (GesJudReport) userSession.get(GesJudReport.NAME);
				
			// Si es nulo no se puede continuar
			if (GesJudReportVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudReport.NAME + " " +
						  "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudReport.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(GesJudReportVO, request);
			
			// no visualizo la lista de resultados
			GesJudReportVO.setPageNumber(0L);
			
			// Tiene errores recuperables
			if (GesJudReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + GesJudReportVO.infoString()); 
				saveDemodaErrors(request, GesJudReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudReport.NAME, GesJudReportVO);
			}

			GesJudReportVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudReportParamRecurso(userSession, GesJudReportVO);
				
			// Tiene errores recuperables
			if (GesJudReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + GesJudReportVO.infoString()); 
				saveDemodaErrors(request, GesJudReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
											   GesJudReport.NAME, GesJudReportVO);
			}
				
			// Tiene errores no recuperables
			if (GesJudReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + GesJudReportVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						GesJudReport.NAME, GesJudReportVO);
			}
				
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, GesJudReportVO);
				
			// Envio el VO al request
			request.setAttribute(GesJudReport.NAME, GesJudReportVO);

			return mapping.findForward(GdeConstants.FWD_GESJUD_REPORT);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudReport.NAME);
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
			GesJudReport gesJudReportVO = (GesJudReport) userSession.get(GesJudReport.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudReportVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudReport.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudReport.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudReportVO, request);
			// Setea el PageNumber del PageModel				
			gesJudReportVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			
            // Tiene errores recuperables
			if (gesJudReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudReportVO.infoString()); 
				saveDemodaErrors(request, gesJudReportVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudReport.NAME, gesJudReportVO);
			}
			
			// Populate de los id seleccionados en los combos y campos de texto
			gesJudReportVO.setStrEventosOpe("");
   			for (EventoVO item: gesJudReportVO.getListEvento()){
   				String nombreAtrOp = item.getId()+"_operacion";
   				String nombreAtrOpTiempo = item.getId()+"_opTiempo";
   				String nombreAtrT = item.getId()+"_tiempo";
   				String nombreAtrUni = item.getId()+"_unidad";
   				String valorTiempo = request.getParameter(nombreAtrT);
				String valorOperacion = request.getParameter(nombreAtrOp);
				String valorOpTiempo = request.getParameter(nombreAtrOpTiempo);
				String valorUnidad = request.getParameter(nombreAtrUni);
				log.debug(nombreAtrOp+"    valor:"+valorOperacion+
   						"      "+nombreAtrT+"      valor:"+valorTiempo+
   						"      "+nombreAtrUni+"     valor:"+valorUnidad+
   						"      "+nombreAtrOpTiempo+"      valor:"+valorOpTiempo);
				
				if(!StringUtil.isNullOrEmpty(valorTiempo)){
					if(NumberUtil.getLong(valorTiempo)==null){
						gesJudReportVO.addRecoverableError(GdeError.GESJUD_REPORT_FORMATO_TIEMPO_INVALIDO);
						break;
					}else if(valorOperacion.equals("0")){
						gesJudReportVO.addRecoverableError(GdeError.GESJUD_REPORT_OPERACION_REQUERIDO_4_TIEMPO);
						break;
					}					
				}
				
				if(!valorOperacion.equals("0"))// Solo se agrega un evento si selecciono alguna accion
					gesJudReportVO.addEventoOpe(item.getIdView(),valorOperacion,valorOpTiempo,valorTiempo,valorUnidad);   				
   			}

			if (gesJudReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudReportVO.infoString()); 
				saveDemodaErrors(request, gesJudReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, GesJudReport.NAME, gesJudReportVO);
			}

			// Llamada al servicio	
			gesJudReportVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudReportResult(userSession, gesJudReportVO);			

			// Tiene errores recuperables
			if (gesJudReportVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudReportVO.infoString()); 
				saveDemodaErrors(request, gesJudReportVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, GesJudReport.NAME, gesJudReportVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudReportVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudReportVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudReport.NAME, gesJudReportVO);
			}
		
			// Envio el VO al request
			request.setAttribute(GesJudReport.NAME, gesJudReportVO);
			// Subo el searchPage al userSession
			userSession.put(GesJudReport.NAME, gesJudReportVO);
			
			return mapping.findForward(GdeConstants.FWD_GESJUD_REPORT);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudReport.NAME);
		}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		GesJudReport gesJudReport = (GesJudReport) userSession.get(GesJudReport.NAME);
		
		// Si es nulo no se puede continuar
		if (gesJudReport == null) {
			log.error("error en: "  + funcName + ": " + GesJudReport.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, GesJudReport.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(gesJudReport, request);
		
        // Tiene errores recuperables
		if (gesJudReport.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + gesJudReport.infoString()); 
			saveDemodaErrors(request, gesJudReport);

			request.setAttribute(GesJudReport.NAME, gesJudReport);
			return mapping.getInputForward();
		}
		

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(gesJudReport.getCuenta().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(gesJudReport.getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, 
				"paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA, false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			GesJudReport gesJudReport =  (GesJudReport) userSession.get(GesJudReport.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudReport == null) {
				log.error("error en: "  + funcName + ": " + GesJudReport.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudReport.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(GesJudReport.NAME, gesJudReport);
				return mapping.findForward(GdeConstants.FWD_GESJUD_REPORT); 
			}
			
			// Seteo el id de la cuenta
			gesJudReport.getCuenta().setId(new Long(selectedId));
			
			gesJudReport = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl()
				.getGesJudReportParamCuenta(userSession, gesJudReport);
			
            // Tiene errores recuperables
			if (gesJudReport.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudReport.infoString()); 
				saveDemodaErrors(request, gesJudReport);
				return forwardErrorRecoverable(mapping, request, userSession, 
						GesJudReport.NAME, gesJudReport);
			}
			
			// Tiene errores no recuperables
			if (gesJudReport.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudReport.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						GesJudReport.NAME, gesJudReport);
			}
			
			
			// Envio el VO al request
			request.setAttribute(GesJudReport.NAME, gesJudReport);
			
			return mapping.findForward(GdeConstants.FWD_GESJUD_REPORT);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudReport.NAME);
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
				return baseException(mapping, request, funcName, exception, ProcesoAsentamientoAdapter.NAME);
			}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, GesJudReport.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, GesJudReport.NAME);
	}
		

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, GesJudReport.NAME);
	}

}
