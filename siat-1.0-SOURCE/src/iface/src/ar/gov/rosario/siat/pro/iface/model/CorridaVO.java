//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.HOUR_MINUTE_MASK;
import coop.tecso.demoda.iface.helper.StringUtil;

public class CorridaVO extends SiatBussImageModel {
	private static final long serialVersionUID = 0;

	private String desCorrida;   // VARCHAR(255) NOT NULL
	private ProcesoVO proceso = new ProcesoVO();
	private Date fechaInicio;      // DATETIME YEAR TO SECOND
	private Date horaInicio;
	private Date fechaFin;         // DATETIME YEAR TO SECOND
	private Date fechaUltResume;   // DATETIME YEAR TO SECOND
    private EstadoCorridaVO estadoCorrida = new EstadoCorridaVO();
	private String  mensajeEstado = "";   // VARCHAR(100)
	private String  observacion = "";   // VARCHAR(255)
	private Integer pasoActual;  // NOT NULL
	private Integer pasoCorrida;  // NOT NULL
	private String nodoOwner="";

	private List<LogCorridaVO> listLogCorrida = new ArrayList<LogCorridaVO>(); 
	
	private String fechaInicioView = "";   
	private String horaInicioView = "";
	private String fechaFinView = "";      
	private String fechaUltResumeView = "";
	
    private Boolean reiniciarBussEnabled      = null;
    private Boolean refrescarBussEnabled      = null;
    private Boolean cancelarBussEnabled       = null;
    private Boolean siguientePasoBussEnabled  = null;
    private Boolean retrocederPasoBussEnabled = null;
	private Boolean modifDatosFormBussEnabled = null;
    
	private Boolean verLogsBussEnabled 		  = null;
	
	// Contructores	
	public CorridaVO() {
		super();
	}
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CorridaVO(int id, String desCorrida) {
		super();
		setId(new Long(id));
		setDesCorrida(desCorrida);
	}

