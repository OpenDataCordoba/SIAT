//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OpeInvSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarOpeInvDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOpeInvDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINV, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			OpeInvSearchPage opeInvSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (opeInvSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvSearchPage.NAME, opeInvSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (opeInvSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvSearchPage.NAME, opeInvSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OpeInvSearchPage.NAME, opeInvSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_OPEINV_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OpeInvSearchPage.NAME);
		
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
			OpeInvSearchPage opeInvSearchPageVO = (OpeInvSearchPage) userSession.get(OpeInvSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				opeInvSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (opeInvSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvSearchPage.NAME, opeInvSearchPageVO);
			}
				
			// Llamada al servicio	
			opeInvSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvSearchPageResult(userSession, opeInvSearchPageVO);			

			// Tiene errores recuperables
			if (opeInvSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvSearchPage.NAME, opeInvSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvSearchPage.NAME, opeInvSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OpeInvSearchPage.NAME, opeInvSearchPageVO);
			// Nuleo el list result
			//opeInvSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvSearchPage.NAME, opeInvSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_OPEINV_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINV);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINV);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINV);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINV);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINV);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINV);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, OpeInvSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OpeInvSearchPage.NAME);
		
	}
		
	public ActionForward admContribuyentes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVCON);
		
	}


	
}
