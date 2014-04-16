//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarCuentaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCuentaDAction.class);
	public final static String CUENTA_SEARCHPAGE_FILTRO = "cuentaSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			CuentaSearchPage cuentaSearchPageFiltro = (CuentaSearchPage) userSession.getNavModel().getParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO);
			
			CuentaSearchPage cuentaSearchPageVO = PadServiceLocator.getCuentaService().getCuentaSearchPageInit(userSession, cuentaSearchPageFiltro);
			
			// Tiene errores recuperables
			if (cuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaSearchPage.NAME, cuentaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaSearchPage.NAME, cuentaSearchPageVO);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , CuentaSearchPage.NAME, cuentaSearchPageVO);
			
			// Si vino seteado el numero de cuenta llamo al buscar
			// Ver si hay que hacer lo mismo con contribuyentes si viene seteado 
			String numeroCuentaFiltro = "";
			if (cuentaSearchPageFiltro != null){
				numeroCuentaFiltro = cuentaSearchPageFiltro.getCuentaTitular().getCuenta().getNumeroCuenta();
			}
			
			if (!StringUtil.isNullOrEmpty(numeroCuentaFiltro)){
				// Seteo la cantidad de registros por pagina.
				cuentaSearchPageVO.setPageNumber(new Long(cuentaSearchPageVO.getRecsByPage()));
				cuentaSearchPageVO.getCuentaTitular().getCuenta().setNumeroCuenta(numeroCuentaFiltro);
				userSession.put(CuentaSearchPage.NAME, cuentaSearchPageVO);
				return this.buscar(mapping, form, request, response);
			}
			
			return mapping.findForward(PadConstants.FWD_CUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			CuentaSearchPage cuentaSearchPageFiltro = (CuentaSearchPage) userSession.getNavModel().getParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO);
			
			CuentaSearchPage cuentaSearchPageVO = PadServiceLocator.getCuentaService().getCuentaSearchPageInit(userSession, cuentaSearchPageFiltro);
			
			// Tiene errores recuperables
			if (cuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaSearchPage.NAME, cuentaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaSearchPage.NAME, cuentaSearchPageVO);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , CuentaSearchPage.NAME, cuentaSearchPageVO);
			
			// Si vino seteado el numero de cuenta llamo al buscar
			// Ver si hay que hacer lo mismo con contribuyentes si viene seteado 
			String numeroCuentaFiltro = "";
			if (cuentaSearchPageFiltro != null){
				numeroCuentaFiltro = cuentaSearchPageFiltro.getCuentaTitular().getCuenta().getNumeroCuenta();
			}
			
			if (!StringUtil.isNullOrEmpty(numeroCuentaFiltro)){
				// Seteo la cantidad de registros por pagina.
				cuentaSearchPageVO.setPageNumber(0L);
				cuentaSearchPageVO.getCuentaTitular().getCuenta().setNumeroCuenta(numeroCuentaFiltro);
				userSession.put(CuentaSearchPage.NAME, cuentaSearchPageVO);
			}
			
			return mapping.findForward(PadConstants.FWD_CUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
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
			CuentaSearchPage cuentaSearchPageVO = (CuentaSearchPage) userSession.get(CuentaSearchPage.NAME);
			
			// Si no se encontro el cuentaSearchPageVO, se intenta recuperar el auxiliar (esto es por si se llamo a un buscar cuenta desde el relacionarCuenta)
			if (cuentaSearchPageVO == null) {
				cuentaSearchPageVO = (CuentaSearchPage) userSession.get(CuentaSearchPage.AUX_NAME);					
			}
			// Si es nulo no se puede continuar
			if (cuentaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaSearchPage.NAME); 					
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cuentaSearchPageVO, request);
				// Setea el PageNumber del PageModel
				
				String reqAttPageNumber = (String)userSession.get("reqAttPageNumber");
				if (!StringUtil.isNullOrEmpty(reqAttPageNumber))
					cuentaSearchPageVO.setPageNumber(new Long(reqAttPageNumber));
				else
					cuentaSearchPageVO.setPageNumber(1L);
			}
			
            // Tiene errores recuperables
			if (cuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaSearchPage.NAME, cuentaSearchPageVO);
			}

			// Llamada al servicio	
			cuentaSearchPageVO = PadServiceLocator.getCuentaService().getCuentaSearchPageResult(userSession, cuentaSearchPageVO);			

			// Tiene errores recuperables
			if (cuentaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaSearchPage.NAME, cuentaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaSearchPage.NAME, cuentaSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CuentaSearchPage.NAME, cuentaSearchPageVO);
			// Nuleo el list result
			//cuentaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CuentaSearchPage.NAME, cuentaSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CUENTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTA);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ENCCUENTA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTA);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTA);
		
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTA);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CuentaSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		return baseVolver(mapping, form, request, response, CuentaSearchPage.NAME);
		
	}
	
	public ActionForward buscarTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		CuentaSearchPage cuentaSearchPageVO = (CuentaSearchPage) userSession.get(CuentaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (cuentaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + CuentaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentaSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cuentaSearchPageVO, request);
		
		return forwardSeleccionar(mapping, request, 
				PadConstants.METOD_CUENTA_PARAM_TITULAR, PadConstants.ACTION_BUSCAR_PERSONA, false);
	}
		
	public ActionForward paramTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				CuentaSearchPage cuentaSearchPageVO =  (CuentaSearchPage) userSession.get(CuentaSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + CuentaSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaSearchPage.NAME); 
				}
				
				// no visualizo la lista de resultados
				cuentaSearchPageVO.setPageNumber(0L);

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(CuentaSearchPage.NAME, cuentaSearchPageVO);
					return mapping.findForward(PadConstants.FWD_CUENTA_SEARCHPAGE); 
				}
				
				// Seteo el id contribuyente seleccionado: no se si es contribuyente o persona
				cuentaSearchPageVO.getCuentaTitular().getContribuyente().setId(new Long(selectedId));
				
				// llamo al param del servicio
				cuentaSearchPageVO = PadServiceLocator.getCuentaService().getCuentaSearchPageParamTitular
					(userSession, cuentaSearchPageVO);

	            // Tiene errores recuperables
				if (cuentaSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaSearchPageVO.infoString()); 
					saveDemodaErrors(request, cuentaSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						CuentaSearchPage.NAME, cuentaSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (cuentaSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						CuentaSearchPage.NAME, cuentaSearchPageVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, cuentaSearchPageVO);
				
				// Envio el VO al request
				request.setAttribute(CuentaSearchPage.NAME, cuentaSearchPageVO);
				userSession.put(CuentaSearchPage.NAME, cuentaSearchPageVO);
				
				userSession.getNavModel().setAct(BaseConstants.ACT_BUSCAR);
				userSession.getNavModel().setPrevAction(cuentaSearchPageVO.getPrevAction());
				userSession.getNavModel().setPrevActionParameter(cuentaSearchPageVO.getPrevActionParameter());
				
				return mapping.findForward(PadConstants.FWD_CUENTA_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
			}
	}

	public ActionForward relacionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//bajo el adapter del usserSession
			CuentaSearchPage cuentaSearchPageVO =  (CuentaSearchPage) userSession.get(CuentaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaSearchPage.NAME); 
			}
		
			// Subo el CuentaSearchPage con un nombre Auxiliar (esto se hace por si se llama a un buscar cuenta mas adelante)
			//request.setAttribute(CuentaSearchPage.AUX_NAME, cuentaSearchPageVO);
			userSession.put(CuentaSearchPage.AUX_NAME, cuentaSearchPageVO);
			
			return baseForwardSearchPage(mapping, request, funcName, PadConstants.ACTION_RELACIONAR_CUENTA, BaseConstants.ACT_RELACIONAR);	
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
		}
	}
	
	
}