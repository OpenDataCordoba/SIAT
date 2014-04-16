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

public final class BuscarDeudaExcProMasConsPorCtaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDeudaExcProMasConsPorCtaDAction.class);

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
					BuscarDeudaExcProMasConsPorCtaDAction.PROCESO_MASIVO_KEY);
			
			DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPage = GdeServiceLocator.
				getGestionDeudaJudicialService().getDeudaProMasConsPorCtaSearchPageInit(userSession, procesoMasivoKey);

			// Tiene errores recuperables
			if (deudaExcProMasConsPorCtaSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPage.infoString()); 
				saveDemodaErrors(request, deudaExcProMasConsPorCtaSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPage);
			} 

			// Tiene errores no recuperables
			if (deudaExcProMasConsPorCtaSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPage);
			}

			baseInicializarSearchPage(mapping, request, userSession , 
					DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPage);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaProMasConsPorCtaSearchPage.NAME);
		}
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPageVO = (DeudaProMasConsPorCtaSearchPage) userSession.get(DeudaProMasConsPorCtaSearchPage.NAME);

		// Si es nulo no se puede continuar
		if (deudaExcProMasConsPorCtaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + DeudaProMasConsPorCtaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(deudaExcProMasConsPorCtaSearchPageVO, request);

		// Tiene errores recuperables
		if (deudaExcProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPageVO.infoString()); 
			saveDemodaErrors(request, deudaExcProMasConsPorCtaSearchPageVO);
			return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
		}

		// carga de parametros a utilizar en la busqueda
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso y numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().getRecurso().setId(
				deudaExcProMasConsPorCtaSearchPageVO.getProcesoMasivo().getRecurso().getId()); 
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(
				deudaExcProMasConsPorCtaSearchPageVO.getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		return forwardSeleccionar(mapping, request, "paramCuenta",  PadConstants.ACTION_BUSCAR_CUENTA, false);
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
			DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPageVO = (DeudaProMasConsPorCtaSearchPage) userSession.get(DeudaProMasConsPorCtaSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasConsPorCtaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaProMasConsPorCtaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME); 
			}
			
			// no visualizo la lista de resultados
			deudaExcProMasConsPorCtaSearchPageVO.setPageNumber(0L);

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();

			// si el id esta vacio, pq selecciono volver, forwardeo 
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
				return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE); 
			}
			deudaExcProMasConsPorCtaSearchPageVO.setPageNumber(1L);
			// Seteo el id contribuyente seleccionado
			deudaExcProMasConsPorCtaSearchPageVO.getCuenta().setId(new Long(selectedId));

			// llamo al param del servicio
			deudaExcProMasConsPorCtaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaProMasConsPorCtaSearchPageParamCuenta(userSession, deudaExcProMasConsPorCtaSearchPageVO);

			// Tiene errores recuperables
			if (deudaExcProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasConsPorCtaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasConsPorCtaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, deudaExcProMasConsPorCtaSearchPageVO);

			// Envio el VO al request
			request.setAttribute(DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE);

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
			DeudaProMasConsPorCtaSearchPage deudaExcProMasConsPorCtaSearchPageVO = (DeudaProMasConsPorCtaSearchPage) userSession.get(DeudaProMasConsPorCtaSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasConsPorCtaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaProMasConsPorCtaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME); 
			}

			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deudaExcProMasConsPorCtaSearchPageVO, request);

				// Setea el PageNumber del PageModel				
				deudaExcProMasConsPorCtaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}

			// Tiene errores recuperables
			if (deudaExcProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasConsPorCtaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
			}

			// Llamada al servicio	
			deudaExcProMasConsPorCtaSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasConsPorCtaSearchPageResult(userSession, deudaExcProMasConsPorCtaSearchPageVO);			

			// Tiene errores recuperables
			if (deudaExcProMasConsPorCtaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasConsPorCtaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasConsPorCtaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasConsPorCtaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);
			}

			// Envio el VO al request
			request.setAttribute(DeudaProMasConsPorCtaSearchPage.NAME, deudaExcProMasConsPorCtaSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE);

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
