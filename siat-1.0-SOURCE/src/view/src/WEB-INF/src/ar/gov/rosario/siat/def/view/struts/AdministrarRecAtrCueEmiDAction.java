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
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiAdapter;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueEmiVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarRecAtrCueEmiDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarRecAtrCueEmiDAction.class);
	
	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			String act = getCurrentAct(request);		
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_RECATRCUEEMI, act);		
			if (userSession == null) return forwardErrorSession(request);

			NavModel navModel = userSession.getNavModel();

			RecAtrCueEmiAdapter recAtrCueEmiAdapterVO = null;
			String stringServicio = "";
			ActionForward actionForward = null;
			try {
				
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				
				if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
					stringServicio = "getRecAtrCueEmiAdapterForView(userSession, commonKey)";
					recAtrCueEmiAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueEmiAdapterForView
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECATRCUEEMI_VIEW_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
					stringServicio = "getRecAtrCueEmiAdapterForUpdate(userSession, commonKey)";
					recAtrCueEmiAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueEmiAdapterForUpdate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECATRCUEEMI_EDIT_ADAPTER);
				}
				if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
					stringServicio = "getRecAtrCueEmiAdapterForDelete(userSession, commonKey)";
					recAtrCueEmiAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueEmiAdapterForView
						(userSession, commonKey);
					recAtrCueEmiAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.RECATRCUEEMI_LABEL);				
					actionForward = mapping.findForward(DefConstants.FWD_RECATRCUEEMI_VIEW_ADAPTER);					
				}
				if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
					stringServicio = "getRecAtrCueEmiAdapterForCreate(userSession)";
					recAtrCueEmiAdapterVO = DefServiceLocator.getGravamenService().getRecAtrCueEmiAdapterForCreate
						(userSession, commonKey);
					actionForward = mapping.findForward(DefConstants.FWD_RECATRCUEEMI_EDIT_ADAPTER);
				}

				if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
				// Nunca Tiene errores recuperables
				
				// Tiene errores no recuperables
				if (recAtrCueEmiAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": servicio: " + 
						stringServicio + ": " + recAtrCueEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Seteo los valores de navegacion en el adapter
				recAtrCueEmiAdapterVO.setValuesFromNavModel(navModel);
							
				if (log.isDebugEnabled()) log.debug(funcName + ": " + RecAtrCueEmiAdapter.NAME + ": "+ recAtrCueEmiAdapterVO.infoString());
				
				// Envio el VO al request
				request.setAttribute(RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				// Subo el apdater al userSession
				userSession.put(RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				
				saveDemodaMessages(request, recAtrCueEmiAdapterVO);			
				
				return actionForward;
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueEmiAdapter.NAME);
			}
		}

	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping,
				DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.AGREGAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrCueEmiAdapter recAtrCueEmiAdapterVO = (RecAtrCueEmiAdapter) userSession.get(RecAtrCueEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueEmiAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueEmiAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recAtrCueEmiAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recAtrCueEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// llamada al servicio
				RecAtrCueEmiVO recAtrCueEmiVO = DefServiceLocator.getGravamenService().createRecAtrCueEmi(userSession, recAtrCueEmiAdapterVO.getRecAtrCueEmi());
				
	            // Tiene errores recuperables
				if (recAtrCueEmiVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueEmiVO.infoString()); 
					saveDemodaErrors(request, recAtrCueEmiVO);
					return forwardErrorRecoverable(mapping, request, userSession, RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueEmiVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueEmiVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrCueEmiAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueEmiAdapter.NAME);
			}
		}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.AGREGAR);
			
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrCueEmiAdapter recAtrCueEmiAdapterVO = (RecAtrCueEmiAdapter) userSession.get(RecAtrCueEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueEmiAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueEmiAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(recAtrCueEmiAdapterVO, request);
				
	            // Tiene errores recuperables
				if (recAtrCueEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// llamada al servicio
				RecAtrCueEmiVO recAtrCueEmiVO = DefServiceLocator.getGravamenService().updateRecAtrCueEmi
					(userSession, recAtrCueEmiAdapterVO.getRecAtrCueEmi());
				
	            // Tiene errores recuperables
				if (recAtrCueEmiVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueEmiVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueEmiVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrCueEmiAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueEmiAdapter.NAME);
			}
		}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			UserSession userSession = canAccess(request, mapping, 
				DefSecurityConstants.ABM_RECATRCUEEMI, BaseSecurityConstants.ELIMINAR);
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				RecAtrCueEmiAdapter recAtrCueEmiAdapterVO = (RecAtrCueEmiAdapter) userSession.get(RecAtrCueEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueEmiAdapter.NAME + 
						" IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueEmiAdapter.NAME); 
				}

				// llamada al servicio
				RecAtrCueEmiVO recAtrCueEmiVO = DefServiceLocator.getGravamenService().deleteRecAtrCueEmi
					(userSession, recAtrCueEmiAdapterVO.getRecAtrCueEmi());
				
	            // Tiene errores recuperables
				if (recAtrCueEmiVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.infoString());
					saveDemodaErrors(request, recAtrCueEmiVO);				
					request.setAttribute(RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECATRCUEEMI_VIEW_ADAPTER);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueEmiVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, RecAtrCueEmiAdapter.NAME);
				

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueEmiAdapter.NAME);
			}
		}

		public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
			
			return baseVolver(mapping, form, request, response, RecAtrCueEmiAdapter.NAME);
		}
		
		public ActionForward buscarAtributo(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {

			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession == null) return forwardErrorSession(request);
		
			// Bajo el adapter del userSession
			RecAtrCueEmiAdapter recAtrCueEmiAdapterVO = (RecAtrCueEmiAdapter) userSession.get(RecAtrCueEmiAdapter.NAME);
			
			List<AtributoVO> listAtributosExcluidos = 
				DefServiceLocator.getGravamenService().getListAtributoRecAtrCueEmi(userSession, recAtrCueEmiAdapterVO.getRecAtrCueEmi().getRecurso().getId());

			// Seteo la lista de atributos que seran excluidos en la busqueda
			userSession.getNavModel().setListVOExcluidos(listAtributosExcluidos);
		
			return forwardSeleccionar(mapping, request, 
					DefConstants.METOD_RECATRCUEEMI_PARAM_ATRIBUTO, DefConstants.ACTION_BUSCAR_ATRIBUTO, false);
		
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
				RecAtrCueEmiAdapter recAtrCueEmiAdapterVO =  (RecAtrCueEmiAdapter) userSession.get(RecAtrCueEmiAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (recAtrCueEmiAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + RecAtrCueEmiAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, RecAtrCueEmiAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
					return mapping.findForward(DefConstants.FWD_RECATRCUEEMI_EDIT_ADAPTER); 
				}

				// Seteo el id atributo seleccionado
				recAtrCueEmiAdapterVO.getRecAtrCueEmi().getAtributo().setId(new Long(selectedId));
				
				// llamo al param del servicio
				recAtrCueEmiAdapterVO = DefServiceLocator.getGravamenService().paramAtributo
					(userSession, recAtrCueEmiAdapterVO);

	            // Tiene errores recuperables
				if (recAtrCueEmiAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.infoString()); 
					saveDemodaErrors(request, recAtrCueEmiAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (recAtrCueEmiAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + recAtrCueEmiAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, recAtrCueEmiAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(RecAtrCueEmiAdapter.NAME, recAtrCueEmiAdapterVO);

				return mapping.findForward(DefConstants.FWD_RECATRCUEEMI_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, RecAtrCueEmiAdapter.NAME);
			}
		}
}
