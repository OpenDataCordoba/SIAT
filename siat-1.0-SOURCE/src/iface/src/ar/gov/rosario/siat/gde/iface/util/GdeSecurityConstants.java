//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.util;


/**
 * Constantes de seguridad del modulo Gestion Deuda
 * 
 * @author Tecso
 * 
 */
public class GdeSecurityConstants {
	
	// ---> ABM Deuda Contribuyente
	public static final String ABM_DEUDA_CONTRIB    = "ABM_DeudaContrib";
	
	public static final String MTD_LIQUIDACION_DEUDA = "liquidacionDeuda";
	public static final String MTD_ESTADO_CUENTA     = "estadoCuenta";
	public static final String MTD_IMPRIMIR_LIST_DEUDA_CONTRIB  = "imprimirListDeudaContrib";
	public static final String MTD_AGREGAR_CUENTA = "agregarCuenta";
	// <--- ABM Deuda Contribuyente
	
	// --- > Liquidacion de Deuda
	public static final String LIQ_DEUDA = "LiquidacionDeuda";
	public static final String ABM_LIQDETALLEDEUDA = "DetalleDeuda";
	public static final String LIQ_DEUDA_RECLAMO_ACENTAMIENTO = "ReclamarAcentamiento";
	public static final String LIQ_CODREFPAG = "ConsultaCodRefPag";
	public static final String MTD_MODIFICAR_DEUDA = "modificarDeuda";
	
	// Permisos para los links
	public static final String MTD_VER_DETALLE_OBJIMP = "verDetalleObjImp";    // Permiso para ver el Detalle del Objeto Imponible
	public static final String MTD_VER_DETALLE_HISTORICOCONTRIB = "verHistoricoContrib"; // Permiso para ver el Historico de los Contribuyentes de la cuenta
	public static final String MTD_VER_DEUDA_CONTRIB = "verDeudaContrib"; // Poder ver el resto de las cuentas de un contribuyente  
	public static final String MTD_VER_CUENTA_DESG_UNIF = "verCuentaDesgUnif"; //Poder ver desgloses y unificaciones
	public static final String MTD_VER_CUENTA_REL =  "verCuentaRel"; // Ver Cuentas relacionadas por mismo Objeto Impsible 
	public static final String MTD_VER_CONVENIO = "verConvenio";  // Poder ver detalles de convenios
	public static final String MTD_VER_DETALLE_DEUDA = "verDetalleDeuda"; // Poder ver el detalle de los conceptos, pagos, etc de la deuda
	public static final String MTD_BUZON_CAMBIOS = "buzonCambios";  // Buzon de Cambios de datos de Persona
	public static final String MTD_VER_HISTORICO_EXENCION = "verHistoricoExe"; // Poder ver historico de Solicitudes de Execcion
	public static final String MTD_VER_PLANILLA = "verPlanilla"; // Poder ver planillas
	
	public static final String MTD_VER_CASO = "verCaso"; // Poder ver detalles de casos: Expedientes, notas, etc.
	
		// Permisos para las Acciones sobre registros de Deuda
	public static final String MTD_SELECT_DEUDA = "selectDeuda"; //  Permite seleccinar el registro de deuda para su operacion 
	public static final String MTD_RECLAMAR_ACENT = "reclamarAcent"; // Permite ir a la pantalla de Reclamar asentamiento
	
	// Permisos para acciones sobre todo el adapter de la liquidacion
	public static final String MTD_RECONFECCIONAR = "reconfecionar";
	public static final String MTD_RECONFECCIONAR_ESP = "reconfeccionarEsp";
	public static final String MTD_FORMALIZAR_CONVENIO = "formalizarConvenio"; // Formalizacion de Convenio de Pago 
	public static final String MTD_FORMALIZAR_CONVENIO_ESP = "formalizarConvenioEspecial"; // Formalizacion de Convenio de Pago Especial
	public static final String MTD_IMPRIMIR_INFORME_DEUDA = "imprimirInformeDeuda";  // Imprimir informe de deuda y planes
	public static final String MTD_VOLANTEPAGOINTRS = "volantePagoIntRS"; // Generacion de Volante de Pago de Intereses para Regimen Simplificado DREI/ETUR
		
	public static final String MTD_CAMBIOPLAN_CDM = "cambioPlanCDM";
	public static final String MTD_CUOTASALDO_CDM = "cuotaSaldoCDM";
	
	public static final String MTD_INFORME_ESCRIBANO = "infDeudaEscribano";

	public static final String MTD_DESGLOSAR_AJUSTE = "desglosarAjuste";
	
	public static final String MTD_DDJJENTVEN_HAB = "ddjjEntVen";
	
