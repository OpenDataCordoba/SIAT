//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.view.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cyq.iface.model.LiqDeudaCyqAdapter;
import ar.gov.rosario.siat.cyq.iface.service.CyqServiceLocator;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import ar.gov.rosario.siat.cyq.view.util.CyqConstants;
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqFormConvenioAdapter;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class AdministrarLiqFormConvenioEspDAction extends
		BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqFormConvenioEspDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);			
		if (userSession == null) return forwardErrorSession(request);
		
		LiqFormConvenioAdapter liqFormConvenioAdapterVO; 
		String stringServicio = "getLiqFormConvenioInit";
		
		try {
			// Recuperamos datos del form en el vo
			log.debug(funcName + " idProcedimiento: " + request.getParameter("selectedId"));
			
			// La instanciacion se realiza aqui por motivos particulares de navegacion
			liqFormConvenioAdapterVO = new LiqFormConvenioAdapter();					
		
			liqFormConvenioAdapterVO.getProcedimiento().setId(Long.parseLong(request.getParameter("selectedId")));
			
			liqFormConvenioAdapterVO.setListIdDeudaSelected(request.getParameterValues("listIdDeudaSelected"));
			
			liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getLiqFormConvenioInit(userSession, liqFormConvenioAdapterVO);
			
			liqFormConvenioAdapterVO.setEsEspecial(true);
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Lo subo a la session con o sin error
			liqFormConvenioAdapterVO.setValuesFromNavModel(userSession.getNavModel());
			
			// Lo subo a la session con o sin error
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			log.error(funcName + " : liqFormConvenioAdapterVO " + liqFormConvenioAdapterVO.infoString());
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaMessages(request, liqFormConvenioAdapterVO);		
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIOESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqFormConvenioAdapter.NAME);
		}
	}
	
	
	public ActionForward refillInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

	
		return mapping.findForward(CyqConstants.FWD_FORMCONVENIOESP_ADAPTER);
	}
	
	/**
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

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getPlanesEsp";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);

			// Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(CyqConstants.FWD_FORMCONVENIOESP_ADAPTER);
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getPlanesEsp(userSession, liqFormConvenioAdapterVO);

	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(CyqConstants.FWD_FORMCONVENIOESP_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			log.debug(funcName + " Es Especial: " + liqFormConvenioAdapterVO.getEsEspecial() );
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
						
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_PLANES_ESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
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
	
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getAlternativaCuotas";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(liqFormConvenioAdapterVO, request);
			
			if (request.getParameter("idPlan") != null){
				liqFormConvenioAdapterVO.getPlanSelected().setIdPlan(Long.parseLong(request.getParameter("idPlan")));
			} else {
				liqFormConvenioAdapterVO.addRecoverableError(CyqError.MSG_PLAN_REQUERIDO);
			}

			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);
	
				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_PLANES_ADAPTER);
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getSimulacionCuotasEsp(userSession, liqFormConvenioAdapterVO);

			
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
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_CUOTAS_ESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}

	
	public ActionForward modificarCuotas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_CUOTAS_ESP_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	public ActionForward validarCuotas(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "validarCuotas";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			liqFormConvenioAdapterVO.clearErrorMessages();
			
			// Recuperamos datos del form en el vo
			Integer cantidadCuotas = liqFormConvenioAdapterVO.getCantMaxCuo();
			
			for (int i=1; i <= cantidadCuotas.intValue(); i++ ){
				
				log.debug( " *********************   "  + i );
				
				log.debug( request.getParameter("capital" + i));
				log.debug( request.getParameter("interes" + i));
				log.debug( request.getParameter("fechaVto" + i));
				
				// Capital
				String capitalString = request.getParameter("capital" + i);
				try{
					Double capital = null;
					
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setCapitalView(capitalString);
					
					// Si es "" o espacios, nuleo el valor.
					if (capitalString.trim().equals("") ){
						liqFormConvenioAdapterVO.addRecoverableValueError("El Capital para la cuota " + i + " es Requerido.");

					// Validacion de Formato
					} else {
						capital = new Double(capitalString);
						liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setCapital(capital);
					} 
					
				} catch (Exception e){
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setCapital(null);
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setCapitalView(capitalString);
					liqFormConvenioAdapterVO.addRecoverableValueError("El formato del Capital para la cuota " + i + " es invalido.");
				}
				
				// Interes
				String interesString = request.getParameter("interes" + i);
				try{
					Double interes = null;
					
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setInteresView(interesString);
					
					// Si es "" o espacios, nuleo el valor.
					if (interesString.trim().equals("") ){
						liqFormConvenioAdapterVO.addRecoverableValueError("El Interes para la cuota " + i + " es Requerido.");
					
					// Validacion de Formato
					} else { 
						interes = new Double(interesString);
						liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setInteres(interes);
					}
					
				} catch (Exception e){
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setInteres(null);
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setInteresView(interesString);
					liqFormConvenioAdapterVO.addRecoverableValueError("El formato del Interes para la cuota " + i + " es invalido.");
				}
				
				// Fecha Vencimiento
				try{
					String valorString = request.getParameter("fechaVto" + i);
					// Si es "" o espacios, nuleo el valor.
					if (valorString.trim().equals("") ){
						liqFormConvenioAdapterVO.addRecoverableValueError("La fecha de Vencimiento para la cuota " + i + " es Requerida.");
					// Validacion de Formato
					} else if(!DateUtil.isValidDate(valorString, DateUtil.ddSMMSYYYY_MASK)){
						liqFormConvenioAdapterVO.addRecoverableValueError("El formato de la fecha de Vencimiento para la cuota " + i + " es invalido.");	
					}
					
					liqFormConvenioAdapterVO.getPlanSelected().getListCuotasForm().get(i -1).setFechaVto(valorString);
					
				} catch (Exception e){
				}
				
			}
			
			// Llamada al servicio
			if (!liqFormConvenioAdapterVO.hasError())
				liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().validarCuotasEsp(userSession, liqFormConvenioAdapterVO);
			
			// Tiene errores no recuperables
			if (liqFormConvenioAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqFormConvenioAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			saveDemodaErrors(request, liqFormConvenioAdapterVO);
			saveDemodaMessages(request, liqFormConvenioAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_CUOTAS_ESP_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}

	
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
			
			liqFormConvenioAdapterVO.clearErrorMessages();
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
					
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_PLANES_ESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaCyqAdapter.NAME);
		}
	}
	
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
			
			liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getLiqFormConvenioInit(userSession, liqFormConvenioAdapterVO);
			
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
			
			//log.debug(funcName + " Es Especial: " + liqFormConvenioAdapterVO.getEsEspecial() );
			//return mapping.findForward(CyqConstants.ACTION_FORMCONVENIOESP);
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIOESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaCyqAdapter.NAME);
		}
	}
	
	public ActionForward volverACuotas(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqFormConvenioAdapter.NAME + ": "+ liqFormConvenioAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			userSession.put(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_CUOTAS_ESP_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}

	
	public ActionForward formalizarPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, CyqSecurityConstants.LIQ_DEUDACYQ, CyqSecurityConstants.MTD_FORMALIZAR_CONVENIO);
		
		if (userSession == null)
			return forwardErrorSession(request);
		String stringServicio = "getFormalizarPlanInit";
		try {
			LiqFormConvenioAdapter liqFormConvenioAdapterVO = (LiqFormConvenioAdapter)userSession.get(LiqFormConvenioAdapter.NAME);
		
			// Recuperamos datos del form en el vo
			
			liqFormConvenioAdapterVO.setNroCuotaSelected(liqFormConvenioAdapterVO.getCantMaxCuoView());
			
			if (liqFormConvenioAdapterVO.getEsEspecial() && DateUtil.isDateAfter(liqFormConvenioAdapterVO.getFechaFormalizacion(), new Date())){
				liqFormConvenioAdapterVO.addRecoverableError(CyqError.MSG_FECHAFOR_INVALIDA);
			}
			if (!liqFormConvenioAdapterVO.getEsEspecial() && DateUtil.isDateAfter(DateUtil.getDate(liqFormConvenioAdapterVO.getFechaFormalizacionView()), new Date())){
				liqFormConvenioAdapterVO.addRecoverableError(CyqError.MSG_FECHAFOR_INVALIDA);
			}

			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_CUOTAS_ESP_ADAPTER);
			}
			
			// Llamada al servicio
			liqFormConvenioAdapterVO = CyqServiceLocator.getConcursoyQuiebraService().getFormalizarPlanInit(userSession, liqFormConvenioAdapterVO);
			
	        // Tiene errores recuperables
			if (liqFormConvenioAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqFormConvenioAdapterVO.infoString()); 
				saveDemodaErrors(request, liqFormConvenioAdapterVO);

				request.setAttribute(LiqFormConvenioAdapter.NAME, liqFormConvenioAdapterVO);
				return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_CUOTAS_ESP_ADAPTER);
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
			
			return mapping.findForward(CyqConstants.FWD_FORMCONVENIO_FORMAL_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,
					LiqFormConvenioAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		return baseVolver(mapping, form, request, response, LiqFormConvenioAdapter.NAME);
		
	}

}