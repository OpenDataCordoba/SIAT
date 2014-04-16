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

public final class AdministrarVolantePagoInteresesRSDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarVolantePagoInteresesRSDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_VOLANTEPAGOINTRS);
			if (userSession == null) return forwardErrorSession(request);
						
			LiqReconfeccionAdapter liqReconfAdapterVO = null;
			
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			
			String stringServicio = "getLiqDeudaReconfeccionAdapterInit: Volante de Pago de Intereses RS";
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
				
				liqReconfAdapterVO = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getLiqReconfeccionAdapterForVolantePagoIntRS(userSession, idCuenta, listIdDeudaSelected, liqCuentaFiltro);
				
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
				
				
				// Si el usuario es anomimo grabamos el tramite web
				if(userSession.getIsAnonimo()){
					// Grabamos el tramite web ******************************************************************
					LiqTramiteWeb liqTramiteWeb = new LiqTramiteWeb();
					
					if (liqReconfAdapterVO.isTieneDeudaVencida()) {
						liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_RECONFECCION_DEUDA); // TODO Modificar auditoria. Nuevo tipo de Tramite?
					} else {
						liqTramiteWeb.setTipoTramite(LiqTramiteWeb.COD_REIMP_DEUDA);
					}
					
					liqTramiteWeb.setIdObjeto(liqReconfAdapterVO.getCuenta().getCodRecurso() +  liqReconfAdapterVO.getCuenta().getNroCuenta());
					
					PadServiceLocator.getVariosWebFacade().grabarTramiteWeb(liqTramiteWeb);
					// Fin tramite web **************************************************************************
				} 
				
				String findFoward = "";
					
				if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqReconfeccionAdapter.NAME + ": "+ liqReconfAdapterVO.infoString());
				
				request.setAttribute(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
				userSession.put(LiqReconfeccionAdapter.NAME, liqReconfAdapterVO);
				saveDemodaMessages(request, liqReconfAdapterVO);
				findFoward = GdeConstants.FWD_VOLANTEPAGOINTRS_ADAPTER;
			
				return mapping.findForward(findFoward);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, LiqReconfeccionAdapter.NAME);
			}
	}
	
	/**
	 * Generar Volante de Pago de Intereses de Regimen Simplificado DREI/ETUR 
	 * 
	 * @author 
	 * @return
	 * @throws Exception
	 */
	public ActionForward generarVolante(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_VOLANTEPAGOINTRS);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "volantePagoIntRS";
		try {
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqReconfeccionAdapterVO, request);

			// Llamada al servicio
			liqReconfeccionAdapterVO = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().generarVolantePagoIntRS(userSession, liqReconfeccionAdapterVO);
			
	        // Tiene errores recuperables
			if (liqReconfeccionAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqReconfeccionAdapterVO.infoString());
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
			return mapping.findForward(GdeConstants.FWD_VOLANTEPAGOINTRS_FINALIZADA);			
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
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_VOLANTEPAGOINTRS);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "volantePagoIntRS";
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
			
			return mapping.findForward(GdeConstants.FWD_VOLANTEPAGOINTRS_IMP_RECIBOS);			
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
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_VOLANTEPAGOINTRS);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "volantePagoIntRS";
		try {
			PrintModel print;
			LiqReconfeccionAdapter liqReconfeccionAdapterVO = (LiqReconfeccionAdapter)userSession.get(LiqReconfeccionAdapter.NAME);
						
			// Llamada al servicio
			print  = GdeServiceLocator.getReconfeccionDeudaServiceHbmImpl().getImprimirRecibos(userSession, liqReconfeccionAdapterVO.getListRecibos());

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

	
}