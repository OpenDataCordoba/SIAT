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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelAdapter;
import ar.gov.rosario.siat.pad.iface.model.CueExcSelVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncCueExcSelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncCueExcSelDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUEEXCSEL_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExcSelAdapter cueExcSelAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			//CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCueExcSelAdapterForCreate(userSession)";
				cueExcSelAdapterVO = PadServiceLocator.getCuentaService().getCueExcSelAdapterForCreate(userSession);
				actionForward = mapping.findForward(PadConstants.FWD_CUEEXCSEL_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExcSelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExcSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExcSelAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExcSelAdapter.ENC_NAME + ": "+ cueExcSelAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelAdapter.ENC_NAME);
		}
	}
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el Adapter del userSession
		CueExcSelAdapter cueExcSelAdapterVO = (CueExcSelAdapter) userSession.get(CueExcSelAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (cueExcSelAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CueExcSelAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cueExcSelAdapterVO, request);
		
        // Tiene errores recuperables
		if (cueExcSelAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cueExcSelAdapterVO.infoString()); 
			saveDemodaErrors(request, cueExcSelAdapterVO);

			request.setAttribute(CueExcSelAdapter.NAME, cueExcSelAdapterVO);
			return mapping.getInputForward();
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(cueExcSelAdapterVO.getCueExcSel().getCuenta().getRecurso()); 
		// y el numero de cuenta en la Serch Page de Cuenta
		cuentaFiltro.getCuentaTitular().getCuenta().
			setNumeroCuenta(cueExcSelAdapterVO.getCueExcSel().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
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
			CueExcSelAdapter cueExcSelAdapterVO =  (CueExcSelAdapter) userSession.get(CueExcSelAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelAdapter.ENC_NAME); 
			}
			
			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			String findForward = PadConstants.FWD_CUEEXCSEL_ENC_EDIT_ADAPTER;
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
				return mapping.findForward(findForward); 
			}
			
			// Seteo el id de la cuenta
			cueExcSelAdapterVO.getCueExcSel().getCuenta().setId(new Long(selectedId));
			
			cueExcSelAdapterVO = PadServiceLocator.getCuentaService()
				.getCueExcSelAdapterParamCuenta(userSession, cueExcSelAdapterVO);
			
			// Tiene errores recuperables
			if (cueExcSelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExcSelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			
			return forwardAgregarAdapter(mapping, request, funcName, findForward);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			PadSecurityConstants.ABM_CUEEXCSEL_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExcSelAdapter cueExcSelAdapterVO = (CueExcSelAdapter) userSession.get(CueExcSelAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExcSelAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExcSelAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExcSelAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExcSelAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExcSelAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExcSelAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			}
			
			// llamada al servicio
			CueExcSelVO cueExcSelVO = PadServiceLocator.getCuentaService().createCueExcSel(userSession, cueExcSelAdapterVO.getCueExcSel());
			
            // Tiene errores recuperables
			if (cueExcSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExcSelVO.infoString()); 
				saveDemodaErrors(request, cueExcSelVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExcSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExcSelVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al Adapter
			if (hasAccess(userSession, PadSecurityConstants.ABM_CUEEXCSEL, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(cueExcSelVO.getId().toString());

				request.setAttribute(CueExcSelAdapter.ENC_NAME, cueExcSelAdapterVO);
				
				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, CueExcSelAdapter.ENC_NAME, 
					PadConstants.PATH_ADMINISTRAR_CUEEXCSEL, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al Adapter				
				return forwardConfirmarOk(mapping, request, funcName, CueExcSelAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExcSelAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExcSelAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExcSelAdapter.ENC_NAME);
		
	}
	
}
	
