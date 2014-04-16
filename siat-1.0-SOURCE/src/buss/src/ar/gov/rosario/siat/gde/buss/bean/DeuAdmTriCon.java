//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.TriCon;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Valor de un Concepto Tributario de una deuda adminstrativa
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_deuAdmTriCon")
public class DeuAdmTriCon extends BaseBO {
	
	//private ClasificacionDeuda clasificacionDeuda;
	//private ViaDeuda viaDeuda; 
	//private Sistema sistema;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idDeudaAdmin") 
	private DeudaAdmin deudaAdmin;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTriCon") 
	private TriCon triCon;

	@Column(name = "anio")
	private Long anio;

	@Column(name = "periodo")
	private Long periodo;

	@Column(name = "fechaEmision")
	private Date fechaEmision;

	@Column(name = "fechaVencimiento")
	private Date fechaVencimiento;

	@Column(name = "importe")
	private Double importe;

	@Column(name = "recargo")
	private Double recargo;

	@Column(name = "saldo")
	private Double saldo;

	@Column(name = "strConceptos")
	private String strConceptos;

	@Column(name = "fechaPago")
	private Date fechaPago;

	public DeuAdmTriCon() {
		super();
	}

	/*
	public ClasificacionDeuda getClasificacionDeuda() {
		return clasificacionDeuda;
	}

	public void setClasificacionDeuda(ClasificacionDeuda clasificacionDeuda) {
		this.clasificacionDeuda = clasificacionDeuda;
	}
	public Sistema getSistema() {
		return sistema;
	}

	public void setSistema(Sistema sistema) {
		this.sistema = sistema;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}
	*/

	public DeudaAdmin getDeudaAdmin() {
		return deudaAdmin;
	}

	public void setDeudaAdmin(DeudaAdmin deudaAdmin) {
		this.deudaAdmin = deudaAdmin;
	}

	public TriCon getTriCon() {
		return triCon;
	}

	public void setTriCon(TriCon triCon) {
		this.triCon = triCon;
	}

	public Long getAnio() {
		return anio;
	}

	public void setAnio(Long anio) {
		this.anio = anio;
	}

	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaPago() {
		return fechaPago;
	}

	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Long getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Long periodo) {
		this.periodo = periodo;
	}

	public Double getRecargo() {
		return recargo;
	}

	public void setRecargo(Double recargo) {
		this.recargo = recargo;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public String getStrConceptos() {
		return strConceptos;
	}

	public void setStrConceptos(String strConceptos) {
		this.strConceptos = strConceptos;
	}
}
