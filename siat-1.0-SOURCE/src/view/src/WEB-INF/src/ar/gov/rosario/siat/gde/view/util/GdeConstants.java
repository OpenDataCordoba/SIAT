//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.view.util;



public interface GdeConstants {
	
	// ---> Deuda Contribuyente
	public static final String ACTION_BUSCAR_DEUDACONTRIB     	= "buscarDeudaContrib";
	public static final String FWD_DEUDACONTRIB_SEARCHPAGE 		= "deudaContribSearchPage";
	public static final String FWD_DEUDACONTRIB_ESTADOCUENTA	= "estadoCuenta";
	public static final String PATH_BUSCAR_DEUDACONTRIB     	= "/gde/BuscarDeudaContrib";
	public static final String ACT_PARAM_CONTRIBUYENTE		    = "paramContribuyente";
	public static final String PATH_BUSCAR_DEUDACONTRIB_ID		= "/gde/BuscarDeudaContrib.do?method=inicializar&id=";
	
	// <--- Deuda Contribuyente
	
	// ---> Liquidacion Deuda  ******************************************************
		// Administrar Deuda
	public static final String PATH_INICIALIZAR_GR = "/gde/AdministrarLiqDeuda";
	public static final String ACTION_INICIALIZAR_GR = "inicializarGr";
	public static final String ACTION_INICIALIZAR_DEUDACONTRIB = "inicializarDeudaContrib";	
	public static final String PATH_BUSCARDEUDA_REC_CUENTA = "/gde/AdministrarLiqDeuda.do?method=inicializarGr";
	
	public static final String PATH_INFORMEESCRIBANO = "/gde/AdministrarTramite.do?method=inicializar";
	public static final String FWD_LIQDEUDA_INGRESO_GR_ADAPTER = "liqDeudaIngresoGrAdapter";
	public static final String FWD_LIQDEUDA_INGRESO_CONTR_ADAPTER = "liqDeudaIngresoContrAdapter";
	public static final String FWD_LIQDEUDA_ADAPTER = "liqDeudaAdapter";
	public static final String FWD_LIQDEUDA_IMPRIMIR_INFORME_DEUDA = "liqDeudaImprimirInfDeuda";
	public static final String ACTION_INGRESAR_LIQDEUDA_GR = "ingresarLiqDeudaGr";
	public static final String FWD_LIQDEUDA_FILTROCUENTAAUTO_ADAPTER = "liqDeudaFilterCuentaAutoAdapter";
	
	
	public static final String FWD_VER_DETALLE_OBJIMP = "verDetalleObjImp";
	public static final String FWD_VER_HISTORICO_CONTRIB = "verHistoricoContrib";
	public static final String FWD_VER_HISTORICO_EXENCION = "verHistoricoExencion";	
	public static final String FWD_VER_PLANILLA = "verPlanilla";
	
		// Administrarar Convenio Cuenta
	public static final String ACTION_VER_CONVENIO_CUENTA = "verConvenio";
	public static final String PATH_VER_CONVENIO="/gde/AdministrarLiqConvenioCuenta.do?method=verConvenio&selectedId=";
	public static final String ACTION_VER_CONVENIO_INIT = "verConvenioInit";
	public static final String ACTION_VER_CONVENIO_VER = "verConvenioVer";
	public static final String FWD_RESCATEINDIV_INIT = "verRescateInd";
	
	public static final String ACT_SALDOPORCAD_ADAPTER="salPorCadIndividualInit";
	public static final String FWD_LIQCONVENIOCUENTA_ADAPTER = "liqConvenioCuentaAdapter";
	public static final String ACT_INCLUDE_VERDETALLE_CONVENIO = "includeVerDetalleConvenio";
	public static final String FWD_LIQCONVENIOSALPORCAD_ADAPTER="liqConvenioSalPorCadAdapter";
	public static final String FWD_LIQCONVENIOCUOTASALDO_ADAPTER = "cuotaSaldoInit";
	
		// Administrar Detalle Deuda
	public static final String PATH_VER_CUENTA = "/gde/AdministrarLiqDeuda.do?method=verCuenta&selectedId=";
	public static final String PATH_VER_CUENTACONTRIB = "/gde/AdministrarLiqDeuda.do?method=verDeudaContrib&id=";
	public static final String ACT_VER_DEUDA_CONTRIB="verDeudaContribuyente";
	public static final String ACTION_VER_DETALLE_DEUDA = "verDetalleDeuda";
	public static final String FWD_LIQDETALLEDEUDA_VIEW_ADAPTER = "liqDetalleDeudaViewAdapter";
	public static final String PATH_ACTION_FOR_CONFIRMAR_MODIFICAR = "/gde/AdministrarLiqDeuda";
	public static final String PATH_ACTION_PARAMETER_FOR_CONFIRMAR_MODIFICAR = "verCuenta&selectedId=";
	public static final String ACT_MODIFICAR_DEUDA="modificarDeuda";
	
	public static final String ACT_INCLUDE_VERDETALLE_DEUDA = "includeVerDetalleDeuda";
	public static final String FWD_VER_DEC_JURADA = "verCuentaDecJur";
	public static final String PATH_VER_DETALLE_DEUDA = "/gde/AdministrarLiqDetalleDeuda.do?method=verDetalleDeuda";
	public static final String PATH_BUSCAR_DECJUR = "/gde/BuscarDecJur";
			
		// Administrar Reclamo
	public static final String ACTION_RECLAMAR_ACENT = "reclamarAcent";
	public static final String FWD_RECLAMO_ADAPTER = "liqReclamoEditAdapter";
	public static final String FWD_RECLAMO_CUOTA_ADAPTER = "liqReclamoCuotaEditAdapter";
	public static final String FWD_RECLAMO_MSG = "liqReclamoMsg";
	public static final String FWD_RECLAMO_CUOTA_MSG = "liqReclamoCuotaMsg";
	
		// Administrar Reconfeccion
	public static final String ACTION_RECONFECCIONAR = "reconfeccionar";
	public static final String ACTION_VOLANTEPAGOINTRS = "volantePagoIntRS";
	public static final String FWD_RECONFECCION_ADAPTER ="liqReconfeccionAdapter";
	public static final String FWD_RECONFECCION_FINALIZADA ="reconfeccionFinalizada";
	public static final String FWD_RECONFECCION_IMP_RECIBOS ="imprimirRecibos";
	// Volante de Pagos de Intereses RS 
	public static final String FWD_VOLANTEPAGOINTRS_ADAPTER ="liqVolantePagoIntRSAdapter";
	public static final String FWD_VOLANTEPAGOINTRS_FINALIZADA ="liqVolantePagoIntRSFinalizada";
	public static final String FWD_VOLANTEPAGOINTRS_IMP_RECIBOS ="imprimirVolante";
	
	 // CDM
	public static final String ACTION_CAMBIO_PLAN_CDM = "cambioPlanCDM";
	public static final String ACTION_CUOTASALDO_CDM = "cuotaSaldoCDM";
	public static final String ACTION_CAMBIO_PLAN_PRINT_ADAPTER = "getPrintAdapter";
	public static final String ACTION_CUOTASALDOCDM_PRINT_ADAPTER = "getCuotaSaldoPrintAdapter";
	
	public static final String FWD_DEUDACDMVENCIDA_ADAPTER ="deudaVencidaCDMAdapter";
	public static final String FWD_CAMBIOPLANCDM_ADAPTER ="cambioPlanCDMAdapter";
	public static final String FWD_CAMBIOPLANCDM_PRINT_ADAPTER ="cambioPlanCDMPrintAdapter";	
	public static final String FWD_CAMBIOPLANCDM_IMP_FORM ="cambioPlanCDMImpForm";
	public static final String FWD_CAMBIOPLANCDM_IMP_RECIBOS = "cambioPlanCDMImpRecibo";
	
