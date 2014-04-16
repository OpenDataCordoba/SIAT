//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.SiatScriptAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;



public final class AdministrarSiatScriptDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSiatScriptDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SiatScriptAdapter siatScriptAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSiatScriptAdapterForView(userSession, commonKey)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSiatScriptAdapterForUpdate(userSession, commonKey)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSiatScriptAdapterForView(userSession, commonKey)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForView(userSession, commonKey);				
				siatScriptAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.SIATSCRIPT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSiatScriptAdapterForCreate(userSession)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getSiatScriptAdapterForView(userSession)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForView(userSession, commonKey);
				siatScriptAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.SIATSCRIPT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getSiatScriptAdapterForView(userSession)";
				siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterForView(userSession, commonKey);
				siatScriptAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.SIATSCRIPT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (siatScriptAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			siatScriptAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SiatScriptAdapter.NAME + ": "+ siatScriptAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SiatScriptAdapter.NAME, siatScriptAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SiatScriptAdapter.NAME, siatScriptAdapterVO);
			 
			saveDemodaMessages(request, siatScriptAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().createSiatScript(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptVO.infoString()); 
				saveDemodaErrors(request, siatScriptVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().updateSiatScript(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().deleteSiatScript
				(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptVO);				
				request.setAttribute(SiatScriptAdapter.NAME, siatScriptAdapterVO);
				return mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().activarSiatScript
				(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptVO);				
				request.setAttribute(SiatScriptAdapter.NAME, siatScriptAdapterVO);
				return mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPT, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// llamada al servicio
			SiatScriptVO siatScriptVO = DefServiceLocator.getConfiguracionService().desactivarSiatScript
				(userSession, siatScriptAdapterVO.getSiatScript());
			
            // Tiene errores recuperables
			if (siatScriptVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptVO);				
				request.setAttribute(SiatScriptAdapter.NAME, siatScriptAdapterVO);
				return mapping.findForward(DefConstants.FWD_SIATSCRIPT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (siatScriptVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SiatScriptAdapter.NAME);
		
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
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(SiatScriptAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// llamada al servicio
			siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptAdapterParam(userSession, siatScriptAdapterVO);
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptAdapter.NAME, siatScriptAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(SiatScriptAdapter.NAME, siatScriptAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SiatScriptAdapter.NAME, siatScriptAdapterVO);
			
			return mapping.findForward(DefConstants.FWD_SIATSCRIPT_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptAdapter.NAME);
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
			String name = SiatScriptAdapter.NAME;
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
			SiatScriptAdapter siatScriptAdapterVO = (SiatScriptAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (siatScriptAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			siatScriptAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			siatScriptAdapterVO = DefServiceLocator.getConfiguracionService().imprimirSiatScript(userSession, siatScriptAdapterVO);

			// limpia la lista de reports y la lista de tablas
			siatScriptAdapterVO.getReport().getListReport().clear();
			siatScriptAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (siatScriptAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptAdapterVO);				
				request.setAttribute(name, siatScriptAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (siatScriptAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, siatScriptAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = siatScriptAdapterVO.getReport().getReportFileName();

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
				return baseForwardAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENC_SIATSCRIPT, BaseConstants.ACT_MODIFICAR);

			}
		
		//Metodos relacionados a SiatScriptUsr
		
		public ActionForward verSiatScriptUsr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPTUSR);

		}

		public ActionForward modificarSiatScriptUsr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPTUSR);

		}

		public ActionForward eliminarSiatScriptUsr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPTUSR);

		}
		
		public ActionForward agregarSiatScriptUsr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_SIATSCRIPTUSR);
			
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, SiatScriptAdapter.NAME);
				
			}
	
}
