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
 * Bean correspondiente a EstadoConCuo
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_estadoConCuo")
public class EstadoConCuo extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_IMPAGO = 1L;
	public static final Long ID_PAGO_BUENO = 2L;
	public static final Long ID_PAGO_A_CUENTA = 3L;
	
	@Column(name = "desEstadoConCuo")
	private String desEstadoConCuo;

	//<#Propiedades#>
	
	// Constructores
	public EstadoConCuo(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public EstadoConCuo(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoConCuo getById(Long id) {
		return (EstadoConCuo) GdeDAOFactory.getEstadoConCuoDAO().getById(id);
	}
	
	public static EstadoConCuo getByIdNull(Long id) {
		return (EstadoConCuo) GdeDAOFactory.getEstadoConCuoDAO().getByIdNull(id);
	}
	
	public static List<EstadoConCuo> getList() {
		return (List<EstadoConCuo>) GdeDAOFactory.getEstadoConCuoDAO().getList();
	}
	
	public static List<EstadoConCuo> getListActivos() {			
		return (List<EstadoConCuo>) GdeDAOFactory.getEstadoConCuoDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesEstadoConCuo() {
		return desEstadoConCuo;
	}

	public void setDesEstadoConCuo(String desEstadoConCuo) {
		this.desEstadoConCuo = desEstadoConCuo;
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
		if (StringUtil.isNullOrEmpty(getDesEstadoConCuo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADOCONCUO_DESESTADOCONCUO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEstadoConCuo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.ESTADOCONCUO_CODESTADOCONCUO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstadoConCuo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getEstadoConCuoDAO().update(this);
	}

	/**
	 * Desactiva el EstadoConCuo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getEstadoConCuoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoConCuo
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
