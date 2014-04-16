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
import ar.gov.rosario.siat.emi.iface.model.EmisionPuntualPreviewAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionTRPAdapter;
import ar.gov.rosario.siat.emi.iface.model.EmisionVO;
import ar.gov.rosario.siat.emi.iface.model.PlanoDetalleVO;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarEmisionTRPDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEmisionTRPDAction.class);

	public static final String CUENTA_EMITIR = "cuentaEmitir";
	
	private static final String MTD_VOLVER_PREVIEW = "volverPreview";
	
	private static final String MTD_PARAM_CUENTA = "paramCuenta";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
	
		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_EMISIONPUNTUAL, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		EmisionTRPAdapter emisionTRPAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CuentaVO cuentaVO = (CuentaVO) userSession.getNavModel().getParameter(AdministrarEmisionTRPDAction.CUENTA_EMITIR);
			
			stringServicio = "getEmisionTRPAdapterForCreate(userSession)";
			emisionTRPAdapterVO = EmiServiceLocator.getEmisionService().getEmisionTRPAdapterForCreate(userSession, cuentaVO);
			actionForward = mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (emisionTRPAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + emisionTRPAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionTRPAdapter.NAME, emisionTRPAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			emisionTRPAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionTRPAdapter.NAME + ": "+ emisionTRPAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPAdapterVO);
	
			return actionForward;
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
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
			EmisionTRPAdapter emisionTRPlAdapterVO = (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}

			// Eliminamos el adapter de la sesion
			userSession.remove(EmisionTRPAdapter.NAME);
			
			return mapping.findForward(EmiConstants.ACTION_ADMINISTRAR_ENC_EMISIONPUNTUAL_PARAM_RECURSO);
					
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}
	
	public ActionForward emitir(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
				EmiSecurityConstants.ABM_EMISIONPUNTUAL, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			EmisionTRPAdapter emisionTRPlAdapterVO = (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
			
			// Limpiamos los checkboxs 
			emisionTRPlAdapterVO.clearCheckBoxs();

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionTRPlAdapterVO, request);
			
			// Tiene errores recuperables
			if (emisionTRPlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionTRPlAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionTRPlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}
			
			// llamada al servicio
			EmisionVO emisionVO = EmiServiceLocator.getEmisionService().createEmisionTRP(userSession, emisionTRPlAdapterVO);
			
	        // Tiene errores recuperables
			if (emisionVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionVO.infoString()); 
				saveDemodaErrors(request, emisionVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}
	
			// Seteamos la emision creada en el adapter
			emisionTRPlAdapterVO.setEmision(emisionVO);
			
			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Seteamos la emision creada en el adapter para Preview
			EmisionPuntualPreviewAdapter emisionPuntualPreviewAdapter = new EmisionPuntualPreviewAdapter();
			emisionPuntualPreviewAdapter.setEmision(emisionVO);
			
			// Envio el VO al request
			request.setAttribute(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapter);

			// Subo el apdater al userSession
			userSession.put(EmisionPuntualPreviewAdapter.NAME, emisionPuntualPreviewAdapter);
			
			// lo dirijo al adapter de modificacion
			return baseForward(mapping, request, funcName, MTD_VOLVER_PREVIEW, 
					EmiConstants.ACTION_ADMINISTRAR_EMISIONPUNTUAL_PREVIEW, EmiConstants.ACT_PREVIEW);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, EmisionTRPAdapter.NAME);
		
	}

	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el searchPage del userSession
		EmisionTRPAdapter emisionTRPlAdapterVO = (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (emisionTRPlAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
		}

		// Limpiamos los checkboxs
		emisionTRPlAdapterVO.clearCheckBoxs();

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(emisionTRPlAdapterVO, request);
		
        // Tiene errores recuperables
		if (emisionTRPlAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + emisionTRPlAdapterVO.infoString()); 
			saveDemodaErrors(request, emisionTRPlAdapterVO);

			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			return mapping.getInputForward();
		}
		
		// Subo el searchPage al userSession
		userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		
		// Seteo el recurso 
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setRecurso(emisionTRPlAdapterVO.getEmision().getRecurso()); 

		// y el numero de cuenta
		cuentaFiltro.getCuentaTitular().getCuenta()
			.setNumeroCuenta(emisionTRPlAdapterVO.getEmision().getCuenta().getNumeroCuenta());

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
			EmisionTRPAdapter emisionTRPlAdapterVO =  (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
			
			// recupero el id seleccionado por el usuario
			String selectedId = request.getParameter("selectedId");
						
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
				return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER); 
			}
			
			// Seteo el id de la cuenta
			emisionTRPlAdapterVO.getEmision().getCuenta().setId(new Long(selectedId));
			
			emisionTRPlAdapterVO = EmiServiceLocator.getEmisionService()
				.getEmisionTRPAdapterParamCuenta(userSession, emisionTRPlAdapterVO);
			
			// Tiene errores recuperables
			if (emisionTRPlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionTRPlAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionTRPlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (emisionTRPlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + emisionTRPlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}			
			
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
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
			EmisionTRPAdapter emisionTRPlAdapterVO = (EmisionTRPAdapter) 
				userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME 
						+ " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(emisionTRPlAdapterVO, request);
			
			log.debug( funcName + " " + emisionTRPlAdapterVO.getEmision().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, emisionTRPlAdapterVO.getEmision()); 
			
			emisionTRPlAdapterVO.getEmision().passErrorMessages(emisionTRPlAdapterVO);
		    
		    saveDemodaMessages(request, emisionTRPlAdapterVO);
		    
		    saveDemodaErrors(request, emisionTRPlAdapterVO);
		    
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);	
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}	
	}

	public ActionForward agregarPlanoDetalle(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		try {

			// Bajo el adapter del usserSession
			EmisionTRPAdapter emisionTRPlAdapterVO =  (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " " + "IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}

			// Limpiamos los checkboxs
			emisionTRPlAdapterVO.clearCheckBoxs();
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionTRPlAdapterVO, request);
			
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			//return forwardAgregarAdapter(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_PLANODETALLE);
			
			// lo dirijo al adapter de modificacion
			return baseForward(mapping, request, funcName,"volverDetalle", 
					EmiConstants.ACTION_ADMINISTRAR_PLANODETALLE, BaseConstants.ACT_AGREGAR);


		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}

	public ActionForward paramDescVisacionPrevia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		try {
			//bajo el adapter del usserSession
			EmisionTRPAdapter emisionTRPlAdapterVO =  (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
			
			// Limpiamos los checkboxs
			emisionTRPlAdapterVO.clearCheckBoxs();
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionTRPlAdapterVO, request);

			if (!emisionTRPlAdapterVO.getDescVisacionPrevia().equals("on")) {
				emisionTRPlAdapterVO.setRecibo1("");
				emisionTRPlAdapterVO.setRecibo2("");
			}
			
            // Tiene errores recuperables
			if (emisionTRPlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionTRPlAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionTRPlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}

	public ActionForward volverDetalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		try {
			//bajo el adapter del usserSession
			EmisionTRPAdapter emisionTRPlAdapterVO =  (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
			
			// Seteo los valores de navegacion en el adapter
			//emisionTRPlAdapterVO.setValuesFromNavModel(navModel);
						
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}
	
	public ActionForward eliminarDetalle(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		try {
			//bajo el adapter del usserSession
			EmisionTRPAdapter emisionTRPlAdapterVO =  (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
			
			// Limpiamos los checkboxs
			//emisionTRPlAdapterVO.clearCheckBoxs();

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(emisionTRPlAdapterVO, request);
			
            // Tiene errores recuperables
			if (emisionTRPlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + emisionTRPlAdapterVO.infoString()); 
				saveDemodaErrors(request, emisionTRPlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);
			}
			
			String selectedId = request.getParameter("selectedId");
			
			PlanoDetalleVO t = null;
			for (PlanoDetalleVO p: emisionTRPlAdapterVO.getListPlanoDetalle()) {
				if (p.getId().equals(new Long(selectedId))) {
					t = p;
				}
			}
			
			emisionTRPlAdapterVO.getListPlanoDetalle().remove((PlanoDetalleVO) t);
			
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, EmisionTRPAdapter.NAME);
	}
	
	public ActionForward volverPreview(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);		
		if (userSession==null) return forwardErrorSession(request);
		
		EmisionTRPAdapter emisionTRPlAdapterVO = null;
		
		try {
			
			// Bajo el adapter del userSession
			 emisionTRPlAdapterVO = (EmisionTRPAdapter) userSession.get(EmisionTRPAdapter.NAME);

			// Si es nulo no se puede continuar
			if (emisionTRPlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + EmisionTRPAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EmisionTRPAdapter.NAME); 
			}
	
			if (log.isDebugEnabled()) log.debug(funcName + ": " + EmisionTRPAdapter.NAME + ": "+ emisionTRPlAdapterVO.infoString());
			
			// Limpiamos los errores si habia
			emisionTRPlAdapterVO.clearErrorMessages();
			
			// Envio el VO al request
			request.setAttribute(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// Subo el apdater al userSession
			userSession.put(EmisionTRPAdapter.NAME, emisionTRPlAdapterVO);

			// lo dirijo al adapter de modificacion
			return mapping.findForward(EmiConstants.FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EmisionTRPAdapter.NAME);
		}
	}

}
