//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import java.util.List;

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
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrValAdapter;
import ar.gov.rosario.siat.def.iface.model.RecursoAdapter;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecAtrValDAction extends BaseDispatchAction {
	
	private Log log = LogFactory.getLog(AdministrarRecAtrValDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECATRVAL, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RecAtrValAdapter recAtrValAdapterVO = null;
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
					stringServicio = "getRecAtrValAdapterForView(userSession, recursoCommonKey, atributoCommonKey)";
					recAtrValAdapterVO = DefServiceLocator.getGravamenService().getRecAtrValAdapterForView
						(userSession, recursoCommonKey, atributoCommonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECATRVAL_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecAtrValAdapterForUpdate(userSession, recursoCommonKey, atributoCommonKey)";
					recAtrValAdapterVO = DefServiceLocator.getGravamenService().getRecAtrValAdapterForView
						(userSession, recursoCommonKey, atributoCommonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECATRVAL_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRecAtrValAdapterForDelete(userSession, recursoCommonKey, atributoCommonKey)";
					recAtrValAdapterVO = DefServiceLocator.getGravamenService().getRecAtrValAdapterForView
						(userSession, recursoCommonKey, atributoCommonKey);
					recAtrValAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECATRVAL_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_RECATRVAL_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRecAtrValAdapterForCreate(userSession, recursoCommonKey)";
					recAtrValAdapterVO = DefServiceLocator.getGravamenService().getRecAtrValAdapterForCreate
						(userSession, recursoCommonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECATRVAL_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recAtrValAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recAtrValAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recAtrValAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RecAtrValAdapter.NAME + ": "+ recAtrValAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecAtrValAdapter.NAME, recAtrValAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecAtrValAdapter.NAME, recAtrValAdapterVO);
				
				saveDemodaMessages(request, recAtrValAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrValAdapter.NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrValAdapter recAtrValAdapterVO = (RecAtrValAdapter) userSession.get(RecAtrValAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrValAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrValAdapter.NAME); 
				}

				// Si es nulo no se puede continuar
				if (recAtrValAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
					recAtrValAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
				
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(recAtrValAdapterVO.getGenericAtrDefinition(), request);
				
					DefinitionUtil.populateVigenciaAtrVal(recAtrValAdapterVO.getGenericAtrDefinition(), request);
				
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!recAtrValAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
						recAtrValAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
					// Se validan formatos
					recAtrValAdapterVO.getGenericAtrDefinition().clearError();
					recAtrValAdapterVO.getGenericAtrDefinition().validate4EditVig();
				}
				
	            // Tiene errores recuperables
				if (recAtrValAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrValAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrValAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// llamada al servicio
				recAtrValAdapterVO = DefServiceLocator.getGravamenService().createRecAtrVal(userSession, recAtrValAdapterVO);
				
	            // Tiene errores recuperables
				if (recAtrValAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrValAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrValAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrValAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrValAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrValAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrValAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrValAdapter recAtrValAdapterVO = (RecAtrValAdapter) userSession.get(RecAtrValAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrValAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrValAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrValAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				//DemodaUtil.populateVO(recAtrValAdapterVO, request);
				
				// Se realiza el populate de los atributos submitidos			
				DefinitionUtil.populateAtrVal4Edit(recAtrValAdapterVO.getGenericAtrDefinition(), request);
				
				DefinitionUtil.populateVigenciaAtrVal(recAtrValAdapterVO.getGenericAtrDefinition(), request);
				
				// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
				if (!recAtrValAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
					recAtrValAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
				
				// Se validan formatos
				recAtrValAdapterVO.getGenericAtrDefinition().clearError();
				recAtrValAdapterVO.getGenericAtrDefinition().validate4EditVig();
				
	            // Tiene errores recuperables
				if (recAtrValAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrValAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrValAdapterVO.getGenericAtrDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// llamada al servicio
				recAtrValAdapterVO = DefServiceLocator.getGravamenService().updateRecAtrVal
					(userSession, recAtrValAdapterVO);
				
	            // Tiene errores recuperables
				if (recAtrValAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrValAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrValAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrValAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrValAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrValAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrValAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECATRVAL, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrValAdapter recAtrValAdapterVO = (RecAtrValAdapter) userSession.get(RecAtrValAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrValAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrValAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrValAdapter.NAME); 
				}

				// llamada al servicio
				recAtrValAdapterVO = DefServiceLocator.getGravamenService().deleteRecAtrVal
					(userSession, recAtrValAdapterVO);
				
	            // Tiene errores recuperables
				if (recAtrValAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrValAdapterVO.infoString());
					saveDemodaErrors(request, recAtrValAdapterVO);				
					request.setAttribute(RecAtrValAdapter.NAME, recAtrValAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECATRVAL_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recAtrValAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrValAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrValAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrValAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecAtrValAdapter.NAME);
		}
		
		public ActionForward buscarAtributo(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			//bajo el adapter del usserSession
			RecAtrValAdapter recAtrValAdapterVO =  (RecAtrValAdapter) userSession.get(RecAtrValAdapter.NAME);
			
			List<AtributoVO> listAtributosExcluidos = 
				DefServiceLocator.getGravamenService().getListAtributoRecAtrVal(userSession, recAtrValAdapterVO.getRecurso().getId());
			 
			// Seteo la lista de atributos que seran excluidos en la busqueda
			userSession.getNavModel().setListVOExcluidos(listAtributosExcluidos);
		
			return forwardSeleccionar(mapping, request, 
					DefConstants.METOD_RECATRVAL_PARAM_ATRIBUTO, DefConstants.ACTION_BUSCAR_ATRIBUTO, false);
		
		}

		public ActionForward paramAtributo(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				RecAtrValAdapter recAtrValAdapterVO =  (RecAtrValAdapter) userSession.get(RecAtrValAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrValAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrValAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrValAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(RecAtrValAdapter.NAME, recAtrValAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECATRVAL_EDIT_ADAPTER); 
				}

				// llamo al param del servicio
				recAtrValAdapterVO = DefServiceLocator.getGravamenService().paramAtributoRecAtrVal
					(userSession, recAtrValAdapterVO, new Long(selectedId));

	            // Tiene errores recuperables
				if (recAtrValAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrValAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrValAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrValAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrValAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrValAdapter.NAME, recAtrValAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, recAtrValAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(RecAtrValAdapter.NAME, recAtrValAdapterVO);

				return mapping.findForward(DefConstants.FWD_RECATRVAL_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrValAdapter.NAME);
			}
		}

}
