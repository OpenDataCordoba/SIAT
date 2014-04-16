//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

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
 * Bean correspondiente a GenCodGes
 * Indica la forma en que se pueden generar los codigos de gestion personal.
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_genCodGes")
public class GenCodGes extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "genCodGes";

	public static final Long MANUAL = 1L;
	public static final Long INTERFACE = 2L;
	public static final Long COPIA_NRO_PRINCIPAL = 3L;
	public static final Long AUTONUMERIO = 4L;
	public static final Long CAMPO_COD_CONTROL = 5L;
		
	@Column(name = "codGenCodGes")
	private String codGenCodGes;
	
	@Column(name = "desGenCodGes")
	private String desGenCodGes;
	
	// Constructores
	public GenCodGes(){
		super();
	}
	
	// Getters y Setters
	public String getCodGenCodGes(){
		return codGenCodGes;
	}
	public void setCodGenCodGes(String codGenCodGes){
		this.codGenCodGes = codGenCodGes;
	}
	public String getDesGenCodGes(){
		return desGenCodGes;
	}
	public void setDesGenCodGes(String desGenCodGes){
		this.desGenCodGes = desGenCodGes;
	}
	
	// Metodos de Clase
	public static GenCodGes getById(Long id) {
		return (GenCodGes) DefDAOFactory.getGenCodGesDAO().getById(id);  
	}
	
	public static GenCodGes getByIdNull(Long id) {
		return (GenCodGes) DefDAOFactory.getGenCodGesDAO().getByIdNull(id);
	}
	
	public static List<GenCodGes> getList() {
		return (List<GenCodGes>) DefDAOFactory.getGenCodGesDAO().getList();
	}
	
	public static List<GenCodGes> getListActivos() {			
		return (List<GenCodGes>) DefDAOFactory.getGenCodGesDAO().getListActiva();
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
		
		if (StringUtil.isNullOrEmpty(getCodGenCodGes())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.GENCODGES_CODGENCODGES);
		}
		if (StringUtil.isNullOrEmpty(getDesGenCodGes())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.GENCODGES_DESGENCODGES);
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
