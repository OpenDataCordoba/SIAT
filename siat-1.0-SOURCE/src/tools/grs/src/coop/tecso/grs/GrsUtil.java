//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.grs;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;


/**
 * Util de GRS
 * @author Andrei 
 * 
 */
public class GrsUtil {

	/**
	 * Interpreta un array de objectos, los toma de a pareces consecutivos y los
	 * guarda en un Map<K, V> segun: key=args[i], value=args[i+1].
	 * 
	 * @param <K>
	 *            El tipo de la clave del mapa.
	 * @param <V>
	 *            El tipo del valor del mapa.
	 * @param args
	 *            Lista de objecto a meter dentro del mapa. Debe ser un numero
	 *            par ordenados (k1, v1, k2, v2, ...)
	 * @return
	 */
	public static <K, V> Map<K, V> atomap(Object... args) {
		Map<K, V> ret = new HashMap<K, V>();
		for (int i = 0; i < args.length; i++) {
			K k = (K) args[i];
			V v = (V) args[++i];
			ret.put(k, v);
		}
		return ret;
	}

	/**
	 * Obtiene el numbre del metodo desde el cual fue invocado este metodo.
	 * NOTA: Este metodo esta disponible solo a fines de loggear en modo debug,
	 * ya que es poco performante.
	 */
	public static String currentMethodName() {
		String funcName = new Exception().fillInStackTrace().getStackTrace()[1].getMethodName();
		return funcName;
	}
	
	static public String stackTrace(Throwable t) {
		final Writer result = new StringWriter();
		t.printStackTrace(new PrintWriter(result));
		return result.toString();
	}

	static public String findFilename(String filename) {
		String ret;
		//System dir
		if (filename.startsWith("<")) {
			ret = filename.substring(1, filename.length() - 1);
			ret = Grs.GrsPath + File.separator + "grs" + File.separator + ret;
			return ret;
		}
		
		return filename;
	}

}
