//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del GesJudEvento
 * @author tecso
 *
 */
public class GesJudEventoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "gesJudEventoVO";
	
	private GesJudVO gesJud = new GesJudVO();

	private EventoVO evento = new EventoVO();

	private Date fechaEvento;

	private String observacion;

	private String fechaEventoView="";
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public GesJudEventoVO() {
		super();
	}


	public GesJudVO getGesJud() {
		return gesJud;
	}


	public void setGesJud(GesJudVO gesJud) {
		this.gesJud = gesJud;
	}


	public EventoVO getEvento() {
		return evento;
	}


	public void setEvento(EventoVO evento) {
		this.evento = evento;
	}


	public Date getFechaEvento() {
		return fechaEvento;
	}


	public void setFechaEvento(Date fechaEvento) {
		this.fechaEvento = fechaEvento;
		this.fechaEventoView = DateUtil.formatDate(fechaEvento, "dd/MM/yyyy");
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public String getFechaEventoView() {
		return fechaEventoView;
	}


	public void setFechaEventoView(String fechaEventoView) {
		this.fechaEventoView = fechaEventoView;
	}
	
	
	// Getters y Setters

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