	public static final String MTD_VER_NOVEDADES_RS = "verNovedadesRS";
	
	public static final String MTD_CIERRE_COMERCIO = "cierreComercio";
	public static final String MTD_IMPRIMIR_CIERRE_COMERCIO = "imprimirCierreComercio";
		// Motivo Cierre
	public static final String ABM_MOTIVOCIERRE = "motivoCierre";
	
		// Cierre Comercio
	public static final String CIERRE_COMERCIO = "cierreComercio";
	
  	   // Declaracion Jurada
	public static final String MTD_DECLARACION_JURADA_MAS = "declaracionJuradaMasiva"; // metodo
	public static final String DECLARACION_JURADA_MAS = "DeclaracionJuradaMasiva";   // accion
	public static final String MTD_EDIT_PAGO = "editPago"; // metodo
	//	<--- Fin Liquidacion de Deuda
	
	// ---> Convenio Cuenta
	public static final String LIQ_CONVENIOCUENTA = "ConvenioCuenta"; // Detalle del convenio de una cuenta.
	
	public static final String MTD_SELECT_CUOTA = "selectCuota"; //  Permite seleccionar el registro de Cuota de Convenio
	public static final String MTD_IMPRIMIR_FORM_CONVENIO = "imprimirFormularioConvenio"; // Impresion Formulario Convenio de Pago
	public static final String MTD_IMPRIMIR_RECIBOS_CONVENIO = "imprimirRecibos"; // Impresion Cuota Convenio de Pago (Reconfeccion o no)
	public static final String MTD_GENERAR_CUOTA_SALDO = "generarCuotaSaldo";
	public static final String MTD_GENERAR_SALDO_X_CADUCIDAD = "generarSaldoPorCaducidad"; 
	public static final String MTD_ATRAS_SALDO_X_CADUCIDAD = "atrasSaldoPorCaducidad";
	public static final String MTD_APLICAR_PAGO_ACUENTA = "aplicarPagoACuenta";
	public static final String MTD_GENERAR_RESCATE_IND = "generarRescateInd";
	public static final String MTD_ANULAR_CONVENIO = "anularConvenio";
	public static final String MTD_VERIFICAR_CONSIST_CONV = "verificarConsistencia";
	// <--- Fin ConvenioCuenta
	
	// ---> ABM Envio Judicial
	public static final String ABM_PROCESO_MASIVO = "ABM_ProcesoMasivo";
	public static final String MTD_ADM_PROCESO    = "administrarProceso";
	// <--- ABM Envio Judicial
	
	// ---> Administrar Proceso Envio Judicial
	public static final String ABM_PROCESO_PROCESO_MASIVO    = "ABM_ProcesoProcesoMasivo";
	public static final String MTD_MODIFICAR_PROCESO_MASIVO  = "modificarProcesoMasivo";
	
	public static final String MTD_SELECCIONAR_DEUDA_ENVIAR       = "seleccionarDeudaEnviar";
	public static final String MTD_ELIMINAR_DEUDA_ENVIAR          = "eliminarDeudaEnviar";
	public static final String MTD_LIMPIAR_SELECCION_DEUDA_ENVIAR = "limpiarSeleccionDeudaEnviar";
	public static final String MTD_LOGS_ARMADO_DEUDA_ENVIAR       = "logsArmadoDeudaEnviar";
	public static final String MTD_PLANILLA_DEUDA_ENVIAR          = "planillaDeudaEnviar";
	public static final String MTD_PLANILLA_CONVENIO_CUOTA_ENVIAR = "planillaConvenioCuotaEnviar";
	public static final String MTD_CONSULTAR_DEUDA_ENVIAR         = "consultarDeudaEnviar";
	
	public static final String ABM_PROMASPROEXC  = "ABM_EnvJudProExc";
	
	public static final String MTD_VER_REPORTES_DEUDA = "verReportesDeuda";
	// <--- Administrar Proceso
	
	// ---> ABM Plan 
	public static final String ABM_PLAN = "ABM_Plan";
	public static final String ABM_PLAN_ENC = "ABM_PlanEnc";
	public static final String ABM_PLANCLADEU = "ABM_PlanClaDeu";
	public static final String ABM_PLANMOTCAD = "ABM_PlanMotCad";
	public static final String ABM_PLANFORACTDEU = "ABM_PlanForActDeu";
	public static final String ABM_PLANDESCUENTO = "ABM_PlanDescuento";
	public static final String ABM_PLANRECURSO = "ABM_PlanRecurso";
	public static final String ABM_PLANINTFIN = "ABM_PlanIntFin";
	public static final String ABM_PLANVEN = "ABM_PlanVen";
	public static final String ABM_PLANATRVAL = "ABM_PlanAtrVal";	
	public static final String ABM_PLANEXE = "ABM_PlanExe";
	public static final String ADM_PLANPRORROGA = "ADM_PlanProrroga";
	public static final String ABM_PLANIMPMIN = "ABM_PlanImpMin";
	// <--- ABM Plan
	
