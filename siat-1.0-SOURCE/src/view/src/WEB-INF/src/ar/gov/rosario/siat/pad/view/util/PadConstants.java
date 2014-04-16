//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.view.util;

public interface PadConstants {
	
	// ---> Persona
	public static final String ACTION_BUSCAR_PERSONA     	= "buscarPersona";
	public static final String ACTION_ADMINISTRAR_PERSONA 	= "administrarPersona";
	
	public static final String FWD_PERSONA_FISICA_SEARCHPAGE = "personaFisicaSearchPage";
	public static final String FWD_PERSONA_JURIDICA_SEARCHPAGE = "personaJuridicaSearchPage";
	
	public static final String FWD_PERSONA_VIEW_ADAPTER 	= "personaViewAdapter";
	public static final String FWD_PERSONA_EDIT_ADAPTER 	= "personaEditAdapter";
	public static final String METOD_PARAM_LOCALIDAD        = "paramLocalidad";
	
	public static final String PATH_BUSCAR_PERSONA     		= "/pad/BuscarPersona.do?method=buscar"; 
	public static final String PATH_ADMINISTRAR_PERSONA     = "/pad/AdministrarPersona.do?method=inicializar&selectedId="; 
	// <--- Persona

	// ---> Contribuyente
	public static final String ACTION_ADMINISTRAR_CONTRIBUYENTE     = "administrarContribuyente";
	public static final String FWD_CONTRIBUYENTE_VIEW_ADAPTER 	    = "contribuyenteViewAdapter";
	public static final String FWD_CONTRIBUYENTE_ADAPTER 	        = "contribuyenteAdapter";
	public static final String ACTION_ADMINISTRAR_ENCCONTRIBUYENTE  = "administrarEncContribuyente";
	public static final String FWD_CONTRIBUYENTE_ENC_EDIT_ADAPTER 	= "contribuyenteEncEditAdapter";
	// <--- Contribuyente

	// ---> ObjImpAtrVal
	public static final String ACTION_ADMINISTRAR_CONATRVAL = "administrarConAtrVal";
	public static final String FWD_CONATRVAL_VIEW_ADAPTER 	= "conAtrValViewAdapter";
	public static final String FWD_CONATRVAL_EDIT_ADAPTER 	= "conAtrValEditAdapter";		
	// <--- ObjImpAtrVal
	
	// ---> Calle
	public static final String ACTION_BUSCAR_CALLE = "buscarCalle";
	public static final String FWD_CALLE_SEARCHPAGE = "calleSearchPage";
	
	// <--- Calle
	
	// ---> Localidad
	public static final String ACTION_BUSCAR_LOCALIDAD = "buscarLocalidad";
	public static final String FWD_LOCALIDAD_SEARCHPAGE = "localidadSearchPage";
	
	// <--- Localidad
	
	// ---> Domicilio 
	public static final String ACT_GET_FOR_VALIDATE = "getForValidate";
	
	public static final String FWD_DOMICILIO_EDIT_ADAPTER = "domicilioEditAdapter";
	public static final String FWD_DOMICILIO_VIEW_ADAPTER = "domicilioViewAdapter";
	public static final String FWD_DOMICILIO_SEARCHPAGE   = "domicilioSearchPage";
	
	// <--- Domoicilio
	
	
	// ---> ObjImp
	public static final String ACTION_BUSCAR_OBJIMP     	= "buscarObjImp";
	public static final String ACTION_ADMINISTRAR_OBJIMP 	= "administrarObjImp";
	public static final String FWD_OBJIMP_SEARCHPAGE 		= "objImpSearchPage";
	public static final String FWD_OBJIMP_SEARCHPAGEAVA		= "objImpSearchPageAva";
	public static final String FWD_OBJIMP_VIEW_ADAPTER 		= "objImpViewAdapter";
	public static final String FWD_OBJIMP_EDIT_ADAPTER 		= "objImpEditAdapter";
	public static final String FWD_OBJIMP_ADAPTER			= "objImpAdapter";
	public static final String ACTION_ADMINISTRAR_ENCOBJIMP = "administrarEncObjImp";
	public static final String FWD_OBJIMP_ENC_EDIT_ADAPTER 	= "objImpEncEditAdapter";	
	// <--- ObjImp

