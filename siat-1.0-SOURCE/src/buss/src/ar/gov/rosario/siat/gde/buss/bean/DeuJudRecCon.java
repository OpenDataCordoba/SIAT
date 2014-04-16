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

/**
 * Representa los conceptos que componen la deuda en via judicial.
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_deuJudRecCon")
public class DeuJudRecCon extends DeuRecCon {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
    @JoinColumn(name="idDeuda") 
	private DeudaJudicial deuda;

	// Contructores 
	public DeuJudRecCon() {
		super();
	}

	// Getters y Setters
	@Override
	public DeudaJudicial getDeuda() {
		return deuda;
	}

	

	public void setDeuda(Deuda deuda) {
		this.deuda = (DeudaJudicial) deuda;
	}

	
	// Metodos de clase
	public static DeuJudRecCon getById(Long id) {
		return (DeuJudRecCon) GdeDAOFactory.getDeuJudRecConDAO().getById(id);
	}
	
	public static DeuJudRecCon getByIdNull(Long id) {
		return (DeuJudRecCon) GdeDAOFactory.getDeuJudRecConDAO().getByIdNull(id);
	}
	
	public static List<DeuJudRecCon> getList() {
		return (ArrayList<DeuJudRecCon>) GdeDAOFactory.getDeuJudRecConDAO().getList();
	}
	
	public static List<DeuJudRecCon> getListActivos() {			
		return (ArrayList<DeuJudRecCon>) GdeDAOFactory.getDeuJudRecConDAO().getListActiva();
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
