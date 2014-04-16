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
import ar.gov.rosario.siat.esp.iface.model.TipoEntradaSearchPage;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoEntradaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoEntradaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_TIPOENTRADA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoEntradaSearchPage tipoEntradaSearchPageVO = EspServiceLocator.getHabilitacionService().getTipoEntradaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoEntradaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoEntradaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoEntradaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_TIPOENTRADA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoEntradaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoEntradaSearchPage.NAME);
		
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
			TipoEntradaSearchPage tipoEntradaSearchPageVO = (TipoEntradaSearchPage) userSession.get(TipoEntradaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoEntradaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoEntradaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoEntradaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoEntradaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoEntradaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoEntradaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoEntradaSearchPageVO = EspServiceLocator.getHabilitacionService().getTipoEntradaSearchPageResult(userSession, tipoEntradaSearchPageVO);			

			// Tiene errores recuperables
			if (tipoEntradaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoEntradaSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoEntradaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoEntradaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoEntradaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			// Nuleo el list result
			//tipoEntradaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(TipoEntradaSearchPage.NAME, tipoEntradaSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_TIPOENTRADA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoEntradaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_TIPOENTRADA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		/*Se utiliza uno de los dos return, segun sea un encabezado detalle o no.*/
		return forwardAgregarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_TIPOENTRADA);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_TIPOENTRADA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_TIPOENTRADA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_TIPOENTRADA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_TIPOENTRADA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoEntradaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoEntradaSearchPage.NAME);
		
	}
		
	
}
