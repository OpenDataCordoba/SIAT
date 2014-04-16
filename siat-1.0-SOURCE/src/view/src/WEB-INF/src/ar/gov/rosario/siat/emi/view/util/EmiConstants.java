//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.view.util;


public interface EmiConstants {
	
	// ---> Resumen de Liquidacion de Deuda
	public static final String ACTION_BUSCAR_RESLIQDEU     		    = "buscarResLiqDeu";
	public static final String ACTION_ADMINISTRAR_RESLIQDEU  		= "administrarResLiqDeu";
	public static final String ACTION_ADMINISTRAR_PROCESO_RESLIQDEU = "administrarProcesoResLiqDeu";
	public static final String FWD_RESLIQDEU_SEARCHPAGE      		= "resLiqDeuSearchPage";
	public static final String FWD_RESLIQDEU_VIEW_ADAPTER 	 		= "resLiqDeuViewAdapter";
	public static final String FWD_RESLIQDEU_EDIT_ADAPTER 	 		= "resLiqDeuEditAdapter";
	public static final String FWD_PROCESO_RESLIQDEU_ADAPTER	    = "procesoResLiqDeuAdapter";
	public static final String PATH_ADMINISTRAR_PROCESO_RESLIQDEU	= "/emi/AdministrarProcesoResLiqDeu";
	// <--- Resumen de Liquidacion de Deuda

	// ---> Consultar EmiInfCue
	public static final String ACTION_BUSCAR_EMIINFCUE  = "buscarEmiInfCue";
	public static final String FWD_EMIINFCUE_SEARCHPAGE = "emiInfCueSearchPage";
	// <--- Consultar EmiInfCue
	
	// ---> Consultar AuxDeuda
	public static final String ACTION_BUSCAR_AUXDEUDA  	 	= "buscarAuxDeuda";
	public static final String ACTION_ADMINISTRAR_AUXDEUDA  = "administrarAuxDeuda";
	public static final String FWD_AUXDEUDA_SEARCHPAGE 	 	= "auxDeudaSearchPage";
	public static final String FWD_AUXDEUDA_VIEW_ADAPTER 	= "auxDeudaViewAdapter";
	// <--- Consultar AuxDeuda
	
	// ---> Valorizacion de Matrices de Parametros de Emision
	public static final String ACTION_BUSCAR_VALEMIMAT 		= "buscarValEmiMat";
	public static final String ACTION_ADMINISTRAR_VALEMIMAT	= "administrarValEmiMat"; 	
	public static final String FWD_VALEMIMAT_SEARCHPAGE     = "valEmiMatSearchPage";
	public static final String FWD_VALEMIMAT_EDIT_ADAPTER  	= "valEmiMatEditAdapter";
	public static final String FWD_VALEMIMAT_VIEW_ADAPTER  	= "valEmiMatViewAdapter";
	public static final String PATH_ADMINISTRAR_VALEMIMAT 	= "/emi/AdministrarValEmiMat";
	// <--- Valorizacion de Matrices de Parametros de Emision

	// ---> Impresion Masiva de Deuda
	public static final String ACTION_BUSCAR_IMPMASDEU      		   = "buscarImpMasDeu";
	public static final String ACTION_ADMINISTRAR_IMPMASDEU 		   = "administrarImpMasDeu";
	public static final String ACTION_ADMINISTRAR_PROCESO_IMPMASDEU    = "administrarProcesoImpMasDeu";
	public static final String FWD_IMPMASDEU_SEARCHPAGE  		       = "impMasDeuSearchPage";
	public static final String FWD_IMPMASDEU_VIEW_ADAPTER 			   = "impMasDeuViewAdapter";
	public static final String FWD_IMPMASDEU_EDIT_ADAPTER 			   = "impMasDeuEditAdapter";
	public static final String FWD_PROCESO_IMPMASDEU_ADAPTER	   	   = "procesoImpMasDeuAdapter";
	public static final String FWD_PROCESO_IMPMASDEU_REINICIAR_ADAPTER = "procesoImpMasDeuReiniciarAdapter";
	public static final String ACT_ADM_PROCESO_IMPMASDEU     		   = "administrarProceso";
	public static final String PATH_ADMINISTRAR_PROCESO_IMPMASDEU	   = "/emi/AdministrarProcesoImpMasDeu";
	// <--- Impresion Masiva																						

