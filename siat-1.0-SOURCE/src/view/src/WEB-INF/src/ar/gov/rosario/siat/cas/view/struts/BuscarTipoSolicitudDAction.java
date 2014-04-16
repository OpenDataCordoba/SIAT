//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudSearchPage;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cas.view.util.CasConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoSolicitudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoSolicitudDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, CasSecurityConstants.ABM_TIPOSOLICITUD, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoSolicitudSearchPage tipoSolicitudSearchPageVO = CasServiceLocator.getSolicitudService().getTipoSolicitudSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoSolicitudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoSolicitudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			
			return mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoSolicitudSearchPage.NAME);
		
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
			TipoSolicitudSearchPage tipoSolicitudSearchPageVO = (TipoSolicitudSearchPage) userSession.get(TipoSolicitudSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoSolicitudSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoSolicitudSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoSolicitudSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoSolicitudSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoSolicitudSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoSolicitudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoSolicitudSearchPageVO = CasServiceLocator.getSolicitudService().getTipoSolicitudSearchPageResult(userSession, tipoSolicitudSearchPageVO);			

			// Tiene errores recuperables
			if (tipoSolicitudSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoSolicitudSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoSolicitudSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoSolicitudSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoSolicitudSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			// Nuleo el list result
			//tipoSolicitudSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(TipoSolicitudSearchPage.NAME, tipoSolicitudSearchPageVO);
			
			return mapping.findForward(CasConstants.FWD_TIPOSOLICITUD_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoSolicitudSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_TIPOSOLICITUD);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
//		Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
//		return forwardAgregarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_TIPOSOLICITUD);
		return forwardAgregarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_ENC_TIPOSOLICITUD);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_TIPOSOLICITUD);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_TIPOSOLICITUD);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_TIPOSOLICITUD);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, CasConstants.ACTION_ADMINISTRAR_TIPOSOLICITUD);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoSolicitudSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoSolicitudSearchPage.NAME);
		
	}
		
	
}
