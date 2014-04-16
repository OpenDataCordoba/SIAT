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
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProcedimientoVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarBajaProcedimientoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarBajaProcedimientoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProcedimientoAdapter procedimientoAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(CyqConstants.ACT_BAJA)) {
				stringServicio = "getProcedimientoAdapterForBaja(userSession)";
				procedimientoAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProcedimientoAdapterForBaja(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCEDIMIENTO_BAJA_ADAPTER);
				procedimientoAdapterVO.addMessage(CyqError.MSG_BAJA_DEVOLUCION_DEUDA);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " act: " + navModel.getAct());
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (procedimientoAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			procedimientoAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProcedimientoAdapter.ENC_NAME + ": "+ procedimientoAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);

			saveDemodaMessages(request, procedimientoAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward baja(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCEDIMIENTO_ENC_CyQ, CyqConstants.ACT_BAJA);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProcedimientoAdapter procedimientoAdapterVO = (ProcedimientoAdapter) userSession.get(ProcedimientoAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (procedimientoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProcedimientoAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(procedimientoAdapterVO, request);
			
			procedimientoAdapterVO.getProcedimiento().setListIdAreaSolicSelected(request.getParameterValues("listIdAreaSolicSelected"));
			
            // Tiene errores recuperables
			if (procedimientoAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// llamada al servicio
			ProcedimientoVO procedimientoVO = CyqServiceLocator.getConcursoyQuiebraService().bajaProcedimiento(userSession, procedimientoAdapterVO.getProcedimiento());
			
            // Tiene errores recuperables
			if (procedimientoVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + procedimientoAdapterVO.infoString()); 
				saveDemodaErrors(request, procedimientoVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (procedimientoVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + procedimientoAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME, procedimientoAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProcedimientoAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProcedimientoAdapter.ENC_NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProcedimientoAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ProcedimientoAdapter.ENC_NAME);
		
	}
}