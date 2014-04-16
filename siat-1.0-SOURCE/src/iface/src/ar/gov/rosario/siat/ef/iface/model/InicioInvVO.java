//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del InicioInv
 * @author tecso
 *
 */
public class InicioInvVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "inicioInvVO";
	
	private String detalle;
	
    private Date fecha;
	
	private OrdenControlVO ordenControl;
	
	// Buss Flags
	
	
	// View Constants
	
	
	private String fechaView = "";


	// Constructores
	public InicioInvVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public InicioInvVO(int id, String detalle) {
		super();
		setId(new Long(id));
		setDetalle(detalle);
	}
	
	// Getters y Setters
	public String getDetalle() {
		return detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public OrdenControlVO getOrdenControl() {
		return ordenControl;
	}

	public void setOrdenControl(OrdenControlVO ordenControlVO) {
		this.ordenControl = ordenControlVO;
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
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	public String getFechaView() {
		return fechaView;
	}

}
