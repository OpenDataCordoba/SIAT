//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Seccion
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_seccion")
public class Seccion extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "descripcion")
	private String descripcion;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idZona")
	private Zona zona;

	//Constructores

	public Seccion(){
		super();
	}
	
	//Getters Y Setters
	
	public String getDescripcion() {
		return descripcion;
	}
	
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	public Zona getZona() {
		return zona;
	}
	
	public void setZona(Zona zona) {
		this.zona = zona;
	}
	
	// Metodos de clase	
	public static Seccion getById(Long id) {
		return (Seccion) DefDAOFactory.getSeccionDAO().getById(id);
	}
	
	public static Seccion getByIdNull(Long id) {
		return (Seccion) DefDAOFactory.getSeccionDAO().getByIdNull(id);
	}
	
	public static List<Seccion> getList() {
		return (ArrayList<Seccion>) DefDAOFactory.getSeccionDAO().getList();
	}
	
	public static List<Seccion> getListActivos() {			
		return (ArrayList<Seccion>) DefDAOFactory.getSeccionDAO().getListActiva();
	}
	
	public static List<Seccion> getListActivosOrder() {			
		return (ArrayList<Seccion>) DefDAOFactory.getSeccionDAO().getListActivosOrder();
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
