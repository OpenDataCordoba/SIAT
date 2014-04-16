//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.view.util;



public interface DefConstants {
    
	// ---> Atributo
	public static final String ACTION_BUSCAR_ATRIBUTO     	= "buscarAtributo";
	public static final String ACTION_ADMINISTRAR_ATRIBUTO 	= "administrarAtributo";
	public static final String FWD_ATRIBUTO_SEARCHPAGE 	    = "atributoSearchPage";
	public static final String FWD_ATRIBUTO_VIEW_ADAPTER 	= "atributoViewAdapter";
	public static final String FWD_ATRIBUTO_EDIT_ADAPTER 	= "atributoEditAdapter";
	// <--- Atributo
	
	//	 ---> Feriado
	public static final String ACTION_BUSCAR_FERIADO     	= "buscarFeriado";
	public static final String ACTION_ADMINISTRAR_FERIADO 	= "administrarFeriado";
	public static final String FWD_FERIADO_SEARCHPAGE 	    = "feriadoSearchPage";
	public static final String FWD_FERIADO_VIEW_ADAPTER 	= "feriadoViewAdapter";
	public static final String FWD_FERIADO_EDIT_ADAPTER 	= "feriadoEditAdapter";
	// <--- Feriado
	
	// ---> Vencimiento
	public static final String ACTION_BUSCAR_VENCIMIENTO     	= "buscarVencimiento";
	public static final String ACTION_ADMINISTRAR_VENCIMIENTO 	= "administrarVencimiento";
	public static final String FWD_VENCIMIENTO_SEARCHPAGE 	    = "vencimientoSearchPage";
	public static final String FWD_VENCIMIENTO_VIEW_ADAPTER 	= "vencimientoViewAdapter";
	public static final String FWD_VENCIMIENTO_EDIT_ADAPTER 	= "vencimientoEditAdapter";
	// <--- Vencimiento
	
	// ---> Categoria
	public static final String ACTION_BUSCAR_CATEGORIA     	   = "buscarCategoria";
	public static final String ACTION_ADMINISTRAR_CATEGORIA    = "administrarCategoria";
	public static final String FWD_CATEGORIA_SEARCHPAGE 	   = "categoriaSearchPage";
	public static final String FWD_CATEGORIA_VIEW_ADAPTER 	   = "categoriaViewAdapter";
	public static final String FWD_CATEGORIA_EDIT_ADAPTER 	   = "categoriaEditAdapter";
	// <--- Categoria
	
	// ---> DomAtr
	// Dominio Atributo
	public static final String ACTION_BUSCAR_DOMATR     	= "buscarDomAtr";
	public static final String FWD_DOMATR_SEARCHPAGE 	    = "domAtrSearchPage";
	public static final String ACTION_ADMINISTRAR_DOMATR 	= "administrarDomAtr";
	public static final String FWD_DOMATR_VIEW_ADAPTER 		= "domAtrViewAdapter";
	public static final String FWD_DOMATR_ADAPTER 		    = "domAtrAdapter";
	public static final String ACTION_ADMINISTRAR_ENCDOMATR = "administrarEncDomAtr";	
	public static final String FWD_DOMATR_ENC_EDIT_ADAPTER 	= "domAtrEncEditAdapter";	
	public static final String PATH_ADMINISTRAR_DOMATR     	= "/def/AdministrarDomAtr";

	// Dominio Atributo Valor
	public static final String ACTION_ADMINISTRAR_DOMATRVAL  = "administrarDomAtrVal";
	public static final String FWD_DOMATRVAL_VIEW_ADAPTER 	 = "domAtrValViewAdapter";
	public static final String FWD_DOMATRVAL_EDIT_ADAPTER 	 = "domAtrValEditAdapter";
	// <--- DomAtr
		
	
	// ---> Servicio Banco
	// Servicio Banco
	public static final String ACTION_BUSCAR_SERVICIOBANCO     	= "buscarServicioBanco";
	public static final String FWD_SERVICIOBANCO_SEARCHPAGE 	    = "servicioBancoSearchPage";
	public static final String ACTION_ADMINISTRAR_SERVICIOBANCO 	= "administrarServicioBanco";
	public static final String FWD_SERVICIOBANCO_VIEW_ADAPTER 		= "servicioBancoViewAdapter";
	public static final String FWD_SERVICIOBANCO_ADAPTER 		    = "servicioBancoAdapter";
	public static final String ACTION_ADMINISTRAR_ENCSERVICIOBANCO = "administrarEncServicioBanco";	
	public static final String FWD_SERVICIOBANCO_ENC_EDIT_ADAPTER 	= "servicioBancoEncEditAdapter";	
	public static final String PATH_ADMINISTRAR_SERVICIOBANCO     	= "/def/AdministrarServicioBanco";

