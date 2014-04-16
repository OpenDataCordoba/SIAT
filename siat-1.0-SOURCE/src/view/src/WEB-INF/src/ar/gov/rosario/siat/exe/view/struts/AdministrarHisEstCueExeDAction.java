//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.HisEstCueExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarHisEstCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarHisEstCueExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_HISESTCUEEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		HisEstCueExeAdapter hisEstCueExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getHisEstCueExeAdapterForView(userSession, commonKey)";
				hisEstCueExeAdapterVO = ExeServiceLocator.getExencionService().getHisEstCueExeAdapterForView(userSession, commonKey);				
				hisEstCueExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.HISESTCUEEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_HISESTCUEEXE_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (hisEstCueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + hisEstCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HisEstCueExeAdapter.NAME, hisEstCueExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			hisEstCueExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + HisEstCueExeAdapter.NAME + ": "+ hisEstCueExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(HisEstCueExeAdapter.NAME, hisEstCueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(HisEstCueExeAdapter.NAME, hisEstCueExeAdapterVO);
			 
			saveDemodaMessages(request, hisEstCueExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HisEstCueExeAdapter.NAME);
		}
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_HISESTCUEEXE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			HisEstCueExeAdapter hisEstCueExeAdapterVO = (HisEstCueExeAdapter) userSession.get(HisEstCueExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (hisEstCueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + HisEstCueExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, HisEstCueExeAdapter.NAME); 
			}

			// llamada al servicio
			HisEstCueExeVO hisEstCueExeVO = ExeServiceLocator.getExencionService().deleteHisEstCueExe(userSession, hisEstCueExeAdapterVO.getHisEstCueExe());
			
            // Tiene errores recuperables
			if (hisEstCueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + hisEstCueExeAdapterVO.infoString());
				saveDemodaErrors(request, hisEstCueExeVO);				
				request.setAttribute(HisEstCueExeAdapter.NAME, hisEstCueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_HISESTCUEEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (hisEstCueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + hisEstCueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, HisEstCueExeAdapter.NAME, hisEstCueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, HisEstCueExeAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, HisEstCueExeAdapter.NAME);
		}
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, HisEstCueExeAdapter.NAME);
		
	}	
	
}
