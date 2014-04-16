//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.EnvioOsirisSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;


public class BuscarEnvioOsirisDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarEnvioOsirisDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ENVIOOSIRIS, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		
		try {
		
			EnvioOsirisSearchPage envioOsirisSearchPageVO = BalServiceLocator.getEnvioOsirisService().getEnvioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (envioOsirisSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioOsirisSearchPageVO.infoString()); 
				saveDemodaErrors(request, envioOsirisSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisSearchPage.NAME, envioOsirisSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (envioOsirisSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + envioOsirisSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisSearchPage.NAME, envioOsirisSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , EnvioOsirisSearchPage.NAME, envioOsirisSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EnvioOsirisSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, EnvioOsirisSearchPage.NAME);

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
			EnvioOsirisSearchPage envioSearchPageVO = (EnvioOsirisSearchPage) userSession.get(EnvioOsirisSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (envioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + EnvioOsirisSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, EnvioOsirisSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(envioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				envioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (envioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioSearchPageVO.infoString()); 
				saveDemodaErrors(request, envioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisSearchPage.NAME, envioSearchPageVO);
			}

			// Llamada al servicio	
			envioSearchPageVO = BalServiceLocator.getEnvioOsirisService().getEnvioSearchPageResult(envioSearchPageVO, userSession);			

			// Tiene errores recuperables
			if (envioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + envioSearchPageVO.infoString()); 
				saveDemodaErrors(request, envioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, EnvioOsirisSearchPage.NAME, envioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (envioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + envioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, EnvioOsirisSearchPage.NAME, envioSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(EnvioOsirisSearchPage.NAME, envioSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(EnvioOsirisSearchPage.NAME, envioSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ENVIOOSIRIS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, EnvioOsirisSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENVIOOSIRIS);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, EnvioOsirisSearchPage.NAME);
			
	}

	public ActionForward obtenerEnvios(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENVIOOSIRIS, BalConstants.ACT_OBTENER_ENVIOOSIRIS);
	}
	
	public ActionForward procesarEnvios(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENVIOOSIRIS, BalConstants.ACT_PROCESAR_ENVIOOSIRIS);
	}

	public ActionForward generarTransaccion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENVIOOSIRIS, BalConstants.ACT_GENERAR_TRANSACCION);
	}
	
	public ActionForward generarDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENVIOOSIRIS, BalConstants.ACT_GENERAR_DECJUR);
	}
	
	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENVIOOSIRIS, BalConstants.ACT_CAMBIAR_ESTADO);
	}
}
