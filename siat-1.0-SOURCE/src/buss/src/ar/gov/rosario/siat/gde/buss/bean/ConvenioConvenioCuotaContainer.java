//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a Convenio
 * 
 * @author tecso
 */

public class ConvenioConvenioCuotaContainer extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	

	private Long idConvenio;
	private Long nroConvenio;
	private Long idSistema;
	private Long idRecurso;
	private Long idConvenioCuota;
	private Integer nroCuota;
	private Date fechaVencimiento;
	private Date fechaPago;
	private Long idEstadoConCuo;
	private Long codRefPag;
	// Constructores
	public ConvenioConvenioCuotaContainer(){
		super();
	}
	
	public Long getIdConvenio() {
		return idConvenio;
	}
	public void setIdConvenio(Long idConvenio) {
		this.idConvenio = idConvenio;
	}
	public Long getIdConvenioCuota() {
		return idConvenioCuota;
	}
	public void setIdConvenioCuota(Long idConvenioCuota) {
		this.idConvenioCuota = idConvenioCuota;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	public Date getFechaPago() {
		return fechaPago;
	}
	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}
	public Long getIdEstadoConCuo() {
		return idEstadoConCuo;
	}
	public void setIdEstadoConCuo(Long idEstadoConCuo) {
		this.idEstadoConCuo = idEstadoConCuo;
	}
	public Integer getNroCuota() {
		return nroCuota;
	}
	public void setNroCuota(Integer nroCuota) {
		this.nroCuota = nroCuota;
	}

	public Long getNroConvenio() {
		return nroConvenio;
	}

	public void setNroConvenio(Long nroConvenio) {
		this.nroConvenio = nroConvenio;
	}

	public Long getIdSistema() {
		return idSistema;
	}

	public void setIdSistema(Long idSistema) {
		this.idSistema = idSistema;
	}

	public Long getIdRecurso() {
		return idRecurso;
	}

	public void setIdRecurso(Long idRecurso) {
		this.idRecurso = idRecurso;
	}

	public Long getCodRefPag() {
		return codRefPag;
	}

	public void setCodRefPag(Long codRefPag) {
		this.codRefPag = codRefPag;
	}
	

	
	
}
	
