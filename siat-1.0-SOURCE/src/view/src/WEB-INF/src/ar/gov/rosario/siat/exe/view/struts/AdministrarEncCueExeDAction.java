//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.iface.util.SiatUtil;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.service.CasServiceLocator;
import ar.gov.rosario.siat.exe.iface.model.CueExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.CueExeVO;
import ar.gov.rosario.siat.exe.iface.model.EstadoCueExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaSearchPage;
import ar.gov.rosario.siat.pad.view.struts.BuscarCuentaDAction;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarEncCueExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarEncCueExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);		
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CUEEXE_ENC, act);		

		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		CueExeAdapter cueExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			

			if (navModel.getAct().equals(BaseConstants.ACT_MODIFICAR)) {
				CommonKey commonKey = new CommonKey(navModel.getSelectedId());
				stringServicio = "getCueExeAdapterForUpdate(userSession, commonKey)";
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);
			}
			if (navModel.getAct().equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getCueExeAdapterForCreate(userSession)";
				//CommonKey ckIdExePreset = !StringUtil.isNullOrEmpty((String) navModel.getParameter("idExencionPreseteada"))?new CommonKey((String) navModel.getParameter("idExencionPreseteada")):null;
				
				boolean permiteManPad = (Boolean)navModel.getParameter("permiteManPad");
				
				cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterForCreate(userSession, permiteManPad);
				actionForward = mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			cueExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + CueExeAdapter.ENC_NAME + ": "+ cueExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeAdapter.ENC_NAME, cueExeAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, 
			ExeSecurityConstants.ABM_CUEEXE_ENC, BaseSecurityConstants.AGREGAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// llamada al servicio
			CueExeVO cueExeVO = ExeServiceLocator.getExencionService().createCueExe(userSession, cueExeAdapterVO.getCueExe());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeVO.infoString()); 
				saveDemodaErrors(request, cueExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}

			// Si tiene permiso lo dirijo al adapter de modificacion, 
			// sino vuelve al searchPage
			if (hasAccess(userSession, ExeSecurityConstants.ABM_CUEEXE, 
				BaseSecurityConstants.MODIFICAR)) {
				
				// seteo el id para que lo use el siguiente action 
				userSession.getNavModel().setSelectedId(cueExeVO.getId().toString());

				// Si tiene estado HA LUGAR va al adapter de seleccion de deuda
				if(cueExeVO.getEstadoCueExe().getId().equals(EstadoCueExeVO.ID_HA_LUGAR)){
					
					// Crea el adapter para el action
					CueExeAdapter cueExeAdapter4cambioEst = ExeServiceLocator.getExencionService().getCueExeAdapterForCambioEstado(userSession, new CommonKey(cueExeVO.getId().toString()));
					cueExeAdapter4cambioEst.setValuesFromNavModel(userSession.getNavModel());
					// Le setea el id del HA LUGAR
					cueExeAdapter4cambioEst.getCueExe().getHisEstCueExe().getEstadoCueExe().setId(EstadoCueExeVO.ID_HA_LUGAR);
					cueExeAdapter4cambioEst.getCueExe().getHisEstCueExe().setObservaciones(SiatUtil.getValueFromBundle("exe.cueExe.observaciones.creada.haLugar"));
					cueExeAdapter4cambioEst.getCueExe().getHisEstCueExe().setFecha(new Date());
					cueExeAdapter4cambioEst.getCueExe().setEsCreadaHaLugar(true);
					
					cueExeAdapter4cambioEst.setPrevAction("/exe/AdministrarCueExe");
					cueExeAdapter4cambioEst.setPrevActionParameter("inicializar&act=ver");
					
					request.setAttribute(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapter4cambioEst);
					userSession.put(CueExeAdapter.CAMBIOESTADO_NAME, cueExeAdapter4cambioEst);
					
					return new ActionForward("/exe/AdministrarCambioEstadoCueExe.do?method=cambiarEstado");
					
				}

				// lo dirijo al adapter de modificacion
				return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.ENC_NAME, 
					ExeConstants.PATH_ADMINISTRAR_CUEEXE, BaseConstants.METHOD_INICIALIZAR, 
					BaseConstants.ACT_MODIFICAR);
			} else {
				
				// lo dirijo al searchPage				
				return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.ENC_NAME);
				
			}
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, 
			ExeSecurityConstants.ABM_CUEEXE_ENC, BaseSecurityConstants.MODIFICAR);		
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(cueExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// llamada al servicio
			CueExeVO cueExeVO = ExeServiceLocator.getExencionService().updateCueExe(userSession, cueExeAdapterVO.getCueExe());
			
            // Tiene errores recuperables
			if (cueExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, CueExeAdapter.ENC_NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}

	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, CueExeAdapter.ENC_NAME);
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		return baseRefill(mapping, form, request, response, funcName, CueExeAdapter.ENC_NAME);
		
	}
	
	
	public ActionForward buscarCuenta(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(BaseConstants.ACT_REFILL);

		// seteo los parametros para cuando oprima seleccionar
		navModel.setSelectAction("/exe/AdministrarCueExe");
		navModel.setSelectActionParameter("paramCuenta");
		navModel.setAgregarEnSeleccion(false);

		// seteo el recurso TGI como filtro
		CueExeAdapter  cueExeAdapter = (CueExeAdapter) userSession.get(CueExeAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (cueExeAdapter == null) {
			log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
		}
		
		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cueExeAdapter, request);
		
        // Tiene errores recuperables
		if (cueExeAdapter.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cueExeAdapter.infoString()); 
			saveDemodaErrors(request, cueExeAdapter);
			return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapter);
		}
		
		CuentaSearchPage cuentaFiltro = new CuentaSearchPage();
		cuentaFiltro.getCuentaTitular().getCuenta().setRecurso(cueExeAdapter.getCueExe().getRecurso());
		
		navModel.putParameter(BuscarCuentaDAction.CUENTA_SEARCHPAGE_FILTRO, cuentaFiltro);
		
		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);
		
		//return mapping.findForward(PadConstants.ACTION_BUSCAR_CUENTA);
		return forwardSeleccionar(mapping, request, 
				PadConstants.METOD_BROCUE_PARAM_CUENTA, PadConstants.ACTION_BUSCAR_CUENTA, false);
		
	}
	
	public ActionForward paramCuenta (ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}
			
			NavModel navModel = userSession.getNavModel();
			
			// Si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if(StringUtil.isNullOrEmpty(navModel.getSelectedId())){
				// Envio el VO al request
				request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);				
			}
			
			// Seteo el id selecionado
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			cueExeAdapterVO.getCueExe().getCuenta().setId(commonKey.getId());
			
			// llamada al servicio
			cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterParamCuenta(userSession, cueExeAdapterVO);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}
	
	public ActionForward paramRecurso (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter)userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}
			
			DemodaUtil.populateVO(cueExeAdapterVO, request);
						
			// llamada al servicio
			cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterParamRecurso(userSession, cueExeAdapterVO);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable
					(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable
					(mapping, request, funcName, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}

	public ActionForward validarCaso(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter adapterVO = (CueExeAdapter)userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (adapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}
			
			// Populate como en un buscar
			DemodaUtil.populateVO(adapterVO, request);
				
			// llamada al servicio
			CasServiceLocator.getCasCasoService().validarCaso(userSession, adapterVO.getCueExe()); 
			
			adapterVO.getCueExe().passErrorMessages(adapterVO);
		    
		    saveDemodaMessages(request, adapterVO);
		    saveDemodaErrors(request, adapterVO);
		    
			request.setAttribute(CueExeAdapter.ENC_NAME, adapterVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}	
	}

	public ActionForward paramExencion (ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
				
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			CueExeAdapter cueExeAdapterVO = (CueExeAdapter)userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}
			
			DemodaUtil.populateVO(cueExeAdapterVO, request);
						
			// llamada al servicio
			cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterParamExencion(userSession, cueExeAdapterVO);
			
            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable
					(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable
					(mapping, request, funcName, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Envio el VO al request
			request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);
		
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}
	
	
	public ActionForward buscarSolicitante(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		CueExeAdapter cueExeAdapterVO = (CueExeAdapter) userSession.get(CueExeAdapter.ENC_NAME);
		
		// Si es nulo no se puede continuar
		if (cueExeAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
		}

		// Recuperamos datos del form en el vo
		DemodaUtil.populateVO(cueExeAdapterVO, request);
				
        // Tiene errores recuperables
		if (cueExeAdapterVO.hasErrorRecoverable()) {
			log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
			saveDemodaErrors(request, cueExeAdapterVO);
			return forwardErrorRecoverable(mapping, request, userSession, CueExeAdapter.ENC_NAME, cueExeAdapterVO);
		}
		
		return forwardSeleccionar(mapping, request, 
			ExeConstants.METOD_CUEEXE_PARAM_SOLICITANTE, PadConstants.ACTION_BUSCAR_PERSONA , false);

	}

	public ActionForward paramSolicitante(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();
		
		try {
			
			//bajo el adapter del usserSession
			CueExeAdapter cueExeAdapterVO =  (CueExeAdapter) userSession.get(CueExeAdapter.ENC_NAME);
			
			// Si es nulo no se puede continuar
			if (cueExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + CueExeAdapter.ENC_NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, CueExeAdapter.ENC_NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER); 
			}

			// Seteo el id atributo seleccionado
			cueExeAdapterVO.getCueExe().getSolicitante().setId(new Long(selectedId));
			
			// llamo al param del servicio
			cueExeAdapterVO = ExeServiceLocator.getExencionService().getCueExeAdapterParamSolicitante(userSession, cueExeAdapterVO);

            // Tiene errores recuperables
			if (cueExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + cueExeAdapterVO.infoString()); 
				saveDemodaErrors(request, cueExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
					CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (cueExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + cueExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
					CueExeAdapter.ENC_NAME, cueExeAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, cueExeAdapterVO);

			// Envio el VO al request
			request.setAttribute(CueExeAdapter.ENC_NAME, cueExeAdapterVO);

			return mapping.findForward(ExeConstants.FWD_CUEEXE_ENC_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, CueExeAdapter.ENC_NAME);
		}
	}

	

	
}
	
