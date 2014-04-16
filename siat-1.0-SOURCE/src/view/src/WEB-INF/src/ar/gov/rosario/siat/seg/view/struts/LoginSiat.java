//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.view.struts;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.base.view.struts.BaseDispatchAction;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.cyq.iface.model.AbogadoVO;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.ef.buss.bean.Inspector;
import ar.gov.rosario.siat.ef.buss.bean.Investigador;
import ar.gov.rosario.siat.ef.buss.bean.Supervisor;
import ar.gov.rosario.siat.ef.iface.model.InspectorVO;
import ar.gov.rosario.siat.ef.iface.model.InvestigadorVO;
import ar.gov.rosario.siat.ef.iface.model.SupervisorVO;
import ar.gov.rosario.siat.gde.iface.model.MandatarioVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import ar.gov.rosario.siat.seg.iface.model.OficinaVO;
import ar.gov.rosario.siat.seg.iface.model.UsuarioSiatVO;
import ar.gov.rosario.siat.seg.iface.service.SegServiceLocator;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.NavModel;

/**
 * Clase que implementa los metodos protegidos de login/logut del SIAT.
 * IMPORTANTE: TODA la logica de login esta concetrada en esta clase!!!. Y asi debe seguir siendo.
 * Esta clase es capaz de manjear los login anonimos, y no Anonimos
 * ya sea desde la Intranet o desde la Web
 * Las clases LoginActoin, y LoginSslAction exponen los metodos protegidos de esta
 * clase, segun sean metodos que quedan expuestos para ser usados via SSL o no
 * @author Tecso Coop. Ltda.
 *
 */
 public class LoginSiat extends BaseDispatchAction {

	static private Log log = LogFactory.getLog(LoginSiat.class);
	static public final int FROM_WEB_LOGIN = 1;
	static public final int FROM_INTRANET_LOGIN = 2;

	/**
	 * Loguea a un tipo en el siat.
	 * Crea todo lo correspondiente al user session y lo mente en el HttpSession
	 * @return common contenedor de errores. Es una instancia de SweUserSession
	*/
	protected Common sessionLogin(HttpServletRequest request, UsuarioVO userVO, int loginFrom) throws Exception {
		HttpSession session = request.getSession(true); 
		UserSession userSession = null;
		SweUserSession sweUserSession = null;
		Common ret = new Common();
		boolean esAnonimo = false;
		
		esAnonimo = BaseSecurityConstants.USR_ANONIMO.equals(userVO.getUsername());

		//Si se intenta un login NO Anonimo, Verificamos si el loginFrom es coherente con la instancia.
		if (!esAnonimo) {
			if (loginFrom == LoginSiat.FROM_WEB_LOGIN && !SiatParam.isWebSiat()) {
				ret.addRecoverableError(SegError.NO_LOGIN_INSTANCIA_WEB);
				return ret;
			} else if (loginFrom == LoginSiat.FROM_INTRANET_LOGIN && !SiatParam.isIntranetSiat()) {
				ret.addRecoverableError(SegError.NO_LOGIN_INSTANCIA_INTRA);
				return ret;
			}
		}
		
		//seteamos el locale a es para struts 
		//(es necesario para los formatKey sobretodo el de currency)
		session.setAttribute(Globals.LOCALE_KEY, new Locale("es"));
		
		// Autenticamos al tipo y obtenemos sus permisos
		sweUserSession = SweServiceLocator.getSweService().login("SIAT", userVO, esAnonimo);
		if (sweUserSession.hasError()) {
			log.info("Login: Falla autenticacion SWE usuario: " + userVO.getUsername());
			ret.setListError(sweUserSession.getListError());
			ret.setListMessage(sweUserSession.getListMessage());
			ret.setErrorType(sweUserSession.getErrorType());
			return ret;
		}

		userSession = new UserSession();
		userSession.setUserName(userVO.getUsername());
		userSession.setLongUserName(sweUserSession.getLongUserName());
		userSession.setPermiteWeb(sweUserSession.getPermiteWeb());
		
		// Pasamos los datos del sweUserSession al UserSessino de Siat
		userSession.setIdsAccionesModuloUsuario(sweUserSession.getIdsAccionesModuloUsuario());
		userSession.setCodsRolUsuario(sweUserSession.getCodsRoles());
		
    	// Analisis de PermiteWeb para Logins no anonimos
    	// verificamos que el usario tenga la bandera permiteWeb si no es anonimo.
    	if (!esAnonimo && loginFrom == LoginSiat.FROM_WEB_LOGIN && !userSession.getPermiteWeb()) {
			log.info("Login: Falla login en web. Usuario OK pero no permiteWeb: " + userVO.getUsername());
			ret.addRecoverableError(SegError.NO_PERMITEWEB);
    		return ret;
    	}
		
    	// Si es el usuario anonimo seteamos el flag de anonimo
    	// actualizamos el tiempo de timeout
    	if (esAnonimo) {
    		userSession.setIsAnonimo(true);
    		session.setMaxInactiveInterval(SiatParam.getInteger(SiatParam.TIMEOUT_ANONIMO, 15) * 60);
    	} else {
    		userSession.setIsAnonimo(false);
    		session.setMaxInactiveInterval(SiatParam.getInteger(SiatParam.TIMEOUT_AUTENTICADO, 60) * 60);			
    	}    	
    	
		// Cargamos en userContext datos de UsuarioSiat
		UsuarioSiatVO usuarioSiat = SegServiceLocator.getSeguridadService().getUsuarioSiatForLogin(userSession);
    	if (usuarioSiat.hasError()) {
			log.info("Login: Falla busqueda de usuario SIAT: " + userVO.getUsername());
    		usuarioSiat.passErrorMessages(ret);
    		return ret;
    	}
		userSession.setIdUsuarioSiat(usuarioSiat.getId());
    	userSession.setIdRNPA(usuarioSiat.getIdRNPA());
    	
    	// Datos del Inspector - Si es pero no esta vigente lo setea en null
    	InspectorVO inspectorVO = usuarioSiat.getInspector();
		if(!ModelUtil.isNullOrEmpty(inspectorVO)){
			inspectorVO = (InspectorVO) Inspector.getById(inspectorVO.getId()).toVO(0, false);
			if(inspectorVO.getEsVigente())
				userSession.setIdInspector(inspectorVO.getId());
			else
				userSession.setIdInspector(null);
    	}else{
    		userSession.setIdInspector(null);
    	}
    	
    	// Datos del Supervisor - Si es pero no esta vigente lo setea en null
		SupervisorVO supervisorVO = usuarioSiat.getSupervisor();
		if(!ModelUtil.isNullOrEmpty(supervisorVO)){
			supervisorVO = (SupervisorVO) Supervisor.getById(supervisorVO.getId()).toVO(0, false);
			if(supervisorVO.getEsVigente())
				userSession.setIdSupervisor(supervisorVO.getId());
			else
				userSession.setIdSupervisor(null);
    	}else{
    		userSession.setIdSupervisor(null);
    	}

    	// Datos del investigador - Si es pero no esta vigente lo setea en null
    	InvestigadorVO investigadorVO = usuarioSiat.getInvestigador();
		if(!ModelUtil.isNullOrEmpty(investigadorVO)){
			investigadorVO = (InvestigadorVO) Investigador.getById(investigadorVO.getId()).toVO(0, false);
			if(investigadorVO.getEsVigente())
				userSession.setIdInvestigador(investigadorVO.getId());
			else
				userSession.setIdInvestigador(null);
    	}else{
    		userSession.setIdInvestigador(null);
    	}
    	
    	// Datos del Procurador
    	ProcuradorVO procurador = usuarioSiat.getProcurador();    	
    	if (procurador == null || procurador.getId() == null || procurador.getId() == 0) {
    		userSession.setIdProcurador(null);
    	} else {
    		userSession.setIdProcurador(procurador.getId());
    	}
    	
    	// Datos del Abogado
    	AbogadoVO abogado = usuarioSiat.getAbogado();    	
    	if (abogado == null || abogado.getId() == null || abogado.getId() == 0) {
    		userSession.setIdAbogado(null);
    	} else {
    		userSession.setIdAbogado(abogado.getId());
    	}
    	
    	//datos del canal
		if (loginFrom == LoginSiat.FROM_INTRANET_LOGIN) {
			userSession.setIdCanal(2L); //CMD, intranet este valor corresponde a tabla bal_canal, y bean Canal
		} else {
			userSession.setIdCanal(1L); //WEB intranet este valor corresponde a tabla bal_canal, y bean Canal
		}
    	
		// Datos del Mandatario
    	MandatarioVO mandatario = usuarioSiat.getMandatario();    	
    	if (mandatario == null || mandatario.getId() == null || mandatario.getId() == 0) {
    		userSession.setIdMandatario(null);
    	} else {
    		userSession.setIdMandatario(mandatario.getId());
    	}
    	// Datos del area
    	AreaVO area = usuarioSiat.getArea();
    	userSession.setIdArea(area.getId());
    	userSession.setDesArea(area.getDesArea());
    	
    	//Datos de la oficina solo si es intranet, si no no posee info de oficina
		if (loginFrom == LoginSiat.FROM_INTRANET_LOGIN) {
	    	if (area.getListOficina().size() == 1) {
	    		log.debug("AREA == 1" + area.getListOficina().size());
	    		OficinaVO oficina = area.getListOficina().get(0);
	    		userSession.setIdOficina(oficina.getId());
	    		userSession.setDesOficina(oficina.getDesOficina());
	    	} else if (area.getListOficina().size() > 1) {
	    		log.debug("AREA > 1" + area.getListOficina().size());
	    		userSession.setIdOficina(-1L); //Esto indica que deben buscarse mas oficinas. Esto se resuelve en los metodos que llaman a este metodo.
	    	} else {
	    		log.debug("AREA == 0" + area.getListOficina().size());
	    		userSession.setIdOficina(null); //Esto indica que el arean no posee ninguna oficina cargada
	    	}
		} else {
	    	userSession.setIdOficina(null);
	    	userSession.setDesOficina("");			
		}
    	    	
    	//algo de logs una vez que se loguearon
    	log.info("Login: OK: Usuario:" + userSession.getUserName() + "(" + userSession.getLongUserName() + ") Area:" + userSession.getDesArea() + " Web Session Timeout:" + session.getMaxInactiveInterval() + "min Info:" + userSession.infoString());
    	
    	// cosas de la navegacion
		NavModel navModel = new NavModel();
		navModel.setPrevAction("/SiatIndex");

		// establecemos cadena de usuario logueado
		userSession.setNavModel(navModel);	

		// subo el userSession a la session
		session.setAttribute("userSession", userSession);
		return ret;
	}

	/**	
	 * Maneja el request de login enviado desde los formularios de login (Web o Intranet)
	 */
	protected ActionForward login(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response, int fromLogin) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		HttpSession session = request.getSession(true); 
		UserSession userSession = null;
		Common control = null; 
		try {			
			UsuarioVO userVO = new UsuarioVO("","",session.getId());

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(userVO, request);

			// Tiene errores recuperables
			if (userVO.hasErrorRecoverable()) {
				saveDemodaErrors(request, userVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioVO.NAME, userVO);
			}

			// puede arrajoar una excecion porque la db este baja o no se pueda conectar.
			// en ese caso enviamos enviamos mensaje de off.
			try {
				control = sessionLogin(request, userVO, fromLogin);
			} catch (Exception e) {
				if (SiatParam.isWebSiat()) { // && "0".equals(SiatParam.getString("webSiatOn"))){
					return mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE);
				}
				if (SiatParam.isIntranetSiat()) { // && "0".equals(SiatParam.getString("intraSiatOn"))){
					return mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE);
				}
				/*
				String err = e.getMessage();
				if (e.getCause() != null) {
					err += e.getCause().getMessage();
				}
				control.addMessageValue("No se pudo concretar login: " + err);
				*/
			}
			
			// Tiene errores recuperables
			if (control.hasErrorRecoverable()) {
				saveDemodaErrors(request, control);
				request.setAttribute( UsuarioVO.NAME, userVO);
				if (fromLogin == LoginSiat.FROM_WEB_LOGIN) {
					return mapping.findForward(SegConstants.FWD_WEB_LOGIN_FORM);
				} else {
					return mapping.findForward(SegConstants.FWD_INTRANET_LOGIN_FORM);
				}
			}

			// Tiene errores no recuperables
			if (control.hasErrorNonRecoverable()) {
				saveDemodaErrors(request, control);
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioVO.NAME, userVO);
			}

			if (fromLogin == LoginSiat.FROM_INTRANET_LOGIN) {
				
				userSession = (UserSession) session.getAttribute("userSession");
				
				if(userSession.getIdArea()!=null){
					// Verifica permisos para ver solicitudes emitidas del area
					if(canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, 
															CasSecurityConstants.ACT_VER_EMITIDAS_MENU)!=null){
						userSession.setCanAccessSolEmitidasMenu(true);					
					}else{
						userSession.setCanAccessSolEmitidasMenu(false);
					}
					
					// Verifica permisos para ver solicitudes pendientes del area
					if(canAccess(request, mapping, CasSecurityConstants.ABM_SOLICITUD, 
																CasSecurityConstants.ACT_VER_PEND_MENU)!=null){
						userSession.setCanAccessSolPendMenu(true);
						
					}else{
						userSession.setCanAccessSolPendMenu(false);
					}
				}else{
					userSession.setCanAccessSolPendMenu(false);
					userSession.setCanAccessSolEmitidasMenu(false);
				}
				
				// Vamos O al menu o a la pantalla de seleeccion de ofcina de area
				// en caso de que el area del usuario tenga mas de una oficina.
				if (userSession.getIdOficina() != null && userSession.getIdOficina().longValue() == -1) { //mas de una oficina!, ver en sessionLogin
					UsuarioSiatVO usuarioSiat = SegServiceLocator.getSeguridadService().getUsuarioSiatForLogin(userSession);
					AreaVO area = usuarioSiat.getArea();
					request.setAttribute(AreaVO.NAME, area);
					return mapping.findForward(SegConstants.FWD_LOGIN_SELEC_OFICINA);
				}
				
				/*if(userSession.getCanAccessSolPendMenu()){			
						// Si tiene solicitudes pendientes las muestra (El permiso ya lo verifico antes)
						if(Solicitud.tienePendientesArea(userSession.getIdArea()))
							return mapping.findForward(SegConstants.ACTION_SIATMENU_SOLPENDIENTES);						
				}*/
			}
			
			return mapping.findForward(SegConstants.ACTION_SIATMENU);			
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioVO.NAME);
		}
	}

	/**
	 * Invalida la session de un usuario siat. 
	 */
	protected ActionForward logout(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		HttpSession session = canAccess(request); 
		if (session == null) return forwardErrorSession(mapping, request);

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			session.invalidate();
			return mapping.findForward(BaseConstants.FWD_SIATINDEX);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioVO.NAME);
		}
	}

	/**
	 * Loguea a un usuario anonimo y luego forwardea a la url pasada en los
	 * parametros.
	 * Esta funcion es el punto de entrada a las funcionalidades de siat anonimas.
	 * Luego de pasa por este method llegaremos al siat con una session anonima
	 * recien creada.
	 * Un usuario Anonimo solo puede acceder desde la Web
	 */
	protected ActionForward anonimo(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response)
	throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// creamos http session
		HttpSession session = request.getSession();
		if (session != null) {
			session.invalidate();
		}
		session = request.getSession(true);
		
		try {
			// logueamo al anonimo
			UsuarioVO userVO = new UsuarioVO(BaseSecurityConstants.USR_ANONIMO, BaseSecurityConstants.PWD_ANONIMO, session.getId());
			Common control = null;

			try {
				control = sessionLogin(request, userVO, FROM_WEB_LOGIN);
			} catch (Exception e) {
				if (SiatParam.isWebSiat()) { // && "0".equals(SiatParam.getString("webSiatOn"))){
					return mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE);
				}
				if (SiatParam.isIntranetSiat()) { // && "0".equals(SiatParam.getString("intraSiatOn"))){
					return mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE);
				}
			}

			// Tiene errores no recuperables
			if (control.hasError()) {
				log.error("No se pudo loguear a un usuario anonimo. Esto se debe a una inconsistencia de datos entre los usuarios de segweb y los SWE. " +
						"Verfique que exista el usuario/password en swe:" 
						+ BaseSecurityConstants.USR_ANONIMO + "/" + BaseSecurityConstants.PWD_ANONIMO);
				saveDemodaErrors(request, control);
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioVO.NAME, control);
			}

			// forwadeamos a donde quiere ir.
			String url = request.getParameter("url");
			UserSession userSession = this.getCurrentUserSession(request, mapping);
			userSession.setUrlReComenzar(url);
			return new ActionForward(url);
		} catch (Exception e) {
			return baseException(mapping, request, funcName, e, UsuarioVO.NAME);			
		}
	}

	/**
	 * Muestra la p�gina para ingresar datos de usuario, contrase�a y puesto trabajo en el caso de que sea por intranet.
	 * TODO: hacer que segnu fromLogin muestro o no la parte de puesto de trabajo
	 */
	protected ActionForward init(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response, int fromLogin)
	throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		
