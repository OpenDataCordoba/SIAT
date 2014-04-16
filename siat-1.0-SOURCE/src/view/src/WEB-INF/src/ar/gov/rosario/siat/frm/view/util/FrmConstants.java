//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.view.util;


public interface FrmConstants {
    
	// ---> Formulario
	public static final String ACTION_BUSCAR_FORMULARIO     	= "buscarFormulario";
	public static final String ACTION_ADMINISTRAR_FORMULARIO 	= "administrarFormulario";
	public static final String FWD_FORMULARIO_SEARCHPAGE 		= "formularioSearchPage";
	public static final String FWD_FORMULARIO_VIEW_ADAPTER 	= "formularioViewAdapter";
	public static final String FWD_FORMULARIO_EDIT_ADAPTER 	= "formularioEditAdapter";	
	// <--- Formulario
 
	//-------------------------------------------
 	// ---> (Para los casos de Encabezado/Detalle)

	// ---> Formulario (Encabezado)
//	public static final String ACTION_BUSCAR_FORMULARIO     	= "buscarFormulario";
//	public static final String FWD_FORMULARIO_SEARCHPAGE 	    = "formularioSearchPage";
	
//	public static final String ACTION_ADMINISTRAR_FORMULARIO 	= "administrarFormulario";
	
//	public static final String FWD_FORMULARIO_VIEW_ADAPTER 		= "formularioViewAdapter";
	public static final String FWD_FORMULARIO_ENC_EDIT_ADAPTER 	= "formularioEncEditAdapter";
	public static final String FWD_FORMULARIO_ADAPTER 		    	= "formularioAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_FORMULARIO 	= "administrarEncFormulario";	
	public static final String PATH_ADMINISTRAR_FORMULARIO     	= "/frm/AdministrarFormulario"; // utilizado para redirigir en el agregar Formulario
	// <--- Formulario
	
	// ---> ForCam (Detalle)
	public static final String ACTION_ADMINISTRAR_FORCAM 	= "administrarForCam";
	public static final String FWD_FORCAM_ADAPTER 			= "forCamAdapter";
	public static final String FWD_FORCAM_VIEW_ADAPTER 	= "forCamViewAdapter";
	public static final String FWD_FORCAM_EDIT_ADAPTER 	= "forCamEditAdapter";
	// <--- ForCam
		
	// <---(Para los casos de Encabezado/Detalle)
	//-------------------------------------------
}
