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
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosAdapter;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarValoresCargadosDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarValoresCargadosDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_VALORESCARGADOS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ValoresCargadosAdapter valoresCargadosAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getValoresCargadosAdapterForView(userSession, commonKey)";
				valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EspConstants.FWD_VALORESCARGADOS_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getValoresCargadosAdapterForUpdate(userSession, commonKey)";
				valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EspConstants.FWD_VALORESCARGADOS_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getValoresCargadosAdapterForView(userSession, commonKey)";
				valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterForView(userSession, commonKey);				
				valoresCargadosAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.VALORESCARGADOS_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_VALORESCARGADOS_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getValoresCargadosAdapterForCreate(userSession)";
				valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterForCreate(userSession);
				actionForward = mapping.findForward(EspConstants.FWD_VALORESCARGADOS_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getValoresCargadosAdapterForView(userSession)";
				valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterForView(userSession, commonKey);
				valoresCargadosAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EspError.VALORESCARGADOS_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_VALORESCARGADOS_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getValoresCargadosAdapterForView(userSession)";
				valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterForView(userSession, commonKey);
				valoresCargadosAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EspError.VALORESCARGADOS_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_VALORESCARGADOS_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (valoresCargadosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + valoresCargadosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			valoresCargadosAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ValoresCargadosAdapter.NAME + ": "+ valoresCargadosAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			 
			saveDemodaMessages(request, valoresCargadosAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_VALORESCARGADOS, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ValoresCargadosAdapter valoresCargadosAdapterVO = (ValoresCargadosAdapter) userSession.get(ValoresCargadosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valoresCargadosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValoresCargadosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValoresCargadosAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(valoresCargadosAdapterVO, request);
			
            // Tiene errores recuperables
			if (valoresCargadosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// llamada al servicio
			ValoresCargadosVO valoresCargadosVO = EspServiceLocator.getHabilitacionService().createValoresCargados(userSession, valoresCargadosAdapterVO.getValoresCargados());
			
            // Tiene errores recuperables
			if (valoresCargadosVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (valoresCargadosVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ValoresCargadosAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_VALORESCARGADOS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ValoresCargadosAdapter valoresCargadosAdapterVO = (ValoresCargadosAdapter) userSession.get(ValoresCargadosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valoresCargadosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValoresCargadosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValoresCargadosAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(valoresCargadosAdapterVO, request);
			
            // Tiene errores recuperables
			if (valoresCargadosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// llamada al servicio
			ValoresCargadosVO valoresCargadosVO = EspServiceLocator.getHabilitacionService().updateValoresCargados(userSession, valoresCargadosAdapterVO.getValoresCargados());
			
            // Tiene errores recuperables
			if (valoresCargadosVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (valoresCargadosVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ValoresCargadosAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_VALORESCARGADOS, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ValoresCargadosAdapter valoresCargadosAdapterVO = (ValoresCargadosAdapter) userSession.get(ValoresCargadosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valoresCargadosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValoresCargadosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValoresCargadosAdapter.NAME); 
			}

			// llamada al servicio
			ValoresCargadosVO valoresCargadosVO = EspServiceLocator.getHabilitacionService().deleteValoresCargados
				(userSession, valoresCargadosAdapterVO.getValoresCargados());
			
            // Tiene errores recuperables
			if (valoresCargadosVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString());
				saveDemodaErrors(request, valoresCargadosVO);				
				request.setAttribute(ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
				return mapping.findForward(EspConstants.FWD_VALORESCARGADOS_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (valoresCargadosVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ValoresCargadosAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosAdapter.NAME);
		}
	}
	
			
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ValoresCargadosAdapter.NAME);
		
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
			ValoresCargadosAdapter valoresCargadosAdapterVO = (ValoresCargadosAdapter) userSession.get(ValoresCargadosAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (valoresCargadosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ValoresCargadosAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValoresCargadosAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(valoresCargadosAdapterVO, request);
			
            // Tiene errores recuperables
			if (valoresCargadosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// llamada al servicio
			/*valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().getValoresCargadosAdapterParam(userSession, valoresCargadosAdapterVO);*/
			
            // Tiene errores recuperables
			if (valoresCargadosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (valoresCargadosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ValoresCargadosAdapter.NAME, valoresCargadosAdapterVO);
			
			return mapping.findForward(EspConstants.FWD_VALORESCARGADOS_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosAdapter.NAME);
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
			String name = ValoresCargadosAdapter.NAME;
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
			ValoresCargadosAdapter valoresCargadosAdapterVO = (ValoresCargadosAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (valoresCargadosAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ValoresCargadosAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			valoresCargadosAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			valoresCargadosAdapterVO = EspServiceLocator.getHabilitacionService().imprimirValoresCargados(userSession, valoresCargadosAdapterVO);

			// limpia la lista de reports y la lista de tablas
			valoresCargadosAdapterVO.getReport().getListReport().clear();
			valoresCargadosAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (valoresCargadosAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosAdapterVO.infoString());
				saveDemodaErrors(request, valoresCargadosAdapterVO);				
				request.setAttribute(name, valoresCargadosAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (valoresCargadosAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, valoresCargadosAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = valoresCargadosAdapterVO.getReport().getReportFileName();

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
