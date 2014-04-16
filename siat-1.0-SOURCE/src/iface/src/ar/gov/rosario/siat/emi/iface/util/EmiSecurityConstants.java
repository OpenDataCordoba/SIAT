//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.util;

import ar.gov.rosario.siat.def.iface.model.TipoEmisionVO;

/**
 * En esta clase se definen las descripciones de los errores que estas asociados a los VO.
 * 
 * @author Tecso
 * 
 */
public class EmiSecurityConstants {

	public static final String ACT_ADMINISTRAR_PROCESO = "administrarProceso";
	
	// ---> ABM Valorizacion de Matrices de Emision
	public static final String ABM_VALEMIMAT = "ABM_ValEmiMat";
	// <--- ABM Valorizacion de Matrices de Emision

	// ---> ABM Proceso de Resumen de Liquidacion Deuda
	public static final String ABM_RESLIQDEU = "ABM_ResLiqDeu";
	// <--- ABM Proceso de Resumen de Liquidacion Deuda

	// ---> ABM Emision Masiva de Deuda
	public static final String ABM_EMISIONMAS = "ABM_EmisionMas";
	// <--- ABM Emision Masiva de Deuda

	// ---> ABM Emision Puntual de Deuda
	public static final String ABM_EMISIONPUNTUAL = "ABM_EmisionPuntual";
	// <--- ABM Emision Puntual de Deuda

	// ---> ABM Emision Extraordinaria
	public static final String ABM_EMISIONEXT = "ABM_EmisionExt";
	// <--- ABM Emision Extraordinaria

	// ---> ABM Proceso de Generacion de Archivos PAS y Debito
	public static final String ABM_PROPASDEB = "ABM_ProPasDeb";
	// <--- ABM Proceso de Generacion de Archivos PAS y Debito

	// ---> ABM Impresion Masiva de Deuda
	public static final String ABM_IMPMASDEU = "ABM_ImpMasDeu";
	// <--- ABM Impresion Masiva de Deuda


	// A deprecar
	
	// ---> ABM Emision CdM
	public static final String ABM_EMISION_CDM   		 	= "ABM_EmisionCdM";
	public static final String ABM_PROCESO_EMISION_CDM 	    = "ABM_ProcesoEmisionCdm";
	public static final String ABM_PROCESO_EMISION_CDM_ENC  = "ABM_EmisionCdm";
	public static final String MTD_ADMINISTRAR_PROCESO 		= "administrarProceso";	
	// <--- ABM Emision CdM

	// ---> ABM Impresion CdM
	public static final String ABM_IMPRESION_CDM   		 	= "ABM_ImpresionCdM";
	// <--- ABM Impresion CdM

	// ---> ABM Emision Corregida CdM
	public static final String ABM_EMISION_COR_CDM   		= "ABM_EmisionCorCdM";
	// <--- ABM Emision Corregida CdM

	
	
	// ---> Administrar Emision Externa
	public static final String ABM_EMISION_EXTERNA 			= "ABM_EmisionExterna";
	public static final String ADM_EMISION_EXTERNA   		= "ADM_EmisionExterna";
	public static final String MTD_CONTINUAR			    = "continuar";
	// <--- Administrar Emision Externa
	
	/**
	 * Retorna la constante de seguridad correspondiente al tipo
	 * de emision.
 	 * */
	public static String getById(Long idTipoEmision) {
		
		if (idTipoEmision.equals(TipoEmisionVO.ID_EMISIONCDM))
			return ABM_EMISION_CDM;
		
		if (idTipoEmision.equals(TipoEmisionVO.ID_IMPRESIONCDM))
			return ABM_IMPRESION_CDM;
		
		if (idTipoEmision.equals(TipoEmisionVO.ID_EMISIONCORCDM))
			return ABM_EMISION_COR_CDM;
		
		return null;
	}
	
}