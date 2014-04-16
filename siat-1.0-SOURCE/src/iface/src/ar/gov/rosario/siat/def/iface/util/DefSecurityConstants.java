//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.util;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class DefSecurityConstants {
	
	// ---> ABM Atributo	
	public static final String ABM_ATRIBUTO     = "ABM_Atributo";
	// <--- ABM Atributo	
	
	// ---> ABM Categoria	
	public static final String ABM_CATEGORIA  = "ABM_Categoria";
	// <--- ABM Categoria
	
	// ---> ABM Dominio Atributo
	// Dominio Atributo
	public static final String ABM_DOMINIO_ATRIBUTO = "ABM_DominioAtributo";
	public static final String ABM_DOMINIO_ATRIBUTO_ENC = "ABM_DominioAtributoEnc";

	// Dominio Atributo Valor
	public static final String ABM_DOMINIO_ATRIBUTO_VALOR = "ABM_DominioAtributoValor";
	// <--- ABM Dominio Atributo

	// ---> ABM ConAtr	
	public static final String ABM_CONTRIBUYENTE_ATRIBUTO    = "ABM_ContribuyenteAtributo";
	// <--- ABM ConAtr
	
	// <-- ABM Tipo Objeto Imponible
	public static final String ABM_TIPO_OBJETO_IMPONIBLE     = "ABM_TipoObjetoImponible";
	public static final String ABM_TIPO_OBJETO_IMPONIBLE_ENC = "ABM_TipoObjetoImponibleEnc";
	public static final String ABM_TIPO_OBJETO_IMPONIBLE_ADM_AREA_ORIGEN = "admAreaOrigen";
														  /*"agregar"
															"eliminar"
															"ver"
															"modificar"
															"modificarEncabezado"
															
															"area_adm"
															"area_agregar"
															"area_eliminar"															
															"area_ver"
															"area_activar"	
															"area_desactivar"															
															"atributo_agregar"
															"atributo_ver"
															"atributo_eliminar"
															"atributo_modificar"*/
	// <-- ABM Tipo Objeto Imponible
		
	public static final String ABM_TIPO_OBJETO_IMPONIBLE_ATRIBUTO = "ABM_TipoObjetoImponibleAtributo";
	
	public static final String ABM_TIPO_OBJETO_IMPONIBLE_ARE_O = "ABM_TipoObjetoImponibleAreaOrigen";
	// ---> ABM Feriado
	public static final String  ABM_FERIADO = "ABM_Feriado";
	
	
	// <--- ABM Feriado
	
    // ---> ABM Vencimiento
	public static final String  ABM_VENCIMIENTO = "ABM_Vencimiento";
	
	
	// <--- ABM Vencimiento
	
	//	 ---> ABM Servicio Banco	
	public static final String ABM_SERVICIO_BANCO  = "ABM_ServicioBanco";
	public static final String ABM_SERVICIO_BANCO_ENC  = "ABM_ServicioBancoEnc";
	// <--- ABM Servicio Banco
	
	// ---> ABM Servicio Banco Recurso	
	public static final String ABM_SERVICIO_BANCO_RECURSO  = "ABM_SerBanRec";
	// <--- ABM Servicio Banco Recurso
	
	// ---> ABM Servicio Banco Descuentos Generales	
	public static final String ABM_SERVICIO_BANCO_DESCUENTOS_GENERALES  = "ABM_SerBanDesGen";
	// <--- ABM Servicio Banco Descuentos Generales
	
	// ---> ABM Recurso	
	public static final String ABM_RECURSO  = "ABM_Recurso";
	public static final String ABM_RECURSO_NOTRIB="ABM_RecursoNoTrib";
	public static final String ABM_RECURSO_ENC  = "ABM_RecursoEnc";
	// <--- ABM Recurso
	
	// ---> ABM RecAtrVal	
	public static final String ABM_RECATRVAL  = "ABM_RecAtrVal";
	// <--- ABM RecAtrVal

	// ---> ABM RecCon	
	public static final String ABM_RECCON  = "ABM_RecCon";
	// <--- ABM RecCon
	
	// ---> ABM RecClaDeu	
	public static final String ABM_RECCLADEU  = "ABM_RecClaDeu";
	// <--- ABM RecClaDeu

	// ---> ABM RecGenCueAtrVa	
	public static final String ABM_RECGENCUEATRVA  = "ABM_RecGenCueAtrVa";
	// <--- ABM RecGenCueAtrVa

	// ---> ABM RecAtrCue	
	public static final String ABM_RECATRCUE  = "ABM_RecAtrCue";
	// <--- ABM RecAtrCue

	// ---> ABM RecEmi	
	public static final String ABM_RECEMI  = "ABM_RecEmi";
	// <--- ABM RecEmi

	// ---> ABM RecAtrCueEmi	
	public static final String ABM_RECATRCUEEMI  = "ABM_RecAtrCueEmi";
	// <--- ABM RecAtrCueEmi
	
	// ---> ABM Area	
	public static final String ABM_AREA  = "ABM_Area";
	// <--- ABM Area
	
	// ---> ABM Parametro	
	public static final String ABM_PARAMETRO = "ABM_Parametro";
	// <--- ABM Parametro	
	
	//	 ---> ABM Calendario
	public static final String ABM_CALENDARIO = "ABM_Calendario";
	// <--- ABM Calendario	
	
	// ---> ABM Cache
	public static final String ABM_CACHE = "ABM_Cache";
	public static final String MTD_REFRESCAR_CACHE = "refrescarCache";
	// <--- ABM Cache
	
	// ---> ABM EmiEmat (Encabezado)
	public static final String ABM_EMIMAT     = "ABM_EmiMat";
	public static final String ABM_EMIMAT_ENC = "ABM_EmiMatEnc";
	// <--- ABM EmiEmat (Encabezado)
	
	// ---> ABM ColEmiEmat (Detalle)
	public static final String ABM_COLEMIMAT 	 = "ABM_ColEmiMat";
	// <--- ABM ColEmiEmat (Detalle)

	// --> ABM RecConADec
	public static final String ABM_RECCONADEC="ABM_RecConADec";
	
	// --> ABM ValUnRecConADe
	public static final String ABM_VALUNRECCONADE="ABM_ValUnRecConADe";
	
	// --> ABM RecMinDec
	public static final String ABM_RECMINDEC="ABM_RecMinDec";
	
	// --> ABM RecAli
	public static final String ABM_RECALI="ABM_RecAli";

	// ---> ABM CodEmi
	public static final String ABM_CODEMI 	  = "ABM_CodEmi";
	public static final String ABM_CODEMI_ENC = "ABM_CodEmi";
	// <--- ABM CodEmi
 
	// ---> ABM Banco	
	public static final String ABM_BANCO     = "ABM_Banco";
	// <--- ABM Banco
	
	// ---> ABM RecursoArea	
	public static final String ABM_RECURSOAREA = "ABM_RecursoArea";
	// <--- ABM RecursoArea

	// ---> ABM SiatScript	
	public static final String ABM_SIATSCRIPT = "ABM_SiatScript";
	public static final String ABM_SIATSCRIPT_ENC = "ABM_SiatScriptEnc";
	// <--- ABM SiatScript

	// ---> ABM SiatScriptUsr	
	public static String ABM_SIATSCRIPTUSR = "ABM_SiatScriptUsr";
	
	// <--- ABM SiatScriptUsr	
	
}