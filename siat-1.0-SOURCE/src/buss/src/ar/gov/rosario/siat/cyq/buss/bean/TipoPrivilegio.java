//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a TipoPrivilegio - Tipo de Privilegio
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_tipoPrivilegio")
public class TipoPrivilegio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;

	//<#Propiedades#>
	
	// Constructores
	public TipoPrivilegio(){
		super();
		// Seteo de valores default			
	}
	
	public TipoPrivilegio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoPrivilegio getById(Long id) {
		return (TipoPrivilegio) CyqDAOFactory.getTipoPrivilegioDAO().getById(id);
	}
	
	public static TipoPrivilegio getByIdNull(Long id) {
		return (TipoPrivilegio) CyqDAOFactory.getTipoPrivilegioDAO().getByIdNull(id);
	}
	
	public static List<TipoPrivilegio> getList() {
		return (ArrayList<TipoPrivilegio>) CyqDAOFactory.getTipoPrivilegioDAO().getList();
	}
	
	public static List<TipoPrivilegio> getListActivos() {			
		return (ArrayList<TipoPrivilegio>) CyqDAOFactory.getTipoPrivilegioDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
}
