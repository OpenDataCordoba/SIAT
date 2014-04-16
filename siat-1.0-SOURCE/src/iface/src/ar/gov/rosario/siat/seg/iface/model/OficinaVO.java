//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;

/**
 * Value Object del Oficina
 * @author tecso
 *
 */
public class OficinaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "oficinaVO";
	private String desOficina = "";
	private AreaVO area;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public OficinaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OficinaVO(int id, String desc) {
		super();
		setId(new Long(id));
	}

	public String getDesOficina() {
		return desOficina;
	}

	public void setDesOficina(String desOficina) {
		this.desOficina = desOficina;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}
	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
