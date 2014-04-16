//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.base.view.struts;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.actions.DispatchAction;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.iface.model.SweContext;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.util.SweCache;
import ar.gov.rosario.swe.view.util.SweConstants;
import ar.gov.rosario.swe.view.util.SweUtil;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.DemodaStringMsg;
import coop.tecso.demoda.iface.model.NavModel;
import coop.tecso.demoda.iface.model.PageModel;

public class SweBaseDispatchAction extends DispatchAction {
	static private String CANACCESS_ERROR_KEY = "canAccess.error";
	static private String CANACCESS_ERROR_FORWARD_KEY = "canAccess.forward";
	static private String CANACCESS_ERROR_USERSESSION = "noUserSession";
	static private String CANACCESS_ERROR_NOACCESS = "noAccess";

	private Log log = LogFactory.getLog(SweBaseDispatchAction.class);	

	// Iniciamos el SweCache para tener siempre disponible
	// los datos de Contexto de seguridad de la aplicacion Swe
	static {
		Log log = LogFactory.getLog(SweBaseDispatchAction.class);
		try {
			if (SweCache.getInstance().getSweContext() == null) {
				SweContext sweContext = SweServiceLocator.getSweService().getSweContext(SweConstants.CODAPL);
				SweCache.getInstance().setSweContext(sweContext);
			}
			// a partir de ahora podemos obtener el SweContext de SWE invocando a:
			//SweCache.getInstance().getSweContext();
		} catch (Exception e) {
			log.error("**************************************");
			log.error("ERROR:");
			log.error("No se pudo Inicializar el contexto de seguridad de SWE.");
			log.error("El comportamiento de la aplicacion es inesperado.");
			log.error("El error fue: ", e);
			log.error("**************************************");

			System.out.println("**************************************");
			System.out.println("ERROR:");
			System.out.println("No se pudo Inicializar el contexto de seguridad de SWE.");
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
	protected SweUserSession canAccess(HttpServletRequest request, ActionMapping mapping, String actionName, 
		String methodName) throws Exception {

		SweUserSession userSession = getCurrentUserSession(request, mapping);

		if (userSession == null) {
			return null;
		}
		
		// seteo el metodo y la accion swe actual
		userSession.setAccionSWE(actionName);
		userSession.setMetodoSWE(methodName);
		
		NavModel navModel = userSession.getNavModel();
		
		long hasAccess = SweCache.getInstance().getSweContext().hasAccess((SweUserSession)userSession.get("sweUserSession") , actionName, methodName);
		
		if (hasAccess==Common.HASACCESS_SINACCESO || hasAccess==Common.HASACCESS_NOMBRESNULOS_ERROR ) { 
			log.info("canAccess():Access Denied '" + userSession.getUserName() + "' '" + actionName + "' '" + methodName + "'");
			request.setAttribute(CANACCESS_ERROR_KEY, CANACCESS_ERROR_NOACCESS);
			request.setAttribute(CANACCESS_ERROR_FORWARD_KEY, mapping.findForward(SweConstants.FWD_MSG));
			navModel.setMessageType(NavModel.NAVMODEL_MESSAGE_TYPE_ERROR);
			navModel.setMessageStr("Acceso Denegado.");
			return null;
		} else {
			log.info("canAccess():Access OK '" + userSession.getUserName() + "' '" + actionName + "' '" + methodName + "'");
		}
		
		return userSession;
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
		
		SweUserSession userSession = (SweUserSession)session.getAttribute("userSession");
		if (userSession == null) {
			return "";
		}
		
		NavModel navModel = userSession.getNavModel();
		if (navModel == null) {
			throw new Exception("navModel null. No se pudo obtener el navModel.");
		} else {
			log.debug("XXX navModel.selectedId = " + navModel.getSelectedId());			

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
	protected SweUserSession getCurrentUserSession(HttpServletRequest request, ActionMapping mapping) 
		throws Exception{
		
		String funcName = "getCurrentUserSession";
		SweUserSession userSession = null;
		HttpSession session = request.getSession();
		
		userSession = (SweUserSession) session.getAttribute("userSession");
		if (userSession == null) {
			log.info(funcName + " - " + "No se encontro UserSession en HttpSession.");
			request.setAttribute(CANACCESS_ERROR_KEY, CANACCESS_ERROR_USERSESSION);
			request.setAttribute(CANACCESS_ERROR_FORWARD_KEY, mapping.findForward(SweConstants.FWD_SESSION_ERROR));
			return null;
		}
		
		userSession.setIpRequest(request.getRemoteAddr());
		// Metemos el userContext en el TLS
		// hasAccess() requiere que este el UserContext este en el TLS
		DemodaUtil.setCurrentUserContext(userSession);
		
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
		
		// se pasa el selectedId y el metod actual al navModel
		if(!StringUtil.isNullOrEmpty(reqAttSelectedId)){
			navModel.setSelectedId(reqAttSelectedId);
		}
		
		if (log.isDebugEnabled()) {
			StringBuilder sb = new StringBuilder();
			sb.append(funcName);
			sb.append("navModel=").append(navModel.infoString());
			sb.append("userSession=OK;");
			sb.append("reqAttMethod=").append(reqAttMethod);
			sb.append(";reqAttSelectedId=").append(reqAttSelectedId);
			sb.append(";reqAttPageNumber=").append(reqAttPageNumber);
			sb.append(";reqAttIsSubmittedForm=").append(reqAttIsSubmittedForm);
			log.debug(sb.toString());
		}
		
		return userSession;
	} 
	
    @Deprecated	
	protected SweUserSession canAccess(HttpServletRequest request, ActionMapping mapping, String methodName) throws Exception {
		return null;
	}

	protected HttpSession getHttpSession(HttpServletRequest request){		
		
		String funcName = "getHttpSession";
		HttpSession session = null;
		try {
			session = request.getSession();
			
			if (session==null){
				if (log.isErrorEnabled()) 
					log.error(funcName + " - No se pudo obtener la sesion del servidor de aplicacion");				
			}
		} 
		
		catch (Exception e) {							
			e.printStackTrace();
			if (log.isErrorEnabled())			
				log.error(funcName + " - excepcion - " + e.toString());
			session = null;
		}
		
		return session;
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
	private String getValueFromBundle(String str){
		String msg = str;
		
		try {
			
			String[] arrPkgNames = str.split("\\.");
			String resBundle = "";
			
			if (arrPkgNames.length > 0)
				resBundle = arrPkgNames[0]; 
			
			if (resBundle.equals("swe"))
				resBundle = "base";
			
			resBundle = "resources." + resBundle; 
				
			log.debug("getActionMessages resBundle: " + resBundle);
			
			String msgRecuperado = ResourceBundle.getBundle(resBundle).getString(str);
			
			return msgRecuperado;
			
		} catch(Exception e){				
			log.error(" error en getValueFromBundle(): key no encontrada " + str );
			//e.printStackTrace();
			return msg;
		}		
	}
	
	
	protected String getPrevActionForward(NavModel navModel, ActionMapping actionMapping) {
		String prevActionFwd = null;
		String prevActionPath = null;

		String funcName = "getPrevActionForward";

		prevActionPath = navModel.getPrevAction() + ".do?method=" + navModel.getPrevActionParameter(); 
		if (log.isDebugEnabled()) log.debug(funcName + " - prevActionPath: " + prevActionPath);
		
		// posterior al prevNavModel ------------------------------------------
		if (navModel.getPrevNavModel()!=null) {
			prevActionPath = navModel.getPrevNavModel().getPrevAction() + ".do?method=" + navModel.getPrevNavModel().getPrevActionParameter(); 
			if (log.isDebugEnabled()) log.debug(funcName + " - prevNavModel.prevActionPath: " + prevActionPath);
		}
		// fin posterior al prevNavModel --------------------------------------
		 
		String forwards[] = actionMapping.findForwards();
		for (int i = 0; i < forwards.length; i++) {
			if (actionMapping.findForward(forwards[i]).getPath().equals(prevActionPath)) {
				prevActionFwd = forwards[i];
				break;
			}
		}		
		
		if (log.isDebugEnabled()) log.debug(funcName + " - prevActionFwd: " + prevActionFwd);
		
		return prevActionFwd;
	}

	protected ActionForward baseSeleccionar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String act, String vONameToRemove)
			throws Exception {
		
		String funcName = "baseSeleccionar." + act;
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		try {					
		
			NavModel navModelAction = (NavModel)userSession.get(mapping.getPath());

			NavModel navModelUS = navModelAction.copyTo(new NavModel()); 
			navModelUS.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModelUS.setAct(act);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": " + navModelUS.infoString());

			userSession.setNavModel(navModelUS);
			
			userSession.remove(vONameToRemove);
			

			return (mapping.findForward(this.getPrevActionForward(navModelUS, mapping)));
			
		} catch (Exception e) {
			e.printStackTrace();
			// falta log, agregar errores
			return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		}
	}
	
	/**
	 * Vuelve a donde corresponde y remueve el model del userSession.
	 * 
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param vONameToRemove
	 * @return ActionForward
	 * @throws Exception
	 */
	protected ActionForward baseVolver(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response, String vONameToRemove)
			throws Exception {
		
		String funcName = "baseVolver";
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		try {					
			NavModel navModelAction = (NavModel)userSession.get(mapping.getPath()); 
			
			NavModel navModelUS = new NavModel();
			navModelUS.setPrevAction(navModelAction.getPrevAction());
			navModelUS.setPrevActionParameter(navModelAction.getPrevActionParameter());
			navModelUS.setSelectedId("");
			navModelUS.setAct("volver");
			if (log.isDebugEnabled()) log.debug(funcName + ": " + navModelUS.infoString());
			

			//request.setAttribute("isSubmittedForm","false");			
			userSession.setNavModel(navModelUS);
			//userSession.put(mapping.getPath(),null);
			userSession.remove(vONameToRemove);
			
			return (mapping.findForward(this.getPrevActionForward(navModelUS, mapping)));
			
		} catch (Exception e) {
			e.printStackTrace();
			// falta definir llamada o no a logout 
			return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		}
	}

	
	protected void baseVer(ActionMapping mapping, SweUserSession userSession, String funcName){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModel.setAct(SweConstants.ACT_VER);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
		
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}
	
	
	protected void baseAgregar(ActionMapping mapping, SweUserSession userSession, String funcName){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());
		
			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId("0");
			navModel.setAct(SweConstants.ACT_AGREGAR);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}
	
	/** Setea los valores de navegacion para ir a la accion
	 *  de agregar, el selecteId pasado como parametro
	 *  se setea en el navModel
	 * 
	 * @param mapping
	 * @param userSession
	 * @param funcName
	 * @param selecteId
	 */
	
	protected void baseAgregar(ActionMapping mapping, SweUserSession userSession, String funcName, String selecteId){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());
		
			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId(selecteId);
			navModel.setAct(SweConstants.ACT_AGREGAR);

			if (log.isDebugEnabled()) log.debug(funcName + ": navModel: " + navModel.infoString());
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}	
	
	protected void baseModificar(ActionMapping mapping, SweUserSession userSession, String funcName){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModel.setAct(SweConstants.ACT_MODIFICAR);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}
	
	protected void baseGenerico(ActionMapping mapping, SweUserSession userSession, String funcName, String act){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModel.setAct(act);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}

	
	protected void baseEliminar(ActionMapping mapping, SweUserSession userSession, String funcName){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModel.setAct(SweConstants.ACT_ELIMINAR);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}	
	
	protected void baseBuscar(ActionMapping mapping, SweUserSession userSession, String funcName){
		try {
			NavModel navModel = userSession.getNavModel();
			NavModel navModelUS = (NavModel)userSession.get(mapping.getPath());

			navModel.setPrevAction(mapping.getPath()); 
			navModel.setPrevActionParameter(SweConstants.ACT_BUSCAR);
			navModel.setPrevNavModel(navModelUS.getPrevNavModel());
			navModel.setSelectedId((String)userSession.get("reqAttSelectedId"));
			navModel.setAct(SweConstants.ACT_BUSCAR);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModel" + navModel.infoString());
		} catch (Exception e){
			e.printStackTrace();			
		}		
	}

	
	protected ActionForward baseException(ActionMapping mapping, HttpServletRequest request, 
			String act, Exception exception)
			throws Exception {
		String funcName = "baseException";
		
		SweUserSession userSession = (SweUserSession) request.getSession().getAttribute("userSession");
		if (userSession==null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		try {
			NavModel navModelAction = (NavModel)userSession.get(mapping.getPath()); 
			
			NavModel navModelUS = new NavModel();
			navModelUS.setPrevAction(navModelAction.getPrevAction());
			navModelUS.setPrevActionParameter(navModelAction.getPrevActionParameter());
			navModelUS.setAct(act);
		
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			exception.printStackTrace(pw);				
			
			log.error("Action exception: " , exception);		
			
			navModelUS.setMessageType(NavModel.NAVMODEL_MESSAGE_TYPE_EXCEPTION);
			navModelUS.setMessageStr(sw.toString());

			userSession.setNavModel(navModelUS);
			userSession.put(mapping.getPath(),null);			
			return (mapping.findForward(SweConstants.FWD_MSG));
		} catch (Exception e) {
			log.error("Error no Manejado en baseException:", e);
			return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		}
	}

	/**
	 * 
	 * 
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
	protected ActionForward baseConfirmar(ActionMapping mapping, HttpServletRequest request, String act, 
			String vOAttributeName, String selectedId, int messageType, String messageStr)
			throws Exception {
		
		String funcName = "baseConfirmar." + act;
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		try {					
			
			NavModel navModelAction = (NavModel)userSession.get(mapping.getPath());

			NavModel navModelUS = navModelAction.copyTo(new NavModel()); 
			navModelUS.setSelectedId(selectedId);
			navModelUS.setAct(act);
			navModelUS.setMessageType(messageType);
			if (StringUtil.isNullOrEmpty(messageStr)) {
				messageStr = "La operacion ha sido realizada con exito";
			}
			navModelUS.setMessageStr(messageStr);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModelUS" + navModelUS.infoString());

			userSession.setNavModel(navModelUS);
			
			userSession.remove(vOAttributeName);

			return mapping.findForward(SweConstants.FWD_MSG);
			
		} catch (Exception e) {
			e.printStackTrace();
			// falta log, agregar errores
			return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		}
	}

	protected ActionForward baseConfirmarYVolver(ActionMapping mapping, HttpServletRequest request, 
			String vOAttribute, int messageType, String messageStr)
			throws Exception {
		
		String funcName = "baseConfirmarYVolver";
		SweUserSession userSession = getCurrentUserSession(request, mapping); 
		if (userSession==null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		try {					
			
			NavModel navModelAction = (NavModel)userSession.get(mapping.getPath());

			NavModel navModelUS = navModelAction.copyTo(new NavModel());
			navModelUS.setAct(funcName);
			navModelUS.setMessageType(messageType);
			if (StringUtil.isNullOrEmpty(messageStr)) {
				messageStr = "La operacion ha sido realizada con exito";
			}
			navModelUS.setMessageStr(messageStr);
			
			if (log.isDebugEnabled()) log.debug(funcName + ": navModelUS" + navModelUS.infoString());

			userSession.setNavModel(navModelUS);
			request.getSession().removeAttribute(vOAttribute);

			return mapping.findForward(SweConstants.FWD_MSG);
			
		} catch (Exception e) {
			e.printStackTrace();
			// falta log, agregar errores
			return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		}
	}

	protected String getUserMenuOptionsStr (NavModel navModel, String currAction) {
		String userMenuOptionsStr = "";
		
		String funcName = "getUserMenuOptionsStr";
		
		String strCurrAction = SweUtil.getActionNameForDisplay(currAction);
		String strAct = SweUtil.getActNameForDisplay(navModel.getAct());
		if (navModel.getPrevNavModel()!=null) {
			String strPNMPrevAction = SweUtil.getActionNameForDisplay(navModel.getPrevNavModel().getPrevAction());
			String strPNMAct = SweUtil.getActNameForDisplay(navModel.getPrevNavModel().getAct());
			
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
	
	protected ActionForward forwardErrorSession(HttpServletRequest request) {
		ActionForward af = (ActionForward) request.getAttribute(CANACCESS_ERROR_FORWARD_KEY);
		String error = (String) request.getAttribute(CANACCESS_ERROR_KEY);

		if (af == null) {
			//
		}

		return af;
	}

	/**
	 *@deprecated Usar: forwardErrorSession(HttpServletRequest request)
	*/
	protected ActionForward forwardErrorSession() {
		ActionForward af = new ActionForward("/seg/Login.do?method=sessionError");
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
	protected ActionForward forwardErrorRecoverable(ActionMapping mapping, HttpServletRequest request, SweUserSession userSession, String vOAttributeName,Common modelVO){
		
		//userSession.put(vOAttributeName, modelVO);
		request.setAttribute(vOAttributeName, modelVO);
		return mapping.getInputForward();
	}
	
	/**
	 * Obtiene los ActionErrors y los carga en el request para que puedan ser mostrados los errores en la jsp.
	 * @param request
	 * @param modelVO
	 */
	protected void saveDemodaErrors(HttpServletRequest request, Common modelVO){
		ActionMessages actionMessages = this.getActionMessages(modelVO.getListError());
		saveErrors(request, actionMessages);
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
	protected ActionForward forwardErrorNonRecoverable(ActionMapping mapping, HttpServletRequest request, String funcName, String vOAttributeName, Common model)throws Exception{
		
		DemodaStringMsg dsm = model.getListError().get(0);
		
		String msgError = dsm.number() +" - " + dsm.key();
		
		return baseConfirmar(mapping, request, funcName, vOAttributeName, "", NavModel.NAVMODEL_MESSAGE_TYPE_ERROR, msgError);	
	}
	
	
	protected ActionForward forwardErrorSessionNullObject(ActionMapping mapping, HttpServletRequest request, String funcName ,String vOAttributeName)throws Exception{
		// TODO: Constantear este mensaje
		String msgError = "No se pudo obtener un objeto de la sesion de usuario";
		
		return baseConfirmar(mapping, request, funcName, vOAttributeName, "", NavModel.NAVMODEL_MESSAGE_TYPE_ERROR, msgError);	
		
	}
	
	/**
	 * 
	 * Forwardea al mensaje de OK de una operacion Create, Update o Delete
	 * 
	 * @param mapping
	 * @param request
	 * @param funcName
	 * @param vOAttributeName
	 * @return
	 */
	protected ActionForward forwardConfirmarOk(ActionMapping mapping, HttpServletRequest request, String funcName, String vOAttributeName) throws Exception{
		// TODO: Preguntar si es necesario el Id para estas acciones.		
		return baseConfirmar(mapping, request, funcName, vOAttributeName, "", NavModel.NAVMODEL_MESSAGE_TYPE_CONFIRMATION, "");

	}
	
	
	/**
	 * <p>Realiza 3 trareas</p>
	 * <p> 1) Pasa el NavModel actual al Historial: Crea un nuevo NavModel a partir del navModel del userSession y se lo agrega al userMap (del userSession) 
	 *    con el mapping.getPath() como key</p>
	 * <p> 2) Verifica si la llamada fue hecha desde el menu principal o desde otro action, para saber si es un abm o una seleccion 
	 *    para habilitar o deshabilitar las acciones de abm o seleccion</p>
	 * <p> 3) Envia el PageModel al request y lo sube al userMap de userSession con el nombre correspondiente.</p>    
	 * 
	 * @param mapping
	 * @param userSession
	 * @param nombreVO
	 * @param model
	 */
	protected void baseInicializarSearchPage(ActionMapping mapping, HttpServletRequest request, SweUserSession userSession, String vOAttributeName, PageModel pageModel){
		String funcName = "baseInicializarSearchPage";
		log.debug("entrando a " + funcName);
		
		pasarNavModelActualAHistorial(mapping, funcName, userSession);
		
		// si la busqueda es llamada desde el menu => ABM
		if (userSession.getNavModel().getPrevAction().equals(SweConstants.ACTION_LOGGED_USER)) {
			pageModel.setABMEnabled(PageModel.ENABLED);
			pageModel.setInactivo(true);
		}
		
		// Envio el VO al request
		request.setAttribute(vOAttributeName, pageModel);
		// Vacio el list result
		pageModel.setListResult(new ArrayList());
		// Subo en el el searchPage al userSession
		userSession.put(vOAttributeName, pageModel);
		
	}

	
	
	protected void pasarNavModelActualAHistorial(ActionMapping mapping, String funcName, SweUserSession userSession){
		
		NavModel navModel =  userSession.getNavModel();
		NavModel navModelUS = navModel.copyTo(new NavModel());
		log.debug(funcName + navModelUS.infoString());
		
		userSession.put(mapping.getPath(), navModelUS);
	}	
}

