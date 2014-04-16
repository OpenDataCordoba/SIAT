//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.struts;

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
import ar.gov.rosario.siat.def.iface.model.TipObjImpAtrAdapter;
import ar.gov.rosario.siat.exe.iface.model.ContribExeAdapter;
import ar.gov.rosario.siat.exe.iface.model.ContribExeSearchPage;
import ar.gov.rosario.siat.exe.iface.model.ContribExeVO;
import ar.gov.rosario.siat.exe.iface.service.ExeServiceLocator;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import ar.gov.rosario.siat.exe.view.util.ExeConstants;
import ar.gov.rosario.siat.pad.iface.model.BrocheVO;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.pad.view.util.PadConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.NavModel;

public final class AdministrarContribExeDAction extends BaseDispatchAction {

	private Log log = LogFactory.getLog(AdministrarContribExeDAction.class);

	public ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		String act = getCurrentAct(request);
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, act); 
		if (userSession == null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		ContribExeAdapter contribExeAdapterVO = null;
		String stringServicio = "";
		ActionForward actionForward = null;
		try {
			
			CommonKey commonKey = new CommonKey(navModel.getSelectedId());
			
			if (act.equals(BaseConstants.ACT_VER)) {
				stringServicio = "getContribExeAdapterForView(userSession, commonKey)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForView(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_MODIFICAR)) {
				stringServicio = "getContribExeAdapterForUpdate(userSession, commonKey)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForUpdate(userSession, commonKey);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);
			}
			if (act.equals(BaseConstants.ACT_ELIMINAR)) {
				stringServicio = "getContribExeAdapterForView(userSession, commonKey)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForView(userSession, commonKey);
				contribExeAdapterVO.addMessage(BaseError.MSG_ELIMINAR, ExeError.CONTRIBEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);				
			}
			
			// nos lleva a la pagina de confirmacion para quitar el Broche asociado a la cuenta.
			if (act.equals(ExeConstants.ACT_QUITAR_BROCHE_INIT)) {
				stringServicio = "getContribExeAdapterForView(userSession, commonKey)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForView(userSession, commonKey);				
				contribExeAdapterVO.addMessage(BaseError.MSG_QUITAR, PadError.BROCHE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);				
			}
			
			if (act.equals(BaseConstants.ACT_AGREGAR)) {
				stringServicio = "getContribExeAdapterForCreate(userSession)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForCreate(userSession);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_ACTIVAR)) {
				stringServicio = "getContribExeAdapterForView(userSession)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForView(userSession, commonKey);				
				contribExeAdapterVO.addMessage(BaseError.MSG_ACTIVAR, ExeError.CONTRIBEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);				
			}
			if (act.equals(BaseConstants.ACT_DESACTIVAR)) {
				stringServicio = "getContribExeAdapterForView(userSession)";
				contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterForView(userSession, commonKey);					
				contribExeAdapterVO.addMessage(BaseError.MSG_DESACTIVAR, ExeError.CONTRIBEXE_LABEL);
				actionForward = mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);				
			}

			if (log.isDebugEnabled()) log.debug(funcName + " salimos de servicio: " + stringServicio + " para " + act );
			// Nunca Tiene errores recuperables
			
			// Tiene errores no recuperables
			if (contribExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": servicio: " + stringServicio + ": " + contribExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Seteo los valores de navegacion en el adapter
			contribExeAdapterVO.setValuesFromNavModel(navModel);
						
			if (log.isDebugEnabled()) log.debug(funcName + ": " + ContribExeAdapter.NAME + ": "+ contribExeAdapterVO.infoString());
			
			// Envio el VO al request
			request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ContribExeAdapter.NAME, contribExeAdapterVO);
			 
			saveDemodaMessages(request, contribExeAdapterVO);
			
			return actionForward;
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}
	
	public ActionForward agregar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, BaseSecurityConstants.AGREGAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contribExeAdapterVO, request);

            // Tiene errores recuperables
			if (contribExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString()); 
				saveDemodaErrors(request, contribExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// llamada al servicio
			ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().createContribExe
				(userSession, contribExeAdapterVO.getContribExe());
			
            // Tiene errores recuperables
			if (contribExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeVO.infoString()); 
				saveDemodaErrors(request, contribExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (contribExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}

	public ActionForward modificar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contribExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (contribExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString()); 
				saveDemodaErrors(request, contribExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// llamada al servicio
			ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().updateContribExe
				(userSession, contribExeAdapterVO.getContribExe());
			
            // Tiene errores recuperables
			if (contribExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString()); 
				saveDemodaErrors(request, contribExeVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Tiene errores no recuperables
			if (contribExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribExeAdapter.NAME);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}

	public ActionForward eliminar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, BaseSecurityConstants.ELIMINAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// llamada al servicio
			ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().deleteContribExe
				(userSession, contribExeAdapterVO.getContribExe());
			
            // Tiene errores recuperables
			if (contribExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString());
				saveDemodaErrors(request, contribExeVO);				
				request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (contribExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribExeAdapter.NAME);
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}
	
	public ActionForward activar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, BaseSecurityConstants.ACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// llamada al servicio
			ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().activarContribExe
				(userSession, contribExeAdapterVO.getContribExe());
			
            // Tiene errores recuperables
			if (contribExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString());
				saveDemodaErrors(request, contribExeVO);				
				request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (contribExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribExeAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}	
	}
	
	public ActionForward desactivar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, BaseSecurityConstants.DESACTIVAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// llamada al servicio
			ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().desactivarContribExe
				(userSession, contribExeAdapterVO.getContribExe());
			
            // Tiene errores recuperables
			if (contribExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString());
				saveDemodaErrors(request, contribExeVO);				
				request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_VIEW_ADAPTER);
			}
			
			// Tiene errores no recuperables
			if (contribExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}
			
			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, ContribExeAdapter.NAME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}
	
	public ActionForward volver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		return baseVolver(mapping, form, request, response, ContribExeAdapter.NAME);
		
	}
	public ActionForward volverBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
	throws Exception {
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = userSession.getNavModel();
	
		navModel.setAct(BaseConstants.ACT_MODIFICAR);
		navModel.setPrevAction(ExeConstants.PATH_BUSCAR_CONTRIBEXE);
		navModel.setPrevActionParameter(BaseConstants.ACT_BUSCAR);

		return mapping.findForward(ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);
	}
	
	public ActionForward buscarContribuyente(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);
		
		UserSession userSession = canAccess(request, mapping, ExeSecurityConstants.ABM_CONTRIBEXE, BaseSecurityConstants.MODIFICAR); 
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contribExeAdapterVO, request);
			
            // Tiene errores recuperables
			if (contribExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString()); 
				saveDemodaErrors(request, contribExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeAdapter.NAME, contribExeAdapterVO);
			}

			return forwardSeleccionar(mapping, request, 
				ExeConstants.METOD_CONTRIBEXE_PARAM_CONTRIBUYENTE, PadConstants.ACTION_BUSCAR_PERSONA , false);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}

	}
	public ActionForward paramContribuyente(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();

		try {

			//bajo el adapter del usserSession
			ContribExeAdapter contribExeAdapterVO =  (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);

			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " " +
				"IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// recupero el id seleccionado por el usuario
			String selectedId = navModel.getSelectedId();

			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request
				request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER); 
			}

			// Seteo el id atributo seleccionado
			contribExeAdapterVO.getContribExe().getContribuyente().setId(new Long(selectedId));

			// llamo al param del servicio
			contribExeAdapterVO = ExeServiceLocator.getDefinicionService().getContribExeAdapterParamContribuyente
			(userSession, contribExeAdapterVO);

			// Tiene errores recuperables
			if (contribExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString()); 
				saveDemodaErrors(request, contribExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, 
						ContribExeAdapter.NAME, contribExeAdapterVO);
			}

			// Tiene errores no recuperables
			if (contribExeAdapterVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeAdapterVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, 
						ContribExeAdapter.NAME, contribExeAdapterVO);
			}

			// grabo los mensajes si hubiere
			saveDemodaMessages(request, contribExeAdapterVO);

			// Envio el VO al request
			request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);

			return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}

	public ActionForward asignarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		//ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
		//userSession.getNavModel().putParameter(BrocheSearchPage.PARAM_RECURSO_READONLY, contribExeAdapterVO.getContribExe().getRecurso());

		return forwardSeleccionar(mapping, request, ExeConstants.METOD_CONTRIBEXE_PARAM_ASIGNAR_BROCHE, PadConstants.ACTION_BUSCAR_BROCHE, false);
	}
	public ActionForward quitarBrocheInit(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String funcName = DemodaUtil.currentMethodName();
		return baseForward(mapping, request, 
				funcName, BaseConstants.ACT_INICIALIZAR, ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE, "quitarBrocheInit"); 
	}
	public ActionForward quitarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {
			// llamada al servicio
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);

			//Seteamos el NavModel para poder Volver
			NavModel navModel = userSession.getNavModel();
			navModel.setPrevAction(ExeConstants.PATH_BUSCAR_CONTRIBEXE);
			navModel.setPrevActionParameter(BaseConstants.ACT_BUSCAR);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			
			ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().paramQuitarBroche(userSession, contribExeAdapterVO.getContribExe());

			// Tiene errores recuperables
			if (contribExeVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeVO.infoString());
				saveDemodaErrors(request, contribExeVO);				
				request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);
			}

			// Tiene errores no recuperables
			if (contribExeVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + contribExeVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
			}

			// Fue Exitoso
			return forwardConfirmarOk(mapping, request, funcName, "", ExeConstants.PATH_ADMINISTRAR_CONTRIBEXE,  BaseConstants.ACT_INICIALIZAR, BaseConstants.ACT_MODIFICAR);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeSearchPage.NAME);
		}
	}
	public ActionForward paramAsignarBroche(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		NavModel navModel = userSession.getNavModel();

		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);

			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				return mapping.findForward(ExeConstants.VOLVER_BUSCAR_CONTRIBEXE);
			}

			// recupero el id seleccionado de broche por el usuario
			String selectedId = navModel.getSelectedId();
			String contribExeSelectedId = (contribExeAdapterVO.getContribExe().getId() != null) ? contribExeAdapterVO.getContribExe().getId().toString() : "0";
			
			// si el id esta vacio, pq selecciono volver, forwardeo al edit adapter
			if (StringUtil.isNullOrEmpty(selectedId)) {
				// Envio el VO al request y restauro el selectedId con el de cuenta, para cuando recarguemo el adapter.
				request.setAttribute(TipObjImpAtrAdapter.NAME, contribExeAdapterVO);
				//si tiene id voy a modificar, sino a agregar.
				if (contribExeAdapterVO.getContribExe().getId()!=null) { 
					navModel.setAct(BaseConstants.ACT_MODIFICAR);
					navModel.setSelectedId(contribExeSelectedId);
				} else {
					navModel.setSelectedId(contribExeSelectedId);
					navModel.setAct(BaseConstants.ACT_AGREGAR);
				}
				
				return mapping.findForward(ExeConstants.ACTION_ADMINISTRAR_CONTRIBEXE);
			}

			CommonKey keyBroche = new CommonKey(selectedId);

			//si tiene id lo modifico en la db, sino solo lo agrego al VO y se modificara en la db cuando haga 'agregar'
			if (contribExeAdapterVO.getContribExe().getId()!=null) { 
				ContribExeVO contribExeVO = ExeServiceLocator.getDefinicionService().paramAsignarBroche(userSession, contribExeAdapterVO.getContribExe(), keyBroche);			

				// Tiene errores recuperables
				if (contribExeVO.hasErrorRecoverable()) {
					log.error("recoverable error en: "  + funcName + ": " + contribExeVO.infoString());
					saveDemodaErrors(request, contribExeVO);				
					request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
					return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);
				}

				// Tiene errores no recuperables
				if (contribExeVO.hasErrorNonRecoverable()) {
					log.error("error en: "  + funcName + ": " + contribExeVO.errorString()); 
					return forwardErrorNonRecoverable(mapping, request, funcName, ContribExeAdapter.NAME, contribExeAdapterVO);
				}
				ActionForward forward = forwardConfirmarOk(mapping, request, funcName, "", ExeConstants.PATH_ADMINISTRAR_CONTRIBEXE,  BaseConstants.ACT_INICIALIZAR, BaseConstants.ACT_MODIFICAR);
				// restauro el selectedId con el de cuenta, para cuando recarguemos el adapter.
				navModel.setSelectedId(contribExeSelectedId);
				return forward;

			} else {
				BrocheVO brocheVO = ExeServiceLocator.getDefinicionService().paramGetBroche(userSession, keyBroche);
				contribExeAdapterVO.getContribExe().setBroche(brocheVO);
				navModel.setSelectedId(contribExeSelectedId);
				request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
				userSession.put(ContribExeAdapter.NAME, contribExeAdapterVO);
				return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);
			}
			

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeSearchPage.NAME);
		}
	}
	
	
	public ActionForward paramExencion(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug("entrando en " + funcName);

		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);

		try {
			// Bajo el adapter del userSession
			ContribExeAdapter contribExeAdapterVO = (ContribExeAdapter) userSession.get(ContribExeAdapter.NAME);
			
			// Si es nulo no se puede continuar
			if (contribExeAdapterVO == null) {
				log.error("error en: "  + funcName + ": " + ContribExeAdapter.NAME + " IS NULL. No se pudo obtener de la sesion");
				return forwardErrorSessionNullObject(mapping, request, funcName, ContribExeAdapter.NAME); 
			}

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(contribExeAdapterVO, request);
			
			if (!ModelUtil.isNullOrEmpty(contribExeAdapterVO.getContribExe().getExencion())){
				contribExeAdapterVO.setPoseeExencion(true);
			} else {
				contribExeAdapterVO.setPoseeExencion(false);
			}
			
			// Tiene errores recuperables
			if (contribExeAdapterVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + contribExeAdapterVO.infoString()); 
				saveDemodaErrors(request, contribExeAdapterVO);
				return forwardErrorRecoverable(mapping, request, userSession, ContribExeAdapter.NAME, contribExeAdapterVO);
			}

			// Envio el VO al request
			request.setAttribute(ContribExeAdapter.NAME, contribExeAdapterVO);
			// Subo el apdater al userSession
			userSession.put(ContribExeAdapter.NAME, contribExeAdapterVO);
			 
			saveDemodaMessages(request, contribExeAdapterVO);
			
			return mapping.findForward(ExeConstants.FWD_CONTRIBEXE_EDIT_ADAPTER);

		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, ContribExeAdapter.NAME);
		}
	}
}
