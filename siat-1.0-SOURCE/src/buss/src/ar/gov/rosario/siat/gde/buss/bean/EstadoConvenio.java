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
 * Bean correspondiente a EstadoConvenio
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_estadoConvenio")
public class EstadoConvenio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_VIGENTE     = 1L;
	public static final long ID_ANULADO	    = 2L;
	public static final long ID_RECOMPUESTO = 3L;
	public static final long ID_CANCELADO   = 4L;
	public static final String DESC_CADUCO  = "Caduco";
	
	@Column(name = "desEstadoConvenio")
	private String desEstadoConvenio;

	//<#Propiedades#>
	
	// Constructores
	public EstadoConvenio(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public EstadoConvenio(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoConvenio getById(Long id) {
		return (EstadoConvenio) GdeDAOFactory.getEstadoConvenioDAO().getById(id);
	}
	
	public static EstadoConvenio getByIdNull(Long id) {
		return (EstadoConvenio) GdeDAOFactory.getEstadoConvenioDAO().getByIdNull(id);
	}
	
	public static List<EstadoConvenio> getList() {
		return (List<EstadoConvenio>) GdeDAOFactory.getEstadoConvenioDAO().getList();
	}
	
	public static List<EstadoConvenio> getListActivos() {			
		return (List<EstadoConvenio>) GdeDAOFactory.getEstadoConvenioDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesEstadoConvenio() {
		return desEstadoConvenio;
	}

	public void setDesEstadoConvenio(String desEstadoConvenio) {
		this.desEstadoConvenio = desEstadoConvenio;
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
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getCodEstadoConvenio())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADOCONVENIO_CODESTADOCONVENIO );
		}
		
		if (StringUtil.isNullOrEmpty(getDesEstadoConvenio())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADOCONVENIO_DESESTADOCONVENIO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEstadoConvenio");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.ESTADOCONVENIO_CODESTADOCONVENIO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstadoConvenio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getEstadoConvenioDAO().update(this);
	}

	/**
	 * Desactiva el EstadoConvenio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getEstadoConvenioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoConvenio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoConvenio
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
