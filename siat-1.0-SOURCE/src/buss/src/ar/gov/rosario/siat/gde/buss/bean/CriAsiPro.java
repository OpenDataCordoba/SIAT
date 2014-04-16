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

/**
 * Bean correspondiente a CriAsiPro
 * Criterios de Asignacion a Procuradores
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_criAsiPro")
public class CriAsiPro extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "criAsiPro";
	
	public static final long ID_CATASTRAL            = 1L;
	public static final long ID_ALFABETICO_DOMICILIO = 2L;
	public static final long ID_ALFABETICO_TITULAR   = 3L;
	
	@Column(name = "desCriAsiPro")
	private String desCriAsiPro;
	
	// Constructores
	public CriAsiPro(){
		super();
	}
	
	// Getters y Setters
	public String getDesCriAsiPro(){
		return desCriAsiPro;
	}
	public void setDesCriAsiPro(String desCriAsiPro){
		this.desCriAsiPro = desCriAsiPro;
	}
	
	// Metodos de Clase
	public static CriAsiPro getById(Long id) {
		return (CriAsiPro) GdeDAOFactory.getCriAsiProDAO().getById(id);  
	}
	
	public static CriAsiPro getByIdNull(Long id) {
		return (CriAsiPro) GdeDAOFactory.getCriAsiProDAO().getByIdNull(id);
	}
	
	public static List<CriAsiPro> getList() {
		return (ArrayList<CriAsiPro>) GdeDAOFactory.getCriAsiProDAO().getList();
	}
	
	public static List<CriAsiPro> getListActivos() {			
		return (ArrayList<CriAsiPro>) GdeDAOFactory.getCriAsiProDAO().getListActiva();
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
		
		if (StringUtil.isNullOrEmpty(getDesCriAsiPro())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.CRIASIPRO_DESCRIASIPRO);
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
