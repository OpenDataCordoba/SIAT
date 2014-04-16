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
 * Bean correspondiente a TipoSalAFav
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoSalAFav")
public class TipoSalAFav extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@Column(name = "desTipoSalAFav")
private String desTipoSalAFav;

	//<#Propiedades#>
	
	// Constructores
	public TipoSalAFav(){
		super();
		// Seteo de valores default			
	}
	
	public TipoSalAFav(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoSalAFav getById(Long id) {
		return (TipoSalAFav) BalDAOFactory.getTipoSalAFavDAO().getById(id);
	}
	
	public static TipoSalAFav getByIdNull(Long id) {
		return (TipoSalAFav) BalDAOFactory.getTipoSalAFavDAO().getByIdNull(id);
	}
	
	public static List<TipoSalAFav> getList() {
		return (List<TipoSalAFav>) BalDAOFactory.getTipoSalAFavDAO().getList();
	}
	
	public static List<TipoSalAFav> getListActivos() {			
		return (List<TipoSalAFav>) BalDAOFactory.getTipoSalAFavDAO().getListActiva();
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
		
		
		if (StringUtil.isNullOrEmpty(getDesTipoSalAFav())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOSALAFAV_DESTIPOSALAFAV);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoSalAFav. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getTipoSalAFavDAO().update(this);
	}

	/**
	 * Desactiva el TipoSalAFav. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getTipoSalAFavDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoSalAFav
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoSalAFav
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public String getDesTipoSalAFav() {
		return desTipoSalAFav;
	}

	public void setDesTipoSalAFav(String desTipoSalAFav) {
		this.desTipoSalAFav = desTipoSalAFav;
	}
	
	//<#MetodosBeanDetalle#>
}
