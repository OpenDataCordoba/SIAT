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
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarDeuJudSinConstanciaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDeuJudSinConstanciaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DEUJUDSINCONSTANCIA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPageVO = GdeServiceLocator.
					getGdeAdmDeuJudServiceHbmImpl().getDeuJudSinConstanciaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuJudSinConstanciaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuJudSinConstanciaSearchPage.NAME);
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
		DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPageVO = (DeuJudSinConstanciaSearchPage) userSession.get(DeuJudSinConstanciaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (deuJudSinConstanciaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + DeuJudSinConstanciaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DeuJudSinConstanciaSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(deuJudSinConstanciaSearchPageVO, request);
		
        // Tiene errores recuperables
		if (deuJudSinConstanciaSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.infoString()); 
			saveDemodaErrors(request, deuJudSinConstanciaSearchPageVO);

			request.setAttribute(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			return mapping.getInputForward();
		}
		

		//Guardamos el SearchPage en el UserSession
		userSession.put(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(deuJudSinConstanciaSearchPageVO.getDeuda().getCuenta().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(deuJudSinConstanciaSearchPageVO.getDeuda().getCuenta().getNumeroCuenta());

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
			DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPageVO =  (DeuJudSinConstanciaSearchPage) userSession.get(DeuJudSinConstanciaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (deuJudSinConstanciaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeuJudSinConstanciaSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeuJudSinConstanciaSearchPage.NAME); 
			}
			
			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			//nuleo el list result
			deuJudSinConstanciaSearchPageVO.setPageNumber(0L);
			
			// si el id esta vacio, pq selecciono volver, forwardeo al searchPage
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			deuJudSinConstanciaSearchPageVO.getDeuda().getCuenta().setId(new Long(selectedId));
			
			deuJudSinConstanciaSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl()
					.getDeuJudSinConstanciaSearchPageParamCuenta(userSession, deuJudSinConstanciaSearchPageVO);
				
            // Tiene errores recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuJudSinConstanciaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuJudSinConstanciaSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DeuJudSinConstanciaSearchPage.NAME);
		
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
			DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPageVO = (DeuJudSinConstanciaSearchPage) userSession.get(DeuJudSinConstanciaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (deuJudSinConstanciaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeuJudSinConstanciaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeuJudSinConstanciaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deuJudSinConstanciaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				deuJudSinConstanciaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuJudSinConstanciaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			}
				
			// Llamada al servicio	
			deuJudSinConstanciaSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getDeuJudSinConstanciaSearchPageResult(userSession, deuJudSinConstanciaSearchPageVO);			

	
			// Tiene errores recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deuJudSinConstanciaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (deuJudSinConstanciaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			}
		
			saveDemodaMessages(request, deuJudSinConstanciaSearchPageVO);
			
			// Envio el VO al request
			request.setAttribute(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuJudSinConstanciaSearchPage.NAME);
		}
	}

	public ActionForward verDetalleDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_VER_DETALLE_DEUDA);
		
	}
	
	public ActionForward incluirDeudaAConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// Bajo el searchPage del userSession
		DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPageVO = (DeuJudSinConstanciaSearchPage) userSession.get(DeuJudSinConstanciaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (deuJudSinConstanciaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + DeuJudSinConstanciaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DeuJudSinConstanciaSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(deuJudSinConstanciaSearchPageVO, request);
		
		// Verifica si se selecciono alguna deuda
		if(request.getParameter("listIdSelected")==null){
			deuJudSinConstanciaSearchPageVO.addRecoverableError(GdeError.DEUJUDSINCONSTANCIA_NINGUNA_DEUDA_SELECCIONADA);
		}
		
        // Tiene errores recuperables
		if (deuJudSinConstanciaSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaSearchPageVO.infoString()); 
			// Envio el VO al request
			request.setAttribute(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			// Subo el SearchPage al userSession
			userSession.put(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
			saveDemodaErrors(request, deuJudSinConstanciaSearchPageVO);				
			return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_SEARCHPAGE);
		}
		
		//Se envia el SearchPage
		navModel.putParameter(DeuJudSinConstanciaSearchPage.NAME, deuJudSinConstanciaSearchPageVO);
		
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DEUJUDSINCONSTANCIA);
		
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DeuJudSinConstanciaSearchPage.NAME);
		
	}
		
}
