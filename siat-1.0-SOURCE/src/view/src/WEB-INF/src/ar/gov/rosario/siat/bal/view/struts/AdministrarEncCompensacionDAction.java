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

import ar.gov.rosario.siat.bal.iface.model.CompensacionAdapter;
import ar.gov.rosario.siat.bal.iface.model.CompensacionVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarEncCompensacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncCompensacionDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION_ENC, act);		

			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CompensacionAdapter compensacionAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());

				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getCompensacionAdapterForUpdate(userSession, commonKey)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForUpdate(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_ENC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getCompensacionAdapterForCreate(userSession)";
					compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterForCreate(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_COMPENSACION_ENC_EDIT_ADAPTER);
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (compensacionAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				compensacionAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + CompensacionAdapter.ENC_NAME + ": "+ compensacionAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				// Subo el apdater al userSession
				userSession.put(CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.ENC_NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_COMPENSACION_ENC, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (compensacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CompensacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(compensacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (compensacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString()); 
					saveDemodaErrors(request, compensacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}
				
				// llamada al servicio
				CompensacionVO compensacionVO = BalServiceLocator.getCompensacionService().createCompensacion(userSession, compensacionAdapterVO.getCompensacion());
				
	            // Tiene errores recuperables
				if (compensacionVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionVO.infoString()); 
					saveDemodaErrors(request, compensacionVO);
					return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (compensacionVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + compensacionVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}

				return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.ENC_NAME);
									
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.ENC_NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_COMPENSACION_ENC, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.ENC_NAME);
				
				// Si es nulo no se puede continuar
				if (compensacionAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CompensacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ENC_NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(compensacionAdapterVO, request);
				
	            // Tiene errores recuperables
				if (compensacionAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString()); 
					saveDemodaErrors(request, compensacionAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}
				
				// llamada al servicio
				CompensacionVO compensacionVO = BalServiceLocator.getCompensacionService().updateCompensacion(userSession, compensacionAdapterVO.getCompensacion());
				
	            // Tiene errores recuperables
				if (compensacionVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString()); 
					saveDemodaErrors(request, compensacionVO);
					return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (compensacionVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, CompensacionAdapter.ENC_NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CompensacionAdapter.ENC_NAME);
			}
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CompensacionAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CompensacionAdapter.ENC_NAME);
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/bal/AdministrarEncCompensacion");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		CompensacionAdapter  compensacionAdapter = (CompensacionAdapter) userSession.get(CompensacionAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (compensacionAdapter == null) {
			log.error("error en: "  + funcName + ": " + CompensacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ENC_NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(compensacionAdapter, request);
		
        // Tiene errores recuperables
		if (compensacionAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + compensacionAdapter.infoString()); 
			saveDemodaErrors(request, compensacionAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ENC_NAME, compensacionAdapter);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(compensacionAdapter.getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(compensacionAdapter.getCompensacion().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
		
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter) userSession.get(CompensacionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ENC_NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			
			// Si el id esta vacio, pq selecciono volver, forwardeo al SearchPage
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_COMPENSACION_ENC_EDIT_ADAPTER);				
			}
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			compensacionAdapterVO.getCompensacion().getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			compensacionAdapterVO = BalServiceLocator.getCompensacionService().getCompensacionAdapterParamCuenta(userSession, compensacionAdapterVO);
			
            // Tiene errores recuperables
			if (compensacionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionAdapterVO.infoString()); 
				saveDemodaErrors(request, compensacionAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (compensacionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_COMPENSACION_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionAdapter.ENC_NAME);
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
			CompensacionAdapter compensacionAdapterVO = (CompensacionAdapter)userSession.get(CompensacionAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(compensacionAdapterVO, request);
			
			log.debug( funcName + " " + compensacionAdapterVO.getCompensacion().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, compensacionAdapterVO.getCompensacion()); 
			
			compensacionAdapterVO.getCompensacion().passErrorMessages(compensacionAdapterVO);
		    
		    saveDemodaMessages(request, compensacionAdapterVO);
		    saveDemodaErrors(request, compensacionAdapterVO);
		    
			request.setAttribute(CompensacionAdapter.ENC_NAME, compensacionAdapterVO);
			
			return mapping.findForward( BalConstants.FWD_COMPENSACION_ENC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionAdapter.ENC_NAME);
		}	
	}
	
}