	// Servicio Banco Recurso
	public static final String ACTION_ADMINISTRAR_SERBANREC  = "administrarSerBanRec";
	public static final String FWD_SERBANREC_VIEW_ADAPTER 	 = "serBanRecViewAdapter";
	public static final String FWD_SERBANREC_EDIT_ADAPTER 	 = "serBanRecEditAdapter";

    // Servicio Banco Descuentos Generales
	public static final String ACTION_ADMINISTRAR_SERBANDESGEN  = "administrarSerBanDesGen";
	public static final String FWD_SERBANDESGEN_VIEW_ADAPTER 	 = "serBanDesGenViewAdapter";
	public static final String FWD_SERBANDESGEN_EDIT_ADAPTER 	 = "serBanDesGenEditAdapter";
	// <--- Servicio Banco

	
	// ---> TipObjImp (Encabezado)
	public static final String ACTION_BUSCAR_TIPOBJIMP     		= "buscarTipObjImp";
	public static final String FWD_TIPOBJIMP_SEARCHPAGE 	    = "tipObjImpSearchPage";
	
	public static final String ACTION_ADMINISTRAR_TIPOBJIMP 	= "administrarTipObjImp";
	
	public static final String FWD_TIPOBJIMP_VIEW_ADAPTER 		= "tipObjImpViewAdapter";
	public static final String FWD_TIPOBJIMP_ENC_EDIT_ADAPTER 	= "tipObjImpEncEditAdapter";
	public static final String FWD_TIPOBJIMP_ADAPTER 		    = "tipObjImpAdapter";
	public static final String ACTION_ADMINISTRAR_ENCTIPOBJIMP 	= "administrarEncTipObjImp";	
	public static final String PATH_ADMINISTRAR_TIPOBJIMP     	= "/def/AdministrarTipObjImp"; // utilizado para redirigir en el agregar TipObjImp
	// <--- TipObjImp

	// ---> TipObjImpAreO
	public static final String ACTION_ADMINISTRAR_TIPOBJIMPAREO = "administrarTipObjImpAreO";
	public static final String FWD_TIPOBJIMPAREO_ADAPTER 	    = "tipObjImpAreOAdapter";
	public static final String FWD_TIPOBJIMPAREO_VIEW_ADAPTER 	= "tipObjImpAreOViewAdapter";
	public static final String FWD_TIPOBJIMPAREO_EDIT_ADAPTER 	= "tipObjImpAreOEditAdapter";
	// <--- TipObjImpAreO
	
	// ---> TipObjImpAtr (Detalle)
	public static final String ACTION_ADMINISTRAR_TIPOBJIMPATR 	= "administrarTipObjImpAtr";
	public static final String FWD_TIPOBJIMPATR_ADAPTER 		= "tipObjImpAtrAdapter";
	public static final String FWD_TIPOBJIMPATR_VIEW_ADAPTER 	= "tipObjImpAtrViewAdapter";
	public static final String FWD_TIPOBJIMPATR_EDIT_ADAPTER 	= "tipObjImpAtrEditAdapter";
	// <--- TipObjImpAtr
	
	// ---> ConAtr
	public static final String ACTION_BUSCAR_CONATR     	= "buscarConAtr";
	public static final String ACTION_ADMINISTRAR_CONATR    = "administrarConAtr";
	public static final String FWD_CONATR_SEARCHPAGE 	    = "conAtrSearchPage";
	public static final String FWD_CONATR_VIEW_ADAPTER 	    = "conAtrViewAdapter";
	public static final String FWD_CONATR_EDIT_ADAPTER 	    = "conAtrEditAdapter";
	public static final String METOD_CONATR_PARAM_ATRIBUTO 	= "paramAtributo";	
	// <--- ConAtr

