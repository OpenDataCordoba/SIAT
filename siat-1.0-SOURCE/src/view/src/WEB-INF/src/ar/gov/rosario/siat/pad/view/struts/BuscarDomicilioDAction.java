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
import ar.gov.rosario.siat.pad.iface.model.DomicilioSearchPage;
import ar.gov.rosario.siat.pad.iface.model.DomicilioVO;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

public class BuscarDomicilioDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(BuscarDomicilioDAction.class);
	
	public final static String DOMICILIO = "domicilio";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		//String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		//UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_DOMICILIO, act);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {

			DomicilioVO domicilioFiltro = (DomicilioVO) userSession.getNavModel().getParameter(BuscarDomicilioDAction.DOMICILIO);
			
			DomicilioSearchPage domicilioSearchPageVO = PadServiceLocator.getPadUbicacionService().getDomicilioSearchPageInit(userSession, domicilioFiltro);
			
			// Tiene errores recuperables
			if (domicilioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioSearchPageVO.infoString()); 
				saveDemodaErrors(request, domicilioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioSearchPage.NAME, domicilioSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (domicilioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioSearchPage.NAME, domicilioSearchPageVO);
			}

			// Si no tiene error
			baseInicializarSearchPage(mapping, request, userSession, DomicilioSearchPage.NAME, domicilioSearchPageVO);

			return mapping.findForward(PadConstants.FWD_DOMICILIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, DomicilioSearchPage.NAME);

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
			DomicilioSearchPage domicilioSearchPageVO = (DomicilioSearchPage) userSession.get(DomicilioSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (domicilioSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + DomicilioSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DomicilioSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(domicilioSearchPageVO, request);
				// Setea el PageNumber del PageModel no hace falta porque la busqueda no pagina				
				//domicilioSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (domicilioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioSearchPageVO.infoString()); 
				saveDemodaErrors(request, domicilioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioSearchPage.NAME, domicilioSearchPageVO);
			}
				
			// Llamada al servicio	
			domicilioSearchPageVO = PadServiceLocator.getPadUbicacionService().getDomicilioSearchPageResult(userSession, domicilioSearchPageVO);			

			// Tiene errores recuperables
			if (domicilioSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + domicilioSearchPageVO.infoString()); 
				saveDemodaErrors(request, domicilioSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, DomicilioSearchPage.NAME, domicilioSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (domicilioSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + domicilioSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, DomicilioSearchPage.NAME, domicilioSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(DomicilioSearchPage.NAME, domicilioSearchPageVO);
			// Nuleo el list result
			//domicilioSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(DomicilioSearchPage.NAME, domicilioSearchPageVO);
			
			return mapping.findForward(PadConstants.FWD_DOMICILIO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DomicilioSearchPage.NAME);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		// composicion del domicilioVO a partir de los valores submitidos recibidos como parametros en el request
		DomicilioVO domicilioVO = new DomicilioVO();
				
		domicilioVO.setId(new Long(request.getParameter("selectedId")));
		domicilioVO.setNumero(new Long(request.getParameter("numero")));
		domicilioVO.getCalle().setNombreCalle(StringUtil.cut(request.getParameter("nombreCalle")));				
		domicilioVO.setLetraCalle(StringUtil.cut(request.getParameter("letraCalle")));
		domicilioVO.setBis(SiNo.getSiNoByAbreviatura(request.getParameter("bis")));
		
		request.setAttribute(DomicilioVO.NAME, domicilioVO); 
		return baseSeleccionar(mapping, request, response, funcName, DomicilioSearchPage.NAME);
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DomicilioSearchPage.NAME);
		
	}
	
}
