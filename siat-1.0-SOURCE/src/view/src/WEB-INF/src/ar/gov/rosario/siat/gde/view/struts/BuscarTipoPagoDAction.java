//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.TipoPagoSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarTipoPagoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarTipoPagoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_TIPOPAGO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TipoPagoSearchPage tipoPagoSearchPageVO = GdeServiceLocator.getDefinicionService().getTipoPagoSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (tipoPagoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoPagoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (tipoPagoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TIPOPAGO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, TipoPagoSearchPage.NAME);
		
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
			TipoPagoSearchPage tipoPagoSearchPageVO = (TipoPagoSearchPage) userSession.get(TipoPagoSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (tipoPagoSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + TipoPagoSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipoPagoSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(tipoPagoSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				tipoPagoSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (tipoPagoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoPagoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			}
				
			// Llamada al servicio	
			tipoPagoSearchPageVO = GdeServiceLocator.getDefinicionService().getTipoPagoSearchPageResult(userSession, tipoPagoSearchPageVO);			

			// Tiene errores recuperables
			if (tipoPagoSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipoPagoSearchPageVO.infoString()); 
				saveDemodaErrors(request, tipoPagoSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (tipoPagoSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipoPagoSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(TipoPagoSearchPage.NAME, tipoPagoSearchPageVO);
			
			return mapping.findForward(GdeConstants.FWD_TIPOPAGO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipoPagoSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOPAGO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		// Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOPAGO);
		//return forwardAgregarSearchPage(mapping, request, funcName, ${Modulo}Constants.ACTION_ADMINISTRAR_ENC_${BEAN});
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOPAGO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOPAGO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOPAGO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, GdeConstants.ACTION_ADMINISTRAR_TIPOPAGO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, TipoPagoSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipoPagoSearchPage.NAME);
		
	}
		
	
}
