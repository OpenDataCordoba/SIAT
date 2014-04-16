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
import ar.gov.rosario.siat.def.iface.model.AreaAdapter;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAreaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAreaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AreaAdapter areaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAreaAdapterForView(userSession, commonKey)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAreaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);
			}
			
			if (act.equals(DefConstants.ACT_ADM_RECURSOAREA_ADAPTER)) {
				stringServicio = "getAreaAdapterForView(userSession, commonKey)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAdapterForRecursoAreaView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECURSOAREA_ADAPTER);
			}			
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAreaAdapterForUpdate(userSession, commonKey)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAreaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_AREA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAreaAdapterForView(userSession, commonKey)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAreaAdapterForView(userSession, commonKey);				
				areaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.AREA_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAreaAdapterForCreate(userSession)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAreaAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_AREA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAreaAdapterForView(userSession)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAreaAdapterForView(userSession, commonKey);
				areaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.AREA_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAreaAdapterForView(userSession)";
				areaAdapterVO = DefServiceLocator.getConfiguracionService().getAreaAdapterForView(userSession, commonKey);
				areaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.AREA_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (areaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + areaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			areaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AreaAdapter.NAME + ": "+ areaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AreaAdapter.NAME, areaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AreaAdapter.NAME, areaAdapterVO);
			 
			saveDemodaMessages(request, areaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO = (AreaAdapter) userSession.get(AreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(areaAdapterVO, request);
			
            // Tiene errores recuperables
			if (areaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString()); 
				saveDemodaErrors(request, areaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// llamada al servicio
			AreaVO areaVO = DefServiceLocator.getConfiguracionService().createArea(userSession, areaAdapterVO.getArea());
			
            // Tiene errores recuperables
			if (areaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaVO.infoString()); 
				saveDemodaErrors(request, areaVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (areaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO = (AreaAdapter) userSession.get(AreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(areaAdapterVO, request);
			
            // Tiene errores recuperables
			if (areaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString()); 
				saveDemodaErrors(request, areaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// llamada al servicio
			AreaVO areaVO = DefServiceLocator.getConfiguracionService().updateArea(userSession, areaAdapterVO.getArea());
			
            // Tiene errores recuperables
			if (areaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString()); 
				saveDemodaErrors(request, areaVO);
				return forwardErrorRecoverable(mapping, request, userSession, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (areaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO = (AreaAdapter) userSession.get(AreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaAdapter.NAME); 
			}

			// llamada al servicio
			AreaVO areaVO = DefServiceLocator.getConfiguracionService().deleteArea
				(userSession, areaAdapterVO.getArea());
			
            // Tiene errores recuperables
			if (areaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString());
				saveDemodaErrors(request, areaVO);				
				request.setAttribute(AreaAdapter.NAME, areaAdapterVO);
				return mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (areaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO = (AreaAdapter) userSession.get(AreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaAdapter.NAME); 
			}

			// llamada al servicio
			AreaVO areaVO = DefServiceLocator.getConfiguracionService().activarArea
				(userSession, areaAdapterVO.getArea());
			
            // Tiene errores recuperables
			if (areaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString());
				saveDemodaErrors(request, areaVO);				
				request.setAttribute(AreaAdapter.NAME, areaAdapterVO);
				return mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (areaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO = (AreaAdapter) userSession.get(AreaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (areaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AreaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AreaAdapter.NAME); 
			}

			// llamada al servicio
			AreaVO areaVO = DefServiceLocator.getConfiguracionService().desactivarArea
				(userSession, areaAdapterVO.getArea());
			
            // Tiene errores recuperables
			if (areaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString());
				saveDemodaErrors(request, areaVO);				
				request.setAttribute(AreaAdapter.NAME, areaAdapterVO);
				return mapping.findForward(DefConstants.FWD_AREA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (areaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AreaAdapter.NAME, areaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AreaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_AREA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO = (AreaAdapter) userSession.get(AreaAdapter.NAME);	
			
			areaAdapterVO.setPrevAction("/def/BuscarArea");
			areaAdapterVO.setPrevActionParameter("buscar");
			
			return baseVolver(mapping, form, request, response, AreaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AreaAdapter.NAME);
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
			String name = AreaAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			AreaAdapter areaAdapterVO= (AreaAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (areaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			areaAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			areaAdapterVO = DefServiceLocator.getConfiguracionService().imprimirArea(userSession, areaAdapterVO);

			// limpia la lista de reports y la lista de tablas
			areaAdapterVO.getReport().getListReport().clear();
			areaAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (areaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + areaAdapterVO.infoString());
				saveDemodaErrors(request, areaAdapterVO);				
				request.setAttribute(name, areaAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (areaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + areaAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, areaAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = areaAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	
	//	 Metodos relacionados RecursoArea
	public ActionForward verRecursoArea(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSOAREA);

	}

	public ActionForward modificarRecursoArea(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSOAREA);

	}

	public ActionForward eliminarRecursoArea(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSOAREA);

	}
	
	public ActionForward agregarRecursoArea(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECURSOAREA);
		
	}
}
