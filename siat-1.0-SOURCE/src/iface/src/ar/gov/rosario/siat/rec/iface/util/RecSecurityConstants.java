//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.util;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso
 * 
 */
public class RecSecurityConstants {
	
	// ---> ABM Tipo de Obra  
	public static final String ABM_TIPOOBRA = "ABM_TipoObra";
	// <--- ABM Tipo de Obra

	// ---> ABM de Forma de Pago  
	public static final String ABM_FORMAPAGO = "ABM_FormaPago";
	// <--- ABM de Forma de Pago
	
	// ---> ABM de Contrato  
	public static final String ABM_CONTRATO = "ABM_Contrato";
	// <--- ABM de Contrato
	
	// ---> ABM de Planilla cuadra  
	public static final String ABM_PLANILLACUADRA = "ABM_PlanillaCuadra";
	public static final String ABM_PLANILLACUADRA_ENC = "ABM_PlanillaCuadraEnc";
	public static final String ABM_PLANILLACUADRA_DETALLE = "ABM_PlanillaCuadraDet";
	
	public static final String MTD_PLANILLACUADRA_INFORMAR_CATASTRALES = "informarCatastrales";
	public static final String MTD_PLANILLACUADRA_CAMBIAR_ESTADO       = "cambiarEstado";
	
	// <--- ABM de Planilla cuadra

	//	 ---> ABM Obra  
	public static final String ABM_OBRA                = "ABM_Obra";
	public static final String ABM_OBRA_ENC            = "ABM_ObraEnc";
	
	public static final String MTD_OBRA_EMITIRINFORME  = "emitirInforme";
	public static final String MTD_OBRA_CAMBIAR_ESTADO = "cambiarEstado";
	
	public static final String ABM_OBRA_FORMAPAGO      = "ABM_ObraFormaPago";
	public static final String ABM_OBRA_PLANILLACUADRA = "ABM_ObraPlanillaCuadra";
	
	public static final String MTD_OBRA_PLANILLACUADRA_CAMBIAR_ESTADO = "cambiarEstado";
	public static final String MTD_OBRA_ASIGNAR_REPARTIDOR            = "asignarRepartidor";	
	

	// ---> ABM de ObrRepVen  
	public static final String ABM_OBRREPVEN = "ABM_ObrRepVen";
	// <--- ABM de ObrRepVen
	
	// ---> ABM de UsoCdM  
	public static final String ABM_USOCDM = "ABM_UsoCdM";
	// <--- ABM de UsoCdM

	// ---> ABM de AnulacionObra  
	public static final String ABM_ANULACIONOBRA = "ABM_anulacionObra";
	public static final String MTD_ANULACIONOBRA_ADMINISTRARPROCESO = "administrarProceso";
	// <--- ABM de AnulacionObra
	
	// --> ABM Novedad de Regimen Simplificado
	public static final String ABM_NOVEDADRS = "ABM_NovedadRS";
	public static final String MTD_APLICAR = "aplicar";
	public static final String MTD_APLICARMASIVO = "aplicarMasivo";
	
		// --> ABM Categoria de Regimen Simplificado
	public static final String ABM_CATEGORIARS = "ABM_CatRSDrei";
	
}