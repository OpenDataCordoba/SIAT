//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

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
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiAdapter;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiVO;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCatRSDreiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCatRSDreiDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CatRSDreiAdapter catRsDreiAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCatRSDreiAdapterForView(userSession, commonKey)";
				catRsDreiAdapterVO = RecServiceLocator.getDreiService().getCatRSDreiAdapterForView(userSession, commonKey.getId());
				actionForward = mapping.findForward(RecConstants.FWD_CATRSDREI_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCatRSDreiAdapterForUpdate(userSession, commonKey)";
				catRsDreiAdapterVO = RecServiceLocator.getDreiService().getCatRSDreiAdapterForUpdate(userSession, commonKey.getId());
				actionForward = mapping.findForward(RecConstants.FWD_CATRSDREI_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCatRSDreiAdapterForView(userSession, commonKey)";
				catRsDreiAdapterVO = RecServiceLocator.getDreiService().getCatRSDreiAdapterForView(userSession, commonKey.getId());				
				catRsDreiAdapterVO.addMessage(BaseError.MSG_ELIMINAR, RecError.CATRSDREI_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_CATRSDREI_VIEW_ADAPTER);			
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				
				stringServicio = "getCatRSDreiAdapterForCreate(userSession)";
				catRsDreiAdapterVO = RecServiceLocator.getDreiService().getCatRSDreiAdapterForCreate(userSession);
				actionForward = mapping.findForward(RecConstants.FWD_CATRSDREI_EDIT_ADAPTER);	
						
			}

			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCatRSDreiAdapterForView(userSession)";
				catRsDreiAdapterVO = RecServiceLocator.getDreiService().getCatRSDreiAdapterForView(userSession, commonKey.getId());
				catRsDreiAdapterVO.addMessage(BaseError.MSG_ACTIVAR, RecError.CATRSDREI_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_CATRSDREI_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCatRSDreiAdapterForView(userSession)";
				catRsDreiAdapterVO = RecServiceLocator.getDreiService().getCatRSDreiAdapterForView(userSession, commonKey.getId());
				catRsDreiAdapterVO.addMessage(BaseError.MSG_DESACTIVAR,RecError.CATRSDREI_LABEL);
				actionForward = mapping.findForward(RecConstants.FWD_CATRSDREI_VIEW_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
						
			// Tiene errores no recuperables
			if (catRsDreiAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + catRsDreiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			catRsDreiAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CatRSDreiAdapter.NAME + ": "+ catRsDreiAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			 
			saveDemodaMessages(request, catRsDreiAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CatRSDreiAdapter catRsDreiAdapterVO = (CatRSDreiAdapter) userSession.get(CatRSDreiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (catRsDreiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(catRsDreiAdapterVO, request);
			
            // Tiene errores recuperables
			if (catRsDreiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRsDreiAdapterVO.infoString()); 
				saveDemodaErrors(request, catRsDreiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// llamada al servicio
			CatRSDreiVO catRsDreiVO = RecServiceLocator.getDreiService().createCatRSDrei(userSession, catRsDreiAdapterVO.getCatRSDrei());
			
            // Tiene errores recuperables
			if (catRsDreiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRsDreiVO.infoString()); 
				saveDemodaErrors(request, catRsDreiVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (catRsDreiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRsDreiVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CatRSDreiAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CatRSDreiAdapter catRsDreiAdapterVO = (CatRSDreiAdapter) userSession.get(CatRSDreiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (catRsDreiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(catRsDreiAdapterVO, request);
			
            // Tiene errores recuperables
			if (catRsDreiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRsDreiAdapterVO.infoString()); 
				saveDemodaErrors(request, catRsDreiAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// llamada al servicio
			CatRSDreiVO catRsDreiVO = RecServiceLocator.getDreiService().updateCatRSDrei(userSession, catRsDreiAdapterVO.getCatRSDrei());
			
            // Tiene errores recuperables
			if (catRsDreiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRsDreiAdapterVO.infoString()); 
				saveDemodaErrors(request, catRsDreiVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (catRsDreiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRsDreiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRsDreiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CatRSDreiAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CatRSDreiAdapter catRsDreiAdapterVO = (CatRSDreiAdapter) userSession.get(CatRSDreiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (catRsDreiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// llamada al servicio
			CatRSDreiVO catRSDreiVO = RecServiceLocator.getDreiService().deleteCatRSDrei(userSession, catRsDreiAdapterVO.getCatRSDrei());
			// llamada al servicio
			
							
            // Tiene errores recuperables
			if (catRSDreiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiVO.infoString());
				saveDemodaErrors(request, catRSDreiVO);				
				request.setAttribute(CatRSDreiAdapter.NAME, catRSDreiVO);
				return mapping.findForward(RecConstants.FWD_CATRSDREI_EDIT_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (catRSDreiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRSDreiVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRSDreiVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CatRSDreiAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CatRSDreiAdapter catRSDreiAdapterVO = (CatRSDreiAdapter) userSession.get(CatRSDreiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (catRSDreiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// llamada al servicio
			CatRSDreiVO catRSDreiVO = RecServiceLocator.getDreiService().activarCatRSDrei
				(userSession, catRSDreiAdapterVO.getCatRSDrei());
			
            // Tiene errores recuperables
			if (catRSDreiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiAdapterVO.infoString());
				saveDemodaErrors(request, catRSDreiVO);				
				request.setAttribute(CatRSDreiAdapter.NAME, catRSDreiAdapterVO);
				return mapping.findForward(RecConstants.FWD_CATRSDREI_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (catRSDreiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRSDreiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRSDreiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CatRSDreiAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CatRSDreiAdapter catRSDreiAdapterVO = (CatRSDreiAdapter) userSession.get(CatRSDreiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (catRSDreiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// llamada al servicio
			CatRSDreiVO catRSDreiVO = RecServiceLocator.getDreiService().desactivarCatRSDrei
				(userSession, catRSDreiAdapterVO.getCatRSDrei());
			
            // Tiene errores recuperables
			if (catRSDreiVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiAdapterVO.infoString());
				saveDemodaErrors(request, catRSDreiVO);				
				request.setAttribute(CatRSDreiAdapter.NAME, catRSDreiAdapterVO);
				return mapping.findForward(RecConstants.FWD_CATRSDREI_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (catRSDreiVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRSDreiAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRSDreiAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CatRSDreiAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CatRSDreiAdapter.NAME);
		
	}
	
	public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CatRSDreiAdapter catRsDreiAdapter = (CatRSDreiAdapter) userSession.get(CatRSDreiAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (catRsDreiAdapter == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(catRsDreiAdapter, request);
			
            // Tiene errores recuperables
			if (catRsDreiAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRsDreiAdapter.infoString()); 
				saveDemodaErrors(request, catRsDreiAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiAdapter.NAME, catRsDreiAdapter);
			}
			
			// llamada al servicio
//			catRsDreiAdapter = RecServiceLocator.getDreiService().getCatRSDreiAdapterParam(userSession, catRsDreiAdapter);
			
            // Tiene errores recuperables
			if (catRsDreiAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRsDreiAdapter.infoString()); 
				saveDemodaErrors(request, catRsDreiAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiAdapter.NAME, catRsDreiAdapter);
			}
			
			// Tiene errores no recuperables
			if (catRsDreiAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRsDreiAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiAdapter.NAME, catRsDreiAdapter);
			}
			
			// Envio el VO al request
			request.setAttribute(CatRSDreiAdapter.NAME, catRsDreiAdapter);
			// Subo el apdater al userSession
			userSession.put(CatRSDreiAdapter.NAME, catRsDreiAdapter);
			
			return mapping.findForward(RecConstants.FWD_CATRSDREI_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiAdapter.NAME);
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
			String name = CatRSDreiAdapter.NAME;
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
			CatRSDreiAdapter catRSDreiAdapterVO = (CatRSDreiAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (catRSDreiAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			catRSDreiAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			catRSDreiAdapterVO = RecServiceLocator.getDreiService().imprimirCatRSDrei(userSession, catRSDreiAdapterVO);

			// limpia la lista de reports y la lista de tablas
			catRSDreiAdapterVO.getReport().getListReport().clear();
			catRSDreiAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (catRSDreiAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiAdapterVO.infoString());
				saveDemodaErrors(request, catRSDreiAdapterVO);				
				request.setAttribute(name, catRSDreiAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (catRSDreiAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRSDreiAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, catRSDreiAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = catRSDreiAdapterVO.getReport().getReportFileName();

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
