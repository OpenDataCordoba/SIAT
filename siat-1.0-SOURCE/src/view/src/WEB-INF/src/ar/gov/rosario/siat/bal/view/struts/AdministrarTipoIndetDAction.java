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

import ar.gov.rosario.siat.bal.iface.model.TipoIndetAdapter;
import ar.gov.rosario.siat.bal.iface.model.TipoIndetVO;
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

public final class AdministrarTipoIndetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoIndetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoIndetAdapter tipoIndetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoIndetAdapterForView(userSession, commonKey)";
				tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoIndetAdapterForUpdate(userSession, commonKey)";
				tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOINDET_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoIndetAdapterForView(userSession, commonKey)";
				tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterForView(userSession, commonKey);				
				tipoIndetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.TIPOINDET_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoIndetAdapterForCreate(userSession)";
				tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOINDET_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoIndetAdapterForView(userSession)";
				tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterForView(userSession, commonKey);
				tipoIndetAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.TIPOINDET_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoIndetAdapterForView(userSession)";
				tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterForView(userSession, commonKey);
				tipoIndetAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.TIPOINDET_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoIndetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoIndetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoIndetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoIndetAdapter.NAME + ": "+ tipoIndetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			 
			saveDemodaMessages(request, tipoIndetAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(TipoIndetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoIndetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoIndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoIndetAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoIndetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoIndetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// llamada al servicio
			TipoIndetVO tipoIndetVO = BalServiceLocator.getDefinicionService().createTipoIndet(userSession, tipoIndetAdapterVO.getTipoIndet());
			
            // Tiene errores recuperables
			if (tipoIndetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetVO.infoString()); 
				saveDemodaErrors(request, tipoIndetVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoIndetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoIndetVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoIndetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(TipoIndetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoIndetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoIndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoIndetAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoIndetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoIndetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// llamada al servicio
			TipoIndetVO tipoIndetVO = BalServiceLocator.getDefinicionService().updateTipoIndet(userSession, tipoIndetAdapterVO.getTipoIndet());
			
            // Tiene errores recuperables
			if (tipoIndetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoIndetVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoIndetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoIndetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoIndetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(TipoIndetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoIndetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoIndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
			}

			// llamada al servicio
			TipoIndetVO tipoIndetVO = BalServiceLocator.getDefinicionService().deleteTipoIndet
				(userSession, tipoIndetAdapterVO.getTipoIndet());
			
            // Tiene errores recuperables
			if (tipoIndetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString());
				saveDemodaErrors(request, tipoIndetVO);				
				request.setAttribute(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
				return mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoIndetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoIndetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoIndetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
		}
	}
	
//	public ActionForward activar(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
//		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, BaseSecurityConstants.ACTIVAR); 
//		if (userSession==null) return forwardErrorSession(request);
//		
//		try {
//			// Bajo el adapter del userSession
//			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(TipoIndetAdapter.NAME);
//			
//			// Si es nulo no se puede continuar
//			if (tipoIndetAdapterVO == null) {
//				log.error("error en: "  + funcName + ": " + TipoIndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
//				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
//			}
//
//			// llamada al servicio
//			TipoIndetVO tipoIndetVO = BalServiceLocator.getDefinicionService().activarTipoIndet(userSession, tipoIndetAdapterVO.getTipoIndet());
//			
//            // Tiene errores recuperables
//			if (tipoIndetVO.hasErrorRecoverable()) {
//				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString());
//				saveDemodaErrors(request, tipoIndetVO);				
//				request.setAttribute(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//				return mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);
//			}
//			
//			// Tiene errores no recuperables
//			if (tipoIndetVO.hasErrorNonRecoverable()) {
//				log.error("error en: "  + funcName + ": " + tipoIndetAdapterVO.errorString()); 
//				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			}
//			
//			// Fue Exitoso
//			return forwardConfirmarOk(mapping, request, funcName, TipoIndetAdapter.NAME);
//		} catch (Exception exception) {
//			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
//		}	
//	}
	
//	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response)
//			throws Exception {
//		
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
//		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, BaseSecurityConstants.DESACTIVAR); 
//		if (userSession==null) return forwardErrorSession(request);
//		
//		try {
//			// Bajo el adapter del userSession
//			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(TipoIndetAdapter.NAME);
//			
//			// Si es nulo no se puede continuar
//			if (tipoIndetAdapterVO == null) {
//				log.error("error en: "  + funcName + ": " + TipoIndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
//				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
//			}
//
//			// llamada al servicio
//			TipoIndetVO tipoIndetVO = BalServiceLocator.getDefinicionService().desactivarTipoIndet(userSession, tipoIndetAdapterVO.getTipoIndet());
//			
//            // Tiene errores recuperables
//			if (tipoIndetVO.hasErrorRecoverable()) {
//				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString());
//				saveDemodaErrors(request, tipoIndetVO);				
//				request.setAttribute(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//				return mapping.findForward(BalConstants.FWD_TIPOINDET_VIEW_ADAPTER);
//			}
//			
//			// Tiene errores no recuperables
//			if (tipoIndetVO.hasErrorNonRecoverable()) {
//				log.error("error en: "  + funcName + ": " + tipoIndetAdapterVO.errorString()); 
//				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			}
//			
//			// Fue Exitoso
//			return forwardConfirmarOk(mapping, request, funcName, TipoIndetAdapter.NAME);
//		} catch (Exception exception) {
//			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
//		}
//	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoIndetAdapter.NAME);
		
	}
	
//	public ActionForward param (ActionMapping mapping, ActionForm form,
//		HttpServletRequest request, HttpServletResponse response)
//		throws Exception {
//		
//		String funcName = DemodaUtil.currentMethodName();
//		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
//		
//		UserSession userSession = getCurrentUserSession(request, mapping); 
//		if (userSession==null) return forwardErrorSession(request);
//		
//		try {
//			// Bajo el adapter del userSession
//			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(TipoIndetAdapter.NAME);
//			
//			// Si es nulo no se puede continuar
//			if (tipoIndetAdapterVO == null) {
//				log.error("error en: "  + funcName + ": " + TipoIndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
//				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
//			}
//
//			// Recuperamos datos del form en el vo
//			DemodaUtil.populateVO(tipoIndetAdapterVO, request);
//			
//            // Tiene errores recuperables
//			if (tipoIndetAdapterVO.hasErrorRecoverable()) {
//				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString()); 
//				saveDemodaErrors(request, tipoIndetAdapterVO);
//				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			}
//			
//			// llamada al servicio
//			tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().getTipoIndetAdapterParam(userSession, tipoIndetAdapterVO);
//			
//            // Tiene errores recuperables
//			if (tipoIndetAdapterVO.hasErrorRecoverable()) {
//				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString()); 
//				saveDemodaErrors(request, tipoIndetAdapterVO);
//				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			}
//			
//			// Tiene errores no recuperables
//			if (tipoIndetAdapterVO.hasErrorNonRecoverable()) {
//				log.error("error en: "  + funcName + ": " + tipoIndetAdapterVO.errorString()); 
//				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			}
//			
//			// Envio el VO al request
//			request.setAttribute(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			// Subo el apdater al userSession
//			userSession.put(TipoIndetAdapter.NAME, tipoIndetAdapterVO);
//			
//			return mapping.findForward(BalConstants.FWD_TIPOINDET_EDIT_ADAPTER);
//		
//		} catch (Exception exception) {
//			return baseException(mapping, request, funcName, exception, TipoIndetAdapter.NAME);
//		}
//	}
		
		
		public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = TipoIndetAdapter.NAME;
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
			TipoIndetAdapter tipoIndetAdapterVO = (TipoIndetAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoIndetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipoIndetAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoIndetAdapterVO = BalServiceLocator.getDefinicionService().imprimirTipoIndet(userSession, tipoIndetAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoIndetAdapterVO.getReport().getListReport().clear();
			tipoIndetAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoIndetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetAdapterVO.infoString());
				saveDemodaErrors(request, tipoIndetAdapterVO);				
				request.setAttribute(name, tipoIndetAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoIndetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoIndetAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoIndetAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoIndetAdapterVO.getReport().getReportFileName();

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
