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

import ar.gov.rosario.siat.bal.iface.model.MovBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.MovBanVO;
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

public final class AdministrarMovBanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarMovBanDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBAN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		MovBanAdapter movBanAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getMovBanAdapterForView(userSession, commonKey)";
				movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBAN_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getMovBanAdapterForView(userSession, commonKey)";
				movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBAN_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getMovBanAdapterForView(userSession, commonKey)";
				movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanAdapterForView(userSession, commonKey);
				movBanAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.MOVBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBAN_VIEW_ADAPTER);				
			}		
			if (navModel.getAct().equals(BaseConstants.ACT_CONCILIAR)) {
				stringServicio = "getMovBanAdapterForView(userSession, commonKey)";
				movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().getMovBanAdapterForView(userSession, commonKey);
				movBanAdapterVO.addMessage(BaseError.MSG_CONCILIAR, BalError.MOVBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_MOVBAN_VIEW_ADAPTER);				
			}		

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (movBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + movBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanAdapter.NAME, movBanAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			movBanAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + MovBanAdapter.NAME + ": "+ movBanAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(MovBanAdapter.NAME, movBanAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MovBanAdapter.NAME, movBanAdapterVO);
			 
			saveDemodaMessages(request, movBanAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_ENC_MOVBAN, BaseConstants.ACT_MODIFICAR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MovBanAdapter movBanAdapterVO = (MovBanAdapter) userSession.get(MovBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (movBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MovBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MovBanAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(movBanAdapterVO, request);
			
            // Tiene errores recuperables
			if (movBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString()); 
				saveDemodaErrors(request, movBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanAdapter.NAME, movBanAdapterVO);
			}
			
			// llamada al servicio
			MovBanVO movBanVO = BalServiceLocator.getConciliacionOsirisService().updateMovBan(userSession, movBanAdapterVO.getMovBan());
			
            // Tiene errores recuperables
			if (movBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString()); 
				saveDemodaErrors(request, movBanVO);
				return forwardErrorRecoverable(mapping, request, userSession, MovBanAdapter.NAME, movBanAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (movBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanAdapter.NAME, movBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MovBanAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MovBanAdapter movBanAdapterVO = (MovBanAdapter) userSession.get(MovBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (movBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MovBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MovBanAdapter.NAME); 
			}

			// llamada al servicio
			MovBanVO movBanVO = BalServiceLocator.getConciliacionOsirisService().deleteMovBan
				(userSession, movBanAdapterVO.getMovBan());
			
            // Tiene errores recuperables
			if (movBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString());
				saveDemodaErrors(request, movBanVO);				
				request.setAttribute(MovBanAdapter.NAME, movBanAdapterVO);
				return mapping.findForward(BalConstants.FWD_MOVBAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (movBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanAdapter.NAME, movBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MovBanAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MovBanAdapter.NAME);
		
	}
   
	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			String name = MovBanAdapter.NAME;
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
			MovBanAdapter movBanAdapterVO = (MovBanAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (movBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}

			// prepara el report del adapter para luego generar el reporte
			movBanAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			movBanAdapterVO = BalServiceLocator.getConciliacionOsirisService().imprimirMovBan(userSession, movBanAdapterVO);

			// limpia la lista de reports y la lista de tablas
			movBanAdapterVO.getReport().getListReport().clear();
			movBanAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (movBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString());
				saveDemodaErrors(request, movBanAdapterVO);				
				request.setAttribute(name, movBanAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (movBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, movBanAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = movBanAdapterVO.getReport().getReportFileName();

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
			return baseRefill(mapping, form, request, response, funcName, MovBanAdapter.NAME);
			
		}
	
	public ActionForward conciliar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.CONCILIAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MovBanAdapter movBanAdapterVO = (MovBanAdapter) userSession.get(MovBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (movBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MovBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MovBanAdapter.NAME); 
			}

			// llamada al servicio
			MovBanVO movBanVO = BalServiceLocator.getConciliacionOsirisService().conciliarMovBan(userSession, movBanAdapterVO.getMovBan());
			
            // Tiene errores recuperables
			if (movBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + movBanAdapterVO.infoString());
				saveDemodaErrors(request, movBanVO);				
				request.setAttribute(MovBanAdapter.NAME, movBanAdapterVO);
				return mapping.findForward(BalConstants.FWD_MOVBAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (movBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + movBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MovBanAdapter.NAME, movBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MovBanAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MovBanAdapter.NAME);
		}
	}
	
	// Metodos relacionados MovBanDet
	public ActionForward verMovBanDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_MOVBANDET);

	}

	public ActionForward conciliarMovBanDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_MOVBANDET, BaseConstants.ACT_CONCILIAR);
	}
	
	
}
