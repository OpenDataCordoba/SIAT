//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.AccModAplVO;
import ar.gov.rosario.swe.iface.model.ItemMenuAdapter;
import ar.gov.rosario.swe.iface.model.ItemMenuVO;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweSecurityConstants;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarItemMenuDAction extends SweBaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarItemMenuDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		String act = getCurrentAct(request);
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MENU, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ItemMenuAdapter itemMenuAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			// puede ser la clave de ItemMenu o de la aplicacion
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(SweConstants.ACT_VER)) {
				stringServicio = "getItemMenuAdapterForView(userSession, commonKey)";
				itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ADAPTER_VIEW);
			}
			if (act.equals(SweConstants.ACT_MODIFICAR)) {
				stringServicio = "getItemMenuAdapterForUpdate(userSession, commonKey)";
				itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ADAPTER);
			}
			if (act.equals(SweConstants.ACT_ELIMINAR)) {
				stringServicio = "getItemMenuAdapterForDelete(userSession, commonKey)";
				itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterForDelete(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ADAPTER_VIEW);					
			}
			if (act.equals(SweSegConstants.ACT_AGREGAR_ROOT)) {
				navModel.setAct(SweConstants.ACT_AGREGAR); // unifico los act de agregacion
				stringServicio = "getItemMenuAdapterForCreateRoot(userSession)";
				itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterForCreateRoot(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ADAPTER);
			}
			if (act.equals(SweSegConstants.ACT_MODIFICAR_ACC_MOD_APL)) {
				stringServicio = "getItemMenuAdapterForCreate(userSession)";
				itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ACC_MOD_ADAPTER);
			}
			if (act.equals(SweSegConstants.ACT_AGREGAR_HIJO)) {
				navModel.setAct(SweConstants.ACT_AGREGAR); // unifico los act de agregacion
				stringServicio = "getItemMenuAdapterForCreate(userSession)";
				itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ADAPTER);
			}

			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (itemMenuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + itemMenuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Incremento el historial de navegacion del userSession
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ItemMenuAdapter.NAME + ": "+ itemMenuAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ItemMenuAdapter.NAME, itemMenuAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ItemMenuAdapter.NAME, itemMenuAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MENU, SweSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ItemMenuAdapter itemMenuAdapterVO = (ItemMenuAdapter) userSession.get(ItemMenuAdapter.NAME);
			 
			// Si es nulo no se puede continuar
			if (itemMenuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ItemMenuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ItemMenuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(itemMenuAdapterVO, request);
			
            // Tiene errores recuperables
			if (itemMenuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuAdapterVO.infoString()); 
				saveDemodaErrors(request, itemMenuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// llamada al servicio
			ItemMenuVO itemMenuVO = SweServiceLocator.getSweService().createItemMenu(userSession, itemMenuAdapterVO.getItemMenu());
			
            // Tiene errores recuperables
			if (itemMenuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuVO.infoString()); 
				saveDemodaErrors(request, itemMenuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (itemMenuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + itemMenuVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ItemMenuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MENU, SweSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ItemMenuAdapter itemMenuAdapterVO = (ItemMenuAdapter) userSession.get(ItemMenuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (itemMenuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ItemMenuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ItemMenuAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(itemMenuAdapterVO, request);
			
			log.debug("FFF url:" + itemMenuAdapterVO.getItemMenu().getUrl());

            // Tiene errores recuperables
			if (itemMenuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuAdapterVO.infoString()); 
				saveDemodaErrors(request, itemMenuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// llamada al servicio
			ItemMenuVO itemMenuVO = SweServiceLocator.getSweService().updateItemMenu(userSession, itemMenuAdapterVO.getItemMenu());
			
            // Tiene errores recuperables
			if (itemMenuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuAdapterVO.infoString()); 
				saveDemodaErrors(request, itemMenuVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (itemMenuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + itemMenuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ItemMenuAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = canAccess(request, mapping, SweSecurityConstants.ABM_MENU, SweSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ItemMenuAdapter itemMenuAdapterVO = (ItemMenuAdapter) userSession.get(ItemMenuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (itemMenuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ItemMenuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ItemMenuAdapter.NAME); 
			}

			// llamada al servicio
			ItemMenuVO itemMenuVO = SweServiceLocator.getSweService().deleteItemMenu(userSession, itemMenuAdapterVO.getItemMenu());

            // Tiene errores recuperables
			if (itemMenuVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuAdapterVO.infoString()); 
				saveDemodaErrors(request, itemMenuVO);
				request.setAttribute(ItemMenuAdapter.NAME, itemMenuAdapterVO);
				return mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ADAPTER_VIEW);
			}

			// Tiene errores no recuperables
			if (itemMenuVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + itemMenuAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ItemMenuAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		return baseVolver(mapping, form, request, response, ItemMenuAdapter.NAME);
	}
	
	// va a la busqueda de accion modulo de la aplicacion del item de menu
	public ActionForward buscarAccModApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);

		try {
			baseBuscar(mapping, userSession, funcName);
			return mapping.findForward(SweSegConstants.ACTION_BUSCAR_ACCMODAPL);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward paramActualizarAccModApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ItemMenuAdapter itemMenuAdapterVO = (ItemMenuAdapter) userSession.get(ItemMenuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (itemMenuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ItemMenuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ItemMenuAdapter.NAME); 
			}

			// Recuperamos la AccModApl seleccionada
			NavModel navModel = userSession.getNavModel();
			// es la clave de AccModApl seleccionado en el buscar
			String selectedId = navModel.getSelectedId();
			if (!StringUtil.isNullOrEmpty(selectedId)){
				CommonKey commonKey = new CommonKey(selectedId);
				itemMenuAdapterVO.getItemMenu().getAccModApl().setId(commonKey.getId());
			}else{
				//itemMenuAdapterVO.getItemMenu().getAccModApl().setId(null);
			}
			
            // Tiene errores recuperables
			if (itemMenuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuAdapterVO.infoString()); 
				saveDemodaErrors(request, itemMenuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// llamada al servicio
			itemMenuAdapterVO = SweServiceLocator.getSweService().getItemMenuAdapterParam(userSession, itemMenuAdapterVO);
			
			// Tiene errores recuperables
			if (itemMenuAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + itemMenuAdapterVO.infoString()); 
				saveDemodaErrors(request, itemMenuAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (itemMenuAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + itemMenuAdapterVO.errorString());
				return forwardErrorNonRecoverable(mapping, request, funcName, ItemMenuAdapter.NAME, itemMenuAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(ItemMenuAdapter.NAME, itemMenuAdapterVO);
			// Nuleo el list result
			//itemMenuAdapterVO.setListResult(new ArrayList()); TODO ver como solucionar esto
			// Subo en el el searchPage al userSession
			userSession.put(ItemMenuAdapter.NAME, itemMenuAdapterVO);
			
			navModel.setAct(SweSegConstants.ACT_MODIFICAR_ACC_MOD_APL);
			return mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ACC_MOD_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward paramQuitarAccModApl(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = SweUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ItemMenuAdapter itemMenuAdapterVO = (ItemMenuAdapter) userSession.get(ItemMenuAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (itemMenuAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ItemMenuAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ItemMenuAdapter.NAME); 
			}
			
			itemMenuAdapterVO.getItemMenu().setAccModApl(new AccModAplVO());
			
			// Envio el VO al request
			request.setAttribute(ItemMenuAdapter.NAME, itemMenuAdapterVO);
			// Nuleo el list result
			// Subo en el el searchPage al userSession
			userSession.put(ItemMenuAdapter.NAME, itemMenuAdapterVO);
			
			//navModel.setAct(SegConstants.ACT_MODIFICAR_ACC_MOD_APL);
			return mapping.findForward(SweSegConstants.FWD_ITEM_MENU_ACC_MOD_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}
}
