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
 * Bean correspondiente a TipPlaCuaDet
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_tipPlaCuaDet")
public class TipPlaCuaDet extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long ID_PARCELA         = 1L;
	public static final Long ID_CARPETA         = 2L;
	public static final Long ID_UNIDADFUNCIONAL = 3L;	

	@Column(name = "desTipPlaCuaDet")
	private String desTipPlaCuaDet;

	// Constructores
	public TipPlaCuaDet(){
		super();
	}
	
	public TipPlaCuaDet(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipPlaCuaDet getById(Long id) {
		return (TipPlaCuaDet) RecDAOFactory.getTipPlaCuaDetDAO().getById(id);
	}
	
	public static TipPlaCuaDet getByIdNull(Long id) {
		return (TipPlaCuaDet) RecDAOFactory.getTipPlaCuaDetDAO().getByIdNull(id);
	}
	
	public static List<TipPlaCuaDet> getList() {
		return (List<TipPlaCuaDet>) RecDAOFactory.getTipPlaCuaDetDAO().getList();
	}
	
	public static List<TipPlaCuaDet> getListActivos() {			
		return (List<TipPlaCuaDet>) RecDAOFactory.getTipPlaCuaDetDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipPlaCuaDet() {
		return desTipPlaCuaDet;
	}

	public void setDesTipPlaCuaDet(String desTipPlaCuaDet) {
		this.desTipPlaCuaDet = desTipPlaCuaDet;
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

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipPlaCuaDet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getTipPlaCuaDetDAO().update(this);
	}

	/**
	 * Desactiva el TipPlaCuaDet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getTipPlaCuaDetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipPlaCuaDet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipPlaCuaDet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
