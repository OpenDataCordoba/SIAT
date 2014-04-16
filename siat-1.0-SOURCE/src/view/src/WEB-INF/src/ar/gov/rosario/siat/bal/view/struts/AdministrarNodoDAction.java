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

import ar.gov.rosario.siat.bal.buss.bean.Clasificador;
import ar.gov.rosario.siat.bal.iface.model.NodoAdapter;
import ar.gov.rosario.siat.bal.iface.model.NodoVO;
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

public class AdministrarNodoDAction extends BaseDispatchAction {


	private Log log = LogFactory.getLog(AdministrarNodoDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_NODO, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			NodoAdapter nodoAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getNodoAdapterForView(userSession, commonKey)";
					nodoAdapterVO = BalServiceLocator.getClasificacionService().getNodoAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_NODO_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getNodoAdapterForView(userSession, commonKey)";
					nodoAdapterVO = BalServiceLocator.getClasificacionService().getNodoAdapterForView
						(userSession, commonKey);
					
					if(nodoAdapterVO.getNodo().getClasificador().getId().longValue() == Clasificador.ID_CLA_RUBRO 
							&&( nodoAdapterVO.getNodo().getNivel().intValue() == 5
								||  nodoAdapterVO.getNodo().getNivel().intValue() == 6)){						
						actionForward = mapping.findForward(BalConstants.FWD_NODO_ADAPTER);
					}else{
						actionForward = mapping.findForward(BalConstants.ACTION_ADMINISTRAR_ENCNODO);
					}
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getNodoAdapterForDelete(userSession, commonKey)";
					nodoAdapterVO = BalServiceLocator.getClasificacionService().getNodoAdapterForView
						(userSession, commonKey);
					nodoAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.NODO_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_NODO_VIEW_ADAPTER);					
				}
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (nodoAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + nodoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NodoAdapter.NAME, nodoAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				nodoAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					NodoAdapter.NAME + ": " + nodoAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(NodoAdapter.NAME, nodoAdapterVO);
				// Subo el apdater al userSession
				userSession.put(NodoAdapter.NAME, nodoAdapterVO);
				
				saveDemodaMessages(request, nodoAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoAdapter.NAME);
			}
		}
	
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_ENCNODO, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_NODO, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				NodoAdapter nodoAdapterVO = (NodoAdapter) userSession.get(NodoAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (nodoAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + NodoAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, NodoAdapter.NAME); 
				}

				// llamada al servicio
				NodoVO nodoVO = BalServiceLocator.getClasificacionService().deleteNodo(userSession, nodoAdapterVO.getNodo());
				
	            // Tiene errores recuperables
				if (nodoVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + nodoAdapterVO.infoString());
					saveDemodaErrors(request, nodoVO);				
					request.setAttribute(NodoAdapter.NAME, nodoAdapterVO);
					return mapping.findForward(BalConstants.FWD_NODO_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (nodoVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + nodoAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, NodoAdapter.NAME, nodoAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, NodoAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, NodoAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, NodoAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, NodoAdapter.NAME);
			
	}
	
	// Metodos relacionados RelPartida

	public ActionForward verRelPartida(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELPARTIDA);

	}

	public ActionForward modificarRelPartida(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELPARTIDA);

	}

	public ActionForward eliminarRelPartida(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELPARTIDA);

	}
	
	public ActionForward agregarRelPartida(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELPARTIDA);
		
	}

	// Metodos relacionados RelCla

	public ActionForward verRelCla(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELCLA);

	}

	public ActionForward modificarRelCla(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELCLA);

	}

	public ActionForward eliminarRelCla(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELCLA);

	}
	
	public ActionForward agregarRelCla(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_RELCLA);
		
	}

}
