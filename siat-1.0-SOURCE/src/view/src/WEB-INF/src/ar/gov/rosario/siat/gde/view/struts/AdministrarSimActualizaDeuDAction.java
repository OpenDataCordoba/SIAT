//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class AdministrarSimActualizaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSimActualizaDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_SIMACTUALIZADEU, BaseSecurityConstants.VER); 
		if (userSession == null) return forwardErrorSession(request);
		
		ActionForward actionForward = null;
		try {			
			actionForward = mapping.findForward(GdeConstants.FWD_SIMACTUALIZADEU_VIEW_ADAPTER);			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, "");
		}
	}
	
	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_SIMACTUALIZADEU, BaseSecurityConstants.VER);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			SiatAdapterModel adapterVO = new SiatAdapterModel();
			
			// Tiene errores recuperables
			if (adapterVO.hasErrorRecoverable()) {
				saveDemodaErrors(request, adapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, "", adapterVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_SIMACTUALIZADEU_VIEW_ADAPTER);			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, "");
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return new ActionForward(BaseConstants.FWD_SIAT_BUILD_MENU);
	}	
}
