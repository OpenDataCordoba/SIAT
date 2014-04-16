//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HisEstOrdCon
 * @author tecso
 *
 */
public class HisEstOrdConVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstOrdConVO";
	
	private OrdenControlVO ordenControl;
	private EstadoOrdenVO estadoOrden = new EstadoOrdenVO();
	private Date fecha;
	private String observacion="";
	
	private String fechaView="";
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public HisEstOrdConVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public HisEstOrdConVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters
	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControl) {
		this.ordenControl = ordenControl;
	}

	public EstadoOrdenVO getEstadoOrden() {
		return estadoOrden;
	}

	public void setEstadoOrden(EstadoOrdenVO estadoOrden) {
		this.estadoOrden = estadoOrden;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}


	
	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public String getFechaView() {
		return fechaView;
	}
	
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
}
