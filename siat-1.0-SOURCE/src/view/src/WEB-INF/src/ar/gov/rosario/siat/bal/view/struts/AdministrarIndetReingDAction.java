//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.IndetReingAdapter;
import ar.gov.rosario.siat.bal.iface.model.IndetVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarIndetReingDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarIndetReingDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDETREING, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			IndetReingAdapter indetReingAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getIndetReingAdapterForView(userSession, commonKey)";
					indetReingAdapterVO = BalServiceLocator.getIndetService().getIndetReingAdapterForView(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_INDETREING_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_VUELTA_ATRAS)) {
					stringServicio = "getIndetReingAdapterForView(userSession, commonKey)";
					indetReingAdapterVO = BalServiceLocator.getIndetService().getIndetReingAdapterForView(userSession, commonKey);
					indetReingAdapterVO.addMessage(BaseError.MSG_VUELTA_ATRAS_INDET, BalError.INDET_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_INDETREING_VIEW_ADAPTER);					
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (indetReingAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + indetReingAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetReingAdapter.NAME, indetReingAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				indetReingAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					IndetReingAdapter.NAME + ": " + indetReingAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(IndetReingAdapter.NAME, indetReingAdapterVO);
				// Subo el apdater al userSession
				userSession.put(IndetReingAdapter.NAME, indetReingAdapterVO);
				
				saveDemodaMessages(request, indetReingAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetReingAdapter.NAME);
			}
		}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, IndetReingAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, IndetReingAdapter.NAME);
			
	}

	public ActionForward vueltaAtras(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDETREING, 
				BaseSecurityConstants.VUELTA_ATRAS);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				IndetReingAdapter indetReingAdapterVO = (IndetReingAdapter) userSession.get(IndetReingAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (indetReingAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + IndetReingAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, IndetReingAdapter.NAME); 
				}

				// llamada al servicio
				IndetVO indetVO = BalServiceLocator.getIndetService().vueltaAtrasReing(userSession, indetReingAdapterVO.getIndetReing());
				
	            // Tiene errores recuperables
				if (indetVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + indetReingAdapterVO.infoString());
					saveDemodaErrors(request, indetVO);				
					request.setAttribute(IndetReingAdapter.NAME, indetReingAdapterVO);
					return mapping.findForward(BalConstants.FWD_INDETREING_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (indetVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + indetReingAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, IndetReingAdapter.NAME, indetReingAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, IndetReingAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, IndetReingAdapter.NAME);
			}
		}
}
