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
 * Bean correspondiente a TipCueBan
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipCueBan")
public class TipCueBan extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@Column(name = "descripcion")
private String descripcion;

	//<#Propiedades#>
	
	// Constructores
	public TipCueBan(){
		super();
		// Seteo de valores default			
	}
	
	public TipCueBan(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipCueBan getById(Long id) {
		return (TipCueBan) BalDAOFactory.getTipCueBanDAO().getById(id);
	}
	
	public static TipCueBan getByIdNull(Long id) {
		return (TipCueBan) BalDAOFactory.getTipCueBanDAO().getByIdNull(id);
	}
	
	public static List<TipCueBan> getList() {
		return (List<TipCueBan>) BalDAOFactory.getTipCueBanDAO().getList();
	}
	
	public static List<TipCueBan> getListActivos() {			
		return (List<TipCueBan>) BalDAOFactory.getTipCueBanDAO().getListActiva();
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
		
		//Validaciones        
		
		
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPCUEBAN_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipCueBan. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getTipCueBanDAO().update(this);
	}

	/**
	 * Desactiva el TipCueBan. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getTipCueBanDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipCueBan
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipCueBan
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	//<#MetodosBeanDetalle#>
}
