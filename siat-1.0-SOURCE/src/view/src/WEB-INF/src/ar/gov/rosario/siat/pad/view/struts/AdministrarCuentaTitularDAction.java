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
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCuentaTitularDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCuentaTitularDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTATITULAR, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CuentaTitularAdapter cuentaTitularAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCuentaTitularAdapterForView(userSession, commonKey)";
				cuentaTitularAdapterVO = PadServiceLocator.getCuentaService().getCuentaTitularAdapterForView(userSession, commonKey); 
				actionForward = mapping.findForward(PadConstants.FWD_CUENTATITULAR_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getCuentaTitularAdapterForUpdate(userSession, commonKey)";
				cuentaTitularAdapterVO = PadServiceLocator.getCuentaService().getCuentaTitularAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTATITULAR_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCuentaTitularAdapterForView(userSession, commonKey)";
				cuentaTitularAdapterVO = PadServiceLocator.getCuentaService().getCuentaTitularAdapterForView(userSession, commonKey); 
				actionForward = mapping.findForward(PadConstants.FWD_CUENTATITULAR_VIEW_ADAPTER);
				
				cuentaTitularAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.CUENTATITULAR_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTATITULAR_VIEW_ADAPTER);					
			}
						
			if (act.equals(PadConstants.ACT_MARCAR_PRINCIPAL)) {
				stringServicio = "getCuentaTitularAdapterForView(userSession, commonKey)";
				cuentaTitularAdapterVO = PadServiceLocator.getCuentaService().getCuentaTitularAdapterForView(userSession, commonKey);				
				cuentaTitularAdapterVO.addMessageValue("Va a marcar a este contribuyente como titular principal de la cuenta");
				actionForward = mapping.findForward(PadConstants.FWD_CUENTATITULAR_VIEW_ADAPTER);
			}
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCuentaTitularAdapterForCreate(userSession)";
				
				// Bajo el adapter del userSession para obtener el id de la cuenta
				CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CuentaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
				}
				
				CommonKey ckCuenta = new CommonKey(cuentaAdapterVO.getCuenta().getId());
				cuentaTitularAdapterVO = PadServiceLocator.getCuentaService().getCuentaTitularAdapterForCreate(userSession, ckCuenta, commonKey );
				
				saveDemodaErrors(request, cuentaTitularAdapterVO);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTATITULAR_EDIT_ADAPTER);				
			}
		
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + 
				stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cuentaTitularAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + cuentaTitularAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cuentaTitularAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				CuentaTitularAdapter.NAME + ": "+ cuentaTitularAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			 
			saveDemodaMessages(request, cuentaTitularAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME); // no es el CuentaTitularAdapter
		}
	}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaTitularAdapter cuentaTitularAdapterVO = (CuentaTitularAdapter) userSession.get(CuentaTitularAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaTitularAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaTitularAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaTitularAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaTitularAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaTitularAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaTitularAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// llamada al servicio
			CuentaTitularVO cuentaTitularVO = PadServiceLocator.getCuentaService().updateCuentaTitular(userSession, cuentaTitularAdapterVO.getCuentaTitular());
			
            // Tiene errores recuperables
			if (cuentaTitularVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularVO.infoString()); 
				saveDemodaErrors(request, cuentaTitularVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaTitularVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaTitularVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaTitularAdapter.NAME, cuentaTitularVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaTitularAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaTitularAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaTitularAdapter cuentaTitularAdapterVO = (CuentaTitularAdapter) userSession.get(CuentaTitularAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaTitularAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaTitularAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaTitularAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaTitularAdapterVO, request);

            // Tiene errores recuperables
			if (cuentaTitularAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaTitularAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// llamada al servicio
			CuentaTitularVO cuentaTitularVO = PadServiceLocator.getCuentaService().createCuentaTitular(userSession, cuentaTitularAdapterVO.getCuentaTitular());
			
            // Tiene errores recuperables
			if (cuentaTitularVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaTitularVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaTitularVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaTitularAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaTitularAdapter.NAME, cuentaTitularVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaTitularAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaTitularAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTATITULAR, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaTitularAdapter cuentaTitularAdapterVO = (CuentaTitularAdapter) userSession.get(CuentaTitularAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaTitularAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaTitularAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaTitularAdapter.NAME); 
			}

			// llamada al servicio
			CuentaTitularVO cuentaTitularVO = PadServiceLocator.getCuentaService().deleteCuentaTitular
				(userSession, cuentaTitularAdapterVO.getCuentaTitular());
			
            // Tiene errores recuperables
			if (cuentaTitularVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularAdapterVO.infoString());
				saveDemodaErrors(request, cuentaTitularVO);				
				request.setAttribute(CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTATITULAR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (cuentaTitularVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaTitularAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaTitularAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaTitularAdapter.NAME);
		}
	}


	public ActionForward marcarTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTATITULAR, PadSecurityConstants.MTD_MARCAR_PRINCIPAL); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CuentaTitularAdapter cuentaTitularAdapterVO = (CuentaTitularAdapter) userSession.get(CuentaTitularAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaTitularAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaTitularAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaTitularAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaTitularAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaTitularAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaTitularAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// llamada al servicio
			CuentaTitularVO cuentaTitularVO = PadServiceLocator.getCuentaService().marcarTitularPrincipal(userSession, cuentaTitularAdapterVO.getCuentaTitular());
			
            // Tiene errores recuperables
			if (cuentaTitularVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaTitularVO.infoString()); 
				saveDemodaErrors(request, cuentaTitularVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaTitularAdapter.NAME, cuentaTitularAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaTitularVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaTitularVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaTitularAdapter.NAME, cuentaTitularVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaTitularAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaTitularAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentaTitularAdapter.NAME);
		
	}

	
}
