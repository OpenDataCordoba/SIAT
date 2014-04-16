//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;

public class LiqExencionVO extends SiatBussImageModel{

	private static final long serialVersionUID = 1L;

	private String desExencion="";
	
	private CasoVO caso = new CasoVO();
	
	private String fechaDesde="";
	private String fechaHasta="";
	private String fechaSolicitud="";
	private Long   idCueExe;
	
	private boolean poseeCaso = false;
	private boolean selectExencionEnabled = false;
		
	// Getters y Setters
	public String getDesExencion() {
		return desExencion;
	}
	public void setDesExencion(String desExencion) {
		this.desExencion = desExencion;
	}
	public String getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(String fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(String fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
	
	public Long getIdCueExe() {
		return idCueExe;
	}
	public void setIdCueExe(Long idCueExe) {
		this.idCueExe = idCueExe;
	}
	
	public String getFechaSolicitud() {
		return fechaSolicitud;
	}
	public void setFechaSolicitud(String fechaSolicitud) {
		this.fechaSolicitud = fechaSolicitud;
	}
	
	public boolean isPoseeCaso() {
		return poseeCaso;
	}
	public void setPoseeCaso(boolean poseeCaso) {
		this.poseeCaso = poseeCaso;
	}
	public CasoVO getCaso() {
		return caso;
	}
	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
	
	public boolean isSelectExencionEnabled() {
		return selectExencionEnabled;
	}
	public void setSelectExencionEnabled(boolean selectExencionEnabled) {
		this.selectExencionEnabled = selectExencionEnabled;
	}
	
	// Metodos para View
	
}
