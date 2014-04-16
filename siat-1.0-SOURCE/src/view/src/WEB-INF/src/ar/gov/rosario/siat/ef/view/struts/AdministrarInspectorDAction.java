//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.InspectorAdapter;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarInspectorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarInspectorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSPECTOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		InspectorAdapter inspectorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getInspectorAdapterForView(userSession, commonKey)";
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INSPECTOR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getInspectorAdapterForUpdate(userSession, commonKey)";
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INSPECTOR_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getInspectorAdapterForView(userSession, commonKey)";
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterForView(userSession, commonKey);				
				inspectorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.INSPECTOR_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_INSPECTOR_VIEW_ADAPTER);				
			}
		

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (inspectorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + inspectorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InspectorAdapter.NAME, inspectorAdapterVO);
			}
			// Seteo los valores de navegacion en el adapter
			inspectorAdapterVO.setValuesFromNavModel(navModel);
			
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				// Seteo los Valores Para poder Volver al Search Page (WARNING PICHANGA!!!!)
				inspectorAdapterVO.setPrevAction("/ef/BuscarInspector");
				inspectorAdapterVO.setPrevActionParameter("buscar");
			}
			// Seteo los valores de navegacion en el adapter
			
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + InspectorAdapter.NAME + ": "+ inspectorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(InspectorAdapter.NAME, inspectorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(InspectorAdapter.NAME, inspectorAdapterVO);
			 
			saveDemodaMessages(request, inspectorAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InspectorAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				EfConstants.ACTION_ADMINISTRAR_ENC_INSPECTOR, BaseConstants.ACT_MODIFICAR);

		}
	
	

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSPECTOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InspectorAdapter inspectorAdapterVO = (InspectorAdapter) userSession.get(InspectorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (inspectorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InspectorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InspectorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(inspectorAdapterVO, request);
			
            // Tiene errores recuperables
			if (inspectorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
				saveDemodaErrors(request, inspectorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InspectorAdapter.NAME, inspectorAdapterVO);
			}
			
			// llamada al servicio
			InspectorVO inspectorVO = EfServiceLocator.getDefinicionService().updateInspector(userSession, inspectorAdapterVO.getInspector());
			
            // Tiene errores recuperables
			if (inspectorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
				saveDemodaErrors(request, inspectorVO);
				return forwardErrorRecoverable(mapping, request, userSession, InspectorAdapter.NAME, inspectorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (inspectorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inspectorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InspectorAdapter.NAME, inspectorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InspectorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InspectorAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSPECTOR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InspectorAdapter inspectorAdapterVO = (InspectorAdapter) userSession.get(InspectorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (inspectorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InspectorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InspectorAdapter.NAME); 
			}

			// llamada al servicio
			InspectorVO inspectorVO = EfServiceLocator.getDefinicionService().deleteInspector
				(userSession, inspectorAdapterVO.getInspector());
			
            // Tiene errores recuperables
			if (inspectorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString());
				saveDemodaErrors(request, inspectorVO);				
				request.setAttribute(InspectorAdapter.NAME, inspectorAdapterVO);
				return mapping.findForward(EfConstants.FWD_INSPECTOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (inspectorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inspectorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InspectorAdapter.NAME, inspectorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InspectorAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InspectorAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, InspectorAdapter.NAME);
		
	}

	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, false);		
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				InspectorAdapter inspectorAdapterVO =  (InspectorAdapter) userSession.get(InspectorAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (inspectorAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + InspectorAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, InspectorAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(InspectorAdapter.NAME, inspectorAdapterVO);
					return mapping.findForward(EfConstants.FWD_INSPECTOR_EDIT_ADAPTER); 
				}
				
				// Se carga el idPersona seleccionado, en el adapter
				inspectorAdapterVO.getInspector().setIdPersona(new Long(selectedId));
				
				// llamo al param del servicio
				inspectorAdapterVO = EfServiceLocator.getDefinicionService().getInspectorAdapterParamPersona(userSession, inspectorAdapterVO);

	            // Tiene errores recuperables
				if (inspectorAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString()); 
					saveDemodaErrors(request, inspectorAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							InspectorAdapter.NAME, inspectorAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (inspectorAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + inspectorAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							InspectorAdapter.NAME, inspectorAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, inspectorAdapterVO);

				// Envio el VO al request
				request.setAttribute(InspectorAdapter.NAME, inspectorAdapterVO);
				// Subo el apdater al userSession
				userSession.put(InspectorAdapter.NAME, inspectorAdapterVO);

				return mapping.findForward(EfConstants.FWD_INSPECTOR_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, InspectorAdapter.NAME);
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
			String name = InspectorAdapter.NAME;
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
			InspectorAdapter inspectorAdapterVO = (InspectorAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (inspectorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			inspectorAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			inspectorAdapterVO = EfServiceLocator.getDefinicionService().imprimirInspector(userSession, inspectorAdapterVO);

			// limpia la lista de reports y la lista de tablas
			inspectorAdapterVO.getReport().getListReport().clear();
			inspectorAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (inspectorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + inspectorAdapterVO.infoString());
				saveDemodaErrors(request, inspectorAdapterVO);				
				request.setAttribute(name, inspectorAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (inspectorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + inspectorAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, inspectorAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = inspectorAdapterVO.getReport().getReportFileName();

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
			return baseRefill(mapping, form, request, response, funcName, InspectorAdapter.NAME);
			
		}
	
	// Metodos relacionados verInsSup
	public ActionForward verInsSup(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INSSUP);

	}

	public ActionForward modificarInsSup(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INSSUP);

	}

	public ActionForward eliminarInsSup(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INSSUP);

	}
	
	public ActionForward agregarInsSup(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_INSSUP);
		
	}

		
}
