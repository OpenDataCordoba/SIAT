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
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarAltaOficioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAltaOficioDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ALTAOFICIO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			AltaOficioSearchPage altaOficioSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (altaOficioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioSearchPageVO.infoString()); 
				saveDemodaErrors(request, altaOficioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (altaOficioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_ALTAOFICIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AltaOficioSearchPage.NAME);
		
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
			AltaOficioSearchPage altaOficioSearchPageVO = (AltaOficioSearchPage) userSession.get(AltaOficioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(altaOficioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				altaOficioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (altaOficioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioSearchPageVO.infoString()); 
				saveDemodaErrors(request, altaOficioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			}
				
			// Llamada al servicio	
			altaOficioSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getAltaOficioSearchPageResult(userSession, altaOficioSearchPageVO);			

			// Tiene errores recuperables
			if (altaOficioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioSearchPageVO.infoString()); 
				saveDemodaErrors(request, altaOficioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (altaOficioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_ALTAOFICIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ALTAOFICIO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ALTAOFICIO);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ALTAOFICIO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ALTAOFICIO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ALTAOFICIO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ALTAOFICIO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, AltaOficioSearchPage.NAME);
		
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		AltaOficioSearchPage searchPage = (AltaOficioSearchPage) userSession.get(AltaOficioSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (searchPage == null) {
			log.error("error en: "  + funcName + ": " + AltaOficioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioSearchPage.NAME); 
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
			AltaOficioSearchPage altaOficioSearchPageVO =  (AltaOficioSearchPage) userSession.get(AltaOficioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (altaOficioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AltaOficioSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AltaOficioSearchPage.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
						
			// Para que no muestra la lista de resultados cuando vuelve del buscar cuenta
			altaOficioSearchPageVO.setPageNumber(0L);

			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
				return mapping.findForward(PadConstants.FWD_ALTAOFICIO_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			altaOficioSearchPageVO.getCuenta().setId(new Long(selectedId));
			
			altaOficioSearchPageVO = PadServiceLocator.getPadObjetoImponibleService()
				.getAltaOficioSearchPageParamCuenta(userSession, altaOficioSearchPageVO);
			
            // Tiene errores recuperables
			if (altaOficioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + altaOficioSearchPageVO.infoString()); 
				saveDemodaErrors(request, altaOficioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (altaOficioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + altaOficioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			}
			
			
			// Envio el VO al request
			request.setAttribute(AltaOficioSearchPage.NAME, altaOficioSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_ALTAOFICIO_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AltaOficioSearchPage.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AltaOficioSearchPage.NAME);
		
	}
		
	
}
