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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ReciboAdapter;
import ar.gov.rosario.siat.gde.iface.model.ReciboSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarReciboDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarReciboDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_RECIBO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ReciboAdapter reciboAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getReciboAdapterForView(userSession, commonKey)";
				
				ReciboSearchPage reciboSearchPage = (ReciboSearchPage) userSession.get(ReciboSearchPage.NAME);
				
				reciboAdapterVO = GdeServiceLocator.getGestionDeudaService().getReciboAdapterForView(userSession, 
						commonKey, reciboSearchPage.getTipoRecibo());
				actionForward = mapping.findForward(GdeConstants.FWD_RECIBO_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (reciboAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + reciboAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ReciboAdapter.NAME, reciboAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			reciboAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ReciboAdapter.NAME + ": "+ reciboAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ReciboAdapter.NAME, reciboAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ReciboAdapter.NAME, reciboAdapterVO);
			 
			saveDemodaMessages(request, reciboAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ReciboAdapter.NAME);
		}
	}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ReciboAdapter.NAME);
		
	}
		
}
