//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.emi.iface.model.ProPasDebSearchPage;
import ar.gov.rosario.siat.emi.iface.service.EmiServiceLocator;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.emi.view.util.EmiConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarProPasDebDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarProPasDebDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EmiSecurityConstants.ABM_PROPASDEB, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ProPasDebSearchPage proPasDebSearchPageVO = EmiServiceLocator.getGeneralService().getProPasDebSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (proPasDebSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPasDebSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (proPasDebSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPasDebSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_PROPASDEB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ProPasDebSearchPage.NAME);
		
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
			ProPasDebSearchPage proPasDebSearchPageVO = (ProPasDebSearchPage) userSession.get(ProPasDebSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (proPasDebSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ProPasDebSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ProPasDebSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(proPasDebSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				proPasDebSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (proPasDebSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPasDebSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			}
				
			// Llamada al servicio	
			proPasDebSearchPageVO = EmiServiceLocator.getGeneralService().getProPasDebSearchPageResult(userSession, proPasDebSearchPageVO);			

			// Tiene errores recuperables
			if (proPasDebSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + proPasDebSearchPageVO.infoString()); 
				saveDemodaErrors(request, proPasDebSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (proPasDebSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + proPasDebSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			// Nuleo el list result
			//proPasDebSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ProPasDebSearchPage.NAME, proPasDebSearchPageVO);
			
			return mapping.findForward(EmiConstants.FWD_PROPASDEB_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ProPasDebSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_PROPASDEB);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_PROPASDEB);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_PROPASDEB);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EmiConstants.ACTION_ADMINISTRAR_PROPASDEB);

	}

	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request, funcName, 
					EmiConstants.ACTION_ADMINISTRAR_PROCESO_PROPASDEB, 
					EmiSecurityConstants.ACT_ADMINISTRAR_PROCESO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ProPasDebSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ProPasDebSearchPage.NAME);
		
	}
}
