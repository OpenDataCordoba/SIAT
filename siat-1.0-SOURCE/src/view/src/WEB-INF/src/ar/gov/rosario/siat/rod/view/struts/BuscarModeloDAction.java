//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.view.struts;

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
import ar.gov.rosario.siat.rod.iface.model.ModeloSearchPage;
import ar.gov.rosario.siat.rod.iface.model.TramiteRAAdapter;
import ar.gov.rosario.siat.rod.iface.service.RodServiceLocator;
import ar.gov.rosario.siat.rod.iface.util.RodSecurityConstants;
import ar.gov.rosario.siat.rod.view.util.RodConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;

public final class BuscarModeloDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(BuscarModeloDAction.class);
		
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, RodSecurityConstants.ABM_TRAMITERA, act); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			TramiteRAAdapter tramiteRA = (TramiteRAAdapter) userSession.get(TramiteRAAdapter.NAME);
			
			ModeloSearchPage modeloSearchPageVO = (ModeloSearchPage) userSession.get(ModeloSearchPage.NAME);
			String descrip = null;
			Integer codigo = null;
			if(modeloSearchPageVO==null){
				descrip =tramiteRA.getTramiteRA().getBDesModelo();
				codigo =tramiteRA.getTramiteRA().getBCodModelo();
			}
			modeloSearchPageVO = RodServiceLocator.getTramiteService().getModeloSearchPageInit(userSession,codigo,descrip);
			modeloSearchPageVO.getModelo().setDesModelo(descrip);
			modeloSearchPageVO.getModelo().setCodModelo(codigo);
			
			
			// Tiene errores recuperables
			if (modeloSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modeloSearchPageVO.infoString()); 
				saveDemodaErrors(request, modeloSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModeloSearchPage.NAME, modeloSearchPageVO);
			} 

			// Tiene errores no recuperables
			if (modeloSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modeloSearchPageVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ModeloSearchPage.NAME, modeloSearchPageVO);
			}
			
			// Si no tiene error
			//baseInicializarSearchPage(mapping, request, userSession , ModeloSearchPage.NAME, modeloSearchPageVO);
			
			// seteo los valores del navegacion en el pageModel
			modeloSearchPageVO.setValuesFromNavModel(userSession.getNavModel());
			
			if (userSession.getNavModel().getAct().equals(BaseConstants.ACT_SELECCIONAR)){
				modeloSearchPageVO.setModoSeleccionar(true);				
			}
			
			// Envio el VO al request
			request.setAttribute(ModeloSearchPage.NAME, modeloSearchPageVO);
			
			// Subo en el el searchPage al userSession
			userSession.put(ModeloSearchPage.NAME, modeloSearchPageVO);
			
			return mapping.findForward(RodConstants.FWD_MODELO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ModeloSearchPage.NAME);
		}
	}

	public ActionForward limpiar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return this.baseRefill(mapping, form, request, response, funcName, ModeloSearchPage.NAME);		
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
			ModeloSearchPage modeloSearchPageVO = (ModeloSearchPage) userSession.get(ModeloSearchPage.NAME);
			
			// Si es nulo no se puede continuar
			if (modeloSearchPageVO == null) {
				log.error("error en: "  + funcName + ": " + ModeloSearchPage.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ModeloSearchPage.NAME); 
			}
			
			// si el buscar diparado desde la pagina de busqueda
			if (((String)userSession.get("reqAttIsSubmittedForm")).equals("true")) {
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(modeloSearchPageVO, request);
				// Setea el PageNumber del PageModel				
				modeloSearchPageVO.setPageNumber(new Long((String)userSession.get("reqAttPageNumber")));
			}
			
            // Tiene errores recuperables
			if (modeloSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modeloSearchPageVO.infoString()); 
				saveDemodaErrors(request, modeloSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModeloSearchPage.NAME, modeloSearchPageVO);
			}
				
			// Llamada al servicio	
			modeloSearchPageVO = RodServiceLocator.getTramiteService().getModeloSearchPageResult(userSession, modeloSearchPageVO);			

			// Tiene errores recuperables
			if (modeloSearchPageVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + modeloSearchPageVO.infoString()); 
				saveDemodaErrors(request, modeloSearchPageVO);
				return forwardErrorRecoverable(mapping, request, userSession, ModeloSearchPage.NAME, modeloSearchPageVO);
			}
			
			// Tiene errores no recuperables
			if (modeloSearchPageVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + modeloSearchPageVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ModeloSearchPage.NAME, modeloSearchPageVO);
			}
		
			// Envio el VO al request
			request.setAttribute(ModeloSearchPage.NAME, modeloSearchPageVO);
			// Nuleo el list result
			
			// Subo en el el searchPage al userSession
			userSession.put(ModeloSearchPage.NAME, modeloSearchPageVO);
			
			return mapping.findForward(RodConstants.FWD_MODELO_SEARCHPAGE);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ModeloSearchPage.NAME);
		}
	}

	public ActionForward seleccionar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseSeleccionar(mapping, request, response, funcName, ModeloSearchPage.NAME);
		
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ModeloSearchPage.NAME);
		
	}
		
	
}
