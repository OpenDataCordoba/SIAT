//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProcuradorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProcuradorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCURADOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcuradorAdapter procuradorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProcuradorAdapterForView(userSession, commonKey)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProcuradorAdapterForUpdate(userSession, commonKey)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProcuradorAdapterForView(userSession, commonKey)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForView(userSession, commonKey);				
				procuradorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PROCURADOR_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);				
			}

			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getProcuradorAdapterForView(userSession,commonKey)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForView(userSession, commonKey);
				procuradorAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.PROCURADOR_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getProcuradorAdapterForView(userSession)";
				procuradorAdapterVO = GdeServiceLocator.getDefinicionService().getProcuradorAdapterForView(userSession, commonKey);
				procuradorAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.PROCURADOR_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procuradorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procuradorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.NAME, procuradorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procuradorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcuradorAdapter.NAME + ": "+ procuradorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcuradorAdapter.NAME, procuradorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcuradorAdapter.NAME, procuradorAdapterVO);
			 
			saveDemodaMessages(request, procuradorAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_ENC_PROCURADOR, BaseConstants.ACT_MODIFICAR);

		}
	
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCURADOR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcuradorAdapter procuradorAdapterVO = (ProcuradorAdapter) userSession.get(ProcuradorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procuradorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcuradorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcuradorAdapter.NAME); 
			}

			// llamada al servicio
			ProcuradorVO procuradorVO = GdeServiceLocator.getDefinicionService().deleteProcurador
				(userSession, procuradorAdapterVO.getProcurador());
			
            // Tiene errores recuperables
			if (procuradorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString());
				saveDemodaErrors(request, procuradorVO);				
				request.setAttribute(ProcuradorAdapter.NAME, procuradorAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (procuradorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.NAME, procuradorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcuradorAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCURADOR, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcuradorAdapter procuradorAdapterVO = (ProcuradorAdapter) userSession.get(ProcuradorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procuradorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcuradorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcuradorAdapter.NAME); 
			}

			// llamada al servicio
			ProcuradorVO procuradorVO = GdeServiceLocator.getDefinicionService().activarProcurador
				(userSession, procuradorAdapterVO.getProcurador());
			
            // Tiene errores recuperables
			if (procuradorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString());
				saveDemodaErrors(request, procuradorVO);				
				request.setAttribute(ProcuradorAdapter.NAME, procuradorAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (procuradorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.NAME, procuradorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcuradorAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCURADOR, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcuradorAdapter procuradorAdapterVO = (ProcuradorAdapter) userSession.get(ProcuradorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (procuradorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcuradorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcuradorAdapter.NAME); 
			}

			// llamada al servicio
			ProcuradorVO procuradorVO = GdeServiceLocator.getDefinicionService().desactivarProcurador
				(userSession, procuradorAdapterVO.getProcurador());
			
            // Tiene errores recuperables
			if (procuradorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString());
				saveDemodaErrors(request, procuradorVO);				
				request.setAttribute(ProcuradorAdapter.NAME, procuradorAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PROCURADOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (procuradorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcuradorAdapter.NAME, procuradorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcuradorAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcuradorAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcuradorAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ProcuradorAdapter.NAME);
			
		}
	
	// Metodos relacionados ProRec
	public ActionForward verProRec(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROREC);

	}

	public ActionForward modificarProRec(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROREC);

	}

	public ActionForward eliminarProRec(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PROREC);

	}
	
	public ActionForward agregarProRec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_PROREC);
		
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
			String name = ProcuradorAdapter.NAME;
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
			ProcuradorAdapter procuradorAdapterVO = (ProcuradorAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (procuradorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			procuradorAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			procuradorAdapterVO = GdeServiceLocator.getDefinicionService().imprimirProcurador(userSession, procuradorAdapterVO);

			// limpia la lista de reports y la lista de tablas
			procuradorAdapterVO.getReport().getListReport().clear();
			procuradorAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (procuradorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procuradorAdapterVO.infoString());
				saveDemodaErrors(request, procuradorAdapterVO);				
				request.setAttribute(name, procuradorAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (procuradorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procuradorAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, procuradorAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = procuradorAdapterVO.getReport().getReportFileName();
			
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
