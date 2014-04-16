//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.view.struts;

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
import ar.gov.rosario.siat.frm.iface.model.FormularioAdapter;
import ar.gov.rosario.siat.frm.iface.model.FormularioVO;
import ar.gov.rosario.siat.frm.iface.service.FrmServiceLocator;
import ar.gov.rosario.siat.frm.iface.util.FrmSecurityConstants;
import ar.gov.rosario.siat.frm.view.util.FrmConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncFormularioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncFormularioDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, FrmSecurityConstants.ABM_FORMULARIO_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		FormularioAdapter formularioAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getFormularioAdapterForUpdate(userSession, commonKey)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getFormularioAdapterForCreate(userSession)";
				formularioAdapterVO = FrmServiceLocator.getFormularioService().getFormularioAdapterForCreate(userSession);
				actionForward = mapping.findForward(FrmConstants.FWD_FORMULARIO_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (formularioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + formularioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			formularioAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + FormularioAdapter.ENC_NAME + ": "+ formularioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(FormularioAdapter.ENC_NAME, formularioAdapterVO);
			// Subo el apdater al userSession
			userSession.put(FormularioAdapter.ENC_NAME, formularioAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			FrmSecurityConstants.ABM_FORMULARIO_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (formularioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormularioAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(formularioAdapterVO, request);
			
            // Tiene errores recuperables
			if (formularioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString()); 
				saveDemodaErrors(request, formularioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}
			
			// llamada al servicio
			FormularioVO formularioVO = FrmServiceLocator.getFormularioService().createFormulario(userSession, formularioAdapterVO.getFormulario());
			
            // Tiene errores recuperables
			if (formularioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioVO.infoString()); 
				saveDemodaErrors(request, formularioVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (formularioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formularioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, FrmSecurityConstants.ABM_FORMULARIO, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(formularioVO.getId().toString());

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.ENC_NAME, 
					FrmConstants.PATH_ADMINISTRAR_FORMULARIO, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			FrmSecurityConstants.ABM_FORMULARIO_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			FormularioAdapter formularioAdapterVO = (FormularioAdapter) userSession.get(FormularioAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (formularioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + FormularioAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, FormularioAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(formularioAdapterVO, request);
			
            // Tiene errores recuperables
			if (formularioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString()); 
				saveDemodaErrors(request, formularioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}
			
			// llamada al servicio
			FormularioVO formularioVO = FrmServiceLocator.getFormularioService().updateFormulario(userSession, formularioAdapterVO.getFormulario());
			
            // Tiene errores recuperables
			if (formularioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + formularioAdapterVO.infoString()); 
				saveDemodaErrors(request, formularioVO);
				return forwardErrorRecoverable(mapping, request, userSession, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (formularioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + formularioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, FormularioAdapter.ENC_NAME, formularioAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, FormularioAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, FormularioAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, FormularioAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, FormularioAdapter.ENC_NAME);
		
	}
	
}
	
