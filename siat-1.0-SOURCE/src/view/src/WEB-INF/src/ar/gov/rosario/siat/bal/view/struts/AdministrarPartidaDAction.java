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

import ar.gov.rosario.siat.bal.iface.model.PartidaAdapter;
import ar.gov.rosario.siat.bal.iface.model.PartidaVO;
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

public final class AdministrarPartidaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPartidaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PartidaAdapter partidaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPartidaAdapterForView(userSession, commonKey)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPartidaAdapterForUpdate(userSession, commonKey)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPartidaAdapterForView(userSession, commonKey)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForView(userSession, commonKey);
				partidaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.PARTIDA_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);				
			}
		
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPartidaAdapterForView(userSession)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForView(userSession, commonKey);
				partidaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.PARTIDA_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getPartidaAdapterForView(userSession)";
				partidaAdapterVO = BalServiceLocator.getDefinicionService().getPartidaAdapterForView(userSession, commonKey);
				partidaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.PARTIDA_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (partidaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			partidaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PartidaAdapter.NAME + ": "+ partidaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PartidaAdapter.NAME, partidaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PartidaAdapter.NAME, partidaAdapterVO);
			 
			saveDemodaMessages(request, partidaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_ENC_PARTIDA, BaseConstants.ACT_MODIFICAR);

		}

	

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(PartidaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PartidaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PartidaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(partidaAdapterVO, request);
			
            // Tiene errores recuperables
			if (partidaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString()); 
				saveDemodaErrors(request, partidaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// llamada al servicio
			PartidaVO partidaVO = BalServiceLocator.getDefinicionService().updatePartida(userSession, partidaAdapterVO.getPartida());
			
            // Tiene errores recuperables
			if (partidaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString()); 
				saveDemodaErrors(request, partidaVO);
				return forwardErrorRecoverable(mapping, request, userSession, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (partidaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(PartidaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PartidaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PartidaAdapter.NAME); 
			}

			// llamada al servicio
			PartidaVO partidaVO = BalServiceLocator.getDefinicionService().deletePartida
				(userSession, partidaAdapterVO.getPartida());
			
            // Tiene errores recuperables
			if (partidaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString());
				saveDemodaErrors(request, partidaVO);				
				request.setAttribute(PartidaAdapter.NAME, partidaAdapterVO);
				return mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (partidaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(PartidaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PartidaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PartidaAdapter.NAME); 
			}

			// llamada al servicio
			PartidaVO partidaVO = BalServiceLocator.getDefinicionService().activarPartida
				(userSession, partidaAdapterVO.getPartida());
			
            // Tiene errores recuperables
			if (partidaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString());
				saveDemodaErrors(request, partidaVO);				
				request.setAttribute(PartidaAdapter.NAME, partidaAdapterVO);
				return mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (partidaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(PartidaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PartidaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PartidaAdapter.NAME); 
			}

			// llamada al servicio
			PartidaVO partidaVO = BalServiceLocator.getDefinicionService().desactivarPartida(userSession, partidaAdapterVO.getPartida());
			
            // Tiene errores recuperables
			if (partidaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString());
				saveDemodaErrors(request, partidaVO);				
				request.setAttribute(PartidaAdapter.NAME, partidaAdapterVO);
				return mapping.findForward(BalConstants.FWD_PARTIDA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (partidaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PartidaAdapter.NAME, partidaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PartidaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PartidaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PartidaAdapter.NAME);
		
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
			String name = PartidaAdapter.NAME;
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
			PartidaAdapter partidaAdapterVO = (PartidaAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (partidaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			partidaAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			partidaAdapterVO = BalServiceLocator.getDefinicionService().imprimirPartida(userSession, partidaAdapterVO);

			// limpia la lista de reports y la lista de tablas
			partidaAdapterVO.getReport().getListReport().clear();
			partidaAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (partidaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + partidaAdapterVO.infoString());
				saveDemodaErrors(request, partidaAdapterVO);				
				request.setAttribute(name, partidaAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (partidaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + partidaAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, partidaAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = partidaAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, PartidaAdapter.NAME);
			
		}
	
	// Metodos relacionados verParCueBan
	public ActionForward verParCueBan(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARCUEBAN);

	}

	public ActionForward modificarParCueBan(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARCUEBAN);

	}

	public ActionForward eliminarParCueBan(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARCUEBAN);

	}
	
	public ActionForward agregarParCueBan(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PARCUEBAN);
		
	}
	
	
}
