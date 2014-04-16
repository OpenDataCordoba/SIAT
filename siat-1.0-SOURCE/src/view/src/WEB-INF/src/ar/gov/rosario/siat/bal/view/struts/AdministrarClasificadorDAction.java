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

import ar.gov.rosario.siat.bal.iface.model.ClasificadorAdapter;
import ar.gov.rosario.siat.bal.iface.model.ClasificadorVO;
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

public final class AdministrarClasificadorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarClasificadorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CLASIFICADOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ClasificadorAdapter clasificadorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getClasificadorAdapterForView(userSession, commonKey)";
				clasificadorAdapterVO = BalServiceLocator.getClasificacionService().getClasificadorAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CLASIFICADOR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getClasificadorAdapterForUpdate(userSession, commonKey)";
				clasificadorAdapterVO = BalServiceLocator.getClasificacionService().getClasificadorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CLASIFICADOR_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getClasificadorAdapterForView(userSession, commonKey)";
				clasificadorAdapterVO = BalServiceLocator.getClasificacionService().getClasificadorAdapterForView(userSession, commonKey);				
				clasificadorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.CLASIFICADOR_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CLASIFICADOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getClasificadorAdapterForCreate(userSession)";
				clasificadorAdapterVO = BalServiceLocator.getClasificacionService().getClasificadorAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_CLASIFICADOR_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getClasificadorAdapterForView(userSession)";
				clasificadorAdapterVO = BalServiceLocator.getClasificacionService().getClasificadorAdapterForView(userSession, commonKey);
				clasificadorAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.CLASIFICADOR_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CLASIFICADOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getClasificadorAdapterForView(userSession)";
				clasificadorAdapterVO = BalServiceLocator.getClasificacionService().getClasificadorAdapterForView(userSession, commonKey);
				clasificadorAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.CLASIFICADOR_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CLASIFICADOR_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (clasificadorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + clasificadorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			clasificadorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ClasificadorAdapter.NAME + ": "+ clasificadorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ClasificadorAdapter.NAME, clasificadorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ClasificadorAdapter.NAME, clasificadorAdapterVO);
			 
			saveDemodaMessages(request, clasificadorAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CLASIFICADOR, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ClasificadorAdapter clasificadorAdapterVO = (ClasificadorAdapter) userSession.get(ClasificadorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (clasificadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ClasificadorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(clasificadorAdapterVO, request);
			
            // Tiene errores recuperables
			if (clasificadorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorAdapterVO.infoString()); 
				saveDemodaErrors(request, clasificadorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// llamada al servicio
			ClasificadorVO clasificadorVO = BalServiceLocator.getClasificacionService().createClasificador(userSession, clasificadorAdapterVO.getClasificador());
			
            // Tiene errores recuperables
			if (clasificadorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorVO.infoString()); 
				saveDemodaErrors(request, clasificadorVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (clasificadorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ClasificadorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CLASIFICADOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ClasificadorAdapter clasificadorAdapterVO = (ClasificadorAdapter) userSession.get(ClasificadorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (clasificadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ClasificadorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(clasificadorAdapterVO, request);
			
            // Tiene errores recuperables
			if (clasificadorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorAdapterVO.infoString()); 
				saveDemodaErrors(request, clasificadorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// llamada al servicio
			ClasificadorVO clasificadorVO = BalServiceLocator.getClasificacionService().updateClasificador(userSession, clasificadorAdapterVO.getClasificador());
			
            // Tiene errores recuperables
			if (clasificadorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorAdapterVO.infoString()); 
				saveDemodaErrors(request, clasificadorVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (clasificadorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ClasificadorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CLASIFICADOR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ClasificadorAdapter clasificadorAdapterVO = (ClasificadorAdapter) userSession.get(ClasificadorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (clasificadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ClasificadorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorAdapter.NAME); 
			}

			// llamada al servicio
			ClasificadorVO clasificadorVO = BalServiceLocator.getClasificacionService().deleteClasificador
				(userSession, clasificadorAdapterVO.getClasificador());
			
            // Tiene errores recuperables
			if (clasificadorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorAdapterVO.infoString());
				saveDemodaErrors(request, clasificadorVO);				
				request.setAttribute(ClasificadorAdapter.NAME, clasificadorAdapterVO);
				return mapping.findForward(BalConstants.FWD_CLASIFICADOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (clasificadorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorAdapter.NAME, clasificadorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ClasificadorAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorAdapter.NAME);
		}
	}
	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ClasificadorAdapter.NAME);
		
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
			String name = ClasificadorAdapter.NAME;
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
			ClasificadorAdapter clasificadorAdapterVO = (ClasificadorAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (clasificadorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			clasificadorAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			clasificadorAdapterVO = BalServiceLocator.getClasificacionService().imprimirClasificador(userSession, clasificadorAdapterVO);

			// limpia la lista de reports y la lista de tablas
			clasificadorAdapterVO.getReport().getListReport().clear();
			clasificadorAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (clasificadorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorAdapterVO.infoString());
				saveDemodaErrors(request, clasificadorAdapterVO);				
				request.setAttribute(name, clasificadorAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (clasificadorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, clasificadorAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = clasificadorAdapterVO.getReport().getReportFileName();

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
