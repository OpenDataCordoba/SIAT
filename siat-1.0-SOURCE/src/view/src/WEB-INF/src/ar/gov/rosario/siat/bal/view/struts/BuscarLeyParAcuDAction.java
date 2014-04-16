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

import ar.gov.rosario.siat.bal.iface.model.LeyParAcuSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarLeyParAcuDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarLeyParAcuDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_LEYPARACU, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			LeyParAcuSearchPage leyParAcuSearchPageVO = BalServiceLocator.getDefinicionService().getLeyParAcuSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (leyParAcuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuSearchPageVO.infoString()); 
				saveDemodaErrors(request, leyParAcuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (leyParAcuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + leyParAcuSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_LEYPARACU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LeyParAcuSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, LeyParAcuSearchPage.NAME);
		
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
			LeyParAcuSearchPage leyParAcuSearchPageVO = (LeyParAcuSearchPage) userSession.get(LeyParAcuSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (leyParAcuSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LeyParAcuSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LeyParAcuSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(leyParAcuSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				leyParAcuSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (leyParAcuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuSearchPageVO.infoString()); 
				saveDemodaErrors(request, leyParAcuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			}
				
			// Llamada al servicio	
			leyParAcuSearchPageVO = BalServiceLocator.getDefinicionService().getLeyParAcuSearchPageResult(userSession, leyParAcuSearchPageVO);			

			// Tiene errores recuperables
			if (leyParAcuSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + leyParAcuSearchPageVO.infoString()); 
				saveDemodaErrors(request, leyParAcuSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (leyParAcuSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + leyParAcuSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);

			userSession.put(LeyParAcuSearchPage.NAME, leyParAcuSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_LEYPARACU_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LeyParAcuSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_LEYPARACU);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_LEYPARACU);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_LEYPARACU);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_LEYPARACU);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_LEYPARACU);			
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LeyParAcuSearchPage.NAME);
		
	}
	
}
