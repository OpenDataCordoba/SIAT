//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoOrigen
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoorigen")
public class TipoOrigen extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoOrigen";
	
	public static final Long ID_ASENTAMIENTO = 1L;
	public static final Long ID_AREAS = 2L;

	
	@Column(name = "desTipoOrigen")
	private String desTipoOrigen;

	//<#Propiedades#>
	
	// Constructores
	public TipoOrigen(){
		super();
		// Seteo de valores default			
	}
	
	public TipoOrigen(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoOrigen getById(Long id) {
		return (TipoOrigen) BalDAOFactory.getTipoOrigenDAO().getById(id);
	}
	
	public static TipoOrigen getByIdNull(Long id) {
		return (TipoOrigen) BalDAOFactory.getTipoOrigenDAO().getByIdNull(id);
	}
	
	public static List<TipoOrigen> getList() {
		return (List<TipoOrigen>) BalDAOFactory.getTipoOrigenDAO().getList();
	}
	
	public static List<TipoOrigen> getListActivos() {			
		return (List<TipoOrigen>) BalDAOFactory.getTipoOrigenDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
		
		if (StringUtil.isNullOrEmpty(getDesTipoOrigen())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOORIGEN_DESTIPOORIGEN);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoOrigen. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getTipoOrigenDAO().update(this);
	}

	/**
	 * Desactiva el TipoOrigen. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getTipoOrigenDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoOrigen
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoOrigen
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public String getDesTipoOrigen() {
		return desTipoOrigen;
	}

	public void setDesTipoOrigen(String desTipoOrigen) {
		this.desTipoOrigen = desTipoOrigen;
	}
	
	//<#MetodosBeanDetalle#>
}
