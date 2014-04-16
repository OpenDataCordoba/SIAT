//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class RodError extends DemodaError {
	// Use Codigos desde 4000 hasta 4999
	//static public String XXXXXX_XXXX_XXX   = addError(4000, "siat.base.xxxxxx");
	
	public static final String TRAMITERA_LABEL = addError(0, "rod.tramiteRA.label");
	public static final String TRAMITERA_NUMERO_TRAMITERA = addError(0, "rod.tramiteRA.nroTramite.label");
	public static final String TRAMITERA_NUMERO_COMUNA = addError(0, "rod.tramiteRA.nroComuna.label");
	public static final String TRAMITERA_TIPOTRAMITE = addError(0, "rod.tramiteRA.tipoTramite.label");
	public static final String DOMICILIO_VALIDO = addError(0, "rod.tramiteRA.DDomicilio.domicilioValido");
	public static final String HISESTTRA_FECHA = addError(0, "rod.tramiteRA.hisEtsTra.fecha.label");
	public static final String ESTADOTRAMITE_LABEL = addError(0, "rod.estadoTramiteRA.title");
	public static final String PROPIETARIO_LABEL = addError(0, "rod.propietario.label");
	public static final String PROPIETARIO_APELLIDOORAZON = addError(0,"rod.tramiteRA.CApellidoORazon.label");
	public static final String PROPIETARIO_NRODOC = addError(0,"rod.tramiteRA.CNroDoc.label");
	public static final String PROPIETARIO_TIPODOC = addError(0,"rod.tramiteRA.CTipoDoc.label");
	public static final String EXISTE_PROPIETARIO = addError(0,"rod.tramiteRA.existePropietario.label");
	public static final String TRAMITERA_FECHA = addError(0,"rod.tramiteRA.fecha.label");
	public static final String PERSONA_VALIDA = addError(0,"rod.tramiteRA.personaValida");
	public static final String PERSONA_INVALIDA = addError(0,"rod.tramiteRA.personaInvalida");
	public static final String VALIDAR_PERSONA = addError(0,"rod.tramiteRA.validarPersona");
	public static final String AGREGAR_PROPIETARIOACTUAL_PRINCIPAL = addError(0,"rod.tramiteRA.agregarPropietarioActualPrincipal");
	public static final String AGREGAR_PROPIETARIOANTERIOR_PRINCIPAL = addError(0,"rod.tramiteRA.agregarPropietarioAnteriorPrincipal");
}

