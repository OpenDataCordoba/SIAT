//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.afi.iface.model.ForDecJurSearchPage;
import ar.gov.rosario.siat.afi.iface.service.AfiServiceLocator;
import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.afi.view.util.AfiConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarForDecJurDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarForDecJurDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, AfiSecurityConstants.ABM_FORDECJUR, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ForDecJurSearchPage forDecJurSearchPageVO = AfiServiceLocator.getFormulariosDJService().getForDecJurSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (forDecJurSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forDecJurSearchPageVO.infoString()); 
				saveDemodaErrors(request, forDecJurSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (forDecJurSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forDecJurSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			
			return mapping.findForward(AfiConstants.FWD_FORDECJUR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForDecJurSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ForDecJurSearchPage.NAME);
		
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
			ForDecJurSearchPage forDecJurSearchPageVO = (ForDecJurSearchPage) userSession.get(ForDecJurSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (forDecJurSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ForDecJurSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ForDecJurSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(forDecJurSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				forDecJurSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (forDecJurSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forDecJurSearchPageVO.infoString()); 
				saveDemodaErrors(request, forDecJurSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			}
			
			// Llamada al servicio	
			forDecJurSearchPageVO = AfiServiceLocator.getFormulariosDJService().getForDecJurSearchPageResult(userSession, forDecJurSearchPageVO);			

			// Tiene errores recuperables
			if (forDecJurSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + forDecJurSearchPageVO.infoString()); 
				saveDemodaErrors(request, forDecJurSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (forDecJurSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + forDecJurSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(ForDecJurSearchPage.NAME, forDecJurSearchPageVO);
			
			return mapping.findForward(AfiConstants.FWD_FORDECJUR_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ForDecJurSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return forwardAgregarSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ForDecJurSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ForDecJurSearchPage.NAME);
		
	}
		
	public ActionForward generarDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR, AfiConstants.ACT_GENERAR_DECJUR);
	}
}
