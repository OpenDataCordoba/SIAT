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

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.CuentaObjImpSearchPage;
import ar.gov.rosario.siat.gde.iface.model.DeudaContribSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarCuentaObjImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCuentaObjImpDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_CUENTA_OBJIMP, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CuentaObjImpSearchPage cuentaObjImpSearchPageVO = GdeServiceLocator.getGestionDeudaService()
											.getCuentaObjImpSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cuentaObjImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaObjImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaObjImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cuentaObjImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaObjImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);

			if (ModelUtil.isNullOrEmpty(cuentaObjImpSearchPageVO.getObjImp())){
				return forwardSeleccionar(mapping, request, 
						"paramObjImp", PadConstants.ACTION_BUSCAR_OBJIMP, false);
			}
			
			return mapping.findForward(GdeConstants.FWD_CUENTA_OBJIMP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaObjImpSearchPage.NAME);
		}
	}

	public ActionForward buscarObjImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		CuentaObjImpSearchPage cuentaObjImpSearchPageVO = (CuentaObjImpSearchPage) 
											userSession.get(CuentaObjImpSearchPage.NAME);

		// Si es nulo no se puede continuar
		if (cuentaObjImpSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + CuentaObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CuentaObjImpSearchPage.NAME); 
		}
		
		//Nuleo el los ID's
		navModel.setSelectedId(null);
		cuentaObjImpSearchPageVO.getObjImp().setId(null);

		
		return forwardSeleccionar(mapping, request, 
				"paramObjImp", PadConstants.ACTION_BUSCAR_OBJIMP, false);
	}
	
	public ActionForward paramObjImp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Bajo el adapter del userSession
				CuentaObjImpSearchPage cuentaObjImpSearchPageVO = (CuentaObjImpSearchPage) userSession.get(CuentaObjImpSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (cuentaObjImpSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + CuentaObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, CuentaObjImpSearchPage.NAME); 
				}

				// recupero el id seleccionado por el usuario
				//String selectedId = navModel.getSelectedId();
				String selectedId = request.getParameter("selectedId");
				
				// si el id esta vacio, pq selecciono volver, forwardeo 
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);
					return baseVolver(mapping, form, request, response, CuentaObjImpSearchPage.NAME);
				}

				
				cuentaObjImpSearchPageVO.setPageNumber(1L);
		
				// Seteo el id del Objeto Imponible seleccionado
				if (cuentaObjImpSearchPageVO.getObjImp().getId() == null)
					cuentaObjImpSearchPageVO.getObjImp().setId(new Long(selectedId));
				
				// llamo al param del servicio
				cuentaObjImpSearchPageVO = GdeServiceLocator.getGestionDeudaService().getCuentaObjImpSearchPageParamObjImp(userSession, cuentaObjImpSearchPageVO);
				
	            // Tiene errores recuperables
				if (cuentaObjImpSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + cuentaObjImpSearchPageVO.infoString()); 
					saveDemodaErrors(request, cuentaObjImpSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (cuentaObjImpSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + cuentaObjImpSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, cuentaObjImpSearchPageVO);
				
				// Envio el VO al request
				request.setAttribute(CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);

				// Subo en el el searchPage al userSession
				userSession.put(CuentaObjImpSearchPage.NAME, cuentaObjImpSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_CUENTA_OBJIMP_SEARCHPAGE);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
			}
		}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CuentaObjImpSearchPage.NAME);
		
	}
	

	
	public ActionForward liquidacionDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Seteo de selectedId para ser utilizado al regreso dentro del paramObjImp
			CuentaObjImpSearchPage cuentaObjImpSearchPageVO = (CuentaObjImpSearchPage) userSession.get(CuentaObjImpSearchPage.NAME);

			cuentaObjImpSearchPageVO.setSelectedId(cuentaObjImpSearchPageVO.getObjImp().getId().toString());
			
			NavModel navModelVolver = new NavModel();
			
			navModelVolver.setPrevAction(GdeConstants.PATH_BUSCAR_CUENTA_OBJIMP);
			navModelVolver.setPrevActionParameter(GdeConstants.ACTION_PARAM_OBJIMP);

			navModel.putParameter(BaseConstants.ACT_VOLVER, navModelVolver);
			
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId;
			request.setAttribute("liqDeudaVieneDe", "cuentaObjImp");
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
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
			CuentaObjImpSearchPage cuentaObjImp = (CuentaObjImpSearchPage)userSession.get(CuentaObjImpSearchPage.NAME);
			
			// Obtengo el id de la cuenta seleccionada
			String selectedId = request.getParameter("selectedId");
			
			//Llamada al servicio que devuelve el searchPage para el estado de cuenta, con los datos de la cuenta seleccionada
			EstadoCuentaSearchPage estadoCuentaSearchPage = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSeachPageFiltro(userSession, new Long(selectedId));
			
			// Subo el searchPage del estado cuenta al userSession
			userSession.getNavModel().putParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO, estadoCuentaSearchPage);
			userSession.put(CuentaObjImpSearchPage.NAME, cuentaObjImp);
			// fowardeo al action de estadoCuenta
			return baseForwardAdapter(mapping, request, funcName, GdeConstants.FWD_DEUDACONTRIB_ESTADOCUENTA, BaseConstants.ACT_BUSCAR);
		
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaObjImpSearchPage.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, CuentaObjImpSearchPage.NAME);
			
	}	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentaObjImpSearchPage.NAME);
		
	}
		
}
