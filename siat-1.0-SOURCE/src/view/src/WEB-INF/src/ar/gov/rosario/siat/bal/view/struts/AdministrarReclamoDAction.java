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

import ar.gov.rosario.siat.bal.iface.model.ReclamoAdapter;
import ar.gov.rosario.siat.bal.iface.model.ReclamoVO;
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

public final class AdministrarReclamoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarReclamoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_RECLAMO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ReclamoAdapter reclamoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getReclamoAdapterForView(userSession, commonKey)";
				reclamoAdapterVO = BalServiceLocator.getReclamoService().getReclamoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_RECLAMO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getReclamoAdapterForUpdate(userSession, commonKey)";
				
				String paramMasDatos = (String) userSession.get(ReclamoAdapter.PARAM_MAS_DATOS);
				
				reclamoAdapterVO = BalServiceLocator.getReclamoService().getReclamoAdapterForUpdate(userSession, commonKey);

				if(paramMasDatos != null && "true".equals(paramMasDatos)){
					userSession.remove(ReclamoAdapter.PARAM_MAS_DATOS);
					reclamoAdapterVO = BalServiceLocator.getReclamoService().buscarMasDatosDelReclamo(userSession, reclamoAdapterVO);
				}
				
				actionForward = mapping.findForward(BalConstants.FWD_RECLAMO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getReclamoAdapterForView(userSession, commonKey)";
				//reclamoAdapterVO = BalServiceLocator.getReclamoService().getReclamoAdapterForView(userSession, commonKey);				
				reclamoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.RECLAMO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_RECLAMO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getReclamoAdapterForCreate(userSession,commonKey)";
				//reclamoAdapterVO = BalServiceLocator.getReclamoService().getReclamoAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_RECLAMO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getReclamoAdapterForView(userSession)";
				//reclamoAdapterVO = BalServiceLocator.getReclamoService().getReclamoAdapterForView(userSession, commonKey);
				reclamoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.RECLAMO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_RECLAMO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getReclamoAdapterForView(userSession)";
				//reclamoAdapterVO = BalServiceLocator.getReclamoService().getReclamoAdapterForView(userSession, commonKey);
				reclamoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.RECLAMO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_RECLAMO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (reclamoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + reclamoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReclamoAdapter.NAME, reclamoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			reclamoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ReclamoAdapter.NAME + ": "+ reclamoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ReclamoAdapter.NAME, reclamoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ReclamoAdapter.NAME, reclamoAdapterVO);
			 
			saveDemodaMessages(request, reclamoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReclamoAdapter.NAME);
		}
	}
	

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_RECLAMO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ReclamoAdapter reclamoAdapterVO = (ReclamoAdapter) userSession.get(ReclamoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (reclamoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ReclamoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ReclamoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(reclamoAdapterVO, request);
			log.debug("enviar Mail: " + request.getParameter("enviarMail"));
			reclamoAdapterVO.getReclamo().setEnviarMail("on".equalsIgnoreCase(request.getParameter("enviarMail")));
			
            // Tiene errores recuperables
			if (reclamoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString()); 
				saveDemodaErrors(request, reclamoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReclamoAdapter.NAME, reclamoAdapterVO);
			}
			
			// llamada al servicio
			
			 ReclamoVO reclamoVO = BalServiceLocator.getReclamoService().updateReclamo(userSession, reclamoAdapterVO.getReclamo());
			
            // Tiene errores recuperables
			if (reclamoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString()); 
				saveDemodaErrors(request, reclamoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ReclamoAdapter.NAME, reclamoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (reclamoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reclamoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReclamoAdapter.NAME, reclamoAdapterVO);
			}
				
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ReclamoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReclamoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ReclamoAdapter.NAME);
		
	}
	
	public ActionForward paramTipoBoleta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ReclamoAdapter reclamoAdapterVO = (ReclamoAdapter) userSession.get(ReclamoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (reclamoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ReclamoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ReclamoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(reclamoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (reclamoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString()); 
					saveDemodaErrors(request, reclamoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ReclamoAdapter.NAME, reclamoAdapterVO);
				}
						
				// llamada al servicio
				// Envio el VO al request
				request.setAttribute(ReclamoAdapter.NAME, reclamoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ReclamoAdapter.NAME, reclamoAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_RECLAMO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ReclamoAdapter.NAME);
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
			String name = ReclamoAdapter.NAME;
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
			ReclamoAdapter reclamoAdapterVO = (ReclamoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (reclamoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			reclamoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			reclamoAdapterVO = BalServiceLocator.getReclamoService().imprimirReclamo(userSession, reclamoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			reclamoAdapterVO.getReport().getListReport().clear();
			reclamoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (reclamoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString());
				saveDemodaErrors(request, reclamoAdapterVO);				
				request.setAttribute(name, reclamoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (reclamoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + reclamoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, reclamoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = reclamoAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward paramEstadoReclamo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ReclamoAdapter reclamoAdapterVO = (ReclamoAdapter) userSession.get(ReclamoAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (reclamoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ReclamoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ReclamoAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(reclamoAdapterVO, request);
				
	            // Tiene errores recuperables
				if (reclamoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString()); 
					saveDemodaErrors(request, reclamoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ReclamoAdapter.NAME, reclamoAdapterVO);
				}
				
				// llamada al servicio
				reclamoAdapterVO = BalServiceLocator.getReclamoService().getParamEstadoReclamo(userSession, reclamoAdapterVO);
				
	            // Tiene errores recuperables
				if (reclamoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString()); 
					saveDemodaErrors(request, reclamoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ReclamoAdapter.NAME, reclamoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (reclamoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + reclamoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ReclamoAdapter.NAME, reclamoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ReclamoAdapter.NAME, reclamoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(ReclamoAdapter.NAME, reclamoAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_RECLAMO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ReclamoAdapter.NAME);
			}
		}

	public ActionForward buscarMasDatos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ReclamoAdapter reclamoAdapterVO = (ReclamoAdapter) userSession.get(ReclamoAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (reclamoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ReclamoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ReclamoAdapter.NAME); 
				}
				
				// llamada al servicio
				reclamoAdapterVO = BalServiceLocator.getReclamoService().buscarMasDatosDelReclamo(userSession, reclamoAdapterVO);
				
	            // Tiene errores recuperables
				if (reclamoAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + reclamoAdapterVO.infoString()); 
					saveDemodaErrors(request, reclamoAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ReclamoAdapter.NAME, reclamoAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (reclamoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + reclamoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ReclamoAdapter.NAME, reclamoAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ReclamoAdapter.NAME, reclamoAdapterVO);
				// Subo el adapter al userSession
				userSession.put(ReclamoAdapter.NAME, reclamoAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_RECLAMO_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ReclamoAdapter.NAME);
			}
		}

		public ActionForward modificarIndet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);
			userSession.put(ReclamoAdapter.PARAM_MAS_DATOS, "true");
		
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET, "modificar");
		}
		
		public ActionForward verIndet(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
		
			UserSession userSession = getCurrentUserSession(request, mapping);
			userSession.put(ReclamoAdapter.PARAM_MAS_DATOS, "true");

			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET, "ver");
		}
		
		public ActionForward buscarIndet(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();

			UserSession userSession = getCurrentUserSession(request, mapping);
			userSession.put(ReclamoAdapter.PARAM_MAS_DATOS, "true");
			
			return baseForwardAdapter(mapping, request, funcName, BalConstants.ACTION_BUSCAR_INDET, "buscar");
		}
		
		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, ReclamoAdapter.NAME);
				
		}

}
