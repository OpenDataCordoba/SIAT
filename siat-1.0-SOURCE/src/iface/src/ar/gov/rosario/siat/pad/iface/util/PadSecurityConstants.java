//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.util;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso
 * 
 */
public class PadSecurityConstants {
	
	// ---> ABM Contribuyente
	public static final String ABM_CONTRIBUYENTE    = "ABM_Contribuyente";
	public static final String ABM_CONTRIBUYENTE_ENC = "ABM_ContribuyenteEnc";	
	
	// <--- ABM Contribuyente
	
	// ---> ABM Contribuyente
	public static final String ABM_CONTRIBUYENTE_ATRIBUTO_VALOR    = "ABM_ContribuyenteaAtributoValor";
	public static final String ABM_CONTRIBUYENTE_CUENTA_TITULAR    = "ABM_ContribuyenteCuentaTitular";
	// <--- ABM Contribuyente	

	
	
	// ---> ABM Persona
	public static final String ABM_PERSONA    = "ABM_Persona";
	
	// <--- ABM Persona
	
	// ---> Busqueda Calle
	public static final String BUSQUEDA_CALLE = "Busqueda_Calle";
	
	
	// <--- Busqueda Calle
	
	// ---> ABM Domicilio
	public static final String	ABM_DOMICILIO = "ABM_Domicilio";
	
	
	// <--- ABM Domicilio
	
	//	 ---> ABM Localidad
	public static final String	ABM_LOCALIDAD = "ABM_Localidad";
	
	
	// <--- ABM Localidad
	
	
	//	 ---> ABM Objeto Imponible
	public static final String	ABM_OBJIMP = "ABM_ObjetoImponible";
	public static final String  ABM_OBJIMP_ENC = "ABM_ObjetoImponibleEnc";	
	public static final String	ABM_OBJIMPATRVAL = "ABM_ObjetoImponibleAtributoValor";
	
	// <--- ABM Objeto Imponible
	
	//   ---> ABM Cuenta
	public static final String ABM_CUENTA        = "ABM_Cuenta";
	public static final String ABM_CUENTA_ENC    = "ABM_CuentaEnc";
	public static final String ABM_CUENTATITULAR = "ABM_CuentaTitular";
	public static final String ABM_CUENTAREL 	 = "ABM_CuentaRel";
	public static final String MTD_MARCAR_PRINCIPAL = "marcarPrincipal"; // Marcar titular, como titular principal
	
	public static final String MTD_BUSCAR_CMD = "buscarCMD";
	
	public static final String ABM_CUENTA_DOMICILIO_ENV = "ABM_CuentaDomicilioEnvio";
	public static final String ABM_RECATRCUEV        = "ABM_RecAtrCueV";
	
	public static final String ABM_ESTADOCUENTA		="ABM_EstadoCuenta";

	//   <--- ABM Cuenta
	
	
	public static final String ABM_CAMBIAR_DOMICILIO_ENVIO = "ABM_CambiarDomicilioEnvio";
	public static final String MTD_INGRESAR            = "ingresar";  // si puede ingresar le permitimos todas las acciones asociada
	
	//	 ---> ABM Repartidor
	public static final String ABM_REPARTIDOR    = "ABM_Repartidor";
	public static final String ABM_REPARTIDOR_ENC = "ABM_RepartidorEnc";	
	// <--- ABM Repartidor
	
	// ---> ABM CriRepCat	
	public static final String ABM_CRIREPCAT  = "ABM_CriRepCat";
	// <--- ABM CriRepCat
	
	// ---> ABM CriRepCalle	
	public static final String ABM_CRIREPCALLE  = "ABM_CriRepCalle";
	// <--- ABM CriRepCalle

	// ---> ABM Broche
	public static final String ABM_BROCHE    = "ABM_Broche";
	public static final String ABM_BROCHE_ADM_BROCHE_CUENTA = "asignarCuenta";
	// <--- ABM Broche

	// ---> ABM BroCue
	public static final String ABM_BROCUE    = "ABM_BroCue";
	// <--- ABM BroCue
	
	// ---> ABM CueExcSel
	public static final String ABM_CUEEXCSEL    = "ABM_CueExcSel";
	public static final String ABM_CUEEXCSEL_ENC = "ABM_CueExcSelEnc";
	// <--- ABM CueExcSel
	
	// ---> ABM CueExcSelDeu
	public static final String ABM_CUEEXCSELDEU    = "ABM_CueExcSelDeu";
	// <--- ABM CueExcSelDue
	
	// ---> Buzon de Cambios
	public static final String ADM_BUZONCAMBIOS = "ADM_BuzonCambios";
	// <--- Fin Buzon Cambios
	
	// ---> Cierre de Comercio	
	public static final String ABM_CIERRECOMERCIO = "ABM_CierreComercios";
	// <--- Cierre de Comercio

}