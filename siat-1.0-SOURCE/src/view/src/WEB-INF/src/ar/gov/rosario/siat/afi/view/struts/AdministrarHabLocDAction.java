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

import ar.gov.rosario.siat.afi.iface.model.HabLocAdapter;
import ar.gov.rosario.siat.afi.iface.service.AfiServiceLocator;
import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.afi.view.util.AfiConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarHabLocDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarHabLocDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, AfiSecurityConstants.ABM_HABLOC, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		HabLocAdapter habLocAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getHabLocAdapterForView(userSession, commonKey)";
				habLocAdapterVO = AfiServiceLocator.getFormulariosDJService().getHabLocAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(AfiConstants.FWD_HABLOC_VIEW_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (habLocAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + habLocAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HabLocAdapter.NAME, habLocAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			habLocAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + HabLocAdapter.NAME + ": "+ habLocAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(HabLocAdapter.NAME, habLocAdapterVO);
			// Subo el apdater al userSession
			userSession.put(HabLocAdapter.NAME, habLocAdapterVO);
			 
			saveDemodaMessages(request, habLocAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HabLocAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, HabLocAdapter.NAME);
		
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
			String name = HabLocAdapter.NAME;
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
			HabLocAdapter habLocAdapterVO = (HabLocAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (habLocAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, HabLocAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			habLocAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			//TODO: determinar si es necesario imprimir reporte HabLoc
			//habLocAdapterVO = AfiServiceLocator.getFormulariosDJService().imprimirHabLoc(userSession, habLocAdapterVO);

			// limpia la lista de reports y la lista de tablas
			habLocAdapterVO.getReport().getListReport().clear();
			habLocAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (habLocAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + habLocAdapterVO.infoString());
				saveDemodaErrors(request, habLocAdapterVO);				
				request.setAttribute(name, habLocAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (habLocAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + habLocAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, habLocAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = habLocAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, HabLocAdapter.NAME);
			
	}
	
}
