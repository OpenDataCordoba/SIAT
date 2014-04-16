//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.InsSupAdapter;
import ar.gov.rosario.siat.ef.iface.model.InsSupVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarInsSupDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarInsSupDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSSUP, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		InsSupAdapter insSupAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getInsSupAdapterForView(userSession, commonKey)";
				insSupAdapterVO = EfServiceLocator.getDefinicionService().getInsSupAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INSSUP_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getInsSupAdapterForUpdate(userSession, commonKey)";
				insSupAdapterVO = EfServiceLocator.getDefinicionService().getInsSupAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INSSUP_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getInsSupAdapterForView(userSession, commonKey)";
				insSupAdapterVO = EfServiceLocator.getDefinicionService().getInsSupAdapterForView(userSession, commonKey);				
				insSupAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.INSSUP_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_INSSUP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getInsSupAdapterForCreate((userSession, commonKey)";
				insSupAdapterVO = EfServiceLocator.getDefinicionService().getInsSupAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_INSSUP_EDIT_ADAPTER);				
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (insSupAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + insSupAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			insSupAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + InsSupAdapter.NAME + ": "+ insSupAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(InsSupAdapter.NAME, insSupAdapterVO);
			// Subo el apdater al userSession
			userSession.put(InsSupAdapter.NAME, insSupAdapterVO);
			 
			saveDemodaMessages(request, insSupAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InsSupAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InsSupAdapter insSupAdapterVO = (InsSupAdapter) userSession.get(InsSupAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (insSupAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InsSupAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InsSupAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(insSupAdapterVO, request);
			
            // Tiene errores recuperables
			if (insSupAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + insSupAdapterVO.infoString()); 
				saveDemodaErrors(request, insSupAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// llamada al servicio
			InsSupVO insSupVO = EfServiceLocator.getDefinicionService().createInsSup(userSession, insSupAdapterVO.getInsSup());
			
            // Tiene errores recuperables
			if (insSupVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + insSupVO.infoString()); 
				saveDemodaErrors(request, insSupVO);
				return forwardErrorRecoverable(mapping, request, userSession, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (insSupVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + insSupVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InsSupAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InsSupAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InsSupAdapter insSupAdapterVO = (InsSupAdapter) userSession.get(InsSupAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (insSupAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InsSupAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InsSupAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(insSupAdapterVO, request);
			
            // Tiene errores recuperables
			if (insSupAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + insSupAdapterVO.infoString()); 
				saveDemodaErrors(request, insSupAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// llamada al servicio
			InsSupVO insSupVO = EfServiceLocator.getDefinicionService().updateInsSup(userSession, insSupAdapterVO.getInsSup());
			
            // Tiene errores recuperables
			if (insSupVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + insSupAdapterVO.infoString()); 
				saveDemodaErrors(request, insSupVO);
				return forwardErrorRecoverable(mapping, request, userSession, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (insSupVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + insSupAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InsSupAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InsSupAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_INSSUP, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			InsSupAdapter insSupAdapterVO = (InsSupAdapter) userSession.get(InsSupAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (insSupAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + InsSupAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, InsSupAdapter.NAME); 
			}

			// llamada al servicio
			InsSupVO insSupVO = EfServiceLocator.getDefinicionService().deleteInsSup
				(userSession, insSupAdapterVO.getInsSup());
			
            // Tiene errores recuperables
			if (insSupVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + insSupAdapterVO.infoString());
				saveDemodaErrors(request, insSupVO);				
				request.setAttribute(InsSupAdapter.NAME, insSupAdapterVO);
				return mapping.findForward(EfConstants.FWD_INSSUP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (insSupVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + insSupAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, InsSupAdapter.NAME, insSupAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, InsSupAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, InsSupAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, InsSupAdapter.NAME);
		
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
			String name = InsSupAdapter.NAME;
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
			InsSupAdapter insSupAdapterVO = (InsSupAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (insSupAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			insSupAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			insSupAdapterVO = EfServiceLocator.getDefinicionService().imprimirInsSup(userSession, insSupAdapterVO);

			// limpia la lista de reports y la lista de tablas
			insSupAdapterVO.getReport().getListReport().clear();
			insSupAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (insSupAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + insSupAdapterVO.infoString());
				saveDemodaErrors(request, insSupAdapterVO);				
				request.setAttribute(name, insSupAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (insSupAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + insSupAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, insSupAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = insSupAdapterVO.getReport().getReportFileName();

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
