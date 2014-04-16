//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class CyqError extends DemodaError {

	// ---> Procedimiento 
	public static final String PROCEDIMIENTO_LABEL = addError(0, "cyq.procedimiento.label");
	public static final String PROCEDIMIENTO_ABOGADO = addError(0, "cyq.procedimiento.abogado.label"); 
	public static final String PROCEDIMIENTO_ANIO = addError(0, "cyq.procedimiento.anio.label"); 
	public static final String PROCEDIMIENTO_ANIOEXP = addError(0, "cyq.procedimiento.anioExp.label"); 
	public static final String PROCEDIMIENTO_AUTO = addError(0, "cyq.procedimiento.auto.label"); 
	public static final String PROCEDIMIENTO_CARATULA = addError(0, "cyq.procedimiento.caratula.label"); 
	public static final String PROCEDIMIENTO_CASO = addError(0, "cyq.procedimiento.caso.label"); 
	public static final String PROCEDIMIENTO_CONTRIBUYENTE = addError(0, "cyq.procedimiento.contribuyente.label"); 
	public static final String PROCEDIMIENTO_DESCONTRIBUYENTE = addError(0, "cyq.procedimiento.desContribuyente.label"); 
	public static final String PROCEDIMIENTO_DOMICILIO = addError(0, "cyq.procedimiento.domicilio.label"); 
	public static final String PROCEDIMIENTO_ESTADOPROCED = addError(0, "cyq.procedimiento.estadoProced.label"); 
	public static final String PROCEDIMIENTO_FECHAALTA = addError(0, "cyq.procedimiento.fechaAlta.label"); 
	public static final String PROCEDIMIENTO_FECHAALTAVER = addError(0, "cyq.procedimiento.fechaAltaVer.label"); 
	public static final String PROCEDIMIENTO_FECHAAUTO = addError(0, "cyq.procedimiento.fechaAuto.label"); 
	public static final String PROCEDIMIENTO_FECHABOLETIN = addError(0, "cyq.procedimiento.fechaBoletin.label"); 
	public static final String PROCEDIMIENTO_FECHADESESTIMIENTO = addError(0, "cyq.procedimiento.fechaDesestimiento.label"); 
	public static final String PROCEDIMIENTO_FECHAHOMO = addError(0, "cyq.procedimiento.fechaHomo.label"); 
	public static final String PROCEDIMIENTO_FECHAINFIND = addError(0, "cyq.procedimiento.fechaInfInd.label"); 
	public static final String PROCEDIMIENTO_FECHAVEROPO = addError(0, "cyq.procedimiento.fechaVerOpo.label"); 
	public static final String PROCEDIMIENTO_HISESTPROCED = addError(0, "cyq.procedimiento.hisEstProced.label"); 
	public static final String PROCEDIMIENTO_JUZGADO = addError(0, "cyq.procedimiento.juzgado.label"); 
	public static final String PROCEDIMIENTO_LUGAROPOSICION = addError(0, "cyq.procedimiento.lugarOposicion.label"); 
	public static final String PROCEDIMIENTO_MOTIVODESESTIMIENTO = addError(0, "cyq.procedimiento.motivoDesestimiento.label"); 
	public static final String PROCEDIMIENTO_NUMERO = addError(0, "cyq.procedimiento.numero.label"); 
	public static final String PROCEDIMIENTO_NUMEXP = addError(0, "cyq.procedimiento.numExp.label"); 
	public static final String PROCEDIMIENTO_OBSERVACION = addError(0, "cyq.procedimiento.observacion.label"); 
	public static final String PROCEDIMIENTO_PEROPODEU = addError(0, "cyq.procedimiento.perOpoDeu.label"); 
	public static final String PROCEDIMIENTO_PROCEDANT = addError(0, "cyq.procedimiento.procedAnt.label"); 
	public static final String PROCEDIMIENTO_PROCYQ = addError(0, "cyq.procedimiento.proCyQ.label"); 
	public static final String PROCEDIMIENTO_TELEFONOOPOSICION = addError(0, "cyq.procedimiento.telefonoOposicion.label"); 
	public static final String PROCEDIMIENTO_TIPOPROCESO = addError(0, "cyq.procedimiento.tipoProceso.label"); 
	public static final String PROCEDIMIENTO_FECHABAJA = addError(0, "cyq.procedimiento.fechaBaja.label");
	
	public static final String MSG_DEVOLUCION_DEUDA  = addError(0, "cyq.procedimientoConversionAdapter.msgDevolucionDeuda");
	public static final String MSG_BAJA_DEVOLUCION_DEUDA = addError(0, "cyq.procedimientoConversionAdapter.msgBajaDevolucionDeuda");
	// <--- Procedimiento
	
	// ---> TipoProceso 
	public static final String TIPOPROCESO_LABEL = addError(0, "cyq.tipoProceso.label"); 
	public static final String TIPOPROCESO_CODTIPOPROCESO = addError(0, "cyq.tipoProceso.codTipoProceso.label"); 
	public static final String TIPOPROCESO_DESTIPOPROCESO = addError(0, "cyq.tipoProceso.desTipoProceso.label"); 
	// <--- TipoProceso
	
	// ---> EstadoProced 
	public static final String ESTADOPROCED_LABEL = addError(0, "cyq.estadoProced.label"); 
	public static final String ESTADOPROCED_DESESTADOPROCED = addError(0, "cyq.estadoProced.desEstadoProced.label"); 
	public static final String ESTADOPROCED_TIPO = addError(0, "cyq.estadoProced.tipo.label");
	public static final String ESTADOPROCED_ESINICIAL = addError(0, "cyq.estadoProced.esInicial.label");
	public static final String ESTADOPROCED_PERMITEMODIFICAR = addError(0, "cyq.estadoProced.permiteModificar.label"); 
	public static final String ESTADOPROCED_TRANSICIONES = addError(0, "cyq.estadoProced.transiciones.label"); 
	// <--- EstadoProced
	
	// ---> HisEstProced 
	public static final String HISESTPROCED_LABEL = addError(0, "cyq.hisEstProced.label");
    public static final String HISESTPROCED_FECHA = addError(0, "cyq.hisEstProced.fecha.label");
    public static final String HISESTPROCED_LOGCAMBIOS = addError(0, "cyq.hisEstProced.logCambios.label");
    public static final String HISESTPROCED_OBSERVACIONES = addError(0, "cyq.hisEstProced.observaciones.label");
    // <--- HisEstProced
	
    // ---> Juzgado 
	public static final String JUZGADO_LABEL = addError(4000, "cyq.juzgado.label");
	public static final String JUZGADO_DESJUZGADO_LABEL = addError(4000, "cyq.juzgado.desJuzgado.label");
	// <--- Juzgado
	
	// ---> Abogado
	public static final String ABOGADO_LABEL = addError(4000, "cyq.abogado.label");
	public static final String ABOGADO_DESABOGADO_LABEL = addError(4000, "cyq.abogado.descripcion.label");
	// <--- Abogado
	
	// ---> MotivoBaja 
	public static final String MOTIVOBAJA_LABEL = addError(0, "cyq.motivoBaja.label"); 
	public static final String MOTIVOBAJA_DESMOTIVOBAJA = addError(0, "cyq.motivoBaja.desMotivoBaja.label"); 
	public static final String MOTIVOBAJA_DEVUELVEDEUDA = addError(0, "cyq.motivoBaja.devuelveDeuda.label"); 
	public static final String MOTIVOBAJA_TIPO = addError(0, "cyq.motivoBaja.tipo.label"); 
	// <--- MotivoBaja
   
	// ---> MotivoResInf 
	public static final String MOTIVORESINF_LABEL = addError(0, "cyq.motivoResInf.label"); 
	public static final String MOTIVORESINF_DESMOTIVORESINF = addError(0, "cyq.motivoResInf.desMotivoResInf.label"); 
	// <--- MotivoResInf 
	 
	// ---> Pro Cue no Deu
	public static final String PROCUENODEU_LABEL = addError(0, "cyq.proCueNoDeu.label"); 
	// <--- Pro Cue no Deu
	
	// ---> TipoPrivilegio
	public static final String TIPOPRIVILEGIO_LABEL = addError(0, "cyq.tipoPrivilegio.label");
	// <--- TipoPrivilegio	
	
	// ---> DeudaPrivilegio 
	public static final String DEUDAPRIVILEGIO_LABEL = addError(0, "cyq.deudaPrivilegio.label"); 
    public static final String DEUDAPRIVILEGIO_IMPORTE = addError(0, "cyq.deudaPrivilegio.importe.label"); 
    public static final String DEUDAPRIVILEGIO_ORDEN = addError(0, "cyq.deudaPrivilegio.orden.label"); 
    public static final String DEUDAPRIVILEGIO_SALDO = addError(0, "cyq.deudaPrivilegio.saldo.label"); 
    public static final String DEUDAPRIVILEGIO_UNIQUE = addError(0, "cyq.deudaPrivilegio.msg.unique.label");
    // <--- DeudaPrivilegio
	
    // ---> PagoPriv     
    public static final String PAGOPRIV_LABEL = addError(0, "cyq.pagoPriv.label"); 
    public static final String PAGOPRIV_DESCRIPCION = addError(0, "cyq.pagoPriv.descripcion.label"); 
    public static final String PAGOPRIV_FECHA = addError(0, "cyq.pagoPriv.fecha.label"); 
    public static final String PAGOPRIV_IMPORTE = addError(0, "cyq.pagoPriv.importe.label");
    public static final String PAGOPRIV_TIPOCANCELACION = addError(0, "cyq.pagoPriv.tipoCancelacion.label");
    // <--- PagoPriv

    
    // ---> Liquidacion de deuda Cyq
    public static final String MSG_PLAN_REQUERIDO = addError(0, "gde.liqFormConvenioAdapter.planRequerido");
	public static final String MSG_NRO_CUOTA_REQUERIDO = addError(0, "gde.liqFormConvenioAdapter.nroCuotaRequerido");
	public static final String MSG_FECHAFOR_INVALIDA = addError (0,"gde.liqFormConvenioAdapter.fechaForInvalida");
	
	// <--- Liquidacion de deuda Cyq
}

