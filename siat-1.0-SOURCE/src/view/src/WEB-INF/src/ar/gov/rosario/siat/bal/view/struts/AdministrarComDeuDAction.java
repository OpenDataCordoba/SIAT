//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.ComDeuAdapter;
import ar.gov.rosario.siat.bal.iface.model.ComDeuVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.NumberUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarComDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarComDeuDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMDEU, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			ComDeuAdapter comDeuAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getComDeuAdapterForDelete(userSession, commonKey)";
					comDeuAdapterVO = BalServiceLocator.getCompensacionService().getComDeuAdapterForView
						(userSession, commonKey);
					comDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.COMDEU_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_COMDEU_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getComDeuAdapterForCreate(userSession)";
					comDeuAdapterVO = BalServiceLocator.getCompensacionService().getComDeuAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_COMDEU_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (comDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + comDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				comDeuAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + ComDeuAdapter.NAME + ": "+ comDeuAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(ComDeuAdapter.NAME, comDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ComDeuAdapter.NAME, comDeuAdapterVO);
				
				saveDemodaMessages(request, comDeuAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ComDeuAdapter.NAME);
			}
		}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_COMDEU, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ComDeuAdapter comDeuAdapterVO = (ComDeuAdapter) userSession.get(ComDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (comDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ComDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ComDeuAdapter.NAME); 
				}

				// Limpia la lista de deuda seleccionada.
				comDeuAdapterVO.setListIdDeudaSelected(null);
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(comDeuAdapterVO, request);
				
	            // Tiene errores recuperables
				if (comDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, comDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Recorrer lista de deuda seleccionada y marcar el liqDeuda con marca pago por menos cuando corresponda.
				if(comDeuAdapterVO.getListIdDeudaSelected() != null){
					// Inicializa los mapas para borrar selecciones anteriores
					comDeuAdapterVO.setMapaValorDeuda(new HashMap<String, Double>());
					comDeuAdapterVO.setMapaCancelaPorMenos(new HashMap<String, Boolean>());
					for(String idDeudaSelected: comDeuAdapterVO.getListIdDeudaSelected()){
						// Se lee el valor de importe a cancelar seteado y se guarda en un mapa junto al id de la deuda seleccionada para compensar
						String importeStr = request.getParameter("valorDeuda"+idDeudaSelected);
						Double importe = NumberUtil.getDouble(importeStr);
						if(importe == null || importe == 0D){
							comDeuAdapterVO.addRecoverableError(BalError.COMDEU_IMPORTE_A_COMPENSAR_ERROR);
							break;
						}
						comDeuAdapterVO.getMapaValorDeuda().put(idDeudaSelected, importe);
						
						// Si esta seteado la cancelacion por menos
						if("on".equals(request.getParameter("cancelaPorMenos"+idDeudaSelected))){
							comDeuAdapterVO.getMapaCancelaPorMenos().put(idDeudaSelected, true);
						}
					}					
				}else{
					// Inicializa los mapas para borrar selecciones anteriores
					comDeuAdapterVO.setMapaValorDeuda(new HashMap<String, Double>());
					comDeuAdapterVO.setMapaCancelaPorMenos(new HashMap<String, Boolean>());
				}
				
				// Tiene errores recuperables
				if (comDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, comDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// llamada al servicio
				comDeuAdapterVO = BalServiceLocator.getCompensacionService().createListComDeu(userSession, comDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (comDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, comDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (comDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + comDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ComDeuAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ComDeuAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_COMDEU, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ComDeuAdapter comDeuAdapterVO = (ComDeuAdapter) userSession.get(ComDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (comDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ComDeuAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ComDeuAdapter.NAME); 
				}

				// llamada al servicio
				ComDeuVO comDeuVO = BalServiceLocator.getCompensacionService().deleteComDeu(userSession, comDeuAdapterVO.getComDeu());
				
	            // Tiene errores recuperables
				if (comDeuVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString());
					saveDemodaErrors(request, comDeuVO);				
					request.setAttribute(ComDeuAdapter.NAME, comDeuAdapterVO);
					return mapping.findForward(BalConstants.FWD_COMDEU_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (comDeuVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + comDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, ComDeuAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ComDeuAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, ComDeuAdapter.NAME);
		}


		public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
			
			// seteo la accion y el parametro para volver
			navModel.setPrevAction(mapping.getPath());
			navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

			// seteo los parametros para cuando oprima seleccionar
			navModel.setSelectAction("/bal/AdministrarComDeu");
			navModel.setSelectActionParameter("paramCuenta");
			navModel.setAgregarEnSeleccion(false);
			
			ComDeuAdapter  comDeuAdapter = (ComDeuAdapter) userSession.get(ComDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (comDeuAdapter == null) {
				log.error("error en: "  + funcName + ": " + ComDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ComDeuAdapter.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(comDeuAdapter, request);
			
	        // Tiene errores recuperables
			if (comDeuAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + comDeuAdapter.infoString()); 
				saveDemodaErrors(request, comDeuAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapter);
			}
			
			CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
			cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(comDeuAdapter.getRecurso());
			cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(comDeuAdapter.getCuenta().getNumeroCuenta());
			
			navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

			// seteo el act a ejecutar en el accion al cual me dirijo		
			navModel.setAct(BaseConstants.ACT_SELECCIONAR);
			
			return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
		}
		
		public ActionForward paramCuenta(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				ComDeuAdapter comDeuAdapterVO = (ComDeuAdapter) userSession.get(ComDeuAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (comDeuAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + ComDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ComDeuAdapter.NAME); 
				}

				// Seteo el id selecionado
				NavModel navModel = userSession.getNavModel();
				
				// Si el id esta vacio, pq selecciono volver, forwardeo 
				if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
					// Envio el VO al request				
					request.setAttribute(ComDeuAdapter.NAME, comDeuAdapterVO);
		
					return mapping.findForward(BalConstants.FWD_COMDEU_EDIT_ADAPTER);				
				}
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				comDeuAdapterVO.getCuenta().setId(commonKey.getId());
				
				// llamada al servicio
				comDeuAdapterVO = BalServiceLocator.getCompensacionService().getComDeuAdapterParamCuenta(userSession, comDeuAdapterVO);
				
	            // Tiene errores recuperables
				if (comDeuAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString()); 
					saveDemodaErrors(request, comDeuAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (comDeuAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + comDeuAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ComDeuAdapter.NAME, comDeuAdapterVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ComDeuAdapter.NAME, comDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(ComDeuAdapter.NAME, comDeuAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_COMDEU_EDIT_ADAPTER);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ComDeuAdapter.NAME);
			}
		}
		
		public ActionForward paramDeuda(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
				
				String funcName = DemodaUtil.currentMethodName();
				if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
				UserSession userSession = getCurrentUserSession(request, mapping); 
				if (userSession==null) return forwardErrorSession(request);
				
				try {
					// Bajo el adapter del userSession
					ComDeuAdapter comDeuAdapterVO = (ComDeuAdapter) userSession.get(ComDeuAdapter.NAME);
			
					// Si es nulo no se puede continuar
					if (comDeuAdapterVO == null) {
						log.error("error en: "  + funcName + ": " + ComDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
						return forwardErrorSessionNullObject(mapping, request, funcName, ComDeuAdapter.NAME); 
					}

					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(comDeuAdapterVO, request);
					
		            // Tiene errores recuperables
					if (comDeuAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString()); 
						saveDemodaErrors(request, comDeuAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapterVO);
					}
					
					// Llamada al servicio
					comDeuAdapterVO = BalServiceLocator.getCompensacionService().getComDeuAdapterParamDeuda(userSession, comDeuAdapterVO);
					
		            // Tiene errores recuperables
					if (comDeuAdapterVO.hasErrorRecoverable()) {
						log.error("recoverable error en: "  + funcName + ": " + comDeuAdapterVO.infoString()); 
						saveDemodaErrors(request, comDeuAdapterVO);
						return forwardErrorRecoverable(mapping, request, userSession, ComDeuAdapter.NAME, comDeuAdapterVO);
					}
					
					// Tiene errores no recuperables
					if (comDeuAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": " + comDeuAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, ComDeuAdapter.NAME, comDeuAdapterVO);
					}
					
					// Envio el VO al request
					request.setAttribute(ComDeuAdapter.NAME, comDeuAdapterVO);
					// Subo el adapter al userSession
					userSession.put(ComDeuAdapter.NAME, comDeuAdapterVO);
					
					return mapping.findForward(BalConstants.FWD_COMDEU_EDIT_ADAPTER);
				
				} catch (Exception exception) {
					return baseException(mapping, request, funcName, exception, ComDeuAdapter.NAME);
				}
		}

		public ActionForward refill(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

				String funcName = DemodaUtil.currentMethodName();
				return baseRefill(mapping, form, request, response, funcName, ComDeuAdapter.NAME);
				
		}
			
}