	public static final String FWD_CUOTASALDOCDM_ADAPTER ="cuotaSaldoCDMAdapter";
	public static final String FWD_CUOTASALDOCDM_PRINT_ADAPTER = "cuotaSaldoCDMPrintAdapter";
	public static final String FWD_CUOTASALDOCDM_IMP_RECIBOS = "cuotaSaldoCDMImpRecibo";
	
		// Formalizacion de Convenio de Pago
	public static final String ACTION_FORMCONVENIO = "formalizarConvenio";
	public static final String ACTION_FORMCONVENIOESP="formalizarConvenioEsp";
	public static final String FWD_FORMCONVENIO_ADAPTER ="liqFormConvenioAdapter";
	public static final String FWD_FORMCONVENIOESP_ADAPTER ="liqFormConvenioEspAdapter";
	public static final String FWD_FORMCONVENIO_PLANES_ADAPTER ="liqFormConvenioPlanesAdapter";
	public static final String FWD_FORMCONVENIO_CUOTAS_ADAPTER ="liqFormConvenioCuotasAdapter";
	public static final String FWD_FORMCONVENIO_SIMULA_CUOTAS_ADAPTER ="liqFormConvenioSimulaCuotasAdapter";
	public static final String FWD_FORMCONVENIO_FORMAL_ADAPTER ="liqFormConvenioFormalAdapter";
	public static final String FWD_FORMCONVENIO_PRINT_ADAPTER ="liqFormConvenioPrintAdapter";
	
	public static final String FWD_FORMCONVENIOCYQ_PRINT_ADAPTER ="liqFormConvenioCyqPrintAdapter"; // Redireccion a Cyq desde Gde
	
	public static final String FWD_FORMCONVENIO_IMP_FORM = "liqFormConvenioImpForm";
	public static final String FWD_FORMCONVENIO_IMP_RECIBOS = "liqFormConvenioImpRecibos";
	public static final String FWD_FORMCONVENIO_IMP_ALTCUOTAS = "liqFormConvenioImpAltCuotas";
	public static final String FWD_FORMCONVENIO_IMP_FORM_AUTO = "liqFormConvenioImpFormAuto"; // Formulario en banco para autoliquidables
	
	public static final String FWD_VER_DETALLE_PLAN = "verDetallePlan";	
	public static final String METOD_FORMCONVENIO_PARAM_PERSONA = "paramPersona";
	public static final String FWD_FORMCONVENIO_SELECCIONARPLAN = "seleccionarPlan";
	public static final String PATH_BUSCAR_CONVENIO="/gde/BuscarConvenio.do?method=buscar&selectedId=";
	public static final String FWD_PAGOACUENTAADAPTER="pagoACuentaInit";
	
		// desglose ajuste
	public static final String ACTION_DESGLOSE_AJUSTE = "desglosarAjuste";
	
	// Cierre de Comercio
	public static final String ACTION_CIERRE_COMERCIO = "cierreComercio";
	public static final String FWD_CIERRE_COMERCIO = "cierreComercioAdapter";
	public static final String PATH_ADMINISTRAR_CIERRE_COMERCIO = "/gde/AdministrarCierreComercio";
	public static final String MTD_CIERRE_COMERCIO="cierreComercio";

	public static final String FWD_CIERRECOMERCIO_VIEW_ADAPTER = "cierreComercioViewAdapter";
	public static final String FWD_CIERRECOMERCIO_EDIT_ADAPTER = "cierreComercioEditAdapter";
	public static final String FWD_CIERRECOMERCIO_ADAPTER = "cierreComercioAdapter";
	// ---> cierreComercio
	
	// ---> Declaracion Jurada
	public static final String ACTION_DECJURMASIVA = "decJurMasiva";
	public static final String FWD_DECJUR_ADAPTER_INIT = "liqDecJurAdapterInit";
	public static final String FWD_DECJUR_ADAPTER_DETALLE  = "liqDecJurAdapterDetalle";
	public static final String FWD_DECJUR_ADAPTER_GENERAL  = "liqDecJurAdapterGeneral";
	public static final String FWD_DECJUR_ADAPTER_SIMULA  = "liqDecJurAdapterSimula";
	public static final String PATH_ADMINISTRAR_CONVENIO="/gde/AdministrarLiqFormConvenio";
	public static final String MTD_INICIALIZAR_FROM_DECJURMAS="inicializarDecJurMas";
	//<--- Declaracion Jurada
	
	// <--- Liquidacion Deuda ******************************************************

	// ---> Envio Judicial
	public static final String PROCESO_MASIVO_PROCESO_MASIVO     	= "buscarProcesoMasivo";
	public static final String FWD_PROCESO_MASIVO_SEARCHPAGE 		= "procesoMasivoSearchPage";
	
	public static final String ACTION_PROCESO_MASIVO_JUDICIAL 	= "administrarProcesoMasivo";
	public static final String FWD_PROCESO_MASIVO_VIEW_ADAPTER 		= "procesoMasivoViewAdapter";
	public static final String FWD_PROCESO_MASIVO_EDIT_ADAPTER 		= "procesoMasivoEditAdapter";
	// <--- Envio Judicial
	
	
	// ---> Proceso Envio Judicial
	public static final String FWD_PROCESO_PROCESO_MASIVO_ADAPTER 	     = "procesoMasivoAdmProcesoAdapter";
	public static final String ACTION_ADMINISTRAR_PROCESO_PROCESO_MASIVO = "administrarProcesoProcesoMasivo";
	public static final String ACT_ADMINISTRAR_PROCESO_PROCESO_MASIVO    = "administrarProceso";
	public static final String FWD_ADMINISTRAR_PROCESO_MASIVO    	     = "administrarProcesoMasivo";
	
	public static final String FWD_ADMINISTRAR_PRO_MAS_PRO_EXC    	     = "administrarProMasProExc";
	
	// ------> forwards de la deuda incluida
	public static final String FWD_BUSCAR_DEUDA_INC_PRO_MAS_AGREGAR    	 = "buscarDeudaIncProMasAgregar";
	public static final String FWD_DEUDA_INC_PRO_MAS_AGREGAR_SEARCHPAGE  = "deudaIncProMasAgregarSearchPage";
	public static final String FWD_BUSCAR_DEUDA_INC_PRO_MAS_ELIMINAR     = "buscarDeudaIncProMasEliminar";
	public static final String FWD_DEUDA_INC_PRO_MAS_ELIMINAR_SEARCHPAGE = "deudaIncProMasEliminarSearchPage";
	// utilizado para ver el logs y limpiar la seleccion de la deuda incluida
	public static final String FWD_ARMADO_SELECCION_DEUDA_INC_PRO_MAS  = "administrarDeudaIncProMasArmadoSeleccion";
	public static final String FWD_PLANILLAS_DEUDA_INC_PRO_MAS         = "administrarDeudaIncProMasPlanillasDeuda";
	public static final String FWD_PLANILLAS_CUENTA_INC_PRO_MAS         = "administrarCuentaIncProMasPlanillasCuenta";
	
	public static final String ACT_PLANILLAS_CUENTA_INC_PRO_MAS          = "planillasCuentaIncProMas";
	public static final String ACT_PLANILLAS_DEUDA_INC_PRO_MAS          = "planillasDeudaIncProMas";
	public static final String ACT_PLANILLAS_CONVENIO_CUOTA_INC_PRO_MAS = "planillasConvenioCuotaProMas";
	
