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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OrdenControlContrSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.struts.AdministrarLiqEstadoCuentaDAction;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarOrdenControlContrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOrdenControlContrDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.EMITIR_ORDENCONTROL, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			OrdenControlContrSearchPage ordenControlContrSearchPageVO = EfServiceLocator.getInvestigacionService().OrdenControlContrSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlContrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (ordenControlContrSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlContrSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlContrSearchPage.NAME);
		}
	}
	
	public ActionForward emisionManual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");

			UserSession userSession = canAccess(request, mapping, EfSecurityConstants.EMITIR_ORDENCONTROL, act); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				
				OrdenControlContrSearchPage ordenControlContrSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenContrManualSearchPageInit(userSession);
				
				// Tiene errores recuperables
				if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
					saveDemodaErrors(request, ordenControlContrSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				} 

				// Tiene errores no recuperables
				if (ordenControlContrSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlContrSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				}
				
				// Si no tiene error
				baseInicializarSearchPage(mapping, request, userSession , OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlContrSearchPage.NAME);
			}
		}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OrdenControlContrSearchPage.NAME);
		
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
			OrdenControlContrSearchPage ordenControlContrSearchPageVO = (OrdenControlContrSearchPage) userSession.get(OrdenControlContrSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlContrSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlContrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlContrSearchPage.NAME); 
			}
			
			
			DemodaUtil.populateVO(ordenControlContrSearchPageVO, request);
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				
				// Setea el PageNumber del PageModel				
				ordenControlContrSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlContrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			}
				
			// Llamada al servicio	
			ordenControlContrSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenControlContrSearchPageResult(userSession, ordenControlContrSearchPageVO);			

			// Tiene errores recuperables
			if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlContrSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (ordenControlContrSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlContrSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlContrSearchPage.NAME);
		}
	}

	public ActionForward paramPlan(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				OrdenControlContrSearchPage ordenControlContrSearchPageVO = (OrdenControlContrSearchPage) userSession.get(OrdenControlContrSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlContrSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlContrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlContrSearchPage.NAME); 
				}
						
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlContrSearchPageVO, request);

				// Llamada al servicio	
				ordenControlContrSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenControlContrSearchPageParamPlan(userSession, ordenControlContrSearchPageVO);			

				// Tiene errores recuperables
				if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
					saveDemodaErrors(request, ordenControlContrSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (ordenControlContrSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlContrSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlContrSearchPage.NAME);
			}
	}
	
	public ActionForward emitir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Bajo el searchPage del userSession
				OrdenControlContrSearchPage ordenControlContrSearchPageVO = (OrdenControlContrSearchPage) userSession.get(OrdenControlContrSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlContrSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlContrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlContrSearchPage.NAME); 
				}
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlContrSearchPageVO, request);
					            
				log.debug("listIdOpeInvConSelected: "+request.getParameter("listIdOpeInvConSelected"));
				
				if(request.getParameter("listIdOpeInvConSelected")==null){
					ordenControlContrSearchPageVO.addRecoverableError(EfError.ORDEN_CONTROL_MSG_IDSOPEINVCON_REQ);
					saveDemodaErrors(request, ordenControlContrSearchPageVO);
					// Envio el VO al request
					request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
					// Subo en el el searchPage al userSession
					userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
					
					return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);

				}
				
				// Llamada al servicio	
				ordenControlContrSearchPageVO = EfServiceLocator.getInvestigacionService().emitirOrdenControl(userSession, ordenControlContrSearchPageVO);			

				// Tiene errores recuperables
				if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
					saveDemodaErrors(request, ordenControlContrSearchPageVO);
					request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
					// Subo en el el searchPage al userSession
					userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
					
					return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);
				}
				
				// Tiene errores no recuperables
				if (ordenControlContrSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlContrSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				}
				// Seteo el continuar
				ordenControlContrSearchPageVO.setPrevActionParameter(BaseConstants.ACT_BUSCAR);
				ordenControlContrSearchPageVO.setPrevAction(EfConstants.PATH_ORDENCONTROL_SPAGE);
				
				// Envio el VO al request
				request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				
				return forwardConfirmarOk(mapping, request, funcName,OrdenControlContrSearchPage.NAME);				
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlContrSearchPage.NAME);
			}
		}
	




	public ActionForward liquidacionDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			OrdenControlContrSearchPage ordenControlContrSearchPage = (OrdenControlContrSearchPage) userSession.get(OrdenControlContrSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlContrSearchPage == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlContrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlContrSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlContrSearchPage, request);
			
            // Tiene errores recuperables
			if (ordenControlContrSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPage.infoString()); 
				saveDemodaErrors(request, ordenControlContrSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPage);
			}
			
			// Envio el VO al request
			request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPage);
			// Subo el apdater al userSession
			userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPage);
			
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId+"&"+"idOpeInvCon="+ordenControlContrSearchPage.getOpeInvCon().getId();
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			request.setAttribute("liqDeudaVieneDe", "ordenControlContrSearchPage");			
			
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
			
			// Obtengo el id de la cuenta del opeInvConCue seleccionado
			String selectedId = request.getParameter("selectedId");
						
			//Llamada al servicio que devuelve el searchPage para el estado de cuenta, con los datos de la cuenta seleccionada
			EstadoCuentaSearchPage estadoCuentaSearchPage = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSeachPageFiltro(userSession, new Long(selectedId));
			
			// Subo el searchPage del estado cuenta al userSession
			userSession.getNavModel().putParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO, estadoCuentaSearchPage);
			
			// fowardeo al action de estadoCuenta
			return baseForward(mapping, request, funcName, "buscar", GdeConstants.FWD_CUENTAS_PROCURADOR_EST_CUENTA, BaseConstants.ACT_BUSCAR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
		}
	}


	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OrdenControlContrSearchPage.NAME);
		
	}
	
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession == null) return forwardErrorSession(request);
		// Bajo el searchPage del userSession
		OrdenControlContrSearchPage ordenControlContrSearchPageVO = (OrdenControlContrSearchPage) userSession.get(OrdenControlContrSearchPage.NAME);
		DemodaUtil.populateVO(ordenControlContrSearchPageVO, request);
		
		userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);		
	}
	
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Bajo el searchPage del userSession
				OrdenControlContrSearchPage ordenControlContrSearchPageVO = (OrdenControlContrSearchPage) userSession.get(OrdenControlContrSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlContrSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlContrSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlContrSearchPage.NAME); 
				}
				
				ordenControlContrSearchPageVO.getOpeInvCon().getContribuyente().getPersona().setId(new Long(navModel.getSelectedId()));
						
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlContrSearchPageVO, request);

				// Llamada al servicio	
				ordenControlContrSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenControlContrSearchPageParamPersona(userSession, ordenControlContrSearchPageVO);
				
				log.debug("resultados: "+ordenControlContrSearchPageVO.getListResult().size());

				// Tiene errores recuperables
				if (ordenControlContrSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + ordenControlContrSearchPageVO.infoString()); 
					saveDemodaErrors(request, ordenControlContrSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (ordenControlContrSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + ordenControlContrSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OrdenControlContrSearchPage.NAME, ordenControlContrSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_ORDENCONTROLCONTR_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlContrSearchPage.NAME);
			}
	}

	
	
		
	
}