	// Getters y Setters	
	public String getDesCorrida() {
		return desCorrida;
	}
	public void setDesCorrida(String desCorrida) {
		this.desCorrida = desCorrida;
	}
	public EstadoCorridaVO getEstadoCorrida() {
		return estadoCorrida;
	}
	public void setEstadoCorrida(EstadoCorridaVO estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
		this.fechaFinView = DateUtil.formatDate(fechaFin, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaFinView() {
		return fechaFinView;
	}
	public void setFechaFinView(String fechaFinView) {
		this.fechaFinView = fechaFinView;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
		this.fechaInicioView = DateUtil.formatDate(fechaInicio, DateUtil.ddSMMSYYYY_MASK);
		this.horaInicioView = DateUtil.formatDate(this.getFechaInicio(), DateUtil.HOUR_MINUTE_MASK);
	}
	public String getFechaInicioView() {
		return fechaInicioView;
	}
	public void setFechaInicioView(String fechaInicioView) {
		this.fechaInicioView = fechaInicioView;
	}
	public Date getFechaUltResume() {
		return fechaUltResume;
	}
	public void setFechaUltResume(Date fechaUltResume) {
		this.fechaUltResume = fechaUltResume;
		this.fechaUltResumeView = DateUtil.formatDate(fechaUltResume, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaUltResumeView() {
		return fechaUltResumeView;
	}
	public void setFechaUltResumeView(String fechaUltResumeView) {
		this.fechaUltResumeView = fechaUltResumeView;
	}
	public String getMensajeEstado() {
		return mensajeEstado;
	}
	public void setMensajeEstado(String mensajeEstado) {
		this.mensajeEstado = mensajeEstado;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Integer getPasoActual() {
		return pasoActual;
	}
	public void setPasoActual(Integer pasoActual) {
		this.pasoActual = pasoActual;
	}
	public ProcesoVO getProceso() {
		return proceso;
	}
	public void setProceso(ProcesoVO proceso) {
		this.proceso = proceso;
	}
	
	public String getNodoOwner() {
		return nodoOwner;
	}
	public void setNodoOwner(String nodoOwner) {
		this.nodoOwner = nodoOwner;
	}
	public List<LogCorridaVO> getListLogCorrida() {
		return listLogCorrida;
	}

	public void setListLogCorrida(List<LogCorridaVO> listLogCorrida) {
		this.listLogCorrida = listLogCorrida;
	}

	public String getHoraInicioView(){
		return this.horaInicioView;
	}
	
	public void setHoraInicioView(String horaInicioView) {
		this.horaInicioView = horaInicioView;
	}
	
	public Date getHoraInicio() {
		return horaInicio;
	}
	
	@HOUR_MINUTE_MASK
	public void setHoraInicio(Date horaInicio) {
		this.horaInicio = horaInicio;
		this.horaInicioView = DateUtil.formatDate(horaInicio, DateUtil.HOUR_MINUTE_MASK);
	}
	
	public String getPasoActualView() {
		return StringUtil.formatInteger(this.getPasoActual());
	}

	public Integer getPasoCorrida() {
		return pasoCorrida;
	}

	public void setPasoCorrida(Integer pasoCorrida) {
		this.pasoCorrida = pasoCorrida;
	}

	public Boolean getCancelarBussEnabled() {
		return cancelarBussEnabled;
	}

	public void setCancelarBussEnabled(Boolean cancelarBussEnabled) {
		this.cancelarBussEnabled = cancelarBussEnabled;
	}

	public Boolean getRefrescarBussEnabled() {
		return refrescarBussEnabled;
	}

	public void setRefrescarBussEnabled(Boolean refrescarBussEnabled) {
		this.refrescarBussEnabled = refrescarBussEnabled;
	}

	public Boolean getReiniciarBussEnabled() {
		return reiniciarBussEnabled;
	}

	public void setReiniciarBussEnabled(Boolean reiniciarBussEnabled) {
		this.reiniciarBussEnabled = reiniciarBussEnabled;
	}

	public Boolean getRetrocederPasoBussEnabled() {
		return retrocederPasoBussEnabled;
	}

	public void setRetrocederPasoBussEnabled(Boolean retrocederPasoBussEnabled) {
		this.retrocederPasoBussEnabled = retrocederPasoBussEnabled;
	}

	public Boolean getSiguientePasoBussEnabled() {
		return siguientePasoBussEnabled;
	}

	public void setSiguientePasoBussEnabled(Boolean siguientePasoBussEnabled) {
		this.siguientePasoBussEnabled = siguientePasoBussEnabled;
	}
	
	
	
	public Boolean getModifDatosFormBussEnabled() {
		return modifDatosFormBussEnabled;
	}
	public void setModifDatosFormBussEnabled(Boolean modifDatosFormBussEnabled) {
		this.modifDatosFormBussEnabled = modifDatosFormBussEnabled;
	}
		
	public Boolean getVerLogsBussEnabled() {
		return verLogsBussEnabled;
	}
	public void setVerLogsBussEnabled(Boolean verLogsBussEnabled) {
		this.verLogsBussEnabled = verLogsBussEnabled;
	}
	public String getReiniciarEnabled() {
		if (this.getReiniciarBussEnabled() == null) return null;
		return this.getReiniciarBussEnabled() ? ENABLED : DISABLED;
	}
	public String getRefrescarEnabled() {
		if (this.getRefrescarBussEnabled() == null) return null;
		return this.getRefrescarBussEnabled() ? ENABLED : DISABLED;
	}
	public String getCancelarEnabled() {
		if (this.getCancelarBussEnabled() == null) return null;
		return this.getCancelarBussEnabled() ? ENABLED : DISABLED;
	}
	public String getSiguientePasoEnabled() {
		if (this.getSiguientePasoBussEnabled() == null) return null;
		return this.getSiguientePasoBussEnabled() ? ENABLED : DISABLED;
	}
	public String getRetrocederPasoEnabled() {
		if (this.getRetrocederPasoBussEnabled() == null) return null;
		return this.getRetrocederPasoBussEnabled() ? ENABLED : DISABLED;
	}
	public String getModifDatosFormEnabled() {
		if (this.getModifDatosFormBussEnabled() == null) return null;
		return this.getModifDatosFormBussEnabled() ? ENABLED : DISABLED;
	}

	public String getVerLogsEnabled(){
		if (this.getVerLogsBussEnabled() == null) return DISABLED;
		return this.getVerLogsBussEnabled() ? ENABLED : DISABLED;
	}

	
	
}
