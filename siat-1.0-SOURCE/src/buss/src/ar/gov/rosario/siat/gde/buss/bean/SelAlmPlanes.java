//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.RescateAdapter;

/**
 * Bean correspondiente a la Seleccion Almacenada de Deuda
 * 
 * @author tecso
 */

@Entity
@DiscriminatorValue("2")
public class SelAlmPlanes extends SelAlm {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "selAlmPlanes";

	// Constructores
	public SelAlmPlanes(){
		super();
	}
	
	
	
	// Getters y Setters

	// Metodos de Clase
	public static SelAlmPlanes getById(Long id) {
		return (SelAlmPlanes) GdeDAOFactory.getSelAlmPlanesDAO().getById(id);  
	}
	
	public static SelAlmPlanes getByIdNull(Long id) {
		return (SelAlmPlanes) GdeDAOFactory.getSelAlmPlanesDAO().getByIdNull(id);
	}
	
	// Metodos de Instancia
	
	public List<SelAlmDet> getListSelAlmDet (){
		return GdeDAOFactory.getSelAlmPlanesDAO().getListByIdSelAlmPlan(this);
	}
	
	public SelAlmDet getSelAlmDetByIdElemento (Long id){
		return GdeDAOFactory.getSelAlmPlanesDAO().getSelAlmDetByIdElemento(this, id);
		
	}
	public List<Long> getListSelAlmDetPaged(RescateAdapter rescateAdapter)throws Exception{
		return GdeDAOFactory.getSelAlmDAO().getListIdConvenioSelAlmByRescate(rescateAdapter, this);
	}
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
	

	
}