	// ---> ObjImpAtrVal
	public static final String ACTION_ADMINISTRAR_OBJIMPATRVAL = "administrarObjImpAtrVal";
	public static final String FWD_OBJIMPATRVAL_VIEW_ADAPTER 	= "objImpAtrValViewAdapter";
	public static final String FWD_OBJIMPATRVAL_EDIT_ADAPTER 	= "objImpAtrValEditAdapter";		
	// <--- ObjImpAtrVal
	
	// ---> CuentaTitular
	public static final String ACTION_ADMINISTRAR_CUENTATITULAR = "administrarCuentaTitular";
	public static final String FWD_CUENTATITULAR_VIEW_ADAPTER 	= "cuentaTitularViewAdapter";
	public static final String FWD_CUENTATITULAR_EDIT_ADAPTER 	= "cuentaTitularEditAdapter";
	
	// <--- CuentaTitular
	
	// ---> CuentaRel
	public static final String ACTION_ADMINISTRAR_CUENTAREL = "administrarCuentaRel";
	public static final String FWD_CUENTAREL_VIEW_ADAPTER 	= "cuentaRelViewAdapter";
	public static final String FWD_CUENTAREL_EDIT_ADAPTER 	= "cuentaRelEditAdapter";	
	public static final String PATH_ADMINISTRAR_CUENTAREL  	= "/pad/AdministrarCuentaRel";
	// <--- CuentaRel
	
	// ---> Cuenta
	public static final String ACTION_BUSCAR_CUENTA     	= "buscarCuenta";
	public static final String FWD_CUENTA_SEARCHPAGE 	    = "cuentaSearchPage";
	public static final String ACTION_ADMINISTRAR_CUENTA    = "administrarCuenta";
	public static final String ACTION_RELACIONAR_CUENTA    = "relacionarCuenta";
	public static final String FWD_CUENTA_VIEW_ADAPTER 		= "cuentaViewAdapter";
	public static final String FWD_CUENTA_EDIT_ADAPTER 		= "cuentaEditAdapter";
	public static final String FWD_CUENTA_ADAPTER 		    = "cuentaAdapter";
	public static final String FWD_RELACIONAR_CUENTA_ADAPTER = "relacionarCuentaAdapter";
	public static final String ACTION_ADMINISTRAR_ENCCUENTA = "administrarEncCuenta";	
	public static final String FWD_CUENTA_ENC_EDIT_ADAPTER 	= "cuentaEncEditAdapter";	
	public static final String FWD_CUENTA_ENC_EDIT_AGREGAR_ADAPTER 	= "cuentaEncEditAgregarAdapter";
	public static final String PATH_ADMINISTRAR_CUENTA     	= "/pad/AdministrarCuenta";
	public static final String PATH_BUSCAR_CUENTA     	    = "/pad/BuscarCuenta";
	public static final String FWD_CUENTA_MODIF_DOM_ENV_ADAPTER  = "cuentaDomicilioEnvioAdapter";
	public static final String METHOD_CUENTA_PARAM_PERSONA = "paramPersona";
	
	public static final String METOD_CUENTA_PARAM_TITULAR = "paramTitular";
	public static final String METOD_CUENTA_PARAM_OBJ_IMP = "paramObjImp";
	public static final String METOD_CUENTA_PARAM_ASIGNAR_BROCHE    = "paramAsignarBroche";
	public static final String METOD_CUENTA_PARAM_QUITAR_BROCHE    = "paramQuitarBroche";
	public static final String METOD_CUENTA_PARAM_MODIFICAR_BROCHE    = "paramModificarBroche";
	
	public static final String ACT_MODIFICAR_DOMICILIO_ENV = "modificarDomicilioEnvio";
	public static final String ACT_AGREGAR_DOMICILIO_ENV   = "agregarDomicilioEnvio";
	public static final String ACT_QUITAR_BROCHE_INIT	   = "quitarBrocheInit";
	public static final String ACT_MODIFICAR_BROCHE_INIT   = "modificarBrocheInit";  
	public static final String ACT_MARCAR_PRINCIPAL		   = "marcarPrincipal"; 
	
