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

import ar.gov.rosario.siat.bal.iface.model.DisParPlaAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParPlaVO;
import ar.gov.rosario.siat.bal.iface.service.BalServiceLocator;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.bal.view.util.BalConstants;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public class AdministrarDisParPlaDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDisParPlaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARPLA, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			DisParPlaAdapter disParPlaAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getDisParPlaAdapterForView(userSession, commonKey)";
					disParPlaAdapterVO = BalServiceLocator.getDistribucionService().getDisParPlaAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARPLA_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getDisParPlaAdapterForDelete(userSession, commonKey)";
					disParPlaAdapterVO = BalServiceLocator.getDistribucionService().getDisParPlaAdapterForView
						(userSession, commonKey);
					disParPlaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DISPARPLA_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_DISPARPLA_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getDisParPlaAdapterForUpdate(userSession, commonKey)";
					disParPlaAdapterVO = BalServiceLocator.getDistribucionService().getDisParPlaAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARPLA_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getDisParPlaAdapterForCreate(userSession)";
					disParPlaAdapterVO = BalServiceLocator.getDistribucionService().getDisParPlaAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARPLA_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (disParPlaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + disParPlaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				disParPlaAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + DisParPlaAdapter.NAME + ": "+ disParPlaAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DisParPlaAdapter.NAME, disParPlaAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DisParPlaAdapter.NAME, disParPlaAdapterVO);
				
				saveDemodaMessages(request, disParPlaAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParPlaAdapter.NAME);
			}
		}
	
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_DISPARPLA, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParPlaAdapter disParPlaAdapterVO = (DisParPlaAdapter) userSession.get(DisParPlaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParPlaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParPlaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParPlaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParPlaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParPlaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaAdapterVO.infoString()); 
					saveDemodaErrors(request, disParPlaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Si es nulo no se puede continuar
				if (disParPlaAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
				//	disParPlaAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
				
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(disParPlaAdapterVO.getGenericAtrDefinition(), request);
				
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!disParPlaAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
						disParPlaAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
					// Se validan formatos
					disParPlaAdapterVO.getGenericAtrDefinition().clearError();
					disParPlaAdapterVO.getGenericAtrDefinition().validate4EditVig();
				}
				
	            // Tiene errores recuperables
				if (disParPlaAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaAdapterVO.infoString()); 
					saveDemodaErrors(request, disParPlaAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Antes de llamar al servicio, pasamos el valor del definition al disParPlaVO
				disParPlaAdapterVO.getDisParPla().setValor(disParPlaAdapterVO.getGenericAtrDefinition().getValorString());
				
				// llamada al servicio
				DisParPlaVO disParPlaVO = BalServiceLocator.getDistribucionService().createDisParPla(userSession, disParPlaAdapterVO.getDisParPla());
				
	            // Tiene errores recuperables
				if (disParPlaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaVO.infoString()); 
					saveDemodaErrors(request, disParPlaVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParPlaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParPlaVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParPlaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParPlaAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DISPARPLA, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParPlaAdapter disParPlaAdapterVO = (DisParPlaAdapter) userSession.get(DisParPlaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParPlaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParPlaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParPlaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParPlaAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParPlaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaAdapterVO.infoString()); 
					saveDemodaErrors(request, disParPlaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Si es nulo no se puede continuar
				if (disParPlaAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
					//disParPlaAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
				
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(disParPlaAdapterVO.getGenericAtrDefinition(), request);
				
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!disParPlaAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
						disParPlaAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
					// Se validan formatos
					disParPlaAdapterVO.getGenericAtrDefinition().clearError();
					disParPlaAdapterVO.getGenericAtrDefinition().validate4EditVig();
				}
				
	            // Tiene errores recuperables
				if (disParPlaAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaAdapterVO.infoString()); 
					saveDemodaErrors(request, disParPlaAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Antes de llamar al servicio, pasamos el valor del definition al disParPlaVO
				disParPlaAdapterVO.getDisParPla().setValor(disParPlaAdapterVO.getGenericAtrDefinition().getValorString());
	
				
				// llamada al servicio
				DisParPlaVO disParPlaVO = BalServiceLocator.getDistribucionService().updateDisParPla(userSession, disParPlaAdapterVO.getDisParPla());
				
	            // Tiene errores recuperables
				if (disParPlaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaAdapterVO.infoString()); 
					saveDemodaErrors(request, disParPlaVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParPlaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParPlaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParPlaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParPlaAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARPLA, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParPlaAdapter disParPlaAdapterVO = (DisParPlaAdapter) userSession.get(DisParPlaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParPlaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParPlaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParPlaAdapter.NAME); 
				}

				// llamada al servicio
				DisParPlaVO disParPlaVO = BalServiceLocator.getDistribucionService().deleteDisParPla
					(userSession, disParPlaAdapterVO.getDisParPla());
				
	            // Tiene errores recuperables
				if (disParPlaVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParPlaAdapterVO.infoString());
					saveDemodaErrors(request, disParPlaVO);				
					request.setAttribute(DisParPlaAdapter.NAME, disParPlaAdapterVO);
					return mapping.findForward(BalConstants.FWD_DISPARPLA_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (disParPlaVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParPlaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParPlaAdapter.NAME, disParPlaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParPlaAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParPlaAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DisParPlaAdapter.NAME);
	}

	
	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DisParPlaAdapter adapterVO = (DisParPlaAdapter)userSession.get(DisParPlaAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + DisParPlaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DisParPlaAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getDisParPla().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getDisParPla()); 
			
			adapterVO.getDisParPla().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(DisParPlaAdapter.NAME, adapterVO);
			
			return mapping.findForward( BalConstants.FWD_DISPARPLA_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParPlaAdapter.NAME);
		}	
	}

}
