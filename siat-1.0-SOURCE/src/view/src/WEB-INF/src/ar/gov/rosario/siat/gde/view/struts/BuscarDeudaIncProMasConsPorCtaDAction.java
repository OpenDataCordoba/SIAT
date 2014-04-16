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
import ar.gov.rosario.siat.gde.iface.model.DeudaProMasConsPorCtaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarDeudaIncProMasConsPorCtaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDeudaIncProMasConsPorCtaDAction.class);

public static String PROCESO_MASIVO_KEY = "procesoMasivoKey";

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			
			// obtengo el id del envio judicial
			CommonKey procesoMasivoKey = (CommonKey) userSession.getNavModel().getParameter(
					BuscarDeudaIncProMasConsPorCtaDAction.PROCESO_MASIVO_KEY);
			
			DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPage = 
				GdeServiceLocator.getGestionDeudaJudicialService().getDeudaProMasConsPorCtaSearchPageInit(userSession, procesoMasivoKey);

			// Tiene errores recuperables
			if (deudaIncProMasConsPorCtaSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPage.infoString()); 
				saveDemodaErrors(request, deudaIncProMasConsPorCtaSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPage);
			} 

			// Tiene errores no recuperables
			if (deudaIncProMasConsPorCtaSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPage);
			}

			baseInicializarSearchPage(mapping, request, userSession , DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPage);

			return mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasConsPorCtaSearchPage.NAME);
		}
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPageVO = (DeudaProMasConsPorCtaSearchPage) userSession.get(DeudaProMasConsPorCtaSearchPage.NAME);

		// Si es nulo no se puede continuar
		if (deudaIncProMasConsPorCtaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + DeudaProMasConsPorCtaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(deudaIncProMasConsPorCtaSearchPageVO, request);

		// Tiene errores recuperables
		if (deudaIncProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPageVO.infoString()); 
			saveDemodaErrors(request, deudaIncProMasConsPorCtaSearchPageVO);
			return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
		}

		// carga de parametros a utilizar en la busqueda
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().getRecurso().setId(
				deudaIncProMasConsPorCtaSearchPageVO.getProcesoMasivo().getRecurso().getId()); 
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(
				deudaIncProMasConsPorCtaSearchPageVO.getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA , false);
		
	}

	public ActionForward paramCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();

		try {

			// Bajo el SearchPage del userSession
			DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPageVO = (DeudaProMasConsPorCtaSearchPage) userSession.get(DeudaProMasConsPorCtaSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaIncProMasConsPorCtaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaProMasConsPorCtaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME); 
			}
			
			// no visualizo la lista de resultados
			deudaIncProMasConsPorCtaSearchPageVO.setPageNumber(0L);

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();

			// si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE); 
			}
			deudaIncProMasConsPorCtaSearchPageVO.setPageNumber(1L);
			// Seteo el id de la cuenta seleccionada
			deudaIncProMasConsPorCtaSearchPageVO.getCuenta().setId(new Long(selectedId));

			// llamo al param del servicio
			deudaIncProMasConsPorCtaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaProMasConsPorCtaSearchPageParamCuenta(userSession, deudaIncProMasConsPorCtaSearchPageVO);

			// Tiene errores recuperables
			if (deudaIncProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaIncProMasConsPorCtaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaIncProMasConsPorCtaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, deudaIncProMasConsPorCtaSearchPageVO);

			// Envio el VO al request
			request.setAttribute(DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasConsPorCtaSearchPage.NAME);
		}
	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Bajo el searchPage del userSession
			DeudaProMasConsPorCtaSearchPage deudaIncProMasConsPorCtaSearchPageVO = (DeudaProMasConsPorCtaSearchPage) userSession.get(DeudaProMasConsPorCtaSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaIncProMasConsPorCtaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaProMasConsPorCtaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME); 
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deudaIncProMasConsPorCtaSearchPageVO, request);

				// Setea el PageNumber del PageModel				
				deudaIncProMasConsPorCtaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}

			// Tiene errores recuperables
			if (deudaIncProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaIncProMasConsPorCtaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
			}

			// Llamada al servicio	
			deudaIncProMasConsPorCtaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaIncProMasConsPorCtaSearchPageResult(userSession, deudaIncProMasConsPorCtaSearchPageVO);			

			// Tiene errores recuperables
			if (deudaIncProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaIncProMasConsPorCtaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaIncProMasConsPorCtaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaIncProMasConsPorCtaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);
			}

			// Envio el VO al request
			request.setAttribute(DeudaProMasConsPorCtaSearchPage.NAME, deudaIncProMasConsPorCtaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasConsPorCtaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return this.baseRefill(mapping, form, request, response, funcName, DeudaProMasConsPorCtaSearchPage.NAME);
	}

	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		return baseVolver(mapping, form, request, response, DeudaProMasConsPorCtaSearchPage.NAME);
	}

}
