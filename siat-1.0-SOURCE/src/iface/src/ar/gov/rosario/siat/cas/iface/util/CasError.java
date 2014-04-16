//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class CasError extends DemodaError {

	// ---> ABM Solicitud
	public static final String SOLICITUD_LABEL        	 = addError(4054, "cas.solicitud.label");
	public static final String SOLICITUD_FECHAALTA       = addError(4055, "cas.solicitud.fechaAlta.label");
	public static final String SOLICITUD_AREAORIGEN      = addError(4056, "cas.solicitud.areaOrigen.label");
	public static final String SOLICITUD_AREADESTINO  	 = addError(4056, "cas.solicitud.areaDestino.label");
	public static final String SOLICITUD_USUARIOALTA     = addError(4055, "cas.solicitud.usuarioAlta.label");
	public static final String SOLICITUD_ASUNTOSOLICITUD = addError(4056, "cas.solicitud.asuntoSolicitud.label");
	public static final String SOLICITUD_FECHACAMEST  	 = addError(4056, "cas.solicitud.fechaCamEst.label");
	public static final String SOLICITUD_DESCRIPCION     = addError(4055, "cas.solicitud.descripcion.label");
	public static final String SOLICITUD_OBSESTSOLICITUD = addError(4056, "cas.solicitud.obsEstSolicitud.label");
	public static final String SOLICITUD_LOGSOLICITUD  	 = addError(4056, "cas.solicitud.logSolicitud.label");
	public static final String SOLICITUD_CUENTA  	     = addError(4057, "cas.solicitud.cuenta.label");
	public static final String SOLICITUD_PERIODO  	     = addError(4057, "cas.solicitud.periodo.label");
	public static final String SOLICITUD_ORDENCONTROL 	 = addError(4057, "cas.solicitud.ordenControl.label");

	// <--- ABM Solicitud

	// ---> Tipo solicitud
	public static final String TIPOSOLICITUD_LABEL        	 	= addError(4054, "cas.tipoSolicitud.label");
	public static final String TIPOSOLICITUD_MSG_NO_PERMITIDA 	= addError(4054, "cas.solicitudAdapter.msg.noPermitido.label");
	// <--- Tipo solicitud

	// ---> Estado solicitud
	public static final String ESTSOLICITUD_LABEL        	 = addError(4054, "cas.estSolicitud.label");
	public static final String OBS_EST_SOLICITUD_LABEL 		 = addError(4054, "cas.solicitud.obsEstSolicitud.label");	
	public static final String EST_SOLICITUD_SINCAMBIOS 	 = addError(4054, "cas.estSolicitud.sinCambios");
	// <--- Estado solicitud
	
	
	// ---> Mensajes de Validacion de Caso
	
	public static final String CASO_LABEL = addError(4054, "cas.caso.label");
	public static final String CASO_NUMERO_LABEL = addError(4054, "cas.caso.numero.label");
	public static final String CASO_ANIO_LABEL = addError(4054, "cas.caso.anio.label");

	public static final String SISTEMAORIGEN_LABEL = addError(4054, "cas.sistemaOrigen.label");	
	
	public static final String MSG_DEBE_VALIDAR_CASO = addError(4054, "cas.caso.msgDebeValidar");
	public static final String CASO_VALIDO        	 = addError(4054, "cas.caso.msgValido");
	public static final String CASO_NO_VALIDO        = addError(4054, "cas.caso.msgNoValido");
	public static final String CASO_NO_SERVICE       = addError(4054, "cas.caso.noService");
	// <---
	
	// ---> Solicitudes Pendientes
	public static final String SOLICITUD_PEND_NROCUENTA_FORMATOINCORRECTO = addError(4054, "cas.solicitud.nroCuenta.formatoIncorrecto");
	// <--- Solicitudes Pendientes
	
	
	// ---> Area Solicitud
	public static final String AREASOLICITUD_LABEL 		   = addError(4054, "cas.tipoSolicitud.areaDestino.label");
	public static final String AREASOLICITUD_COD 		   = addError(4054, "cas.areaSolicitud.codigo.ref");
	public static final String AREASOLICITUD_FECHA_ULT_MDF = addError(4054, "cas.areaSolicitud.fechaUltMdf.label");
	// <--- Area Solicitud
	
	// ---> Tipo Solicitud
	public static final String TIPOSOLICITUD_FECHA_ULT_MDF = addError(4054, "cas.areaSolicitud.fechaUltMdf.label");
	public static final String TIPOSOLICITUD_DESCRIPCION = addError(4054, "cas.tipoSolicitud.descripcion.label");
	public static final String TIPOSOLICITUD_CODIGO = addError(4054, "cas.tipoSolicitud.codigo.ref");	
	// <--- Tipo Solicitud
	
	// ---> Reporte de Solicitudes Pendientes
	public static final String SOLPEND_REPORT_FECHA_EMI_DESDE = addError (0,"cas.solPendReport.fechaEmiDesde.label");
	public static final String SOLPEND_REPORT_FECHA_EMI_HASTA = addError (0,"cas.solPendReport.fechaEmiHasta.label");
	public static final String SOLPEND_REPORT_AREA = addError (0,"cas.solPendReport.area.label");
	public static final String SOLPEND_REPORT_TIPO_SOLICITUD = addError (0,"cas.solPendReport.tipoSolicitud.label");
	// <--- Reporte de Solicitudes Pendientes
	
}