	// ---> Recurso
	// Recurso
	public static final String ACTION_BUSCAR_RECURSO     	= "buscarRecurso";
	public static final String FWD_RECURSO_SEARCHPAGE 	    = "recursoSearchPage";
	public static final String ACTION_ADMINISTRAR_RECURSO 	= "administrarRecurso";
	public static final String FWD_RECURSO_VIEW_ADAPTER 		= "recursoViewAdapter";
	public static final String FWD_RECURSONOTRIB_VIEW_ADAPTER	= "recursoNoTribViewAdapter";
	public static final String FWD_RECURSO_ADAPTER 		    = "recursoAdapter";
	public static final String ACTION_ADMINISTRAR_ENCRECURSO = "administrarEncRecurso";	
	public static final String FWD_RECURSO_ENC_EDIT_ADAPTER 	= "recursoEncEditAdapter";	
	public static final String FWD_RECURSO_NOTRIB_ENC_EDITADAPTER	="recursoNoTribEncAdapter";
	public static final String FWD_RECURSO_NOTRIB_ADAPTER		= "recursoNoTribAdapter";
	public static final String PATH_ADMINISTRAR_RECURSO     	= "/def/AdministrarRecurso";
	
	// Recursos Autoliquidables
	public static final String ACTION_ADMINISTRAR_RECURSO_AUTOLIQUIDABLE 	= "administrarRecursoAutoLiquidable";
	public static final String FWD_RECURSO_AUTOLIQUIDABLE_ADAPTER 		    = "recursoAutoLiquidableAdapter";
	

	// RecAtrVal
	public static final String ACTION_ADMINISTRAR_RECATRVAL  = "administrarRecAtrVal";
	public static final String FWD_RECATRVAL_VIEW_ADAPTER 	 = "recAtrValViewAdapter";
	public static final String FWD_RECATRVAL_EDIT_ADAPTER 	 = "recAtrValEditAdapter";
	public static final String METOD_RECATRVAL_PARAM_ATRIBUTO 	= "paramAtributo";
	
	// RecCon
	public static final String ACTION_ADMINISTRAR_RECCON  = "administrarRecCon";
	public static final String FWD_RECCON_VIEW_ADAPTER 	 = "recConViewAdapter";
	public static final String FWD_RECCON_EDIT_ADAPTER 	 = "recConEditAdapter";

	// RecClaDeu
	public static final String ACTION_ADMINISTRAR_RECCLADEU  = "administrarRecClaDeu";
	public static final String FWD_RECCLADEU_VIEW_ADAPTER 	 = "recClaDeuViewAdapter";
	public static final String FWD_RECCLADEU_EDIT_ADAPTER 	 = "recClaDeuEditAdapter";

	// RecGenCueAtrVa
	public static final String ACTION_ADMINISTRAR_RECGENCUEATRVA  = "administrarRecGenCueAtrVa";
	public static final String FWD_RECGENCUEATRVA_VIEW_ADAPTER 	 = "recGenCueAtrVaViewAdapter";
	public static final String FWD_RECGENCUEATRVA_EDIT_ADAPTER 	 = "recGenCueAtrVaEditAdapter";
	
	// RecConADec
	public static final String ACTION_ADMINISTRAR_RECCONADEC	= "administrarRecConADec";
	public static final String FWD_RECCONADEC_VIEW_ADAPTER		= "recConADecViewAdapter";
	public static final String FWD_RECCONADEC_EDIT_ADAPTER		= "recConADecEditAdapter";
	public static final String ACTION_ADMINISTRAR_VALUNRECCONADEC="administrarValUnRecConADe";
	
	// ValUnRecConADe
	public static final String FWD_VALUNRECCONADE_VIEW_ADAPTER		= "valUnRecConADeViewAdapter";
	public static final String FWD_VALUNRECCONADE_EDIT_ADAPTER		= "valUnRecConADeEditAdapter";
	
		
	// RecEmi
	public static final String ACTION_ADMINISTRAR_RECEMI  = "administrarRecEmi";
	public static final String FWD_RECEMI_VIEW_ADAPTER 	 = "recEmiViewAdapter";
	public static final String FWD_RECEMI_EDIT_ADAPTER 	 = "recEmiEditAdapter";

