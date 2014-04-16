//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.base.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class BaseError extends DemodaError {
	//Use Codigos desde 000 hasta 999
	//static public String XXXXXX_XXXX_XXX   = addError(0, "base.xxxxxx");
	
	public static final String MSG_ELIMINAR = addError(000, "base.eliminar");
	public static final String MSG_ACTIVAR = addError(001, "base.activar");
	public static final String MSG_DESACTIVAR = addError(002, "base.desactivar");
	public static final String MSG_QUITAR = addError(001, "base.quitar");
	public static final String MSG_CONCILIAR = addError(001, "base.conciliar");
	public static final String MSG_CAMPO_REQUERIDO = addError(003, "base.requerido");
	public static final String MSG_CAMPO_REQUERIDOS_EXCLUYENTES = addError(003, "base.requeridosExcluyentes");
	public static final String MSG_LISTA_DETALLE_REQUERIDO = addError(003, "base.listaDetalleRequerida");
	
	
	public static final String MSG_CAMPO_UNICO = addError(1006, "base.unico");
	public static final String MSG_CAMPO_UNICOS2 = addError(1007, "base.unicos2");
	public static final String MSG_CAMPO_UNICOS3 = addError(1008, "base.unicos3");
	
	public static final String MSG_VALORMAYORQUE = addError(003, "base.valorMayorQue");
	public static final String MSG_VALORMAYORIGUALQUE = addError(003, "base.valorMayorIgualQue");
	public static final String MSG_VALORMENORQUE = addError(003, "base.valorMenorQue");
	public static final String MSG_VALORMENORIGUALQUE = addError(003, "base.valorMenorIgualQue");
	public static final String MSG_VALORMENORQUECERO = addError(003, "base.valorMenorQueCero");
	public static final String MSG_VALORMENORIGUALQUECERO = addError(003, "base.valorMenorIgualQueCero");
	
	public static final String MSG_IGUALQUE = addError(003, "base.valorIgualQue");
	
	public static final String MSG_ELIMINAR_REGISTRO_ASOCIADO   = addError(003, "base.eliminarRegistroAsociado");	
	public static final String MSG_ELIMINAR_REGISTROS_ASOCIADOS = addError(003, "base.eliminarRegistrosAsociados");
	public static final String MSG_MODIFICAR_REGISTROS_ASOCIADOS = addError(003,"base.modificarRegistrosAsociados");
	public static final String MSG_ELIMINAR_REGISTROS_ASOCIADOS_WARN = addError(003,"base.eliminarRegistrosAsociados.warn");
	
	public static final String MSG_FORMATO_CAMPO_INVALIDO = addError(003, "base.formatoInvalido");
	public static final String MSG_RANGO_INVALIDO = addError(003, "base.rangoInvalido");
	public static final String MSG_FUERA_DE_DOMINIO = addError(016, "base.fueraDeDominio");
	
	public static final String MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO = addError(003, "base.formatoPorcentajeInvalido");
		
	public static final String MSG_FECHA_ACTUAL = addError(017, "base.fechaActual");
	
	public static final String MSG_NO_ENCONTRADO = addError(18, "base.noEncontrado");
	
	public static final String MSG_SOLAPAMIENTO_VIGENCIAYVALOR = addError(0, "base.solapamientoVigenciaYValor");
	public static final String MSG_SOLAPAMIENTO_VIGENCIA = addError(0, "base.solapamientoVigencia");
	
	public static final String FECHA_DESDE = addError(017, "base.fechaDesde");
	public static final String FECHA_HASTA = addError(017, "base.fechaHasta");
	public static final String FECHA_ALTA = addError(017, "base.fechaAlta");
	public static final String FECHA_BAJA = addError(017, "base.fechaBaja");	
	
	public static final String ESTADO_INVALIDO = addError(017, "base.estadoInvalido");
	
	public static final String MSG_ERROR_CONCURRENCIA = addError(017, "base.errorConcurrencia");
	public static final String MSG_FUNCION_NODISPONIBLE = addError(29, "base.funcionNoDisponible");
	
	public static final String FORMULARIO_LARGO_CAMPO_INVALIDO = addError(00, "base.formulario.largoCampoInvalido");
	public static final String MSG_REINGRESAR_INDET = addError(00, "base.indet.reingresar");
	public static final String MSG_DESGLOCE_INDET = addError(00, "base.indet.desgloce");
	public static final String MSG_VUELTA_ATRAS_INDET = addError(00, "base.indet.vueltaAtras");
	
	public static final String MSG_FILTROS_REQUERIDOS = addError(20, "base.filtrosRequeridos");
	
	public static final String MSG_SOLAPAMIENTO_INDICE= addError(21, "base.solapamientoIndice");
	
	
}
