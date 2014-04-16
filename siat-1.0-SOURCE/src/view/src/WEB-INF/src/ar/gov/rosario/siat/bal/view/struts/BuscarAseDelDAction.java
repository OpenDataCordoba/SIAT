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

import ar.gov.rosario.siat.bal.iface.model.AseDelSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarAseDelDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAseDelDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_ASEDEL, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		try {
		
			AseDelSearchPage aseDelSearchPageVO = BalServiceLocator.getDelegadorService().getAseDelSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (aseDelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aseDelSearchPageVO.infoString()); 
				saveDemodaErrors(request, aseDelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AseDelSearchPage.NAME, aseDelSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (aseDelSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aseDelSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AseDelSearchPage.NAME, aseDelSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AseDelSearchPage.NAME, aseDelSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ASEDEL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AseDelSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, AseDelSearchPage.NAME);

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
			AseDelSearchPage aseDelSearchPageVO = (AseDelSearchPage) userSession.get(AseDelSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (aseDelSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AseDelSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AseDelSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(aseDelSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				aseDelSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (aseDelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aseDelSearchPageVO.infoString()); 
				saveDemodaErrors(request, aseDelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AseDelSearchPage.NAME, aseDelSearchPageVO);
			}

			// Llamada al servicio	
			aseDelSearchPageVO = BalServiceLocator.getDelegadorService().getAseDelSearchPageResult(userSession, aseDelSearchPageVO);			

			// Tiene errores recuperables
			if (aseDelSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + aseDelSearchPageVO.infoString()); 
				saveDemodaErrors(request, aseDelSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AseDelSearchPage.NAME, aseDelSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (aseDelSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + aseDelSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AseDelSearchPage.NAME, aseDelSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(AseDelSearchPage.NAME, aseDelSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(AseDelSearchPage.NAME, aseDelSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_ASEDEL_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AseDelSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASEDEL);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASEDEL);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASEDEL);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ASEDEL);
			
		}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, AseDelSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, AseDelSearchPage.NAME);
			
	}

	public ActionForward admProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_PROCESO_ASEDEL, BalConstants.ACT_ADM_PROCESO_ASEDEL);
	}

}