	// RecAtrCueEmi
	public static final String ACTION_ADMINISTRAR_RECATRCUEEMI  = "administrarRecAtrCueEmi";
	public static final String FWD_RECATRCUEEMI_VIEW_ADAPTER 	 = "recAtrCueEmiViewAdapter";
	public static final String FWD_RECATRCUEEMI_EDIT_ADAPTER 	 = "recAtrCueEmiEditAdapter";
	public static final String METOD_RECATRCUEEMI_PARAM_ATRIBUTO 	= "paramAtributo";
	
	// RecAtrCue
	public static final String ACTION_ADMINISTRAR_RECATRCUE  = "administrarRecAtrCue";
	public static final String FWD_RECATRCUE_VIEW_ADAPTER 	 = "recAtrCueViewAdapter";
	public static final String FWD_RECATRCUE_EDIT_ADAPTER 	 = "recAtrCueEditAdapter";
	public static final String METOD_RECATRCUE_PARAM_ATRIBUTO 	= "paramAtributo";

	// RecMinDec
	public static final String FWD_RECMINDEC_VIEW_ADAPTER	 = "recMinDecViewAdapter";
	public static final String FWD_RECMINDEC_EDIT_ADAPTER	 = "recMinDecEditAdapter";
	public static final String ACTION_ADMINISTRAR_RECMINDEC	 = "administrarRecMinDec";
	
	// RecAli
	public static final String ACTION_ADMINISTRAR_RECALI	 = "administrarRecAli";
	public static final String FWD_RECALI_VIEW_ADAPTER	 	 = "recAliViewAdapter";
	public static final String FWD_RECALI_EDIT_ADAPTER	 	 = "recAliEditAdapter";
	
	
	// <--- Recurso
	
	// ---> Area
	public static final String ACTION_BUSCAR_AREA     	= "buscarArea";
	public static final String ACTION_ADMINISTRAR_AREA 	= "administrarArea";
	public static final String FWD_AREA_SEARCHPAGE 	    = "areaSearchPage";
	public static final String FWD_AREA_VIEW_ADAPTER 	= "areaViewAdapter";
	public static final String FWD_AREA_EDIT_ADAPTER 	= "areaEditAdapter";
	public static final String ACT_ADM_RECURSOAREA_ADAPTER 	= "admRecursoArea";
	public static final String PATH_ADMINISTRAR_AREA    = "/def/AdministrarArea";
	
	// RecursoArea
	public static final String ACTION_ADMINISTRAR_RECURSOAREA  = "administrarRecursoArea";
	public static final String FWD_RECURSOAREA_ADAPTER			 = "recursoAreaAdapter";
	public static final String FWD_RECURSOAREA_VIEW_ADAPTER 	 = "recursoAreaViewAdapter";
	public static final String FWD_RECURSOAREA_EDIT_ADAPTER 	 = "recursoAreaEditAdapter";
	// <--- Area
	
	// ---> Parametro
	public static final String ACTION_BUSCAR_PARAMETRO  	= "buscarParametro";
	public static final String ACTION_ADMINISTRAR_PARAMETRO = "administrarParametro";
	public static final String FWD_PARAMETRO_SEARCHPAGE 	= "parametroSearchPage";
	public static final String FWD_PARAMETRO_VIEW_ADAPTER 	= "parametroViewAdapter";
	public static final String FWD_PARAMETRO_EDIT_ADAPTER 	= "parametroEditAdapter";
	// <--- Parametro
	
	// ---> Calendario 
	public static final String ACTION_ADMINISTRAR_CALENDARIO = "administrarCalendario";
	public static final String FWD_CALENDARIO_SEARCHPAGE 	 = "calendarioSearchPage";
	public static final String FWD_CALENDARIO_VIEW_ADAPTER 	 = "calendarioViewAdapter";
	public static final String FWD_CALENDARIO_EDIT_ADAPTER 	 = "calendarioEditAdapter";
	// <--- Calendario

