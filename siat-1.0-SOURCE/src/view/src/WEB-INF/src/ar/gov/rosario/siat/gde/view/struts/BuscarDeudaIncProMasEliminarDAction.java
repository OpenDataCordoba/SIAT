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
import ar.gov.rosario.siat.gde.iface.model.DeudaIncProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;

// Utilizado en la busqueda de deuda a eliminar de la seleccion almacenada incluida del envio judicial
// realiza la eliminacion de los selalmdet
public final class BuscarDeudaIncProMasEliminarDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarDeudaIncProMasEliminarDAction.class);
	
	public static String PROCESO_MASIVO_KEY = "procesoMasivoKey";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		// TODO OJO CON LOS CANACCESS
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO);
		//userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CommonKey procesoMasivoKey = new CommonKey(userSession.getNavModel().getSelectedId());
			
			DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPage = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaIncProMasEliminarSearchPageInit(userSession, procesoMasivoKey);
			
			// Tiene errores recuperables
			if (deudaIncProMasEliminarSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPage.infoString()); 
				saveDemodaErrors(request, deudaIncProMasEliminarSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPage);
			} 

			// Tiene errores no recuperables
			if (deudaIncProMasEliminarSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPage);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPage);
			
			return mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_ELIMINAR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaIncProMasEliminarSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DeudaIncProMasEliminarSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO = (DeudaIncProMasEliminarSearchPage) userSession.get(DeudaIncProMasEliminarSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (deudaIncProMasEliminarSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + DeudaIncProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME); 
				}
				
				// si el buscar diparado desde la pagina de busqueda
				if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(deudaIncProMasEliminarSearchPageVO, request);
					
					deudaIncProMasEliminarSearchPageVO.setListIdRecClaDeu(request.getParameterValues("listIdRecClaDeu"));
					
					// Setea el PageNumber del PageModel				
					deudaIncProMasEliminarSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
					// Setea el MaxRegistros del PageModel porque la busqueda pagina pero no hacemos un count
					//deudaIncProMasEliminarSearchPageVO.setMaxRegistros(new Long(1000));
				}
				
	            // Tiene errores recuperables
				if (deudaIncProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaIncProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				}
					
				// Llamada al servicio	
				deudaIncProMasEliminarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaIncProMasEliminarSearchPageResult(userSession, deudaIncProMasEliminarSearchPageVO);			

				// Tiene errores recuperables
				if (deudaIncProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaIncProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (deudaIncProMasEliminarSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_DEUDA_INC_PRO_MAS_ELIMINAR_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaIncProMasEliminarSearchPage.NAME);
			}
		}
	
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaIncProMasEliminarSearchPage.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			// TODO por ahora vuelve al inicializar 
			return baseRefill(mapping, form, request, response, funcName, DeudaIncProMasEliminarSearchPage.NAME);
			
		}


	public ActionForward verPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO = (DeudaIncProMasEliminarSearchPage) userSession.get(DeudaIncProMasEliminarSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaIncProMasEliminarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaIncProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME); 
			}

			// no hacemos populate

			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			baseResponseFile(response,fileName);
			
            log.debug("finalizando: " + funcName);
			// se prefiere antes que el null: ver
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaIncProMasEliminarSearchPage.NAME);
		}
	}

	
	public ActionForward eliminarTodaDeudaProMasicialSeleccionada(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_MASIVO, BaseSecurityConstants.ELIMINAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el SearchPage del userSession
				DeudaIncProMasEliminarSearchPage deudaIncProMasEliminarSearchPageVO = (DeudaIncProMasEliminarSearchPage) userSession.get(DeudaIncProMasEliminarSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (deudaIncProMasEliminarSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + DeudaIncProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME); 
				}

				// TODO ver si no realizamos este populate ya que estaria cambiando el resultado de la busqueda
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deudaIncProMasEliminarSearchPageVO, request);
				
	            // Tiene errores recuperables
				if (deudaIncProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaIncProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				}
				
				// llamada al servicio
				deudaIncProMasEliminarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().eliminarTodaDeudaProMasicialSeleccionada(userSession, deudaIncProMasEliminarSearchPageVO);
				
	            // Tiene errores recuperables
				if (deudaIncProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaIncProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (deudaIncProMasEliminarSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + deudaIncProMasEliminarSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME, deudaIncProMasEliminarSearchPageVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DeudaIncProMasEliminarSearchPage.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaIncProMasEliminarSearchPage.NAME);
			}
		}

	// navega a la eliminacion de la seleccion individual
	public ActionForward eliminarSeleccionIndividual(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		// TODO revisar el canAccess: asunto permisos y constantes
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO); 
 
		if (userSession==null) return forwardErrorSession(request);
			
		// no ejecuto el baseForward para no altera el SearchPage
		return mapping.findForward(GdeConstants.FWD_ADMINISTRAR_DEUDA_INC_PRO_MAS_ELIMINAR);
	}
	
}
