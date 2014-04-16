//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class ExeError extends DemodaError {
	
	// ---> Exencion
	public static final String EXENCION_LABEL = addError(0, "exe.exencion.label");
	public static final String EXENCION_ACTUALIZADEUDA = addError(0, "exe.exencion.actualizaDeuda.label");
	public static final String EXENCION_AFECTAEMISION = addError(0, "exe.exencion.afectaEmision.label");
	public static final String EXENCION_APLICAMINIMO = addError(0, "exe.exencion.aplicaMinimo.label");
	public static final String EXENCION_CODEXENCION = addError(0, "exe.exencion.codExencion.label");
	public static final String EXENCION_DESEXENCION = addError(0, "exe.exencion.desExencion.label");
	public static final String EXENCION_ENVIACYQ = addError(0, "exe.exencion.enviaCyQ.label");
	public static final String EXENCION_ENVIAJUDICIAL = addError(0, "exe.exencion.enviaJudicial.label");
	public static final String EXENCION_ESPARCIAL = addError(0, "exe.exencion.esParcial.label");
	public static final String EXENCION_MONTOMINIMO = addError(0, "exe.exencion.montoMinimo.label");
	public static final String EXENCION_LISTEXERES = addError(0, "exe.exencion.listExeRes.label");
	public static final String EXENCION_LISTEXERECCON = addError(0, "exe.exencion.listExeRecCon.label");
	public static final String EXENCION_PERMITEMANPAD = addError(0, "exe.exencion.permiteManPad.label");
	
	// <--- Exencion

	// ---> ExeRecCon
	public static final String EXERECCON_LABEL = addError(0, "exe.exeRecCon.label");
	public static final String EXERECCON_MONTOFIJO = addError(0, "exe.exeRecCon.montoFijo.label");
	public static final String EXERECCON_PORCENTAJE = addError(0, "exe.exeRecCon.porcentaje.label");
	public static final String EXERECCON_FECHADESDE = addError(0, "exe.exeRecCon.fechaDesde.label");
	public static final String EXERECCON_FECHAHASTA = addError(0, "exe.exeRecCon.fechaHasta.label");
	// <--- ExeRecCon
	
	// ---> ContribExe
	public static final String CONTRIBEXE_LABEL         = addError(0, "exe.contribExe.label");
	public static final String CONTRIBEXE_DESCONTRIBEXE = addError(0, "exe.contribExe.desContribExe.label");
	public static final String CONTRIBEXE_FECHADESDE    = addError(0, "exe.contribExe.fechaDesde.label");
	public static final String CONTRIBEXE_FECHAHASTA    = addError(0, "exe.contribExe.fechaHasta.label");
	public static final String CONTRIBEXE_REPETIDO      = addError(0, "exe.contribExe.repetido.label");	
	// <--- ContribExe
	
	// ---> CueExe
	public static final String CUEEXE_LABEL = addError(0, "exe.cueExe.label");
	public static final String CUEEXE_REF = addError(0, "exe.cueExe.ref");	
	public static final String CUEEXE_FECHADESDE = addError(0, "exe.cueExe.fechaDesde.label");
	public static final String CUEEXE_FECHAHASTA = addError(0, "exe.cueExe.fechaHasta.label");
	public static final String CUEEXE_FECHASOLICITUD = addError(0, "exe.cueExe.fechaSolicitud.label");
	
	public static final String CUEEXE_SOLICITANTE = addError(0, "exe.cueExe.solicitante.label");
	public static final String CUEEXE_SOLIC_DESCRIPCION = addError(0, "exe.cueExe.solicDescripcion.label");
	public static final String CUEEXE_EXPEDIENTE = addError(0, "exe.cueExe.expediente.label");
	public static final String CUEEXE_SOLIC_FECHADESDE = addError(0, "exe.cueExe.solicFechaDesde.label");
	public static final String CUEEXE_SOLIC_FECHAHASTA = addError(0, "exe.cueExe.solicFechaHasta.label");
	public static final String CUEEXE_FECHARESOLUCION = addError(0, "exe.cueExe.fechaResolucion.label");
	public static final String CUEEXE_ORDENANZA = addError(0, "exe.cueExe.ordenanza.label");
	public static final String CUEEXE_ARTICULO = addError(0, "exe.cueExe.articulo.label");
	public static final String CUEEXE_INCISO = addError(0, "exe.cueExe.inciso.label");
	public static final String CUEEXE_FECHA_ULT_INS = addError(0, "exe.cueExe.fechaUltIns.label");
	public static final String CUEEXE_NRO_BENEFICIARIO = addError(0, "exe.cueExe.nroBeneficiario.label");
	public static final String CUEEXE_CAJA = addError(0, "exe.cueExe.caja.label");
	public static final String CUEEXE_OBSERVACIONES = addError(0, "exe.cueExe.observaciones.label");
	public static final String CUEEXE_DOCUMENTACION = addError(0, "exe.cueExe.documentacion.label");
	public static final String CUEEXE_FECHA_PRESENT = addError(0, "exe.cueExe.fechaPresent.label");
	public static final String CUEEXE_CLASE = addError(0, "exe.cueExe.clase.label");
	public static final String CUEEXE_FECHA_VENCONTINQ = addError(0, "exe.cueExe.fechaVencContInq.label");
	public static final String CUEEXE_FECHA_CADHAB = addError(0, "exe.cueExe.fechaCadHab.label");
	
	public static final String CUEEXE_QUITA_SOBRE_TASA_TIPOPARCELA = addError(0, "exe.cueExe.quitaSobreTasa.tipoParcela.label");
	public static final String MSG_POSEE_DEUDA_EMITIDA = addError(0, "exe.marcaCueExeSearchPage.msgPosseDeudaEmitida");	
	public static final String MSG_NO_ES_ESTADO_BORRAR = addError(0, "exe.cueExe.msgNoEsEstadoABorrar");
	//	 <--- CueExe
	
	// ---> HisEstCueExe
	public static final String HISESTCUEEXE_LABEL = addError(0, "exe.hisEstCueExe.label");
	public static final String HISESTCUEEXE_FECHA = addError(0, "exe.hisEstCueExe.fecha.label");
	public static final String HISESTCUEEXE_LOGCAMBIOS = addError(0, "exe.hisEstCueExe.logCambios.label");
	public static final String HISESTCUEEXE_OBSERVACIONES = addError(0, "exe.hisEstCueExe.observaciones.label");
	// <--- HisEstCueExe

	// ---> Adm envio de cuentas
	public static final String SOLCUEEXE_ENVIO_CUENTA_INEXISTENTE = addError(0, "exe.solCueExeEnvioSearchPage.error.cuentaInexistente");
	public static final String SOLCUEEXE_ENVIO_CUENTA_REQUERIDA_PARA_CTAS_RELAC = addError(0, "exe.solCueExeEnvioSearchPage.error.cuentaRequeridadParaCtasRelac");
	public static final String SOLCUEEXE_ENVIO_SELECCION_VACIA = addError(0,"exe.solCueExeEnvioSearchPage.error.seleccionVacia");
	public static final String SOLCUEEXE_ENVIO_CASO_SIN_VALIDAR = addError(0,"exe.solCueExeEnvioSearchPage.error.casoSinValidar");
	public static final String SOLCUEEXE_ENVIO_CASO_DATOS_INCOMPLETOS = addError(0,"exe.solCueExeEnvioSearchPage.error.caso.datosIncompletos");
	// <--- Adm envio de cuentas
	
	// ---> abm TipoSujeto
	public static final String TIPOSUJETO_LABEL = addError(0, "exe.tipoSujeto.label");
	public static final String TIPOSUJETO_CODTIPOSUJETO = addError(0, "exe.tipoSujeto.codTipoSujeto.label");
	public static final String TIPOSUJETO_DESTIPOSUJETO = addError(0, "exe.tipoSujeto.desTipoSujeto.label");
	//<--- abm TipoSujeto
	
	// ---> abm TipSujExe
	public static final String TIPSUJEXE_LABEL = addError(0, "exe.tipSujExe.label");
	public static final String TIPSUJEXE_CODTIPSUJEXE = addError(0, "exe.tipSujExe.codTipSujExe.label");
	public static final String TIPSUJEXE_DESTIPSUJEXE = addError(0, "exe.tipSujExe.desTipoSujExe.label");
	//<--- abm TipSujExe
	
	public static final String CUEEXECONVIVIENTE_LABEL = addError(0, "exe.conviviente.label");
	public static final String CUEEXECONVIVIENTE_NOMBRE = addError(0, "exe.conviviente.nombre");
	
	public static final String MARCA_CUEEXE_NINGUNA_SELECTED = addError(0, "exe.marcaCueExeSearchPage.error.ningunaSelected");
	public static final String MARCA_CUEEXE_ESTADO_CAMBIAR = addError(0, "exe.marcaCueExeSearchPage.marcarEstado.label");
     
	// ---> abm EstadoCueExe
	public static final String ESTADOCUEEXE_LABEL = addError(0, "exe.estadoCueExe.label");
	public static final String ESTADOCUEEXE_CODESTADOCUEEXE = addError(0, "exe.exencion.codEstadoCueExe.label");
	public static final String ESTADOCUEEXE_TIPO = addError(0, "exe.estadoCueExe.tipo.label");
	public static final String ESTADOCUEEXE_ESRESOLUCION = addError(0, "exe.estadoCueExe.esResolucion.label");
	public static final String ESTADOCUEEXE_DES = addError(0, "exe.estadoCueExe.desEstadoCueExe.label");
	//<--- abm EstadoCueExe
}

