//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean corresponiente al Tipo de Habilitacion. (INTERNO o EXTERNO)
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_tipoHab")
public class TipoHab extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String COD_INTERNA = "INT";
	public static final String COD_EXTERNA = "EXT";

	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	//Constructores 
	public TipoHab(){
		super();
	}

	// Getters Y Setters 
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	// Metodos de clase	
	public static TipoHab getById(Long id) {
		return (TipoHab) EspDAOFactory.getTipoHabDAO().getById(id);
	}
	
	public static TipoHab getByIdNull(Long id) {
		return (TipoHab) EspDAOFactory.getTipoHabDAO().getByIdNull(id);
	}
	
	public static List<TipoHab> getList() {
		return (ArrayList<TipoHab>) EspDAOFactory.getTipoHabDAO().getList();
	}
	
	public static List<TipoHab> getListActivos() {			
		return (ArrayList<TipoHab>) EspDAOFactory.getTipoHabDAO().getListActiva();
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(StringUtil.isNullOrEmpty(getCodigo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOHAB_CODIGO);
		}

		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOHAB_DESCRIPCION);
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
		
		/*if (GenericDAO.hasReference(this, TipoHabPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			EspError.DISPAR_LABEL , EspError.DISPARPLA_LABEL);
		}*/
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
