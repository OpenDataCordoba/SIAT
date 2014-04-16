//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.model.TipObjImpSearchPage;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipObjImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipObjImpDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE, 
				BaseSecurityConstants.BUSCAR ); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipObjImpSearchPage tipObjImpSearchPageVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipObjImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipObjImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipObjImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_TIPOBJIMP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipObjImpSearchPage.NAME);
		
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
			TipObjImpSearchPage tipObjImpSearchPageVO = (TipObjImpSearchPage) userSession.get(TipObjImpSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipObjImpSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipObjImpSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipObjImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipObjImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			}
				
			// Llamada al servicio	
			tipObjImpSearchPageVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpSearchPageResult(userSession, tipObjImpSearchPageVO);			

			// Tiene errores recuperables
			if (tipObjImpSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipObjImpSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(TipObjImpSearchPage.NAME, tipObjImpSearchPageVO);
			
			return mapping.findForward(DefConstants.FWD_TIPOBJIMP_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMP);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_ENCTIPOBJIMP);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMP);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMP);

	}

	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardActivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMP);			
		}
		
		public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			return forwardDesactivarSearchPage(mapping, request, funcName, DefConstants.ACTION_ADMINISTRAR_TIPOBJIMP);
		}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipObjImpSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipObjImpSearchPage.NAME);
		
	}
	
	public ActionForward admAreas(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		return baseForwardSearchPage(mapping, request, funcName, 
				 DefConstants.ACTION_ADMINISTRAR_TIPOBJIMP,DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ADM_AREA_ORIGEN );		
	}
	

}