	public static final String VOLVER_BUSCAR_CUENTA = "volverBuscarCuenta";
		
	public static final String FWD_CUENTACMD_SEARCHPAGE = "cuentaCMDSearchPage";
	
	public static final String ACTION_ADMINISTRAR_RECATRCUEV = "administrarRecAtrCueV";
	public static final String FWD_RECATRCUEV_EDIT_ADAPTER = "recAtrCueVEditAdapter";
	// <--- Cuenta
	
	// ---> EstadoCuenta
	public static final String ACTION_ADMINISTRAR_ESTADOCUENTA		="administrarEstCue";
	public static final String FWD_ESTADOCUENTA_SEARCHPAGE			="estCueSearchPage";
	public static final String FWD_ESTADOCUENTA_VIEW_ADAPTER		="estCueViewAdapter";
	public static final String FWD_ESTADOCUENTA_EDIT_ADAPTER		="estCueEditAdapter";
	// <--- EstadoCuenta
	// Cambio de domicilio de envio
	
	
	public static final String ACTION_ADMINISTRAR_CAMBIO_DOM_ENVIO 	= "administrarCambiarDomEnvio";
	
	public static final String FWD_CAMBIAR_DOM_ENVIO_INGRESO     = "cambiarDomEnvioIngreso";          
	public static final String FWD_CAMBIAR_DOM_ENVIO_CUENTAS_REL = "cambiarDomEnvioCuentasRel";
	public static final String FWD_CAMBIAR_DOM_ENVIO_NUEVO_DOM   = "cambiarDomEnvioNuevoDom";
	public static final String FWD_CAMBIAR_DOM_ENVIO_SOLICITANTE = "cambiarDomEnvioSolicitante";
	
	
	// ---> Repartidor
	// Repartidor
	public static final String ACTION_BUSCAR_REPARTIDOR     	= "buscarRepartidor";
	public static final String FWD_REPARTIDOR_SEARCHPAGE 	    = "repartidorSearchPage";
	public static final String ACTION_ADMINISTRAR_REPARTIDOR 	= "administrarRepartidor";
	public static final String FWD_REPARTIDOR_VIEW_ADAPTER 		= "repartidorViewAdapter";
	public static final String FWD_REPARTIDOR_ADAPTER 		    = "repartidorAdapter";
	public static final String ACTION_ADMINISTRAR_ENCREPARTIDOR = "administrarEncRepartidor";	
	public static final String FWD_REPARTIDOR_ENC_EDIT_ADAPTER 	= "repartidorEncEditAdapter";	
	public static final String PATH_ADMINISTRAR_REPARTIDOR     	= "/pad/AdministrarRepartidor";
	public static final String METOD_REPARTIDOR_PARAM_PERSONA 	= "paramPersona";

	// CriRepCat
	public static final String ACTION_ADMINISTRAR_CRIREPCAT  = "administrarCriRepCat";
	public static final String FWD_CRIREPCAT_VIEW_ADAPTER 	 = "criRepCatViewAdapter";
	public static final String FWD_CRIREPCAT_EDIT_ADAPTER 	 = "criRepCatEditAdapter";

	// CriRepCalle
	public static final String ACTION_ADMINISTRAR_CRIREPCALLE  = "administrarCriRepCalle";
	public static final String FWD_CRIREPCALLE_VIEW_ADAPTER 	 = "criRepCalleViewAdapter";
	public static final String FWD_CRIREPCALLE_EDIT_ADAPTER 	 = "criRepCalleEditAdapter";
	public static final String METOD_CRIREPCALLE_PARAM_CALLE 	= "paramCalle";
	
	// ---> Broche
	// Broche
	public static final String FWD_BROCHE_SEARCHPAGE 	    = "brocheSearchPage";
	public static final String ACTION_BUSCAR_BROCHE 		= "buscarBroche";
	public static final String ACTION_ADMINISTRAR_BROCHE 	= "administrarBroche";
	public static final String FWD_BROCHE_VIEW_ADAPTER 		= "brocheViewAdapter";
	public static final String FWD_BROCHE_EDIT_ADAPTER 	    = "brocheEditAdapter";
	public static final String PATH_ADMINISTRAR_BROCHE     	= "/pad/AdministrarBroche";
	public static final String ACT_ADM_BROCUE		     	= "asignarCuenta";
	
