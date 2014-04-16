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
import ar.gov.rosario.siat.ef.iface.model.OpeInvConSearchPage;
import ar.gov.rosario.siat.ef.iface.model.OpeInvSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarOpeInvConActasDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOpeInvConActasDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_ASIG_ACTAS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			OpeInvConSearchPage opeInvConActaSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConActasInicioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (opeInvConActaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConActaSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvConActaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, opeInvConActaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (opeInvConActaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvConActaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConSearchPage.NAME, opeInvConActaSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OpeInvConSearchPage.NAME, opeInvConActaSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_OPEINVCONACTA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
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
			OpeInvConSearchPage opeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvConSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				opeInvConSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (opeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvSearchPage.NAME, opeInvConSearchPageVO);
			}
				
			// Llamada al servicio	
			opeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConActasInicioSearchPageResult(userSession, opeInvConSearchPageVO);			

			// Tiene errores recuperables
			if (opeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvConSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvConSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			// Nuleo el list result
			//opeInvSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_OPEINVCONACTA_SEARCHPAGE);
			
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
		
		return baseVolver(mapping, form, request, response, OpeInvConSearchPage.NAME);
		
	}
		
	public ActionForward admContribuyentes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVCON);
		
	}
	
	public ActionForward asignar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			OpeInvConSearchPage opeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvConSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				opeInvConSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (opeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvSearchPage.NAME, opeInvConSearchPageVO);
			}
				
			if(request.getParameter("listIdContribSelected")==null){
				opeInvConSearchPageVO.addRecoverableError(EfError.ASIGNAR_INV_MSG_IDS_REQUERIDO);
				// Envio el VO al request
				request.setAttribute(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
				saveDemodaErrors(request, opeInvConSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_OPEINVCONACTA_SEARCHPAGE);
			}
			
			userSession.getNavModel().putParameter("idsSelected", opeInvConSearchPageVO.getListIdContribSelected());

			return baseForwardSearchPage(mapping, request, funcName, EfConstants.FWD_ASIGNAR_INV_ADAPTER, EfConstants.ACT_ASIGNARINV);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
			
	}

	public ActionForward hojaRuta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			OpeInvConSearchPage opeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvConSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				opeInvConSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (opeInvConSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvConSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvSearchPage.NAME, opeInvConSearchPageVO);
			}
				
			if(request.getParameter("listIdContribSelected")==null){
				opeInvConSearchPageVO.addRecoverableError(EfError.ASIGNAR_INV_MSG_IDS_REQUERIDO);
				// Envio el VO al request
				request.setAttribute(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
				// Subo el searchPage al userSession
				userSession.put(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
				saveDemodaErrors(request, opeInvConSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_OPEINVCONACTA_SEARCHPAGE);
			}
			
			userSession.getNavModel().putParameter("idsSelected", opeInvConSearchPageVO.getListIdContribSelected());

			return baseForwardSearchPage(mapping, request, funcName, EfConstants.FWD_ASIGNAR_INV_ADAPTER, EfConstants.ACT_HOJARUTA);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvSearchPage.NAME);
		}
			
	}
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);		
	}
	
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				// Bajo el searchPage del userSession
				OpeInvConSearchPage opeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (opeInvConSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
				}
				
				
				if(!StringUtil.isNullOrEmpty(navModel.getSelectedId())){
					opeInvConSearchPageVO.getOpeInvCon().getContribuyente().getPersona().setId(new Long(navModel.getSelectedId()));
							
					// Recuperamos datos del form en el vo
					DemodaUtil.populateVO(opeInvConSearchPageVO, request);
	
					// Llamada al servicio	
					opeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConSearchPageParamPersona(userSession, opeInvConSearchPageVO);
					
				}
				// Envio el VO al request
				request.setAttribute(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
				
				return mapping.findForward(EfConstants.FWD_OPEINVCONACTA_SEARCHPAGE);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
			}
	}
	
}
