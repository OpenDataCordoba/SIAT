//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.pad.iface.model.CierreComercioSearchPage;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.PrintModel;

public final class BuscarCierreComercioDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCierreComercioDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CIERRECOMERCIO, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			CierreComercioSearchPage cierreComercioSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getCierreComercioSearchPageInit(userSession);
			
			// Tiene errores recuperables
			if (cierreComercioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioSearchPageVO.infoString()); 
				saveDemodaErrors(request, cierreComercioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cierreComercioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreComercioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CIERRECOMERCIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CierreComercioSearchPage.NAME);
		
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
			CierreComercioSearchPage cierreComercioSearchPageVO = (CierreComercioSearchPage) userSession.get(CierreComercioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cierreComercioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cierreComercioSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				cierreComercioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (cierreComercioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioSearchPageVO.infoString()); 
				saveDemodaErrors(request, cierreComercioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			}
				
			// Llamada al servicio	
			cierreComercioSearchPageVO = PadServiceLocator.getPadObjetoImponibleService().getCierreComercioSearchPageResult(userSession, cierreComercioSearchPageVO);			

			// Tiene errores recuperables
			if (cierreComercioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cierreComercioSearchPageVO.infoString()); 
				saveDemodaErrors(request, cierreComercioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cierreComercioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cierreComercioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			// Nuleo el list result
			//cierreComercioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CierreComercioSearchPage.NAME, cierreComercioSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_CIERRECOMERCIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CIERRECOMERCIO);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		//Se utiliza uno de los dos return, segun sea un encabezado detalle o no.
		return forwardAgregarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CIERRECOMERCIO);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CIERRECOMERCIO);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CIERRECOMERCIO);

	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardActivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CIERRECOMERCIO);			
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return forwardDesactivarSearchPage(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_CIERRECOMERCIO);
	}
	
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CierreComercioSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CierreComercioSearchPage.NAME);
		
	}

	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CierreComercioSearchPage adapterVO = (CierreComercioSearchPage)userSession.get(CierreComercioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + CierreComercioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CierreComercioSearchPage.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getCierreComercio().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCierreComercio()); 
			
			adapterVO.getCierreComercio().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(CierreComercioSearchPage.NAME, adapterVO);
			
			return mapping.findForward(PadConstants.FWD_CIERRECOMERCIO_SEARCHPAGE); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CierreComercioSearchPage.NAME);
		}	
	}

	public ActionForward irImprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_CIERRECOMERCIO, PadConstants.ACT_CIERRECOMERCIO_IMPRIMIR);
	
		//UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		
		try {
			//CierreComercioAdapter cierreComercioAdapterVO = (CierreComercioAdapter) userSession.get(CierreComercioAdapter.NAME);
			
			//cierreComercioAdapterVO.getCierreComercio().getCuentaVO().getId();
			
			
			//Long cuentaId = new Long(request.getParameter("cuentaId"));
			
			Long cuentaId = new Long(request.getParameter("selectedId"));
			
			PrintModel print = GdeServiceLocator.getGestionDeudaService().imprimirCierreComercio(userSession, cuentaId);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, LiqDeudaAdapter.NAME);
		}
	}
	
}
