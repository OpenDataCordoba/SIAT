//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del EstadoProced
 * @author tecso
 *
 */
public class EstadoProcedVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "estadoProcedVO";
	
	private String desEstadoProced;
	private String tipo;
	private String transiciones;
	private AreaVO area = new AreaVO();
	private SiNo permiteModificar = SiNo.OpcionSelecionar;
	private SiNo esInicial = SiNo.OpcionSelecionar;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public EstadoProcedVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public EstadoProcedVO(int id, String desc) {
		super();
		setId(new Long(id));
		setDesEstadoProced(desc);
	}

	
	// Getters y Setters
	public String getDesEstadoProced() {
		return desEstadoProced;
	}

	public void setDesEstadoProced(String desEstadoProced) {
		this.desEstadoProced = desEstadoProced;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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

	public SiNo getPermiteModificar() {
		return permiteModificar;
	}

	public void setPermiteModificar(SiNo permiteModificar) {
		this.permiteModificar = permiteModificar;
	}

	public SiNo getEsInicial() {
		return esInicial;
	}

	public void setEsInicial(SiNo esInicial) {
		this.esInicial = esInicial;
	}


	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
