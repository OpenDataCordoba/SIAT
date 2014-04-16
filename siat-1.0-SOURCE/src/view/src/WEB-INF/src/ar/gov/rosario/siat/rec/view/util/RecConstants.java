//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.view.util;


public interface RecConstants {
    
	// ---> TipoObra
	public static final String ACTION_BUSCAR_TIPOOBRA     	= "buscarTipoObra";
	public static final String ACTION_ADMINISTRAR_TIPOOBRA 	= "administrarTipoObra";
	public static final String FWD_TIPOOBRA_SEARCHPAGE 		= "tipoObraSearchPage";
	public static final String FWD_TIPOOBRA_VIEW_ADAPTER 	= "tipoObraViewAdapter";
	public static final String FWD_TIPOOBRA_EDIT_ADAPTER 	= "tipoObraEditAdapter";	
	// <--- TipoObra
	
	// ---> FormaPago
	public static final String ACTION_BUSCAR_FORMAPAGO     	= "buscarFormaPago";
	public static final String ACTION_ADMINISTRAR_FORMAPAGO = "administrarFormaPago";
	public static final String FWD_FORMAPAGO_SEARCHPAGE 	= "formaPagoSearchPage";
	public static final String FWD_FORMAPAGO_VIEW_ADAPTER 	= "formaPagoViewAdapter";
	public static final String FWD_FORMAPAGO_EDIT_ADAPTER 	= "formaPagoEditAdapter";	
	// <--- FormaPago
	
	// ---> Contrato
	public static final String ACTION_BUSCAR_CONTRATO     	= "buscarContrato";
	public static final String ACTION_ADMINISTRAR_CONTRATO 	= "administrarContrato";
	public static final String FWD_CONTRATO_SEARCHPAGE 		= "contratoSearchPage";
	public static final String FWD_CONTRATO_VIEW_ADAPTER 	= "contratoViewAdapter";
	public static final String FWD_CONTRATO_EDIT_ADAPTER 	= "contratoEditAdapter";	
	// <--- Contrato
	
	// ---> PlanillaCuadra (Encabezado)
	public static final String ACTION_BUSCAR_PLANILLACUADRA     	 = "buscarPlanillaCuadra";
	public static final String FWD_PLANILLACUADRA_SEARCHPAGE 	     = "planillaCuadraSearchPage";
	
	public static final String ACTION_ADMINISTRAR_PLANILLACUADRA 	 = "administrarPlanillaCuadra";
	
	public static final String FWD_PLANILLACUADRA_VIEW_ADAPTER 		 = "planillaCuadraViewAdapter";
	public static final String FWD_PLANILLACUADRA_ENC_EDIT_ADAPTER 	 = "planillaCuadraEncEditAdapter";
	public static final String ACTION_BUSCAR_CALLE     	             = "buscarCalle";	
	public static final String FWD_PLANILLACUADRA_ADAPTER 		     = "planillaCuadraAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_PLANILLACUADRA = "administrarEncPlanillaCuadra";	
	public static final String PATH_ADMINISTRAR_PLANILLACUADRA     	 = "/rec/AdministrarPlanillaCuadra"; // utilizado para redirigir en el agregar PlanillaCuadra
	public static final String ACT_CAMBIAR_ESTADO                    = "cambiarEstado";
	public static final String ACT_INFORMAR_CATASTRALES              = "informarCatastrales";
	public static final String ACT_MODIFICAR_NUMERO_CUADRA           = "modificarNumeroCuadra";
	public static final String MTD_PARAM_CALLE                       = "paramCalle";
	// <--- PlanillaCuadra (Encabezado)
	
	// ---> PlaCuaDet (Detalle)
	public static final String ACTION_BUSCAR_PLACUADET     	  = "buscarPlaCuaDet";
	public static final String FWD_PLACUADET_SEARCHPAGE       = "plaCuaDetSearchPage";
	
	public static final String ACTION_ADMINISTRAR_PLACUADET   = "administrarPlaCuaDet";
	public static final String FWD_PLACUADET_ADAPTER 		  = "plaCuaDetAdapter";
	public static final String FWD_PLACUADET_VIEW_ADAPTER 	  = "plaCuaDetViewAdapter";
	public static final String FWD_PLACUADET_EDIT_ADAPTER 	  = "plaCuaDetEditAdapter";
	// <--- PlaCuaDet
		
	// Mantenedor de Obra
	// ---> Obra (Encabezado)
	public static final String ACTION_BUSCAR_OBRA     	= "buscarObra";
	public static final String FWD_OBRA_SEARCHPAGE 	    = "obraSearchPage";
	
	public static final String ACTION_ADMINISTRAR_OBRA 	= "administrarObra";
	
	public static final String FWD_OBRA_VIEW_ADAPTER 		= "obraViewAdapter";
	public static final String FWD_OBRA_ENC_EDIT_ADAPTER 	= "obraEncEditAdapter";
	public static final String FWD_OBRA_ADAPTER 		    = "obraAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_OBRA 	= "administrarEncObra";	
	public static final String PATH_ADMINISTRAR_OBRA     	= "/rec/AdministrarObra"; // utilizado para redirigir en el agregar Obra
	// <--- Obra
	
