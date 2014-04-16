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
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarCuentasProcuradorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCuentasProcuradorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CuentasProcuradorSearchPage cuentasProcuradorSearchPage = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getCuentasProcuradorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cuentasProcuradorSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentasProcuradorSearchPage.infoString()); 
				saveDemodaErrors(request, cuentasProcuradorSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			} 

			// Tiene errores no recuperables
			if (cuentasProcuradorSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentasProcuradorSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			}
			
			// Envio el VO al request
			request.setAttribute(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			// Subo el apdater al userSession
			userSession.put(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			 
			saveDemodaMessages(request, cuentasProcuradorSearchPage);
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);

			return mapping.findForward(GdeConstants.FWD_CUENTAS_PROCURADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CuentasProcuradorSearchPage.NAME);
		
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
			CuentasProcuradorSearchPage cuentasProcuradorSearchPageVO = (CuentasProcuradorSearchPage) userSession.get(CuentasProcuradorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentasProcuradorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CuentasProcuradorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentasProcuradorSearchPage.NAME); 
			}
			
			DemodaUtil.populateVO(cuentasProcuradorSearchPageVO, request);
			
			// Llamada al servicio	
			cuentasProcuradorSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getCuentasProcuradorSearchPageResult(userSession, cuentasProcuradorSearchPageVO);			

			// Tiene errores recuperables
			if (cuentasProcuradorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentasProcuradorSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentasProcuradorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cuentasProcuradorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentasProcuradorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_CUENTAS_PROCURADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
		}
	}

	public ActionForward paramProcurador(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				CuentasProcuradorSearchPage cuentasProcuradorSearchPageVO = (CuentasProcuradorSearchPage) userSession.get(CuentasProcuradorSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (cuentasProcuradorSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + CuentasProcuradorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentasProcuradorSearchPage.NAME); 
				}
				
				DemodaUtil.populateVO(cuentasProcuradorSearchPageVO, request);
				
				// Llamada al servicio	
				cuentasProcuradorSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().
								getCuentasProcuradorParamProcurador(userSession, cuentasProcuradorSearchPageVO);			

				// Tiene errores recuperables
				if (cuentasProcuradorSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentasProcuradorSearchPageVO.infoString()); 
					saveDemodaErrors(request, cuentasProcuradorSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (cuentasProcuradorSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentasProcuradorSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
				}
			
				// Seteo para que no muestre la lista de resultados
				cuentasProcuradorSearchPageVO.setPageNumber(0L);
				
				// Envio el VO al request
				request.setAttribute(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_CUENTAS_PROCURADOR_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
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
		CuentasProcuradorSearchPage searchPage = (CuentasProcuradorSearchPage) userSession.get(CuentasProcuradorSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (searchPage == null) {
			log.error("error en: "  + funcName + ": " + CuentasProcuradorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentasProcuradorSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(searchPage, request);
		
        // Tiene errores recuperables
		if (searchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + searchPage.infoString()); 
			saveDemodaErrors(request, searchPage);

			request.setAttribute(CuentasProcuradorSearchPage.NAME, searchPage);
			return mapping.getInputForward();
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		//cuentaFiltro.getCuentaTitular().getCuenta()
			//.setRecurso(new RecursoVO(searchPage.getIdRecurso().intValue(), "")); 

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
			CuentasProcuradorSearchPage cuentasProcuradorSearchPage =  (CuentasProcuradorSearchPage) userSession.get(CuentasProcuradorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentasProcuradorSearchPage == null) {
				log.error("error en: "  + funcName + ": " + CuentasProcuradorSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentasProcuradorSearchPage.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
						
			// Para que no muestra la lista de resultados cuando vuelve del buscar cuenta
			cuentasProcuradorSearchPage.setPageNumber(0L);

			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
				return mapping.findForward(GdeConstants.FWD_CUENTAS_PROCURADOR_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			cuentasProcuradorSearchPage.getCuenta().setId(new Long(selectedId));
			
			cuentasProcuradorSearchPage = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl()
				.getCuentasProcuradorParamCuenta(userSession, cuentasProcuradorSearchPage);
			
            // Tiene errores recuperables
			if (cuentasProcuradorSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentasProcuradorSearchPage.infoString()); 
				saveDemodaErrors(request, cuentasProcuradorSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, 
						CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			}
			
			// Tiene errores no recuperables
			if (cuentasProcuradorSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentasProcuradorSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			}
			
			
			// Envio el VO al request
			request.setAttribute(CuentasProcuradorSearchPage.NAME, cuentasProcuradorSearchPage);
			
			return mapping.findForward(GdeConstants.FWD_CUENTAS_PROCURADOR_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
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
			
			// Seteo de selectedId para ser utilizado al regreso dentro del paramObjImp
			//CuentaObjImpSearchPage cuentaObjImpSearchPageVO = (CuentaObjImpSearchPage) userSession.get(CuentaObjImpSearchPage.NAME);			CuentasProcuradorSearchPage cuentasProcuradorSearchPage = (CuentasProcuradorSearchPage) userSession.get(CuentasProcuradorSearchPage.NAME);

			//cuentaObjImpSearchPageVO.setSelectedId(cuentaObjImpSearchPageVO.getObjImp().getId().toString());
			
			NavModel navModelVolver = new NavModel();
			
			navModelVolver.setPrevAction(GdeConstants.PATH_BUSCAR_CUENTAS_PROCURADOR);
			navModelVolver.setPrevActionParameter(BaseConstants.ACT_BUSCAR);

			navModel.putParameter(BaseConstants.ACT_VOLVER, navModelVolver);
			
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId;
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			request.setAttribute("liqDeudaVieneDe", "cuentaProcurador");
			
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
			
			//Llamada al servicio que devuelve el searchPage para el estado de cuenta, con los datos de la cuenta seleccionada
			EstadoCuentaSearchPage estadoCuentaSearchPage = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSeachPageFiltro(userSession, new Long(selectedId));
			
			// Subo el searchPage del estado cuenta al userSession
			userSession.getNavModel().putParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO, estadoCuentaSearchPage);
			
			// fowardeo al action de estadoCuenta
			return baseForwardSearchPage(mapping, request, funcName, GdeConstants.FWD_CUENTAS_PROCURADOR_EST_CUENTA, BaseConstants.ACT_BUSCAR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentasProcuradorSearchPage.NAME);
		
	}	

}
