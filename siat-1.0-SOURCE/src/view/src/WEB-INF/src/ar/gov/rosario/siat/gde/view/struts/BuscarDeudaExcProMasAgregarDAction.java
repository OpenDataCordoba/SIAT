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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasAgregarSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;

//Busca la deuda a excluir del envio a judicial para su agregacion
public final class BuscarDeudaExcProMasAgregarDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDeudaExcProMasAgregarDAction.class);

	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 

		if (userSession==null) return forwardErrorSession(request);

		try {

			CommonKey recursoKey = new CommonKey(userSession.getNavModel().getSelectedId());
			
			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPage = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasAgregarSearchPageInit(userSession, recursoKey);

			// Tiene errores recuperables
			if (deudaExcProMasAgregarSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPage.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPage);
			} 

			// Tiene errores no recuperables
			if (deudaExcProMasAgregarSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPage);
			}

			baseInicializarSearchPage(mapping, request, userSession , DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPage);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_AGREGAR_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasAgregarSearchPage.NAME);
		}
	}


	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Bajo el searchPage del userSession
			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO = (DeudaExcProMasAgregarSearchPage) userSession.get(DeudaExcProMasAgregarSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasAgregarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasAgregarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME); 
			}

			// si el buscar fue diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deudaExcProMasAgregarSearchPageVO, request);
				// carga los ids de la clasificacion de la deuda
				deudaExcProMasAgregarSearchPageVO.setListIdRecClaDeu(request.getParameterValues("listIdRecClaDeu"));

				// Setea el PageNumber del PageModel				
				deudaExcProMasAgregarSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
			// Tiene errores recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Llamada al servicio	
			deudaExcProMasAgregarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasAgregarSearchPageResult(userSession, deudaExcProMasAgregarSearchPageVO);			

			// Tiene errores recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Envio el VO al request
			request.setAttribute(DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);

			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_AGREGAR_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasAgregarSearchPage.NAME);
		}
	}

	// navega a la agregacion de la seleccion individual
	public ActionForward agregarSeleccionIndividual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		// TODO revisar el canAccess: asunto permisos y constantes
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
		if (userSession==null) return forwardErrorSession(request);

		// no ejecuto el baseForward para no altera el SearchPage
		return mapping.findForward(GdeConstants.FWD_ADMINISTRAR_DEUDA_EXC_PRO_MAS_AGREGAR);
	}


	public ActionForward verPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {

			// Bajo el searchPage del userSession
			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO = (DeudaExcProMasAgregarSearchPage) userSession.get(DeudaExcProMasAgregarSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasAgregarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasAgregarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME); 
			}

			// no hacemos populate

			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			baseResponseFile(response, fileName);
			log.debug("finalizando: " + funcName);
			// se prefiere antes que el null: ver
			return null;

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasAgregarSearchPage.NAME);
		}
	}


	public ActionForward agregarTodaDeudaProMasicialSeleccionada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el SearchPage del userSession
			DeudaExcProMasAgregarSearchPage deudaExcProMasAgregarSearchPageVO = (DeudaExcProMasAgregarSearchPage) userSession.get(DeudaExcProMasAgregarSearchPage.NAME);

			// Si es nulo no se puede continuar
			if (deudaExcProMasAgregarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasAgregarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(deudaExcProMasAgregarSearchPageVO, request);

			// Tiene errores recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// llamada al servicio
			deudaExcProMasAgregarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().agregarTodaDeudaExcProMasicialSeleccionada(userSession, deudaExcProMasAgregarSearchPageVO);

			// Tiene errores recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.infoString()); 
				saveDemodaErrors(request, deudaExcProMasAgregarSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Tiene errores no recuperables
			if (deudaExcProMasAgregarSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasAgregarSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME, deudaExcProMasAgregarSearchPageVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DeudaExcProMasAgregarSearchPage.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasAgregarSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DeudaExcProMasAgregarSearchPage.NAME);

	} 

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		return baseVolver(mapping, form, request, response, DeudaExcProMasAgregarSearchPage.NAME);
	}

}
