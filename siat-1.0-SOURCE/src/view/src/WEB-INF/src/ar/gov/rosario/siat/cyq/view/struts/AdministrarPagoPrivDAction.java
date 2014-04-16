//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivAdapter;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivDeuVO;
import ar.gov.rosario.siat.cyq.iface.model.PagoPrivVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarPagoPrivDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPagoPrivDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PAGOPRIV, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PagoPrivAdapter pagoPrivAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getPagoPrivAdapterForView(userSession, commonKey)";
				pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PAGOPRIV_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPagoPrivAdapterForUpdate(userSession, commonKey)";
				pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PAGOPRIV_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getPagoPrivAdapterForView(userSession, commonKey)";
				pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterForView(userSession, commonKey);				
				actionForward = mapping.findForward(CyqConstants.FWD_PAGOPRIV_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getPagoPrivAdapterForCreate(userSession)";
				
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");
				
				pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterForCreate(userSession, commonKey, listIdDeudaSelected);
				actionForward = mapping.findForward(CyqConstants.FWD_PAGOPRIV_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (pagoPrivAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adaptertrue
			pagoPrivAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PagoPrivAdapter.NAME + ": "+ pagoPrivAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			 
			saveDemodaMessages(request, pagoPrivAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PAGOPRIV, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(pagoPrivAdapterVO, request);
			
			for (PagoPrivDeuVO ppv: pagoPrivAdapterVO.getPagoPriv().getListPagoPrivDeu()){
				
				log.debug( request.getParameter("importe" + ppv.getId()));
				
				
			}
			
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// llamada al servicio
			pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().createPagoPriv(userSession, pagoPrivAdapterVO);
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PagoPrivAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PAGOPRIV, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(pagoPrivAdapterVO, request);
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// llamada al servicio
			PagoPrivVO pagoPrivVO = CyqServiceLocator.getConcursoyQuiebraService().updatePagoPriv(userSession, pagoPrivAdapterVO.getPagoPriv());
			
            // Tiene errores recuperables
			if (pagoPrivVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PagoPrivAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PAGOPRIV, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// llamada al servicio
			PagoPrivVO pagoPrivVO = CyqServiceLocator.getConcursoyQuiebraService().deletePagoPriv(userSession, pagoPrivAdapterVO.getPagoPriv());
			
            // Tiene errores recuperables
			if (pagoPrivVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString());
				saveDemodaErrors(request, pagoPrivVO);				
				request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PAGOPRIV_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PagoPrivAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}
	
	public ActionForward generarOIT(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PAGOPRIV, CyqSecurityConstants.MTD_GENERAR_OIT); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// llamada al servicio
			PagoPrivVO pagoPrivVO = CyqServiceLocator.getConcursoyQuiebraService().generarOIT(userSession, pagoPrivAdapterVO.getPagoPriv());
			
            // Tiene errores recuperables
			if (pagoPrivVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString());
				saveDemodaErrors(request, pagoPrivVO);				
				request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PAGOPRIV_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PagoPrivAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PagoPrivAdapter.NAME);
		
	}
	

	public ActionForward buscarCuentaBanco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (pagoPrivAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(pagoPrivAdapterVO, request);
				
        // Tiene errores recuperables
		if (pagoPrivAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
			saveDemodaErrors(request, pagoPrivAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
		}
		
		return forwardSeleccionar(mapping, request, CyqConstants.METOD_PARAM_CUENTABANCO, BalConstants.ACTION_BUSCAR_CUENTABANCO, false);

	}
	
	
	public ActionForward paramCuentaBanco (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			//bajo el adapter del usserSession
			PagoPrivAdapter pagoPrivAdapterVO =  (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PAGOPRIV_EDIT_ADAPTER); 
			}

			// Seteo el id atributo seleccionado
			pagoPrivAdapterVO.getPagoPriv().getCuentaBanco().setId(new Long(selectedId));
			
			// llamo al param del servicio
			pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterParamCuentaBanco(userSession, pagoPrivAdapterVO);

            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, pagoPrivAdapterVO);

			// Envio el VO al request
			request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);

			return mapping.findForward(CyqConstants.FWD_PAGOPRIV_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}
		
	public ActionForward paramCancelaDeuda (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(pagoPrivAdapterVO, request);
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// llamada al servicio
			pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterParamCancelaDeuda(userSession, pagoPrivAdapterVO);
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_PAGOPRIV_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}
	
	
	
	public ActionForward paramTipoCancelacion (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PagoPrivAdapter pagoPrivAdapterVO = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(pagoPrivAdapterVO, request);
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// llamada al servicio
			//pagoPrivAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPagoPrivAdapterParamCancelaDeuda(userSession, pagoPrivAdapterVO);
			
            // Tiene errores recuperables
			if (pagoPrivAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + pagoPrivAdapterVO.infoString()); 
				saveDemodaErrors(request, pagoPrivAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (pagoPrivAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + pagoPrivAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PagoPrivAdapter.NAME, pagoPrivAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_PAGOPRIV_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}

	public ActionForward imprimirRecibo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			PagoPrivAdapter pagoPrivAdapter = (PagoPrivAdapter) userSession.get(PagoPrivAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (pagoPrivAdapter == null) {
				log.error("error en: "  + funcName + ": " + PagoPrivAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PagoPrivAdapter.NAME); 
			}
			
			// Llamada al servicio
			PrintModel print = CyqServiceLocator.getConcursoyQuiebraService().imprimirRecibo(userSession, pagoPrivAdapter);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PagoPrivAdapter.NAME);
		}
	}
	
}
