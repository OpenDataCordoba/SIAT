//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class GdeError extends DemodaError {
	// Use Codigos desde 10000 hasta 10999
    // static public String XXXXXX_XXXX_XXX   = addError(10000, "gde.xxxxxx");
	
	//	 ---> ABM Servicio Banco	
	// Servicio Banco Descuentos Generales
	public static final String SERBANDESGEN_LABEL        	= addError(10000, "gde.serBanDesGen.label");
	public static final String SERBANDESGEN_FECHADESDE  	= addError(10001, "gde.serBanDesGen.fechaDesde.label");
	public static final String SERBANDESGEN_FECHAHASTA  	= addError(10002, "gde.serBanDesGen.fechaHasta.label");
	public static final String SERBANDESGEN_DESGEN  		= addError(10003, "gde.serBanDesGen.desDesGen.label");
	public static final String SERBANDESGEN_SERVICIOBANCO 		= addError(10004, "gde.serBanDesGen.servicioBanco.label");	
	//	 <--- ABM Servicio Banco

	// ---> CRITERIO ASIGNACION PROCURADORES
	public static final String CRIASIPRO_LABEL        	= addError(10050, "gde.criAsiPro.label");
	public static final String CRIASIPRO_DESCRIASIPRO  	= addError(10051, "gde.criAsiPro.desCriAsiPro.label");
	//	<--- CRITERIO ASIGNACION PROCURADORES

	// --> Procurador
	public static final String PROCURADOR_LABEL        	  = addError(10100, "gde.procurador.label");
	public static final String PROCURADOR_PERSONA  	      = addError(10101, "gde.procurador.persona.label");
	public static final String PROCURADOR_DESCRIPCION  	  = addError(10102, "gde.procurador.descripcion.label");
	public static final String PROCURADOR_DOMICILIO  	  = addError(10103, "gde.procurador.domicilio.label");
	public static final String PROCURADOR_TELEFONO  	  = addError(10104, "gde.procurador.telefono.label");
	public static final String PROCURADOR_HORARIOATENCION = addError(10105, "gde.procurador.horarioAtencion.label");
	public static final String PROCURADOR_TIPOPROCURADOR  = addError(10106, "gde.procurador.tipoProcurador.label");
	public static final String PROCURADOR_OBSERVACION     = addError(10107, "gde.procurador.observacion.label");
	// <-- Procurador
	
	
	// ---> ABM Envio Judicial	
	public static final String PROCESO_MASIVO_LABEL            = addError(10110, "gde.procesoMasivo.label");
	public static final String PROCESO_MASIVO_FECHA_ENVIO      = addError(10111, "gde.procesoMasivo.fechaEnvio.label");
	public static final String PROCESO_MASIVO_RECURSO          = addError(10112, "gde.procesoMasivo.recurso.label");
	public static final String PROCESO_MASIVO_OBSERVACION      = addError(10113, "gde.procesoMasivo.observacion.label");
	public static final String PROCESO_MASIVO_SELALMINC        = addError(10114, "gde.procesoMasivo.selAlmInc.label");
	public static final String PROCESO_MASIVO_SELALMEXC        = addError(10115, "gde.procesoMasivo.selAlmExc.label");
	public static final String PROCESO_MASIVO_UTILIZACRITERIO  = addError(10116, "gde.procesoMasivo.utilizaCriterio.label");
	public static final String PROCESO_MASIVO_PROCURADOR       = addError(10117, "gde.procesoMasivo.procurador.label");
	public static final String PROCESO_MASIVO_CONCUENTAEXCSEL  = addError(10118, "gde.procesoMasivo.conCuentaExcSel.label");
	public static final String PROCESO_MASIVO_CASO             = addError(10119, "gde.procesoMasivo.caso.label");
	public static final String PROCESO_MASIVO_ESVUELTAATRAS    = addError(10120, "gde.procesoMasivo.esVueltaAtras.label");
	public static final String PROCESO_MASIVO_PROCESOMASIVO    = addError(10121, "gde.procesoMasivo.procesoMasivo.label");
	public static final String PROCESO_MASIVO_CORRIDA          = addError(10122, "gde.procesoMasivo.corrida.label");
	public static final String PROCESO_MASIVO_USUARIOALTA      = addError(10123, "gde.procesoMasivo.usuarioAlta.label");
	public static final String PROCESO_MASIVO_TIPPROMAS        = addError(10124, "gde.procesoMasivo.tipProMas.label");
	public static final String PROCESO_MASIVO_VIADEUDA         = addError(10125, "gde.procesoMasivo.viaDeuda.label");
	public static final String PROCESO_MASIVO_RECURSOS_ESTADOSCORRIDA  = addError(10126, "gde.procesoMasivo.recursosEstadosCorrida.label");
	public static final String PROCESO_MASIVO_SEARCHPAGE_FECHAENVIODESDE = addError(10127, "gde.procesoMasivoSearchPage.fechaEnvioDesde.label");
	public static final String PROCESO_MASIVO_SEARCHPAGE_FECHAENVIOHASTA = addError(10128, "gde.procesoMasivoSearchPage.fechaEnvioHasta.label");
	public static final String PROCESO_MASIVO_MODIFICAR_NO_PERMITIDO     = addError(10129, "gde.procesoMasivo.modificarNoPermitido");
	public static final String PROCESO_MASIVO_ELIMINAR_NO_PERMITIDO      = addError(10130, "gde.procesoMasivo.eliminarNoPermitido");
	public static final String PROCESO_MASIVO_GENERACONSTANCIA = addError(0, "gde.procesoMasivo.generaConstancia.label");
	
	//ABM Agente de Retención 
	public static final String AGERET_LABEL      = addError(10131, "gde.ageret.label");
	public static final String AGERET_DESAGERET      = addError(10132, "gde.ageRet.desAgeRet.label");
	public static final String AGERET_CUIT      = addError(10133, "gde.ageRet.cuit.label");
	public static final String AGERET_FECHADESDE      = addError(10134, "gde.ageRet.fechaDesde.label");
	public static final String AGERET_FECHAHASTA      = addError(10135, "gde.ageRet.fechaHasta.label");
	
	
	// Errores de los procesos con formularios parametricos
	public static final String PROCESO_MASIVO_FOMULARIO =  addError(0, "gde.procesoMasivoAdapter.formulario.label");
	public static final String PROCESO_MASIVO_FORMATO_SALIDA =  addError(0, "gde.procesoMasivoAdapter.formatoSalida.label");
	
	// <--- ABM Envio Judicial
	
	// ---> Liquidacion de Deuda
	public static final String LIQRECLAMO_FECHAPAGO_FORMAT_ERROR = addError(0, "gde.liqReclamo.fechaPago.formatError");
	public static final String LIQRECLAMO_DEUDA_FECHAPAGO_MENORASENT_ERROR = addError(0, "gde.liqReclamoDedua.fechaPago.menorQueAsentamiento");
	public static final String LIQRECLAMO_CUOTA_FECHAPAGO_MENORASENT_ERROR = addError(0, "gde.liqReclamoCuota.fechaPago.menorQueAsentamiento");
	public static final String LIQRECLAMO_IMPORTEPAGADO_FORMAT_ERROR = addError(0, "gde.liqReclamo.importePagado.formatError");
	public static final String LIQRECLAMO_IMPORTEPAGADO_NEGATIVO_ERROR = addError(0, "gde.liqReclamo.importePagado.negativoError");
	public static final String LIQRECLAMO_BANCO_REQUIRED = addError(0, "gde.liqReclamo.banco.required");
	public static final String LIQRECLAMO_APELLIDO_REQUIRED = addError(0, "gde.liqReclamo.apellido.required");
	public static final String LIQRECLAMO_NOMBRE_REQUIRED = addError(0, "gde.liqReclamo.nombre.required");
	public static final String LIQRECLAMO_NRODOC_REQUIRED = addError(0, "gde.liqReclamo.nroDoc.required");
	public static final String LIQRECLAMO_NRODOC_FORMAT_ERROR = addError(0, "gde.liqReclamo.nroDoc.formatError");
	public static final String LIQRECLAMO_EMAIL_REQUIRED = addError(0, "gde.liqReclamo.email.required");
	public static final String LIQRECLAMO_EMAIL_FORMAT_ERROR = addError(0, "gde.liqReclamo.email.formatError");

	
	public static final String LIQRECONF_NO_HAY_DEUDAS_SELECCIONADAS = addError(0,"gde.reconfeccion.error.noHayDeudasSeleccionadas");
	public static final String LIQRECONF_FECHAVENCIMIENTO_ESP = addError(0,"gde.liqReconfeccionAdapter.fechaVencimientoEsp.label");
	public static final String LIQRECONF_FECHAACTUALIZACION_ESP = addError(0,"gde.liqReconfeccionAdapter.fechaActualizacionEsp.label");
	public static final String LIQRECONF_DEUDAS_NO_PERMITIDAS = addError(0,"gde.reconfeccion.error.deudasNoPermitidas");
	public static final String LIQRECONF_MSG_NO_PAGA_VENCIDO = addError(0,"gde.reconfeccion.msjNoPagaVencido");
	public static final String LIQRECONF_NO_DIAHABIL = addError(0,"gde.reconfeccion.error.noDiaHabil");
	public static final String LIQRECONF_FECHAVENCIMIENTOMENOR = addError(0,"gde.reconfeccion.fechaVencimiento.menorQueActual");
	public static final String LIQRECONF_TIPO_DECLARACION=addError(0,"gde.liqReconfeccionAdapter.tipoReconfeccion.label");
	public static final String LIQRECONF_TIPO_DECLARACION_INCORRECTO=addError(0, "gde.liqReconfeccionAdapter.tipoReconfeccion.error");
	public static final String LIQRECONF_DEUDAS_RESTRINGIDA = addError(0,"gde.reconfeccion.error.deudasRestringidas");
	
	public static final String LIQVOLANTEPAGOINTRS_NO_HAY_DEUDAS_SELECCIONADAS = addError(0,"gde.liqVolantePagoIntRSAdapter.error.noHayDeudasSeleccionadas");
	public static final String LIQVOLANTEPAGOINTRS_MAS_DE_UNA_DEUDAS_SELECCIONADA = addError(0,"gde.liqVolantePagoIntRSAdapter.error.masDeUnaDeuda");
	public static final String LIQVOLANTEPAGOINTRS_DEUDA_NO_RS_SELECCIONADA = addError(0,"gde.liqVolantePagoIntRSAdapter.error.deudaNoRS");
	public static final String LIQVOLANTEPAGOINTRS_FECHAPAGO = addError(0,"gde.liqVolantePagoIntRSAdapter.fechaPago.label");
	public static final String LIQVOLANTEPAGOINTRS_NO_DIAHABIL = addError(0,"gde.liqVolantePagoIntRSAdapter.error.noDiaHabil");
	public static final String LIQVOLANTEPAGOINTRS_DEUDAS_NO_PERMITIDAS = addError(0,"gde.liqVolantePagoIntRSAdapter.error.deudasNoPermitidas");
	public static final String LIQVOLANTEPAGOINTRS_DEUDA_RS_NO_PERMITIDA = addError(0,"gde.liqVolantePagoIntRSAdapter.error.deudaRSNoPermitida");
	
	public static final String MSG_DEUDA_NO_ASIGNADA_A_PROCURADOR = addError(0, "gde.liqDeudaAdapter.msgDeudaNoAsignadaAProcurador");
	
	public static final String MSG_DEUDA_EXENTA = "gde.deuda.msgExenta";
	public static final String MSG_DEUDA_INDETERMINADA = "gde.deuda.msgIndeterminada";
	public static final String MSG_DEUDA_RECLAMADA = "gde.deuda.msgReclamada";
	public static final String MSG_DEUDA_SIN_TITULARES_ACTIVOS = "gde.liqDeudaAdapter.msgDeudaSinTitulares";
	public static final String MSG_DEUDA_PAGA="gde.deuda.msgPaga";
	public static final String MSG_DEUDA_OSIRISPAGOPENDIENTE = "gde.deuda.msgOsirisPagoPendiente";
	
		// Formalizacion de Convenio de Pago: mensajes de deshabilitacion de planes.
	public static final String MSG_PLAN_DESHAB_X_EXENCION = "gde.liqFormConvenioAdapter.planDeshabXExencion";
	public static final String MSG_PLAN_DESHAB_X_TPO_DEU_VENCIDA = "gde.liqFormConvenioAdapter.planDeshabXTpoDeuVencida";
	public static final String MSG_PLAN_DESHAB_X_TPO_DEU_NO_VENCIDA = "gde.liqFormConvenioAdapter.planDeshabXTpoDeuNOVencida";
	public static final String MSG_PLAN_DESHAB_X_FECHAS_VTO_FUERA_RANGO = "gde.liqFormConvenioAdapter.planDeshabXFechasVtoFueraRango";
	public static final String MSG_PLAN_DESHAB_X_CLA_DEU = "gde.liqFormConvenioAdapter.planDeshabXClaDeu";
	public static final String MSG_PLAN_DESHAB_X_APLICA_TOT_IMPAGO = "gde.liqFormConvenioAdapter.planDeshabXAplicaTotImpago";
	public static final String MSG_PLAN_DESHAB_X_PERMISOS_ESPECIALES = "gde.liqFormConvenioAdapter.planDeshabXPermisosEspeciales";
	public static final String MSG_PLAN_DESHAB_X_IMP_MINIMO = "gde.liqFormConvenioAdapter.planDeshabXImpMinimo";
	public static final String MSG_PLAN_DESHAB_X_CANT_MIN_PERIODO = "gde.liqFormConvenioAdapter.planDeshabXCantMinPeriodos";
	public static final String MSG_PLAN_DESHAB_X_ATR_VAL = "gde.liqFormConvenioAdapter.planDeshabXAtrVal";
		
		// Formalizacion de Convenio de Pago: mensajes de validacion para formalizar el convenio 
	
	public static final String MSG_PLAN_REQUERIDO = addError(0, "gde.liqFormConvenioAdapter.planRequerido");
	public static final String MSG_NRO_CUOTA_REQUERIDO = addError(0, "gde.liqFormConvenioAdapter.nroCuotaRequerido");
	public static final String MSG_SELECT_DEUDA_REQUERIDO = addError(0, "gde.liqFormConvenioAdapter.noHayDeuda.title");
	public static final String MSG_SELECT_DEUDA_ENCONVENIO = addError(0, "gde.liqFormConvenioAdapter.deudaEnConvenio.title");
	public static final String MSG_SELECT_DEUDA_CERO = addError(0, "gde.liqFormConvenioAdapter.deudaSaldoCero.title");
	public static final String MSG_SELECT_DEUDA_DISTINTA_VIA = addError(0, "gde.liqFormConvenioAdapter.deduaDistintaVia.title");	
	public static final String MSG_VIA_DEUDA_NO_PERMITIDA_USR = addError(0, "gde.liqFormConvenioAdapter.viaDeudaNoPermitidaUsr");
	public static final String MSG_DEUDA_NO_PERMITIDA_USR = addError(0, "gde.liqFormConvenioAdapter.deudaNoPermitidaUsr");
	public static final String MSG_DEUDA_DISTINTO_PROCURADOR = addError(0, "gde.liqFormConvenioAdapter.deudasDistintoProcurador");
	
	
	public static final String MSG_CUOTA_INTERES_NULO = "gde.liqFormConvenioAdapter.interesNulo";
	public static final String MSG_INTERES_NULO = addError(0, "gde.liqFormConvenioAdapter.interesNulo");
	public static final String MSG_INTERES_MAL_FORMATO = addError(0, "gde.liqFormConvenioAdapter.interesMalFormato");
	public static final String MSG_VENCIMIENTO_NULO = addError(0, "gde.liqFormConvenioAdapter.vencimientoNulo");
	public static final String MSG_SERVICIOBANCO_NO_UNICO = addError(0, "gde.liqFormConvenioAdapter.servicioBancoNoUnico");
	public static final String MSG_PERSONA_JURIDICA = addError(0, "gde.liqFormConvenioAdapter.personaJuridica");
	
		// Cambio de Plan CdM
	public static final String MSG_PLANCDM_REQUERIDO = addError(0, "rec.cambioPlanCDMAdapter.planRequerido");
	public static final String MSG_DEUDA_VENCIDA = addError(0, "rec.cambioPlanCDMAdapter.poseeDeudaVencida");
	public static final String MSG_DEUDA_NO_VENCIDA = addError(0, "rec.cambioPlanCDMAdapter.poseeDeudaNoVencida");
	public static final String MSG_PLANCDM_DESHAB_X_EXENCION = "rec.cambioPlanCDMAdapter.planDeshabXExencion";
	public static final String MSG_PLANCDM_DESHAB_X_MONTOMINIMO = "rec.cambioPlanCDMAdapter.planDeshabXMontoMinimo";
	public static final String MSG_PLANCDM_DESHAB_X_OBRANOPLANMAY = "rec.cambioPlanCDMAdapter.planDeshabXObraNoPlanMay";
	
		// Cuota Saldo CdM
	public static final String MSG_CUOTASALDO_CDM_ANULACION_DEUDA = addError(0,"rec.cuotaSaldoCDMAdapter.msgAnulacionDeuda");
	public static final String MSG_CUOTASALDO_CDM_DEUDA_VENCIDA_USRANONIMO = addError(0, "rec.cuotaSaldoCDMAdapter.msgDeudaVencidaUsrAnonimo");
	public static final String MSG_CUOTASALDO_CDM_FALTAN_DATOS_OBRA = addError(0, "rec.cuotaSaldoCDMAdapter.msgDatosIncorrectos");
	
		// Desglose Ajuste
	public static final String MSG_DESGLOSE_AJUSTE_CLASIFICACION_DEUDA = addError(0,"gde.desgloseAjuste.msgValidacionClaDeu");
	public static final String DESGLOSE_AJUSTE_FECHA_LIMITE = addError(0,"gde.desgloseAjusteAdapter.fechaLimite.label");
	public static final String DESGLOSE_AJUSTE_FECHA_VENCIMIENTO = addError(0,"gde.desgloseAjusteAdapter.liqDeuda.fechaVencimiento");
	
		// Cierre Comercio
	public static final String MOTIVOCIERRE_DESMOTIVOCIERRE = addError(0,"gde.motivoCierre.desMotivoCierre.label");
	public static final String CIERRE_COMERCIO_FECHACESEACTIVIDAD = addError(0,"gde.cierreComercio.fechaCeseActividad.label");
	public static final String CIERRE_COMERCIO_MOTIVOCIERRE = addError(0,"gde.motivoCierre.label");
	public static final String CIERRE_COMERCIO_FECHAFALLECIMIENTOTITULAR = addError(0,"gde.cierreComercio.fechaFallecimientoTitular.label");
	public static final String MSG_NO_CONTINUAR_TRAMITE = addError(0,"gde.cierreComercio.noContinuarTramite");
	public static final String CIERRE_COMERCIO_CASO_NOEMIMUL= addError(0,"gde.cierreComercio.noEmiMul.label");
	public static final String CIERRE_COMERCIO_CASO= addError(0,"cas.caso.label");
	
	// <--- Liquidacion de Deuda

	// --> Seleccion almacen: agregacion de parametros en la busqueda de deuda
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTODESDE    = addError(10200, "gde.selAlmAgregarParametrosSearchPage.fechaVencimientoDesde.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOHASTA    = addError(10201, "gde.selAlmAgregarParametrosSearchPage.fechaVencimientoHasta.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVTOLIMITE   = addError(10202, "gde.selAlmAgregarParametrosSearchPage.fechaVencimientoLimite.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEHISTDESDE = addError(10203, "gde.selAlmAgregarParametrosSearchPage.importeHistoricoDesde.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEHISTHASTA = addError(10204, "gde.selAlmAgregarParametrosSearchPage.importeHistoricoHasta.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEACTDESDE  = addError(10205, "gde.selAlmAgregarParametrosSearchPage.importeActualizadoDesde.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_IMPORTEACTHASTA  = addError(10206, "gde.selAlmAgregarParametrosSearchPage.importeActualizadoHasta.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_APLIC_TOTAL_DEU  = addError(10207, "gde.selAlmAgregarParametrosSearchPage.aplicaAlTotalDeuda.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_DEUDA    = addError(10208, "gde.selAlmAgregarParametrosSearchPage.cantidadMinimaDeuda.label");
	
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_REG_EXCEDIDA        = addError(10209, "gde.selAlmAgregarParametrosSearchPage.ctdRegExcedida.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_DEUDA_MENOR = addError(10210, "gde.selAlmAgregarParametrosSearchPage.ctdMinDeudaMenor.label");
	
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_CUOTA    = addError(10211, "gde.selAlmAgregarParametrosSearchPage.cantidadMinimaCuota.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_CTD_MIN_CUOTA_MENOR = addError(10212, "gde.selAlmAgregarParametrosSearchPage.ctdMinCuotaMenor.label");
	public static final String SEL_ALM_AGR_PARAM_SEARCHPAGE_FECHAVENCIMIENTO    = addError(10213, "gde.selAlmAgregarParametrosSearchPage.fechaVencimiento.label");

	// <-- Seleccion almacen: agregacion de parametros en la busqueda de deuda
	
	// ---> ABM Proceso Envio Judicial
	// Deuda excluida
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTODESDE  = addError(10214, "gde.deudaExcProMasAgregarSearchPage.fechaVencimientoDesde.label");
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTOHASTA  = addError(10215, "gde.deudaExcProMasAgregarSearchPage.fechaVencimientoHasta.label");
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_FECHAVTOLIMITE = addError(10216, "gde.deudaExcProMasAgregarSearchPage.fechaVencimientoLimite.label");
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_NRO_CTA_FECHA  = addError(10217, "gde.deudaExcProMasAgregarSearchPage.nroCtaFecha.label");
	
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_FILTROS_REQUERIDOS  = addError(10218, "gde.deudaExcProMasAgregarSearchPage.filtrosRequeridos.label");
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_CTD_REG_EXCEDIDA    = addError(10218, "gde.deudaExcProMasAgregarSearchPage.ctdRegExcedida.label");
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_RESULTADO_VACIO     = addError(10219, "gde.deudaExcProMasAgregarSearchPage.resultadoVacio.label");
	public static final String DEUDA_EXC_PRO_MAS_SEARCHPAGE_DEUDAS_NO_SELECCIONADAS = addError(10219, "gde.deudaExcProMasAgregarSearchPage.deudasNoSeleccionadas.label");

	public static final String DEUDA_INC_PRO_MAS_SEARCHPAGE_DEUDAS_NO_SELECCIONADAS = addError(10219, "gde.deudaIncProMasAgregarSearchPage.deudasNoSeleccionadas.label");
	
	public static final String DEUDA_PRO_MAS_CONS_POR_CTA_SEARCHPAGE_CTA_NO_CORR_REC = addError(10219, "gde.deudaProMasConsPorCtaSearchPage.cuentaNoCorresponde.label");
	
	// <--- ABM Proceso Envio Judicial
	

	// ---> ABM Log Seleccion Almacenada
	public static final String SELALMLOG_SELALM     = addError(10220, "gde.selAlmLog.selAlm.label"); 
	public static final String SELALMLOG_ACCIONLOG  = addError(10221, "gde.selAlmLog.accionLog.label");
	public static final String SELALMLOG_DETALLELOG = addError(10222, "gde.selAlmLog.detalleLog.label");
	// <--- ABM Log Seleccion Almacenada
	
	// ---> ABM DesGen
	public static final String DESGEN_DESDESGEN = addError(0, "gde.desGen.desDesGen.label");
	public static final String DESGEN_LABEL = addError(0, "gde.desGen.label");
	public static final String DESGEN_LEYENDADESGEN = addError(0, "gde.desGen.leyendaDesGen.label");
	public static final String DESGEN_PORDES = addError(0, "gde.desGen.porDes.label");
	// <--- ABM DesGen
	
	// ---> ABM ProRec
	public static final String PROREC_LABEL = addError(0, "gde.proRec.label");
	public static final String PROREC_RECURSO_LABEL = addError(0, "gde.proRec.recurso.label");
	public static final String PROREC_FECHADESDE_LABEL = addError(0, "gde.proRec.fechaDesde.label");
	public static final String PROREC_FECHAHASTA_LABEL = addError(0, "gde.proRec.fechaHasta.label");
	public static final String PROREC_RECURSOASIGNADO_LABEL = addError(0,"gde.proRec.recursoAsignado.label");
	// 	<--- ABM ProRec
	
	// ---> ABM ProRecDesHas
	public static final String PRORECDESHAS_LABEL = addError(0, "gde.proRecDesHas.label");
	public static final String PRORECDESHAS_DESDE_LABEL = addError(0, "gde.proRecDesHas.desde.label");
	public static final String PRORECDESHAS_HASTA_LABEL = addError(0, "gde.proRecDesHas.hasta.label");
	public static final String PRORECDESHAS_FECHADESDE_LABEL = addError(0, "gde.proRecDesHas.fechaDesde.label");
	public static final String PRORECDESHAS_FECHAHASTA_LABEL = addError(0, "gde.proRecDesHas.fechaHasta.label");
	public static final String PRORECDESHAS_RANGOASIGNADO_LABEL = addError(0,"gde.proRecDesHas.rangoAsignado.label");
	// 	<--- ABM ProRecDesHas
	
	// ---> ABM ProRecCom
	public static final String PRORECCOM_LABEL = addError(0, "gde.proRecCom.label");
	public static final String PRORECCOM_PROREC_LABEL = addError(0, "gde.proRecCom.proRec.label");
	public static final String PRORECCOM_FECVTODEUDES_LABEL = addError(0, "gde.proRecCom.fecVtoDeuDes.label");
	public static final String PRORECCOM_FECVTODEUHAS_LABEL = addError(0, "gde.proRecCom.fecVtoDeuHas.label");
	public static final String PRORECCOM_PORCENTAJECOMISION_LABEL = addError(0, "gde.proRecCom.porcentajeComision.label");
	public static final String PRORECCOM_FECHADESDE_LABEL = addError(0, "gde.proRecCom.fechaDesde.label");
	public static final String PRORECCOM_FECHAHASTA_LABEL = addError(0, "gde.proRecCom.fechaHasta.label");
	public static final String PRORECCOM_COMISIONEXISTENTE_LABEL = addError(0,"gde.proRecDesHas.comisionExistente.label");
	// 	<--- ABM ProRecCom
	
	//  ---> ABM DeuRecCon
	public static final String DEURECCON_LABEL = addError(0, "gde.deuRecCon.label");
	// 	<--- ABM DeuRecCon

	//  ---> ABM Deuda
	public static final String DEUDA_LABEL = addError(0, "gde.deuda.label");
	public static final String DEUDA_CODREFPAG = addError(0, "gde.deuda.codRefPag.label"); 
	public static final String DEUDA_FECHAEMISION = addError(0, "gde.deuda.fechaEmision.label"); 
	public static final String DEUDA_FECHAVENCIMIENTO = addError(0, "gde.deuda.fechaVencimiento.label"); 
	public static final String DEUDA_ANIO = addError(0, "gde.deuda.anio.label"); 
	public static final String DEUDA_PERIODO = addError(0, "gde.deuda.periodo.label"); 
	public static final String DEUDA_IMPORTEBRUTO = addError(0, "gde.deuda.importeBruto.label");
	public static final String DEUDA_IMPORTE = addError(0, "gde.deuda.importe.label"); 
	public static final String DEUDA_SALDO = addError(0, "gde.deuda.saldo.label"); 
	public static final String DEUDA_SALDOACTUALIZADO = addError(0, "gde.deuda.saldoActualizado.label"); 
	public static final String DEUDA_FECHAPAGO = addError(0, "gde.deuda.fechaPago.label");
	public static final String DEUDA_ESTAIMPRESA = addError(0, "gde.deuda.estaImpresa.label");
	public static final String DEUDA_ACTUALIZACION = addError(0, "gde.deuda.actualizacion.label");
	
	public static final String DEUDA_CONCEPTO_IMPORTE_ERROR = addError(0, "gde.deuda.conceptoRequerido.label");
	// 	<--- ABM Deuda

	// ---> Convenio
	public static final String CONVENIO_LABEL = addError(0, "gde.convenio.label");
	public static final String CONVENIO_CANAL = addError(0, "gde.convenio.canal.label");
	public static final String CONVENIO_CANTIDADCUOTASPLAN = addError(0, "gde.convenio.cantidadCuotasPlan.label");
	public static final String CONVENIO_CASOMANUAL = addError(0, "gde.convenio.casoManual.label");
	public static final String CONVENIO_CUENTA = addError(0, "gde.convenio.cuenta.label");
	public static final String CONVENIO_DESACTUALIZACION = addError(0, "gde.convenio.desActualizacion.label");
	public static final String CONVENIO_DESCAPITALORIGINAL = addError(0, "gde.convenio.desCapitalOriginal.label");
	public static final String CONVENIO_DESINTERES = addError(0, "gde.convenio.desInteres.label");
	public static final String CONVENIO_ESTADOCONVENIO = addError(0, "gde.convenio.estadoConvenio.label");
	public static final String CONVENIO_FECHAFOR = addError(0, "gde.convenio.fechaFor.label");
	public static final String CONVENIO_PERFOR = addError(0, "gde.convenio.perFor.label");
	public static final String CONVENIO_IP = addError(0, "gde.convenio.ip.label");
	public static final String CONVENIO_OBSERVACIONFOR = addError(0, "gde.convenio.observacionFor.label");
	public static final String CONVENIO_TIPODOCAPO = addError(0, "gde.convenio.tipoDocApo.label");
	public static final String CONVENIO_TIPOPERFOR = addError(0, "gde.convenio.tipoPerFor.label");
	public static final String CONVENIO_TOTACTUALIZACION = addError(0, "gde.convenio.totActualizacion.label");
	public static final String CONVENIO_TOTCAPITALORIGINAL = addError(0, "gde.convenio.totCapitalOriginal.label");
	public static final String CONVENIO_TOTIMPORTECONVENIO = addError(0, "gde.convenio.totImporteConvenio.label");
	public static final String CONVENIO_TOTINTERES = addError(0, "gde.convenio.totInteres.label");
	public static final String CONVENIO_ULTCUOIMP = addError(0, "gde.convenio.ultCuoImp.label");
	public static final String CONVENIO_USUARIOFOR = addError(0, "gde.convenio.usuarioFor.label");
	public static final String CONVENIO_VIADEUDA = addError(0, "gde.convenio.viaDeuda.label");
	public static final String CONVENIO_CUOTADESDE_RECONFECCION = addError(0,"gde.liqConvenioCuentaAdapter.reconfec.cuotaHastaError");
	public static final String CONVENIO_RECONFECCION_ESTADO = addError (0,"gde.liqConvenioCuentaAdapter.convenioFor.estadoReconfeccionError");
	public static final String CONVENIO_CUOTASALDODESDENULA = addError (0,"gde.liqConvenioCuotaSaldoAdapter.cuotaDesde.nulaError");
	public static final String CONVENIOESP_CASOVALIDO_REQUERIDO = addError (0,"gde.liqFormConvenioAdapter.casoValidoRequerido");
	public static final String CONVENIO_CUOTASALDODESDEFUERA_RANGO = addError (0, "gde.liqConvenioCuotaSaldoAdapter.cuotaDesde.rango");
	public static final String CONVENIO_CUOTASALDOCONINDETERMINADA = addError (0, "gde.convenio.reimpresion.tieneIndeter");
	public static final String CONVENIO_FECHAFOR_INVALIDA = addError (0,"gde.convenio.fechaForInvalida.msg");
	//	 <--- Convenio
	
	//	---> Convenio Saldo Por Caducidad
	public static final String SALPORCAD_ESTADOCONVENIO = addError(0,"gde.convenio.salPorCad.estadoErroneo.label");
	public static final String SALPORCADVUELTA_ESTADO = addError (0,"gde.convenio.salPorCadVuelta.estadoErroneo.label");
	public static final String SALPORCAD_CUOTASALDO = addError (0,"gde.convenio.salPorCad.tieneCuotaSaldo");
	public static final String SALPORCAD_INDET = addError (0,"gde.convenio.salPorCad.tieneIndet");
	public static final String SALPORCADVUELTA_HUBOPAGOS = addError(0,"gde.convenio.salPorCadVuelta.huboPagos");
	public static final String SALPORCADVUELTA_INCLUIDAOTROCONV = addError(0,"gde.convenio.salPorCadVuelta.incluidaOtroConv");
	public static final String SALPORCADVUELTA_CAMBIODEVIA= addError(0,"gde.convenio.salPorCadVuelta.cambioDeVia");
	public static final String SALPORCAD_NOSERECUPERODEUDA = addError(0,"gde.salPorCad.noSeEncontroDeuda");
	public static final String SALPORCADVUELTA_ESTADO_DEUDA_INCORRECTO = addError(0,"gde.salPorCadVuelta.deudaOtroEstado");
	public static final String SALPORCADVUELTA_ESTADOERRONEO = addError(0,"gde.salPorCadVuelta.estadoErroneo");
	
	// --- > Anulacion de convenios
	public static final String ANULACION_CONVENIO_PAGOS = addError(0,"gde.liqConvenioSalPorCad.anulacion.pagos.error");
	
	// ----> Convenio Cuota Saldo
	public static final String CUOTASALDO_ESTADOCONVENIO = addError(0,"gde.convenio.cuotaSaldo.estadoErroneo");
	public static final String CUOTASALDO_CUOTASVENCIDAS = addError(0,"gde.convenio.cuotaSaldo.existenCuotasVencidas");
	public static final String CUOTASALDO_DESDE_MENOR_MIN_PLAN = addError(0, "gde.convenio.cuotaSaldo.menorADefPlan");
	public static final String CUOTASALDO_ALLCUOTASVENCIDAS = addError (0, "gde.convenio.cuotaSaldo.todasVencidas");
	
	
	// ---> Convenio Aplicar Pago a Cuenta
	public static final String PAGOCUENTA_ESTADOCONVENIO = addError (0,"gde.convenio.aplicarPagoCuenta.estado");
	public static final String PAGOCUENTA_NOTIENE = addError (0,"gde.convenio.aplicarPagoCuenta.noExisten");
	public static final String RESCATE_ESTADOCONVENIO = addError(0,"gde.convenio.rescate.estado");
	
	// ---> ABM Descuento Especial
	public static final String DESESP_DESDESESP = addError(0, "gde.desEsp.desDesEsp.label");
	public static final String DESESP_RECURSODESESP = addError(0, "def.recurso.label");
	public static final String DESESP_TIPODEUDADESESP = addError(0, "gde.desEsp.tipoDeuda.label");
	public static final String DESESP_VIADEUDADESESP = addError(0, "gde.desEsp.viaDeuda.label");
	public static final String DESESP_FECHAVTODESDEDESESP = addError(0, "gde.desEsp.fechaVtoDeudaDesde.label");
	public static final String DESESP_FECHAVTOHASTADESESP = addError(0, "gde.desEsp.fechaVtoDeudaHasta.label");
	public static final String DESESP_LEYENDADESESP = addError(0, "gde.desEsp.leyenda");
	public static final String DESESP_LABEL = addError(0, "gde.desEsp.label");
	public static final String DESESP_PORDESCAP = addError(0, "gde.desEsp.porDesCap.label");
	public static final String DESESP_PORDESACT = addError(0, "gde.desEsp.porDesAct.label");
	public static final String DESESP_PORDESINT = addError(0, "gde.desEsp.porDesInt.label");
	// <--- ABM Descuento Especial	

	// ---> ABM DesRecClaDeu
	public static final String DESRECCLADEU_LABEL = addError(0, "gde.desRecClaDeu.label");
	public static final String DESRECCLADEU_FECHADESDE = addError(0, "gde.desRecClaDeu.fechaDesde.label");
	public static final String DESRECCLADEU_FECHAHASTA = addError(0, "gde.desRecClaDeu.fechaHasta.label");
	public static final String DESRECCLADEU_CLASIFICACION = addError(0, "gde.desEsp.listDesRecClaDeu.label");
	// <--- ABM DesRecClaDeu
	
	// ---> ABM DesAtrVal
	public static final String DESATRVAL_LABEL = addError(0, "gde.desAtrVal.label");
	public static final String DESATRVAL_FECHADESDE = addError(0, "gde.desAtrVal.fechaDesde.label");
	public static final String DESATRVAL_FECHAHASTA = addError(0, "gde.desAtrVal.fechaHasta.label");
	public static final String DESATRVAL_ATRIBUTO_LABEL = addError(0, "gde.desAtrVal.atributo.label");
	// <--- ABM DesAtrVal
	
	// ---> ABM DesEspExe
	public static final String DESESPEXE_LABEL = addError(0, "gde.desEspExe.label");
	public static final String DESESPEXE_FECHADESDE = addError(0, "gde.desEspExe.fechaDesde.label");
	public static final String DESESPEXE_FECHAHASTA= addError(0, "gde.desEspExe.fechaHasta.label");
	// <--- ABM DesEspExe
	
	//  ---> ABM ProMasProExc
	public static final String PROMASPROEXC_LABEL = addError(1300, "gde.proMasProExc.label");
	
	

	
	// 	<--- ABM ProMasProExc

	
	//	 ---> Plan
	public static final String PLAN_LABEL = addError(0, "gde.plan.label");
	public static final String PLAN_APLICATOTALIMPAGO = addError(0, "gde.plan.aplicaTotalImpago.label");
	public static final String PLAN_ATRIBUTOASE = addError(0, "gde.plan.atributoAse.label");
	public static final String PLAN_CANCUOAIMPENFORM = addError(0, "gde.plan.canCuoAImpEnForm.label");
	public static final String PLAN_CANMAXCUO = addError(0, "gde.plan.canMaxCuo.label");
	public static final String PLAN_CANMINCUOPARCUOSAL = addError(0, "gde.plan.canMinCuoParCuoSal.label");
	public static final String PLAN_CANMINPER = addError(0, "gde.plan.canMinPer.label");
	public static final String PLAN_CUODESPARAREC = addError(0, "gde.plan.cuoDesParaRec.label");
	public static final String PLAN_DESPLAN = addError(0, "gde.plan.desPlan.label");
	public static final String PLAN_ORDENANZA_LABEL = addError(0, "gde.plan.ordenanza.label");
	public static final String PLAN_ESMANUAL = addError(0, "gde.plan.esManual.label");
	public static final String PLAN_FECHAALTA = addError(0, "gde.plan.fechaAlta.label");
	public static final String PLAN_FECHAALTA_REF = addError(0, "gde.plan.fechaAlta.ref");	
	public static final String PLAN_FECHABAJA = addError(0, "gde.plan.fechaBaja.label");
	public static final String PLAN_FECHABAJA_REF = addError(0, "gde.plan.fechaBaja.ref");
	public static final String PLAN_FECVENDEUDES = addError(0, "gde.plan.fecVenDeuDes.label");
	public static final String PLAN_FECVENDEUHAS = addError(0, "gde.plan.fecVenDeuHas.label");
	public static final String PLAN_IMPMINCUO = addError(0, "gde.plan.impMinCuo.label");
	public static final String PLAN_IMPMINDEU = addError(0, "gde.plan.impMinDeu.label");
	public static final String PLAN_LEYENDAPLAN = addError(0, "gde.plan.leyendaPlan.label");
	public static final String PLAN_LINKNORMATIVA = addError(0, "gde.plan.linkNormativa.label");
	public static final String PLAN_POSEEACTESP = addError(0, "gde.plan.poseeActEsp.label");
	public static final String PLAN_APLICAPAGCUE = addError(0, "gde.plan.aplicaPagCue.label");
	public static final String PLAN_CANMAXCUO_REF = addError(0, "gde.plan.canMaxCuo.ref");
	
	public static final String PLAN_LISTPLANCLADEU = addError(0, "gde.plan.listPlanClaDeu.label");
	public static final String PLAN_LISTPLANMOTCAD = addError(0, "gde.plan.listPlanMotCad.label");
	public static final String PLAN_LISTPLANFORACTDEU = addError(0, "gde.plan.listPlanForActDeu.label");
	public static final String PLAN_LISTPLANDESCUENTO = addError(0, "gde.plan.listPlanDescuento.label");
	public static final String PLAN_LISTPLANINTFIN = addError(0, "gde.plan.listPlanIntFin.label");
	public static final String PLAN_LISTPLANVEN = addError(0, "gde.plan.listPlanVen.label");
	public static final String PLAN_LISTPLANRECURSO = addError(0,"gde.plan.listRecurso.label");
	public static final String PLAN_LISTPLANVENCUOMAXMOD = addError(0,"gde.plan.listPlanVen.modifMenorCuoMax");
	public static final String PLAN_LISTPLANATRVAL = addError(0, "gde.plan.listPlanAtrVal.label");
	public static final String PLAN_LISTPLANEXE = addError(0, "gde.plan.listPlanExe.label");
	public static final String PLAN_LISTPLANPRORROGA = addError(0, "gde.plan.listPlanProrroga.label");
	public static final String PLAN_FECHAFORMALIZACION = addError(0, "gde.liqFormConvenioEspAdapter.parametros.fechaForm.legend");
	public static final String PLAN_LISTPLANIMPMIN = addError(0, "gde.plan.listPlanImpMin.label");	
	public static final String PLAN_DESCCAPITAL = addError(0, "gde.liqFormConvenioEspAdapter.parametros.descCapital.legend");
	public static final String PLAN_DESCACTUALIZACION = addError(0, "gde.liqFormConvenioEspAdapter.parametros.descActualizacion.legend");
	public static final String PLAN_INTERES = addError(0, "gde.liqFormConvenioEspAdapter.parametros.interes.legend");
	public static final String PLAN_VENPRIMERACUOTA = addError(0, "gde.liqFormConvenioEspAdapter.parametros.venPrimeraCuota.legend");
	public static final String PLAN_UNVALORREQUERIDO = addError(0, "gde.liqFormConvenioEspAdapter.parametros.unValorRequerido");
	public static final String PLAN_CANTMAXCUO = addError (0,"gde.liqFormConvenioAdapter.cantMaxCuo.label");
	public static final String PLAN_IMPMINCUOESP = addError (0,"gde.planImpMin.impMinCuo.label");
	
	public static final String PLAN_LISTPLANVEN_MANUAL = addError(0, "gde.plan.listPlanVen.manual");
	
	public static final String PLAN_LIST_PLANVEN_CUOMAX = addError(0, "gde.plan.planVen.CantMaxCuo");
	public static final String PLAN_LIST_PLANINTFIN_CUOMAX = addError(0, "gde.plan.planIntFin.CantMaxCuo");
	//	 <--- Plan

	// ---> TipoDeudaPlan
	public static final String TIPODEUDAPLAN_LABEL = addError(0, "gde.tipoDeudaPlan.label");
	public static final String TIPODEUDAPLAN_DESTIPODEUDAPLAN = addError(0, "gde.tipoDeudaPlan.desTipoDeudaPlan.label");
	//	 <--- TipoDeudaPlan
	
	 // ---> PlanProrroga
	public static final String PLANPRORROGA_LABEL = addError(0, "gde.planProrroga.label");
	public static final String PLANPRORROGA_DESPLANPRORROGA = addError(0, "gde.planProrroga.desPlanProrroga.label");
	public static final String PLANPRORROGA_FECVTO = addError(0, "gde.planProrroga.fecVto.label");
	public static final String PLANPRORROGA_FECVTONUE = addError(0, "gde.planProrroga.fecVtoNue.label");
	public static final String PLANPRORROGA_FECHADESDE = addError(0, "gde.planProrroga.fechaDesde.label");
	public static final String PLANPRORROGA_FECHAHASTA = addError(0, "gde.planProrroga.fechaHasta.label");
	//	 <--- PlanProrroga
	
	 // ---> PlanForActDeu
	public static final String PLANFORACTDEU_LABEL = addError(0, "gde.planForActDeu.label");
	public static final String PLANFORACTDEU_ESCOMUN = addError(0, "gde.planForActDeu.esComun.label");
	public static final String PLANFORACTDEU_FECVENDEUDES = addError(0, "gde.planForActDeu.fecVenDeuDes.label");
	public static final String PLANFORACTDEU_PORCENTAJE = addError(0, "gde.planForActDeu.porcentaje.label");
	public static final String PLANFORACTDEU_FECHADESDE = addError(0, "gde.planForActDeu.fechaDesde.label");
	public static final String PLANFORACTDEU_FECHAHASTA = addError(0, "gde.planForActDeu.fechaHasta.label");
	public static final String PLANFORACTDEU_CLASSNAME = addError(0, "gde.planForActDeu.className.label");
	//	 <--- PlanForActDeu
	
	 // ---> PlanExe
	public static final String PLANEXE_LABEL = addError(0, "gde.planExe.label");
	public static final String PLANEXE_FECHADESDE = addError(0, "gde.planExe.fechaDesde.label");
	public static final String PLANEXE_FECHAHASTA = addError(0, "gde.planExe.fechaHasta.label");
	//	 <--- PlanExe
	
	 // ---> PlanAtrVal
	public static final String PLANATRVAL_LABEL = addError(0, "gde.planAtrVal.label");
	public static final String PLANATRVAL_VALOR = addError(0, "gde.planAtrVal.valor.label");
	public static final String PLANATRVAL_FECHADESDE = addError(0, "gde.planAtrVal.fechaDesde.label");
	public static final String PLANATRVAL_FECHAHASTA = addError(0, "gde.planAtrVal.fechaHasta.label");
	//	 <--- PlanAtrVal
	
	 // ---> PlanIntFin
	public static final String PLANINTFIN_LABEL = addError(0, "gde.planIntFin.label");
	public static final String PLANINTFIN_CUOTAHASTA = addError(0, "gde.planIntFin.cuotaHasta.label");
	public static final String PLANINTFIN_FECHADESDE = addError(0, "gde.planIntFin.fechaDesde.label");
	public static final String PLANINTFIN_FECHAHASTA = addError(0, "gde.planIntFin.fechaHasta.label");
	public static final String PLANINTFIN_INTERES = addError(0, "gde.planIntFin.interes.label");
	public static final String PLANINTFIN_UNICO = addError(0,"gde.planIntFin.unicoPlanIntFin");
	//	 <--- PlanIntFin

	 // ---> PlanDescuento
	public static final String PLANDESCUENTO_LABEL = addError(0, "gde.planDescuento.label");
	public static final String PLANDESCUENTO_CANTIDADCUOTASPLAN = addError(0, "gde.planDescuento.cantidadCuotasPlan.label");
	public static final String PLANDESCUENTO_PORDESACT = addError(0, "gde.planDescuento.porDesAct.label");
	public static final String PLANDESCUENTO_PORDESCAP = addError(0, "gde.planDescuento.porDesCap.label");
	public static final String PLANDESCUENTO_PORDESINT = addError(0, "gde.planDescuento.porDesInt.label");
	public static final String PLANDESCUENTO_FECHADESDE = addError(0, "gde.planDescuento.fechaDesde.label");
	public static final String PLANDESCUENTO_FECHAHASTA = addError(0, "gde.planDescuento.fechaHasta.label");
	public static final String PLANDESCUENTO_UNADESCUENTO_REQUERIDO = addError(0, "gde.planDescuento.unDescuentoRequerida");
	public static final String PLANDESCUENTO_UNICO = addError(0,"gde.planDescuento.unicoPlanDescuento");
	
	//	 <--- PlanDescuento
	
	 // ---> PlanVen
	public static final String PLANVEN_LABEL = addError(0, "gde.planVen.label");
	public static final String PLANVEN_CUOTAHASTA = addError(0, "gde.planVen.cuotaHasta.label");
	public static final String PLANVEN_FECHADESDE = addError(0, "gde.planVen.fechaDesde.label");
	public static final String PLANVEN_FECHAHASTA = addError(0, "gde.planVen.fechaHasta.label");
	public static final String PLANVEN_UNICO = addError(0,"gde.planVen.unicoPlanVen");
	
	public static final String PLANVEN_PORDESACT = addError(0,"gde.planDescuento.porDesAct.label");
	public static final String PLANVEN_PORDESCAP = addError(0,"gde.planDescuento.porDesCap.label");
	public static final String PLANVEN_PORDESINT = addError(0,"gde.planDescuento.porDesInt.label");
	// 	<--- PlanVen
	
	// ---> PlanMotCad
	public static final String PLANMOTCAD_LABEL = addError(0, "gde.planMotCad.label");
	public static final String PLANMOTCAD_DESPLANMOTCAD = addError(0, "gde.planMotCad.desPlanMotCad.label");
	public static final String PLANMOTCAD_ESESPECIAL = addError(0, "gde.planMotCad.esEspecial.label");
	public static final String PLANMOTCAD_CANTCUOCON = addError(0, "gde.planMotCad.cantCuoCon.label");
	public static final String PLANMOTCAD_CANTCUOALT = addError(0, "gde.planMotCad.cantCuoAlt.label");
	public static final String PLANMOTCAD_CANTDIAS = addError(0, "gde.planMotCad.cantDias.label");
	public static final String PLANMOTCAD_CLASSNAME = addError(0, "gde.planMotCad.className.label");
	public static final String PLANMOTCAD_FECHADESDE = addError(0, "gde.planMotCad.fechaDesde.label");
	public static final String PLANMOTCAD_FECHAHASTA = addError(0, "gde.planMotCad.fechaHasta.label");
	public static final String PLANMOTCAD_UNACANTIDAD_REQUERIDA = addError(0, "gde.planMotCad.unaCantidadRequerida");
	public static final String PLANMOTCAD_UNICOPLANMOTCAD = addError(0, "gde.planMotCad.unicoPlanMotCad");
	//	<--- PlanMotCad
	
	 // ---> PlanClaDeu
	public static final String PLANCLADEU_LABEL = addError(0, "gde.planClaDeu.label");
	public static final String PLANCLADEU_FECHADESDE = addError(0, "gde.planClaDeu.fechaDesde.label");
	public static final String PLANCLADEU_FECHAHASTA = addError(0, "gde.planClaDeu.fechaHasta.label");
	public static final String PLANCLADEU_UNICOPLANCLADEU = addError(0,"gde.planClaDeu.unicoPlanClaDeu");
	//	 <--- PlanClaDeu	
	
	// ---> PlanImpMin
	public static final String PLANIMPMIN_LABEL = addError(0, "gde.planImpMin.label");
	public static final String PLANIMPMIN_CANTIDADCUOTAS = addError(0, "gde.planImpMin.cantidadCuotas.label");
	public static final String PLANIMPMIN_IMPMINCUO = addError(0, "gde.planImpMin.impMinCuo.label");
	public static final String PLANIMPMIN_IMPMINDEU = addError(0, "gde.planImpMin.impMinDeu.label");
	public static final String PLANIMPMIN_FECHADESDE = addError(0, "gde.planImpMin.fechaDesde.label");
	public static final String PLANIMPMIN_FECHAHASTA = addError(0, "gde.planImpMin.fechaHasta.label");
	public static final String PLANIMPMIN_UNICOPLANIMPMIN = addError(0,"gde.planImpMin.unicoPlanImpMin");
	//	 <--- PlanImpMin
	
	// ---> Admin. Planilla Envio Deuda
	public static final String PLA_ENV_DEU_PRO_SEARCHPAGE_FECHADESDE = addError(0, "gde.plaEnvDeuPro.fechaEnvioDesde.label");	
	public static final String PLA_ENV_DEU_PRO_SEARCHPAGE_FECHAHASTA = addError(0, "gde.plaEnvDeuPro.fechaEnvioHasta.label"); 
	public static final String PLA_ENV_DEU_PRO_MSG_CONSTANCIAS_NO_PREPARADAS_PARA_HABILITACION = addError(0, "gde.plaEnvDeuProViewAdapter.msgConsntanciasNoPreparadasParaHabilitar");	
	public static final String PLA_ENV_DEU_PRO_MSG_CONST_NO_SELEC_O_NO_PREPARADAS_PARA_HAB = addError(0, "gde.plaEnvDeuProViewAdapter.msgConstNoSelecONoPrepParaHab");
	// <--- Admin. Planilla Envio Deuda

	// ---> Evento
	public static final String EVENTO_LABEL = addError(0, "gde.evento.label");
	public static final String EVENTO_CODIGO = addError(0, "gde.evento.codigo.label");
	public static final String EVENTO_DESCRIPCION = addError(0, "gde.evento.descripcion.label");
	public static final String EVENTO_ETAPAPROCESAL = addError(0, "gde.evento.etapaProcesal.label");
	public static final String EVENTO_AFECTACADJUI = addError(0, "gde.evento.afectaCadJui.label");
	public static final String EVENTO_AFECTAPRESSEN = addError(0, "gde.evento.afectaPresSen.label");
	public static final String EVENTO_ESUNICOENGESJUD = addError(0, "gde.evento.esUnicoEnGesJud.label");
	// <--- Evento
	
	// ---> Admin. Constancias de Deuda
	public static final String CONSTANCIADEU_LABEL = addError(0, "gde.constanciaDeu.label");
	public static final String CONSTANCIADEU_TITULAR_REF = addError(0, "gde.constanciaDeu.titular.ref");
	public static final String CONSTANCIADEU_PROCURADOR_LABEL= addError(0, "gde.procurador.label");
	public static final String CONSTANCIADEU_CUENTA_LABEL = addError(0, "pad.cuenta.label");
	public static final String CONSTANCIADEU_CUENTA_NO_ASOCIADA_A_DEU_JUD = addError(0, "gde.constanciaDeu.error.noAsociadaADeudaJudicial");
	public static final String CONSTANCIADEU_DEU_INCLUIDA_EN_CONDEUDET = addError(0, "gde.constanciaDeu.error.deudaIncluidaEnConDeuDet");
	
	public static final String CONSTANCIADEU_YA_FUE_HABILITADA = addError(0, "gde.constanciaDeu.error.yaHabilitada");
	public static final String CONSTANCIADEU_NO_CREADA = addError(0, "gde.constanciaDeu.error.NoEstaEnCreada");
	public static final String CONSTANCIADEU_SIN_DETALLE = addError(0, "gde.constanciaDeu.error.sinDetalle");
	public static final String CONSTANCIADEU_MSJ_HABILITAR = addError(0, "gde.constanciaDeu.msj.habilitar");
	public static final String CONSTANCIADEU_MSJ_RECOMPONER = addError(0, "gde.constanciaDeu.msj.recomponer");
	public static final String CONSTANCIADEU_ERROR_TIT_DEUDAS_NO_EN_JUDICIALES = addError(0, "gde.constanciaDeu.error.deudasNoEnJudiciales");
	public static final String CONSTANCIADEU_ERROR_DELETE_TIENE_DETALLES =  addError(0, "gde.constanciaDeu.error.delete.tieneDetalles");
	public static final String CONSTANCIADEU_MSJ_ELIMINAR =  addError(0, "gde.constanciaDeu.msj.eliminar");
	public static final String CONSTANCIADEU__MSJ_RECURSO_REQ = addError(0, "gde.constanciaDeu.msj.recursoReq");
	public static final String CONSTANCIADEU__MSJ_CUENTA_NO_EXISTE_PARA_RECURSO = addError(0, "gde.constanciaDeu.msj.cuentaNoExisteParaRecurso");
	public static final String CONSTANCIADEU_MSJ_ANULAR = addError(0, "gde.constanciaDeu.msj.anular");
	public static final String CONSTANCIADEU_MSJ_ELIMINAR_EN_GESJUD = addError(0, "gde.constanciaDeu.msj.eliminarEnGesJud");
	public static final String CONSTANCIADEU_MSJ_EN_GESJUD = addError(0, "gde.constanciaDeu.msj.enGesJud");
	public static final String CONSTANCIADEU_CUENTA_TITULAR_PERS_NO_ENC = addError(0, "gde.constanciaDeu.error.cuentaTitularPersNoEnc");
	
	public static final String CONDEUDET_LABEL = addError(0, "gde.conDeuDet.label");	
	public static final String CONDEUDET_MSJ_POSEE_DEUDAS_CONVENIO = addError(0, "gde.conDeuDet.msj.poseeDeudasConvenio");
	public static final String CONDEUDET_MSJ_POSEE_DEUDAS_INDET =  addError(0, "gde.conDeuDet.msj.poseeDeudasIndet");
	public static final String CONDEUDET_MSJ_POSEE_DEUDAS_CANCEL = addError(0, "gde.conDeuDet.msj.poseeDeudasCancel");
	public static final String CONDEUDET_MSJ_POSEE_DEUDAS_VIA_JUD_SIN_EST_JUD = addError(0, "gde.conDeuDet.msj.poseeDeudasViaJudSinEstJud");
	public static final String CONDEUDET_MSJ_NO_POSEE_DEUDAS_DISPONIBLES =  addError(0, "gde.conDeuDet.msj.noPoseeDeudasDisponibles");
	public static final String CONDEUDET_MSJ_POSEE_DEUDAS_EN_GESTION_JUDICIAL = addError(0, "gde.conDeuDet.msj.PoseeDeudasEnJuicio");
	public static final String CONDEUDET_AGEGAR_NINGUNA_DEUDA_SELECTED= addError(0, "gde.conDeuDet.error.ningunaDeudaSelected");
	// <--- Admin. Constancias de Deuda
	
	// ---> Etapa Procesal
	public static final String ETAPAPROCESAL_DESETAPAPROCESAL = addError(0, "gde.etapaProcesal.desEtapaProcesal.label");
	// <--- Etapa Procesal

	
	// ---> ABM TraspasoDeuda y DevolucionDeuda
	// Traspaso Deuda
	public static final String TRASPASODEUDA_LABEL          = addError(0,"gde.traspasoDeuda.label");
	public static final String TRASPASODEUDA_FECHA_TRASPASO = addError(0,"gde.traspasoDeuda.fechaTraspaso");
	public static final String TRASPASODEUDA_RECURSO        = addError(0,"gde.traspasoDeuda.recurso");
	public static final String TRASPASODEUDA_PRO_ORI        = addError(0,"gde.traspasoDeuda.proOri");
	public static final String TRASPASODEUDA_PRO_DES        = addError(0,"gde.traspasoDeuda.proDes"); 
	public static final String TRASPASODEUDA_CUENTA         = addError(0,"gde.traspasoDeuda.cuenta"); 
	public static final String TRASPASODEUDA_PLA_ENV_DEU_PRO_DEST = addError(0,"gde.traspasoDeuda.plaEnvDeuProDest");
	public static final String TRASPASODEUDA_OBSERVACION    = addError(0,"gde.traspasoDeuda.observacion");
	public static final String TRASPASODEUDA_USUARIO_ALTA   = addError(0,"gde.traspasoDeuda.usuarioAlta");

	public static final String TRASPASODEUDA_CUENTA_NO_ASOCIADA_RECURSO = addError(0,"gde.traspasoDeuda.cuentaNoAsociadaRecurso");
	public static final String TRASPASODEUDA_PRO_ORI_PRO_DES_COINCIDENTES       = addError(0,"gde.traspasoDeuda.proOriProDesCoincidentes");
	public static final String TRASPASODEUDA_CUENTA_PRO_ORI_SIN_DEUDAS_JUDICIAL = addError(0,"gde.traspasoDeuda.cuentaProOriSinDeudasJudicial");
	public static final String TRASPASODEUDA_LIST_TRA_DEU_DET_NO_SELEC = addError(0,"gde.traspasoDeuda.listTraDeuDet.noSeleccionados");

	// Devolucion Deuda
	public static final String DEVOLUCIONDEUDA_LABEL        = addError(0,"gde.devolucionDeuda.label");
	public static final String DEVOLUCIONDEUDA_FECHA_DEVOLUCION = addError(0,"gde.devolucionDeuda.fechaDevolucion");
	public static final String DEVOLUCIONDEUDA_RECURSO      = addError(0,"gde.devolucionDeuda.recurso");
	public static final String DEVOLUCIONDEUDA_PROCURADOR   = addError(0,"gde.devolucionDeuda.procurador"); 
	public static final String DEVOLUCIONDEUDA_CUENTA       = addError(0,"gde.devolucionDeuda.cuenta"); 
	public static final String DEVOLUCIONDEUDA_OBSERVACION  = addError(0,"gde.devolucionDeuda.observacion");
	public static final String DEVOLUCIONDEUDA_USUARIO_ALTA = addError(0,"gde.devolucionDeuda.usuarioAlta");
	public static final String DEVOLUCIONDEUDA_CUENTA_NO_ASOCIADA_RECURSO         = addError(0,"gde.devolucionDeuda.cuentaNoAsociadaRecurso");
	public static final String DEVOLUCIONDEUDA_CUENTA_PRO_ORI_SIN_DEUDAS_JUDICIAL = addError(0,"gde.devolucionDeuda.cuentaProOriSinDeudasJudicial");
	public static final String DEVOLUCIONDEUDA_LIST_DEV_DEU_DET_NO_SELEC = addError(0,"gde.devolucionDeuda.listDevDeuDet.noSeleccionados");
	
	
	// ---> DevDeuDet	
	public static final String DEVDEUDET_LABEL    = addError(0,"gde.devDeuDet.label");
	public static final String DEVDEUDET_ID_DEUDA = addError(0,"gde.devDeuDet.idDeuda");
	public static final String DEVDEUDET_CONSTANCIA_DEU_ORI = addError(0,"gde.devDeuDet.constanciaDeuOri");

	// ---> TraDeuDet
	public static final String TRADEUDET_LABEL    = addError(0,"gde.traDeuDet.label");
	public static final String TRADEUDET_ID_DEUDA = addError(0,"gde.traDeuDet.idDeuda");
	public static final String TRADEUDET_CONSTANCIA_DEU_ORI = addError(0,"gde.traDeuDet.constanciaDeuOri");
	
	// ---> Traspasos de Deuda entre Procuradores y Devoluciones de Deuda a Via Administrativa 
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_ACCION             = addError(0, "gde.traspasoDevolucionDeudaSearchPage.accion.label");
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_RECURSO            = addError(0, "gde.traspasoDevolucionDeudaSearchPage.recurso.label");
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_PROCURADOR_ORIGEN  = addError(0, "gde.traspasoDevolucionDeudaSearchPage.procuradorOrigen.label");
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_PROCURADOR_DESTINO = addError(0, "gde.traspasoDevolucionDeudaSearchPage.procuradorDestino.label");
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_CUENTA             = addError(0, "gde.traspasoDevolucionDeudaSearchPage.cuenta.label");
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_FECHA_DESDE        = addError(0, "gde.traspasoDevolucionDeudaSearchPage.fechaDesde.label");
	public static final String TRASPASO_DEVOLUCION_SEARCHPAGE_FECHA_HASTA        = addError(0, "gde.traspasoDevolucionDeudaSearchPage.fechaHasta.label");
	
	public static final String TRADEVDEUDET_AGREGAR_NO_PERMITIDO  = addError(0,"gde.traDevDeuDet.agregarNoPermitido");	
	
	public static final String TRASPASO_DEVOLUCION_DEUDA_ADAPTER_ACCION            = addError(0, "gde.traspasoDevolucionDeudaAdapter.accion");
	
	// <--- Traspasos de Deuda entre Procuradores y Devoluciones de Deuda a Via Administrativa
	
	// ---> Admin. Deuda Judicial sin Constancias 
	public static final String DEUJUDSINCONSTANCIA_PROCURADOR_LABEL= addError(0, "gde.deuJudSinConstancia.procurador.label");
	public static final String DEUJUDSINCONSTANCIA_CUENTA_LABEL= addError(0, "gde.deuJudSinConstancia.cuenta.label");
	public static final String DEUJUDSINCONSTANCIA_CUENTA_RECURSO_LABEL= addError(0, "gde.deuJudSinConstancia.cuenta.recurso.label");
	public static final String DEUJUDSINCONSTANCIA_NINGUNA_DEUDA_SELECCIONADA=addError(0, "gde.deuJudSinConstancia.error.ningunaDeudaSeleccionada");
	public static final String DEUJUDSINCONSTANCIA_NINGUNA_CONSTANCIA_SELECCIONADA=addError(0, "gde.deuJudSinConstancia.error.ningunaConstanciaSeleccionada");
	// <--- Admin. Deuda Judicial sin Constancias
	
	public static final String DEUJUDICIAL_INCLUIDA_EN_CONVENIO_PAGO = addError(0, "gde.deuJudicial.incluidaEnConvenioPago");
	public static final String DEUJUDICIAL_INDETERMINADA             = addError(0, "gde.deuJudicial.indeterminada");
	public static final String DEUJUDICIAL_CANCELADA                 = addError(0, "gde.deuJudicial.cancelada");
	public static final String DEUJUDICIAL_INC_EN_GES_JUD            = addError(0, "gde.deuJudicial.incEnGesJud");

	// ---> ADM Gestiones Judiciales
	public static final String GESJUD_DESCRIPCION_LABEL 			= addError(0, "gde.gesJud.desGesJud.label");
	public static final String GESJUD_JUZGADO_LABEL 				= addError(0, "gde.gesJud.juzgado.label");
	public static final String GESJUD_CASO_LABEL 					= addError(0, "gde.gesJud.caso.label");
	public static final String GESJUD_LABEL 						= addError(0, "gde.gesJud.label");
	public static final String GESJUD_ERRORS_INSERT_EVENTO_NO_CONTIENE_PREDECESORES = addError(0, "gde.gesJud.error.evento.noContienePredecesoreslabel");
	public static final String GESJUD_ERRORS_INSERT_EVENTO_FECHA_INVALIDA = addError(0, "gde.gesJud.error.evento.fechaInvalida");
	public static final String GESJUD_ERRORS_DELETE_EVENTO_NO_ES_EL_ULTIMO = addError(0, "gde.gesJud.error.evento.eliminar.noEsElUltimo");
	public static final String GESJUD_ERRORS_REG_CADUCIDAD = addError(0, "gde.gesJud.error.regCaducidad");
	public static final String GESJUD_FECHA_CADUCIDAD_LABEL = addError(0, "gde.gesJud.fechaCaducidad.label");
	public static final String GESJUD_TIPO_JUZGADO_LABEL = addError(0, "gde.gesJud.tipoJuzgado.label");
	public static final String GESJUD_FECHAALTA_LABEL = addError(0, "gde.gesJud.fechaAlta.label");
	

	public static final String GESJUDDEU_LABEL 									= addError(0, "gde.gesJudDeu.label");
	public static final String GESJUDDEU_MSJ_POSEE_DEUDAS_CONVENIO 				= addError(0, "gde.gesJudDeu.msj.poseeDeudasConvenio");
	public static final String GESJUDDEU_MSJ_POSEE_DEUDAS_INDET					= addError(0, "gde.gesJudDeu.msj.poseeDeudasIndet");
	public static final String GESJUDDEU_MSJ_POSEE_DEUDAS_CANCEL				= addError(0, "gde.gesJudDeu.msj.poseeDeudasCancel");
	public static final String GESJUDDEU_MSJ_POSEE_DEUDAS_VIA_JUD_SIN_EST_JUD 	= addError(0, "gde.gesJudDeu.msj.poseeDeudasViaJudSinEstJud");
	public static final String GESJUDDEU_MSJ_NO_POSEE_DEUDAS_DISPONIBLES 		= addError(0, "gde.gesJudDeu.msj.noPoseeDeudasDisponibles");
	public static final String GESJUDDEU_AGEGAR_NINGUNA_DEUDA_SELECTED			= addError(0, "gde.gesJudDeu.error.ningunaDeudaSelected");
	public static final String GESJUDDEU_MSJ_POSEE_DEUDAS_EN_GESJUD				= addError(0, "gde.gesJudDeu.error.poseeDeudasEnGesJud");
	public static final String GESJUDDEU_AGREGAR_CONSTANCIA_ESTADO_INVALIDO 	= addError(0, "gde.gesJudDeu.error.agregarConst.estadoInvalido");
	
	public static final String GESJUDEVENTO_LABEL 				= addError(0, "gde.gesJudEvento.label"); 
	public static final String GESJUDEVENTO_FECHAEVENTO_LABEL 	= addError(0, "gde.gesJudEvento.fechaEvento.label");
	public static final String GESJUDEVENTO_ERROR_FECHAEVENTO_ANTERIOR_FECHAALTA = addError(0, "gde.gesJudEvento.error.fechaEventoAnteriorFechaAlta.label");
	public static final String GESJUDEVENTO_ERROR_FECHA_ANTERIOR_FECHAACTUAL = addError(0, "gde.gesJudEvento.error.fechaEventoAnteriorFechaActual.label");
	// <--- ADM Gestiones Judiciales	
	
	
	// ---> ABM Rescate 
	public static final String RESCATE_SEARCHPAGE_FECHADESDE = addError(0, "gde.rescateSearchPage.fechaDesde.label");
	public static final String RESCATE_SEARCHPAGE_FECHAHASTA = addError(0, "gde.rescateSearchPage.fechaHasta.label");
	
	public static final String RESCATE_LABEL             = addError(0, "gde.rescate.label");
	public static final String RESCATE_RECURSO           = addError(0, "gde.rescate.recurso");
	public static final String RESCATE_PLAN              = addError(0, "gde.rescate.plan");
	public static final String RESCATE_FECHA_VIG_RESCATE = addError(0, "gde.rescate.fechaVigRescate");
	public static final String RESCATE_FECHA_RESCATE     = addError(0, "gde.rescate.fechaRescate");
	public static final String RESCATE_OBSERVACION       = addError(0, "gde.rescate.observacion");
	public static final String RESCATE_CASO              = addError(0, "gde.rescate.caso");
	public static final String RESCATE_CORRIDA           = addError(0, "gde.rescate.corrida");
	// <--- ABM Rescate	

	
	// ---> Consultar Estado Cuenta
	public static final String ESTADOCUENTA_CUENTA_NO_ENCONTRADA = addError(0, "gde.estadoCuenta.cuentaNoEncontrada");
	public static final String ESTADO_CUENTA_FECHA_DESDE = addError(0, "gde.estadoCuenta.fechaVtoDesde.label");
	public static final String ESTADO_CUENTA_FECHA_HASTA = addError(0, "gde.estadoCuenta.fechaVtoHasta.label");
	public static final String ESTADO_CUENTA_NROCUENTA_LABEL = addError(0, "gde.estadoCuenta.numeroCuenta.label");
	// <--- Consultar Estado Cuenta


	public static final String CONVENIO_REPORT_RECURSO = addError(0, "gde.convenioReport.recurso");
	public static final String CONVENIO_REPORT_FECHA_CONVENIO_DESDE = addError(0, "gde.convenioReport.fechaConvenioDesde.label");
	public static final String CONVENIO_REPORT_FECHA_CONVENIO_HASTA = addError(0, "gde.convenioReport.fechaConvenioHasta.label");
	public static final String CONVENIO_REPORT_CUOTA_DESDE = addError(0, "gde.convenioReport.cuotaDesde.label");
	public static final String CONVENIO_REPORT_CUOTA_HASTA = addError(0, "gde.convenioReport.cuotaHasta.label");
	public static final String CONVENIO_REPORT_CTD_MAX_REG = addError(0, "gde.convenioReport.cantidadMaximaRegistros");
	
	public static final String RECAUDACION_REPORT_FECHA_DESDE = addError(0, "gde.recaudacionReport.fechaDesde.label");
	public static final String RECAUDACION_REPORT_FECHA_HASTA = addError(0, "gde.recaudacionReport.fechaHasta.label");
	public static final String RECAUDACION_REPORT_TIPO_FECHA = addError(0, "gde.recaudacionReport.tipoFecha.label");
	
	public static final String CONVENIO_A_CADUCAR_REPORT_RECURSO = addError(0, "gde.convenioACaducarReport.recurso");
	public static final String CONVENIO_A_CADUCAR_REPORT_FECHA_CONVENIO_DESDE = addError(0, "gde.convenioACaducarReport.fechaConvenioDesde.label");
	public static final String CONVENIO_A_CADUCAR_REPORT_FECHA_CONVENIO_HASTA = addError(0, "gde.convenioACaducarReport.fechaConvenioHasta.label");
	public static final String CONVENIO_A_CADUCAR_REPORT_FECHA_CADUCIDAD = addError(0, "gde.convenioACaducarReport.fechaCaducidad.label");
	public static final String CONVENIO_A_CADUCAR_REPORT_CUOTA_DESDE = addError(0, "gde.convenioACaducarReport.cuotaDesde.label");
	public static final String CONVENIO_A_CADUCAR_REPORT_CUOTA_HASTA = addError(0, "gde.convenioACaducarReport.cuotaHasta.label");
	public static final String CONVENIO_A_CADUCAR_REPORT_IMPORTE_CUOTA_DESDE = addError(0, "gde.convenioACaducarReport.importeCuotaDesde.label");
	public static final String CONVENIO_A_CADUCAR_REPORT_IMPORTE_CUOTA_HASTA = addError(0, "gde.convenioACaducarReport.importeCuotaHasta.label");
	
	//public static final String CONVENIO_A_CADUCAR_REPORT_CTD_MAX_REG = addError(0, "gde.convenioACaducarReport.cantidadMaximaRegistros");
	
	
	// ---> Control Informe Deuda Escribano
	public static final String TIPOTRAMITE_LABEL             = addError(0, "gde.tipoTramite.label");
	public static final String TRAMITE_NRORECIBO_LABEL       = addError(0, "gde.tramite.nroRecibo.label");
	public static final String TRAMITE_ANIORECIBO_LABEL       = addError(0, "gde.tramite.anioRecibo.label");
	public static final String TRAMITE_CODREFPAG_LABEL 		= addError(0, "gde.tramite.codRefPag.label");
	// <--- Control Informe Deuda Escribano

	// ---> ABM LiqCom
	public static final String LIQCOM_FECHALIQ_LABEL = addError(0, "gde.liqCom.fechaLiquidacion.label");
	public static final String LIQCOM_FECHAPAGODESDE_LABEL = addError(0, "gde.liqCom.fechaPagoDesde.label");
	public static final String LIQCOM_FECHAPAGOHASTA_LABEL = addError(0, "gde.liqCom.fechaPagoHasta.label");
	public static final String CORRIDA_LABEL = addError(0, "gde.liqCom.corrida.label");
	public static final String LIQCOM_LABEL = addError(0, "gde.liqCom.label");
	public static final String LIQCOM_YA_EXISTE_LIQCOM = addError(0, "gde.liqCom.yaExisteLiqCom");
	public static final String LIQCOM_RECURSO_SERBAN_REQ = addError(0, "gde.liqCom.recursoSerBan.req");
	// <--- ABM LiqCom
	
	// ---> Consultar convenio/recibo no liquidables
	public static final String CONRECNOLIQ_TIPO_PAGO_LABEL = addError(0, "gde.conRecNoLiqSearchPage.tipoPago.label");
	public static final String CONRECNOLIQ_PROCESAR_NINGUNO_SELECTED = addError(003, "gde.conRecNoLiqSearchPage.msj.ningunoSelected.label");
	public static final String CONRECNOLIQ_RECIBO_LABEL = addError(003, "gde.conRecNoLiqSearchPage.recibo.label");
	public static final String CONRECNOLIQ_SEARCHPAGE_FECHADESDE_LABEL = addError(003, "gde.conRecNoLiqSearchPage.fechaDesde.label");
	public static final String CONRECNOLIQ_SEARCHPAGE_FECHAHASTA_LABEL = addError(003, "gde.conRecNoLiqSearchPage.fechaHasta.label");
	// <--- Consultar convenio/recibo no liquidables	
	
	// ---> ABM Saldo por Caducidad Masivo 
	public static final String SALDOCADUCIDAD_SEARCHPAGE_FECHADESDE = addError(0, "gde.salPorCadSearchPage.fechaDesde.label");
	public static final String SALDOCADUCIDAD_SEARCHPAGE_FECHAHASTA = addError(0, "gde.salPorCadSearchPage.fechaHasta.label");
	public static final String SALDOCADUCIDAD_RECURSO           = addError(0, "gde.salPorCad.recurso");
	public static final String SALDOCADUCIDAD_PLAN              = addError(0, "gde.salPorCad.plan");
	public static final String SALDOCADUCIDAD_FECHA			    = addError(0, "gde.salPorCad.fechaSalCad");
	public static final String SALDOCADUCIDAD_CORRIDA			= addError (0,"gde.salPorCad.corrida");

	public static final String SALPORCADDET_CONVENIO			=addError (0, "gde.salPorCadDet.convenio");
	public static final String SALPORCADDET_SALPORCAD			= addError (0,"gde.salPorCadDet.salPorCad");
	public static final String SALPORCAD_INDETERMINADOS			= addError (0,"gde.salPorCad.indeterminados");
	public static final String SALPORCAD_FALTAN_CONDEUCUO		= addError(0,"gde.salPorCad.faltanConDeuCuo");
	

	// <--- ABM Saldo por Caducidad Masivo
	
	// ---> Anulacion
	public static final String ANULACION_LABEL = addError(0, "gde.anulacion.label");
	public static final String ANULACION_FECHAANULACION = addError(0, "gde.anulacion.fechaAnulacion.label");
	public static final String ANULACION_OBSERVACION = addError(0, "gde.anulacion.observacion.label");
	// <--- Anulacion
	
	// ---> MotAnuDeu
	public static final String MOTANUDEU_LABEL = addError(0, "gde.motAnuDeu.label");
	public static final String MOTANUDEU_DESMOTANUDEU = addError(0, "gde.motAnuDeu.desMotAnuDeu.label");
	// <--- MotAnuDeu
	
	// ---> VerPagosConvenio
	public static final String VERPAGOSCONVENIO_NOEXISTENPAGOS = addError(0,"gde.verPagosConvenio.noHayPagosError");

	 // ---> Recibo
	public static final String RECIBO_LABEL = addError(0, "gde.recibo.label");
	public static final String RECIBO_CODREFPAG = addError(0, "gde.recibo.codRefPag.label");
	public static final String RECIBO_ANIORECIBO = addError(0, "gde.recibo.anioRecibo.label");
	public static final String RECIBO_NRORECIBO = addError(0, "gde.recibo.nroRecibo.label");
	public static final String RECIBO_DESACTUALIZACION = addError(0, "gde.recibo.desActualizacion.label");
	public static final String RECIBO_DESCAPITALORIGINAL = addError(0, "gde.recibo.desCapitalOriginal.label");
	public static final String RECIBO_DESESP = addError(0, "gde.recibo.desEsp.label");
	public static final String RECIBO_DESGEN = addError(0, "gde.recibo.desGen.label");
	public static final String RECIBO_ESCUOTASALDO = addError(0, "gde.recibo.esCuotaSaldo.label");
	public static final String RECIBO_ESTAIMPRESO = addError(0, "gde.recibo.estaImpreso.label");
	public static final String RECIBO_FECHAGENERACION = addError(0, "gde.recibo.fechaGeneracion.label");
	public static final String RECIBO_FECHAPAGO = addError(0, "gde.recibo.fechaPago.label");
	public static final String RECIBO_FECHAVENCIMIENTO = addError(0, "gde.recibo.fechaVencimiento.label");
	public static final String RECIBO_IMPORTESELLADO = addError(0, "gde.recibo.importeSellado.label");
	public static final String RECIBO_TOTACTUALIZACION = addError(0, "gde.recibo.totActualizacion.label");
	public static final String RECIBO_TOTCAPITALORIGINAL = addError(0, "gde.recibo.totCapitalOriginal.label");
	public static final String RECIBO_TOTIMPORTERECIBO = addError(0, "gde.recibo.totImporteRecibo.label");
	public static final String RECIBO_USUARIOCUOTASALDO = addError(0, "gde.recibo.usuarioCuotaSaldo.label");
	// <--- Recibo		
	
	// ---> Consulta de importe a recaudar
	public static final String IMPORTE_RECAUDAR_FECHADESDE_LABEL = addError(0, "gde.importeRecaudadoReport.fechaPagoDesde.label");
	public static final String IMPORTE_RECAUDAR_FECHAHASTA_LABEL = addError(0, "gde.importeRecaudadoReport.fechaPagoHasta.label");
	// <--- Consulta de importe a recaudar	
	
	// ---> DeudaAdmRecCon	
	public static final String DEUDAADMRECCON_DEUDA_LABEL = addError(0, "gde.deudaAdmRecCon.deuda.label");
	public static final String DEUDAADMRECCON_RECCON_LABEL = addError(0, "gde.deudaAdmRecCon.recCon.label");
	public static final String DEUDAADMRECCON_IMPORTEBRUTO_LABEL = addError(0, "gde.deudaAdmRecCon.importeBruto.label");
	public static final String DEUDAADMRECCON_IMPORTE_LABEL = addError(0, "gde.deudaAdmRecCon.importe.label");
	public static final String DEUDAADMRECCON_SALDO_LABEL = addError(0, "gde.deudaAdmRecCon.saldo.label");
	// <--- DeudaAdmRecCon
	
	// ---> PlanRecurso
	public static final String PLANRECURSO_DUPLICADO = addError(0,"gde.planRecurso.duplicate.error");
	public static final String PLANRECURSO_LABEL = addError (0,"gde.planRecurso.label");
	public static final String PLANRECURSO_UNICO = addError(0," gde.planRecurso.esUnicoError");
	public static final String PLANRECURSO_FECHADESDEMAYOR = addError(0,"gde.planRecurso.fechaDesde.mayorError");
	public static final String PLANRECURSO_FECHAHASTAMENOR = addError(0,"gde.planRecurso.fechaHasta.menorError");
	// <--- PlanRecurso
	
	public static final String FECHA_RECONFECC_NOINGRESADA = addError (0, "gde.procesoMasivoAdmProcesoAdapter.fechaReconf.Error");
	
	public static final String ANULAR_CONVENIO_MSG = addError (0,"gde.liqConvenioSalPorCad.anulacion.msg");
	public static final String ANULAR_CONVENIO1_MSG = addError (0,"gde.liqConvenioSalPorCad.anulacion.msg1");
	
	// ---> Carga de eventos de gestion Judicial por archivo
	public static final String UPLOAD_EVENTO_MSG_FILE_GRABADA = addError (0,"gde.uploadEventoGesJud.msg.fileGrabada");
	public static final String UPLOAD_EVENTO_MSG_FILE_NOTFOUND = addError (0,"gde.uploadEventoGesJud.msg.fileNotFound");
	public static final String UPLOAD_EVENTO_FILE_LABEL = addError (0,"gde.uploadEventoGesJudAdapter.file.label");
	public static final String UPLOAD_EVENTO_CANT_TRANSAC_SUPERADAS = addError (0,"gde.uploadEventoGesJudAdapter.msg.cantTransacSuperadas");
	// <--- Carga de eventos de gestion Judicial por archivo

	// ---> Reportes de Seguimiento de las Gestión Judicial
	public static final String GESJUD_REPORT_FECHA_HASTA = addError (0,"gde.gesJud.report.fechaHasta.label");
	public static final String GESJUD_REPORT_FECHA_DESDE = addError (0,"gde.gesJud.report.fechaDesde.label");
	public static final String GESJUD_REPORT_FORMATO_TIEMPO_INVALIDO = addError (0,"gde.gesJud.report.formatoTiempoInvalido");
	// <--- Reportes de Seguimiento de las Gestión Judicial
	public static final String GESJUD_REPORT_OPERACION_REQUERIDO_4_TIEMPO = addError (0,"gde.gesJud.report.opRequerido4Tiempo");
	
	public static final String MAIL_REC_ASE_ERROR = addError (0,"gde.mailRecAse.error");
	
	public static final String DEU_CUE_GESJUD_CUENTA_NO_ENCONTRADA= addError (0,"gde.deuCueGesJudSearchPage.msg.cuentaNoEncontrada");

	// ---> Reporte de totales de emision
	public static final String EMISION_REPORT_FECHA_EMI_DESDE = addError (0,"gde.emisionReport.fechaEmiDesde.label");
	public static final String EMISION_REPORT_FECHA_EMI_HASTA = addError (0,"gde.emisionReport.fechaEmiHasta.label");	
	// <--- Reporte de totales de emision

	// ---> Reporte de Recaudado NUEVO
	public static final String RECAUDADO_REPORT_FECHA_EMI_DESDE = addError (0,"gde.recaudadoReport.fechaEmiDesde.label");
	public static final String RECAUDADO_REPORT_FECHA_EMI_HASTA = addError (0,"gde.recaudadoReport.fechaEmiHasta.label");	
	// <--- Reporte de Recaudado NUEVO	
	
	// ---> Reporte de contribuyente Cer
	public static final String CONTRIBUYENTECER_REPORT_FECHA_PAGO_DESDE = addError (0,"gde.contribuyenteCerReport.fechaPagoDesde.label");
	public static final String CONTRIBUYENTECER_REPORT_FECHA_PAGO_HASTA = addError (0,"gde.contribuyenteCerReport.fechaPagoHasta.label");	
	public static final String CONTRIBUYENTECER_REPORT_FECHA_REPORTE = addError (0,"gde.contribuyenteCerReport.fechaReporte.label");	
	public static final String CONTRIBUYENTECER_REPORT_PERIODO_DESDE = addError (0,"gde.contribuyenteCerReport.periodoDesde.label");
	public static final String CONTRIBUYENTECER_REPORT_PERIODO_HASTA = addError (0,"gde.contribuyenteCerReport.periodoHasta.label");
	// <--- Reporte de contribuyente Cer

	// ---> Reporte de Distribucion de Totales de Emitidos
	public static final String DISTRIBUCION_REPORT_FECHA_EMI_DESDE = addError (0,"gde.distribucionReport.fechaEmiDesde.label");
	public static final String DISTRIBUCION_REPORT_FECHA_EMI_HASTA = addError (0,"gde.distribucionReport.fechaEmiHasta.label");	
	// <--- Reporte de Distribucion de Totales de Emitidos

	// ---> Reporte de Deuda Anulada
	public static final String DEUDAANULADA_REPORT_FECHA_DESDE = addError (0,"gde.deudaAnuladaReport.fechaDesde.label");
	public static final String DEUDAANULADA_REPORT_FECHA_HASTA = addError (0,"gde.deudaAnuladaReport.fechaHasta.label");	
	// <--- Reporte de Deuda Anulada
	
	// ---> Reporte de Deuda de Procurador
	public static final String DEUDA_PROCURADOR_REPORT_FECHA_VTO_DESDE = addError (0,"gde.deudaProcuradorReport.fechaVtoDesde.label");
	public static final String DEUDA_PROCURADOR_REPORT_FECHA_VTO_HASTA = addError (0,"gde.deudaProcuradorReport.fechaVtoHasta.label");
	public static final String DEUDA_PROCURADOR_REPORT_TIPO_REPORTE = addError (0,"gde.deudaProcuradorReport.tipoReporte.label");
	// <--- Reporte de Deuda de Procurador
	
	public static final String CONSULTA_CODREFPAG_TIPO = addError(0,"gde.liqCodRefPagAdapter.tipoBoleta.error");

	// ---> ABM ProPreDeu: Proceso de Prescripcion de Deuda
	public static final String PROPREDEU_LABEL 		   = addError(0, "gde.proPreDeu.label");
	public static final String PROPREDEU_VIADEUDA 	   = addError(0, "gde.proPreDeu.viaDeuda.label");
	public static final String PROPREDEU_SERVICIOBANCO = addError(0, "gde.proPreDeu.servicioBanco.label");
	public static final String PROPREDEU_FECHATOPE     = addError(0, "gde.proPreDeu.fechaTope.label");

	public static final String PROPREDEUDET_LABEL 	   		= addError(0, "gde.proPreDeuDet.label");
	public static final String PROPREDEUDET_PROPREDEU  		= addError(0, "gde.proPreDeuDet.proPreDeu.label");
	public static final String PROPREDEUDET_CUENTA     		= addError(0, "gde.proPreDeuDet.cuenta.label");
	public static final String PROPREDEUDET_DEUDA      		= addError(0, "gde.proPreDeuDet.deuda.label");
	public static final String PROPREDEUDET_VIADEUDA   		= addError(0, "gde.proPreDeuDet.viaDeuda.label");
	public static final String PROPREDEUDET_ESTADO 	  	    = addError(0, "gde.proPreDeuDet.estado.label");
	public static final String PROPREDEUDET_ACCION 	   		= addError(0, "gde.proPreDeuDet.accion.label");
	public static final String PROPREDEUDET_ESTPROPREDEUDET = addError(0, "gde.proPreDeuDet.estProPreDeuDet.label");

	public static final String ESTPROPREDEUDET_DESCRIPCION  = addError(0, "gde.estProPreDeuDet.descripcion.label");
	
	public static final String PROPREDEU_SEARCHPAGE_FECHADESDE = addError(0, "gde.proPreDeuSearchPage.fechaDesde.label");
	public static final String PROPREDEU_SEARCHPAGE_FECHAHASTA = addError(0, "gde.proPreDeuSearchPage.fechaHasta.label");
	// <--- ABM ProPreDeu: Proceso de Prescripcion de Deuda
	
	// ---> ABM Declaraciones juradas
	public static final String DECJUR_LABEL=addError(0,"gde.decJur.label");
	public static final String DECJURSEARCHPAGE_PERIODODESDE_INVALIDO=addError(0, "gde.decJurSearchPage.periodoDesdeInvalido");
	public static final String DECJURSEARCHPAGE_PERIODOHASTA_INVALIDO=addError(0, "gde.decJurSearchPage.periodoHastaInvalido");
	public static final String DECJURSEARCHPAGE_RECURSO_LABEL = addError(0, "gde.proRec.recurso.label");
	public static final String DECURSEARCHPAGE_CUENTA_LABEL = addError(0,"gde.decJurSearchPage.cuenta.label");
	public static final String DECJURSEARCHPAGE_CUENTA_NOEXISTE = addError(0,"gde.decJur.cuenta.noExiste");
	
	public static final String DECJUR_VUELTA_ATRAS_LABEL=addError(0,"gde.decJur.vueltaAtras.label");
	public static final String DECJUR_DEUDA_CANCELADA_ERROR=addError(0,"gde.decJur.deudaCancelada.error");
	public static final String DECJUR_DEUDA_EN_CONVENIO_ERROR=addError(0,"gde.decJur.deudaEnConvenio.error");
	public static final String DECJUR_DEUDA_VIA_DEUDA_ERROR=addError(0,"gde.decJur.deudaViaDeuda.error");
	public static final String DECJUR_DEUDA_ESTADO_DEUDA_ERROR=addError(0,"gde.decJur.deudaEstadoDeuda.error");
	
	public static final String DECJURADAPTER_TIPO_LABEL=addError(0,"gde.decJurAdapter.tipoDJ");
	public static final String DECJURADAPTER_PERIODO_LABEL=addError(0,"gde.decJur.periodo.label");
	public static final String DECJURADAPTER_RADIO_LABEL=addError(0,"gde.decJurAdapter.radio.label");
	public static final String DECJURADAPTER_DEUDA_ENCONVENIO=addError(0,"gde.decJurAdapter.deudaEnConvenio");
	public static final String DECJURADAPTER_CANPER_LABEL=addError(0,"gde.decJurAdapter.canPer.label");

	public static final String DECJURADAPTER_PERIODODESDE_LABEL=addError(0,"gde.decJurAdapter.periodoDesde.label");
	public static final String DECJURADAPTER_PERIODOHASTA_LABEL=addError(0,"gde.decJurAdapter.periodoHasta.label");
	
	public static final String DECJURADAPTER_DEUDA_NOENCONTRADA=addError(0,"gde.decJurAdapter.deudaNoEncontrada");
	public static final String DECJURADAPTER_EXISTEN_DECLARACIONES=addError(0,"gde.decJurAdapter.existenOtrasDDJJ");
	public static final String DECJURADAPTER_TOTAL_MENOR_A_CERO=addError(0,"gde.decJurAdapter.totalMenorACero");
	
	public static final String DECJURADAPTER_DEUDA_RESTRINCCION_AFIP=addError(0,"gde.liqDecJurAdapter.restriccionAfip.error");
	
	// ---> Detalle de declaraciones juradas
	public static final String DECJURDET_LABEL= addError(0,"gde.decJurDet.label");
	public static final String DECJURDET_ACTIVIDAD_LABEL= addError(0,"gde.decJurDetAdapter.actividad.label");
	public static final String DECJURDET_RECCONADEC_YAEXISTE = addError(0,"gde.decJurDetAdapter.recConADec.yaExiste");
	
	// ---> Detalle de Pagos de Declaraciones Juradas
	public static final String DECJURPAG_LABEL = addError(0,"gde.decJurPag.label");
	public static final String DECJURPAG_TIPOPAGO_LABEL = addError(0,"gde.decJurPagAdapter.tipoPago.label");
	public static final String DECJURPAG_IMPORTE_LABEL=addError(0,"gde.decJurPagAdapter.importe.label");
	// <--- ABM Declaraciones juradas

	// TipoMulta
	public static final String TIPOMULTA_LABEL = addError(0, "gde.tipoMulta.label");
	public static final String TIPOMULTA_DESTIPOMULTA = addError(0, "gde.tipoMulta.descripcion.label");
	public static final String TIPOMULTA_ESIMPORTEMANUAL = addError(0, "gde.tipoMulta.esImporteManual.label");
	public static final String TIPOMULTA_RECCLADEU = addError(0, "gde.tipoMulta.recClaDeu.label");
	public static final String TIPOMULTA_RECURSO = addError(0, "gde.tipoMulta.recurso.label");
	public static final String TIPOMULTA_ASOCIADAAORDEN = addError(0,"gde.tipoMulta.asociadaAOrden.label");
	
	// Multa
	public static final String MULTA_FECHAACTUALIZACION = addError(0, "gde.multaEditAdapter.fechaActualizacion.label");
	public static final String MULTA_TIPOMULTA = addError(0, "gde.tipoMulta.label");
	public static final String MULTA_ORDENCONTROL = addError(0, "gde.multa.ordenControl.label");
	public static final String MULTA_LABEL = addError(0, "gde.multa.label");
	public static final String MULTASEARCHPAGE_CUENTA_RECURSO_LABEL= addError(0, "def.recurso.label");
	public static final String MULTASEARCHPAGE_CUENTA_LABEL=addError(0, "pad.cuenta.label");
	public static final String MULTASEARCHPAGE_CUENTA_NOEXISTE = addError(0,"gde.multa.cuenta.noExiste");
	public static final String MULTA_NUMEROORDEN_LABEL = addError(0,"ef.ordenControl.nroOrden.label");
	public static final String MULTA_ANIOORDEN_LABEL = addError(0,"ef.ordenControl.anioOrden.label");
	public static final String MSG_MULTA_ORDENCONTROL_NOEXISTE=addError(0,"gde.multa.ordenControl.noExiste");
	public static final String MULTA_ORDEN=addError(0,"gde.multa.ordenControl.label");
	public static final String MULTA_ORDEN_CASO = addError(0, "gde.multa.ordenControl.label");
	public static final String MSG_MULTA_DETAJUDET_NOEXISTE =addError(0, "gde.multaEditAdapter.detajudet.noExiste");
	public static final String MSG_MULTA_NODEFINIBLE=addError(0, "gde.multa.noDefinible");
	public static final String MULTA_SEARCHPAGE_FECHAEMISIONDESDE=addError(10127, "gde.multaSearchPage.fechaEmisionDesde.label");
	public static final String MULTA_SEARCHPAGE_FECHAEMISIONHASTA=addError(10127, "gde.multaSearchPage.fechaEmisionHasta.label");
	public static final String MULTA_FECHAEMISION=addError(10127, "gde.multa.fechaEmision.label");
	public static final String MULTA_FECHANOTIFICACION=addError(10127, "gde.multa.fechaNotificacion.label");
	public static final String MSG_IMPOSIBLE_ELIMINAR=addError(0,"gde.multa.imposibleEliminarMulta");
	public static final String IMPOSIBLE_AGREGAR_MULTA=addError(0,"gde.multa.imposibleAgregarMulta");
	public static final String MULTA_FECHARESOLUCION_LABEL=addError(0, "gde.multaEditAdapter.fechaResolucion.label");
	public static final String MULTA_RESOLUCION_LABEL=addError(0, "gde.multaEditAdapter.resolucion.label");
	public static final String MULTA_CANMIN_MENOR_CANMINDES=addError(0,"gde.multa.canMinMenorCanMinDes.error");
	public static final String MULTA_CANMIN_MAYOR_CANMINHAS=addError(0,"gde.multa.canMinMayorCanMinHas.error");
	
	// DescuentoMulta
	public static final String DESCUENTOMULTA_DESCRIPCION = addError(0, "gde.descuentoMulta.descripcion.label");
	public static final String MULTA_DESCUENTOMULTA = addError(0, "gde.descuentoMulta.label");
	
	// MultaDet
	public static final String MULTADET_LABEL = addError(0, "gde.multaDet.label");
	
	
	//MultaHistorico
	public static final String MULTAHISTORICO_PORCENTAJE_LABEL=addError(0,"gde.multaHistorico.porcentaje.label");
	public static final String MULTAHISTORICO_PERIODODESDE_LABEL=addError(0,"gde.multaHistorico.periodoDesde");
	public static final String MULTAHISTORICO_PERIODOHASTA_LABEL=addError(0,"gde.multaHistorico.periodoHasta");
	public static final String MULTAHISTORICO_ANIODESDE_LABEL=addError(0,"gde.multaHistorico.anioDesde");
	public static final String MULTAHISTORICO_ANIOHASTA_LABEL=addError(0,"gde.multaHistorico.anioHasta");
	
	// Mandatario
	public static final String MANDATARIO_DESCRIPCION = addError(0, "gde.mandatario.descripcion.label");
	public static final String MANDATARIO_LABEL = addError(0, "gde.mandatario.label");
	
	//Gestion de Cobranza
	public static final String COBRANZA_GESCOB_ESTADO_LABEL = addError(0,"gde.cobranzaSearchPage.estadoCobranza.label");
	public static final String COBRANZA_PER_COB_LABEL=addError(0, "gde.cobranaAsignarAdapter.asignarA.label");
	public static final String COBRANZA_FECHARESOLUCION_LABEL=addError(0,"gde.cobranza.fechaResolucion.label");
	public static final String COBRANZA_AJUSTES_TIPOORDEN = addError(0,"gde.cobranza.ajuste.tipoOrden.error");
	
	// PerCob
	public static final String PERCOB_LABEL = addError(0,"gde.perCob.label");
	public static final String COBRANZA_LABEL = addError(0,"gde.cobranza.label");
	

	//IndiceCompensacion
	public static final String INDICECOMPENSACION_LABEL = addError(0, "gde.indiceCompensacion.label");
	public static final String INDICECOMPENSACION_PERIODODESDE_LABEL = addError(0, "gde.indiceCompensacion.periodoDesde.label");
	public static final String INDICECOMPENSACION_PERIODOHASTA_LABEL = addError(0, "gde.indiceCompensacion.periodoHasta.label");
	
	// AnularDeuda
	public static final String ANULAR_DEUDA_NO_EMITIDA_ERROR = addError(0, "gde.liqDeudaAdapter.anularDeudaNoEmitida.error");

	// TipoPago
	public static final String TIPOPAGO_LABEL = addError(0, "gde.tipoPago.label");
	public static final String TIPOPAGO_DESTIPOPAGO = addError(0, "gde.tipoPago.destipopago.label");
	
	// PagoDeuda
	public static final String PAGODEUDA_LABEL = addError(0, "gde.pagoDeuda.label");
	
	// ConvenioCuota
	public static final String CONVENIOCUOTA_LABEL = addError(0, "gde.convenioCuota.label");
	
}



