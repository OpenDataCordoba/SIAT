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

import ar.gov.rosario.siat.bal.iface.model.TipoCobSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarTipoCobDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoCobDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOB, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoCobSearchPage tipoCobSearchPageVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoCobSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoCobSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPOCOB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoCobSearchPage.NAME);
		
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
			TipoCobSearchPage tipoCobSearchPageVO = (TipoCobSearchPage) userSession.get(TipoCobSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoCobSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoCobSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoCobSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoCobSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoCobSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoCobSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoCobSearchPageVO = BalServiceLocator.getFolioTesoreriaService().getTipoCobSearchPageResult(userSession, tipoCobSearchPageVO);			

			// Tiene errores recuperables
			if (tipoCobSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoCobSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoCobSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoCobSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoCobSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoCobSearchPage.NAME, tipoCobSearchPageVO);

			userSession.put(TipoCobSearchPage.NAME, tipoCobSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPOCOB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoCobSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOB);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOB);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOB);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOB);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOB);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOB);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoCobSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoCobSearchPage.NAME);
		
	}
	
}
