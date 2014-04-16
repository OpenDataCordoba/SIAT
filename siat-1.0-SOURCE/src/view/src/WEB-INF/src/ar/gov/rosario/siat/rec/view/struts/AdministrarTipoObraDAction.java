//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.TipoObraAdapter;
import ar.gov.rosario.siat.rec.iface.model.TipoObraVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipoObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipoObraDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipoObraAdapter tipoObraAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipoObraAdapterForView(userSession, commonKey)";
				tipoObraAdapterVO = RecServiceLocator.getCdmService().getTipoObraAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipoObraAdapterForUpdate(userSession, commonKey)";
				tipoObraAdapterVO = RecServiceLocator.getCdmService().getTipoObraAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_TIPOOBRA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipoObraAdapterForView(userSession, commonKey)";
				tipoObraAdapterVO = RecServiceLocator.getCdmService().getTipoObraAdapterForView(userSession, commonKey);
				tipoObraAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.TIPOOBRA_LABEL);					
				actionForward = mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getTipoObraAdapterForCreate(userSession)";
				tipoObraAdapterVO = RecServiceLocator.getCdmService().getTipoObraAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_TIPOOBRA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getTipoObraAdapterForView(userSession)";
				tipoObraAdapterVO = RecServiceLocator.getCdmService().getTipoObraAdapterForView(userSession, commonKey);
				tipoObraAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.TIPOOBRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getTipoObraAdapterForView(userSession)";
				tipoObraAdapterVO = RecServiceLocator.getCdmService().getTipoObraAdapterForView(userSession, commonKey);
				tipoObraAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, RecError.TIPOOBRA_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tipoObraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipoObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipoObraAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipoObraAdapter.NAME + ": "+ tipoObraAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipoObraAdapter.NAME, tipoObraAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipoObraAdapter.NAME, tipoObraAdapterVO);
			 
			saveDemodaMessages(request, tipoObraAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoObraAdapter tipoObraAdapterVO = (TipoObraAdapter) userSession.get(TipoObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoObraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoObraAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoObraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// llamada al servicio
			TipoObraVO tipoObraVO = RecServiceLocator.getCdmService().createTipoObra(userSession, tipoObraAdapterVO.getTipoObra());
			
            // Tiene errores recuperables
			if (tipoObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraVO.infoString()); 
				saveDemodaErrors(request, tipoObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoObraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoObraAdapter tipoObraAdapterVO = (TipoObraAdapter) userSession.get(TipoObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoObraAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipoObraAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipoObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoObraAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// llamada al servicio
			TipoObraVO tipoObraVO = RecServiceLocator.getCdmService().updateTipoObra(userSession, tipoObraAdapterVO.getTipoObra());
			
            // Tiene errores recuperables
			if (tipoObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString()); 
				saveDemodaErrors(request, tipoObraVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipoObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoObraAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoObraAdapter tipoObraAdapterVO = (TipoObraAdapter) userSession.get(TipoObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoObraAdapter.NAME); 
			}

			// llamada al servicio
			TipoObraVO tipoObraVO = RecServiceLocator.getCdmService().deleteTipoObra
				(userSession, tipoObraAdapterVO.getTipoObra());
			
            // Tiene errores recuperables
			if (tipoObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString());
				saveDemodaErrors(request, tipoObraVO);				
				request.setAttribute(TipoObraAdapter.NAME, tipoObraAdapterVO);
				return mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoObraAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoObraAdapter tipoObraAdapterVO = (TipoObraAdapter) userSession.get(TipoObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoObraAdapter.NAME); 
			}

			// llamada al servicio
			TipoObraVO tipoObraVO = RecServiceLocator.getCdmService().activarTipoObra
				(userSession, tipoObraAdapterVO.getTipoObra());
			
            // Tiene errores recuperables
			if (tipoObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString());
				saveDemodaErrors(request, tipoObraVO);				
				request.setAttribute(TipoObraAdapter.NAME, tipoObraAdapterVO);
				return mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoObraAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_TIPOOBRA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipoObraAdapter tipoObraAdapterVO = (TipoObraAdapter) userSession.get(TipoObraAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipoObraAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoObraAdapter.NAME); 
			}

			// llamada al servicio
			TipoObraVO tipoObraVO = RecServiceLocator.getCdmService().desactivarTipoObra(userSession, tipoObraAdapterVO.getTipoObra());
			
            // Tiene errores recuperables
			if (tipoObraVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString());
				saveDemodaErrors(request, tipoObraVO);				
				request.setAttribute(TipoObraAdapter.NAME, tipoObraAdapterVO);
				return mapping.findForward(RecConstants.FWD_TIPOOBRA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipoObraVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoObraAdapter.NAME, tipoObraAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipoObraAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoObraAdapter.NAME);
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
			String name = TipoObraAdapter.NAME;
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
			TipoObraAdapter tipoObraAdapterVO = (TipoObraAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (tipoObraAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			tipoObraAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			tipoObraAdapterVO = RecServiceLocator.getCdmService().imprimirTipoObra(userSession, tipoObraAdapterVO);

			// limpia la lista de reports y la lista de tablas
			tipoObraAdapterVO.getReport().getListReport().clear();
			tipoObraAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (tipoObraAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoObraAdapterVO.infoString());
				saveDemodaErrors(request, tipoObraAdapterVO);				
				request.setAttribute(name, tipoObraAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (tipoObraAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoObraAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, tipoObraAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = tipoObraAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoObraAdapter.NAME);
		
	}
		
}
