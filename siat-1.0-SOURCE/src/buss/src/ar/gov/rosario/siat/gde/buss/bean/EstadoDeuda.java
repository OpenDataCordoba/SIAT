//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Estado Deuda
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_estadoDeuda")
public class EstadoDeuda extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final long ID_ADMINISTRATIVA = 1L;
	public static final long ID_ANULADA = 2L;
	public static final long ID_CANCELADA = 3L;
	public static final long ID_CONDONADA = 4L;
	public static final long ID_JUDICIAL = 5L;
	public static final long ID_PRESCRIPTA = 6L;
	
	@Column(name = "desEstadoDeuda")
	private String desEstadoDeuda;
	
	// Constructores
	public EstadoDeuda(){
		super();
	}
	
	// Getters y setters
	public String getDesEstadoDeuda() {
		return desEstadoDeuda;
	}
	public void setDesEstadoDeuda(String desEstadoDeuda) {
		this.desEstadoDeuda = desEstadoDeuda;
	}

	// Metodos de clase
	public static EstadoDeuda getById(Long id) {
		return (EstadoDeuda) GdeDAOFactory.getEstadoDeudaDAO().getById(id);
	}
	
	public static EstadoDeuda getByIdNull(Long id) {
		return (EstadoDeuda) GdeDAOFactory.getEstadoDeudaDAO().getByIdNull(id);
	}
	
	public static List<EstadoDeuda> getList() {
		return (ArrayList<EstadoDeuda>) GdeDAOFactory.getEstadoDeudaDAO().getList();
	}
	
	public static List<EstadoDeuda> getListActivos() {			
		return (ArrayList<EstadoDeuda>) GdeDAOFactory.getEstadoDeudaDAO().getListActiva();
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
