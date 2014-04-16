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

import ar.gov.rosario.siat.bal.iface.model.TipoIndetSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoIndetDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoIndetDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOINDET, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoIndetSearchPage tipoIndetSearchPageVO = BalServiceLocator.getDefinicionService().getTipoIndetSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoIndetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoIndetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoIndetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoIndetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPOINDET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoIndetSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoIndetSearchPage.NAME);
		
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
			TipoIndetSearchPage tipoIndetSearchPageVO = (TipoIndetSearchPage) userSession.get(TipoIndetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoIndetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoIndetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoIndetSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoIndetSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoIndetSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoIndetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoIndetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoIndetSearchPageVO = BalServiceLocator.getDefinicionService().getTipoIndetSearchPageResult(userSession, tipoIndetSearchPageVO);			

			// Tiene errores recuperables
			if (tipoIndetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoIndetSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoIndetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoIndetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoIndetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(TipoIndetSearchPage.NAME, tipoIndetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPOINDET_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoIndetSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOINDET);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOINDET);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOINDET);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOINDET);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOINDET);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOINDET);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoIndetSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoIndetSearchPage.NAME);
		
	}
		
	
}
