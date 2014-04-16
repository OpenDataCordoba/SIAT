//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del Juzgado
 * @author tecso
 *
 */
public class JuzgadoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "juzgadoVO";
	
	private String desJuzgado;
	
	private List<AbogadoVO>  listAbogado = new ArrayList<AbogadoVO>();
	
	// Buss Flags
	
	
	// View Constants
	
	
	public List<AbogadoVO> getListAbogado() {
		return listAbogado;
	}

	public void setListAbogado(List<AbogadoVO> listAbogado) {
		this.listAbogado = listAbogado;
	}

	// Constructores
	public JuzgadoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public JuzgadoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesJuzgado(desc);
	}


	
	public String getDesJuzgado() {
		return desJuzgado;
	}

	public void setDesJuzgado(String desJuzgado) {
		this.desJuzgado = desJuzgado;
	}
	
	// Getters y Setters


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
