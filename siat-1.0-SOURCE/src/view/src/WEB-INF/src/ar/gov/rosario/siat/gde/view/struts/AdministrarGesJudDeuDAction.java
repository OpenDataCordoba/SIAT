//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DeudaJudicialVO;
import ar.gov.rosario.siat.gde.iface.model.GesJudAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudDeuVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarGesJudDeuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarGesJudDeuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDDEU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		GesJudDeuAdapter gesJudDeuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getGesJudDeuAdapterForView(userSession, commonKey)";
				gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDDEU_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getGesJudDeuAdapterForUpdate(userSession, commonKey)";
				gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getGesJudDeuAdapterForView(userSession, commonKey)";
				gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterForView(userSession, commonKey);				
				gesJudDeuAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.GESJUDDEU_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDDEU_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getGesJudDeuAdapterForCreate(userSession)";				
				gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterForCreate(userSession, commonKey);
				gesJudDeuAdapterVO.setVerResultados(false);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);				
			}
			if (act.equals(GdeConstants.ACT_GESJUDDEU_AGREGAR_FROM_CONSTANCIA)) {
				stringServicio = "getGesJudDeuAdapterForCreate(userSession)";
				GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
				Long idConstanciaDeu = (Long) navModel.getParameter("idConstanciaDeu");
				Long idGesJud = gesJudAdapterVO.getGesJud().getId();
				gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterForCreateFromConstancia(userSession, idGesJud, idConstanciaDeu);
				gesJudDeuAdapterVO.setVerResultados(true);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (gesJudDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + gesJudDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			gesJudDeuAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + GesJudDeuAdapter.NAME + ": "+ gesJudDeuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			 
			saveDemodaMessages(request, gesJudDeuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudDeuAdapter.NAME);
		}
	}

	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudDeuAdapter gesJudDeuAdapterVO = (GesJudDeuAdapter) userSession.get(GesJudDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudDeuAdapterVO, request);			
			
			// Verifica si se selecciono alguna deuda
			if(gesJudDeuAdapterVO.getCuenta()==null || gesJudDeuAdapterVO.getCuenta().getId()==null || gesJudDeuAdapterVO.getCuenta().getId()<=0){
				gesJudDeuAdapterVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,	PadError.CUENTA_LABEL);
			}

            // Tiene errores recuperables
			if (gesJudDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString()); 
				// Envio el VO al request
				request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
				saveDemodaErrors(request, gesJudDeuAdapterVO);				
				return mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);
			}

			// llamada al servicio
			gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterResult(userSession, gesJudDeuAdapterVO);
			
            // Tiene errores recuperables
			if (gesJudDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Seteo para que muestre los resultados
			gesJudDeuAdapterVO.setVerResultados(true);

			// Envio el VO al request
			request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			 
			saveDemodaMessages(request, gesJudDeuAdapterVO);
			
			return mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudDeuAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudDeuAdapter gesJudDeuAdapterVO = (GesJudDeuAdapter) userSession.get(GesJudDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudDeuAdapterVO, request);
			
			// Verifica si se selecciono alguna deuda
			if(request.getParameter("idsDeudaSelected")==null){
				gesJudDeuAdapterVO.addRecoverableError(GdeError.GESJUDDEU_AGEGAR_NINGUNA_DEUDA_SELECTED);
			}

			
            // Tiene errores recuperables
			if (gesJudDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString()); 
				// Envio el VO al request
				request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
				// Subo el apdater al userSession
				userSession.put(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
				saveDemodaErrors(request, gesJudDeuAdapterVO);				
				return mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);
			}

			
			// llamada al servicio
			gesJudDeuAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().createGesJudDeu(userSession, gesJudDeuAdapterVO);
			
            // Tiene errores recuperables
			if (gesJudDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudDeuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudDeuAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudDeuAdapter gesJudDeuAdapterVO = (GesJudDeuAdapter) userSession.get(GesJudDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudDeuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudDeuAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudDeuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudDeuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// llamada al servicio
			GesJudDeuVO gesJudDeuVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updateGesJudDeu(userSession, gesJudDeuAdapterVO.getGesJudDeu());
			
            // Tiene errores recuperables
			if (gesJudDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudDeuVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudDeuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudDeuAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_GESJUDDEU, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudDeuAdapter gesJudDeuAdapterVO = (GesJudDeuAdapter) userSession.get(GesJudDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudDeuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudDeuAdapter.NAME); 
			}

			// llamada al servicio
			GesJudDeuVO gesJudDeuVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().deleteGesJudDeu
				(userSession, gesJudDeuAdapterVO.getGesJudDeu());
			
            // Tiene errores recuperables
			if (gesJudDeuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapterVO.infoString());
				saveDemodaErrors(request, gesJudDeuVO);				
				request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
				return mapping.findForward(GdeConstants.FWD_GESJUDDEU_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (gesJudDeuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudDeuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudDeuAdapter.NAME, gesJudDeuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudDeuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudDeuAdapter.NAME);
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
		GesJudDeuAdapter gesJudDeuAdapter = (GesJudDeuAdapter) userSession.get(GesJudDeuAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (gesJudDeuAdapter == null) {
			log.error("error en: "  + funcName + ": " + GesJudDeuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, GesJudDeuAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(gesJudDeuAdapter, request);
		
		//Seteo para que al volver de buscar la cuenta no muestre la lista, para el caso de que haya buscado antes
		gesJudDeuAdapter.setListDeudaJudicial(new ArrayList<DeudaJudicialVO>());
		gesJudDeuAdapter.setVerResultados(false);
		
        // Tiene errores recuperables
		if (gesJudDeuAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapter.infoString()); 
			saveDemodaErrors(request, gesJudDeuAdapter);

			request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapter);
			return mapping.getInputForward();
		}
		
		// Se crea el searchPage para ir a la busqueda de cuenta
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		//cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(constanciaDeuAdapter.getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(gesJudDeuAdapter.getCuenta().getNumeroCuenta());

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
			GesJudDeuAdapter gesJudDeuAdapter =  (GesJudDeuAdapter) userSession.get(GesJudDeuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudDeuAdapter == null) {
				log.error("error en: "  + funcName + ": " + GesJudDeuAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudDeuAdapter.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
		//	constanciaDeuSearchPageVO = PadServiceLocator.getCuentaService().getCueExcSelSearchPageInit(userSession);
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapter);
				return mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER); 
			}
			
			// Seteo el id de la cuenta
			gesJudDeuAdapter.getCuenta().setId(new Long(selectedId));
			
			// Llamada al servicio
			gesJudDeuAdapter = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudDeuAdapterParamCuenta(userSession, gesJudDeuAdapter);
			
            // Tiene errores recuperables
			if (gesJudDeuAdapter.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudDeuAdapter.infoString()); 
				saveDemodaErrors(request, gesJudDeuAdapter);
				return forwardErrorRecoverable(mapping, request, userSession, 
						GesJudDeuAdapter.NAME, gesJudDeuAdapter);
			}
			
			// Tiene errores no recuperables
			if (gesJudDeuAdapter.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudDeuAdapter.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						GesJudDeuAdapter.NAME, gesJudDeuAdapter);
			}
						
			// Envio el VO al request
			request.setAttribute(GesJudDeuAdapter.NAME, gesJudDeuAdapter);
			// Subo el apdater al userSession
			userSession.put(GesJudDeuAdapter.NAME, gesJudDeuAdapter);
			 
			saveDemodaMessages(request, gesJudDeuAdapter);

			return mapping.findForward(GdeConstants.FWD_GESJUDDEU_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudDeuAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, GesJudDeuAdapter.NAME);
		
	}	
}
