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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpVO;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarObjImpDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarObjImpDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObjImpAdapter objImpAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getObjImpAdapterForView(userSession, commonKey)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getObjImpAdapterForView(userSession, commonKey)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getObjImpAdapterForView(userSession, commonKey)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForView(userSession, commonKey);				
				objImpAdapterVO.addMessage(BaseError.MSG_ELIMINAR, PadError.OBJIMP_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getObjImpAdapterForCreate(userSession)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getPadObjImpAdapterForView(userSession)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForView(userSession, commonKey);
				objImpAdapterVO.addMessage(BaseError.MSG_ACTIVAR, PadError.OBJIMP_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getObjImpAdapterForView(userSession)";
				objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterForView(userSession, commonKey);
				objImpAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, PadError.OBJIMP_LABEL);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (objImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			objImpAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ObjImpAdapter.NAME + ": "+ objImpAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObjImpAdapter.NAME, objImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObjImpAdapter.NAME, objImpAdapterVO);
			 
			saveDemodaMessages(request, objImpAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}
	}
	
	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseForwardAdapter(mapping, request, funcName, 
			PadConstants.ACTION_ADMINISTRAR_ENCOBJIMP, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}
			
			// Recuperamos datos del form que no son metadata
			DemodaUtil.populateVO(objImpAdapterVO, request);
			
			// Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Metodo auxiliar para loguear todo el request
			DefinitionUtil.requestValues(request);
			
			// Se realiza el populate de los atributos submitidos
			for(TipObjImpAtrDefinition itemDefinition: objImpAdapterVO.getTipObjImpDefinition().getListTipObjImpAtrDefinition()){
				DefinitionUtil.populateAtrVal4Edit(itemDefinition, request);
			}
			
			// Se validan formatos
			objImpAdapterVO.getTipObjImpDefinition().clearError();
			objImpAdapterVO.getTipObjImpDefinition().validate();
			
            // Tiene errores recuperables
			if (objImpAdapterVO.getTipObjImpDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO.getTipObjImpDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// llamada al servicio con el adapter porque en el viaja en VO y el Definition
			objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().createObjImp(userSession, objImpAdapterVO);
			
            // Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// llamada al servicio
			ObjImpVO objImpVO = PadServiceLocator.getPadObjetoImponibleService().updateObjImp(userSession, objImpAdapterVO.getObjImp());
			
            // Tiene errores recuperables
			if (objImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}

			// llamada al servicio
			ObjImpVO objImpVO = PadServiceLocator.getPadObjetoImponibleService().deleteObjImp
				(userSession, objImpAdapterVO.getObjImp());
			
            // Tiene errores recuperables
			if (objImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString());
				saveDemodaErrors(request, objImpVO);				
				request.setAttribute(ObjImpAdapter.NAME, objImpAdapterVO);
				return mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (objImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}

			// llamada al servicio
			ObjImpVO objImpVO = PadServiceLocator.getPadObjetoImponibleService().activarObjImp
				(userSession, objImpAdapterVO.getObjImp());
			
            // Tiene errores recuperables
			if (objImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString());
				saveDemodaErrors(request, objImpVO);				
				request.setAttribute(ObjImpAdapter.NAME, objImpAdapterVO);
				return mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (objImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMP, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}
			
			// Recuperamos datos del form que no son metadata
			DemodaUtil.populateVO(objImpAdapterVO, request);

			// Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO, PadConstants.FWD_OBJIMP_VIEW_ADAPTER);
			}

			// llamada al servicio
			ObjImpVO objImpVO = PadServiceLocator.getPadObjetoImponibleService().desactivarObjImp
				(userSession, objImpAdapterVO.getObjImp());
			
            // Tiene errores recuperables
			if (objImpVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString());
				saveDemodaErrors(request, objImpVO);				
				request.setAttribute(ObjImpAdapter.NAME, objImpAdapterVO);
				return mapping.findForward(PadConstants.FWD_OBJIMP_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (objImpVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObjImpAdapter.NAME);
		
	}
	
	public ActionForward paramTipObjImp (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAdapter objImpAdapterVO = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpAdapterVO, request);
			
            // Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// llamada al servicio
			objImpAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAdapterParamTipObjImp(userSession, objImpAdapterVO);
			
            // Tiene errores recuperables
			if (objImpAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAdapter.NAME, objImpAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ObjImpAdapter.NAME, objImpAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObjImpAdapter.NAME, objImpAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMP_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAdapter.NAME);
		}
	}
	
	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ObjImpAdapter.NAME);
		
	}
	
	// Metodos relacionados ObjImpAtrVal
	public ActionForward verObjImpAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardVerAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_OBJIMPATRVAL);

	}

	public ActionForward modificarObjImpAtrVal(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return forwardModificarAdapter(mapping, request, funcName, PadConstants.ACTION_ADMINISTRAR_OBJIMPATRVAL);

	}
	
}
