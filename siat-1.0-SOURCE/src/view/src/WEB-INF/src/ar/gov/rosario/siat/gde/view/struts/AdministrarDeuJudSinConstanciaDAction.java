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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.buss.bean.EstConDeu;
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaAdapter;
import ar.gov.rosario.siat.gde.iface.model.DeuJudSinConstanciaSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstConDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDeuJudSinConstanciaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDeuJudSinConstanciaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DEUJUDSINCONSTANCIA, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDeuJudSinConstanciaAdapterForView(userSession, deuda, listIdSelected)";

				DeuJudSinConstanciaSearchPage deuJudSinConstanciaSearchPageVO = (DeuJudSinConstanciaSearchPage) navModel.getParameter(DeuJudSinConstanciaSearchPage.NAME);
				
				deuJudSinConstanciaAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().
					getDeuJudSinConstanciaAdapterForView(userSession, deuJudSinConstanciaSearchPageVO.getDeuda(), deuJudSinConstanciaSearchPageVO.getListIdSelected());
				actionForward = mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (deuJudSinConstanciaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + deuJudSinConstanciaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			deuJudSinConstanciaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DeuJudSinConstanciaAdapter.NAME + ": "+ deuJudSinConstanciaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			 
			saveDemodaMessages(request, deuJudSinConstanciaAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuJudSinConstanciaAdapter.NAME);
		}
	}

	public ActionForward buscarConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		
		DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapterVO = (DeuJudSinConstanciaAdapter) userSession.get(DeuJudSinConstanciaAdapter.NAME);
		
		ConstanciaDeuSearchPage constanciaDeuSearchPage = new ConstanciaDeuSearchPage(); 
		
		constanciaDeuSearchPage.getConstanciaDeu().setProcurador(deuJudSinConstanciaAdapterVO.getDeuda().getProcurador());
		constanciaDeuSearchPage.getConstanciaDeu().setCuenta(deuJudSinConstanciaAdapterVO.getDeuda().getCuenta());
		//constanciaDeuSearchPage.getConstanciaDeu().setEstConDeu((EstConDeuVO) EstConDeu.getById(EstConDeu.ID_CREADA).toVO());
		constanciaDeuSearchPage.setHabilitarCuentaEnabled(false);
		constanciaDeuSearchPage.setHabilitarProcuradorEnabled(false);
		constanciaDeuSearchPage.setHabilitarRecursoEnabled(false);
		//constanciaDeuSearchPage.setHabilitarEstadoEnabled(false);
		constanciaDeuSearchPage.setBuscarCreadasManualmente(true);
		
		//no permitimos seleccionar constancias en estado ANULADA
		constanciaDeuSearchPage.getListEstConDeuVOAExluirEnSeleccion()
			.add((EstConDeuVO) EstConDeu.getById(EstConDeu.ID_ANULADA).toVO());
		
		//no permitimos seleccionar constancias en estado CANCELADA
		constanciaDeuSearchPage.getListEstConDeuVOAExluirEnSeleccion()
			.add((EstConDeuVO) EstConDeu.getById(EstConDeu.ID_CANCELADA).toVO());

		
		userSession.getNavModel().putParameter(BuscarConstanciaDeuDAction.CONSTANCIA_SEARCHPAGE_FILTRO
				,constanciaDeuSearchPage);
		
		// Forwardeo a la Search Page de la Constancia
		return forwardSeleccionar(mapping, request, 
				"paramConstancia", GdeConstants.ACTION_BUSCAR_CONSTANCIADEU, false);
	}
	
	public ActionForward paramConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
	
		// Bajo el searchPage del userSession
		DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapterVO = (DeuJudSinConstanciaAdapter) userSession.get(DeuJudSinConstanciaAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (deuJudSinConstanciaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + DeuJudSinConstanciaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, DeuJudSinConstanciaAdapter.NAME); 
		}
		
		// recupero el id seleccionado por el usuario
		String selectedId = navModel.getSelectedId();
		if (log.isDebugEnabled()) log.debug(funcName + "Selected Id" + selectedId);
		
		// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
		if (StringUtil.isNullOrEmpty(selectedId)) {
			// Envio el VO al request
			request.setAttribute(DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_ADAPTER); 
		}
	
		// Seteo el id de la cuenta
		deuJudSinConstanciaAdapterVO.getConstanciaDeu().setId(new Long(selectedId));

		deuJudSinConstanciaAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl()
			.getDeuJudSinConstanciaAdapterParamConstancia(userSession, deuJudSinConstanciaAdapterVO);
		
        // Tiene errores recuperables
		if (deuJudSinConstanciaAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaAdapterVO.infoString()); 
			saveDemodaErrors(request, deuJudSinConstanciaAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, 
					DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
		}
		
		// Tiene errores no recuperables
		if (deuJudSinConstanciaAdapterVO.hasErrorNonRecoverable()) {
			log.error("error en: "  + funcName + ": " + deuJudSinConstanciaAdapterVO.errorString()); 
			return forwardErrorNonRecoverable(mapping, request, funcName, 
					DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
		}

		// Envio el VO al request
		request.setAttribute(DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
		
		return mapping.findForward(GdeConstants.FWD_DEUJUDSINCONSTANCIA_ADAPTER);
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DEUJUDSINCONSTANCIA, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DeuJudSinConstanciaAdapter deuJudSinConstanciaAdapterVO = (DeuJudSinConstanciaAdapter) userSession.get(DeuJudSinConstanciaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (deuJudSinConstanciaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DeuJudSinConstanciaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeuJudSinConstanciaAdapter.NAME); 
			}

            // Tiene errores recuperables
			if (deuJudSinConstanciaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaAdapterVO.infoString()); 
				saveDemodaErrors(request, deuJudSinConstanciaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			}
			
			// llamada al servicio
			 deuJudSinConstanciaAdapterVO = GdeServiceLocator.
					getGdeAdmDeuJudServiceHbmImpl().createConstanciaDeuDet(userSession, deuJudSinConstanciaAdapterVO);
			
            // Tiene errores recuperables
			if (deuJudSinConstanciaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deuJudSinConstanciaAdapterVO.infoString()); 
				saveDemodaErrors(request, deuJudSinConstanciaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (deuJudSinConstanciaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deuJudSinConstanciaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeuJudSinConstanciaAdapter.NAME, deuJudSinConstanciaAdapterVO);
			}
			
			// Fue Exitoso
			
			//Volvemos al inicio
			
			return forwardConfirmarOk(mapping, request, funcName, DeuJudSinConstanciaAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeuJudSinConstanciaAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DeuJudSinConstanciaAdapter.NAME);
		
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, DeuJudSinConstanciaAdapter.NAME);
	}

}
