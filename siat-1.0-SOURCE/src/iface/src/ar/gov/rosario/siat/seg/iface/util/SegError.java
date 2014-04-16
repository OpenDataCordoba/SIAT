//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class SegError extends DemodaError {
	// Use Codigos desde 17000 hasta 17999
    // static public String XXXXXX_XXXX_XXX   = addError(17000, "seg.xxxxxx");

	// ---> UsuarioVO (de swe)
	public static final String USUARIO_NEWPASS_LABEL       = addError(0, "seg.changePasswordForm.newPassword.label");
	public static final String USUARIO_NEWPASSREENTRY_LABEL  = addError(0, "seg.changePasswordForm.newPasswordReentry.label");
	public static final String MSG_NO_COINICEN_ERROR  = addError(0, "seg.changePasswordForm.msgNoCoinciden.error");
	public static final String MSG_SERVICE_ERROR  = addError(0, "seg.changePasswordForm.service.error");
	// <--- UsuarioVO (de swe)

	
	// --> UsuarioSIAT
	public static final String USUARIOSIAT_LABEL       = addError(17000, "seg.usuarioSiat.label");
	public static final String USUARIOSIAT_USUARIOSIAT = addError(17001, "seg.usuarioSiat.usuarioSIAT.label");
	public static final String NO_EXISTE_USUARIOSIAT = addError(17002, "seg.usuarioSiat.noExisteUsuario");

	public static final String NO_LOGIN_INSTANCIA_WEB = addError(17003, "seg.loginSiat.noLoginInstanciaWeb");
	public static final String NO_LOGIN_INSTANCIA_INTRA = addError(17004, "seg.loginSiat.noLoginInstanciaIntra");
	public static final String NO_PERMITEWEB = addError(17005, "seg.loginSiat.noPermiteWeb");

	// <-- UsuarioSIAT

	// --> Oficina
	public static final String OFICINA_LABEL       = addError(17100, "seg.oficina.label");
	public static final String OFICINA_DESOFICINA  	 = addError(17101,"seg.oficina.desOficina.label");
	// <-- Oficina

}

