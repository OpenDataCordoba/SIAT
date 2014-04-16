//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.text.SimpleDateFormat;
import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Estructura de datos para parsear los archivos de migracion.
 * Arma un arreglo de String colocando lo que lee del archivo de migracion y parseando por pipes '|'
 * @author Tecso Coop. Ltda.
 *
 */
public class Datum {

		private String[] cols = null;  
		private int colNumMax;
		
		 
		public Datum(int colNum){
			cols = new String[colNum];
			colNumMax = colNum;
		}
		
		 /**
		 * @param Fila (string) que se desea parsear.
		 */
		public static Datum parse(String row){
			if (row == null) return null;
			
			String rowParse = new String(row);
			int numPipes = 1;
			for(int i=0;i<row.length();i++){
				if(row.charAt(i)== '|')
					numPipes++;
			}
			Datum datum = new Datum(numPipes);
			/*
			String[] tmp = row.split("|");
			Datum datum = new Datum(tmp.length);
			datum.cols = tmp; 
			*/
			for(int i=0;i<numPipes;i++){
				datum.setCols(i, StringUtil.substringHasta(rowParse, '|'));
				rowParse = StringUtil.substringDesde(rowParse, '|');
			}
			
			return datum;
		}
		
		 /**
		 * Parsea el string recibido separandolo en string cada vez que encuentra el caracter 
		 * pasado como parametro.
		 *  
		 * @param Fila (string) que se desea parsear, parseChar (char) que se usara para parsear.
		 */
		public static Datum parseAtChar(String row, char parseChar){
			
			String rowParse = new String(row);
			int numPipes = 1;
			for(int i=0;i<row.length();i++){
				if(row.charAt(i)== parseChar)
					numPipes++;
			}
			Datum datum = new Datum(numPipes);
			for(int i=0;i<numPipes;i++){
				datum.setCols(i, StringUtil.substringHasta(rowParse, parseChar));
				rowParse = StringUtil.substringDesde(rowParse, parseChar);
			}
			
			return datum;
		}
		
		
		 /**
		 * @return El string del array en el indice dado. 
		 */
		public String getCols(int index){
			return cols[index];
		}
		
		/**
		 * @return El array de string. 
		 */
		public String[] getCols(){
			return cols;
		}
		
		/**
		 * @param El indice, y el string a setear en el array.
		 */
		public void setCols(int index, String valor){
			this.cols[index] = valor;
		}
		
		 /**
		 * @return El Numero maximo de columnas del datum.
		 */
		public int getColNumMax(){
			return colNumMax;
		}
		
		/**
		 * Obtiene una fecha formateando el string de la columna indicada
		 * 
		 * @param n&uacute;mero de columna
		 * @return Date
		 */
		 public Date getDate(int index) throws Exception {
		    	Date result = null;
		    	String dateToValidate = getCols(index);
				try {
					if (dateToValidate.length()=="yyyyMMdd".length()) {
						SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
						sdf.setLenient( false );
						result = sdf.parse(dateToValidate);
						if (result !=null) {
							return result;
						}
					}
				} catch (Exception e) {
					throw new Exception("Formato de Fecha Incorrecto en columna "+index,e);
				}	
		    	return result;
		}
		 
		 public Date getDate(int index, String dateMask) throws Exception {
		    	String dateToValidate = getCols(index);
		    	return DateUtil.getDate(dateToValidate, dateMask);
		}

		 

		 /**
		  * Obtiene un Long desde el string de la columna indicada
		  * 
		  * @param n&uacute;mero de columna
		  * @return Long
		  */
		  public Long getLong(int index) throws Exception {
			  Long result = null;
			  String longToConvert = getCols(index).trim();
			  try{
				result = Long.valueOf(longToConvert);  
			  } catch(Exception e){
				  throw new Exception("El Formato no se corresponde con un Long en columna "+index,e);
			  }
			  return result;
		  }
		  
		 /**
		  * Obtiene un Double desde el string de la columna indicada
		  * 
		  * @param n&uacute;mero de columna
		  * @return Double
		  */
		  public Double getDouble(int index) throws Exception {
			  Double result = null;
			  String doubleToConvert = getCols(index).trim();
			  try{
				  doubleToConvert = doubleToConvert.replace(',', '.');
				  result = Double.valueOf(doubleToConvert);  
			  } catch(Exception e){
				  throw new Exception("El Formato no se corresponde con un Double en columna datum: " + index,e);
			  }
			  return result;
		  }
		  
		 /**
		  * Obtiene un Integer desde el string de la columna indicada
		  * 
		  * @param n&uacute;mero de columna
		  * @return Integer
		  */
		  public Integer getInteger(int index) throws Exception {
			  Integer result = null;
			  String IntegerToConvert = getCols(index).trim();
			  try{
				result = Integer.valueOf(IntegerToConvert);  
			  } catch(Exception e){
				  throw new Exception("El Formato no se corresponde con un Integer en columna "+index,e);
			  }
			  return result;
		  }
			
		 /**
		  * Obtiene un Float desde el string de la columna indicada
		  * 
		  * @param n&uacute;mero de columna
		  * @return Float
		  */
		  public Float getFloat(int index) throws Exception {
			  Float result = null;
			  String FloatToConvert = getCols(index).trim();
			  try{
				result = Float.valueOf(FloatToConvert);  
			  } catch(Exception e){
				  throw new Exception("El Formato no se corresponde con un Float en columna "+index,e);
			  }
			  return result;
		  }

		public String getString(int index) {
			String s = getCols(index).trim();
			return s.replace('\\', ' ');
		}

		  
}