	public static final String FWD_CONS_POR_CTA_DEUDA_INC_PRO_MAS       = "buscarDeudaIncProMasConsPorCta";
	// <------ fin forwards de la deuda incluida
	// ------> forwards de la deuda excluida	
	public static final String FWD_BUSCAR_DEUDA_EXC_PRO_MAS_AGREGAR    	 = "buscarDeudaExcProMasAgregar";
	public static final String FWD_DEUDA_EXC_PRO_MAS_AGREGAR_SEARCHPAGE  = "deudaExcProMasAgregarSearchPage";
	public static final String FWD_DEUDA_EXC_PRO_MAS_ELIMINAR_SEARCHPAGE = "deudaExcProMasEliminarSearchPage";
	public static final String FWD_ARMADO_SELECCION_DEUDA_EXC_PRO_MAS    = "administrarDeudaExcProMasArmadoSeleccion";
	public static final String FWD_PLANILLAS_DEUDA_EXC_PRO_MAS           = "administrarDeudaExcProMasPlanillasDeuda";
	public static final String FWD_PLANILLAS_CUENTA_EXC_PRO_MAS          = "administrarCuentaExcProMasPlanillasCuenta";
	public static final String FWD_CONS_POR_CTA_DEUDA_EXC_PRO_MAS        = "buscarDeudaExcProMasConsPorCta";
	
	// <------ fin forwards de la deuda excluida
	
	// Reportes disponibles para el perfeccionamiento
	public static final String FWD_PRO_MAS_REPORTES_DEUDA_INC         = "administrarProMasRepDeudaInc";
	public static final String FWD_PRO_MAS_REPORTES_DEUDA_EXC         = "administrarProMasRepDeudaExc";
	
	public static final String ACT_RETROCEDER  = "retroceder";
	public static final String ACT_REINICIAR   = "reiniciar";
	
	public static final String FWD_CORRIDA_PRO_MAS_VIEW_ADAPTER = "corridaProcesoMasivoViewAdapter";
	public static final String FWD_ADMINISTRAR_CORRIDA_PRO_MAS  = "administrarCorridaProcesoMasivo";
	
	public static final String ACT_REPORTES_DEUDA_PRO_MAS          = "reportesDeudaProMas";
	public static final String ACT_REPORTES_CONVENIO_CUOTA_PRO_MAS = "reportesConvenioCuotaProMas";
	public static final String ACT_REPORTES_CUENTA_PRO_MAS         = "reportesCuentaProMas";

	// <--- Proceso Envio Judicial
	
	// SelAlm Parametros
	public static final String FWD_SEL_ALM_AGREGAR_PARAMETROS_SEARCHPAGE  = "selAlmAgregarParametrosSearchPage";
	
	public static final String FWD_ADMINISTRAR_DEUDA_INC_PRO_MAS_AGREGAR  = "administrarDeudaIncProMasAgregar";
	public static final String FWD_ADMINISTRAR_DEUDA_INC_PRO_MAS_ELIMINAR = "administrarDeudaIncProMasEliminar";
	
	
	public static final String FWD_DEUDA_INC_PRO_MAS_ELIMINAR_SELEC_IND_SEARCHPAGE  = "deudaIncProMasEliminarSelecIndSearchPage";
	//public static final String FWD_DEUDA_INC_PRO_MAS_AGREGAR_SELEC_IND_SEARCHPAGE  =  "deudaIncProMasAgregarSelecIndSearchPage";
																					  
	// limpiar seleccion y logsArmado
	public static final String FWD_DEUDA_INC_PRO_MAS_ARMADO_SELECCION_ADAPTER  =  "deudaIncProMasArmadoSeleccionAdapter";
	public static final String ACT_LIMPIAR_SELECCION_DEUDA_PRO_MAS    = "limpiarSeleccionDeudaEnviar";
	public static final String ACT_LOGS_ARMADO_DEUDA_PRO_MAS          = "logsArmadoDeudaEnviar";
	
	// planillaDeuda
	public static final String FWD_DEUDA_INC_PRO_MAS_PLANILLAS_DEUDA_ADAPTER  =  "deudaIncProMasPlanillasDeudaAdapter";
	// consulta deuda
	public static final String FWD_DEUDA_INC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE  = "deudaIncProMasConsPorCtaSearchPage";
	
	
	public static final String FWD_ADMINISTRAR_DEUDA_EXC_PRO_MAS_AGREGAR  = "administrarDeudaExcProMasAgregar";
	public static final String FWD_ADMINISTRAR_DEUDA_EXC_PRO_MAS_ELIMINAR = "administrarDeudaExcProMasEliminar";

	public static final String FWD_DEUDA_EXC_PRO_MAS_ELIMINAR_SELEC_IND_SEARCHPAGE  = "deudaExcProMasEliminarSelecIndSearchPage";
	public static final String FWD_DEUDA_EXC_PRO_MAS_AGREGAR_SELEC_IND_SEARCHPAGE  =  "deudaExcProMasAgregarSelecIndSearchPage";
	public static final String FWD_BUSCAR_DEUDA_EXC_PRO_MAS_ELIMINAR     = "buscarDeudaExcProMasEliminar";
	
	// limpiar seleccion y logsArmado
	public static final String FWD_DEUDA_EXC_PRO_MAS_ARMADO_SELECCION_ADAPTER  =  "deudaExcProMasArmadoSeleccionAdapter";

	// planillaDeuda excluida
	public static final String FWD_DEUDA_EXC_PRO_MAS_PLANILLAS_DEUDA_ADAPTER  =  "deudaExcProMasPlanillasDeudaAdapter";
	// consulta deuda excluida por nro de cuenta
	public static final String FWD_DEUDA_EXC_PRO_MAS_CONS_POR_CTA_SEARCHPAGE  = "deudaExcProMasConsPorCtaSearchPage";

	// ProMasProExc: procuradores excluidos del envio judicial
	public static final String FWD_PROMASPROEXC_VIEW_ADAPTER  = "proMasProExcViewAdapter";
	public static final String FWD_PROMASPROEXC_EDIT_ADAPTER  = "proMasProExcEditAdapter";
	
	// reporte de Deuda Incluida del envio judicial
	public static final String FWD_PRO_MAS_REPORTES_DEUDA_INC_ADAPTER  =  "procesoMasivoReportesDeudaIncluidaAdapter";
	// reporte de Deuda Incluida del envio judicial
	public static final String FWD_PRO_MAS_REPORTES_DEUDA_EXC_ADAPTER  =  "procesoMasivoReportesDeudaExcluidaAdapter";
	
	//	EstadoCuentaAction (DEMO)
	public static final String FWD_INGRESO_ESTADO_CUENTA_TGI = "ingresoEstadoCuentaTgi";
	public static final String FWD_OBTENER_ESTADO_DEUDA = "obtenerEstadoCuenta";
	public static final String PATH_INGRESO_ESTADOCUENTA_ID="/gde/AdministrarLiqEstadoCuenta.do?method=buscar&selectedId=";
		
	// ---> Descuento General
	public static final String FWD_DESGEN_SEARCHPAGE = "desGenSearchPage";
	public static final String FWD_DESGEN_DOSEARCH = "doSearch";
	public static final String ACTION_ADMINISTRAR_DESGEN = "AdministrarDesGen";
	public static final String FWD_DESGEN_EDIT_ADAPTER = "desGenEditAdapter";
	public static final String FWD_DESGEN_VIEW_ADAPTER = "desGenViewAdapter";
	// <--- Descuento General
	
