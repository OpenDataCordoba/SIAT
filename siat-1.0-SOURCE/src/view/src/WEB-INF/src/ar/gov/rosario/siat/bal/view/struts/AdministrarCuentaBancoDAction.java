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

import ar.gov.rosario.siat.bal.iface.model.CuentaBancoAdapter;
import ar.gov.rosario.siat.bal.iface.model.CuentaBancoVO;
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

public final class AdministrarCuentaBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCuentaBancoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CuentaBancoAdapter cuentaBancoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCuentaBancoAdapterForView(userSession, commonKey)";
				cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().getCuentaBancoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCuentaBancoAdapterForUpdate(userSession, commonKey)";
				cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().getCuentaBancoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(BalConstants.FWD_CUENTABANCO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCuentaBancoAdapterForView(userSession, commonKey)";
				cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().getCuentaBancoAdapterForView(userSession, commonKey);				
				cuentaBancoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.CUENTABANCO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCuentaBancoAdapterForCreate(userSession)";
				cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().getCuentaBancoAdapterForCreate(userSession);
				actionForward = mapping.findForward(BalConstants.FWD_CUENTABANCO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getCuentaBancoAdapterForView(userSession)";
				cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().getCuentaBancoAdapterForView(userSession, commonKey);
				cuentaBancoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, BalError.CUENTABANCO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCuentaBancoAdapterForView(userSession)";
				cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().getCuentaBancoAdapterForView(userSession, commonKey);
				cuentaBancoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, BalError.CUENTABANCO_LABEL);
				actionForward = mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cuentaBancoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cuentaBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cuentaBancoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CuentaBancoAdapter.NAME + ": "+ cuentaBancoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			 
			saveDemodaMessages(request, cuentaBancoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaBancoAdapter cuentaBancoAdapterVO = (CuentaBancoAdapter) userSession.get(CuentaBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaBancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaBancoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaBancoAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaBancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// llamada al servicio
			CuentaBancoVO cuentaBancoVO = BalServiceLocator.getDefinicionService().createCuentaBanco(userSession, cuentaBancoAdapterVO.getCuentaBanco());
			
            // Tiene errores recuperables
			if (cuentaBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaBancoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaBancoAdapter cuentaBancoAdapterVO = (CuentaBancoAdapter) userSession.get(CuentaBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaBancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaBancoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaBancoAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaBancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// llamada al servicio
			CuentaBancoVO cuentaBancoVO = BalServiceLocator.getDefinicionService().updateCuentaBanco(userSession, cuentaBancoAdapterVO.getCuentaBanco());
			
            // Tiene errores recuperables
			if (cuentaBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaBancoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaBancoAdapter cuentaBancoAdapterVO = (CuentaBancoAdapter) userSession.get(CuentaBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaBancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaBancoAdapter.NAME); 
			}

			// llamada al servicio
			CuentaBancoVO cuentaBancoVO = BalServiceLocator.getDefinicionService().deleteCuentaBanco
				(userSession, cuentaBancoAdapterVO.getCuentaBanco());
			
            // Tiene errores recuperables
			if (cuentaBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString());
				saveDemodaErrors(request, cuentaBancoVO);				
				request.setAttribute(CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
				return mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaBancoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaBancoAdapter cuentaBancoAdapterVO = (CuentaBancoAdapter) userSession.get(CuentaBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaBancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaBancoAdapter.NAME); 
			}

			// llamada al servicio
			CuentaBancoVO cuentaBancoVO = BalServiceLocator.getDefinicionService().activarCuentaBanco
				(userSession, cuentaBancoAdapterVO.getCuentaBanco());
			
            // Tiene errores recuperables
			if (cuentaBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString());
				saveDemodaErrors(request, cuentaBancoVO);				
				request.setAttribute(CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
				return mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaBancoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaBancoAdapter cuentaBancoAdapterVO = (CuentaBancoAdapter) userSession.get(CuentaBancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaBancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaBancoAdapter.NAME); 
			}

			// llamada al servicio
			CuentaBancoVO cuentaBancoVO = BalServiceLocator.getDefinicionService().desactivarCuentaBanco
				(userSession, cuentaBancoAdapterVO.getCuentaBanco());
			
            // Tiene errores recuperables
			if (cuentaBancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString());
				saveDemodaErrors(request, cuentaBancoVO);				
				request.setAttribute(CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
				return mapping.findForward(BalConstants.FWD_CUENTABANCO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoAdapter.NAME, cuentaBancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaBancoAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentaBancoAdapter.NAME);
		
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
			String name = CuentaBancoAdapter.NAME;
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
			CuentaBancoAdapter cuentaBancoAdapterVO = (CuentaBancoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  cuentaBancoAdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			cuentaBancoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			cuentaBancoAdapterVO = BalServiceLocator.getDefinicionService().imprimirCuentaBanco(userSession, cuentaBancoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			cuentaBancoAdapterVO.getReport().getListReport().clear();
			cuentaBancoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (cuentaBancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoAdapterVO.infoString());
				saveDemodaErrors(request, cuentaBancoAdapterVO);				
				request.setAttribute(name, cuentaBancoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  cuentaBancoAdapterVO.NAME,  cuentaBancoAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, cuentaBancoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = cuentaBancoAdapterVO.getReport().getReportFileName();

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
