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

import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorAdapter;
import ar.gov.rosario.siat.bal.iface.model.SaldoAFavorVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSaldoAFavorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSaldoAFavorDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		SaldoAFavorAdapter saldoAFavorAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getSaldoAFavorAdapterForView(userSession, commonKey)";
				saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getSaldoAFavorAdapterForUpdate(userSession, commonKey)";
				saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_SALDOAFAVOR_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getSaldoAFavorAdapterForView(userSession, commonKey)";
				saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorAdapterForView(userSession, commonKey);				
				saldoAFavorAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.SALDOAFAVOR_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getSaldoAFavorAdapterForCreate(userSession)";
				saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_SALDOAFAVOR_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getSaldoAFavorAdapterForView(userSession)";
				saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorAdapterForView(userSession, commonKey);
				saldoAFavorAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.SALDOAFAVOR_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getSaldoAFavorAdapterForView(userSession)";
				saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().getSaldoAFavorAdapterForView(userSession, commonKey);
				saldoAFavorAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.SALDOAFAVOR_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (saldoAFavorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + saldoAFavorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			saldoAFavorAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + SaldoAFavorAdapter.NAME + ": "+ saldoAFavorAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			// Subo el apdater al userSession
			userSession.put(SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			 
			saveDemodaMessages(request, saldoAFavorAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SaldoAFavorAdapter saldoAFavorAdapterVO = (SaldoAFavorAdapter) userSession.get(SaldoAFavorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(saldoAFavorAdapterVO, request);
			
            // Tiene errores recuperables
			if (saldoAFavorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// llamada al servicio
			SaldoAFavorVO saldoAFavorVO = BalServiceLocator.getSaldoAFavorService().createSaldoAFavor(userSession, saldoAFavorAdapterVO.getSaldoAFavor());
			
            // Tiene errores recuperables
			if (saldoAFavorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SaldoAFavorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SaldoAFavorAdapter saldoAFavorAdapterVO = (SaldoAFavorAdapter) userSession.get(SaldoAFavorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(saldoAFavorAdapterVO, request);
			
            // Tiene errores recuperables
			if (saldoAFavorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// llamada al servicio
			SaldoAFavorVO saldoAFavorVO = BalServiceLocator.getSaldoAFavorService().updateSaldoAFavor(userSession, saldoAFavorAdapterVO.getSaldoAFavor());
			
            // Tiene errores recuperables
			if (saldoAFavorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString()); 
				saveDemodaErrors(request, saldoAFavorVO);
				return forwardErrorRecoverable(mapping, request, userSession, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SaldoAFavorAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SaldoAFavorAdapter saldoAFavorAdapterVO = (SaldoAFavorAdapter) userSession.get(SaldoAFavorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorAdapter.NAME); 
			}

			// llamada al servicio
			SaldoAFavorVO saldoAFavorVO = BalServiceLocator.getSaldoAFavorService().deleteSaldoAFavor
				(userSession, saldoAFavorAdapterVO.getSaldoAFavor());
			
            // Tiene errores recuperables
			if (saldoAFavorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString());
				saveDemodaErrors(request, saldoAFavorVO);				
				request.setAttribute(SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
				return mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SaldoAFavorAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SaldoAFavorAdapter saldoAFavorAdapterVO = (SaldoAFavorAdapter) userSession.get(SaldoAFavorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorAdapter.NAME); 
			}

			// llamada al servicio
			SaldoAFavorVO saldoAFavorVO = BalServiceLocator.getSaldoAFavorService().activarSaldoAFavor
				(userSession, saldoAFavorAdapterVO.getSaldoAFavor());
			
            // Tiene errores recuperables
			if (saldoAFavorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString());
				saveDemodaErrors(request, saldoAFavorVO);				
				request.setAttribute(SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
				return mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SaldoAFavorAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}	
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
			SaldoAFavorAdapter adapterVO = (SaldoAFavorAdapter)userSession.get(SaldoAFavorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
				
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getSaldoAFavor()); 
			
			adapterVO.getSaldoAFavor().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(SaldoAFavorAdapter.NAME, adapterVO);
			
			return mapping.findForward(BalConstants.FWD_SALDOAFAVOR_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}	
	}

	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SALDOAFAVOR, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SaldoAFavorAdapter saldoAFavorAdapterVO = (SaldoAFavorAdapter) userSession.get(SaldoAFavorAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + SaldoAFavorAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SaldoAFavorAdapter.NAME); 
			}

			// llamada al servicio
			SaldoAFavorVO saldoAFavorVO = BalServiceLocator.getSaldoAFavorService().desactivarSaldoAFavor
				(userSession, saldoAFavorAdapterVO.getSaldoAFavor());
			
            // Tiene errores recuperables
			if (saldoAFavorVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString());
				saveDemodaErrors(request, saldoAFavorVO);				
				request.setAttribute(SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
				return mapping.findForward(BalConstants.FWD_SALDOAFAVOR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SaldoAFavorAdapter.NAME, saldoAFavorAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, SaldoAFavorAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SaldoAFavorAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SaldoAFavorAdapter.NAME);
		
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
			String name = SaldoAFavorAdapter.NAME;
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
			SaldoAFavorAdapter saldoAFavorAdapterVO = (SaldoAFavorAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (saldoAFavorAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			saldoAFavorAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			saldoAFavorAdapterVO = BalServiceLocator.getSaldoAFavorService().imprimirSaldoAFavor(userSession, saldoAFavorAdapterVO);

			// limpia la lista de reports y la lista de tablas
			saldoAFavorAdapterVO.getReport().getListReport().clear();
			saldoAFavorAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (saldoAFavorAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + saldoAFavorAdapterVO.infoString());
				saveDemodaErrors(request, saldoAFavorAdapterVO);				
				request.setAttribute(name, saldoAFavorAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (saldoAFavorAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + saldoAFavorAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, saldoAFavorAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = saldoAFavorAdapterVO.getReport().getReportFileName();

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
