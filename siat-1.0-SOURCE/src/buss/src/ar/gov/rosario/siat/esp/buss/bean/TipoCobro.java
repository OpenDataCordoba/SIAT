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
 * Bean corresponiente al Tipo de Cobro del Evento (Online, Entradas troqueladas, etc)
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_tipoCobro")
public class TipoCobro extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	//Constructores 
	public TipoCobro(){
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
	public static TipoCobro getById(Long id) {
		return (TipoCobro) EspDAOFactory.getTipoCobroDAO().getById(id);
	}
	
	public static TipoCobro getByIdNull(Long id) {
		return (TipoCobro) EspDAOFactory.getTipoCobroDAO().getByIdNull(id);
	}
	
	public static List<TipoCobro> getList() {
		return (ArrayList<TipoCobro>) EspDAOFactory.getTipoCobroDAO().getList();
	}
	
	public static List<TipoCobro> getListActivos() {			
		return (ArrayList<TipoCobro>) EspDAOFactory.getTipoCobroDAO().getListActiva();
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOCOBRO_CODIGO);
		}

		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOCOBRO_DESCRIPCION);
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
		
		/*if (GenericDAO.hasReference(this, TipoCobroPla.class, "disPar")) {
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
