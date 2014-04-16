//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.util;

import ar.gov.rosario.siat.base.iface.util.BaseError;

/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class ProError extends BaseError {
	// Use Codigos desde 11000 hasta 11999
	//static public String XXXXXX_XXXX_XXX   = addError(11000, "siat.base.xxxxxx");
	
	// ---> TIPO EJECUCION
	public static final String TIPOEJECUCION_LABEL        	= addError(11000, "pro.tipoEjecucion.label");
	public static final String TIPOEJECUCION_DESTIPOEJECUCION  	= addError(11001, "pro.tipoEjecucion.desTipoEjecucion.label");
	//	<--- TIPO EJECUCION
	
	// ---> TIPO PROGEJEC
	public static final String TIPOPROGEJEC_LABEL        	= addError(11002, "pro.tipoProgEjec.label");
	public static final String TIPOPROGEJEC_DESTIPOPROGEJEC  	= addError(11003, "pro.tipoProgEjec.desTipoProgEjec.label");
	//	<--- TIPO PROGEJEC
	
	// ---> PROCESO
	public static final String PROCESO_LABEL        	= addError(11004, "pro.proceso.label");
	public static final String PROCESO_CODPROCESO  	= addError(11005, "pro.proceso.codProceso.label");
	public static final String PROCESO_DESPROCESO  	= addError(11006, "pro.proceso.desProceso.label");
	public static final String PROCESO_ESASINCRONICO  	= addError(11007, "pro.proceso.esAsincronico.label");
	public static final String PROCESO_TIPOEJECUCION  	= addError(11008, "pro.proceso.tipoEjecucion.label");
	public static final String PROCESO_DIRECTORIOINPUT  	= addError(11009, "pro.proceso.directorioInput.label");
	public static final String PROCESO_CANTPASOS  	= addError(11010, "pro.proceso.cantPasos.label");
	public static final String PROCESO_TIPOPROGEJEC  	= addError(11011, "pro.proceso.tipoProgEjec.label");
	public static final String PROCESO_CLASSFORNAME  	= addError(11012, "pro.proceso.classForName.label");
	public static final String PROCESO_SPVALIDATE  	= addError(11013, "pro.proceso.spValidate.label");
	public static final String PROCESO_SPEXECUTE  	= addError(11014, "pro.proceso.spExecute.label");
	public static final String PROCESO_SPRESUME  	= addError(11015, "pro.proceso.spResume.label");
	public static final String PROCESO_SPCANCEL  	= addError(11016, "pro.proceso.spCancel.label");
	public static final String PROCESO_CRONEXPRESSION 	= addError(11017, "pro.proceso.cronExpression.label");
	//	<--- PROCESO
	
	//	---> CORRIDA
	public static final String CORRIDA_PROCESO  	= addError(0, "pro.corrida.proceso.label");
	public static final String CORRIDA_LABEL  	    = addError(0, "pro.corrida.label");	
	
	public static final String CORRIDA_FECHA  	= addError(11018, "pro.corrida.fecha.label");
	public static final String CORRIDA_HORA  	= addError(11019, "pro.corrida.hora.label");
	public static final String CORRIDA_NO_PERMITE_RETROCEDER_PASO = addError(11020, "pro.corrida.noPermiteRetrocederPaso");
	public static final String CORRIDA_NO_PERMITE_REINICIAR_PASO   = addError(11021, "pro.corrida.noPermiteReiniciarPaso");
	
	public static final String CORRIDA_NO_PERMITE_ACTIVAR_PASO   = addError(11021, "pro.corrida.noPermiteActivarPaso");
	public static final String CORRIDA_NO_PERMITE_CANCELAR_PASO   = addError(11021, "pro.corrida.noPermiteCancelarPaso");
	public static final String CORRIDA_NO_PERMITE_SIGUIENTE_PASO   = addError(11021, "pro.corrida.noPermiteSiguientePaso");
	//	<--- CORRIDA
	
	//	---> PASO CORRIDA	
	public static final String PASO_CORRIDA_NO_ENCONTRADO = addError(11030, "pro.pasoCorrida.noEncontrado");
	//	<--- PASO CORRIDA	
	
	//	---> ENVIO ARCHIVOS
	public static final String ENVIO_ARCHIVOS_LISTA_VACIA = addError(11031, "pro.envioArchivosAdapter.listaVacia");
	public static final String ENVIO_ARCHIVOS_USUARIO 	  = addError(11032, "pro.envioArchivosAdapter.usuario.label");
	public static final String ENVIO_ARCHIVOS_PASSWORD 	  = addError(11033, "pro.envioArchivosAdapter.password.label");
	public static final String ENVIO_ARCHIVOS_DESTINO 	  = addError(11034, "pro.envioArchivosAdapter.destino.label");
	public static final String ENVIO_ARCHIVOS_SCRIPTERROR = addError(11035, "pro.envioArchivosAdapter.scriptError.label");
	//	<--- ENVIO ARCHIVOS
}