	// ---> Emision Extraordinaria
	public static final String ACTION_BUSCAR_EMISIONEXT      = "buscarEmisionExt";
	public static final String ACTION_ADMINISTRAR_EMISIONEXT = "administrarEmisionExt";
	public static final String FWD_EMISIONEXT_SEARCHPAGE 	 = "emisionExtSearchPage";
	public static final String FWD_EMISIONEXT_VIEW_ADAPTER 	 = "emisionExtViewAdapter";
	public static final String FWD_EMISIONEXT_EDIT_ADAPTER   = "emisionExtEditAdapter";	
	// <--- Emision Extraordinaria

	// ---> Emision Masiva de Deuda
	public static final String ACTION_BUSCAR_EMISIONMAS      	     = "buscarEmisionDeu";
	public static final String ACTION_ADMINISTRAR_EMISIONMAS 	     = "administrarEmisionMas";
	public static final String ACTION_ADMINISTRAR_PROCESO_EMISIONMAS = "administrarProcesoEmisionMas";
	public static final String FWD_EMISIONMAS_SEARCHPAGE 	 	     = "emisionMasSearchPage";
	public static final String FWD_EMISIONMAS_VIEW_ADAPTER 	 	  	 = "emisionMasViewAdapter";
	public static final String FWD_EMISIONMAS_EDIT_ADAPTER  	   	 = "emisionMasEditAdapter";
	public static final String FWD_PROCESO_EMISIONMAS_ADAPTER	     = "procesoEmisionMasAdapter";
	public static final String PATH_ADMINISTRAR_PROCESO_EMISIONMAS 	 = "/emi/AdministrarProcesoEmisionMas";
	// <--- Emision Masiva de Deuda

	// ---> Generacion de Archivos PAS y DEB
	public static final String ACTION_BUSCAR_PROPASDEB     		    = "buscarProPasDeb";
	public static final String ACTION_ADMINISTRAR_PROPASDEB  		= "administrarProPasDeb";
	public static final String ACTION_ADMINISTRAR_PROCESO_PROPASDEB = "administrarProcesoProPasDeb";
	public static final String FWD_PROPASDEB_SEARCHPAGE      		= "proPasDebSearchPage";
	public static final String FWD_PROPASDEB_VIEW_ADAPTER 	 		= "proPasDebViewAdapter";
	public static final String FWD_PROPASDEB_EDIT_ADAPTER 	 		= "proPasDebEditAdapter";
	public static final String FWD_PROCESO_PROPASDEB_ADAPTER	    = "procesoProPasDebAdapter";
	public static final String PATH_ADMINISTRAR_PROCESO_PROPASDEB	= "/emi/AdministrarProcesoProPasDeb";
	// <--- Generacion de Archivos PAS y DEB

