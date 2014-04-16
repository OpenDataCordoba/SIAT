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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean corresponiente al Lugar del Evento.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_lugarEvento")
public class LugarEvento extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "domicilio")
	private String domicilio;
	
	@Column(name = "factorOcupacional")
	private Long factorOcupacional;
	
	//Constructores 
	public LugarEvento(){
		super();
	}

	// Getters Y Setters 
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
	public Long getFactorOcupacional() {
		return factorOcupacional;
	}
	public void setFactorOcupacional(Long factorOcupacional) {
		this.factorOcupacional = factorOcupacional;
	}
	
	// Metodos de clase	
	public static LugarEvento getById(Long id) {
		return (LugarEvento) EspDAOFactory.getLugarEventoDAO().getById(id);
	}
	
	public static LugarEvento getByIdNull(Long id) {
		return (LugarEvento) EspDAOFactory.getLugarEventoDAO().getByIdNull(id);
	}
	
	public static List<LugarEvento> getList() {
		return (ArrayList<LugarEvento>) EspDAOFactory.getLugarEventoDAO().getList();
	}
	
	public static List<LugarEvento> getListActivos() {			
		return (ArrayList<LugarEvento>) EspDAOFactory.getLugarEventoDAO().getListActiva();
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
		if(StringUtil.isNullOrEmpty(getDomicilio())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.LUGAREVENTO_DOMICILIO);
		}

		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.LUGAREVENTO_DESCRIPCION);
		}
		
		if(getFactorOcupacional() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.LUGAREVENTO_FACTOROCUPACIONAL);
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
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
