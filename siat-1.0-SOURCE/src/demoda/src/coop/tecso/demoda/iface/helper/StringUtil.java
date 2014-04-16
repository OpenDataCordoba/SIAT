//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import coop.tecso.demoda.iface.model.Datum;


public class StringUtil {
    
	static Logger log = Logger.getLogger(StringUtil.class);

	public static String SELECT_OPCION_SELECCIONAR = "Seleccionar...";
	public static String SELECT_OPCION_TODOS = "Todos";
	public static String SELECT_OPCION_TODAS = "Todas";
	public static String SELECT_OPCION_NULA = "";
	public static String SELECT_OPCION_NINGUNO = "<Ninguno>";
	public static String SELECT_OPCION_NINGUNA="Ninguna";
	
	public static Long 	 NO_POSEE_ID = new Long(-2);
	public static String NO_POSEE_DESCRIPCION = "No Posee";
	
	public static String ENABLED = "enabled";
	public static String DISABLED = "disabled";
	
	/**
	 * Mapa utilizado por el metodo genCodBarForTxt  
	 *
	 * */
	private static String[] mapCodBar = {// 00		 01		  02	   03		04		 05
										"nnWWn" ,"NnwwN" ,"nNwwN", "NNwwn", "nnWwN", "NnWwn"
										 // 06		 07		  08	   09		10		 11
									  , "nNWwn", "nnwWN", "NnwWn", "nNwWn" ,"wnNNw", "WnnnW"
									  	 // 12		 13		  14	   15		16		 17
									  , "wNnnW", "WNnnw", "wnNnW", "WnNnw", "wNNnw", "wnnNW"
									  	 // 18		 19		  20	   21		22		 23
									  , "WnnNw", "wNnNw", "nwNNw", "NwnnW", "nWnnW", "NWnnw"
									  	 // 24		 25		  26	   27		28		 29
									  , "nwNnW", "NwNnw", "nWNnw", "nwnNW", "NwnNw", "nWnNw"
									  	 // 30		 31		  32	   33		34		 35
									  , "wwNNn", "WwnnN", "wWnnN", "WWnnn", "wwNnN", "WwNnn"
									  	 // 36		 37		  38	   39		40		 41
									  , "wWNnn", "wwnNN", "WwnNn", "wWnNn", "nnWNw", "NnwnW"
									  	 // 42		 43		  44	   45		46		 47
									  , "nNwnW", "NNwnw", "nnWnW", "NnWnw", "nNWnw", "nnwNW"
									  	 // 48		 49		  50	   51		52		 53
									  , "NnwNw", "nNwNw", "wnWNn", "WnwnN", "wNwnN", "WNwnn"
									  	 // 54		 55		  56	   57		58		 59
									  , "wnWnN", "WnWnn", "wNWnn", "wnwNN", "WnwNn", "wNwNn"
									  	 // 60		 61		  62	   63		64		 65
									  , "nwWNn", "NwwnN", "nWwnN", "NWwnn", "nwWnN", "NwWnn"
									  	 // 66		 67		  68	   69		70		 71
									  , "nWWnn", "nwwNN", "NwwNn", "nWwNn", "nnNWw", "NnnwW"
									  	 // 72		 73		  74	   75		76		 77
									  , "nNnwW", "NNnww", "nnNwW", "NnNww", "nNNww", "nnnWW"
									  	 // 78		 79		  80	   81		82		 83
									  , "NnnWw", "nNnWw", "wnNWn", "WnnwN", "wNnwN", "WNnwn"
									  	 // 84		 85		  86	   87		88		 89
									  , "wnNwN", "WnNwn", "wNNwn", "wnnWN", "WnnWn", "wNnWn"
									  	 // 90		 91		  92	   93		94		 95
									  , "nwNWn", "NwnwN", "nWnwN", "NWnwn", "nwNwN", "NwNwn"
									  	 // 96		 97		  98	   99
									  , "nWNwn", "nwnWN", "NwnWn", "nWnWn"};

	/**
	 * Mapa utilizado por el metodo de compresion de codigo de barra para afip.  
	 * 
	 * */
	private static String[] mapCompCodBar = {// 00		 01		  02	   03		04		 05
											   "!"   ,  "\""  ,   "#"   ,  "$"  ,  "%"   ,  "&"
											 // 06		 07		  08	   09		10		 11
											,  "'"   ,  "("   ,   ")"   ,  "*"  ,  "+"   ,  ","
										  	 // 12		 13		  14	   15		16		 17
										    ,  "-"   ,  "."   ,   "/"   ,  "0"  ,  "1"   ,  "2"
										  	 // 18		 19		  20	   21		22		 23
										    ,  "3"   ,  "4"   ,   "5"   ,  "6"  ,  "7"   ,  "8"
										  	 // 24		 25		  26	   27		28		 29
										    ,  "9"   ,  ":"   ,   ";"   ,  "<"  ,  "="   ,  ">"
										  	 // 30		 31		  32	   33		34		 35
										    ,  "?"   ,  "@"   ,   "A"   ,  "B"  ,  "C"   ,  "D"
										  	 // 36		 37		  38	   39		40		 41
										    ,  "E"   ,  "F"   ,   "G"   ,  "H"  ,  "I"   ,  "J"
										  	 // 42		 43		  44	   45		46		 47
										    ,  "K"   ,  "L"   ,   "M"   ,  "N"  ,  "O"   ,  "P"
										  	 // 48		 49		  50	   51		52		 53
										    ,  "Q"   ,  "R"   ,   "S"   ,  "T"  ,  "U"   ,  "V"
										  	 // 54		 55		  56	   57		58		 59
										    ,  "W"   ,  "X"   ,   "Y"   ,  "Z"  ,  "["   ,  "\\"
										  	 // 60		 61		  62	   63		64		 65
										    ,  "]"   ,  "^"   ,   "_"   ,  "`"  ,  "a"   ,  "b"
										  	 // 66		 67		  68	   69		70		 71
										    ,  "c"   ,  "d"   ,   "e"   ,  "f"  ,  "g"   ,  "h"
										  	 // 72		 73		  74	   75		76		 77
										    ,  "i"   ,  "j"   ,   "k"   ,  "l"  ,  "m"   ,  "n"
										  	 // 78		 79		  80	   81		82		 83
										    ,  "o"   ,  "p"   ,   "q"   ,  "r"  ,  "s"   ,  "t"
										  	 // 84		 85		  86	   87		88		 89
										    ,  "u"   ,  "v"   ,   "w"   ,  "x"  ,  "y"   ,  "z"
										  	 // 90		 91		  92	   93		94		 95
										    ,  "¡"   ,  "¢"   ,   "£"   ,  "¤"  ,  "¥"   ,  "¦"
										  	 // 96		 97		  98	   99
										    ,  "§"   ,  "¨"   ,   "©"   ,  "ª"};
	