	// ---> Descuentos Especiales
	public static final String FWD_DESESP_SEARCHPAGE = "desEspSearchPage";
	public static final String ACTION_ADMINISTRAR_DESESP = "AdministrarDesEsp";
	public static final String ACTION_ADMINISTRAR_ENC_DESESP = "administrarEncDesEsp";
	public static final String FWD_DESESP_VIEW_ADAPTER = "desEspViewAdapter";
	public static final String FWD_DESESP_EDIT_ADAPTER = "desEspEditAdapter";
	public static final String FWD_DESESP_ENC_EDIT_ADAPTER = "DesEspEncEditAdapter";
	public static final String PATH_ADMINISTRAR_DESESP = "/gde/AdministrarDesEsp";
	public static final String METOD_DESATRVAL_PARAM_ATRIBUTO = "paramAtributo";
	public static final String FWD_DESESP_ADAPTER = "desEspAdapter";
	// <--- Descuentos Especiales
	
	// ---> DesRecClaDeu
	public static final String ACTION_ADMINISTRAR_DESRECCLADEU = "administrarDesRecClaDeu";
	public static final String FWD_DESRECCLADEU_EDIT_ADAPTER = "DesRecClaDeuEditAdapter";
	public static final String FWD_DESRECCLADEU_VIEW_ADAPTER = "DesRecClaDeuViewAdapter";
	// <--- DesRecClaDeu
	
	// ---> DesAtrVal
	public static final String ACTION_ADMINISTRAR_DESATRVAL = "administrarDesAtrVal";
	public static final String FWD_DESATRVAL_VIEW_ADAPTER = "desAtrValViewAdapter";
	public static final String FWD_DESATRVAL_EDIT_ADAPTER = "desAtrValEditAdapter";
	// <--- DesAtrVAl
	
	// ---> DesEspExe
	public static final String FWD_DESESPEXE_VIEW_ADAPTER = "desEspExeViewAdapter";
	public static final String FWD_DESESPEXE_EDIT_ADAPTER = "desEspExeEditAdapter";
	public static final String ACTION_ADMINISTRAR_DESESPEXE = "administrarDesEspExe";
	// <--- DesEspExe
	
	//	 ---> Plan (Encabezado)
	public static final String ACTION_BUSCAR_PLAN     	= "buscarPlan";
	public static final String FWD_PLAN_SEARCHPAGE 	    = "planSearchPage";
	
	public static final String ACTION_ADMINISTRAR_PLAN 	= "administrarPlan";
	
	public static final String METOD_PARAM_ATRIBUTO 	= "paramAtributo";
	public static final String ACTION_BUSCAR_ATRIBUTO 	= "buscarAtributo";
	
	public static final String FWD_PLAN_VIEW_ADAPTER 		= "planViewAdapter";
	public static final String FWD_PLAN_ENC_EDIT_ADAPTER 	= "planEncEditAdapter";
	public static final String FWD_PLAN_ADAPTER 		    	= "planAdapter";
	public static final String ACTION_ADMINISTRAR_ENC_PLAN 	= "administrarEncPlan";	
	public static final String PATH_ADMINISTRAR_PLAN     	= "/gde/AdministrarPlan"; // utilizado para redirigir en el agregar Plan
	
	// ---> PlanClaDeu (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANCLADEU 	= "administrarPlanClaDeu";
	public static final String FWD_PLANCLADEU_VIEW_ADAPTER 	= "planClaDeuViewAdapter";
	public static final String FWD_PLANCLADEU_EDIT_ADAPTER 	= "planClaDeuEditAdapter";
	// <--- PlanClaDeu
	
	// ---> PlanMotCad (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANMOTCAD 	= "administrarPlanMotCad";
	public static final String FWD_PLANMOTCAD_VIEW_ADAPTER 	= "planMotCadViewAdapter";
	public static final String FWD_PLANMOTCAD_EDIT_ADAPTER 	= "planMotCadEditAdapter";
	// <--- PlanMotCad
	
	// ---> PlanForActDeu (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANFORACTDEU 	= "administrarPlanForActDeu";	
	public static final String FWD_PLANFORACTDEU_VIEW_ADAPTER 	= "planForActDeuViewAdapter";
	public static final String FWD_PLANFORACTDEU_EDIT_ADAPTER 	= "planForActDeuEditAdapter";
	// <--- PlanForActDeu
	
	// ---> PlanDescuento (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANDESCUENTO 	= "administrarPlanDescuento";
	public static final String FWD_PLANDESCUENTO_VIEW_ADAPTER 	= "planDescuentoViewAdapter";
	public static final String FWD_PLANDESCUENTO_EDIT_ADAPTER 	= "planDescuentoEditAdapter";
	// <--- PlanDescuento
	
	// ---> PlanIntFin (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANINTFIN 	= "administrarPlanIntFin";
	public static final String FWD_PLANINTFIN_VIEW_ADAPTER 	= "planIntFinViewAdapter";
	public static final String FWD_PLANINTFIN_EDIT_ADAPTER 	= "planIntFinEditAdapter";
	// <--- PlanIntFin
	
	// ---> PlanVen (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANVEN 	= "administrarPlanVen";
	public static final String FWD_PLANVEN_VIEW_ADAPTER 	= "planVenViewAdapter";
	public static final String FWD_PLANVEN_EDIT_ADAPTER 	= "planVenEditAdapter";
	// <--- PlanVen
	
	// ---> PlanExe (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANEXE 	= "administrarPlanExe";
	public static final String FWD_PLANEXE_VIEW_ADAPTER 	= "planExeViewAdapter";
	public static final String FWD_PLANEXE_EDIT_ADAPTER 	= "planExeEditAdapter";
	// <--- PlanExe
	
	// ---> PlanProrroga (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANPRORROGA 	= "administrarPlanProrroga";
	public static final String MTD_ADM_PLANPRORROGA 			= "admPlanProrroga";
	public static final String FWD_PLANPRORROGA_ADAPTER 	= "planProrrogaAdapter";
	public static final String FWD_PLANPRORROGA_VIEW_ADAPTER 	= "planProrrogaViewAdapter";
	public static final String FWD_PLANPRORROGA_EDIT_ADAPTER 	= "planProrrogaEditAdapter";
	// <--- PlanProrroga
	
	// ---> PlanAtrVal (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANATRVAL 	= "administrarPlanAtrVal";
	public static final String FWD_PLANATRVAL_VIEW_ADAPTER 	= "planAtrValViewAdapter";
	public static final String FWD_PLANATRVAL_EDIT_ADAPTER 	= "planAtrValEditAdapter";
	// <--- PlanAtrVal
	
	// ---> PlanImpMin (Detalle)
	public static final String ACTION_ADMINISTRAR_PLANIMPMIN 	= "administrarPlanImpMin";
	public static final String FWD_PLANIMPMIN_ADAPTER 			= "planImpMinAdapter";
	public static final String FWD_PLANIMPMIN_VIEW_ADAPTER 	= "planImpMinViewAdapter";
	public static final String FWD_PLANIMPMIN_EDIT_ADAPTER 	= "planImpMinEditAdapter";
	// <--- PlanImpMin
	
	// ---> PlanRecurso
	public static final String ACTION_ADMINISTRAR_PLANRECURSO  = "administrarPlanRecurso";
	// < --- Plan
	
	// ---> Procurador
	public static final String FWD_PROCURADOR_SEARCHPAGE = "procuradorSearchPage";
	public static final String ACTION_ADMINISTRAR_PROCURADOR = "administrarProcurador";
	public static final String ACTION_ADMINISTRAR_ENC_PROCURADOR="administrarEncProcurador";
	public static final String FWD_PROCURADOR_ENC_EDIT_ADAPTER="procuradorEncEditAdapter";
	public static final String PATH_ADMINISTRAR_PROCURADOR="/gde/AdministrarProcurador";
	public static final String FWD_PROCURADOR_VIEW_ADAPTER="procuradorViewAdapter";
	public static final String FWD_PROCURADOR_ADAPTER = "procuradorAdapter";

