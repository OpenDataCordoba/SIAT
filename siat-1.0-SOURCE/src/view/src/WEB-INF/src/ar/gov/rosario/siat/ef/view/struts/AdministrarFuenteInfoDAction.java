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
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoAdapter;
import ar.gov.rosario.siat.ef.iface.model.FuenteInfoVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarFuenteInfoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFuenteInfoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		FuenteInfoAdapter fuenteInfoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getFuenteInfoAdapterForView(userSession, commonKey)";
				fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().getFuenteInfoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getFuenteInfoAdapterForUpdate(userSession, commonKey)";
				fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().getFuenteInfoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_FUENTEINFO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getFuenteInfoAdapterForView(userSession, commonKey)";
				fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().getFuenteInfoAdapterForView(userSession, commonKey);				
				fuenteInfoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.FUENTEINFO_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getFuenteInfoAdapterForCreate(userSession)";
				fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().getFuenteInfoAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_FUENTEINFO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getFuenteInfoAdapterForView(userSession)";
				fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().getFuenteInfoAdapterForView(userSession, commonKey);
				fuenteInfoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EfError.FUENTEINFO_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getFuenteInfoAdapterForView(userSession)";
				fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().getFuenteInfoAdapterForView(userSession, commonKey);
				fuenteInfoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EfError.FUENTEINFO_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (fuenteInfoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + fuenteInfoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			fuenteInfoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + FuenteInfoAdapter.NAME + ": "+ fuenteInfoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			 
			saveDemodaMessages(request, fuenteInfoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FuenteInfoAdapter fuenteInfoAdapterVO = (FuenteInfoAdapter) userSession.get(FuenteInfoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FuenteInfoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(fuenteInfoAdapterVO, request);
			
            // Tiene errores recuperables
			if (fuenteInfoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// llamada al servicio
			FuenteInfoVO fuenteInfoVO = EfServiceLocator.getDefinicionService().createFuenteInfo(userSession, fuenteInfoAdapterVO.getFuenteInfo());
			
            // Tiene errores recuperables
			if (fuenteInfoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FuenteInfoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FuenteInfoAdapter fuenteInfoAdapterVO = (FuenteInfoAdapter) userSession.get(FuenteInfoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FuenteInfoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(fuenteInfoAdapterVO, request);
			
            // Tiene errores recuperables
			if (fuenteInfoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// llamada al servicio
			FuenteInfoVO fuenteInfoVO = EfServiceLocator.getDefinicionService().updateFuenteInfo(userSession, fuenteInfoAdapterVO.getFuenteInfo());
			
            // Tiene errores recuperables
			if (fuenteInfoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString()); 
				saveDemodaErrors(request, fuenteInfoVO);
				return forwardErrorRecoverable(mapping, request, userSession, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FuenteInfoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FuenteInfoAdapter fuenteInfoAdapterVO = (FuenteInfoAdapter) userSession.get(FuenteInfoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FuenteInfoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoAdapter.NAME); 
			}

			// llamada al servicio
			FuenteInfoVO fuenteInfoVO = EfServiceLocator.getDefinicionService().deleteFuenteInfo
				(userSession, fuenteInfoAdapterVO.getFuenteInfo());
			
            // Tiene errores recuperables
			if (fuenteInfoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString());
				saveDemodaErrors(request, fuenteInfoVO);				
				request.setAttribute(FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
				return mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FuenteInfoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FuenteInfoAdapter fuenteInfoAdapterVO = (FuenteInfoAdapter) userSession.get(FuenteInfoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FuenteInfoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoAdapter.NAME); 
			}

			// llamada al servicio
			FuenteInfoVO fuenteInfoVO = EfServiceLocator.getDefinicionService().activarFuenteInfo
				(userSession, fuenteInfoAdapterVO.getFuenteInfo());
			
            // Tiene errores recuperables
			if (fuenteInfoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString());
				saveDemodaErrors(request, fuenteInfoVO);				
				request.setAttribute(FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
				return mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FuenteInfoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_FUENTEINFO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FuenteInfoAdapter fuenteInfoAdapterVO = (FuenteInfoAdapter) userSession.get(FuenteInfoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FuenteInfoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoAdapter.NAME); 
			}

			// llamada al servicio
			FuenteInfoVO fuenteInfoVO = EfServiceLocator.getDefinicionService().desactivarFuenteInfo(userSession, fuenteInfoAdapterVO.getFuenteInfo());
			
            // Tiene errores recuperables
			if (fuenteInfoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString());
				saveDemodaErrors(request, fuenteInfoVO);				
				request.setAttribute(FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
				return mapping.findForward(EfConstants.FWD_FUENTEINFO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, fuenteInfoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FuenteInfoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FuenteInfoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FuenteInfoAdapter.NAME);
		
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
			String name = FuenteInfoAdapter.NAME;
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
			FuenteInfoAdapter fuenteInfoAdapterVO = (FuenteInfoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (fuenteInfoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, FuenteInfoAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			fuenteInfoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			fuenteInfoAdapterVO = EfServiceLocator.getDefinicionService().imprimirFuenteInfo(userSession, fuenteInfoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			fuenteInfoAdapterVO.getReport().getListReport().clear();
			fuenteInfoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (fuenteInfoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fuenteInfoAdapterVO.infoString());
				saveDemodaErrors(request, fuenteInfoAdapterVO);				
				request.setAttribute(name, fuenteInfoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (fuenteInfoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fuenteInfoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, FuenteInfoAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, fuenteInfoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = fuenteInfoAdapterVO.getReport().getReportFileName();

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
