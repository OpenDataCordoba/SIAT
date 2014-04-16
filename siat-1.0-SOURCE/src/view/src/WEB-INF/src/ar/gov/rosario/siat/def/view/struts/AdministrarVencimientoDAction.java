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
import ar.gov.rosario.siat.def.iface.model.VencimientoAdapter;
import ar.gov.rosario.siat.def.iface.model.VencimientoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarVencimientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAtributoDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, act); 
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();
			VencimientoAdapter vencimientoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (act.equals(BaseConstants.ACT_VER)) {
					stringServicio = "getVencimientoAdapterForView(userSession, commonKey)";
					vencimientoAdapterVO = DefServiceLocator.getGravamenService().getVencimientoAdapterForView(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);
				}
				if (act.equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getVencimientoAdapterForUpdate(userSession, commonKey)";
					vencimientoAdapterVO = DefServiceLocator.getGravamenService().getVencimientoAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_VENCIMIENTO_EDIT_ADAPTER);
				}
				if (act.equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getVencimientoAdapterForView(userSession, commonKey)";
					vencimientoAdapterVO = DefServiceLocator.getGravamenService().getVencimientoAdapterForView(userSession, commonKey);
					vencimientoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.VENCIMIENTO_LABEL);					
					actionForward = mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getVencimientoAdapterForCreate(userSession)";
					vencimientoAdapterVO = DefServiceLocator.getGravamenService().getVencimientoAdapterForCreate(userSession);
					actionForward = mapping.findForward(DefConstants.FWD_VENCIMIENTO_EDIT_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_ACTIVAR)) {
					stringServicio = "getVencimientoAdapterForView(userSession)";
					vencimientoAdapterVO = DefServiceLocator.getGravamenService().getVencimientoAdapterForView(userSession, commonKey);
					vencimientoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.VENCIMIENTO_LABEL);					
					actionForward = mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);				
				}
				if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
					stringServicio = "getVencimientoAdapterForView(userSession)";
					vencimientoAdapterVO = DefServiceLocator.getGravamenService().getVencimientoAdapterForView(userSession, commonKey);
					vencimientoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.VENCIMIENTO_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);				
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (vencimientoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + vencimientoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoAdapter.NAME, vencimientoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				vencimientoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + VencimientoAdapter.NAME + ": "+ vencimientoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(VencimientoAdapter.NAME, vencimientoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(VencimientoAdapter.NAME, vencimientoAdapterVO);
				 
				saveDemodaMessages(request, vencimientoAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, VencimientoAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			VencimientoAdapter vencimientoAdapterVO = (VencimientoAdapter) userSession.get(VencimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (vencimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + VencimientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, VencimientoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(vencimientoAdapterVO, request);

            // Tiene errores recuperables
			if (vencimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, vencimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// llamada al servicio
			VencimientoVO vencimientoVO = DefServiceLocator.getGravamenService().createVencimiento(userSession, vencimientoAdapterVO.getVencimiento());
			
            // Tiene errores recuperables
			if (vencimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoVO.infoString()); 
				saveDemodaErrors(request, vencimientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (vencimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, VencimientoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, VencimientoAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			VencimientoAdapter vencimientoAdapterVO = (VencimientoAdapter) userSession.get(VencimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (vencimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + VencimientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, VencimientoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(vencimientoAdapterVO, request);
			
            // Tiene errores recuperables
			if (vencimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, vencimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// llamada al servicio
			VencimientoVO vencimientoVO = DefServiceLocator.getGravamenService().updateVencimiento(userSession, vencimientoAdapterVO.getVencimiento());
			
            // Tiene errores recuperables
			if (vencimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, vencimientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (vencimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, VencimientoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, VencimientoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			VencimientoAdapter vencimientoAdapterVO = (VencimientoAdapter) userSession.get(VencimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (vencimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + VencimientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, VencimientoAdapter.NAME); 
			}

			// llamada al servicio
			VencimientoVO vencimientoVO = DefServiceLocator.getGravamenService().deleteVencimiento
				(userSession, vencimientoAdapterVO.getVencimiento());
			
            // Tiene errores recuperables
			if (vencimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString());
				saveDemodaErrors(request, vencimientoVO);				
				request.setAttribute(VencimientoAdapter.NAME, vencimientoAdapterVO);
				return mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (vencimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, VencimientoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, VencimientoAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			VencimientoAdapter vencimientoAdapterVO = (VencimientoAdapter) userSession.get(VencimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (vencimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + VencimientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, VencimientoAdapter.NAME); 
			}

			// llamada al servicio
			VencimientoVO vencimientoVO = DefServiceLocator.getGravamenService().activarVencimiento
				(userSession, vencimientoAdapterVO.getVencimiento());
			
            // Tiene errores recuperables
			if (vencimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString());
				saveDemodaErrors(request, vencimientoVO);				
				request.setAttribute(VencimientoAdapter.NAME, vencimientoAdapterVO);
				return mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (vencimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, VencimientoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, VencimientoAdapter.NAME);
		}	
	}
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_VENCIMIENTO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			VencimientoAdapter vencimientoAdapterVO = (VencimientoAdapter) userSession.get(VencimientoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (vencimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + VencimientoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, VencimientoAdapter.NAME); 
			}

			// llamada al servicio
			VencimientoVO vencimientoVO = DefServiceLocator.getGravamenService().desactivarVencimiento
				(userSession, vencimientoAdapterVO.getVencimiento());
			
            // Tiene errores recuperables
			if (vencimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString());
				saveDemodaErrors(request, vencimientoVO);				
				request.setAttribute(VencimientoAdapter.NAME, vencimientoAdapterVO);
				return mapping.findForward(DefConstants.FWD_VENCIMIENTO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (vencimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, VencimientoAdapter.NAME, vencimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, VencimientoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, VencimientoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, VencimientoAdapter.NAME);
			
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
			String name = VencimientoAdapter.NAME;
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
			VencimientoAdapter vencimientoAdapterVO = (VencimientoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (vencimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			vencimientoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			vencimientoAdapterVO = DefServiceLocator.getGravamenService().imprimirVencimiento(userSession, vencimientoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			vencimientoAdapterVO.getReport().getListReport().clear();
			vencimientoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (vencimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + vencimientoAdapterVO.infoString());
				saveDemodaErrors(request, vencimientoAdapterVO);				
				request.setAttribute(name, vencimientoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (vencimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + vencimientoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, vencimientoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = vencimientoAdapterVO.getReport().getReportFileName();

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
