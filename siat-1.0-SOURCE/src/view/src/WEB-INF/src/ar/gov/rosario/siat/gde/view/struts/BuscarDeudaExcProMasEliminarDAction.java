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
import ar.gov.rosario.siat.gde.iface.model.DeudaExcProMasEliminarSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;

// Utilizado en la busqueda de deuda a eliminar de la seleccion almacenada excluida del envio judicial
// realiza la eliminacion de todos los selalmdet
public final class BuscarDeudaExcProMasEliminarDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarDeudaExcProMasEliminarDAction.class);
	
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		// TODO OJO CON LOS CANACCESS
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_PROCESO_PROCESO_MASIVO,
				GdeConstants.ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CommonKey procesoMasivoKey = new CommonKey(userSession.getNavModel().getSelectedId());
			
			DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPage = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasEliminarSearchPageInit(userSession, procesoMasivoKey);
			
			// Tiene errores recuperables
			if (deudaExcProMasEliminarSearchPage.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPage.infoString()); 
				saveDemodaErrors(request, deudaExcProMasEliminarSearchPage);
				return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPage);
			} 

			// Tiene errores no recuperables
			if (deudaExcProMasEliminarSearchPage.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPage.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPage);
			}
			
			baseInicializarSearchPage(mapping, request, userSession , DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPage);
			
			return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_ELIMINAR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasEliminarSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DeudaExcProMasEliminarSearchPage.NAME);
		
	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO = (DeudaExcProMasEliminarSearchPage) userSession.get(DeudaExcProMasEliminarSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (deudaExcProMasEliminarSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + DeudaExcProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME); 
				}
				
				// si el buscar diparado desde la pagina de busqueda
				if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(deudaExcProMasEliminarSearchPageVO, request);
					
					deudaExcProMasEliminarSearchPageVO.setListIdRecClaDeu(request.getParameterValues("listIdRecClaDeu"));
					
					// Setea el PageNumber del PageModel: necesario para que muestre el resultado				
					deudaExcProMasEliminarSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				}
				
	            // Tiene errores recuperables
				if (deudaExcProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaExcProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				}
					
				// Llamada al servicio	
				deudaExcProMasEliminarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().getDeudaExcProMasEliminarSearchPageResult(userSession, deudaExcProMasEliminarSearchPageVO);			

				// Tiene errores recuperables
				if (deudaExcProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaExcProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (deudaExcProMasEliminarSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				}
			
				// Envio el VO al request
				request.setAttribute(DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				
				return mapping.findForward(GdeConstants.FWD_DEUDA_EXC_PRO_MAS_ELIMINAR_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaExcProMasEliminarSearchPage.NAME);
			}
		}
	
		
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, DeudaExcProMasEliminarSearchPage.NAME);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			// TODO por ahora vuelve al inicializar 
			return baseRefill(mapping, form, request, response, funcName, DeudaExcProMasEliminarSearchPage.NAME);
			
		}


	public ActionForward verPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO = (DeudaExcProMasEliminarSearchPage) userSession.get(DeudaExcProMasEliminarSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (deudaExcProMasEliminarSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DeudaExcProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME); 
			}

			// no hacemos populate

			// obtenemos el nombre del archivo seleccionado
			String fileName = request.getParameter("fileParam");
			baseResponseFile(response,fileName);

            log.debug("finalizando: " + funcName);
			// se prefiere antes que el null: ver
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaExcProMasEliminarSearchPage.NAME);
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
				DeudaExcProMasEliminarSearchPage deudaExcProMasEliminarSearchPageVO = (DeudaExcProMasEliminarSearchPage) userSession.get(DeudaExcProMasEliminarSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (deudaExcProMasEliminarSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + DeudaExcProMasEliminarSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME); 
				}

				// TODO ver si no realizamos este populate ya que estaria cambiando el resultado de la busqueda
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(deudaExcProMasEliminarSearchPageVO, request);
				
	            // Tiene errores recuperables
				if (deudaExcProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaExcProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				}
				
				// llamada al servicio
				deudaExcProMasEliminarSearchPageVO = GdeServiceLocator.getGestionDeudaJudicialService().eliminarTodaDeudaExcProMasicialSeleccionada(userSession, deudaExcProMasEliminarSearchPageVO);
				
	            // Tiene errores recuperables
				if (deudaExcProMasEliminarSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.infoString()); 
					saveDemodaErrors(request, deudaExcProMasEliminarSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (deudaExcProMasEliminarSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + deudaExcProMasEliminarSearchPageVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME, deudaExcProMasEliminarSearchPageVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DeudaExcProMasEliminarSearchPage.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DeudaExcProMasEliminarSearchPage.NAME);
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
		return mapping.findForward(GdeConstants.FWD_ADMINISTRAR_DEUDA_EXC_PRO_MAS_ELIMINAR);
	}
	
}
