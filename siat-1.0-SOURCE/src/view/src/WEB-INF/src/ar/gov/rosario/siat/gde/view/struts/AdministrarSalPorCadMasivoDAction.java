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
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdapter;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoAdministrarAdapter;
import ar.gov.rosario.siat.gde.iface.model.SalPorCadMasivoSelAdapter;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadSearchPage;
import ar.gov.rosario.siat.gde.iface.model.SaldoPorCaducidadVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pro.iface.model.AdpCorridaAdapter;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarSalPorCadMasivoDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarSalPorCadMasivoDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		navModel.setAct(getCurrentAct(request));
		try {
			
			SalPorCadMasivoAdapter salPorCadVO = GdeServiceLocator.getGdePlanPagoService().getSalPorCadMasivoForInit (userSession);
			
			salPorCadVO.setAct("agregar");
			userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadVO);
			request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadVO);
			
			return mapping.findForward(GdeConstants.FWD_SALPORCADMASIVOADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SalPorCadMasivoAdapter.NAME);
		}
	}
	

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return new ActionForward(GdeConstants.PATH_BUSCARSALPORCAD);
	}
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		
		if(log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		try{
			SalPorCadMasivoAdapter salPorCadMasivoVO = (SalPorCadMasivoAdapter)userSession.get(SalPorCadMasivoAdapter.NAME);
			DemodaUtil.populateVO(salPorCadMasivoVO, request);
			salPorCadMasivoVO = GdeServiceLocator.getGdePlanPagoService().getSalPorCadMasivoParamRecurso(salPorCadMasivoVO, userSession);
			
			if (salPorCadMasivoVO.hasError()){
				userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
				request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
				saveDemodaErrors(request, salPorCadMasivoVO);
				return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
			}
			userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
			
			request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
			
			return mapping.findForward(GdeConstants.FWD_SALPORCADMASIVOADAPTER);
		
		}catch (Exception exception){
			return baseException(mapping, request, funcName, exception, SaldoPorCaducidadSearchPage.NAME);
		}
	}
	
	public ActionForward agregar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+ " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		try{
			SalPorCadMasivoAdapter salPorCadMasivoVO = (SalPorCadMasivoAdapter)userSession.get(SalPorCadMasivoAdapter.NAME);
			DemodaUtil.populateVO(salPorCadMasivoVO, request);
			
			salPorCadMasivoVO = GdeServiceLocator.getGdePlanPagoService().createSalPorCad(salPorCadMasivoVO, userSession); 
			if (salPorCadMasivoVO.hasError()){
				userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
				request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
				saveDemodaErrors(request, salPorCadMasivoVO);
				
				return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoAdapter.NAME, salPorCadMasivoVO);
			}
			
			request.setAttribute("selectedId", salPorCadMasivoVO.getSaldoPorCaducidad().getId());
			
			return new ActionForward (GdeConstants.PATH_SALPORCADADMINISTRAR);
			
		}catch (Exception exception){
			return baseException(mapping, request, funcName, exception, SalPorCadMasivoAdapter.NAME);
		}
	}
	
	public ActionForward ver (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		Long selectedId=0L;
		
		if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
			selectedId = Long.parseLong(request.getParameter("selectedId"));
		}
		log.debug("selectedId: "+selectedId);
		SalPorCadMasivoAdapter salPorCadAdapter= GdeServiceLocator.getGdePlanPagoService().getSalPorCadMasivoForView(selectedId, userSession);
		
		salPorCadAdapter.setAct("ver");
		userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADMASIVOADAPTER);
	}
	
	public ActionForward modificarForView (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);

		Long selectedId=0L;
		
		if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
			selectedId = Long.parseLong(request.getParameter("selectedId"));
		}
		log.debug("selectedId: "+selectedId);
		SalPorCadMasivoAdapter salPorCadAdapter= GdeServiceLocator.getGdePlanPagoService().getSalPorCadMasivoForView(selectedId, userSession);
		
		salPorCadAdapter.setAct("modificar");
		userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADMASIVOADAPTER);
	}
	
	public ActionForward modificar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		NavModel navModel = userSession.getNavModel();
		
		SalPorCadMasivoAdapter salPorCadAdapter = (SalPorCadMasivoAdapter)userSession.get(SalPorCadMasivoAdapter.NAME);
		DemodaUtil.populateVO(salPorCadAdapter, request);
		
		salPorCadAdapter = GdeServiceLocator.getGdePlanPagoService().editSalPorCadMasivo(salPorCadAdapter,userSession);
		
		if (salPorCadAdapter.hasError()){
			saveDemodaErrors(request, salPorCadAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		}
		//Seteo adonde vuelve el confirmar
		navModel.setPrevAction("/gde/BuscarSalPorCad");
		navModel.setPrevActionParameter("buscar");
		salPorCadAdapter.setPrevAction(navModel.getPrevAction());
		salPorCadAdapter.setPrevActionParameter(navModel.getPrevActionParameter());
		
		return forwardConfirmarOk(mapping, request, funcName, SalPorCadMasivoAdapter.NAME);
	}
	
	
	public ActionForward eliminarForView (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		Long selectedId=0L;
		
		if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
			selectedId = Long.parseLong(request.getParameter("selectedId"));
		}
		log.debug("selectedId: "+selectedId);
		SalPorCadMasivoAdapter salPorCadAdapter= GdeServiceLocator.getGdePlanPagoService().getSalPorCadMasivoForView(selectedId, userSession);
		
		salPorCadAdapter.setAct("eliminar");
		salPorCadAdapter.addMessageValue("Va a Eliminar el Saldo por Caducidad Masivo");
		userSession.put(SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		request.setAttribute(SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADMASIVOADAPTER);
	}

	public ActionForward eliminar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
	
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		NavModel navModel = userSession.getNavModel();
		
		SalPorCadMasivoAdapter salPorCadAdapter = (SalPorCadMasivoAdapter)userSession.get(SalPorCadMasivoAdapter.NAME);
		DemodaUtil.populateVO(salPorCadAdapter, request);
		
		salPorCadAdapter = GdeServiceLocator.getGdePlanPagoService().deleteSalPorCadMasivo(salPorCadAdapter,userSession);
		
		if (salPorCadAdapter.hasError()){
			return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoAdapter.NAME, salPorCadAdapter);
		}
		//Seteo adonde vuelve el confirmar
		navModel.setPrevAction("/gde/BuscarSalPorCad");
		navModel.setPrevActionParameter("buscar");
		salPorCadAdapter.setPrevAction(navModel.getPrevAction());
		salPorCadAdapter.setPrevActionParameter(navModel.getPrevActionParameter());
		
		return forwardConfirmarOk(mapping, request, funcName, SalPorCadMasivoAdapter.NAME);
	}

	public ActionForward administrarCorrida (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName + " :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		Long selectedId;
		if (StringUtil.isNullOrEmpty(request.getParameter("selectedId"))){
			selectedId =(Long)(request.getAttribute("selectedId"));
		}else{
			selectedId= Long.parseLong(request.getParameter("selectedId"));
		}
		SaldoPorCaducidadVO salPorCadVO = new SaldoPorCaducidadVO();
		
		salPorCadVO.setId(selectedId);
		
		try{
			SalPorCadMasivoAdministrarAdapter salPorCadMasAdminAdapter = GdeServiceLocator.getGdePlanPagoService().getSalPorCadMasAdminInit(salPorCadVO, userSession);
			
			userSession.put(SalPorCadMasivoAdministrarAdapter.NAME, salPorCadMasAdminAdapter);
			request.setAttribute(SalPorCadMasivoAdministrarAdapter.NAME, salPorCadMasAdminAdapter);
			
			return mapping.findForward(GdeConstants.FWD_SALDOMASIVOADMINADAPTER);
			
		}catch (Exception exception){
			return baseException(mapping, request, funcName, exception, SalPorCadMasivoAdministrarAdapter.NAME);
		}
	}
	
	public ActionForward activar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName= DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		NavModel navModel = userSession.getNavModel();
		
		SalPorCadMasivoAdministrarAdapter salPorCadAdminAdapter = (SalPorCadMasivoAdministrarAdapter)userSession.get(SalPorCadMasivoAdministrarAdapter.NAME);
		DemodaUtil.populateVO(salPorCadAdminAdapter, request);
		
		// Cargo el id de la corrida como parametro
		navModel.putParameter(AdpCorridaAdapter.PARAM_ID_CORRIDA_SELECTED, salPorCadAdminAdapter.getSaldoPorCaducidad().getCorrida().getId());
		navModel.setPrevAction("/gde/BuscarSalPorCad");
		navModel.setPrevActionParameter("buscar");
		
		
		return baseForward(mapping, request, funcName,"volver", ProConstants.ACTION_ADMINISTRAR_ADP_EMISION, ProConstants.ACT_ACTIVAR);
		
	}
	
	public ActionForward seleccionar (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);

		
		Long selectedId=0L;
		SalPorCadMasivoSelAdapter salPorCadSelVO= new SalPorCadMasivoSelAdapter();
		if (!StringUtil.isNullOrEmpty(request.getParameter("selectedId"))&&!(Long.parseLong(request.getParameter("selectedId"))==0L)){
			selectedId = Long.parseLong(request.getParameter("selectedId"));
			log.debug("HAY SELECTED ID");
			if (userSession.get("iniciarEnPagina")!=null){
				log.debug("PAGINA DE VUELTA: "+(String) userSession.get("iniciarEnPagina"));
				salPorCadSelVO.setPageNumber(Long.parseLong(((String)userSession.get("iniciarEnPagina"))));
				userSession.put("iniciarEnPagina", null);
			}else{
				salPorCadSelVO.setPageNumber(1L);
			}
			salPorCadSelVO.getSaldoPorCaducidad().setId(selectedId);

			log.debug("SETEADO EL SELECTED ID");
		}else{
			salPorCadSelVO = (SalPorCadMasivoSelAdapter)userSession.get(SalPorCadMasivoSelAdapter.NAME);
			DemodaUtil.populateVO(salPorCadSelVO, request);
			log.debug("SE OBTIENE EL ADAPTER "+salPorCadSelVO.getSaldoPorCaducidad().getId());
			log.debug("PAGINA ACTUAL: "+salPorCadSelVO.getPageNumber());
			//salPorCadSelVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
		}
		log.debug("selectedId: "+selectedId);

		
		if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
			DemodaUtil.populateVO(salPorCadSelVO, request);
			// Setea el PageNumber del PageModel				
		}
		
		 salPorCadSelVO= GdeServiceLocator.getGdePlanPagoService().getSaldoPorCaducidadSel(salPorCadSelVO, userSession);
		
		userSession.put(SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
		request.setAttribute(SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADSELECCIONAR);
	}
	
	public ActionForward verConvenio (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled())log.debug(funcName+" :enter");
		UserSession userSession = getCurrentUserSession(request, mapping);
		SalPorCadMasivoSelAdapter salPorCadSelAdapterVO = (SalPorCadMasivoSelAdapter)userSession.get(SalPorCadMasivoSelAdapter.NAME);
		request.setAttribute("vieneDe", "salPorCad");
		request.setAttribute("idSalPorCad", salPorCadSelAdapterVO.getSaldoPorCaducidad().getId());
		userSession.put("iniciarEnPagina",salPorCadSelAdapterVO.getPageNumber().toString());
		log.debug("subo el numero de pagina "+salPorCadSelAdapterVO.getPageNumber());
		
		return mapping.findForward(GdeConstants.ACTION_VER_CONVENIO_CUENTA);
	}

	public ActionForward quitarConvenio (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		
		String funcName=DemodaUtil.currentMethodName();
		if(log.isDebugEnabled())log.debug(funcName+" :enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		
		SalPorCadMasivoSelAdapter salPorCadSelVO = (SalPorCadMasivoSelAdapter)userSession.get(SalPorCadMasivoSelAdapter.NAME);
		//DemodaUtil.populateVO(salPorCadSelVO, request);
		
		Long selectedId=0L;
		
		if (request.getParameter("selectedId")!=null){
			selectedId = Long.parseLong(request.getParameter("selectedId"));
		}
		
		salPorCadSelVO = GdeServiceLocator.getGdePlanPagoService().getSalPorCadSelQuitarConvenio(selectedId, salPorCadSelVO, userSession);
		userSession.put("iniciarEnPagina", salPorCadSelVO.getPageNumber().toString());
		
		userSession.put(SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
		request.setAttribute(SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
		
		return mapping.findForward(GdeConstants.FWD_SALPORCADSELECCIONAR);
		
	}
	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			SalPorCadMasivoAdapter adapterVO = (SalPorCadMasivoAdapter) userSession.get(SalPorCadMasivoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + SalPorCadMasivoSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, SalPorCadMasivoSelAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getSaldoPorCaducidad().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getSaldoPorCaducidad()); 
			
			adapterVO.getSaldoPorCaducidad().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(SalPorCadMasivoAdapter.NAME, adapterVO);
			
			return mapping.findForward(GdeConstants.FWD_SALPORCADMASIVOADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, SalPorCadMasivoSelAdapter.NAME);
		}	
	}

	public ActionForward paramEstadoConvenio(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				SalPorCadMasivoSelAdapter salPorCadSelVO = (SalPorCadMasivoSelAdapter) userSession.get(SalPorCadMasivoSelAdapter.NAME);
		
				// Si es nulo no se puede continuar
				if (salPorCadSelVO == null) {
					log.error("error en: "  + funcName + ": " + SalPorCadMasivoSelAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, SalPorCadMasivoSelAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(salPorCadSelVO, request);
				
	            // Tiene errores recuperables
				if (salPorCadSelVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + salPorCadSelVO.infoString()); 
					saveDemodaErrors(request, salPorCadSelVO);
					return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
				}
				
				 salPorCadSelVO= GdeServiceLocator.getGdePlanPagoService().getSaldoPorCaducidadSel(salPorCadSelVO, userSession);
				 
				 // Tiene errores recuperables
				 if (salPorCadSelVO.hasErrorRecoverable()) {
					 log.error("recoverable error en: "  + funcName + ": " + salPorCadSelVO.infoString()); 
					 saveDemodaErrors(request, salPorCadSelVO);
					 return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
				 }
				 
				 // Tiene errores no recuperables
				 if (salPorCadSelVO.hasErrorNonRecoverable()) {
					 log.error("error en: "  + funcName + ": " + salPorCadSelVO.errorString()); 
					 return forwardErrorNonRecoverable(mapping, request, funcName, SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
				 }
		
				// Envio el VO al request
				request.setAttribute(SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
				// Subo el adapter al userSession
				userSession.put(SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
				
				return mapping.findForward(GdeConstants.FWD_SALPORCADSELECCIONAR);
			
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, SalPorCadMasivoSelAdapter.NAME);
			}
	}

	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String name = SalPorCadMasivoSelAdapter.NAME;
			String reportFormat = request.getParameter("report.reportFormat");
			
			// **Bajo el searchPage del userSession
			String responseFile = request.getParameter("responseFile");
			if ("1".equals(responseFile)) {
				String fileName = (String) userSession.get("baseImprimir.reportFilename");
				// realiza la visualizacion del reporte
				baseResponseEmbedContent(response, fileName, "application/pdf");
				return null;
			}
			
			// Bajo el adapter del userSession
			SalPorCadMasivoSelAdapter salPorCadSelVO = (SalPorCadMasivoSelAdapter) userSession.get(name);
			
			// Si es nulo no se puede continuar
			if (salPorCadSelVO == null) {
				log.error("error en: "  + funcName + ": " + name + 
					" IS NULL. No se pudo obtener de la sesion");
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			 salPorCadSelVO.setParaImprimir(true);
			 salPorCadSelVO= GdeServiceLocator.getGdePlanPagoService().getSaldoPorCaducidadSel(salPorCadSelVO, userSession);
			 salPorCadSelVO.setParaImprimir(false);
			 
			 // Tiene errores recuperables
			 if (salPorCadSelVO.hasErrorRecoverable()) {
				 log.error("recoverable error en: "  + funcName + ": " + salPorCadSelVO.infoString()); 
				 saveDemodaErrors(request, salPorCadSelVO);
				 return forwardErrorRecoverable(mapping, request, userSession, SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
			 }
			 
			 // Tiene errores no recuperables
			 if (salPorCadSelVO.hasErrorNonRecoverable()) {
				 log.error("error en: "  + funcName + ": " + salPorCadSelVO.errorString()); 
				 return forwardErrorNonRecoverable(mapping, request, funcName, SalPorCadMasivoSelAdapter.NAME, salPorCadSelVO);
			 }
	
			// llamada al servicio que genera el reporte
			salPorCadSelVO = GdeServiceLocator.getGdePlanPagoService().imprimirSalPorCadMasivoSel(userSession, salPorCadSelVO);

			// limpia la lista de reports y la lista de tablas
			salPorCadSelVO.getReport().getListReport().clear();
			salPorCadSelVO.getReport().getReportListTable().clear();
			
            // Tiene errores recuperables
			if (salPorCadSelVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + salPorCadSelVO.infoString());
				saveDemodaErrors(request, salPorCadSelVO);				
				request.setAttribute(name, salPorCadSelVO);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Tiene errores no recuperables
			if (salPorCadSelVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + salPorCadSelVO.errorString());
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
			
			// Subo en el el searchPage al userSession
			userSession.put(name, salPorCadSelVO);
			
			// obtenemos el nombre del archivo seleccionado
			String fileName = salPorCadSelVO.getReport().getReportFileName();

			// **preparamos para mostrar el imprimir
			request.setAttribute("path", request.getRequestURI());
			userSession.put("baseImprimir.reportFilename", fileName);
			log.debug("exit: " + funcName);
			return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);

		} catch (Exception exception) {
			return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
		}
	}

}