    /**
     * Se fija si una String es null o esta con espacios vacios
     * @param string la String a comprobar 
     * @return true si es null o vacia
     */
    public static boolean isNullOrEmpty(String string) {
        
        final String VACIO = "";
        if (string == null || string.trim().equals(VACIO)) {
            return true;
        }
        return false;
    }
    
    /**
     * Quita todos los espacios en blanco sobrantes de una string de modo seguro
     * @param txt La String a cortar
     * @return Una string cortada o null si no tenia nada
     */
    public static String cut(String txt) {
        if (!isNullOrEmpty(txt)) {
            txt = txt.trim();
        } else {
            try {
                if (txt.length() > 0) {
                    txt = txt.trim();
                }
            } catch (Exception e) {
                
            }
        }
        return txt;
    }
    
    /**
     * Quita todos los espacios en blanco sobrantes de una string de modo seguro
     * y devuelve una string en mayusculas
     * 
     * @param txt
     *            La String a cortar
     * @return Una string cortada en mayusculas o null si no tenia nada
     */
    public static String cutAndUpperCase(String txt) {

        txt = cut(txt);
        if (!isNullOrEmpty(txt)) {
            txt = txt.toUpperCase();
        }
        return txt;
    }
    
    /**
     * Compara dos strings quitando los espacios en blanco y el case.<br>
     * Si una string es null y la otra es "" considera que son iguales
     * @param a
     * @param b
     * @return
     */
    public static boolean iguales(String a, String b) {
        
        String tmpa = cut(a);
        String tmpb = cut(b);
        
        if (isNullOrEmpty(tmpa) || isNullOrEmpty(tmpb)) {
            if (isNullOrEmpty(tmpa) && isNullOrEmpty(tmpb)) {
                return true;
            } else {
                return false;
            }
        }
        
        return (tmpa.compareToIgnoreCase(tmpb) == 0);
    }
    
    /**
     * Reemplaza caracteres no validos para el informix
     * @param txt
     * @return
     */
    public static String escapar(String txt) {
        
        if (!isNullOrEmpty(txt)) {
            String tmp = "";
            
            for (int i = 0; i < txt.length(); i++) {
                if (txt.charAt(i) != '\'') {
                    tmp += txt.charAt(i);
                } else {
                    tmp += "\'\'";
                }
            }
            return tmp;
        }
        return txt;
    }

    /**
     * Reemplaza caracteres no validos para el informix y pasa a upper el string
	 * con el metodo toUpperCase()
     * @param txt
     * @return
     */
    public static String escaparUpper(String txt) {
		return escapar(txt).toUpperCase();
	}
	
