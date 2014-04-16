//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.bean.RetYPer;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a DecJurPag
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_decJurPag")
public class DecJurPag extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idDecJur")
	private DecJur decJur;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idTipPagDecJur")
	private TipPagDecJur tipPagDecJur;
	
	@Column(name="detalle")
	private String detalle;
	
	@Column(name="importe")
	private Double importe;
	
	@Column(name="saldo")
	private Double saldo;
	
	@Column(name="certificado")
	private String certificado;
	
	@Column(name="cuitAgente")
	private String cuitAgente;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idAgeRet")
	private AgeRet ageRet;
	
	@Column(name="fechaPago")
	private Date fechaPago;
	
	@ManyToOne(optional=true,fetch=FetchType.LAZY)
	@JoinColumn(name="idRetYPer")
	private RetYPer retYPer;
	
	
	// Constructores
	public DecJurPag(){
		super();
	}
	
	
	// Metodos de Clase
	public static DecJurPag getById(Long id) {
		return (DecJurPag) GdeDAOFactory.getDecJurPagDAO().getById(id);
	}
	
	public static DecJurPag getByIdNull(Long id) {
		return (DecJurPag) GdeDAOFactory.getDecJurPagDAO().getByIdNull(id);
	}
	
	public static List<DecJurPag> getList() {
		return (List<DecJurPag>) GdeDAOFactory.getDecJurPagDAO().getList();
	}
	
	public static List<DecJurPag> getListActivos() {			
		return (List<DecJurPag>) GdeDAOFactory.getDecJurPagDAO().getListActiva();
	}
	
	
	// Getters y setters
	public DecJur getDecJur() {
		return decJur;
	}


	public void setDecJur(DecJur decJur) {
		this.decJur = decJur;
	}


	public TipPagDecJur getTipPagDecJur() {
		return tipPagDecJur;
	}


	public void setTipPagDecJur(TipPagDecJur tipPagDecJur) {
		this.tipPagDecJur = tipPagDecJur;
	}


	public String getDetalle() {
		return detalle;
	}


	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}


	public Double getImporte() {
		return importe;
	}


	public void setImporte(Double importe) {
		this.importe = importe;
	}


	public Double getSaldo() {
		return saldo;
	}


	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}


	public String getCertificado() {
		return certificado;
	}


	public void setCertificado(String certificado) {
		this.certificado = certificado;
	}


	public String getCuitAgente() {
		return cuitAgente;
	}


	public void setCuitAgente(String cuitAgente) {
		this.cuitAgente = cuitAgente;
	}


	public Date getFechaPago() {
		return fechaPago;
	}


	public void setFechaPago(Date fechaPago) {
		this.fechaPago = fechaPago;
	}

	public AgeRet getAgeRet() {
		return ageRet;
	}
	public void setAgeRet(AgeRet ageRet) {
		this.ageRet = ageRet;
	}

	public RetYPer getRetYPer() {
		return retYPer;
	}

	public void setRetYPer(RetYPer retYPer) {
		this.retYPer = retYPer;
	}


	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	
		
		return true;
	}
	
	// Metodos de negocio

		
	
	//<#MetodosBeanDetalle#>
}
