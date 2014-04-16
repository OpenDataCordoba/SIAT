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

import ar.gov.rosario.siat.bal.iface.model.TranBalAdapter;
import ar.gov.rosario.siat.bal.iface.model.TranBalVO;
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

public class AdministrarTranBalDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTranBalDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANBAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TranBalAdapter tranBalAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTranBalAdapterForView(userSession, commonKey)";
				tranBalAdapterVO = BalServiceLocator.getBalanceService().getTranBalAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TRANBAL_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTranBalAdapterForUpdate(userSession, commonKey)";
				tranBalAdapterVO = BalServiceLocator.getBalanceService().getTranBalAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TRANBAL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTranBalAdapterForView(userSession, commonKey)";
				tranBalAdapterVO = BalServiceLocator.getBalanceService().getTranBalAdapterForView(userSession, commonKey);				
				tranBalAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.TRANBAL_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TRANBAL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTranBalAdapterForCreate(userSession)";
				tranBalAdapterVO = BalServiceLocator.getBalanceService().getTranBalAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TRANBAL_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tranBalAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tranBalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tranBalAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TranBalAdapter.NAME + ": "+ tranBalAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TranBalAdapter.NAME, tranBalAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TranBalAdapter.NAME, tranBalAdapterVO);
			 
			saveDemodaMessages(request, tranBalAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranBalAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TranBalAdapter tranBalAdapterVO = (TranBalAdapter) userSession.get(TranBalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tranBalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TranBalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TranBalAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tranBalAdapterVO, request);
			
            // Tiene errores recuperables
			if (tranBalAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalAdapterVO.infoString()); 
				saveDemodaErrors(request, tranBalAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// llamada al servicio
			TranBalVO tranBalVO = BalServiceLocator.getBalanceService().createTranBal(userSession, tranBalAdapterVO.getTranBal());
			
            // Tiene errores recuperables
			if (tranBalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalVO.infoString()); 
				saveDemodaErrors(request, tranBalVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tranBalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tranBalVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TranBalAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranBalAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TranBalAdapter tranBalAdapterVO = (TranBalAdapter) userSession.get(TranBalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tranBalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TranBalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TranBalAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tranBalAdapterVO, request);
			
            // Tiene errores recuperables
			if (tranBalAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalAdapterVO.infoString()); 
				saveDemodaErrors(request, tranBalAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// llamada al servicio
			TranBalVO tranBalVO = BalServiceLocator.getBalanceService().updateTranBal(userSession, tranBalAdapterVO.getTranBal());
			
            // Tiene errores recuperables
			if (tranBalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalAdapterVO.infoString()); 
				saveDemodaErrors(request, tranBalVO);
				return forwardErrorRecoverable(mapping, request, userSession, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tranBalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tranBalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TranBalAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranBalAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TRANBAL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TranBalAdapter tranBalAdapterVO = (TranBalAdapter) userSession.get(TranBalAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tranBalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TranBalAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TranBalAdapter.NAME); 
			}

			// llamada al servicio
			TranBalVO tranBalVO = BalServiceLocator.getBalanceService().deleteTranBal
				(userSession, tranBalAdapterVO.getTranBal());
			
            // Tiene errores recuperables
			if (tranBalVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalAdapterVO.infoString());
				saveDemodaErrors(request, tranBalVO);				
				request.setAttribute(TranBalAdapter.NAME, tranBalAdapterVO);
				return mapping.findForward(BalConstants.FWD_TRANBAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tranBalVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tranBalAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TranBalAdapter.NAME, tranBalAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TranBalAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TranBalAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TranBalAdapter.NAME);
		
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
			String name = TranBalAdapter.NAME;
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
			TranBalAdapter tranBalAdapterVO = (TranBalAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tranBalAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  tranBalAdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tranBalAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tranBalAdapterVO = BalServiceLocator.getBalanceService().imprimirTranBal(userSession, tranBalAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tranBalAdapterVO.getReport().getListReport().clear();
			tranBalAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tranBalAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tranBalAdapterVO.infoString());
				saveDemodaErrors(request, tranBalAdapterVO);				
				request.setAttribute(name, tranBalAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tranBalAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tranBalAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  tranBalAdapterVO.NAME,  tranBalAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tranBalAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tranBalAdapterVO.getReport().getReportFileName();

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
