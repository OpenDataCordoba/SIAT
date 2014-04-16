//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarRecursoAutoLiquidableDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecursoAutoLiquidableDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECURSO, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			RecursoAdapter recursoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.NAME) ;
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecursoAdapterForView(userSession, commonKey)";
					recursoAdapterVO = DefServiceLocator.getGravamenService().getRecursoAdapterForView
						(userSession, commonKey);
				}
				
				actionForward = mapping.findForward(DefConstants.FWD_RECURSO_AUTOLIQUIDABLE_ADAPTER);
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recursoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recursoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecursoAdapter.NAME, recursoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recursoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					RecursoAdapter.NAME + ": " + recursoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecursoAdapter.NAME, recursoAdapterVO);
				
				// Subo el apdater al userSession
				userSession.put(RecursoAdapter.NAME, recursoAdapterVO);
				
				saveDemodaMessages(request, recursoAdapterVO);			
							
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecursoAdapter.NAME);
			}
		}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecursoAdapter.NAME);
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, RecursoAdapter.NAME);
			
		}

	//Metodos relacionados a RecConADec
		public ActionForward verRecConADec(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCONADEC);

			}
		public ActionForward modificarRecConADec(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCONADEC);

			}
		
		public ActionForward eliminarRecConADec(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCONADEC);

			}
		
		public ActionForward agregarRecConADec(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECCONADEC);

			}

		// Metodos relacionados RecMinDec

		public ActionForward verRecMinDec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECMINDEC);

		}

		public ActionForward modificarRecMinDec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECMINDEC);

		}

		public ActionForward eliminarRecMinDec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECMINDEC);

		}
		
		public ActionForward agregarRecMinDec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECMINDEC);
			
		}

		// Metodos relacionados RecAli

		public ActionForward verRecAli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECALI);

		}

		public ActionForward modificarRecAli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECALI);

		}

		public ActionForward eliminarRecAli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECALI);

		}
		
		public ActionForward agregarRecAli(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarAdapter(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_RECALI);
			
		}

}
