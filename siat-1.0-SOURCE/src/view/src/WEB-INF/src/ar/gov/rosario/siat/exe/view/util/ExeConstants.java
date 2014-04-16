//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.view.util;


public interface ExeConstants {
    
	// ---> Exencion (Encabezado)
	public static final String ACTION_BUSCAR_EXENCION     	= "buscarExencion";
	public static final String FWD_EXENCION_SEARCHPAGE 	    = "exencionSearchPage";
	
	public static final String ACTION_ADMINISTRAR_EXENCION 	= "administrarExencion";
	
	public static final String FWD_EXENCION_VIEW_ADAPTER 		= "exencionViewAdapter";
	public static final String FWD_EXENCION_ENC_EDIT_ADAPTER 	= "exencionEncEditAdapter";
	public static final String FWD_EXENCION_ADAPTER 		    	= "exencionAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_EXENCION 	= "administrarEncExencion";	
	public static final String PATH_ADMINISTRAR_EXENCION     	= "/exe/AdministrarExencion"; // utilizado para redirigir en el agregar Exencion
	
	// ---> ExeRecCon (Detalle)
	public static final String ACTION_ADMINISTRAR_EXERECCON 	= "administrarExeRecCon";
	public static final String FWD_EXERECCON_ADAPTER 			= "exeRecConAdapter";
	public static final String FWD_EXERECCON_VIEW_ADAPTER 	= "exeRecConViewAdapter";
	public static final String FWD_EXERECCON_EDIT_ADAPTER 	= "exeRecConEditAdapter";
	// <--- ExeRecCon

	// <--- Exencion

	// ---> ContribExe
	public static final String ACTION_BUSCAR_CONTRIBEXE     	     = "buscarContribExe";
	public static final String ACTION_ADMINISTRAR_CONTRIBEXE 	     = "administrarContribExe";
	public static final String FWD_CONTRIBEXE_SEARCHPAGE 		     = "contribExeSearchPage";
	public static final String FWD_CONTRIBEXE_VIEW_ADAPTER 		     = "contribExeViewAdapter";
	public static final String FWD_CONTRIBEXE_EDIT_ADAPTER 		     = "contribExeEditAdapter";
	public static final String METOD_CONTRIBEXE_PARAM_CONTRIBUYENTE  = "paramContribuyente";
	public static final String PATH_ADMINISTRAR_CONTRIBEXE           = "/exe/AdministrarContribExe";
	public static final String PATH_BUSCAR_CONTRIBEXE    	         = "/exe/BuscarContribExe";
	public static final String METOD_CONTRIBEXE_PARAM_ASIGNAR_BROCHE = "paramAsignarBroche";
	public static final String VOLVER_BUSCAR_CONTRIBEXE              = "volverBuscarContribExe";
	// <--- ContribExe

	// ---> CueExe
	public static final String ACTION_BUSCAR_CUEEXE     	= "buscarCueExe";
	public static final String ACTION_ADMINISTRAR_ENC_CUEEXE = "administrarEncCueExe";
	public static final String ACTION_ADMINISTRAR_CUEEXE 	= "administrarCueExe";
	public static final String ACTION_ADMINISTRAR_CUEEXE_CONVIV	= "/exe/AdministrarCueExeConviv";
	public static final String FWD_CUEEXE_SEARCHPAGE 		= "cueExeSearchPage";
	public static final String FWD_CUEEXE_ADAPTER			= "cueExeAdapter";
	public static final String FWD_CUEEXE_VIEW_ADAPTER			= "cueExeViewAdapter";
	public static final String FWD_CUEEXE_ENC_EDIT_ADAPTER  = "cueExeEncEditAdapter";
	public static final String PATH_ADMINISTRAR_CUEEXE 		= "/exe/AdministrarCueExe";
	public static final String PATH_BUSCARCUEEXE 		= "/exe/BuscarCueExe";
	public static final String METOD_CUEEXE_PARAM_SOLICITANTE = "paramSolicitante";
	
	public static final String ACTION_ADMINISTRAR_CAMBIOESTADO_CUEEXE = "administrarCambioEstadoCueExe";
	public static final String FWD_CAMBIOESTADO_CUEEXE_ADAPTER = "cambioEstadoCueExeAdapter";
	public static final String FWD_AGREGARSOLICITUD_CUEEXE_ADAPTER = "agregarSolicitudCueExeAdapter";
	
	public static final String ACTION_SELECCIONAR_DEUDA 	      = "seleccionarDeuda";	
	public static final String FWD_SELECCIONDEUDA_EDIT_ADAPTER    = "seleccionDeudaEditAdapter";
	
	public static final String ACT_QUITAR_BROCHE_INIT		= "quitarBrocheInit";
	
