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

import ar.gov.rosario.siat.bal.iface.model.OtrIngTesSearchPage;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.UserSession;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public class BuscarOtrIngTesDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarOtrIngTesDAction.class);
	public final static String OTRINGTES_SEARCHPAGE_FILTRO = "otrIngTesSPFiltro";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, act);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			OtrIngTesSearchPage otrIngTesSearchPageFiltro = (OtrIngTesSearchPage) userSession.getNavModel().getParameter(BuscarOtrIngTesDAction.OTRINGTES_SEARCHPAGE_FILTRO);
			
			OtrIngTesSearchPage otrIngTesSearchPageVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesSearchPageInit(userSession, otrIngTesSearchPageFiltro);
			
			// Tiene errores recuperables
			if (otrIngTesSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + otrIngTesSearchPageVO.infoString()); 
				saveDemodaErrors(request, otrIngTesSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (otrIngTesSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + otrIngTesSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}
			
			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession , OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			
			if(otrIngTesSearchPageFiltro != null){
				// Setea el PageNumber del PageModel				
				otrIngTesSearchPageVO.setPageNumber(1L);
				
				// Llamada al servicio	
				otrIngTesSearchPageVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesSearchPageResult(userSession, otrIngTesSearchPageVO);			

				// Tiene errores recuperables
				if (otrIngTesSearchPageVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + otrIngTesSearchPageVO.infoString()); 
					saveDemodaErrors(request, otrIngTesSearchPageVO);
					return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
				}
				
				// Tiene errores no recuperables
				if (otrIngTesSearchPageVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + otrIngTesSearchPageVO.errorString());
					return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
				}
				
				// Envio el VO al request
				request.setAttribute(OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}
			
			return mapping.findForward(BalConstants.FWD_OTRINGTES_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OtrIngTesSearchPage.NAME);
		}
	}
	
	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return this.baseRefill(mapping, form, request, response, funcName, OtrIngTesSearchPage.NAME);

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
			OtrIngTesSearchPage otrIngTesSearchPageVO = (OtrIngTesSearchPage) userSession.get(OtrIngTesSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (otrIngTesSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OtrIngTesSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(otrIngTesSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				otrIngTesSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
				
				// Verificamos si se tildo el checkList para 'Solo mostrar registros con errores en su distribucion'
				String filtroDistribucionErronea = request.getParameter("filtroDistribucionErronea");
				if(filtroDistribucionErronea != null)
					otrIngTesSearchPageVO.setFiltroDistribucionErronea(true);
				else
					otrIngTesSearchPageVO.setFiltroDistribucionErronea(false);
			}
			
            // Tiene errores recuperables
			if (otrIngTesSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + otrIngTesSearchPageVO.infoString()); 
				saveDemodaErrors(request, otrIngTesSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}

			// Llamada al servicio	
			otrIngTesSearchPageVO = BalServiceLocator.getOtroIngresoTesoreriaService().getOtrIngTesSearchPageResult(userSession, otrIngTesSearchPageVO);			

			// Tiene errores recuperables
			if (otrIngTesSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + otrIngTesSearchPageVO.infoString()); 
				saveDemodaErrors(request, otrIngTesSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (otrIngTesSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + otrIngTesSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			
			return mapping.findForward(BalConstants.FWD_OTRINGTES_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OtrIngTesSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardVerSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTES);
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardAgregarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTES);
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardModificarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTES);	
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarSearchPage(mapping, request, funcName, BalConstants.ACTION_ADMINISTRAR_OTRINGTES);
			
		}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseSeleccionar(mapping, request, response, funcName, OtrIngTesSearchPage.NAME);
	}	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			return baseVolver(mapping, form, request, response, OtrIngTesSearchPage.NAME);
			
	}
	
	
	public ActionForward distribuir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage (mapping, request, funcName, BalConstants.FWD_OTRINGTES_DIS_IMP, BalConstants.ACT_DISTRIBUIR);
	}

	public ActionForward imputar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage (mapping, request, funcName, BalConstants.FWD_OTRINGTES_DIS_IMP, BalConstants.ACT_IMPUTAR);
	}

	public ActionForward generarRecibo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardSearchPage (mapping, request, funcName, BalConstants.FWD_OTRINGTES_RECIBO_PRINT, BalConstants.ACT_GENERAR_RECIBO);
	}
	
	public ActionForward incluir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_OTRINGTES, BaseSecurityConstants.INCLUIR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el SearchPage del userSession
			OtrIngTesSearchPage otrIngTesSearchPageVO = (OtrIngTesSearchPage) userSession.get(OtrIngTesSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (otrIngTesSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + OtrIngTesSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OtrIngTesSearchPage.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(otrIngTesSearchPageVO, request);
			
            // Tiene errores recuperables
			if (otrIngTesSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + otrIngTesSearchPageVO.infoString()); 
				saveDemodaErrors(request, otrIngTesSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}
			
			// llamada al servicio
			otrIngTesSearchPageVO = BalServiceLocator.getFolioTesoreriaService().incluirOtrIngTes(userSession, otrIngTesSearchPageVO);
			
            // Tiene errores recuperables
			if (otrIngTesSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + otrIngTesSearchPageVO.infoString()); 
				saveDemodaErrors(request, otrIngTesSearchPageVO);
				// En lugar de llamar al 'forwardErrorRecoverable' realizamos las ultimas acciones que se realizan en este metodo, excluyendo el limpiado de la lista de resultados.
				request.setAttribute(OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
				return mapping.getInputForward();
			}
			
			// Tiene errores no recuperables
			if (otrIngTesSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + otrIngTesSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OtrIngTesSearchPage.NAME, otrIngTesSearchPageVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OtrIngTesSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OtrIngTesSearchPage.NAME);
		}
	}
}
