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
 * Bean correspondiente a Cierre de Sucursal incluido en el Cierre de Banco del Envio Osiris
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_cierreSucursal")
public class CierreSucursal extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco") 
	private CierreBanco cierreBanco;

	@Column(name = "nroCieSuc")
	private Long nroCieSuc;

	@Column(name = "sucursal")
	private Long sucursal;
	
	@Column(name = "tipoSucursal")
	private Integer tipoSucursal;
	
	@Column(name = "fechaRegistro")
	private Date fechaRegistro;
		
	@Column(name = "fechaCierre")
	private Date fechaCierre;
	
	// Constructores
	public CierreSucursal(){
		super();		
	}
	
	public CierreSucursal(Long id){
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
	
	public Date getFechaCierre() {
		return fechaCierre;
	}

	public void setFechaCierre(Date fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Long getNroCieSuc() {
		return nroCieSuc;
	}

	public void setNroCieSuc(Long nroCieSuc) {
		this.nroCieSuc = nroCieSuc;
	}

	public Long getSucursal() {
		return sucursal;
	}

	public void setSucursal(Long sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getTipoSucursal() {
		return tipoSucursal;
	}

	public void setTipoSucursal(Integer tipoSucursal) {
		this.tipoSucursal = tipoSucursal;
	}

	// Metodos de Clase
	public static CierreSucursal getById(Long id) {
		return (CierreSucursal) BalDAOFactory.getCierreSucursalDAO().getById(id);
	}
	
	public static CierreSucursal getByIdNull(Long id) {
		return (CierreSucursal) BalDAOFactory.getCierreSucursalDAO().getByIdNull(id);
	}
	
	public static List<CierreSucursal> getList() {
		return (ArrayList<CierreSucursal>) BalDAOFactory.getCierreSucursalDAO().getList();
	}
	
	public static List<CierreSucursal> getListActivos() {			
		return (ArrayList<CierreSucursal>) BalDAOFactory.getCierreSucursalDAO().getListActiva();
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
	 * Activa el CierreSucursal. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getCierreSucursalDAO().update(this);
	}

	/**
	 * Desactiva el CierreSucursal. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getCierreSucursalDAO().update(this);
	}
	
	/**
	 * Valida la activacion del CierreSucursal
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del CierreSucursal
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
}
