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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCueExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExeAdapter cueExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			// El act=agregar surge de la navegacion: 
			// agregar con estado Ha Lugar -> cuenta no posee deuda para el periodo ->
			// cambiarEstado -> ok
			if (act.equals(BaseConstants.ACT_VER) || act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCueExeAdapterForView(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CUEEXE_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCueExeAdapterForView(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CUEEXE_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCueExeAdapterForView(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CUEEXE_VIEW_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExeAdapterVO.setValuesFromNavModel(navModel);
			
			Long idCuenta = (Long) userSession.get(BuscarCueExeDAction.VER_CUEEXE_ID_CUENTA);

			// Pasamos la bandera de Modo Ver
			if (idCuenta != null){
				cueExeAdapterVO.setModoVer(true);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExeAdapter.NAME + ": "+ cueExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExeAdapter.NAME, cueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeAdapter.NAME, cueExeAdapterVO);
			 
			saveDemodaMessages(request, cueExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ENC_CUEEXE, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CAMBIOESTADO_CUEEXE, "cambiarEstado");

	}
	
	
	public ActionForward agregarSolicitud(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CAMBIOESTADO_CUEEXE, "agregarSolicitud");

	}	
	
	public ActionForward modificarHisEstCueExe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CAMBIOESTADO_CUEEXE, "modificarHisEstCueExe");
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// llamada al servicio
			CueExeVO cueExeVO = ExeServiceLocator.getExencionService().createCueExe(userSession, cueExeAdapterVO.getCueExe());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeVO.infoString()); 
				saveDemodaErrors(request, cueExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// llamada al servicio
			CueExeVO cueExeVO = ExeServiceLocator.getExencionService().updateCueExe(userSession, cueExeAdapterVO.getCueExe());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExeAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeAdapter.NAME);
		
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
			String name = CueExeAdapter.NAME;
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
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			cueExeAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			cueExeAdapterVO = ExeServiceLocator.getExencionService().imprimirCueExe(userSession, cueExeAdapterVO);

			// limpia la lista de reports y la lista de tablas
			cueExeAdapterVO.getReport().getListReport().clear();
			cueExeAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString());
				saveDemodaErrors(request, cueExeAdapterVO);				
				request.setAttribute(name, cueExeAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, cueExeAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = cueExeAdapterVO.getReport().getReportFileName();
			
			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE, BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.NAME); 
			}

			// llamada al servicio
			CueExeVO cueExeVO = ExeServiceLocator.getExencionService().deleteCueExe(userSession, cueExeAdapterVO.getCueExe());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString());
				saveDemodaErrors(request, cueExeVO);				
				request.setAttribute(CueExeAdapter.NAME, cueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CUEEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.NAME, cueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.NAME);
		}
	}	
	
	public ActionForward eliminarHisEstCueExe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_HISESTCUEEXE);

	}
	
}
