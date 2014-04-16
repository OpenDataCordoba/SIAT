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
import ar.gov.rosario.siat.def.iface.model.TipObjImpAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrAdapter;
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrVO;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.def.view.util.DefConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarTipObjImpAtrDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarTipObjImpAtrDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName + " act = " + act);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		TipObjImpAtrAdapter tipObjImpAtrAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (navModel.getAct().equals(BaseConstants.ACT_VER)) {
				stringServicio = "getTipObjImpAtrAdapterForView(userSession, commonKey)";
				tipObjImpAtrAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAtrAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_VIEW_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getTipObjImpAtrAdapterForUpdate(userSession, commonKey)";
				tipObjImpAtrAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAtrAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getTipObjImpAtrAdapterForView(userSession, commonKey)";
				tipObjImpAtrAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAtrAdapterForView(userSession, commonKey);
				tipObjImpAtrAdapterVO.addMessage(BaseError.MSG_ELIMINAR, DefError.TIPOBJIMPATR_LABEL);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_VIEW_ADAPTER);					
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {

				TipObjImpAdapter tipObjImpAdapter =  (TipObjImpAdapter) userSession.get(TipObjImpAdapter.NAME);
				if (tipObjImpAdapter == null) {
					log.error("error en: "  + funcName + ": " + TipObjImpAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAdapter.NAME); 
				}
				
				CommonKey tipObjImpKey = new CommonKey(tipObjImpAdapter.getTipObjImp().getId());
				stringServicio = "getTipObjImpAtrAdapterForCreate(userSession)";
				tipObjImpAtrAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAtrAdapterForCreate(userSession, tipObjImpKey, commonKey);
				actionForward = mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_EDIT_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			if (tipObjImpAtrAdapterVO == null){
				log.debug(funcName + ": " + stringServicio + " retorna NULL");
				return null;
			}
			
            // Tiene errores recuperables
			if (tipObjImpAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAtrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + tipObjImpAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			tipObjImpAtrAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + TipObjImpAtrAdapter.NAME + ": "+ tipObjImpAtrAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			 
			saveDemodaMessages(request, tipObjImpAtrAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAtrAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAtrAdapter tipObjImpAtrAdapterVO = (TipObjImpAtrAdapter) userSession.get(TipObjImpAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAtrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAtrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpAtrAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipObjImpAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// llamada al servicio
			TipObjImpAtrVO tipObjImpAtrVO = DefServiceLocator.getObjetoImponibleService().createTipObjImpAtr(userSession, tipObjImpAtrAdapterVO.getTipObjImpAtr());
			
            // Tiene errores recuperables
			if (tipObjImpAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAtrVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAtrAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAtrAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAtrAdapter tipObjImpAtrAdapterVO = (TipObjImpAtrAdapter) userSession.get(TipObjImpAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAtrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAtrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpAtrAdapterVO, request);
			
            // Tiene errores recuperables
			if (tipObjImpAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// llamada al servicio
			TipObjImpAtrVO tipObjImpAtrVO = DefServiceLocator.getObjetoImponibleService().updateTipObjImpAtr(userSession, tipObjImpAtrAdapterVO.getTipObjImpAtr());
			
            // Tiene errores recuperables
			if (tipObjImpAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAtrAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAtrAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			TipObjImpAtrAdapter tipObjImpAtrAdapterVO = (TipObjImpAtrAdapter) userSession.get(TipObjImpAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAtrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAtrAdapter.NAME); 
			}

			// llamada al servicio
			TipObjImpAtrVO tipObjImpAtrVO = DefServiceLocator.getObjetoImponibleService().deleteTipObjImpAtr
				(userSession, tipObjImpAtrAdapterVO.getTipObjImpAtr());
			
            // Tiene errores recuperables
			if (tipObjImpAtrVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString());
				saveDemodaErrors(request, tipObjImpAtrVO);				
				request.setAttribute(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
				return mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (tipObjImpAtrVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, TipObjImpAtrAdapter.NAME);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAtrAdapter.NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, TipObjImpAtrAdapter.NAME);
	}
	
	public ActionForward paramEsAtributoSIAT (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			TipObjImpAtrAdapter tipObjImpAtrAdapterVO = (TipObjImpAtrAdapter) userSession.get(TipObjImpAtrAdapter.NAME);

			// Si es nulo no se puede continuar
			if (tipObjImpAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAtrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAtrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpAtrAdapterVO, request);

			// si es atributoSIAT, limpiamos el contenido de PosColInt y PosColIntHas
			if (tipObjImpAtrAdapterVO.getTipObjImpAtr().getEsAtributoSIAT().getEsSI()){
				tipObjImpAtrAdapterVO.getTipObjImpAtr().setPosColInt(null);
				tipObjImpAtrAdapterVO.getTipObjImpAtr().setPosColIntHas(null);
				tipObjImpAtrAdapterVO.getTipObjImpAtr().setPosColIntView("");
				tipObjImpAtrAdapterVO.getTipObjImpAtr().setPosColIntHasView("");
			}

			// Tiene errores recuperables
			if (tipObjImpAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}

			// llamada al servicio: no hace falta

			// Tiene errores recuperables
			if (tipObjImpAtrAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
				saveDemodaErrors(request, tipObjImpAtrAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}

			// Tiene errores no recuperables
			if (tipObjImpAtrAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
			// Subo el apdater al userSession
			userSession.put(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);

			return mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, TipObjImpAtrAdapter.NAME);
		}
	}
	
	public ActionForward buscarAtributo(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// Bajo el adapter del userSession
			TipObjImpAtrAdapter tipObjImpAtrAdapterVO = (TipObjImpAtrAdapter) userSession.get(TipObjImpAtrAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (tipObjImpAtrAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + TipObjImpAtrAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAtrAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(tipObjImpAtrAdapterVO, request);
			
			// seteo de filtros a excluir en la seleccion de atributo
			navModel.putParameter(BuscarAtributoDAction.LIST_ATRIBUTOS_EXCLUIDOS, tipObjImpAtrAdapterVO.getTipObjImpAtr().getTipObjImp().getListAtributo());

			return forwardSeleccionar(mapping, request, "paramAtributo", 
				DefConstants.ACTION_BUSCAR_ATRIBUTO, false); 
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
				TipObjImpAtrAdapter tipObjImpAtrAdapterVO =  (TipObjImpAtrAdapter) userSession.get(TipObjImpAtrAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (tipObjImpAtrAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + TipObjImpAtrAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, TipObjImpAtrAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId = navModel.getSelectedId();
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
					return mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_EDIT_ADAPTER); 
				}

				// Seteo el id atributo seleccionado
				tipObjImpAtrAdapterVO.getTipObjImpAtr().getAtributo().setId(new Long(selectedId));
				
				tipObjImpAtrAdapterVO = DefServiceLocator.getObjetoImponibleService().getTipObjImpAtrAdapterParamAtributo(userSession, tipObjImpAtrAdapterVO);
				
	            // Tiene errores recuperables
				if (tipObjImpAtrAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.infoString()); 
					saveDemodaErrors(request, tipObjImpAtrAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
						TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (tipObjImpAtrAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + tipObjImpAtrAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
						TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);
				}
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, tipObjImpAtrAdapterVO);
				
				// Envio el VO al request
				request.setAttribute(TipObjImpAtrAdapter.NAME, tipObjImpAtrAdapterVO);

				return mapping.findForward(DefConstants.FWD_TIPOBJIMPATR_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, TipObjImpAtrAdapter.NAME);
			}
		}



}