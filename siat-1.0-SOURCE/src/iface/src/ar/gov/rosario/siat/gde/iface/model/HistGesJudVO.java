//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

public class HistGesJudVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private GesJudVO gesJud = new GesJudVO();

	private Date fecha = new Date();
	
	private String descripcion = "";

	private String fechaView="";
	
	public GesJudVO getGesJud() {
		return gesJud;
	}

	public void setGesJud(GesJudVO gesJud) {
		this.gesJud = gesJud;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, "dd/MM/yyyy");
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	//view getters
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getFechaView() {
		return fechaView;
	}

	
}
