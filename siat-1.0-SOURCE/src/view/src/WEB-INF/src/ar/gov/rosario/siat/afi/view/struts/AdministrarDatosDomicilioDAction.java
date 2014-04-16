//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.afi.iface.model.DatosDomicilioAdapter;
import ar.gov.rosario.siat.afi.iface.service.AfiServiceLocator;
import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.afi.view.util.AfiConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDatosDomicilioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDatosDomicilioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, AfiSecurityConstants.ABM_DATOSDOMICILIO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DatosDomicilioAdapter datosDomicilioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDatosDomicilioAdapterForView(userSession, commonKey)";
				datosDomicilioAdapterVO = AfiServiceLocator.getFormulariosDJService().getDatosDomicilioAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(AfiConstants.FWD_DATOSDOMICILIO_VIEW_ADAPTER);
			}			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (datosDomicilioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + datosDomicilioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DatosDomicilioAdapter.NAME, datosDomicilioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			datosDomicilioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DatosDomicilioAdapter.NAME + ": "+ datosDomicilioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DatosDomicilioAdapter.NAME, datosDomicilioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DatosDomicilioAdapter.NAME, datosDomicilioAdapterVO);
			 
			saveDemodaMessages(request, datosDomicilioAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DatosDomicilioAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DatosDomicilioAdapter.NAME);
		
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
			String name = DatosDomicilioAdapter.NAME;
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
			DatosDomicilioAdapter datosDomicilioAdapterVO = (DatosDomicilioAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (datosDomicilioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, DatosDomicilioAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			datosDomicilioAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			//TODO: ver si es necesario Imprimir Reporte de Datos Domicilio
//			datosDomicilioAdapterVO = AfiServiceLocator.getFormulariosDJService().imprimirDatosDomicilio(userSession, datosDomicilioAdapterVO);

			// limpia la lista de reports y la lista de tablas
			datosDomicilioAdapterVO.getReport().getListReport().clear();
			datosDomicilioAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (datosDomicilioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + datosDomicilioAdapterVO.infoString());
				saveDemodaErrors(request, datosDomicilioAdapterVO);				
				request.setAttribute(name, datosDomicilioAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (datosDomicilioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + datosDomicilioAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, datosDomicilioAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = datosDomicilioAdapterVO.getReport().getReportFileName();

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
