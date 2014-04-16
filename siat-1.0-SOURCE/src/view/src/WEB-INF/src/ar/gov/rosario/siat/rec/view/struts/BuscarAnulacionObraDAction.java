//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.rec.iface.model.AnulacionObraSearchPage;
import ar.gov.rosario.siat.rec.iface.service.RecServiceLocator;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import ar.gov.rosario.siat.rec.view.util.RecConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarAnulacionObraDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarAnulacionObraDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RecSecurityConstants.ABM_ANULACIONOBRA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		//Chequeo de acceso por instancia
		if (!userSession.getEsAdmin() && SiatParam.isWebSiat() && !SiatParam.isIntranetSiat()) {
			return forwardFuncionNoDisponible(request);
		}

		try {
			
			AnulacionObraSearchPage anulacionObraSearchPageVO = RecServiceLocator.getCdmService().getAnulacionObraSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (anulacionObraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraSearchPageVO.infoString()); 
				saveDemodaErrors(request, anulacionObraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (anulacionObraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_ANULACIONOBRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, AnulacionObraSearchPage.NAME);
		
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
			AnulacionObraSearchPage anulacionObraSearchPageVO = (AnulacionObraSearchPage) userSession.get(AnulacionObraSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (anulacionObraSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + AnulacionObraSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, AnulacionObraSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(anulacionObraSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				anulacionObraSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (anulacionObraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraSearchPageVO.infoString()); 
				saveDemodaErrors(request, anulacionObraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			}
				
			// Llamada al servicio	
			anulacionObraSearchPageVO = RecServiceLocator.getCdmService().getAnulacionObraSearchPageResult(userSession, anulacionObraSearchPageVO);			

			// Tiene errores recuperables
			if (anulacionObraSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + anulacionObraSearchPageVO.infoString()); 
				saveDemodaErrors(request, anulacionObraSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (anulacionObraSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + anulacionObraSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			// Nuleo el list result
			//anulacionObraSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(AnulacionObraSearchPage.NAME, anulacionObraSearchPageVO);
			
			return mapping.findForward(RecConstants.FWD_ANULACIONOBRA_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, AnulacionObraSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ANULACIONOBRA);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		
		return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ANULACIONOBRA);
		//return forwardAgregarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ENC_ANULACIONOBRA);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ANULACIONOBRA);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, RecConstants.ACTION_ADMINISTRAR_ANULACIONOBRA);

	}
	
	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, 
					RecConstants.ACTION_ADMINISTRAR_PROCESO_ANULACIONOBRA, 
					RecConstants.ACT_ADM_PROCESO_ANULACION_OBRA);
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, AnulacionObraSearchPage.NAME);
		
	}
		
}
