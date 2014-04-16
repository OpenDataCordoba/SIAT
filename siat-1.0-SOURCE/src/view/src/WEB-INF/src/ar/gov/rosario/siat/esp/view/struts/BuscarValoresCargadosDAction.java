//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.esp.iface.model.ValoresCargadosSearchPage;
import ar.gov.rosario.siat.esp.iface.service.EspServiceLocator;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import ar.gov.rosario.siat.esp.view.util.EspConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarValoresCargadosDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarValoresCargadosDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, EspSecurityConstants.ABM_VALORESCARGADOS, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			ValoresCargadosSearchPage valoresCargadosSearchPageVO = EspServiceLocator.getHabilitacionService().getValoresCargadosSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (valoresCargadosSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosSearchPageVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (valoresCargadosSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_VALORESCARGADOS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ValoresCargadosSearchPage.NAME);
		
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
			ValoresCargadosSearchPage valoresCargadosSearchPageVO = (ValoresCargadosSearchPage) userSession.get(ValoresCargadosSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (valoresCargadosSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ValoresCargadosSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ValoresCargadosSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(valoresCargadosSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				valoresCargadosSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (valoresCargadosSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosSearchPageVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			}
				
			// Llamada al servicio	
			valoresCargadosSearchPageVO = EspServiceLocator.getHabilitacionService().getValoresCargadosSearchPageResult(userSession, valoresCargadosSearchPageVO);			

			// Tiene errores recuperables
			if (valoresCargadosSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + valoresCargadosSearchPageVO.infoString()); 
				saveDemodaErrors(request, valoresCargadosSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (valoresCargadosSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + valoresCargadosSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			// Nuleo el list result
			//${bean}SearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ValoresCargadosSearchPage.NAME, valoresCargadosSearchPageVO);
			
			return mapping.findForward(EspConstants.FWD_VALORESCARGADOS_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ValoresCargadosSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_VALORESCARGADOS);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		/*Se utiliza uno de los dos return, segun sea un encabezado detalle o no.*/
		return forwardAgregarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_VALORESCARGADOS);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_VALORESCARGADOS);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_VALORESCARGADOS);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_VALORESCARGADOS);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, EspConstants.ACTION_ADMINISTRAR_VALORESCARGADOS);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ValoresCargadosSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ValoresCargadosSearchPage.NAME);
		
	}
		
	
}
