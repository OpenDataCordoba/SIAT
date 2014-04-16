//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.seg.view.util;


public interface SweSegConstants {
	public static final String FWD_WELCOME = "welcome";
	public static final String FWD_LOGINFORM = "loginForm";
    
	// ---> Aplicacion 
	public static final String FWD_APLICACION_SEARCHPAGE = "aplicacionSearchPage";
	public static final String ACTION_ADMINISTRAR_APLICACION = "administrarAplicacion";
	public static final String FWD_APLICACION_ADAPTER_VIEW = "aplicacionAdapterView";
	public static final String FWD_APLICACION_ADAPTER = "aplicacionAdapter";
	// <--- Aplicacion
    
	//	 ---> UsrApl 
	public static final String ACTION_BUSCAR_USRAPL      = "buscarUsrApl";
	public static final String FWD_USRAPL_SEARCHPAGE     = "usrAplSearchPage";
	public static final String ACTION_ADMINISTRAR_USRAPL = "administrarUsrApl";
	public static final String ACTION_CLONAR_USRAPL      = "administrarCloneUsrApl";
	public static final String FWD_USRAPL_ADAPTER_VIEW   = "usrAplAdapterView";
	public static final String FWD_USRAPL_ADAPTER        = "usrAplAdapter";
	public static final String FWD_CLONEUSRAPL_ADAPTER   = "cloneUsrAplAdapter";
	// <--- UsrApl
	
	//	 ---> UsrRolApl 
	public static final String ACTION_BUSCAR_USRROLAPL      = "buscarUsrRolApl";
	public static final String FWD_USRROLAPL_SEARCHPAGE     = "usrRolAplSearchPage";
	public static final String ACTION_ADMINISTRAR_USRROLAPL = "administrarUsrRolApl";
	public static final String FWD_USRROLAPL_ADAPTER_VIEW   = "usrRolAplAdapterView";
	public static final String FWD_USRROLAPL_ADAPTER        = "usrRolAplAdapter";
	public static final String ACTION_BUSCAR_ROLAPL_FOR_CREATE_USRROLAPL  = "buscarUsrRolAplForCreateUsrRolApl";
	
	// <--- UsrRolApl
	
	// ---> Modulo Aplicacion
	public static final String ACTION_BUSCAR_MODAPL      = "buscarModApl";	
	public static final String FWD_MODAPL_SEARCHPAGE     = "modAplSearchPage";
	public static final String ACTION_ADMINISTRAR_MODAPL = "administrarModApl";
	public static final String FWD_MODAPL_ADAPTER_VIEW   = "modAplAdapterView";
	public static final String FWD_MODAPL_ADAPTER        = "modAplAdapter";
	// <--- Modulo Aplicacion  
	
	// ---> Acciones Modulo Aplicacion
	public static final String ACTION_BUSCAR_ACCMODAPL      = "buscarAccModApl";	
	public static final String FWD_ACCMODAPL_SEARCHPAGE     = "accModAplSearchPage";
	public static final String ACTION_ADMINISTRAR_ACCMODAPL = "administrarAccModApl";
	public static final String FWD_ACCMODAPL_ADAPTER_VIEW   = "accModAplAdapterView";
	public static final String FWD_ACCMODAPL_ADAPTER        = "accModAplAdapter";
	// <--- Acciones Modulo Aplicacion 	
    
	//	 ---> Rol Aplicacion
	public static final String ACTION_BUSCAR_ROLAPL      = "buscarRolApl";	
	public static final String FWD_ROLAPL_SEARCHPAGE     = "rolAplSearchPage";
	public static final String ACTION_ADMINISTRAR_ROLAPL = "administrarRolApl";
	public static final String FWD_ROLAPL_ADAPTER_VIEW   = "rolAplAdapterView";
	public static final String FWD_ROLAPL_ADAPTER        = "rolAplAdapter";
	// <--- Rol Aplicacion    
	
	// ---> Rol Accion Modulo Aplicacion
	public static final String ACTION_BUSCAR_ROLACCMODAPL = "buscarRolAccModApl";
	public static final String FWD_ROLACCMODAPL_SEARCHPAGE = "rolAccModAplSearchPage";
	public static final String ACTION_ADMINISTRAR_ROLACCMODAPL = "administrarRolAccModApl";
	public static final String FWD_ROLACCMODAPL_ADAPTER_VIEW   = "rolAccModAplAdapterView";
	
	// <---
	
