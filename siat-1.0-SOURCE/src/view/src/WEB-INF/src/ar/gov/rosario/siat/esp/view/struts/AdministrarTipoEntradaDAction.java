//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

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
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaAdapter;
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipoEntradaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoEntradaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_TIPOENTRADA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoEntradaAdapter tipoEntradaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoEntradaAdapterForView(userSession, commonKey)";
				tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().getTipoEntradaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EspConstants.FWD_TIPOENTRADA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoEntradaAdapterForUpdate(userSession, commonKey)";
				tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().getTipoEntradaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EspConstants.FWD_TIPOENTRADA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoEntradaAdapterForView(userSession, commonKey)";
				tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().getTipoEntradaAdapterForView(userSession, commonKey);				
				tipoEntradaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.TIPOENTRADA_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_TIPOENTRADA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoEntradaAdapterForCreate(userSession)";
				tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().getTipoEntradaAdapterForCreate(userSession);
				actionForward = mapping.findForward(EspConstants.FWD_TIPOENTRADA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoEntradaAdapterForView(userSession)";
				tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().getTipoEntradaAdapterForView(userSession, commonKey);
				tipoEntradaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EspError.TIPOENTRADA_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_TIPOENTRADA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoEntradaAdapterForView(userSession)";
				tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().getTipoEntradaAdapterForView(userSession, commonKey);
				tipoEntradaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EspError.TIPOENTRADA_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_TIPOENTRADA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoEntradaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoEntradaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoEntradaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoEntradaAdapter.NAME + ": "+ tipoEntradaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			 
			saveDemodaMessages(request, tipoEntradaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoEntradaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_TIPOENTRADA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoEntradaAdapter tipoEntradaAdapterVO = (TipoEntradaAdapter) userSession.get(TipoEntradaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoEntradaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoEntradaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoEntradaAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoEntradaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// llamada al servicio
			TipoEntradaVO tipoEntradaVO = EspServiceLocator.getHabilitacionService().createTipoEntrada(userSession, tipoEntradaAdapterVO.getTipoEntrada());
			
            // Tiene errores recuperables
			if (tipoEntradaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoEntradaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoEntradaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoEntradaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoEntradaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_TIPOENTRADA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoEntradaAdapter tipoEntradaAdapterVO = (TipoEntradaAdapter) userSession.get(TipoEntradaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoEntradaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoEntradaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoEntradaAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoEntradaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// llamada al servicio
			TipoEntradaVO tipoEntradaVO = EspServiceLocator.getHabilitacionService().updateTipoEntrada(userSession, tipoEntradaAdapterVO.getTipoEntrada());
			
            // Tiene errores recuperables
			if (tipoEntradaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoEntradaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoEntradaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoEntradaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoEntradaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_TIPOENTRADA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoEntradaAdapter tipoEntradaAdapterVO = (TipoEntradaAdapter) userSession.get(TipoEntradaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoEntradaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoEntradaAdapter.NAME); 
			}

			// llamada al servicio
			TipoEntradaVO tipoEntradaVO = EspServiceLocator.getHabilitacionService().deleteTipoEntrada
				(userSession, tipoEntradaAdapterVO.getTipoEntrada());
			
            // Tiene errores recuperables
			if (tipoEntradaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaAdapterVO.infoString());
				saveDemodaErrors(request, tipoEntradaVO);				
				request.setAttribute(TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
				return mapping.findForward(EspConstants.FWD_TIPOENTRADA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoEntradaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoEntradaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoEntradaAdapter.NAME, tipoEntradaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoEntradaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoEntradaAdapter.NAME);
		}
	}
	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoEntradaAdapter.NAME);
		
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
			String name = TipoEntradaAdapter.NAME;
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
			TipoEntradaAdapter tipoEntradaAdapterVO = (TipoEntradaAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoEntradaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, TipoEntradaAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipoEntradaAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoEntradaAdapterVO = EspServiceLocator.getHabilitacionService().imprimirTipoEntrada(userSession, tipoEntradaAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoEntradaAdapterVO.getReport().getListReport().clear();
			tipoEntradaAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoEntradaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaAdapterVO.infoString());
				saveDemodaErrors(request, tipoEntradaAdapterVO);				
				request.setAttribute(name, tipoEntradaAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoEntradaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoEntradaAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoEntradaAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoEntradaAdapterVO.getReport().getReportFileName();

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
