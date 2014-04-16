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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.LiqConvenioCuotaSaldoAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqCuentaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqReconfeccionAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqTramiteWeb;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarLiqReconfeccionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqReconfeccionDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECONFECCIONAR);
			if (userSession == null) return forwardErrorSession(request);
			
			Boolean esReconfEspecial = (Boolean) userSession.getNavModel().getParameter("esReconfEspecial");
			if (esReconfEspecial == null) { 
				esReconfEspecial = false;
			}
			
			LiqReconfeccionAdapter liqReconfAdapterVO = null;
			
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			
			String stringServicio = "getLiqDeudaReconfeccionAdapterInit";
			try {
				// Recuperamos datos del form en el vo
				log.debug(funcName + " idCuenta: "
						+ request.getParameter("selectedId") + "listIdDeudaSelected: " 
						+ request.getParameter("listIdDeudaSelected"));

				// Validar que halla registros de deuda seleccionados
				// Mostrar los ids sumbitidos

				// cargo los ids de deuda seleccionadas.
				String[] listIdDeudaSelected = request.getParameterValues("listIdDeudaSelected");

				if (listIdDeudaSelected != null && listIdDeudaSelected.length>0) {
					for (int i = 0; i < listIdDeudaSelected.length; i++) {
						log.debug(funcName + " idDeuda Selected="
								+ listIdDeudaSelected[i]);
					}
				}
				
				Long idCuenta = Long.parseLong(request.getParameter("selectedId"));
				
				liqReconfAdapterVO = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getLiqReconfeccionAdapter(
						userSession, idCuenta, listIdDeudaSelected, esReconfEspecial, liqCuentaFiltro);
				
		        // Tiene errores recuperables
				if (liqReconfAdapterVO.hasErrorRecoverable()) {
					// subimos el adapter con el error cargado para que lo tome "fwdDeudaAdapter".
					userSession.put(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
					request.setAttribute("selectedId", request.getParameter("selectedId"));

					return this.volverACuenta(mapping, form, request, response);
				}
				
				// Tiene errores no recuperables
				if (liqReconfAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReconfAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
				}
				
				// aqui verificamos si hay alguna deuda vencida. 
				// en caso afirmativo mandamos al jsp para que el usuario ingrese la fecha de vencimiento.
				// en caso negativo, no hay nada que pedir, mandamos a reconfeccionar directamente, que en este caso ser'a 
				// una simple reimpresi'on de la deuda no vencida
	
				
				// Si el usuario es anomimo grabamos el tramite web
				if(userSession.getIsAnonimo()){
					// Grabamos el tramite web ******************************************************************
					LiqTramiteWeb liqTramiteWeb = new LiqTramiteWeb();
					
					if (liqReconfAdapterVO.isTieneDeudaVencida()) {
						liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_RECONFECCION_DEUDA);
					} else {
						liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_REIMP_DEUDA);
					}
					
					liqTramiteWeb.setIdObjeto(liqReconfAdapterVO.getCuenta().getCodRecurso() +  
											  liqReconfAdapterVO.getCuenta().getNroCuenta());
					
					PadServiceLocator.getVariosWebFacade().grabarTramiteWeb(liqTramiteWeb);
					// Fin tramite web **************************************************************************
				} 
				
				String findFoward = "";
				if (liqReconfAdapterVO.isTieneDeudaVencida() || liqReconfAdapterVO.getCuenta().getEsRecursoAutoliquidable()==true) {
					
					if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfAdapterVO.infoString());
					
					request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
					userSession.put(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
					saveDemodaMessages(request, liqReconfAdapterVO);
					findFoward = GdeConstants.FWD_RECONFECCION_ADAPTER;
				
				} else {
					// no tiene deuda vencida
					// se ejecuta lo mismo que en el metodo reconfeccionar
					

					// Llamada al servicio
					LiqReconfeccionAdapter liqReconfeccionAdapterVO = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().reconfeccionar(userSession, liqReconfAdapterVO);
					
			        // Tiene errores recuperables
					if (liqReconfeccionAdapterVO.hasErrorRecoverable()) {
						// Envio el VO al request
						request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
						userSession.put(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
						saveDemodaErrors(request, liqReconfeccionAdapterVO);
						return mapping.getInputForward();
					}
					
					// Tiene errores no recuperables
					if (liqReconfeccionAdapterVO.hasErrorNonRecoverable()) {
						log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReconfeccionAdapterVO.errorString()); 
						return forwardErrorNonRecoverable(mapping, request, funcName, LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
					}
					
					if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfeccionAdapterVO.infoString());
					
					// Envio el VO al request
					request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
					userSession.put(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
					
					saveDemodaMessages(request, liqReconfeccionAdapterVO);	
					findFoward = GdeConstants.FWD_RECONFECCION_FINALIZADA;
				}
				return mapping.findForward(findFoward);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, LiqReconfeccionAdapter.NAME);
			}
	}
	
	/**
	 * 
	 * @author 
	 * @return
	 * @throws Exception
	 */
	public ActionForward reconfeccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECONFECCIONAR);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "reconfeccionar";
		try {
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqReconfeccionAdapterVO, request);

			// Llamada al servicio
			liqReconfeccionAdapterVO = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().reconfeccionar(userSession, liqReconfeccionAdapterVO);
			
			log.debug("Es reimpresion de cuotas: "+liqReconfeccionAdapterVO.getEsReimpresionCuotas());
	        // Tiene errores recuperables
			if (liqReconfeccionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqReconfeccionAdapterVO.infoString());
				// Envio el VO al request
				if (liqReconfeccionAdapterVO.getEsCuotaSaldo()){
					LiqConvenioCuotaSaldoAdapter liqConvenioCuotaSaldo = (LiqConvenioCuotaSaldoAdapter)userSession.get(LiqConvenioCuotaSaldoAdapter.NAME);
					liqReconfeccionAdapterVO.passErrorMessages(liqConvenioCuotaSaldo);
					request.setAttribute(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldo);
					userSession.put(LiqConvenioCuotaSaldoAdapter.NAME, liqConvenioCuotaSaldo);
					saveDemodaErrors(request, liqConvenioCuotaSaldo);
					
					return mapping.findForward(GdeConstants.FWD_LIQCONVENIOCUOTASALDO_ADAPTER);
				}
				request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
				userSession.put(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
				saveDemodaErrors(request, liqReconfeccionAdapterVO);
				

				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqReconfeccionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReconfeccionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfeccionAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
			
			userSession.put(LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
			
			saveDemodaMessages(request, liqReconfeccionAdapterVO);	
			return mapping.findForward(GdeConstants.FWD_RECONFECCION_FINALIZADA);
			
		} catch (Exception exception) {
			log.debug("problema "+exception.getLocalizedMessage());
			return baseException(mapping, request, funcName, exception,
					LiqReconfeccionAdapter.NAME);
		}
	}

	public ActionForward impRecibos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECONFECCIONAR);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "reconfeccionar";
		try {
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
									
	        // Tiene errores recuperables
			if (liqReconfeccionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqReconfeccionAdapterVO.infoString()); 
				saveDemodaErrors(request, liqReconfeccionAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqReconfeccionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReconfeccionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
			}

			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfeccionAdapterVO.infoString());
			
			return mapping.findForward(GdeConstants.FWD_RECONFECCION_IMP_RECIBOS);			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqReconfeccionAdapter.NAME);
		}
	}
	
	public ActionForward getPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_RECONFECCIONAR);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "reconfeccionar";
		try {
			PrintModel print;
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
						
			// Llamada al servicio
			if (liqReconfeccionAdapterVO.getEsReimpresionCuotas()){
				print = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getImprimirRecibosCuotas(userSession, liqReconfeccionAdapterVO.getListRecibos());
			} else {
				print  = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getImprimirRecibos(userSession, liqReconfeccionAdapterVO.getListRecibos());
			}
	        // Tiene errores recuperables
			if (liqReconfeccionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqReconfeccionAdapterVO.infoString()); 
				saveDemodaErrors(request, liqReconfeccionAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqReconfeccionAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqReconfeccionAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqReconfeccionAdapter.NAME, liqReconfeccionAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfeccionAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqReconfeccionAdapter.NAME);
		}
	}
	
	/**
	 * Volver al menu principal
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverAlMenu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		return new ActionForward(BaseConstants.FWD_SIAT_BUILD_MENU);

	}


	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = "volver";
		if (log.isDebugEnabled())
			log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		try {

			String actForward = "";

			// Si alguien me clavo un navModel con el nombre "volver", vuelvo a
			// ese lugar
			NavModel navModelVolver = (NavModel) navModel
					.getParameter(BaseConstants.ACT_VOLVER);

			if (navModelVolver != null) {
				actForward = StringUtil.getActionPath(navModelVolver
						.getPrevAction(), navModelVolver
						.getPrevActionParameter());
				navModel.clearParametersMap();
			} else {
				// Seteo de valores para volver al Ingreso GR
				actForward = StringUtil.getActionPath(
						GdeConstants.PATH_INICIALIZAR_GR,
						GdeConstants.ACTION_INGRESAR_LIQDEUDA_GR);
			}

			log.debug(funcName + ": intentando volver a :" + actForward);

			return new ActionForward(actForward);
		} catch (Exception e) {
			log.error("Exception - ", e);
			e.printStackTrace();
			// falta definir llamada o no a logout
			return forwardErrorSession(mapping, request);
		}
	}

	public ActionForward volverACuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
			
			// Si es una reimpresion de cuotas de convenio.
			if (liqReconfeccionAdapterVO !=null && liqReconfeccionAdapterVO.getEsReimpresionCuotas()){
				return new ActionForward(GdeConstants.PATH_VER_CONVENIO+liqReconfeccionAdapterVO.getConvenio().getIdConvenio());
			}
			
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));

			Long selectedId;
			
			if (request.getParameter("selectedId") != null ){ 			
				selectedId = liqReconfeccionAdapterVO.getCuenta().getIdCuenta();
			} else {
				selectedId = new Long(request.getParameter("selectedId"));
			}
			
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}	

	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			LiqReconfeccionAdapter adapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqReconfeccionAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqReconfeccionAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCasoContainer().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCasoContainer()); 
			
			adapterVO.getCasoContainer().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(LiqReconfeccionAdapter.NAME, adapterVO);
			
			return mapping.findForward(GdeConstants.FWD_RECONFECCION_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqReconfeccionAdapter.NAME);
		}	
	}
}