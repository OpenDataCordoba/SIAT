//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.demoda.iface.model;



/**
 *  Representa un dato de un reporte.
 *   
 * @author Tecso
 *
 */
public class ReportDatoVO {
	
    // nombre que lo identifica
	private String nombre = "";  
	
	private String metodoEjecutar = null;// soporta notacion con puntos;

	private Integer width = new Integer(0);
	
	private Class claseEnumeracion = null ;
	
	public ReportDatoVO(){
	}
	
	public ReportDatoVO(String nombre, String metodoEjecutar) {
		this.nombre = nombre;
		this.metodoEjecutar = metodoEjecutar;
	}
	
	public ReportDatoVO(String nombre, String metodoEjecutar, Integer width) {
		this.nombre = nombre;
		this.metodoEjecutar = metodoEjecutar;
		this.width = width;
	}

	
	// Getters Y Setters
	public String getMetodoEjecutar() {
		return metodoEjecutar;
	}
	public void setMetodoEjecutar(String metodoEjecutar) {
		this.metodoEjecutar = metodoEjecutar;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public Integer getWidth() {
		return width;
	}
	public void setWidth(Integer width) {
		this.width = width;
	}

	public Class getClaseEnumeracion() {
		return claseEnumeracion;
	}

	public void setClaseEnumeracion(Class claseEnumeracion) {
		this.claseEnumeracion = claseEnumeracion;
	}
	
	
}
