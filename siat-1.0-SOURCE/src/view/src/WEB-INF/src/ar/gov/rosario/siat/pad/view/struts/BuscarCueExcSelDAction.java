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
import ar.gov.rosario.siat.pad.iface.model.CueExcSelSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarCueExcSelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCueExcSelDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSEL, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CueExcSelSearchPage cueExcSelSearchPageVO = PadServiceLocator.getCuentaService().getCueExcSelSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cueExcSelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExcSelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cueExcSelSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CUEEXCSEL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelSearchPage.NAME);
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
		CueExcSelSearchPage cueExcSelSearchPageVO = (CueExcSelSearchPage) userSession.get(CueExcSelSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (cueExcSelSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + CueExcSelSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cueExcSelSearchPageVO, request);
		
        // Tiene errores recuperables
		if (cueExcSelSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cueExcSelSearchPageVO.infoString()); 
			saveDemodaErrors(request, cueExcSelSearchPageVO);

			request.setAttribute(CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			return mapping.getInputForward();
		}
		

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(cueExcSelSearchPageVO.getCueExcSel().getCuenta().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(cueExcSelSearchPageVO.getCueExcSel().getCuenta().getNumeroCuenta());

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
			CueExcSelSearchPage cueExcSelSearchPageVO =  (CueExcSelSearchPage) userSession.get(CueExcSelSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelSearchPage.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			cueExcSelSearchPageVO = PadServiceLocator.getCuentaService().getCueExcSelSearchPageInit(userSession);
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
				return mapping.findForward(PadConstants.FWD_CUEEXCSEL_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			cueExcSelSearchPageVO.getCueExcSel().getCuenta().setId(new Long(selectedId));
			
			cueExcSelSearchPageVO = PadServiceLocator.getCuentaService()
				.getCueExcSelSearchPageParamCuenta(userSession, cueExcSelSearchPageVO);
			
            // Tiene errores recuperables
			if (cueExcSelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExcSelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CUEEXCSEL_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelSearchPage.NAME);
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
			CueExcSelSearchPage cueExcSelSearchPageVO = (CueExcSelSearchPage) userSession.get(CueExcSelSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cueExcSelSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				cueExcSelSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (cueExcSelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExcSelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			}
				
			// Llamada al servicio	
			cueExcSelSearchPageVO = PadServiceLocator.getCuentaService().getCueExcSelSearchPageResult(userSession, cueExcSelSearchPageVO);			

			// Tiene errores recuperables
			if (cueExcSelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExcSelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(CueExcSelSearchPage.NAME, cueExcSelSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CUEEXCSEL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

	String funcName = DemodaUtil.currentMethodName();
	return this.baseRefill(mapping, form, request, response, funcName, CueExcSelSearchPage.NAME);
	
}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSEL);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_ENC_CUEEXCSEL);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSEL);

	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSEL);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUEEXCSEL);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CueExcSelSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExcSelSearchPage.NAME);
		
	}

}
