//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.DesEspAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesEspVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesEspDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesEspDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesEspAdapter desEspAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDesEspAdapterForView(userSession, commonKey)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesEspAdapterForUpdate(userSession, commonKey)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDesEspAdapterForDelete(userSession, commonKey)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView
					(userSession, commonKey);
				desEspAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DESESP_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getDesEspAdapterForView(userSession)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView
					(userSession, commonKey);
				desEspAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.DESESP_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getDesEspAdapterForView(userSession)";
				desEspAdapterVO = GdeServiceLocator.getDefinicionService().getDesEspAdapterForView
					(userSession, commonKey);
				desEspAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.DESESP_LABEL);			
				actionForward = mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desEspAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + desEspAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desEspAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desEspAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				DesEspAdapter.NAME + ": " + desEspAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesEspAdapter.NAME, desEspAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesEspAdapter.NAME, desEspAdapterVO);
			
			saveDemodaMessages(request, desEspAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			GdeConstants.ACTION_ADMINISTRAR_ENC_DESESP, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desEspAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// llamada al servicio
			DesEspVO desEspVO = GdeServiceLocator.getDefinicionService().deleteDesEsp
				(userSession, desEspAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desEspVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString());
				saveDemodaErrors(request, desEspVO);				
				request.setAttribute(DesEspAdapter.NAME, desEspAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desEspVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desEspAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desEspAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// llamada al servicio
			DesEspVO desEspVO = GdeServiceLocator.getDefinicionService().activarDesEsp
				(userSession, desEspAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desEspVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString());
				saveDemodaErrors(request, desEspVO);				
				request.setAttribute(DesEspAdapter.NAME, desEspAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desEspVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desEspAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESESP, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesEspAdapter desEspAdapterVO = (DesEspAdapter) userSession.get(DesEspAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desEspAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesEspAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesEspAdapter.NAME); 
			}

			// llamada al servicio
			DesEspVO desEspVO = GdeServiceLocator.getDefinicionService().desactivarDesEsp
				(userSession, desEspAdapterVO.getDesEsp());
			
            // Tiene errores recuperables
			if (desEspVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString());
				saveDemodaErrors(request, desEspVO);				
				request.setAttribute(DesEspAdapter.NAME, desEspAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESESP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desEspVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesEspAdapter.NAME, desEspAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesEspAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesEspAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DesEspAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DesEspAdapter.NAME);
		
	}
	
	// Metodos relacionados ClasificDeuda
	public ActionForward verClasificDeuda(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESRECCLADEU);

	}

	public ActionForward modificarClasificDeuda(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESRECCLADEU);

	}

	public ActionForward eliminarClasificDeuda(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESRECCLADEU);

	}
	
	public ActionForward agregarClasificDeuda(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESRECCLADEU);
		
	}
	
	// Metodos relacionados DesAtrVal
	public ActionForward verDesAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESATRVAL);

	}

	public ActionForward modificarDesAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESATRVAL);

	}

	public ActionForward eliminarDesAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESATRVAL);

	}
	
	public ActionForward agregarDesAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESATRVAL);
		
	}
	
	// Metodos relacionados DesEspExe
	public ActionForward verDesEspExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESESPEXE);

	}

	public ActionForward modificarDesEspExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESESPEXE);

	}

	public ActionForward eliminarDesEspExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESESPEXE);

	}
	
	public ActionForward agregarDesEspExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESESPEXE);
		
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
			String name = DesEspAdapter.NAME;
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
			DesEspAdapter desEspAdapterVO = (DesEspAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (desEspAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			desEspAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			desEspAdapterVO = GdeServiceLocator.getDefinicionService().imprimirDesEsp(userSession, desEspAdapterVO);

			// limpia la lista de reports y la lista de tablas
			desEspAdapterVO.getReport().getListReport().clear();
			desEspAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (desEspAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desEspAdapterVO.infoString());
				saveDemodaErrors(request, desEspAdapterVO);				
				request.setAttribute(name, desEspAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (desEspAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desEspAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, desEspAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = desEspAdapterVO.getReport().getReportFileName();

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
	
