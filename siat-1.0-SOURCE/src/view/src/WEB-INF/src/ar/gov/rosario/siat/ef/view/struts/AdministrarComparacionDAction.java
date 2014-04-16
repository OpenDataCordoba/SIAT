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
import ar.gov.rosario.siat.ef.iface.model.ComparacionAdapter;
import ar.gov.rosario.siat.ef.iface.model.ComparacionVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarComparacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarComparacionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName+"       selectedId:"+request.getParameter("selectedId"));

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPARACION, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ComparacionAdapter comparacionAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			CommonKey commonKey;
			if(request.getParameter("selectedId")!=null){
				commonKey = new CommonKey(navModel.getSelectedId());				
			}else if(userSession.get(ComparacionAdapter.NAME)!=null){
				comparacionAdapterVO = (ComparacionAdapter) userSession.get(ComparacionAdapter.NAME);
				commonKey = new CommonKey(comparacionAdapterVO.getComparacion().getId());
			}else{				
				commonKey = new CommonKey(navModel.getSelectedId());
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getComparacionAdapterForView(userSession, commonKey)";
				comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().getComparacionAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMPARACION_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getComparacionAdapterForUpdate(userSession, commonKey)";
				comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().getComparacionAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_COMPARACION_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getComparacionAdapterForDelete(userSession, commonKey)";
				comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().getComparacionAdapterForView
					(userSession, commonKey);
				comparacionAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.COMPARACION_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_COMPARACION_VIEW_ADAPTER);					
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (comparacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + comparacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			comparacionAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ComparacionAdapter.NAME + ": " + comparacionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ComparacionAdapter.NAME, comparacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ComparacionAdapter.NAME, comparacionAdapterVO);
			
			saveDemodaMessages(request, comparacionAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			EfConstants.ACTION_ADMINISTRAR_ENC_COMPARACION, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPARACION, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComparacionAdapter comparacionAdapterVO = (ComparacionAdapter) userSession.get(ComparacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comparacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComparacionAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComparacionAdapter.NAME); 
			}

			// llamada al servicio
			ComparacionVO comparacionVO = EfServiceLocator.getFiscalizacionService().deleteComparacion
				(userSession, comparacionAdapterVO.getComparacion());
			
            // Tiene errores recuperables
			if (comparacionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString());
				saveDemodaErrors(request, comparacionVO);				
				request.setAttribute(ComparacionAdapter.NAME, comparacionAdapterVO);
				return mapping.findForward(EfConstants.FWD_COMPARACION_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (comparacionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comparacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ComparacionAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ComparacionAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ComparacionAdapter.NAME);
		
	}
	
	public ActionForward agregarCompFuenteRes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPARACION, EfSecurityConstants.MTD_CALCULARDIF); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComparacionAdapter comparacionAdapterVO = (ComparacionAdapter) userSession.get(ComparacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comparacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComparacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComparacionAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comparacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (comparacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// llamada al servicio
			comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().createCompFuenteRes(userSession, comparacionAdapterVO);
			
            // Tiene errores recuperables
			if (comparacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comparacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comparacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ComparacionAdapter.NAME, comparacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ComparacionAdapter.NAME, comparacionAdapterVO);
			
			request.setAttribute("irA", "seccionCompFuenteRes");
			
			return mapping.findForward(EfConstants.FWD_COMPARACION_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.NAME);
		}
	}
	
	public ActionForward eliminarCompFuenteRes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_COMPFUENTERES, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ComparacionAdapter comparacionAdapterVO = (ComparacionAdapter) userSession.get(ComparacionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comparacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ComparacionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComparacionAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comparacionAdapterVO, request);
			
            // Tiene errores recuperables
			if (comparacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			comparacionAdapterVO.getCompFuenteResVO().setId(Long.valueOf(request.getParameter("selectedId")));
			
			// llamada al servicio
			comparacionAdapterVO = EfServiceLocator.getFiscalizacionService().deleteCompFuenteRes(userSession, comparacionAdapterVO);
			
            // Tiene errores recuperables
			if (comparacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapterVO.infoString()); 
				saveDemodaErrors(request, comparacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (comparacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comparacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ComparacionAdapter.NAME, comparacionAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ComparacionAdapter.NAME, comparacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ComparacionAdapter.NAME, comparacionAdapterVO);
			
			request.setAttribute("irA", "seccionCompFuenteRes");
			
			return mapping.findForward(EfConstants.FWD_COMPARACION_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ComparacionAdapter.NAME);
		}
	}
	// Metodos relacionados CompFuente
	public ActionForward verCompFuente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMPFUENTE);

	}

	public ActionForward eliminarCompFuente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMPFUENTE);

	}
	
	public ActionForward agregarCompFuente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMPFUENTE);
		
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
			String name = ComparacionAdapter.NAME;
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
			ComparacionAdapter comparacionAdapter = (ComparacionAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (comparacionAdapter == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			comparacionAdapter.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			comparacionAdapter = EfServiceLocator.getFiscalizacionService().imprimirComparacion(userSession, comparacionAdapter);

			// limpia la lista de reports y la lista de tablas
			comparacionAdapter.getReport().getListReport().clear();
			comparacionAdapter.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (comparacionAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comparacionAdapter.infoString());
				saveDemodaErrors(request, comparacionAdapter);				
				request.setAttribute(name, comparacionAdapter);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (comparacionAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + comparacionAdapter.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, comparacionAdapter);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = comparacionAdapter.getReport().getReportFileName();

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
	