	// BroCue
	public static final String FWD_BROCUE_SEARCHPAGE 	    = "broCueSearchPage";
	public static final String ACTION_ADMINISTRAR_BROCUE  = "administrarBroCue";
	public static final String FWD_BROCUE_ADAPTER 	 = "broCueAdapter";
	public static final String FWD_BROCUE_VIEW_ADAPTER 	 = "broCueViewAdapter";
	public static final String FWD_BROCUE_EDIT_ADAPTER 	 = "broCueEditAdapter";
	public static final String METOD_BROCUE_PARAM_CUENTA 	= "paramCuenta";
	
	// ---> CueExcSel 
	public static final String FWD_CUEEXCSEL_SEARCHPAGE 			  = "cueExcSelSearchPage"; 
	public static final String ACTION_ADMINISTRAR_CUEEXCSEL     	  = "administrarCueExcSel";
	public static final String ACTION_ADMINISTRAR_ENC_CUEEXCSEL 	  = "administrarEncCueExcSel";
	public static final String FWD_CUEEXCSEL_ENC_EDIT_ADAPTER		  = "cueExcSelEncEditAdapter";
	public static final String PATH_ADMINISTRAR_CUEEXCSEL     		  = "/pad/AdministrarCueExcSel";
	public static final String FWD_CUEEXCSEL_ENC_EDIT_AGREGAR_ADAPTER = "cueExcSelEncEditAgregarAdapter";
	public static final String FWD_CUEEXCSEL_VIEW_ADAPTER			  = "cueExcSelViewAdapter";
	public static final String FWD_CUEEXCSEL_ADAPTER				  = "cueExcSelAdapter";
	// <--- CueExcSel
	
	// ---> CueExcSelDeu
	public static final String ACTION_ADMINISTRAR_CUEEXCSELDEU = "administrarCueExcSelDeu";
	public static final String FWD_CUEEXCSELDEU_ADAPTER 	   = "cueExcSelDeuAdapter";	
	public static final String FWD_CUEEXCSELDEU_VIEW_ADAPTER   = "cueExcSelDeuViewAdapter";
	// <--- CueExcSelDeu
	
	// ---> Buzon Cambio datos Persona
	public static final String FWD_BUZONCAMBIOS_EDIT_ADAPTER = "buzonCambiosEditAdapter";
	public static final String PATH_ADMINISTRAR_BUZON_CAMBIOS = "/pad/AdministrarBuzonCambios";
	public static final String PATH_ADMINISTRAR_BUZON_CAMBIOS_INICIALIZAR = "/pad/AdministrarBuzonCambios.do?method=inicializar";
	// <----

	// ---> AltaOficio
	public static final String FWD_ALTAOFICIO_SEARCHPAGE 		= "altaOficioSearchPage";
	public static final String ACTION_ADMINISTRAR_ALTAOFICIO 	= "administrarAltaOficio";
	public static final String FWD_ALTAOFICIO_EDIT_ADAPTER		= "altaOficioEditAdapter";
	public static final String METOD_PARAM_INSPECTOR 			= "paramInspector";
	public static final String ACTION_BUSCAR_INSPECTOR 			= "buscarInspector";
	public static final String FWD_ALTAOFICIO_VIEW_ADAPTER 		= "altaOficioViewAdapter";
	public static final String METOD_PARAM_SUPERVISOR 			= "paramSupervisor";
	public static final String ACTION_BUSCAR_SUPERVISOR 		= "buscarSupervisor";

	// <--- cirreComercio
	public static final String FWD_CIERRECOMERCIO_SEARCHPAGE = "cierreComercioSearchPage";
	public static final String ACTION_ADMINISTRAR_CIERRECOMERCIO = "administrarCierreComercio";
	public static final String ACT_CIERRECOMERCIO_IMPRIMIR = "imprimir";
	// ---> cierreComercio

}
