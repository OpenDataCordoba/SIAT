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
import ar.gov.rosario.siat.def.iface.model.EmiMatAdapter;
import ar.gov.rosario.siat.def.iface.model.EmiMatVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEmiMatDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmiMatDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_EMIMAT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmiMatAdapter emiMatAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEmiMatAdapterForView(userSession, commonKey)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getEmiMatAdapterForView(userSession, commonKey)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getEmiMatAdapterForView(userSession, commonKey)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForView(userSession, commonKey);				
				emiMatAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.EMIMAT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getEmiMatAdapterForView(userSession)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForView(userSession, commonKey);
				emiMatAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.EMIMAT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getEmiMatAdapterForView(userSession)";
				emiMatAdapterVO = DefServiceLocator.getEmisionService().getEmiMatAdapterForView(userSession, commonKey);
				emiMatAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.EMIMAT_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emiMatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.NAME, emiMatAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emiMatAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmiMatAdapter.NAME + ": "+ emiMatAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmiMatAdapter.NAME, emiMatAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmiMatAdapter.NAME, emiMatAdapterVO);
			 
			saveDemodaMessages(request, emiMatAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				DefConstants.ACTION_ADMINISTRAR_ENC_EMIMAT, BaseConstants.ACT_MODIFICAR);

		}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_EMIMAT, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmiMatAdapter emiMatAdapterVO = (EmiMatAdapter) userSession.get(EmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmiMatAdapter.NAME); 
			}

			// llamada al servicio
			EmiMatVO emiMatVO = DefServiceLocator.getEmisionService().deleteEmiMat
				(userSession, emiMatAdapterVO.getEmiMat());
			
            // Tiene errores recuperables
			if (emiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString());
				saveDemodaErrors(request, emiMatVO);				
				request.setAttribute(EmiMatAdapter.NAME, emiMatAdapterVO);
				return mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (emiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.NAME, emiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmiMatAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_EMIMAT, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmiMatAdapter emiMatAdapterVO = (EmiMatAdapter) userSession.get(EmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmiMatAdapter.NAME); 
			}

			// llamada al servicio
			EmiMatVO emiMatVO = DefServiceLocator.getEmisionService().activarEmiMat
				(userSession, emiMatAdapterVO.getEmiMat());
			
            // Tiene errores recuperables
			if (emiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString());
				saveDemodaErrors(request, emiMatVO);				
				request.setAttribute(EmiMatAdapter.NAME, emiMatAdapterVO);
				return mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (emiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.NAME, emiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmiMatAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_EMIMAT, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmiMatAdapter emiMatAdapterVO = (EmiMatAdapter) userSession.get(EmiMatAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmiMatAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmiMatAdapter.NAME); 
			}

			// llamada al servicio
			EmiMatVO emiMatVO = DefServiceLocator.getEmisionService().desactivarEmiMat
				(userSession, emiMatAdapterVO.getEmiMat());
			
            // Tiene errores recuperables
			if (emiMatVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString());
				saveDemodaErrors(request, emiMatVO);				
				request.setAttribute(EmiMatAdapter.NAME, emiMatAdapterVO);
				return mapping.findForward(DefConstants.FWD_EMIMAT_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (emiMatVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiMatAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmiMatAdapter.NAME, emiMatAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmiMatAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmiMatAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmiMatAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, EmiMatAdapter.NAME);
			
		}

	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			String name = EmiMatAdapter.NAME;
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
			EmiMatAdapter emiMatAdapterVO = (EmiMatAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (emiMatAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
 			}

			// prepara el report del adapter para luego generar el reporte
			emiMatAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			emiMatAdapterVO =  DefServiceLocator.getEmisionService().imprimirEmiMat(userSession, emiMatAdapterVO);

			// limpia la lista de reports y la lista de tablas
			emiMatAdapterVO.getReport().getListReport().clear();
			emiMatAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (emiMatAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emiMatAdapterVO.infoString());
				saveDemodaErrors(request, emiMatAdapterVO);				
				request.setAttribute(name, emiMatAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (emiMatAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emiMatAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, emiMatAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = emiMatAdapterVO.getReport().getReportFileName();

			// preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	// ---> Metodos Relacionados con ColEmiMat
	public ActionForward verColEmiMat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_COLEMIMAT);

		}

	public ActionForward modificarColEmiMat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_COLEMIMAT);

	}

	public ActionForward eliminarColEmiMat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_COLEMIMAT);

	}
	
	public ActionForward agregarColEmiMat(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_COLEMIMAT);
		
	}
	// <--- Metodos Relacionados con ColEmiMat

}
