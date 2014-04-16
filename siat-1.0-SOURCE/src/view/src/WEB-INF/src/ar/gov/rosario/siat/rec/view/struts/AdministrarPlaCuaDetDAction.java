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
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetAdapter;
import ar.gov.rosario.siat.rec.iface.model.PlaCuaDetVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarPlaCuaDetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlaCuaDetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlaCuaDetAdapter plaCuaDetAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPlaCuaDetAdapterForView(userSession, commonKey)";
				plaCuaDetAdapterVO = RecServiceLocator.getCdmService().getPlaCuaDetAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLACUADET_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlaCuaDetAdapterForUpdate(userSession, commonKey)";
				plaCuaDetAdapterVO = RecServiceLocator.getCdmService().getPlaCuaDetAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(RecConstants.FWD_PLACUADET_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPlaCuaDetAdapterForView(userSession, commonKey)";
				plaCuaDetAdapterVO = RecServiceLocator.getCdmService().getPlaCuaDetAdapterForView
					(userSession, commonKey);				
				plaCuaDetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.PLACUADET_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_PLACUADET_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + 
				stringServicio + " para " + act );

			// Tiene errores no recuperables
			if (plaCuaDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + plaCuaDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			plaCuaDetAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlaCuaDetAdapter.NAME + ": "+ 
				plaCuaDetAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			 
			saveDemodaMessages(request, plaCuaDetAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaCuaDetAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaCuaDetAdapter plaCuaDetAdapterVO = (PlaCuaDetAdapter) userSession.get(PlaCuaDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaCuaDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaCuaDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaCuaDetAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaCuaDetAdapterVO, request);
			
			
            // Tiene errores recuperables
			if (plaCuaDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetAdapterVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			}
			
			// Populate de los id seleccionados en los combos de UsoCdM para 
			// cada uno de los detalles 
   			for (PlaCuaDetVO item: plaCuaDetAdapterVO.getPlaCuaDet().getListPlaCuaDet())
				item.getUsoCdM().setId(new Long((request.getParameter(item.getId().toString()))));
				
			// llamada al servicio
			PlaCuaDetVO plaCuaDetVO = RecServiceLocator.getCdmService().updatePlaCuaDet(userSession, plaCuaDetAdapterVO.getPlaCuaDet());
			
            // Tiene errores recuperables
			if (plaCuaDetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetAdapterVO.infoString()); 
				saveDemodaErrors(request, plaCuaDetVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (plaCuaDetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaCuaDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaCuaDetAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaCuaDetAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaCuaDetAdapter plaCuaDetAdapterVO = (PlaCuaDetAdapter) userSession.get(PlaCuaDetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaCuaDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaCuaDetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaCuaDetAdapter.NAME); 
			}

			// llamada al servicio
			PlaCuaDetVO plaCuaDetVO = RecServiceLocator.getCdmService().deletePlaCuaDet
				(userSession, plaCuaDetAdapterVO.getPlaCuaDet());
			
            // Tiene errores recuperables
			if (plaCuaDetVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetAdapterVO.infoString());
				saveDemodaErrors(request, plaCuaDetVO);				
				request.setAttribute(PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
				return mapping.findForward(RecConstants.FWD_PLACUADET_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (plaCuaDetVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaCuaDetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaCuaDetAdapter.NAME, plaCuaDetAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaCuaDetAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaCuaDetAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaCuaDetAdapter.NAME);
		
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
			String name = PlaCuaDetAdapter.NAME;
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
			PlaCuaDetAdapter plaCuaDetAdapterVO = (PlaCuaDetAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (plaCuaDetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			plaCuaDetAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			plaCuaDetAdapterVO = RecServiceLocator.getCdmService().imprimirPlaCuaDet(userSession, plaCuaDetAdapterVO);

			// limpia la lista de reports y la lista de tablas
			plaCuaDetAdapterVO.getReport().getListReport().clear();
			plaCuaDetAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (plaCuaDetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaCuaDetAdapterVO.infoString());
				saveDemodaErrors(request, plaCuaDetAdapterVO);				
				request.setAttribute(name, plaCuaDetAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (plaCuaDetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaCuaDetAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, plaCuaDetAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = plaCuaDetAdapterVO.getReport().getReportFileName();

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
