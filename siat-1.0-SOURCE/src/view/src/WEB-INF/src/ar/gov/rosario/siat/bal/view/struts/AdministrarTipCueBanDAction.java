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

import ar.gov.rosario.siat.bal.iface.model.TipCueBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipCueBanVO;
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

public final class AdministrarTipCueBanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipCueBanDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipCueBanAdapter tipCueBanAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipCueBanAdapterForView(userSession, commonKey)";
				tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().getTipCueBanAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipCueBanAdapterForUpdate(userSession, commonKey)";
				tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().getTipCueBanAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPCUEBAN_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipCueBanAdapterForView(userSession, commonKey)";
				tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().getTipCueBanAdapterForView(userSession, commonKey);				
				tipCueBanAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.TIPCUEBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipCueBanAdapterForCreate(userSession)";
				tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().getTipCueBanAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_TIPCUEBAN_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipCueBanAdapterForView(userSession)";
				tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().getTipCueBanAdapterForView(userSession, commonKey);
				tipCueBanAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.TIPCUEBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipCueBanAdapterForView(userSession)";
				tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().getTipCueBanAdapterForView(userSession, commonKey);
				tipCueBanAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.TIPCUEBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipCueBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipCueBanAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipCueBanAdapter.NAME + ": "+ tipCueBanAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			 
			saveDemodaMessages(request, tipCueBanAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipCueBanAdapter tipCueBanAdapterVO = (TipCueBanAdapter) userSession.get(TipCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipCueBanAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipCueBanAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipCueBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString()); 
				saveDemodaErrors(request, tipCueBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// llamada al servicio
			TipCueBanVO tipCueBanVO = BalServiceLocator.getDefinicionService().createTipCueBan(userSession, tipCueBanAdapterVO.getTipCueBan());
			
            // Tiene errores recuperables
			if (tipCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanVO.infoString()); 
				saveDemodaErrors(request, tipCueBanVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipCueBanAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipCueBanAdapter tipCueBanAdapterVO = (TipCueBanAdapter) userSession.get(TipCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipCueBanAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipCueBanAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipCueBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString()); 
				saveDemodaErrors(request, tipCueBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// llamada al servicio
			TipCueBanVO tipCueBanVO = BalServiceLocator.getDefinicionService().updateTipCueBan(userSession, tipCueBanAdapterVO.getTipCueBan());
			
            // Tiene errores recuperables
			if (tipCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString()); 
				saveDemodaErrors(request, tipCueBanVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipCueBanAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipCueBanAdapter tipCueBanAdapterVO = (TipCueBanAdapter) userSession.get(TipCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipCueBanAdapter.NAME); 
			}

			// llamada al servicio
			TipCueBanVO tipCueBanVO = BalServiceLocator.getDefinicionService().deleteTipCueBan
				(userSession, tipCueBanAdapterVO.getTipCueBan());
			
            // Tiene errores recuperables
			if (tipCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString());
				saveDemodaErrors(request, tipCueBanVO);				
				request.setAttribute(TipCueBanAdapter.NAME, tipCueBanAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipCueBanAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipCueBanAdapter tipCueBanAdapterVO = (TipCueBanAdapter) userSession.get(TipCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipCueBanAdapter.NAME); 
			}

			// llamada al servicio
			TipCueBanVO tipCueBanVO = BalServiceLocator.getDefinicionService().activarTipCueBan
				(userSession, tipCueBanAdapterVO.getTipCueBan());
			
            // Tiene errores recuperables
			if (tipCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString());
				saveDemodaErrors(request, tipCueBanVO);				
				request.setAttribute(TipCueBanAdapter.NAME, tipCueBanAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipCueBanAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPCUEBAN, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipCueBanAdapter tipCueBanAdapterVO = (TipCueBanAdapter) userSession.get(TipCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipCueBanAdapter.NAME); 
			}

			// llamada al servicio
			TipCueBanVO tipCueBanVO = BalServiceLocator.getDefinicionService().desactivarTipCueBan
				(userSession, tipCueBanAdapterVO.getTipCueBan());
			
            // Tiene errores recuperables
			if (tipCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString());
				saveDemodaErrors(request, tipCueBanVO);				
				request.setAttribute(TipCueBanAdapter.NAME, tipCueBanAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPCUEBAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipCueBanAdapter.NAME, tipCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipCueBanAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipCueBanAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipCueBanAdapter.NAME);
		
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
			String name = TipCueBanAdapter.NAME;
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
			TipCueBanAdapter tipCueBanAdapterVO = (TipCueBanAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  tipCueBanAdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipCueBanAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipCueBanAdapterVO = BalServiceLocator.getDefinicionService().imprimirTipCueBan(userSession, tipCueBanAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipCueBanAdapterVO.getReport().getListReport().clear();
			tipCueBanAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipCueBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipCueBanAdapterVO.infoString());
				saveDemodaErrors(request, tipCueBanAdapterVO);				
				request.setAttribute(name, tipCueBanAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipCueBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipCueBanAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  tipCueBanAdapterVO.NAME,  tipCueBanAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipCueBanAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipCueBanAdapterVO.getReport().getReportFileName();

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
