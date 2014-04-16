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

import ar.gov.rosario.siat.bal.iface.model.SistemaSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarSistemaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarSistemaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_SISTEMA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			SistemaSearchPage sistemaSearchPageVO = BalServiceLocator.getDefinicionService().getSistemaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (sistemaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaSearchPageVO.infoString()); 
				saveDemodaErrors(request, sistemaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaSearchPage.NAME, sistemaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (sistemaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaSearchPage.NAME, sistemaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , SistemaSearchPage.NAME, sistemaSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_SISTEMA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, SistemaSearchPage.NAME);
		
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
			SistemaSearchPage sistemaSearchPageVO = (SistemaSearchPage) userSession.get(SistemaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (sistemaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + SistemaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SistemaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(sistemaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				sistemaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (sistemaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaSearchPageVO.infoString()); 
				saveDemodaErrors(request, sistemaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaSearchPage.NAME, sistemaSearchPageVO);
			}
				
			// Llamada al servicio	
			sistemaSearchPageVO = BalServiceLocator.getDefinicionService().getSistemaSearchPageResult(userSession, sistemaSearchPageVO);			

			// Tiene errores recuperables
			if (sistemaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + sistemaSearchPageVO.infoString()); 
				saveDemodaErrors(request, sistemaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, SistemaSearchPage.NAME, sistemaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (sistemaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + sistemaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, SistemaSearchPage.NAME, sistemaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(SistemaSearchPage.NAME, sistemaSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(SistemaSearchPage.NAME, sistemaSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_SISTEMA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SistemaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SISTEMA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SISTEMA);
		//return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_ENC_SISTEMA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SISTEMA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SISTEMA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SISTEMA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_SISTEMA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, SistemaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, SistemaSearchPage.NAME);
		
	}
		
	
}
