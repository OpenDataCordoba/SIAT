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
import ar.gov.rosario.siat.ef.iface.model.OrdenControlSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.struts.AdministrarLiqEstadoCuentaDAction;
import ar.gov.rosario.siat.gde.view.struts.BuscarDeudaContribDAction;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarOrdenControlDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOrdenControlDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.EMITIR_ORDENCONTROL, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			OrdenControlSearchPage ordenControlSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenControlSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (ordenControlSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (ordenControlSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ORDENCONTROL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OrdenControlSearchPage.NAME);
		
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
			OrdenControlSearchPage ordenControlSearchPageVO = (OrdenControlSearchPage) userSession.get(OrdenControlSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(ordenControlSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				ordenControlSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (ordenControlSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
				
			// Llamada al servicio	
			ordenControlSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenControlSearchPageResult(userSession, ordenControlSearchPageVO);			

			// Tiene errores recuperables
			if (ordenControlSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (ordenControlSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_ORDENCONTROL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlSearchPage.NAME);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, OrdenControlSearchPage.NAME);
		
	}

	public ActionForward emitir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, EfConstants.ACT_ORDENCONTROL_BUSCAR_CONTR, BaseConstants.ACT_BUSCAR);
			
		}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACT_ADM_ORDENCONTROL_INV);
			
		}
	
	
	public ActionForward emitirManual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return new ActionForward(EfConstants.PATH_ORDENCONTROLEMIMANUAL_SEARCHPAGE);
			
		}
	
	

	public ActionForward impresionOrdenesControl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			OrdenControlSearchPage ordenControlSearchPageVO = (OrdenControlSearchPage) userSession.get(OrdenControlSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlSearchPage.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlSearchPageVO, request);
				            
			log.debug("idsSelected: "+request.getParameter("idsSelected"));
			
			if(request.getParameter("idsSelected")==null){
				ordenControlSearchPageVO.addRecoverableError(EfError.ORDEN_CONTROL_MSG_IDSOPEINVCON_REQ);
				saveDemodaErrors(request, ordenControlSearchPageVO);
				// Envio el VO al request
				request.setAttribute(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_ORDENCONTROL_SEARCHPAGE);

			}
			
			// Tiene errores recuperables
			if (ordenControlSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (ordenControlSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + ordenControlSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
		
			
			userSession.getNavModel().putParameter("listIdOrdenControlSelected", ordenControlSearchPageVO.getIdsSelected());
			
			// Envio el VO al request
			request.setAttribute(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			
			return baseForwardSearchPage(mapping, request, funcName, EfConstants.ACT_ADM_ORDENCONTROL, 
					EfConstants.ACT_IMPRESION_ORDENCONTROL);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OrdenControlSearchPage.NAME);
		}
		
			
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OrdenControlSearchPage.NAME);
		
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
			OrdenControlSearchPage ordenControlSearchPageVO = (OrdenControlSearchPage) userSession.get(OrdenControlSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlSearchPageVO, request);
			
            // Tiene errores recuperables
			if (ordenControlSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId+"&"+"idOpeInvCon="+ordenControlSearchPageVO.getOrdenControl().getOpeInvCon().getId();
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			request.setAttribute("liqDeudaVieneDe", "ordenControlSearchPage");			
			
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
			OrdenControlSearchPage opeInvConAdapterVO =  (OrdenControlSearchPage) userSession.get(OrdenControlSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvConAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, opeInvConAdapterVO);
			}
			
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
	
	
	
	public ActionForward liquidacionDeudaContr(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			OrdenControlSearchPage ordenControlSearchPageVO = (OrdenControlSearchPage) userSession.get(OrdenControlSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (ordenControlSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OrdenControlSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(ordenControlSearchPageVO, request);
			
            // Tiene errores recuperables
			if (ordenControlSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + ordenControlSearchPageVO.infoString()); 
				saveDemodaErrors(request, ordenControlSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
			
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String pathVerCuenta = GdeConstants.PATH_BUSCAR_DEUDACONTRIB_ID + selectedId;
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			userSession.put(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY, selectedId);
			
			navModel.putParameter(BuscarDeudaContribDAction.CONTRIBUYENTE_KEY, new CommonKey(selectedId));
			
			request.setAttribute("vieneDe", "ordenControlSearchPage");			
			
			return  new ActionForward (pathVerCuenta);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}
	
	
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
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
				OrdenControlSearchPage ordenControlSearchPageVO = (OrdenControlSearchPage) userSession.get(OrdenControlSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (ordenControlSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + OrdenControlSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OrdenControlSearchPage.NAME); 
				}
				
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					ordenControlSearchPageVO.getOrdenControl().getContribuyente().getPersona().setId(new Long(navModel.getSelectedId()));
							
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(ordenControlSearchPageVO, request);
	
					// Llamada al servicio	
					ordenControlSearchPageVO = EfServiceLocator.getInvestigacionService().getOrdenControlSearchPageParamPersona(userSession, ordenControlSearchPageVO);
					
				}
				
				// Envio el VO al request
				request.setAttribute(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OrdenControlSearchPage.NAME, ordenControlSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_ORDENCONTROL_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OrdenControlSearchPage.NAME);
			}
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, OrdenControlSearchPage.NAME);
			
	}

	
}
