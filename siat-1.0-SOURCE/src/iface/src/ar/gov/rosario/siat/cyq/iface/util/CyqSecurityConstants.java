//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.util;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class CyqSecurityConstants {
	
	// ---> Administrar Dedua CyQ (SINC)
	public static final String ADM_DEUDA_CYQ  = "ADM_DeudaCyQ";
	// <--- Administrar Dedua CyQ (SINC)
	
	// ---> Mantenedor de Procedimiento CyQ
	public static final String ABM_PROCEDIMIENTO_CyQ = "ABM_ProcedimientoCyQ";
	public static final String ABM_PROCEDIMIENTO_CyQ_AREAS = "ABM_ProcedimientoCyQAreas";
	public static final String ABM_PROCEDIMIENTO_ENC_CyQ = "ABM_ProcedimientoCyQEnc";
	
	public static final String MTD_LIQUIDACION_DEUDA = "liquidacionDeuda";
	public static final String MTD_ESTADO_CUENTA     = "estadoCuenta";
	
	public static final String MTD_CAMBIAR_ESTADO = "cambiarEstado";

	public static final String MTD_AGREGAR_DEUDA_ADMIN  = "agregarDeudaAdmin";
	public static final String MTD_QUITAR_DEUDA_ADMIN  = "quitarDeudaAdmin";
	public static final String MTD_AGREGAR_DEUDA_JUDICIAL  = "agregarDeudaJudicial";
	public static final String MTD_QUITAR_DEUDA_JUDICIAL  = "quitarDeudaJudicial";
	public static final String INFORMAR  = "informe";
	public static final String BAJA  = "baja";
	public static final String CONVERSION  = "conversion";

	public static final String ABM_PROCUENODEU = "ABM_ProCueNoDeu";
	
	public static final String LIQ_DEUDACYQ = "LiqDeudaCyq";

	public static final String MTD_FORMALIZAR_CONVENIO = "formalizarConvenio"; // Formalizacion de Convenio de Pago
	
	public static final String ABM_DEUDAPRIVILEGIO = "ABM_DeudaPrivilegio";
	
	public static final String ABM_PAGOPRIV = "ABM_PagoPriv";
	public static final String MTD_GENERAR_OIT = "generarOIT";
	// <--- Mantenedor de Procedimiento CyQ
	
	// ---> Mantenedor de Abogado
	public static final String ABM_ABOGADO = "ABM_Abogado";
	// <--- Mantenedor de Abogado
	
	// ---> Mantenedor de Juzgado
	public static final String ABM_JUZGADO= "ABM_Juzgado";
	// <--- Mantenedor de Juzgado
	
	public static final String ABM_MOTIVOBAJA = "ABM_MotivoBaja";
	
	public static final String ABM_MOTIVORESINF = "ABM_MotivoResInf";
	
}