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

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean corresponiente al Tipo de Entradas para habilitar en los distintos Eventos o Espectaculos.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_tipoEntrada")
public class TipoEntrada extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "codigo")
	private String codigo;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	//Constructores 
	public TipoEntrada(){
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
	public static TipoEntrada getById(Long id) {
		return (TipoEntrada) EspDAOFactory.getTipoEntradaDAO().getById(id);
	}
	
	public static TipoEntrada getByIdNull(Long id) {
		return (TipoEntrada) EspDAOFactory.getTipoEntradaDAO().getByIdNull(id);
	}
	
	public static TipoEntrada getByCodigo(String codigo) {
		return (TipoEntrada) EspDAOFactory.getTipoEntradaDAO().getByCodigo(codigo);
	}
	
	public static List<TipoEntrada> getList() {
		return (ArrayList<TipoEntrada>) EspDAOFactory.getTipoEntradaDAO().getList();
	}
	
	public static List<TipoEntrada> getListActivos() {			
		return (ArrayList<TipoEntrada>) EspDAOFactory.getTipoEntradaDAO().getListActiva();
	}
	
	public static List<TipoEntrada> getListActivaOrdenada() {			
		return (ArrayList<TipoEntrada>) EspDAOFactory.getTipoEntradaDAO().getListActivaOrdenada();
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOENTRADA_CODIGO);
		}

		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPOENTRADA_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Unicidad
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codigo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, EspError.TIPOENTRADA_CODIGO);			
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
		
		if (GenericDAO.hasReference(this, PrecioEvento.class, "tipoEntrada")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			EspError.TIPOENTRADA_LABEL , EspError.PRECIOEVENTO_LABEL);
		}
		if (GenericDAO.hasReference(this, EntHab.class, "tipoEntrada")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.TIPOENTRADA_LABEL , EspError.ENTHAB_LABEL);
		}
		if (GenericDAO.hasReference(this, EntVen.class, "tipoEntrada")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.TIPOENTRADA_LABEL , EspError.ENTVEN_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
