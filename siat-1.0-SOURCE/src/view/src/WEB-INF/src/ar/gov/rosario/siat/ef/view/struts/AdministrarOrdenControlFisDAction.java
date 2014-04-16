//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.OrdenControlFisAdapter;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.struts.AdministrarLiqEstadoCuentaDAction;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarOrdenControlFisDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOrdenControlFisDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
		OrdenControlFisAdapter ordenControlFisAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {

			CommonKey commonKey = new CommonKey(navModel.getSelectedId());

			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getOrdenControlFisAdapterForView(userSession, commonKey)";
				ordenControlFisAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getOrdenControlFisAdapterForUpdate(userSession, commonKey)";
				ordenControlFisAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_EDIT_ADAPTER);
			}

			if (navModel.getAct().equals(EfConstants.ACT_ADMINISTRAR)) {
				stringServicio = "getOrdenControlFisAdapterForAdmin(userSession, commonKey)";
				ordenControlFisAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisAdapterForAdmin(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_ADAPTER);
			}

			if (navModel.getAct().equals(EfConstants.ACT_ASIGNAR_ORDENCONTROLFISINIT)) {
				stringServicio = "getOrdenControlFisAdapterForAdmin(userSession, commonKey)";
				ordenControlFisAdapterVO = EfServiceLocator.getFiscalizacionService().getAsignarOrdenControlFisAdapterInit(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_ASIGNARORDEN_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables

			// Tiene errores no recuperables
			if (ordenControlFisAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + ordenControlFisAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Seteo los valores de navegacion en el adapter
			ordenControlFisAdapterVO.setValuesFromNavModel(navModel);

			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
					OrdenControlFisAdapter.NAME + ": " + ordenControlFisAdapterVO.infoString());

			log.debug("irA:"+userSession.get("irA"));			
			request.setAttribute("irA", userSession.get("irA"));
			userSession.remove("irA");

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);

			saveDemodaMessages(request, ordenControlFisAdapterVO);			

			return actionForward;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		return baseVolver(mapping, form, request, response, OrdenControlFisAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, OrdenControlFisAdapter.NAME);

	}

	// ---> metodos para el modificar
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// llamada al servicio
			OrdenControlVO ordenControlVO = EfServiceLocator.getFiscalizacionService().updateOrdenControlFis(userSession, ordenControlFisAdapterVO.getOrdenControl());
			ordenControlVO.passErrorMessages(ordenControlFisAdapterVO);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlFisAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlFisAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdenControlFisAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward cerrarOrden(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, EfSecurityConstants.CERRAR_ORDENCONTROL); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// llamada al servicio
			OrdenControlVO ordenControlVO = EfServiceLocator.getFiscalizacionService().cerrarOrdenControl(userSession, ordenControlFisAdapterVO.getOrdenControl());
			ordenControlVO.passErrorMessages(ordenControlFisAdapterVO);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlFisAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlFisAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdenControlFisAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
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
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter)userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Populate como en un buscar
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			log.debug( funcName + " " + ordenControlFisAdapterVO.getOrdenControl().getCaso().infoString());

			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, ordenControlFisAdapterVO.getOrdenControl()); 

			ordenControlFisAdapterVO.getOrdenControl().passErrorMessages(ordenControlFisAdapterVO);

			saveDemodaMessages(request, ordenControlFisAdapterVO);
			saveDemodaErrors(request, ordenControlFisAdapterVO);

			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);

			return mapping.findForward( EfConstants.FWD_ORDENCONTROLFIS_EDIT_ADAPTER); 

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}	
	}

	// <--- metodos para el modificar	


	// ---> metodos para el ViewAdapter
	public ActionForward liquidacionDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();

		try {

			// Bajo el adapter del userSession
			OrdenControlFisAdapter OrdenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (OrdenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(OrdenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (OrdenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + OrdenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, OrdenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, OrdenControlFisAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, OrdenControlFisAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, OrdenControlFisAdapterVO);
				
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();

			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId;//+"&"+"idOpeInvCon="+OrdenControlFisAdapterVO.getOpeInvCon().getId();

			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);

			request.setAttribute("liqDeudaVieneDe", "admOrdenControlFis");

			return  new ActionForward (pathVerCuenta);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}

	public ActionForward estadoCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			//bajo el adapter del usserSession
			OrdenControlFisAdapter ordenControlFisAdapterVO =  (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Obtengo el id de la cuenta del opeInvConCue seleccionado
			String selectedId = request.getParameter("selectedId");

			//Llamada al servicio que devuelve el searchPage para el estado de cuenta, con los datos de la cuenta seleccionada
			EstadoCuentaSearchPage estadoCuentaSearchPage = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSeachPageFiltro(userSession, new Long(selectedId));

			// Subo el searchPage del estado cuenta al userSession
			userSession.getNavModel().putParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO, estadoCuentaSearchPage);

			// fowardeo al action de estadoCuenta
			return baseForward(mapping, request, funcName, "volverDeLiqDeuda", GdeConstants.FWD_CUENTAS_PROCURADOR_EST_CUENTA, BaseConstants.ACT_BUSCAR);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
		}
	}

	public ActionForward volverDeLiqDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// no llama a ningun servicio, usa el adapter de la session 			


			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);

			saveDemodaMessages(request, ordenControlFisAdapterVO);

			if(ordenControlFisAdapterVO.isAdministrarOrden()) return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_ADAPTER);
			else return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_VIEW_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward histAcciones(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// llama al servicio
			ordenControlFisAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisAdapterForViewHistoricos(userSession, ordenControlFisAdapterVO);

			// Seteo los valores de navegacion en el adapter
			ordenControlFisAdapterVO.setValuesFromNavModel(userSession.getNavModel());

			request.setAttribute("irA", "historico");

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);

			return  mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_VIEW_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}

	public ActionForward ordenesAnteriores(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// llama al servicio
			ordenControlFisAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisAdapterForViewOrdenesAnt(userSession, ordenControlFisAdapterVO);

			// Seteo los valores de navegacion en el adapter
			ordenControlFisAdapterVO.setValuesFromNavModel(userSession.getNavModel());

			request.setAttribute("irA", "ordenesAnteriores");

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);

			return  mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_VIEW_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}

	public ActionForward verOrdenAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			//guarda el adapter de la orden de control actual en la sesion, para el volver
			userSession.put(OrdenControlFisAdapter.NAME_ORDENCONTROL_ORIG, ordenControlFisAdapterVO);

			CommonKey commonKey = new CommonKey(request.getParameter("selectedId"));

			// llama al servicio
			OrdenControlFisAdapter ordenControlAnteriorFisAdapterVO = EfServiceLocator.getFiscalizacionService().getOrdenControlFisAdapterForViewOrdenAnterior(userSession, commonKey);

			// Seteo los valores de navegacion en el adapter
			ordenControlAnteriorFisAdapterVO.setValuesFromNavModel(userSession.getNavModel());
			ordenControlAnteriorFisAdapterVO.setAct("verOrdenAnterior");

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlAnteriorFisAdapterVO);			
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlAnteriorFisAdapterVO);

			return  mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_VIEW_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}

	public ActionForward volverDeOrdenAnterior(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);

		try {

			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME_ORDENCONTROL_ORIG);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			ordenControlFisAdapterVO.setVerBussEnabled(true);

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);

			saveDemodaMessages(request, ordenControlFisAdapterVO);


			return mapping.findForward(EfConstants.FWD_ORDENCONTROLFIS_VIEW_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}
	// <--- metodos para el ViewAdapter


	// ---> metodos para ABM PeriodoOrden
	public ActionForward agregarPeriodoOrden(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_PERIODOORDEN);		
	}

	public ActionForward eliminarPeriodoOrden(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_PERIODOORDEN);		
	}
	// <--- metodos para ABM PeriodoOrden

	// ---> ABM Acta
	public ActionForward agregarActa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ENC_ACTA);		
	}

	public ActionForward verActa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 		
		return forwardVerAdapter(mapping, request, funcName, EfConstants.FWD_ADM_ACTA);		
	}

	public ActionForward modificarActa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardModificarAdapter(mapping, request, funcName, EfConstants.FWD_ADM_ACTA);		
	}

	public ActionForward eliminarActa(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.FWD_ADM_ACTA);		
	}
	// <--- ABM Acta

	// ---> ABM InicioInv
	public ActionForward agregarInicioInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_INICIOINV);		
	}

	public ActionForward modificarInicioInv(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_INICIOINV);		
	}

	// <--- ABM InicioInv

	// ---> ABM PlaFueDat
	public ActionForward agregarPlaFueDat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ENC_PLAFUEDAT);		
	}

	public ActionForward verPlaFueDat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 		
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_PLAFUEDAT);		
	}

	public ActionForward modificarPlaFueDat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_PLAFUEDAT);		
	}

	public ActionForward eliminarPlaFueDat(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName(); 

		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ABM_PLAFUEDAT);		
	}

	// <--- ABM PlaFueDat

	// Metodos relacionados Comparacion
	public ActionForward verComparacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMPARACION);

	}

	public ActionForward modificarComparacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMPARACION);

	}

	public ActionForward eliminarComparacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMPARACION);

	}

	public ActionForward agregarComparacion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ENC_COMPARACION);

	}

	// Metodos relacionados OrdConBasImp
	public ActionForward agregarOrdConBasImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDCONBASIMP);

	}



	public ActionForward cargarAjustesOrdConBasImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDCONBASIMP, EfConstants.ACT_CARGAR_AJUSTES);

	}

	public ActionForward eliminarOrdConBasImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDCONBASIMP);

	}

	public ActionForward cargarAlicuotasOrdConBasImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ORDCONBASIMP, EfConstants.ACT_CARGAR_ALICUOTAS);

	}

	// Metodos relacionados DetAju
	public ActionForward verDetAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJU);

	}

	public ActionForward modificarDetAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJU);

	}

	public ActionForward eliminarDetAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJU);

	}

	public ActionForward agregarDetAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ENC_DETAJU);

	}


	public ActionForward paramSearchInpector(ActionMapping mapping, ActionForm form,

			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlAdapterVO =  (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);


			// Si es nulo no se puede continuar
			if (ordenControlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName,OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// llamada al servicio
			ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().getParamSearchInspector(userSession, ordenControlAdapterVO);


			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlAdapterVO);

			return mapping.findForward(EfConstants.FWD_ASIGNARORDEN_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward paramSearchSupervisor(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlAdapterVO =  (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);


			// Si es nulo no se puede continuar
			if (ordenControlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName,OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// llamada al servicio
			ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().getParamSearchSupervisor(userSession, ordenControlAdapterVO);


			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlAdapterVO);

			return mapping.findForward(EfConstants.FWD_ASIGNARORDEN_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward paramSearchSupervisorSelect(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlAdapterVO =  (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);


			// Si es nulo no se puede continuar
			if (ordenControlAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName,OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// llamada al servicio
			ordenControlAdapterVO = EfServiceLocator.getFiscalizacionService().getParamSearchSupervisorSelect(userSession, ordenControlAdapterVO);


			// Tiene errores recuperables
			if (ordenControlAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlFisAdapter.NAME, ordenControlAdapterVO);

			return mapping.findForward(EfConstants.FWD_ASIGNARORDEN_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward asignarOrden(ActionMapping mapping, ActionForm form,	HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, EfSecurityConstants.MTD_ASIGNARORDEN); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// llamada al servicio
			OrdenControlVO ordenControlVO = EfServiceLocator.getFiscalizacionService().asignarOrdenControl(userSession, ordenControlFisAdapterVO);

			// Tiene errores recuperables
			if (ordenControlVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlVO);
				// Envio el VO al request
				request.setAttribute(OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
				return mapping.findForward(EfConstants.FWD_ASIGNARORDEN_ADAPTER);
			}

			// Tiene errores no recuperables
			if (ordenControlVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlFisAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdenControlFisAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}



	// metodos relacionados mesaEntrada
	public ActionForward enviarMesaEntrada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_ORDENCONTROLFIS, EfSecurityConstants.MTD_ENVIAR_MESA_ENTRADA); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			OrdenControlFisAdapter ordenControlFisAdapterVO = (OrdenControlFisAdapter) userSession.get(OrdenControlFisAdapter.NAME);

			// Si es nulo no se puede continuar
			if (ordenControlFisAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlFisAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlFisAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlFisAdapterVO, request);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// llamada al servicio
			OrdenControlVO ordenControlVO = EfServiceLocator.getFiscalizacionService().enviarMesaEntrada(userSession, ordenControlFisAdapterVO.getOrdenControl());
			ordenControlVO.passErrorMessages(ordenControlFisAdapterVO);

			// Tiene errores recuperables
			if (ordenControlFisAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlFisAdapterVO.infoString()); 
				saveDemodaErrors(request, ordenControlFisAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Tiene errores no recuperables
			if (ordenControlFisAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlFisAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlFisAdapter.NAME, ordenControlFisAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OrdenControlFisAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlFisAdapter.NAME);
		}
	}

	public ActionForward verMesaEntrada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_MESAENTRADA);

	}

	public ActionForward modificarMesaEntrada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_MESAENTRADA);

	}

	public ActionForward eliminarMesaEntrada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_MESAENTRADA);

	}

	public ActionForward agregarMesaEntrada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_MESAENTRADA);

	}

	// Metodos relacionados AproOrdCon
	public ActionForward verAproOrdCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_APROORDCON);

	}

	public ActionForward modificarAproOrdCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_APROORDCON);

	}

	public ActionForward eliminarAproOrdCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_APROORDCON);

	}

	public ActionForward agregarAproOrdCon(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {


		String funcName = DemodaUtil.currentMethodName();

		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_APROORDCON);

	}

	// Metodos relacionados DetAjuDocSop
	public ActionForward verDetAjuDocSop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDOCSOP);

	}

	public ActionForward modificarDetAjuDocSop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDOCSOP);

	}

	public ActionForward eliminarDetAjuDocSop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDOCSOP);

	}

	public ActionForward agregarDetAjuDocSop(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_DETAJUDOCSOP);

	}

	// Metodos relacionados ComAju
	public ActionForward verComAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMAJU);

	}

	public ActionForward modificarComAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMAJU);

	}

	public ActionForward eliminarComAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_COMAJU);

	}

	public ActionForward agregarComAju(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarAdapter(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_ENC_COMAJU);

	}
}

