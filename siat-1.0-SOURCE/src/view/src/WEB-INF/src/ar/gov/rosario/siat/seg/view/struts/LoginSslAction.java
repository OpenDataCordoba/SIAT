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

/**
 * Esta clase se utiliza para manejar los request relacionados con el manejo de Login y Logout que utilizan SSL
 * Esta clase solo hace publico algunos metodos de LoginSiat. En especial los metodos que pueden ser llamados utilizando SSL
 * Los metodos que son usados SIN SSL, estan en LoginAction
*/
public final class LoginSslAction extends LoginSiat {
	
	private Log log = LogFactory.getLog(LoginSslAction.class);
	
	/**
	 * Hace publico el metodo para obtner el formulario de ingreso de login via WEB
	 * Este metodo solo funciona si se trata de webSiat.war
	 * Si es ejecutado apuntando a intraSiat.war retorna error 
	 */
	public ActionForward webInit(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		return super.init(mapping, form, request, response, LoginSiat.FROM_WEB_LOGIN);
	}
	
	/**
	 * Hace publico el metodo para autentica usuarios desde la web
	 * Este metodo solo funciona si se trata de webSiat.war
	 * Si es ejecutado apuntando a intraSiat.war retorna error 
	 */
	public ActionForward webLogin(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		return super.login(mapping, form, request, response, LoginSiat.FROM_WEB_LOGIN);
	}
	
	/**
	 * Hace publico el metodo para mostrar el formulario de ingreso a siat desde la intranet.
	 * Este metodo solo funciona si se trata de intraSiat.war
	 * Si es ejecutado apuntando a webSiat.war retorna error 
	 */
	public ActionForward intranetInit(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		return super.init(mapping, form, request, response, LoginSiat.FROM_INTRANET_LOGIN);
	}
	
	/**
	 * Hace publico el metodo para autentica usuarios desde la intraner
	 * Este metodo solo funciona si se trata de intraSiat.war
	 * Si es ejecutado apuntando a webSiat.war retorna error 
	 */
	public ActionForward intranetLogin(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		return super.login(mapping, form, request, response, LoginSiat.FROM_INTRANET_LOGIN);
	}
	
	/**
	 * Hace publico el metodo para registrar la oficina desde la que se ingresa al SIAT
	 */
	public ActionForward loginSelecOficina(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
	throws Exception {
		return super.loginSelecOficina(mapping, form, request, response);
	}
	
	
	public ActionForward changePassInit(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {
			return super.changePassInit(mapping, form, request, response);
	}
	
	public ActionForward changePass(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {
			return super.changePass(mapping, form, request, response);
	}
	
	public ActionForward volver(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
		throws Exception {
			return super.volver(mapping, form, request, response);
	}
}
