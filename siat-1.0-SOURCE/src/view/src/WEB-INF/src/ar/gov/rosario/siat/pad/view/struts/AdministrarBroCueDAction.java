//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.BroCueAdapter;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarBroCueDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarBroCueDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCUE, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			BroCueAdapter broCueAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getBroCueAdapterForView(userSession, commonKey)";
					broCueAdapterVO = PadServiceLocator.getDistribucionService().getBroCueAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_BROCUE_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getBroCueAdapterForUpdate(userSession, commonKey)";
					broCueAdapterVO = PadServiceLocator.getDistribucionService().getBroCueAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_BROCUE_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getBroCueAdapterForCreate(userSession)";
					broCueAdapterVO = PadServiceLocator.getDistribucionService().getBroCueAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(PadConstants.FWD_BROCUE_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (broCueAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + broCueAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				broCueAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + BroCueAdapter.NAME + ": "+ broCueAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(BroCueAdapter.NAME, broCueAdapterVO);
				// Subo el apdater al userSession
				userSession.put(BroCueAdapter.NAME, broCueAdapterVO);
				
				saveDemodaMessages(request, broCueAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BroCueAdapter.NAME);
			}
		}
		
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				PadSecurityConstants.ABM_BROCUE, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BroCueAdapter broCueAdapterVO = (BroCueAdapter) userSession.get(BroCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (broCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BroCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BroCueAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(broCueAdapterVO, request);
				
	            // Tiene errores recuperables
				if (broCueAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + broCueAdapterVO.infoString()); 
					saveDemodaErrors(request, broCueAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// llamada al servicio
				BroCueVO broCueVO = PadServiceLocator.getDistribucionService().createBroCue(userSession, broCueAdapterVO.getBroCue());
				
	            // Tiene errores recuperables
				if (broCueVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + broCueVO.infoString()); 
					saveDemodaErrors(request, broCueVO);
					return forwardErrorRecoverable(mapping, request, userSession, BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (broCueVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + broCueVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BroCueAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BroCueAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_BROCUE, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				BroCueAdapter broCueAdapterVO = (BroCueAdapter) userSession.get(BroCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (broCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + BroCueAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, BroCueAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(broCueAdapterVO, request);
				
	            // Tiene errores recuperables
				if (broCueAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + broCueAdapterVO.infoString()); 
					saveDemodaErrors(request, broCueAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// llamada al servicio
				BroCueVO broCueVO = PadServiceLocator.getDistribucionService().updateBroCue(userSession, broCueAdapterVO.getBroCue());
				
	            // Tiene errores recuperables
				if (broCueVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + broCueAdapterVO.infoString()); 
					saveDemodaErrors(request, broCueVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (broCueVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + broCueAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						BroCueAdapter.NAME, broCueAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, BroCueAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, BroCueAdapter.NAME);
			}
		}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, BroCueAdapter.NAME);
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// Bajo el adapter del userSession
		BroCueAdapter broCueAdapterVO = (BroCueAdapter) userSession.get(BroCueAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (broCueAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + BroCueAdapter.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, BroCueAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(broCueAdapterVO, request);
		
        // Tiene errores recuperables
		if (broCueAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + broCueAdapterVO.infoString()); 
			saveDemodaErrors(request, broCueAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, 
				BroCueAdapter.NAME, broCueAdapterVO);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(broCueAdapterVO.getBroCue().getBroche().getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(broCueAdapterVO.getBroCue().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		return forwardSeleccionar(mapping, request, 
				PadConstants.METOD_BROCUE_PARAM_CUENTA, PadConstants.ACTION_BUSCAR_CUENTA, false);
	
	}

	public ActionForward paramCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			BroCueAdapter broCueAdapterVO = (BroCueAdapter) userSession.get(BroCueAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (broCueAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + BroCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BroCueAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(BroCueAdapter.NAME, broCueAdapterVO);
				return mapping.findForward(PadConstants.FWD_BROCUE_EDIT_ADAPTER); 
			}

			// llamo al param del servicio
			broCueAdapterVO = PadServiceLocator.getDistribucionService().paramCuentaBroCue
				(userSession, broCueAdapterVO, new Long(selectedId));

            // Tiene errores recuperables
			if (broCueAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + broCueAdapterVO.infoString()); 
				saveDemodaErrors(request, broCueAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					BroCueAdapter.NAME, broCueAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (broCueAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + broCueAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					BroCueAdapter.NAME, broCueAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, broCueAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(BroCueAdapter.NAME, broCueAdapterVO);

			return mapping.findForward(PadConstants.FWD_BROCUE_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BroCueAdapter.NAME);
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
			BroCueAdapter adapterVO = (BroCueAdapter)userSession.get(BroCueAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + BroCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BroCueAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getBroCue().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getBroCue()); 
			
			adapterVO.getBroCue().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(BroCueAdapter.NAME, adapterVO);
			
			return mapping.findForward( PadConstants.FWD_BROCUE_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BroCueAdapter.NAME);
		}	
	}
}