	// ---> Emision Puntual de Deuda
	public static final String ACTION_BUSCAR_EMISIONPUNTUAL     	 	 = "buscarEmisionPuntual";
	public static final String ACTION_ADMINISTRAR_EMISIONPUNTUAL 	 	 = "administrarEmisionPuntual";
	public static final String ACTION_ADMINISTRAR_EMISIONPUNTUAL_PREVIEW = "administrarEmisionPuntualPreview";
	public static final String ACTION_ADMINISTRAR_ENC_EMISIONPUNTUAL 	 = "administrarEncEmisionPuntual";
	public static final String ACT_PREVIEW 							 	 = "ver";
	public static final String FWD_EMISIONPUNTUAL_SEARCHPAGE 	 	 	 = "emisionPuntualSearchPage";
	public static final String FWD_EMISIONPUNTUAL_ENC_EDIT_ADAPTER   	 = "emisionPuntualEncEditAdapter";
	public static final String FWD_EMISIONPUNTUAL_VIEW_ADAPTER 		 	 = "emisionPuntualViewAdapter";
	public static final String FWD_EMISIONPUNTUAL_PREVIEW_ADAPTER 	 	 = "emisionPuntualPreviewAdapter";
	public static final String FWD_EMISIONPUNTUAL_RECIBOS_ADAPTER 	 	 = "emisionPuntualRecibosAdapter";

																			
	public static final String ACTION_ADMINISTRAR_EMISIONTRP 			 			= "administrarEmisionTRP";
	public static final String ACTION_ADMINISTRAR_ENC_EMISIONPUNTUAL_PARAM_RECURSO  = "administrarEncEmisionPuntualParamRecurso";
	public static final String ACTION_ADMINISTRAR_PLANODETALLE 						= "administrarPlanoDetalle";
	public static final String FWD_EMISIONPUNTUAL_TRP_ENC_EDIT_ADAPTER   			= "emisionPuntualTRPEncEditAdapter";
	public static final String FWD_PLANODETALLE_EDIT_ADAPTER   						= "planoDetalleEditAdapter";
	// <--- Emision Puntual de Deuda
	
	// ---> Emisino Externa
	public static final String FWD_EMISIONEXTERNA_SEARCHPAGE 	 		 = "emisionExternaSearchPage";
	public static final String FWD_EMISIONEXTERNA_UPLOADADAPTER  		 = "emisionExternaUploadAdapter";
	public static final String ACTION_ADMINISTRAR_EMISIONEXTERNA 		 = "administrarEmisionExterna";
	public static final String FWD_EMISIONEXTERNA_VIEW_ADAPTER 	 		 = "emisionExternaViewAdapter";
	public static final String ACTION_ADMINISTRAR_PROCESO_EMISIONEXTERNA = "administrarProcesoEmisionExterna";
	public static final String FWD_PROCESO_EMISIONEXTERNA_ADAPTER	     = "procesoEmisionExternaAdapter";
	// <--- Emision Externa
	
	
	// A deprecar

	// ---> Emision
	public static final String ACTION_BUSCAR_EMISION      = "buscarEmision";
	public static final String ACTION_ADMINISTRAR_EMISION = "administrarEmision";
	public static final String FWD_EMISION_SEARCHPAGE 	  = "emisionSearchPage";
	public static final String FWD_EMISION_VIEW_ADAPTER   = "emisionViewAdapter";
	public static final String FWD_EMISION_EDIT_ADAPTER   = "emisionEditAdapter";	
	// <--- Emision
	
	// ---> Emision CdM
	public static final String ACT_ADM_PROCESO_EMISION_CDM	   		   = "administrarProcesoCdM";
	public static final String ACT_ADM_PROCESO_IMPRESION_CDM  		   = "administrarImpresionCdM";
	public static final String ACT_ADM_PROCESO_EMISION_COR_CDM 		   = "administrarProcesoEmisionCorCdM"; 
	public static final String ACTION_ADMINISTRAR_PROCESO_EMISION_CDM  = "administrarProcesoEmisionCdM";
	public static final String FWD_PROCESO_EMISION_CDM_ADAPTER 		   = "procesoEmisionCdMAdapter";
	// <--- Emision CdM
	
	// ---> Proceso Impresion CDM
	public static final String ACTION_ADMINISTRAR_PROCESO_IMPRESION_CDM  = "administrarProcesoImpresionCdM";
	public static final String FWD_PROCESO_IMPRESION_CDM_ADAPTER 		 = "procesoImpresionCdMAdapter";
	// <--- Proceso Impresion CDM

	// ---> Proceso Emision Corregida CDM
	public static final String ACTION_ADMINISTRAR_PROCESO_EMISION_COR_CDM  = "administrarProcesoEmisionCorCdM";
	public static final String FWD_PROCESO_EMISION_COR_CDM_ADAPTER 		   = "procesoEmisionCorCdMAdapter";
	// <--- Proceso Emision Corregida CDM
}