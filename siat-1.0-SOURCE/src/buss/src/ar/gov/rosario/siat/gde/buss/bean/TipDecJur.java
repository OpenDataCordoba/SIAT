//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipDecJur
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipDecJur")
public class TipDecJur extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final long ID_ORIGINAL=1L;
	public static final long ID_RECTIFICATIVA=2L;
	
	
	@Column(name = "desTipo")
	private String desTipo;
	
	@Column(name = "abreviatura")
	private String abreviatura;

	//<#Propiedades#>
	
	// Constructores
	public TipDecJur(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	
	// Metodos de Clase
	public static TipDecJur getById(Long id) {
		return (TipDecJur) GdeDAOFactory.getTipDecJurDAO().getById(id);
	}
	
	public static TipDecJur getByIdNull(Long id) {
		return (TipDecJur) GdeDAOFactory.getTipDecJurDAO().getByIdNull(id);
	}
	
	public static List<TipDecJur> getList() {
		return (List<TipDecJur>) GdeDAOFactory.getTipDecJurDAO().getList();
	}
	
	public static List<TipDecJur> getListActivos() {			
		return (List<TipDecJur>) GdeDAOFactory.getTipDecJurDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipo() {
		return desTipo;
	}


	public void setDesTipo(String desTipo) {
		this.desTipo = desTipo;
	}
	
	public String getAbreviatura() {
		return abreviatura;
	}


	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}
	
	
	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}




	


	public boolean validateUpdate() throws Exception {
		// limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
	
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoDeudaPlan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipDecJurDAO().update(this);
	}

	/**
	 * Desactiva el TipoDeudaPlan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipDecJurDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoDeudaPlan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoDeudaPlan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
