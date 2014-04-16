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

import ar.gov.rosario.siat.bal.iface.model.ParCueBanAdapter;
import ar.gov.rosario.siat.bal.iface.model.ParCueBanVO;
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

public final class AdministrarParCueBanDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarParCueBanDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARCUEBAN, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ParCueBanAdapter parCueBanAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getParCueBanAdapterForView(userSession, commonKey)";
				parCueBanAdapterVO = BalServiceLocator.getDefinicionService().getParCueBanAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARCUEBAN_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getParCueBanAdapterForUpdate(userSession, commonKey)";
				parCueBanAdapterVO = BalServiceLocator.getDefinicionService().getParCueBanAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARCUEBAN_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getParCueBanAdapterForView(userSession, commonKey)";
				parCueBanAdapterVO = BalServiceLocator.getDefinicionService().getParCueBanAdapterForView(userSession, commonKey);				
				parCueBanAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.PARCUEBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARCUEBAN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getParCueBanAdapterForCreate(userSession, commonKey)";
				parCueBanAdapterVO = BalServiceLocator.getDefinicionService().getParCueBanAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARCUEBAN_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getParCueBanAdapterForView(userSession)";
				parCueBanAdapterVO = BalServiceLocator.getDefinicionService().getParCueBanAdapterForView(userSession, commonKey);
				parCueBanAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.PARCUEBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARCUEBAN_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getParCueBanAdapterForView(userSession)";
				parCueBanAdapterVO = BalServiceLocator.getDefinicionService().getParCueBanAdapterForView(userSession, commonKey);
				parCueBanAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.PARCUEBAN_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARCUEBAN_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (parCueBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + parCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			parCueBanAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ParCueBanAdapter.NAME + ": "+ parCueBanAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ParCueBanAdapter.NAME, parCueBanAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ParCueBanAdapter.NAME, parCueBanAdapterVO);
			 
			saveDemodaMessages(request, parCueBanAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParCueBanAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParCueBanAdapter parCueBanAdapterVO = (ParCueBanAdapter) userSession.get(ParCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParCueBanAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(parCueBanAdapterVO, request);
			
            // Tiene errores recuperables
			if (parCueBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parCueBanAdapterVO.infoString()); 
				saveDemodaErrors(request, parCueBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// llamada al servicio
			ParCueBanVO parCueBanVO = BalServiceLocator.getDefinicionService().createParCueBan(userSession, parCueBanAdapterVO.getParCueBan());
			
            // Tiene errores recuperables
			if (parCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parCueBanVO.infoString()); 
				saveDemodaErrors(request, parCueBanVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (parCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parCueBanVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParCueBanAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParCueBanAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParCueBanAdapter parCueBanAdapterVO = (ParCueBanAdapter) userSession.get(ParCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParCueBanAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(parCueBanAdapterVO, request);
			
            // Tiene errores recuperables
			if (parCueBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parCueBanAdapterVO.infoString()); 
				saveDemodaErrors(request, parCueBanAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// llamada al servicio
			ParCueBanVO parCueBanVO = BalServiceLocator.getDefinicionService().updateParCueBan(userSession, parCueBanAdapterVO.getParCueBan());
			
            // Tiene errores recuperables
			if (parCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parCueBanAdapterVO.infoString()); 
				saveDemodaErrors(request, parCueBanVO);
				return forwardErrorRecoverable(mapping, request, userSession, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (parCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParCueBanAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParCueBanAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARCUEBAN, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ParCueBanAdapter parCueBanAdapterVO = (ParCueBanAdapter) userSession.get(ParCueBanAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (parCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ParCueBanAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ParCueBanAdapter.NAME); 
			}

			// llamada al servicio
			ParCueBanVO parCueBanVO = BalServiceLocator.getDefinicionService().deleteParCueBan(userSession, parCueBanAdapterVO.getParCueBan());
			
            // Tiene errores recuperables
			if (parCueBanVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parCueBanAdapterVO.infoString());
				saveDemodaErrors(request, parCueBanVO);				
				request.setAttribute(ParCueBanAdapter.NAME, parCueBanAdapterVO);
				return mapping.findForward(BalConstants.FWD_PARCUEBAN_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (parCueBanVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parCueBanAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ParCueBanAdapter.NAME, parCueBanAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ParCueBanAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ParCueBanAdapter.NAME);
		}
	}
	
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ParCueBanAdapter.NAME);
		
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
			String name = ParCueBanAdapter.NAME;
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
			ParCueBanAdapter parCueBanAdapterVO = (ParCueBanAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (parCueBanAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  parCueBanAdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			parCueBanAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			parCueBanAdapterVO = BalServiceLocator.getDefinicionService().imprimirParCueBan(userSession, parCueBanAdapterVO);

			// limpia la lista de reports y la lista de tablas
			parCueBanAdapterVO.getReport().getListReport().clear();
			parCueBanAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (parCueBanAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + parCueBanAdapterVO.infoString());
				saveDemodaErrors(request, parCueBanAdapterVO);				
				request.setAttribute(name, parCueBanAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (parCueBanAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + parCueBanAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  parCueBanAdapterVO.NAME,  parCueBanAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, parCueBanAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = parCueBanAdapterVO.getReport().getReportFileName();

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