	// ---> ABM Convenio (Consultar convenio)
	public static final String ABM_CONVENIO = "ABM_Convenio";
	// <--- ABM Convenio (Consultar convenio)

	// ---> ABM Descuentos Generales
	public static final String ABM_DESGEN = "ABM_DesGen";
	// <--- ABM Descuentos Generales

	// ---> ABM Descuentos Especiales	
	public static final String ABM_DESESP = "ABM_DesEsp";
	public static final String ABM_DESESP_ENC = "ABM_DesEspEnc";
	public static final String ABM_DESRECCLADEU = "ABM_DesRecClaDeu";
	public static final String ABM_DESATRVAL = "ABM_DesAtrVal";
	public static final String ABM_DESESPEXE = "ABM_DesEspExe";
	// <--- ABM Descuentos Especiales	

	// ---> ABM Procurador
	public static final String ABM_PROCURADOR = "ABM_Procurador";
	public static final String ABM_PROCURADOR_ENC = "ABM_ProcuradorEnc";
 	public static final String ABM_PROREC = "ABM_ProRec";
 	public static final String ABM_PROREC_ENC = "ABM_ProRecEnc";
 	public static final String ABM_PRORECDESHAS = "ABM_ProRecDesHas";
 	public static final String ABM_PRORECCOM = "ABM_ProRecCom";
	// <--- ABM Procurador
 	
	// ---> ABM Seleccion Almacenada
 	public static final String ABM_SEL_ALM = "ABM_SelAlm";
 	public static final String MTD_AGREGAR_PARAMETROS = "agregarParametros";
	// <--- ABM Seleccion Almacenada
 	
 	// ---> Administrar Planillas Deuda Envio Judicial
 	public static final String ADM_PLANILLA_DEU_PRO_MAS = "ADM_PlanillasDeudaJudicial";
 	public static final String MTD_RECOMPONER_PLANILLA = "recomponerPlanilla";
 	public static final String MTD_HABILITAR_PLANILLA = "habilitarPlanilla";
 	public static final String MTD_VER_CONSTANCIAS = "verConstancias";
 	public static final String MTD_VER_LISTADO_CONSTANCIAS = "verListadoConstancias";
 	public static final String MTD_HABILITAR_CONSTANCIAS_DEUDA = "habilitarConstanciasDeuda";
 	// <--- Administrar Planillas Deuda Envio Judicial 	

 	//  ---> ABM Evento
	public static final String ABM_EVENTO = "ABM_Evento";
	public static final String ABM_EVENTO_ENC = "ABM_EventoEnc";
 	//  <--- ABM Evento

 	// ---> Adm. Constancias de Deuda	
	public static final String ADM_CONSTANCIA_DEUDA_JUDICIAL = "ADM_ConstanciasDeudaJudicial";
	public static final String MTD_RECOMPONER_CONSTANCIA = "recomponer";
	public static final String MTD_TRASPASAR_CONSTANCIA = "traspasar";
	public static final String MTD_HABILITAR_CONSTANCIA = "habilitar";
	public static final String MTD_IMPRIMIR_CONSTANCIA = "impresionConstancia";
	public static final String MTD_ANULAR_CONSTANCIA = "anular";
	public static final String ADM_TITULARES_CONSTANCIA_DEUDA_JUDICIAL = "ADM_titConDeuJud";
	public static final String ABM_CONDEUDET = "ABM_ConDeuDet";
 	// <--- Adm. Constancias de Deuda

	// ---> Deuda Judicial Sin Constancia
	public static final String ABM_DEUJUDSINCONSTANCIA = "ABM_DeuJudSinConstancia";
	// <--- Deuda Judicial Sin Constancia
	
	// ---> ABM Traspaso Devolucion Deuda unificado
	public static final String ABM_TRASPASO_DEVOLUCION_DEUDA     = "ABM_TraspasoDevolucionDeuda";
	public static final String ABM_TRASPASO_DEVOLUCION_DEUDA_ENC = "ABM_TraspasoDevolucionDeudaEnc";
	
	public static final String ABM_TRADEVDEUDET     = "ABM_TraDevDeuDet"; // unifica manejo de detalles de traspasos y devoluciones
	// <--- ABM Traspaso Devolucion Deuda
	
