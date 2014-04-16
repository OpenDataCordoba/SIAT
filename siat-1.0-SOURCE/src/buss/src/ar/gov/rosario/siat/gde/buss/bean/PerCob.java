//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a PerCob
 * Representa el personal con funciones en las distintas Cobranzas
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_perCob")
public class PerCob extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idArea")
	private Area area;
		
	@Column(name="nombreApellido")
	private String nombreApellido;
	
	
	
	// Constructores
	public PerCob(){
		super();
	}
	
	// Getters y Setters
	
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public String getNombreApellido() {
		return nombreApellido;
	}

	public void setNombreApellido(String nombreApellido) {
		this.nombreApellido = nombreApellido;
	}

	// Metodos de Clase
	public static PerCob getById(Long id) {
		return (PerCob) GdeDAOFactory.getPerCobDAO().getById(id);  
	}
	
	public static PerCob getByIdNull(Long id) {
		return (PerCob) GdeDAOFactory.getPerCobDAO().getByIdNull(id);
	}
	
	public static List<PerCob> getList() {
		return (List<PerCob>) GdeDAOFactory.getPerCobDAO().getList();
	}
	
	public static List<PerCob> getListActivos() {			
		return (List<PerCob>) GdeDAOFactory.getPerCobDAO().getListActiva();
	}
	
	public static List<PerCob> getListVigenteByArea(Area area){
		return GdeDAOFactory.getPerCobDAO().getListVigenteByArea(area);
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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		
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

		if (GenericDAO.hasReference(this, Cobranza.class, "perCob")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				GdeError.PERCOB_LABEL , GdeError.COBRANZA_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio


	/**
	 * Activa el PerCob. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPerCobDAO().update(this);
	}

	/**
	 * Desactiva el PerCob. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPerCobDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PerCob
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PerCob
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
}
