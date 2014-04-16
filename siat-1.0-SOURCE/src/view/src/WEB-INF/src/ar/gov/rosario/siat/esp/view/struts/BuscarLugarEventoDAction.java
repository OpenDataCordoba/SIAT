//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.LugarEventoSearchPage;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarLugarEventoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarLugarEventoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_LUGAREVENTO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			LugarEventoSearchPage lugarEventoSearchPageVO = EspServiceLocator.getHabilitacionService().getLugarEventoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (lugarEventoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoSearchPageVO.infoString()); 
				saveDemodaErrors(request, lugarEventoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (lugarEventoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + lugarEventoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_LUGAREVENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LugarEventoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, LugarEventoSearchPage.NAME);
		
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
			LugarEventoSearchPage lugarEventoSearchPageVO = (LugarEventoSearchPage) userSession.get(LugarEventoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (lugarEventoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + LugarEventoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, LugarEventoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(lugarEventoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				lugarEventoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (lugarEventoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoSearchPageVO.infoString()); 
				saveDemodaErrors(request, lugarEventoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			}
				
			// Llamada al servicio	
			lugarEventoSearchPageVO = EspServiceLocator.getHabilitacionService().getLugarEventoSearchPageResult(userSession, lugarEventoSearchPageVO);			

			// Tiene errores recuperables
			if (lugarEventoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + lugarEventoSearchPageVO.infoString()); 
				saveDemodaErrors(request, lugarEventoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (lugarEventoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + lugarEventoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
	
			// Subo en el el searchPage al userSession
			userSession.put(LugarEventoSearchPage.NAME, lugarEventoSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_LUGAREVENTO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LugarEventoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_LUGAREVENTO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		/*Se utiliza uno de los dos return, segun sea un encabezado detalle o no.*/
		return forwardAgregarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_LUGAREVENTO);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_LUGAREVENTO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_LUGAREVENTO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_LUGAREVENTO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_LUGAREVENTO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, LugarEventoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, LugarEventoSearchPage.NAME);
		
	}
		
	
}
