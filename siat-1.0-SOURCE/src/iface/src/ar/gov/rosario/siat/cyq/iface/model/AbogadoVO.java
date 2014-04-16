//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Abogado
 * @author tecso
 *
 */
public class AbogadoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "abogadoVO";
	
    private String descripcion;
	
	private String domicilio;
	
	private String telefono;
	
	private JuzgadoVO juzgado= new JuzgadoVO();
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public AbogadoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public AbogadoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDescripcion(desc);
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public JuzgadoVO getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(JuzgadoVO juzgado) {
		this.juzgado = juzgado;
	}
	
	// Getters y Setters
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
