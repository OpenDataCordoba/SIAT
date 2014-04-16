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

import ar.gov.rosario.siat.bal.iface.model.DisParRecAdapter;
import ar.gov.rosario.siat.bal.iface.model.DisParRecVO;
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

public class AdministrarDisParRecDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(AdministrarDisParRecDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARREC, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			DisParRecAdapter disParRecAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getDisParRecAdapterForView(userSession, commonKey)";
					disParRecAdapterVO = BalServiceLocator.getDistribucionService().getDisParRecAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARREC_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getDisParRecAdapterForDelete(userSession, commonKey)";
					disParRecAdapterVO = BalServiceLocator.getDistribucionService().getDisParRecAdapterForView
						(userSession, commonKey);
					disParRecAdapterVO.addMessage(BaseError.MSG_ELIMINAR, BalError.DISPARREC_LABEL);				
					actionForward = mapping.findForward(BalConstants.FWD_DISPARREC_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getDisParRecAdapterForUpdate(userSession, commonKey)";
					disParRecAdapterVO = BalServiceLocator.getDistribucionService().getDisParRecAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARREC_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getDisParRecAdapterForCreate(userSession)";
					disParRecAdapterVO = BalServiceLocator.getDistribucionService().getDisParRecAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(BalConstants.FWD_DISPARREC_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (disParRecAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + disParRecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				disParRecAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + DisParRecAdapter.NAME + ": "+ disParRecAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(DisParRecAdapter.NAME, disParRecAdapterVO);
				// Subo el apdater al userSession
				userSession.put(DisParRecAdapter.NAME, disParRecAdapterVO);
				
				saveDemodaMessages(request, disParRecAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParRecAdapter.NAME);
			}
		}
	
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				BalSecurityConstants.ABM_DISPARREC, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParRecAdapter disParRecAdapterVO = (DisParRecAdapter) userSession.get(DisParRecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParRecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParRecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParRecAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParRecAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParRecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecAdapterVO.infoString()); 
					saveDemodaErrors(request, disParRecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Si es nulo no se puede continuar
				if (disParRecAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
					//disParRecAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
				
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(disParRecAdapterVO.getGenericAtrDefinition(), request);
				
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!disParRecAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
						disParRecAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
					// Se validan formatos
					disParRecAdapterVO.getGenericAtrDefinition().clearError();
					disParRecAdapterVO.getGenericAtrDefinition().validate4EditVig();
				}
				
	            // Tiene errores recuperables
				if (disParRecAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecAdapterVO.infoString()); 
					saveDemodaErrors(request, disParRecAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Antes de llamar al servicio, pasamos el valor del definition al disParRecVO
				disParRecAdapterVO.getDisParRec().setValor(disParRecAdapterVO.getGenericAtrDefinition().getValorString());
				
				// llamada al servicio
				DisParRecVO disParRecVO = BalServiceLocator.getDistribucionService().createDisParRec(userSession, disParRecAdapterVO.getDisParRec());
				
	            // Tiene errores recuperables
				if (disParRecVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecVO.infoString()); 
					saveDemodaErrors(request, disParRecVO);
					return forwardErrorRecoverable(mapping, request, userSession, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParRecVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParRecVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParRecAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParRecAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				BalSecurityConstants.ABM_DISPARREC, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParRecAdapter disParRecAdapterVO = (DisParRecAdapter) userSession.get(DisParRecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParRecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParRecAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParRecAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(disParRecAdapterVO, request);
				
	            // Tiene errores recuperables
				if (disParRecAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecAdapterVO.infoString()); 
					saveDemodaErrors(request, disParRecAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Si es nulo no se puede continuar
				if (disParRecAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
					//disParRecAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
				
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(disParRecAdapterVO.getGenericAtrDefinition(), request);
				
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!disParRecAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
						disParRecAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
					// Se validan formatos
					disParRecAdapterVO.getGenericAtrDefinition().clearError();
					disParRecAdapterVO.getGenericAtrDefinition().validate4EditVig();
				}
				
	            // Tiene errores recuperables
				if (disParRecAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecAdapterVO.infoString()); 
					saveDemodaErrors(request, disParRecAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Antes de llamar al servicio, pasamos el valor del definition al disParRecVO
				disParRecAdapterVO.getDisParRec().setValor(disParRecAdapterVO.getGenericAtrDefinition().getValorString());
	
				
				// llamada al servicio
				DisParRecVO disParRecVO = BalServiceLocator.getDistribucionService().updateDisParRec(userSession, disParRecAdapterVO.getDisParRec());
				
	            // Tiene errores recuperables
				if (disParRecVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecAdapterVO.infoString()); 
					saveDemodaErrors(request, disParRecVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (disParRecVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParRecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParRecAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParRecAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, BalSecurityConstants.ABM_DISPARREC, 
				BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				DisParRecAdapter disParRecAdapterVO = (DisParRecAdapter) userSession.get(DisParRecAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (disParRecAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + DisParRecAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, DisParRecAdapter.NAME); 
				}

				// llamada al servicio
				DisParRecVO disParRecVO = BalServiceLocator.getDistribucionService().deleteDisParRec
					(userSession, disParRecAdapterVO.getDisParRec());
				
	            // Tiene errores recuperables
				if (disParRecVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + disParRecAdapterVO.infoString());
					saveDemodaErrors(request, disParRecVO);				
					request.setAttribute(DisParRecAdapter.NAME, disParRecAdapterVO);
					return mapping.findForward(BalConstants.FWD_DISPARREC_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (disParRecVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + disParRecAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, DisParRecAdapter.NAME, disParRecAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, DisParRecAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, DisParRecAdapter.NAME);
			}
		}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DisParRecAdapter.NAME);
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
			DisParRecAdapter adapterVO = (DisParRecAdapter)userSession.get(DisParRecAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + DisParRecAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DisParRecAdapter.NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
			
			log.debug( funcName + " " + adapterVO.getDisParRec().getCaso().infoString());
			
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getDisParRec()); 
			
			adapterVO.getDisParRec().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(DisParRecAdapter.NAME, adapterVO);
			
			return mapping.findForward( BalConstants.FWD_DISPARREC_EDIT_ADAPTER); 
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DisParRecAdapter.NAME);
		}	
	}

	
}
