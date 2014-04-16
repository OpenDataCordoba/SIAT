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

import ar.gov.rosario.siat.bal.iface.model.CuentaBancoSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCuentaBancoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCuentaBancoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_CUENTABANCO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CuentaBancoSearchPage cuentaBancoSearchPageVO = BalServiceLocator.getDefinicionService().getCuentaBancoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cuentaBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cuentaBancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CUENTABANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CuentaBancoSearchPage.NAME);
		
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
			CuentaBancoSearchPage cuentaBancoSearchPageVO = (CuentaBancoSearchPage) userSession.get(CuentaBancoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cuentaBancoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CuentaBancoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CuentaBancoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cuentaBancoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				cuentaBancoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (cuentaBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			}
				
			// Llamada al servicio	
			cuentaBancoSearchPageVO = BalServiceLocator.getDefinicionService().getCuentaBancoSearchPageResult(userSession, cuentaBancoSearchPageVO);			

			// Tiene errores recuperables
			if (cuentaBancoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cuentaBancoSearchPageVO.infoString()); 
				saveDemodaErrors(request, cuentaBancoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cuentaBancoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cuentaBancoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(CuentaBancoSearchPage.NAME, cuentaBancoSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_CUENTABANCO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentaBancoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CUENTABANCO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
	
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CUENTABANCO);
	 }

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CUENTABANCO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CUENTABANCO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CUENTABANCO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_CUENTABANCO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CuentaBancoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CuentaBancoSearchPage.NAME);
		
	}
		
	
}
