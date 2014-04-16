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
import ar.gov.rosario.siat.def.iface.model.SiatScriptUsrAdapter;
import ar.gov.rosario.siat.def.iface.model.SiatScriptUsrVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSiatScriptUsrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSiatScriptUsrDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPTUSR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SiatScriptUsrAdapter siatScriptUsrAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSiatScriptUsrAdapterForView(userSession, commonKey)";
				siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSiatScriptUsrAdapterForUpdate(userSession, commonKey)";
				siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSiatScriptUsrAdapterForView(userSession, commonKey)";
				siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterForView(userSession, commonKey);				
				siatScriptUsrAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.SIATSCRIPTUSR_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSiatScriptUsrAdapterForCreate(userSession)";
				siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getSiatScriptUsrAdapterForView(userSession)";
				siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterForView(userSession, commonKey);
				siatScriptUsrAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.SIATSCRIPTUSR_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getSiatScriptUsrAdapterForView(userSession)";
				siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterForView(userSession, commonKey);
				siatScriptUsrAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.SIATSCRIPTUSR_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (siatScriptUsrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + siatScriptUsrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			siatScriptUsrAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SiatScriptUsrAdapter.NAME + ": "+ siatScriptUsrAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			 
			saveDemodaMessages(request, siatScriptUsrAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(SiatScriptUsrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptUsrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptUsrAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptUsrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptUsrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// llamada al servicio
			SiatScriptUsrVO siatScriptUsrVO = DefServiceLocator.getConfiguracionService().createSiatScriptUsr(userSession, siatScriptUsrAdapterVO.getSiatScriptUsr());
			
            // Tiene errores recuperables
			if (siatScriptUsrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrVO.infoString()); 
				saveDemodaErrors(request, siatScriptUsrVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptUsrAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(SiatScriptUsrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptUsrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptUsrAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptUsrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptUsrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// llamada al servicio
			SiatScriptUsrVO siatScriptUsrVO = DefServiceLocator.getConfiguracionService().updateSiatScriptUsr(userSession, siatScriptUsrAdapterVO.getSiatScriptUsr());
			
            // Tiene errores recuperables
			if (siatScriptUsrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptUsrVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptUsrAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(SiatScriptUsrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptUsrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// llamada al servicio
			SiatScriptUsrVO siatScriptUsrVO = DefServiceLocator.getConfiguracionService().deleteSiatScriptUsr
				(userSession, siatScriptUsrAdapterVO.getSiatScriptUsr());
			
            // Tiene errores recuperables
			if (siatScriptUsrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptUsrVO);				
				request.setAttribute(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
				return mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptUsrAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(SiatScriptUsrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptUsrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// llamada al servicio
			SiatScriptUsrVO siatScriptUsrVO = DefServiceLocator.getConfiguracionService().activarSiatScriptUsr
				(userSession, siatScriptUsrAdapterVO.getSiatScriptUsr());
			
            // Tiene errores recuperables
			if (siatScriptUsrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptUsrVO);				
				request.setAttribute(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
				return mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptUsrAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_SIATSCRIPTUSR, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(SiatScriptUsrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptUsrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// llamada al servicio
			SiatScriptUsrVO siatScriptUsrVO = DefServiceLocator.getConfiguracionService().desactivarSiatScriptUsr
				(userSession, siatScriptUsrAdapterVO.getSiatScriptUsr());
			
            // Tiene errores recuperables
			if (siatScriptUsrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptUsrVO);				
				request.setAttribute(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
				return mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SiatScriptUsrAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SiatScriptUsrAdapter.NAME);
		
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
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(SiatScriptUsrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SiatScriptUsrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(siatScriptUsrAdapterVO, request);
			
            // Tiene errores recuperables
			if (siatScriptUsrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptUsrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// llamada al servicio
			siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().getSiatScriptUsrAdapterParam(userSession, siatScriptUsrAdapterVO);
			
            // Tiene errores recuperables
			if (siatScriptUsrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString()); 
				saveDemodaErrors(request, siatScriptUsrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SiatScriptUsrAdapter.NAME, siatScriptUsrAdapterVO);
			
			return mapping.findForward(DefConstants.FWD_SIATSCRIPTUSR_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SiatScriptUsrAdapter.NAME);
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
			String name = SiatScriptUsrAdapter.NAME;
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
			SiatScriptUsrAdapter siatScriptUsrAdapterVO = (SiatScriptUsrAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (siatScriptUsrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, SiatScriptUsrAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			siatScriptUsrAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			siatScriptUsrAdapterVO = DefServiceLocator.getConfiguracionService().imprimirSiatScriptUsr(userSession, siatScriptUsrAdapterVO);

			// limpia la lista de reports y la lista de tablas
			siatScriptUsrAdapterVO.getReport().getListReport().clear();
			siatScriptUsrAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (siatScriptUsrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + siatScriptUsrAdapterVO.infoString());
				saveDemodaErrors(request, siatScriptUsrAdapterVO);				
				request.setAttribute(name, siatScriptUsrAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (siatScriptUsrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + siatScriptUsrAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, siatScriptUsrAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = siatScriptUsrAdapterVO.getReport().getReportFileName();

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
