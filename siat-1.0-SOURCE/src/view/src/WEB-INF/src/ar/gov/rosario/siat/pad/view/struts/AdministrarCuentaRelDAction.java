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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaRelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaRelVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCuentaRelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCuentaRelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTAREL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CuentaRelAdapter cuentaRelAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCuentaRelAdapterForView(userSession, commonKey)";
				cuentaRelAdapterVO = PadServiceLocator.getCuentaService().getCuentaRelAdapterForView(userSession, commonKey); 
				actionForward = mapping.findForward(PadConstants.FWD_CUENTAREL_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCuentaRelAdapterForView(userSession, commonKey)";
				cuentaRelAdapterVO = PadServiceLocator.getCuentaService().getCuentaRelAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTAREL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCuentaRelAdapterForCreate(userSession)";
				cuentaRelAdapterVO = PadServiceLocator.getCuentaService().getCuentaRelAdapterForCreate(userSession, commonKey );
				actionForward = mapping.findForward(PadConstants.FWD_CUENTAREL_EDIT_ADAPTER);				
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCuentaRelAdapterForView(userSession, commonKey)";
				cuentaRelAdapterVO = PadServiceLocator.getCuentaService().getCuentaRelAdapterForView(userSession, commonKey); 
				actionForward = mapping.findForward(PadConstants.FWD_CUENTAREL_VIEW_ADAPTER);
				
				cuentaRelAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.CUENTAREL_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTAREL_VIEW_ADAPTER);					
			}
		
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + 
				stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cuentaRelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + cuentaRelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cuentaRelAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				CuentaRelAdapter.NAME + ": "+ cuentaRelAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			 
			saveDemodaMessages(request, cuentaRelAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME); 
		}
	}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaRelAdapter cuentaRelAdapterVO = (CuentaRelAdapter) userSession.get(CuentaRelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaRelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaRelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaRelAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaRelAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaRelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaRelAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaRelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// llamada al servicio
			CuentaRelVO cuentaRelVO = PadServiceLocator.getCuentaService().updateCuentaRel(userSession, cuentaRelAdapterVO.getCuentaRel());
			
            // Tiene errores recuperables
			if (cuentaRelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaRelVO.infoString()); 
				saveDemodaErrors(request, cuentaRelVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaRelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaRelVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaRelAdapter.NAME, cuentaRelVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaRelAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaRelAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaRelAdapter cuentaRelAdapterVO = (CuentaRelAdapter) userSession.get(CuentaRelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaRelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaRelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaRelAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaRelAdapterVO, request);

            // Tiene errores recuperables
			if (cuentaRelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaRelAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaRelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// llamada al servicio
			CuentaRelVO cuentaRelVO = PadServiceLocator.getCuentaService().createCuentaRel(userSession, cuentaRelAdapterVO.getCuentaRel());
			
            // Tiene errores recuperables
			if (cuentaRelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaRelAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaRelVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaRelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaRelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaRelAdapter.NAME, cuentaRelVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaRelAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaRelAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentaRelAdapter.NAME);
		
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
		navModel.setPrevAction(PadConstants.PATH_ADMINISTRAR_CUENTAREL);
		navModel.setPrevActionParameter("paramCuenta");

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/pad/AdministrarCuentaRel");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		CuentaRelAdapter  cuentaRelAdapter = (CuentaRelAdapter) userSession.get(CuentaRelAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (cuentaRelAdapter == null) {
			log.error("error en: "  + funcName + ": " + CuentaRelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentaRelAdapter.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cuentaRelAdapter, request);
		
        // Tiene errores recuperables
		if (cuentaRelAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cuentaRelAdapter.infoString()); 
			saveDemodaErrors(request, cuentaRelAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, CuentaRelAdapter.NAME, cuentaRelAdapter);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(cuentaRelAdapter.getCuentaRel().getCuentaDestino().getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(cuentaRelAdapter.getCuentaRel().getCuentaDestino().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		
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
			CuentaRelAdapter cuentaRelAdapterVO = (CuentaRelAdapter) userSession.get(CuentaRelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaRelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaRelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaRelAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			
			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(CuentaRelAdapter.NAME, cuentaRelAdapterVO);
				navModel.setSelectedId(cuentaRelAdapterVO.getCuentaRel().getCuentaOrigen().getId().toString());
				
				return mapping.findForward(PadConstants.FWD_CUENTAREL_EDIT_ADAPTER);				
			}
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			cuentaRelAdapterVO.getCuentaRel().getCuentaDestino().setId(commonKey.getId());
			
			// llamada al servicio
			cuentaRelAdapterVO = PadServiceLocator.getCuentaService().getCuentaRelAdapterParamCuenta(userSession, cuentaRelAdapterVO);
			
            // Tiene errores recuperables
			if (cuentaRelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaRelAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaRelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaRelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaRelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_CUENTAREL_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaRelAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTAREL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaRelAdapter cuentaRelAdapterVO = (CuentaRelAdapter) userSession.get(CuentaRelAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaRelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaRelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaRelAdapter.NAME); 
			}

			// llamada al servicio
			CuentaRelVO cuentaRelVO = PadServiceLocator.getCuentaService().deleteCuentaRel(userSession, cuentaRelAdapterVO.getCuentaRel());
			
            // Tiene errores recuperables
			if (cuentaRelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaRelAdapterVO.infoString());
				saveDemodaErrors(request, cuentaRelVO);				
				request.setAttribute(CuentaRelAdapter.NAME, cuentaRelAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTAREL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cuentaRelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaRelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaRelAdapter.NAME, cuentaRelAdapterVO);
			}
			
			cuentaRelAdapterVO.clearMessage();
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaRelAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaRelAdapter.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CuentaRelAdapter.NAME);
	}
}
