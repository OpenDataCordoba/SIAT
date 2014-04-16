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
import ar.gov.rosario.siat.def.iface.model.BancoAdapter;
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarBancoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_BANCO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		BancoAdapter bancoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getBancoAdapterForView(userSession, commonKey)";
				bancoAdapterVO = DefServiceLocator.getServicioBancoService().getBancoAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_BANCO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getBancoAdapterForUpdate(userSession, commonKey)";
				bancoAdapterVO = DefServiceLocator.getServicioBancoService().getBancoAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_BANCO_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getBancoAdapterForView(userSession, commonKey)";
				bancoAdapterVO = DefServiceLocator.getServicioBancoService().getBancoAdapterForView(userSession, commonKey);				
				bancoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.BANCO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_BANCO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getBancoAdapterForCreate(userSession)";
				bancoAdapterVO = DefServiceLocator.getServicioBancoService().getBancoAdapterForCreate(userSession);
				actionForward = mapping.findForward(DefConstants.FWD_BANCO_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getBancoAdapterForView(userSession)";
				bancoAdapterVO = DefServiceLocator.getServicioBancoService().getBancoAdapterForView(userSession, commonKey);
				bancoAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.BANCO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_BANCO_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getBancoAdapterForView(userSession)";
				bancoAdapterVO = DefServiceLocator.getServicioBancoService().getBancoAdapterForView(userSession, commonKey);
				bancoAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.BANCO_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_BANCO_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (bancoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + bancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			bancoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + BancoAdapter.NAME + ": "+ bancoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(BancoAdapter.NAME, bancoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(BancoAdapter.NAME, bancoAdapterVO);
			 
			saveDemodaMessages(request, bancoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BancoAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_BANCO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BancoAdapter bancoAdapterVO = (BancoAdapter) userSession.get(BancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (bancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BancoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(bancoAdapterVO, request);
			
            // Tiene errores recuperables
			if (bancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoAdapterVO.infoString()); 
				saveDemodaErrors(request, bancoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// llamada al servicio
			BancoVO bancoVO = DefServiceLocator.getServicioBancoService().createBanco(userSession, bancoAdapterVO.getBanco());
			
            // Tiene errores recuperables
			if (bancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoVO.infoString()); 
				saveDemodaErrors(request, bancoVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (bancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + bancoVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, BancoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BancoAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_BANCO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BancoAdapter bancoAdapterVO = (BancoAdapter) userSession.get(BancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (bancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BancoAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(bancoAdapterVO, request);
			
            // Tiene errores recuperables
			if (bancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoAdapterVO.infoString()); 
				saveDemodaErrors(request, bancoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// llamada al servicio
			BancoVO bancoVO = DefServiceLocator.getServicioBancoService().updateBanco(userSession, bancoAdapterVO.getBanco());
			
            // Tiene errores recuperables
			if (bancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoAdapterVO.infoString()); 
				saveDemodaErrors(request, bancoVO);
				return forwardErrorRecoverable(mapping, request, userSession, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (bancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + bancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, BancoAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BancoAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_BANCO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			BancoAdapter bancoAdapterVO = (BancoAdapter) userSession.get(BancoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (bancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BancoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BancoAdapter.NAME); 
			}

			// llamada al servicio
			BancoVO bancoVO = DefServiceLocator.getServicioBancoService().deleteBanco(userSession, bancoAdapterVO.getBanco());
			
            // Tiene errores recuperables
			if (bancoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoAdapterVO.infoString());
				saveDemodaErrors(request, bancoVO);				
				request.setAttribute(BancoAdapter.NAME, bancoAdapterVO);
				return mapping.findForward(DefConstants.FWD_BANCO_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (bancoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + bancoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BancoAdapter.NAME, bancoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, BancoAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BancoAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, BancoAdapter.NAME);
		
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
			String name = BancoAdapter.NAME;
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
			BancoAdapter bancoAdapterVO = (BancoAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (bancoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}

			// prepara el report del adapter para luego generar el reporte
			bancoAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			bancoAdapterVO = DefServiceLocator.getServicioBancoService().imprimirBanco(userSession, bancoAdapterVO);

			// limpia la lista de reports y la lista de tablas
			bancoAdapterVO.getReport().getListReport().clear();
			bancoAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (bancoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + bancoAdapterVO.infoString());
				saveDemodaErrors(request, bancoAdapterVO);				
				request.setAttribute(name, bancoAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (bancoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + bancoAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, bancoAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = bancoAdapterVO.getReport().getReportFileName();

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
