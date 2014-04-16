//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipoJuzgado
 * @author tecso
 *
 */
public class TipoJuzgadoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoJuzgadoVO";
	
	private String codTipoJuzgado;
	
	private String desTipoJuzgado;

	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipoJuzgadoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipoJuzgadoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesTipoJuzgado(desc);
	}

	// Getters y Setters
	public String getCodTipoJuzgado() {
		return codTipoJuzgado;
	}

	public void setCodTipoJuzgado(String codTipoJuzgado) {
		this.codTipoJuzgado = codTipoJuzgado;
	}

	public String getDesTipoJuzgado() {
		return desTipoJuzgado;
	}

	public void setDesTipoJuzgado(String desTipoJuzgado) {
		this.desTipoJuzgado = desTipoJuzgado;
	}
	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
