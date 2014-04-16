//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.TipoMultaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoMultaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoMultaDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOMULTA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoMultaSearchPage tipoMultaSearchPageVO = GdeServiceLocator.getDefinicionService().getTipoMultaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoMultaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoMultaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoMultaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TIPOMULTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoMultaSearchPage.NAME);
		
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
			TipoMultaSearchPage tipoMultaSearchPageVO = (TipoMultaSearchPage) userSession.get(TipoMultaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoMultaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoMultaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoMultaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoMultaSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoMultaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoMultaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoMultaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoMultaSearchPageVO = GdeServiceLocator.getDefinicionService().getTipoMultaSearchPageResult(userSession, tipoMultaSearchPageVO);			

			// Tiene errores recuperables
			if (tipoMultaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoMultaSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoMultaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoMultaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoMultaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			// Nuleo el list result
			//tipoMultaSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(TipoMultaSearchPage.NAME, tipoMultaSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TIPOMULTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoMultaSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOMULTA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOMULTA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOMULTA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOMULTA);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOMULTA);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOMULTA);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoMultaSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoMultaSearchPage.NAME);
		
	}
		
	
}
