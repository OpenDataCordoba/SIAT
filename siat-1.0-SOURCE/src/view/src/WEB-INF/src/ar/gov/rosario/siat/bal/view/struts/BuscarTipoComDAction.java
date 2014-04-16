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

import ar.gov.rosario.siat.bal.iface.model.TipoComSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoComDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoComDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_TIPOCOM, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoComSearchPage tipoComSearchPageVO = BalServiceLocator.getCompensacionService().getTipoComSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComSearchPage.NAME, tipoComSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoComSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComSearchPage.NAME, tipoComSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoComSearchPage.NAME, tipoComSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPOCOM_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoComSearchPage.NAME);
		
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
			TipoComSearchPage tipoComSearchPageVO = (TipoComSearchPage) userSession.get(TipoComSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoComSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoComSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoComSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoComSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoComSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComSearchPage.NAME, tipoComSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoComSearchPageVO = BalServiceLocator.getCompensacionService().getTipoComSearchPageResult(userSession, tipoComSearchPageVO);			

			// Tiene errores recuperables
			if (tipoComSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoComSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoComSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoComSearchPage.NAME, tipoComSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoComSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoComSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoComSearchPage.NAME, tipoComSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoComSearchPage.NAME, tipoComSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(TipoComSearchPage.NAME, tipoComSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_TIPOCOM_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoComSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOM);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		// Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOM);
		//return forwardAgregarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_ENC_${BEAN});
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOM);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOM);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOM);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_TIPOCOM);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoComSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoComSearchPage.NAME);
		
	}
		
	
}
