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

import ar.gov.rosario.siat.bal.iface.model.CompensacionSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class BuscarCompensacionDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCompensacionDAction.class);
	public final static String COMPENSACION_SEARCHPAGE_FILTRO = "compensacionSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			CompensacionSearchPage compensacionSearchPageFiltro = (CompensacionSearchPage) userSession.getNavModel().getParameter(BuscarCompensacionDAction.COMPENSACION_SEARCHPAGE_FILTRO);
		
			CompensacionSearchPage compensacionSearchPageVO = BalServiceLocator.getCompensacionService().getCompensacionSearchPageInit(userSession, compensacionSearchPageFiltro);
			
			// Tiene errores recuperables
			if (compensacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, compensacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (compensacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CompensacionSearchPage.NAME, compensacionSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_COMPENSACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, CompensacionSearchPage.NAME);

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
			CompensacionSearchPage compensacionSearchPageVO = (CompensacionSearchPage) userSession.get(CompensacionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(compensacionSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				compensacionSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (compensacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, compensacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}

			// Llamada al servicio	
			compensacionSearchPageVO = BalServiceLocator.getCompensacionService().getCompensacionSearchPageResult(userSession, compensacionSearchPageVO);			

			// Tiene errores recuperables
			if (compensacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, compensacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (compensacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CompensacionSearchPage.NAME, compensacionSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(CompensacionSearchPage.NAME, compensacionSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_COMPENSACION_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMPENSACION);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENCCOMPENSACION);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMPENSACION);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMPENSACION);
			
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, CompensacionSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, CompensacionSearchPage.NAME);
			
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/bal/BuscarCompensacion");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		CompensacionSearchPage  compensacionSearchPage = (CompensacionSearchPage) userSession.get(CompensacionSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (compensacionSearchPage == null) {
			log.error("error en: "  + funcName + ": " + CompensacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(compensacionSearchPage, request);
		
        // Tiene errores recuperables
		if (compensacionSearchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPage.infoString()); 
			saveDemodaErrors(request, compensacionSearchPage);
			return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPage);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(compensacionSearchPage.getRecurso());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(compensacionSearchPage.getCompensacion().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CompensacionSearchPage compensacionSearchPageVO = (CompensacionSearchPage) userSession.get(CompensacionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionSearchPage.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			
			// Si el id esta vacio, pq selecciono volver, forwardeo al SearchPage
			if (StringUtil.isNullOrEmpty(navModel.getSelectedId())) {
				// Envio el VO al request				
				request.setAttribute(CompensacionSearchPage.NAME, compensacionSearchPageVO);
				
				return mapping.findForward(BalConstants.FWD_COMPENSACION_SEARCHPAGE);				
			}
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			compensacionSearchPageVO.getCompensacion().getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			compensacionSearchPageVO = BalServiceLocator.getCompensacionService().getCompensacionSearchPageParamCuenta(userSession, compensacionSearchPageVO);
			
            // Tiene errores recuperables
			if (compensacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, compensacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (compensacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CompensacionSearchPage.NAME, compensacionSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(CompensacionSearchPage.NAME, compensacionSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_COMPENSACION_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionSearchPage.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CompensacionSearchPage.NAME);
	}

	public ActionForward enviar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMPENSACION, BaseConstants.ACT_ENVIAR);	
	}

	public ActionForward devolver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_COMPENSACION, BaseConstants.ACT_DEVOLVER);	
	}
	
	public ActionForward incluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.INCLUIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			CompensacionSearchPage compensacionSearchPageVO = (CompensacionSearchPage) userSession.get(CompensacionSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (compensacionSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CompensacionSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CompensacionSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(compensacionSearchPageVO, request);
			
            // Tiene errores recuperables
			if (compensacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, compensacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// llamada al servicio
			compensacionSearchPageVO = BalServiceLocator.getBalanceService().incluirCompensacion(userSession, compensacionSearchPageVO);
			
            // Tiene errores recuperables
			if (compensacionSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + compensacionSearchPageVO.infoString()); 
				saveDemodaErrors(request, compensacionSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (compensacionSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + compensacionSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CompensacionSearchPage.NAME, compensacionSearchPageVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CompensacionSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CompensacionSearchPage.NAME);
		}
	}
}
