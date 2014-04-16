//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import java.util.List;

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
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualAdapter;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqReciboVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;


public final class AdministrarEmisionPuntualDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionPuntualDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionPuntualAdapter emisionPuntualAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId()); 
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEmisionPuntualAdapterForView(userSession, commonKey)";
				emisionPuntualAdapterVO = EmiServiceLocator.getEmisionService()
					.getEmisionPuntualAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_VIEW_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionPuntualAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + emisionPuntualAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualAdapter.NAME, emisionPuntualAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionPuntualAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionPuntualAdapter.NAME + ": " + emisionPuntualAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualAdapter.NAME, emisionPuntualAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualAdapter.NAME, emisionPuntualAdapterVO);
			
			saveDemodaMessages(request, emisionPuntualAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualAdapter.NAME);
		}
	}
	
	public ActionForward getRecibosPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.ABM_EMISIONPUNTUAL, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			EmisionPuntualAdapter emisionPuntualAdapterVO = (EmisionPuntualAdapter) userSession.get(EmisionPuntualAdapter.NAME);
						
			// Generamos el PDF
			PrintModel print = null;
			List<LiqReciboVO> listLiqReciboVO = emisionPuntualAdapterVO.getListLiqReciboVO();
			print = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getImprimirRecibos(userSession, listLiqReciboVO);

			// Tiene errores no recuperables
			if (print == null) {
				log.error("error en: "  + funcName + ": " + emisionPuntualAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualAdapter.NAME, emisionPuntualAdapterVO);
			}

			baseResponsePrintModel(response, print);
			
			return null;
		} catch (Exception e) {
			if (log.isDebugEnabled()) log.error("No se pudieron crear los recibos ", e);
			return baseException(mapping, request, funcName, e,EmisionPuntualAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) 
		throws Exception {

		return baseVolver(mapping, form, request, response, EmisionPuntualAdapter.NAME);
	}

}
