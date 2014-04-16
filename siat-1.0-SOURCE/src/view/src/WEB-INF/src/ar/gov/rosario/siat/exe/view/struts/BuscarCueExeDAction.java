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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.exe.iface.model.CueExeSearchPage;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.CommonNavegableView;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class BuscarCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarCueExeDAction.class);
	
	public static String VER_CUEEXE_ID_CUENTA = "VER_CUEEXE_ID_CUENTA";
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		// Verifica si viene un idExencion preseteado para llenar los combos con eso
		//String idExencion = request.getParameter("idExencion");
		
		String permiteManPad = request.getParameter("permiteManPad");
		
		String securityConstant = StringUtil.isNullOrEmpty(permiteManPad)?ExeSecurityConstants.ABM_CUEEXE:
								ExeSecurityConstants.ABM_CUEEXE_EMI;
		
		
		//verificamos si nos pasaron un idCuenta, si viene NO verificamos permiso,
		//sino es un inicializar comun, y verificamos por los permisos.
		Long idCuenta = (Long) userSession.get(VER_CUEEXE_ID_CUENTA);
		if (idCuenta == null) {
			userSession = canAccess(request, mapping, securityConstant, act); 
			if (userSession == null) return forwardErrorSession(request);
		}
		
		
		try {
			
			CueExeSearchPage cueExeSearchPageVO = new CueExeSearchPage();
			
			cueExeSearchPageVO.getCueExe().getCuenta().setId(idCuenta);
			
			/*if(!StringUtil.isNullOrEmpty(idExencion)){
				cueExeSearchPageVO.setConExencionPreseteada(true);
				cueExeSearchPageVO.getCueExe().getExencion().setId(new Long(idExencion));				
			}*/
			
			
			if(!StringUtil.isNullOrEmpty(permiteManPad)){
				cueExeSearchPageVO.setPermiteManPad(true);
			}
				
			cueExeSearchPageVO = ExeServiceLocator.getExencionService().getCueExeSearchPageInit(userSession, cueExeSearchPageVO);
			
			// Tiene errores recuperables
			if (cueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeSearchPage.NAME, cueExeSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (cueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
			
			// Si no tiene error
			if (idCuenta != null){
				// seteo los valores del navegacion en el pageModel
				cueExeSearchPageVO.setValuesFromNavModel(userSession.getNavModel());
				
				cueExeSearchPageVO.setModoVer(true);
				
				// Envio el VO al request
				request.setAttribute(CueExeSearchPage.NAME, cueExeSearchPageVO);

				// Subo en el el searchPage al userSession
				userSession.put(CueExeSearchPage.NAME, cueExeSearchPageVO);
				
			} else {
			
				baseInicializarSearchPage(mapping, request, userSession , CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, CueExeSearchPage.NAME);
		
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
			CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(cueExeSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				cueExeSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (cueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
				
			// Llamada al servicio	
			cueExeSearchPageVO = ExeServiceLocator.getExencionService().getCueExeSearchPageResult(userSession, cueExeSearchPageVO);			

			// Tiene errores recuperables
			if (cueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(CueExeSearchPage.NAME, cueExeSearchPageVO);
			// Nuleo el list result
			//cueExeSearchPageVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(CueExeSearchPage.NAME, cueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeSearchPage.NAME);
		}
	}

	public ActionForward ver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CUEEXE);

	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, 
				ExeSecurityConstants.ABM_CUEEXE_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);

		// Bajo el searchPage del userSession
		CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
			
		//if(cueExeSearchPageVO.getConExencionPreseteada())
		//	userSession.getNavModel().putParameter("idExencionPreseteada", StringUtil.formatLong(cueExeSearchPageVO.getCueExe().getExencion().getId()));
		
		userSession.getNavModel().putParameter("permiteManPad", cueExeSearchPageVO.getPermiteManPad());
		
		return forwardAgregarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_ENC_CUEEXE);
		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CUEEXE);

	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardEliminarSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CUEEXE);

	}
		
	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, CueExeSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExeSearchPage.NAME);
		
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
		CueExeSearchPage  cueExeSearchPage = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
		
		// Si es nulo no se puede continuar
		if (cueExeSearchPage == null) {
			log.error("error en: "  + funcName + ": " + CueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExeSearchPage.NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cueExeSearchPage, request);
		
        // Tiene errores recuperables
		if (cueExeSearchPage.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cueExeSearchPage.infoString()); 
			saveDemodaErrors(request, cueExeSearchPage);
			return forwardErrorRecoverable(mapping, request, userSession, CueExeSearchPage.NAME, cueExeSearchPage);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		//cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(cueExeSearchPage.getRecursoTGI());
		cuentaFiltro.getCuentaTitular().getCuenta().setNumeroCuenta(cueExeSearchPage.getCueExe().getCuenta().getNumeroCuenta());
		
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
			CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeSearchPage.NAME); 
			}

			// Seteo el id selecionado
			NavModel navModel = userSession.getNavModel();
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			cueExeSearchPageVO.getCueExe().getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			cueExeSearchPageVO = ExeServiceLocator.getExencionService().getCueExeSearchPageParamCuenta(userSession, cueExeSearchPageVO);
			
            // Tiene errores recuperables
			if (cueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExeSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CueExeSearchPage.NAME, cueExeSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(CueExeSearchPage.NAME, cueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeSearchPage.NAME);
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
			CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeSearchPage.NAME); 
			}
			
			DemodaUtil.populateVO(cueExeSearchPageVO, request);

			// llamada al servicio
			cueExeSearchPageVO = ExeServiceLocator.getExencionService().getCueExeSearchPageParamRecurso(userSession, cueExeSearchPageVO);
			
            // Tiene errores recuperables
			if (cueExeSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeSearchPageVO.infoString()); 
				saveDemodaErrors(request, cueExeSearchPageVO);
				return forwardErrorRecoverable
					(mapping, request, userSession, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable
					(mapping, request, funcName, CueExeSearchPage.NAME, cueExeSearchPageVO);
			}

			cueExeSearchPageVO.setPageNumber(0L);
			
			// Envio el VO al request
			request.setAttribute(CueExeSearchPage.NAME, cueExeSearchPageVO);
			// Subo el apdater al userSession
			userSession.put(CueExeSearchPage.NAME, cueExeSearchPageVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_SEARCHPAGE);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeSearchPage.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeSearchPage.NAME);
		
	}
	
	public ActionForward verConvivientes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			
			return forwardVerSearchPage(mapping, request, funcName, ExeConstants.ACTION_ADMINISTRAR_CUEEXECONVIV);
			
		}
	
	public ActionForward baseImprimirDetallado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		// Bajo el searchPage del userSession
		String responseFile = request.getParameter("responseFile");
		if ("1".equals(responseFile)) {
			String fileName = (String) userSession.get("baseImprimir.reportFilename");
			// realiza la visualizacion del reporte
			baseResponseEmbedContent(response, fileName, "application/pdf");
			return null;
		}
		
		String name = request.getParameter("name");

		CommonNavegableView cnv = (CommonNavegableView) userSession.get(name);

		cnv.getReport().setImprimirDetalle("2");

		return baseImprimir(mapping,form, request,response);
	}
	
	
	
	
	
	public ActionForward administrarCueExe(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			userSession.put("reqAttIsSubmittedForm", "false");
			// Setea el PageNumber del PageModel				
			cueExeSearchPageVO.setPageNumber(1L);
			
			cueExeSearchPageVO.setModoVer(false);
			
			cueExeSearchPageVO.getCueExe().getCuenta().setId(null);
			
			// inicializamos el searchPage
			cueExeSearchPageVO = ExeServiceLocator.getExencionService().getCueExeSearchPageInit(userSession, cueExeSearchPageVO);
			
			log.debug(funcName + " cuenta: " + cueExeSearchPageVO.getCueExe().getCuenta().getNumeroCuenta());
			
			// Subo en el el searchPage al userSession
			userSession.put(CueExeSearchPage.NAME, cueExeSearchPageVO);
			userSession.remove(BuscarCueExeDAction.VER_CUEEXE_ID_CUENTA);
			 
			 
			return this.buscar(mapping, form, request, response);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeSearchPage.NAME);
		}
	}

	
	public ActionForward imprimirHistorico(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			// Bajo el searchPage del userSession
			CueExeSearchPage cueExeSearchPageVO = (CueExeSearchPage) userSession.get(CueExeSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeSearchPage.NAME); 
			}

			CommonKey cuentaKey = new CommonKey(cueExeSearchPageVO.getCueExe().getCuenta().getId());
			
			// Llamada al servicio
			PrintModel print = ExeServiceLocator.getExencionService().getImprimirHistorico(userSession, cuentaKey);
			
			baseResponsePrintModel(response, print);
			
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeSearchPage.NAME);
		}
	}
	
}
