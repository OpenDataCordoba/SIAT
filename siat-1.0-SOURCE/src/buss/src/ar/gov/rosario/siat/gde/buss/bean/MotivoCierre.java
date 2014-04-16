//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a MotivoCierre - 
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_motivoCierre")
public class MotivoCierre extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final long ID_FALLECIMIENTO_TITULAR = 1L;
	
	@Column(name = "desMotivo")
	private String desMotivo;

	//<#Propiedades#>
	
	// Constructores
	public MotivoCierre(){
		super();
		// Seteo de valores default			
	}
	
	public MotivoCierre(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MotivoCierre getById(Long id) {
		return (MotivoCierre) GdeDAOFactory.getMotivoCierreDAO().getById(id);
	}
	
	public static MotivoCierre getByIdNull(Long id) {
		return (MotivoCierre) GdeDAOFactory.getMotivoCierreDAO().getByIdNull(id);
	}
	
	// Getters y setters
	public String getDesMotivo() {
		return desMotivo;
	}

	public void setDesMotivo(String desMotivo) {
		this.desMotivo = desMotivo;
	}
	
	public static List<MotivoCierre> getList() {
		return (ArrayList<MotivoCierre>) GdeDAOFactory.getMotivoCierreDAO().getList();
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
		
		//	Validaciones        
		
		if (StringUtil.isNullOrEmpty(getDesMotivo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.MOTIVOCIERRE_DESMOTIVOCIERRE);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el MotivoCierre. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getMotivoCierreDAO().update(this);
	}

	/**
	 * Desactiva el MotivoCierre. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getMotivoCierreDAO().update(this);
	}
	
	/**
	 * Valida la activacion del MotivoCierre
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del MotivoCierre
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
