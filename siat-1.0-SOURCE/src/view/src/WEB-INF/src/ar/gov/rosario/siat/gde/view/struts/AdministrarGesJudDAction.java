//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.struts;

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
import ar.gov.rosario.siat.gde.iface.model.ConstanciaDeuSearchPage;
import ar.gov.rosario.siat.gde.iface.model.GesJudAdapter;
import ar.gov.rosario.siat.gde.iface.model.GesJudVO;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarGesJudDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarGesJudDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		GesJudAdapter gesJudAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER) || act.equals(GdeConstants.ACTION_GESJUD_VER_HISTORICOS)) {
				stringServicio = "getGesJudAdapterForView(userSession, commonKey)";
				gesJudAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUD_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getGesJudAdapterForUpdate(userSession, commonKey)";
				gesJudAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUD_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getGesJudAdapterForView(userSession, commonKey)";
				gesJudAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudAdapterForView(userSession, commonKey);				
				gesJudAdapterVO.addMessage(BaseError.MSG_ELIMINAR, GdeError.GESJUD_LABEL);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUD_VIEW_ADAPTER);				
			}
			if (act.equals(GdeConstants.ACTION_GESJUD_REG_CADUCIDAD)) {
				stringServicio = "getGesJudAdapterForView(userSession, commonKey)";
				gesJudAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getGesJudAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_GESJUD_REG_CADUCIDAD_ADAPTER);
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (gesJudAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + gesJudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			gesJudAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + GesJudAdapter.NAME + ": "+ gesJudAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(GesJudAdapter.NAME, gesJudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(GesJudAdapter.NAME, gesJudAdapterVO);
			 
			saveDemodaMessages(request, gesJudAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// llamada al servicio
			GesJudVO gesJudVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().createGesJud(userSession, gesJudAdapterVO.getGesJud());
			
            // Tiene errores recuperables
			if (gesJudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudVO.infoString()); 
				saveDemodaErrors(request, gesJudVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}
	}

	public ActionForward modificarEncabezado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_ENC_GESJUD, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// llamada al servicio
			GesJudVO gesJudVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updateGesJud(userSession, gesJudAdapterVO.getGesJud());
			
            // Tiene errores recuperables
			if (gesJudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudVO);
				return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (gesJudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}
	}

	public ActionForward registrarCaducidad(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, GdeSecurityConstants.REG_CADUCIDAD_GESJUD); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(gesJudAdapterVO, request);
			
            // Tiene errores recuperables
			if (gesJudAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString()); 
				saveDemodaErrors(request, gesJudAdapterVO);
				//return forwardErrorRecoverable(mapping, request, userSession, GesJudAdapter.NAME, gesJudAdapterVO);
				return mapping.findForward(GdeConstants.FWD_GESJUD_REG_CADUCIDAD_ADAPTER);
			}
			
			// llamada al servicio
			GesJudVO gesJudVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().registrarCaducidad(userSession, gesJudAdapterVO.getGesJud());
			
            // Tiene errores recuperables
			if (gesJudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString());
				// Envio el VO al request
				request.setAttribute(GesJudAdapter.NAME, gesJudAdapterVO);
				// Subo el apdater al userSession
				userSession.put(GesJudAdapter.NAME, gesJudAdapterVO);
				saveDemodaErrors(request, gesJudVO);
				return mapping.findForward(GdeConstants.FWD_GESJUD_REG_CADUCIDAD_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (gesJudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}
	}
	
	public ActionForward verHistorico(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
			}

			// seteo de acciones para volver
			userSession.getNavModel().setAct(BaseConstants.ACT_VER);
			userSession.getNavModel().setPrevAction(BaseConstants.ACT_VER);
			gesJudAdapterVO.setAct(BaseConstants.ACT_VER);
			
			// Envio el VO al request
			request.setAttribute(GesJudAdapter.NAME, gesJudAdapterVO);
			// Subo el apdater al userSession
			userSession.put(GesJudAdapter.NAME, gesJudAdapterVO);
			
			return baseForward(mapping, request, funcName, BaseConstants.ACT_VER, GdeConstants.FWD_GESJUD_VIEW_HISTORICOS_ADAPTER, GdeConstants.ACTION_GESJUD_VER_HISTORICOS);		
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}
	}
	
	public ActionForward agregarConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, BaseSecurityConstants.BUSCAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
			}

			// Llamada al servicio para obtener el searchPage para ir a la busqueda de constancias
			ConstanciaDeuSearchPage constanciaDeuSearchPage = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getConstanciaDeuSearchPageforGesJud(userSession);
			
			userSession.getNavModel().putParameter("CONSTANCIASEARCHPAGE",constanciaDeuSearchPage);
			
			// Forwardeo a la Search Page de la Constancia
			return forwardSeleccionar(mapping, request, 
					"paramConstancia", GdeConstants.ACTION_BUSCAR_CONSTANCIADEU, false);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}		
	}
	
	public ActionForward paramConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
	
		// Bajo el adapter del userSession
		GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (gesJudAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
		}
		
		// recupero el id seleccionado por el usuario
		String selectedId = navModel.getSelectedId();
		if (log.isDebugEnabled()) log.debug(funcName + "Selected Id" + selectedId);
		
		// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
		if (StringUtil.isNullOrEmpty(selectedId)) {
			// Envio el VO al request
			request.setAttribute(GesJudAdapter.NAME, gesJudAdapterVO);
			return mapping.findForward(GdeConstants.FWD_GESJUD_ADAPTER); 
		}
	
		//Seteo el id seleccionado en el navModel
		navModel.putParameter("idConstanciaDeu", new Long(selectedId));		
		
		//forwardeo al action de administrarGesJudDeu
		return baseForward(mapping, request, funcName, BaseConstants.ACT_REFILL,
					GdeConstants.ACTION_ADMINISTRAR_GESJUDDEU, GdeConstants.ACT_GESJUDDEU_AGREGAR_FROM_CONSTANCIA);
	}
	
	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_GESJUD, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (gesJudAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
			}

			// llamada al servicio
			GesJudVO gesJudVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().deleteGesJud
				(userSession, gesJudAdapterVO.getGesJud());
			
            // Tiene errores recuperables
			if (gesJudVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + gesJudAdapterVO.infoString());
				saveDemodaErrors(request, gesJudVO);				
				request.setAttribute(GesJudAdapter.NAME, gesJudAdapterVO);
				return mapping.findForward(GdeConstants.FWD_GESJUD_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (gesJudVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + gesJudAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, GesJudAdapter.NAME, gesJudAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, GesJudAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, GesJudAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, GesJudAdapter.NAME);
		
	}
	
	public ActionForward volverDeHistoricos(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		GesJudAdapter gesJudAdapterVO = (GesJudAdapter) userSession.get(GesJudAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (gesJudAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + GesJudAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, GesJudAdapter.NAME); 
		}
		userSession.getNavModel().setAct(BaseConstants.ACT_VER);		
		
		gesJudAdapterVO.setAct(BaseConstants.ACT_VER);
		// Envio el VO al request
		request.setAttribute(GesJudAdapter.NAME, gesJudAdapterVO);
		// Subo el apdater al userSession
		userSession.put(GesJudAdapter.NAME, gesJudAdapterVO);

		return refill(mapping, form, request, response);
			
		}	

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, GesJudAdapter.NAME);
			
	}

	// metodos para ABM GesJudDeu
	public ActionForward agregarGesJudDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_GESJUDDEU, BaseConstants.ACT_AGREGAR);

	}

	public ActionForward modificarGesJudDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_GESJUDDEU, BaseConstants.ACT_MODIFICAR);

	}
	
	public ActionForward verGesJudDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_GESJUDDEU, BaseConstants.ACT_VER);

	}
	
	public ActionForward eliminarGesJudDeu(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_GESJUDDEU, BaseConstants.ACT_ELIMINAR);

	}

	// metodos para ABM GesJudEvento
	public ActionForward agregarGesJudEvento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_GESJUDEVENTO, BaseConstants.ACT_AGREGAR);

	}

	public ActionForward verGesJudEvento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_EVENTO, BaseConstants.ACT_VER);

	}
	
	public ActionForward eliminarGesJudEvento(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();			
			return baseForwardAdapter(mapping, request, funcName, 
				GdeConstants.ACTION_ADMINISTRAR_GESJUDEVENTO, BaseConstants.ACT_ELIMINAR);

	}

}