    //-----------------------------------------------------------------------------------------------------------
    public static final String ACTION_LOGIN   = "login";
    public static final String FWD_LOGIN_FORM = "loginForm";
    
    public static final String ACTION_CHANGE_PASSWORD   = "changePassword";
    public static final String FWD_CHANGE_PASSWORD_FORM = "changePasswordForm";
    
    public static final String ACTION_SELECCIONAR_CS = "seleccionarCS";
    public static final String FWD_SELECCIONCS_FORM  = "seleccionCSForm";
    
    public static final String ACTION_BUILD_MENU = "buildMenu";
    public static final String FWD_MENU_SUCCESS  = "menuSuccess";
    public static final String FWD_MENU_ACCION_DEFAULT  = "accionDefault";
    
    public static final String ACTION_LOGOUT = "logout";
    
    public static final String ACTION_BUSCAR_USUARIO = "buscarUsuario";
    public static final String FWD_USUARIO_PAGE      = "usuarioPage";

    public static final String ACTION_ADMINISTRAR_USUARIO = "administrarUsuario";
    public static final String FWD_USUARIO_ADAPTER        = "usuarioAdapter";
    public static final String FWD_USUARIO_ADAPTER_VIEW   = "usuarioAdapterView";
    public static final String FWD_USUARIO_E_ADAPTER      = "usuarioEAdapter";
    public static final String FWD_USUARIO_AM_ADAPTER     = "usuarioAMAdapter";
    public static final String FWD_USUARIO_AR_ADAPTER     = "usuarioARAdapter";
    
    public static final String ACTION_ADMINISTRAR_USUARIO_ROL = "administrarUsuarioRol";
    public static final String FWD_USUARIO_ROL_ADAPTER        = "usuarioRolAdapter";
    public static final String FWD_USUARIO_ROL_ADAPTER_VIEW   = "usuarioRolAdapterView";
    
    
	// ABM de Roles ----------------------------------------------------
	public static final String ACTION_BUSCAR_ROL      	= "buscarRol";
	public static final String FWD_BUSCAR_ROL_PAGE       = "rolSearchPage";

	public static final String ACTION_ADMINISTRAR_ROL    = "administrarRol";
	public static final String FWD_ROL_ADAPTER           = "rolAdapter";
	public static final String FWD_ROL_ADAPTER_VIEW      = "rolAdapterView";
	
	public static final String ACTION_BUSCAR_ROL_ACCION_MOD 	= "buscarRolAccionMod";
	public static final String FWD_BUSCAR_ROL_ACCIONMOD_PAGE = "rolAccionModSearchPage";

	public static final String ACTION_ADMINISTRAR_ROL_ACCIONMOD = "administrarRolAccionMod";
	public static final String FWD_ROL_ACCIONMOD_ADAPTER        = "rolAccionModAdapter";
	public static final String FWD_ROL_ACCIONMOD_ADAPTER_VIEW   = "rolAccionModAdapterView";
	public static final String FWD_ROL_ACCIONMOD_ADAPTER_M      = "rolAccionModAdapterM";
	// Fin ABM de Roles ------------------------------------------------

	//	 ---> ItemMenu
	public static final String ACTION_BUSCAR_ITEM_MENU       = "buscarItemMenuRoot";	
	public static final String FWD_ITEM_MENU_SEARCHPAGE      = "itemMenuSearchPage";
	public static final String ACTION_ADMINISTRAR_ITEM_MENU  = "administrarItemMenu";
	public static final String FWD_ITEM_MENU_ADAPTER_VIEW    = "itemMenuAdapterView";
	public static final String FWD_ITEM_MENU_ADAPTER         = "itemMenuAdapter";
	public static final String FWD_ITEM_MENU_ACC_MOD_ADAPTER = "itemMenuAccModAdapter";
	public static final String FWD_ITEM_MENU_HIJOS_ADAPTER   = "itemMenuHijosAdapter";
	public static final String ACTION_VOLVER_BUSCAR_APLICACION = "volverBuscarAplicacion";	
	
	public static final String ACT_AGREGAR_ROOT             = "agregarItemMenuRoot";
	public static final String ACT_AGREGAR_HIJO             = "agregarItemMenuHijo";
	public static final String ACT_MODIFICAR_ACC_MOD_APL    = "modificarAccModApl";
	public static final String ACT_ADMINISTRAR_ITEM_MENU_HIJOS = "administrarItemMenuHijos";
	
	// <--- ItemMenu    


}
