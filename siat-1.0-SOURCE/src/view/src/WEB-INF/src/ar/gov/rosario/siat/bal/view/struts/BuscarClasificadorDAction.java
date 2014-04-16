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

import ar.gov.rosario.siat.bal.iface.model.ClasificadorSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarClasificadorDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarClasificadorDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CLASIFICADOR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ClasificadorSearchPage clasificadorSearchPageVO = BalServiceLocator.getClasificacionService().getClasificadorSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (clasificadorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorSearchPageVO.infoString()); 
				saveDemodaErrors(request, clasificadorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (clasificadorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CLASIFICADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ClasificadorSearchPage.NAME);
		
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
			ClasificadorSearchPage clasificadorSearchPageVO = (ClasificadorSearchPage) userSession.get(ClasificadorSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (clasificadorSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ClasificadorSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ClasificadorSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(clasificadorSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				clasificadorSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (clasificadorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorSearchPageVO.infoString()); 
				saveDemodaErrors(request, clasificadorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			}
				
			// Llamada al servicio	
			clasificadorSearchPageVO = BalServiceLocator.getClasificacionService().getClasificadorSearchPageResult(userSession, clasificadorSearchPageVO);			

			// Tiene errores recuperables
			if (clasificadorSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + clasificadorSearchPageVO.infoString()); 
				saveDemodaErrors(request, clasificadorSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (clasificadorSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + clasificadorSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ClasificadorSearchPage.NAME, clasificadorSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CLASIFICADOR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ClasificadorSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CLASIFICADOR);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		/*Se utiliza uno de los dos return, segun sea un encabezado detalle o no.*/
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CLASIFICADOR);				
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CLASIFICADOR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CLASIFICADOR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CLASIFICADOR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CLASIFICADOR);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ClasificadorSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ClasificadorSearchPage.NAME);
		
	}
		
	
}
