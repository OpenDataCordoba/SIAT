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
import ar.gov.rosario.siat.gde.iface.model.MandatarioSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarMandatarioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarMandatarioDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_MANDATARIO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			MandatarioSearchPage mandatarioSearchPageVO = GdeServiceLocator.getDefinicionService().getMandatarioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (mandatarioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioSearchPageVO.infoString()); 
				saveDemodaErrors(request, mandatarioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (mandatarioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_MANDATARIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, MandatarioSearchPage.NAME);
		
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
			MandatarioSearchPage mandatarioSearchPageVO = (MandatarioSearchPage) userSession.get(MandatarioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (mandatarioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + MandatarioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MandatarioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(mandatarioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				mandatarioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (mandatarioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioSearchPageVO.infoString()); 
				saveDemodaErrors(request, mandatarioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			}
				
			// Llamada al servicio	
			mandatarioSearchPageVO = GdeServiceLocator.getDefinicionService().getMandatarioSearchPageResult(userSession, mandatarioSearchPageVO);			

			// Tiene errores recuperables
			if (mandatarioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + mandatarioSearchPageVO.infoString()); 
				saveDemodaErrors(request, mandatarioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (mandatarioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + mandatarioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			// Nuleo el list result
			//mandatarioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(MandatarioSearchPage.NAME, mandatarioSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_MANDATARIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MandatarioSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MANDATARIO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MANDATARIO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MANDATARIO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MANDATARIO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MANDATARIO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_MANDATARIO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, MandatarioSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MandatarioSearchPage.NAME);
		
	}
		
	
}
