//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.exe.iface.model.MarcaCueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class BuscarMarcaCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarMarcaCueExeDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);			
		
		try {
			
			MarcaCueExeSearchPage marcaCueExeSearchPageVO = new MarcaCueExeSearchPage();
							
			marcaCueExeSearchPageVO = ExeServiceLocator.getExencionService().getMarcaCueExeSearchPageInit(userSession, marcaCueExeSearchPageVO);
			
			// Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (marcaCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + marcaCueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
			
			// Si no tiene error			
			baseInicializarSearchPage(mapping, request, userSession , MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_MARCA_CUEEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MarcaCueExeSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, MarcaCueExeSearchPage.NAME);
		
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
			MarcaCueExeSearchPage marcaCueExeSearchPageVO = (MarcaCueExeSearchPage) userSession.get(MarcaCueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (marcaCueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + MarcaCueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MarcaCueExeSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(marcaCueExeSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				marcaCueExeSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
				
			// Llamada al servicio	
			marcaCueExeSearchPageVO = ExeServiceLocator.getExencionService().getMarcaCueExeSearchPageResult(userSession, marcaCueExeSearchPageVO);			

			// Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (marcaCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + marcaCueExeSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			// Nuleo el list result
			//marcaCueExeSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_MARCA_CUEEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MarcaCueExeSearchPage.NAME);
		}
	}

		
	public ActionForward marcarCueExe(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			MarcaCueExeSearchPage marcaCueExeSearchPageVO = (MarcaCueExeSearchPage) userSession.get(MarcaCueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (marcaCueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + MarcaCueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MarcaCueExeSearchPage.NAME); 
			}
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(marcaCueExeSearchPageVO, request);
	
			if(request.getParameter("idsSelected")==null){
				marcaCueExeSearchPageVO.addRecoverableError(ExeError.MARCA_CUEEXE_NINGUNA_SELECTED);				
			}
			
            // Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString());
				// Envio el VO al request
				request.setAttribute(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);

				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return mapping.findForward(ExeConstants.FWD_MARCA_CUEEXE_SEARCHPAGE);
			}
				
			// Llamada al servicio	
			marcaCueExeSearchPageVO = ExeServiceLocator.getExencionService().getMarcarCueExe(userSession, marcaCueExeSearchPageVO);			

			// Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString());
				// Envio el VO al request
				request.setAttribute(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
				// Subo en el el searchPage al userSession
				userSession.put(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);

				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return mapping.findForward(ExeConstants.FWD_MARCA_CUEEXE_SEARCHPAGE);
			}
			
			// Tiene errores no recuperables
			if (marcaCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + marcaCueExeSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			// Subo en el el searchPage al userSession
			userSession.put(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			
			return forwardConfirmarOk(mapping, request, funcName, MarcaCueExeSearchPage.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MarcaCueExeSearchPage.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, MarcaCueExeSearchPage.NAME);
		
	}
	
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/exe/BuscarCueExe");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);
		
		// seteo el recurso TGI como filtro
		MarcaCueExeSearchPage  marcaCueExeSearchPage = (MarcaCueExeSearchPage) userSession.get(MarcaCueExeSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (marcaCueExeSearchPage == null) {
			log.error("error en: "  + funcName + ": " + MarcaCueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, MarcaCueExeSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(marcaCueExeSearchPage, request);
		
        // Tiene errores recuperables
		if (marcaCueExeSearchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPage.infoString()); 
			saveDemodaErrors(request, marcaCueExeSearchPage);
			return forwardErrorRecoverable(mapping, request, userSession, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPage);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		//cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(cueExeSearchPage.getRecursoTGI());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(marcaCueExeSearchPage.getCueExe().getCuenta().getNumeroCuenta());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MarcaCueExeSearchPage marcaCueExeSearchPageVO = (MarcaCueExeSearchPage) userSession.get(MarcaCueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (marcaCueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + MarcaCueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MarcaCueExeSearchPage.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			marcaCueExeSearchPageVO.getCueExe().getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			marcaCueExeSearchPageVO = ExeServiceLocator.getExencionService().getMarcaCueExeSearchPageParamCuenta(userSession, marcaCueExeSearchPageVO);
			
            // Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (marcaCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + marcaCueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_MARCA_CUEEXE_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MarcaCueExeSearchPage.NAME);
		}
	}
	
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			MarcaCueExeSearchPage marcaCueExeSearchPageVO = (MarcaCueExeSearchPage) userSession.get(MarcaCueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (marcaCueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + MarcaCueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, MarcaCueExeSearchPage.NAME); 
			}
			
			DemodaUtil.populateVO(marcaCueExeSearchPageVO, request);

			// llamada al servicio
			marcaCueExeSearchPageVO = ExeServiceLocator.getExencionService().getMarcaCueExeSearchPageParamRecurso(userSession, marcaCueExeSearchPageVO);
			
            // Tiene errores recuperables
			if (marcaCueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + marcaCueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, marcaCueExeSearchPageVO);
				return forwardErrorRecoverable
					(mapping, request, userSession, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (marcaCueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + marcaCueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable
					(mapping, request, funcName, MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			}

			marcaCueExeSearchPageVO.setPageNumber(0L);
			
			// Envio el VO al request
			request.setAttribute(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(MarcaCueExeSearchPage.NAME, marcaCueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_MARCA_CUEEXE_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, MarcaCueExeSearchPage.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, MarcaCueExeSearchPage.NAME);
		
	}
	
}
