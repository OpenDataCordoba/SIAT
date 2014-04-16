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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.gde.iface.model.AccionTraspasoDevolucion;
import ar.gov.rosario.siat.gde.iface.model.DevolucionDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTraspasoDevolucionDeudaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTraspasoDevolucionDeudaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = null; 
		
		if (act.equals(GdeConstants.ACT_AGREGAR_TRADEVDEUDET)){
			userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRADEVDEUDET, BaseConstants.ACT_AGREGAR);
		}else{
			userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA, act);
		}
		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		
		TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		
		try {
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			// obtencion del id de la accion seleccionada(Traspaso o Devolucion) y construccion de la accionSeleccionadaKey
			Integer idAccion = (Integer) userSession.getNavModel().getParameter(
					TraspasoDevolucionDeudaAdapter.ACCION_KEY);
			CommonKey accionSeleccionadaKey = null;
			if (idAccion != null){
				accionSeleccionadaKey = new CommonKey(idAccion.longValue());
			}else{
				// se da cuando vuelve del verCuenta o verConstancia y necesitamos reconstruir la accionSeleccionadaKey
				
				// Bajo el adapter del userSession
				traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (traspasoDevolucionDeudaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME); 
				}
				idAccion = traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion().getId();
				accionSeleccionadaKey = new CommonKey(idAccion.longValue());
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTraspasoDevolucionDeudaAdapterForView(userSession, commonKey, accionSeleccionadaKey)";
				traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().
					getTraspasoDevolucionDeudaAdapterForView(userSession, commonKey, accionSeleccionadaKey);
				actionForward = mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ADAPTER);
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTraspasoDevolucionDeudaAdapterForDelete(userSession, commonKey)";
				traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().
					getTraspasoDevolucionDeudaAdapterForView(userSession, commonKey, accionSeleccionadaKey);
				// cargo el mensaje de advertencia de borrado de un traspaso o una devolucion
				if(AccionTraspasoDevolucion.TRASPASO.getId().equals(idAccion)){
					traspasoDevolucionDeudaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.TRASPASODEUDA_LABEL);
				}else if(AccionTraspasoDevolucion.DEVOLUCION.getId().equals(idAccion)){
					traspasoDevolucionDeudaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DEVOLUCIONDEUDA_LABEL);
				} 
				actionForward = mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ADAPTER);					
			}
			
			if (navModel.getAct().equals(GdeConstants.ACT_AGREGAR_TRADEVDEUDET)) {
				if(AccionTraspasoDevolucion.TRASPASO.getId().equals(idAccion)){
					stringServicio = "getTraDeuDetAdapterForCreate(userSession, commonKey, constanciaKey)";
					// obtencion de la clave de la constancia si estamos trabajando con una constancia 
					CommonKey constanciaKey = null;
					
					// obtencion del id de la constancia
					Long idConstancia = (Long) userSession.getNavModel().getParameter(
							TraspasoDevolucionDeudaAdapter.CONSTANCIA_KEY);
					
					if (traspasoDevolucionDeudaAdapterVO != null){
						// volvi del ver deuda o constancia
						idConstancia = traspasoDevolucionDeudaAdapterVO.getConstanciaDeuVO().getId();
					}
					
					if (idConstancia != null){
						constanciaKey = new CommonKey(idConstancia);
					}
					
					traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().
						getTraDeuDetAdapterForCreate(userSession, commonKey, constanciaKey);
					actionForward = mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ADAPTER);
					
				}else if(AccionTraspasoDevolucion.DEVOLUCION.getId().equals(idAccion)){
					stringServicio = "getDevDeuDetAdapterForCreate(userSession, commonKey)";
					traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().getDevDeuDetAdapterForCreate(userSession, commonKey);
					actionForward = mapping.findForward(GdeConstants.FWD_TRASPASO_DEVOLUCION_DEUDA_ADAPTER);
				} 
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Tiene errores recuperables, pero no son tratados
			
			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			traspasoDevolucionDeudaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				TraspasoDevolucionDeudaAdapter.NAME + ": " + traspasoDevolucionDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
			
			saveDemodaMessages(request, traspasoDevolucionDeudaAdapterVO);
			saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRASPASO_DEVOLUCION_DEUDA, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME); 
			}

			// obtencion de la accion y llamada al servicio segun corresponda
			AccionTraspasoDevolucion accion = traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion(); 
			if (accion.getEsTraspaso()){
				TraspasoDeudaVO traspasoDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().deleteTraspasoDeuda(userSession, traspasoDevolucionDeudaAdapterVO.getTraspasoDeuda());
				
	            // Tiene errores recuperables
				if (traspasoDeudaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString());
					saveDemodaErrors(request, traspasoDeudaVO);				
					request.setAttribute(TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
					return mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (traspasoDeudaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
				}
			}else if (accion.getEsDevolucion()){
				DevolucionDeudaVO devolucionDeudaVO = GdeServiceLocator.getGestionDeudaJudicialService().deleteDevolucionDeuda(userSession, traspasoDevolucionDeudaAdapterVO.getDevolucionDeuda());
				
	            // Tiene errores recuperables
				if (devolucionDeudaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString());
					saveDemodaErrors(request, devolucionDeudaVO);				
					request.setAttribute(TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
					return mapping.findForward(DefConstants.FWD_DOMATR_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (devolucionDeudaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
				}
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.NAME);
		}
	}
	
	public ActionForward agregarListTraDevDeuDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TRADEVDEUDET, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TraspasoDevolucionDeudaAdapter traspasoDevolucionDeudaAdapterVO = (TraspasoDevolucionDeudaAdapter) userSession.get(TraspasoDevolucionDeudaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (traspasoDevolucionDeudaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TraspasoDevolucionDeudaAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(traspasoDevolucionDeudaAdapterVO, request);

            // Tiene errores recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			// obtencion de la accion y llamada al servicio segun corresponda
			AccionTraspasoDevolucion accion = traspasoDevolucionDeudaAdapterVO.getAccionTraspasoDevolucion(); 
			if (accion.getEsTraspaso()){
				traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().createListTraDeuDet(userSession, traspasoDevolucionDeudaAdapterVO);
			}else if (accion.getEsDevolucion()){
				traspasoDevolucionDeudaAdapterVO = GdeServiceLocator.getGestionDeudaJudicialService().createListDevDeuDet(userSession, traspasoDevolucionDeudaAdapterVO);
			} 
			
            // Tiene errores recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, traspasoDevolucionDeudaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (traspasoDevolucionDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + traspasoDevolucionDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME, traspasoDevolucionDeudaAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TraspasoDevolucionDeudaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TraspasoDevolucionDeudaAdapter.NAME);
		}
	}

	public ActionForward verDetalleDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		// esta pasando el selectedId por request compuesto por el idDeuda - idEstadoDeuda
		log.debug(funcName + " idCompuesto: " + request.getParameter("selectedId"));
		
		// vuelve al refill
		return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
				GdeConstants.ACTION_VER_DETALLE_DEUDA, BaseConstants.ACT_INICIALIZAR);
	}
	
	public ActionForward verConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		// esta pasando el selectedId por request del id de la constancia
		log.debug(funcName + " idCompuesto: " + request.getParameter("selectedId"));
		
		return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL, 
				GdeConstants.ACTION_ABM_CONDEUDET, BaseConstants.ACT_VER);
	}


	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TraspasoDevolucionDeudaAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		return this.baseRefill(mapping, form, request, response, funcName, TraspasoDevolucionDeudaAdapter.NAME);
	}
	
}
	
