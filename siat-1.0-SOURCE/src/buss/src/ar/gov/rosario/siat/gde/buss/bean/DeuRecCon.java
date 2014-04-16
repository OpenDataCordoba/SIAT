//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

import ar.gov.rosario.siat.def.buss.bean.RecCon;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Representa los conceptos que componen la deuda.
 * De esta extienden los concepto todas las deudas
 * 
 * @author tecso
 *
 */
@MappedSuperclass
public abstract class DeuRecCon extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
    @JoinColumn(name="idRecCon") 
	private RecCon recCon;

	@Column(name = "importeBruto")
	private Double importeBruto;

	@Column(name = "importe")
	private Double importe;

	@Column(name = "saldo")
	private Double saldo;

	// Contructores 

	public abstract Deuda getDeuda();	

	public abstract void setDeuda(Deuda deuda);

	
	public DeuRecCon() {
		super();
	}

	// Getters y Setters
	public RecCon getRecCon() {
		return recCon;
	}
	
	public void setRecCon(RecCon recCon) {
		this.recCon = recCon;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getImporteBruto() {
		return importeBruto;
	}

	public void setImporteBruto(Double importeBruto) {
		this.importeBruto = importeBruto;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

}
