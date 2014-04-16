//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;

/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class EmiError extends DemodaError {

	// ---> Emision 	
	public static final String EMISION_LABEL = addError(0,"emi.emision.label");
	public static final String EMISION_ANIO = addError(0,"emi.emision.anio.label");
	public static final String EMISION_PERIODO_DESDE = addError(0,"emi.emision.periodoDesde.label");
	public static final String EMISION_PERIODO_HASTA = addError(0,"emi.emision.periodoHasta.label");
	public static final String EMISION_FECHAEMISION = addError(0,"emi.emision.fechaEmision.label");
	public static final String EMISION_OBSERVACION = addError(0,"emi.emision.observacion.label");
	public static final String EMISION_VALOR = addError(0,"emi.emision.valor.label");
	public static final String EMISION_ESTADO_INVALIDO = addError(0,"emi.emision.estadoInvalido.label");	
	public static final String EMISION_ERROR_SERBANREC = addError(0,"emi.emision.serBanRec.label");
	public static final String EMISION_ERROR_RECCLADEU = addError(0,"emi.emision.recClaDeu.label");
	public static final String EMISION_ERROR_GENERAR_ALFAX = addError(0,"emi.emision.errorGenerarAlfax.label");
	public static final String EMISION_ERROR_GENERAR_DEUDAADMIN = addError(0,"emi.emision.errorGenerarDeudaAdmin");
	public static final String EMISION_ERROR_GENERAR_DEUDAJUDICIAL = addError(0,"emi.emision.errorGenerarDeudaJudicial");
	public static final String EMISION_ERROR_GENERAR_CONVENIO = addError(0,"emi.emision.errorGenerarConvenio");
	// <--- Emision
	
	// ---> Emision de cdm
	public static final String EMISIONCDM_FECHA_VENCIMIENTO = addError(0, "emi.emisionCdM.fechaVencimiento.label");	
	// <--- Emision de cdm	

	// ---> Impresion de cdm
	public static final String IMPRESIONCDM_ANIO = addError(0, "emi.impresionCdMEditAdapter.anio.label");	
	public static final String IMPRESIONCDM_MES = addError(0, "emi.impresionCdMEditAdapter.mes.label");
	public static final String IMPRESIONCDM_FORMATO_SALIDA = addError(0,"emi.impresionCdMEditAdapter.formatoSalida.label");
	// <--- Impresion de cdm	

	// ---> Emision extraordinaria
	public static final String EMISIONEXT_ANIO_LABEL = addError(0,"emi.emisionExt.anio.label");
	public static final String EMISIONEXT_PERIODO_LABEL = addError(0,"emi.emisionExt.periodo.label");
	public static final String EMISIONEXT_MSJ_CONCEPTOS_VACIOS = addError(0,"emi.emisionExtAdapter.msg.conceptosVacios.label");
	public static final String EMISIONEXT_FECVTO_LABEL = addError(0,"emi.emisionExt.fechaVto.label");
	public static final String EMISIONEXT_RECCLADEU_LABEL = addError(0, "emi.emisionExt.recClaDeu.label");

	public static final String EMISIONEXT_SEARCHPAGE_FECHADESDE = addError(0, "emi.emisionExtSearchPage.fechaDesde.label");
	public static final String EMISIONEXT_SEARCHPAGE_FECHAHASTA = addError(0, "emi.emisionExtSearchPage.fechaHasta.label");
	// <--- Emision extraordinaria	
	
	public static final String EMISION_ELIMINAR_NO_PERMITIDO  = addError(0, "emi.emision.eliminarNoPermitido");
	
	// ---> Emision Corregida de CdM
	public static final String EMISIONCORCDM_VALACTTIPOBR = addError(0, "emi.emisionCorCdMEditAdapter.valActTipObr.label");	
	// <--- Emision Corregida de CdM	

	// ---> Valorizacion de Matrices de Parametros de Emision
	public static final String VALEMIMAT_LABEL	= addError(0, "emi.valEmiMat.label");
	public static final String VALEMIMAT_EMIMAT = addError(0, "emi.valEmiMat.emiMat.label");
	// <--- Valorizacion de Matrices de Parametros de Emision
	
	// ---> Proceso de Resumen de Liquidacion de Deuda
	public static final String RESLIQDEU_LABEL 			   = addError(0, "emi.resLiqDeu.label");
	public static final String RESLIQDEU_RECURSO 		   = addError(0, "emi.resLiqDeu.recurso.label");
	public static final String RESLIQDEU_FECHA_ANALISIS    = addError(0, "emi.resLiqDeu.fechaAnalisis.label");
	public static final String RESLIQDEU_ANIO 		   	   = addError(0, "emi.resLiqDeu.anio.label");
	public static final String RESLIQDEU_PERIODO_DESDE     = addError(0, "emi.resLiqDeu.periodoDesde.label");
	public static final String RESLIQDEU_PERIODO_HASTA     = addError(0, "emi.resLiqDeu.periodoHasta.label");
	
	public static final String RESLIQDEU_CORRIDA_EXISTENTE = addError(0, "emi.resLiqDeu.corridaExistente.label");
	// <--- Proceso de Resumen de Liquidacion de Deuda

	// ---> Proceso de Generacion de archivos PAS y Deuda
	public static final String PROPASDEB_LABEL 				   = addError(0, "emi.proPasDeb.label");
	public static final String PROPASDEB_RECURSO	    	   = addError(0, "emi.proPasDeb.recurso.label");
	public static final String PROPASDEB_ATRIBUTO	    	   = addError(0, "emi.proPasDeb.atributo.label");
	public static final String PROPASDEB_ANIO 				   = addError(0, "emi.proPasDeb.anio.label");
	public static final String PROPASDEB_PERIODO	 		   = addError(0, "emi.proPasDeb.periodo.label");
	public static final String PROPASDEB_FECHA_ENVIO		   = addError(0, "emi.proPasDeb.fechaEnvio.label");
	public static final String PROPASDEB_MODIFICAR_FECHA_ENVIO = addError(0, "emi.proPasDeb.modificarFechaEnvio.label");
	// <--- Proceso de Generacion de archivos PAS y Deuda

	// ---> Impresion Masiva de Deuda
	public static final String IMPMASDEU_LABEL 		   = addError(0, "emi.impMasDeu.label");
	public static final String IMPMASDEU_RECURSO 	   = addError(0, "emi.impMasDeu.recurso.label");
	public static final String IMPMASDEU_FORMATOSALIDA = addError(0, "emi.impMasDeu.formatoSalida.label");
	public static final String IMPMASDEU_ANIO 		   = addError(0, "emi.impMasDeu.anio.label");
	public static final String IMPMASDEU_PERIODODESDE  = addError(0, "emi.impMasDeu.periodoDesde.label");
	public static final String IMPMASDEU_PERIODOHASTA  = addError(0, "emi.impMasDeu.periodoHasta.label");
	
	public static final String IMPMASDEU_FOMULARIO_PDF_NO_ENCONTRADO = addError(0, "emi.impMasDeu.formularioPdfNoEncontrado.label");
	public static final String IMPMASDEU_FOMULARIO_TXT_NO_ENCONTRADO = addError(0, "emi.impMasDeu.formularioTxtNoEncontrado.label");
	// <--- Impresion Masiva de Deuda
	
	// ---> Emision Masiva
	public static final String EMISIONMAS_SEARCHPAGE_FECHADESDE = addError(0, "emi.emisionMasSearchPage.fechaDesde.label");
	public static final String EMISIONMAS_SEARCHPAGE_FECHAHASTA = addError(0, "emi.emisionMasSearchPage.fechaHasta.label");
	// <--- Emision Masiva
	
	// ---> Emision Puntual
	public static final String EMISIONPUNTUAL_ANIO_LABEL 		 	= addError(0,"emi.emisionPuntualAdapter.anio.label");
	public static final String EMISIONPUNTUAL_PERIODODESDE_LABEL 	= addError(0,"emi.emisionPuntualAdapter.periodoDesde.label");
	public static final String EMISIONPUNTUAL_PERIODOHASTA_LABEL 	= addError(0,"emi.emisionPuntualAdapter.periodoHasta.label");
	public static final String EMISIONPUNTUAL_SEARCHPAGE_FECHADESDE = addError(0,"emi.emisionPuntualSearchPage.fechaDesde.label");
	public static final String EMISIONPUNTUAL_SEARCHPAGE_FECHAHASTA = addError(0,"emi.emisionPuntualSearchPage.fechaHasta.label");
	// <--- Emision Puntual

	// ---> Emision de Tasa por Revision de Planos 
	public static final String SELECCION_PLANO_DETALLE 	= addError(0,"emi.emisionTRPAdapter.seleccionPlanoDetalle.label");

	public static final String SITUACION_INMUEBLE 		= addError(0,"emi.planoDetalleAdapter.situacionInmueble.label");
	public static final String SOLICITUD_INMUEBLE 		= addError(0,"emi.planoDetalleAdapter.solicitudInmueble.label");
	public static final String CATEGORIA_INMUEBLE 		= addError(0,"emi.planoDetalleAdapter.categoriaInmueble.label");
	public static final String SUPERFICIE_EDIFICADA 	= addError(0,"emi.planoDetalleAdapter.supEdificar.label");
	public static final String SUPERFICIE_FINAL 		= addError(0,"emi.planoDetalleAdapter.supFinal.label");
	// <--- Emision de Tasa por Revision de Planos

	// ---> Emision Externa
	public static final String EMISIONEXTERNA_UPLOAD_FILE_LABEL 	= addError(0,"emi.emisionExterna.uploadFile.label");
	public static final String UPLOAD_EVENTO_MSG_FILE_NOTFOUND		= addError(0,"emi.emisionExterna.fileNotFound.label");
	// <--- Emision Externa
	
}

