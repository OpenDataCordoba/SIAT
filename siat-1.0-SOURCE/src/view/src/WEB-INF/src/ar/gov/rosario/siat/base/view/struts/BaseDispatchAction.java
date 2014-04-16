//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.view.struts;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.MDC;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.base.iface.util.SiatCache;
import ar.gov.rosario.siat.base.view.util.BaseConstants;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.pro.iface.model.EnvioArchivosAdapter;
import ar.gov.rosario.siat.pro.view.util.ProConstants;
import ar.gov.rosario.swe.iface.model.SweContext;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.CommonKey;
import coop.tecso.demoda.iface.model.CommonNavegableView;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.NavItem;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PageModel;
import coop.tecso.demoda.iface.model.PrintModel;


public class BaseDispatchAction extends DispatchAction {
	static private String ERROR_KEY = "canAccess.error";
	static private String ERROR_FORWARD_KEY = "canAccess.forward";
	static private String ERROR_USERSESSION = "noUserSession";
	static private String ERROR_NOACCESS = "noAccess";

	private Log log = LogFactory.getLog(BaseDispatchAction.class);	

	// Iniciamos el SweCache para tener siempre disponible
	// los datos de Contexto de seguridad de la aplicacion Swe
	
	static {
		Log log = LogFactory.getLog(BaseDispatchAction.class);
		try {
			//DefServiceLocator.getConfiguracionService().initializeSiat();
		} catch (Exception e) {
			log.error("**************************************");
			log.error("ERROR:");
			log.error("No se pudo Inicializar el contexto de seguridad SWE de SIAT.");
			log.error("El comportamiento de la aplicacion es inesperado.");
			log.error("El error fue: ", e);
			log.error("**************************************");

			System.out.println("**************************************");
			System.out.println("ERROR:");
			System.out.println("No se pudo Inicializar el contexto de seguridad de SWE de SIAT.");
			System.out.println("El comportamiento de la aplicacion es inesperado.");
			System.out.println("El error fue: " + e);
			e.printStackTrace();
			System.out.println("**************************************");
		}
	}
	
	//----------------------------------------------------//
	//		Funciones de Acceso	                          //		
	//----------------------------------------------------//
	
	protected HttpSession canAccess(HttpServletRequest request) {		
		HttpSession session = request.getSession();
		return session;
	}
	
		
	/**
	 * Toma los siguientes valores del request y los pasa al userMap del userSession validando nulidad. 
	 * 
	 * reqAttMethod, reqAttSelectedId, reqAttPageNumber, reqAttIsSubmittedForm
	 * 
	 * Chequea acceso mediante SWE con actionName y methodName
	 * 
	 * @param request
	 * @param mapping
	 * @param actionName
	 * @param methodName
	 * @return UserSession
	 * @throws Exception
	 */
	protected UserSession canAccess(HttpServletRequest request, ActionMapping mapping, String actionName, 
		String methodName) throws Exception {

		log.info("canAccess() : path:" + mapping.getPath() + " accion-swe:" + actionName + " metodo-swe:" + methodName);
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession == null) {
			return null;
		}
		
		// seteo el metodo y la accion swe actual
		userSession.setAccionSWE(actionName);
		userSession.setMetodoSWE(methodName);
		
