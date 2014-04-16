//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del MesaEntrada
 * @author tecso
 *
 */
public class MesaEntradaVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "mesaEntradaVO";
	
	private OrdenControlVO ordenControl;
	
	private EstadoOrdenVO estadoOrden = new EstadoOrdenVO();
	
    private Date fecha;
	
	private String observacion;
	
	
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaView = "";


	// Constructores
	public MesaEntradaVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public MesaEntradaVO(int id, String desc) {
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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getFechaView() {
		return fechaView;
	}

}
