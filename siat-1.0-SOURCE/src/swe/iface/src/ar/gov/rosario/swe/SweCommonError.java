//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe;


import coop.tecso.demoda.iface.error.DemodaError;



/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class SweCommonError extends DemodaError {
	
	static public String LOGIN_NOPER = addError(100, "swe.login.noper");
	static public String LOGIN_NOACC = addError(101, "swe.login.noacc");
	static public String LOGIN_NOUSRAPL = addError(102, "swe.login.nousrapl");

	// Aplicacion -1000
	static public String APLICACION_CODIGO_REQUIRED      = addError(1000, "swe.seg.aplicacion.codigo.required");
	static public String APLICACION_DESCRIPCION_REQUIRED = addError(1001, "swe.seg.aplicacion.descripcion.required");
	static public String APLICACION_USRAPL_HASREF = addError(1002, "swe.seg.aplicacion.usrapl.hasref");
	static public String APLICACION_ROLAPL_HASREF = addError(1003, "swe.seg.aplicacion.rolapl.hasref");
	static public String APLICACION_MODAPL_HASREF = addError(1004, "swe.seg.aplicacion.modapl.hasref");
	static public String APLICACION_ITEMMENU_HASREF = addError(1005, "swe.seg.aplicacion.itemmenu.hasref");
	static public String APLICACION_PERMISOSAPP_HASREF = addError(1005, "swe.seg.aplicacion.permisos.hasref");
	static public String APLICACION_TIPOAUTH_REQUIRED = addError(1011, "swe.seg.aplicacion.tipoauth.required");
	
	// errores de contraint violation 
	static public String APLICACION_UNIQUE = addError(1006, "swe.seg.aplicacion.unique");
	static public String APLICACION_HASREF = addError(1007, "swe.seg.aplicacion.hasref");
    static public String APLICACION_SEGTIMEOUT_REQUIRED = addError(1008, "swe.seg.aplicacion.segtimeout.required");
    static public String APLICACION_MAXNIVELMENU_REQUIRED = addError(1009, "swe.seg.aplicacion.maxnivelmenu.required");
    static public String APLICACION_MAXNIVELMENU_MENOR_CERO = addError(1010, "swe.seg.aplicacion.maxnivelmenu.menorcero");
    
	// UsrApl - 1100 
    static public String USR_APL_USERNAME_INEXISTENTE = addError(1100, "swe.seg.usrapl.username.inexistente");
    static public String USR_APL_USERNAME_REQUIRED    = addError(1100, "swe.seg.usrapl.username.required");
    static public String USR_APL_PASSWORD_REQUIRED    = addError(1101, "swe.seg.usrapl.password.required");
	static public String USR_APL_USERNAME_UNIQUE      = addError(1102, "swe.seg.usrapl.username.unique");
	static public String USR_APL_FECHAALTA_REQUIRED   = addError(1103, "swe.seg.usrapl.fechaAlta.required");
	// errores de contraint violation
	static public String USR_APL_UNIQUE               = addError(1105, "swe.seg.usrapl.unique");
	static public String USR_APL_HASREF     = addError(1106, "swe.seg.usrapl.usrrolapl.hasref"); //usrapl tiene referencias desde usrrolapl

	// UsrRolaApl - 1200
	// errores de contraint violation
	static public String USRROLAPL_UNIQUE = addError(1200, "swe.seg.usrapl.username.unique");
	static public String USRROLAPL_HASREF = addError(1201, "swe.seg.usrapl.username.hasref");	

	// ModApl - 1300
	static public String MODAPL_NOMBREMODULO_REQUIRED = addError(1300, "swe.seg.modApl.nombre.required");	
	// errores de contraint violation
	static public String MODAPL_UNIQUE                = addError(1302, "swe.seg.modapl.unique");
	static public String MODAPL_HASREF                = addError(1303, "swe.seg.modapl.hasref");
	static public String MODAPL_ACCMODAPL_HASREF      = addError(1304, "swe.seg.modapl.accmodapl.hasref");

	// RolApl - 1400
	static public String ROLAPL_CODIGO_REQUIRED = addError(1400, "swe.seg.rolApl.codigo.required");
	static public String ROLAPL_DESCRIPCION_REQUIRED = addError(1401, "swe.seg.rolApl.descripcion.required");
	// errores de contraint violation
	static public String ROLAPL_UNIQUE = addError(1402, "swe.seg.rolapl.unique");
	static public String ROLAPL_HASREF = addError(1403, "swe.seg.rolapl.hasref");	
	static public String ROLAPL_USRROLAPL_HASREF = addError(1404, "swe.seg.rolapl.usrRolApl.hasref");
	static public String ROLAPL_ROLACCMODAPL_HASREF = addError(1405, "swe.seg.rolapl.rolAccModApl.hasref");
	
	// AccModApl - 1500
	static public String ACCMODAPL_APLICACION_REQUIRED = addError(1020, "swe.seg.accModApl.aplicacion.required");
	static public String ACCMODAPL_MODAPL_REQUIRED   = addError(1021, "swe.seg.accModApl.aplicacion.required");
	static public String ACCMODAPL_NOMBREACCION_REQUIRED   = addError(1022, "swe.seg.accModApl.nombreAccion.required");
	static public String ACCMODAPL_NOMBREMETODO_REQUIRED   = addError(1023, "swe.seg.accModApl.nombreMetodo.required");
	static public String ACCMODAPL_DESCRIPCION_REQUIRED   = addError(1023, "swe.seg.accModApl.descripcion.required");	
	// errores de contraint violation
	static public String ACCMODAPL_UNIQUE = addError(1500, "swe.seg.accmodapl.unique");
	static public String ACCMODAPL_HASREF = addError(1501, "swe.seg.accmodapl.hasref");
	static public String ACCMODAPL_ROLACCMODAPL_HASREF = addError(1502, "swe.seg.accmodapl.rolaccmodapl.hasref");
	static public String ACCMODAPL_ITEMMENU_HASREF  = addError(1503, "swe.seg.accmodapl.itemmenu.hasref");

	// Rol Acc Mol Apl - 1600
	static public String ROLACCMODAPL_ACCMODAPL_REQUIRED = addError(1600, "swe.seg.rolAccModApl.accModApl.required");
	static public String ROLACCMODAPL_ROLAPL_REQUIRED = addError(1601, "swe.seg.rolAccModApl.rolApl.required");
	// errores de contraint violation
	static public String ROLACCMODAPL_UNIQUE = addError(1602, "swe.seg.rolAccModApl.rolApl.unique");
	static public String ROLACCMODAPL_HASREF = addError(1603, "swe.seg.rolAccModApl.rolApl.hasref");	
	
	// ItemMenu - 1700
	static public String ITEM_MENU_ITEM_MENU_PADRE_HASREF = addError(1700, "swe.seg.itemMenu.itemMenuPadre.hasref");
	static public String ITEM_MENU_TITULO_REQUIRED = addError(1701, "swe.seg.itemMenu.titulo.required");
	// errores de contraint violation
	static public String ITEM_MENU_UNIQUE = addError(1702, "swe.seg.itemMenu.unique");
	static public String ITEM_MENU_HASREF = addError(1703, "swe.seg.itemMenu.hasref");	
	
	// UsrAuth - 1800 
	static public String USRAUTH_NOMUSR_REQUIRED	= addError(1801, "swe.seg.usrAuth.userName.required");
	static public String USRAUTH_PASS_REQUIRED 		= addError(1802, "swe.seg.usrAuth.password.required");	
	static public String USRAUTH_LOGIN_ERROR 		= addError(1803, "swe.seg.usrAuth.login.error");
	static public String USRAUTH_UPDATE_ERROR 		= addError(1804, "swe.seg.usrAuth.update.error");
	static public String USRAUTH_PASS_ERROR 		= addError(1805, "swe.seg.usrAuth.password.error");
	static public String USRAUTH_DELETE_ERROR 		= addError(1806, "swe.seg.usrAuth.delete.error");
	static public String USRAUTH_UNIQUE		 		= addError(1807, "swe.seg.usrAuth.unique");

}

