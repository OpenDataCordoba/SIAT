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
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesRecConVO;
import ar.gov.rosario.siat.bal.iface.model.OtrIngTesVO;
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
import coop.tecso.demoda.iface.model.PrintModel;

public class AdministrarOtrIngTesDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOtrIngTesDAction.class);

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
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getOtrIngTesAdapterForView(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getOtrIngTesAdapterForUpdate(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getOtrIngTesAdapterForDelete(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForView
						(userSession, commonKey);
					otrIngTesAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.OTRINGTES_LABEL);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getOtrIngTesAdapterForCreate(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForCreate
						(userSession);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BalConstants.ACT_GENERAR_RECIBO)) {
					stringServicio = "getOtrIngTesAdapterForView(userSession, commonKey)";
					otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_OTRINGTES_RECIBO_PRINT);
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
		
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_OTRINGTES, BaseSecurityConstants.AGREGAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesAdapterVO, request);
				
				if(otrIngTesAdapterVO.isParamRecurso() && !otrIngTesAdapterVO.isParamUnicoConcepto()){
					for(OtrIngTesRecConVO otrIngTesRecCon: otrIngTesAdapterVO.getOtrIngTes().getListOtrIngTesRecCon()){
						try{
							Double importe = Double.valueOf(request.getParameter("importe"+otrIngTesRecCon.getIdView()));
							otrIngTesRecCon.setImporte(importe);							
						}catch(Exception e){
							otrIngTesAdapterVO.addRecoverableError(BalError.OTRINGTES_RECCON_ERROR);
							break;
						}
					}
				}
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// llamada al servicio
				OtrIngTesVO otrIngTesVO = BalServiceLocator.getOtroIngresoTesoreriaService().createOtrIngTes(userSession, otrIngTesAdapterVO.getOtrIngTes());
				
	            // Tiene errores recuperables
				if (otrIngTesVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesVO.infoString()); 
					saveDemodaErrors(request, otrIngTesVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}

				
				// Si tiene permiso lo dirijo al adapter de distribucion, 
				// sino vuelve al searchPage
				if (hasAccess(userSession, BalSecurityConstants.ABM_OTRINGTES, 
					BalSecurityConstants.DISTRIBUIR)) {
					
					// seteo el id para que lo use el siguiente action 
					userSession.getNavModel().setSelectedId(otrIngTesVO.getId().toString());

					// lo dirijo al adapter de modificacion
					return forwardConfirmarOk(mapping, request, funcName, OtrIngTesAdapter.NAME, 
						BalConstants.PATH_DISTRIBUIR_OTRINGTES, BaseConstants.METHOD_INICIALIZAR, 
						BalConstants.ACT_DISTRIBUIR);
				} else {
					// lo dirijo al searchPage			
					return forwardConfirmarOk(mapping, request, funcName, OtrIngTesAdapter.NAME);
				}
				
									
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_OTRINGTES, BaseSecurityConstants.MODIFICAR);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesAdapterVO, request);
				
				if(otrIngTesAdapterVO.isParamRecurso() && !otrIngTesAdapterVO.isParamUnicoConcepto()){
					for(OtrIngTesRecConVO otrIngTesRecCon: otrIngTesAdapterVO.getOtrIngTes().getListOtrIngTesRecCon()){
						try{
							Double importe = Double.valueOf(request.getParameter("importe"+otrIngTesRecCon.getIdView()));
							otrIngTesRecCon.setImporte(importe);							
						}catch(Exception e){
							otrIngTesAdapterVO.addRecoverableError(BalError.OTRINGTES_RECCON_ERROR);
							break;
						}
					}
				}
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// llamada al servicio
				OtrIngTesVO otrIngTesVO = BalServiceLocator.getOtroIngresoTesoreriaService().updateOtrIngTes(userSession, otrIngTesAdapterVO.getOtrIngTes());
				
	            // Tiene errores recuperables
				if (otrIngTesVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
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
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, 
				BaseSecurityConstants.ELIMINAR);
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
				OtrIngTesVO otrIngTesVO = BalServiceLocator.getOtroIngresoTesoreriaService().deleteOtrIngTes
					(userSession, otrIngTesAdapterVO.getOtrIngTes());
				
	            // Tiene errores recuperables
				if (otrIngTesVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString());
					saveDemodaErrors(request, otrIngTesVO);				
					request.setAttribute(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
					return mapping.findForward(BalConstants.FWD_OTRINGTES_VIEW_ADAPTER);
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
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesAdapterVO, request);
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Llamada al servicio
				otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().paramRecurso(userSession, otrIngTesAdapterVO);
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}

				// Envio el VO al request
				request.setAttribute(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				// Subo el adapter al userSession
				userSession.put(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				
				saveDemodaMessages(request, otrIngTesAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_OTRINGTES_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
	}
	
	public ActionForward paramArea(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesAdapterVO, request);
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Llamada al servicio
				otrIngTesAdapterVO = BalServiceLocator.getOtroIngresoTesoreriaService().paramArea(userSession, otrIngTesAdapterVO);
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}

				// Envio el VO al request
				request.setAttribute(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				// Subo el adapter al userSession
				userSession.put(OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				
				saveDemodaMessages(request, otrIngTesAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_OTRINGTES_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
	}
	
	
	public ActionForward generarRecibo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_OTRINGTES, BalSecurityConstants.ABM_OTRINGTES_REC);		
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				PrintModel print;
				// Bajo el adapter del userSession
				OtrIngTesAdapter otrIngTesAdapterVO = (OtrIngTesAdapter) userSession.get(OtrIngTesAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (otrIngTesAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OtrIngTesAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesAdapter.NAME); 
				}
				
				// llamada al servicio
				print = BalServiceLocator.getOtroIngresoTesoreriaService().generarRecibo(userSession, otrIngTesAdapterVO.getOtrIngTes());
				
	            // Tiene errores recuperables
				if (otrIngTesAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesAdapterVO.infoString()); 
					saveDemodaErrors(request, otrIngTesAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesAdapter.NAME, otrIngTesAdapterVO);
				}

				baseResponsePrintModel(response, print);
				return null;
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OtrIngTesAdapter.NAME);
			}
	}

}
