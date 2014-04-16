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

import ar.gov.rosario.siat.bal.iface.model.AuxCaja7Adapter;
import ar.gov.rosario.siat.bal.iface.model.AuxCaja7VO;
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

public final class AdministrarAuxCaja7DAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAuxCaja7DAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AuxCaja7Adapter auxCaja7AdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAuxCaja7AdapterForView(userSession, commonKey)";
				auxCaja7AdapterVO = BalServiceLocator.getBalanceService().getAuxCaja7AdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_AUXCAJA7_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAuxCaja7AdapterForUpdate(userSession, commonKey)";
				auxCaja7AdapterVO = BalServiceLocator.getBalanceService().getAuxCaja7AdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_AUXCAJA7_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAuxCaja7AdapterForView(userSession, commonKey)";
				auxCaja7AdapterVO = BalServiceLocator.getBalanceService().getAuxCaja7AdapterForView(userSession, commonKey);				
				auxCaja7AdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.AUXCAJA7_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_AUXCAJA7_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAuxCaja7AdapterForCreate(userSession)";
				auxCaja7AdapterVO = BalServiceLocator.getBalanceService().getAuxCaja7AdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_AUXCAJA7_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAuxCaja7AdapterForView(userSession, commonKey)";
				auxCaja7AdapterVO = BalServiceLocator.getBalanceService().getAuxCaja7AdapterForView(userSession, commonKey);
				auxCaja7AdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.AUXCAJA7_LABEL);					
				actionForward = mapping.findForward(BalConstants.FWD_AUXCAJA7_VIEW_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (auxCaja7AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + auxCaja7AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			auxCaja7AdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AuxCaja7Adapter.NAME + ": "+ auxCaja7AdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			// Subo el apdater al userSession
			userSession.put(AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			 
			saveDemodaMessages(request, auxCaja7AdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7Adapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AuxCaja7Adapter auxCaja7AdapterVO = (AuxCaja7Adapter) userSession.get(AuxCaja7Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (auxCaja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AuxCaja7Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AuxCaja7Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(auxCaja7AdapterVO, request);
			
            // Tiene errores recuperables
			if (auxCaja7AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7AdapterVO.infoString()); 
				saveDemodaErrors(request, auxCaja7AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// llamada al servicio
			AuxCaja7VO auxCaja7VO = BalServiceLocator.getBalanceService().createAuxCaja7(userSession, auxCaja7AdapterVO.getAuxCaja7());
			
            // Tiene errores recuperables
			if (auxCaja7VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7VO.infoString()); 
				saveDemodaErrors(request, auxCaja7VO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (auxCaja7VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7VO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AuxCaja7Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7Adapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AuxCaja7Adapter auxCaja7AdapterVO = (AuxCaja7Adapter) userSession.get(AuxCaja7Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (auxCaja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AuxCaja7Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AuxCaja7Adapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(auxCaja7AdapterVO, request);
			
            // Tiene errores recuperables
			if (auxCaja7AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7AdapterVO.infoString()); 
				saveDemodaErrors(request, auxCaja7AdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// llamada al servicio
			AuxCaja7VO auxCaja7VO = BalServiceLocator.getBalanceService().updateAuxCaja7(userSession, auxCaja7AdapterVO.getAuxCaja7());
			
            // Tiene errores recuperables
			if (auxCaja7VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7AdapterVO.infoString()); 
				saveDemodaErrors(request, auxCaja7VO);
				return forwardErrorRecoverable(mapping, request, userSession, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// Tiene errores no recuperables
			if (auxCaja7VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AuxCaja7Adapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7Adapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AuxCaja7Adapter auxCaja7AdapterVO = (AuxCaja7Adapter) userSession.get(AuxCaja7Adapter.NAME);
			
			// Si es nulo no se puede continuar
			if (auxCaja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AuxCaja7Adapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AuxCaja7Adapter.NAME); 
			}

			// llamada al servicio
			AuxCaja7VO auxCaja7VO = BalServiceLocator.getBalanceService().deleteAuxCaja7
				(userSession, auxCaja7AdapterVO.getAuxCaja7());
			
            // Tiene errores recuperables
			if (auxCaja7VO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7AdapterVO.infoString());
				saveDemodaErrors(request, auxCaja7VO);				
				request.setAttribute(AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
				return mapping.findForward(BalConstants.FWD_AUXCAJA7_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (auxCaja7VO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7AdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AuxCaja7Adapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AuxCaja7Adapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AuxCaja7Adapter.NAME);
		
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
			String name = AuxCaja7Adapter.NAME;
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
			AuxCaja7Adapter auxCaja7AdapterVO = (AuxCaja7Adapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (auxCaja7AdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  auxCaja7AdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			auxCaja7AdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			auxCaja7AdapterVO = BalServiceLocator.getBalanceService().imprimirAuxCaja7(userSession, auxCaja7AdapterVO);

			// limpia la lista de reports y la lista de tablas
			auxCaja7AdapterVO.getReport().getListReport().clear();
			auxCaja7AdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (auxCaja7AdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + auxCaja7AdapterVO.infoString());
				saveDemodaErrors(request, auxCaja7AdapterVO);				
				request.setAttribute(name, auxCaja7AdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (auxCaja7AdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + auxCaja7AdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  auxCaja7AdapterVO.NAME,  auxCaja7AdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, auxCaja7AdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = auxCaja7AdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}         
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_AUXCAJA7, 
				BaseSecurityConstants.ACTIVAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				AuxCaja7Adapter auxCaja7AdapterVO = (AuxCaja7Adapter) userSession.get(AuxCaja7Adapter.NAME);
				
				// Si es nulo no se puede continuar
				if (auxCaja7AdapterVO == null) {
					log.error("error en: "  + funcName + ": " + AuxCaja7Adapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, AuxCaja7Adapter.NAME); 
				}
				
				// llamada al servicio
				AuxCaja7VO auxCaja7VO = BalServiceLocator.getBalanceService().activarAuxCaja7(userSession, auxCaja7AdapterVO.getAuxCaja7());
				
	            // Tiene errores recuperables
				if (auxCaja7VO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + auxCaja7AdapterVO.infoString());
					saveDemodaErrors(request, auxCaja7VO);				
					request.setAttribute(AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
					return mapping.findForward(BalConstants.FWD_AUXCAJA7_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (auxCaja7VO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + auxCaja7AdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, AuxCaja7Adapter.NAME, auxCaja7AdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, AuxCaja7Adapter.NAME);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, AuxCaja7Adapter.NAME);
			}	
	}
}
