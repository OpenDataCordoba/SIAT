//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

/**
 * Value Object del TipSujExe
 * @author tecso
 *
 */
public class TipSujExeVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipSujExeVO";
	
	private TipoSujetoVO tipoSujeto = new TipoSujetoVO();
	private ExencionVO exencion = new ExencionVO();
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public TipSujExeVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public TipSujExeVO(int id, String desc) {
		super();
		setId(new Long(id));

	}
	// Getters y Setters
	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
	}

	public TipoSujetoVO getTipoSujeto() {
		return tipoSujeto;
	}

	public void setTipoSujeto(TipoSujetoVO tipoSujeto) {
		this.tipoSujeto = tipoSujeto;
	}
	
	

	
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
