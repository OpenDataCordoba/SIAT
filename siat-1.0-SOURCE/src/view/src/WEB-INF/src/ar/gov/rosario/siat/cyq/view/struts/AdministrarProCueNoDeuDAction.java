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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.ProCueNoDeuAdapter;
import ar.gov.rosario.siat.cyq.iface.model.ProCueNoDeuVO;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
				   
public final class AdministrarProCueNoDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarProCueNoDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCUENODEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ProCueNoDeuAdapter proCueNoDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getProCueNoDeuAdapterForView(userSession, commonKey)";
				proCueNoDeuAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProCueNoDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCUENODEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getProCueNoDeuAdapterForView(userSession, commonKey)";
				proCueNoDeuAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProCueNoDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCUENODEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getProCueNoDeuAdapterForView(userSession, commonKey)";
				proCueNoDeuAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getProCueNoDeuAdapterForView(userSession, commonKey);				
				proCueNoDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, CyqError.PROCUENODEU_LABEL);
				actionForward = mapping.findForward(CyqConstants.FWD_PROCUENODEU_VIEW_ADAPTER);				
			}
			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (proCueNoDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + proCueNoDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			proCueNoDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ProCueNoDeuAdapter.NAME + ": "+ proCueNoDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			 
			saveDemodaMessages(request, proCueNoDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProCueNoDeuAdapter.NAME);
		}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCUENODEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProCueNoDeuAdapter proCueNoDeuAdapterVO = (ProCueNoDeuAdapter) userSession.get(ProCueNoDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proCueNoDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProCueNoDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProCueNoDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(proCueNoDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (proCueNoDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proCueNoDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, proCueNoDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			}
			
			// llamada al servicio
			ProCueNoDeuVO proCueNoDeuVO = CyqServiceLocator.getConcursoyQuiebraService().updateProCueNoDeu(userSession, proCueNoDeuAdapterVO.getProCueNoDeu());
			
            // Tiene errores recuperables
			if (proCueNoDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proCueNoDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, proCueNoDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (proCueNoDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proCueNoDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProCueNoDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProCueNoDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.ABM_PROCUENODEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ProCueNoDeuAdapter proCueNoDeuAdapterVO = (ProCueNoDeuAdapter) userSession.get(ProCueNoDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (proCueNoDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ProCueNoDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProCueNoDeuAdapter.NAME); 
			}

			// llamada al servicio
			ProCueNoDeuVO proCueNoDeuVO = CyqServiceLocator.getConcursoyQuiebraService().deleteProCueNoDeu(userSession, proCueNoDeuAdapterVO.getProCueNoDeu());
			
            // Tiene errores recuperables
			if (proCueNoDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proCueNoDeuAdapterVO.infoString());
				saveDemodaErrors(request, proCueNoDeuVO);				
				request.setAttribute(ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
				return mapping.findForward(CyqConstants.FWD_PROCUENODEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (proCueNoDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proCueNoDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProCueNoDeuAdapter.NAME, proCueNoDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ProCueNoDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProCueNoDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProCueNoDeuAdapter.NAME);
		
	}
	
}
