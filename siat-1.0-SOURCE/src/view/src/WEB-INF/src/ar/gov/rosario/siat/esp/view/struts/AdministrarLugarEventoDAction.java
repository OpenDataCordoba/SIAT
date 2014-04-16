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
import ar.gov.rosario.siat.esp.iface.model.LugarEventoAdapter;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoVO;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarLugarEventoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLugarEventoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_LUGAREVENTO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		LugarEventoAdapter lugarEventoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getLugarEventoAdapterForView(userSession, commonKey)";
				lugarEventoAdapterVO = EspServiceLocator.getHabilitacionService().getLugarEventoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EspConstants.FWD_LUGAREVENTO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getLugarEventoAdapterForUpdate(userSession, commonKey)";
				lugarEventoAdapterVO = EspServiceLocator.getHabilitacionService().getLugarEventoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EspConstants.FWD_LUGAREVENTO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getLugarEventoAdapterForView(userSession, commonKey)";
				lugarEventoAdapterVO = EspServiceLocator.getHabilitacionService().getLugarEventoAdapterForView(userSession, commonKey);				
				lugarEventoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EspError.LUGAREVENTO_LABEL);
				actionForward = mapping.findForward(EspConstants.FWD_LUGAREVENTO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getLugarEventoAdapterForCreate(userSession)";
				lugarEventoAdapterVO = EspServiceLocator.getHabilitacionService().getLugarEventoAdapterForCreate(userSession);
				actionForward = mapping.findForward(EspConstants.FWD_LUGAREVENTO_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (lugarEventoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + lugarEventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			lugarEventoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LugarEventoAdapter.NAME + ": "+ lugarEventoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			 
			saveDemodaMessages(request, lugarEventoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LugarEventoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_LUGAREVENTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LugarEventoAdapter lugarEventoAdapterVO = (LugarEventoAdapter) userSession.get(LugarEventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (lugarEventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LugarEventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LugarEventoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(lugarEventoAdapterVO, request);
			
            // Tiene errores recuperables
			if (lugarEventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoAdapterVO.infoString()); 
				saveDemodaErrors(request, lugarEventoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// llamada al servicio
			LugarEventoVO lugarEventoVO = EspServiceLocator.getHabilitacionService().createLugarEvento(userSession, lugarEventoAdapterVO.getLugarEvento());
			
            // Tiene errores recuperables
			if (lugarEventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoVO.infoString()); 
				saveDemodaErrors(request, lugarEventoVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (lugarEventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + lugarEventoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LugarEventoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LugarEventoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_LUGAREVENTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LugarEventoAdapter lugarEventoAdapterVO = (LugarEventoAdapter) userSession.get(LugarEventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (lugarEventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LugarEventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LugarEventoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(lugarEventoAdapterVO, request);
			
            // Tiene errores recuperables
			if (lugarEventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoAdapterVO.infoString()); 
				saveDemodaErrors(request, lugarEventoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// llamada al servicio
			LugarEventoVO lugarEventoVO = EspServiceLocator.getHabilitacionService().updateLugarEvento(userSession, lugarEventoAdapterVO.getLugarEvento());
			
            // Tiene errores recuperables
			if (lugarEventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoAdapterVO.infoString()); 
				saveDemodaErrors(request, lugarEventoVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (lugarEventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + lugarEventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LugarEventoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LugarEventoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_LUGAREVENTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LugarEventoAdapter lugarEventoAdapterVO = (LugarEventoAdapter) userSession.get(LugarEventoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (lugarEventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LugarEventoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LugarEventoAdapter.NAME); 
			}

			// llamada al servicio
			LugarEventoVO lugarEventoVO = EspServiceLocator.getHabilitacionService().deleteLugarEvento
				(userSession, lugarEventoAdapterVO.getLugarEvento());
			
            // Tiene errores recuperables
			if (lugarEventoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoAdapterVO.infoString());
				saveDemodaErrors(request, lugarEventoVO);				
				request.setAttribute(LugarEventoAdapter.NAME, lugarEventoAdapterVO);
				return mapping.findForward(EspConstants.FWD_LUGAREVENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (lugarEventoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + lugarEventoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LugarEventoAdapter.NAME, lugarEventoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, LugarEventoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LugarEventoAdapter.NAME);
		}
	}
	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LugarEventoAdapter.NAME);
		
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
			String name = LugarEventoAdapter.NAME;
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
			LugarEventoAdapter lugarEventoAdapterVO = (LugarEventoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (lugarEventoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, LugarEventoAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			lugarEventoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			lugarEventoAdapterVO = EspServiceLocator.getHabilitacionService().imprimirLugarEvento(userSession, lugarEventoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			lugarEventoAdapterVO.getReport().getListReport().clear();
			lugarEventoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (lugarEventoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoAdapterVO.infoString());
				saveDemodaErrors(request, lugarEventoAdapterVO);				
				request.setAttribute(name, lugarEventoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (lugarEventoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + lugarEventoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, lugarEventoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = lugarEventoAdapterVO.getReport().getReportFileName();

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
