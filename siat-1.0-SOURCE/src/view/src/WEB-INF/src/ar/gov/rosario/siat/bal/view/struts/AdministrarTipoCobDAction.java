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

import ar.gov.rosario.siat.bal.iface.model.TipoCobAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoCobVO;
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

public class AdministrarTipoCobDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoCobDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoCobAdapter tipoCobAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoCobAdapterForView(userSession, commonKey)";
				tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoCobAdapterForUpdate(userSession, commonKey)";
				tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOB_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoCobAdapterForView(userSession, commonKey)";
				tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobAdapterForView(userSession, commonKey);				
				tipoCobAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.TIPOCOB_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoCobAdapterForCreate(userSession)";
				tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOB_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoCobAdapterForView(userSession)";
				tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobAdapterForView(userSession, commonKey);
				tipoCobAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.TIPOCOB_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoCobAdapterForView(userSession)";
				tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobAdapterForView(userSession, commonKey);
				tipoCobAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.TIPOCOB_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoCobAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoCobAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoCobAdapter.NAME + ": "+ tipoCobAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoCobAdapter.NAME, tipoCobAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoCobAdapter.NAME, tipoCobAdapterVO);
			 
			saveDemodaMessages(request, tipoCobAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoCobAdapter tipoCobAdapterVO = (TipoCobAdapter) userSession.get(TipoCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoCobAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoCobAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoCobAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoCobAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// llamada al servicio
			TipoCobVO tipoCobVO = BalServiceLocator.getFolioTesoreriaService().createTipoCob(userSession, tipoCobAdapterVO.getTipoCob());
			
            // Tiene errores recuperables
			if (tipoCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobVO.infoString()); 
				saveDemodaErrors(request, tipoCobVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoCobAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoCobAdapter tipoCobAdapterVO = (TipoCobAdapter) userSession.get(TipoCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoCobAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoCobAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoCobAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoCobAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// llamada al servicio
			TipoCobVO tipoCobVO = BalServiceLocator.getFolioTesoreriaService().updateTipoCob(userSession, tipoCobAdapterVO.getTipoCob());
			
            // Tiene errores recuperables
			if (tipoCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoCobVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoCobAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoCobAdapter tipoCobAdapterVO = (TipoCobAdapter) userSession.get(TipoCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoCobAdapter.NAME); 
			}

			// llamada al servicio
			TipoCobVO tipoCobVO = BalServiceLocator.getFolioTesoreriaService().deleteTipoCob(userSession, tipoCobAdapterVO.getTipoCob());
			
            // Tiene errores recuperables
			if (tipoCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString());
				saveDemodaErrors(request, tipoCobVO);				
				request.setAttribute(TipoCobAdapter.NAME, tipoCobAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoCobAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoCobAdapter tipoCobAdapterVO = (TipoCobAdapter) userSession.get(TipoCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoCobAdapter.NAME); 
			}

			// llamada al servicio
			TipoCobVO tipoCobVO = BalServiceLocator.getFolioTesoreriaService().activarTipoCob(userSession, tipoCobAdapterVO.getTipoCob());
			
            // Tiene errores recuperables
			if (tipoCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString());
				saveDemodaErrors(request, tipoCobVO);				
				request.setAttribute(TipoCobAdapter.NAME, tipoCobAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoCobAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoCobAdapter tipoCobAdapterVO = (TipoCobAdapter) userSession.get(TipoCobAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoCobAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoCobAdapter.NAME); 
			}

			// llamada al servicio
			TipoCobVO tipoCobVO = BalServiceLocator.getFolioTesoreriaService().desactivarTipoCob(userSession, tipoCobAdapterVO.getTipoCob());
			
            // Tiene errores recuperables
			if (tipoCobVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString());
				saveDemodaErrors(request, tipoCobVO);				
				request.setAttribute(TipoCobAdapter.NAME, tipoCobAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOCOB_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoCobVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobAdapter.NAME, tipoCobAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoCobAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoCobAdapter.NAME);
		
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
			String name = TipoCobAdapter.NAME;
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
			TipoCobAdapter tipoCobAdapterVO = (TipoCobAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoCobAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}

			// prepara el report del adapter para luego generar el reporte
			tipoCobAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoCobAdapterVO = BalServiceLocator.getFolioTesoreriaService().imprimirTipoCob(userSession, tipoCobAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoCobAdapterVO.getReport().getListReport().clear();
			tipoCobAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoCobAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobAdapterVO.infoString());
				saveDemodaErrors(request, tipoCobAdapterVO);				
				request.setAttribute(name, tipoCobAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoCobAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoCobAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoCobAdapterVO.getReport().getReportFileName();

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
