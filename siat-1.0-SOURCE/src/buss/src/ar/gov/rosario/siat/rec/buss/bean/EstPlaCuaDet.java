//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstPlaCuaDet
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_estPlaCuaDet")
public class EstPlaCuaDet extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_INFORMADA  = 1L;
	public static final Long ID_A_EMITIR   = 2L;
	public static final Long ID_EMITIDA    = 3L;	
	public static final Long ID_MODIFICADA = 4L;
	public static final Long ID_ANULADA    = 5L;
	public static final Long ID_REEMITIDA  = 6L;	
	
	@Column(name = "desEstPlaCuaDet")
	private String desEstPlaCuaDet;
	
	// Constructores
	public EstPlaCuaDet(){
		super();
		// Seteo de valores default	
	}
	
	public EstPlaCuaDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstPlaCuaDet getById(Long id) {
		return (EstPlaCuaDet) RecDAOFactory.getEstPlaCuaDetDAO().getById(id);
	}
	
	public static EstPlaCuaDet getByIdNull(Long id) {
		return (EstPlaCuaDet) RecDAOFactory.getEstPlaCuaDetDAO().getByIdNull(id);
	}
	
	public static List<EstPlaCuaDet> getList() {
		return (List<EstPlaCuaDet>) RecDAOFactory.getEstPlaCuaDetDAO().getList();
	}
	
	public static List<EstPlaCuaDet> getListActivos() {			
		return (List<EstPlaCuaDet>) RecDAOFactory.getEstPlaCuaDetDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesEstPlaCuaDet() {
		return desEstPlaCuaDet;
	}

	public void setDesEstPlaCuaDet(String desEstPlaCuaDet) {
		this.desEstPlaCuaDet = desEstPlaCuaDet;
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
							RecError.${BEAN}_LABEL, RecError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesEstPlaCuaDet())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ESTPLACUADET_DESESTPLACUADET);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstPlaCuaDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getEstPlaCuaDetDAO().update(this);
	}

	/**
	 * Desactiva el EstPlaCuaDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getEstPlaCuaDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstPlaCuaDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstPlaCuaDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
