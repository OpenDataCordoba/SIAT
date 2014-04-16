//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.error;


/**
 * Clase de constantes de errores
 * <p>Las constantes de error poseen el formato: <NumeroError><espacio><BundleKey> por ejemplo:
 * <p>1000 demoda.error.campo_requerido
 * <p>1001 demoda.error.fecha_invalida
 * <p>
 * <p>Cuando se implemente esta clase para cada aplicacion o para cada modulo, 
 * utilizar la funcion addError() para dar formato al mensaje.
 * Tambien utilizar errorNumber() y errorKey() para extaer el numero y key 
 * de la constante de error.
*/
public class DemodaError {
	public static String addError(long number, String key) {
		return number + " " + key;
	}

	/** Retorna el numero de error de una constante de error.
	 * <p>En caso de que no se puedea extraer el numero de error retorna -1. 
	*/
	public static long errorNumber(String errorConst) {
		try { 
			String[] tok = errorConst.split(" ");
			return Long.parseLong(tok[0]); 
		} catch(Exception e) {
			return -1;
		}
	}


	/** Retorna el key de error de una constante de error.
	 * <p>En caso de que no se puedea extraer el key de retorna 
	 * la cadena "demoda.bad_format_error"
	*/
	public static String errorKey(String errorConst) {
		try { 
			String[] tok = errorConst.split(" ");
			return tok[1]; 
		} catch (Exception e) {
			return "demoda.bad_format_error";
		}
	}
	
}
