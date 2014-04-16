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
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarConstanciaDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarConstanciaDeuDAction.class);
	
	public final static String CONSTANCIA_SEARCHPAGE_FILTRO = "CONSTANCIA_SEARCHPAGE_FILTRO";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ConstanciaDeuSearchPage constanciaDeuSearchPageFiltro = (ConstanciaDeuSearchPage) userSession.getNavModel().getParameter(CONSTANCIA_SEARCHPAGE_FILTRO);
			ConstanciaDeuSearchPage constanciaDeuSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuSearchPageInit(userSession, constanciaDeuSearchPageFiltro);
			
			// Tiene errores recuperables
			if (constanciaDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (constanciaDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ConstanciaDeuSearchPage.NAME);
		
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
			ConstanciaDeuSearchPage constanciaDeuSearchPageVO = (ConstanciaDeuSearchPage) userSession.get(ConstanciaDeuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(constanciaDeuSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				constanciaDeuSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (constanciaDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
				
			// Llamada al servicio	
			constanciaDeuSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuSearchPageResult(userSession, constanciaDeuSearchPageVO);			

			// Tiene errores recuperables
			if (constanciaDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_CONSTANCIADUE);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE);
	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE);

	}

	public ActionForward habilitar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE, GdeConstants.ACT_CONSTANCIADEU_HABILITAR);

	}

	public ActionForward recomponerConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE, GdeConstants.ACT_CONSTANCIADEU_RECOMPONER);

	}
	
	public ActionForward impresionConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE, GdeConstants.ACT_CONSTANCIADEU_IMPRIMIR);

	}
	
	public ActionForward anularConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_CONSTANCIADUE, GdeConstants.ACT_CONSTANCIADEU_ANULAR);

	}
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		ConstanciaDeuSearchPage searchPage = (ConstanciaDeuSearchPage) userSession.get(ConstanciaDeuSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (searchPage == null) {
			log.error("error en: "  + funcName + ": " + CueExcSelSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(searchPage, request);
		
        // Tiene errores recuperables
		if (searchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + searchPage.infoString()); 
			saveDemodaErrors(request, searchPage);

			request.setAttribute(CueExcSelSearchPage.NAME, searchPage);
			return mapping.getInputForward();
		}
		

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(searchPage.getConstanciaDeu().getCuenta().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(searchPage.getConstanciaDeu().getCuenta().getNumeroCuenta());

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
			ConstanciaDeuSearchPage constanciaDeuSearchPageVO =  (ConstanciaDeuSearchPage) userSession.get(ConstanciaDeuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelSearchPage.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
						
			// Para que no muestra la lista de resultados cuando vuelve del buscar cuenta
			constanciaDeuSearchPageVO.setPageNumber(0L);

			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			constanciaDeuSearchPageVO.getConstanciaDeu().getCuenta().setId(new Long(selectedId));
			
			constanciaDeuSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl()
				.getConstanciaDeuSearchPageParamCuenta(userSession, constanciaDeuSearchPageVO);
			
            // Tiene errores recuperables
			if (constanciaDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CueExcSelSearchPage.NAME, constanciaDeuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
			
			
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuSearchPage.NAME);
		}
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ConstanciaDeuSearchPage constanciaDeuSearchPageVO = (ConstanciaDeuSearchPage) userSession.get(ConstanciaDeuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (constanciaDeuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ConstanciaDeuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConstanciaDeuSearchPage.NAME); 
			}
							
            // Tiene errores recuperables
			if (constanciaDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
				
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(constanciaDeuSearchPageVO, request);
			
			// Llamada al servicio	
			constanciaDeuSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuSearchPageParamProcuradorByRecurso(userSession, constanciaDeuSearchPageVO);			
			
			//Esto es para que no muestre la tabla de resultados
			constanciaDeuSearchPageVO.setPageNumber(0L);
			
			// Tiene errores recuperables
			if (constanciaDeuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + constanciaDeuSearchPageVO.infoString()); 
				saveDemodaErrors(request, constanciaDeuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (constanciaDeuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + constanciaDeuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ConstanciaDeuSearchPage.NAME, constanciaDeuSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CONSTANCIADUE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConstanciaDeuSearchPage.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConstanciaDeuSearchPage.NAME);
		
	}
		
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ConstanciaDeuSearchPage.NAME);
	}	
}
