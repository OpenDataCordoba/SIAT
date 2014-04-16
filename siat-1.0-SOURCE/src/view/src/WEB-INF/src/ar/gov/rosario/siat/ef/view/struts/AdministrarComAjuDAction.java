//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.ComAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComAjuVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarComAjuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarComAjuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMAJU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ComAjuAdapter comAjuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getComAjuAdapterForView(userSession, commonKey)";
				comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getComAjuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMAJU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getComAjuAdapterForUpdate(userSession, commonKey)";
				comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getComAjuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMAJU_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getComAjuAdapterForView(userSession, commonKey)";
				comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getComAjuAdapterForView(userSession, commonKey);				
				comAjuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.COMAJU_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_COMAJU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getComAjuAdapterForCreate(userSession)";
				comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getComAjuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMAJU_ENC_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (comAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + comAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			comAjuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ComAjuAdapter.NAME + ": "+ comAjuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ComAjuAdapter.NAME, comAjuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ComAjuAdapter.NAME, comAjuAdapterVO);
			 
			saveDemodaMessages(request, comAjuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				EfConstants.ACTION_ADMINISTRAR_ENC_COMAJU, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComAjuAdapter comAjuAdapterVO = (ComAjuAdapter) userSession.get(ComAjuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComAjuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComAjuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comAjuAdapterVO, request);
			
            // Tiene errores recuperables
			if (comAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, comAjuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// llamada al servicio
			ComAjuVO comAjuVO = EfServiceLocator.getFiscalizacionService().createComAju(userSession, comAjuAdapterVO.getComAju());
			
            // Tiene errores recuperables
			if (comAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuVO.infoString()); 
				saveDemodaErrors(request, comAjuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comAjuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ComAjuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComAjuAdapter comAjuAdapterVO = (ComAjuAdapter) userSession.get(ComAjuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComAjuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComAjuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comAjuAdapterVO, request);
			
            // Tiene errores recuperables
			if (comAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, comAjuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// llamada al servicio
			ComAjuVO comAjuVO = EfServiceLocator.getFiscalizacionService().updateComAju(userSession, comAjuAdapterVO.getComAju());
			
            // Tiene errores recuperables
			if (comAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString()); 
				saveDemodaErrors(request, comAjuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ComAjuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMAJU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComAjuAdapter comAjuAdapterVO = (ComAjuAdapter) userSession.get(ComAjuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComAjuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComAjuAdapter.NAME); 
			}

			// llamada al servicio
			ComAjuVO comAjuVO = EfServiceLocator.getFiscalizacionService().deleteComAju
				(userSession, comAjuAdapterVO.getComAju());
			
            // Tiene errores recuperables
			if (comAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString());
				saveDemodaErrors(request, comAjuVO);				
				request.setAttribute(ComAjuAdapter.NAME, comAjuAdapterVO);
				return mapping.findForward(EfConstants.FWD_COMAJU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (comAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComAjuAdapter.NAME, comAjuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ComAjuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComAjuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ComAjuAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, ComAjuAdapter.NAME);
			
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
			String name = ComAjuAdapter.NAME;
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
			ComAjuAdapter comAjuAdapterVO = (ComAjuAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (comAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			comAjuAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			comAjuAdapterVO = EfServiceLocator.getFiscalizacionService().imprimirComAju(userSession, comAjuAdapterVO);

			// limpia la lista de reports y la lista de tablas
			comAjuAdapterVO.getReport().getListReport().clear();
			comAjuAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (comAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comAjuAdapterVO.infoString());
				saveDemodaErrors(request, comAjuAdapterVO);				
				request.setAttribute(name, comAjuAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (comAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comAjuAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, comAjuAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = comAjuAdapterVO.getReport().getReportFileName();

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
