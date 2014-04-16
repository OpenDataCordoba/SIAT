//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.struts;

import java.util.Date;
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
import ar.gov.rosario.siat.def.iface.model.RecAtrCueAdapter;
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
import coop.tecso.demoda.iface.model.SiNo;

public final class AdministrarRecAtrCueDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecAtrCueDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECATRCUE, act);		
		if (userSession == null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();

		RecAtrCueAdapter recAtrCueAdapterVO = null;
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
				stringServicio = "getRecAtrCueAdapterForView(userSession, recursoCommonKey, atributoCommonKey)";
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueAdapterForView
					(userSession, recursoCommonKey, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECATRCUE_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getRecAtrCueAdapterForUpdate(userSession, recursoCommonKey, atributoCommonKey)";
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueAdapterForView
					(userSession, recursoCommonKey, atributoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECATRCUE_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getRecAtrCueAdapterForDelete(userSession, recursoCommonKey, atributoCommonKey)";
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueAdapterForView
					(userSession, recursoCommonKey, atributoCommonKey);
				recAtrCueAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECATRCUE_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_RECATRCUE_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getRecAtrCueAdapterForCreate(userSession, recursoCommonKey)";
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueAdapterForCreate
					(userSession, recursoCommonKey);
				actionForward = mapping.findForward(DefConstants.FWD_RECATRCUE_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (recAtrCueAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + recAtrCueAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			recAtrCueAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + RecAtrCueAdapter.NAME + ": "+ recAtrCueAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
			// Subo el apdater al userSession
			userSession.put(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
			
			saveDemodaMessages(request, recAtrCueAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, RecAtrCueAdapter.NAME);
		}
	}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrCueAdapter recAtrCueAdapterVO = (RecAtrCueAdapter) userSession.get(RecAtrCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recAtrCueAdapterVO, request);
				
				// Si es nulo no se puede continuar
				if (recAtrCueAdapterVO.getRecAtrCueDefinition().getAtributo().getId() == null) {
					recAtrCueAdapterVO.getRecAtrCueDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
				}else{
					
					// Se realiza el populate de los atributos submitidos			
					DefinitionUtil.populateAtrVal4Edit(recAtrCueAdapterVO.getRecAtrCueDefinition(), request);
					
					DefinitionUtil.populateVigenciaAtrVal(recAtrCueAdapterVO.getRecAtrCueDefinition(), request);
									
					// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
					if (!recAtrCueAdapterVO.getRecAtrCueDefinition().getValorView().equals(""))
						recAtrCueAdapterVO.getRecAtrCueDefinition().setIsSubmited(true);
					
					// Se validan formatos
					recAtrCueAdapterVO.getRecAtrCueDefinition().clearError();
					if (recAtrCueAdapterVO.getRecAtrCueDefinition().getPoseeVigencia()) {
						recAtrCueAdapterVO.getRecAtrCueDefinition().validate4EditVig();
					} else {
						recAtrCueAdapterVO.getRecAtrCueDefinition().setFechaDesde(new Date());
						recAtrCueAdapterVO.getRecAtrCueDefinition().validate("manual");				
					} 
				}
				
	            // Tiene errores recuperables
				if (recAtrCueAdapterVO.getRecAtrCueDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueAdapterVO.getRecAtrCueDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// llamada al servicio
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().createRecAtrCue(userSession, recAtrCueAdapterVO);
				
	            // Tiene errores recuperables
				if (recAtrCueAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrCueAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrCueAdapter recAtrCueAdapterVO = (RecAtrCueAdapter) userSession.get(RecAtrCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recAtrCueAdapterVO, request);
				
				// Se realiza el populate de los atributos submitidos			
				DefinitionUtil.populateAtrVal4Edit(recAtrCueAdapterVO.getRecAtrCueDefinition(), request);
				
				DefinitionUtil.populateVigenciaAtrVal(recAtrCueAdapterVO.getRecAtrCueDefinition(), request);
								
				// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
				if (!recAtrCueAdapterVO.getRecAtrCueDefinition().getValorView().equals(""))
					recAtrCueAdapterVO.getRecAtrCueDefinition().setIsSubmited(true);
				
				// Se validan formatos
				recAtrCueAdapterVO.getRecAtrCueDefinition().clearError();
				if (recAtrCueAdapterVO.getRecAtrCueDefinition().getPoseeVigencia()) {
					recAtrCueAdapterVO.getRecAtrCueDefinition().validate4EditVig();
				} else {
					recAtrCueAdapterVO.getRecAtrCueDefinition().setFechaDesde(new Date());
					recAtrCueAdapterVO.getRecAtrCueDefinition().validate("manual");				
				} 
				
	            // Tiene errores recuperables
				if (recAtrCueAdapterVO.getRecAtrCueDefinition().hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueAdapterVO.getRecAtrCueDefinition());
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// llamada al servicio
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().updateRecAtrCue(userSession, recAtrCueAdapterVO);
				
	            // Tiene errores recuperables
				if (recAtrCueAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrCueAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECATRCUE, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrCueAdapter recAtrCueAdapterVO = (RecAtrCueAdapter) userSession.get(RecAtrCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueAdapter.NAME); 
				}

				// llamada al servicio
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().deleteRecAtrCue
					(userSession, recAtrCueAdapterVO);
				
	            // Tiene errores recuperables
				if (recAtrCueAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueAdapterVO.infoString());
					saveDemodaErrors(request, recAtrCueAdapterVO);				
					request.setAttribute(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECATRCUE_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrCueAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecAtrCueAdapter.NAME);
		}
		
		public ActionForward buscarAtributo(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			UserSession userSession = getCurrentUserSession(request, mapping);			
			if (userSession == null) return forwardErrorSession(request);
			
			//bajo el adapter del usserSession
			RecAtrCueAdapter recAtrCueAdapterVO =  (RecAtrCueAdapter) userSession.get(RecAtrCueAdapter.NAME);
			
			List<AtributoVO> listAtributosExcluidos = 
				DefServiceLocator.getGravamenService().getListAtributoRecAtrCue(userSession, recAtrCueAdapterVO.getRecurso().getId());
			 
			// Seteo la lista de atributos que seran excluidos en la busqueda
			userSession.getNavModel().setListVOExcluidos(listAtributosExcluidos);
		
			return forwardSeleccionar(mapping, request, 
					DefConstants.METOD_RECATRCUE_PARAM_ATRIBUTO, DefConstants.ACTION_BUSCAR_ATRIBUTO, false);
		
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
				RecAtrCueAdapter recAtrCueAdapterVO =  (RecAtrCueAdapter) userSession.get(RecAtrCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECATRCUE_EDIT_ADAPTER); 
				}

				// llamo al param del servicio
				recAtrCueAdapterVO = DefServiceLocator.getGravamenService().paramAtributoRecAtrCue
					(userSession, recAtrCueAdapterVO, new Long(selectedId));

	            // Tiene errores recuperables
				if (recAtrCueAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, recAtrCueAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);

				return mapping.findForward(DefConstants.FWD_RECATRCUE_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueAdapter.NAME);
			}
		}
		
		
		public ActionForward paramPoseeVigencia(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
						
			try {
				
				//bajo el adapter del usserSession
				RecAtrCueAdapter recAtrCueAdapterVO =  (RecAtrCueAdapter) userSession.get(RecAtrCueAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recAtrCueAdapterVO, request);
				
				// Si se selecciona un valor valido
				if (SiNo.getEsValido(recAtrCueAdapterVO.getRecAtrCueDefinition().getRecAtrCue().getPoseeVigencia().getId())){
					recAtrCueAdapterVO.setParamPoseeVigencia(true);
				} else {
					recAtrCueAdapterVO.setParamPoseeVigencia(false);
				}
				
				userSession.put(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(RecAtrCueAdapter.NAME, recAtrCueAdapterVO);

				return mapping.findForward(DefConstants.FWD_RECATRCUE_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueAdapter.NAME);
			}
		}
}
