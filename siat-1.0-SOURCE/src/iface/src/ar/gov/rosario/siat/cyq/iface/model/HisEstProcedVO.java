//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HisEstProced
 * @author tecso
 *
 */
public class HisEstProcedVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstProcedVO";
	
	private ProcedimientoVO procedimiento;	
	private EstadoProcedVO estadoProced = new EstadoProcedVO();	
	private Date fecha;	
	private String logCambios;	
	private String observaciones;
	
	private String fechaView;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public HisEstProcedVO() {
		super();
	}

	
	// Getters y Setters
	public ProcedimientoVO getProcedimiento() {
		return procedimiento;
	}
	public void setProcedimiento(ProcedimientoVO procedimiento) {
		this.procedimiento = procedimiento;
	}

	public EstadoProcedVO getEstadoProced() {
		return estadoProced;
	}
	public void setEstadoProced(EstadoProcedVO estadoProced) {
		this.estadoProced = estadoProced;
	}

	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
		this.fechaView = DateUtil.formatDate(fecha, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getLogCambios() {
		return logCambios;
	}
	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}

	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
