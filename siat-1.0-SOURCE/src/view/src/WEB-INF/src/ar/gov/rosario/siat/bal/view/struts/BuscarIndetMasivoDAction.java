//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.bal.iface.model.IndetSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ListUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarIndetMasivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarIndetMasivoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_INDET_MASIVO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			IndetSearchPage indetSearchPageVO = BalServiceLocator.getIndetService().getIndetSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , IndetSearchPage.NAME, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDET_MASIVO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, IndetSearchPage.NAME);
		
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
			IndetSearchPage indetSearchPageVO = (IndetSearchPage) userSession.get(IndetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (indetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + IndetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, IndetSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(indetSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				indetSearchPageVO.setPageNumber(1L);
				indetSearchPageVO.setPaged(false);
			}
			
            // Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
				
			// Validar que al menos se haya cargado algún filtro
			if(!indetSearchPageVO.existeAlgunFiltro()){
				indetSearchPageVO.addRecoverableError(BaseError.MSG_FILTROS_REQUERIDOS);
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			// Llamada al servicio	
			indetSearchPageVO = BalServiceLocator.getIndetService().getIndetSearchPageResult(userSession, indetSearchPageVO);			

			// Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetSearchPage.NAME, indetSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(IndetSearchPage.NAME, indetSearchPageVO);
			// Nuleo el list result

			// Subo en el el searchPage al userSession
			userSession.put(IndetSearchPage.NAME, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDET_MASIVO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, IndetSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, IndetSearchPage.NAME);
		
	}
		
	/*public ActionForward reingresar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			// TODO HACER ACA O SUBIR LA LISTA DE ID SELECCIONADO A SESION
			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_INDET_MASIVO, BalConstants.ACT_REINGRESAR_INDET);
	}*/
	
	
	public ActionForward reingresar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping,BalSecurityConstants.ABM_INDET_MASIVO, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
				
			// Bajo el searchPage del userSession
			IndetSearchPage indetSearchPageVO = (IndetSearchPage) userSession.get(IndetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (indetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + IndetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, IndetSearchPage.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(indetSearchPageVO, request);
			
            // Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			// Validacion sobre seleccion de indeterminados
			if(ListUtil.isNullOrEmpty(indetSearchPageVO.getListIndetVOSelected())){
				indetSearchPageVO.addRecoverableError(BalError.INDET_SELECCIONAR_REGISTRO);
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}
			// llamada al servicio
			/*indetSearchPageVO = BalServiceLocator.getIndetService().getIndetMasivoForReingreso(userSession, indetSearchPageVO);
			
            // Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				// En lugar de llamar al 'forwardErrorRecoverable' realizamos las ultimas acciones que se realizan en este metodo, excluyendo el limpiado de la lista de resultados.
				request.setAttribute(IndetSearchPage.NAME, indetSearchPageVO);
				return mapping.getInputForward();
				//return mapping.findForward(BalConstants.FWD_INDET_MASIVO_SEARCHPAGE;
			}
			
			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetSearchPage.NAME, indetSearchPageVO);
			}*/
			
			// Envio el VO al request
			request.setAttribute(IndetSearchPage.NAME, indetSearchPageVO);

			indetSearchPageVO.addMessage(BalError.MSG_REINGRESO_MASIVO_INDET);

			userSession.put(IndetSearchPage.NAME, indetSearchPageVO);
			
			saveDemodaMessages(request, indetSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_INDET_MASIVO_REINGRESO);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
	
	public ActionForward confirmarReingreso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			IndetSearchPage indetSearchPageVO = (IndetSearchPage) userSession.get(IndetSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (indetSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + IndetSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, IndetSearchPage.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			/*DemodaUtil.populateVO(indetSearchPageVO, request);
			
            // Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, IndetSearchPage.NAME, indetSearchPageVO);
			}*/
			
			// llamada al servicio
			indetSearchPageVO = BalServiceLocator.getIndetService().reingresoMasivo(userSession, indetSearchPageVO);
			
            // Tiene errores recuperables
			if (indetSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + indetSearchPageVO.infoString()); 
				saveDemodaErrors(request, indetSearchPageVO);
				// En lugar de llamar al 'forwardErrorRecoverable' realizamos las ultimas acciones que se realizan en este metodo, excluyendo el limpiado de la lista de resultados.
				request.setAttribute(IndetSearchPage.NAME, indetSearchPageVO);
				return mapping.getInputForward();
				//return mapping.findForward(BalConstants.FWD_INDET_MASIVO_SEARCHPAGE;
			}
			
			// Tiene errores no recuperables
			if (indetSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + indetSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, IndetSearchPage.NAME, indetSearchPageVO);
			}
			
			userSession.getNavModel().setConfAction(BalConstants.PATH_BUSCAR_INDET_MASIVO);
			userSession.getNavModel().setConfActionParameter(BaseConstants.ACT_INICIALIZAR);
			
			// Envio el VO al request
			request.setAttribute(IndetSearchPage.NAME, indetSearchPageVO);

			userSession.remove(IndetSearchPage.NAME);
			
			return forwardMessage(mapping, userSession.getNavModel(), NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, 
					BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
}
