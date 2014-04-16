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

import ar.gov.rosario.siat.afi.view.util.AfiConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.DecJurAdapter;
import ar.gov.rosario.siat.gde.iface.model.DecJurVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;


public final class AdministrarDecJurDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarDecJurDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		DecJurAdapter decJurAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		
		try {
			
			CommonKey selectedId = null;
			String idSelected = request.getParameter(BaseConstants.SELECTED_ID);
			if (!StringUtil.isNullOrEmpty(idSelected)){
				selectedId= new CommonKey(Long.parseLong(idSelected));
			}
			
			decJurAdapterVO = (DecJurAdapter)userSession.get(DecJurAdapter.NAME);
			
			Long idDecJur;
			if (selectedId==null){
				idDecJur =decJurAdapterVO.getDecJur().getId();
			}else{
				idDecJur = selectedId.getId();
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRecConADecAdapterForView(userSession, atributoCommonKey)";
				decJurAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurAdapterForView(userSession, idDecJur);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJUR_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDecJurAdapterForUpdate(userSession, recursoCommonKey, atributoCommonKey)";
				decJurAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurAdapterForUpdate(userSession, idDecJur);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJUR_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecConADecAdapterForDelete(userSession, recursoCommonKey, atributoCommonKey)";

				decJurAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurAdapterForView(userSession, idDecJur);
				decJurAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DECJUR_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJUR_VIEW_ADAPTER);
			}
			
			if (navModel.getAct().equals(BaseConstants.ACT_VUELTA_ATRAS)) {
				stringServicio = "getRecConADecAdapterForView(userSession, recursoCommonKey, atributoCommonKey)";
				
				decJurAdapterVO = GdeServiceLocator.getGestionDeudaService().getDecJurAdapterForView(userSession, idDecJur);
				decJurAdapterVO.addMessage(GdeError.DECJUR_VUELTA_ATRAS_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DECJUR_VIEW_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (decJurAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + decJurAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurAdapter.NAME, decJurAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			decJurAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DecJurAdapter.NAME + ": "+ decJurAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DecJurAdapter.NAME, decJurAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
			
			saveDemodaMessages(request, decJurAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = "baseVolver";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();		
		try {

			navModel.clearParametersMap();
			navModel.cleanListVOExcluidos();
			navModel.setSelectedId("");

			userSession.remove(DecJurAdapter.NAME);
			userSession.remove(DecJurAdapter.DATOS_RANGO);

			log.debug(funcName + ": intentando volver a :" + StringUtil.getActionPath("/gde/BuscarDecJur", "refill"));

			return new ActionForward (StringUtil.getActionPath("/gde/BuscarDecJur", "refill"));

		} catch (Exception e) {
			log.error("Exception - ", e);
			e.printStackTrace();
			// falta definir llamada o no a logout 
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
		
	public ActionForward agregarDecJurDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			return forwardAgregarAdapter(mapping, request, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMIINSTRARDECJURDET);
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurAdapter decJurAdapterVO = (DecJurAdapter) userSession.get(DecJurAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurAdapter.NAME); 
			}

			// llamada al servicio
			DecJurVO decJurVO = GdeServiceLocator.getGestionDeudaService().deleteDecJur(userSession, decJurAdapterVO.getDecJur());
			
            // Tiene errores recuperables
			if (decJurVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurAdapterVO.infoString());
				saveDemodaErrors(request, decJurVO);				
				request.setAttribute(DecJurAdapter.NAME, decJurAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DECJUR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (decJurVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurAdapter.NAME, decJurAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DecJurAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurAdapter.NAME);
		}
	}
		
	public ActionForward verDecJurDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
			return forwardVerAdapter(mapping, request, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMIINSTRARDECJURDET);
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, DecJurAdapter.NAME);
	}
	
	public ActionForward eliminarDecJurDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return forwardEliminarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMIINSTRARDECJURDET);
	}
	
	public ActionForward modificarDecJurDet(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
			String funcName= DemodaUtil.currentMethodName();
			return forwardModificarAdapter(mapping, request, funcName, GdeConstants.ACTION_ADMIINSTRARDECJURDET);
	}
	
	public ActionForward agregarDecJurPag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
			return forwardAgregarAdapter(mapping, request, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMIINSTRARDECJURPAG);
	}
	
	public ActionForward verDecJurPag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			return forwardVerAdapter(mapping, request, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMIINSTRARDECJURPAG);
	}
	
	public ActionForward modificarDecJurPag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
			return forwardModificarAdapter(mapping, request, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMIINSTRARDECJURPAG);
	}
	
	public ActionForward eliminarDecJurPag(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception {
		
			return forwardEliminarAdapter(mapping, request, BaseConstants.ACT_INICIALIZAR, GdeConstants.ACTION_ADMIINSTRARDECJURPAG);
	}
	
	public ActionForward grabarAdicionales (ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response)throws Exception{
		String funcName=DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurAdapter decJurAdapterVO = (DecJurAdapter) userSession.get(DecJurAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurAdapter.NAME); 
			}
			DemodaUtil.populateVO(decJurAdapterVO, request);
			
			// llamada al servicio
			decJurAdapterVO = GdeServiceLocator.getGestionDeudaService().updateDecJurAdicionalesAdapter( userSession, decJurAdapterVO);
			
            // Tiene errores recuperables
			if (decJurAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurAdapterVO.infoString()); 
				saveDemodaErrors(request, decJurAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DecJurAdapter.NAME, decJurAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (decJurAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurAdapter.NAME, decJurAdapterVO);
			}
			
			// Fue Exitoso
			userSession.getNavModel().setAct(BaseConstants.ACT_MODIFICAR);
			return this.inicializar(mapping, form, request, response);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurAdapter.NAME);
		}
		
	}
		
	
	public ActionForward vueltaAtras(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, 
			BaseSecurityConstants.VUELTA_ATRAS);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DecJurAdapter decJurAdapterVO = (DecJurAdapter) userSession.get(DecJurAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (decJurAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DecJurAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DecJurAdapter.NAME); 
			}

			// llamada al servicio
			DecJurVO decJurVO = GdeServiceLocator.getGestionDeudaService().vueltaAtrasDecJur(userSession, decJurAdapterVO.getDecJur());
			
            // Tiene errores recuperables
			if (decJurVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + decJurAdapterVO.infoString());
				saveDemodaErrors(request, decJurVO);				
				request.setAttribute(DecJurAdapter.NAME, decJurAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DECJUR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (decJurVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + decJurAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DecJurAdapter.NAME, decJurAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DecJurAdapter.NAME, GdeConstants.PATH_BUSCAR_DECJUR, BaseConstants.ACT_REFILL, BaseConstants.ACT_BUSCAR);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurAdapter.NAME);
		}
	}
	
	/**
	 * Forward a buscar Formularios de declaraciones juradas
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public ActionForward verForDecJur(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DECJUR, funcName);
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			DecJurAdapter decJurAdapterVO = new DecJurAdapter();
			
			if (request.getParameter("idDecJur") != null)
				decJurAdapterVO.getDecJur().setId(new Long(request.getParameter("idDecJur")));
			
			// Traspaso de permisos al adapter que el elevado a la session.
			decJurAdapterVO.setPrevAction("/gde/AdministrarDecJur");//userSession.getAccionSWE());
			decJurAdapterVO.setPrevActionParameter(BaseConstants.ACT_INICIALIZAR);//userSession.getMetodoSWE()); //BaseConstants.ACT_REFILL);//
			decJurAdapterVO.setSelectedId(decJurAdapterVO.getDecJur().getId().toString());
			
			userSession.put(DecJurAdapter.NAME, decJurAdapterVO);
			
			return forwardVerAdapter(mapping, request, funcName, AfiConstants.ACTION_ADMINISTRAR_FORDECJUR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DecJurAdapter.NAME);
		}
	}
}
