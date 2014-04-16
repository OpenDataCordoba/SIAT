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

import ar.gov.rosario.siat.bal.iface.model.TipoComAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoComVO;
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

public final class AdministrarTipoComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoComDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoComAdapter tipoComAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoComAdapterForView(userSession, commonKey)";
				tipoComAdapterVO = BalServiceLocator.getCompensacionService().getTipoComAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoComAdapterForUpdate(userSession, commonKey)";
				tipoComAdapterVO = BalServiceLocator.getCompensacionService().getTipoComAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOM_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoComAdapterForView(userSession, commonKey)";
				tipoComAdapterVO = BalServiceLocator.getCompensacionService().getTipoComAdapterForView(userSession, commonKey);				
				tipoComAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.TIPOCOM_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoComAdapterForCreate(userSession)";
				tipoComAdapterVO = BalServiceLocator.getCompensacionService().getTipoComAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOM_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoComAdapterForView(userSession)";
				tipoComAdapterVO = BalServiceLocator.getCompensacionService().getTipoComAdapterForView(userSession, commonKey);
				tipoComAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.TIPOCOM_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoComAdapterForView(userSession)";
				tipoComAdapterVO = BalServiceLocator.getCompensacionService().getTipoComAdapterForView(userSession, commonKey);
				tipoComAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.TIPOCOM_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoComAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoComAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoComAdapter.NAME + ": "+ tipoComAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoComAdapter.NAME, tipoComAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoComAdapter.NAME, tipoComAdapterVO);
			 
			saveDemodaMessages(request, tipoComAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoComAdapter tipoComAdapterVO = (TipoComAdapter) userSession.get(TipoComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoComAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// llamada al servicio
			TipoComVO tipoComVO = BalServiceLocator.getCompensacionService().createTipoCom(userSession, tipoComAdapterVO.getTipoCom());
			
            // Tiene errores recuperables
			if (tipoComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComVO.infoString()); 
				saveDemodaErrors(request, tipoComVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoComAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoComAdapter tipoComAdapterVO = (TipoComAdapter) userSession.get(TipoComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoComAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoComAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoComAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// llamada al servicio
			TipoComVO tipoComVO = BalServiceLocator.getCompensacionService().updateTipoCom(userSession, tipoComAdapterVO.getTipoCom());
			
            // Tiene errores recuperables
			if (tipoComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoComVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoComAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoComAdapter tipoComAdapterVO = (TipoComAdapter) userSession.get(TipoComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoComAdapter.NAME); 
			}

			// llamada al servicio
			TipoComVO tipoComVO = BalServiceLocator.getCompensacionService().deleteTipoCom
				(userSession, tipoComAdapterVO.getTipoCom());
			
            // Tiene errores recuperables
			if (tipoComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString());
				saveDemodaErrors(request, tipoComVO);				
				request.setAttribute(TipoComAdapter.NAME, tipoComAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoComAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoComAdapter tipoComAdapterVO = (TipoComAdapter) userSession.get(TipoComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoComAdapter.NAME); 
			}

			// llamada al servicio
			TipoComVO tipoComVO = BalServiceLocator.getCompensacionService().activarTipoCom
				(userSession, tipoComAdapterVO.getTipoCom());
			
            // Tiene errores recuperables
			if (tipoComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString());
				saveDemodaErrors(request, tipoComVO);				
				request.setAttribute(TipoComAdapter.NAME, tipoComAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoComAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoComAdapter tipoComAdapterVO = (TipoComAdapter) userSession.get(TipoComAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoComAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoComAdapter.NAME); 
			}

			// llamada al servicio
			TipoComVO tipoComVO = BalServiceLocator.getCompensacionService().desactivarTipoCom
				(userSession, tipoComAdapterVO.getTipoCom());
			
            // Tiene errores recuperables
			if (tipoComVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString());
				saveDemodaErrors(request, tipoComVO);				
				request.setAttribute(TipoComAdapter.NAME, tipoComAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOCOM_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoComVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComAdapter.NAME, tipoComAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoComAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoComAdapter.NAME);
		
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
			String name = TipoComAdapter.NAME;
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
			TipoComAdapter tipoComAdapterVO = (TipoComAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoComAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipoComAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoComAdapterVO = BalServiceLocator.getCompensacionService().imprimirTipoCom(userSession, tipoComAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoComAdapterVO.getReport().getListReport().clear();
			tipoComAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoComAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComAdapterVO.infoString());
				saveDemodaErrors(request, tipoComAdapterVO);				
				request.setAttribute(name, tipoComAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoComAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoComAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoComAdapterVO.getReport().getReportFileName();

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
