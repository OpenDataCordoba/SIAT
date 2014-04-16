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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAdapter;
import ar.gov.rosario.siat.pad.iface.model.ObjImpAtrValAdapter;
import ar.gov.rosario.siat.pad.iface.service.PadServiceLocator;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarObjImpAtrValDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarObjImpAtrValDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMPATRVAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ObjImpAtrValAdapter objImpAtrValAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			ObjImpAdapter objImpAdapterVO  = (ObjImpAdapter) userSession.get(ObjImpAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAdapter.NAME); 
			}
			
			CommonKey idObjImpAtrVal = new CommonKey(navModel.getSelectedId());			
			CommonKey idObjImp = new CommonKey(objImpAdapterVO.getObjImp().getId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getObjImpAtrValAdapterForView(userSession, commonKey)";
				objImpAtrValAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAtrValAdapterForView(userSession, idObjImp, idObjImpAtrVal);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMPATRVAL_VIEW_ADAPTER);
			}	
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getObjImpAtrValAdapterForUpdate(userSession, commonKey)";
				objImpAtrValAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAtrValAdapterForView(userSession, idObjImp, idObjImpAtrVal);
				actionForward = mapping.findForward(PadConstants.FWD_OBJIMPATRVAL_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (objImpAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + objImpAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			objImpAtrValAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ObjImpAtrValAdapter.NAME + ": "+ objImpAtrValAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			 
			saveDemodaMessages(request, objImpAtrValAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAtrValAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMPATRVAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAtrValAdapter objImpAtrValAdapterVO = (ObjImpAtrValAdapter) userSession.get(ObjImpAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (objImpAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// llamada al servicio
			objImpAtrValAdapterVO = PadServiceLocator.getPadObjetoImponibleService().updateObjImpAtrVal(userSession, objImpAtrValAdapterVO);
			
            // Tiene errores recuperables
			if (objImpAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAtrValAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, PadSecurityConstants.ABM_OBJIMPATRVAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAtrValAdapter objImpAtrValAdapterVO = (ObjImpAtrValAdapter) userSession.get(ObjImpAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAtrValAdapter.NAME); 
			}

			// Se realiza el populate de los atributos submitidos			
			DefinitionUtil.populateAtrVal4Edit(objImpAtrValAdapterVO.getTipObjImpAtrDefinition(), request);
			
			DefinitionUtil.populateVigenciaAtrVal(objImpAtrValAdapterVO.getTipObjImpAtrDefinition(), request);
			
			// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
			if (!objImpAtrValAdapterVO.getTipObjImpAtrDefinition().getValorView().equals(""))
				objImpAtrValAdapterVO.getTipObjImpAtrDefinition().setIsSubmited(true);
			
			// Se validan formatos
			objImpAtrValAdapterVO.getTipObjImpAtrDefinition().clearError();
			objImpAtrValAdapterVO.getTipObjImpAtrDefinition().validate4EditNovedad();
			
            // Tiene errores recuperables
			if (objImpAtrValAdapterVO.getTipObjImpAtrDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAtrValAdapterVO.getTipObjImpAtrDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// llamada al servicio
			objImpAtrValAdapterVO = PadServiceLocator.getPadObjetoImponibleService().updateObjImpAtrVal(userSession, objImpAtrValAdapterVO);
			
            // Tiene errores recuperables
			if (objImpAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ObjImpAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAtrValAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ObjImpAtrValAdapter.NAME);
		
	}
	
	/*public ActionForward param (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ObjImpAtrValAdapter objImpAtrValAdapterVO = (ObjImpAtrValAdapter) userSession.get(ObjImpAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (objImpAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ObjImpAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ObjImpAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(objImpAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (objImpAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// llamada al servicio
			objImpAtrValAdapterVO = PadServiceLocator.getPadObjetoImponibleService().getObjImpAtrValAdapterParam(userSession, objImpAtrValAdapterVO);
			
            // Tiene errores recuperables
			if (objImpAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + objImpAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, objImpAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (objImpAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + objImpAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ObjImpAtrValAdapter.NAME, objImpAtrValAdapterVO);
			
			return mapping.findForward(PadConstants.FWD_OBJIMPATRVAL_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ObjImpAtrValAdapter.NAME);
		}
	}*/
		
}
