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
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarOpeInvBusDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOpeInvConDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_OPEINVCON, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			//obtiene el idOpeInv de la session
			Long idOpeInv = (Long) userSession.get("idOpeInv");
			Integer tipBus = (Integer) userSession.get("tipBus");
			
			OpeInvBusSearchPage opeInvBusSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvBusSearchPageInit(userSession, idOpeInv, tipBus);
			
			// Tiene errores recuperables
			if (opeInvBusSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvBusSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (opeInvBusSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			}
			
			baseInicializarSearchPage(mapping, request, userSession, OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);

			// Envio el VO al request
			request.setAttribute(OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);

			return mapping.findForward(EfConstants.FWD_OPEINVBUS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvBusSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OpeInvBusSearchPage.NAME);
		
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
			OpeInvBusSearchPage opeInvBusSearchPageVO = (OpeInvBusSearchPage) userSession.get(OpeInvBusSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvBusSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvBusSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvBusSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvBusSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				opeInvBusSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (opeInvBusSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvBusSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			}
				
			// Llamada al servicio	
			opeInvBusSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvBusSearchPageResult(userSession, opeInvBusSearchPageVO);			

			// Tiene errores recuperables
			if (opeInvBusSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusSearchPageVO.infoString()); 
				saveDemodaErrors(request, opeInvBusSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvBusSearchPage.NAME, opeInvBusSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_OPEINVBUS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvBusSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);

		}

	public ActionForward administrarProceso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage(mapping, request, funcName, EfConstants.ACT_ADM_PROCESO_OPEINVBUS, EfConstants.ACT_ADM_PROCESO_OPEINVBUS);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OpeInvBusSearchPage.NAME);
		
	}
		
	
}
