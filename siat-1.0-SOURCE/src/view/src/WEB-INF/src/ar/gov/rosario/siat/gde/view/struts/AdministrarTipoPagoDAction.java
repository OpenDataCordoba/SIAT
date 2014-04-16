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
import ar.gov.rosario.siat.gde.iface.model.TipoPagoAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipoPagoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoPagoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoPagoAdapter tipoPagoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoPagoAdapterForView(userSession, commonKey)";
				tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().getTipoPagoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoPagoAdapterForUpdate(userSession, commonKey)";
				tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().getTipoPagoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOPAGO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoPagoAdapterForView(userSession, commonKey)";
				tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().getTipoPagoAdapterForView(userSession, commonKey);				
				tipoPagoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.TIPOPAGO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoPagoAdapterForCreate(userSession)";
				tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().getTipoPagoAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOPAGO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoPagoAdapterForView(userSession)";
				tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().getTipoPagoAdapterForView(userSession, commonKey);
				tipoPagoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.TIPOPAGO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoPagoAdapterForView(userSession)";
				tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().getTipoPagoAdapterForView(userSession, commonKey);
				tipoPagoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.TIPOPAGO_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoPagoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoPagoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoPagoAdapter.NAME + ": "+ tipoPagoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			 
			saveDemodaMessages(request, tipoPagoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoPagoAdapter tipoPagoAdapterVO = (TipoPagoAdapter) userSession.get(TipoPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// llamada al servicio
			TipoPagoVO tipoPagoVO = GdeServiceLocator.getDefinicionService().createTipoPago(userSession, tipoPagoAdapterVO.getTipoPago());
			
            // Tiene errores recuperables
			if (tipoPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoVO.infoString()); 
				saveDemodaErrors(request, tipoPagoVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoPagoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoPagoAdapter tipoPagoAdapterVO = (TipoPagoAdapter) userSession.get(TipoPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoPagoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoPagoAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoPagoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// llamada al servicio
			TipoPagoVO tipoPagoVO = GdeServiceLocator.getDefinicionService().updateTipoPago(userSession, tipoPagoAdapterVO.getTipoPago());
			
            // Tiene errores recuperables
			if (tipoPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoPagoVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoPagoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoPagoAdapter tipoPagoAdapterVO = (TipoPagoAdapter) userSession.get(TipoPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoPagoAdapter.NAME); 
			}

			// llamada al servicio
			TipoPagoVO tipoPagoVO = GdeServiceLocator.getDefinicionService().deleteTipoPago
				(userSession, tipoPagoAdapterVO.getTipoPago());
			
            // Tiene errores recuperables
			if (tipoPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString());
				saveDemodaErrors(request, tipoPagoVO);				
				request.setAttribute(TipoPagoAdapter.NAME, tipoPagoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoPagoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoPagoAdapter tipoPagoAdapterVO = (TipoPagoAdapter) userSession.get(TipoPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoPagoAdapter.NAME); 
			}

			// llamada al servicio
			TipoPagoVO tipoPagoVO = GdeServiceLocator.getDefinicionService().activarTipoPago
				(userSession, tipoPagoAdapterVO.getTipoPago());
			
            // Tiene errores recuperables
			if (tipoPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString());
				saveDemodaErrors(request, tipoPagoVO);				
				request.setAttribute(TipoPagoAdapter.NAME, tipoPagoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoPagoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoPagoAdapter tipoPagoAdapterVO = (TipoPagoAdapter) userSession.get(TipoPagoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoPagoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoPagoAdapter.NAME); 
			}

			// llamada al servicio
			TipoPagoVO tipoPagoVO = GdeServiceLocator.getDefinicionService().desactivarTipoPago
				(userSession, tipoPagoAdapterVO.getTipoPago());
			
            // Tiene errores recuperables
			if (tipoPagoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString());
				saveDemodaErrors(request, tipoPagoVO);				
				request.setAttribute(TipoPagoAdapter.NAME, tipoPagoAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TIPOPAGO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoAdapter.NAME, tipoPagoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoPagoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoPagoAdapter.NAME);
		
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
			String name = TipoPagoAdapter.NAME;
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
			TipoPagoAdapter tipoPagoAdapterVO = (TipoPagoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoPagoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipoPagoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoPagoAdapterVO = GdeServiceLocator.getDefinicionService().imprimirTipoPago(userSession, tipoPagoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoPagoAdapterVO.getReport().getListReport().clear();
			tipoPagoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoPagoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoAdapterVO.infoString());
				saveDemodaErrors(request, tipoPagoAdapterVO);				
				request.setAttribute(name, tipoPagoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoPagoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoPagoAdapterVO.getReport().getReportFileName();

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
