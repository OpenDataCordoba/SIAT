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
import ar.gov.rosario.siat.def.iface.model.TipObjImpVO;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.pad.iface.model.ObjImpSearchPage;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarObjImpDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarObjImpDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			// Cargo el parametro del tipObjImp seleccionado en el searchPage
			// Si hay valor para este parametro entnoces el combo de tipObjImp se pone con este valor y en readonly
			TipObjImpVO tipObjImpReadOnly = (TipObjImpVO) userSession.getNavModel().getParameter(ObjImpSearchPage.PARAM_TIPOBJIMP_READONLY);
			ObjImpSearchPage objImpSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpSearchPageInit(userSession, tipObjImpReadOnly);
			
			// Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (objImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ObjImpSearchPage.NAME, objImpSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ObjImpSearchPage.NAME);
		
	}
	
	public ActionForward limpiarAva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return this.buscarAvaInit(mapping, form, request, response);
			
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
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(objImpSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				objImpSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
				
			// Llamada al servicio	
			objImpSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpSearchPageResult(userSession, objImpSearchPageVO);			

			// Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (objImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ObjImpSearchPage.NAME, objImpSearchPageVO);
			// Nuleo el list result
			//objImpSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ObjImpSearchPage.NAME, objImpSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}

	public ActionForward buscarAva(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				// Bajo el searchPage del userSession
				ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
				
				// Si es nulo no se puede continuar
				if (objImpSearchPageVO == null) {
					log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
				}
				
				// si el buscar diparado desde la pagina de busqueda
				if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
					// Recuperamos datos del form en el vo

					// Metodo auxiliar para loguear todo el request					
					DefinitionUtil.requestValues(request);
					//Map<String,String> mapFiltros = DefinitionUtil.requestToHashMap(request);
					
					for(TipObjImpAtrDefinition itemDefinition: objImpSearchPageVO.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
						//itemDefinition.populateAtrVal4Busqueda(mapFiltros);
						DefinitionUtil.populateAtrVal4Busqueda(itemDefinition, request);
					}
					
					objImpSearchPageVO.getTipObjImpDefinition().clearError();
					objImpSearchPageVO.getTipObjImpDefinition().validate();
					
					DemodaUtil.populateVO(objImpSearchPageVO, request);
					// Setea el PageNumber del PageModel				
					objImpSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
					
				}
				
	            // Tiene errores recuperables
				if (objImpSearchPageVO.getTipObjImpDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
					//saveDemodaErrors(request, objImpSearchPageVO);
					saveDemodaErrors(request, objImpSearchPageVO.getTipObjImpDefinition());
					request.setAttribute(ObjImpSearchPage.NAME, objImpSearchPageVO);
					return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGEAVA);
				}
					
				// Llamada al servicio	
				objImpSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpSearchPageResultAva(userSession, objImpSearchPageVO);			

				// Tiene errores recuperables
				if (objImpSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
					
					// limpia la lista de resultados y inicializa el nro de pag de busq en cero.
					objImpSearchPageVO.setListResult(new ArrayList());
					objImpSearchPageVO.setPageNumber(0L);
					
					saveDemodaErrors(request, objImpSearchPageVO);
					request.setAttribute(ObjImpSearchPage.NAME, objImpSearchPageVO);
					return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGEAVA);
				}
				
				// Tiene errores no recuperables
				if (objImpSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(ObjImpSearchPage.NAME, objImpSearchPageVO);
				// Nuleo el list result
				//objImpSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
				// Subo en el el searchPage al userSession
				userSession.put(ObjImpSearchPage.NAME, objImpSearchPageVO);
				
				return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGEAVA);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
			}
		}
	
	public ActionForward buscarAvaInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ObjImpSearchPage objImpSearchPageVO =  (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (objImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Seteo bandera para que los metodos ver, agregar, modificar, etc, sepan a donde volver
			objImpSearchPageVO.setEsBusquedaAvanzada(true);
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ObjImpSearchPage.NAME, objImpSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGEAVA);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}
	
	public ActionForward buscarSimpleInit(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ObjImpSearchPage objImpSearchPageVO =  (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (objImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Seteo bandera para que los metodos ver, agregar, modificar, etc, sepan a donde volver
			objImpSearchPageVO.setEsBusquedaAvanzada(true);
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ObjImpSearchPage.NAME, objImpSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
		
	}
	
	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String actVolver = "";
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			if (objImpSearchPageVO.getEsBusquedaAvanzada()){
				actVolver = BaseConstants.ACT_BUSCAR_AVANZADO;
			} else {
				actVolver = BaseConstants.ACT_BUSCAR;
			}
		
			return baseForward(mapping, request, funcName, actVolver, PadConstants.ACTION_ADMINISTRAR_OBJIMP, BaseConstants.ACT_VER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}
	

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String actVolver = "";
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			if (objImpSearchPageVO.getEsBusquedaAvanzada()){
				actVolver = BaseConstants.ACT_BUSCAR_AVANZADO;
			} else {
				actVolver = BaseConstants.ACT_BUSCAR;
			}
		
			return baseForward(mapping, request, funcName, actVolver, PadConstants.ACTION_ADMINISTRAR_OBJIMP, BaseConstants.ACT_AGREGAR);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}
	

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String actVolver = "";
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			if (objImpSearchPageVO.getEsBusquedaAvanzada()){
				actVolver = BaseConstants.ACT_BUSCAR_AVANZADO;
			} else {
				actVolver = BaseConstants.ACT_BUSCAR;
			}
		
			return baseForward(mapping, request, funcName, actVolver, PadConstants.ACTION_ADMINISTRAR_OBJIMP, BaseConstants.ACT_MODIFICAR);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}

	}
	

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String actVolver = "";
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			if (objImpSearchPageVO.getEsBusquedaAvanzada()){
				actVolver = BaseConstants.ACT_BUSCAR_AVANZADO;
			} else {
				actVolver = BaseConstants.ACT_BUSCAR;
			}
		
			return baseForward(mapping, request, funcName, actVolver, PadConstants.ACTION_ADMINISTRAR_OBJIMP, BaseConstants.ACT_ELIMINAR);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}

	}
	
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String actVolver = "";
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			if (objImpSearchPageVO.getEsBusquedaAvanzada()){
				actVolver = BaseConstants.ACT_BUSCAR_AVANZADO;
			} else {
				actVolver = BaseConstants.ACT_BUSCAR;
			}
		
			return baseForward(mapping, request, funcName, actVolver, PadConstants.ACTION_ADMINISTRAR_OBJIMP, BaseConstants.ACT_ACTIVAR);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}		
	}
	
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String actVolver = "";
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}
			
			if (objImpSearchPageVO.getEsBusquedaAvanzada()){
				actVolver = BaseConstants.ACT_BUSCAR_AVANZADO;
			} else {
				actVolver = BaseConstants.ACT_BUSCAR;
			}
		
			return baseForward(mapping, request, funcName, actVolver, PadConstants.ACTION_ADMINISTRAR_OBJIMP, BaseConstants.ACT_DESACTIVAR);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}
	
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ObjImpSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObjImpSearchPage.NAME);
		
	}
	
		
	public ActionForward paramTipObjImp (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpSearchPageVO, request);
			
            // Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// llamada al servicio
			objImpSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpSearchPageParamTipObjImp(userSession, objImpSearchPageVO);
			
            // Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (objImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Vuelvo el PAGENUMBER a 0 para evitar que el viewresult sea false
			objImpSearchPageVO.setPageNumber(new Long(0)); 
			// Envio el VO al request
			request.setAttribute(ObjImpSearchPage.NAME, objImpSearchPageVO);
			// Subo el searchpage al userSession
			userSession.put(ObjImpSearchPage.NAME, objImpSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}
	
	
	public ActionForward paramTipObjImpAva (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			ObjImpSearchPage objImpSearchPageVO = (ObjImpSearchPage) userSession.get(ObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpSearchPageVO, request);
			
            // Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// llamada al servicio
			objImpSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpSearchPageParamTipObjImp(userSession, objImpSearchPageVO);
			
            // Tiene errores recuperables
			if (objImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, objImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (objImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpSearchPage.NAME, objImpSearchPageVO);
			}
			
			// Vuelvo el PAGENUMBER a 0 para evitar que el viewresult sea false
			objImpSearchPageVO.setPageNumber(new Long(0)); 
			// Envio el VO al request
			request.setAttribute(ObjImpSearchPage.NAME, objImpSearchPageVO);
			// Subo el searchpage al userSession
			userSession.put(ObjImpSearchPage.NAME, objImpSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_SEARCHPAGEAVA);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpSearchPage.NAME);
		}
	}
	
}
