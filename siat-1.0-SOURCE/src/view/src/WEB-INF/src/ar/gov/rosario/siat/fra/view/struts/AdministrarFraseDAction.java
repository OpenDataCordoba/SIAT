//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.view.struts;

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
import ar.gov.rosario.siat.fra.iface.model.FraseAdapter;
import ar.gov.rosario.siat.fra.iface.model.FraseVO;
import ar.gov.rosario.siat.fra.iface.service.FraServiceLocator;
import ar.gov.rosario.siat.fra.iface.util.FraError;
import ar.gov.rosario.siat.fra.iface.util.FraSecurityConstants;
import ar.gov.rosario.siat.fra.iface.util.Frase;
import ar.gov.rosario.siat.fra.view.util.FraConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarFraseDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarFraseDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, FraSecurityConstants.ABM_FRASE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		FraseAdapter fraseAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getFraseAdapterForView(userSession, commonKey)";
				fraseAdapterVO = FraServiceLocator.getFraseService().getFraseAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(FraConstants.FWD_FRASE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getFraseAdapterForUpdate(userSession, commonKey)";
				fraseAdapterVO = FraServiceLocator.getFraseService().getFraseAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(FraConstants.FWD_FRASE_EDIT_ADAPTER);
			}
			
			if (act.equals(FraConstants.ACT_PUBLICAR)) {
				stringServicio = "getFraseAdapterForView(userSession)";
				fraseAdapterVO = FraServiceLocator.getFraseService().getFraseAdapterForView(userSession, commonKey);
				fraseAdapterVO.addMessage(FraError.MSG_PUBLICAR);
				actionForward = mapping.findForward(FraConstants.FWD_FRASE_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (fraseAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + fraseAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FraseAdapter.NAME, fraseAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			fraseAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + FraseAdapter.NAME + ": "+ fraseAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(FraseAdapter.NAME, fraseAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FraseAdapter.NAME, fraseAdapterVO);
			 
			saveDemodaMessages(request, fraseAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FraseAdapter.NAME);
		}
	}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, FraSecurityConstants.ABM_FRASE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FraseAdapter fraseAdapterVO = (FraseAdapter) userSession.get(FraseAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fraseAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FraseAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FraseAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(fraseAdapterVO, request);
			
            // Tiene errores recuperables
			if (fraseAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseAdapterVO.infoString()); 
				saveDemodaErrors(request, fraseAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FraseAdapter.NAME, fraseAdapterVO);
			}
			
			// llamada al servicio
			FraseVO fraseVO = FraServiceLocator.getFraseService().updateFrase(userSession, fraseAdapterVO.getFrase());
			
            // Tiene errores recuperables
			if (fraseVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseAdapterVO.infoString()); 
				saveDemodaErrors(request, fraseVO);
				return forwardErrorRecoverable(mapping, request, userSession, FraseAdapter.NAME, fraseAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (fraseVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fraseAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FraseAdapter.NAME, fraseAdapterVO);
			}
			
			// Recargamos las frases
			Frase.getInstance().reload();		
			
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FraseAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FraseAdapter.NAME);
		}
	}

	public ActionForward publicar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, FraSecurityConstants.ABM_FRASE, FraSecurityConstants.MTD_PUBLICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FraseAdapter fraseAdapterVO = (FraseAdapter) userSession.get(FraseAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (fraseAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FraseAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FraseAdapter.NAME); 
			}

			// llamada al servicio
			FraseVO fraseVO = FraServiceLocator.getFraseService().publicarFrase(userSession, fraseAdapterVO.getFrase());
			
            // Tiene errores recuperables
			if (fraseVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseAdapterVO.infoString());
				saveDemodaErrors(request, fraseVO);				
				request.setAttribute(FraseAdapter.NAME, fraseAdapterVO);
				return mapping.findForward(FraConstants.FWD_FRASE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (fraseVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fraseAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FraseAdapter.NAME, fraseAdapterVO);
			}
						
			// Recargamos las frases
			Frase.getInstance().reload();
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FraseAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FraseAdapter.NAME);
		}	
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FraseAdapter.NAME);
		
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
			String name = FraseAdapter.NAME;
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
			FraseAdapter fraseAdapterVO = (FraseAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (fraseAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			fraseAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			fraseAdapterVO = FraServiceLocator.getFraseService().imprimirFrase(userSession, fraseAdapterVO);

			// limpia la lista de reports y la lista de tablas
			fraseAdapterVO.getReport().getListReport().clear();
			fraseAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (fraseAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + fraseAdapterVO.infoString());
				saveDemodaErrors(request, fraseAdapterVO);				
				request.setAttribute(name, fraseAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (fraseAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + fraseAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, fraseAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = fraseAdapterVO.getReport().getReportFileName();

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
