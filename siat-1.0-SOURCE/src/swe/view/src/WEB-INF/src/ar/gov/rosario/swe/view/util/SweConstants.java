//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.swe.view.util;


public interface SweConstants {
	public static final String CODAPL = "SWE"; // Codigo de la aplicacion en SWE

   	public static final String FWD_WELCOME = "welcome";
	public static final String FWD_SESSION_ERROR = "sessionError";
	public static final String ACTION_LOGGED_USER = "/Login";
	public static final String SUCCESS_MESSAGE_DESCRIPTION = "La operacion ha sido realizada con exito";	
	public static final String FWD_MSG       = "Message";


	//	ACTs en General --------------------------------------------------------
	public static final String ACT_INICIALIZAR = "inicializar";
	public static final String ACT_BUSCAR      = "buscar";
	public static final String ACT_VER         = "ver";
	public static final String ACT_AGREGAR     = "agregar";
	public static final String ACT_CLONAR      = "clonar";
	public static final String ACT_MODIFICAR   = "modificar";
	public static final String ACT_ELIMINAR    = "eliminar";
	public static final String ACT_BAJA        = "baja";
	public static final String ACT_ALTA        = "alta";
	public static final String ACT_VOLVER      = "volver";
	public static final String ACT_PARAM       = "param";
	public static final String ACT_SELECCIONAR = "seleccionar";

    //	Fin ACTs en General ----------------------------------------------------	
	
   	public static final String FWD_LOGIN_MSG = "LoginMessage";

	public static final String ACT_SELECCIONAR_LOCALIDAD  = "seleccionarLocalidad";
	public static final String ACT_SELECCIONAR_DOMICILIO  = "seleccionarDomicilio";
	public static final String ACT_ADMIN_ROL   = "adminRol";
	public static final String ACT_ASIGNAR_TURNO  		= "asignarTurno";
	public static final String ACT_ASIGNAR_TURNO_DTT	= "asignarTurnoDtt";
	public static final String ACT_REPROGRAMAR_TURNO  	= "reprogramarTurno";
	public static final String ACT_ANULAR_TURNO  		= "anularTurno";
	public static final String ACT_ANULAR_TURNO_DTT		= "anularTurnoDtt";
	public static final String ACT_IMPRIMIR_TURNO  		= "imprimirTurno";
	public static final String ACT_AUSENTAR_TURNO  		= "marcarAusente";
	public static final String ACT_VER_TURNO_DTT  		= "verTurnoDtt";

	public static final String ACT_VER_HISTORICOVACUNACIONES  = "verHistoricoVacunaciones";		
	// Fin ACTs en General ----------------------------------------------------
}
