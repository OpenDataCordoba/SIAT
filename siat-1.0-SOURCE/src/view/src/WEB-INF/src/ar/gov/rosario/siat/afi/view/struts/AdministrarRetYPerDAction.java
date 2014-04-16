//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.afi.iface.model.RetYPerAdapter;
import ar.gov.rosario.siat.afi.iface.service.AfiServiceLocator;
import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.afi.view.util.AfiConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRetYPerDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRetYPerDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, AfiSecurityConstants.ABM_RETYPER, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		RetYPerAdapter retYPerAdapterAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRetYPerAdapterForView(userSession, commonKey)";
				retYPerAdapterAdapterVO = AfiServiceLocator.getFormulariosDJService().getRetYPerAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(AfiConstants.FWD_RETYPER_VIEW_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (retYPerAdapterAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + retYPerAdapterAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RetYPerAdapter.NAME, retYPerAdapterAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			retYPerAdapterAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RetYPerAdapter.NAME + ": "+ retYPerAdapterAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RetYPerAdapter.NAME, retYPerAdapterAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RetYPerAdapter.NAME, retYPerAdapterAdapterVO);
			 
			saveDemodaMessages(request, retYPerAdapterAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RetYPerAdapter.NAME);
		}
	}	
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, RetYPerAdapter.NAME);
		
	}
		

	
}
