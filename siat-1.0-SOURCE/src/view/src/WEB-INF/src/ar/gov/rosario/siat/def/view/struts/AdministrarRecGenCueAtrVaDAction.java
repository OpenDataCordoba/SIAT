//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.RecGenCueAtrVaAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecGenCueAtrVaDAction extends BaseDispatchAction {

	
	private Log log = LogFactory.getLog(AdministrarRecGenCueAtrVaDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECGENCUEATRVA, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			// Bajo el adapter del userSession
			RecursoAdapter recursoAdapterVO = (RecursoAdapter) userSession.get(RecursoAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (recursoAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + RecursoAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, RecursoAdapter.NAME); 
			}
			
			CommonKey atributoCommonKey = new CommonKey(navModel.getSelectedId());
			CommonKey recursoCommonKey = new CommonKey(recursoAdapterVO.getRecurso().getId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getRecGenCueAtrVaAdapterForView(userSession, recursoCommonKey, atributoCommonKey)";
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().getRecGenCueAtrVaAdapterForView
					(userSession, recursoCommonKey, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECGENCUEATRVA_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecGenCueAtrVaAdapterForUpdate(userSession, recursoCommonKey, atributoCommonKey)";
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().getRecGenCueAtrVaAdapterForView
					(userSession, recursoCommonKey, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECGENCUEATRVA_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecGenCueAtrVaAdapterForDelete(userSession, recursoCommonKey, atributoCommonKey)";
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().getRecGenCueAtrVaAdapterForView
					(userSession, recursoCommonKey, atributoCommonKey);
				recGenCueAtrVaAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECGENCUEATRVA_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_RECGENCUEATRVA_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRecGenCueAtrVaAdapterForCreate(userSession, recursoCommonKey)";
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().getRecGenCueAtrVaAdapterForCreate
					(userSession, recursoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECGENCUEATRVA_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recGenCueAtrVaAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + recGenCueAtrVaAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recGenCueAtrVaAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecGenCueAtrVaAdapter.NAME + ": "+ recGenCueAtrVaAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
			
			saveDemodaMessages(request, recGenCueAtrVaAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecGenCueAtrVaAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO = (RecGenCueAtrVaAdapter) userSession.get(RecGenCueAtrVaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recGenCueAtrVaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecGenCueAtrVaAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME); 
				}

				// Si es nulo no se puede continuar
				if (recGenCueAtrVaAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
					recGenCueAtrVaAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
				
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(recGenCueAtrVaAdapterVO.getGenericAtrDefinition(), request);
				
					DefinitionUtil.populateVigenciaAtrVal(recGenCueAtrVaAdapterVO.getGenericAtrDefinition(), request);
				
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!recGenCueAtrVaAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
						recGenCueAtrVaAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
					// Se validan formatos
					recGenCueAtrVaAdapterVO.getGenericAtrDefinition().clearError();
					recGenCueAtrVaAdapterVO.getGenericAtrDefinition().validate4EditVig();
				}
				
	            // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString()); 
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// llamada al servicio
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().createRecGenCueAtrVa(userSession, recGenCueAtrVaAdapterVO);
				
	            // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString()); 
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecGenCueAtrVaAdapter.NAME);
			}
		}
	
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO = (RecGenCueAtrVaAdapter) userSession.get(RecGenCueAtrVaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recGenCueAtrVaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecGenCueAtrVaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				//DemodaUtil.populateVO(recGenCueAtrVaAdapterVO, request);
				
				// Se realiza el populate de los atributos submitidos			
				DefinitionUtil.populateAtrVal4Edit(recGenCueAtrVaAdapterVO.getGenericAtrDefinition(), request);
				
				DefinitionUtil.populateVigenciaAtrVal(recGenCueAtrVaAdapterVO.getGenericAtrDefinition(), request);
				
				// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
				if (!recGenCueAtrVaAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
					recGenCueAtrVaAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
				// Se validan formatos
				recGenCueAtrVaAdapterVO.getGenericAtrDefinition().clearError();
				recGenCueAtrVaAdapterVO.getGenericAtrDefinition().validate4EditVig();
				
	            // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString()); 
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// llamada al servicio
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().updateRecGenCueAtrVa
					(userSession, recGenCueAtrVaAdapterVO);
				
	            // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString()); 
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecGenCueAtrVaAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECGENCUEATRVA, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO = (RecGenCueAtrVaAdapter) userSession.get(RecGenCueAtrVaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recGenCueAtrVaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecGenCueAtrVaAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME); 
				}

				// llamada al servicio
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().deleteRecGenCueAtrVa
					(userSession, recGenCueAtrVaAdapterVO);
				
	            // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString());
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO);				
					request.setAttribute(RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECGENCUEATRVA_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecGenCueAtrVaAdapter.NAME);
			}
		}

	
		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecGenCueAtrVaAdapter.NAME);
		}
		
		public ActionForward paramAtributo(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
					
			try {
				
				//bajo el adapter del usserSession
				RecGenCueAtrVaAdapter recGenCueAtrVaAdapterVO = (RecGenCueAtrVaAdapter) userSession.get(RecGenCueAtrVaAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recGenCueAtrVaAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecGenCueAtrVaAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecGenCueAtrVaAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recGenCueAtrVaAdapterVO, request);
				
				 // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString()); 
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// llamo al servicio
				recGenCueAtrVaAdapterVO = DefServiceLocator.getGravamenService().getRecGenCueAtrVaAdapterParamAtributo(userSession, recGenCueAtrVaAdapterVO);

	            // Tiene errores recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.infoString()); 
					saveDemodaErrors(request, recGenCueAtrVaAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recGenCueAtrVaAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recGenCueAtrVaAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, recGenCueAtrVaAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				// Subo el adapter al userSession
				userSession.put(RecGenCueAtrVaAdapter.NAME, recGenCueAtrVaAdapterVO);
				
				return mapping.findForward(DefConstants.FWD_RECGENCUEATRVA_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecGenCueAtrVaAdapter.NAME);
			}
		}

		
}
