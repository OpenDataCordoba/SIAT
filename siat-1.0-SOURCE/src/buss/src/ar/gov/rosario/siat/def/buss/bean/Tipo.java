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
 * Bean correspondiente a Tipo
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipo")
public class Tipo extends BaseBO {
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_TIPO_TRIBUTARIA=1L;
	public static final Long ID_TIPO_NOTRIB=2L;

	@Column(name = "desTipo") 
	private String desTipo;
	
	// Constructores
	public Tipo(){
		super();
	}
	
	// Getters y setters
	public String getDesTipo() {
		return desTipo;
	}
	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}

	// Metodos de clase
	public static Tipo getById(Long id) {
		return (Tipo) DefDAOFactory.getTipoDAO().getById(id);
	}
	
	public static Tipo getByIdNull(Long id) {
		return (Tipo) DefDAOFactory.getTipoDAO().getByIdNull(id);
	}
	
	public static List<Tipo> getList() {
		return (ArrayList<Tipo>) DefDAOFactory.getTipoDAO().getList();
	}
	
	public static List<Tipo> getListActivos() {			
		return (ArrayList<Tipo>) DefDAOFactory.getTipoDAO().getListActiva();
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
