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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioAdapter;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarCierreComercioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCierreComercioDAction.class);

	public ActionForward cierreComercio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CIERRE_COMERCIO);
		UserSession userSession =getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getCierreComercioInit()";

		CierreComercioAdapter cierreComercioAdapter = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
		if(cierreComercioAdapter==null){
			cierreComercioAdapter = new CierreComercioAdapter();
		}

		try {
			
			// En el servicio lo separamos.
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
				cierreComercioAdapter.setIdCuenta(new Long(request.getParameter("selectedId")));//cierreComercioAdapter.setSelectedId(request.getParameter("selectedId"));
			}
			
			cierreComercioAdapter = GdeServiceLocator.getGdeGDeudaAutoService().
					getCierreComercioAdapterInit(userSession, cierreComercioAdapter);
			
			// Tiene errores no recuperables
			if (cierreComercioAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cierreComercioAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapter);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CierreComercioAdapter.NAME + ": "+ cierreComercioAdapter.infoString());
			
			// Envio el VO al request
			request.setAttribute(CierreComercioAdapter.NAME, cierreComercioAdapter);
			// lo mantiene en la sesion
			userSession.put(CierreComercioAdapter.NAME, cierreComercioAdapter);
			
			saveDemodaMessages(request, cierreComercioAdapter);
			
			return mapping.findForward(GdeConstants.FWD_CIERRE_COMERCIO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}
	}
	
	/**
	 * Utilizado para volver desde el el detalle de una Deuda 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverACuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		
		try {
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			Long selectedId = new Long(request.getParameter("selectedId"));
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}

	}
	
	public ActionForward agregarMulta(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession =getCurrentUserSession(request, mapping);
		CierreComercioAdapter cierreComercio = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
		userSession.put(CierreComercioAdapter.NAME, cierreComercio);
		
		return forwardAgregarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MULTA);
		
	}
	
	public ActionForward eliminarMulta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession =getCurrentUserSession(request, mapping);
			CierreComercioAdapter cierreComercio = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			userSession.put(CierreComercioAdapter.NAME, cierreComercio);
			
			return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MULTA);
			
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		log.debug(funcName);
		return cierreComercio(mapping, form, request, response);
		
	}
	
	public ActionForward agregarCierreComercio (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CIERRE_COMERCIO);
		UserSession userSession =getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			
			
			// Si es nulo no se puede continuar
			if (cierreComercioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cierreComercioAdapterVO, request);
			
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// llamada al servicio
			cierreComercioAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().createCierreComercio(userSession, cierreComercioAdapterVO);
			
			cierreComercioAdapterVO.setCierreComercio(cierreComercioAdapterVO.getCierreComercio());
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cierreComercioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreComercioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			// lo mantiene en la sesion
			userSession.put(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			
			saveDemodaMessages(request, cierreComercioAdapterVO);
			
			// Fue Exitoso
			//return forwardConfirmarOk(mapping, request, funcName, CierreComercioAdapter.NAME);
			return mapping.findForward(GdeConstants.FWD_CIERRE_COMERCIO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}
	}
	
	public ActionForward paramMotivoCierre (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (cierreComercioAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cierreComercioAdapterVO, request);
				
	            // Tiene errores recuperables
				if (cierreComercioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
					saveDemodaErrors(request, cierreComercioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				}
				
				// llamada al servicio
				cierreComercioAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getCierreComercioAdapterParamMotivoCierre(userSession, cierreComercioAdapterVO);
				
	            // Tiene errores recuperables
				if (cierreComercioAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
					saveDemodaErrors(request, cierreComercioAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (cierreComercioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cierreComercioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_CIERRE_COMERCIO);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
			}
		}

	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioAdapter adapterVO = (CierreComercioAdapter)userSession.get(CierreComercioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCierreComercio().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCierreComercio()); 
			
			adapterVO.getCierreComercio().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(CierreComercioAdapter.NAME, adapterVO);
			
			ActionForward forward = mapping.findForward( GdeConstants.FWD_CIERRE_COMERCIO);
			
			if(adapterVO.getCierreDefinitivo())
				forward=mapping.findForward(GdeConstants.FWD_CIERRECOMERCIO_EDIT_ADAPTER);
			
			return forward; 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}	
	}

	public ActionForward validarCasoNoEmiMul(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioAdapter adapterVO = (CierreComercioAdapter)userSession.get(CierreComercioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCierreComercio().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCierreComercio().getCasoNoEmiMul()); 
			
			adapterVO.getCierreComercio().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(CierreComercioAdapter.NAME, adapterVO);
			
			return mapping.findForward( GdeConstants.FWD_CIERRE_COMERCIO); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}	
	}

	public ActionForward modificarCierreComercio (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CIERRE_COMERCIO);
		UserSession userSession =getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			
			
			// Si es nulo no se puede continuar
			if (cierreComercioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cierreComercioAdapterVO, request);
			
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// llamada al servicio
			cierreComercioAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().updateCierreComercio(userSession, cierreComercioAdapterVO);
		
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cierreComercioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreComercioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			// lo mantiene en la sesion
			userSession.put(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			
			saveDemodaMessages(request, cierreComercioAdapterVO);
			
			// Fue Exitoso
			//return forwardConfirmarOk(mapping, request, funcName, CierreComercioAdapter.NAME);
			return mapping.findForward(GdeConstants.FWD_CIERRE_COMERCIO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}
	}

	public ActionForward inicioCierreComercio (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CIERRE_COMERCIO);
		UserSession userSession =getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			
			
			// Si es nulo no se puede continuar
			if (cierreComercioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cierreComercioAdapterVO, request);
			
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// llamada al servicio
			cierreComercioAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().inicioCierreComercio(userSession, cierreComercioAdapterVO);
			
			cierreComercioAdapterVO.setCierreComercio(cierreComercioAdapterVO.getCierreComercio());
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cierreComercioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreComercioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + cierreComercioAdapterVO.getIdCuenta() + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
		
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}
	}

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CIERRECOMERCIO, act); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CierreComercioAdapter cierreComercioAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (act.equals(BaseConstants.ACT_VER)) {
					stringServicio = "getCierreComercioAdapterForView(userSession, commonKey)";
					cierreComercioAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getCierreComercioAdapterForView(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_CIERRECOMERCIO_VIEW_ADAPTER);
				}
				if (act.equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getCierreComercioAdapterForUpdate(userSession, commonKey)";
					cierreComercioAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getCierreComercioAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_CIERRECOMERCIO_EDIT_ADAPTER);
				}
							
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (cierreComercioAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cierreComercioAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				cierreComercioAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + CierreComercioAdapter.NAME + ": "+ cierreComercioAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CierreComercioAdapter.NAME, cierreComercioAdapterVO);
				 
				saveDemodaMessages(request, cierreComercioAdapterVO);
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
			}
		}

	public ActionForward modificarCierreDefComercio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CIERRECOMERCIO, BaseSecurityConstants.MODIFICAR);
		UserSession userSession =getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cierreComercioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cierreComercioAdapterVO, request);
			
            // Tiene errores recuperables
			if (cierreComercioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// llamada al servicio
			CierreComercioVO cierreComercioVO = GdeServiceLocator.getGdeGDeudaAutoService().updateCierreComercio(userSession, cierreComercioAdapterVO.getCierreComercio());
			
            // Tiene errores recuperables
			if (cierreComercioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioAdapterVO.infoString()); 
				saveDemodaErrors(request, cierreComercioVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cierreComercioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreComercioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioAdapter.NAME, cierreComercioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CierreComercioAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioAdapter.NAME);
		}
	}
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CierreComercioAdapter.NAME);
		
	}	
	

	public ActionForward irImprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_IMPRIMIR_CIERRE_COMERCIO);
	
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		
		try {
			//CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			
			//cierreComercioAdapterVO.getCierreComercio().getCuentaVO().getId();
			
			
			//Long cuentaId = new Long(request.getParameter("cuentaId"));
			
			Long cuentaId = new Long(request.getParameter("selectedId"));
			
			PrintModel print = GdeServiceLocator.getGestionDeudaService().imprimirCierreComercio(userSession, cuentaId);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
}
