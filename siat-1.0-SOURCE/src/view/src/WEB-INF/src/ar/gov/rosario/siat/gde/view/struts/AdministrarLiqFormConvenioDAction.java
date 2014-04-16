//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import java.util.Date;

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
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import ar.gov.rosario.siat.gde.iface.model.LiqPlanVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarLiqFormConvenioDAction extends
		BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqFormConvenioDAction.class);
	
	
	/**
	 * Metodo que se llama desde la liquidacion de la deuda
	 * 
	 * 
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);			
		if (userSession == null) return forwardErrorSession(request);
		
		LiqFormConvenioAdapter liqFormConvenioAdapterVO; 
		String stringServicio = "getLiqFormConvenioInit";
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idCuenta: " + request.getParameter("selectedId"));
			
			liqFormConvenioAdapterVO = new LiqFormConvenioAdapter();
			
			liqFormConvenioAdapterVO.getCuenta().setIdCuenta(Long.parseLong(request.getParameter("selectedId")));
			
			liqFormConvenioAdapterVO.setListIdDeudaSelected(request.getParameterValues("listIdDeudaSelected"));
			
			// Pasamos el filtro de la liquidacion de deuda.
			LiqCuentaVO liqCuentaFiltro=(LiqCuentaVO)userSession.get("liqCuentaFilter");
			
			if (liqCuentaFiltro==null){
				liqCuentaFiltro = new LiqCuentaVO();
			}
			
			liqFormConvenioAdapterVO.setCuentaFilter(liqCuentaFiltro);
			
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getLiqFormConvenioInit(userSession, 
					liqFormConvenioAdapterVO);
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Lo subo a la session con o sin error

			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				
				request.setAttribute("selectedId", liqFormConvenioAdapterVO.getCuenta().getIdCuenta());
									
				return this.volverACuenta(mapping, form, request, response);
				
			} else {
				
				log.error(funcName + " : liqFormConvenioAdapterVO " + liqFormConvenioAdapterVO.infoString());
				
				saveDemodaMessages(request, liqFormConvenioAdapterVO);		
				// Envio el VO al request
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	/**
	 * Metodo que se llama desde la Declaracion Jurada Masiva
	 * 
	 * 
	 */
	public ActionForward inicializarDecJurMas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);			
		if (userSession == null) return forwardErrorSession(request);
		
		LiqFormConvenioAdapter liqFormConvenioAdapterVO; 
		String stringServicio = "getLiqFormConvenioInit";
		
		try {
			// Recuperamos datos del form en el vo
			
			LiqDecJurAdapter liqDecJurAdapterVO = (LiqDecJurAdapter) userSession.getNavModel().getParameter(LiqDecJurAdapter.NAME);
			
			if (liqDecJurAdapterVO == null) return forwardErrorSession(request);

			log.debug(funcName + " idCuenta: " + liqDecJurAdapterVO.getCuenta().getIdCuenta());
			log.debug(funcName + " listId: " + liqDecJurAdapterVO.getListIdDeudaGenerada());
			log.debug(funcName + " fechaFor: " + liqDecJurAdapterVO.getFechaFormalizacionView());
			
			liqFormConvenioAdapterVO = new LiqFormConvenioAdapter();
			
			liqFormConvenioAdapterVO.getCuenta().setIdCuenta(liqDecJurAdapterVO.getCuenta().getIdCuenta());
			
			// Pasamos los ids de deuda generada
			liqFormConvenioAdapterVO.setListIdDeudaSelected(liqDecJurAdapterVO.getListIdDeudaGenerada());
			
			// Llamamos al init
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getLiqFormConvenioInit(userSession, 
																											liqFormConvenioAdapterVO);
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasError()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				
				for (DemodaStringMsg de:liqFormConvenioAdapterVO.getListError()){
					log.error(" 	ERROR: " + getValueFromBundle(de.key()));
				}
				
				// Lo subimos a la session para que el base confirmar le pida los errores.
				userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			liqFormConvenioAdapterVO.setFechaFormSelected(liqDecJurAdapterVO.getFechaFormalizacionView());
			
			// Llamada al getPlanes
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getPlanes(userSession, liqFormConvenioAdapterVO);
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			// Recupero los mensages de los properties para los planes deshabilitados
			for (LiqPlanVO liqPlanVO:liqFormConvenioAdapterVO.getListPlan()){
				if (!liqPlanVO.isEsSeleccionable()){
					liqPlanVO.setMsgDeshabilitado( getValueFromBundle(liqPlanVO.getMsgDeshabilitado()));
				}
			}
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/**
	 * Presenta las opciones de planes disponibles para realizar la formalizacion.
	 * 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward seleccionarPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPlanes";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getPlanes(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			// Recupero los mensages de los properties para los planes deshabilitados
			for (LiqPlanVO liqPlanVO:liqFormConvenioAdapterVO.getListPlan()){
				if (!liqPlanVO.isEsSeleccionable()){
					liqPlanVO.setMsgDeshabilitado( getValueFromBundle(liqPlanVO.getMsgDeshabilitado()));
				}
			}
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}

	
	/**
	 *	Forward a la pantalla View del mantendor de plan de pago  
	 * 
	 * @author Cristian
	 * @throws Exception
	 */
	public ActionForward verDetallePlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.LIQ_DEUDA, 
				GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			return 	baseForward(mapping,request, funcName, BaseConstants.ACT_REFILL, GdeConstants.FWD_VER_DETALLE_PLAN , BaseConstants.ACT_VER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/**
	 *	Cuando el usuario selecciona un plan, se le presentan todas las 
	 * 	alternativas de cuotas en las que puede formalizarlo.  
	 *  
	 *  - Permite imprimirlas
	 * 
	 * @author Cristian
	 * @throws Exception
	 */
	public ActionForward alternativaCuotas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getAlternativaCuotas";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			
			if (request.getParameter("idPlan") != null){
				liqFormConvenioAdapterVO.getPlanSelected().setIdPlan(Long.parseLong(request.getParameter("idPlan")));
			} else {
				liqFormConvenioAdapterVO.addRecoverableError(GdeError.MSG_PLAN_REQUERIDO);
			}

			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
	
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getAlternativaCuotas(userSession, liqFormConvenioAdapterVO);

			
			// Recupero los mensages de los properties para los planes deshabilitados
			for (LiqCuotaVO liqCuotaVO:liqFormConvenioAdapterVO.getPlanSelected().getListAltCuotas()){
				if (!liqCuotaVO.getEsSeleccionable()){
					liqCuotaVO.setMsgErrorCuota(getValueFromBundle(liqCuotaVO.getMsgErrorCuota()));
				}
			}
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_CUOTAS_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/**
	 * - Muestra la simulacion para un numero de cuotas seleccionadas
	 * - Permite su impresion. 
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward verSimulacionCuotas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "reconfeccionar";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			
			log.debug(funcName + " nroCuota: " + request.getParameter("selectedId"));
			
			if (request.getParameter("selectedId") != null){
				liqFormConvenioAdapterVO.setNroCuotaSelected(request.getParameter("selectedId"));
			} else {
				//TODO: validar que halla seleccionado un plan
				
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getSimulacionCuotas(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_CUOTAS_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_SIMULA_CUOTAS_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/**
	 * Redirige a la pantalla final de formalizacion, donde se ingresan los datos de 
	 * la persona que formaliza, y observaciones.  
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward formalizarPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getFormalizarPlanInit";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
		
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			
			log.debug(funcName + " nroCuota: " + request.getParameter("nroCuota"));
			
			if (request.getParameter("nroCuota") != null){
				liqFormConvenioAdapterVO.setNroCuotaSelected(request.getParameter("nroCuota"));
			} else {
				liqFormConvenioAdapterVO.addRecoverableError(GdeError.MSG_NRO_CUOTA_REQUERIDO);
			}
			
			if (liqFormConvenioAdapterVO.getEsEspecial() && DateUtil.isDateAfter(liqFormConvenioAdapterVO.getFechaFormalizacion(), new Date())){
				liqFormConvenioAdapterVO.addRecoverableError(GdeError.CONVENIO_FECHAFOR_INVALIDA);
			}
			if (!liqFormConvenioAdapterVO.getEsEspecial() && DateUtil.isDateAfter(DateUtil.getDate(liqFormConvenioAdapterVO.getFechaFormSelected()), new Date())){
				liqFormConvenioAdapterVO.addRecoverableError(GdeError.CONVENIO_FECHAFOR_INVALIDA);
			}

			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_CUOTAS_ADAPTER);
			}
			
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getFormalizarPlanInit(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_CUOTAS_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	/**
	 * Si el convenio cumple con todas las validaciones, se realiza el grabado del mismo
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward grabarFormalizacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getFormalizarPlanInit";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Si el convenio ya esta formalizado forward to print (por ulizar goBack del browser)
			if (liqFormConvenioAdapterVO.getConvenio().getIdConvenio() != null){
				return getConvenioFormalizado(mapping, form, request, response);
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
																							 
			liqFormConvenioAdapterVO.getConvenio().setObservacionFor(request.getParameter("convenio.observacionFor"));
						
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				
				liqFormConvenioAdapterVO.getConvenio().getPersona().setPersonaBuscada(false);
				
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().formalizarPlan(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				saveDemodaMessages(request, liqFormConvenioAdapterVO);
				
				liqFormConvenioAdapterVO.getConvenio().getPersona().setPersonaBuscada(false);
				
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			// recupero el navMocel del usserSession
			NavModel navModel = userSession.getNavModel();
			
			//le seteo la accion a donde ir al navModel
			navModel.setConfAction("/gde/AdministrarLiqFormConvenio");
			navModel.setConfActionParameter("getConvenioFormalizado");
						
			// me dirije al mensaje de confirmacion OK
			return this.forwardMessage(mapping, navModel, 
				NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/**
	 * Obtiene un convenio ya formalizado para la impresion del formulario de formalizacion y
	 * los recibos de cuotas.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward getConvenioFormalizado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)return forwardErrorSession(request);
		
		try {
			
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);

			// Llamada al servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getConvenioFormalizado(userSession, liqFormConvenioAdapterVO);
	        			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PRINT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	
	public ActionForward buscarPersonaSimple(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}
			
			// Reseteamos la persona utilizada como filtro
			liqFormConvenioAdapterVO.getConvenio().setPersona(new PersonaVO());
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			liqFormConvenioAdapterVO.getConvenio().setObservacionFor(request.getParameter("convenio.observacionFor"));
			// Este copiado debe ser de esta manera ya que no utilizamos Common Models
			
            // Tiene errores recuperables solo puede ser por formato en el numero de documento
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
								
				liqFormConvenioAdapterVO.getConvenio().getPersona().setPersonaBuscada(true);
				liqFormConvenioAdapterVO.getConvenio().getPersona().addRecoverableValueError("El formato del campo N\u00FAmero Doc. es incorrecto");
				
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			}
			
			liqFormConvenioAdapterVO.getConvenio().setPersona(PadServiceLocator.getPadPersonaService().
					getPersonaBySexoyNroDoc(userSession,liqFormConvenioAdapterVO.getConvenio().getPersona()));
			
			if (liqFormConvenioAdapterVO.getConvenio().getPersona().isPersonaEncontrada())
				liqFormConvenioAdapterVO.getConvenio().setPoseeDatosPersona(true);
			else
				liqFormConvenioAdapterVO.getConvenio().setPoseeDatosPersona(false);
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	
	public ActionForward limpiarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			liqFormConvenioAdapterVO.getConvenio().setObservacionFor(request.getParameter("convenio.observacionFor"));
			// Este copiado debe ser de esta manera ya que no utilizamos Common Models
			
			liqFormConvenioAdapterVO.getConvenio().setPersona(new PersonaVO());
			liqFormConvenioAdapterVO.getConvenio().getPersona().setPersonaBuscada(true);
			liqFormConvenioAdapterVO.getConvenio().getPersona().setPersonaEncontrada(false);
			liqFormConvenioAdapterVO.getConvenio().setPoseeDatosPersona(false);
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			liqFormConvenioAdapterVO.getConvenio().setObservacionFor(request.getParameter("convenio.observacionFor"));
			// Este copiado debe ser de esta manera ya que no utilizamos Common Models
			
            // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			return forwardSeleccionar(mapping, request, 
					GdeConstants.METOD_FORMCONVENIO_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}

	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId;
			Boolean personaModificada = (Boolean) userSession.get("modificar");
			if(personaModificada != null && personaModificada){
				selectedId = liqFormConvenioAdapterVO.getConvenio().getPersona().getId().toString();
				userSession.put("modificar", null);
			}else{
				selectedId = navModel.getSelectedId();				
			}
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER); 
			}

			// llamo al param del servicio
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().paramPersona
							(userSession, liqFormConvenioAdapterVO, new Long(selectedId));

            // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {

			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);
			
			
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	/**
	 * Utilizado para volver desde el el detalle de una Deuda 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverACuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			log.debug(funcName + " selectedId: " + request.getParameter("selectedId"));
			
			Long selectedId = new Long(request.getParameter("selectedId"));
			log.debug("selectedId"+selectedId);
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId + "&validAuto=false";
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			return  new ActionForward (pathVerCuenta);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}

	/**
	 * Vuelve a la primer pantalla de la formalizacion segun corresponda a Plan Manual o no. 
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverAInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapterByIdCuenta";
		try {

			// Recuperamos datos de la session		
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);
			
			liqFormConvenioAdapterVO = GdeServiceLocator.getFormConvenioDeudaService().getLiqFormConvenioInit(userSession, 
					liqFormConvenioAdapterVO);
			
			 // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

			//	request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);				
			
			log.debug(funcName + " Es Especial: " + liqFormConvenioAdapterVO.getEsEspecial() );
			if (liqFormConvenioAdapterVO.getEsEspecial()){
				return mapping.findForward(GdeConstants.ACTION_FORMCONVENIOESP);				
			}else{
				return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_ADAPTER);				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Volver desde las alternativas de cuotas a la seleccion de planes.
	 *  
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverAPlanes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {

			// Recuperamos datos de la session		
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);				
			
			return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 *	Forward a la jsp liqFormConvenioImpForm 
	 *  la cual llama a ImprimirConvenio con el metod getFormPDF().
	 * 
	 * @author Cristian
	 * @throws Exception
	 */
	public ActionForward printForm(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);		
		liqFormConvenioAdapterVO.setPrevActionParameter(GdeConstants.ACTION_FORMCONVENIO);
		
		return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_IMP_FORM);		
	}
	
	/**
	 *	Forward a la jsp liqFormConvenioImpRecibos 
	 *  la cual llama a ImprimirConvenio getRecibosPDF.
	 * 
	 * @author Cristian
	 * @throws Exception
	 */
	public ActionForward printRecibos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_DEUDA, GdeSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);		
		liqFormConvenioAdapterVO.setPrevActionParameter(GdeConstants.ACTION_FORMCONVENIO);
		
		return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_IMP_RECIBOS);		
	}

	
	public ActionForward printAltCuotas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);		
				
		request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
		
		return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_IMP_ALTCUOTAS);		
	}
	
	
	public ActionForward getAltCuotasPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPrintForm";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print  = GdeServiceLocator.getFormConvenioDeudaService().getPrintAltCuotas(userSession, liqFormConvenioAdapterVO);
						
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	public ActionForward printFormAuto(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		// Parameter para el volver del ImprimirConvenio
		LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);		
				
		request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
		
		return mapping.findForward(GdeConstants.FWD_FORMCONVENIO_IMP_FORM_AUTO);		
	}
	
	
	public ActionForward getFormAutoPDF(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping,
				GdeSecurityConstants.LIQ_CONVENIOCUENTA, GdeSecurityConstants.MTD_IMPRIMIR_FORM_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPrintForm";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
						
			// Llamada al servicio
			PrintModel print  = GdeServiceLocator.getFormConvenioDeudaService().getPrintFormAuto(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			baseResponsePrintModel(response, print);
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	/**
	 * Redirige al menu, una vez que se han impreso el formulario de formalizacion y los recibos.
	 * 
	 * 
	 * @author Cristian
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward finalizar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter) userSession.get(LiqFormConvenioAdapter.NAME);
			
			liqFormConvenioAdapterVO.setPrevAction(BaseConstants.ACT_SIAT_MENU);
			liqFormConvenioAdapterVO.setPrevActionParameter("build");
						
			return baseVolver(mapping, form, request, response, LiqFormConvenioAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
	public ActionForward modificarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (liqFormConvenioAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + LiqFormConvenioAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LiqFormConvenioAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			liqFormConvenioAdapterVO.getConvenio().setObservacionFor(request.getParameter("convenio.observacionFor"));
			// Este copiado debe ser de esta manera ya que no utilizamos Common Models
			
            // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			NavModel navModel = userSession.getNavModel();
			
			// seteo la accion y el parametro para volver
			navModel.setPrevAction(mapping.getPath());
			navModel.setPrevActionParameter("paramPersona");

			userSession.put("modificar", true);
			// seteo el act a ejecutar en el accion al cual me dirijo
			navModel.setAct("modificar");

			return new ActionForward("/pad/AdministrarPersona.do?method=inicializar");
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
}