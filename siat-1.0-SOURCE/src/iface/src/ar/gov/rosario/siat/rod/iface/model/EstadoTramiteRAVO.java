//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del EstTramiteRA
 * @author tecso
 *
 */
public class EstadoTramiteRAVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estTramiteRAVO";
	
	private String desEstTra;
	private SiNo esEstado;
	private String transiciones; // las transiciones posibles del estado, separadas por como (1,2,...)
	private AreaVO area = new AreaVO();
	private String observacion;

	// Buss Flags

	// View Constants

	// Constructores
	public EstadoTramiteRAVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoTramiteRAVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstTra(desc);
	}
	
	// Getters y Setters
	
	public String getDesEstTra() {
		return desEstTra;
	}

	public void setDesEstTra(String desEstTra) {
		this.desEstTra = desEstTra;
	}

	public SiNo getEsEstado() {
		return esEstado;
	}

	public void setEsEstado(SiNo esEstado) {
		this.esEstado = esEstado;
	}

	public String getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/** Devuelve la descripcion del estado
	 * 	con el area concatenada entre parentesis
	 * 
	 * @return
	 */
	public String getDesEstTramiteRAConArea() {
		String estadoConArea = this.getDesEstTra();

		// en caso que la descripcion sea seleccionar
		// no agrego nada
		if (!ModelUtil.isNullOrEmpty(this)) {
			estadoConArea = estadoConArea + " (" + this.getArea().getDesArea() + ")";
		}

		return estadoConArea;
	}

	// Buss flags getters y setters

	// View flags getters

	// View getters
}