	public static final String ACTION_ADMINISTRAR_CUEEXECONVIV = "administrarCueExeConviv";
	public static final String FWD_CUEEXECONVIV_ADAPTER 	   = "cueExeConvivAdapter";
	
	public static final String ACTION_ADMINISTRAR_HISESTCUEEXE = "administrarHisEstCueExe";
	
	public static final String FWD_HISESTCUEEXE_VIEW_ADAPTER = "hisEstCueExeViewAdapter";
	// <--- CueExe

	// ---> Administrar Marcacion de Deuda
	public static final String FWD_DEDUDA_EXENCION_INGRESO_ADAPTER	 = "deudaExencionIngresoAdapter";
	public static final String FWD_CUENTA_EXENCION_ADAPTER	 = "cuentaExencionAdapter";
	public static final String FWD_DEUDA_EXENCION_ADAPTER	 = "deudaExencionAdapter";
	public static final String FWD_INICIALIZAR_DEDUA_EXENCION = "/exe/AdministrarDeudaExencion.do?method=inicializar";
	public static final String PATH_DEDUDA_EXENCION_ADAPTER = "/exe/AdministrarDeudaExencion";
	// <--- Administrar Marcacion de Deuda
	
	// ---> ABM Envios solcueExe
	public static final String FWD_SOLCUEEXE_ENVIO_SEARCHPAGE = "solCueExeEnvioSearchPage";
	// <--- ABM Envios solcueExe
	
	// ---> Adm envio de cuentas (solCueExe)
	public static final String METOD_PARAM_SOLICITANTE = "paramSolicitante";
	public static final String FWD_SOLCUEEXE_ENVIO_VIEW_ADAPTER = "solCueExeEnvioViewAdapter";
	public static final String ACT_ENVIAR_CATASTRO = "enviarCatastro";
	public static final String ACT_ENVIAR_SINTYS = "enviarSintys";
	public static final String ACT_ENVIAR_DG = "enviarDG";
	public static final String ACTION_ADMINISTRAR_SOLCUEEXE_ENVIO = "administrarSolCueExeEnvio";
	// <--- Adm envio de cuentas (solCueExe)
	
	// ---> ABM Tipo Sujeto
	public static final String FWD_TIPOSUJETO_SEARCHPAGE = "tipoSujetoSearchPage";
	public static final String ACTION_ADMINISTRAR_TIPOSUJETO = "administrarTipoSujeto";
	public static final String FWD_TIPOSUJETO_VIEW_ADAPTER = "tipoSujetoViewAdapter";
	public static final String FWD_TIPOSUJETO_ADAPTER = "tipoSujetoAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_TIPOSUJETO = "administrarEncTipoSujeto";
	public static final String FWD_TIPOSUJETO_ENC_EDIT_ADAPTER = "tipoSujetoEncEditAdapter";
	public static final String PATH_ADMINISTRAR_TIPOSUJETO = "/exe/AdministrarTipoSujeto";
	// <--- ABM Tipo Sujeto
	
	// ---> ABM TipSujExe
	public static final String ACTION_ADMINISTRAR_TIPSUJEXE = "administrarTipSujExe";
	public static final String FWD_TIPSUJEXE_VIEW_ADAPTER = "tipSujExeViewAdapter";
	public static final String FWD_TIPSUJEXE_EDIT_ADAPTER = "tipSujExeEditAdapter";
	// ---> ABM TipSujExe
	
	// ---> ABM SolCueExeConviv
	public static final String FWD_CUEEXECONVIV_EDITADAPTER = "cueExeConvivEditAdapter";
	// <--- ABM SolCueExeConviv

	// ---> ADM Marcas de Exenciones
	public static final String FWD_MARCA_CUEEXE_SEARCHPAGE = "marcaCueExeSearchPage";
	// <---> ADM Marcas de Exenciones
	
	// ---> ABM Estado Cuenta/Exencion
	public static final String FWD_ESTADOCUEEXE_SEARCHPAGE = "estadoCueExeSearchPage";
	public static final String ACTION_ADMINISTRAR_ESTADOCUEEXE = "administrarEstadoCueExe";
	public static final String FWD_ESTADOCUEEXE_VIEW_ADAPTER = "estadoCueExeViewAdapter";
	public static final String FWD_ESTADOCUEEXE_EDIT_ADAPTER = "estadoCueExeEditAdapter";
	public static final String FWD_ESTADOCUEEXE_ADAPTER = "tipoSujetoAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_ESTADOCUEEXE = "administrarEncestadoCueExe";
	public static final String FWD_ESTADOCUEEXE_ENC_EDIT_ADAPTER = "estadoCueExeEncEditAdapter";
	public static final String PATH_ADMINISTRAR_ESTADOCUEEXE = "/exe/AdministrarEstadoCueExe";
	// <--- ABM Estado Cuenta/Exencion
}
