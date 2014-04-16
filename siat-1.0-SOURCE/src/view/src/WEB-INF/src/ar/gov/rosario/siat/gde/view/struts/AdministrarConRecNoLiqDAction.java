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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.ConRecNoLiqAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarConRecNoLiqDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarConRecNoLiqDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_CONRECNOLIQ, act); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		ConRecNoLiqAdapter conRecNoLiqAdapterVO = new ConRecNoLiqAdapter();
		
		String stringServicio = "";		
		try {						
			conRecNoLiqAdapterVO.setResultTipoRecibo((Boolean) navModel.getParameter("resultTipoRecibo"));
			
			if (act.equals(GdeConstants.ACTION_ADM_CONRECNOLIQ_PROCESAR)) {
				conRecNoLiqAdapterVO.setIdsSelected((String[]) navModel.getParameter("idsSelected"));
				stringServicio = "procesarConRecNoLiq(userSession, commonKey)";
				conRecNoLiqAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().procesarConRecNoLiq(userSession, conRecNoLiqAdapterVO);
				
			}else if(act.equals(GdeConstants.ACTION_ADM_CONRECNOLIQ_VOLVER_LIQUIDABLE)){
				String[] idsSelected = {(String) navModel.getParameter("selectedId")};
				conRecNoLiqAdapterVO.setIdsSelected(idsSelected);
				conRecNoLiqAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().volverLiquidablesConRecNoLiq(userSession, conRecNoLiqAdapterVO);				
			}
						
			// Seteo los valores de navegacion en el adapter
			conRecNoLiqAdapterVO.setValuesFromNavModel(navModel);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " +stringServicio+ "     "+ConRecNoLiqAdapter.NAME + ": "+ conRecNoLiqAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConRecNoLiqAdapter.NAME, conRecNoLiqAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConRecNoLiqAdapter.NAME, conRecNoLiqAdapterVO);
			
			saveDemodaMessages(request, conRecNoLiqAdapterVO);
			
			return forwardConfirmarOk(mapping, request, act, ConRecNoLiqAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConRecNoLiqAdapter.NAME);
		}			
	}	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConRecNoLiqAdapter.NAME);
		
	}		
}
