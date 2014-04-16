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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.emi.view.struts.AdministrarEncEmisionPuntualDAction;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class BuscarDeudaContribDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarDeudaContribDAction.class);
	public static String CONTRIBUYENTE_KEY = "contribuyenteKey";
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter"+"  act="+act);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DEUDA_CONTRIB, act);
		if (userSession==null) return forwardErrorSession(request);
		
		log.debug("Cont_KEY: "+ userSession.getNavModel().getParameter(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY));
				
		try {
			
			CommonKey contribuyenteKey = (CommonKey) userSession.getNavModel().getParameter(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY);
			
			DeudaContribSearchPage deudaContribSearchPage = GdeServiceLocator.getGestionDeudaService().getDeudaContribSearchPageInit(userSession, contribuyenteKey);
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, deudaContribSearchPage);
			
			// Tiene errores recuperables
			if (deudaContribSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaContribSearchPage.infoString()); 
				saveDemodaErrors(request, deudaContribSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaContribSearchPage.NAME, deudaContribSearchPage);
			} 

			// Tiene errores no recuperables
			if (deudaContribSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaContribSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaContribSearchPage.NAME, deudaContribSearchPage);
			}
			
			// Se este action fue llamado desde otro inicializo con contribuyente seteado.
			if (contribuyenteKey != null){
				deudaContribSearchPage.setPageNumber(1L);
				
				//seteo los valores del navegacion en el pageModel
				deudaContribSearchPage.setValuesFromNavModel(userSession.getNavModel());
				
				if (userSession.getNavModel().getAct().equals(BaseConstants.ACT_SELECCIONAR))
					deudaContribSearchPage.setModoSeleccionar(true);
				
				// Envio el VO al request
				request.setAttribute(DeudaContribSearchPage.NAME, deudaContribSearchPage);
				
				// Subo en el el searchPage al userSession
				userSession.put(DeudaContribSearchPage.NAME, deudaContribSearchPage);
			
			} else {
				// Si no tiene error
				baseInicializarSearchPage(mapping, request, userSession , DeudaContribSearchPage.NAME, deudaContribSearchPage);
				deudaContribSearchPage.setPageNumber(1L);
			}
			
			//obtengo de donde se obtuvo la pagina
			String vieneDe = (String) request.getAttribute("vieneDe");
			
			//posibles path de origen
			String pathEstCuenta=GdeConstants.PATH_INGRESO_ESTADOCUENTA_ID;
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA;
			String pathBuscarPersona = PadConstants.PATH_BUSCAR_PERSONA;
			
			//Verifico si ya tengo seteada la vuelta
			String vuelta="";
			if (userSession.get("deudaContribVuelveA")!=null){
				vuelta=userSession.get("deudaContribVuelveA").toString();
			}
			log.debug("vieneDe: "+vieneDe);
			//seteo la vuelta segun el vieneDe
			if (vuelta.equals("") || "liqDeuda".equals(vieneDe)){
				if ("liqDeuda".equals(vieneDe)&& (vuelta.indexOf(pathVerCuenta)>(-1)||vuelta.equals(""))){
					userSession.put("deudaContribVuelveA", pathVerCuenta+request.getParameter("cuentaId"));
					deudaContribSearchPage.setMuestraBuscarContrib(false);
				}else if ("menu".equals(vieneDe)){
					userSession.put("deudaContribVuelveA",pathBuscarPersona);
				}else if ("estadoCuenta".equals(vieneDe)){
					userSession.put("deudaContribVuelveA", pathEstCuenta + request.getParameter("cuentaId"));
				}else if (vuelta == null) {
					log.error("No se especifico el atributo en el request 'vieneDe'.para usar esta funcion hay que modificar el codigo del action");
					return forwardErrorNonRecoverable (mapping, request, funcName, DeudaContribSearchPage.NAME, deudaContribSearchPage);
				}else if ("ordenControlSearchPage".equals(vieneDe)){
					userSession.put("deudaContribVuelveA", EfConstants.PATH_ORDENCONTROL_SEARCHPAGE);
				}else{
					deudaContribSearchPage.setMuestraBuscarContrib(false);
				}
			}else{
				deudaContribSearchPage.setMuestraBuscarContrib(false);
			}
				
			if (ModelUtil.isNullOrEmpty(deudaContribSearchPage.getContribuyente())){
				return forwardSeleccionar(mapping, request,	GdeConstants.ACT_PARAM_CONTRIBUYENTE, PadConstants.ACTION_BUSCAR_PERSONA, true);
			}
			return mapping.findForward(GdeConstants.FWD_DEUDACONTRIB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DeudaContribSearchPage.NAME);
		
	}
	
	public ActionForward buscarContribuyente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		DeudaContribSearchPage deudaContribSearchPageVO = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (deudaContribSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + DeudaContribSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DeudaContribSearchPage.NAME); 
		}
		
		return forwardSeleccionar(mapping, request, 
				GdeConstants.ACT_PARAM_CONTRIBUYENTE, PadConstants.ACTION_BUSCAR_PERSONA, true);
	}
		
	public ActionForward paramContribuyente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			DeudaContribSearchPage deudaContribSearchPageVO = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
							
			// Si es nulo no se puede continuar
			if (deudaContribSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaContribSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaContribSearchPage.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = null; 
			
			// Para paginado
			if (!StringUtil.isNullOrEmpty(request.getParameter("idContrib"))) {
				selectedId = request.getParameter("idContrib");
				
				log.debug(funcName + " selectedId 1: " +  selectedId);
				
			} else if (!StringUtil.isNullOrEmpty(deudaContribSearchPageVO.getSelectedId())){
				selectedId = deudaContribSearchPageVO.getSelectedId();
				deudaContribSearchPageVO.setSelectedId(null);
				log.debug(funcName + " selectedId 2: " +  selectedId);
				
			// Desde la busqueda de contribuyentes
			} else {
				selectedId = userSession.getNavModel().getSelectedId();
				log.debug(funcName + " selectedId 3: " +  selectedId);
			}
			
			if (StringUtil.isNullOrEmpty(selectedId)) {
				return volver(mapping, form, request, response);
			}
			
			// Seteo el id contribuyente seleccionado
			deudaContribSearchPageVO.getContribuyente().setId(new Long(selectedId));
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				if (StringUtil.isNumeric((String)userSession.get("reqAttPageNumber"))){
					deudaContribSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				} else {
					deudaContribSearchPageVO.setPageNumber(1L);
				}
			} else {
				deudaContribSearchPageVO.setPageNumber(1L);
			}
			
            // Tiene errores recuperables
			if (deudaContribSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaContribSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaContribSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaContribSearchPage.NAME, deudaContribSearchPageVO);
			}
			
			// llamo al param del servicio
			deudaContribSearchPageVO = GdeServiceLocator.getGestionDeudaService().getDeudaContribSearchPageParamContribuyente(userSession, deudaContribSearchPageVO);
			
            // Tiene errores recuperables
			if (deudaContribSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaContribSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaContribSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DeudaContribSearchPage.NAME, deudaContribSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (deudaContribSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaContribSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DeudaContribSearchPage.NAME, deudaContribSearchPageVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, deudaContribSearchPageVO);
			
			// Envio el VO al request
			request.setAttribute(DeudaContribSearchPage.NAME, deudaContribSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDACONTRIB_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}

	
	public ActionForward paramServicioBanco(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el adapter del userSession
			DeudaContribSearchPage deudaContribSearchPageVO = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
							
			// Si es nulo no se puede continuar
			if (deudaContribSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaContribSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaContribSearchPage.NAME); 
			}

			DemodaUtil.populateVO(deudaContribSearchPageVO, request);
			
            // Tiene errores recuperables
			if (deudaContribSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaContribSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaContribSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaContribSearchPage.NAME, deudaContribSearchPageVO);
			}
			
			// llamo al param del servicio
			deudaContribSearchPageVO = GdeServiceLocator.getGestionDeudaService().getDeudaContribSearchPageParamServicioBanco(userSession, deudaContribSearchPageVO);
			
            // Tiene errores recuperables
			if (deudaContribSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaContribSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaContribSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DeudaContribSearchPage.NAME, deudaContribSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (deudaContribSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaContribSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DeudaContribSearchPage.NAME, deudaContribSearchPageVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, deudaContribSearchPageVO);
			saveDemodaErrors(request, deudaContribSearchPageVO);
			
			// Envio el VO al request
			request.setAttribute(DeudaContribSearchPage.NAME, deudaContribSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDACONTRIB_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}
	
	
	public ActionForward liquidacionDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Seteo de selectedId para ser utilizado al regreso dentro del paramContribuyente.
			DeudaContribSearchPage deudaContrib = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
			deudaContrib.setSelectedId(deudaContrib.getContribuyente().getId().toString());
			
			NavModel navModelVolver = new NavModel();
			
			navModelVolver.setPrevAction( GdeConstants.PATH_BUSCAR_DEUDACONTRIB);
			navModelVolver.setPrevActionParameter(GdeConstants.ACT_PARAM_CONTRIBUYENTE);
			
			navModel.putParameter(BaseConstants.ACT_VOLVER, navModelVolver);
			
			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String[] arrId = selectedId.split("-");
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + arrId[0] + "&idCuentaTitular=" + arrId[1] + "&validAuto=true"; 
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			String deuConVuelve = "";
			if (userSession.get("deudaContribVuelveA")!=null){
				deuConVuelve= userSession.get("deudaContribVuelveA").toString();
			}
			
			if (BaseConstants.FWD_SIAT_BUILD_MENU.equals(userSession.get("deudaContribVuelveA"))||
					deuConVuelve.indexOf(GdeConstants.PATH_INGRESO_ESTADOCUENTA_ID)>(-1) || deuConVuelve.equals(EfConstants.PATH_ORDENCONTROL_SEARCHPAGE)){
				request.setAttribute("liqDeudaVieneDe", "deudaContrib");
				log.debug("liqDeudaVieneDe: deudaContrib");
			}
			
			return  new ActionForward (pathVerCuenta);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}
	
	public ActionForward estadoCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Obtengo el id de la cuenta seleccionada
			String selectedId = request.getParameter("selectedId");
			
			DeudaContribSearchPage deudaContribSearchPage = (DeudaContribSearchPage)userSession.get(DeudaContribSearchPage.NAME);
			
			//Llamada al servicio que devuelve el searchPage para el estado de cuenta, con los datos de la cuenta seleccionada
			EstadoCuentaSearchPage estadoCuentaSearchPage = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSeachPageFiltro(userSession, new Long(selectedId));
			
			// Subo el searchPage del estado cuenta al userSession
			userSession.getNavModel().putParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO, estadoCuentaSearchPage);
			userSession.getNavModel().putParameter(DeudaContribSearchPage.NAME, deudaContribSearchPage);
			// fowardeo al action de estadoCuenta
			return baseForwardAdapter(mapping, request, funcName, GdeConstants.FWD_DEUDACONTRIB_ESTADOCUENTA, BaseConstants.ACT_BUSCAR);
		
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		String volverA;
		if(null == userSession.get("deudaContribVuelveA")){
			volverA = BaseConstants.FWD_SIAT_BUILD_MENU;
		}else{
			volverA = userSession.get("deudaContribVuelveA").toString();
			userSession.remove("deudaContribVuelveA");
		}
		
		return new ActionForward(volverA);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		DeudaContribSearchPage deudaCont= (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
		if (deudaCont != null){
			userSession.getNavModel().putParameter(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY, new CommonKey (deudaCont.getContribuyente().getId()));
		}
		
		return baseRefill(mapping, form, request, response, funcName, DeudaContribSearchPage.NAME);
		
	}	
	
	public ActionForward imprimirListDeudaContrib(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())
			log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping);
			
		if (userSession == null) return forwardErrorSession(request);

		try {
			// Bajo el searchPage del userSession
			DeudaContribSearchPage deudaContribSearchPage = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaContribSearchPage == null) {
				log.error("error en: "  + funcName + ": " + DeudaContribSearchPage.NAME + 
				" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaContribSearchPage.NAME); 
			}
			
			// llamada al servicio
			PrintModel print  = GdeServiceLocator.getGestionDeudaService().imprimirListDeudaContrib(userSession, deudaContribSearchPage);
				
			baseResponsePrintModel(response, print);
				
			return null;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception,	PersonaSearchPage.NAME);
		}
	}

	
	public ActionForward agregarCuenta(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		return baseForward(mapping, request, funcName, "paramContribuyente", PadConstants.ACTION_ADMINISTRAR_ENCCUENTA, BaseConstants.ACT_AGREGAR);
		
	}


	public ActionForward irAEmision(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
				PadSecurityConstants.ABM_CUENTA_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			DeudaContribSearchPage deudaContribSearchPageVO = (DeudaContribSearchPage) userSession.get(DeudaContribSearchPage.NAME);
			
			userSession.getNavModel().setPrevAction(GdeConstants.PATH_BUSCAR_DEUDACONTRIB);
			userSession.getNavModel().setPrevActionParameter(GdeConstants.ACT_PARAM_CONTRIBUYENTE + "&idContrib=" +  
					deudaContribSearchPageVO.getContribuyente().getPersona().getIdView());
			
			userSession.getNavModel().setSelectedId(commonKey.getId().toString());
			userSession.getNavModel().setAct(BaseConstants.ACT_AGREGAR);
			
			CuentaAdapter cuentaAdapter = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);
			
			userSession.getNavModel().putParameter(AdministrarEncEmisionPuntualDAction.CUENTA_EMITIR, cuentaAdapter.getCuenta());
			
			String actFwd = "/emi/AdministrarEncEmisionPuntual.do?method=inicializar";

			return new ActionForward(actFwd); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.ENC_NAME);
		}
	}
}
