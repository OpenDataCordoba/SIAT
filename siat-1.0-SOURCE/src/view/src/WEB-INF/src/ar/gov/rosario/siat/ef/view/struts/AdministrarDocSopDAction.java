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
import ar.gov.rosario.siat.ef.iface.model.DocSopAdapter;
import ar.gov.rosario.siat.ef.iface.model.DocSopVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDocSopDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDocSopDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DocSopAdapter docSopAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDocSopAdapterForView(userSession, commonKey)";
				docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDocSopAdapterForUpdate(userSession, commonKey)";
				docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DOCSOP_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDocSopAdapterForView(userSession, commonKey)";
				docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterForView(userSession, commonKey);				
				docSopAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.DOCSOP_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDocSopAdapterForCreate(userSession)";
				docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterForCreate(userSession);
				actionForward = mapping.findForward(EfConstants.FWD_DOCSOP_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getDocSopAdapterForView(userSession)";
				docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterForView(userSession, commonKey);
				docSopAdapterVO.addMessage(BaseError.MSG_ACTIVAR, EfError.DOCSOP_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getDocSopAdapterForView(userSession)";
				docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterForView(userSession, commonKey);
				docSopAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, EfError.DOCSOP_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (docSopAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + docSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			docSopAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DocSopAdapter.NAME + ": "+ docSopAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DocSopAdapter.NAME, docSopAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DocSopAdapter.NAME, docSopAdapterVO);
			 
			saveDemodaMessages(request, docSopAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(DocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(docSopAdapterVO, request);
			
            // Tiene errores recuperables
			if (docSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString()); 
				saveDemodaErrors(request, docSopAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// llamada al servicio
			DocSopVO docSopVO = EfServiceLocator.getDefinicionService().createDocSop(userSession, docSopAdapterVO.getDocSop());
			
            // Tiene errores recuperables
			if (docSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopVO.infoString()); 
				saveDemodaErrors(request, docSopVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (docSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DocSopAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(DocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(docSopAdapterVO, request);
			
            // Tiene errores recuperables
			if (docSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString()); 
				saveDemodaErrors(request, docSopAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// llamada al servicio
			DocSopVO docSopVO = EfServiceLocator.getDefinicionService().updateDocSop(userSession, docSopAdapterVO.getDocSop());
			
            // Tiene errores recuperables
			if (docSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString()); 
				saveDemodaErrors(request, docSopVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (docSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DocSopAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(DocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// llamada al servicio
			DocSopVO docSopVO = EfServiceLocator.getDefinicionService().deleteDocSop
				(userSession, docSopAdapterVO.getDocSop());
			
            // Tiene errores recuperables
			if (docSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString());
				saveDemodaErrors(request, docSopVO);				
				request.setAttribute(DocSopAdapter.NAME, docSopAdapterVO);
				return mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (docSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DocSopAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(DocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// llamada al servicio
			DocSopVO docSopVO = EfServiceLocator.getDefinicionService().activarDocSop(userSession, docSopAdapterVO.getDocSop());
			
            // Tiene errores recuperables
			if (docSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString());
				saveDemodaErrors(request, docSopVO);				
				request.setAttribute(DocSopAdapter.NAME, docSopAdapterVO);
				return mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (docSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DocSopAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DOCSOP, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(DocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// llamada al servicio
			DocSopVO docSopVO = EfServiceLocator.getDefinicionService().desactivarDocSop(userSession, docSopAdapterVO.getDocSop());
			
            // Tiene errores recuperables
			if (docSopVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString());
				saveDemodaErrors(request, docSopVO);				
				request.setAttribute(DocSopAdapter.NAME, docSopAdapterVO);
				return mapping.findForward(EfConstants.FWD_DOCSOP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (docSopVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DocSopAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DocSopAdapter.NAME);
		
	}
	
	/*public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(DocSopAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DocSopAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(docSopAdapterVO, request);
			
            // Tiene errores recuperables
			if (docSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString()); 
				saveDemodaErrors(request, docSopAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// llamada al servicio
			docSopAdapterVO = EfServiceLocator.getDefinicionService().getDocSopAdapterParam(userSession, docSopAdapterVO);
			
            // Tiene errores recuperables
			if (docSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString()); 
				saveDemodaErrors(request, docSopAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (docSopAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DocSopAdapter.NAME, docSopAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DocSopAdapter.NAME, docSopAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DocSopAdapter.NAME, docSopAdapterVO);
			
			return mapping.findForward(EfConstants.FWD_DOCSOP_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DocSopAdapter.NAME);
		}
	}
		
		*/
	
		public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = DocSopAdapter.NAME;
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
			DocSopAdapter docSopAdapterVO = (DocSopAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (docSopAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, DocSopAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			docSopAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			docSopAdapterVO = EfServiceLocator.getDefinicionService().imprimirDocSop(userSession, docSopAdapterVO);

			// limpia la lista de reports y la lista de tablas
			docSopAdapterVO.getReport().getListReport().clear();
			docSopAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (docSopAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + docSopAdapterVO.infoString());
				saveDemodaErrors(request, docSopAdapterVO);				
				request.setAttribute(name, docSopAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (docSopAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + docSopAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, docSopAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = docSopAdapterVO.getReport().getReportFileName();

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
