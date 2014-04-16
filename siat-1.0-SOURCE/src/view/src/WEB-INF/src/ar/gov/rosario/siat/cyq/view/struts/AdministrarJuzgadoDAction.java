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
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.JuzgadoVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarJuzgadoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarJuzgadoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		JuzgadoAdapter juzgadoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getJuzgadoAdapterForView(userSession, commonKey)";
				juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().getJuzgadoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getJuzgadoAdapterForUpdate(userSession, commonKey)";
				juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().getJuzgadoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_JUZGADO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getJuzgadoAdapterForView(userSession, commonKey)";
				juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().getJuzgadoAdapterForView(userSession, commonKey);				
				juzgadoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CyqError.JUZGADO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getJuzgadoAdapterForCreate(userSession)";
				juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().getJuzgadoAdapterForCreate(userSession);
				actionForward = mapping.findForward(CyqConstants.FWD_JUZGADO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getJuzgadoAdapterForView(userSession)";
				juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().getJuzgadoAdapterForView(userSession, commonKey);
				juzgadoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, CyqError.JUZGADO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getJuzgadoAdapterForView(userSession)";
				juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().getJuzgadoAdapterForView(userSession, commonKey);
				juzgadoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, CyqError.JUZGADO_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (juzgadoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + juzgadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			juzgadoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + JuzgadoAdapter.NAME + ": "+ juzgadoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(JuzgadoAdapter.NAME, juzgadoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(JuzgadoAdapter.NAME, juzgadoAdapterVO);
			 
			saveDemodaMessages(request, juzgadoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			JuzgadoAdapter juzgadoAdapterVO = (JuzgadoAdapter) userSession.get(JuzgadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (juzgadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + JuzgadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, JuzgadoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(juzgadoAdapterVO, request);
			
            // Tiene errores recuperables
			if (juzgadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString()); 
				saveDemodaErrors(request, juzgadoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// llamada al servicio
			JuzgadoVO juzgadoVO = CyqServiceLocator.getDefinicionService().createJuzgado(userSession, juzgadoAdapterVO.getJuzgado());
			
            // Tiene errores recuperables
			if (juzgadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoVO.infoString()); 
				saveDemodaErrors(request, juzgadoVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (juzgadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, JuzgadoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			JuzgadoAdapter juzgadoAdapterVO = (JuzgadoAdapter) userSession.get(JuzgadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (juzgadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + JuzgadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, JuzgadoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(juzgadoAdapterVO, request);
			
            // Tiene errores recuperables
			if (juzgadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString()); 
				saveDemodaErrors(request, juzgadoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// llamada al servicio
			JuzgadoVO juzgadoVO = CyqServiceLocator.getDefinicionService().updateJuzgado(userSession, juzgadoAdapterVO.getJuzgado());
			
            // Tiene errores recuperables
			if (juzgadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString()); 
				saveDemodaErrors(request, juzgadoVO);
				return forwardErrorRecoverable(mapping, request, userSession, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (juzgadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, JuzgadoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			JuzgadoAdapter juzgadoAdapterVO = (JuzgadoAdapter) userSession.get(JuzgadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (juzgadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + JuzgadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, JuzgadoAdapter.NAME); 
			}

			// llamada al servicio
			JuzgadoVO juzgadoVO = CyqServiceLocator.getDefinicionService().deleteJuzgado
				(userSession, juzgadoAdapterVO.getJuzgado());
			
            // Tiene errores recuperables
			if (juzgadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString());
				saveDemodaErrors(request, juzgadoVO);				
				request.setAttribute(JuzgadoAdapter.NAME, juzgadoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (juzgadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, JuzgadoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			JuzgadoAdapter juzgadoAdapterVO = (JuzgadoAdapter) userSession.get(JuzgadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (juzgadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + JuzgadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, JuzgadoAdapter.NAME); 
			}

			// llamada al servicio
			JuzgadoVO juzgadoVO = CyqServiceLocator.getDefinicionService().activarJuzgado
				(userSession, juzgadoAdapterVO.getJuzgado());
			
            // Tiene errores recuperables
			if (juzgadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString());
				saveDemodaErrors(request, juzgadoVO);				
				request.setAttribute(JuzgadoAdapter.NAME, juzgadoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (juzgadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, JuzgadoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_JUZGADO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			JuzgadoAdapter juzgadoAdapterVO = (JuzgadoAdapter) userSession.get(JuzgadoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (juzgadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + JuzgadoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, JuzgadoAdapter.NAME); 
			}

			// llamada al servicio
			JuzgadoVO juzgadoVO = CyqServiceLocator.getDefinicionService().desactivarJuzgado
				(userSession, juzgadoAdapterVO.getJuzgado());
			
            // Tiene errores recuperables
			if (juzgadoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString());
				saveDemodaErrors(request, juzgadoVO);				
				request.setAttribute(JuzgadoAdapter.NAME, juzgadoAdapterVO);
				return mapping.findForward(CyqConstants.FWD_JUZGADO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (juzgadoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, JuzgadoAdapter.NAME, juzgadoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, JuzgadoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, JuzgadoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, JuzgadoAdapter.NAME);
		
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
			String name = JuzgadoAdapter.NAME;
			
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
			JuzgadoAdapter juzgadoAdapterVO = (JuzgadoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (juzgadoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			juzgadoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			juzgadoAdapterVO = CyqServiceLocator.getDefinicionService().imprimirJuzgado(userSession, juzgadoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			juzgadoAdapterVO.getReport().getListReport().clear();
			juzgadoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (juzgadoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + juzgadoAdapterVO.infoString());
				saveDemodaErrors(request, juzgadoAdapterVO);				
				request.setAttribute(name, juzgadoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (juzgadoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + juzgadoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, juzgadoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = juzgadoAdapterVO.getReport().getReportFileName();

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
