//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Detalle de Movimientos Bancarios. Información desagregada a nivel de impuesto.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_movBanDet")
public class MovBanDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idMovBan") 
	private MovBan movBan;
	
	@Column(name = "impuesto")
	private Long impuesto;

	@Column(name = "bancoRec")
	private Long bancoRec;
	
	@Column(name = "anexoOperativo")
	private Integer anexoOperativo;
	
	@Column(name = "nroCierreBanco")
	private Long nroCierreBanco;
		
	@Column(name = "debito")
	private Double debito;
	
	@Column(name = "credito")
	private Double credito;
	
	@Column(name = "nroCuenta")
	private String nroCuenta;
	
	@Column(name = "moneda")
	private Integer moneda;

	@Column(name = "conciliado")
	private Integer conciliado;

	// Constructores
	public MovBanDet(){
		super();		
	}
	
	public MovBanDet(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public Integer getAnexoOperativo() {
		return anexoOperativo;
	}

	public void setAnexoOperativo(Integer anexoOperativo) {
		this.anexoOperativo = anexoOperativo;
	}

	public Long getBancoRec() {
		return bancoRec;
	}

	public void setBancoRec(Long bancoRec) {
		this.bancoRec = bancoRec;
	}

	public Integer getConciliado() {
		return conciliado;
	}

	public void setConciliado(Integer conciliado) {
		this.conciliado = conciliado;
	}

	public Double getCredito() {
		return credito;
	}

	public void setCredito(Double credito) {
		this.credito = credito;
	}

	public Double getDebito() {
		return debito;
	}

	public void setDebito(Double debito) {
		this.debito = debito;
	}

	public Long getImpuesto() {
		return impuesto;
	}

	public void setImpuesto(Long impuesto) {
		this.impuesto = impuesto;
	}

	public Integer getMoneda() {
		return moneda;
	}

	public void setMoneda(Integer moneda) {
		this.moneda = moneda;
	}

	public MovBan getMovBan() {
		return movBan;
	}

	public void setMovBan(MovBan movBan) {
		this.movBan = movBan;
	}

	public Long getNroCierreBanco() {
		return nroCierreBanco;
	}

	public void setNroCierreBanco(Long nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
	}

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	
	// Metodos de Clase
	public static MovBanDet getById(Long id) {
		return (MovBanDet) BalDAOFactory.getMovBanDetDAO().getById(id);
	}
	
	public static MovBanDet getByIdNull(Long id) {
		return (MovBanDet) BalDAOFactory.getMovBanDetDAO().getByIdNull(id);
	}
	
	public static List<MovBanDet> getList() {
		return (ArrayList<MovBanDet>) BalDAOFactory.getMovBanDetDAO().getList();
	}
	
	public static List<MovBanDet> getListActivos() {			
		return (ArrayList<MovBanDet>) BalDAOFactory.getMovBanDetDAO().getListActiva();
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
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el MovBanDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getMovBanDetDAO().update(this);
	}

	/**
	 * Desactiva el MovBanDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getMovBanDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del MovBanDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del MovBanDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}


}
