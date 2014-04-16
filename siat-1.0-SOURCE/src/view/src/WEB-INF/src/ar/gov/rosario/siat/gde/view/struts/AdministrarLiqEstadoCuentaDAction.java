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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaAdapter;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarLiqEstadoCuentaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarLiqEstadoCuentaDAction.class);
	public final static String ESTADOCUENTA_SEARCHPAGE_FILTRO = "estadoCuentaSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_ESTADOCUENTA, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			EstadoCuentaSearchPage estadoEstadoCuentaSearchPageFiltro = (EstadoCuentaSearchPage) userSession.getNavModel().getParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO);

			EstadoCuentaSearchPage estadoestadoEstadoCuentaSearchPageVO = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSearchPageInit(userSession, estadoEstadoCuentaSearchPageFiltro);
			
			// Tiene errores recuperables
			if (estadoestadoEstadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoestadoEstadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoestadoEstadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCuentaSearchPage.NAME, estadoestadoEstadoCuentaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (estadoestadoEstadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoestadoEstadoCuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCuentaSearchPage.NAME, estadoestadoEstadoCuentaSearchPageVO);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , EstadoCuentaSearchPage.NAME, estadoestadoEstadoCuentaSearchPageVO);
			
			
			return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCuentaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_ESTADOCUENTA, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			EstadoCuentaSearchPage estadoCuentaSearchPageFiltro = (EstadoCuentaSearchPage) userSession.getNavModel().getParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO);
			
			EstadoCuentaSearchPage estadoCuentaSearchPageVO = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSearchPageInit(userSession, estadoCuentaSearchPageFiltro);
			
			// Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (estadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			
			// Si vino seteado el numero de cuenta llamo al buscar
			// Ver si hay que hacer lo mismo con contribuyentes si viene seteado 
			String numeroCuentaFiltro = "";
			if (estadoCuentaSearchPageFiltro != null){
				numeroCuentaFiltro = estadoCuentaSearchPageFiltro.getCuenta().getNumeroCuenta();
			}
			
			if (!StringUtil.isNullOrEmpty(numeroCuentaFiltro)){
				// Seteo la cantidad de registros por pagina.
				estadoCuentaSearchPageVO.setPageNumber(0L);
				estadoCuentaSearchPageVO.getCuenta().setNumeroCuenta(numeroCuentaFiltro);
				userSession.put(EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCuentaSearchPage.NAME);
		}
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession == null) return forwardErrorSession(request);
		
		try {
		
			// Bajo el searchPage del userSession
			EstadoCuentaSearchPage estadoCuentaSearchPageVO = (EstadoCuentaSearchPage) userSession.get(EstadoCuentaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCuentaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCuentaSearchPage.NAME); 
			}
									
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(estadoCuentaSearchPageVO, request);
				// Setea el PageNumber del PageModel
				
				String reqAttPageNumber = (String)userSession.get("reqAttPageNumber");
				if (!StringUtil.isNullOrEmpty(reqAttPageNumber))
					estadoCuentaSearchPageVO.setPageNumber(new Long(reqAttPageNumber));
				else
					estadoCuentaSearchPageVO.setPageNumber(1L);
			}
			
			// Se realizan las validaciones de Recurso y cuenta
			if (ModelUtil.isNullOrEmpty(estadoCuentaSearchPageVO.getCuenta().getRecurso())){
				estadoCuentaSearchPageVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);				
			}
			
			if (StringUtil.isNullOrEmpty(estadoCuentaSearchPageVO.getCuenta().getNumeroCuenta())){
				estadoCuentaSearchPageVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADO_CUENTA_NROCUENTA_LABEL);
			}
			
			// Valida el rango de fechas ingresado
			Date fechaDesde= estadoCuentaSearchPageVO.getFechaVtoDesde();
			Date fechaHasta = estadoCuentaSearchPageVO.getFechaVtoHasta();
			if(fechaDesde!=null && fechaHasta!=null && DateUtil.isDateAfter(fechaDesde, fechaHasta)){
				estadoCuentaSearchPageVO.addRecoverableError(BaseError.MSG_VALORMAYORQUE, 
											GdeError.ESTADO_CUENTA_FECHA_DESDE, GdeError.ESTADO_CUENTA_FECHA_HASTA);			
			}

            // Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			long idRecurso = estadoCuentaSearchPageVO.getCuenta().getRecurso().getId().longValue();
			
			// Llamada al servicio	
			EstadoCuentaAdapter liqEstadoCuentaAdapter = GdeServiceLocator.getGestionDeudaService().getLiqEstadoCuentaAdapter(userSession, estadoCuentaSearchPageVO);			
			
			// Se pasan los posibles errores al searchPage
			liqEstadoCuentaAdapter.passErrorMessages(estadoCuentaSearchPageVO);
			
			// Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			// Subo el searchPage al userSession
			estadoCuentaSearchPageVO.getCuenta().getRecurso().setId(new Long(idRecurso));
			userSession.put(EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			
			// Envio el VO al request
			request.setAttribute(EstadoCuentaAdapter.NAME, liqEstadoCuentaAdapter);
			// Subo en el adapter al userSession
			userSession.put(EstadoCuentaAdapter.NAME, liqEstadoCuentaAdapter);
			
			return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCuentaSearchPage.NAME);
		}
	}

	public ActionForward verDetalleDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			         
		return baseForward(mapping, request, funcName, "buscar", GdeConstants.ACTION_VER_DETALLE_DEUDA, ""); 
	}
	
	public ActionForward verDeudaContrib(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DEUDA_CONTRIB, BaseConstants.ACT_BUSCAR);
		if (userSession == null) return forwardErrorSession(request);
		
			CommonKey contribuyenteKey = null;
				
			// Recuperamos datos del form en el vo		
			log.debug(funcName + " idTitular: " + request.getParameter("id"));
			if (request.getParameter("id") != null ){ 			
				contribuyenteKey = new CommonKey(request.getParameter("id"));
			}
			
			userSession.getNavModel().putParameter(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY, contribuyenteKey);
			userSession.getNavModel().setAct(BaseConstants.ACT_BUSCAR);
		
		request.setAttribute("vieneDe", "estadoCuenta");
		return mapping.findForward(GdeConstants.ACT_VER_DEUDA_CONTRIB);
	       
	}
	
	public ActionForward verConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		
		request.setAttribute("vieneDe", "estadoCuenta");
		return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_CUENTA);
			         
	}

	/**
	 * Recarga el combo de Clasificacion de Deuda segun el valor del recurso seleccionado.
	 * 
	 * @return
	 * @throws Exception
	 */
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			//bajo el adapter del usserSession
			EstadoCuentaSearchPage estadoCuentaSearchPageVO =  (EstadoCuentaSearchPage) userSession.get(EstadoCuentaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCuentaSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCuentaSearchPage.NAME); 
			}
						
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(estadoCuentaSearchPageVO, request);

			estadoCuentaSearchPageVO = GdeServiceLocator.getGestionDeudaService()
				.getEstadoCuentaSearchPageParamClasificacion(userSession, estadoCuentaSearchPageVO);
			
            // Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}			
			
			// Envio el VO al request
			request.setAttribute(EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCuentaSearchPage.NAME);
		}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		EstadoCuentaSearchPage searchPage = (EstadoCuentaSearchPage) userSession.get(EstadoCuentaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (searchPage == null) {
			log.error("error en: "  + funcName + ": " + EstadoCuentaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCuentaSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(searchPage, request);
		
        // Tiene errores recuperables
		if (searchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + searchPage.infoString()); 
			saveDemodaErrors(request, searchPage);

			request.setAttribute(EstadoCuentaSearchPage.NAME, searchPage);
			return mapping.getInputForward();
		}
		
		// Subo el searchPage al userSession
		userSession.put(EstadoCuentaSearchPage.NAME, searchPage);


		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(searchPage.getCuenta().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(searchPage.getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, 
				"paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA, false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			EstadoCuentaSearchPage estadoCuentaSearchPageVO =  (EstadoCuentaSearchPage) userSession.get(EstadoCuentaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EstadoCuentaSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCuentaSearchPage.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			//String selectedId = navModel.getSelectedId();
			String selectedId = request.getParameter("selectedId");
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			estadoCuentaSearchPageVO.getCuenta().setId(new Long(selectedId));
			
			estadoCuentaSearchPageVO = GdeServiceLocator.getGestionDeudaService()
				.getEstadoCuentaSearchPageParamCuenta(userSession, estadoCuentaSearchPageVO);
			
            // Tiene errores recuperables
			if (estadoCuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + estadoCuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, estadoCuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (estadoCuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + estadoCuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			}			
			
			// Envio el VO al request
			request.setAttribute(EstadoCuentaSearchPage.NAME, estadoCuentaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EstadoCuentaSearchPage.NAME);
		}
	}
	
	public ActionForward irImprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_ESTADOCUENTA, BaseConstants.ACT_VER);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// Bajo el searchPage del userSession
			EstadoCuentaSearchPage estadoCuentasearchPage = (EstadoCuentaSearchPage) userSession.get(EstadoCuentaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (estadoCuentasearchPage == null) {
				log.error("error en: "  + funcName + ": " + EstadoCuentaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EstadoCuentaSearchPage.NAME); 
			}

			DemodaUtil.populateVO(estadoCuentasearchPage, request);
			
			
			// Envio el VO al request
			request.setAttribute(EstadoCuentaSearchPage.NAME, estadoCuentasearchPage);

			userSession.put(EstadoCuentaSearchPage.NAME, estadoCuentasearchPage);
			
			return mapping.findForward(GdeConstants.FWD_ESTADOCUENTA_IMPRIMIR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward imprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_ESTADOCUENTA, BaseConstants.ACT_VER);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//Se obtiene el adapter de la session
			EstadoCuentaAdapter estadoCuentaAdapter = (EstadoCuentaAdapter) userSession.get(EstadoCuentaAdapter.NAME);

			log.debug("adapter:"+estadoCuentaAdapter);
			log.debug("cant pagoDeuda:"+estadoCuentaAdapter.getListDeudaPagoDeuda().size());
			// Llamada al servicio
			PrintModel print = GdeServiceLocator.getGestionDeudaService().getImprimirEstadoCuenta(userSession, estadoCuentaAdapter);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, EstadoCuentaSearchPage.NAME);
		
	}
	
	public ActionForward volverFromDetalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, EstadoCuentaSearchPage.NAME);
			
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, EstadoCuentaAdapter.NAME);
			
		}

	public ActionForward verHistoricoExe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.CONSULTAR_ESTADOCUENTA, BaseConstants.ACT_VER);
		if (userSession == null) return forwardErrorSession(request);

		userSession.getNavModel().putParameter("vieneDe", GdeConstants.ESTADOCUENTA);
		
		// Va al metodo "verHistoricoExencion" de la liquidacion de la deuda
		return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, "verHistoricoExencion", "verHistoricoExe");
	}
}