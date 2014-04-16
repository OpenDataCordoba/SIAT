//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlanillaCuadraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanillaCuadraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanillaCuadraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_PLANILLACUADRA, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanillaCuadraAdapter planillaCuadraAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanillaCuadraAdapterForUpdate(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanillaCuadraAdapterForDelete(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView
					(userSession, commonKey);
				planillaCuadraAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.PLANILLACUADRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView
					(userSession, commonKey);
				planillaCuadraAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.PLANILLACUADRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView
					(userSession, commonKey);
				planillaCuadraAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, RecError.PLANILLACUADRA_LABEL);			
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);				
			}
			if (act.equals(RecConstants.ACT_CAMBIAR_ESTADO)) {
				stringServicio = "getPlanillaCuadraAdapterForCambiarEstado(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForCambiarEstado
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);				
			}
			
			if (act.equals(RecConstants.ACT_INFORMAR_CATASTRALES)) {
				stringServicio = "getPlanillaCuadraAdapterForView(userSession, commonKey)";
				planillaCuadraAdapterVO = RecServiceLocator.getCdmService().getPlanillaCuadraAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLANILLACUADRA_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planillaCuadraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planillaCuadraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				PlanillaCuadraAdapter.NAME + ": " + planillaCuadraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			
			saveDemodaMessages(request, planillaCuadraAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			RecConstants.ACTION_ADMINISTRAR_ENC_PLANILLACUADRA, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_PLANILLACUADRA, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = (PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.NAME); 
			}

			// llamada al servicio
			PlanillaCuadraVO planillaCuadraVO = RecServiceLocator.getCdmService().deletePlanillaCuadra
				(userSession, planillaCuadraAdapterVO.getPlanillaCuadra());
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString());
				saveDemodaErrors(request, planillaCuadraVO);				
				request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_PLANILLACUADRA, RecSecurityConstants.MTD_PLANILLACUADRA_CAMBIAR_ESTADO);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanillaCuadraAdapter planillaCuadraAdapterVO = 
				(PlanillaCuadraAdapter) userSession.get(PlanillaCuadraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanillaCuadraAdapter.NAME 
					+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanillaCuadraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(planillaCuadraAdapterVO, request);
			
            // Tiene errores recuperables
			if (planillaCuadraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraAdapterVO);
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);
			}
			
			// llamada al servicio
			PlanillaCuadraVO planillaCuadraVO = 
				RecServiceLocator.getCdmService().cambiarEstadoPlanillaCuadra
				(userSession, planillaCuadraAdapterVO);
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraAdapterVO.infoString()); 
				saveDemodaErrors(request, planillaCuadraVO);
				request.setAttribute(PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);					
				return mapping.findForward(RecConstants.FWD_PLANILLACUADRA_VIEW_ADAPTER);					
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlanillaCuadraAdapter.NAME, planillaCuadraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanillaCuadraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanillaCuadraAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanillaCuadraAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PlanillaCuadraAdapter.NAME);
		
	}
	
	// Metodos relacionados PlaCuaDet
	public ActionForward verPlaCuaDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);

	}

	public ActionForward modificarPlaCuaDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);

	}

	public ActionForward eliminarPlaCuaDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_PLACUADET);

	}
	
	public ActionForward agregarPlaCuaDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter
			(mapping, request, funcName, RecConstants.ACTION_BUSCAR_PLACUADET);
		
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
			String name = PlanillaCuadraAdapter.NAME;
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
			PlanillaCuadraAdapter planillaCuadraVO = (PlanillaCuadraAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (planillaCuadraVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			planillaCuadraVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			planillaCuadraVO =  RecServiceLocator.getCdmService().imprimirPlanillaCuadra(userSession, planillaCuadraVO);

			// limpia la lista de reports y la lista de tablas
			planillaCuadraVO.getReport().getListReport().clear();
			planillaCuadraVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (planillaCuadraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planillaCuadraVO.infoString());
				saveDemodaErrors(request, planillaCuadraVO);				
				request.setAttribute(name, planillaCuadraVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (planillaCuadraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planillaCuadraVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, planillaCuadraVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = planillaCuadraVO.getReport().getReportFileName();

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
	
