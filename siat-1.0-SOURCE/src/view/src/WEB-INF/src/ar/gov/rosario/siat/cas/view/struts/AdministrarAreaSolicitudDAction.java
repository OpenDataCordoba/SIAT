//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

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
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.AreaSolicitudVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAreaSolicitudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAreaSolicitudDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_AREASOLICITUD, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AreaSolicitudAdapter areaSolicitudAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAreaSolicitudAdapterForView(userSession, commonKey)";
				areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAreaSolicitudAdapterForUpdate(userSession, commonKey)";
				areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_AREASOLICITUD_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAreaSolicitudAdapterForView(userSession, commonKey)";
				areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterForView(userSession, commonKey);				
				areaSolicitudAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CasError.AREASOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAreaSolicitudAdapterForCreate(userSession)";
				areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_AREASOLICITUD_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAreaSolicitudAdapterForView(userSession)";
				areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterForView(userSession, commonKey);
				areaSolicitudAdapterVO.addMessage(BaseError.MSG_ACTIVAR, CasError.AREASOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAreaSolicitudAdapterForView(userSession)";
				areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterForView(userSession, commonKey);
				areaSolicitudAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, CasError.AREASOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (areaSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + areaSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			areaSolicitudAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AreaSolicitudAdapter.NAME + ": "+ areaSolicitudAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			 
			saveDemodaMessages(request, areaSolicitudAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(AreaSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(areaSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (areaSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, areaSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// llamada al servicio
			AreaSolicitudVO areaSolicitudVO = CasServiceLocator.getSolicitudService().createAreaSolicitud(userSession, areaSolicitudAdapterVO.getAreaSolicitud());
			
            // Tiene errores recuperables
			if (areaSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudVO.infoString()); 
				saveDemodaErrors(request, areaSolicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaSolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(AreaSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(areaSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (areaSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, areaSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// llamada al servicio
			AreaSolicitudVO areaSolicitudVO = CasServiceLocator.getSolicitudService().updateAreaSolicitud(userSession, areaSolicitudAdapterVO.getAreaSolicitud());
			
            // Tiene errores recuperables
			if (areaSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, areaSolicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaSolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(AreaSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// llamada al servicio
			AreaSolicitudVO areaSolicitudVO = CasServiceLocator.getSolicitudService().deleteAreaSolicitud
				(userSession, areaSolicitudAdapterVO.getAreaSolicitud());
			
            // Tiene errores recuperables
			if (areaSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, areaSolicitudVO);				
				request.setAttribute(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaSolicitudAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(AreaSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// llamada al servicio
			AreaSolicitudVO areaSolicitudVO = CasServiceLocator.getSolicitudService().activarAreaSolicitud
				(userSession, areaSolicitudAdapterVO.getAreaSolicitud());
			
            // Tiene errores recuperables
			if (areaSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, areaSolicitudVO);				
				request.setAttribute(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaSolicitudAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_AREASOLICITUD, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(AreaSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// llamada al servicio
			AreaSolicitudVO areaSolicitudVO = CasServiceLocator.getSolicitudService().desactivarAreaSolicitud
				(userSession, areaSolicitudAdapterVO.getAreaSolicitud());
			
            // Tiene errores recuperables
			if (areaSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, areaSolicitudVO);				
				request.setAttribute(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_AREASOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaSolicitudAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AreaSolicitudAdapter.NAME);
		
	}
	
	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(AreaSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(areaSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (areaSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, areaSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// llamada al servicio
			areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getAreaSolicitudAdapterParam(userSession, areaSolicitudAdapterVO);
			
            // Tiene errores recuperables
			if (areaSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, areaSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AreaSolicitudAdapter.NAME, areaSolicitudAdapterVO);
			
			return mapping.findForward(CasConstants.FWD_AREASOLICITUD_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaSolicitudAdapter.NAME);
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
			String name = AreaSolicitudAdapter.NAME;
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
			AreaSolicitudAdapter areaSolicitudAdapterVO = (AreaSolicitudAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (areaSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, AreaSolicitudAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			areaSolicitudAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			areaSolicitudAdapterVO = CasServiceLocator.getSolicitudService().imprimirAreaSolicitud(userSession, areaSolicitudAdapterVO);

			// limpia la lista de reports y la lista de tablas
			areaSolicitudAdapterVO.getReport().getListReport().clear();
			areaSolicitudAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (areaSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, areaSolicitudAdapterVO);				
				request.setAttribute(name, areaSolicitudAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (areaSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaSolicitudAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, areaSolicitudAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = areaSolicitudAdapterVO.getReport().getReportFileName();

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
