//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarEmisionPuntualDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEmisionPuntualDAction.class);
	
	private static final String MTD_PARAM_CUENTA = "paramCuenta";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		try {
			EmisionPuntualSearchPage emisionMasSearchPageVO = EmiServiceLocator
					.getEmisionService().getEmisionPuntualSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (emisionMasSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionMasSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (emisionMasSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionMasSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, EmisionPuntualSearchPage.NAME);
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
			EmisionPuntualSearchPage emisionMasSearchPageVO = (EmisionPuntualSearchPage) userSession.get(EmisionPuntualSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionMasSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(emisionMasSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				emisionMasSearchPageVO.setPageNumber(new Long((String) userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (emisionMasSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionMasSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			}
				
			// Llamada al servicio	
			emisionMasSearchPageVO = EmiServiceLocator.getEmisionService()
					.getEmisionPuntualSearchPageResult(userSession, emisionMasSearchPageVO);			

			// Tiene errores recuperables
			if (emisionMasSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionMasSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionMasSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (emisionMasSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionMasSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);

			// Subo en el el searchPage al userSession
			userSession.put(EmisionPuntualSearchPage.NAME, emisionMasSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_EMISIONPUNTUAL);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_ENC_EMISIONPUNTUAL);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionPuntualSearchPage.NAME);
		
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		EmisionPuntualSearchPage emisionPuntualSearchPageVO = (EmisionPuntualSearchPage) 
						userSession.get(EmisionPuntualSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (emisionPuntualSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + EmisionPuntualSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(emisionPuntualSearchPageVO, request);
		
        // Tiene errores recuperables
		if (emisionPuntualSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionPuntualSearchPageVO.infoString()); 
			saveDemodaErrors(request, emisionPuntualSearchPageVO);

			request.setAttribute(EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);
			return mapping.getInputForward();
		}
		
		// Subo el searchPage al userSession
		userSession.put(EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(emisionPuntualSearchPageVO.getEmision().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setNumeroCuenta(emisionPuntualSearchPageVO.getEmision().getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, MTD_PARAM_CUENTA, PadConstants.ACTION_BUSCAR_CUENTA, false);
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName 
				+ " idSelected:"+request.getParameter("selectedId"));
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		try {
			//bajo el adapter del usserSession
			EmisionPuntualSearchPage emisionPuntualSearchPageVO =  (EmisionPuntualSearchPage) 
					userSession.get(EmisionPuntualSearchPage.NAME);
			
			// Para que no se muestre el resultSet
			emisionPuntualSearchPageVO.setPageNumber(0L);

			// Si es nulo no se puede continuar
			if (emisionPuntualSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionPuntualSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionPuntualSearchPage.NAME); 
			}
			
			// recupero el id seleccionado por el usuario
			String selectedId = request.getParameter("selectedId");
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);
				return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_SEARCHPAGE); 
			}
			
			// Seteo el id de la cuenta
			emisionPuntualSearchPageVO.getEmision().getCuenta().setId(new Long(selectedId));
			
			emisionPuntualSearchPageVO = EmiServiceLocator.getEmisionService()
				.getEmisionPuntualSearchPageParamCuenta(userSession, emisionPuntualSearchPageVO);
			
			// Tiene errores recuperables
			if (emisionPuntualSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionPuntualSearchPageVO.infoString()); 
				saveDemodaErrors(request, emisionPuntualSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (emisionPuntualSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionPuntualSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);
			}			
						
			// Envio el VO al request
			request.setAttribute(EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualSearchPage.NAME, emisionPuntualSearchPageVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionPuntualSearchPage.NAME);
		}
	}

}
