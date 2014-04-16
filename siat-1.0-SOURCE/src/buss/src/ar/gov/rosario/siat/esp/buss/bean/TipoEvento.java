//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean corresponiente al Tipo de Eventos para habilitar en los distintos Eventos o Espectaculos.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_tipoEvento")
public class TipoEvento extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	//Constructores 
	public TipoEvento(){
		super();
	}

	// Getters Y Setters 
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	

	// Metodos de clase	
	public static TipoEvento getById(Long id) {
		return (TipoEvento) EspDAOFactory.getTipoEventoDAO().getById(id);
	}
	
	public static TipoEvento getByIdNull(Long id) {
		return (TipoEvento) EspDAOFactory.getTipoEventoDAO().getByIdNull(id);
	}
	
	public static List<TipoEvento> getList() {
		return (ArrayList<TipoEvento>) EspDAOFactory.getTipoEventoDAO().getList();
	}
	
	public static List<TipoEvento> getListActivos() {			
		return (ArrayList<TipoEvento>) EspDAOFactory.getTipoEventoDAO().getListActiva();
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOEVENTO_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (GenericDAO.hasReference(this, Habilitacion.class, "tipoEvento")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.TIPOEVENTO_LABEL , EspError.HABILITACION_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
