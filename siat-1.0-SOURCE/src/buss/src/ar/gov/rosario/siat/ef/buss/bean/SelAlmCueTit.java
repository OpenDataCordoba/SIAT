//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.gov.rosario.siat.gde.buss.bean.SelAlm;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;

/**
 * Bean correspondiente a la Seleccion Almacenada de Contribuyentes
 * 
 * @author tecso
 */

@Entity
@DiscriminatorValue("6")
public class SelAlmCueTit extends SelAlm {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmDeuda";

	// Constructores
	public SelAlmCueTit(){
		super();
	}
	
	
	
	// Getters y Setters

	// Metodos de Clase
	public static SelAlmCueTit getById(Long id) {
		return (SelAlmCueTit) GdeDAOFactory.getSelAlmDeudaDAO().getById(id);  
	}
	
	public static SelAlmCueTit getByIdNull(Long id) {
		return (SelAlmCueTit) GdeDAOFactory.getSelAlmDAO().getByIdNull(id);
	}
	
	// Metodos de Instancia

	//Validaciones
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
	
	/**
	 * Obtiene la cantidad total de Detalles de la Seleccion Almacenada.
	 * return Long
	 */
	//public Long obtenerCantidadRegistros(){
	//	return GdeDAOFactory.getSelAlmDetDAO().getTotalBySelAlmDeuda(this);
	//}


}
