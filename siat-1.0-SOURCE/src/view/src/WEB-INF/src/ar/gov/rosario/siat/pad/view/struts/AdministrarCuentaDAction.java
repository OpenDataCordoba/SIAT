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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrAdapter;
import ar.gov.rosario.siat.pad.iface.model.BroCueVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.iface.model.CuentaTitularAdapter;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarCuentaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarCuentaDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, act); 
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		CuentaAdapter cuentaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			log.debug("param SELECTED ID::::: " + request.getParameter("selectedId"));
			log.debug("nav model SELECTED ID::::: " + navModel.getSelectedId());
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getCuentaAdapterForView(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getCuentaAdapterForView(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);				
				cuentaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.CUENTA_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);				
			}
			
			// nos lleva a la pagina de confirmacion para quitar el Broche asociado a la cuenta.
			if (act.equals(PadConstants.ACT_QUITAR_BROCHE_INIT)) {
				stringServicio = "getCuentaAdapterForView(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);				
				cuentaAdapterVO.addMessage(BaseError.MSG_QUITAR, PadError.BROCHE_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);				
			}
			
			if (act.equals(PadConstants.ACT_MODIFICAR_BROCHE_INIT)) {
				stringServicio = "getCuentaAdapterForView(userSession, commonKey)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);				
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_EDIT_ADAPTER);				
			}
			
			// nos lleva a la visualizacion del adapter 
			// que permite la administracion: de encabezado, domicilio de envio y de la lista de titulares de la cuenta
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPadCuentaAdapterForView(userSession)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);
				if(ModelUtil.isNullOrEmpty(cuentaAdapterVO.getCuenta().getDomicilioEnvio())){
					cuentaAdapterVO.addMessage(PadError.CUENTA_SIN_DOMICILIO_ENVIO);
				}
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPadCuentaAdapterForView(userSession)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);
				cuentaAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.CUENTA_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getCuentaAdapterForView(userSession)";
				cuentaAdapterVO = PadServiceLocator.getCuentaService().getCuentaAdapterForView(userSession, commonKey);
				cuentaAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.CUENTA_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);				
			}
			
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (cuentaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			cuentaAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + CuentaAdapter.NAME + ": "+ cuentaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);

			// Subo el apdater al userSession
			userSession.put(CuentaAdapter.NAME, cuentaAdapterVO);

			saveDemodaMessages(request, cuentaAdapterVO);

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_ENCCUENTA, BaseConstants.ACT_MODIFICAR);
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}

			// llamada al servicio
			CuentaVO cuentaVO = PadServiceLocator.getCuentaService().
			deleteCuenta(userSession, cuentaAdapterVO.getCuenta());

			// Tiene errores recuperables
			if (cuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString());
				saveDemodaErrors(request, cuentaVO);				
				request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (cuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME);
		}
	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}

			// llamada al servicio
			CuentaVO cuentaVO = PadServiceLocator.getCuentaService().activarCuenta
			(userSession, cuentaAdapterVO.getCuenta());

			// Tiene errores recuperables
			if (cuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString());
				saveDemodaErrors(request, cuentaVO);				
				request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (cuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME);
		}	
	}

	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CUENTA, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);

			// Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			// llamada al servicio
			CuentaVO cuentaVO = PadServiceLocator.getCuentaService().desactivarCuenta
			(userSession, cuentaAdapterVO.getCuenta());

			// Tiene errores recuperables
			if (cuentaVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString());
				saveDemodaErrors(request, cuentaVO);				
				request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (cuentaVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CuentaAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		return baseVolver(mapping, form, request, response, CuentaAdapter.NAME);
	}

	public ActionForward volverBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
	
		navModel.setAct(BaseConstants.ACT_MODIFICAR);
		navModel.setPrevAction(PadConstants.PATH_BUSCAR_CUENTA);
		navModel.setPrevActionParameter(BaseConstants.ACT_BUSCAR);

		return mapping.findForward(PadConstants.ACTION_ADMINISTRAR_CUENTA);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CuentaAdapter.NAME);
	}

	// Metodos relacionados de la lista de CuentaTitular de la Cuenta
	public ActionForward verCuentaTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTATITULAR);
	}

	public ActionForward modificarCuentaTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CUENTATITULAR);
	}

	public ActionForward eliminarCuentaTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_CUENTATITULAR);
	}

	
	public ActionForward marcarPrincipal(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_CUENTATITULAR, PadConstants.ACT_MARCAR_PRINCIPAL);
	}
	
	public ActionForward agregarCuentaTitular(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo las acciones seleccionar
		navModel.setSelectAction("/pad/AdministrarCuentaTitular");
		navModel.setSelectActionParameter("inicializar");
		navModel.setSelectAct(BaseConstants.ACT_AGREGAR); 

		// seteo que no pueda agregar en la seleccion
		navModel.setAgregarEnSeleccion(false);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);

		// Bajo el adapter del userSession
		CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);

		// Si es nulo no se puede continuar
		if (cuentaAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
		}

		return mapping.findForward(PadConstants.ACTION_BUSCAR_PERSONA);
	}


	public ActionForward modificarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_ENCCUENTA, PadConstants.ACT_MODIFICAR_DOMICILIO_ENV);
	}

	public ActionForward agregarDomicilio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		return baseForwardAdapter(mapping, request, funcName, 
				PadConstants.ACTION_ADMINISTRAR_ENCCUENTA, PadConstants.ACT_AGREGAR_DOMICILIO_ENV);
	}

	
	public ActionForward asignarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);
		
		userSession.getNavModel().putParameter(BrocheSearchPage.PARAM_RECURSO_READONLY, cuentaAdapterVO.getCuenta().getRecurso());
		
		return forwardSeleccionar(mapping, request, PadConstants.METOD_CUENTA_PARAM_ASIGNAR_BROCHE, PadConstants.ACTION_BUSCAR_BROCHE, false);
	}
	
	public ActionForward quitarBrocheInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForward(mapping, request, 
				funcName, BaseConstants.ACT_INICIALIZAR, PadConstants.ACTION_ADMINISTRAR_CUENTA, "quitarBrocheInit"); 
	}
	
	public ActionForward quitarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			// llamada al servicio
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);
			
			//Seteamos el NavModel para poder Volver
			NavModel navModel = userSession.getNavModel();
			navModel.setPrevAction(PadConstants.PATH_BUSCAR_CUENTA);
			navModel.setPrevActionParameter(BaseConstants.ACT_BUSCAR);

			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}

			BroCueVO brocueVO = PadServiceLocator.getCuentaService().paramQuitarBroche(userSession, cuentaAdapterVO.getCuenta());
			
			// Tiene errores recuperables
			if (brocueVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocueVO.infoString());
				saveDemodaErrors(request, brocueVO);				
				request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (brocueVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + brocueVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, "", PadConstants.PATH_ADMINISTRAR_CUENTA,  BaseConstants.ACT_INICIALIZAR, BaseConstants.ACT_MODIFICAR);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
		}
	}

	public ActionForward paramAsignarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);

			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				return mapping.findForward(PadConstants.VOLVER_BUSCAR_CUENTA);
			}

			// recupero el id seleccionado de broche por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request y restauro el selectedId con el de cuenta, para cuando recarguemo el adapter.
				request.setAttribute(TipObjImpAtrAdapter.NAME, cuentaAdapterVO);
				navModel.setSelectedId(cuentaAdapterVO.getCuenta().getId().toString());
				navModel.setAct(BaseConstants.ACT_MODIFICAR);
				return mapping.findForward(PadConstants.ACTION_ADMINISTRAR_CUENTA);
			}
			
			// llamada al servicio
			CommonKey keyBroche = new CommonKey(selectedId);
			BroCueVO broCueVO = PadServiceLocator.getCuentaService().paramAsignarBroche(userSession, cuentaAdapterVO.getCuenta(), keyBroche);
			
			// Tiene errores recuperables
			if (broCueVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + broCueVO.infoString());
				saveDemodaErrors(request, broCueVO);				
				request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);
				return mapping.findForward(PadConstants.FWD_CUENTA_VIEW_ADAPTER);
			}

			// Tiene errores no recuperables
			if (broCueVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + broCueVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaAdapter.NAME, cuentaAdapterVO);
			}

			cuentaAdapterVO.getCuenta().setBroCue(broCueVO);
			
			// Envio el VO al request
			request.setAttribute(CuentaAdapter.NAME, cuentaAdapterVO);

			// Subo el apdater al userSession
			userSession.put(CuentaAdapter.NAME, cuentaAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_CUENTA_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
		}
	}
	
	public ActionForward guardarAsignarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);	
				
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.NAME, cuentaAdapterVO);
			}
			
			Long idBroche = cuentaAdapterVO.getCuenta().getBroche().getId();
			Long idCuenta = cuentaAdapterVO.getCuenta().getId();
			
			cuentaAdapterVO.getCuenta().getBroCue().getBroche().setId(idBroche);
			cuentaAdapterVO.getCuenta().getBroCue().getCuenta().setId(idCuenta);
			
			// llamada al servicio
			BroCueVO brocueVO = PadServiceLocator.getCuentaService().asignarBroche(userSession, cuentaAdapterVO.getCuenta().getBroCue());
			
            // Tiene errores recuperables
			if (brocueVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocueVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.NAME, cuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (brocueVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + brocueVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaTitularAdapter.NAME, cuentaAdapterVO);
			}
					
			// Fue Exitoso
			ActionForward forward = forwardConfirmarOk(mapping, request, funcName, "", 
					PadConstants.PATH_ADMINISTRAR_CUENTA,  BaseConstants.ACT_INICIALIZAR, BaseConstants.ACT_MODIFICAR);
			// restauro el selectedId con el de cuenta, para cuando recarguemos el adapter.
			navModel.setSelectedId(cuentaAdapterVO.getCuenta().getId().toString());
			
			return forward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
		}
	}
	
	
	public ActionForward modificarBrocheInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		try {
			return baseForward(mapping, request, 
					funcName, BaseConstants.ACT_INICIALIZAR, PadConstants.ACTION_ADMINISTRAR_CUENTA, "modificarBrocheInit"); 
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME);
		}
	}
	
	
	public ActionForward modificarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			CuentaAdapter cuentaAdapterVO = (CuentaAdapter) userSession.get(CuentaAdapter.NAME);	
				
			// Si es nulo no se puede continuar
			if (cuentaAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cuentaAdapterVO, request);
			
            // Tiene errores recuperables
			if (cuentaAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaAdapterVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.NAME, cuentaAdapterVO);
			}
			
			// llamada al servicio
			BroCueVO brocueVO = PadServiceLocator.getCuentaService().modificarBroche(userSession, cuentaAdapterVO.getCuenta().getBroCue());
			
            // Tiene errores recuperables
			if (brocueVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocueVO.infoString()); 
				saveDemodaErrors(request, cuentaAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaAdapter.NAME, cuentaAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (brocueVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + brocueVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaTitularAdapter.NAME, cuentaAdapterVO);
			}
					
			// Fue Exitoso
			ActionForward forward = forwardConfirmarOk(mapping, request, funcName, "", 
					PadConstants.PATH_ADMINISTRAR_CUENTA,  BaseConstants.ACT_INICIALIZAR, BaseConstants.ACT_MODIFICAR);
			// restauro el selectedId con el de cuenta, para cuando recarguemos el adapter.
			navModel.setSelectedId(cuentaAdapterVO.getCuenta().getId().toString());
			
			return forward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaSearchPage.NAME);
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
			CuentaAdapter adapterVO = (CuentaAdapter)userSession.get(CuentaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCuenta().getBroCue().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCuenta().getBroCue()); 
			
			adapterVO.getCuenta().getBroCue().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(CuentaAdapter.NAME, adapterVO);
			
			return mapping.findForward( PadConstants.FWD_CUENTA_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaAdapter.NAME);
		}	
	}

	
	
	public ActionForward verRecAtrCueV(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_RECATRCUEV);
	}
	
	public ActionForward modificarRecAtrCueV(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_RECATRCUEV);
	}
}
