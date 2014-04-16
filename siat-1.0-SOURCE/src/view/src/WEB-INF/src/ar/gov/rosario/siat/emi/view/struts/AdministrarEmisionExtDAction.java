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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.emi.iface.model.EmisionExtAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.gde.iface.model.DeuAdmRecConVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEmisionExtDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionExtDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONEXT, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionExtAdapter emisionExtAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getEmisionExtAdapterForView(userSession, commonKey)";
				emisionExtAdapterVO = EmiServiceLocator.getEmisionService().getEmisionExtAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONEXT_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getEmisionExtAdapterForCreate(userSession)";
				emisionExtAdapterVO = EmiServiceLocator.getEmisionService().getEmisionExtAdapterForCreate(userSession);
				actionForward = mapping.findForward(EmiConstants.FWD_EMISIONEXT_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionExtAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emisionExtAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionExtAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionExtAdapter.NAME + ": "+ emisionExtAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionExtAdapter.NAME, emisionExtAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExtAdapter.NAME, emisionExtAdapterVO);
			 
			saveDemodaMessages(request, emisionExtAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONEXT, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionExtAdapter emisionExtAdapterVO = (EmisionExtAdapter) userSession.get(EmisionExtAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExtAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExtAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExtAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionExtAdapterVO, request);
			
			
			// Bajo los valores que se ingresaron de los conceptos del recurso 
			for(DeuAdmRecConVO c: emisionExtAdapterVO.getListDeuAdmRecConVO()){
				c.setImporte(request.getParameter("importe"+c.getIdView()));
			}

			// Tiene errores recuperables
			if (emisionExtAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExtAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService().createEmisionExt(userSession, emisionExtAdapterVO);
			
            // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, EmisionExtAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionExtAdapter.NAME);
		
	}
	
	public ActionForward paramRecurso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionExtAdapter emisionExtAdapterVO = (EmisionExtAdapter) userSession.get(EmisionExtAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExtAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExtAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExtAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionExtAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionExtAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExtAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// llamada al servicio
			emisionExtAdapterVO = EmiServiceLocator.getEmisionService()
				.getEmisionExtAdapterParamRecurso(userSession, emisionExtAdapterVO);
			
            // Tiene errores recuperables
			if (emisionExtAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExtAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExtAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExtAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EmisionExtAdapter.NAME, emisionExtAdapterVO);
			// Subo el apdater al userSession
			userSession.put(EmisionExtAdapter.NAME, emisionExtAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONEXT_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtAdapter.NAME);
		}
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionExtAdapter emisionExtAdapterVO = (EmisionExtAdapter) userSession.get(EmisionExtAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExtAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExtAdapter.NAME 
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExtAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(emisionExtAdapterVO, request);
			
			log.debug( funcName + " " + emisionExtAdapterVO.getEmision().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, emisionExtAdapterVO.getEmision()); 
			
			emisionExtAdapterVO.getEmision().passErrorMessages(emisionExtAdapterVO);
		    
		    saveDemodaMessages(request, emisionExtAdapterVO);
		    
		    saveDemodaErrors(request, emisionExtAdapterVO);
		    
			request.setAttribute(EmisionExtAdapter.NAME, emisionExtAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONEXT_EDIT_ADAPTER);	
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtAdapter.NAME);
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
		EmisionExtAdapter emisionExtAdapterVO = (EmisionExtAdapter) userSession.get(EmisionExtAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (emisionExtAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + EmisionExtAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExtAdapter.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(emisionExtAdapterVO, request);
		
        // Tiene errores recuperables
		if (emisionExtAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionExtAdapterVO.infoString()); 
			saveDemodaErrors(request, emisionExtAdapterVO);

			request.setAttribute(EmisionExtAdapter.NAME, emisionExtAdapterVO);
			return mapping.getInputForward();
		}
		
		// Subo el searchPage al userSession
		userSession.put(EmisionExtAdapter.NAME, emisionExtAdapterVO);

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(emisionExtAdapterVO.getEmision().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setNumeroCuenta(emisionExtAdapterVO.getCuenta().getNumeroCuenta());

		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		// Forwardeo a la Search Page de Cuenta
		return forwardSeleccionar(mapping, request, "paramCuenta", PadConstants.ACTION_BUSCAR_CUENTA, false);
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
			EmisionExtAdapter emisionExtAdapterVO =  (EmisionExtAdapter) userSession.get(EmisionExtAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionExtAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionExtAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionExtAdapter.NAME); 
			}
			

			// recupero el id seleccionado por el usuario
			//String selectedId = navModel.getSelectedId();
			String selectedId = request.getParameter("selectedId");
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(EmisionExtAdapter.NAME, emisionExtAdapterVO);
				return mapping.findForward(EmiConstants.FWD_EMISIONEXT_EDIT_ADAPTER); 
			}
			
			// Seteo el id de la cuenta
			emisionExtAdapterVO.getCuenta().setId(new Long(selectedId));
			
			emisionExtAdapterVO = EmiServiceLocator.getEmisionService()
				.getEmisionExtAdapterParamCuenta(userSession, emisionExtAdapterVO);
			
			// Tiene errores recuperables
			if (emisionExtAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionExtAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionExtAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionExtAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionExtAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EmisionExtAdapter.NAME, emisionExtAdapterVO);
			}			
			
			// Envio el VO al request
			request.setAttribute(EmisionExtAdapter.NAME, emisionExtAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionExtAdapter.NAME, emisionExtAdapterVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONEXT_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionExtAdapter.NAME);
		}
	}
		
}
