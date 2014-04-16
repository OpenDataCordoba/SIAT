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
import ar.gov.rosario.siat.ef.iface.model.AliComFueColAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuAdapter;
import ar.gov.rosario.siat.ef.iface.model.DetAjuVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDetAjuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDetAjuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJU, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DetAjuAdapter detAjuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDetAjuAdapterForView(userSession, commonKey)";
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJU_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDetAjuAdapterForUpdate(userSession, commonKey)";
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJU_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDetAjuAdapterForDelete(userSession, commonKey)";
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuAdapterForView
					(userSession, commonKey);
				detAjuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.DETAJU_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_DETAJU_VIEW_ADAPTER);					
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (detAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + detAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.NAME, detAjuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			detAjuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				DetAjuAdapter.NAME + ": " + detAjuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DetAjuAdapter.NAME, detAjuAdapterVO);
			
			saveDemodaMessages(request, detAjuAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			EfConstants.ACTION_ADMINISTRAR_ENC_DETAJU, BaseConstants.ACT_MODIFICAR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_DETAJU, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (detAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DetAjuAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.NAME); 
			}

			// llamada al servicio
			DetAjuVO detAjuVO = EfServiceLocator.getFiscalizacionService().deleteDetAju
				(userSession, detAjuAdapterVO.getDetAju());
			
            // Tiene errores recuperables
			if (detAjuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString());
				saveDemodaErrors(request, detAjuVO);				
				request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
				return mapping.findForward(EfConstants.FWD_DETAJU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (detAjuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.NAME, detAjuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DetAjuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DetAjuAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DetAjuAdapter.NAME);
		
	}

	public ActionForward irAgregarMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detAjuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetAjuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(detAjuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
					saveDemodaErrors(request, detAjuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, detAjuAdapterVO);
				}
				
				// setea el tipoAgregarMasivo seleccionado
				detAjuAdapterVO.setTipoAgregarMasivo(Integer.parseInt(request.getParameter("selectedId")));
				
				// Seteo los valores de navegacion en el adapter
				detAjuAdapterVO.setValuesFromNavModel(userSession.getNavModel());
				detAjuAdapterVO.setPrevAction("/ef/AdministrarDetAju");
				detAjuAdapterVO.setPrevActionParameter("inicializar");
							
				// Envio el VO al request
				request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DetAjuAdapter.NAME, detAjuAdapterVO);

				return mapping.findForward(EfConstants.FWD_AGREGAR_MASIVO);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
			}
	}

	public ActionForward irModificarRetencion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detAjuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetAjuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(detAjuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
					saveDemodaErrors(request, detAjuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, detAjuAdapterVO);
				}
				
				// setea el id seleccionado
				detAjuAdapterVO.getDetAjuDet().setId(Long.parseLong(request.getParameter("selectedId")));				

				// llamada al servicio
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().getDetAjuAdapter4ModifRetencion(userSession, detAjuAdapterVO);

				// Seteo los valores de navegacion en el adapter
				detAjuAdapterVO.setValuesFromNavModel(userSession.getNavModel());
				detAjuAdapterVO.setPrevAction("/ef/AdministrarDetAju");
				detAjuAdapterVO.setPrevActionParameter("inicializar");

				// Envio el VO al request
				request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DetAjuAdapter.NAME, detAjuAdapterVO);

				return mapping.findForward(EfConstants.FWD_DETAJU_MODIF_RET_ADAPTER);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
			}
	}

	public ActionForward agregarMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detAjuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetAjuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(detAjuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
					saveDemodaErrors(request, detAjuAdapterVO);
					request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
					// Subo el apdater al userSession
					userSession.put(DetAjuAdapter.NAME, detAjuAdapterVO);
					
					saveDemodaMessages(request, detAjuAdapterVO);			

					return mapping.findForward(EfConstants.FWD_AGREGAR_MASIVO);
				}
								
				// llamada al servicio
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().agregarMasivo(userSession, detAjuAdapterVO);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString());
					saveDemodaErrors(request, detAjuAdapterVO);				
					request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
					// Subo el apdater al userSession
					userSession.put(DetAjuAdapter.NAME, detAjuAdapterVO);
					
					saveDemodaMessages(request, detAjuAdapterVO);			

					return mapping.findForward(EfConstants.FWD_AGREGAR_MASIVO);
				}
				
				// Tiene errores no recuperables
				if (detAjuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.NAME, detAjuAdapterVO);
				}
				
				userSession.getNavModel().setAct(BaseConstants.ACT_MODIFICAR);
				return new ActionForward(EfConstants.PATH_ADMINISTRAR_DETAJU+".do?method=inicializar&selectedId="+detAjuAdapterVO.getDetAju().getId());
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
			}
	}
	
	public ActionForward modificarRetencion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detAjuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetAjuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(detAjuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
					saveDemodaErrors(request, detAjuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, detAjuAdapterVO);
				}
								
				// llamada al servicio
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().modificarRetencion(userSession, detAjuAdapterVO);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString());
					saveDemodaErrors(request, detAjuAdapterVO);				
					request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
					return mapping.findForward(EfConstants.FWD_DETAJU_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (detAjuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.NAME, detAjuAdapterVO);
				}
				
				userSession.getNavModel().setAct(BaseConstants.ACT_MODIFICAR);
				return new ActionForward(EfConstants.PATH_ADMINISTRAR_DETAJU+".do?method=inicializar&selectedId="+detAjuAdapterVO.getDetAju().getId());
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
			}
	}
	

	// Metodos relacionados DetAjuDet
	public ActionForward verDetAjuDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDET);

	}

	public ActionForward modificarDetAjuDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDET);

	}

	public ActionForward eliminarDetAjuDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDET);

	}
	
	public ActionForward agregarDetAjuDet(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDET);
		
	}
	
	public ActionForward calcularBases(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ALICOMFUECOL, EfConstants.ACT_INIT);
			
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
			String name = DetAjuAdapter.NAME;
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
			DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (detAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			detAjuAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().imprimirDetAju(userSession, detAjuAdapterVO,false);

			// limpia la lista de reports y la lista de tablas
			detAjuAdapterVO.getReport().getListReport().clear();
			detAjuAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (detAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString());
				saveDemodaErrors(request, detAjuAdapterVO);				
				request.setAttribute(name, detAjuAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (detAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, detAjuAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = detAjuAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	public ActionForward imprimirAjuPosReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// obtiene el nombre del page del request
			//String name = request.getParameter("name");
			String name = DetAjuAdapter.NAME;
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
			DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (detAjuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorSessionNullObject(mapping, request, funcName, ObraAdapter.NAME); 
			}

			// prepara el report del adapter para luego generar el reporte
			detAjuAdapterVO.prepareReport(Long.valueOf(reportFormat));
			
			// llamada al servicio que genera el reporte
			detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().imprimirDetAju(userSession, detAjuAdapterVO,true);

			// limpia la lista de reports y la lista de tablas
			detAjuAdapterVO.getReport().getListReport().clear();
			detAjuAdapterVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (detAjuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString());
				saveDemodaErrors(request, detAjuAdapterVO);				
				request.setAttribute(name, detAjuAdapterVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (detAjuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				//return forwardErrorNonRecoverable(mapping, request, funcName, ObraAdapter.NAME, obraAdapterVO);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, detAjuAdapterVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = detAjuAdapterVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}
	
	
	public ActionForward recalcularAct(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DetAjuAdapter detAjuAdapterVO = (DetAjuAdapter) userSession.get(DetAjuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (detAjuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DetAjuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DetAjuAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(detAjuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString()); 
					saveDemodaErrors(request, detAjuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, AliComFueColAdapter.NAME, detAjuAdapterVO);
				}
								
				// llamada al servicio
				detAjuAdapterVO = EfServiceLocator.getFiscalizacionService().updateActualizacion(userSession, detAjuAdapterVO);
				
	            // Tiene errores recuperables
				if (detAjuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + detAjuAdapterVO.infoString());
					saveDemodaErrors(request, detAjuAdapterVO);				
					request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
					return mapping.findForward(EfConstants.FWD_DETAJU_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (detAjuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + detAjuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DetAjuAdapter.NAME, detAjuAdapterVO);
				}
				
				userSession.put(DetAjuAdapter.NAME, detAjuAdapterVO);
				request.setAttribute(DetAjuAdapter.NAME, detAjuAdapterVO);
				
				return mapping.findForward(EfConstants.FWD_DETAJU_ADAPTER);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DetAjuAdapter.NAME);
			}
	}

}
	
