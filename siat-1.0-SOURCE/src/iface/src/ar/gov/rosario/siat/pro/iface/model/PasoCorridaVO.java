//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import java.util.Date;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.BussImageModel;

public class PasoCorridaVO extends BussImageModel {
	private static final long serialVersionUID = 0;

	private CorridaVO corrida = new CorridaVO();
	
	private Integer paso;   // INTEGER NOT NULL
	 
	private Date fechaCorrida;   // DATETIME HOUR TO MINUTE NOT NULL
	
	private EstadoCorridaVO estadoCorrida = new EstadoCorridaVO();
	
	private String observacion;   // VARCHAR(255)

	private String fechaCorridaView = "";   

	// Contructores	
	public PasoCorridaVO() {
		super();
	}

	// Getters y Setters	
	public CorridaVO getCorrida() {
		return corrida;
	}
	public void setCorrida(CorridaVO corrida) {
		this.corrida = corrida;
	}
	public EstadoCorridaVO getEstadoCorrida() {
		return estadoCorrida;
	}
	public void setEstadoCorrida(EstadoCorridaVO estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}
	public Date getFechaCorrida() {
		return fechaCorrida;
	}
	public void setFechaCorrida(Date fechaCorrida) {
		this.fechaCorrida = fechaCorrida;
		this.fechaCorridaView = DateUtil.formatDate(fechaCorrida, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaCorridaView() {
		return fechaCorridaView;
	}
	public void setFechaCorridaView(String fechaCorridaView) {
		this.fechaCorridaView = fechaCorridaView;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Integer getPaso() {
		return paso;
	}
	public void setPaso(Integer paso) {
		this.paso = paso;
	}
	public String getHoraCorridaView(){
		return DateUtil.formatDate(this.getFechaCorrida(), DateUtil.HOUR_MINUTE_MASK);
	}
}
