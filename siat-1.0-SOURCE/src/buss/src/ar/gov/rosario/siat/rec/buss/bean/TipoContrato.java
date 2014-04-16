//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a TipoContrato
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_tipocontrato")
public class TipoContrato extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	// Constructores
	public TipoContrato(){
		super();
	}
	
	// Metodos de Clase
	public static TipoContrato getById(Long id) {
		return (TipoContrato) RecDAOFactory.getTipoContratoDAO().getById(id);
	}
	
	public static TipoContrato getByIdNull(Long id) {
		return (TipoContrato) RecDAOFactory.getTipoContratoDAO().getByIdNull(id);
	}
	
	public static List<TipoContrato> getList() {
		return (List<TipoContrato>) RecDAOFactory.getTipoContratoDAO().getList();
	}
	
	public static List<TipoContrato> getListActivos() {			
		return (List<TipoContrato>) RecDAOFactory.getTipoContratoDAO().getListActiva();
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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoContrato. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getTipoContratoDAO().update(this);
	}

	/**
	 * Desactiva el TipoContrato. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getTipoContratoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoContrato
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoContrato
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