/*/ Permitimos "apagar" la aplicacion por instancia
if (SiatParam.isWebSiat() && "0".equals(SiatParam.getString("webSiatOn"))){
	return mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE);
}
if (SiatParam.isIntranetSiat() && "0".equals(SiatParam.getString("intraSiatOn"))){
	return mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE);
}*/
		
		try {
			UsuarioVO userVO = new UsuarioVO();
			request.setAttribute(UsuarioVO.NAME, userVO);
			if (fromLogin == FROM_WEB_LOGIN) {
				return mapping.findForward(SegConstants.FWD_WEB_LOGIN_FORM);
			} else {
				return mapping.findForward(SegConstants.FWD_INTRANET_LOGIN_FORM);
			}
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioVO.NAME);
		}
	}
	
	/**
	 * Selecciona una oficina durante el login de usuario.
	 */
	protected ActionForward loginSelecOficina(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		HttpSession session = request.getSession(true); 
		UserSession userSession = null;
		
		try {
			userSession = (UserSession) session.getAttribute("userSession");
			// volvemos a obtener informacion del area agregamos informacion de la oficina
			UsuarioSiatVO usuarioSiat = SegServiceLocator.getSeguridadService().getUsuarioSiatForLogin(userSession);
			AreaVO area = usuarioSiat.getArea();
			
			//buscamos oficina dentra de area con el id submitido.
			long idOficina = Long.parseLong(request.getParameter("oficina.id"));
			for (OficinaVO of : area.getListOficina()) {
				if (of.getId().longValue() == idOficina) {
					userSession.setIdOficina(of.getId());
					userSession.setDesOficina(of.getDesOficina());
					break;
				}
			}
			
			return mapping.findForward(SegConstants.ACTION_SIATMENU);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioVO.NAME);
		}
	}
	
	/**
	 * Inicializa el formulario de cambio de password
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 */
	protected ActionForward changePassInit(ActionMapping mapping,
			 ActionForm form,
			 HttpServletRequest request,
			 HttpServletResponse response) throws Exception {
	
		UsuarioVO usuarioVO = new UsuarioVO();
		request.setAttribute(UsuarioVO.NAME, usuarioVO);
		
		return mapping.findForward(SegConstants.FWD_CHANGE_PASS_FORM);

	}
	
	
	/**
	 * Realiza el cambio de password
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward changePass(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		HttpSession session = request.getSession(true);
		
		try {
			UsuarioVO userVO = new UsuarioVO("","", session.getId());

			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(userVO, request);

			
			if (StringUtil.isNullOrEmpty(userVO.getNewPassword())){
				userVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, SegError.USUARIO_NEWPASS_LABEL);
			}
			
			if (StringUtil.isNullOrEmpty(userVO.getNewPasswordReentry())){
				userVO.addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, SegError.USUARIO_NEWPASSREENTRY_LABEL);
			}
			
			if (!StringUtil.isNullOrEmpty(userVO.getNewPassword()) && 
					!StringUtil.isNullOrEmpty(userVO.getNewPasswordReentry()) &&
					!userVO.getNewPassword().equals(userVO.getNewPasswordReentry())) {
				userVO.addRecoverableError(SegError.MSG_NO_COINICEN_ERROR);
			}
				
			// Tiene errores recuperables
			if (userVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + userVO.infoString()); 
				saveDemodaErrors(request, userVO);
				request.setAttribute(UsuarioVO.NAME, userVO);
				return mapping.findForward(SegConstants.FWD_CHANGE_PASS_FORM);
			}
			
			// llamada al servicio
			UsuarioVO usuarioVO = SegServiceLocator.getSeguridadService().changePass(userSession, userVO);
			
            // Tiene errores recuperables
			if (usuarioVO.hasErrorRecoverable()) {
				log.error("recoverable error en: "  + funcName + ": " + usuarioVO.infoString()); 
				saveDemodaErrors(request, usuarioVO);
				request.setAttribute(UsuarioVO.NAME, usuarioVO);
				return mapping.findForward(SegConstants.FWD_CHANGE_PASS_FORM);
			}
			
			// Tiene errores no recuperables
			if (usuarioVO.hasErrorNonRecoverable()) {
				log.error("error en: "  + funcName + ": " + usuarioVO.errorString()); 
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioVO.NAME, usuarioVO);
			}
			
			// Fue Exitoso
			NavModel navModel = userSession.getNavModel();
				
			//le seteo la accion a donde ir en la pantalla de confirmacion al navModel
			navModel.setConfAction("/seg/SiatMenu");
			navModel.setConfActionParameter("build");
					
			return this.forwardMessage(mapping, navModel, NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioVO.NAME);
		}
	}

	/**
	 * Volver desde el formulario de cambio de password
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward volver(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		return mapping.findForward(SegConstants.ACTION_SIATMENU);
	}
	
}
