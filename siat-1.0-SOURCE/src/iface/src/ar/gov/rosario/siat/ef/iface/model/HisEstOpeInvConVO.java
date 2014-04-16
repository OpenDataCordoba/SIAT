//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HisEstOpeInvCon
 * @author tecso
 *
 */
public class HisEstOpeInvConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstOpeInvConVO";
	
	private String desEstado ="";
	
	private Date fechaEstado = new Date();
	
	private String observaciones="";
	
	private String usuario = "";
		
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public HisEstOpeInvConVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public HisEstOpeInvConVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	

	public String getDesEstado() {
		return desEstado;
	}

	public void setDesEstado(String desEstado) {
		this.desEstado = desEstado;
	}

	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	
	

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaEstadoView(){
		return DateUtil.formatDate(this.fechaEstado, DateUtil.ddSMMSYYYY_HH_MM_MASK);
	}
}
