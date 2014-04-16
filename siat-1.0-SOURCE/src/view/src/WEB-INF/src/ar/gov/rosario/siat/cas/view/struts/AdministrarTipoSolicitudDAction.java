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
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudAdapter;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasError;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipoSolicitudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoSolicitudDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoSolicitudAdapter tipoSolicitudAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoSolicitudAdapterForView(userSession, commonKey)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoSolicitudAdapterForUpdate(userSession, commonKey)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoSolicitudAdapterForView(userSession, commonKey)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForView(userSession, commonKey);				
				tipoSolicitudAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CasError.TIPOSOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoSolicitudAdapterForCreate(userSession)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForCreate(userSession);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoSolicitudAdapterForView(userSession)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForView(userSession, commonKey);
				tipoSolicitudAdapterVO.addMessage(BaseError.MSG_ACTIVAR, CasError.TIPOSOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoSolicitudAdapterForView(userSession)";
				tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterForView(userSession, commonKey);
				tipoSolicitudAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, CasError.TIPOSOLICITUD_LABEL);
				actionForward = mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoSolicitudAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoSolicitudAdapter.NAME + ": "+ tipoSolicitudAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			 
			saveDemodaMessages(request, tipoSolicitudAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// llamada al servicio
			TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().createTipoSolicitud(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
			
            // Tiene errores recuperables
			if (tipoSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// llamada al servicio
			TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().updateTipoSolicitud(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
			
            // Tiene errores recuperables
			if (tipoSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// llamada al servicio
			TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().deleteTipoSolicitud
				(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
			
            // Tiene errores recuperables
			if (tipoSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, tipoSolicitudVO);				
				request.setAttribute(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// llamada al servicio
			TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().activarTipoSolicitud
				(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
			
            // Tiene errores recuperables
			if (tipoSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, tipoSolicitudVO);				
				request.setAttribute(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// llamada al servicio
			TipoSolicitudVO tipoSolicitudVO = CasServiceLocator.getSolicitudService().desactivarTipoSolicitud
				(userSession, tipoSolicitudAdapterVO.getTipoSolicitud());
			
            // Tiene errores recuperables
			if (tipoSolicitudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, tipoSolicitudVO);				
				request.setAttribute(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
				return mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoSolicitudAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoSolicitudAdapter.NAME);
		
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
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(TipoSolicitudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoSolicitudAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// llamada al servicio
			tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().getTipoSolicitudAdapterParam(userSession, tipoSolicitudAdapterVO);
			
            // Tiene errores recuperables
			if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoSolicitudAdapter.NAME, tipoSolicitudAdapterVO);
			
			return mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudAdapter.NAME);
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
			String name = TipoSolicitudAdapter.NAME;
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
			TipoSolicitudAdapter tipoSolicitudAdapterVO = (TipoSolicitudAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipoSolicitudAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoSolicitudAdapterVO = CasServiceLocator.getSolicitudService().imprimirTipoSolicitud(userSession, tipoSolicitudAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoSolicitudAdapterVO.getReport().getListReport().clear();
			tipoSolicitudAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoSolicitudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudAdapterVO.infoString());
				saveDemodaErrors(request, tipoSolicitudAdapterVO);				
				request.setAttribute(name, tipoSolicitudAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoSolicitudAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoSolicitudAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_ENC_TIPOSOLICITUD, BaseConstants.ACT_MODIFICAR);

		}
	
	// Metodos relacionados Area Solicitud
	public ActionForward verAreaSolicitud(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_AREASOLICITUD);

	}

	public ActionForward modificarAreaSolicitud(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_AREASOLICITUD);

	}

	public ActionForward eliminarAreaSolicitud(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_AREASOLICITUD);

	}
	
	public ActionForward agregarAreaSolicitud(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_AREASOLICITUD);
		
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, TipoSolicitudAdapter.NAME);
			
		}
	
}
