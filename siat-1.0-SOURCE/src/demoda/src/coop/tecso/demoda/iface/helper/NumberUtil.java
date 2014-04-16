//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.helper;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class NumberUtil {
	
	  /**
	   * Redondea un Double con la cantidad de decimales pasada como parametro
	   * @param value - valor a redondear
	   * @param decimalPlace -cant. de decimales a utilizar 
	   * @return
	   */
	  public static Double round(Double value, int decimalPlace){
		if (value == null)
			value = 0D;
		  
		Double power_of_ten = 1D;
	    while (decimalPlace-- > 0)
	       power_of_ten *= 10.0;
	    return Math.round(value * power_of_ten) / power_of_ten;
	  }
	  
	  public static Double roundNull(Double value, int decimalPlace){
			if (value == null)
				return value;
			return NumberUtil.round(value, decimalPlace);
	  } 
	  
	  /**
	   * Trunca un valor recibido en la cantidad de decimales recibidos. 
	   * 
	   * @author Cristian
	   * @param value
	   * @param decimalPlace
	   * @return
	   */
	  public static Double truncate(Double value, int decimalPlace){
		  
		  if (value == null)
			  return null;
		  
		  String formatoDouble = "#." + StringUtil.devuelveNumerales(decimalPlace); 
		  DecimalFormat df = new DecimalFormat(formatoDouble);

		  String strValue = StringUtil.parseComaToPoint(df.format(value));
		  Double ret  = new Double(strValue);
		  
		  return ret;
	  }
	  
	  /**
	   * Recibe un Double y Devuelve un String que lo representa en palabras.<br>
	   * Utiliza como separador de comas el "." y como moneda "pesos"
	   * @param numero
	   * @param toUpperCase si es "true" devuelve la cadena en MAYUSCULAS
	   * @return
	   * @author arobledo
	   */
	  public static String getNroEnPalabras(Double numero, boolean toUpperCase){
		  return getNroEnPalabras(numero, ".", "pesos", toUpperCase);
	  }
	  /**
	   * Recibe un Double y Devuelve un String que lo representa en palabras
	   * @param numero  - numero a convertir a letras
	   * @param separadorComa - letra que identifica la coma (puede ser "." o ",");
	   * @param moneda - Palabra para definir la moneda utilizada ( puede ser "Pesos", "Dolares", etc.)
	   * @param toUpperCase si es "true" devuelve la cadena en MAYUSCULAS
	   * @return - cadena vacia si numero o separadorComa son nulos o vacios.<br>
	   * 	Ej: "Uno Mil Doscientos Treinta y Cuatro pesos con  Cuarenta y Cinco centavos"
	   * @author arobledo
	   */
	  public static String getNroEnPalabras(Double numero, String separadorComa, String moneda, boolean toUpperCase){
		  if(numero==null || numero.doubleValue()<=0)
			  return "";
		  if(separadorComa==null)
			  return "";		  
		  String strNro = String.valueOf(numero);
		  int posicionComa = strNro.indexOf(separadorComa);
		  if(posicionComa<0)
			  return "";
		  String parteEntera = strNro.substring(0, posicionComa);
		  String parteDecimal = strNro.substring(posicionComa+1);
		  if(parteDecimal.length()==0)
			  parteDecimal="00";
		  if(parteDecimal.length()==1)
			  parteDecimal+="0";
			  
		  String nroEnPalabras=getNroEnPalabras(new BigDecimal(parteEntera))+" "+moneda+" con "+getNroEnPalabras(new BigDecimal(parteDecimal))+"  centavos";
		  return (toUpperCase?nroEnPalabras.toUpperCase():nroEnPalabras);
	  }
	    /**
	     * Recibe un BigDecimal y Devuelve un String que lo representa en palabras
	     */
	    public static String getNroEnPalabras(BigDecimal numero) {
	        String palabras = "";
			
	        if ( numero != null) {
	            
	        	int n = numero.intValue();
	            int cm,dm,um, cmi,dmi,umi, ce,de,un;
	            double r1,r2,r3,r4,r5,r6,r7,r8,r9;
	            // millones
	            cm=(int) (n/100000000);
	            r1=n%100000000;  // % es el modulo!
	            dm=(int) (r1/10000000);
	            r2=r1%10000000;
	            um=(int) (r2/1000000);
	            // cien miles
	            r3=r2%1000000;
	            cmi=(int) (r3/100000);
	            r4=r3%100000;
	            dmi=(int) (r4/10000);
	            r5=r4%10000;
	            umi=(int) (r5/1000);
	            // miles
	            r6=r5%1000;
	            ce=(int) (r6/100);
	            r7=r6%100;
	            de=(int) (r7/10);
	            r8=r7%10;
	            un=(int) (r8/1);
	//	        r9=r8%1;

	            if (n<1000000000 && n>=1000000)
	                palabras = letras(cm,dm,um) + " Millones " + letras(cmi,dmi,umi) + " Mil " + letras(ce,de,un);

	            if (n<1000000 && n>=1000)
	                palabras = letras(cmi,dmi,umi) + " Mil " + letras(ce,de,un);

	            if (n<1000 && n>=1)
	                palabras = letras(ce,de,un);

	            if (n==0)
	                palabras = "Cero";

	            return palabras;
	        } else 
				return "Sin monto establecido";
		}
	    
	    /**
	     * Funcion utilizada por getNroEnPalabras()
	     * @param c
	     * @param d
	     * @param u
	     * @return
	     */
	    private static String letras(int c, int d, int u) {
	        String lc = "";
	        String ld = "";
	        String lu = "";

	        switch (c) {
	              case 0: lc=" ";break;
	              case 1: {
	                  if (d==0 && u==0)
	                          lc="Cien";
	                  else
	                          lc="Ciento";}break;
	              case 2: lc="Doscientos";break;                                
	              case 3: lc="Trescientos";break;
	              case 4: lc="Cuatrocientos";break;
	              case 5: lc="Quinientos";break;
	              case 6: lc="Seiscientos";break;
	              case 7: lc="Setecientos";break;
	              case 8: lc="Ochocientos";break;
	              case 9: lc="Novecientos";break;
	        }

	        switch (d) {
	            case 0: ld=" ";break;
	            case 1:
	                { switch (u) {
	                      case 0:ld="Diez";break;
	                      case 1:ld="Once";break;
	                      case 2:ld="Doce";break;
	                      case 3:ld="Trece";break;
	                      case 4:ld="Catorce";break;
	                      case 5:ld="Quince";break;
	                      case 6:ld="Dieciseis";break;
	                      case 7:ld="Diecisiete";break;
	                      case 8:ld="Dieciocho";break;
	                      case 9:ld="Diecinueve";break;
	                      }
	                } break;

	            case 2:ld="Veinte";break;
	            case 3:ld="Treinta";break;
	            case 4:ld="Cuarenta";break;
	            case 5:ld="Cincuenta";break;
	            case 6:ld="Sesenta";break;
	            case 7:ld="Setenta";break;
	            case 8:ld="Ochenta";break;
	            case 9:ld="Noventa";break;
	        }

	        switch (u) {
	            case 0: lu=" ";break;
	            case 1: lu="Uno";break;
	            case 2: lu="Dos";break;
	            case 3: lu="Tres";break;
	            case 4: lu="Cuatro";break;
	            case 5: lu="Cinco";break;
	            case 6: lu="Seis";break;
	            case 7: lu="Siete";break;
	            case 8: lu="Ocho";break;
	            case 9: lu="Nueve";break;
	        }

	        if (d==1)
	            return lc+ld;

	        if (d==0 || u==0)
	            return lc+" "+ld+lu;
	        else
	            return lc+" "+ld+" y "+lu;
	    }

	    /**
	     *  Compara si dos n&uacutemeros "Double" son iguales considerando cierta tolerancia pasada como parametro.
	     *  <i>
	     *  <p>Ejemplo:</p><p> 	double1 = 3.4567 	double2= 3.457		tolerancia=0.01 </p>
	     *  <p>			|diferencia| = |double1 - double2| = |3.4567 - 3.457| = 0.0003</p>
	     *  <p>			|diferencia| < 0.01 => True</p> 
	     *  </i>
	     * @param doubleToValidate
	     * @param doubleToCompare
	     * @param tolerance
	     * @return boolean
	     */
	    public static boolean isDoubleEqualToDouble(Double doubleToValidate, Double doubleToCompare, Double tolerance){
	    	Double diference = doubleToValidate-doubleToCompare;
	    	if(diference.doubleValue()<0)
	    		diference *= -1;
	    	if(diference.doubleValue()<tolerance.doubleValue())
	    		return true;
	    	else
	    		return false;
	    }
	    
	    
	    public static Double addDoubles(Double d1, Double d2){
	    	if (d1 == null) return d2;
	    	if (d2 == null) return d1;
	    	return d1 + d2;
	    }
	    
	    /**
	     * Convierte el String a Double. Si no puede devuelve null. 
	     * 
	     * @param strValor
	     * @return
	     */
	    public static Double getDouble(String strValor){
	    	Double valor;
	    	try{
	    		strValor = strValor.replace(',', '.');
	    		valor = new Double(strValor);
	    	}catch(Exception e){
	    		valor = null;
	    	}
	    	return valor;
	    }

		/**
		 * Devuelve el valor Integer del valor pasado como parametro
		 * @param value
		 * @return null si no se puede parsear a Integer
		 * @author arobledo
		 */
	    public static Integer getInt(String value){
			try{
				if (value == null) return null;
				return Integer.valueOf(value.trim());
			}catch(NumberFormatException e){}
			return null;
		}
		
		/**
		 * Devuelve el valor Long del valor pasado como parametro
		 * @param value
		 * @return null si no se puede parsear a Long
		 * @author arobledo
		 */
	    public static Long getLong(String value){
			try{
				if (value == null) return null;
				return Long.valueOf(value.trim());
			}catch(NumberFormatException e){}
			return null;
		}
	    
	    /**
	     * Devuelve el valor mayor de un array de dobles, si en el array hay valores nulos no los tiene en cuenta
	     * Devuelve nulo si todos los valores del array son nulos o este esta vacio
	     * @param listDobles
	     * @return
	     */
	    public static Double getMayorValor(Double[] listDobles){
	    	Double mayor=null;
	    	for (Double doble:listDobles){
	    		if (mayor ==null){
	    			mayor = doble;
	    		}else if (doble != null && doble > mayor){
	    			mayor=doble;
	    		}
	    	}
	    	return mayor;
	    }
}
