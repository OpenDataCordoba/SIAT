//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.ConAtrAdapter;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarPersonaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarPersonaDAction.class);
	
	public final static String  PERSONA = "persona";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_PERSONA , act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			PersonaSearchPage personaSearchPageVO = PadServiceLocator.getPadPersonaService().getPersonaSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (personaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaSearchPageVO.infoString()); 
				saveDemodaErrors(request, personaSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, PersonaSearchPage.NAME, personaSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (personaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + personaSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PersonaSearchPage.NAME, personaSearchPageVO);
			}

			// seteo los parametros provenientes del navModel
			Boolean paramSelectSoloContrib = (Boolean)
		 		userSession.getNavModel().getParameter(PersonaSearchPage.PARAM_SELECCIONAR_SOLO_CONTRIB);
		 	
		 	if (paramSelectSoloContrib != null) {
		 		personaSearchPageVO.setParamSelectSoloContrib(paramSelectSoloContrib);
		 	}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , PersonaSearchPage.NAME, personaSearchPageVO);
			
			if (personaSearchPageVO.getEsBusqPersFisica()){
				return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
			}else{
				return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
			}
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, PersonaSearchPage.NAME);
		
	}
	
	public ActionForward paramTipoPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				PersonaSearchPage personaSearchPageVO = (PersonaSearchPage) userSession.get(PersonaSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (personaSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + PersonaSearchPage.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PersonaSearchPage.NAME); 
				}
				Character tp = request.getParameter("persona.tipoPersona").charAt(0);	
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(personaSearchPageVO, request);
				log.debug("tipo persona despues del populate: " + personaSearchPageVO.getPersona().getTipoPersona());
				personaSearchPageVO.getPersona().setTipoPersona(tp);

				//limpio la lista de resultados
				personaSearchPageVO.setListResult(new ArrayList());
				personaSearchPageVO.setPageNumber(new Long(0));
				
	            // Tiene errores recuperables
				if (personaSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + personaSearchPageVO.infoString()); 
					saveDemodaErrors(request, personaSearchPageVO);
					request.setAttribute(PersonaSearchPage.NAME, personaSearchPageVO);
					//return forwardErrorRecoverable(mapping, request, userSession, PersonaSearchPage.NAME, personaSearchPageVO);
					if (personaSearchPageVO.getEsBusqPersFisica()){
						return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
					}else{
						return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
					}
				}
				
				// Envio el VO al request
				request.setAttribute(PersonaSearchPage.NAME, personaSearchPageVO);

				if (personaSearchPageVO.getEsBusqPersFisica()){
					return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
				}else{
					return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
				}
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
			}
		}

	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			PersonaSearchPage personaSearchPageVO = (PersonaSearchPage) userSession.get(PersonaSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (personaSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + PersonaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PersonaSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(personaSearchPageVO, request);

				// Setea el PageNumber del PageModel				
				personaSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				// Setea el MaxRegistros del PageModel porque la busqueda pagina pero no hacemos un count
				personaSearchPageVO.setMaxRegistros(new Long(1000));
			}
			
            // Tiene errores recuperables
			if (personaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaSearchPageVO.infoString());
				//limpio la lista de resultados
				personaSearchPageVO.setListResult(new ArrayList());
				personaSearchPageVO.setPageNumber(new Long(0)); // para que no muestre la lista de resultados
				saveDemodaErrors(request, personaSearchPageVO);
				request.setAttribute(PersonaSearchPage.NAME, personaSearchPageVO);
				//return forwardErrorRecoverable(mapping, request, userSession, PersonaSearchPage.NAME, personaSearchPageVO);
				if (personaSearchPageVO.getEsBusqPersFisica()){
					return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
				}else{
					return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
				}
			}
				
			// Llamada al servicio	
			personaSearchPageVO = PadServiceLocator.getPadPersonaService().getPersonaSearchPageResult(userSession, personaSearchPageVO);			

			// Tiene errores recuperables
			if (personaSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + personaSearchPageVO.infoString());
				//limpio la lista de resultados
				personaSearchPageVO.setListResult(new ArrayList());
				personaSearchPageVO.setPageNumber(new Long(0)); // para que no muestre la lista de resultados
				saveDemodaErrors(request, personaSearchPageVO);
				request.setAttribute(PersonaSearchPage.NAME, personaSearchPageVO);
				//return forwardErrorRecoverable(mapping, request, userSession, PersonaSearchPage.NAME, personaSearchPageVO);
				if (personaSearchPageVO.getEsBusqPersFisica()){
					return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
				}else{
					return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
				}
			}
			
			// Tiene errores no recuperables
			if (personaSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + personaSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, PersonaSearchPage.NAME, personaSearchPageVO);
			}
			
			log.debug(funcName + " userSession...AgregarEnSeleccion(): " + userSession.getNavModel().getAgregarEnSeleccion());
			// Seteamos la bandera agregar en seleccion
			personaSearchPageVO.setAgregarEnSeleccion(userSession.getNavModel().getAgregarEnSeleccion());
			
			// Envio el VO al request
			request.setAttribute(PersonaSearchPage.NAME, personaSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(PersonaSearchPage.NAME, personaSearchPageVO);
			
			if (personaSearchPageVO.getEsBusqPersFisica()){
				return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
			}else{
				return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
			}
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PersonaSearchPage.NAME);
		}
	}
 
	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();

		// si es contribuyente va al AdministrarContribuyente.
		String abm = request.getParameter("esContribuyente");
		if (abm != null && abm.equals("true")){
			return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CONTRIBUYENTE);
		}
		
		// si no es contribuyente va al AdministrarPersona.
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_PERSONA);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

		// Bajo el adapter del userSession
		PersonaSearchPage personaSearchPageVO = (PersonaSearchPage) userSession.get(PersonaSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (personaSearchPageVO == null) {
			log.error("error en: "  + funcName + ": " + PersonaSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PersonaSearchPage.NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(personaSearchPageVO, request);
		
		// Tiene errores recuperables
		if (personaSearchPageVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + personaSearchPageVO.infoString());
			//limpio la lista de resultados
			personaSearchPageVO.setListResult(new ArrayList());
			personaSearchPageVO.setPageNumber(new Long(0)); // para que no muestre la lista de resultados
			saveDemodaErrors(request, personaSearchPageVO);
			request.setAttribute(PersonaSearchPage.NAME, personaSearchPageVO);
			//return forwardErrorRecoverable(mapping, request, userSession, PersonaSearchPage.NAME, personaSearchPageVO);
			if (personaSearchPageVO.getEsBusqPersFisica()){
				return mapping.findForward(PadConstants.FWD_PERSONA_FISICA_SEARCHPAGE);
			}else{
				return mapping.findForward(PadConstants.FWD_PERSONA_JURIDICA_SEARCHPAGE);
			}
		}
		
		// seteo de la persona como filtro a utilizar en la agregacion de la nueva persona
		navModel.putParameter(BuscarPersonaDAction.PERSONA, personaSearchPageVO.getPersona());
		
		return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_PERSONA);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		// si es contribuyente va al AdministrarContribuyente.
		String abm = request.getParameter("esContribuyente");
		if (abm != null && abm.equals("true")){
			return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CONTRIBUYENTE);
		}
		
		// si no es contribuyente va al AdministrarPersona.
		return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_PERSONA);
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_PERSONA);

	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		// issue #8312: GDE - Gestión de Deuda/Convenios. Gestión por Contribuyente. Navegación
		return baseSeleccionar(mapping, request, response, funcName, PersonaSearchPage.NAME, false);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		// issue #8312: GDE - Gestión de Deuda/Convenios. Gestión por Contribuyente. Navegación
		if(userSession.getNavModel().getPrevAction().contains("BuscarDeudaContrib"))
			return new ActionForward(BaseConstants.FWD_SIAT_BUILD_MENU);
		else
			return baseVolver(mapping, form, request, response, PersonaSearchPage.NAME);
	}
	
}
