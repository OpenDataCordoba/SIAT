//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rod.iface.model.TramiteRASearchPage;
import ar.gov.rosario.siat.rod.iface.service.RodServiceLocator;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;
import ar.gov.rosario.siat.rod.view.util.RodConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTramiteRADAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTramiteRADAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TramiteRASearchPage tramiteRASearchPageVO = RodServiceLocator.getTramiteService().getTramiteRASearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tramiteRASearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRASearchPageVO.infoString()); 
				saveDemodaErrors(request, tramiteRASearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tramiteRASearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRASearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			

			return mapping.findForward(RodConstants.FWD_TRAMITERA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRASearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TramiteRASearchPage.NAME);
		
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
			TramiteRASearchPage tramiteRASearchPageVO = (TramiteRASearchPage) userSession.get(TramiteRASearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tramiteRASearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TramiteRASearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TramiteRASearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tramiteRASearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tramiteRASearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tramiteRASearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRASearchPageVO.infoString()); 
				saveDemodaErrors(request, tramiteRASearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			}
				
			// Llamada al servicio	
			tramiteRASearchPageVO = RodServiceLocator.getTramiteService().getTramiteRASearchPageResult(userSession, tramiteRASearchPageVO);			

			// Tiene errores recuperables
			if (tramiteRASearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tramiteRASearchPageVO.infoString()); 
				saveDemodaErrors(request, tramiteRASearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tramiteRASearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tramiteRASearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(TramiteRASearchPage.NAME, tramiteRASearchPageVO);
			
			return mapping.findForward(RodConstants.FWD_TRAMITERA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TramiteRASearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_TRAMITERA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_TRAMITERA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_TRAMITERA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RodConstants.ACTION_ADMINISTRAR_TRAMITERA);

	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TramiteRASearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TramiteRASearchPage.NAME);
		
	}

	public ActionForward cambiarEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, 
				RodConstants.ACTION_ADMINISTRAR_TRAMITERA,RodConstants.ACT_CAMBIAR_ESTADO);
		}

	
}
