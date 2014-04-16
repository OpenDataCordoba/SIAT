//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.view.util;


public interface EfConstants {

	// ---> Inspectores
	public static final String FWD_INSPECTOR_SEARCHPAGE 	= "inspectorSearchPage";
	public static final String ACTION_ADMINISTRAR_INSPECTOR = "administrarInspector";
	public static final String FWD_INSPECTOR_VIEW_ADAPTER 	= "inspectorViewAdapter";
	public static final String FWD_INSPECTOR_EDIT_ADAPTER 	= "administrarInspector";
	public static final String METOD_PARAM_PERSONA 			= "paramPersona";
	
    public static final String ACTION_ADMINISTRAR_ENC_INSPECTOR = "administrarEncInspector";
	public static final String FWD_INSPECTOR_ENC_EDIT_ADAPTER = "InspectorEncEditAdapter";
	public static final String PATH_ADMINISTRAR_INSPECTOR = "/ef/AdministrarInspector";
	public static final String METOD_DESATRVAL_PARAM_ATRIBUTO = "paramAtributo";
	public static final String FWD_INSPECTOR_ADAPTER = "inspectorAdapter";

	// <--- Inspectores
	
	// ---> InsSup
	public static final String ACTION_ADMINISTRAR_INSSUP = "administrarInsSup";
	public static final String FWD_INSSUP_EDIT_ADAPTER = "InsSupEditAdapter";
	public static final String FWD_INSSUP_VIEW_ADAPTER = "InsSupViewAdapter";
	// <--- InsSup
	
	// ---> Supervisor
	public static final String FWD_SUPERVISOR_SEARCHPAGE 	= "supervisorSearchPage";
	public static final String ACTION_ADMINISTRAR_SUPERVISOR = "administrarSupervisor";
	public static final String FWD_SUPERVISOR_VIEW_ADAPTER 	= "supervisorViewAdapter";
	public static final String FWD_SUPERVISOR_EDIT_ADAPTER 	= "supervisorEditAdapter";
	
	// <--- Supervisor
	
	// ---> FuenteInfo
	public static final String FWD_FUENTEINFO_SEARCHPAGE 	= "fuenteInfoSearchPage";
	public static final String ACTION_ADMINISTRAR_FUENTEINFO= "administrarFuenteInfo";
	public static final String FWD_FUENTEINFO_VIEW_ADAPTER 	= "fuenteInfoViewAdapter";
	public static final String FWD_FUENTEINFO_EDIT_ADAPTER 	= "fuenteInfoEditAdapter";
	
	// <--- FuenteInfo

	// ---> Plan Fiscal
	public static final String FWD_PLANFISCAL_SEARCHPAGE = "planFiscalSearchPage";
	public static final String ACTION_ADMINISTRAR_PLANFISCAL = "administrarPlanFiscal";
	public static final String FWD_PLANFISCAL_VIEW_ADAPTER = "planFiscalViewAdapter";
	public static final String FWD_PLANFISCAL_EDIT_ADAPTER = "planFiscalEditAdapter";
	// <--- Plan Fiscal

	// ---> Investigador
	public static final String FWD_INVESTIGADOR_SEARCHPAGE = "investigadorSearchPage";
	public static final String ACTION_ADMINISTRAR_INVESTIGADOR = "administrarInvestigador";
	public static final String FWD_INVESTIGADOR_VIEW_ADAPTER = "investigadorViewAdapter";
	public static final String FWD_INVESTIGADOR_EDIT_ADAPTER = "investigadorEditAdapter";
	// <--- Investigador    

	// ---> OpeInv
	public static final String FWD_OPEINV_SEARCHPAGE = "opeInvSearchPage";
	public static final String ACTION_ADMINISTRAR_OPEINV = "administrarOpeInv";
	public static final String FWD_OPEINV_VIEW_ADAPTER = "opeInvViewAdapter";
	public static final String FWD_OPEINV_EDIT_ADAPTER = "opeInvEditAdapter";
	// <--- OpeInv
	
