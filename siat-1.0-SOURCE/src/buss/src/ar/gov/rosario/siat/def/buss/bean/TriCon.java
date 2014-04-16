//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Conceptos del Tributo
 * @author tecso
 */
@Entity
@Table(name = "def_triCon")
public class TriCon extends BaseBO {

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idTributo") 
	private Tributo tributo;

	@Column(name = "codTriCon")
	private String codTriCon;

	@Column(name = "desTriCon")
	private String desTriCon;

	@Column(name = "abrevTriCon")
	private String abrevTriCon;

	@Column(name = "porcentaje")
	private Double porcentaje;

	@Column(name = "incrementa")
	private Integer incrementa;

	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	public TriCon() {
		super();
	}

	public Tributo getTributo() {
		return tributo;
	}
	public void setTributo(Tributo tributo) {
		this.tributo = tributo;
	}

	public String getAbrevTriCon() {
		return abrevTriCon;
	}
	public void setAbrevTriCon(String abrevTriCon) {
		this.abrevTriCon = abrevTriCon;
	}

	public String getCodTriCon() {
		return codTriCon;
	}
	public void setCodTriCon(String codTriCon) {
		this.codTriCon = codTriCon;
	}

	public String getDesTriCon() {
		return desTriCon;
	}
	public void setDesTriCon(String desTriCon) {
		this.desTriCon = desTriCon;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Integer getIncrementa() {
		return incrementa;
	}
	public void setIncrementa(Integer incrementa) {
		this.incrementa = incrementa;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
}