	// ---> ProRec(detalle)
	public static final String ACTION_ADMINISTRAR_PROREC = "administrarProRec";
	public static final String ACTION_ADMINISTRAR_ENC_PROREC = "administrarEncProRec";
	public static final String FWD_PROREC_ENC_EDIT_ADAPTER="proRecEncEditAdapter";
	public static final String PATH_ADMINISTRAR_PROREC="/gde/AdministrarProRec";
	public static final String FWD_PROREC_ADAPTER = "proRecAdapter";
	public static final String FWD_PROREC_VIEW_ADAPTER = "proRecViewAdapter";
	// <--- ProRec(detalle)
	
	//	 ---> ProRecDesHas(detalle)
	public static final String ACTION_ADMINISTRAR_PRORECDESHAS = "administrarProRecDesHas";
	public static final String FWD_PRORECDESHAS_EDIT_ADAPTER = "proRecDesHasEditAdapter";
	public static final String FWD_PRORECDESHAS_VIEW_ADAPTER = "proRecDesHasViewAdapter";
	//  <--- ProRecDesHas(detalle)
	
	//	 ---> ProRecDesHas(detalle)
	public static final String ACTION_ADMINISTRAR_PRORECCOM = "administrarProRecCom";
	public static final String FWD_PRORECCOM_EDIT_ADAPTER = "proRecComEditAdapter";
	public static final String FWD_PRORECCOM_VIEW_ADAPTER = "proRecComViewAdapter";
	//  <--- ProRecDesHas(detalle)	
	
	// <--- Procurador
	
	// ---> Administrar Planilla Envio Deudas
	
	public static final String ACTION_ADMINISTRAR_PLAENVDEUPRO = "administrarPlaEnvDeuPro";
	public static final String ACTION_ADM_PLAENVDEUPRO_HABILITAR_PLANILLA = "habilitarPlanilla";
	public static final String ACTION_ADM_PLAENVDEUPRO_RECOMPONER_PLANILLA = "recomponerPlanilla";
	public static final String ACTION_ADM_PLAENVDEUPRO_IMP_PAD = "imprimirPadron";
	public static final String ACTION_ADM_PLAENVDEUPRO_VER_CONSTANCIAS = "verConstancias";
	public static final String ACTION_ADM_PLAENVDEUPRO_HABILITAR_CONSTANCIAS = "habilitarConstancias";
	public static final String ACTION_ADM_PLAENVDEUPRO_IMP_CON = "imprimirConstanciasPlanilla";
	public static final String ACTION_ADM_PLAENVDEUPRO_GENERAR_ARCHIVO = "generarArchivoCD";
	public static final String FWD_PLAENVDEUPRO_SEARCHPAGE = "plaEnvDeuProSearchPage";
	public static final String FWD_PLAENVDEUPRO_VIEW_ADAPTER = "plaEnvDeuProViewAdapter";
	public static final String FWD_PLAENVDEUPRO_EDIT_ADAPTER = "plaEnvDeuProEditAdapter";
	public static final String FWD_PLAENVDEUPRO_RECOMPONER = "plaEnvDeuProRecomponer";
	public static final String FWD_VER_ARCHIVOS_PROCURADOR = "verArchivosProcurador";

	// <--- Administrar Planilla Envio Deudas
	
	// ---> Evento
	public static final String ACTION_ADMINISTRAR_EVENTO="administrarEvento";
	public static final String FWD_EVENTO_SEARCHPAGE = "eventoSearchPage";
	public static final String FWD_EVENTO_VIEW_ADAPTER = "eventoViewAdapter"; 
	public static final String FWD_EVENTO_EDIT_ADAPTER = "eventoEditAdapter";
	
	public static final String PATH_ADMINISTRAR_EVENTO = "/gde/AdministrarEvento";
	// <--- Evento

	// ---> Administrar Constancias de Deuda
	public static final String FWD_CONSTANCIADUE_SEARCHPAGE = "constanciaDeuSearchPage";
	public static final String ACTION_ADMINISTRAR_CONSTANCIADUE_VER_HISTORICO = "verHistorico";
	public static final String ACTION_ADMINISTRAR_CONSTANCIADUE = "administrarConstanciaDeu";
	public static final String ACTION_CONSTANCIADUE_MODIFICAR_DOMICILIO_ENV = "modificarDomicilioEnvio";	
	public static final String ACTION_ADMINISTRAR_ENC_CONSTANCIADUE = "administrarEncConstanciaDeu";
	public static final String FWD_CONSTANCIADUE_ENC_EDIT_ADAPTER = "constanciaDeuEncEditAdapter";
	public static final String FWD_CONSTANCIADEU_ADAPTER = "constanciaDeuAdapter";
	public static final String FWD_CONSTANCIADUE_VIEW_ADAPTER = "constanciaDeuViewAdapter";
	public static final String FWD_CONSTANCIADEU_BUSCAR_LOC_ADAPTER = "buscarLocalidad";
	public static final String PATH_ADMINISTRAR_CONSTANCIADUE = "/gde/AdministrarConstanciaDeu";
	public static final String FWD_CONSTANCIADUE_MODIF_DOM_ENV_ADAPTER = "constanciaDeuDomEnvAdapter";
	public static final String ACT_CONSTANCIADEU_ELIMINAR = "eliminar";
	
	public static final String ACTION_ADMIN_CONDEUTIT = "administrarConDeuTit";
	public static final String ACTION_ADMIN_CONDEUTIT_VER = "verTitular";
	public static final String FWD_ADMIN_CONDEUTIT_VIEW_ADAPTER = "conDeuTitViewAdapter";
	public static final String METOD_PARAM_PERSONA = "paramPersona";
	
	public static final String ACTION_ABM_CONDEUDET = "administrarConDeuDet";
	public static final String FWD_CONDEUDET_VIEW_ADAPTER = "conDeuDetViewAdapter";
	public static final String FWD_CONDEUDET_EDIT_ADAPTER = "conDeuDetEditAdapter";
	public static final String ACT_CONSTANCIADEU_IMPRIMIR = "impresionConstancia";
	public static final String ACT_CONSTANCIADEU_HABILITAR = "habilitar";
	public static final String ACT_CONSTANCIADEU_RECOMPONER = "recomponer";
	public static final String ACT_CONSTANCIADEU_ANULAR = "anular";
	public static final String ACTION_ADMIN_CONSTANCIADEU = "administrarConstancia";
	// <--- Administrar Constancias de Deuda	

	// ---> Administrar Deudas Judiciales Sin Constancia
	public static final String FWD_DEUJUDSINCONSTANCIA_SEARCHPAGE = "deuJudSinConstanciaSearchPage";
	public static final String FWD_DEUJUDSINCONSTANCIA_ADAPTER= "deuJudSinConstanciaAdapter";
	public static final String ACTION_BUSCAR_CONSTANCIADEU = "buscarConstanciaDeu";
	public static final String ACTION_ADMINISTRAR_DEUJUDSINCONSTANCIA = "administrarDeuJudSinConstancia";
	// <--- Administrar Deudas Judiciales Sin Constancia
	
	// ---> Consultar Convenio
	public static final String FWD_CONVENIO_SEARCHPAGE = "convenioSearchPage";
	// <--- Consultar Convenio
	
	// ---> Traspaso Devolucion Deuda
	public static final String ACTION_BUSCAR_TRASPASO_DEVOLUCION_DEUDA  	= "buscarTraspasoDevolucionDeuda";
	public static final String FWD_TRASPASO_DEVOLUCION_DEUDA_SEARCHPAGE 	= "traspasoDevolucionDeudaSearchPage";
	
