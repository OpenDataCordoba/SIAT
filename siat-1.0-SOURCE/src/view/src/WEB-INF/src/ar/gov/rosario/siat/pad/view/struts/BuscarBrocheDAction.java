//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.BrocheSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarBrocheDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarBrocheDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_BROCHE, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
		
			// Cargo el parametro del recurso seleccionado en el searchPage
			// Si hay valor para este parametro entnoces el combo de recurso se pone con este valor y en readonly
			RecursoVO recursoReadOnly = (RecursoVO) userSession.getNavModel().getParameter(BrocheSearchPage.PARAM_RECURSO_READONLY);
			BrocheSearchPage brocheSearchPageVO = PadServiceLocator.getDistribucionService().getBrocheSearchPageInit(userSession, recursoReadOnly);
			
			// Tiene errores recuperables
			if (brocheSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocheSearchPageVO.infoString()); 
				saveDemodaErrors(request, brocheSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BrocheSearchPage.NAME, brocheSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (brocheSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + brocheSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, BrocheSearchPage.NAME, brocheSearchPageVO);
			}
				
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , BrocheSearchPage.NAME, brocheSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_BROCHE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BrocheSearchPage.NAME);
		}
	}
	

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, BrocheSearchPage.NAME);

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
			BrocheSearchPage brocheSearchPageVO = (BrocheSearchPage) userSession.get(BrocheSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (brocheSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + BrocheSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, BrocheSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(brocheSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				brocheSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (brocheSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocheSearchPageVO.infoString()); 
				saveDemodaErrors(request, brocheSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BrocheSearchPage.NAME, brocheSearchPageVO);
			}

			// Llamada al servicio	
			brocheSearchPageVO = PadServiceLocator.getDistribucionService().getBrocheSearchPageResult(userSession, brocheSearchPageVO);			

			// Tiene errores recuperables
			if (brocheSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + brocheSearchPageVO.infoString()); 
				saveDemodaErrors(request, brocheSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, BrocheSearchPage.NAME, brocheSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (brocheSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + brocheSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, BrocheSearchPage.NAME, brocheSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(BrocheSearchPage.NAME, brocheSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(BrocheSearchPage.NAME, brocheSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_BROCHE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, BrocheSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE);
			
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE);			
	}
		
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE);
	}

	public ActionForward asignarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			//return baseForwardSearchPage (mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_BROCHE, PadConstants.ACT_ADM_BROCUE);
			return baseForwardSearchPage (mapping, request, funcName, PadConstants.FWD_BROCUE_SEARCHPAGE, BaseConstants.ACT_INICIALIZAR);
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, BrocheSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, BrocheSearchPage.NAME);			
	}

}
