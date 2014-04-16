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
@Table(name = "gde_deuCanRecCon")
public class DeuCanRecCon extends DeuRecCon {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false) 
    @JoinColumn(name="idDeuda") 
	private DeudaCancelada deuda;

	// Contructores 
	
	public DeuCanRecCon() {
		super();
	}
	
	// Getters y Setters
	@Override
	public DeudaCancelada getDeuda() {
		return deuda;
	}

	public void setDeuda(DeudaCancelada deuda) {
		this.deuda = deuda;
	}


	public void setDeuda(Deuda deuda) {
		this.deuda = (DeudaCancelada) deuda;
	}

	// Metodos de clase

	public static DeuCanRecCon getById(Long id) {
		return (DeuCanRecCon) GdeDAOFactory.getDeuCanRecConDAO().getById(id);
	}
	
	public static DeuCanRecCon getByIdNull(Long id) {
		return (DeuCanRecCon) GdeDAOFactory.getDeuCanRecConDAO().getByIdNull(id);
	}
	
	public static List<DeuCanRecCon> getList() {
		return (ArrayList<DeuCanRecCon>) GdeDAOFactory.getDeuCanRecConDAO().getList();
	}
	
	public static List<DeuCanRecCon> getListActivos() {			
		return (ArrayList<DeuCanRecCon>) GdeDAOFactory.getDeuCanRecConDAO().getListActiva();
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
