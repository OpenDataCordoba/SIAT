//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a TipoSellado
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoSellado")
public class TipoSellado extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final long ID_TIPO_MONTO_FIJO = 2L;

	public static final long ID_TIPO_PORCENTAJE = 1L;
	
	
	@Column(name = "desTipoSellado")
	private String desTipoSellado;

	// Constructores
	public TipoSellado(){
		super();
	}
	
	public TipoSellado(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoSellado getById(Long id) {
		return (TipoSellado) BalDAOFactory.getTipoSelladoDAO().getById(id);
	}
	
	public static TipoSellado getByIdNull(Long id) {
		return (TipoSellado) BalDAOFactory.getTipoSelladoDAO().getByIdNull(id);
	}
	
	public static List<TipoSellado> getList() {
		return (ArrayList<TipoSellado>) BalDAOFactory.getTipoSelladoDAO().getList();
	}
	
	public static List<TipoSellado> getListActivos() {			
		return (ArrayList<TipoSellado>) BalDAOFactory.getTipoSelladoDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesTipoSellado() {
		return desTipoSellado;
	}

	public void setDesTipoSellado(String desTipoSellado) {
		this.desTipoSellado = desTipoSellado;
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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		if (StringUtil.isNullOrEmpty(getDesTipoSellado())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOSELLADO_DESTIPOSELLADO);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoSellado. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getTipoSelladoDAO().update(this);
	}

	/**
	 * Desactiva el TipoSellado. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getTipoSelladoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoSellado
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoSellado
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
