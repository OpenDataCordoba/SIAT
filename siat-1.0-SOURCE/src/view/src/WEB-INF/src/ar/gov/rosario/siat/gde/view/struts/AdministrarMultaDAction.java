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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.CierreComercioAdapter;
import ar.gov.rosario.siat.gde.iface.model.MultaAdapter;
import ar.gov.rosario.siat.gde.iface.model.MultaHistoricoVO;
import ar.gov.rosario.siat.gde.iface.model.MultaVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;


public final class AdministrarMultaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarMultaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		MultaAdapter multaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getMultaAdapterForView(userSession, commonKey)";
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getMultaAdapterForUpdate(userSession, commonKey)";
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getMultaAdapterForView(userSession, commonKey)";
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterForView(userSession, commonKey);				
				multaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.MULTA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getMultaAdapterForCreate(userSession)";
				Long idCuenta;
				multaAdapterVO = new MultaAdapter();
				
				if(!StringUtil.isNullOrEmpty(request.getParameter("idCuenta"))/*request.getParameter("idCuenta")!=null*/){
					idCuenta = new Long(request.getParameter("idCuenta"));
				}else{
					CierreComercioAdapter cierreComercio = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
					idCuenta=cierreComercio.getIdCuenta();
					multaAdapterVO.setListMulta(cierreComercio.getListMulta());
					multaAdapterVO.setEnCierreComercio(true);
					
				}				
				multaAdapterVO.setIdCuenta(idCuenta);
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterForCreate(userSession,multaAdapterVO);
				
				actionForward = mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getMultaAdapterForView(userSession)";
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterForView(userSession, commonKey);
				multaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, GdeError.MULTA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getMultaAdapterForView(userSession)";
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterForView(userSession, commonKey);
				multaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, GdeError.MULTA_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (multaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + multaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			multaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + MultaAdapter.NAME + ": "+ multaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MultaAdapter.NAME, multaAdapterVO);
			 
			saveDemodaMessages(request, multaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (multaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(multaAdapterVO, request);
			
            // Tiene errores recuperables
			if (multaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
				saveDemodaErrors(request, multaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// llamada al servicio
			MultaVO multaVO = GdeServiceLocator.getGdeGDeudaAutoService().createMulta(userSession, multaAdapterVO.getMulta());
			multaAdapterVO.getListMulta().add(multaVO);
			
            // Tiene errores recuperables
			if (multaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaVO.infoString()); 
				saveDemodaErrors(request, multaVO);
				return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (multaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + multaVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			CierreComercioAdapter cierreComercio = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			if(cierreComercio!=null){
				//Seteamos los parametros para volver a cierre comercio
				multaAdapterVO.setPrevAction(GdeConstants.PATH_ADMINISTRAR_CIERRE_COMERCIO);
				multaAdapterVO.setPrevActionParameter(BaseConstants.ACT_REFILL);
				
				cierreComercio.setListMulta(multaAdapterVO.getListMulta());
			}
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MultaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (multaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(multaAdapterVO, request);
			
            // Tiene errores recuperables
			if (multaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
				saveDemodaErrors(request, multaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// llamada al servicio
			MultaVO multaVO = GdeServiceLocator.getGdeGDeudaAutoService().updateMulta(userSession, multaAdapterVO.getMulta());
			
            // Tiene errores recuperables
			if (multaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
				saveDemodaErrors(request, multaVO);
				return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (multaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MultaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (multaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
			}

			// llamada al servicio
			MultaVO multaVO = GdeServiceLocator.getGdeGDeudaAutoService().deleteMulta
				(userSession, multaAdapterVO.getMulta());
			
            // Tiene errores recuperables
			if (multaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString());
				saveDemodaErrors(request, multaVO);				
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (multaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			CierreComercioAdapter cierreComercio = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			if(cierreComercio!=null){
				//Seteamos los parametros para volver a cierre comercio
				multaAdapterVO.setPrevAction(GdeConstants.PATH_ADMINISTRAR_CIERRE_COMERCIO);
				multaAdapterVO.setPrevActionParameter(BaseConstants.ACT_REFILL);
				/*MultaVO multa = new MultaVO(-1,"");
				cierreComercio.setMulta(multa);*/
			}
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MultaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (multaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
			}

			// llamada al servicio
			MultaVO multaVO = GdeServiceLocator.getGdeGDeudaAutoService().activarMulta
				(userSession, multaAdapterVO.getMulta());
			
            // Tiene errores recuperables
			if (multaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString());
				saveDemodaErrors(request, multaVO);				
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (multaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MultaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (multaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
			}

			// llamada al servicio
			MultaVO multaVO = GdeServiceLocator.getGdeGDeudaAutoService().desactivarMulta
				(userSession, multaAdapterVO.getMulta());
			
            // Tiene errores recuperables
			if (multaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString());
				saveDemodaErrors(request, multaVO);				
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				return mapping.findForward(GdeConstants.FWD_MULTA_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (multaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, MultaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MultaAdapter.NAME);
		
	}
	
	public ActionForward paramTipoMulta (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (multaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(multaAdapterVO, request);
			
            // Tiene errores recuperables
			if (multaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
				saveDemodaErrors(request, multaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// llamada al servicio
			multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterParamTipoMulta(userSession, multaAdapterVO);
			
            // Tiene errores recuperables
			if (multaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
				saveDemodaErrors(request, multaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (multaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(MultaAdapter.NAME, multaAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
		}
	}
		
	public ActionForward paramDetalleMulta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(multaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (multaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
					saveDemodaErrors(request, multaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
				}
				
				// llamada al servicio
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterParamDetalleMulta(userSession, multaAdapterVO);
				
	            // Tiene errores recuperables
				if (multaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
					saveDemodaErrors(request, multaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (multaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(MultaAdapter.NAME, multaAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
			}
		}
	public ActionForward paramDescuentoMulta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(multaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (multaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
					saveDemodaErrors(request, multaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
				}
				
				// llamada al servicio
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().getMultaAdapterParamDescuentoMulta(userSession, multaAdapterVO);
				
	            // Tiene errores recuperables
				if (multaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + multaAdapterVO.infoString()); 
					saveDemodaErrors(request, multaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, MultaAdapter.NAME, multaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (multaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + multaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, MultaAdapter.NAME, multaAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(MultaAdapter.NAME, multaAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
			}
		}
		
		public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter)userSession.get(MultaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}
				
				// Populate como en un buscar
				DemodaUtil.populateVO(multaAdapterVO, request);
				
				log.debug( funcName + " " + multaAdapterVO.getMulta().getCaso().infoString());
				
				// llamada al servicio
				if(!multaAdapterVO.isHistorico()){
					CasServiceLocator.getCasCasoService().validarCaso(userSession, multaAdapterVO.getMulta());
					multaAdapterVO.getMulta().passErrorMessages(multaAdapterVO);
				}else{
					CasServiceLocator.getCasCasoService().validarCaso(userSession, multaAdapterVO.getMultaHistorico());
					multaAdapterVO.getMultaHistorico().passErrorMessages(multaAdapterVO);
				}
			    
			    saveDemodaMessages(request, multaAdapterVO);
			    saveDemodaErrors(request, multaAdapterVO);
			    
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				
				if (multaAdapterVO.isHistorico())
					return mapping.findForward(GdeConstants.FWD_MULTAHISTORICO_ADAPTER);
				else
					return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER); 
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
			}	
		}
	 
		public ActionForward imprimir(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled())
				log.debug("entrando en " + funcName);

			//UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MULTA, GdeSecurityConstants.IMPRIMIR);
			UserSession userSession = getCurrentUserSession(request, mapping);
				
			if (userSession == null)
				return forwardErrorSession(request);
			String stringServicio = "imprimir";
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter) userSession.get(MultaAdapter.NAME);

				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}
				
				// llamada al servicio
					PrintModel print  = GdeServiceLocator.getGdeGDeudaAutoService().imprimirMulta(userSession, multaAdapterVO);
					
					baseResponsePrintModel(response, print);
					
					return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception,
						MultaAdapter.NAME);
			}
		}
		
		
		public ActionForward modificarPorcentaje(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter)userSession.get(MultaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}
				
				// Populate como en un buscar
				DemodaUtil.populateVO(multaAdapterVO, request);
				
				
				
				// llamada al servicio no hay
				
				
				multaAdapterVO.getMulta().getOrdenControl().passErrorMessages(multaAdapterVO);
				
				multaAdapterVO.setHistorico(true);
			    
			    saveDemodaMessages(request, multaAdapterVO);
			    saveDemodaErrors(request, multaAdapterVO);
			    
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				
				userSession.put(MultaAdapter.NAME, multaAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_MULTAHISTORICO_ADAPTER); 
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
			}	
		}
		
		public ActionForward volverDeHistorico(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter)userSession.get(MultaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}
				
				// Populate como en un buscar
				DemodaUtil.populateVO(multaAdapterVO, request);
				multaAdapterVO.setHistorico(false);
				multaAdapterVO.setMultaHistorico(new MultaHistoricoVO());
				
				
				// llamada al servicio no hay
				
			    
			    saveDemodaMessages(request, multaAdapterVO);
			    saveDemodaErrors(request, multaAdapterVO);
			    
			    
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				
				userSession.put(MultaAdapter.NAME, multaAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
			}
				
		}
		
		
		public ActionForward agregarMultaHistorico(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				MultaAdapter multaAdapterVO = (MultaAdapter)userSession.get(MultaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (multaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + MultaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, MultaAdapter.NAME); 
				}
				
				// Populate como en un buscar
				DemodaUtil.populateVO(multaAdapterVO, request);
				
				
				
				// llamada al servicio no hay
				multaAdapterVO = GdeServiceLocator.getGdeGDeudaAutoService().createMultaHistorico(userSession, multaAdapterVO);
				
				
			    
				if (multaAdapterVO.hasErrorRecoverable()){
					saveDemodaMessages(request, multaAdapterVO);
				    saveDemodaErrors(request, multaAdapterVO);
				    request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
					return mapping.findForward(GdeConstants.FWD_MULTAHISTORICO_ADAPTER);	
				}
				
				multaAdapterVO.getMulta().getOrdenControl().passErrorMessages(multaAdapterVO);
				//seteo la bandera historico a false ya que vuelve al adapter principal de multa
				multaAdapterVO.setHistorico(false);
			    
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				userSession.put(MultaAdapter.NAME, multaAdapterVO);
				
				
				userSession.put(MultaAdapter.NAME+"Enc", multaAdapterVO);
				
				multaAdapterVO.setPrevAction(GdeConstants.PATH_ADM_MULTA_REFILL);
				    
				multaAdapterVO.setPrevActionParameter(BaseConstants.ACT_REFILL);
				
				
				return forwardConfirmarOk(mapping, request, funcName, MultaAdapter.NAME);
				//return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, MultaAdapter.NAME);
			}	
		}
		
		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				
				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession==null) return forwardErrorSession(request);
				MultaAdapter multaAdapterVO = (MultaAdapter)userSession.get(MultaAdapter.NAME+"Enc");
				
				multaAdapterVO.setPrevAction(GdeConstants.PATH_BUSCAR_MULTA);
			    
			    multaAdapterVO.setPrevActionParameter(BaseConstants.ACT_BUSCAR);
				
				request.setAttribute(MultaAdapter.NAME, multaAdapterVO);
				userSession.put(MultaAdapter.NAME, multaAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_MULTA_EDIT_ADAPTER);
				
		}

}

