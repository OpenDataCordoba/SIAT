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
 * Accion de Logeo 
 * @author tecso
 *
 */
@Entity
@Table(name = "gde_accionLog")
public class AccionLog extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final long ID_LIMPIAR_SELECCION = 1L;
	public static final long ID_AGREGAR_MASIVO = 2L;
	public static final long ID_AGREGAR_MASIVO_CON_CHECKS = 3L;
	public static final long ID_ELIMINAR_MASIVO = 4L;
	public static final long ID_ELIMINAR_MASIVO_CON_CHECKS = 5L;
	public static final long ID_ELIMINAR_PUNTUAL = 6L;
	
	@Column(name = "desAccionLog")
	private String desAccionLog;
	

	// Constructores
	public AccionLog(){
		super();
	}
	
	// Getters y setters
	public String getDesAccionLog() {
		return desAccionLog;
	}
	public void setDesAccionLog(String desAccionLog) {
		this.desAccionLog = desAccionLog;
	}
	

	// Metodos de clase
	public static AccionLog getById(Long id) {
		return (AccionLog) GdeDAOFactory.getAccionLogDAO().getById(id);
	}
	
	public static AccionLog getByIdNull(Long id) {
		return (AccionLog) GdeDAOFactory.getAccionLogDAO().getByIdNull(id);
	}
	
	public static List<AccionLog> getList() {
		return (ArrayList<AccionLog>) GdeDAOFactory.getAccionLogDAO().getList();
	}
	
	public static List<AccionLog> getListActivos() {			
		return (ArrayList<AccionLog>) GdeDAOFactory.getAccionLogDAO().getListActiva();
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
