//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.ContribExeSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.pad.iface.model.PersonaSearchPage;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarContribExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarContribExeDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ContribExeSearchPage contribExeSearchPageVO = ExeServiceLocator.getDefinicionService().getContribExeSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (contribExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, contribExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeSearchPage.NAME, contribExeSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (contribExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeSearchPage.NAME, contribExeSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ContribExeSearchPage.NAME, 
				contribExeSearchPageVO);
			return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ContribExeSearchPage.NAME);

	}
	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ContribExeSearchPage contribExeSearchPageVO = (ContribExeSearchPage) userSession.get(ContribExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(contribExeSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				contribExeSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (contribExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, contribExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeSearchPage.NAME, contribExeSearchPageVO);
			}
				
			// Llamada al servicio	
			contribExeSearchPageVO = ExeServiceLocator.getDefinicionService().getContribExeSearchPageResult(userSession, contribExeSearchPageVO);			

			// Tiene errores recuperables
			if (contribExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, contribExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeSearchPage.NAME, contribExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (contribExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeSearchPage.NAME, contribExeSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ContribExeSearchPage.NAME, contribExeSearchPageVO);
			// Nuleo el list result
			//contribExeSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ContribExeSearchPage.NAME, contribExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ContribExeSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ContribExeSearchPage.NAME);
		
	}
	
	public ActionForward buscarContribuyente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		userSession.getNavModel().putParameter(PersonaSearchPage.PARAM_SELECCIONAR_SOLO_CONTRIB, new Boolean(true));

		return forwardSeleccionar(mapping, request, 
			ExeConstants.METOD_CONTRIBEXE_PARAM_CONTRIBUYENTE, PadConstants.ACTION_BUSCAR_PERSONA , false);

	}

	public ActionForward paramContribuyente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			ContribExeSearchPage contribExeSearchPageVO =  (ContribExeSearchPage) 
				userSession.get(ContribExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeSearchPage.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeSearchPage.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ContribExeSearchPage.NAME, contribExeSearchPageVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_SEARCHPAGE); 
			}

			// Seteo el id atributo seleccionado
			contribExeSearchPageVO.getContribExe().getContribuyente().setId(new Long(selectedId));
			
			// llamo al param del servicio
			contribExeSearchPageVO = ExeServiceLocator.getDefinicionService().getContribExeSearchPageParamContribuyente
				(userSession, contribExeSearchPageVO);

            // Tiene errores recuperables
			if (contribExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, contribExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ContribExeSearchPage.NAME, contribExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (contribExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ContribExeSearchPage.NAME, contribExeSearchPageVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, contribExeSearchPageVO);

			// Envio el VO al request
			request.setAttribute(ContribExeSearchPage.NAME, contribExeSearchPageVO);

			return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_SEARCHPAGE);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeSearchPage.NAME);
		}
	}

}