	// ---> OpeInvCon
	public static final String FWD_OPEINVCON_SEARCHPAGE = "opeInvConSearchPage";
	public static final String ACTION_ADMINISTRAR_OPEINVCON = "administrarOpeInvCon";
	public static final String FWD_OPEINVCON_VIEW_ADAPTER = "opeInvConViewAdapter";
	public static final String FWD_OPEINVCON_EDIT_ADAPTER = "opeInvConEditAdapter";
	public static final String FWD_OPEINVCONACTA_SEARCHPAGE = "opeInvConActaSearchPage";
	public static final String FWD_OPEINVCONACTA_INVESTIG_ADAPTER = "asignarInvAdapter";
	public static final String ACT_EXCLUIR_DE_SELEC = "excluirDeSelec";
	public static final String PATH_MODIFICAR_OPEINVCON = "/ef/AdministrarOpeInvCon.do?method=volverDeLiqDeuda";
	public static final String FWD_OPEINVBUS_SEARCHPAGE = "opeInvBusSearchPage";
	public static final String ACTION_ADMINISTRAR_OPEINVBUS = "administrarOpeInvBus";
	public static final String FWD_OPEINVBUS_EDIT_ADAPTER = "opeInvBusEditAdapter";
	public static final String FWD_OPEINVBUS_VIEW_ADAPTER = "opeInvBusViewAdapter";
	public static final String ACT_ADM_PROCESO_OPEINVBUS = "admProcesoOpeInvBus";
	public static final String FWD_PROCESO_OPEINVBUS_ADAPTER = "procesoOpeInvBusAdapter";
	// <--- OpeInvCon
	
	// ---> OpeInvConActas
	public static final String FWD_ASIGNAR_INV_ADAPTER = "asignarInvAdapter";
	public static final String FWD_OPEINVCONACTA_HOJARUTA_ADAPTER = "generarHojaRuta";
	public static final String ACT_ASIGNARINV = "AsignarInvestigador";
	public static final String ACT_HOJARUTA = "generarHojaRuta";
	// <--- OpeInvConActas

	// ---> Actas
	public static final String FWD_ACTAINV_SEARCHPAGE = "actaInvSearchPage";
	public static final String ACTION_ADM_ACTAINV_VER_OPEINVCON = "verOpeInvCon";
	public static final String ACTION_ADMINISTRAR_ACTAINV = "administrarActaInv";
	public static final String FWD_ACTAINV_SEARCHPAGE_VER_OPEINVCON = "verOpeInvCon";
	public static final String FWD_ACTAINV_ADAPTER = "actaInvAdapter";
	public static final String FWD_ASIGNAR_INV_MAPA_VIEW = "verMapa";
	// <--- Actas	
	
	// ---> Aprobacion de Actas
	public static final String FWD_APROBACIONACTAINV_SEARCHPAGE = "aprobacionActaInvSearchPage";
	public static final String FWD_APROBACTAINV_ADAPTER = "aprobActaInvAdapter";
	public static final String ACT_CAMBIARESTADO = "cambiarEstado";
	public static final String FWD_APROBACTAINV = "aprovActInv";
	// <--- Aprobacion de Actas
	
	// ---> Emitir Ordenes de Control
	public static final String FWD_ORDENCONTROL_SEARCHPAGE = "ordenControlSearchPage";
	public static final String FWD_ORDENCONTROLCONTR_SEARCHPAGE = "ordenControlContrSearchPage";
	public static final String ACT_ORDENCONTROL_BUSCAR_CONTR = "buscarContr";
	public static final Object PATH_ORDENCONTROL_SEARCHPAGE = "/ef/BuscarOrdenControl.do?method=buscar";
	public static final String PATH_ORDENCONTROL_SPAGE = "/ef/BuscarOrdenControl";
	public static final String PATH_ORDENCONTROLEMIMANUAL_SEARCHPAGE = "/ef/BuscarOrdenControlContr.do?method=emisionManual";
	public static final String PATH_ORDENCONTROLCONTR_SEARCHPAGE = "/ef/BuscarOrdenControlContr.do?method=buscar";
	public static final String ACT_ADM_ORDENCONTROL = "admOrdenControl";
	public static final String ACT_ADMINISTRAR = "administrar";
	public static final String ACT_IMPRESION_ORDENCONTROL = "impresionOrdenControl";
	public static final String FWD_ORDENCONTROL_IMPRIMIR_ADAPTER = "ordenControlImprimirAdapter";
	public static final String FWD_ORDENCONTROLINV_ADAPTER = "ordenControlInvEditAdapter";
	public static final String ACT_ADM_ORDENCONTROL_INV="admOrdenControlInv";
	public static final String ACT_ASIGNAR_ORDENCONTROLFISINIT =  "asignarOrdenInit";
	public static final String FWD_ASIGNARORDEN_ADAPTER = "asignarOrdenAdapter";
	// <--- Emitir Ordenes de Control

