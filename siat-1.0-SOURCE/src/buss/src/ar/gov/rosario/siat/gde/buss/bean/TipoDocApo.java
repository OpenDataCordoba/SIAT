//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoDocApo
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipoDocApo")
public class TipoDocApo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desTipoDocApo")
	private String desTipoDocApo;

	//<#Propiedades#>
	
	// Constructores
	public TipoDocApo(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public TipoDocApo(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoDocApo getById(Long id) {
		return (TipoDocApo) GdeDAOFactory.getTipoDocApoDAO().getById(id);
	}
	
	public static TipoDocApo getByIdNull(Long id) {
		return (TipoDocApo) GdeDAOFactory.getTipoDocApoDAO().getByIdNull(id);
	}
	
	public static List<TipoDocApo> getList() {
		return (List<TipoDocApo>) GdeDAOFactory.getTipoDocApoDAO().getList();
	}
	
	public static List<TipoDocApo> getListActivos() {			
		return (List<TipoDocApo>) GdeDAOFactory.getTipoDocApoDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoDocApo() {
		return desTipoDocApo;
	}

	public void setDesTipoDocApo(String desTipoDocApo) {
		this.desTipoDocApo = desTipoDocApo;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesTipoDocApo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPODOCAPO_DESTIPODOCAPO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codTipoDocApo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.TIPODOCAPO_CODTIPODOCAPO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoDocApo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipoDocApoDAO().update(this);
	}

	/**
	 * Desactiva el TipoDocApo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipoDocApoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoDocApo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoDocApo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