	// ---> EmiMat
	public static final String ACTION_BUSCAR_EMIMAT  		 = "buscarEmiMat";
	public static final String ACTION_ADMINISTRAR_EMIMAT     = "administrarEmiMat";
	public static final String ACTION_ADMINISTRAR_ENC_EMIMAT = "administrarEncEmiMat";
	public static final String FWD_EMIMAT_SEARCHPAGE 		 = "emiMatSearchPage";
	public static final String FWD_EMIMAT_ADAPTER	 	 	 = "emiMatAdapter";
	public static final String FWD_EMIMAT_VIEW_ADAPTER	 	 = "emiMatViewAdapter";
	public static final String FWD_EMIMAT_ENC_EDIT_ADAPTER	 = "emiMatEncEditAdapter";
	public static final String PATH_ADMINISTRAR_EMIMAT		 = "/def/AdministrarEmiMat";
	// <--- EmiMat

	// ---> ColEmiMat
	public static final String ACTION_ADMINISTRAR_COLEMIMAT  = "administrarColEmiMat";
	public static final String FWD_COLEMIMAT_VIEW_ADAPTER	 = "colEmiMatViewAdapter";
	public static final String FWD_COLEMIMAT_EDIT_ADAPTER	 = "colEmiMatEditAdapter";
	// <--- ColEmiMat
	
	// ---> CodEmi
	public static final String ACTION_BUSCAR_CODEMI 	   	 = "buscarCodEmi";
	public static final String ACTION_ADMINISTRAR_CODEMI   	 = "administrarCodEmi";
	public static final String ACTION_ADMINISTRAR_ENC_CODEMI = "administrarEncCodEmi";
	public static final String PATH_ADMINISTRAR_CODEMI 	   	 = "/def/AdministrarCodEmi";
	public static final String FWD_CODEMI_SEARCHPAGE	   	 = "codEmiSearchPage";
	public static final String FWD_CODEMI_ENC_EDIT_ADAPTER   = "codEmiEncEditAdapter";
	public static final String FWD_CODEMI_ADAPTER	 	   	 = "codEmiAdapter";
	public static final String FWD_CODEMI_VIEW_ADAPTER	   	 = "codEmiViewAdapter";
	// <--- CodEmi
	
	// ---> Banco
	public static final String ACTION_BUSCAR_BANCO     	= "buscarBanco";
	public static final String ACTION_ADMINISTRAR_BANCO 	= "administrarBanco";
	public static final String FWD_BANCO_SEARCHPAGE 	    = "bancoSearchPage";
	public static final String FWD_BANCO_VIEW_ADAPTER 	= "bancoViewAdapter";
	public static final String FWD_BANCO_EDIT_ADAPTER 	= "bancoEditAdapter";
	// <--- Banco
	
	// ---> SiatScript
	public static final String FWD_SIATSCRIPT_SEARCHPAGE 		= "siatScriptSearchPage";
	public static final String FWD_SIATSCRIPT_ENC_EDIT_ADAPTER = "siatScriptEncEditAdapter";
	public static final String ACTION_ADMINISTRAR_SIATSCRIPT 	= "administrarSiatScript";
	public static final String ACTION_ADMINISTRAR_ENC_SIATSCRIPT = "administrarEncSiatScript";	
	public static final String PATH_ADMINISTRAR_SIATSCRIPT = "/def/AdministrarSiatScript";
	public static final String FWD_SIATSCRIPT_VIEW_ADAPTER = "siatScriptViewAdapter";
	public static final String FWD_SIATSCRIPT_EDIT_ADAPTER = "siatScriptEditAdapter";	
	// <--- SiatScript
	
	// ---> SiatScriptUsr
	public static final String ACTION_ADMINISTRAR_SIATSCRIPTUSR = "administrarSiatScriptUsr";
	public static final String FWD_SIATSCRIPTUSR_VIEW_ADAPTER = "siatScriptUsrViewAdapter";
	public static final String FWD_SIATSCRIPTUSR_EDIT_ADAPTER = "siatScriptUsrEditAdapter";	
	// <--- SiatScriptUsr
}