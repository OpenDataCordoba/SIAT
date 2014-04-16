//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.view.util;

public class BaseConstants {

	// TODO: analizar si conviene subier estas constantes a demoda
	public static final String FWD_SESSION_ERROR = "sessionError";
	public static final String FWD_SESSION_ANONIMO_ERROR = "sessionAnonimoError";
	public static final String FWD_ERROR_NAVEGACION = "siatErrorNavegacion";
	public static final String FWD_ERROR_SERVER = "siatErrorServer";
	public static final String FWD_VIEW_IMPRIMIR = ".base.view.imprimir";
	

	public static final String FWD_SIATINDEX = "siatIndex";
	public static final String SESSION_ERROR_DESCRIPTION = "siatIndex";	
	public static final String SUCCESS_MESSAGE_DESCRIPTION = "La operaci\u00F3n ha sido realizada con \u00E9xito";	
	public static final String EXCEPTION_MESSAGE_DESCRIPTION = "La aplicaci\u00F3n no pudo concretar la transacci\u00F3n";
	
	/*El action Base utiliza esta constante para decidir si activa el modo ABM o seleccion */
	public static final String ACT_SIAT_MENU         = "/seg/SiatMenu"; 
	public static final String FWD_SIAT_BUILD_MENU   = "/seg/SiatMenu.do?method=build"; 
	
	//	ACTs en General --------------------------------------------------------
	public static final String ACT_INICIALIZAR = "inicializar";
	public static final String ACT_BUSCAR      = "buscar";
	public static final String ACT_VER         = "ver";
	public static final String ACT_AGREGAR     = "agregar";
	public static final String ACT_MODIFICAR   = "modificar";
	public static final String ACT_ELIMINAR    = "eliminar";
	public static final String ACT_ACTIVAR     = "activar";
	public static final String ACT_DESACTIVAR  = "desactivar";
	public static final String ACT_VOLVER      = "volver";
	public static final String ACT_PARAM       = "param";
	public static final String ACT_SELECCIONAR = "seleccionar";
	public static final String ACT_REFILL      = "refill";
	public static final String ACT_BUSCAR_AVANZADO  = "buscarAva";
	public static final String ACT_IMPRIMIR    = "imprimir";
	public static final String ACT_BAJA        = "baja";
	public static final String ACT_ALTA        = "alta";
	public static final String ACT_ANULAR      = "anular";
	public static final String ACT_ACEPTAR     = "aceptar";
	public static final String ACT_ENVIAR      = "enviar";
	public static final String ACT_DEVOLVER    = "devolver";
	public static final String ACT_ASIGNAR	   = "asignar";
	public static final String ACT_VUELTA_ATRAS = "vueltaAtras";
	public static final String ACT_RELACIONAR = "relacionar";
	public static final String ACT_APLICAR = "aplicar";
	public static final String ACT_SIMULAR = "simular";
	public static final String ACT_CONCILIAR = "conciliar";
    //	Fin ACTs en General ----------------------------------------------------
	
	//	Metodos en General -----------------------------------------------------
	public static final String METHOD_INICIALIZAR = "inicializar";
    //	Fin ACTs en General ----------------------------------------------------	
	
	public static final String FWD_MSG         = "siatMessage";
	public static final String FWD_ERROR_PRINT = "siatErrorPrint";
	public static final String FWD_SIAT_OFFLINE  = "siatOffLine";
	
	public static final String SELECTED_ID ="selectedId";
}
