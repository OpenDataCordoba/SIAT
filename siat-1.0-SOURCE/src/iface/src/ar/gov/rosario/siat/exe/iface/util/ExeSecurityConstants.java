//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.util;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso
 * 
 */
public class ExeSecurityConstants {
	
	
	// ---> ABM Exencion
	public static final String ABM_EXENCION    = "ABM_Exencion";
	public static final String ABM_EXENCION_ENC    = "ABM_ExencionEnc";
	
	// Conceptos de la exencion
	public static final String ABM_EXERECCON = "ABM_ExeRecCon";
	// <--- ABM Exencion
	
	// ---> ABM Contrib. exento
	public static final String ABM_CONTRIBEXE    = "ABM_ContribExe";
	// <--- ABM Contrib. exento
	
	// ---> Administrar Exenciones
	public static final String ABM_CUEEXE = "ABM_CueExe";
	/** Para usuarios de emision, viene un idExencion preseteado */
	public static final String ABM_CUEEXE_EMI = "ABM_CueExeEmi";
	public static final String ABM_CUEEXE_ENC = "ABM_CueExeEnc";

	public static final String MTD_MODIFICAR_HISESTCUEEXE = "modificarHisEstCueExe";
	public static final String MTD_CAMBIARESTADO = "cambiarEstado";
	public static final String MTD_AGREGARSOLICITUD = "agregarSolicitud";
	
	public static final String ABM_HISESTCUEEXE = "ABM_HisEstCueExe";	
	public static final String ADM_DEUDA_EXENCION = "ADM_DeudaExencion";
	// <--- Administrar Exenciones
	
	// ---> ADM de envios de solicitud de exencion
	public static final String ADM_SOLCUEEXE_ENVIOS = "ADM_SolCueExe_envios";
	public static final String MTD_IMPRIMIR = "imprimir";
	public static final String MTD_ENVIAR_CATASTRO = "enviarCatastro";
	public static final String MTD_ENVIAR_SINTYS = "enviarSintys";
	public static final String MTD_ENVIAR_DG = "enviarDG";	
	// <--- ADM de envios de solicitud de exencion	

	//	 ---> ABM Tipo Sujeto
	public static final String ABM_TIPOSUJETO = "ABM_TipoSujeto";
	public static final String ABM_TIPOSUJETO_ENC = "ABM_TipoSujetoEnc";
	// <--- ABM Tipo Sujeto 
	
	//	 ---> ABM TipSujExe
	public static final String ABM_TIPSUJEXE = "ABM_TipSujExe";
	// <--- ABM TipSujExe 

	// ---> ABM SolCueExeConviv
	public static final String ABM_CUEEXECONVIV = "ABM_CueExeConviv";
	// <--- ABM SolCueExeConviv
	
    //	 ---> ABM Estado Cuenta/Exencion
    public static final String ABM_ESTADOCUEEXE = "ABM_EstadoCueExe";
	public static final String ABM_ESTADOCUEEXE_ENC = "ABM_EstadoCueExeEnc";
	// <--- ABM Estado Cuenta/Exencion
	
}