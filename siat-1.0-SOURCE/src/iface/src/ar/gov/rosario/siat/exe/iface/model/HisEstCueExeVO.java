//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Value Object del HisEstCueExe
 * @author tecso
 *
 */
public class HisEstCueExeVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstCueExeVO";
	
	private CueExeVO cueExe;
	private EstadoCueExeVO estadoCueExe = new EstadoCueExeVO();
	private Date fecha;
	private String logCambios;
	private String observaciones;
	
	
	private String fechaView;
	
	// Constructores
	public HisEstCueExeVO() {
		super();
	}

	
	// Getters y Setters
	
	public CueExeVO getCueExe() {
		return cueExe;
	}
	public void setCueExe(CueExeVO cueExe) {
		this.cueExe = cueExe;
	}

	public EstadoCueExeVO getEstadoCueExe() {
		return estadoCueExe;
	}
	public void setEstadoCueExe(EstadoCueExeVO estadoCueExe) {
		this.estadoCueExe = estadoCueExe;
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
	public String getModificarHisEstCueExeEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getModificarBussEnabled(), ExeSecurityConstants.ABM_CUEEXE, ExeSecurityConstants.MTD_MODIFICAR_HISESTCUEEXE);
	}
	
	// View getters
	public String getFechaView() {
		return fechaView;
	}
	public void setFechaView(String fechaView) {
		this.fechaView = fechaView;
	}
	
}
