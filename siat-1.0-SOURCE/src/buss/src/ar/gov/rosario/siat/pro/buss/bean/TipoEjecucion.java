//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a TipoEjecucion
 * Representa los tipos de ejecucion.
 * 
 * @author tecso
 */

@Entity
@Table(name = "pro_tipoEjecucion")
public class TipoEjecucion extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoEjecucion";

	@Column(name = "desTipoEjecucion")
	private String desTipoEjecucion;
	
	// Constructores
	public TipoEjecucion(){
		super();
	}
	
	// Getters y Setters
	public String getDesTipoEjecucion(){
		return desTipoEjecucion;
	}
	public void setDesTipoEjecucion(String desTipoEjecucion){
		this.desTipoEjecucion = desTipoEjecucion;
	}
	
	// Metodos de Clase
	public static TipoEjecucion getById(Long id) {
		return (TipoEjecucion) ProDAOFactory.getTipoEjecucionDAO().getById(id);  
	}
	
	public static TipoEjecucion getByIdNull(Long id) {
		return (TipoEjecucion) ProDAOFactory.getTipoEjecucionDAO().getByIdNull(id);
	}
	
	public static List<TipoEjecucion> getList() {
		return (List<TipoEjecucion>) ProDAOFactory.getTipoEjecucionDAO().getList();
	}
	
	public static List<TipoEjecucion> getListActivos() {			
		return (List<TipoEjecucion>) ProDAOFactory.getTipoEjecucionDAO().getListActiva();
	}


	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		
		if (hasError()) {
			return false;
		}

				
		return !hasError();
	}
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
	
		if (hasError()) {
			return false;
		}

			
		return !hasError();
	}

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		
		if (StringUtil.isNullOrEmpty(getDesTipoEjecucion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.TIPOEJECUCION_DESTIPOEJECUCION);
		}
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		
		
		return !hasError();
	}

	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio
	

	

}
