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
import ar.gov.rosario.siat.gde.iface.model.DesGenSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarDesGenDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarDesGenDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESGEN, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			DesGenSearchPage desGenSearchPageVO = GdeServiceLocator.getDefinicionService().getDesGenSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (desGenSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenSearchPageVO.infoString()); 
				saveDemodaErrors(request, desGenSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenSearchPage.NAME, desGenSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (desGenSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenSearchPage.NAME, desGenSearchPageVO);
			}
						
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , DesGenSearchPage.NAME, desGenSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DESGEN_DOSEARCH);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DesGenSearchPage.NAME);
		
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
			DesGenSearchPage desGenSearchPageVO = (DesGenSearchPage) userSession.get(DesGenSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (desGenSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DesGenSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesGenSearchPage.NAME); 
			}

			// si el buscar fue disparado desde el método inicializar
			desGenSearchPageVO.setPageNumber(new Long(1));
			DemodaUtil.populateVO(desGenSearchPageVO, request);

			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(desGenSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				desGenSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (desGenSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenSearchPageVO.infoString()); 
				saveDemodaErrors(request, desGenSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenSearchPage.NAME, desGenSearchPageVO);
			}
				
			// Llamada al servicio	
			desGenSearchPageVO = GdeServiceLocator.getDefinicionService().getDesGenSearchPageResult(userSession, desGenSearchPageVO);			

			// Tiene errores recuperables
			if (desGenSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desGenSearchPageVO.infoString()); 
				saveDemodaErrors(request, desGenSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesGenSearchPage.NAME, desGenSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (desGenSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desGenSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DesGenSearchPage.NAME, desGenSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DesGenSearchPage.NAME, desGenSearchPageVO);
			// Subo el searchPage al userSession
			userSession.put(DesGenSearchPage.NAME, desGenSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_DESGEN_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesGenSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESGEN);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESGEN);
		//return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_ENC_DESGEN);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESGEN);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESGEN);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESGEN);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_DESGEN);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, DesGenSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DesGenSearchPage.NAME);
		
	}
		
	
}