	public static final String ACTION_ADMINISTRAR_TRASPASO_DEVOLUCION_DEUDA     = "administrarTraspasoDevolucionDeuda";
	public static final String ACTION_ADMINISTRAR_ENC_TRASPASO_DEVOLUCION_DEUDA = "administrarEncTraspasoDevolucionDeuda";
	public static final String FWD_TRASPASO_DEVOLUCION_DEUDA_ADAPTER 	        = "traspasoDevolucionDeudaAdapter";
	public static final String FWD_TRASPASO_DEVOLUCION_DEUDA_ENC_EDIT_ADAPTER   = "traspasoDevolucionDeudaEncEditAdapter";
	public static final String PATH_ADMINISTRAR_TRASPASO_DEVOLUCION_DEUDA     	= "/gde/AdministrarTraspasoDevolucionDeuda"; // utilizado para redirigir en el agregar TraspasoDevolucionDeuda
	
	public static final String ACT_AGREGAR_TRADEVDEUDET = "agregarTraDevDeuDet"; // unifica la agregacion de detalles de traspasos y devoluciones
	

	// <--- Traspaso Devolucion Deuda
	
	// ---> Consultar Cuenta por Objeto Imponible
	public static final String FWD_CUENTA_OBJIMP_SEARCHPAGE = "cuentaObjImpSearchPage";
	public static final String PATH_BUSCAR_CUENTA_OBJIMP    = "/gde/BuscarCuentaObjImp";
	public static final String PATH_BUSCAR_CUENTA_OBJIMP_ID = "/gde/BuscarCuentaObjImp.do?method=paramObjImp&selectedId=";
	public static final String ACTION_PARAM_OBJIMP			= "paramObjImp";
	// <--- Consultar Cuenta por Objeto Imponible
	
	// ---> Consultar Cuentas por Procurador
	public static final String FWD_CUENTAS_PROCURADOR_SEARCHPAGE = "cuentasProcuradorSearchPage";
	public static final String FWD_CUENTAS_PROCURADOR_EST_CUENTA ="estadoCuentaAdapter";
	public static final String PATH_BUSCAR_CUENTAS_PROCURADOR = "/gde/BuscarCuentasProcurador";
	public static final String PATH_BUSCAR_CUENTAS_PROCURADOR_ID = "/gde/BuscarCuentasProcurador.do?method=buscar";
	// <--- Consultar Cuentas por Procurador
	
	// ---> ADM Gestion Judicial
	public static final String FWD_GESJUD_SEARCHPAGE = "gesJudSearchPage";
	public static final String ACTION_ADMINISTRAR_GESJUD = "administrarGesJud";
	public static final String ACTION_GESJUD_VER_HISTORICOS = "verHistoricos";
	public static final String ACTION_GESJUD_REG_CADUCIDAD = "registrarCaducidad";
	public static final String ACTION_ADMINISTRAR_ENC_GESJUD = "administrarEncGesJud";
	public static final String FWD_GESJUD_ENC_EDIT_ADAPTER = "gesJudEncEditAdapter";
	public static final String PATH_ADMINISTRAR_GESJUD = "/gde/AdministrarGesJud";
	public static final String FWD_GESJUD_VIEW_ADAPTER = "gesJudViewAdapter";
	public static final String FWD_GESJUD_VIEW_HISTORICOS_ADAPTER = "gesJudViewHistoricosAdapter";
	public static final String FWD_GESJUD_EDIT_ADAPTER = "gesJudEditAdapter";
	public static final String FWD_GESJUD_REG_CADUCIDAD_ADAPTER = "gesJudRegCaducidadAdapter";
	public static final String FWD_GESJUD_ADAPTER = "gesJudAdapter";
	
	public static final String ACTION_ADMINISTRAR_GESJUDDEU = "administrarGesJudDeu";
	public static final String ACT_GESJUDDEU_AGREGAR_FROM_CONSTANCIA = "agregarFromConstancia";
	public static final String FWD_GESJUDDEU_EDIT_ADAPTER = "gesJudDeuEditAdapter";
	public static final String FWD_GESJUDDEU_VIEW_ADAPTER = "gesJudDeuViewAdapter";

	public static final String FWD_GESJUDEVENTO_VIEW_ADAPTER = "gesJudEventoViewAdapter";
	public static final String FWD_GESJUDEVENTO_EDIT_ADAPTER = "gesJudEventoEditAdapter";
	public static final String ACTION_ADMINISTRAR_GESJUDEVENTO = "administrarGesJudEvento";
	// <--- ADM Gestion Judicial	
	
	// ---> Rescate
	public static final String FWD_RESCATE_SEARCHPAGE   = "rescateSearchPage";
	public static final String FWD_RESCATE_VIEW_ADAPTER = "rescateViewAdapter";
	public static final String FWD_RESCATE_EDIT_ADAPTER = "rescateEditAdapter";
	public static final String PATH_BUSCARRESCATE = "/gde/BuscarRescate.do?method=buscar";
	public static final String FWD_RESCATE_SELADAPTER = "rescateMasivoSelAdapter";
	public static final String PATH_RESCATE_ADMINISTRAR = "/gde/AdministrarRescate.do?method=seleccionar&selectedId=";
	public static final String ACTION_ADMINISTRAR_RESCATE 	  = "administrarRescate";
	public static final String ACTION_ADMINISTRAR_ENC_RESCATE = "administrarEncRescate";
	
	// <--- Rescate	

	// ---> Reporte Convenio
	public static final String FWD_CONVENIO_REPORT = "convenioReport";
	// <--- Reporte Convenio
	// ---> Reporte Recaudacion
	public static final String FWD_RECAUDACION_REPORT = "recaudacionReport";
	// <--- Reporte Recaudacion
	
	// ---> Reporte Convenio Formalizado
	public static final String FWD_CONVENIO_FORM_REPORT = "convenioFormReport";
	// <--- Reporte Convenio Formalizado
	
	// ---> Reporte Importe a Recaudar
	public static final String FWD_IMPORTE_RECAUDAR_REPORT = "importeRecaudarReport";
	// <--- Reporte Importe a Recaudar
	
	// ---> Reporte Importe Recaudado planes
	public static final String FWD_IMPORTE_RECAUDADO_REPORT = "importeRecaudadoReport";
	// <--- Reporte Importe Recaudado planes

	// ---> Reporte Respuesta Operativos
	public static final String FWD_RESPUESTA_OPERATIVOS_REPORT = "respuestaOperativosReport";
	// <--- Reporte Respuesta Operativos

	// ---> Reporte Convenio A Caducar
	public static final String FWD_CONVENIO_A_CADUCAR_REPORT = "convenioACaducarReport";
	// <--- Reporte Convenio A Caducar
	
	// ---> Consultar EstadoCuenta
	public static final String ESTADOCUENTA = "estadoCuenta";
	public static final String FWD_ESTADOCUENTA_SEARCHPAGE = "estadoCuentaSearchPage";
	public static final String FWD_ESTADOCUENTA_ADAPTER = "estadoCuentaAdapter";
	public static final String FWD_ESTADOCUENTA_IMPRIMIR = "estadoCuentaImprimir";
	// <--- Consultar EstadoCuenta

	// ---> Informe de deuda para escribanos
	public static final String FWD_TRAMITE_SEARCHPAGE = "tramiteSearchPage";
	public static final String FWD_TRAMITE_ADAPTER = "tramiteAdapter";
	public static final String FWD_TRAMITE_PRINT = "tramitePrint"; 
	// <--- Informe de deuda para escribanos
	
