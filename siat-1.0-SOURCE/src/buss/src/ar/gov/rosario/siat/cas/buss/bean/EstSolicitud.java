//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstSolicitud
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_estsolicitud")
public class EstSolicitud extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_PENDIENTE = 1L; 
	public static final Long ID_REALIZADO = 2L;	
	public static final Long ID_RECHAZADO = 3L;	
	public static final Long ID_FALLO_ACTUALIZ = 4L;
	public static final Long ID_APROBADO = 5L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	// Constructores
	public EstSolicitud(){
		super();
	}
	
	public EstSolicitud(Long id, String descripcion){
		super();
		setId(id);
		setDescripcion(descripcion);
	}
	
	// Metodos de Clase
	public static EstSolicitud getById(Long id) {
		return (EstSolicitud) CasDAOFactory.getEstSolicitudDAO().getById(id);
	}
	
	public static EstSolicitud getByIdNull(Long id) {
		return (EstSolicitud) CasDAOFactory.getEstSolicitudDAO().getByIdNull(id);
	}
	
	public static List<EstSolicitud> getList() {
		return (List<EstSolicitud>) CasDAOFactory.getEstSolicitudDAO().getList();
	}
	
	public static List<EstSolicitud> getListActivos() {			
		return (List<EstSolicitud>) CasDAOFactory.getEstSolicitudDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, ${BeanRelacionado}.class, "${bean}")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							CasError.${BEAN}_LABEL, CasError.${BEAN_RELACIONADO}_LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstSolicitud. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getEstSolicitudDAO().update(this);
	}

	/**
	 * Desactiva el EstSolicitud. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getEstSolicitudDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstSolicitud
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstSolicitud
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
