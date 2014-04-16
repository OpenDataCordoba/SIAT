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
import ar.gov.rosario.siat.gde.iface.model.EventoAdapter;
import ar.gov.rosario.siat.gde.iface.model.EventoVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEventoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEventoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EventoAdapter eventoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEventoAdapterForView(userSession, commonKey)";
				eventoAdapterVO = GdeServiceLocator.getDefinicionService().getEventoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEventoAdapterForUpdate(userSession, commonKey)";
				eventoAdapterVO = GdeServiceLocator.getDefinicionService().getEventoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_EVENTO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEventoAdapterForView(userSession, commonKey)";
				eventoAdapterVO = GdeServiceLocator.getDefinicionService().getEventoAdapterForView(userSession, commonKey);				
				eventoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.EVENTO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEventoAdapterForCreate(userSession)";
				eventoAdapterVO = GdeServiceLocator.getDefinicionService().getEventoAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_EVENTO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getEventoAdapterForView(userSession)";
				eventoAdapterVO = GdeServiceLocator.getDefinicionService().getEventoAdapterForView(userSession, commonKey);
				eventoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.EVENTO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getEventoAdapterForView(userSession)";
				eventoAdapterVO = GdeServiceLocator.getDefinicionService().getEventoAdapterForView(userSession, commonKey);
				eventoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.EVENTO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (eventoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + eventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			eventoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EventoAdapter.NAME + ": "+ eventoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EventoAdapter.NAME, eventoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EventoAdapter.NAME, eventoAdapterVO);
			 
			saveDemodaMessages(request, eventoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EventoAdapter eventoAdapterVO = (EventoAdapter) userSession.get(EventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (eventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EventoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(eventoAdapterVO, request);
			
            // Tiene errores recuperables
			if (eventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString()); 
				saveDemodaErrors(request, eventoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			//Seteamos los Predecesores
			String[] listIdSelected = eventoAdapterVO.getListIdSelected(); 
			String predecesores = "";
			
			if (listIdSelected != null && listIdSelected.length>0) {
				for (int i = 0; i < listIdSelected.length; i++) {
					predecesores += listIdSelected[i] + ','; 
				}
			}
			
			eventoAdapterVO.getEvento().setPredecesores(predecesores);
			
			// llamada al servicio
			EventoVO eventoVO = GdeServiceLocator.getDefinicionService().createEvento(userSession, eventoAdapterVO.getEvento());
			
            // Tiene errores recuperables
			if (eventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoVO.infoString()); 
				saveDemodaErrors(request, eventoVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (eventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EventoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EventoAdapter eventoAdapterVO = (EventoAdapter) userSession.get(EventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (eventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EventoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(eventoAdapterVO, request);
			
            // Tiene errores recuperables
			if (eventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString()); 
				saveDemodaErrors(request, eventoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			//Seteamos los Predecesores
			String[] listIdSelected = eventoAdapterVO.getListIdSelected(); 
			String predecesores = "";
			
			if (listIdSelected != null && listIdSelected.length>0) {
				for (int i = 0; i < listIdSelected.length; i++) {
					predecesores += listIdSelected[i] + ','; 
				}
			}
			
			eventoAdapterVO.getEvento().setPredecesores(predecesores);
			
			// llamada al servicio
			EventoVO eventoVO = GdeServiceLocator.getDefinicionService().updateEvento(userSession, eventoAdapterVO.getEvento());
			
            // Tiene errores recuperables
			if (eventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString()); 
				saveDemodaErrors(request, eventoVO);
				return forwardErrorRecoverable(mapping, request, userSession, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (eventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EventoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EventoAdapter eventoAdapterVO = (EventoAdapter) userSession.get(EventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (eventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EventoAdapter.NAME); 
			}

			// llamada al servicio
			EventoVO eventoVO = GdeServiceLocator.getDefinicionService().deleteEvento
				(userSession, eventoAdapterVO.getEvento());
			
            // Tiene errores recuperables
			if (eventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString());
				saveDemodaErrors(request, eventoVO);				
				request.setAttribute(EventoAdapter.NAME, eventoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (eventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EventoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EventoAdapter eventoAdapterVO = (EventoAdapter) userSession.get(EventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (eventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EventoAdapter.NAME); 
			}

			// llamada al servicio
			EventoVO eventoVO = GdeServiceLocator.getDefinicionService().activarEvento
				(userSession, eventoAdapterVO.getEvento());
			
            // Tiene errores recuperables
			if (eventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString());
				saveDemodaErrors(request, eventoVO);				
				request.setAttribute(EventoAdapter.NAME, eventoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (eventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EventoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_EVENTO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EventoAdapter eventoAdapterVO = (EventoAdapter) userSession.get(EventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (eventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EventoAdapter.NAME); 
			}

			// llamada al servicio
			EventoVO eventoVO = GdeServiceLocator.getDefinicionService().desactivarEvento
				(userSession, eventoAdapterVO.getEvento());
			
            // Tiene errores recuperables
			if (eventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString());
				saveDemodaErrors(request, eventoVO);				
				request.setAttribute(EventoAdapter.NAME, eventoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_EVENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (eventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EventoAdapter.NAME, eventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EventoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EventoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EventoAdapter.NAME);
		
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
			String name = EventoAdapter.NAME;
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
			EventoAdapter eventoAdapterVO = (EventoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (eventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			eventoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			eventoAdapterVO = GdeServiceLocator.getDefinicionService().imprimirEvento(userSession, eventoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			eventoAdapterVO.getReport().getListReport().clear();
			eventoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (eventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + eventoAdapterVO.infoString());
				saveDemodaErrors(request, eventoAdapterVO);				
				request.setAttribute(name, eventoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (eventoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + eventoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, eventoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = eventoAdapterVO.getReport().getReportFileName();

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
