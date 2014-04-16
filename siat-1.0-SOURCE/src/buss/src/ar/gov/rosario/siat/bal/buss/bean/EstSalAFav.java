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
 * Bean correspondiente a EstSalAFav
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estSalAFav")
public class EstSalAFav extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	public static final long ID_CREADO = 1L;
	public static final long ID_EN_COMPENSACION = 2L;
	
@Column(name = "desEstSalAFav")
private String desEstSalAFav;

	//<#Propiedades#>
	
	// Constructores
	public EstSalAFav(){
		super();
		// Seteo de valores default			
	}
	
	public EstSalAFav(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstSalAFav getById(Long id) {
		return (EstSalAFav) BalDAOFactory.getEstSalAFavDAO().getById(id);
	}
	
	public static EstSalAFav getByIdNull(Long id) {
		return (EstSalAFav) BalDAOFactory.getEstSalAFavDAO().getByIdNull(id);
	}
	
	public static List<EstSalAFav> getList() {
		return (List<EstSalAFav>) BalDAOFactory.getEstSalAFavDAO().getList();
	}
	
	public static List<EstSalAFav> getListActivos() {			
		return (List<EstSalAFav>) BalDAOFactory.getEstSalAFavDAO().getListActiva();
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
		
		if (StringUtil.isNullOrEmpty(getDesEstSalAFav())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.ESTSALAFAV_DESESTSALAFAV);
		}
		
		if (hasError()) {
			return false;
		}
		
	
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstSalAFav. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getEstSalAFavDAO().update(this);
	}

	/**
	 * Desactiva el EstSalAFav. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getEstSalAFavDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstSalAFav
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstSalAFav
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public String getDesEstSalAFav() {
		return desEstSalAFav;
	}

	public void setDesEstSalAFav(String desEstSalAFav) {
		this.desEstSalAFav = desEstSalAFav;
	}
	
	//<#MetodosBeanDetalle#>
}