	// ---> ABM LiqCom
	public static final String FWD_LIQCOM_SEARCHPAGE = "liqComSearchPage";
	public static final String ACTION_ADMINISTRAR_LIQCOM = "administrarLiqCom";
	public static final String FWD_LIQCOM_VIEW_ADAPTER = "liqComViewAdapter";
	public static final String FWD_LIQCOM_EDIT_ADAPTER = "liqComEditAdapter";
	public static final String ACT_ADM_PROCESO_LIQCOM = "administrarProcesoLiqCom";
	public static final String FWD_PROCESO_LIQCOM_ADAPTER = "procesoLiqComAdapter";
	// <--- ABM LiqCom
	
	// ---> Consulta convenio/recibo no liquidables
	public static final String FWD_CONRECNOLIQ_SEARCHPAGE = "conRecNoLiqSearchPage";
	public static final String ACTION_ADM_CONRECNOLIQ = "admConRecNoLiq";
	public static final String ACTION_ADM_CONRECNOLIQ_PROCESAR = "procesarConRecNoLiq";
	public static final String ACTION_ADM_CONRECNOLIQ_VOLVER_LIQUIDABLE = "volverLiquidableConRecNoLiq";
	// <--- Consulta convenio/recibo no liquidables	

	//--->ABM Saldo por Caducidad
	public static final String FWD_ABMSALPORCAD = "salPorCadSearchPage";
	public static final String FWD_ADDSALPORCAD = "agregarSalPorCad";
	public static final String FWD_SALPORCADMASIVOADAPTER = "salPorCadMasivoAdapter";
	public static final String FWD_SALDOMASIVOADMINADAPTER = "salPorCadAdminAdapter";
	public static final String PATH_SALPORCADADMINISTRAR = "/gde/AdministrarSalPorCadMasivo.do?method=administrarCorrida";
	public static final String PATH_BUSCARSALPORCAD = "/gde/BuscarSalPorCad.do?method=buscar";
	public static final String FWD_SALPORCADVER = "ver";
	public static final String FWD_SALPORCADMODIFICAR= "modificar";
	public static final String FWD_SALPORCADADMIN = "administrar";
	public static final String FWD_SALPORCADELIMINAR="eliminar";
	public static final String FWD_SALPORCADSELECCIONAR="seleccionar";
	public static final String PATH_SALPORCADSELECCIONAR="/gde/AdministrarSalPorCadMasivo.do?method=seleccionar&selectedId=";
	
	// ---> Anular/Desanular Deuda
	public static final String FWD_ANULARDEDUDA_INGRESO_ADAPTER = "anularDeudaIngresoAdapter";
	public static final String FWD_ANULARDEDUDA_ADAPTER = "anularDeudaAdapter";
	public static final String FWD_ANULARDEDUDA_ANULAR_ADAPTER = "anularDeudaAnularAdapter";
	public static final String FWD_INICIALIZAR_ANULARDEDUA = "/gde/AdministrarAnularDeuda.do?method=inicializar";
	public static final String PATH_ANULARDEUDA_ADAPTER   = "/gde/AdministrarAnularDeuda";
	// <--- Anular/Desanular Deuda
	
	// ---> Ver pagos Convenio
	public static final String FWD_VERPAGOSCONVENIOS = "verPagosConvenio";

	
	// ---> Consultar Recibo
	public static final String ACTION_BUSCAR_RECIBO     	= "buscarRecibo";
	public static final String ACTION_ADMINISTRAR_RECIBO 	= "administrarRecibo";
	public static final String FWD_RECIBO_SEARCHPAGE 		= "reciboSearchPage";
	public static final String FWD_RECIBO_ADAPTER 			= "reciboAdapter";
	// <--- Fin Consultar Recibo

	// ---> Consultar estados 
	public static final String FWD_CONVENIOESTADOSADAPTER = "convenioEstadosAdapter";
	
	// --> PlanRecurso
	public static final String FWD_PLANRECURSOEDIT = "planRecursoEditAdapter";
	public static final String FWD_PLANRECURSOVIEW = "planRecursoViewAdapter";

	// ---> Reportes de Seguimiento de las Gestión Judicial
	public static final String FWD_GESJUD_REPORT = "gesJudReport";
	// <--- Reportes de Seguimiento de las Gestión Judicial

	
	// ---> Ver consistencia de convenios
	public static final String FWD_CONVENIO_CONSIST = "verConvConsist";
	


	// ---> Reporte de deudas de cuenta en gestion judicial
	public static final String FWD_DEUCUEGESJUD_SEARCHPAGE = "deuCueGesJudSearchPage";
	// <--- Reporte de deudas de cuenta en gestion judicial

	
	// --> Reportes de Anulacion de Deuda
	public static final String FWD_DEUDAANULADA_REPORT = "deudaAnuladaReport";
	// <--- Reportes de Anulacion de Deuda


	// ---> Reporte de totales de emision
	public static final String FWD_EMISION_REPORT = "emisionReport";
	// <--- Reporte de totales de emision

	// ---> Reporte de Recaudado NUEVO
	public static final String FWD_RECAUDADO_REPORT = "recaudadoReport";
	// <--- Reporte de Recaudado NUEVO

	// ---> Reporte de Distribucion de Totales Emitidos
	public static final String FWD_DISTRIBUCION_REPORT = "distribucionReport";
	// <--- Reporte de Distribucion de Totales Emitidos
	
	// ---> Reporte de Contribuyente Cer
	public static final String FWD_CONTRIBUYENTECER_REPORT = "contribuyenteCerReport";
	// <--- Reporte de Contribuyente Cer
	
	// ---> Reporte de Contribuyente Cer 
	public static final String FWD_RECCONCER_REPORT = "recConCerReport";
	// <--- Reporte de Contribuyente Cer
	
	// ---> Reporte de Recaudación Cer 
	public static final String FWD_RECAUDACIONCER_REPORT = "recaudacionCerReport";
	// <--- Reporte de Recaudación Cer
	
	// ---> Reporte de Detalle Recaudación Cer 
	public static final String FWD_DETRECAUDACIONCER_REPORT = "detRecaudacionCerReport";
	// <--- Reporte de Detalle Recaudación Cer
	
	// ---> Reporte de Deuda de Procurador
	public static final String FWD_DEUDA_PROCURADOR_REPORT = "deudaProcuradorReport";
	// <--- Reporte de Deuda de Procurador
	public static final String METHOD_SEL_ALM_PARAMETROS_PARAM_PERSONA = "paramPersona";
	
	// ---> Consulta de cod ref pago
	public static final String FWD_CODREFPAG_SEARCHPAGE = "codRefPagSearchPage";
	public static final String FWD_CODREFPAG_ADAPTER = "codRefPagAdapter";
	
	// ---> Proceso Prescripcion de Deuda
	public static final String ACTION_BUSCAR_PROPREDEU    	 	= "buscarProPreDeu";
	public static final String ACTION_ADMINISTRAR_PROPREDEU    	= "administrarProPreDeu";
	public static final String FWD_PROPREDEU_SEARCHPAGE 		= "proPreDeuSearchPage";
	public static final String FWD_PROPREDEU_VIEW_ADAPTER 		= "proPreDeuViewAdapter";
	public static final String FWD_PROPREDEU_EDIT_ADAPTER 		= "proPreDeuEditAdapter";
	
	public static final String ACT_ADM_PROCESO_PRESCRIPCION_DEUDA = "administrarProceso";
	public static final String FWD_PROCESO_PRESCRIPCION_DEUDA_ADAPTER = "procesoPrescripcionDeudaAdapter";
	public static final String ACTION_ADMINISTRAR_PROCESO_PRESCRIPCION_DEUDA = "administrarProcesoPrescripcionDeuda";
	
