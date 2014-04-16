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

import ar.gov.rosario.siat.bal.iface.model.Caja7Adapter;
import ar.gov.rosario.siat.bal.iface.model.Caja7VO;
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

public final class AdministrarCaja7DAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCaja7DAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA7, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		Caja7Adapter caja7AdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCaja7AdapterForView(userSession, commonKey)";
				caja7AdapterVO = BalServiceLocator.getBalanceService().getCaja7AdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA7_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCaja7AdapterForUpdate(userSession, commonKey)";
				caja7AdapterVO = BalServiceLocator.getBalanceService().getCaja7AdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA7_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCaja7AdapterForView(userSession, commonKey)";
				caja7AdapterVO = BalServiceLocator.getBalanceService().getCaja7AdapterForView(userSession, commonKey);				
				caja7AdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.CAJA7_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA7_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCaja7AdapterForCreate(userSession)";
				caja7AdapterVO = BalServiceLocator.getBalanceService().getCaja7AdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CAJA7_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (caja7AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + caja7AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			caja7AdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + Caja7Adapter.NAME + ": "+ caja7AdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(Caja7Adapter.NAME, caja7AdapterVO);
			// Subo el apdater al userSession
			userSession.put(Caja7Adapter.NAME, caja7AdapterVO);
			 
			saveDemodaMessages(request, caja7AdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja7Adapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			Caja7Adapter caja7AdapterVO = (Caja7Adapter) userSession.get(Caja7Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (caja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + Caja7Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, Caja7Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(caja7AdapterVO, request);
			
            // Tiene errores recuperables
			if (caja7AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja7AdapterVO.infoString()); 
				saveDemodaErrors(request, caja7AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// llamada al servicio
			Caja7VO caja7VO = BalServiceLocator.getBalanceService().createCaja7(userSession, caja7AdapterVO.getCaja7());
			
            // Tiene errores recuperables
			if (caja7VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja7VO.infoString()); 
				saveDemodaErrors(request, caja7VO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (caja7VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja7VO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, Caja7Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja7Adapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			Caja7Adapter caja7AdapterVO = (Caja7Adapter) userSession.get(Caja7Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (caja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + Caja7Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, Caja7Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(caja7AdapterVO, request);
			
            // Tiene errores recuperables
			if (caja7AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja7AdapterVO.infoString()); 
				saveDemodaErrors(request, caja7AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// llamada al servicio
			Caja7VO caja7VO = BalServiceLocator.getBalanceService().updateCaja7(userSession, caja7AdapterVO.getCaja7());
			
            // Tiene errores recuperables
			if (caja7VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja7AdapterVO.infoString()); 
				saveDemodaErrors(request, caja7VO);
				return forwardErrorRecoverable(mapping, request, userSession, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (caja7VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja7AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, Caja7Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja7Adapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CAJA7, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			Caja7Adapter caja7AdapterVO = (Caja7Adapter) userSession.get(Caja7Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (caja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + Caja7Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, Caja7Adapter.NAME); 
			}

			// llamada al servicio
			Caja7VO caja7VO = BalServiceLocator.getBalanceService().deleteCaja7
				(userSession, caja7AdapterVO.getCaja7());
			
            // Tiene errores recuperables
			if (caja7VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja7AdapterVO.infoString());
				saveDemodaErrors(request, caja7VO);				
				request.setAttribute(Caja7Adapter.NAME, caja7AdapterVO);
				return mapping.findForward(BalConstants.FWD_CAJA7_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (caja7VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja7AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, Caja7Adapter.NAME, caja7AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, Caja7Adapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, Caja7Adapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, Caja7Adapter.NAME);
		
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
			String name = Caja7Adapter.NAME;
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
			Caja7Adapter caja7AdapterVO = (Caja7Adapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (caja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  caja7AdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			caja7AdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			caja7AdapterVO = BalServiceLocator.getBalanceService().imprimirCaja7(userSession, caja7AdapterVO);

			// limpia la lista de reports y la lista de tablas
			caja7AdapterVO.getReport().getListReport().clear();
			caja7AdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (caja7AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + caja7AdapterVO.infoString());
				saveDemodaErrors(request, caja7AdapterVO);				
				request.setAttribute(name, caja7AdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (caja7AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + caja7AdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  caja7AdapterVO.NAME,  caja7AdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, caja7AdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = caja7AdapterVO.getReport().getReportFileName();

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