	// ---> ADM Ordenes de Control
	public static final String FWD_ORDENCONTROLFIS_SEARCHPAGE 		= "ordenControlFisSearchPage";
	public static final String ACTION_ADMINISTRAR_ORDENCONTROLFIS 	= "admOrdenControlFis";
	public static final String FWD_ORDENCONTROLFIS_VIEW_ADAPTER 	= "ordenControlFisViewAdapter";
	public static final String FWD_ORDENCONTROLFIS_EDIT_ADAPTER 	= "ordenControlFisEditAdapter";
	public static final String FWD_ORDENCONTROLFIS_ADAPTER 			= "ordenControlFisAdapter";
	public static final Object PATH_ORDENCONTROL_VIEWADAPTER 		= "/ef/AdministrarOrdenControlFis.do?method=volverDeLiqDeuda";	
	public static final String PATH_ADMINISTRAR_ORDENCONTROLFIS 	= "/ef/AdministrarOrdenControlFis";
	public static final String PATH_BUSCAR_ORDENCONTROLFIS			= "/ef/BuscarOrdenControlFis";
	
	// <--- ADM Ordenes de Control
	
	// ---> ABM PeriodoOrden 
	public static final String FWD_PERIODOORDEN_EDIT_ADAPTER = "periodoOrdenEditAdapter";
	public static final String FWD_PERIODOORDEN_VIEW_ADAPTER = "periodoOrdenViewAdapter";
	public static final String ACTION_ABM_PERIODOORDEN = "adminisrtarPeriodoOrden";
	// <--- ABM PeriodoOrden
	
	// ---> ADM Acta
	public static final String FWD_ACTA_ENC_EDIT_ADAPTER = "actaEncEditAdapter";
	public static final String PATH_ADMINISTRAR_ACTA = "/ef/AdministrarActa";
	public static final String ACTION_ADMINISTRAR_ENC_ACTA = "administrarEncActa";
	public static final String FWD_ACTA_VIEW_ADAPTER = "actaViewAdapter";
	public static final String FWD_ACTA_ADAPTER = "actaAdapter";
	public static final String ACTION_ADMINISTRAR_ORDCONDOC = "administarOrdConDoc";
	public static final String FWD_ADM_ACTA = "administrarActa";
	// <--- ADM Acta

	// ---> ABM OrdConDoc
	public static final String FWD_ORDCONDOC_VIEW_ADAPTER = "ordConDocViewAdapter";
	public static final String FWD_ORDCONDOC_EDIT_ADAPTER = "ordConDocEditAdapter";
	// <--- ABM OrdConDoc	

	// ---> ABM InicioInv
	public static final String FWD_INICIOINV_EDIT_ADAPTER 	= "inicioInvEditAdapter";
	public static final String ACTION_ABM_INICIOINV 		= "administrarInicioInv";
	// <--- ABM InicioInv	
	
	// ---> ABM PlaFueDat
	public static final String ACTION_ADMINISTRAR_ENC_PLAFUEDAT 	= "administrarEncPlaFueDat";
	public static final String ACTION_ABM_PLAFUEDAT 				= "administrarPlaFueDat";
	public static final String FWD_PLAFUEDAT_ENC_EDIT_ADAPTER 		= "PlaFueDatEncEditAdapter";
	public static final String PATH_ADMINISTRAR_PLAFUEDAT 			= "/ef/AdministrarPlaFueDat";
	public static final String FWD_PLAFUEDAT_VIEW_ADAPTER 			= "plaFueDatViewAdapter";
	public static final String FWD_PLAFUEDAT_ADAPTER 				= "plaFueDatAdapter";
	// <--- ABM PlaFueDat
	