	// ---> Consultar Cuenta por Objeto Imponible
	public static final String ABM_CUENTA_OBJIMP = "ABM_CuentaObjImp";
	// <--- Consultar Cuenta por Objeto Imponible

	// ---> Consultar Cuentas por Procurador
	public static final String CONSULTAR_CUENTAS_PROCURADOR = "consultarCuentasProcurador";
	// <--- Consultar Cuentas por Procurador
	
	// ---> ADM Gestion Judicial
	public static final String ADM_GESJUD = "ADM_GesJud";
	public static final String ADM_GESJUD_ENC = "ADM_GesJudEnc";
	public static final String REG_CADUCIDAD_GESJUD = "registrarCaducidad";
	
	public static final String ABM_GESJUDDEU = "ABM_GesJudDeu";
	public static final String ABM_GESJUDEVENTO = "ABM_GesJudEvento";
	// <--- ADM Gestion Judicial	

	// ---> ABM Rescate
	public static final String ABM_RESCATE       = "ABM_Rescate";
	public static final String MTD_ADM_SELECCION = "administrarSeleccion";
	// <--- ABM Rescate
	
	// ---> ABM Saldo por Caducidad
	public static final String ABM_SALDOPORCADUCIDAD = "ABM_SaldoCaducidad";
	// <--- ABM Saldo por Caducidad
	
	// ---> ABM ConvenioReport
	public static final String ABM_CONVENIO_REPORT         = "ABM_Convenio_Report";
	public static final String ABM_CONVENIO_CADUCAR_REPORT = "ABM_Convenio_Caducar_Report";
	public static final String ABM_CONVENIO_FORM_REPORT    = "ABM_Convenio_Form_Report";
	// <--- ABM ConvenioReport
	
	// ---> ABM RecaudacionReport
	public static final String ABM_RECAUDACION_REPORT         = "ABM_Recaudacion_Report";
	// <--- ABM RecaudacionReport
	
	// ---> Consultar Estado Cuenta 
	public static final String CONSULTAR_ESTADOCUENTA = "Consultar_EstadoCuenta";
	// <--- Consultar Estado Cuenta
	
	
	// ---> Informe de Deuda Escribano 
	public static final String INFORME_DEUDA_ESCRIBANO = "InfDeuEsc";
	public static final String ABM_TRAMITE = "ABM_Tramite";
	// <--- Informe de Deuda Escribano

	// ---> ABM LiqCom
	public static final String ABM_LIQCOM = "ABM_LiqCom";	
	// <--- ABM LiqCom	

	// ---> Consultar convenios/recibos no liquidables
	public static final String CONSULTAR_CONRECNOLIQ = "Consultar_convenio_recibo_noLiq";
	public static final String MTD_CONRECNOLIQ_PROCESAR = "procesarConRecNoLiq";
	public static final String MTD_CONRECNOLIQ_VOLVER_LIQUIDABLE = "volverLiquidableConRecNoLiq";
	// <--- Consultar convenios/recibos no liquidables
	
	// ---> Anular/Desanular Deuda
	public static final String ADM_ANULARDEUDA = "ADM_AnularDeuda";
	// <--- Anular/Desanular Deuda

	// ---> Consultar Importe a Recaudar
	public static final String CONSULTAR_IMPORTE_RECAUDAR = "Consultar_Importe_Recaudar";
	// <--- Consultar Importe a Recaudar
	
	
	// ---> Consulta de Recibos
	public static final String CONSULTAR_RECIBO = "Consultar_Recibos";
	// <--- Fin Consulta de Recibos

	// ---> Consultar Importe Recaudado
	public static final String CONSULTAR_IMPORTE_RECAUDADO_PLANES = "Consultar_Importe_Recaudado_planes";
	// <--- Consultar Importe Recaudado	

	// ---> Consultar Respuesta Operativos
	public static final String CONSULTAR_RESPUESTA_OPERATIVOS = "Consultar_Respuesta_Operativos";
	// <--- Consultar Respuesta Operativos	

	// ---> Consultar Convenio A Caducar
	public static final String CONSULTAR_CONVENIO_A_CADUCAR = "Consultar_Convenio_A_Caducar";
	// <--- Consultar Convenio A Caducar	

	// ---> Carga de eventos de gestion Judicial por archivo
	public static final String UPLOAD_EVENTO_GESJUD = "uploadAventoGesjud";
	public static final String MTD_UPLOAD = "upload";
	// <--- Carga de eventos de gestion Judicial por archivo

	// ---> Reportes de Seguimiento de las Gestión Judicial
	public static final String ABM_GEJUD_REPORT = "ABM_GesJudReport";
	// <--- Reportes de Seguimiento de las Gestión 

