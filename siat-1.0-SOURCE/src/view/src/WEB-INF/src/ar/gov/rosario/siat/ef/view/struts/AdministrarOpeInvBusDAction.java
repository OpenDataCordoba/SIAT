//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

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
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvBusVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.pad.iface.model.AltaOficioAdapter;
import ar.gov.rosario.siat.pad.iface.model.TipObjImpAtrDefinition;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarOpeInvBusDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOpeInvBusDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
				
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINVBUS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OpeInvBusAdapter opeInvBusAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
					
			if (act.equals(BaseConstants.ACT_VER)) {				
				stringServicio = "getOpeInvBusAdapterForView(userSession, commonKey)";
				opeInvBusAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvBusAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVBUS_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getOpeInvBusAdapterForUpdate(userSession, commonKey)";
				opeInvBusAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvBusAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVBUS_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				//lo saca de la session
				commonKey = new CommonKey((Long)userSession.get("idOpeInv"));
				Integer tipBus = (Integer) userSession.get("tipBus");
				
				stringServicio = "getOpeInvBusAdapterForCreate(userSession)";
				opeInvBusAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvBusAdapterForCreate(userSession, commonKey, tipBus);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVBUS_EDIT_ADAPTER);				
			}			
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {				
				stringServicio = "getOpeInvBusAdapterForView(userSession, commonKey)";
				opeInvBusAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvBusAdapterForView(userSession, commonKey);
				opeInvBusAdapterVO.addMessage(BaseError.MSG_ELIMINAR, EfError.OPEINVBUS_LABEL);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVBUS_VIEW_ADAPTER);
			}
			
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (opeInvBusAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + opeInvBusAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			opeInvBusAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OpeInvBusAdapter.NAME + ": "+ opeInvBusAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			 
			saveDemodaMessages(request, opeInvBusAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvBusAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINVBUS, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvBusAdapter opeInvBusAdapterVO = (OpeInvBusAdapter) userSession.get(OpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvBusAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvBusAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvBusAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvBusAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvBusAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			}
			
			// Metodo auxiliar para loguear todo el request
			DefinitionUtil.requestValues(request);
			
			// Se realiza el populate de los atributos submitidos CONTR -----------------------------------------
			for(TipObjImpAtrDefinition itemDefinition: opeInvBusAdapterVO.getTipObjImpDefinition4Contr().getListTipObjImpAtrDefinition()){
				DefinitionUtil.populateAtrVal4Busqueda(itemDefinition, request);
			}
			
			// Se validan formatos
			opeInvBusAdapterVO.getTipObjImpDefinition4Contr().clearError();
			opeInvBusAdapterVO.getTipObjImpDefinition4Contr().validate();
			
            // Tiene errores recuperables
			if (opeInvBusAdapterVO.getTipObjImpDefinition4Contr().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvBusAdapterVO.getTipObjImpDefinition4Contr());
				
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, opeInvBusAdapterVO);
			}
			
			
			// Se realiza el populate de los atributos submitidos COMERCIO --------------------------------------------
			for(TipObjImpAtrDefinition itemDefinition: opeInvBusAdapterVO.getTipObjImpDefinition4Comercio().getListTipObjImpAtrDefinition()){
				DefinitionUtil.populateAtrVal4Busqueda(itemDefinition, request);
			}
			
			// Se validan formatos
			opeInvBusAdapterVO.getTipObjImpDefinition4Comercio().clearError();
			opeInvBusAdapterVO.getTipObjImpDefinition4Comercio().validate();
			
            // Tiene errores recuperables
			if (opeInvBusAdapterVO.getTipObjImpDefinition4Comercio().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvBusAdapterVO.getTipObjImpDefinition4Comercio());
				
				return forwardErrorRecoverable(mapping, request, userSession, AltaOficioAdapter.NAME, opeInvBusAdapterVO);
			}

			
			// llamada al servicio
			opeInvBusAdapterVO = EfServiceLocator.getInvestigacionService().createOpeInvBus(userSession, opeInvBusAdapterVO);
			
            // Tiene errores recuperables
			if (opeInvBusAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvBusAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvBusAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvBusAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINVBUS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvBusAdapter opeInvBusAdapterVO = (OpeInvBusAdapter) userSession.get(OpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvBusAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvBusAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvBusAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvBusAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvBusAdapterVO);
				// Envio el VO al request
				request.setAttribute(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);

				
				return mapping.findForward(EfConstants.FWD_OPEINVBUS_VIEW_ADAPTER);
			}
			
			// llamada al servicio
			OpeInvBusVO opeInvBusVO = EfServiceLocator.getInvestigacionService().updateOpeInvBus(userSession, opeInvBusAdapterVO.getOpeInvBus());
			
            // Tiene errores recuperables
			if (opeInvBusVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvBusVO);

				// Envio el VO al request
				request.setAttribute(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
				
				return mapping.findForward(EfConstants.FWD_OPEINVBUS_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvBusAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvBusAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		log.debug("volver...");
		return baseVolver(mapping, form, request, response, OpeInvBusAdapter.NAME);
		
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ABM_OPEINVBUS, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvBusAdapter opeInvBusAdapterVO = (OpeInvBusAdapter) userSession.get(OpeInvBusAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvBusAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvBusAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvBusAdapter.NAME); 
			}

			// llamada al servicio
			OpeInvBusVO opeInvBusVO = EfServiceLocator.getInvestigacionService().deleteOpeInvBusVO(userSession, opeInvBusAdapterVO.getOpeInvBus());
			
            // Tiene errores recuperables
			if (opeInvBusVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvBusAdapterVO.infoString());
				saveDemodaErrors(request, opeInvBusVO);				
				request.setAttribute(OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
				return mapping.findForward(EfConstants.FWD_OPEINVBUS_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (opeInvBusVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvBusAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvBusAdapter.NAME, opeInvBusAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvBusAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvBusAdapter.NAME);
		}
	}
}
