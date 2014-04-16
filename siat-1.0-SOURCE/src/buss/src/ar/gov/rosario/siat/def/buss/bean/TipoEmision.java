//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a TipoEmision: Representa 
 * los Tipos de Emision que admite un Recurso.
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_tipoEmision")
public class TipoEmision extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoEmision";
	
	public static final Long ID_INDIVIDUAL 	   = 1L;
	public static final Long ID_MASIVA 	  	   = 2L;
	public static final Long ID_CORREGIDA 	   = 3L;
	public static final Long ID_EXTRAORDINARIA = 4L;
	public static final Long ID_EXTERNA 	   = 5L;
	
	// A deprecar
	public static final String ID_TIPOEMISION = "idTipoEmision";
 	public static final Long ID_EMISIONCDM 	  = 100L;
	public static final Long ID_IMPRESIONCDM  = 101L;
	public static final Long ID_EMISIONCORCDM = 102L;

	
	@Column(name = "desTipoEmision")
	private String desTipoEmision;
	
	// Constructores
	public TipoEmision(){
		super();
	}
	
	// Getters y Setters
	public String getDesTipoEmision(){
		return desTipoEmision;
	}
	
	public void setDesTipoEmision(String desTipoEmision){
		this.desTipoEmision = desTipoEmision;
	}
	
	// Metodos de Clase
	public static TipoEmision getById(Long id) {
		return (TipoEmision) DefDAOFactory.getTipoEmisionDAO().getById(id);  
	}
	
	public static TipoEmision getByIdNull(Long id) {
		return (TipoEmision) DefDAOFactory.getTipoEmisionDAO().getByIdNull(id);
	}
	
	public static List<TipoEmision> getList() {
		return (ArrayList<TipoEmision>) DefDAOFactory.getTipoEmisionDAO().getList();
	}
	
	public static List<TipoEmision> getListActivos() {			
		return (ArrayList<TipoEmision>) DefDAOFactory.getTipoEmisionDAO().getListActiva();
	}

	// Metodos de Instancia
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
		
		// limpiamos la lista de errores
		clearError();
		
		// Validaciones de Requeridos
		if (StringUtil.isNullOrEmpty(getDesTipoEmision())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.TIPOEMISION_DESTIPOEMISION);
		}
		
		if (hasError()) {
			return false;
		}

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
}