	// ---> ObraFormaPago (Detalle)
	public static final String ACTION_ADMINISTRAR_OBRAFORMAPAGO	= "administrarObraFormaPago";
	public static final String FWD_OBRAFORMAPAGO_ADAPTER 		= "obraFormaPagoAdapter";
	public static final String FWD_OBRAFORMAPAGO_VIEW_ADAPTER 	= "obraFormaPagoViewAdapter";
	public static final String FWD_OBRAFORMAPAGO_EDIT_ADAPTER 	= "obraFormaPagoEditAdapter";
	// <--- ObraFormaPago

	// ---> ObraPlanillaCuadra (Detalle)
	public static final String ACTION_ADMINISTRAR_OBRAPLANILLACUADRA     = "administrarObraPlanillaCuadra";
	public static final String FWD_OBRAPLANILLACUADRA_VIEW_ADAPTER 	     = "obraPlanillaCuadraViewAdapter";
	public static final String FWD_OBRAPLANILLACUADRA_SELECT_SEARCHPAGE  = "planillaCuadraSearchPage";	
	// <--- ObraPlanillaCuadra

	// ---> ObrRepVen (Detalle)
	public static final String ACTION_ADMINISTRAR_OBRREPVEN	= "administrarObrRepVen";
	public static final String FWD_OBRREPVEN_EDIT_ADAPTER 	= "obrRepVenEditAdapter";
	// <--- ObrRepVen (Detalle)
	
	// ---> Asignar repartidores a planillas
	public static final String ACTION_BUSCARPLANILLAS_FORASIGNARREPARTIDOR = "buscarPlanillasForAsignarRepartidor";	
	public static final String FWD_PLANILLACUADRA_FORASIGNARREPARTIDOR     = "planillaCuadraForAsignarRepartidor";
	public static final String ACT_OBRA_ASIGNAR_REPARTIDOR                 = "asignarRepartidor";	
	public static final String MTD_PARAM_CALLE_FORASIGNARREPARTIDOR		   = "paramCalleForAsignarRepartidor"; 
	// <--- Asignar repartidores a planillas	
	
	// Fin Mantenedor de Obra
	
	// ---> UsoCdM
	public static final String ACTION_BUSCARUSOCDM = "buscarUsoCdM";  
	public static final String ACTION_ADMINISTRAR_USOCDM = "administrarUsoCdM";
	public static final String FWD_USOCDM_SEARCHPAGE = "usoCdMSearchPage";
	public static final String FWD_USOCDM_VIEW_ADAPTER = "usoCdMViewAdapter";
	public static final String FWD_USOCDM_EDIT_ADAPTER = "usoCdMEditAdapter";
	// <--- UsoCdM

	// ---> AnulacionObra
	public static final String ACTION_ADMINISTRAR_ANULACIONOBRA   		= "administrarAnulacionObra"; 
	public static final String ACTION_ADMINISTRAR_PROCESO_ANULACIONOBRA = "administrarProcesoAnulacionObra";
	public static final String FWD_ANULACIONOBRA_SEARCHPAGE		 	    = "anulacionObraSearchPage";
	public static final String FWD_ANULACIONOBRA_EDIT_ADAPTER 	  		= "anulacionObraEditAdapter";
	public static final String FWD_ANULACIONOBRA_VIEW_ADAPTER 	  		= "anulacionObraViewAdapter";
	public static final String ACT_ADM_PROCESO_ANULACION_OBRA	  		= "administrarProceso"; 
	public static final String FWD_PROCESO_ANULACION_OBRA_ADAPTER 		= "procesoAnulacionObraAdapter"; 
	// <--- AnulacionObra
	
	// ---> Novedad RS
	public static final String FWD_NOVEDADRS_SEARCHPAGE					= "novedadRSSearchPage";
	public static final String ACTION_BUSCAR_NOVEDADRS     	= "buscarNovedadRS";
	public static final String ACTION_ADMINISTRAR_NOVEDADRS	= "administrarNovedadRS";
	public static final String FWD_NOVEDADRS_VIEW_ADAPTER 	= "novedadRSViewAdapter";
	public static final String FWD_NOVEDADRS_EDIT_ADAPTER   = "novedadRSEditAdapter";
	public static final String ACT_APLICAR_NOVEDADRS	  	= "aplicar";
	public static final String ACT_APLICAR_MASIVO_NOVEDADRS	  	= "aplicarMasivo";
	
	public static final String FWD_CATRSDREI_EDIT_ADAPTER				= "catRSDreiEditAdapter";
	public static final String FWD_CATRSDREI_SEARCHPAGE 				= "catRSDreiSearchPage";
	public static final String ACTION_ADMINISTRAR_CATRSDREI 			= "administrarCatRSDrei";
	public static final String FWD_CATRSDREI_VIEW_ADAPTER		 		= "catRSDreiViewAdapter";
	
	
	
	
	
	
	
	
	
}
