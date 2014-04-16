//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;

public class ProcuradorVO extends SiatBussImageModel {
	private static final long serialVersionUID = 0;

	private String descripcion = ""; // not null
	private String domicilio = ""; // VARCHAR(100)
	private String telefono = ""; // VARCHAR(100)
	private String horarioAtencion = ""; // VARCHAR(255)
	private String observacion = ""; // VARCHAR(255)

	private TipoProcuradorVO tipoProcurador = new TipoProcuradorVO(); // not null

	private List<ProRecVO> listProRec = new ArrayList<ProRecVO>();
	
	public ProcuradorVO() {
		super();
	}
	
	public ProcuradorVO(long id,  String nombreView) {
		this();
		this.setId(id);
		this.setDescripcion(nombreView);
	}

	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getHorarioAtencion() {
		return horarioAtencion;
	}
	public void setHorarioAtencion(String horarioAtencion) {
		this.horarioAtencion = horarioAtencion;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}
	public TipoProcuradorVO getTipoProcurador() {
		return tipoProcurador;
	}
	public void setTipoProcurador(TipoProcuradorVO tipoProcurador) {
		this.tipoProcurador = tipoProcurador;
	}
	public String getNombreConCod(){
		return (this.getId()!=(-1))? this.getId().toString() + "-" + this.descripcion :this.descripcion; 
	}
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public List<ProRecVO> getListProRec() {
		return listProRec;
	}

	public void setListProRec(List<ProRecVO> listProRec) {
		this.listProRec = listProRec;
	}

}
