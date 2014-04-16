//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rec.iface.model.CatRSDreiSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarCatRSDreiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCatRSDreiDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_CATEGORIARS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CatRSDreiSearchPage catRSDreiSearchPageVO = RecServiceLocator.getDreiService().getCatRSDreiSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (catRSDreiSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiSearchPageVO.infoString()); 
				saveDemodaErrors(request, catRSDreiSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (catRSDreiSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRSDreiSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_CATRSDREI_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CatRSDreiSearchPage.NAME);
		
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
			CatRSDreiSearchPage catRSDreiSearchPageVO = (CatRSDreiSearchPage) userSession.get(CatRSDreiSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (catRSDreiSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CatRSDreiSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CatRSDreiSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(catRSDreiSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				catRSDreiSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (catRSDreiSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiSearchPageVO.infoString()); 
				saveDemodaErrors(request, catRSDreiSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			}
				
			// Llamada al servicio	
			catRSDreiSearchPageVO = RecServiceLocator.getDreiService().getCatRSDreiSearchPageResult(userSession, catRSDreiSearchPageVO);			

			// Tiene errores recuperables
			if (catRSDreiSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + catRSDreiSearchPageVO.infoString()); 
				saveDemodaErrors(request, catRSDreiSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (catRSDreiSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + catRSDreiSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			}
		

			// Envio el VO al request
			request.setAttribute(CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);
			// Nuleo el list result
			//catRSDreiSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CatRSDreiSearchPage.NAME, catRSDreiSearchPageVO);			
			return mapping.findForward(RecConstants.FWD_CATRSDREI_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CatRSDreiSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CATRSDREI);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
//		Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CATRSDREI);
//		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ENC_CATRSDREI);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CATRSDREI);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CATRSDREI);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CATRSDREI);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_CATRSDREI);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CatRSDreiSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CatRSDreiSearchPage.NAME);
		
	}
		
	
}
