//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;

@Entity
@Table(name = "gde_deuAnuRecCon")
public class DeuAnuRecCon extends DeuRecCon {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idDeuda") 
	private DeudaAnulada deuda;

	// Contructores 
	
	public DeuAnuRecCon() {
		super();
	}

	
	// Getters y Setters
	public DeudaAnulada getDeuda() {
		return deuda;
	}

	public void setDeuda(Deuda deuda) {
		this.deuda = (DeudaAnulada) deuda;
	}

	
	// Metodos de clase

	public static DeuAnuRecCon getById(Long id) {
		return (DeuAnuRecCon) GdeDAOFactory.getDeuAnuRecConDAO().getById(id);
	}
	
	public static DeuAnuRecCon getByIdNull(Long id) {
		return (DeuAnuRecCon) GdeDAOFactory.getDeuAnuRecConDAO().getByIdNull(id);
	}
	
	public static List<DeuAnuRecCon> getList() {
		return (ArrayList<DeuAnuRecCon>) GdeDAOFactory.getDeuAnuRecConDAO().getList();
	}
	
	public static List<DeuAnuRecCon> getListActivos() {			
		return (ArrayList<DeuAnuRecCon>) GdeDAOFactory.getDeuAnuRecConDAO().getListActiva();
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author 
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}
		
		//Validaciones de Negocio
		
		return true;
	}

	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;		
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

	// Metodos de negocio
	

	

}
