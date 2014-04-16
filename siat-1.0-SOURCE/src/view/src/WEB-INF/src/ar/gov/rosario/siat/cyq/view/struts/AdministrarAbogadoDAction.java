//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

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
import ar.gov.rosario.siat.cyq.iface.model.AbogadoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarAbogadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarAbogadoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		AbogadoAdapter abogadoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getAbogadoAdapterForView(userSession, commonKey)";
				abogadoAdapterVO = CyqServiceLocator.getDefinicionService().getAbogadoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getAbogadoAdapterForUpdate(userSession, commonKey)";
				abogadoAdapterVO = CyqServiceLocator.getDefinicionService().getAbogadoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_ABOGADO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getAbogadoAdapterForView(userSession, commonKey)";
				abogadoAdapterVO = CyqServiceLocator.getDefinicionService().getAbogadoAdapterForView(userSession, commonKey);				
				abogadoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CyqError.ABOGADO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getAbogadoAdapterForCreate(userSession)";
				abogadoAdapterVO = CyqServiceLocator.getDefinicionService().getAbogadoAdapterForCreate(userSession);
				actionForward = mapping.findForward(CyqConstants.FWD_ABOGADO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAbogadoAdapterForView(userSession)";
				abogadoAdapterVO = CyqServiceLocator.getDefinicionService().getAbogadoAdapterForView(userSession, commonKey);
				abogadoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, CyqError.ABOGADO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAbogadoAdapterForView(userSession)";
				abogadoAdapterVO = CyqServiceLocator.getDefinicionService().getAbogadoAdapterForView(userSession, commonKey);
				abogadoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, CyqError.ABOGADO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (abogadoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + abogadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			abogadoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + AbogadoAdapter.NAME + ": "+ abogadoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(AbogadoAdapter.NAME, abogadoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(AbogadoAdapter.NAME, abogadoAdapterVO);
			 
			saveDemodaMessages(request, abogadoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AbogadoAdapter abogadoAdapterVO = (AbogadoAdapter) userSession.get(AbogadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (abogadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AbogadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AbogadoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(abogadoAdapterVO, request);
			
            // Tiene errores recuperables
			if (abogadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString()); 
				saveDemodaErrors(request, abogadoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// llamada al servicio
			AbogadoVO abogadoVO = CyqServiceLocator.getDefinicionService().createAbogado(userSession, abogadoAdapterVO.getAbogado());
			
            // Tiene errores recuperables
			if (abogadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoVO.infoString()); 
				saveDemodaErrors(request, abogadoVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (abogadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AbogadoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AbogadoAdapter abogadoAdapterVO = (AbogadoAdapter) userSession.get(AbogadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (abogadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AbogadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AbogadoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(abogadoAdapterVO, request);
			
            // Tiene errores recuperables
			if (abogadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString()); 
				saveDemodaErrors(request, abogadoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// llamada al servicio
			AbogadoVO abogadoVO = CyqServiceLocator.getDefinicionService().updateAbogado(userSession, abogadoAdapterVO.getAbogado());
			
            // Tiene errores recuperables
			if (abogadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString()); 
				saveDemodaErrors(request, abogadoVO);
				return forwardErrorRecoverable(mapping, request, userSession, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (abogadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AbogadoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AbogadoAdapter abogadoAdapterVO = (AbogadoAdapter) userSession.get(AbogadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (abogadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AbogadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AbogadoAdapter.NAME); 
			}

			// llamada al servicio
			AbogadoVO abogadoVO = CyqServiceLocator.getDefinicionService().deleteAbogado
				(userSession, abogadoAdapterVO.getAbogado());
			
            // Tiene errores recuperables
			if (abogadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString());
				saveDemodaErrors(request, abogadoVO);				
				request.setAttribute(AbogadoAdapter.NAME, abogadoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (abogadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AbogadoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AbogadoAdapter abogadoAdapterVO = (AbogadoAdapter) userSession.get(AbogadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (abogadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AbogadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AbogadoAdapter.NAME); 
			}

			// llamada al servicio
			AbogadoVO abogadoVO = CyqServiceLocator.getDefinicionService().activarAbogado
				(userSession, abogadoAdapterVO.getAbogado());
			
            // Tiene errores recuperables
			if (abogadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString());
				saveDemodaErrors(request, abogadoVO);				
				request.setAttribute(AbogadoAdapter.NAME, abogadoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (abogadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AbogadoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_ABOGADO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			AbogadoAdapter abogadoAdapterVO = (AbogadoAdapter) userSession.get(AbogadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (abogadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + AbogadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AbogadoAdapter.NAME); 
			}

			// llamada al servicio
			AbogadoVO abogadoVO = CyqServiceLocator.getDefinicionService().desactivarAbogado
				(userSession, abogadoAdapterVO.getAbogado());
			
            // Tiene errores recuperables
			if (abogadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString());
				saveDemodaErrors(request, abogadoVO);				
				request.setAttribute(AbogadoAdapter.NAME, abogadoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_ABOGADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (abogadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AbogadoAdapter.NAME, abogadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, AbogadoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AbogadoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AbogadoAdapter.NAME);
		
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
			String name = AbogadoAdapter.NAME;
			
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
			AbogadoAdapter abogadoAdapterVO = (AbogadoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (abogadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			abogadoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			abogadoAdapterVO = CyqServiceLocator.getDefinicionService().imprimirAbogado(userSession, abogadoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			abogadoAdapterVO.getReport().getListReport().clear();
			abogadoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (abogadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + abogadoAdapterVO.infoString());
				saveDemodaErrors(request, abogadoAdapterVO);				
				request.setAttribute(name, abogadoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (abogadoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + abogadoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, abogadoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = abogadoAdapterVO.getReport().getReportFileName();

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
