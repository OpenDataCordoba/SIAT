//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.view.struts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.view.util.UserSession;
import ar.gov.rosario.siat.def.iface.service.DefServiceLocator;
import ar.gov.rosario.siat.def.iface.service.IDefConfiguracionService;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.seg.view.util.SegConstants;
import ar.gov.rosario.swe.iface.model.UsuarioVO;
import coop.tecso.demoda.iface.helper.DemodaUtil;


/**
 * Esta clase se utiliza para manejar los request relacionados con el manejo de session, (Login, logout, refrescar caches, etc.)
 * Los metodos relacionados con login/logout se encuentran todos juntos en la clase LoginSiat.
 * Esta clase solo hace publico algunos metodos de LoginSiat. En especial los metodos que pueden ser llamados sin pasar por SSL.
 * Los demas metodos relacionados con el login/logout que SI pasan por SSL, estan en SslLoginSiat.
 * @author Tecso Coop. Ltda.
 *
 */
public final class LoginAction extends LoginSiat {
	
	static private Log log = LogFactory.getLog(LoginAction.class);
	
	/***
	 * Hace publico el metodo anonimo para la parte NO SSL
	 */
	public ActionForward anonimo(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.anonimo(mapping, form, request, response);
	}

	/***
	 * Hace publico el metodo de logout para la parte NOSSL
	 */
	public ActionForward logout(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		return super.logout(mapping, form, request, response);
	}

	/***
	 * Cuando occurre un error inesperado relacionado con el estado de la session forwardeamos a este método.
	 */
	public ActionForward sessionError(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");
		
		try {
			UsuarioVO userVO = new UsuarioVO();
			ActionForward af = null;
			
			request.setAttribute(UsuarioVO.NAME, userVO);
			userVO.addRecoverableError("101 swe.seg.sessionError");
			saveDemodaErrors(request, userVO);
			// es un usuario logueado. saltamos al relogin
			if (SiatParam.isIntranetSiat()) {
				af = new ActionForward(mapping.findForward(SegConstants.FWD_INTRANET_LOGIN_FORM));
			} else {
				af = new ActionForward(mapping.findForward(SegConstants.FWD_WEB_LOGIN_FORM));					
			}
			return af;
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, UsuarioVO.NAME);
		}
	}
	

	/***
	 * Refresca el cache de swe
	 */
	public ActionForward refrescarCache(ActionMapping mapping,
								 ActionForm form,
								 HttpServletRequest request,
								 HttpServletResponse response)
		throws Exception {
		String funcName = DemodaUtil.currentMethodName();
		if (log.isDebugEnabled()) log.debug(funcName + ": enter");

		UserSession userSession = canAccess(request, mapping, DefSecurityConstants.ABM_CACHE, DefSecurityConstants.MTD_REFRESCAR_CACHE); 
		if (userSession == null) return forwardErrorSession(request);
		
		try {
			String cache = request.getParameter("cache");
			IDefConfiguracionService defConf = DefServiceLocator.getConfiguracionService();
			//si no viene el parametro, pasa null, que siginifica actualizar todos.
			defConf.refreshCache(cache);
			
		} catch (Exception exception) {
			return baseException(mapping, request, funcName, exception, "");
		}

		return mapping.findForward(SegConstants.ACTION_SIATMENU);
	}

}
