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

import ar.gov.rosario.siat.bal.iface.model.IndetAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.model.ReclamoAdapter;
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

public class AdministrarIndetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarIndetDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDET, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			IndetAdapter indetAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getIndetAdapterForView(userSession, commonKey)";
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForView	(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getIndetAdapterForCreate(userSession, commonKey)";
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForCreate(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_INDET_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getIndetAdapterForUpdate(userSession, commonKey)";		
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForView(userSession, commonKey);
					String paramReclamo = (String) userSession.get(ReclamoAdapter.PARAM_MAS_DATOS);
					if(paramReclamo != null && "true".equals(paramReclamo))
						indetAdapterVO.setParamReclamo(true);
					
					actionForward = mapping.findForward(BalConstants.FWD_INDET_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getIndetAdapterForView(userSession, commonKey)";
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForView(userSession, commonKey);
					indetAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.INDET_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BalConstants.ACT_REINGRESAR_INDET)) {
					stringServicio = "getIndetAdapterForView(userSession, commonKey)";
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForView(userSession, commonKey);
					indetAdapterVO.addMessage(BaseError.MSG_REINGRESAR_INDET, BalError.INDET_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BalConstants.ACT_DESGLOCE_INDET)) {
					stringServicio = "getIndetAdapterForView(userSession, commonKey)";
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForDesgloce(userSession, commonKey);
					indetAdapterVO.addMessage(BaseError.MSG_DESGLOCE_INDET, BalError.INDET_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BalConstants.ACT_GENERAR_SALDO_A_FAVOR)) {
					stringServicio = "getIndetAdapterForView(userSession, commonKey)";
					indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterForGenSaldo(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_INDET_GEN_SALDO_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (indetAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				indetAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					IndetAdapter.NAME + ": " + indetAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(IndetAdapter.NAME, indetAdapterVO);
				// Subo el apdater al userSession
				userSession.put(IndetAdapter.NAME, indetAdapterVO);
				
				saveDemodaMessages(request, indetAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
		}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_INDET, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(indetAdapterVO, request);
				
	            // Tiene errores recuperables
				if (indetAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().createIndet(userSession, indetAdapterVO.getIndet());
				
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetVO.infoString()); 
					saveDemodaErrors(request, indetVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}

				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_INDET, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(indetAdapterVO, request);
				
	            // Tiene errores recuperables
				if (indetAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().updateIndet(userSession, indetAdapterVO.getIndet());
				
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
	}

	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDET, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().deleteIndet
					(userSession, indetAdapterVO.getIndet());
				
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString());
					saveDemodaErrors(request, indetVO);				
					request.setAttribute(IndetAdapter.NAME, indetAdapterVO);
					return mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, IndetAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, IndetAdapter.NAME);
			
	}

	public ActionForward reingresar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDET, 
				BalSecurityConstants.ABM_INDET_REINGRESAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().reingresarIndet(userSession, indetAdapterVO.getIndet());
				
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString());
					saveDemodaErrors(request, indetVO);				
					request.setAttribute(IndetAdapter.NAME, indetAdapterVO);
					return mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
		}

	public ActionForward modificarYreingresar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_INDET, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(indetAdapterVO, request);
				
	            // Tiene errores recuperables
				if (indetAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().reingresarIndet(userSession, indetAdapterVO.getIndet());
			
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
	}
	
	public ActionForward desgloce(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDET, 
				BalSecurityConstants.ABM_INDET_DESGLOCE);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().desglozarIndet(userSession, indetAdapterVO.getIndet());
				
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString());
					saveDemodaErrors(request, indetVO);				
					request.setAttribute(IndetAdapter.NAME, indetAdapterVO);
					return mapping.findForward(BalConstants.FWD_INDET_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
			}
		}


	public ActionForward genSaldoAFavor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_INDET, BalSecurityConstants.GENERAR_SALDO);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(indetAdapterVO, request);
				
	            // Tiene errores recuperables
				if (indetAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO, BalConstants.FWD_INDET_GEN_SALDO_ADAPTER);
				}
				
				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().genSaldoAFavorForIndet(userSession, indetAdapterVO);
			
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
					saveDemodaErrors(request, indetVO);
					return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO, BalConstants.FWD_INDET_GEN_SALDO_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
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
		navModel.setSelectAction("/bal/AdministrarIndet");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		IndetAdapter  indetAdapter = (IndetAdapter) userSession.get(IndetAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (indetAdapter == null) {
			log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(indetAdapter, request);
		
        // Tiene errores recuperables
		if (indetAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + indetAdapter.infoString()); 
			saveDemodaErrors(request, indetAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapter);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(indetAdapter.getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(indetAdapter.getCuenta().getNumeroCuenta());
		
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
			IndetAdapter indetAdapterVO = (IndetAdapter) userSession.get(IndetAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (indetAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + IndetAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, IndetAdapter.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();

			// Si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(IndetAdapter.NAME, indetAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_INDET_GEN_SALDO_ADAPTER);
			}
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			indetAdapterVO.getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			indetAdapterVO = BalServiceLocator.getIndetService().getIndetAdapterParamCuenta(userSession, indetAdapterVO);
			
			// Tiene errores recuperables
			if (indetAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetAdapterVO.infoString()); 
				saveDemodaErrors(request, indetAdapterVO);

				return forwardErrorRecoverable(mapping, request, userSession, IndetAdapter.NAME, indetAdapterVO, BalConstants.FWD_INDET_GEN_SALDO_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (indetAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetAdapter.NAME, indetAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(IndetAdapter.NAME, indetAdapterVO);
			// Subo el apdater al userSession
			userSession.put(IndetAdapter.NAME, indetAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_INDET_GEN_SALDO_ADAPTER);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetAdapter.NAME);
		}
	}
}