	public static final String ACTION_BUSCAR_PROPREDEUDET  = "buscarProPreDeuDet";
	public static final String FWD_PROPREDEUDET_SEARCHPAGE = "proPreDeuDetSearchPage";
	// <--- Proceso Prescripcion de Deuda
	
	// ---> Acciones masivas a convenios
	public static final String FWD_ACCION_CONVENIO_ADAPTER = "accionConvenioAdapter";
	
	// --> Declaraciones juradas
	public static final String FWD_DECJUD_SEARCHPAGE = "decJurSearchPage";
	
	// ---> DecJur
	public static final String FWD_DECJUR_EDIT_ADAPTER  = "decJurEditAdapter";
	public static final String FWD_DECJUR_VIEW_ADAPTER  = "decJurViewAdapter";
	public static final String ACTION_ADMINISTRAR_DECJUD="administrarDecJur";
	public static final String ACTION_ADMIINSTRARDECJURDET="administrarDecJurDet";
	public static final String PATH_ADMINISTRAR_DECJUR   = "/gde/AdministrarDecJur.do?method=inicializar";
	public static final String ACTION_ADMIINSTRARDECJURPAG="administrarDecJurPag";
	// <--- DecJur
	
	// ---> DecJurDet
	public static final String FWD_DECJURDET_EDIT_ADAPTER  = "decJurDetEditAdapter";
	public static final String FWD_DECJURDET_VIEW_ADAPTER  = "decJurDetViewAdapter";
	
	// <--- DecJurDet
	
	// ---> DecJurPag
	public static final String FWD_DECJURPAG_EDIT_ADAPTER  = "decJurPagEditAdapter";
	public static final String FWD_DECJURPAG_VIEW_ADAPTER  = "decJurPagViewAdapter";
	
	// <--- DecJurPag
	
	// ---> Desglose Ajuste
	public static final String FWD_DESGLOSEAJUSTE_ADAPTER ="desgloseAjusteAdapter";
	// <--- Desglose Ajuste
	
	// ---> Multas
	public static final String FWD_MULTA_VIEW_ADAPTER  = "multaViewAdapter";
	public static final String FWD_MULTA_EDIT_ADAPTER  = "multaEditAdapter";
	public static final String FWD_MULTA_SEARCHPAGE = "multaSearchPage";
	public static final String PATH_BUSCAR_MULTA		= "/gde/BuscarMulta";
	public static final String ACTION_ADMINISTRAR_MULTA = "administrarMulta";
	public static final String PATH_ADM_MULTA_REFILL = "/gde/AdministrarMulta";
	public static final String FWD_MULTA_ADAPTER="multaAdapter";
	public static final String FWD_MULTAHISTORICO_ADAPTER="multaHistoricoAdapter";
	// <--- Multas
	
	// ---> Mandatario
	public static final String FWD_MANDATARIO_VIEW_ADAPTER = "mandatarioViewAdapter";
	public static final String FWD_MANDATARIO_EDIT_ADAPTER = "mandatarioEditAdapter";
	public static final String ACTION_ADMINISTRAR_MANDATARIO = "administrarMandatario";
	public static final String FWD_MANDATARIO_SEARCHPAGE = "mandatarioSearchPage";
	// <--- Mandatario
	
	// ---> Cobranza
	public static final String FWD_COBRANZA_SEARCHPAGE="cobranzaSearchPage";
	public static final String ACTION_ADMINISTRAR_COBRANZA="administrarCobranza";
	public static final String FWD_COBRANZA_ADAPTER="cobranzaEditAdapter";
	public static final String PATH_COBRANZA_ADAPTER="/gde/AdministrarCobranza";
	public static final String PATH_BUSCAR_COBRANZA="/gde/BuscarCobranza";
	public static final String FWD_COBRANZA_VIEW_ADAPTER="cobranzaViewAdapter";
	public static final String FWD_COBRANZA_ASIGN_ADAPTER="cobranzaAsignarAdapter";
	public static final String METHOD_COBRANZA_PARAM_PERSONA = "paramPersona";
	//<--- Cobranza
	
	// ---> PerCob
	public static final String FWD_PERCOB_SEARCHPAGE = "perCobSearchPage";
	public static final String ACTION_ADMINISTRAR_PERCOB = "administrarPerCob";
	public static final String FWD_PERCOB_VIEW_ADAPTER = "perCobViewAdapter";
	public static final String FWD_PERCOB_EDIT_ADAPTER = "perCobEditAdapter";
	
	// <--- PerCob
	
	// <--- AgeRet
	public static final String FWD_AGERET_SEARCHPAGE = "ageRetSearchPage";
	public static final String ACTION_ADMINISTRAR_AGERET = "administrarAgeRet";
	public static final String FWD_AGERET_VIEW_ADAPTER = "ageRetViewAdapter";
	public static final String FWD_AGERET_EDIT_ADAPTER = "ageRetEditAdapter";
	public static final String FWD_AGERET_ADAPTER = "ageRetAdapter";
	// ---> AgeRet

	// <--- TipoMulta
	public static final String FWD_TIPOMULTA_SEARCHPAGE="tipoMultaSearchPage";
	public static final String ACTION_ADMINISTRAR_TIPOMULTA="administrarTipoMulta";
	public static final String FWD_TIPOMULTA_VIEW_ADAPTER = "tipoMultaViewAdapter";
	public static final String FWD_TIPOMULTA_EDIT_ADAPTER="tipoMultaEditAdapter";
	public static final String FWD_TIPOMULTA_ADAPTER = "tipoMultaAdapter";
	// <--- TipoMulta
	
	// <--- IndiceCompensacion
	public static final String FWD_INDICECOMPENSACION_SEARCHPAGE="indiceCompensacionSearchPage";
	public static final String ACTION_ADMINISTRAR_INDICECOMPENSACION="administrarIndiceCompensacion";
	public static final String FWD_INDICECOMPENSACION_VIEW_ADAPTER = "indiceCompensacionViewAdapter";
	public static final String FWD_INDICECOMPENSACION_EDIT_ADAPTER="indiceCompensacionEditAdapter";
	public static final String FWD_INDICECOMPENSACION_ADAPTER = "indiceCompensacionAdapter";
	// <--- IndiceCompensacion	

	// --> Declaracion Jurada de Entradas Vendidas para Habilitacion
	public static final String ACTION_DDJJ_ENTVEN  	= "administrarDDJJEntVen";
	// <-- Declaracion Jurada de Entradas Vendidas para Habilitacion
	
	// --> Ver Novedades de Regimen Simplificado
	public static final String ACTION_VER_NOVEDADES_RS 	= "buscarNovedadRS";
	// <-- Ver Novedades de Regimen Simplificado

	public static final String FWD_SIMACTUALIZADEU_VIEW_ADAPTER = "simActualizaDeuViewAdapter";
	
	public static final String FWD_CTRLINFDEU_VIEW_ADAPTER = "ctrlInfDeuViewAdapter";
	
	// <--- TipoPago
	public static final String FWD_TIPOPAGO_SEARCHPAGE="tipoPagoSearchPage";
	public static final String ACTION_ADMINISTRAR_TIPOPAGO="administrarTipoPago";
	public static final String FWD_TIPOPAGO_VIEW_ADAPTER = "tipoPagoViewAdapter";
	public static final String FWD_TIPOPAGO_EDIT_ADAPTER="tipoPagoEditAdapter";
	public static final String FWD_TIPOPAGO_ADAPTER = "tipoPagoAdapter";
	// <--- TipoPago
}
