//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class AfiError extends DemodaError {	
	// Use Codigos desde 4000 hasta 4999

	// Formulario de Declaracion Jurada
	public static final String FORDECJUR_MSG_GENERAR_DECJUR = addError(0,"afi.forDecJurAdapter.generarDecJur.msg");
	
	public static final String FORDECJUR_RECURSO 		= addError(4000,"afi.forDecJur.recurso.label");
	public static final String FORDECJUR_FECHA_PRESENTACION 	  = addError(4002,"afi.forDecJur.fechaPresentacion.label");
	public static final String FORDECJUR_FECHA_PRESENTACION_DESDE = addError(4002,"afi.forDecJurSearchPage.fechaPresentacionDesde.label");
	public static final String FORDECJUR_FECHA_PRESENTACION_HASTA = addError(4003,"afi.forDecJurSearchPage.fechaPresentacionHasta.label");
	public static final String FORDECJUR_ENVIO_AFIP 			= addError(4004,"afi.forDecJur.envioAfip.label");
	public static final String FORDECJUR_IDTRANSACCION_AFIP 	= addError(4005,"afi.forDecJur.idTransaccionAfip.label");
	public static final String FORDECJUR_CUIT 					= addError(4006,"afi.forDecJur.cuit.label");
	
	
	public static final String LOCAL_NUMEROCUENTA 		= addError(4010,"afi.local.numeroCuenta.label");
	public static final String LOCAL_FECINIACT		    = addError(4011,"afi.local.fecIniAct.label");
	public static final String LOCAL_CENTRALIZAINGRESOS = addError(4012,"afi.local.centralizaIngresos.label");
	public static final String LOCAL_NOMBREFANTASIA 	= addError(4013,"afi.local.nombreFantasia.label");
	public static final String LOCAL_CONTRIBETUR 		= addError(4014,"afi.local.contribEtur.label");
	
	
	
	
	
	
}

