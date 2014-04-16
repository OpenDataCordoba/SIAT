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

import ar.gov.rosario.siat.bal.iface.model.ControlConciliacionSearchPage;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.CtrlInfDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.CtrlInfDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCtrlInfDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCtrlInfDeuDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CTRLINFDEU, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			CtrlInfDeuAdapter ctrlInfDeuAdapter = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				 ctrlInfDeuAdapter = new CtrlInfDeuAdapter();
				 ctrlInfDeuAdapter.setParamEncontrado(false);
				 ctrlInfDeuAdapter.setParamNoEncontrado(false);

				 actionForward = mapping.findForward(GdeConstants.FWD_CTRLINFDEU_VIEW_ADAPTER);
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (ctrlInfDeuAdapter.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + ctrlInfDeuAdapter.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapter);
				}
				
				// Seteo los valores de navegacion en el adapter
				ctrlInfDeuAdapter.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
						CtrlInfDeuAdapter.NAME + ": " + ctrlInfDeuAdapter.infoString());
				
				// Envio el VO al request
				request.setAttribute(CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapter);
				// Subo el apdater al userSession
				userSession.put(CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapter);
				
				saveDemodaMessages(request, ctrlInfDeuAdapter);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CtrlInfDeuAdapter.NAME);
			}
		}
	

		public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, CtrlInfDeuAdapter.NAME);
			
		}
		
		public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				CtrlInfDeuAdapter ctrlInfDeuAdapterVO = (CtrlInfDeuAdapter) userSession.get(CtrlInfDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (ctrlInfDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + CtrlInfDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ControlConciliacionSearchPage.NAME); 
				}
								
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ctrlInfDeuAdapterVO, request);
								
	            // Tiene errores recuperables
				if (ctrlInfDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ctrlInfDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, ctrlInfDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
				}
					
				// Llamada al servicio	
				ctrlInfDeuAdapterVO = GdeServiceLocator.getGestionDeudaService().getCtrlInfDeuAdapterForDesbloquear(userSession, ctrlInfDeuAdapterVO);			

				// Tiene errores recuperables
				if (ctrlInfDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ctrlInfDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, ctrlInfDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (ctrlInfDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ctrlInfDeuAdapterVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
				}
			
				// Envio el VO al request
				request.setAttribute(CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
				
				// Subo en el el searchPage al userSession
				userSession.put(CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
				
				return mapping.findForward(GdeConstants.FWD_CTRLINFDEU_VIEW_ADAPTER);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CtrlInfDeuAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, CtrlInfDeuAdapter.NAME);
			
		}
		
		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, CtrlInfDeuAdapter.NAME);
		}
		
		public ActionForward eliminar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CTRLINFDEU, 
					BaseSecurityConstants.ELIMINAR);
				if (userSession==null) return forwardErrorSession(request);
				
				try {
					// Bajo el adapter del userSession
					CtrlInfDeuAdapter ctrlInfDeuAdapterVO = (CtrlInfDeuAdapter) userSession.get(CtrlInfDeuAdapter.NAME);
					
					// Si es nulo no se puede continuar
					if (ctrlInfDeuAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + CtrlInfDeuAdapter.NAME + 
							" IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, CtrlInfDeuAdapter.NAME); 
					}

					// llamada al servicio
					CtrlInfDeuVO ctrlInfDeuVO = GdeServiceLocator.getGestionDeudaService().deleteCtrlInfDeu(userSession, ctrlInfDeuAdapterVO.getCtrlInfDeu());
					
		            // Tiene errores recuperables
					if (ctrlInfDeuVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + ctrlInfDeuAdapterVO.infoString());
						saveDemodaErrors(request, ctrlInfDeuVO);				
						request.setAttribute(CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
						return mapping.findForward(GdeConstants.FWD_CTRLINFDEU_VIEW_ADAPTER);
					}
					
					// Tiene errores no recuperables
					if (ctrlInfDeuVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + ctrlInfDeuAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, CtrlInfDeuAdapter.NAME, ctrlInfDeuAdapterVO);
					}
					
					// Fue Exitoso
					return forwardConfirmarOk(mapping, request, funcName, CtrlInfDeuAdapter.NAME);

				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, CtrlInfDeuAdapter.NAME);
				}
		}

}
