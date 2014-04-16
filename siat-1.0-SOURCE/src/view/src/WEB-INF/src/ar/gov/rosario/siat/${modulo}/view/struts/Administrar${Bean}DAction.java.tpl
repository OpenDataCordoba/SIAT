package ar.gov.rosario.siat.${modulo}.view.struts;

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
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}Adapter;
import ar.gov.rosario.siat.${modulo}.iface.model.${Bean}VO;
import ar.gov.rosario.siat.${modulo}.iface.service.${Modulo}ServiceLocator;
import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}Error;
import ar.gov.rosario.siat.${modulo}.iface.util.${Modulo}SecurityConstants;
import ar.gov.rosario.siat.${modulo}.view.util.${Modulo}Constants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import ar.gov.rosario.siat.base.view.util.UserSession;

public final class Administrar${Bean}DAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(Administrar${Bean}DAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		${Bean}Adapter ${bean}AdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "get${Bean}AdapterForView(userSession, commonKey)";
				${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "get${Bean}AdapterForUpdate(userSession, commonKey)";
				${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(${Modulo}Constants.FWD_${BEAN}_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "get${Bean}AdapterForView(userSession, commonKey)";
				${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterForView(userSession, commonKey);				
				${bean}AdapterVO.addMessage(BaseError.MSG_ELIMINAR, ${Modulo}Error.${BEAN}_LABEL);
				actionForward = mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "get${Bean}AdapterForCreate(userSession)";
				${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterForCreate(userSession);
				actionForward = mapping.findForward(${Modulo}Constants.FWD_${BEAN}_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "get${Bean}AdapterForView(userSession)";
				${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterForView(userSession, commonKey);
				${bean}AdapterVO.addMessage(BaseError.MSG_ACTIVAR, ${Modulo}Error.${BEAN}_LABEL);
				actionForward = mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "get${Bean}AdapterForView(userSession)";
				${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterForView(userSession, commonKey);
				${bean}AdapterVO.addMessage(BaseError.MSG_DESACTIVAR, ${Modulo}Error.${BEAN}_LABEL);
				actionForward = mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (${bean}AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + ${bean}AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			${bean}AdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ${Bean}Adapter.NAME + ": "+ ${bean}AdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(${Bean}Adapter.NAME, ${bean}AdapterVO);
			// Subo el apdater al userSession
			userSession.put(${Bean}Adapter.NAME, ${bean}AdapterVO);
			 
			saveDemodaMessages(request, ${bean}AdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			${Bean}Adapter ${bean}AdapterVO = (${Bean}Adapter) userSession.get(${Bean}Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(${bean}AdapterVO, request);
			
            // Tiene errores recuperables
			if (${bean}AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString()); 
				saveDemodaErrors(request, ${bean}AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// llamada al servicio
			${Bean}VO ${bean}VO = ${Modulo}ServiceLocator.get${Submodulo}Service().create${Bean}(userSession, ${bean}AdapterVO.get${Bean}());
			
            // Tiene errores recuperables
			if (${bean}VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}VO.infoString()); 
				saveDemodaErrors(request, ${bean}VO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (${bean}VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}VO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ${Bean}Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			${Bean}Adapter ${bean}AdapterVO = (${Bean}Adapter) userSession.get(${Bean}Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(${bean}AdapterVO, request);
			
            // Tiene errores recuperables
			if (${bean}AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString()); 
				saveDemodaErrors(request, ${bean}AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// llamada al servicio
			${Bean}VO ${bean}VO = ${Modulo}ServiceLocator.get${Submodulo}Service().update${Bean}(userSession, ${bean}AdapterVO.get${Bean}());
			
            // Tiene errores recuperables
			if (${bean}VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString()); 
				saveDemodaErrors(request, ${bean}VO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (${bean}VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ${Bean}Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			${Bean}Adapter ${bean}AdapterVO = (${Bean}Adapter) userSession.get(${Bean}Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// llamada al servicio
			${Bean}VO ${bean}VO = ${Modulo}ServiceLocator.get${Submodulo}Service().delete${Bean}
				(userSession, ${bean}AdapterVO.get${Bean}());
			
            // Tiene errores recuperables
			if (${bean}VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString());
				saveDemodaErrors(request, ${bean}VO);				
				request.setAttribute(${Bean}Adapter.NAME, ${bean}AdapterVO);
				return mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (${bean}VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ${Bean}Adapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			${Bean}Adapter ${bean}AdapterVO = (${Bean}Adapter) userSession.get(${Bean}Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// llamada al servicio
			${Bean}VO ${bean}VO = ${Modulo}ServiceLocator.get${Submodulo}Service().activar${Bean}
				(userSession, ${bean}AdapterVO.get${Bean}());
			
            // Tiene errores recuperables
			if (${bean}VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString());
				saveDemodaErrors(request, ${bean}VO);				
				request.setAttribute(${Bean}Adapter.NAME, ${bean}AdapterVO);
				return mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (${bean}VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ${Bean}Adapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ${Modulo}SecurityConstants.ABM_${BEAN}, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			${Bean}Adapter ${bean}AdapterVO = (${Bean}Adapter) userSession.get(${Bean}Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// llamada al servicio
			${Bean}VO ${bean}VO = ${Modulo}ServiceLocator.get${Submodulo}Service().desactivar${Bean}
				(userSession, ${bean}AdapterVO.get${Bean}());
			
            // Tiene errores recuperables
			if (${bean}VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString());
				saveDemodaErrors(request, ${bean}VO);				
				request.setAttribute(${Bean}Adapter.NAME, ${bean}AdapterVO);
				return mapping.findForward(${Modulo}Constants.FWD_${BEAN}_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (${bean}VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ${Bean}Adapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ${Bean}Adapter.NAME);
		
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
			${Bean}Adapter ${bean}AdapterVO = (${Bean}Adapter) userSession.get(${Bean}Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (${bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ${Bean}Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ${Bean}Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(${bean}AdapterVO, request);
			
            // Tiene errores recuperables
			if (${bean}AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString()); 
				saveDemodaErrors(request, ${bean}AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// llamada al servicio
			${bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().get${Bean}AdapterParam(userSession, ${bean}AdapterVO);
			
            // Tiene errores recuperables
			if (${bean}AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ${bean}AdapterVO.infoString()); 
				saveDemodaErrors(request, ${bean}AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (${bean}AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ${bean}AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ${Bean}Adapter.NAME, ${bean}AdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(${Bean}Adapter.NAME, ${bean}AdapterVO);
			// Subo el apdater al userSession
			userSession.put(${Bean}Adapter.NAME, ${bean}AdapterVO);
			
			return mapping.findForward(${Modulo}Constants.FWD_${BEAN}_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ${Bean}Adapter.NAME);
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
			String name = {Bean}Adapter.NAME;
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
			{Bean}Adapter {bean}AdapterVO = ({Bean}Adapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if ({bean}AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, {Bean}Adapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			{bean}AdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			{bean}AdapterVO = ${Modulo}ServiceLocator.get${Submodulo}Service().imprimir{Bean}(userSession, {bean}AdapterVO);

			// limpia la lista de reports y la lista de tablas
			{bean}AdapterVO.getReport().getListReport().clear();
			{bean}AdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if ({bean}AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + {bean}AdapterVO.infoString());
				saveDemodaErrors(request, {bean}AdapterVO);				
				request.setAttribute(name, {bean}AdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (${bean}AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + {bean}AdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, {bean}AdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = {bean}AdapterVO.getReport().getReportFileName();

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
