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

import ar.gov.rosario.siat.bal.iface.model.MovBanDetAdapter;
import ar.gov.rosario.siat.bal.iface.model.MovBanDetVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarMovBanDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarMovBanDetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBANDET, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		MovBanDetAdapter movBanDetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getMovBanDetAdapterForView(userSession, commonKey)";
				movBanDetAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanDetAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBANDET_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_CONCILIAR)) {
				stringServicio = "getMovBanDetAdapterForView(userSession)";
				movBanDetAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanDetAdapterForView(userSession, commonKey);
				movBanDetAdapterVO.addMessage(BaseError.MSG_CONCILIAR, BalError.MOVBANDET_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBANDET_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (movBanDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + movBanDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanDetAdapter.NAME, movBanDetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			movBanDetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + MovBanDetAdapter.NAME + ": "+ movBanDetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(MovBanDetAdapter.NAME, movBanDetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MovBanDetAdapter.NAME, movBanDetAdapterVO);
			 
			saveDemodaMessages(request, movBanDetAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanDetAdapter.NAME);
		}
	}
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MovBanDetAdapter.NAME);
		
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
			String name = MovBanDetAdapter.NAME;
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
			MovBanDetAdapter movBanDetAdapterVO = (MovBanDetAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (movBanDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  movBanDetAdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			movBanDetAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			movBanDetAdapterVO = BalServiceLocator.getConciliacionOsirisService().imprimirMovBanDet(userSession, movBanDetAdapterVO);

			// limpia la lista de reports y la lista de tablas
			movBanDetAdapterVO.getReport().getListReport().clear();
			movBanDetAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (movBanDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanDetAdapterVO.infoString());
				saveDemodaErrors(request, movBanDetAdapterVO);				
				request.setAttribute(name, movBanDetAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (movBanDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanDetAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  movBanDetAdapterVO.NAME,  movBanDetAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, movBanDetAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = movBanDetAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}         
	}
	
	public ActionForward conciliar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBANDET, BaseSecurityConstants.CONCILIAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MovBanDetAdapter movBanDetAdapterVO = (MovBanDetAdapter) userSession.get(MovBanDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (movBanDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MovBanDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MovBanDetAdapter.NAME); 
			}

			// llamada al servicio
			MovBanDetVO movBanDetVO = BalServiceLocator.getConciliacionOsirisService().conciliarMovBanDet(userSession, movBanDetAdapterVO.getMovBanDet());
			
            // Tiene errores recuperables
			if (movBanDetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanDetAdapterVO.infoString());
				saveDemodaErrors(request, movBanDetVO);				
				request.setAttribute(MovBanDetAdapter.NAME, movBanDetAdapterVO);
				return mapping.findForward(BalConstants.FWD_MOVBANDET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (movBanDetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanDetAdapter.NAME, movBanDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MovBanDetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanDetAdapter.NAME);
		}
	}
		
}
