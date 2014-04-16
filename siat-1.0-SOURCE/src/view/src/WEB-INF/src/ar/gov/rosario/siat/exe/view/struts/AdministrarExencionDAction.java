//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.exe.iface.model.ExencionAdapter;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarExencionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarExencionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXENCION, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ExencionAdapter exencionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getExencionAdapterForView(userSession, commonKey)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getExencionAdapterForUpdate(userSession, commonKey)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getExencionAdapterForDelete(userSession, commonKey)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForView
					(userSession, commonKey);
				exencionAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.EXENCION_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getExencionAdapterForView(userSession)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForView
					(userSession, commonKey);
				exencionAdapterVO.addMessage(BaseError.MSG_ACTIVAR, ExeError.EXENCION_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getExencionAdapterForView(userSession)";
				exencionAdapterVO = ExeServiceLocator.getDefinicionService().getExencionAdapterForView
					(userSession, commonKey);
				exencionAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, ExeError.EXENCION_LABEL);			
				actionForward = mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (exencionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.NAME, exencionAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			exencionAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ExencionAdapter.NAME + ": " + exencionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ExencionAdapter.NAME, exencionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ExencionAdapter.NAME, exencionAdapterVO);
			
			saveDemodaMessages(request, exencionAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			ExeConstants.ACTION_ADMINISTRAR_ENC_EXENCION, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXENCION, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(ExencionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.NAME); 
			}

			// llamada al servicio
			ExencionVO exencionVO = ExeServiceLocator.getDefinicionService().deleteExencion
				(userSession, exencionAdapterVO.getExencion());
			
            // Tiene errores recuperables
			if (exencionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString());
				saveDemodaErrors(request, exencionVO);				
				request.setAttribute(ExencionAdapter.NAME, exencionAdapterVO);
				return mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (exencionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.NAME, exencionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExencionAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXENCION, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(ExencionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.NAME); 
			}

			// llamada al servicio
			ExencionVO exencionVO = ExeServiceLocator.getDefinicionService().activarExencion
				(userSession, exencionAdapterVO.getExencion());
			
            // Tiene errores recuperables
			if (exencionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString());
				saveDemodaErrors(request, exencionVO);				
				request.setAttribute(ExencionAdapter.NAME, exencionAdapterVO);
				return mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (exencionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.NAME, exencionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExencionAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_EXENCION, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(ExencionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ExencionAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ExencionAdapter.NAME); 
			}

			// llamada al servicio
			ExencionVO exencionVO = ExeServiceLocator.getDefinicionService().desactivarExencion
				(userSession, exencionAdapterVO.getExencion());
			
            // Tiene errores recuperables
			if (exencionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString());
				saveDemodaErrors(request, exencionVO);				
				request.setAttribute(ExencionAdapter.NAME, exencionAdapterVO);
				return mapping.findForward(ExeConstants.FWD_EXENCION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (exencionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ExencionAdapter.NAME, exencionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ExencionAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ExencionAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ExencionAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ExencionAdapter.NAME);
		
	}
	
	// Metodos relacionados ExeRecCon
	public ActionForward verExeRecCon(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXERECCON);

	}

	public ActionForward modificarExeRecCon(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXERECCON);

	}

	public ActionForward eliminarExeRecCon(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXERECCON);

	}
	
	public ActionForward agregarExeRecCon(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_EXERECCON);
		
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
			String name = ExencionAdapter.NAME;
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
			ExencionAdapter exencionAdapterVO = (ExencionAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (exencionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			exencionAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			exencionAdapterVO = ExeServiceLocator.getDefinicionService().imprimirExencion(userSession, exencionAdapterVO);

			// limpia la lista de reports y la lista de tablas
			exencionAdapterVO.getReport().getListReport().clear();
			exencionAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (exencionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + exencionAdapterVO.infoString());
				saveDemodaErrors(request, exencionAdapterVO);				
				request.setAttribute(name, exencionAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (exencionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + exencionAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, exencionAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = exencionAdapterVO.getReport().getReportFileName();

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
	
