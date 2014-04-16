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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.DesGenAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesGenVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesGenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesGenDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesGenAdapter desGenAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDesGenAdapterForView(userSession, commonKey)";
				desGenAdapterVO = GdeServiceLocator.getDefinicionService().getDesGenAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesGenAdapterForUpdate(userSession, commonKey)";
				desGenAdapterVO = GdeServiceLocator.getDefinicionService().getDesGenAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESGEN_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDesGenAdapterForView(userSession, commonKey)";
				desGenAdapterVO = GdeServiceLocator.getDefinicionService().getDesGenAdapterForView(userSession, commonKey);				
				desGenAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DESGEN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDesGenAdapterForCreate(userSession)";
				desGenAdapterVO = GdeServiceLocator.getDefinicionService().getDesGenAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_DESGEN_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getDesGenAdapterForView(userSession)";
				desGenAdapterVO = GdeServiceLocator.getDefinicionService().getDesGenAdapterForView(userSession, commonKey);
				desGenAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.DESGEN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getDesGenAdapterForView(userSession)";
				desGenAdapterVO = GdeServiceLocator.getDefinicionService().getDesGenAdapterForView(userSession, commonKey);
				desGenAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.DESGEN_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desGenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desGenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desGenAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesGenAdapter.NAME + ": "+ desGenAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesGenAdapter.NAME, desGenAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesGenAdapter.NAME, desGenAdapterVO);
			 
			saveDemodaMessages(request, desGenAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesGenAdapter desGenAdapterVO = (DesGenAdapter) userSession.get(DesGenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desGenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desGenAdapterVO, request);
			
            // Tiene errores recuperables
			if (desGenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString()); 
				saveDemodaErrors(request, desGenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// llamada al servicio
			DesGenVO desGenVO = GdeServiceLocator.getDefinicionService().createDesGen(userSession, desGenAdapterVO.getDesGen());
			
            // Tiene errores recuperables
			if (desGenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenVO.infoString()); 
				saveDemodaErrors(request, desGenVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desGenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesGenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesGenAdapter desGenAdapterVO = (DesGenAdapter) userSession.get(DesGenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desGenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desGenAdapterVO, request);
			
            // Tiene errores recuperables
			if (desGenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString()); 
				saveDemodaErrors(request, desGenAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// llamada al servicio
			DesGenVO desGenVO = GdeServiceLocator.getDefinicionService().updateDesGen(userSession, desGenAdapterVO.getDesGen());
			
            // Tiene errores recuperables
			if (desGenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString()); 
				saveDemodaErrors(request, desGenVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desGenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesGenAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesGenAdapter desGenAdapterVO = (DesGenAdapter) userSession.get(DesGenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desGenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenAdapter.NAME); 
			}

			// llamada al servicio
			DesGenVO desGenVO = GdeServiceLocator.getDefinicionService().deleteDesGen
				(userSession, desGenAdapterVO.getDesGen());
			
            // Tiene errores recuperables
			if (desGenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString());
				saveDemodaErrors(request, desGenVO);				
				request.setAttribute(DesGenAdapter.NAME, desGenAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desGenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesGenAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesGenAdapter desGenAdapterVO = (DesGenAdapter) userSession.get(DesGenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desGenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenAdapter.NAME); 
			}

			// llamada al servicio
			DesGenVO desGenVO = GdeServiceLocator.getDefinicionService().activarDesGen
				(userSession, desGenAdapterVO.getDesGen());
			
            // Tiene errores recuperables
			if (desGenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString());
				saveDemodaErrors(request, desGenVO);				
				request.setAttribute(DesGenAdapter.NAME, desGenAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desGenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesGenAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesGenAdapter desGenAdapterVO = (DesGenAdapter) userSession.get(DesGenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desGenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenAdapter.NAME); 
			}

			// llamada al servicio
			DesGenVO desGenVO = GdeServiceLocator.getDefinicionService().desactivarDesGen
				(userSession, desGenAdapterVO.getDesGen());
			
            // Tiene errores recuperables
			if (desGenVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString());
				saveDemodaErrors(request, desGenVO);				
				request.setAttribute(DesGenAdapter.NAME, desGenAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESGEN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desGenVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenAdapter.NAME, desGenAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesGenAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DesGenAdapter.NAME);
			
		}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesGenAdapter adapterVO = (DesGenAdapter)userSession.get(DesGenAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getDesGen().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getDesGen()); 
			
			adapterVO.getDesGen().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(DesGenAdapter.NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_DESGEN_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenAdapter.NAME);
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
			String name = DesGenAdapter.NAME;
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
			DesGenAdapter desGenAdapterVO = (DesGenAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (desGenAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			desGenAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			desGenAdapterVO = GdeServiceLocator.getDefinicionService().imprimirDesGen(userSession, desGenAdapterVO);

			// limpia la lista de reports y la lista de tablas
			desGenAdapterVO.getReport().getListReport().clear();
			desGenAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (desGenAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenAdapterVO.infoString());
				saveDemodaErrors(request, desGenAdapterVO);				
				request.setAttribute(name, desGenAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (desGenAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, desGenAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = desGenAdapterVO.getReport().getReportFileName();

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
