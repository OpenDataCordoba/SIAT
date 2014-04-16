//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.frm.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class FrmError extends DemodaError {

	public static final String FORMULARIO_CODFORMULARIO = addError(4000, "frm.formulario.codFormulario.label");
	public static final String FORMULARIO_DESFORMULARIO = addError(4000, "frm.formulario.desFormulario.label");
	public static final String FORMULARIO_LABEL = addError(4000, "frm.formulario.label");
	public static final String FORMULARIO_DESIMP = addError(4000, "frm.desImp.label");
	
	public static final String FORCAM_LABEL = addError(4000, "frm.forCam.label");
	public static final String FORCAM_CODFORCAM = addError(4000, "frm.forCam.codForCam.label");
	public static final String FORCAM_DESFORCAM = addError(4000, "frm.forCam.desForCam.label");
	public static final String FORCAM_FECHAHASTA_LABEL = addError(4000, "frm.forCam.fechaHasta.label");
	public static final String FORCAM_FECHADESDE_LABEL = addError(4000, "frm.forCam.fechaDesde.label");
	public static final String FORCAM_UNICO_EN_RANGO = addError(4000, "frm.forCam.unicoEnRango.label");
	
	
}

