//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;

import java.util.HashMap;
import java.util.Map;


/**
 *  Representa un pie de una columna de un reporte.
 *  Permite realizar operaciones de suma y conteo sobre los elementos de la columna
 *   
 * @author Tecso
 *
 */
public class ReportPieVO {
	
    public final static Integer SUM   = 1; // indica realizar la accion suma de la columna
    public final static Integer COUNT = 2; // indica contar los valores distintos de la columna

    // nombre que lo identifica
	private String nombre = "";  
	// propiedad a ejecutar
	private String propiedad = ""; 
    // Suma o Conteo
	private Integer accion;
	// contiene el resultado de la suma. Double, Long o Integer
	private Object suma;        
	// contiene el resultado del conteo de los distintos elementos cargados en la columna
	private Map<Long,String> contador = new HashMap<Long,String>();

	public ReportPieVO(){}
	
	/**
	 * Constructor sin propiedad a ejecutar
	 * @param nombre de la columna
	 * @param accion a realizar: Suma o Conteo
	 */
	public ReportPieVO(String nombre, Integer accion){
		this.nombre    = nombre;
		this.accion    = accion;
	}

	/**
	 * Constructor
	 * @param nombre de la columna
	 * @param propiedad propiedad a ejecutar. Con su resultado se ejecuta la accion
	 * @param accion a realizar: Suma o Conteo
	 */
	public ReportPieVO(String nombre, String propiedad, Integer accion){
		this(nombre,accion);
		this.propiedad = propiedad;
	}

	// Getters Y Setters
	public Integer getAccion() {
		return accion;
	}
	public void setAccion(Integer accion) {
		this.accion = accion;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getPropiedad() {
		return propiedad;
	}
	public void setPropiedad(String propiedad) {
		this.propiedad = propiedad;
	}
	public Object getSuma() {
		return suma;
	}
	public void setSuma(Object suma) {
		this.suma = suma;
	}
	public Map<Long, String> getContador() {
		return contador;
	}
	public void setContador(Map<Long, String> contador) {
		this.contador = contador;
	}
	
	/**
	 * Determina si se realiza una suma
	 * @return boolean
	 */
	public boolean getEsSuma(){
		return SUM.equals(this.getAccion());
	}
	
	/**
	 * Determina si se realiza un conteo
	 * @return boolean
	 */
	public boolean getEsCount(){
		return COUNT.equals(this.getAccion());
	}


	/**
	 * Realiza la suma y la acumula en la propiedad suma.
	 * Tiene en cuenta la clase del objeto parcial que recibe como parametro
	 * @param parcial
	 */
	public void sumar(Object parcial){
		if (Long.class.isInstance(parcial)){
			this.sumar((Long) parcial);
		}
		if (Double.class.isInstance(parcial)){
			this.sumar((Double) parcial);
		}
		if (Integer.class.isInstance(parcial)){
			this.sumar((Integer) parcial);
		}
	}
	public void sumar(Long parcial){
		this.suma = (this.suma!=null)?((Long) this.suma + parcial):parcial;
	}
	public void sumar(Double parcial){
		this.suma = (this.suma!=null)?((Double) this.suma + parcial):parcial;
	}
	public void sumar(Integer parcial){
		this.suma = (this.suma!=null)?((Integer) this.suma + parcial):parcial;
	}
	
	/**
	 * Utilizado para contar los distintos ids
	 * @param id
	 */
	public void contar(Long id){
		this.contador.put(id, null);
	}

	/**
	 * Obtiene el resultado de la suma o del conteo.
	 * @return String
	 */
	public String getResultado(){
		if(this.getEsSuma()){
			return String.valueOf(suma);
			//return (suma!=null)?suma.toString():"";
		}else if(this.getEsCount()){
			return String.valueOf(this.contador.size());
		}
		return "";
	}
}
