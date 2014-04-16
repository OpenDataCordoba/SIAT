//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.swe.SweServiceLocator;
import ar.gov.rosario.swe.base.view.struts.SweBaseDispatchAction;
import ar.gov.rosario.swe.iface.model.SweContext;
import ar.gov.rosario.swe.iface.model.SweUserSession;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import ar.gov.rosario.swe.iface.util.SweCache;
import ar.gov.rosario.swe.seg.view.util.SweSegConstants;
import ar.gov.rosario.swe.view.util.SweConstants;
import coop.tecso.demoda.iface.helper.DemodaUtil;
import coop.tecso.demoda.iface.model.NavModel;


public final class LoginAction extends SweBaseDispatchAction {
	
	private Log log = LogFactory.getLog(LoginAction.class);

	public ActionForward inicializar(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		//obtengo la session y si existe la invalido y creo una nueva. 
		HttpSession session = request.getSession(false);
		if (session != null) {
			session.invalidate();
			request.getSession(true);
		}

		try {
			UsuarioVO userVO = new UsuarioVO();
			request.setAttribute(UsuarioVO.NAME, userVO);
			return mapping.findForward(SweSegConstants.FWD_LOGINFORM);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward login(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		HttpSession session = canAccess(request); 
		if (session == null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		SweUserSession userSession = null;
		SweUserSession sweUserSession = null; 
		try {
			UsuarioVO userVO = new UsuarioVO("","",session.getId());
			
			// Recuperamos datos del form en el vo
			DemodaUtil.populateVO(userVO, request);
			// Tiene errores recuperables
			if (userVO.hasErrorRecoverable()) {
				saveDemodaErrors(request, userVO);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioVO.NAME, userVO);
			}

			// realizamos login SWE
			sweUserSession = SweServiceLocator.getSweService().login(SweConstants.CODAPL, userVO);
			
			// Tiene errores recuperables
			if (sweUserSession.hasErrorRecoverable()) {
				saveDemodaErrors(request, sweUserSession);
				return forwardErrorRecoverable(mapping, request, userSession, UsuarioVO.NAME, userVO);
			}
			
			// Tiene errores no recuperables
			if (sweUserSession.hasErrorNonRecoverable()) {
				saveDemodaErrors(request, sweUserSession);
				return forwardErrorNonRecoverable(mapping, request, funcName, UsuarioVO.NAME, userVO);
			}

			userSession = new SweUserSession();
			userSession.setUserName(userVO.getUsername());
			userSession.setIdUsuarioSwe(sweUserSession.getIdUsuarioSwe());

			//Pasamos los datos del sweUserSession al UserSessino de Swe
			userSession.setIdsAccionesModuloUsuario(sweUserSession.getIdsAccionesModuloUsuario());
			userSession.setCodsRolUsuario(sweUserSession.getCodsRoles());

			// cosas de la navegacion
			NavModel navModel = new NavModel();
			navModel.setPrevAction("/SweIndex");
			navModel.setAct("buscar");
			pasarNavModelActualAHistorial(mapping, funcName, userSession);
			
			// establecemos cadena de usuario logueado
			userSession.setNavModel(navModel);	
			userSession.put("sweUserSession", sweUserSession);
				
            // setea en sesion el mCRLoginFacade
//			session.setAttribute("usuario",  sweUserSession.getMcrLoginFacade());
				
			// subo el userSession a la session
			session.setAttribute("userSession", userSession);
			
			return mapping.findForward(SweSegConstants.FWD_APLICACION_SEARCHPAGE);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward logout(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		HttpSession session = canAccess(request); 
		if (session == null) return (mapping.findForward(SweConstants.FWD_SESSION_ERROR));
		
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			session.invalidate();
			return mapping.findForward(SweSegConstants.FWD_WELCOME);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}

	public ActionForward sessionError(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		//obtengo la session y si existe la invalido y creo una nueva. 
		HttpSession session = request.getSession(true);

		try {
			UsuarioVO userVO = new UsuarioVO();
			request.setAttribute(UsuarioVO.NAME, userVO);
			userVO.addRecoverableError("101 swe.seg.sessionError");
			saveDemodaErrors(request, userVO);
			return mapping.findForward(SweSegConstants.FWD_LOGINFORM);
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception);
		}
	}


	public ActionForward sweRefresh(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		try {
			SweContext sweContext = SweServiceLocator.getSweService().getSweContext(SweConstants.CODAPL);
			SweCache.getInstance().setSweContext(sweContext);
		} catch (Exception e) {
			log.error("**************************************");
			log.error("ERROR:");
			log.error("No se pudo Inicializar el contexto de seguridad de SWE.");
			log.error("El comportamiento de la aplicacion es inesperado.");
			log.error("El error fue: ", e);
			log.error("**************************************");
		}
		return new ActionForward("/");
	}

}