	// ---> Reporte de deudas de cuenta en gestion judicial
	public static final String CONSULTAR_DEUCUEGESJUD = "Consultar_DeuCueGesJud";
	// <--- Reporte de deudas de cuenta en gestion judicial	

	// ---> Deuda Anulada Report
	public static final String DEUDA_ANULADA_REPORT = "Reporte_DeduaAnulada";
	// <--- Deuda Anulada Report
	
		// ---> reporte de total de emision
	public static final String EMISION_REPORT = "Reporte_Emision";

	
	// ---> Reporte de Recaudado NUEVO
	public static final String RECAUDADO_REPORT = "Reporte_Recaudado";
	// <--- Reporte de Recaudado NUEVO
	
	// ---> Reporte de Total contribuyente Cer
	public static final String CONTRIBUYENTECER_REPORT = "Reporte_ContribuyenteCer";
	// <--- Reporte de Total contribuyente Cer
	
	// ---> Reporte de Total contribuyente Cer X recurso
	public static final String RECCONCER_REPORT = "Reporte_RecConCer";
	// <--- Reporte de Total contribuyente Cer X recurso
	
	// ---> Reporte de Total Recaudación Cer 
	public static final String RECAUDACIONCER_REPORT = "Reporte_RecaudacionCer";
	// <--- Reporte de Total Recaudación Cer 
	
	// ---> Reporte de Total Detalle Recaudación Cer 
	public static final String DETRECAUDACIONCER_REPORT = "Reporte_DetRecaudacionCer";
	// <--- Reporte de Total Detalle Recaudación Cer 
	
	
	// ---> Reporte de Distribucion de Total Emitido
	public static final String DISTRIBUCION_REPORT = "Reporte_Distribucion";
	// <--- Reporte de Distribucion de Total Emitido

	// ---> Reporte de Deuda de Procurador
	public static final String DEUDA_PROCURADOR_REPORT = "Reporte_DeudaProcurador";
	// <--- Reporte de Deuda de Procurador

	// ---> ABM ProPreDeu: Proceso de Prescripcion de Deuda
	public static final String ABM_PROPREDEU = "ABM_ProPreDeu";
	public static final String ABM_PROPREDEU_ADM_PROCESO = "administrarProceso";
	// <--- ABM ProPreDeu: Proceso de Prescripcion de Deuda
	
	// ---> Acciones masivas a convenios
	public static final String ACCION_MASIVA_CONVENIOS="Accion_Masiva_a_Convenios";
	
	// ---> ABM Declaraciones Juradas
	public static final String ABM_DECJUR="Consultar_DecJur";
	public static final String ABM_DECJUR_VUELTA_ATRAS = "vueltaAtras";
	
	// ---> ABM TipoMulta
	public static final String ABM_TIPOMULTA="ABM_TipoMulta";
	
	// ---> ABM Multa
	public static final String ABM_MULTA="ABM_Multa";

	// ---> ABM IndiceCompensacion
	public static final String ABM_INDICECOMPENSACION="ABM_IndiceCompensacion";
	
	// ---> Desglose Ajuste
	public static final String MTD_DESGLOSE_AJUSTE = "desgloseAjuste";
	
	// ---> Multa
	public static final String ABM_Multa = "ABM_Multa";
	
	// ---> Descuento Multa
	public static final String ABM_DESCUENTOMULTA = "ABM_DescuentoMulta";
	
	// ---> MultaDet
	public static final String ABM_MULTADET = "ABM_MultaDet";
	
	public static final String IMPRIMIR = "imprimir";
	
	// ---> Mandatario
	public static final String ABM_MANDATARIO = "ABM_Mandatario";
	// <--- Mandatario
	
	// ---> ABM Cobranza
	public static final String ABM_COBRANZA = "ABM_Cobranza";
	

	// --> ABM PerCob
	public static final String ABM_PERCOB = "ABM_PerCob";
	// <--- ABM PerCob

	// --> ABM AgeRet
	public static final String ABM_AGERET = "ABM_AgeRet";
	// <--- ABM AgeRet	

	// --> ABM Simulacion actulizacion de deuda
	public static final String ABM_SIMACTUALIZADEU = "ABM_SimActualizaDeu";
	// <-- ABM Simulacion actulizacion de deuda
	
	//	--> ABM Desbloquear CtrlInfDeu
	public static final String ABM_CTRLINFDEU = "ABM_CtrlInfDeu";
	// <-- ABM Desbloquear CtrlInfDeu
	
	// ABM TipoPago
	public static final String ABM_TIPOPAGO = "ABM_TipoPago";
}
