//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del EstPlaCua
 * @author tecso
 *
 */
public class EstPlaCuaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estPlaCuaVO";
	
	private String desEstPlaCua;
	private SiNo esEstado;
	private SiNo permiteInconsis;
	private String transiciones; // las transiciones posibles del estado, separadas por como (1,2,...)
	private AreaVO area = new AreaVO();
	private String observacion;

	// Buss Flags

	// View Constants

	// Constructores
	public EstPlaCuaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstPlaCuaVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstPlaCua(desc);
	}
	
	// Getters y Setters
	
	public String getDesEstPlaCua() {
		return desEstPlaCua;
	}

	public void setDesEstPlaCua(String desEstPlaCua) {
		this.desEstPlaCua = desEstPlaCua;
	}

	public SiNo getEsEstado() {
		return esEstado;
	}

	public void setEsEstado(SiNo esEstado) {
		this.esEstado = esEstado;
	}

	public SiNo getPermiteInconsis() {
		return permiteInconsis;
	}

	public void setPermiteInconsis(SiNo permiteInconsis) {
		this.permiteInconsis = permiteInconsis;
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
	public String getDesEstPlaCuaConArea() {
		String estadoConArea = this.getDesEstPlaCua();

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
