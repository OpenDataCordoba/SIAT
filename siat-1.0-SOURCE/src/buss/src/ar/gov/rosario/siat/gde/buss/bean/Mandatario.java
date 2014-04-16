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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Mandatario
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_mandatario")
public class Mandatario extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "domicilio")
	private String domicilio;
	
	@Column(name = "telefono")
	private String telefono;
	
	@Column(name = "horarioAtencion")
	private String horarioAtencion;
	
	@Column(name = "observaciones")
	private String observaciones;
	
	
	// Constructores
	public Mandatario(){
		super();
		// Seteo de valores default	
	}
	
	public Mandatario(Long id){
		super();
		setId(id);
	}
	
	
	// Metodos de Clase
	public static Mandatario getById(Long id) {
		return (Mandatario) GdeDAOFactory.getMandatarioDAO().getById(id);
	}
	
	public static Mandatario getByIdNull(Long id) {
		return (Mandatario) GdeDAOFactory.getMandatarioDAO().getByIdNull(id);
	}
	
	public static List<Mandatario> getList() {
		return (ArrayList<Mandatario>) GdeDAOFactory.getMandatarioDAO().getList();
	}
	
	public static List<Mandatario> getListActivos() {			
		return (ArrayList<Mandatario>) GdeDAOFactory.getMandatarioDAO().getListActiva();
	}
	
	
	// Getters y setters

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDomicilio() {
		return domicilio;
	}

	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getHorarioAtencion() {
		return horarioAtencion;
	}

	public void setHorarioAtencion(String horarioAtencion) {
		this.horarioAtencion = horarioAtencion;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
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
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.${BEAN}_LABEL, GdeError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MANDATARIO_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Mandatario. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getMandatarioDAO().update(this);
	}

	/**
	 * Desactiva el Mandatario. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getMandatarioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Mandatario
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Mandatario
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