	// ---> ABM PLAFUEDATCOL
	public static final String ACTION_ADMINISTRAR_PLAFUEDATCOL 	= "administrarPlaFueDatCol";
	public static final String FWD_PLAFUEDATCOL_VIEW_ADAPTER 	= "plaFueDatColViewAdapter";
	public static final String FWD_PLAFUEDATCOL_EDIT_ADAPTER 	= "plaFueDatColEditAdapter";
	
	public static final String ACTION_ADMINISTRAR_PLAFUEDATDET 	= "administrarPlaFueDatDet";
	public static final String FWD_PLAFUEDATDET_VIEW_ADAPTER 	= "plaFueDatDetViewAdapter";
	public static final String FWD_PLAFUEDATDET_EDIT_ADAPTER 	= "plaFueDatDetEditAdapter";

	
	// ---> ADM SolicitudEmiPerRetro
	public static final String FWD_SOLICITUDEMIPERRETRO_EDIT_ADAPTER = "solicitudEmiPerRetroEditAdapter";
	// <--- ADM SolicitudEmiPerRetro

	// <--- ABM PLAFUEDATCOL
	
	// ---> ABM COMPARACION
	public static final String FWD_COMPARACION_ENC_EDIT_ADAPTER 	= "comparacionEncEditAdapter";
	public static final String PATH_ADMINISTRAR_COMPARACION 		= "/ef/AdministrarComparacion";
	public static final String ACTION_ADMINISTRAR_COMPARACION 		= "administrarComparacion";
	public static final String ACTION_ADMINISTRAR_ENC_COMPARACION 	= "administrarEncComparacion";
	public static final String FWD_COMPARACION_VIEW_ADAPTER 		= "comparacionViewAdapter";
	public static final String FWD_COMPARACION_ADAPTER 				= "comparacionAdapter";
	// <--- ABM COMPARACION
	
	// ---> ABM COMPFUENTE
	public static final String ACTION_ADMINISTRAR_COMPFUENTE 	= "administrarCompFuente";
	public static final String FWD_COMPFUENTE_VIEW_ADAPTER 		= "compFuenteViewAdapter";
	public static final String FWD_COMPFUENTE_EDIT_ADAPTER 		= "compFuenteEditAdapter";
	// <--- ABM COMPFUENTE
	
	// ---> ABM OrdConBasImp
	public static final String ACTION_ADMINISTRAR_ORDCONBASIMP 				= "administrarOrdConBasImp";
	public static final String ACT_CARGAR_AJUSTES 							= "cargarAjustes";
	public static final String ACT_CARGAR_ALICUOTAS 						= "cargarAlicuotas";
	public static final String FWD_ORDCONBASIMP_EDIT_ADAPTER 				= "ordConBasImpEditAdapter";
	public static final String FWD_ORDCONBASIMP_ADAPTER 					= "ordConBasImpAdapter";
	public static final String FWD_ORDCONBASIMP_VIEW_ADAPTER 				= "ordConBasImpViewAdapter";
	public static final String FWD_ORDCONBASIMP_AJUSTES_EDIT_ADAPTER 		= "ordConBasImpAjEditAdapter";
	public static final String FWD_ORDCONBASIMP_AJUSTAR_PERIODOS_ADAPTER 	= "ordConBasImpAjustarPeriodosAdapter";
	public static final String FWD_ORDCONBASIMP_ALICUOTAS_ADAPTER 			= "ordConBasImpAlicuotasAdapter";	
	// <--- ABM OrdConBasImp
	
