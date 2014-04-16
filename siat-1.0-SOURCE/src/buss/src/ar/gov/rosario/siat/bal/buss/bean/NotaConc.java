//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
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
 * Bean correspondiente a Notas Conciliadas del Envio Osiris.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_notaConc")
public class NotaConc extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idConciliacion") 
	private Conciliacion conciliacion;

	@Column(name = "fechaConciliacion")
	private Date fechaConciliacion;

	@Column(name = "nroCuenta")
	private String nroCuenta;
	
	@Column(name = "impuesto")
	private Long impuesto;
	
	@Column(name = "moneda")
	private Integer moneda;
		
	@Column(name = "tipoNota")
	private Integer tipoNota;
	
	@Column(name = "importe")
	private Double importe;

	@Column(name = "fechaAcredit")
	private Date fechaAcredit;
	
	// Constructores
	public NotaConc(){
		super();		
	}
	
	public NotaConc(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public Conciliacion getConciliacion() {
		return conciliacion;
	}

	public void setConciliacion(Conciliacion conciliacion) {
		this.conciliacion = conciliacion;
	}

	public Date getFechaAcredit() {
		return fechaAcredit;
	}

	public void setFechaAcredit(Date fechaAcredit) {
		this.fechaAcredit = fechaAcredit;
	}

	public Date getFechaConciliacion() {
		return fechaConciliacion;
	}

	public void setFechaConciliacion(Date fechaConciliacion) {
		this.fechaConciliacion = fechaConciliacion;
	}

	public Double getImporte() {
		return importe;
	}

	public void setImporte(Double importe) {
		this.importe = importe;
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
	public static NotaConc getById(Long id) {
		return (NotaConc) BalDAOFactory.getNotaConcDAO().getById(id);
	}
	
	public static NotaConc getByIdNull(Long id) {
		return (NotaConc) BalDAOFactory.getNotaConcDAO().getByIdNull(id);
	}
	
	public static List<NotaConc> getList() {
		return (ArrayList<NotaConc>) BalDAOFactory.getNotaConcDAO().getList();
	}
	
	public static List<NotaConc> getListActivos() {			
		return (ArrayList<NotaConc>) BalDAOFactory.getNotaConcDAO().getListActiva();
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
	 * Activa el NotaConc. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getNotaConcDAO().update(this);
	}

	/**
	 * Desactiva el NotaConc. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getNotaConcDAO().update(this);
	}
	
	/**
	 * Valida la activacion del NotaConc
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del NotaConc
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