    public static boolean isInteger (String strToValidate) {
    	
		try {
			Integer strValidated = Integer.parseInt(strToValidate);
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}

    public static boolean isLong (String strToValidate) {
    	
		try {
			Long strValidated = Long.parseLong(strToValidate);
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
    
    public static boolean isDouble (String strToValidate) {
    	
		try {
			Double strValidated = Double.parseDouble(strToValidate);
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
    
    /**
     * reemplaza un substring por otro
     * @param str
     * @param pattern
     * @param replace
     * @return
     */
    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();
    
        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e+pattern.length();
        }
        result.append(str.substring(s));
        return result.toString();
    }
    
    /**
     * Si el string es mayor a la longitud pasada lo trunca sino lo devuelve como esta
     * @param str
     * @param pattern
     * @param replace
     * @return
     */
    public static String substring(String str,Integer longitud) {

    	String strReturn = "";
    	
    	if (str != null && longitud != null) {
    		
	    	if (str.length() > longitud) {
	    		strReturn = str.substring(0,longitud);
	    	} else {
	    		strReturn = str;
	    	}
	    	
    	}
    	
    	return strReturn;

    }
    
    /**
     * Devuelve un string con la logitud indicada si es mayor lo trunca,
     * sino le agrega espacios al comienzo de la cadena.
     * @param String str,int longitud 
     * @param pattern
     * @param replace
     * @return
     */
    public static String llenarConEspacios(String str,int longitud) {

    	String strReturn = "";
    	
    	if ( str != null ) {
    		
        	int lonStr = str.length();     		
    		
	    	if (lonStr < longitud) {
	    		
	    		strReturn = StringUtil.getStringEspaciosBanco(longitud - lonStr) + str ;

	    	} else if ( lonStr == longitud ) {
	    		
	    		strReturn = str;
	    		
	    	} else {
	    		
	    		strReturn = str.substring(0,10);
	    		
	    	}
	    	
    	}
    	
    	return strReturn;

    }
    
    /**
     * Devuelve un string de espacios en blanco con la longitud indicada
     * @param int longitud 
     * @param pattern
     * @param replace
     * @return
     */
    public static String getStringEspaciosBanco(int longitud) {

    	String strReturn = "";

    	for(int i=1 ; i <= longitud ; i++) {
    		strReturn = strReturn + " ";
    	}
    	
    	return strReturn;

    }
    
    /**
     * Devuelve true si el string pasado como parametro es un numero decimal 
     * con la cantidad de enteros y la cantidad de decimales indicadas
     * @param String strDoubleVal 
     * @param int cantEnteros
     * @param int cantDecimales
     * @return boolean
     */
    public static boolean isDecimalValido(String strDoubleVal ,int cantEnteros,int cantDecimales) {
    	
    		if ( strDoubleVal.matches( "[0-9]{1," + cantEnteros + "}[.][0-9]{0,"+ cantDecimales + "}" ) ) {
    			return true;
    		} else if (strDoubleVal.matches( "[0-9]{1," + cantEnteros + "}" ) ){
    			return true;
    		} else {	
    			return false;
    		}

    }

    /**
     * Devuelve un string al cual le cambia las comas por los puntos, si no
     * posee comas devuelve el string como esta
     * @param String strComaValue 
     * @return String
     */
    
    public static String parseComaToPoint(String strComaValue) {
    	
    	String strReturn = strComaValue;
    	
    	if ( strReturn != null ) {
    		
    		if ( strComaValue.contains(",") ) {
    			 strReturn = strComaValue.replace(',','.');
     		}
    		
    	}
    	
    	return strReturn;
    	
    }

    /**
     * Devuelve true si el string ingresado es un double con punto o coma 
     * ,la cantidad de enteros ingresada y la cantidad de decimales ingresada.
     * @param String strToValidate
     * @param int cantEnteros
     * @param int cantDecimales 
     * @return boolean
     */
    
    public static boolean isDouble (String strToValidate,int cantEnteros, int cantDecimales) {
    	
    	String strSinComa =  StringUtil.parseComaToPoint(strToValidate);
    	
    	if ( StringUtil.isDouble(strSinComa) && 
    			StringUtil.isDecimalValido(strSinComa,cantEnteros,cantDecimales) ) {
    		return true;
    	} else {
    		return false;
    	}
    	
    }

     /**
     * Devuelve un String con la cadena VALOR repetida la cantidad de veces pasada como parametro 
     * @param valor
     * @param cant
     * @return
     */
    public static String devuelveCadena(String valor, int cant){
    	String cadenaRet = "";
    	
    	for (int i = 1 ; i <= cant ; i++) {
    		cadenaRet = cadenaRet + valor;
    	}

    	return cadenaRet;
    }
    
    /**
     * Devuelve un String que redondea los decimales del Double
     * pasado como parametro en la cantidad de decimales 
     * del parametro cantDecimales, y completa con "0" la parte entera hasta completar cantEnteros, si es necesario.<br>
     * Ej: 123.34D , 5, 3  --> devuelve 00123,340 
     *   
     * 
     * @param Double doubleARedondear
     * @param int cantEnteros
     * @param int cantDecimales 
     * @return boolean
     */
    
    public static String redondearDecimales ( Double doubleARedondear,int cantEnteros,int cantDecimales ) {
    	
    	String stringReturn = "";
    	
    	if ( doubleARedondear != null ) { 
    	
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator(',');
            String formatoDouble = devuelveCadena("0", cantEnteros) + "." + devuelveCadena("0", cantDecimales);
	    	DecimalFormat formatoDoubles = new DecimalFormat(formatoDouble, simbolos);
	    	
	    	stringReturn = formatoDoubles.format(doubleARedondear);
    	}

    	return stringReturn;
    }

    /** Devueleve un string con la cantidad de numerales , que 
     *  indique el parametro 
     * 
     * @param cantCeros
     * @return
     */
    
    public static String devuelveNumerales (int cantNumerales) {
    	
    	String ceros = "";
    	
    	for (int i = 1 ; i <= cantNumerales ; i++) {
    		ceros = ceros + "#";
    	}

    	return ceros;
    	
    }    
 
    /**
     *  Formatea numero con el simbolo ',' para separacion decimal y '.' para separar miles.
     * 
     * @param doubleNumber
     * @return
     */
    public static String formatDoubleWithComa(Double doubleNumber) {
    	String strFormated = "";
    
    	if (doubleNumber != null) {
    		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    		simbolos.setDecimalSeparator(',');
    		simbolos.setGroupingSeparator('.');
    		DecimalFormat formatter = new DecimalFormat("###,###,###,##0.00", simbolos);
    		
    		strFormated = formatter.format(doubleNumber);
    	}
    	
    	return strFormated;    	
    }
    
    public static String formatDouble(Double doubleNumber) {
    	String strFormated = "";
    
    	if (doubleNumber != null) {
    		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    		simbolos.setDecimalSeparator('.');
    		DecimalFormat formatter = new DecimalFormat("##########.########", simbolos);
    		
    		strFormated = formatter.format(doubleNumber);
    	}
    	
    	return strFormated;    	
    }
    
    
    /**
     * Recibe un Double, la cantidad de caracteres para la parte entera (intpart) y la cantidad de decimales (decPart)
     * 
     * Devuelve un String de tamanio: intPart+decPart, sin punto y completado a la izquierda con ceros 
     * @return
     */
    public static String formatDoubleCblStyle(Double doubleNumber, int intPart, int decPart) {
    	
    	String strFormated = "";
    	
    	if ( doubleNumber != null ) { 
    	
            DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
            simbolos.setDecimalSeparator('.');
            // arma un string de 000000000.00
            String formatoDouble = devuelveCadena("0", intPart) + "." + devuelveCadena("0", decPart);
	    	DecimalFormat formatoDoubles = new DecimalFormat(formatoDouble, simbolos);

	    	// obtiene el string formateado
	    	strFormated = formatoDoubles.format(doubleNumber);
	    	
	    	// le saca el puntito
	    	strFormated = strFormated.replaceAll("\\.", "");
			
    	}

    	return strFormated;
    	
    	
    }
    
    
    /**
     *  Formatea un double y devuelve un string utilizando como mascara del DecimalFormat el parametro pasado.
     * 
     * @param doubleNumber
     * @param mask
     * @return
     */
    public static String formatDouble(Double doubleNumber, String mask) {
    	String strFormated = "";
    
    	if (doubleNumber != null) {
    		DecimalFormatSymbols simbolos = new DecimalFormatSymbols();
    		simbolos.setDecimalSeparator('.');
    		DecimalFormat formatter = new DecimalFormat(mask, simbolos);
    		
    		strFormated = formatter.format(doubleNumber);
    	}
    	
    	return strFormated;    	
    }
    
    public static String formatDouble(Double valor, DecimalFormat decimalFormat){
		return ((valor == null)? "" : decimalFormat.format(valor));
	}
    
    public static String formatInteger(Integer integerNumber){
    	String strFormated = "";
    	if (integerNumber != null)
    		strFormated = integerNumber.toString();
    	
    	return strFormated;    	
    }
    
    public static String formatFloat(Float floatNumber){
    	String strFormated = "";
    	if (floatNumber != null)
    		strFormated = floatNumber.toString();
    	
    	return strFormated;    	
    }
    
    public static String formatLong(Long longNumber){
    	String strFormated = "";
    	if (longNumber != null)
    		strFormated = longNumber.toString();
    	
    	return strFormated;    	
    }
 	
    /**
     * ejemplo:   formatLong(1, 3) -> "001";
     *            formatLong(1000, 3) -> "1000"; 
     * @param longNumber
     * @param totalLength
     * @return
     */
    public static String formatLong(Long longNumber, int totalLength){
    	String complete = "0";
    	StringBuilder sb = new StringBuilder();
    	
    	if (longNumber != null)
    		sb.append(longNumber.toString());
    	for(int i = sb.length(); i<totalLength; i++) {
    		sb.insert(0, complete);
    	}
    	return sb.toString();    	
    }

    /** Obtiene un substring a partir de un string
     *  cortandolo donde aparece el stringHasta
     * 
     * @param str
     * @param charHasta
     * @return
     */
    public static String substringHasta(String strCompleto, char charHasta) {
    	String strReturn = null;
    	
    	if (strCompleto != null) {
    		
    		int indexOfStr = strCompleto.indexOf(charHasta);
    		
    		if (indexOfStr != -1) {
    			strReturn = strCompleto.substring(0,indexOfStr);
    		} else {
    			strReturn = strCompleto;
    		}
	    	
    	}
    	return strReturn;
    }
    
    /** Obtiene un substring a partir de un string
     *  comenzado despues de donde aparece el charDesde
     * 
     * @param str
     * @param charDesde
     * @return
     */
    public static String substringDesde(String strCompleto, char charDesde) {
	    String strReturn = null;
	    
    	if (strCompleto != null) {
    		
    		int indexOfStr = strCompleto.indexOf(charDesde);
    		
    		if (indexOfStr != -1) {
    			strReturn = strCompleto.substring(indexOfStr + 1, strCompleto.length());
    		} else {
    			strReturn = strCompleto;
    		}
	    	
    	}
    	return strReturn;
    }
    
    public static String getValorOrNoPosee(String valor){    	
    	if (valor == null || "".equals(valor.trim())){
    		return "No Posee";
    	} else {
    		return valor;
    	}
    	
    }

    public static String getActionPath (String accion, String metodo ) {
		String actionPath = accion + ".do?method=" + metodo;
		return actionPath;
	}
    
    public static boolean isNumeric (String strToValidate) {
    	
		try {
			if ( isInteger(strToValidate) ||
					isDouble(strToValidate) ||
						isLong(strToValidate))				
				return true;
			else
				return false;			
			
		} catch (Exception e) {
			return false;
		}
	}
    
    /**
     * Completa la cadena recibida con ceros a la izquierda hasta llegar a la longitud deseada.
     * 
     * @author Cristian
     * @param valor
     * @param cantidadCeros
     * @return
     */
    public static String completarCerosIzq(String valor, int longitud ){
    	
    	try {
    	
    		String valorRet = valor;
    		
    		if (valor.length() < longitud){
	        	for(int i= valor.length() ; i < longitud ; i++) {
	        		valorRet = "0" + valorRet;
	        	}
    		}
    		
    		return valorRet;
    	} catch (Exception e) {
			return valor;
		}
    }
    
    /**
     * Quita los ceros de la izquierda de una cadena de forma numerica.
     * 
     * @author Cristian
     * @param valor
     * @return
     */
    public static String quitarCerosIzq(String valor){
    	try {
    		
    		String valorRet = valor.trim();
    		
    		if (valorRet != null && valorRet.length() > 0){
    			if (valorRet.startsWith("0")){
    				return quitarCerosIzq(valorRet.substring(1, valorRet.length()));
    			} 
    		} 
    		
    		return valorRet;
    		
    	} catch (Exception e) {
			return valor;
		}
    }
    
    public static String completarCaracteresIzq(String valor, int longitud, Character caracter ){
    	
    	try {
    	
    		String valorRet = valor;
    		
    		if (valor.length() < longitud){
	        	for(int i= valor.length() ; i < longitud ; i++) {
	        		valorRet = caracter + valorRet;
	        	}
    		}
    		
    		return valorRet;
    	} catch (Exception e) {
			return valor;
		}
    }

    public static String completarCaracteresDer(String valor, int longitud, Character caracter ){
    	
    	try {
    	
    		String valorRet = valor;
    		
    		if (valor.length() < longitud){
	        	for(int i= valor.length() ; i < longitud ; i++) {
	        		valorRet = valorRet + caracter;
	        	}
    		}
    		
    		return valorRet;
    	} catch (Exception e) {
			return valor;
		}
    }

    /**
     * Recibe un n&uacute;mero como parametro y calcula a partir de este el d&iacute;gito verificador (DV) 
     * por M&oacute;dulo 11
     * 
     * @param numero
     * @return digitoVerificador (DV)
     */
    public static String calcularDigitoVerificador(String numero) {
    
    	/* Ejemplo: 
    	  +---+---+---+---+---+---+   +---+<p>
     	  | 2 | 6 | 1 | 5 | 3 | 3 | - | 9 |
          +---+---+---+---+---+---+   +---+
            |   |   |   |   |   |
           x7  x6  x5  x4  x3  x2
            |   |   |   |   |   |
          =14 =36  =5 =20  =9  =6
            +---+---+---+---+---+-> = (90 x 10) / 11 = 81, resto 9 => DV = 9 
    	*/

    	StringBuffer result = new StringBuffer();
    	int length = numero.length();
    	long valorTemp = 0;
    	long digitoVerificador = 0;
    	int c=2;
    
    	try{
    		for(int i = length-1; i>=0; i--){
    			valorTemp = valorTemp + Long.valueOf(numero.substring(i,i+1))*c;
    			c++;
    		}
    		valorTemp = valorTemp * 10;

    		if(valorTemp==0){
    			return null;
    		}
    		digitoVerificador = valorTemp%11;    	

    	}catch(Exception e){
    		return null;
    	}
    	
    	result.append(digitoVerificador);
       	
    	return result.toString();
    }

    
    /**
     * Recibe un n&uacute;mero como parametro y calcula a partir de este el d&iacute;gito verificador (DV) 
     * por M&oacute;dulo 10 de AFIP
     * 
     * Soporta Strings de hasta 100 posiciones
     * 
     * @param numero
     * @return numero+digitoVerificador )
     */
    public static String agregaDVMod10AFIP(String numero) {
 
    	int[] serie={ 1,  2,  3,  4,  5,  6,  7,  8,  9,
    			     11, 12, 13, 14, 15, 16, 17, 18, 19,
    			     21, 22, 23, 24, 25, 26, 27, 28, 29,
    			     31, 32, 33, 34, 35, 36, 37, 38, 39,
    			     41, 42, 43, 44, 45, 46, 47, 48, 49,
    			     51, 52, 53, 54, 55, 56, 57, 58, 59,
    			     61, 62, 63, 64, 65, 66, 67, 68, 69,
    			     71, 72, 73, 74, 75, 76, 77, 78, 79,
    			     81, 82, 83, 84, 85, 86, 87, 88, 89,
    			     91, 92, 93, 94, 95, 96, 97, 98, 99,
                     101 };
    	
    	int length =0 ;
    	int valorTemp = 0;
    	int digitoVerificador = 0;
    
    	try {
    		
    		length= numero.length();
    		if (length>100) {
    			return null;
    		}
    		for (int i = 0; i<=length-1; i++) {
    			valorTemp = valorTemp + Integer.valueOf(numero.substring(i,i+1)) * serie[i];
    		}

    		if(valorTemp==0){
    			return null;
    		}
    		digitoVerificador = valorTemp%10;    	

    	}catch(Exception e){
    		return null;
    	}
    	
    	return numero+new Integer(digitoVerificador).toString();
       	
    }

    
    
    /**
     * Recibe un n&uacute;mero como parametro , calcula a partir de este el d&iacute;gito verificador (DV) 
     * por M&oacute;dulo 11 y se lo agrega final.
     *
     * Nota: -si el DV esta entre 1 y 9, le agrega un cero a la izquierda
     *   	 -si el DV es 0 devuelve 11.	
     *
     * @param numero
     * @return digitoVerificador (DV)
     */
    public static String agregaDigitoVerificador(String numero) {

    	Integer dv = new Integer(calcularDigitoVerificador(numero));
    	
    	if (dv == 0) dv = 11;
    	
    	return numero + ((dv < 10) ? "0"+ dv.toString() : dv.toString());
    }
    
    /**
     * Recibe un n&uacute;mero como parametro, calcula a partir de este el d&iacute;gito verificador por M&oacute;dulo 11
     * y lo agrega al final separando ambos n&uacute;meros con el char separador pasado como parametro.
 	 *
     * @param numero
     * @param separador
     * @return string con (numero+Separador+digitoVerificador)
     */
    public static String agregaDigitoVerificador(String numero, String separador){
    	/* Ejemplo: 
    	  +---+---+---+---+---+---+   +---+<p>
     	  | 2 | 6 | 1 | 5 | 3 | 3 | - | 9 |
          +---+---+---+---+---+---+   +---+
            |   |   |   |   |   |
           x7  x6  x5  x4  x3  x2
            |   |   |   |   |   |
          =14 =36  =5 =20  =9  =6
            +---+---+---+---+---+-> = (90 x 10) / 11 = 81, resto 9 => DV = 9 
    	*/
    	StringBuffer result = new StringBuffer();
    	int length = numero.length();
    	long valorTemp = 0;
    	long digitoVerificador = 0;
    	int c=2;
    	if(separador.length()>1){
    		return null;
    	}
    	try{
    		for(int i = length-1; i>=0; i--){
    			valorTemp = valorTemp + Long.valueOf(numero.substring(i,i+1))*c;
    			c++;
    			//System.out.println("Numero en i:"+Long.valueOf(numero.substring(i,i+1))+" valorTemp:"+valorTemp+" c:"+c+" i:"+i);
    		}
    		valorTemp = valorTemp * 10;
    		//System.out.println("valorTemp:"+valorTemp);
    		if(valorTemp==0){
    			return null;
    		}
    		digitoVerificador = valorTemp%11;    	
    		//System.out.println("digitoVerif:"+digitoVerificador);
    	}catch(Exception e){
    		return null;
    	}
    	result.append(numero);
    	result.append(separador);
    	result.append(digitoVerificador);
    	
    	return result.toString();
    }

    /**
     * Recibe un n&uacute;mero como parametro, calcula a partir de este el d&iacute;gito verificador por M&oacute;dulo 11
     * con el M&eacute;todo que utiliza la Municipalidad de Rosario y lo agrega al final separando ambos n&uacute;meros
     * con el char separador pasado como parametro.
     * (Es una variante del m&eacute;todo original)
 	 *
     * @param numero
     * @param separador
     * @return string con (numero+Separador+digitoVerificador)
     */
    public static String agregaDigitoVerificadorMR(String numero, String separador){
    	/* Ejemplo: 
    	  +---+---+---+---+---+---+   +---+<p>
     	  | 2 | 6 | 1 | 5 | 3 | 3 | - | 9 |
          +---+---+---+---+---+---+   +---+
            |   |   |   |   |   |
           x6  x5  x4  x3  x2  x1
            |   |   |   |   |   |
          =12 =30  =4 =15  =6  =3
            +---+---+---+---+---+-> = 70 Mod 11 , resto x  => DV = 11-x 
    	*/
    	StringBuffer result = new StringBuffer();
    	int length = numero.length();
    	long valorTemp = 0;
    	long digitoVerificador = 0;
    	int c=1;
    	if(separador.length()>1){
    		return null;
    	}
    	try{
    		for(int i = length-1; i>=0; i--){
    			valorTemp = valorTemp + Long.valueOf(numero.substring(i,i+1))*c;
    			c++;
    			//System.out.println("Numero en i:"+Long.valueOf(numero.substring(i,i+1))+" valorTemp:"+valorTemp+" c:"+c+" i:"+i);
    		}
    		//System.out.println("valorTemp:"+valorTemp);
    		if(valorTemp==0){
    			return null;
    		}
    		digitoVerificador = 11-(valorTemp%11);    	
    		//System.out.println("digitoVerif:"+digitoVerificador);
    	}catch(Exception e){
    		return null;
    	}
    	result.append(numero);
    	result.append(separador);
    	result.append(digitoVerificador);
    	
    	return result.toString();
    }

    /**
     * - Recibe un array de Long  y devuelve un String con los elementos del arrary separados por coma.
     * - Funcion creada para ser utilizada para armar clausualas HQL del tipo IN(a, b, ...,n)
     * 
     * @author Cristian
     * @param arrLong
     * @return String
     */
    public static String getStringComaSeparate(Long[] arrLong){
    	
    	String strComaSepatare = "";
    	
    	if (arrLong != null){
	    	for (int i=0; i < arrLong.length; i++){
	    		strComaSepatare += arrLong[i];
				
				if (i < arrLong.length -1)
					strComaSepatare += ", ";
			}
    	}
    	
    	return strComaSepatare;
    }
    
    public static String getStringComaSeparate(String[] arrStr){
    	
    	String strComaSepatare = "";
    	
    	if (arrStr != null){
	    	for (int i=0; i < arrStr.length; i++){
	    		strComaSepatare += arrStr[i];
				
				if (i < arrStr.length -1)
					strComaSepatare += ", ";
			}
    	}
    	return strComaSepatare;
    }

    public static String getStringComaSeparate(List<String> listStr){
    	
    	String strComaSepatare = "";
    	
    	if (listStr != null){
	    	for (int i=0; i < listStr.size(); i++){
	    		strComaSepatare += listStr.get(i);
				
				if (i < listStr.size() -1)
					strComaSepatare += ", ";
			}
    	}
    	return strComaSepatare;
    }

    /**
     * Retorna el numero que llega como parametro, sin el/los punto/s, si es que tiene
     * @param numero
     * @return
     */
    public static String eliminarPuntos(Double numero){
    	String strReturn =null;
    	if ( numero != null ) {
    		strReturn = "";
    		String strNro = String.valueOf(numero);
    		char[] arrChar = strNro.toCharArray();
    		for(char c: arrChar){
    			if(c!='.')
    				strReturn +=c;
    		}
    	}
    	return strReturn;
    }

    /**
     * Devuelve un string al cual le cambia los puntos por las comas , si no
     * posee comas puntos el string como esta
     * @param String numero 
     * @return String
     */
    public static String parsePointToComa(String numero){
    	String strReturn = numero;
    	
    	if ( strReturn != null ) {
    		
    		if ( numero.contains(".") ) {
    			 strReturn = numero.replace('.',',');
     		}    		
    	}    	
    	return strReturn;
    }

    /**
     * Calcula el digito verificador para el nro de codigo de barra pasado como parametro y devuelve el nro de codigo de barra + digito verificador 
     * <br><br>Algoritmo: Calculo del digito verificador del codigo de barras (dv de 1 solo digito) - Descripcion del requerimiento del Banco.
     	<br>?	Usamos la posicion 48 del codigo de barras para poner el dv. 
     	<br>?	Especificacion: Calculo con ponderador 31 y modulo 10
     	<br><br>
			La rutina multiplica cada digito de la barra por 3 o 1 (alternativamente) y lo suma a un acumulador. 
			Despues resta de 10 el ultimo digito del acumulador (10 ? ult dig).
			Si este es mayor de cero, el resultado es el digito de verificacion.
			Si el ultimo digito del acumulador es = cero, el digito de verificacion es cero.
     * @param nro
     * @return
     */
    public static String agregarDigitoVerificadorCodBar(String nroCodBar){
    	
    	if (nroCodBar != null) {
    		
    		int acum =0;
    		int cantDigitos = nroCodBar.length();
    		int ponderador = 3;
    		// Calculamos el dv 
    		for(int i=0; i < cantDigitos; i++) {
    			int digitoActual = Integer.parseInt(String.valueOf(nroCodBar.charAt(i)));
    			acum +=digitoActual*ponderador;
    			ponderador = (ponderador == 3) ? 1 : 3;
    		}
    		int dv = acum % 10;    		
    		
			// Agregamos el dv a nroCodBar
			nroCodBar += (dv > 0) ? 10 - dv : 0;
    		
    		return nroCodBar;
    	}
    	
    	return null;
    }
    
    /**
     * Realiza la concatenacion de 2 string pudiendo ser nulos.
     * 
     * @param s1
     * @param s2
     * @return String
     */
    public static String concat(String s1, String s2){
    	String resultado = "";
    	if(s1 != null) resultado = s1;
    	if(s2 != null) resultado += s2;
    	return resultado;
    }

    /**
     * Verifica si el valor pasado como parametro tiene el formato ###/###/###/###.<br>
     * (Sin la SECCION al inicio)
     * @param value
     * @return
     * @author arobledo
     */
    public static boolean validaCatastralSinSeccion(String value){
    	if(!isNullOrEmpty(value)){
    		String regex = "\\d\\d\\d/\\d\\d\\d/\\d\\d\\d/\\d\\d\\d"; // \d --> digito entre 0..9
    		if(value.matches(regex))
    			return true;
    	}
    	return false;
    	
    }
    
    /**
     * Valida si una direccion de correo electronico es correcta. 
     * 
     * @param strMail
     * @return
     */
    public static boolean isValidMail(String strMail) {
    	
		try {
					
			if (strMail == null)
				return false;
			
		    //Set the email pattern string
		    Pattern p = Pattern.compile(".+@.+\\.[a-z]+");

		    //Match the given string with the pattern
		    Matcher m = p.matcher(strMail);

		    //check whether match is found 
		    boolean matchFound = m.matches();

		    if (matchFound)
		      return true;
		    else
		      return false;
			
		} catch (Exception e) {
			return false;
		}
	}
    
   /**
    * Genera, para el numero de codigo de barras numCodBar, una representacion 
	* del codigo de barras formada por n,N,w,W.
	* 
	* Ejemplo: 
	*	- Entrada: 780047408008400000010980800000011140702100702280
	*   - Salida:  NnnWwnnWWnnnwNWnnWNwwnNWnNnwWnnnWNwnnWWnnnWWnNnw
	*   		   wNnNwWnwnNWnwnNWnnnWWnnnWWnNnwwNWnnnWnnWNwnnNWwN
	*   		   wnnWnnWWnnnNWwnWnnWwnNWn
	*
	* @param numCodBar
	* @return String
	* @throws Exception
	* */
	public static String genCodBarForTxt(String numCodBar) throws Exception {
		try {
			String strCodBar = "";

			int cantDigitos = numCodBar.length();
			for(int i=0; i < cantDigitos - 1; i+= 2) {
				int elem = new Integer(numCodBar.substring(i, i + 2));
				strCodBar += mapCodBar[elem];
			}

			return strCodBar;
			
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * retorna una cadena trimeada, o vacia si se paso null.
	 * @param s
	 * @return
	 */
	public static String nulltrim(String s) {
		if (s==null) return "";
		return s.trim();
	}

	
	/** Formate el valor pasado como parametro, completando con ceros a la izquierda en cada una de
	 * sus partes (seccion/manzana/grafico/subdivision/subparcela)
	 * 
	 * @param value
	 * @return
	 */ 
	public static String getCatastralFormateada(String value){
		Datum datum = Datum.parseAtChar(value, '/');
		String[] catastral = new String[datum.getColNumMax()]; 
		catastral[0] = StringUtil.completarCerosIzq(datum.getCols(0), 2);
		for(int i=1;i<datum.getColNumMax();i++)
			catastral[i] = StringUtil.completarCerosIzq(datum.getCols(i), 3);
		value = "";
		for(int i=0;i<datum.getColNumMax()-1;i++)
			value += catastral[i]+"/";		
		value += catastral[datum.getColNumMax()-1];
		
		return value;
	}

	/**
	 * Devuelve el valor pasado como parametro con el simbolo "%" o el simbolo "por mil"
	 * @param valor
	 * @return
	 */
	public static String getValorPorcent(Double valor){
		
		if (valor == null)
			return "";	
		
		Double valorPorc=0D;
		if (valor.doubleValue()< 0.01D){ 
			valorPorc=  NumberUtil.truncate(valor*1000,2);
			return valorPorc.toString()+"&#8240;";
		}else{
			valorPorc=  NumberUtil.truncate(valor*100,2);
			return valorPorc.toString()+"%";
		}

	}
	
	/**
	 * Toma como parametro una cadena s de longitud n y retorna
	 * la cadena formada por: s mas (size - n) caracteres c.
	 * 
	 * Si size < n, trunca s.
	 * 
	 * @param String s
	 * @param Char c
	 * @param int size
	 * @return
   	 */
	public static String fillWithCharToRight(String s, char c, int size) {
    	
		if (s == null) s = "";
    	String suffix = "";
    	
    	int n = s.length(); 
    	
    	if (n > size) 
    		return s.substring(0, size);
    	
    	for (int i= n ; i < size ; i++) 
        		suffix += c;
        	
		return s + suffix;
	}

	/**
	 * Toma como parametro una cadena s de longitud n y retorna
	 * la cadena formada por: (size - n) caracteres c mas s.
	 * 
	 * Si size < n, trunca s.
	 * 
	 * @param String s
	 * @param Char c
	 * @param int size
	 * @return
   	 */
	public static String fillWithCharToLeft(String s, char c, int size) {
    	if (s == null) s = "";

    	String prefix = "";
    	
    	int n = s.length();
		
    	if (n > size) 
    		return s.substring(0, size);
    	
    	for (int i= 0 ; i < size - n; i++)
        		prefix += c;
		
		return prefix + s;
	}

	/**
	 * Toma como parametro una cadena s de longitud n y retorna
	 * la cadena formada por: s mas (size - n) espacios.
	 * 
	 * Si size < n, trunca s.
	 * 
	 * @param String s
	 * @param Char c
	 * @param int size
	 * @return
   	 */
	public static String fillWithBlanksToRight(String s, int size) {
    	return fillWithCharToRight(s, ' ', size);
	}

	/**
	 * Toma como parametro una cadena s de longitud n y retorna
	 * la cadena formada por: (size - n) espacios mas s.
	 * 
	 * Si size < n, trunca s.
	 * 
	 * @param String s
	 * @param Char c
	 * @param int size
	 * @return
   	 */
	public static String fillWithBlanksToLeft(String s, int size) {
		return fillWithCharToLeft(s, ' ', size);
	}

	/***
	 * Divide una string s en segmentos de longitud chunkSize.
	 * 
	 * @param String s
	 * @param int chunkSize
	 * @return
	 */
    public static String[] splitIntoChunks(String s, int chunkSize) {
    	String regex = "\\G.{1,"+  chunkSize + "}($|\\s)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(s);
        List<String> resultList = new ArrayList<String>();
        
        while (matcher.find()) 
        	resultList.add(fillWithBlanksToRight(matcher.group(), chunkSize));
        
        return (String[]) resultList.toArray(new String[0]);
    }

    /**
     * 
     * 
     * @return
     */
    public static String completarCeros(String valor, int longitud ){
    	
    	try {
    	
    		String valorRet = valor;
    		
    		if (valor.length() < longitud){
	        	for(int i= valor.length() ; i < longitud ; i++) {
	        		valorRet = "0" + valorRet;
	        	}
    		}
    		
    		return valorRet;
    	} catch (Exception e) {
			return valor;
		}
    }
    
    /**
     * Retorna un nodo XML con la siguiente estructura:
     * <tagName>content</tagName>
     * 
     * @param tagName
     * @param content
     * @return String
     */
	public static String writeXMLNode(String tagName, String content) {
		String xmlNode = ""; 
		xmlNode += "<" + tagName +">";
		xmlNode += writeXMLContent(content);
		xmlNode += "</"+ tagName +">";
		
		return xmlNode;
	}
	
    /**
     * Escapa una string para ser utilizada en 
     * archivos XML.
     * 
     * @param str
     * @return String
     */
	public static String writeXMLContent(String str) {
		return (str != null) ? escape(str) : ""; 
	}

	private static String escape(String str) {
		String esc = "";
		esc = str.replace("&" , "&amp;");   // &amp;	&
		esc = esc.replace("<" , "&lt;");    // &lt;		<
		esc = esc.replace(">" , "&gt;");    // &gt;		>
		esc = esc.replace("\"", "&quot;"); 	// &quot; 	"
		esc = esc.replace("'" , "&apos;");  // &apos; 	'
		
		return esc;
	}

    /**
     * Retorna el contenido del tag que esta 
     * en el buffer.
     * 
     * @param buffer
     * @param tagName
     * 
     * @return String
     */
	public static String getXMLContentByTag(String buffer, String tagName) {
        String strPattern = "<" + tagName + ">(?s)(.*)</"+ tagName +">";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(buffer);

        int pos = 0;
        while (matcher.find(pos)) {
                String atrAse = matcher.group(1);
                return atrAse;
        }
        
        return "";
	}
	
    /**
     * Modifica el contenido del tag que esta en el 
     * buffer por el valor pasado como parametro.
     * 
     * Si el tag aparece mas de una vez, deja una
     * unica ocurrencia. 
     * 
     * Si el tag no esta presente, lo agrega.
     * 
     * @param  buffer
     * @param  tagName
     * @param  newValue
     * @return String
     */
	public static String setXMLContentByTag(String buffer, String tagName, String newValue) {
		if (buffer == null)
			buffer = "";
		
		String strPattern = "<" + tagName + ">(?s)(.*)</"+ tagName +">";
        Pattern pattern = Pattern.compile(strPattern);
        Matcher matcher = pattern.matcher(buffer);
        
        String newNode = writeXMLNode(tagName,newValue);

        if (!matcher.find()) 
        	return buffer + newNode;
        
        return matcher.replaceAll(newNode);
	}


	/**
	 * Formatea numeros de cuenta desde la vista para consultar en la db.
	 * - Trimea.
	 * - Quita los ceros a la izquierda si es numerica.
	 * - Escapa caracteres invalidos y pasa a mayuscula.
	 * 
	 * @param valor
	 * @return
	 */
	public static String formatNumeroCuenta(String numeroCuenta){
    	try {
    		
    		String valorRet = numeroCuenta.trim();
    		
    		if (StringUtil.isNumeric(numeroCuenta))
    			valorRet = StringUtil.quitarCerosIzq(numeroCuenta);
			else
				valorRet = numeroCuenta;
    		
    		valorRet = escaparUpper(valorRet);
    		
    		return valorRet;
    		
    	} catch (Exception e) {
			return numeroCuenta;
		}
    }
	
	 /**
	    * Comprime el numero de codigo de barras numCodBar utilizando la compresion definida por AFIP. Si el numero de caracteres del codigo es impar se agrega un cero al final.
		* La salida es el codigo de barra comprimido entre "{}".
		* Ejemplo: 
		*	- Entrada: 60572703893637401122193234056078
		*   - Salida:  {]Z<$zEFI,74AC&]o}
		*
		* @param numCodBar
		* @return String
		* @throws Exception
		* */
		public static String genCodBarComprimidoForAfip(String numCodBar) {
			try {
				String strCodBar = "";

				int cantDigitos = numCodBar.length();
					
				// Si el numero de caracteres es impar se agrega un cero al final.
				if(cantDigitos%2!=0){
					numCodBar = numCodBar+"0";
					cantDigitos++;
				}
				
				for(int i=0; i < cantDigitos - 1; i+= 2) {
					int elem = new Integer(numCodBar.substring(i, i + 2));
					strCodBar += mapCompCodBar[elem];
				}

				// Se agregan caracteres al principio y al final que marcan al codigo como comprimido "{codBarComprimido}"
				strCodBar = "{"+strCodBar+"}";
				
				return strCodBar;
			} catch (Exception e) {
				return null;
			}
		}
		
	public static String stackTrace(Throwable e) {
    	StringWriter result = new StringWriter();
    	PrintWriter printWriter = new PrintWriter(result);
    	e.printStackTrace(printWriter);
    
    	return result.toString();
	}
	
}
