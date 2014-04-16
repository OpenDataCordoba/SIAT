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
 * Bean correspondiente a TipoPerFor
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_tipoPerFor")
public class TipoPerFor extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desTipoPerFor")
	private String desTipoPerFor;
	
	@Column(name = "esTitular")
	private Integer esTitular;

	//<#Propiedades#>
	
	// Constructores
	public TipoPerFor(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public TipoPerFor(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoPerFor getById(Long id) {
		return (TipoPerFor) GdeDAOFactory.getTipoPerForDAO().getById(id);
	}
	
	public static TipoPerFor getByIdNull(Long id) {
		return (TipoPerFor) GdeDAOFactory.getTipoPerForDAO().getByIdNull(id);
	}
	
	public static List<TipoPerFor> getList() {
		return (List<TipoPerFor>) GdeDAOFactory.getTipoPerForDAO().getList();
	}
	
	public static List<TipoPerFor> getListActivos() {			
		return (List<TipoPerFor>) GdeDAOFactory.getTipoPerForDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoPerFor() {
		return desTipoPerFor;
	}

	public void setDesTipoPerFor(String desTipoPerFor) {
		this.desTipoPerFor = desTipoPerFor;
	}

	public Integer getEsTitular() {
		return esTitular;
	}

	public void setEsTitular(Integer esTitular) {
		this.esTitular = esTitular;
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
		if (StringUtil.isNullOrEmpty(getDesTipoPerFor())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.TIPOPERFOR_DESTIPOPERFOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codTipoPerFor");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.TIPOPERFOR_CODTIPOPERFOR);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoPerFor. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getTipoPerForDAO().update(this);
	}

	/**
	 * Desactiva el TipoPerFor. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getTipoPerForDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoPerFor
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoPerFor
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
