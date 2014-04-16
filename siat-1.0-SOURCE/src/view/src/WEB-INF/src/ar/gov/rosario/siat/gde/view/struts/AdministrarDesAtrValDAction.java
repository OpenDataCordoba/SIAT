//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.DesAtrValAdapter;
import ar.gov.rosario.siat.gde.iface.model.DesAtrValVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarDesAtrValDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarDesAtrValDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		DesAtrValAdapter desAtrValAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getDesAtrValAdapterForView(userSession, commonKey)";
				desAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getDesAtrValAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESATRVAL_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getDesAtrValAdapterForUpdate(userSession, commonKey)";
				desAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getDesAtrValAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESATRVAL_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getDesAtrValAdapterForView(userSession, commonKey)";
				desAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getDesAtrValAdapterForView(userSession, commonKey);				
				desAtrValAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.DESATRVAL_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_DESATRVAL_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getDesAtrValAdapterForCreate(userSession)";
				desAtrValAdapterVO = GdeServiceLocator.getDefinicionService().getDesAtrValAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_DESATRVAL_EDIT_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (desAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + desAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			desAtrValAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + DesAtrValAdapter.NAME + ": "+ desAtrValAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesAtrValAdapter.NAME, desAtrValAdapterVO);
			 
			saveDemodaMessages(request, desAtrValAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesAtrValAdapter desAtrValAdapterVO = (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (desAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, desAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// llamada al servicio
			DesAtrValVO desAtrValVO = GdeServiceLocator.getDefinicionService().createDesAtrVal(userSession, desAtrValAdapterVO.getDesAtrVal());
			
            // Tiene errores recuperables
			if (desAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValVO.infoString()); 
				saveDemodaErrors(request, desAtrValVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desAtrValVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesAtrValAdapter desAtrValAdapterVO = (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (desAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, desAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// llamada al servicio
			DesAtrValVO desAtrValVO = GdeServiceLocator.getDefinicionService().updateDesAtrVal(userSession, desAtrValAdapterVO.getDesAtrVal());
			
            // Tiene errores recuperables
			if (desAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, desAtrValVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesAtrValAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesAtrValAdapter desAtrValAdapterVO = (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// llamada al servicio
			DesAtrValVO desAtrValVO = GdeServiceLocator.getDefinicionService().deleteDesAtrVal
				(userSession, desAtrValAdapterVO.getDesAtrVal());
			
            // Tiene errores recuperables
			if (desAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString());
				saveDemodaErrors(request, desAtrValVO);				
				request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESATRVAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesAtrValAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
	}
/*	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesAtrValAdapter desAtrValAdapterVO = (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// llamada al servicio
			DesAtrValVO desAtrValVO = GdeServiceLocator.getDefinicionService().activarDesAtrVal
				(userSession, desAtrValAdapterVO.getDesAtrVal());
			
            // Tiene errores recuperables
			if (desAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString());
				saveDemodaErrors(request, desAtrValVO);				
				request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESATRVAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesAtrValAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesAtrValAdapter desAtrValAdapterVO = (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// llamada al servicio
			DesAtrValVO desAtrValVO = GdeServiceLocator.getDefinicionService().desactivarDesAtrVal
				(userSession, desAtrValAdapterVO.getDesAtrVal());
			
            // Tiene errores recuperables
			if (desAtrValVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString());
				saveDemodaErrors(request, desAtrValVO);				
				request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESATRVAL_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (desAtrValVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, DesAtrValAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
	}
	*/
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, DesAtrValAdapter.NAME);
		
	}

	public ActionForward buscarAtributo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ABM_DESATRVAL, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			DesAtrValAdapter desAtrValAdapterVO = (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(desAtrValAdapterVO, request);
			
            // Tiene errores recuperables
			if (desAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, desAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
						
			List<AtributoVO> listAtributosExcluidos = 
				GdeServiceLocator.getDefinicionService().getListAtributoDesAtrVal(userSession, desAtrValAdapterVO);
			 
			// Seteo la lista de atributos que seran excluidos en la busqueda
			userSession.getNavModel().setListVOExcluidos(listAtributosExcluidos);

			// Envio el VO al request
			request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);
			// Subo el apdater al userSession
			userSession.put(DesAtrValAdapter.NAME, desAtrValAdapterVO);
			 			
			return forwardSeleccionar(mapping, request, 
					GdeConstants.METOD_DESATRVAL_PARAM_ATRIBUTO, GdeConstants.ACTION_BUSCAR_ATRIBUTO, false);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
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
			DesAtrValAdapter desAtrValAdapterVO =  (DesAtrValAdapter) userSession.get(DesAtrValAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (desAtrValAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + DesAtrValAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, DesAtrValAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);
				return mapping.findForward(GdeConstants.FWD_DESATRVAL_EDIT_ADAPTER); 
			}

			// llamo al param del servicio
			desAtrValAdapterVO = GdeServiceLocator.getDefinicionService().paramAtributoDesAtrVal
				(userSession, desAtrValAdapterVO, new Long(selectedId));

            // Tiene errores recuperables
			if (desAtrValAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + desAtrValAdapterVO.infoString()); 
				saveDemodaErrors(request, desAtrValAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (desAtrValAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + desAtrValAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						DesAtrValAdapter.NAME, desAtrValAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, desAtrValAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(DesAtrValAdapter.NAME, desAtrValAdapterVO);

			return mapping.findForward(GdeConstants.FWD_DESATRVAL_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DesAtrValAdapter.NAME);
		}
	}	
	
	
}
