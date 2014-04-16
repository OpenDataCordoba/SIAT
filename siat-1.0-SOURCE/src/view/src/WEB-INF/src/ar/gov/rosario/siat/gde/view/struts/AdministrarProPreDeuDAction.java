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
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.ProPreDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarProPreDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProPreDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROPREDEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProPreDeuAdapter proPreDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProPreDeuAdapterForView(userSession, commonKey)";
				proPreDeuAdapterVO = GdeServiceLocator.getAdmDeuConService().getProPreDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROPREDEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProPreDeuAdapterForUpdate(userSession, commonKey)";
				proPreDeuAdapterVO = GdeServiceLocator.getAdmDeuConService().getProPreDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PROPREDEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProPreDeuAdapterForView(userSession, commonKey)";
				proPreDeuAdapterVO = GdeServiceLocator.getAdmDeuConService().getProPreDeuAdapterForView(userSession, commonKey);				
				proPreDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.PROPREDEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_PROPREDEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getProPreDeuAdapterForCreate(userSession)";
				proPreDeuAdapterVO = GdeServiceLocator.getAdmDeuConService().getProPreDeuAdapterForCreate(userSession);
				actionForward = mapping.findForward(GdeConstants.FWD_PROPREDEU_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proPreDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proPreDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proPreDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProPreDeuAdapter.NAME + ": "+ proPreDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			 
			saveDemodaMessages(request, proPreDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROPREDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProPreDeuAdapter proPreDeuAdapterVO = (ProPreDeuAdapter) userSession.get(ProPreDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPreDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPreDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPreDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proPreDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (proPreDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, proPreDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// llamada al servicio
			ProPreDeuVO proPreDeuVO = GdeServiceLocator.getAdmDeuConService().createProPreDeu(userSession, proPreDeuAdapterVO.getProPreDeu());
			
            // Tiene errores recuperables
			if (proPreDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuVO.infoString()); 
				saveDemodaErrors(request, proPreDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProPreDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROPREDEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProPreDeuAdapter proPreDeuAdapterVO = (ProPreDeuAdapter) userSession.get(ProPreDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPreDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPreDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPreDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proPreDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (proPreDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, proPreDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// llamada al servicio
			ProPreDeuVO proPreDeuVO = GdeServiceLocator.getAdmDeuConService().updateProPreDeu(userSession, proPreDeuAdapterVO.getProPreDeu());
			
            // Tiene errores recuperables
			if (proPreDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, proPreDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProPreDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROPREDEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProPreDeuAdapter proPreDeuAdapterVO = (ProPreDeuAdapter) userSession.get(ProPreDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proPreDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProPreDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPreDeuAdapter.NAME); 
			}

			// llamada al servicio
			ProPreDeuVO proPreDeuVO = GdeServiceLocator.getAdmDeuConService().deleteProPreDeu
				(userSession, proPreDeuAdapterVO.getProPreDeu());
			
            // Tiene errores recuperables
			if (proPreDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuAdapterVO.infoString());
				saveDemodaErrors(request, proPreDeuVO);				
				request.setAttribute(ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_PROPREDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPreDeuAdapter.NAME, proPreDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProPreDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPreDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProPreDeuAdapter.NAME);
		
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
			String name = ProPreDeuAdapter.NAME;
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
			ProPreDeuAdapter proPreDeuAdapterVO = (ProPreDeuAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (proPreDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName,  proPreDeuAdapterVO.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			proPreDeuAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			proPreDeuAdapterVO = GdeServiceLocator.getAdmDeuConService().imprimirProPreDeu(userSession, proPreDeuAdapterVO);

			// limpia la lista de reports y la lista de tablas
			proPreDeuAdapterVO.getReport().getListReport().clear();
			proPreDeuAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (proPreDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPreDeuAdapterVO.infoString());
				saveDemodaErrors(request, proPreDeuAdapterVO);				
				request.setAttribute(name, proPreDeuAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (proPreDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPreDeuAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName,  proPreDeuAdapterVO.NAME,  proPreDeuAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, proPreDeuAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = proPreDeuAdapterVO.getReport().getReportFileName();

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
