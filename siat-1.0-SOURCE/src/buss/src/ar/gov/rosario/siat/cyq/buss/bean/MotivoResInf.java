//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a MotivoResInf
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_motivoResInf")
public class MotivoResInf extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desMotivoResInf")
	private String desMotivoResInf;

	//<#Propiedades#>
	
	// Constructores
	public MotivoResInf(){
		super();
		// Seteo de valores default			
	}
	
	public MotivoResInf(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MotivoResInf getById(Long id) {
		return (MotivoResInf) CyqDAOFactory.getMotivoResInfDAO().getById(id);
	}
	
	public static MotivoResInf getByIdNull(Long id) {
		return (MotivoResInf) CyqDAOFactory.getMotivoResInfDAO().getByIdNull(id);
	}
	
	public static List<MotivoResInf> getList() {
		return (List<MotivoResInf>) CyqDAOFactory.getMotivoResInfDAO().getList();
	}
	
	public static List<MotivoResInf> getListActivos() {			
		return (List<MotivoResInf>) CyqDAOFactory.getMotivoResInfDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesMotivoResInf() {
		return desMotivoResInf;
	}
	public void setDesMotivoResInf(String desMotivoResInf) {
		this.desMotivoResInf = desMotivoResInf;
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
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el MotivoResInf. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getMotivoResInfDAO().update(this);
	}

	/**
	 * Desactiva el MotivoResInf. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getMotivoResInfDAO().update(this);
	}
	
	/**
	 * Valida la activacion del MotivoResInf
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del MotivoResInf
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