	// ---> ABM DetAju
	public static final String FWD_DETAJU_ENC_EDIT_ADAPTER 		= "detAjuEncEditAdapter";
	public static final String FWD_DETAJU_VIEW_ADAPTER 			= "detAjuViewAdapter";
	public static final String FWD_DETAJU_ADAPTER 				= "detAjuAdapter";
	public static final String PATH_ADMINISTRAR_DETAJU 			= "/ef/AdministrarDetAju";
	public static final String ACTION_ADMINISTRAR_ENC_DETAJU 	= "administrarEncDetAju";
	public static final String ACTION_ADMINISTRAR_DETAJU 		= "administrarDetAju";
	// <--- ABM DetAju
	
	// ---> ABM DetAjuDet
	public static final String ACTION_ADMINISTRAR_DETAJUDET 	= "administrarDetAjuDet";
	// <--- ABM DetAjuDet
	
	// ---> ABM AliComFueCol
	public static final String ACTION_ADMINISTRAR_ALICOMFUECOL 	= "administrarAliComFueCol";
	public static final String ACT_INIT 						= "init";
	public static final String FWD_ALICOMFUECOL_VIEW_ADAPTER 	= "aliComFueColViewAdapter";
	public static final String FWD_ALICOMFUECOL_EDIT_ADAPTER 	= "aliComFueColEditAdapter";
	public static final String FWD_ALICOMFUECOL_INIT_ADAPTER 	= "aliComFueColInitAdapter";
	public static final String FWD_ALICOMFUECOL_HIST_ALICUOTA 	= "aliComFueColHistAlicuotaAdapter";
	public static final String FWD_AGREGAR_MASIVO 				= "agregarMasivo";
	public static final String FWD_DETAJU_MODIF_RET_ADAPTER 	= "modifRetAdapter";
	// <--- ABM AliComFueCol	
	
	// ---> ABM MesaEntrada
	public static final String FWD_MESAENTRADA_VIEW_ADAPTER 	= "mesaEntradaViewAdapter";
	public static final String FWD_MESAENTRADA_EDIT_ADAPTER 	= "mesaEntradaEditAdapter";
	public static final String ACTION_ADMINISTRAR_MESAENTRADA 	= "administrarMesaEntrada";
	// <--- ABM MesaEntrada
	
	// ---> ABM AproOrdCon
	public static final String FWD_APROORDCON_VIEW_ADAPTER 		= "aproOrdConViewAdapter";
	public static final String FWD_APROORDCON_EDIT_ADAPTER 		= "aproOrdConEditAdapter";
	public static final String ACTION_ADMINISTRAR_APROORDCON 	= "administrarAproOrdCon";	
	// <--- ABM AproOrdCon
	
	// ---> ABM DetAjuDocSop
	public static final String FWD_DETAJUDOCSOP_VIEW_ADAPTER 	= "detAjuDocSopViewAdapter";
	public static final String FWD_DETAJUDOCSOP_EDIT_ADAPTER 	= "detAjuDocSopEditAdapter";
	public static final String ACTION_ADMINISTRAR_DETAJUDOCSOP 	= "administrarDetAjuDocSop";
	public static final String PATH_ADMINISTRAR_DETAJUDOCSOP 	= "/ef/AdministrarDetAjuDocSop";
	// <--- ABM DetAjuDocSop
	

	// ---> DOCSOP
	public static final String FWD_DOCSOP_SEARCHPAGE 	= "docSopSearchPage";
	public static final String ACTION_ADMINISTRAR_DOCSOP = "administrarDocSop";
	public static final String FWD_DOCSOP_VIEW_ADAPTER 	= "docSopViewAdapter";
	public static final String FWD_DOCSOP_EDIT_ADAPTER 	= "docSopEditAdapter";
	// <--- DOCSOP

	// ---> ABM ComAju
	public static final String FWD_COMAJU_VIEW_ADAPTER 			= "comAjuViewAdapter";
	public static final String FWD_COMAJU_ENC_EDIT_ADAPTER 		= "comAjuEncEditAdapter";
	public static final String ACTION_ADMINISTRAR_COMAJU 		= "administrarComAju";
	public static final String ACTION_ADMINISTRAR_ENC_COMAJU 	= "administrarEncComAju";
	public static final String PATH_ADMINISTRAR_COMAJU 			= "/ef/AdministrarComAju";
	public static final String FWD_COMAJU_ADAPTER 				= "comAjuAdapter";
	
	

}
