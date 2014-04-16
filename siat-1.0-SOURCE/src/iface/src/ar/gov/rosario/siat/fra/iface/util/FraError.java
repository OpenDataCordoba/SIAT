//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.util;


import coop.tecso.demoda.iface.error.DemodaError;


/**
 * En esta clase se definen las descripciones de los errores que estas asociaos a los VO.
 * 
 * @author Tecso Coop. Ltda.
 * 
 */
public class FraError extends DemodaError {
	
	public static final String FRASE_LABEL = addError(0, "fra.frase.label");
	public static final String FRASE_TITLE = addError(0, "fra.frase.title");
	public static final String FRASE_LABEL_CODFRASE = addError(0, "fra.frase.codFrase.label");
	public static final String FRASE_REF_CODFRASE = addError(0, "fra.frase.codFrase.ref");
	public static final String FRASE_LABEL_DESFRASE = addError(0, "fra.frase.desFrase.label");
	public static final String FRASE_REF_DESFRASE = addError(0, "fra.frase.desFrase.ref");
	public static final String FRASE_LABEL_MODULO = addError(0, "fra.frase.modulo.label");	
	public static final String FRASE_LABEL_PAGINA = addError(0, "fra.frase.pagina.label");	
	public static final String FRASE_LABEL_ETIQUETA = addError(0, "fra.frase.etiqueta.label");	
	public static final String FRASE_LABEL_VALOR = addError(0, "fra.frase.valor.label");
	
	public static final String MSG_PUBLICAR = addError(0, "fra.fraseEditAdapter.msgPublicar");
	
}

