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

import ar.gov.rosario.siat.bal.iface.model.Caja69Adapter;
import ar.gov.rosario.siat.bal.iface.model.Caja69VO;
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

public class AdministrarCaja69DAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCaja69DAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA69, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		Caja69Adapter caja69AdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCaja69AdapterForView(userSession, commonKey)";
				caja69AdapterVO = BalServiceLocator.getBalanceService().getCaja69AdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA69_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCaja69AdapterForUpdate(userSession, commonKey)";
				caja69AdapterVO = BalServiceLocator.getBalanceService().getCaja69AdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA69_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCaja69AdapterForView(userSession, commonKey)";
				caja69AdapterVO = BalServiceLocator.getBalanceService().getCaja69AdapterForView(userSession, commonKey);				
				caja69AdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.CAJA69_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA69_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCaja69AdapterForCreate(userSession)";
				caja69AdapterVO = BalServiceLocator.getBalanceService().getCaja69AdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA69_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (caja69AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + caja69AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			caja69AdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + Caja69Adapter.NAME + ": "+ caja69AdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(Caja69Adapter.NAME, caja69AdapterVO);
			// Subo el apdater al userSession
			userSession.put(Caja69Adapter.NAME, caja69AdapterVO);
			 
			saveDemodaMessages(request, caja69AdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja69Adapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			Caja69Adapter caja69AdapterVO = (Caja69Adapter) userSession.get(Caja69Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (caja69AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + Caja69Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, Caja69Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(caja69AdapterVO, request);
			
            // Tiene errores recuperables
			if (caja69AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja69AdapterVO.infoString()); 
				saveDemodaErrors(request, caja69AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// llamada al servicio
			Caja69VO caja69VO = BalServiceLocator.getBalanceService().createCaja69(userSession, caja69AdapterVO.getCaja69());
			
            // Tiene errores recuperables
			if (caja69VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja69VO.infoString()); 
				saveDemodaErrors(request, caja69VO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (caja69VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja69VO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, Caja69Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja69Adapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			Caja69Adapter caja69AdapterVO = (Caja69Adapter) userSession.get(Caja69Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (caja69AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + Caja69Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, Caja69Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(caja69AdapterVO, request);
			
            // Tiene errores recuperables
			if (caja69AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja69AdapterVO.infoString()); 
				saveDemodaErrors(request, caja69AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// llamada al servicio
			Caja69VO caja69VO = BalServiceLocator.getBalanceService().updateCaja69(userSession, caja69AdapterVO.getCaja69());
			
            // Tiene errores recuperables
			if (caja69VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja69AdapterVO.infoString()); 
				saveDemodaErrors(request, caja69VO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (caja69VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja69AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, Caja69Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja69Adapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA69, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			Caja69Adapter caja69AdapterVO = (Caja69Adapter) userSession.get(Caja69Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (caja69AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + Caja69Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, Caja69Adapter.NAME); 
			}

			// llamada al servicio
			Caja69VO caja69VO = BalServiceLocator.getBalanceService().deleteCaja69
				(userSession, caja69AdapterVO.getCaja69());
			
            // Tiene errores recuperables
			if (caja69VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja69AdapterVO.infoString());
				saveDemodaErrors(request, caja69VO);				
				request.setAttribute(Caja69Adapter.NAME, caja69AdapterVO);
				return mapping.findForward(BalConstants.FWD_CAJA69_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (caja69VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja69AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja69Adapter.NAME, caja69AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, Caja69Adapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja69Adapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, Caja69Adapter.NAME);
		
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
			String name = Caja69Adapter.NAME;
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
			Caja69Adapter caja69AdapterVO = (Caja69Adapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (caja69AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  caja69AdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			caja69AdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			caja69AdapterVO = BalServiceLocator.getBalanceService().imprimirCaja69(userSession, caja69AdapterVO);

			// limpia la lista de reports y la lista de tablas
			caja69AdapterVO.getReport().getListReport().clear();
			caja69AdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (caja69AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja69AdapterVO.infoString());
				saveDemodaErrors(request, caja69AdapterVO);				
				request.setAttribute(name, caja69AdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (caja69AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja69AdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  caja69AdapterVO.NAME,  caja69AdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, caja69AdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = caja69AdapterVO.getReport().getReportFileName();

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
