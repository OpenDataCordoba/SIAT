//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.buss.auth;

import ar.gov.rosario.swe.iface.model.UsuarioVO;

public abstract class SweAuthLogin {
	
	public static final String SWE_AUTH_SUCCESS_LOGIN			= "swe.auth.login.success";
	public static final String SWE_AUTH_INVALID_LOGIN			= "swe.auth.login.fail";	
	
	public static final String SWE_AUTH_SUCCESS_UPDATE			= "swe.auth.update.success";
	public static final String SWE_AUTH_INVALID_UPDATE			= "swe.auth.update.fail";
	public static final String SWE_AUTH_INVALID_USR_NAME		= "swe.auth.usrname.invalid";
	public static final String SWE_AUTH_INVALID_EMPTY_PASSWORD	= "swe.auth.password.empty";
	public static final String SWE_AUTH_INVALID_RETYPE_PASSWORD	= "swe.auth.password.fail";
	
	public static final String SWE_AUTH_SUCCESS_DELETE			= "swe.auth.delete.success";
	public static final String SWE_AUTH_FAIL_DELETE				= "swe.auth.delete.fail";
	
	
	public abstract String login(UsuarioVO usuarioVO, String codApp);

}
