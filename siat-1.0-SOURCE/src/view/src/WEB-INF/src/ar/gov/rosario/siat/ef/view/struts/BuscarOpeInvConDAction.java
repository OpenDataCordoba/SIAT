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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.buss.bean.OpeInvBus;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConSearchPage;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;

public final class BuscarOpeInvConDAction extends BaseDispatchAction {

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
			CommonKey commonKey = new CommonKey(userSession.getNavModel().getSelectedId());
			
			OpeInvConSearchPage opeInvConSearchPage = new OpeInvConSearchPage();
			baseInicializarSearchPage(mapping, request, userSession, OpeInvConSearchPage.NAME, opeInvConSearchPage);
			opeInvConSearchPage.setPageNumber(1L);
			opeInvConSearchPage.setRecsByPage(20L);			
			
			opeInvConSearchPage.getOpeInvCon().getOpeInv().setId(commonKey.getId());
			OpeInvConSearchPage opeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConSearchPageResult(userSession, opeInvConSearchPage);
			
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
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);

			return mapping.findForward(EfConstants.FWD_OPEINVCON_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, OpeInvConSearchPage.NAME);
		
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
				log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
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
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			}
				
			// Llamada al servicio	
			opeInvConSearchPageVO = EfServiceLocator.getInvestigacionService().getOpeInvConSearchPageResult(userSession, opeInvConSearchPageVO);			

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
		
			//seteo para que se posicione en la busqueda
			request.setAttribute("irA", "busqueda");
			
			// Envio el VO al request
			request.setAttribute(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OpeInvConSearchPage.NAME, opeInvConSearchPageVO);
			
			return mapping.findForward(EfConstants.FWD_OPEINVCON_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVCON);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
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
				log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// Setea el id de opeInv en la session
			userSession.getNavModel().putParameter("idOpeInv", opeInvConSearchPageVO.getOpeInvCon().getOpeInv().getId());
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConSearchPage.NAME);
		}
		
		return baseForward(mapping, request, funcName, BaseConstants.ACT_BUSCAR, EfConstants.ACTION_ADMINISTRAR_OPEINVCON, BaseConstants.ACT_AGREGAR);		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVCON);

	}

	public ActionForward excluirDeSelec(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardSearchPage(mapping, request,funcName,EfConstants.ACTION_ADMINISTRAR_OPEINVCON, 
																			EfConstants.ACT_EXCLUIR_DE_SELEC);
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVCON);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVCON);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, OpeInvConSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, OpeInvConSearchPage.NAME);
		
	}

	public ActionForward agregarMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			// Bajo el searchPage del userSession
			OpeInvConSearchPage opeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// Setea el tipo de busqueda AGREGAR
			userSession.put("tipBus", OpeInvBus.TIPO_AGREGAR);
			
			// Setea el id de opeInv en la session
			userSession.put("idOpeInv", opeInvConSearchPageVO.getOpeInvCon().getOpeInv().getId());

			return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);		
	}

	public ActionForward eliminarMasivo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);

			// Bajo el searchPage del userSession
			OpeInvConSearchPage opeInvConSearchPageVO = (OpeInvConSearchPage) userSession.get(OpeInvConSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConSearchPage.NAME); 
			}
			
			// Setea el tipo de busqueda AGREGAR
			userSession.put("tipBus", OpeInvBus.TIPO_ELIMINAR);
			
			// Setea el id de opeInv en la session
			userSession.put("idOpeInv", opeInvConSearchPageVO.getOpeInvCon().getOpeInv().getId());

			return forwardAgregarSearchPage(mapping, request, funcName, EfConstants.ACTION_ADMINISTRAR_OPEINVBUS);		
	}
}
