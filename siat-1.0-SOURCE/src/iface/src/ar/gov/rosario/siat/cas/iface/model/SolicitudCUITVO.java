//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.PersonaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Value Object del SolicitudCUIT
 * @author tecso
 *
 */
public class SolicitudCUITVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "solicitudCUITVO";
	
	private SolicitudVO solicitud;

	private PersonaVO persona = new PersonaVO();

	private Integer tipoModificacion;

	private Integer tipoPersona;

	private String apellidoReportado;

	private String nombreReportado;

	private String razonSocReportado;

	private String tipoDocReportado;

	private Long docReportado;

	private Integer cuit01Aprobado;

	private Long cuit02Aprobado;

	private Integer cuit03Aprobado;

	private String apellidoAprobado;

	private String nombreAprobado;

	private String razonSocAprobado;

	private String datosRegPadron;
	// Buss Flags
	
	
	private String cuit01AprobadoView = "";
	private String cuit02AprobadoView = "";
	private String cuit03AprobadoView = "";
	// View Constants
	
	
	// Constructores
	public SolicitudCUITVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public SolicitudCUITVO(int id, String desc) {
		super();
		setId(new Long(id));		
	}
	
	// Getters y Setters

	public SolicitudVO getSolicitudVO() {
		return solicitud;
	}

	public void setSolicitudVO(SolicitudVO solicitudVO) {
		this.solicitud = solicitudVO;
	}

	public PersonaVO getPersona() {
		return persona;
	}

	public void setPersonaVO(PersonaVO personaVO) {
		this.persona = personaVO;
	}

	public Integer getTipoModificacion() {
		return tipoModificacion;
	}

	public void setTipoModificacion(Integer tipoModificacion) {
		this.tipoModificacion = tipoModificacion;
	}

	public Integer getTipoPersona() {
		return tipoPersona;
	}

	public void setTipoPersona(Integer tipoPersona) {
		this.tipoPersona = tipoPersona;
	}

	public String getApellidoReportado() {
		return apellidoReportado;
	}

	public void setApellidoReportado(String apellidoReportado) {
		this.apellidoReportado = apellidoReportado;
	}

	public String getNombreReportado() {
		return nombreReportado;
	}

	public void setNombreReportado(String nombreReportado) {
		this.nombreReportado = nombreReportado;
	}

	public String getRazonSocReportado() {
		return razonSocReportado;
	}

	public void setRazonSocReportado(String razonSocReportado) {
		this.razonSocReportado = razonSocReportado;
	}

	public String getTipoDocReportado() {
		return tipoDocReportado;
	}

	public void setTipoDocReportado(String tipoDocReportado) {
		this.tipoDocReportado = tipoDocReportado;
	}

	public Long getDocReportado() {
		return docReportado;
	}

	public void setDocReportado(Long docReportado) {
		this.docReportado = docReportado;
	}

	public Integer getCuit01Aprobado() {
		return cuit01Aprobado;
	}

	public void setCuit01Aprobado(Integer cuit01aprobado) {
		this.cuit01Aprobado = cuit01aprobado;
		this.cuit01AprobadoView = StringUtil.formatInteger(cuit01Aprobado);
	}

	public Long getCuit02Aprobado() {
		return cuit02Aprobado;
	}

	public void setCuit02Aprobado(Long cuit02Aprobado) {
		this.cuit02Aprobado = cuit02Aprobado;
		this.cuit02AprobadoView = StringUtil.formatLong(cuit02Aprobado);
	}

	public Integer getCuit03Aprobado() {
		return cuit03Aprobado;
	}

	public void setCuit03Aprobado(Integer cuit03Aprobado) {
		this.cuit03Aprobado = cuit03Aprobado;
		this.cuit03AprobadoView = StringUtil.formatInteger(cuit03Aprobado);
	}

	public String getApellidoAprobado() {
		return apellidoAprobado;
	}

	public void setApellidoAprobado(String apellidoAprobado) {
		this.apellidoAprobado = apellidoAprobado;
	}

	public String getNombreAprobado() {
		return nombreAprobado;
	}

	public void setNombreAprobado(String nombreAprobado) {
		this.nombreAprobado = nombreAprobado;
	}

	public String getRazonSocAprobado() {
		return razonSocAprobado;
	}

	public void setRazonSocAprobado(String razonSocAprobado) {
		this.razonSocAprobado = razonSocAprobado;
	}

	public String getDatosRegPadron() {
		return datosRegPadron;
	}

	public void setDatosRegPadron(String datosRegPadron) {
		this.datosRegPadron = datosRegPadron;
	}
	// Buss flags getters y setters

	// View getters
	public String getDocReportadoView(){
		return StringUtil.formatLong(docReportado);
	}

	public void setearValoresDefecto() {
		if(!StringUtil.isNullOrEmpty(apellidoReportado))
			apellidoAprobado=apellidoReportado;
		
		if(!StringUtil.isNullOrEmpty(nombreReportado))
			nombreAprobado=nombreReportado;
		
		if(!StringUtil.isNullOrEmpty(razonSocReportado))
			razonSocAprobado=razonSocReportado;
		
	}

	public String getCuit01AprobadoView() {
		return cuit01AprobadoView;
	}
	public void setCuit01AprobadoView(String cuit01AprobadoView) {
		this.cuit01AprobadoView = cuit01AprobadoView;
	}
	public String getCuit02AprobadoView() {
		return cuit02AprobadoView;
	}
	public void setCuit02AprobadoView(String cuit02AprobadoView) {
		this.cuit02AprobadoView = cuit02AprobadoView;
	}
	public String getCuit03AprobadoView() {
		return cuit03AprobadoView;
	}
	public void setCuit03AprobadoView(String cuit03AprobadoView) {
		this.cuit03AprobadoView = cuit03AprobadoView;
	}
	
	
	// View getters
}
