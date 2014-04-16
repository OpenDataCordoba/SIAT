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

import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaTimer;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDeudaReclamadaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeudaReclamadaDAction.class);
	
	/**
	 * Metodo iniciliziza para el grupo de usuarios perntenecientes a Gestion de Recurso.
	 * 
	 * @throws Exception
	 */
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ADM_DEUDA_RECLAMADA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		LiqDeudaAdapter liqDeudaAdapterVO = null;
		String stringServicio = "getLiqDeudaAdapterGRInit";
		try {
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterGRInit(userSession);
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_DEDUDA_RECLAMADA_INGRESO_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Fordward a la busqueda de cuenta desde el ingreso GR
	 * 
	 * @throws Exception
	 */
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ADM_DEUDA_RECLAMADA, funcName);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();  
		
		// Recuperamos datos del form en el vo		
		log.debug(funcName + " numeroCuenta: " + request.getParameter("numeroCuenta"));
		if (request.getParameter("numeroCuenta") != null ){ 			
			liqDeudaAdapterVO.setNumeroCuenta(request.getParameter("numeroCuenta"));
		}
		
		log.debug(funcName + " idRecurso: " + request.getParameter("idRecurso"));
		if ( request.getParameter("idRecurso") != null &&			
				request.getParameter("idRecurso") != "-1"){
			liqDeudaAdapterVO.setIdRecurso(new Long(request.getParameter("idRecurso")));
		}
		
        // Tiene errores recuperables
		if (liqDeudaAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
			saveDemodaErrors(request, liqDeudaAdapterVO);

			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			return mapping.getInputForward();
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().getRecurso().setId(liqDeudaAdapterVO.getIdRecurso()); 
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(liqDeudaAdapterVO.getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
	}
	
	/**
	 * Para para el retorno de la busqueda de cuenta, setea el numero de cuenta y el recurso a liqDeuda.
	 * 
	 * @throws Exception
	 */
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			LiqDeudaAdapter liqDeudaAdapterVO = new LiqDeudaAdapter();
			
			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			
			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterGRInit(userSession);
			
			// si el id esta vacio, pq selecciono volver, forwardeo al SearchPage
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request				
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				
				return mapping.findForward(BalConstants.FWD_DEDUDA_RECLAMADA_INGRESO_ADAPTER);				
			}
			
			liqDeudaAdapterVO.setIdCuenta(new Long(selectedId));			
			
			// llamada al servicio
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getLiqDeudaAdapterParamCuenta(userSession, liqDeudaAdapterVO);
			
            // Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_DEDUDA_RECLAMADA_INGRESO_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 * Ingreso para el usuario Gestion de Recurso
	 * Busca la cuenta por recurso y numero de cuenta.
	 * 
	 * @throws Exception
	 */
	public ActionForward ingresar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ADM_DEUDA_RECLAMADA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "getLiqDeudaAdapterByRecursoNroCuenta()";
		try {
			
			// Bajo el charango de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			liqDeudaAdapterVO.clearError();
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " numeroCuenta: " + request.getParameter("numeroCuenta"));
			if (request.getParameter("numeroCuenta") != null ){ 			
				liqDeudaAdapterVO.setNumeroCuenta(request.getParameter("numeroCuenta"));
			}
			
			log.debug(funcName + " idRecurso: " + request.getParameter("idRecurso"));
			if ( request.getParameter("idRecurso") != null &&			
					request.getParameter("idRecurso") != "-1"){
				liqDeudaAdapterVO.setIdRecurso(new Long(request.getParameter("idRecurso")));
			}
			
	        // Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);

				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.getInputForward();
			}
			
			DemodaTimer dt = new DemodaTimer();
			
			log.info("DemodaTimer -> LiqDeuda INIT: ");
			
			liqDeudaAdapterVO = GdeServiceLocator.getGestionDeudaService().getByRecursoNroCuenta4DeudaReclamada(userSession, 
																											liqDeudaAdapterVO);
			log.info(dt.stop("LiqDeuda TOTAL"));
			
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + LiqDeudaAdapter.NAME + ": "+ liqDeudaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			saveDemodaMessages(request, liqDeudaAdapterVO);
			
			return mapping.findForward(BalConstants.FWD_DEDUDA_RECLAMADA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	/**
	 *  Marga el registro de deuda seleccionado como reclamado.
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward marcar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ADM_DEUDA_RECLAMADA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "marcarReclamada()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			// Bajo el cachorro de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			liqDeudaAdapterVO.setSelectedId(request.getParameter("selectedId"));
			
			
			liqDeudaAdapterVO = BalServiceLocator.getReclamoService().marcarReclamada(userSession, 
					liqDeudaAdapterVO);
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(BalConstants.FWD_DEDUDA_RECLAMADA_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			userSession.getNavModel().setConfAction(BalConstants.PATH_DEDUDA_RECLAMADA_ADAPTER);
			userSession.getNavModel().setConfActionParameter("ingresar");
			
			// Envio el VO al request
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			// Fue Exitoso
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
	/**
	 * Quita la marca de reclamada a un registro de deuda. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward desmarcar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ADM_DEUDA_RECLAMADA, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		String stringServicio = "marcarReclamada()";
		try {
			
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idDeuda: " + request.getParameter("selectedId"));
			
			// Bajo el cachorro de la session
			LiqDeudaAdapter liqDeudaAdapterVO = (LiqDeudaAdapter) userSession.get(LiqDeudaAdapter.NAME);
			
			liqDeudaAdapterVO.setSelectedId(request.getParameter("selectedId"));
			
			
			liqDeudaAdapterVO = BalServiceLocator.getReclamoService().marcarNOReclamada(userSession, 
					liqDeudaAdapterVO);
			
			// Tiene errores recuperables
			if (liqDeudaAdapterVO.hasErrorRecoverable()) {
				
				log.error("recoverable error en: "  + funcName + ": " + liqDeudaAdapterVO.infoString()); 
				saveDemodaErrors(request, liqDeudaAdapterVO);
				request.setAttribute(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
				return mapping.findForward(BalConstants.FWD_DEDUDA_RECLAMADA_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (liqDeudaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + liqDeudaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			}
			
			userSession.getNavModel().setConfAction(BalConstants.PATH_DEDUDA_RECLAMADA_ADAPTER);
			userSession.getNavModel().setConfActionParameter("ingresar");
			
			// Envio el VO al request
			userSession.put(LiqDeudaAdapter.NAME, liqDeudaAdapterVO);
			
			// Fue Exitoso
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
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
		
		return new ActionForward (BaseConstants.FWD_SIAT_BUILD_MENU);
		
	}
	
	
	/**
	 * Volver desde el apdapter al ingreso para GR. 
	 * 
	 * @author Cristian
	 * @return
	 * @throws Exception
	 */
	public ActionForward volverAIngreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		return new ActionForward (BalConstants.FWD_INICIALIZAR_DEDUA_RECLAMADA);
		
	}

}