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

import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProAdapter;
import ar.gov.rosario.siat.gde.iface.model.PlaEnvDeuProVO;
import ar.gov.rosario.siat.gde.iface.model.TraspasoDevolucionDeudaAdapter;
import ar.gov.rosario.siat.gde.iface.service.GdeServiceLocator;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.gde.view.util.GdeConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PrintModel;

public final class AdministrarPlaEnvDeuProDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarPlaEnvDeuProDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER) || act.equals(GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS)) {
				stringServicio = "getPlaEnvDeuProAdapterForView(userSession, commonKey)";
				plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getPlaEnvDeuProAdapterForUpdate(userSession, commonKey)";
				plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_EDIT_ADAPTER);
			}
			if (act.equals(GdeConstants.ACTION_ADM_PLAENVDEUPRO_RECOMPONER_PLANILLA)) {
				stringServicio = "getPlaEnvDeuProAdapterForView(userSession, commonKey)";
				plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProAdapterForView(userSession, commonKey);
				plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_RECOMPONER_PLANILLA);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER);
			}
			if (act.equals(GdeConstants.ACTION_ADM_PLAENVDEUPRO_IMP_PAD)) {
				stringServicio = "getPlaEnvDeuProAdapterForView(userSession, commonKey)";
				PrintModel print  = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().imprimirPadron(userSession, commonKey);
				baseResponsePrintModel(response, print);
				return null;
			}			
			if (act.equals(GdeConstants.ACTION_ADM_PLAENVDEUPRO_IMP_CON)) {
				stringServicio = "imprimirConstanciasPlanilla(userSession, commonKey)";
				PrintModel print  = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().imprimirConstanciasPlanilla(userSession, commonKey);
				baseResponsePrintModel(response, print);
				return null;
			}
			
			if (act.equals(GdeConstants.ACTION_ADM_PLAENVDEUPRO_HABILITAR_PLANILLA)) {
				stringServicio = "getPlaEnvDeuProAdapterForView(userSession, commonKey)";
				plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProAdapterForHabilitar(userSession, commonKey);
				plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_HABILITAR_PLANILLA);
				actionForward = mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER);
			}
			if (act.equals(GdeConstants.ACTION_ADM_PLAENVDEUPRO_GENERAR_ARCHIVO)) {
				stringServicio = "getPlaEnvDeuProgenerarArchivoCD(userSession, commonKey)";
				plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProGenerarArchivoCD  (userSession, commonKey);
				plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_GENERAR_ARCHIVO);
				
				baseResponseFile(response, plaEnvDeuProAdapterVO.getNombreArchivoCD());
				return null;//actionForward =mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER);
			}
			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (plaEnvDeuProAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + plaEnvDeuProAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			plaEnvDeuProAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + PlaEnvDeuProAdapter.NAME + ": "+ plaEnvDeuProAdapterVO.infoString());
			
			saveDemodaMessages(request, plaEnvDeuProAdapterVO);
			// Envio el VO al request
			request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);

			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}
	}
	
	public ActionForward verConstancias(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
			}
			
			// Llamada al servicio
			plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProAdapterForHabilitarConstancias(userSession, plaEnvDeuProAdapterVO);

			// Seteo la accion de ver las constancias
			plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS);
				
			// Envio el VO al request
			request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			 
			saveDemodaMessages(request, plaEnvDeuProAdapterVO);
	
			return baseForward(mapping, request, funcName, BaseConstants.ACT_VER, GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER, GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}		
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
				HttpServletRequest request, HttpServletResponse response)
				throws Exception {
	
			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
			
			UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.MODIFICAR); 
			if (userSession==null) return forwardErrorSession(request);
			
			try {
				// Bajo el adapter del userSession
				PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
				
				// Si es nulo no se puede continuar
				if (plaEnvDeuProAdapterVO == null) {
					log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
				}
	
				// Recuperamos datos del form en el vo
				DemodaUtil.populateVO(plaEnvDeuProAdapterVO, request);
				
	            // Tiene errores recuperables
				if (plaEnvDeuProAdapterVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
					saveDemodaErrors(request, plaEnvDeuProAdapterVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				}
				
				// llamada al servicio
				PlaEnvDeuProVO plaEnvDeuProVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().updatePlaEnvDeuPro(userSession, plaEnvDeuProAdapterVO.getPlaEnvDeuPro());
				
	            // Tiene errores recuperables
				if (plaEnvDeuProVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
					saveDemodaErrors(request, plaEnvDeuProVO);
					return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				}
				
				// Tiene errores no recuperables
				if (plaEnvDeuProVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				}
				
				// Fue Exitoso
				return forwardConfirmarOk(mapping, request, funcName, PlaEnvDeuProAdapter.NAME);
				
			} catch (Exception exception) {
				return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
			}
		}

	public ActionForward recomponerPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaEnvDeuProAdapterVO, request);
			
            // Tiene errores recuperables
			if (plaEnvDeuProAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			//No llama a ningun servicio
			PrintModel print = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().recomponerPlanilla(userSession, plaEnvDeuProAdapterVO.getPlaEnvDeuPro());

			// Envio el VO al request
			request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);

			baseResponsePrintModel(response, print);
			return null;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}
	}
	
	public ActionForward irRecomponerPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaEnvDeuProAdapterVO, request);
			
            // Tiene errores recuperables
			if (plaEnvDeuProAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			//No llama a ningun servicio
			
			// Envio el VO al request
			request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);

			return mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_RECOMPONER);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}
	}
	
	public ActionForward habilitarPlanilla(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaEnvDeuProAdapterVO, request);
			
            // Tiene errores recuperables
			if (plaEnvDeuProAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// llamada al servicio
			PlaEnvDeuProVO plaEnvDeuProVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().habilitarPlanilla(userSession, plaEnvDeuProAdapterVO.getPlaEnvDeuPro());
			
            // Tiene errores recuperables
			if (plaEnvDeuProVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (plaEnvDeuProVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// Tiene mensajes 
			if(plaEnvDeuProVO.hasMessage()){
				// Envio el VO al request
				request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				saveDemodaMessages(request, plaEnvDeuProVO);
				return mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaEnvDeuProAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}
	}
	
	public ActionForward irHabilitarConstanciasDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
			}
			
			// Llamada al servicio
			plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().getPlaEnvDeuProAdapterForHabilitarConstancias(userSession, plaEnvDeuProAdapterVO);

			// Seteo la accion de ver las constancias
			plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS);

			// Seteo la bandera para poder seleccionar las constancias y mostrar el boton para habilitarlas
			plaEnvDeuProAdapterVO.setModoSeleccionar(true);
				
			// Envio el VO al request
			request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			// Subo el apdater al userSession
			userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			 
			saveDemodaMessages(request, plaEnvDeuProAdapterVO);
	
			return baseForward(mapping, request, funcName, BaseConstants.ACT_VER, GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER, GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}		
	}
	
	public ActionForward habilitarConstanciasDeuda(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (plaEnvDeuProAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(plaEnvDeuProAdapterVO, request);
			
            // Tiene errores recuperables
			if (plaEnvDeuProAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// llamada al servicio
			plaEnvDeuProAdapterVO = GdeServiceLocator.getGdeAdmDeuJudServiceHbmImpl().habilitarConstancias(userSession, plaEnvDeuProAdapterVO);
			
            // Tiene errores recuperables
			if (plaEnvDeuProAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.infoString()); 
				saveDemodaErrors(request, plaEnvDeuProAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (plaEnvDeuProAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + plaEnvDeuProAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
			}
			
			// Tiene mensajes 
			if(plaEnvDeuProAdapterVO.hasMessage()){
				// Envio el VO al request
				request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				// Subo el apdater al userSession
				userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
				saveDemodaMessages(request, plaEnvDeuProAdapterVO);				
				return mapping.findForward(GdeConstants.FWD_PLAENVDEUPRO_VIEW_ADAPTER);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, PlaEnvDeuProAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, PlaEnvDeuProAdapter.NAME);
		}
	}
	
	public ActionForward traspasarConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_CONSTANCIA_DEUDA_JUDICIAL, GdeSecurityConstants.MTD_TRASPASAR_CONSTANCIA); 
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (plaEnvDeuProAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
		}

		// Setea el id de la constancia seleccionada para ir al traspaso
		String idConstanciaDeu = request.getParameter("selectedId");
		log.debug("Va a setear el idConstanciaDeu para ir al traspaso - idConstanciaDeu: "+idConstanciaDeu);
		userSession.getNavModel().putParameter(TraspasoDevolucionDeudaAdapter.CONSTANCIA_KEY, new Long(idConstanciaDeu));
		
		//plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_HABILITAR_PLANILLA);
		return baseForward(mapping, request, funcName,"irHabilitarConstanciasDeuda" , GdeConstants.ACTION_ADMINISTRAR_ENC_TRASPASO_DEVOLUCION_DEUDA, BaseConstants.ACT_AGREGAR);
	}
	
	public ActionForward verConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (plaEnvDeuProAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
		}

		// seteo de acciones para volver
		userSession.getNavModel().setAct(BaseConstants.ACT_VER);
		plaEnvDeuProAdapterVO.setAct(BaseConstants.ACT_VER);
		
		// Envio el VO al request
		request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
		// Subo el apdater al userSession
		userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);

		
		return baseForward(mapping, request, funcName, GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS, GdeConstants.ACTION_ADMIN_CONSTANCIADEU, BaseConstants.ACT_VER);		
	}

	public ActionForward recomponerConstancia(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (plaEnvDeuProAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
		}

		//acciones para volver
			//userSession.getNavModel().setAct(BaseConstants.ACT_VER);
		plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS);
		
		// Seteo la bandera para poder seleccionar las constancias y mostrar el boton para habilitarlas
		plaEnvDeuProAdapterVO.setModoSeleccionar(true);
		
		// Envio el VO al request
		request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
		// Subo el apdater al userSession
		userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
		
		return baseForward(mapping, request, funcName, GdeConstants.ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS, GdeConstants.ACTION_ADMIN_CONSTANCIADEU, GdeConstants.ACT_CONSTANCIADEU_RECOMPONER);		
	}
	
	public ActionForward volverDeVerConstancias(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (plaEnvDeuProAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
		}

		userSession.getNavModel().setAct(BaseConstants.ACT_VER);
		plaEnvDeuProAdapterVO.setAct(BaseConstants.ACT_VER);
		// Envio el VO al request
		request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
		// Subo el apdater al userSession
		userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);

		return refill(mapping, form, request, response);
			
	}
	
	public ActionForward volverDeHabilitarConstancias(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		
		UserSession userSession = canAccess(request, mapping, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, BaseSecurityConstants.VER); 
		if (userSession==null) return forwardErrorSession(request);
		
		// Bajo el adapter del userSession
		PlaEnvDeuProAdapter plaEnvDeuProAdapterVO = (PlaEnvDeuProAdapter) userSession.get(PlaEnvDeuProAdapter.NAME);
		
		// Si es nulo no se puede continuar
		if (plaEnvDeuProAdapterVO == null) {
			log.error("error en: "  + funcName + ": " + PlaEnvDeuProAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
			return forwardErrorSessionNullObject(mapping, request, funcName, PlaEnvDeuProAdapter.NAME); 
		}
		
		
		plaEnvDeuProAdapterVO.setValuesFromNavModel(userSession.getNavModel());
		plaEnvDeuProAdapterVO.setAct(GdeConstants.ACTION_ADM_PLAENVDEUPRO_HABILITAR_PLANILLA);
		
		//seteo para volver del habilitar planilla a la pantalla de busqueda
		plaEnvDeuProAdapterVO.setPrevAction("/gde/BuscarPlaEnvDeuPro");
		plaEnvDeuProAdapterVO.setPrevActionParameter(BaseConstants.ACT_BUSCAR);
		
		// Envio el VO al request
		request.setAttribute(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);
		// Subo el apdater al userSession
		userSession.put(PlaEnvDeuProAdapter.NAME, plaEnvDeuProAdapterVO);

		return refill(mapping, form, request, response);
			
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return baseVolver(mapping, form, request, response, PlaEnvDeuProAdapter.NAME);
		
	}

	public ActionForward refill(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			return baseRefill(mapping, form, request, response, funcName, PlaEnvDeuProAdapter.NAME);
			
	}

	
}
