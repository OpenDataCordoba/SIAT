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
import ar.gov.rosario.siat.def.iface.model.ConAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.ConAtrVO;
import ar.gov.rosario.siat.def.iface.model.RecAtrCueAdapter;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import ar.gov.rosario.siat.def.view.util.DefinitionUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarConAtrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarConAtrDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO, act);		
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ConAtrAdapter conAtrAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getConAtrAdapterForView(userSession, commonKey)";
				conAtrAdapterVO = DefServiceLocator.getContribuyenteService().getConAtrAdapterForView
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getConAtrAdapterForUpdate(userSession, commonKey)";
				conAtrAdapterVO = DefServiceLocator.getContribuyenteService().getConAtrAdapterForUpdate
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CONATR_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getConAtrAdapterForDelete(userSession, commonKey)";
				conAtrAdapterVO = DefServiceLocator.getContribuyenteService().getConAtrAdapterForView
					(userSession, commonKey);
				conAtrAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.CONATR_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getConAtrAdapterForCreate(userSession, commonKey)";
				conAtrAdapterVO = DefServiceLocator.getContribuyenteService().getConAtrAdapterForCreate
					(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_CONATR_EDIT_ADAPTER);					
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getAtributoAdapterForView(userSession)";
				conAtrAdapterVO = DefServiceLocator.getContribuyenteService().getConAtrAdapterForView
					(userSession, commonKey);
				conAtrAdapterVO.addMessage(BaseError.MSG_ACTIVAR, DefError.CONATR_LABEL);					
				actionForward = mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getAtributoAdapterForView(userSession)";
				conAtrAdapterVO = DefServiceLocator.getContribuyenteService().getConAtrAdapterForView
					(userSession, commonKey);
				conAtrAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, DefError.CONATR_LABEL);				
				actionForward = mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (conAtrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + 
					stringServicio + ": " + conAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			conAtrAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + 
				ConAtrAdapter.NAME + ": " + conAtrAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ConAtrAdapter.NAME, conAtrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ConAtrAdapter.NAME, conAtrAdapterVO);
			
			saveDemodaMessages(request, conAtrAdapterVO);			
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO, 
			BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrAdapter conAtrAdapterVO = (ConAtrAdapter) userSession.get(ConAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(conAtrAdapterVO, request);
			
			// Tiene errores recuperables
			if (conAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrAdapter.NAME, 
					conAtrAdapterVO);
			}
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
				conAtrAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
			}else{
			
				// Se realiza el populate de los atributos submitidos			
				DefinitionUtil.populateAtrVal4Edit(conAtrAdapterVO.getGenericAtrDefinition(), request);
			
				// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
				if (!conAtrAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
					conAtrAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
			
				// Se validan formatos
				conAtrAdapterVO.getGenericAtrDefinition().clearError();
				conAtrAdapterVO.getGenericAtrDefinition().validate4EditNovedad();
			}
			
            // Tiene errores recuperables
			if (conAtrAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrAdapterVO.getGenericAtrDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Antes de llamar al servicio, pasamos el valor por defecto del definition al conAtrVo
			conAtrAdapterVO.getConAtr().setValorDefecto(conAtrAdapterVO.getGenericAtrDefinition().getValorString());
		
			// llamada al servicio
			ConAtrVO conAtrVO = DefServiceLocator.getContribuyenteService().createConAtr
				(userSession, conAtrAdapterVO.getConAtr());
			
            // Tiene errores recuperables
			if (conAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrVO.infoString()); 
				saveDemodaErrors(request, conAtrVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CONTRIBUYENTE_ATRIBUTO, 
			BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrAdapter conAtrAdapterVO = (ConAtrAdapter) userSession.get(ConAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(conAtrAdapterVO, request);
			
            // Tiene errores recuperables
			if (conAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ConAtrAdapter.NAME, conAtrAdapterVO);
			}

			// Si es nulo no se puede continuar
			if (conAtrAdapterVO.getGenericAtrDefinition().getIdDefinition() == null) {
				conAtrAdapterVO.getGenericAtrDefinition().addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, "& Atributo");
			}else{
			
				// Se realiza el populate de los atributos submitidos			
				DefinitionUtil.populateAtrVal4Edit(conAtrAdapterVO.getGenericAtrDefinition(), request);
			
				// Si fue submitido el valor se setea bandera para mostrarlo si no pasa la validacion
				if (!conAtrAdapterVO.getGenericAtrDefinition().getValorView().equals(""))
					conAtrAdapterVO.getGenericAtrDefinition().setIsSubmited(true);
			
				// Se validan formatos
				conAtrAdapterVO.getGenericAtrDefinition().clearError();
				conAtrAdapterVO.getGenericAtrDefinition().validate4EditNovedad();
			}
			
            // Tiene errores recuperables
			if (conAtrAdapterVO.getGenericAtrDefinition().hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrAdapterVO.getGenericAtrDefinition());
				return forwardErrorRecoverable(mapping, request, userSession, RecAtrCueAdapter.NAME, conAtrAdapterVO);
			}
			
			// Antes de llamar al servicio, pasamos el valor por defecto del definition al conAtrVo
			conAtrAdapterVO.getConAtr().setValorDefecto(conAtrAdapterVO.getGenericAtrDefinition().getValorString());
			
			// llamada al servicio
			ConAtrVO conAtrVO = DefServiceLocator.getContribuyenteService().updateConAtr
				(userSession, conAtrAdapterVO.getConAtr());
			
            // Tiene errores recuperables
			if (conAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrVO);
				return forwardErrorRecoverable(mapping, request, userSession, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
			BaseSecurityConstants.ELIMINAR);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrAdapter conAtrAdapterVO = (ConAtrAdapter) userSession.get(ConAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrAdapter.NAME); 
			}

			// llamada al servicio
			ConAtrVO conAtrVO = DefServiceLocator.getContribuyenteService().deleteConAtr
				(userSession, conAtrAdapterVO.getConAtr());
			
            // Tiene errores recuperables
			if (conAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString());
				saveDemodaErrors(request, conAtrVO);				
				request.setAttribute(ConAtrAdapter.NAME, conAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (conAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
			BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrAdapter conAtrAdapterVO = (ConAtrAdapter) userSession.get(ConAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrAdapter.NAME + 
					" IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrAdapter.NAME); 
			}

			// llamada al servicio
			ConAtrVO conAtrVO = DefServiceLocator.getContribuyenteService().activarConAtr
				(userSession, conAtrAdapterVO.getConAtr());
			
            // Tiene errores recuperables
			if (conAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString());
				saveDemodaErrors(request, conAtrVO);				
				request.setAttribute(ConAtrAdapter.NAME, conAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (conAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_DOMINIO_ATRIBUTO, 
			BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ConAtrAdapter conAtrAdapterVO = (ConAtrAdapter) userSession.get(ConAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrAdapter.NAME); 
			}

			// llamada al servicio
			ConAtrVO conAtrVO = DefServiceLocator.getContribuyenteService().desactivarConAtr
				(userSession, conAtrAdapterVO.getConAtr());
			
            // Tiene errores recuperables
			if (conAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString());
				saveDemodaErrors(request, conAtrVO);				
				request.setAttribute(ConAtrAdapter.NAME, conAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_CONATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (conAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ConAtrAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, ConAtrAdapter.NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, ConAtrAdapter.NAME);
		
	}

	public ActionForward buscarAtributo(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);			
		
		List<AtributoVO> listAtributosExcluidos = 
			DefServiceLocator.getContribuyenteService().getListAtributoConAtr(userSession);

		// Seteo la lista de atributos que seran excluidos en la busqueda
		userSession.getNavModel().setListVOExcluidos(listAtributosExcluidos);
		
		return forwardSeleccionar(mapping, request, 
			DefConstants.METOD_CONATR_PARAM_ATRIBUTO, DefConstants.ACTION_BUSCAR_ATRIBUTO, false);
		
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
			ConAtrAdapter conAtrAdapterVO =  (ConAtrAdapter) userSession.get(ConAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (conAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ConAtrAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ConAtrAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ConAtrAdapter.NAME, conAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_CONATR_EDIT_ADAPTER); 
			}

			// Seteo el id atributo seleccionado
			conAtrAdapterVO.getConAtr().getAtributo().setId(new Long(selectedId));
			
			// llamo al param del servicio
			conAtrAdapterVO = DefServiceLocator.getContribuyenteService().paramAtributo
				(userSession, conAtrAdapterVO);

            // Tiene errores recuperables
			if (conAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + conAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, conAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (conAtrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + conAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					ConAtrAdapter.NAME, conAtrAdapterVO);
			}
			
			// grabo los mensajes si hubiere
			saveDemodaMessages(request, conAtrAdapterVO);
			
			// Envio el VO al request
			request.setAttribute(ConAtrAdapter.NAME, conAtrAdapterVO);

			return mapping.findForward(DefConstants.FWD_CONATR_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ConAtrAdapter.NAME);
		}
	}
	
}
	