		// Permitimos "apagar" la aplicacion por instancia
		if (SiatParam.isWebSiat() && "0".equals(SiatParam.getString("webSiatOn")) && !userSession.getEsAdmin()){
			request.setAttribute(ERROR_FORWARD_KEY, mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE));
			return null;
		}
		
		if (SiatParam.isIntranetSiat() && "0".equals(SiatParam.getString("intraSiatOn")) && !userSession.getEsAdmin()){
			request.setAttribute(ERROR_FORWARD_KEY, mapping.findForward(BaseConstants.FWD_SIAT_OFFLINE));
			return null;
		}
		
		if (!this.hasAccess(userSession, actionName, methodName)) {
			request.setAttribute(ERROR_KEY, ERROR_NOACCESS);
			request.setAttribute(ERROR_FORWARD_KEY, mapping.findForward(BaseConstants.FWD_MSG));
			
			
			NavModel navModel = userSession.getNavModel();
			//le seteo la accion a donde ir en la pantalla de confirmacion al navModel
			
			navModel.setConfAction(navModel.getPrevAction());
			navModel.setConfActionParameter(navModel.getPrevActionParameter());
			
			navModel.setMessageType(NavModel.NAVMODEL_MESSAGE_TYPE_ERROR);
			navModel.setMessageStr("Acceso Denegado.");
			return null;
		} 
		
		return userSession;
	}
	
	protected Boolean hasAccess(UserSession userSession, String actionName, String methodName) throws Exception {
		
		Boolean  hasAccess = true;
		SweContext ctx = SiatCache.getInstance().getSweContext();
		long hasAccessLong = ctx.hasAccess(userSession.getIdsAccionesModuloUsuario(), actionName, methodName);
		if (hasAccessLong==Common.HASACCESS_SINACCESO || hasAccessLong==Common.HASACCESS_NOMBRESNULOS_ERROR ) { 
			log.info("canAccess():Access Denied " + userSession.getUserName() + " " + actionName + " " + methodName);
			hasAccess = false;
		}
		
		return hasAccess;
	}
	
	
	/**
	 * Devuelve el act seteado en el navmodel del userSession.
	 * 
	 * @param request
	 * @param mapping
	 * @return
	 * @throws Exception
	 */
	protected String getCurrentAct(HttpServletRequest request) throws Exception {
		HttpSession session = request.getSession();
		
		UserSession userSession = (UserSession)session.getAttribute("userSession");
		if (userSession == null) {
			return "";
		}
		
		NavModel navModel = userSession.getNavModel();
		if (navModel == null) {
			throw new Exception("navModel null. No se pudo obtener el navModel.");
		} else {
			return navModel.getAct();
		}
	}
	
	/**
	 * Devuelve el UserSession, si no lo encuentra setea valores en el request y devuelve null
	 * 
	 * @param request
	 * @param mapping
	 * @return
	 */
	protected UserSession getCurrentUserSession(HttpServletRequest request, ActionMapping mapping) 
		throws Exception{
		
		String funcName = "getCurrentUserSession";
		UserSession userSession = null;
		HttpSession session = request.getSession();

		log.info(funcName + "(): path:" + mapping.getPath());
		userSession = (UserSession) session.getAttribute("userSession");
		if (userSession == null) {
			log.info(funcName + " - " + "No se encontro UserSession en HttpSession.");
			request.setAttribute(ERROR_KEY, ERROR_USERSESSION);
			request.setAttribute(ERROR_FORWARD_KEY, forwardErrorSession(mapping, request));				
			return null;
		}
		
		userSession.setIpRequest(request.getRemoteAddr());
		// Metemos el userContext en el TLS
		// hasAccess() requiere que este el UserContext este en el TLS
		DemodaUtil.setCurrentUserContext(userSession);
		MDC.put("userName", userSession.getUserName());
		MDC.put("sweAccionMetodo", userSession.getAccionSWE() + ":" + userSession.getMetodoSWE());
		NavModel navModel = userSession.getNavModel();
		
		if (navModel == null) {
			throw new Exception("navModel null. No se pudo obtener el navModel.");
		}

		if (navModel.getPrevAction().equals("")) {
			throw new Exception("navModel.getPrevAction vacio. No puede continuar.");
		}

		String reqAttMethod = request.getParameter("method");
		if(reqAttMethod==null) reqAttMethod = "";
		
		String reqAttSelectedId = request.getParameter("selectedId");
		if(reqAttSelectedId==null) reqAttSelectedId = "";
		
		String reqAttPageNumber = request.getParameter("pageNumber");
		if(reqAttPageNumber==null) reqAttPageNumber = "";
		
		String reqAttIsSubmittedForm = request.getParameter("isSubmittedForm");
		if(reqAttIsSubmittedForm==null) reqAttIsSubmittedForm = "false";
		
		userSession.put("reqAttMethod", reqAttMethod);
		userSession.put("reqAttSelectedId", reqAttSelectedId);
		userSession.put("reqAttPageNumber", reqAttPageNumber);
		userSession.put("reqAttIsSubmittedForm", reqAttIsSubmittedForm);

		String act = request.getParameter("act");
		if (act != null && !"".equals(act)) {
			navModel.setAct(act);
		}
		
		// se pasa el selectedId y el metod actual al navModel
		if(!StringUtil.isNullOrEmpty(reqAttSelectedId)){
			navModel.setSelectedId(reqAttSelectedId);
		}
		
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(funcName);
			sb.append(" navModel=").append(navModel.infoString());
			sb.append("userSession=OK;");
			sb.append("reqAttMethod=").append(reqAttMethod);
			sb.append(";reqAttSelectedId=").append(reqAttSelectedId);
			sb.append(";reqAttPageNumber=").append(reqAttPageNumber);
			sb.append(";reqAttIsSubmittedForm=").append(reqAttIsSubmittedForm);
			log.debug(sb.toString());
		}
		
		return userSession;
	} 
	
	protected UserSession canAccess(HttpServletRequest request, ActionMapping mapping, String methodName) throws Exception {
		return null;
	}

	//----------------------------------------------------//
	//		Funciones de navegacion y generales           //		
	//----------------------------------------------------//	

	/**
	 * Vuelve al lugar donde indique el model(Adapter o SearchPage), 
	 * y remueve el model del userSession.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param VOName
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward baseVolver(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response, String VOName)
		throws Exception {
		
		String funcName = "baseVolver";
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();		
		try {
			CommonNavegableView cnv = (CommonNavegableView) userSession.get(VOName);
			
			// Si es null, viene de un Refresh o esta mal seteado
			if (cnv == null) {
				String currentActionForRefresh = (String)userSession.get("currentActionForRefresh");
				String currentParameterForRefresh = (String) userSession.get("currentParameterForRefresh");
				
				log.debug(funcName + ": intentando volver a :" + currentActionForRefresh);
				return new ActionForward (StringUtil.getActionPath(currentActionForRefresh, 
						currentParameterForRefresh));
			} else {
				// Si no es null, setemaos los parametros para el Refresh
				userSession.put("currentActionForRefresh", cnv.getPrevAction());
				userSession.put("currentParameterForRefresh", cnv.getPrevActionParameter());
			
				userSession.remove(VOName);
			
				// limpio el mapa de parametros, la lista de excluidos el selected id
				navModel.clearParametersMap();
				navModel.cleanListVOExcluidos();
				navModel.setSelectedId("");
			}	
			
			log.debug(funcName + ": intentando volver a :" + cnv.getAccionVolver());
			return new ActionForward (cnv.getAccionVolver());
		} catch (Exception e) {
			log.error("Exception - ", e);
			e.printStackTrace();
			// falta definir llamada o no a logout 
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
	
	/** Esta base confirmar forwardea al prevAction y prevActionParameter
	 *  definido en el VOName pasado como parametro
	 * 
	 * @param mapping
	 * @param request
	 * @param act
	 * @param vOAttributeName
	 * @param selectedId
	 * @param messageType
	 * @param messageStr
	 * @return
	 * @throws Exception
	 */
	protected ActionForward baseConfirmar(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String VOName, 
		int messageType, String messageStr)
		throws Exception {

		String funcName = "baseConfirmar." + funcNameOrigen;
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		try {

			// recupero el navModel del usserSession
			NavModel navModel = userSession.getNavModel();
			
			//recupero el CommonNavegableView del usserSession
			CommonNavegableView cnv = (CommonNavegableView) userSession.get(VOName);			
			
			//le seteo la accion a donde ir en la pantalla de confirmacion al navModel
			navModel.setConfAction(cnv.getPrevAction());
			navModel.setConfActionParameter(cnv.getPrevActionParameter());
			
			// saco el VO del usser session
			userSession.remove(VOName);
				
			return this.forwardMessage(mapping, navModel, messageType, messageStr);
			
		} catch (Exception e) {
			e.printStackTrace();
			// falta log, agregar errores
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
	
	/**Setea el tipo de mansage y la descripcion del 
	 * mensaje y me forwrdea al mismo
	 * 
	 * @param mapping
	 * @param navModel
	 * @param messageType
	 * @param messageStr
	 * @return
	 * @throws Exception
	 */
	protected ActionForward forwardMessage(ActionMapping mapping,
		NavModel navModel, int messageType, String messageStr)
		throws Exception {
		
		navModel.setMessageType(messageType);
		
		if (StringUtil.isNullOrEmpty(messageStr)) {
			messageStr = BaseConstants.SUCCESS_MESSAGE_DESCRIPTION;
		}
		navModel.setMessageStr(messageStr);

		return mapping.findForward(BaseConstants.FWD_MSG);
		
	}
	
	/** Forwardea al mensaje de OK de una operacion Create, Update o Delete
	 * 
	 * @param mapping
	 * @param request
	 * @param funcName
	 * @param vOAttributeName
	 * @return
	 */
	protected ActionForward forwardConfirmarOk(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String VOName) throws Exception {

		return baseConfirmar(mapping, request, funcNameOrigen, VOName, 
			NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);

	}

	/** Forwardea al mensaje de OK y vuelve al NavItem seleccionado
	 * 
	 * @param mapping
	 * @param request
	 * @param funcName
	 * @param vOAttributeName
	 * @return
	 */
		
	protected ActionForward forwardConfirmarOkNavItem(ActionMapping mapping, HttpServletRequest request, 
		NavItem navItem, String VOName) throws Exception {
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		NavModel navModel = new NavModel();
		
		//le seteo la accion a donde ir en la pantalla de confirmacion al navModel
		navModel.setConfAction(navItem.getAccion());
		navModel.setConfActionParameter(navItem.getMetodo());
		if (navItem.getSelectedId() != null && navItem.getSelectedId() != 0L) {
			navModel.setConfActionParameter(navItem.getMetodo() + "&selectedId=" + navItem.getSelectedId());
		}

		userSession.remove(VOName);
		return this.forwardMessage(mapping, navModel, NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);
	}

	
	
	/** Este metodo de dirige a la pantalla de confirmacion ok, y luego
	 *  al actionConfirmacion, methodConfirmacion y act pasados como 
	 *  parametros
	 * 
	 * @param mapping
	 * @param request
	 * @param funcName
	 * @param VOName
	 * @param pathConfirmacion PATH de action a donde ir luego de confirmacion. No se puede utilizan un action de struts
	 * @param methodConfirmacion
	 * @param act
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward forwardConfirmarOk(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String VOName, String pathConfirmacion, String methodConfirmacion, String act) 
		throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);

		try {

			// recupero el navMocel del usserSession
			NavModel navModel = userSession.getNavModel();
			
			//le seteo la accion a donde ir al navModel
			navModel.setConfAction(pathConfirmacion);
			navModel.setConfActionParameter(methodConfirmacion);
			navModel.setAct(act);
			
			// saco el vo del usserSession
			userSession.remove(VOName);
			
			// me dirije al mensaje de confirmacion OK
			return this.forwardMessage(mapping, navModel, 
				NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, BaseConstants.SUCCESS_MESSAGE_DESCRIPTION);

			
		} catch (Exception e) {
			e.printStackTrace();
			// falta log, agregar errores
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}

	}
	
	/** 
	 * Forwardea a la accion y el metodo definidos para seleccionar.
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param act
	 * @param vONameToRemove
	 * @return
	 * @throws Exception
	 */
	protected ActionForward baseSeleccionar(ActionMapping mapping, HttpServletRequest request, 
		HttpServletResponse response, String quitarEstaVariable, String VOName) throws Exception {

		String funcName = "baseSeleccionar ";
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel =  userSession.getNavModel();
		
		try {
			// recupero la accion a donde volver cuando se selecciona
			CommonNavegableView cnv = (CommonNavegableView) userSession.get(VOName);

			// Si es null, venimos de un refresh o esta mal seteado
			if (cnv == null) {
				String currentActionForRefresh = (String)userSession.get("currentActionForRefresh");
				String currentParameterForRefresh = (String) userSession.get("currentParameterForRefresh");

				log.debug(funcName + ": intentando volver a :" + currentActionForRefresh);

				return new ActionForward (StringUtil.getActionPath(currentActionForRefresh,	currentParameterForRefresh));
			}else {
				// Si no es null, setemaos los parametros para el Refresh
				userSession.put("currentActionForRefresh", cnv.getPrevAction());
				userSession.put("currentParameterForRefresh", cnv.getPrevActionParameter());

				// elimino el VO del usserSession
				userSession.remove(VOName);

				// seteo el act en el nav model
				navModel.setAct(cnv.getSelectAct());

				// limpio el mapa de parametros del navModel y la lista de excluidos
				navModel.clearParametersMap();
				navModel.cleanListVOExcluidos();
			}
			
			// forwardeo a la accion defenido para la seleccion
			return new ActionForward (cnv.getAccionSeleccionar());			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + " exception - ", e);
			// falta log, agregar errores
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
	

	/**
	 * Forwardea a la accion y el metodo definidos para seleccionar.
	 * Surge por issue #8312: GDE - Gestión de Deuda/Convenios. Gestión por Contribuyente. Navegación
	 * El parametro "removeVO" indica si se debe eliminar o no el VO de la sesion.
	 * Si removeVO = true, el metodo funciona de igual manera baseSeleccionar original.
	 * 
	 * @param mapping
	 * @param request
	 * @param response
	 * @param quitarEstaVariable
	 * @param VOName
	 * @param removeVO
	 * @return
	 * @throws Exception
	 */
	protected ActionForward baseSeleccionar(ActionMapping mapping, HttpServletRequest request, 
		HttpServletResponse response, String quitarEstaVariable, String VOName, Boolean removeVO) throws Exception {

		String funcName = "baseSeleccionar ";
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		NavModel navModel =  userSession.getNavModel();
		
		try {
			// recupero la accion a donde volver cuando se selecciona
			CommonNavegableView cnv = (CommonNavegableView) userSession.get(VOName);
			log.debug(funcName + ": removeVO =" + removeVO);

			// Si es null, venimos de un refresh o esta mal seteado
			if (cnv == null) {
				String currentActionForRefresh = (String)userSession.get("currentActionForRefresh");
				String currentParameterForRefresh = (String) userSession.get("currentParameterForRefresh");

				log.debug(funcName + ": intentando volver a :" + currentActionForRefresh);

				return new ActionForward (StringUtil.getActionPath(currentActionForRefresh, currentParameterForRefresh));
			} else {
				// Si no es null, setemaos los parametros para el Refresh
				userSession.put("currentActionForRefresh", cnv.getPrevAction());
				userSession.put("currentParameterForRefresh", cnv.getPrevActionParameter());

				// elimino el VO del usserSession
				if(removeVO) userSession.remove(VOName);

				// seteo el act en el nav model
				navModel.setAct(cnv.getSelectAct());

				// limpio el mapa de parametros del navModel y la lista de excluidos
				navModel.clearParametersMap();
				navModel.cleanListVOExcluidos();
			}
			
			// forwardeo a la accion defenido para la seleccion
			return new ActionForward (cnv.getAccionSeleccionar());			
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + " exception - ", e);
			// falta log, agregar errores
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
	
	/** Setea los valores iniciales en el navModel y llama de nuevo al 
	 *  metodo inicializar para que redibuje la jsp.
	 *  
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param funcNameOrigen
	 * @param vONameToRefill
	 * @return
	 * @throws Exception
	 */
	protected ActionForward baseRefill(ActionMapping mapping, ActionForm form, HttpServletRequest request, 
		HttpServletResponse response, String funcNameOrigen, String vONameToRefill) throws Exception {

		String funcName = "baseRefill";
 
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			
			NavModel navModel = userSession.getNavModel();
			// recupero la accion a donde volver cuando se seleciona
			CommonNavegableView cnv = (CommonNavegableView) userSession.get(vONameToRefill);
			navModel.setValuesFromCommonNavegableView(cnv);
			
			return this.inicializar(mapping, form, request, response);

		} catch (Exception e) {
			e.printStackTrace();
			log.error(funcName + " exception - ", e);
			// falta log, agregar errores
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}

	/** Este metodo es solo utilizado como plantilla en esta clase
	 *  pero debe ser redefinido siempre en la subcalse.
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	protected ActionForward inicializar(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {
		
		return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		
	}

	
	public ActionForward buscar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
	}
	
	public ActionForward imprimirReportFromAdapter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		// imprime un reporte implementado en cada Action
		return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
	}

	
	//----------------------------------------------------//
	//		Funciones de Manejo de errores y excepciones  //		
	//----------------------------------------------------//	

	/**
	 * Obtiene los ActionMessages y los carga en el request para que puedan ser mostrados los errores en la jsp.
	 * @param request
	 * @param modelVO
	 */
	protected void saveDemodaErrors(HttpServletRequest request, Common modelVO){
		ActionMessages actionErrors = this.getActionMessages(modelVO.getListError());
		saveErrors(request, actionErrors);		
	}
	
	/**
	 * Obtiene los ActionMessages y los carga en el request para que puedan ser mostrados como mensajes en la jsp.
	 * @param request
	 * @param modelVO
	 */
	protected void saveDemodaMessages(HttpServletRequest request, Common modelVO){
		ActionMessages actionMessages = this.getActionMessages(modelVO.getListMessage());
		saveMessages(request, actionMessages);		
	}
	
	/**
	 * Recorre la lista de DemodaStringMsg y la transforma  ActionMessages
	 * 
	 * @author Cristian
	 * @param lista
	 * @return
	 */
	private ActionMessages getActionMessages(List<DemodaStringMsg> lista) {
		ActionMessages actionMessages =  new ActionMessages();
		log.debug("entrando getActionMessages: lista " + lista.size() );
	
		for (DemodaStringMsg  dsm:  lista) {
						
			log.debug("dsm" + dsm.number() + " - " + dsm.key());
			
			ActionMessage am = null;
			
			// Si no tiene parametros
			if (dsm.params() == null || dsm.params().length == 0){

				// si el demoda Strin message contiene un valor
				if (dsm.isValue() ) {
					am = new ActionMessage("error.generic", dsm.key().substring(1));
				} 
				
				// si el demoda Strin message contiene una key				
				if (dsm.isKey() ) {
					String msg = getValueFromBundle(dsm.key());
					am = new ActionMessage("error.generic", msg);			
				} 
			
			// Si posee parametros
			} else {
				log.debug( dsm.params().length + " params encontrados"); 
						
				Object[] objParam = new Object[dsm.params().length];
				
				for (int i=0 ; i < dsm.params().length; i++ ){					
					String  strParam = (String) dsm.params()[i];
					
					log.debug( " param: " + i + " valor: " + strParam);
					
					if ( strParam.startsWith("&") ) {
						// si el parametro contiene un valor
						objParam[i] = strParam.substring(1);
					} else {
						// si el parametro contiene una key
						// si la key contiene la cadena .formatError la cambio por label 
						if (strParam.contains("formatError"))	
							strParam = strParam.replaceAll("formatError", "label");
						
						objParam[i] =  getValueFromBundle(getKeyFromStringError(strParam));
					} 
				}
				
				log.debug( "ActionMessage ---> msg: " + dsm.key() + " objParam: " + objParam[0]);
						
				am = new ActionMessage(dsm.key(), objParam);
			}
			
			actionMessages.add(ActionMessages.GLOBAL_MESSAGE, am);
			
		}
		
		return actionMessages;
	}
	
	/**
	 * Dado que el str puede venir con la forma:  numero + " " + key
	 * devuelve la key  
	 * 
	 * @author Cristian
	 * @param str
	 * @return
	 */
	private String getKeyFromStringError(String str){
		String key = str;
		
		try {
			
			if (str.contains(" ")){
				String[] arrSpaces = str.split(" ");
				return arrSpaces[1];
			}
			
		} catch(Exception e){				
			log.error(" error en getKeyFromStringError()");			
		}		
		
		return key;
	}
	
	/**
	 * Dada la key str recibida, utiliza la cadena hasta el primer punto como bundle y toda la cadena como key.
	 * Si puede recurar el valor, lo devuelve.
	 * Sino devuelve la key tal cual la recibe.  
	 * 
	 * @author Cristian
	 * @param str
	 * @return
	 */
	public String getValueFromBundle(String str){
		String msg = str;
		
		try {
			
			String[] arrPkgNames = str.split("\\.");
			String resBundle = "";
			
			if (arrPkgNames.length > 0)
				resBundle = arrPkgNames[0]; 
			
			resBundle = "resources." + resBundle; 
			
			log.debug("getActionMessages resBundle: " + resBundle);
			
			String msgRecuperado = ResourceBundle.getBundle(resBundle).getString(str);
			
			return msgRecuperado;
			
		} catch(Exception e){				
			log.error(" error en getValueFromBundle(): key " + str + " no encontrada " );
			//e.printStackTrace();
			return msg;
		}		
	}

	/** Esta es la excepcion que disparan todos los action y forwardea 
	 *  a la pantalla de confirmacion con la descripcion del error
	 * 
	 * @param mapping
	 * @param request
	 * @param act
	 * @param exception
	 * @return
	 * @throws Exception
	 */	
	protected ActionForward baseException(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, Exception exception, String VOName) throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + "enter");
		log.error("Ocurrio una exception en un Action: ", exception);
		try {

			UserSession userSession = getCurrentUserSession(request, mapping);
			if (userSession==null) return forwardErrorSession(request);

			// recupero el navModel del usserSession
			NavModel navModel = userSession.getNavModel();

			//recupero el CommonNavegableView del usserSession
			CommonNavegableView cnv = (CommonNavegableView) userSession.get(VOName);			

			// si el objeto esta en el userSession lo bajo y seteo los valores al navModel			
			if (cnv != null ) {

				//le seteo la accion a donde ir en la pantalla de confirmacion al navModel
				navModel.setConfAction(cnv.getPrevAction());
				navModel.setConfActionParameter(cnv.getPrevActionParameter());

				// saco el VO del usser session
				userSession.remove(VOName);

			} 

			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);
			
			navModel.setExcepcionStr(sw.toString());
			
			// forwardeo a la pantalla de mensaje con el icono de error y la descripcio del mismo
			//return this.forwardMessage(mapping, navModel, NavModel.NAVMODEL_MESSAGE_TYPE_EXCEPTION, sw.toString());			
			return this.forwardMessage(mapping, navModel,NavModel.NAVMODEL_MESSAGE_TYPE_EXCEPTION, 
					BaseConstants.EXCEPTION_MESSAGE_DESCRIPTION);
			
		} catch (Exception e) {
			log.error("Error no Manejado en baseException:", e);
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));
		}
	}
	

	/** 
	 * Esta funcion forwardea al ActionForward almacenado en el request.
	 * Es un parche feo, que existe por cambios que ocurrieron el esquema del canaccess()
	 * durante el desarrollo de la parte de seguridad.
	 * <p>Para no modificar todo el codigo existente, se opto por para que el canAccess() si 
	 * falla ya sea por falta de session o de permisos, almacene el forward al que tendria
	 * que ir por dichos errores en el request. Luego cuando desde el Action se llama
	 * a forwardErrorSession(), se saca del request y se realiza el forward.
	 * <p>Esto se hizo asi para ser coherente con todo lo programado anteriormente incluso APS.
	 * @param request
	 * @return el forward almacenado en el atributo del request con valor de constante ERROR_FORWARD_KEY
	 */
	protected ActionForward forwardErrorSession(HttpServletRequest request) {
		log.info("forwardErrorSession: forward a pagina login, por error o por no tener session.");
		ActionForward af = (ActionForward) request.getAttribute(ERROR_FORWARD_KEY);
		return af;
	}

	protected ActionForward forwardErrorSession(ActionMapping mapping, HttpServletRequest request) {
		ActionForward af = null;
		//verifica si se trata de un request de un usuario anonimo.
		if ("1".equals(request.getParameter("anonimo"))) {
			log.info("forwardErrorSession: forward a pagina login, por error o por no tener session.");
			af = mapping.findForward(BaseConstants.FWD_SESSION_ANONIMO_ERROR);
		} else {
			// es un usuario logueado. saltamos al relogin
			log.info("forwardErrorSession: forward a pagina login, por error o por no tener session.");
			af = new ActionForward(mapping.findForward(BaseConstants.FWD_SESSION_ERROR));
		}
		return af;
	}

	
	/**
	 * Recibe un Common con su listError cargada.
	 * Carga el Common recibido en el request. 
	 * Devuelve el InputForward.
	 * 
	 * @param mapping
	 * @param request
	 * @param userSession
	 * @param vOAttributeName
	 * @param modelVO es el Common
	 * @return ActionForward
	 */
	protected ActionForward forwardErrorRecoverable(ActionMapping mapping, HttpServletRequest request, UserSession userSession, String vOAttributeName,Common modelVO){

		// limpia la lista de resultados y inicializa el nro de pag de busq en cero.
		if (PageModel.class.isInstance(modelVO)){		
			((PageModel)modelVO).setListResult(new ArrayList());
			((PageModel)modelVO).setPageNumber(0L);
		}

		//userSession.put(vOAttributeName, modelVO);
		request.setAttribute(vOAttributeName, modelVO);
		return mapping.getInputForward();
	}
	
	/**
	 * Recibe un Common con su listError cargada.
	 * Carga el Common recibido en el request. 
	 * Devuelve el forward que se pasï¿½ como parï¿½metro
	 * 
	 * Se utiliza para ir a un forward distinto del "input" en caso de error.
	 * 
	 * @param mapping
	 * @param request
	 * @param userSession
	 * @param vOAttributeName
	 * @param modelVO es el Common
	 * @param forward es el forward al que va
	 * @return ActionForward
	 */	
	protected ActionForward forwardErrorRecoverable(ActionMapping mapping, HttpServletRequest request, 
			UserSession userSession, String vOAttributeName,Common modelVO, String forward){

		// limpia la lista de resultados y inicializa el nro de pag de busq en cero.
		if (PageModel.class.isInstance(modelVO)){		
			((PageModel)modelVO).setListResult(new ArrayList());
			((PageModel)modelVO).setPageNumber(0L);
		}

		//userSession.put(vOAttributeName, modelVO);
		request.setAttribute(vOAttributeName, modelVO);
		return mapping.findForward(forward);
	}
	
	/**
	 * Recibe el funcName(act) de struts para setear en act del NavModel,
	 * vOAttributeName para removerlo del userSession, 
	 * y el pageModel para obtener el ErrorDescription, si es nulo muestra mensaje de error de session.
	 * Con estos datos llama al baseConfirmar con messageType = NAVMODEL_MESSAGE_TYPE_ERROR.
	 * 
	 * @param mapping
	 * @param request
	 * @param funcName
	 * @param vOAttributeName
	 * @param pageModel
	 * @return
	 * @throws Exception
	 */
	protected ActionForward forwardErrorNonRecoverable(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String VOName, Common model)throws Exception {
		
		DemodaStringMsg dsm = model.getListError().get(0);
		
		String msgError = dsm.number() +" - " + dsm.key();
		
		return baseConfirmar(mapping, request, funcNameOrigen , VOName,
				NavModel.NAVMODEL_MESSAGE_TYPE_ERROR, msgError);	
	}
	
	protected ActionForward forwardErrorSessionNullObject(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen ,String VOName)throws Exception {
		
		return baseConfirmar(mapping, request, funcNameOrigen, VOName, 
			NavModel.NAVMODEL_MESSAGE_TYPE_ERROR, BaseConstants.SESSION_ERROR_DESCRIPTION);	
		
	}

	
	//---------------------------------------------------------------------//
	//	Funciones a utilizar de navegacion para los SearchPage y Adapter   //		
	//---------------------------------------------------------------------//
	/** Este es el metodo que setea los valores necesarios para saber a
	 *  donde volver en el NavModel y me forwardea al forward pasado como 
	 *  parametro, con el act que tambien se pasa como parametro.
	 * 
	 * @param ActionMapping mapping
	 * @param HttpServletRequest request, 
	 * @param String funcNameOrigen
	 * @param String actVolver 
	 * @param String forward
	 * @param String act
	 */
	protected ActionForward baseForward(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String actVolver ,String forward, String act) throws Exception {
		
		if (log.isDebugEnabled()) log.debug("funcNameOrigen:" + funcNameOrigen);
		
		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		try {
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());

			// seteo la accion y el parametro para volver
			navModel.setPrevAction(mapping.getPath());
			navModel.setPrevActionParameter(actVolver);

			// limpio las acciones seleccionar
			navModel.setSelectAction("");
			navModel.setSelectActionParameter("");

			// seteo el act a ejecutar en el accion al cual me dirijo
			navModel.setAct(act);

			log.debug(" forward to: " + forward);
			
			return mapping.findForward(forward);
		
		} catch (Exception e) {
			log.error("Exception - ", e);
			e.printStackTrace();
			return (mapping.findForward(BaseConstants.FWD_ERROR_NAVEGACION));			
		}		
	}

	/** Este forward me envia al "forward" pasado como parametro en modo seleccion
	 *  y vuelve al action actual al metodo indicado en "paramVolver".
	 *  El parametro "agregarEnSeleccion" indica si el boton agregar estara
	 *  habilitado en la busqueda.
	 * 
	 * @param mapping
	 * @param request
	 * @param paramVolver
	 * @param forward
	 * @param agregarEnSeleccion
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward forwardSeleccionar(ActionMapping mapping, HttpServletRequest request, 
		String paramVolver, String forward, boolean agregarEnSeleccion) throws Exception {

		UserSession userSession = getCurrentUserSession(request, mapping);
		if (userSession==null) return forwardErrorSession(request);
		
		NavModel navModel = userSession.getNavModel();
		if (log.isDebugEnabled()) log.debug("forwardSeleccionar: navModel" + navModel.infoString());

		// seteo la accion y el parametro para volver
		navModel.setPrevAction(mapping.getPath());
		navModel.setPrevActionParameter(paramVolver);
		
		// seteo la accion y el parametro para volver cuando selecciono
		navModel.setSelectAction(mapping.getPath());
		navModel.setSelectActionParameter(paramVolver);
		
		//seteo el valor que determina si se puede agregar o no en la seleccion
		navModel.setAgregarEnSeleccion(agregarEnSeleccion);

		// seteo el act a ejecutar en el accion al cual me dirijo		
		navModel.setAct(BaseConstants.ACT_SELECCIONAR);

		return mapping.findForward(forward); 
	}

	//----------------------------------------------------------------------//
	//	Funciones para los SearchPage										//		
	//----------------------------------------------------------------------//
	/** Este es el foward de los SearchPage, y setea el metodo a volver en "buscar"
	 * 
	 */
	protected ActionForward baseForwardSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward, String act) throws Exception {
	
		return baseForward(mapping, request, funcNameOrigen, BaseConstants.ACT_BUSCAR, forward, act);
		
	}
	
	/**
	 * <p>Realiza 3 trareas</p>
	 * <p> 1) Setea los valores de navegacion en el pageModel </p>
	 * <p> 2) Verifica si la llamada fue hecha desde el menu principal o desde otro action, para saber si es un abm o una seleccion 
	 *    para habilitar o deshabilitar las acciones de abm o seleccion</p>
	 * <p> 3) Envia el PageModel al request y lo sube al userMap de userSession con el nombre correspondiente.</p>    
	 * 
	 * @param mapping
	 * @param userSession
	 * @param nombreVO
	 * @param model
	 */
	protected void baseInicializarSearchPage(ActionMapping mapping, HttpServletRequest request, 
		UserSession userSession, String vOAttributeName, PageModel pageModel) {
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		// seteo los valores del navegacion en el pageModel
		pageModel.setValuesFromNavModel(userSession.getNavModel());
		
		if (userSession.getNavModel().getAct().equals(BaseConstants.ACT_SELECCIONAR)){
			pageModel.setModoSeleccionar(true);				
		}
		
		// Envio el VO al request
		request.setAttribute(vOAttributeName, pageModel);
		// Vacio el list result
		pageModel.setListResult(new ArrayList());
		// Subo en el el searchPage al userSession
		userSession.put(vOAttributeName, pageModel);
		if (log.isDebugEnabled()) log.debug(funcName + ": exit");		
		
	}
	
	protected ActionForward forwardVerSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {

		return baseForwardSearchPage (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_VER);		

	}

	protected ActionForward forwardModificarSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {

		return baseForwardSearchPage (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_MODIFICAR);

	}
		
	protected ActionForward forwardActivarSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {
		
		return baseForwardSearchPage (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_ACTIVAR);
		
	}
	
	protected ActionForward forwardDesactivarSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {
		
		return baseForwardSearchPage (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_DESACTIVAR);
		
	}

	protected ActionForward forwardEliminarSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {
		
		return baseForwardSearchPage (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_ELIMINAR);
		
	}
	
	protected ActionForward forwardAgregarSearchPage(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {

		return baseForwardSearchPage (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_AGREGAR);

	}

	//----------------------------------------------------------------------//
	//	Funciones para los Adapter										//		
	//----------------------------------------------------------------------//

	/** 
	 * Este es el foward de los Adapter, y setea el metodo a volver en "refill"
	 * 
	 */
	protected ActionForward baseForwardAdapter(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward, String act) throws Exception {
		
		return baseForward(mapping, request, funcNameOrigen, BaseConstants.ACT_REFILL, forward, act);
			
	}
	
	protected ActionForward forwardVerAdapter(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {						

		return baseForwardAdapter (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_VER);		

	}

	protected ActionForward forwardModificarAdapter(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {

		return baseForwardAdapter (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_MODIFICAR);

	}
		
	protected ActionForward forwardEliminarAdapter(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {
		
		return baseForwardAdapter (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_ELIMINAR);
		
	}
	
	protected ActionForward forwardAgregarAdapter(ActionMapping mapping, HttpServletRequest request, 
		String funcNameOrigen, String forward) throws Exception {

		return baseForwardAdapter (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_AGREGAR);

	}
	
	protected ActionForward forwardActivarAdapter(ActionMapping mapping, HttpServletRequest request, 
			String funcNameOrigen, String forward) throws Exception {

			return baseForwardAdapter (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_ACTIVAR);
	}

	protected ActionForward forwardDesactivarAdapter(ActionMapping mapping, HttpServletRequest request, 
			String funcNameOrigen, String forward) throws Exception {

			return baseForwardAdapter (mapping, request, funcNameOrigen, forward, BaseConstants.ACT_DESACTIVAR);
	}

	// fileName nombre del archivo a visualizar
	public void baseResponseExcel( HttpServletResponse response, String fileName) throws Exception {

 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		File fileIn = new File(fileName);
		// dibujo del xls
		response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileIn.getName() + "\"");
		ServletOutputStream os = response.getOutputStream();
		
		log.debug(fileIn.getPath());
		FileInputStream fileinputstream = new FileInputStream(fileIn);
		byte [] rgb = new byte [(int)fileIn.length()];
		fileinputstream.read(rgb);
		fileinputstream.close();
		
		InputStream is = new ByteArrayInputStream(rgb);
		  
		byte[] buf   = new byte[1024];
        int numbytes = 0;
		while (numbytes != -1) {
		    os.write(buf,0,numbytes);
		    try {
			numbytes = is.read(buf,0,1024);
		    }
		    catch (java.io.IOException e) {
			numbytes = -1;
			log.debug(e);
			//annoying bug?
		    }
		}
		os.flush();
		os.close();

        log.debug("finalizando: " + funcName);
	}


	
	// fileName nombre del archivo a visualizar
	public boolean baseResponseFile( HttpServletResponse response, String fileName) throws Exception {
		return baseResponseFile(response, fileName, "application/octet-stream");
	}

	
	// fileName nombre del archivo a visualizar
	private boolean baseResponseFile( HttpServletResponse response, String fileName, String contentType) throws Exception {

 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {
			File fileIn = new File(fileName);
			log.debug("Leyendo: " + fileIn.getPath());
			
			//InputStream is = new ByteArrayInputStream(rgb);
			InputStream is = new FileInputStream(fileIn);
			  
			byte[] buf   = new byte[1024];
	        int numbytes = 0;
	        
	        response.setContentType(contentType);
	        response.setContentLength((int)fileIn.length());
	        response.setHeader("Content-Disposition", "attachment; filename=\"" +fileIn.getName() + "\"");
			ServletOutputStream os = response.getOutputStream();
			while (numbytes != -1) {
			    os.write(buf,0,numbytes);
			    try {
					numbytes = is.read(buf,0,1024);
			    }
			    catch (java.io.IOException e) {
					numbytes = -1;
					log.error(e);
					//annoying bug?
			    }
			}
			os.flush();
			os.close();

	        return true;			
		} catch (FileNotFoundException e) {
			log.error("Archivo no encontrado: " + fileName,e);
			response.setContentType("text/html");
			response.setHeader("Content-Disposition", null);
			response.reset(); 
			return false;
		}
	}


	// fileName nombre del archivo a visualizar
	public void baseResponsePrintModel(HttpServletResponse response, PrintModel pm) throws Exception {

 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		

		if (pm.getRenderer() == PrintModel.RENDER_PDF) {
		  response.setContentType("application/pdf");
		} else if (pm.getRenderer() == PrintModel.RENDER_TXT) {
			  response.setContentType("application/text"); //  text/plain			
		}
		
		byte[] content = pm.getByteArray();
		
        response.setContentLength(content.length);
        response.getOutputStream().write(content);
        response.getOutputStream().flush();

        log.debug("finalizando: " + funcName);
	}
	
	
	// fileName nombre del archivo a visualizar
	protected boolean baseResponseEmbedContent( HttpServletResponse response, String fileName, String contentType) throws Exception {

 		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		try {
			File fileIn = new File(fileName);
			log.debug("Leyendo: " + fileIn.getPath());
			
			//InputStream is = new ByteArrayInputStream(rgb);
			InputStream is = new FileInputStream(fileIn);
			  
			byte[] buf   = new byte[1024];
	        int numbytes = 0;
	        
	        response.setContentType(contentType);
	        response.setContentLength((int)fileIn.length());
	        //response.setHeader("Content-Disposition", "attachment; filename=\"" +fileIn.getName() + "\"");
			ServletOutputStream os = response.getOutputStream();
			while (numbytes != -1) {
			    os.write(buf,0,numbytes);
			    try {
					numbytes = is.read(buf,0,1024);
			    }
			    catch (java.io.IOException e) {
					numbytes = -1;
					log.error(e);
					//annoying bug?
			    }
			}
			os.flush();
			os.close();

	        return true;			
		} catch (FileNotFoundException e) {
			log.error("Archivo no encontrado: " + fileName,e);
			response.setContentType("text/html");
			response.setHeader("Content-Disposition", null);
			response.reset(); 
			return false;
		}
	}

	// Envio de archivos asociados a un paso de una corrida
	public ActionForward enviarArchivos(ActionMapping mapping, ActionForm form,
		HttpServletRequest request, HttpServletResponse response)
		throws Exception {

		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
		
		UserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession == null) return forwardErrorSession(request);
		try {
			
			NavModel navModel = userSession.getNavModel();
			if (log.isDebugEnabled()) log.debug("baseForward: navModel" + navModel.infoString());
		
			// Obtenemos el id seleccionado
			String selectedId = request.getParameter("selectedId");
			
			Long idPasoCorrida = Long.parseLong(selectedId);
			
			if (log.isDebugEnabled()) log.debug("Id Paso Corrida : " + idPasoCorrida);
			
			navModel.putParameter(EnvioArchivosAdapter.PARAM_ID_PASOCORRIDA, idPasoCorrida);

			log.debug("exit: " + funcName);
			
			return baseForwardAdapter(mapping, request, funcName, 
					ProConstants.ACTION_ADMINISTRAR_ENVIO_ARCHIVOS, 
					ProConstants.ACT_ENVIAR_ARCHIVOS);
			
		} catch (Exception exception) {
			log.error("Error no Manejado en enviarArchivos:", exception);
			return forwardErrorSession(request);
		}
	}
	
	//----------------------------------------------------//
	//		Funciones a deprecadas y fuera de uso	      //		
	//----------------------------------------------------//
	@Deprecated
	protected String getUserMenuOptionsStr (NavModel navModel, String currAction) {
		String userMenuOptionsStr = "";
		
		String funcName = "getUserMenuOptionsStr";
		
		String strCurrAction = getActionNameForDisplay(currAction);
		String strAct = getActNameForDisplay(navModel.getAct());
		if (navModel.getPrevNavModel()!=null) {
			String strPNMPrevAction = getActionNameForDisplay(navModel.getPrevNavModel().getPrevAction());
			String strPNMAct = getActNameForDisplay(navModel.getPrevNavModel().getAct());
			
			if (log.isDebugEnabled()) {
				log.debug(funcName + "- strCurrAction: " + strCurrAction);
				log.debug(funcName + "- strAct: " + strAct);
				log.debug(funcName + "- strPNMPrevAction: " + strPNMPrevAction);
				log.debug(funcName + "- strPNMAct: " + strPNMAct);
			}
			
			if (!strCurrAction.equals(strPNMPrevAction))
				//userMenuOptionsStr += strPNMPrevAction + strPNMAct + " | ";
				userMenuOptionsStr += strPNMPrevAction;
			
			if (!strCurrAction.equals(strPNMPrevAction) || !strAct.equals(strPNMAct)) 
				userMenuOptionsStr += strPNMAct + " ";
			
			if (!strCurrAction.equals(strPNMPrevAction))
				userMenuOptionsStr += "| ";
		}
		
		userMenuOptionsStr += strCurrAction + strAct; 
		
		return userMenuOptionsStr;
	}

	@Deprecated
    public static String getActionNameForDisplay(String actionName){
		String actionNameForDisplay = "Sin Titulo";

		// XXX No implementado

		return actionNameForDisplay;
	}
    
    @Deprecated
	public static String getActNameForDisplay(String actName){
		String actNameForDisplay = "";
		
		if( !actName.equals("") && !actName.startsWith("seleccionar") 
				&& !actName.startsWith("volver") && !actName.startsWith("inicializar")
				&& !actName.startsWith("adapter") && !actName.startsWith("buscar")){
			actNameForDisplay = " [ " + actName + " ]";
		}
		
		return actNameForDisplay;
	}
	
	@Deprecated
	public static boolean isABMAct(String act) {
		boolean result = false;
		
		if ( act==null ) act = "";
		if ( act.equals(BaseConstants.ACT_VER) || act.equals(BaseConstants.ACT_AGREGAR) || 
			 act.equals(BaseConstants.ACT_MODIFICAR) || act.equals(BaseConstants.ACT_ELIMINAR) ||
			 act.equals(BaseConstants.ACT_ALTA) || act.equals(BaseConstants.ACT_BAJA)	 ){
			 result = true;
		}
		
		return result;
	}

	public ActionForward forwardFuncionNoDisponible(HttpServletRequest request) {
		CommonKey vo = new CommonKey("0");
		vo.addRecoverableError(BaseError.MSG_FUNCION_NODISPONIBLE);
		saveDemodaErrors(request, vo);
		return new ActionForward("/seg/SiatMenu.do?method=build");
	}
	
	public ActionForward forwardNavItem(NavItem navItem) {
		String url = navItem.getAccion() + ".do?method=" + navItem.getMetodo();
		Long id = navItem.getSelectedId();
		log.debug("forwardNavItem: navItem: accion:" + navItem.getAccion() + " metodo:" + navItem.getMetodo() + " selectedId:" + navItem.getSelectedId() );
	    if (id == null || id == 0L) {
			return new ActionForward(url);
		}
		

		return new ActionForward(url + "&selectedId=" + id);
	} 

	public ActionForward baseImprimir(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws Exception {

			String funcName = DemodaUtil.currentMethodName();
			if (log.isDebugEnabled()) log.debug(funcName + ": enter");		
			UserSession userSession = getCurrentUserSession(request, mapping); 
			if (userSession == null) return forwardErrorSession(request);
			
			try {
				// obtiene el nombre del page del request
				String name = request.getParameter("name");
				String reportFormat = request.getParameter("report.reportFormat");
				String responseFile = request.getParameter("responseFile");
				
				// Bajo el searchPage del userSession
				if ("1".equals(responseFile)) {
					String fileName = (String) userSession.get("baseImprimir.reportFilename");
					// realiza la visualizacion del reporte
					baseResponseEmbedContent(response, fileName, "application/pdf");
					return null;
				}
				
				CommonNavegableView cnv = (CommonNavegableView) userSession.get(name);
				// Si es nulo no se puede continuar
				if (cnv == null) {
					log.error("error en: "  + funcName + ": " + name + " IS NULL. No se pudo obtener de la sesion");
					return forwardErrorSessionNullObject(mapping, request, funcName, name); 
				}
				
				// carga de los valores del request al pageModel
				DemodaUtil.populateVO(cnv, request);
				
				// Tiene errores recuperables
				if (cnv.hasErrorRecoverable()) {
				
					log.error("recoverable error en: "  + funcName + ": " + cnv.infoString()); 
					saveDemodaErrors(request, cnv);
					saveDemodaMessages(request, cnv);
					return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
				}
				

				cnv.getReport().setImprimir(Boolean.TRUE); // para que la busqueda genere el reporte
			    cnv.prepareReport(Long.valueOf(reportFormat));
			    
			  // realizacion de la busqueda y generacion del reporte
				
				if(PageModel.class.isInstance(cnv)){
					// en las busquedas
					this.buscar(mapping, form, request, response); // implementado en cada Action
				}else{
					// en los adapters
					this.imprimirReportFromAdapter(mapping, form, request, response); // implementado en cada Action	
				}
				
				cnv.getReport().setImprimir(Boolean.FALSE);
				//pageModelVO.setPaged(paged);
				cnv.getReport().getReportFiltros().clear();
				cnv.getReport().getListReport().clear();
				cnv.getReport().getReportListTable().clear();
							
				// Tiene errores recuperables
				if (cnv.hasErrorRecoverable()) {
					log.error("Recoverable error en: "  + funcName + ": " + cnv.infoString());
					saveDemodaErrors(request, cnv);
					saveDemodaMessages(request, cnv);
					request.setAttribute(name, cnv);
					return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
					//return forwardErrorPrint(mapping, request, funcName, name, pageModelVO);
					//return actionForward;
				}
				
				// Subo en el el searchPage al userSession
				// Esto lo sube a la session, una posterior llamada a este metodo con 
				// la bandera responseFile, lo baja de y retorna el archivo generado
				userSession.put(name, cnv);
				request.setAttribute("path", request.getRequestURI());
				
				// obtenemos el nombre del archivo seleccionado
				// String fileName = cnv.getReport().getReportFileName();
				// realiza la visualizacion del reporte
				// baseResponseFile(response,fileName);

				userSession.put("baseImprimir.reportFilename", cnv.getReport().getReportFileName());
				log.debug("exit: " + funcName);
				return new ActionForward(BaseConstants.FWD_VIEW_IMPRIMIR);
				
			} catch (Exception exception) { 
				log.error("Error no Manejado en baseException:", exception);
				return mapping.findForward(BaseConstants.FWD_ERROR_PRINT);
			}
		}
}
