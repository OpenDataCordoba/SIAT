//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
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
@Table(name = "gde_oriDecJur")
public class OriDecJur extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_CMD = 1L;
	public static final Long ID_ENVIO_OSIRIS = 2L;
	
	@Column(name = "desOrigen")
	private String desOrigen;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;

	@Column(name = "fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public OriDecJur(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	
	// Metodos de Clase
	public static OriDecJur getById(Long id) {
		return (OriDecJur) GdeDAOFactory.getOriDecJurDAO().getById(id);
	}
	
	public static OriDecJur getByIdNull(Long id) {
		return (OriDecJur) GdeDAOFactory.getOriDecJurDAO().getByIdNull(id);
	}
	
	public static List<OriDecJur> getList() {
		return (List<OriDecJur>) GdeDAOFactory.getOriDecJurDAO().getList();
	}
	
	public static List<OriDecJur> getListActivos() {			
		return (List<OriDecJur>) GdeDAOFactory.getOriDecJurDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesOrigen() {
		return desOrigen;
	}


	public void setDesOrigen(String desOrigen) {
		this.desOrigen = desOrigen;
	}


	public Date getFechaDesde() {
		return fechaDesde;
	}


	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}


	public Date getFechaHasta() {
		return fechaHasta;
	}


	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
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
