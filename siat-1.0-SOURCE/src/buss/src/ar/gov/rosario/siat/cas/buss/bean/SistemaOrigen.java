//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a SistemaOrigen
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_sistemaOrigen")
public class SistemaOrigen extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desSistemaOrigen")
	private String desSistemaOrigen;
	
	@Column(name = "esValidable")
	private Integer esValidable;

	//<#Propiedades#>
	


	// Constructores
	public SistemaOrigen(){
		super();
		// Seteo de valores default			
	}
	
	public SistemaOrigen(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static SistemaOrigen getById(Long id) {
		return (SistemaOrigen) CasDAOFactory.getSistemaOrigenDAO().getById(id);
	}
	
	public static SistemaOrigen getByIdNull(Long id) {
		return (SistemaOrigen) CasDAOFactory.getSistemaOrigenDAO().getByIdNull(id);
	}
	
	public static List<SistemaOrigen> getList() {
		return (List<SistemaOrigen>) CasDAOFactory.getSistemaOrigenDAO().getList();
	}
	
	public static List<SistemaOrigen> getListActivos() {			
		return (List<SistemaOrigen>) CasDAOFactory.getSistemaOrigenDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesSistemaOrigen() {
		return desSistemaOrigen;
	}

	public void setDesSistemaOrigen(String desSistemaOrigen) {
		this.desSistemaOrigen = desSistemaOrigen;
	}

	public Integer getEsValidable() {
		return esValidable;
	}

	public void setEsValidable(Integer esValidable) {
		this.esValidable = esValidable;
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
	
	/**
	 * Activa el SistemaOrigen. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getSistemaOrigenDAO().update(this);
	}

	/**
	 * Desactiva el SistemaOrigen. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getSistemaOrigenDAO().update(this);
	}
	
	/**
	 * Valida la activacion del SistemaOrigen
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del SistemaOrigen
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
