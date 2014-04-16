//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.NovedadRSSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarNovedadRSDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarNovedadRSDAction.class);
	public final static String NOVEDADRS_SEARCHPAGE_FILTRO = "novedadRSSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession =  getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NovedadRSSearchPage novedadRSSearchPageFiltro = (NovedadRSSearchPage) userSession.getNavModel().getParameter(BuscarNovedadRSDAction.NOVEDADRS_SEARCHPAGE_FILTRO);
		
		if (novedadRSSearchPageFiltro == null){
			canAccess(request, mapping, RecSecurityConstants.ABM_NOVEDADRS, act);		
			if (userSession==null) return forwardErrorSession(request);
		}/*
		UserSession userSession =  canAccess(request, mapping, RecSecurityConstants.ABM_NOVEDADRS, act);		
		if (userSession==null) return forwardErrorSession(request);
		*/
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}
		
		try {
			NovedadRSSearchPage novedadRSSearchPageVO = RecServiceLocator.getDreiService().getNovedadRSSearchPageInit(userSession, novedadRSSearchPageFiltro);
			
			// Tiene errores recuperables
			if (novedadRSSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + novedadRSSearchPageVO.infoString()); 
				saveDemodaErrors(request, novedadRSSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (novedadRSSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + novedadRSSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			
			// Si vino seteado el id de cuenta llamo al buscar 
			/*String nroCuentaFiltro = "";
			if (novedadRSSearchPageFiltro != null){
				nroCuentaFiltro = novedadRSSearchPageVO.getNovedadRS().getCuentaDRei().getNumeroCuenta();
			}*/
			
			//if (!StringUtil.isNullOrEmpty(nroCuentaFiltro)){
			if (novedadRSSearchPageFiltro != null){
				//novedadRSSearchPageVO.setValuesFromNavModel(userSession.getNavModel());
				log.debug("ENTRO POR LA LIQUIDACION!!!!!!!!!!!!!!!!");
				// Seteo la cantidad de registros por pagina.
				novedadRSSearchPageVO.setPageNumber(1L);//;new Long(novedadRSSearchPageVO.getRecsByPage()));
				novedadRSSearchPageVO.getNovedadRS().setNroCuenta(novedadRSSearchPageVO.getNovedadRS().getCuentaDRei().getNumeroCuenta());
				userSession.put(NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
				//userSession.put("reqAttIsSubmittedForm", "false");
				return this.buscar(mapping, form, request, response);
			}else{
				log.debug("ENTRO POR MANTENEDOR!!!!!!!!!!!!!!!!");
			}
			
			return mapping.findForward(RecConstants.FWD_NOVEDADRS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, NovedadRSSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, NovedadRSSearchPage.NAME);

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
			NovedadRSSearchPage novedadRSSearchPageVO = (NovedadRSSearchPage) userSession.get(NovedadRSSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (novedadRSSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + NovedadRSSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, NovedadRSSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true") && !novedadRSSearchPageVO.getOrigenLiquidacion()) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(novedadRSSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				novedadRSSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
				// Verificamos si se tildo el checkList para 'Solo los que tengan observaciones especiales'
				String filtroConObservacion = request.getParameter("filtroConObservacion");
				if(filtroConObservacion != null)
					novedadRSSearchPageVO.setFiltroConObservacion(true);
				else
					novedadRSSearchPageVO.setFiltroConObservacion(false);
			}
			
            // Tiene errores recuperables
			if (novedadRSSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + novedadRSSearchPageVO.infoString()); 
				saveDemodaErrors(request, novedadRSSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			}

			// Llamada al servicio	
			novedadRSSearchPageVO = RecServiceLocator.getDreiService().getNovedadRSSearchPageResult(userSession, novedadRSSearchPageVO);			

			// Tiene errores recuperables
			if (novedadRSSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + novedadRSSearchPageVO.infoString()); 
				saveDemodaErrors(request, novedadRSSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (novedadRSSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + novedadRSSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(NovedadRSSearchPage.NAME, novedadRSSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_NOVEDADRS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, NovedadRSSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_NOVEDADRS);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, NovedadRSSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, NovedadRSSearchPage.NAME);
			
	}

	public ActionForward aplicar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_NOVEDADRS, RecConstants.ACT_APLICAR_NOVEDADRS);
	}

	public ActionForward aplicarMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_NOVEDADRS, RecConstants.ACT_APLICAR_MASIVO_NOVEDADRS);
	}

}
