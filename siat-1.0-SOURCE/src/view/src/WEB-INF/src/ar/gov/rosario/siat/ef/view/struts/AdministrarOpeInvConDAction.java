//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConAdapter;
import ar.gov.rosario.siat.ef.iface.model.OpeInvConVO;
import ar.gov.rosario.siat.ef.iface.service.EfServiceLocator;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import ar.gov.rosario.siat.ef.view.util.EfConstants;
import ar.gov.rosario.siat.gde.iface.model.CuentasProcuradorSearchPage;
import ar.gov.rosario.siat.gde.iface.model.EstadoCuentaSearchPage;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.view.struts.AdministrarLiqEstadoCuentaDAction;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarOpeInvConDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarOpeInvConDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
				
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
				
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_OPEINVCON, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		OpeInvConAdapter opeInvConAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {				
				stringServicio = "getOpeInvConAdapterForView(userSession, commonKey)";
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVCON_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getOpeInvConAdapterForUpdate(userSession, commonKey)";
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER);
			}
			if (act.equals(EfConstants.ACT_EXCLUIR_DE_SELEC)) {
				stringServicio = "getOpeInvConAdapterForView(userSession, commonKey)";
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterForView(userSession, commonKey);				
				opeInvConAdapterVO.addMessage(EfError.OPEINVCON_MSG_ELIMINAR);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVCON_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				commonKey = new CommonKey((Long)navModel.getParameter("idOpeInv"));
				stringServicio = "getOpeInvConAdapterForCreate(userSession)";
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterForCreate(userSession, commonKey);
				actionForward = mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER);				
			}			

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (opeInvConAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + opeInvConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			opeInvConAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + OpeInvConAdapter.NAME + ": "+ opeInvConAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OpeInvConAdapter.NAME, opeInvConAdapterVO);
			 
			saveDemodaMessages(request, opeInvConAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_OPEINVCON, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvConAdapter opeInvConAdapterVO = (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvConAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// llamada al servicio
			OpeInvConVO opeInvConVO = EfServiceLocator.getInvestigacionService().createOpeInvCon(userSession, opeInvConAdapterVO.getOpeInvCon());
			
            // Tiene errores recuperables
			if (opeInvConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConVO.infoString()); 
				saveDemodaErrors(request, opeInvConVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvConVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvConAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_OPEINVCON, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvConAdapter opeInvConAdapterVO = (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvConAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// llamada al servicio
			OpeInvConVO opeInvConVO = EfServiceLocator.getInvestigacionService().updateOpeInvCon(userSession, opeInvConAdapterVO.getOpeInvCon());
			
            // Tiene errores recuperables
			if (opeInvConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvConVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (opeInvConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvConAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
		}
	}

	public ActionForward excluirDeSelec(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_OPEINVCON, EfSecurityConstants.ADM_OPEINVCON_EXCLUIR_DE_SELEC); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvConAdapter opeInvConAdapterVO = (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}

			// llamada al servicio
			OpeInvConVO opeInvConVO = EfServiceLocator.getInvestigacionService().excluirDeSeleccion
																(userSession, opeInvConAdapterVO.getOpeInvCon());
			
            // Tiene errores recuperables
			if (opeInvConVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString());
				saveDemodaErrors(request, opeInvConVO);				
				request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
				return mapping.findForward(EfConstants.FWD_OPEINVCON_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (opeInvConVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + opeInvConAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, OpeInvConAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
		}
	}
	
	public ActionForward volverDeLiqDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, EfSecurityConstants.ADM_OPEINVCON, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			OpeInvConAdapter opeInvConAdapterVO = (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}
			
			// no llama a ningun servicio, usa el adapter de la session (porque puede ser un alta, que no tiene ID de opeInvCon, o modificacion)			
			
			
			// Envio el VO al request
			request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OpeInvConAdapter.NAME, opeInvConAdapterVO);
			 
			saveDemodaMessages(request, opeInvConAdapterVO);
			
			
			return mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		log.debug("volver...");
		return baseVolver(mapping, form, request, response, OpeInvConAdapter.NAME);
		
	}
	
	public ActionForward buscarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		return forwardSeleccionar(mapping, request, EfConstants.METOD_PARAM_PERSONA, PadConstants.ACTION_BUSCAR_PERSONA, true);		
	}
	
	public ActionForward paramPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			NavModel navModel = userSession.getNavModel();
			
			try {
				
				//bajo el adapter del usserSession
				OpeInvConAdapter opeInvConAdapterVO =  (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (opeInvConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
				}

				// recupero el id seleccionado por el usuario
				String selectedId;
				Boolean personaModificada = (Boolean) userSession.get("modificar");
				if(personaModificada != null && personaModificada){
					selectedId = opeInvConAdapterVO.getOpeInvCon().getContribuyente().getPersona().getId().toString();
					userSession.put("modificar", null);
				}else{
					selectedId = navModel.getSelectedId();				
				}
				
				// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
				if (StringUtil.isNullOrEmpty(selectedId)) {
					// Envio el VO al request
					request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
					return mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER); 
				}
				
				CommonKey ckIdPersona = new CommonKey(selectedId);
				
				// llamo al param del servicio
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterParamPersona(userSession, opeInvConAdapterVO, ckIdPersona);

	            // Tiene errores recuperables
				if (opeInvConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
					saveDemodaErrors(request, opeInvConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							OpeInvConAdapter.NAME, opeInvConAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (opeInvConAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + opeInvConAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							OpeInvConAdapter.NAME, opeInvConAdapterVO);
				}

				// grabo los mensajes si hubiere
				saveDemodaMessages(request, opeInvConAdapterVO);

				// Envio el VO al request
				request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OpeInvConAdapter.NAME, opeInvConAdapterVO);

				return mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
			}
	}
		
	public ActionForward paramSelectCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				OpeInvConAdapter opeInvConAdapterVO =  (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (opeInvConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvConAdapterVO, request);
				
	            // Tiene errores recuperables
				if (opeInvConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
					saveDemodaErrors(request, opeInvConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
				}
				

				Integer indexCtaSelec = new Integer(request.getParameter("selectedId"));
				
				// llamada al servicio
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterParamSelectCuenta(userSession, opeInvConAdapterVO, indexCtaSelec);

	            // Tiene errores recuperables
				if (opeInvConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
					saveDemodaErrors(request, opeInvConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, 
							OpeInvConAdapter.NAME, opeInvConAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (opeInvConAdapterVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + opeInvConAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, 
							OpeInvConAdapter.NAME, opeInvConAdapterVO);
				}

				// setea para ir a la parte de la pantalla donde estan las cuentas
				request.setAttribute("irA", "cuentas");
				
				// grabo los mensajes si hubiere
				saveDemodaMessages(request, opeInvConAdapterVO);

				// Envio el VO al request
				request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OpeInvConAdapter.NAME, opeInvConAdapterVO);

				return mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
			}
	}

	public ActionForward paramEstado(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				
				//bajo el adapter del usserSession
				OpeInvConAdapter opeInvConAdapterVO =  (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (opeInvConAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " " +
					"IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
				}

				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(opeInvConAdapterVO, request);
				
	            // Tiene errores recuperables
				if (opeInvConAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
					saveDemodaErrors(request, opeInvConAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
				}
				

				// llama al servicio
				opeInvConAdapterVO = EfServiceLocator.getInvestigacionService().getOpeInvConAdapterParamEstado(userSession, opeInvConAdapterVO);				
				
				// setea para ir a la parte de la pantalla donde estan los estados
				request.setAttribute("irA", "estados");
				
				// Envio el VO al request
				request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
				// Subo el apdater al userSession
				userSession.put(OpeInvConAdapter.NAME, opeInvConAdapterVO);

				return mapping.findForward(EfConstants.FWD_OPEINVCON_EDIT_ADAPTER);

			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
			}
	}
/*
	public ActionForward liquidacionDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			// Bajo el adapter del userSession
			OpeInvConAdapter opeInvConAdapterVO = (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvConAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(OpeInvConAdapter.NAME, opeInvConAdapterVO);
			// Subo el apdater al userSession
			userSession.put(OpeInvConAdapter.NAME, opeInvConAdapterVO);
			
			//	recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			String pathVerCuenta = GdeConstants.PATH_VER_CUENTA + selectedId+"&"+"idOpeInvCon="+opeInvConAdapterVO.getOpeInvCon().getId();
			
			log.debug(funcName + " pathVerCuenta =" + pathVerCuenta);
			
			request.setAttribute("liqDeudaVieneDe", "modifOpeInvCon");			
			
			return  new ActionForward (pathVerCuenta);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, DeudaContribSearchPage.NAME);
		}
	}
*/	
	public ActionForward estadoCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
	
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			
			//bajo el adapter del usserSession
			OpeInvConAdapter opeInvConAdapterVO =  (OpeInvConAdapter) userSession.get(OpeInvConAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (opeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(opeInvConAdapterVO, request);
			
            // Tiene errores recuperables
			if (opeInvConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + opeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, opeInvConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, opeInvConAdapterVO);
			}
			
			// Obtengo el id de la cuenta del opeInvConCue seleccionado
			String selectedId = request.getParameter("selectedId");
						
			//Llamada al servicio que devuelve el searchPage para el estado de cuenta, con los datos de la cuenta seleccionada
			EstadoCuentaSearchPage estadoCuentaSearchPage = GdeServiceLocator.getGestionDeudaService().getEstadoCuentaSeachPageFiltro(userSession, new Long(selectedId));

		//	estadoCuentaSearchPage.setPrevAction("/ef/AdministrarOpeInvCon");
		//	estadoCuentaSearchPage.setPrevActionParameter("volverDeLiqDeuda");
			
			// Subo el searchPage del estado cuenta al userSession
			userSession.getNavModel().putParameter(AdministrarLiqEstadoCuentaDAction.ESTADOCUENTA_SEARCHPAGE_FILTRO, estadoCuentaSearchPage);
			
			// fowardeo al action de estadoCuenta
			return baseForward(mapping, request, funcName, "volverDeLiqDeuda", GdeConstants.FWD_CUENTAS_PROCURADOR_EST_CUENTA, BaseConstants.ACT_BUSCAR);
			//return baseForwardSearchPage(mapping, request, funcName, GdeConstants.FWD_CUENTAS_PROCURADOR_EST_CUENTA, BaseConstants.ACT_BUSCAR);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CuentasProcuradorSearchPage.NAME);
		}
	}

	public ActionForward modificarPersona(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();		
		
		UserSession userSession = getCurrentUserSession(request, mapping);			
		if (userSession == null) return forwardErrorSession(request);
				
		try {
			// Bajo el adapter del userSession
			OpeInvConAdapter OpeInvConAdapterVO = (OpeInvConAdapter)userSession.get(OpeInvConAdapter.NAME);
	
			// Si es nulo no se puede continuar
			if (OpeInvConAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + OpeInvConAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, OpeInvConAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(OpeInvConAdapterVO, request);			
			// Este copiado debe ser de esta manera ya que no utilizamos Common Models
			
            // Tiene errores recuperables
			if (OpeInvConAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + OpeInvConAdapterVO.infoString()); 
				saveDemodaErrors(request, OpeInvConAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, OpeInvConAdapter.NAME, OpeInvConAdapterVO);
			}
			
			// guardo el id de la persona seleccionada
//			userSession.put("idPersona", OpeInvConAdapterVO.getOpeInvCon().getContribuyente().getPersona().getId().toString());
			userSession.put("modificar", true);
			
			NavModel navModel = userSession.getNavModel();
			
			// seteo la accion y el parametro para volver
			navModel.setPrevAction(mapping.getPath());
			navModel.setPrevActionParameter("paramPersona");

			userSession.put("modificar", true);
			// seteo el act a ejecutar en el accion al cual me dirijo
			navModel.setAct("modificar");

			return new ActionForward("/pad/AdministrarPersona.do?method=inicializar");
			
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, OpeInvConAdapter.NAME);
		}
	}	
}
