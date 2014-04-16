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
import ar.gov.rosario.siat.exe.iface.model.CueExeSearchPage;
import ar.gov.rosario.siat.gde.iface.model.GesJudSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarGesJudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarGesJudDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			GesJudSearchPage gesJudSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (gesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, gesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudSearchPage.NAME, gesJudSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (gesJudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudSearchPage.NAME, gesJudSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , GesJudSearchPage.NAME, gesJudSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_GESJUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, GesJudSearchPage.NAME);
		
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
			GesJudSearchPage gesJudSearchPageVO = (GesJudSearchPage) userSession.get(GesJudSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(gesJudSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				gesJudSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (gesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, gesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudSearchPage.NAME, gesJudSearchPageVO);
			}
				
			// Llamada al servicio	
			gesJudSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudSearchPageResult(userSession, gesJudSearchPageVO);			

			// Tiene errores recuperables
			if (gesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, gesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudSearchPage.NAME, gesJudSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudSearchPage.NAME, gesJudSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(GesJudSearchPage.NAME, gesJudSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(GesJudSearchPage.NAME, gesJudSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_GESJUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_GESJUD);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_GESJUD);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_GESJUD);

	}

	public ActionForward registrarCaducidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, GdeConstants.ACTION_ADMINISTRAR_GESJUD, GdeConstants.ACTION_GESJUD_REG_CADUCIDAD);

	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_GESJUD);

	}
		
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, GesJudSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, GesJudSearchPage.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeSearchPage.NAME);
		
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
		navModel.setSelectAction("/gde/BuscarGesJud");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		// seteo el recurso TGI como filtro
		GesJudSearchPage  gesJudSearchPage = (GesJudSearchPage) userSession.get(GesJudSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (gesJudSearchPage == null) {
			log.error("error en: "  + funcName + ": " + GesJudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, GesJudSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(gesJudSearchPage, request);
		
        // Tiene errores recuperables
		if (gesJudSearchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + gesJudSearchPage.infoString()); 
			saveDemodaErrors(request, gesJudSearchPage);
			return forwardErrorRecoverable(mapping, request, userSession, GesJudSearchPage.NAME, gesJudSearchPage);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(gesJudSearchPage.getCuenta().getNumeroCuenta());
		
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
			GesJudSearchPage gesJudSearchPageVO = (GesJudSearchPage) userSession.get(GesJudSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudSearchPage.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			gesJudSearchPageVO.getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			gesJudSearchPageVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudSearchPageParamCuenta(userSession, gesJudSearchPageVO);
			
            // Tiene errores recuperables
			if (gesJudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudSearchPageVO.infoString()); 
				saveDemodaErrors(request, gesJudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudSearchPage.NAME, gesJudSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudSearchPage.NAME, gesJudSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(GesJudSearchPage.NAME, gesJudSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(GesJudSearchPage.NAME, gesJudSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_GESJUD_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudSearchPage.NAME);
		}
	}
}
