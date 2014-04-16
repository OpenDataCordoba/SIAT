//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Zona
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_zona")
public class Zona extends BaseBO {

	private static final long serialVersionUID = 1L;
	public static final String ID_ZONA = "idZona";	
	
	@Column(name = "descripcion")
	private String descripcion;

	//Constructores
	
	public Zona(){
		super();
	}
	
	//Getters Y Setters
	
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	// Metodos de clase	
	public static Zona getById(Long id) {
		return (Zona) DefDAOFactory.getZonaDAO().getById(id);
	}
	
	public static Zona getByIdNull(Long id) {
		return (Zona) DefDAOFactory.getZonaDAO().getByIdNull(id);
	}
	
	public static List<Zona> getList() {
		return (ArrayList<Zona>) DefDAOFactory.getZonaDAO().getList();
	}
	
	public static List<Zona> getListActivos() {			
		return (ArrayList<Zona>) DefDAOFactory.getZonaDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
	
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
