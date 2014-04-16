//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

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
import ar.gov.rosario.siat.cas.iface.model.UsoExpedienteAdapter;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarUsoExpedienteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarUsoExpedienteDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_USOEXPEDIENTE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		UsoExpedienteAdapter usoExpedienteAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getUsoExpedienteAdapterForView(userSession, commonKey)";
				usoExpedienteAdapterVO = CasServiceLocator.getCasCasoService().getUsoExpedienteAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_USOEXPEDIENTE_VIEW_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (usoExpedienteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + usoExpedienteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsoExpedienteAdapter.NAME, usoExpedienteAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			usoExpedienteAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + UsoExpedienteAdapter.NAME + ": "+ usoExpedienteAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(UsoExpedienteAdapter.NAME, usoExpedienteAdapterVO);
			// Subo el apdater al userSession
			userSession.put(UsoExpedienteAdapter.NAME, usoExpedienteAdapterVO);
			 
			saveDemodaMessages(request, usoExpedienteAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsoExpedienteAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, UsoExpedienteAdapter.NAME);
		
	}
	
	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = UsoExpedienteAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			UsoExpedienteAdapter usoExpedienteAdapterVO = (UsoExpedienteAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (usoExpedienteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}
			
			
			// prepara el report del adapter para luego generar el reporte
			usoExpedienteAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			usoExpedienteAdapterVO = CasServiceLocator.getCasCasoService().imprimirConsultaExpediente(userSession, usoExpedienteAdapterVO);

			// limpia la lista de reports y la lista de tablas
			usoExpedienteAdapterVO.getReport().getListReport().clear();
			usoExpedienteAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (usoExpedienteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usoExpedienteAdapterVO.infoString());
				saveDemodaErrors(request, usoExpedienteAdapterVO);				
				request.setAttribute(name, usoExpedienteAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (usoExpedienteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usoExpedienteAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, usoExpedienteAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = usoExpedienteAdapterVO.getReport().getReportFileName();
			
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
