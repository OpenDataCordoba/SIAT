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
 * Bean correspondiente a Notas de Abono incluidas en el Cierre Banco del Envio Osiris.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_notaImpto")
public class NotaImpto extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco") 
	private CierreBanco cierreBanco;

	@Column(name = "tipoNota")
	private Integer tipoNota;

	@Column(name = "impuesto")
	private Long impuesto;
	
	@Column(name = "moneda")
	private Integer moneda;
	
	@Column(name = "nroCuenta")
	private String nroCuenta;
		
	@Column(name = "importe")
	private Double importe;
	
	@Column(name = "importeIVA")
	private Double importeIVA;
	
	// Constructores
	public NotaImpto(){
		super();		
	}
	
	public NotaImpto(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public CierreBanco getCierreBanco() {
		return cierreBanco;
	}

	public void setCierreBanco(CierreBanco cierreBanco) {
		this.cierreBanco = cierreBanco;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
	}

	public Double getImporteIVA() {
		return importeIVA;
	}

	public void setImporteIVA(Double importeIVA) {
		this.importeIVA = importeIVA;
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

	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public Integer getTipoNota() {
		return tipoNota;
	}

	public void setTipoNota(Integer tipoNota) {
		this.tipoNota = tipoNota;
	}

	
	// Metodos de Clase
	public static NotaImpto getById(Long id) {
		return (NotaImpto) BalDAOFactory.getNotaImptoDAO().getById(id);
	}
	
	public static NotaImpto getByIdNull(Long id) {
		return (NotaImpto) BalDAOFactory.getNotaImptoDAO().getByIdNull(id);
	}
	
	public static List<NotaImpto> getList() {
		return (ArrayList<NotaImpto>) BalDAOFactory.getNotaImptoDAO().getList();
	}
	
	public static List<NotaImpto> getListActivos() {			
		return (ArrayList<NotaImpto>) BalDAOFactory.getNotaImptoDAO().getListActiva();
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
	 * Activa el NotaImpto. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getNotaImptoDAO().update(this);
	}

	/**
	 * Desactiva el NotaImpto. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getNotaImptoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del NotaImpto
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del NotaImpto
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}
