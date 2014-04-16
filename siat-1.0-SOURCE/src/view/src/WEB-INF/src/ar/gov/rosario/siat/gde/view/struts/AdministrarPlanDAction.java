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
import ar.gov.rosario.siat.gde.iface.model.PlanAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlanDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLAN, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlanAdapter planAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlanAdapterForView(userSession, commonKey)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlanAdapterForView(userSession, commonKey)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForView
					(userSession, commonKey);
				
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlanAdapterForDelete(userSession, commonKey)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForView
					(userSession, commonKey);
				planAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PLAN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPlanAdapterForView(userSession)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForView
					(userSession, commonKey);
				planAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.PLAN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getPlanAdapterForView(userSession)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForView
					(userSession, commonKey);
				planAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.PLAN_LABEL);			
				actionForward = mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);				
			}

			if (act.equals(GdeConstants.MTD_ADM_PLANPRORROGA)) {
				stringServicio = "getPlanProrrogaAdapterForView(userSession)";
				planAdapterVO = GdeServiceLocator.getDefinicionService().getPlanAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLANPRORROGA_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (planAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.NAME, planAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			planAdapterVO.setValuesFromNavModel(navModel);
			
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				// Seteo los Valores Para poder Volver al Search Page (WARNING PICHANGA!!!!)
				planAdapterVO.setPrevAction("/gde/BuscarPlan");
				planAdapterVO.setPrevActionParameter("buscar");
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				PlanAdapter.NAME + ": " + planAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlanAdapter.NAME, planAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlanAdapter.NAME, planAdapterVO);
			
			saveDemodaMessages(request, planAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			GdeConstants.ACTION_ADMINISTRAR_ENC_PLAN, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLAN, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.NAME); 
			}

			// llamada al servicio
			PlanVO planVO = GdeServiceLocator.getDefinicionService().deletePlan
				(userSession, planAdapterVO.getPlan());
			
            // Tiene errores recuperables
			if (planVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString());
				saveDemodaErrors(request, planVO);				
				request.setAttribute(PlanAdapter.NAME, planAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.NAME, planAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.NAME);
		}
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
			String name = PlanAdapter.NAME;
			
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
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			planAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			planAdapterVO = RecServiceLocator.getCdmService().imprimirPlan(userSession, planAdapterVO);

			// limpia la lista de reports y la lista de tablas
			planAdapterVO.getReport().getListReport().clear();
			planAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (planAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString());
				saveDemodaErrors(request, planAdapterVO);				
				request.setAttribute(name, planAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (planAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, planAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = planAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}


	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLAN, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.NAME); 
			}

			// llamada al servicio
			PlanVO planVO = GdeServiceLocator.getDefinicionService().activarPlan
				(userSession, planAdapterVO.getPlan());
			
            // Tiene errores recuperables
			if (planVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString());
				saveDemodaErrors(request, planVO);				
				request.setAttribute(PlanAdapter.NAME, planAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.NAME, planAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PLAN, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlanAdapter planAdapterVO = (PlanAdapter) userSession.get(PlanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (planAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlanAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlanAdapter.NAME); 
			}
			
			DemodaUtil.populateVO(planAdapterVO, request);
				
			// llamada al servicio
			PlanVO planVO = GdeServiceLocator.getDefinicionService().desactivarPlan
				(userSession, planAdapterVO.getPlan());
			
            // Tiene errores recuperables
			if (planVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + planAdapterVO.infoString());
				saveDemodaErrors(request, planVO);				
				request.setAttribute(PlanAdapter.NAME, planAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PLAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (planVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + planAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlanAdapter.NAME, planAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlanAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlanAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlanAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, PlanAdapter.NAME);
		
	}
	
	// Metodos relacionados PlanClaDeu
	public ActionForward verPlanClaDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANCLADEU);

	}

	public ActionForward modificarPlanClaDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANCLADEU);

	}

	public ActionForward eliminarPlanClaDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANCLADEU);

	}
	
	public ActionForward agregarPlanClaDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANCLADEU);
		
	}

	
	// Metodos relacionados PlanMotCad
	public ActionForward verPlanMotCad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANMOTCAD);

	}

	public ActionForward modificarPlanMotCad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANMOTCAD);

	}

	public ActionForward eliminarPlanMotCad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANMOTCAD);

	}
	
	public ActionForward agregarPlanMotCad(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANMOTCAD);
		
	}

	
	// Metodos relacionados PlanForActDeu
	public ActionForward verPlanForActDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANFORACTDEU);

	}

	public ActionForward modificarPlanForActDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANFORACTDEU);

	}

	public ActionForward eliminarPlanForActDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANFORACTDEU);

	}
	
	public ActionForward agregarPlanForActDeu(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANFORACTDEU);
		
	}

	// Metodos relacionados PlanDescuento
	public ActionForward verPlanDescuento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANDESCUENTO);

	}

	public ActionForward modificarPlanDescuento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANDESCUENTO);

	}

	public ActionForward eliminarPlanDescuento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANDESCUENTO);

	}
	
	public ActionForward agregarPlanDescuento(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANDESCUENTO);
		
	}

	// Metodos relacionados PlanIntFin
	public ActionForward verPlanIntFin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANINTFIN);

	}

	public ActionForward modificarPlanIntFin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANINTFIN);

	}

	public ActionForward eliminarPlanIntFin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName,GdeConstants.ACTION_ADMINISTRAR_PLANINTFIN);

	}
	
	public ActionForward agregarPlanIntFin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANINTFIN);
		
	}

	// Metodos relacionados PlanVen
	public ActionForward verPlanVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANVEN);

	}

	public ActionForward modificarPlanVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANVEN);

	}

	public ActionForward eliminarPlanVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANVEN);

	}
	
	public ActionForward agregarPlanVen(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANVEN);
		
	}
	
	// Metodos relacionados PlanExe
	public ActionForward verPlanExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANEXE);

	}

	public ActionForward modificarPlanExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANEXE);

	}

	public ActionForward eliminarPlanExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANEXE);

	}
	
	public ActionForward agregarPlanExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANEXE);
		
	}

	// Metodos relacionados PlanProrroga
	public ActionForward verPlanProrroga(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANPRORROGA);

	}

	public ActionForward modificarPlanProrroga(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANPRORROGA);

	}

	public ActionForward eliminarPlanProrroga(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANPRORROGA);

	}
	
	public ActionForward agregarPlanProrroga(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANPRORROGA);
		
	}

	// Metodos relacionados PlanAtrVal
	public ActionForward verPlanAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANATRVAL);

	}

	public ActionForward modificarPlanAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANATRVAL);

	}

	public ActionForward eliminarPlanAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANATRVAL);

	}
	
	public ActionForward agregarPlanAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANATRVAL);
		
	}

	// Metodos relacionados PlanImpMin
	public ActionForward verPlanImpMin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANIMPMIN);

	}

	public ActionForward modificarPlanImpMin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANIMPMIN);

	}

	public ActionForward eliminarPlanImpMin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANIMPMIN);

	}
	
	public ActionForward agregarPlanImpMin(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANIMPMIN);
		
	}
	
	public ActionForward verPlanRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANRECURSO);
	}
	
	public ActionForward agregarPlanRecurso(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANRECURSO);
	}
	
	public ActionForward modificarPlanRecurso (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANRECURSO);
	}
	
	public ActionForward eliminarPlanRecurso (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_PLANRECURSO);
	}

}
