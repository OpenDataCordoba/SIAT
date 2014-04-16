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

import ar.gov.rosario.siat.bal.iface.model.DupliceAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarDupliceDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDupliceDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DUPLICE, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			DupliceAdapter dupliceAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getDupliceAdapterForView(userSession, commonKey)";
					dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterForView(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DUPLICE_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_IMPUTAR)) {
					stringServicio = "getDupliceAdapterForImputar(userSession, commonKey)";		
					dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterForImputar(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DUPLICE_IMPUTAR_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_GENERAR_SALDO_A_FAVOR)) {
					stringServicio = "getDupliceAdapterForView(userSession, commonKey)";
					dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterForGenSaldo(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DUPLICE_GEN_SALDO_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getPartidaAdapterForView(userSession, commonKey)";
					dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterForView(userSession, commonKey);
					dupliceAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DUPLICE_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_DUPLICE_VIEW_ADAPTER);				
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getDupliceAdapterForCreate(userSession)";
					dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterForCreate(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_DUPLICE_EDIT_ADAPTER);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (dupliceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + dupliceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				dupliceAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					DupliceAdapter.NAME + ": " + dupliceAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DupliceAdapter.NAME, dupliceAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DupliceAdapter.NAME, dupliceAdapterVO);
				
				saveDemodaMessages(request, dupliceAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, DupliceAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DupliceAdapter.NAME);
			
	}


	public ActionForward imputar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DUPLICE, BalSecurityConstants.IMPUTAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DupliceAdapter dupliceAdapterVO = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (dupliceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(dupliceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (dupliceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, dupliceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				IndetVO indetVO = BalServiceLocator.getCompensacionService().imputarDuplice(userSession, dupliceAdapterVO);
			
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, indetVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + dupliceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DupliceAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
			}
	}

	public ActionForward genSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DUPLICE, BalSecurityConstants.GENERAR_SALDO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DupliceAdapter dupliceAdapterVO = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (dupliceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(dupliceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (dupliceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, dupliceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO, BalConstants.FWD_DUPLICE_GEN_SALDO_ADAPTER);
				}
				
				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getCompensacionService().genSaldoAFavorForDuplice(userSession, dupliceAdapterVO);
			
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, indetVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO, BalConstants.FWD_DUPLICE_GEN_SALDO_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + dupliceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DupliceAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
			}
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
		navModel.setSelectAction("/bal/AdministrarDuplice");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		DupliceAdapter  dupliceAdapter = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (dupliceAdapter == null) {
			log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(dupliceAdapter, request);
		
        // Tiene errores recuperables
		if (dupliceAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + dupliceAdapter.infoString()); 
			saveDemodaErrors(request, dupliceAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapter);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(dupliceAdapter.getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(dupliceAdapter.getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
	}
	
	public ActionForward paramCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DupliceAdapter dupliceAdapterVO = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (dupliceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();

			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(DupliceAdapter.NAME, dupliceAdapterVO);
				
				if(dupliceAdapterVO.getActForParamCuenta().equals(BalConstants.ACT_GENERAR_SALDO_A_FAVOR)){
					return mapping.findForward(BalConstants.FWD_DUPLICE_GEN_SALDO_ADAPTER);
				}else{
					return mapping.findForward(BalConstants.FWD_DUPLICE_IMPUTAR_ADAPTER);				
				}
			}
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			dupliceAdapterVO.getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterParamCuenta(userSession, dupliceAdapterVO);
			
			// Tiene errores recuperables
			if (dupliceAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
				saveDemodaErrors(request, dupliceAdapterVO);
				if(dupliceAdapterVO.getActForParamCuenta().equals(BalConstants.ACT_GENERAR_SALDO_A_FAVOR)){
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO, BalConstants.FWD_DUPLICE_GEN_SALDO_ADAPTER);
				}else{
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO);
				}
			}
			
			// Tiene errores no recuperables
			if (dupliceAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + dupliceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, dupliceAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DupliceAdapter.NAME, dupliceAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DupliceAdapter.NAME, dupliceAdapterVO);
			
			if(dupliceAdapterVO.getActForParamCuenta().equals(BalConstants.ACT_GENERAR_SALDO_A_FAVOR)){
				return mapping.findForward(BalConstants.FWD_DUPLICE_GEN_SALDO_ADAPTER);
			}else{
				return mapping.findForward(BalConstants.FWD_DUPLICE_IMPUTAR_ADAPTER);				
			}
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
		}
	}
	
	public ActionForward paramActualizar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DupliceAdapter dupliceAdapterVO = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (dupliceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(dupliceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (dupliceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, dupliceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Llamada al servicio
				dupliceAdapterVO = BalServiceLocator.getCompensacionService().getDupliceAdapterParamActualizar(userSession, dupliceAdapterVO);
				
	            // Tiene errores recuperables
				if (dupliceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, dupliceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (dupliceAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + dupliceAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, dupliceAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(DupliceAdapter.NAME, dupliceAdapterVO);
				// Subo el adapter al userSession
				userSession.put(DupliceAdapter.NAME, dupliceAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_DUPLICE_IMPUTAR_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
			}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_PARTIDA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DupliceAdapter dupliceAdapterVO = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (dupliceAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
			}

			// llamada al servicio
			IndetVO dupliceVO = BalServiceLocator.getCompensacionService().deleteDuplice(userSession, dupliceAdapterVO.getDuplice());
			
			
            // Tiene errores recuperables
			if (dupliceVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + dupliceAdapterVO.infoString());
				saveDemodaErrors(request, dupliceVO);				
				request.setAttribute(DupliceAdapter.NAME, dupliceAdapterVO);
				return mapping.findForward(BalConstants.FWD_DUPLICE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (dupliceVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + dupliceAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, dupliceAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DupliceAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DUPLICE, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DupliceAdapter DupliceAdapterVO = (DupliceAdapter) userSession.get(DupliceAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (DupliceAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DupliceAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DupliceAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(DupliceAdapterVO, request);
				
	            // Tiene errores recuperables
				if (DupliceAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + DupliceAdapterVO.infoString()); 
					saveDemodaErrors(request, DupliceAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, DupliceAdapterVO);
				}
				
				// llamada al servicio
				IndetVO DupliceVO = BalServiceLocator.getCompensacionService().createDuplice(userSession, DupliceAdapterVO.getDuplice());
				
	            // Tiene errores recuperables
				if (DupliceVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + DupliceVO.infoString()); 
					saveDemodaErrors(request, DupliceVO);
					return forwardErrorRecoverable(mapping, request, userSession, DupliceAdapter.NAME, DupliceAdapterVO, BalConstants.FWD_DUPLICE_EDIT_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (DupliceVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + DupliceVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DupliceAdapter.NAME, DupliceAdapterVO);
				}

				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DupliceAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DupliceAdapter.NAME);
			}
	}
}
