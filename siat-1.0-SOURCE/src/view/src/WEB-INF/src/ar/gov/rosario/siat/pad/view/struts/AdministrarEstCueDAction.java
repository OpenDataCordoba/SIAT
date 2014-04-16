//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.pad.iface.model.EstCueAdapter;
import ar.gov.rosario.siat.pad.iface.model.EstCueVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEstCueDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEstCueDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_ESTADOCUENTA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EstCueAdapter estadoCuentaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEstCueAdapterForView(userSession, commonKey)";
				estadoCuentaAdapterVO = PadServiceLocator.getCuentaService().getEstCueAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_ESTADOCUENTA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEstCueAdapterForUpdate(userSession, commonKey)";
				estadoCuentaAdapterVO = PadServiceLocator.getCuentaService().getEstCueAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_ESTADOCUENTA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEstCueAdapterForView(userSession, commonKey)";
				estadoCuentaAdapterVO = PadServiceLocator.getCuentaService().getEstCueAdapterForView(userSession, commonKey);				
				estadoCuentaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.ESTADOCUENTA_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_ESTADOCUENTA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEstCueAdapterForCreate(userSession)";
				estadoCuentaAdapterVO = PadServiceLocator.getCuentaService().getEstCueAdapterForCreate(userSession);
				actionForward = mapping.findForward(PadConstants.FWD_ESTADOCUENTA_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (estadoCuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + estadoCuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			estadoCuentaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EstCueAdapter.NAME + ": "+ estadoCuentaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EstCueAdapter.NAME, estadoCuentaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EstCueAdapter.NAME, estadoCuentaAdapterVO);
			 
			saveDemodaMessages(request, estadoCuentaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstCueAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_ESTADOCUENTA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstCueAdapter estadoCuentaAdapterVO = (EstCueAdapter) userSession.get(EstCueAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstCueAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(estadoCuentaAdapterVO, request);
			
            // Tiene errores recuperables
			if (estadoCuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// llamada al servicio
			EstCueVO estadoCuentaVO = PadServiceLocator.getCuentaService().createEstCue(userSession, estadoCuentaAdapterVO.getEstCue());
			
            // Tiene errores recuperables
			if (estadoCuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstCueAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstCueAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_ESTADOCUENTA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstCueAdapter estadoCuentaAdapterVO = (EstCueAdapter) userSession.get(EstCueAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstCueAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(estadoCuentaAdapterVO, request);
			
            // Tiene errores recuperables
			if (estadoCuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// llamada al servicio
			EstCueVO estadoCuentaVO = PadServiceLocator.getCuentaService().updateEstCue(userSession, estadoCuentaAdapterVO.getEstCue());
			
            // Tiene errores recuperables
			if (estadoCuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstCueAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstCueAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_ESTADOCUENTA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EstCueAdapter estadoCuentaAdapterVO = (EstCueAdapter) userSession.get(EstCueAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EstCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstCueAdapter.NAME); 
			}

			// llamada al servicio
			EstCueVO estadoCuentaVO = PadServiceLocator.getCuentaService().deleteEstCue
				(userSession, estadoCuentaAdapterVO.getEstCue());
			
            // Tiene errores recuperables
			if (estadoCuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaAdapterVO.infoString());
				saveDemodaErrors(request, estadoCuentaVO);				
				request.setAttribute(EstCueAdapter.NAME, estadoCuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_ESTADOCUENTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstCueAdapter.NAME, estadoCuentaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EstCueAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstCueAdapter.NAME);
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
			String name = EstCueAdapter.NAME;
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
			EstCueAdapter estCueAdapterVO = (EstCueAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (estCueAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, {Bean}Adapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			estCueAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			estCueAdapterVO = PadServiceLocator.getCuentaService().imprimirEstCue(userSession, estCueAdapterVO);

			// limpia la lista de reports y la lista de tablas
			estCueAdapterVO.getReport().getListReport().clear();
			estCueAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (estCueAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estCueAdapterVO.infoString());
				saveDemodaErrors(request, estCueAdapterVO);				
				request.setAttribute(name, estCueAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (estCueAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estCueAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, estCueAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = estCueAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EstCueAdapter.NAME);
		
	}
	
} 
