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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.LiqDeudaCyqAdapter;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarLiqDeudaCyqDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqDeudaCyqDAction.class);
	

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, funcName);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		String stringServicio = "getLiqDeudaCyqInit";
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			LiqDeudaCyqAdapter liqDeudaCyqAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getLiqDeudaCyqInit(userSession, commonKey);
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " act: " + navModel.getAct());
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (liqDeudaCyqAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaCyqAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaCyqAdapter.NAME, liqDeudaCyqAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			liqDeudaCyqAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaCyqAdapter.NAME + ": "+ liqDeudaCyqAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaCyqAdapter.NAME, liqDeudaCyqAdapterVO);
			// Subo el apdater al userSession
			userSession.put(LiqDeudaCyqAdapter.NAME, liqDeudaCyqAdapterVO);
			
			saveDemodaMessages(request, liqDeudaCyqAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_LIQDEUDACYQ_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaCyqAdapter.NAME);
		}
	}

	
	public ActionForward agregarDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_DEUDAPRIVILEGIO);
		
	}
	
	public ActionForward registrarPago(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, funcName);
		LiqDeudaCyqAdapter liqDeudaCyqAdapterVO = (LiqDeudaCyqAdapter) userSession.get(LiqDeudaCyqAdapter.NAME);
		
		liqDeudaCyqAdapterVO.clearError();
		
		// Validarmos que exista deuda seleccionada.
		if (request.getParameterValues("listIdDeudaSelected") == null ){
			liqDeudaCyqAdapterVO.addRecoverableValueError("Debe seleccionar deuda a cancelar");
		
			// Envio el VO al request
			request.setAttribute(LiqDeudaCyqAdapter.NAME, liqDeudaCyqAdapterVO);
			// Subo el apdater al userSession
			
			saveDemodaErrors(request, liqDeudaCyqAdapterVO);
			saveDemodaMessages(request, liqDeudaCyqAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_LIQDEUDACYQ_ADAPTER);
		}
		
		return forwardAgregarAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_PAGOPRIV);
		
	}

	
	public ActionForward verPagoPriv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
				
		return forwardVerAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_PAGOPRIV);
		
	}	
	
	public ActionForward formalizarConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, funcName);		
		LiqDeudaCyqAdapter liqDeudaCyqAdapterVO = (LiqDeudaCyqAdapter) userSession.get(LiqDeudaCyqAdapter.NAME);
		
		
		liqDeudaCyqAdapterVO.clearError();
		
		// Validarmos que exista deuda seleccionada.
		if (request.getParameterValues("listIdDeudaSelected") == null ){
			liqDeudaCyqAdapterVO.addRecoverableError(GdeError.MSG_SELECT_DEUDA_REQUERIDO);
		}
		
		// Validamos que el procedimiento posea Fecha Homologacion
		if (liqDeudaCyqAdapterVO.getProcedimiento().getFechaHomo() == null){
			liqDeudaCyqAdapterVO.addRecoverableValueError("La fecha de Homologacion del procedimiento es requerida.");
		}
		
		if (liqDeudaCyqAdapterVO.hasError()){
			// Envio el VO al request
			request.setAttribute(LiqDeudaCyqAdapter.NAME, liqDeudaCyqAdapterVO);
			// Subo el apdater al userSession
			
			saveDemodaErrors(request, liqDeudaCyqAdapterVO);
			saveDemodaMessages(request, liqDeudaCyqAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_LIQDEUDACYQ_ADAPTER);
		}
		
		return forwardAgregarAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_CONVENIO);
		
	}

	
	public ActionForward formalizarConvenioEsp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, funcName);
		LiqDeudaCyqAdapter liqDeudaCyqAdapterVO = (LiqDeudaCyqAdapter) userSession.get(LiqDeudaCyqAdapter.NAME);
		
		liqDeudaCyqAdapterVO.clearError();
		
		// Validarmos que exista deuda seleccionada.
		if (request.getParameterValues("listIdDeudaSelected") == null ){
			liqDeudaCyqAdapterVO.addRecoverableError(GdeError.MSG_SELECT_DEUDA_REQUERIDO);
		}
		
		// Validamos que el procedimiento posea Fecha Homologacion
		if (liqDeudaCyqAdapterVO.getProcedimiento().getFechaHomo() == null){
			liqDeudaCyqAdapterVO.addRecoverableValueError("La fecha de Homologacion del procedimiento es requerida.");
		}
		
		if (liqDeudaCyqAdapterVO.hasError()){
			// Envio el VO al request
			request.setAttribute(LiqDeudaCyqAdapter.NAME, liqDeudaCyqAdapterVO);
			// Subo el apdater al userSession
			
			saveDemodaErrors(request, liqDeudaCyqAdapterVO);
			saveDemodaMessages(request, liqDeudaCyqAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_LIQDEUDACYQ_ADAPTER);
		}
		
		return forwardAgregarAdapter(mapping, request, funcName, CyqConstants.ACTION_ADMINISTRAR_CONVENIO_ESP);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, LiqDeudaCyqAdapter.NAME);
		
	}
	

	
	/**
	 *  Forward a ver Convenio
	 *  
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward verConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		request.setAttribute("vieneDe", "liqDeudaCyq");
		return mapping.findForward(CyqConstants.ACTION_VER_CONVENIO_CUENTA);
		
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		return baseVolver(mapping, form, request, response, LiqDeudaCyqAdapter.NAME);
	}
}	