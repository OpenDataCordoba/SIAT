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

import ar.gov.rosario.siat.bal.iface.model.OtrIngTesAdapter;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class DistribuirOtrIngTesDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(DistribuirOtrIngTesDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, act);		
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			OtrIngTesAdapter otrIngTesAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BalConstants.ACT_DISTRIBUIR)) {
					stringServicio = "getOtrIngTesAdapterForAdm(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForAdm
						(userSession, commonKey);

					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_DIS_IMP);
				}
				if (navModel.getAct().equals(BalConstants.ACT_IMPUTAR)) {
					stringServicio = "getOtrIngTesAdapterForAdm(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForAdm
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_DIS_IMP);
				}
				
				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (otrIngTesAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + otrIngTesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				otrIngTesAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					OtrIngTesAdapter.NAME + ": " + otrIngTesAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				
				saveDemodaMessages(request, otrIngTesAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
		}
	
	public ActionForward verDetalleDistribuidor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				BalConstants.ACTION_ADMINISTRAR_DISPARDET, BaseConstants.ACT_INICIALIZAR);
	}
	
	public ActionForward imputar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, 
					BalSecurityConstants.IMPUTAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}

				// llamada al servicio
				OtrIngTesVO otrIngTesVO = BalServiceLocator.getOtroIngresoTesoreriaService().imputarOtrIngTes
					(userSession, otrIngTesAdapterVO.getOtrIngTes());
				
	            // Tiene errores recuperables
				if (otrIngTesVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString());
					saveDemodaErrors(request, otrIngTesVO);				
					request.setAttribute(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
					return mapping.findForward(BalConstants.FWD_OTRINGTES_DIS_IMP);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, OtrIngTesAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
		}
	
	public ActionForward distribuir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, 
				BalSecurityConstants.DISTRIBUIR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}

				// llamada al servicio
				OtrIngTesVO otrIngTesVO = BalServiceLocator.getOtroIngresoTesoreriaService().distribuirOtrIngTes
					(userSession, otrIngTesAdapterVO.getOtrIngTes());
				
	            // Tiene errores recuperables
				if (otrIngTesVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString());
					saveDemodaErrors(request, otrIngTesVO);				
					request.setAttribute(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
					return mapping.findForward(BalConstants.FWD_OTRINGTES_DIS_IMP);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
							
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, OtrIngTesAdapter.NAME, BalConstants.PATH_DISTRIBUIR_OTRINGTES,
						BaseConstants.METHOD_INICIALIZAR, BalConstants.ACT_DISTRIBUIR);
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, OtrIngTesAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, OtrIngTesAdapter.NAME);
			
	}
	
	// Metodos relacionados OtrIngTesPar

	public ActionForward verOtrIngTesPar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTESPAR);

	}

	public ActionForward modificarOtrIngTesPar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTESPAR);

	}

	public ActionForward eliminarOtrIngTesPar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTESPAR);

	}
	
	public ActionForward agregarOtrIngTesPar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTESPAR);
		
	}

	public ActionForward generarRecibo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, 
					BalSecurityConstants.DISTRIBUIR);
				if (userSession==null) return forwardErrorSession(request);
			// para que pueda volver a la busqueda de otrIngTes
			userSession.getNavModel().setPrevAction(BalConstants.PATH_BUSCAR_OTRINGTES);
			userSession.getNavModel().setPrevActionParameter(BaseConstants.ACT_BUSCAR);
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(otrIngTesAdapterVO.getOtrIngTes().getId().toString());
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
			userSession.getNavModel().setAct(BalConstants.ACT_GENERAR_RECIBO);
			
			return mapping.findForward(BalConstants.FWD_OTRINGTES_RECIBO_PRINT);
	}
}
