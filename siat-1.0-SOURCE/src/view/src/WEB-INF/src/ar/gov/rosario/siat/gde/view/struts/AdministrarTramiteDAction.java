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
import ar.gov.rosario.siat.gde.iface.model.InformeDeudaCaratula;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.TipoTramiteVO;
import ar.gov.rosario.siat.gde.iface.model.TramiteAdapter;
import ar.gov.rosario.siat.gde.iface.model.TramiteSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarTramiteDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTramiteDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request); 
		act="buscar"; //lo harcodeamos porque detectamos que durante la navegacion, se pierde el currentAct del navModel.
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.INFORME_DEUDA_ESCRIBANO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TramiteAdapter tramiteAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			// TODO: ver como resolver el act para utilizar el mismo inicializar para desmarcar tramite.
			//if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTramiteAdapterInit(userSession, commonKey)";
				tramiteAdapterVO = GdeServiceLocator.getGestionDeudaService().getTramiteAdapterInit(userSession);
				
				tramiteAdapterVO.setIdCuenta(commonKey.getId());				
				actionForward = mapping.findForward(GdeConstants.FWD_TRAMITE_ADAPTER);
			//}
						

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (tramiteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tramiteAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteAdapter.NAME, tramiteAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tramiteAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TramiteAdapter.NAME + ": "+ tramiteAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TramiteAdapter.NAME, tramiteAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TramiteAdapter.NAME, tramiteAdapterVO);
			 
			saveDemodaMessages(request, tramiteAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteAdapter.NAME);
		}
	}
	
	
	public ActionForward validaTramite(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			TramiteAdapter tramiteAdapterVO = (TramiteAdapter) userSession.get(TramiteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteAdapter.NAME); 
			}
			
			DemodaUtil.populateVO(tramiteAdapterVO, request);
			
            // Tiene errores recuperables
			if (tramiteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteAdapter.NAME, tramiteAdapterVO);
			}
			
			// Llamada al servicio	
			tramiteAdapterVO = GdeServiceLocator.getGestionDeudaService().validarTramite(userSession, tramiteAdapterVO);			
			
			// Tiene errores recuperables
			if (tramiteAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteAdapterVO.infoString()); 
				saveDemodaErrors(request, tramiteAdapterVO);
				request.setAttribute(TramiteSearchPage.NAME, tramiteAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteAdapter.NAME, tramiteAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteAdapter.NAME, tramiteAdapterVO);
			}
			
			if (tramiteAdapterVO.isEsValido()){
				
				userSession.put(TramiteAdapter.NAME, tramiteAdapterVO);
				return mapping.findForward(GdeConstants.FWD_TRAMITE_PRINT);
			}

			return null;
						
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteAdapter.NAME);
		}
	}

	//TODO: Desmarcar Tramite
	
	
	
	public ActionForward imprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			TramiteAdapter tramiteAdapterVO = (TramiteAdapter) userSession.get(TramiteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteAdapter.NAME); 
			}
			
			if (tramiteAdapterVO.isEsValido()){
				
				// Registrar uso Tramite
				tramiteAdapterVO = GdeServiceLocator.getGestionDeudaService().registrarUsoTramite(userSession, tramiteAdapterVO);
				
				// Forward impresion Informe Deuda
				if ( TipoTramiteVO.COD_SELLADO_CONSULTA.equals(tramiteAdapterVO.getTramite().getTipoTramite().getCodTipoTramite())) {
					
					PrintModel print = GdeServiceLocator.getGestionDeudaService().imprimirInformeDeudaEscribano(userSession, 
							tramiteAdapterVO.getIdCuenta(), null);
					
					
					baseResponsePrintModel(response, print);
					return null;
				}
				
				//Forward impresion Libre Deuda 
				if ( TipoTramiteVO.COD_SELLADO_LIBREDEUDA.equals(tramiteAdapterVO.getTramite().getTipoTramite().getCodTipoTramite())) {
					
					InformeDeudaCaratula informeDeudaCaratula = new InformeDeudaCaratula();
					
					informeDeudaCaratula.setDesTipoTramite("SELLADO - LIBRE DEUDA");					
					informeDeudaCaratula.setNroRecibo(tramiteAdapterVO.getTramite().getNroReciboForCaratula());
					informeDeudaCaratula.setNroLiquidacion(tramiteAdapterVO.getNroLiquidacion());
					
					// Devuelve el print model con la caratula seteada y el nroLiquidacion Generado
					PrintModel print = GdeServiceLocator.getGestionDeudaService().imprimirInformeDeudaEscribano(userSession, 
							tramiteAdapterVO.getIdCuenta(), informeDeudaCaratula);
					
					
					baseResponsePrintModel(response, print);
					return null;
				}
			}  
			
			return null;
				
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteAdapter.NAME);
		}
		
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TramiteAdapter.NAME);
		
	}
	
	public ActionForward volverACuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, "verCuenta");
		if (userSession == null) return forwardErrorSession(request);

		
		try {
			// Bajo el adapter del userSession
			TramiteAdapter tramiteAdapterVO = (TramiteAdapter) userSession.get(TramiteAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteAdapter.NAME); 
			}
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + tramiteAdapterVO.getIdCuenta() + "&validAuto=false";
			
			request.setAttribute("liqDeudaVieneDe", "Informe Deuda Escribano");
			
			userSession.remove(TramiteAdapter.NAME);
			
			return new ActionForward(pathVerCuenta);
			
		} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
		
}
