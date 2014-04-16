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

import ar.gov.rosario.siat.bal.iface.model.CierreBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CierreBancoVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCierreBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCierreBancoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CIERREBANCO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CierreBancoAdapter cierreBancoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCierreBancoAdapterForView(userSession, commonKey)";
				cierreBancoAdapterVO = BalServiceLocator.getEnvioOsirisService().getCierreBancoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CIERREBANCO_VIEW_ADAPTER);
			}	
			if (act.equals(BaseConstants.ACT_CONCILIAR)) {
				stringServicio = "getCierreBancoAdapterForConciliar(userSession, commonKey)";
				cierreBancoAdapterVO = BalServiceLocator.getConciliacionOsirisService().getCierreBancoAdapterForConciliar(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CIERREBANCO_EDIT_ADAPTER);
			}	

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cierreBancoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cierreBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreBancoAdapter.NAME, cierreBancoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cierreBancoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CierreBancoAdapter.NAME + ": "+ cierreBancoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CierreBancoAdapter.NAME, cierreBancoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CierreBancoAdapter.NAME, cierreBancoAdapterVO);
			 
			saveDemodaMessages(request, cierreBancoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreBancoAdapter.NAME);
		}
	}
		
	public ActionForward verTransaccioAfip(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANAFIP);
			
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CierreBancoAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CierreBancoAdapter.NAME);
			
	}
	
	/**
	 * Esta opcion no debe usarse. Es solo para la etapa de desarrollo
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward generarDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANAFIP, BalConstants.ACT_GENERAR_DECJUR);
	}
	
	public ActionForward conciliar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CIERREBANCO, BaseSecurityConstants.CONCILIAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreBancoAdapter cierreBancoAdapterVO = (CierreBancoAdapter) userSession.get(CierreBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cierreBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreBancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreBancoAdapter.NAME); 
			}

			// llamada al servicio
			CierreBancoVO cierreBancoVO = BalServiceLocator.getConciliacionOsirisService().conciliarCierreBanco(userSession, cierreBancoAdapterVO.getCierreBanco());
			
            // Tiene errores recuperables
			if (cierreBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreBancoAdapterVO.infoString());
				saveDemodaErrors(request, cierreBancoVO);				
				request.setAttribute(CierreBancoAdapter.NAME, cierreBancoAdapterVO);
				return mapping.findForward(BalConstants.FWD_CIERREBANCO_EDIT_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cierreBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreBancoAdapter.NAME, cierreBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CierreBancoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreBancoAdapter.NAME);
		}
	}
	
	public ActionForward eliminarTranAfip(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TRANAFIP);
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
			String name = CierreBancoAdapter.NAME;
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
			CierreBancoAdapter cierreBancoAdapterVO = (CierreBancoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (cierreBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, CierreBancoAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			cierreBancoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			cierreBancoAdapterVO = BalServiceLocator.getConciliacionOsirisService().imprimirCierreBancoPDF(userSession, cierreBancoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			cierreBancoAdapterVO.getReport().getListReport().clear();
			cierreBancoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (cierreBancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreBancoAdapterVO.infoString());
				saveDemodaErrors(request, cierreBancoAdapterVO);				
				request.setAttribute(name, cierreBancoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (cierreBancoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreBancoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, cierreBancoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = cierreBancoAdapterVO.getReport().getReportFileName();

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
