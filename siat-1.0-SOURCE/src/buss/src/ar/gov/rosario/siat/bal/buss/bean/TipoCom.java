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
 * Bean correspondiente a TipoCom
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoCom")
public class TipoCom extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_POR_SALDO_A_FAVOR = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;

	
	// Constructores
	public TipoCom(){
		super();
	}
	
	public TipoCom(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static TipoCom getById(Long id) {
		return (TipoCom) BalDAOFactory.getTipoComDAO().getById(id);
	}
	
	public static TipoCom getByIdNull(Long id) {
		return (TipoCom) BalDAOFactory.getTipoComDAO().getByIdNull(id);
	}
	
	public static List<TipoCom> getList() {
		return (ArrayList<TipoCom>) BalDAOFactory.getTipoComDAO().getList();
	}
	
	public static List<TipoCom> getListActivos() {			
		return (ArrayList<TipoCom>) BalDAOFactory.getTipoComDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							${Modulo}Error.${BEAN}_LABEL, ${Modulo}Error. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPOCOM_DESCRIPCION);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el TipoCom. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getTipoComDAO().update(this);
	}

	/**
	 * Desactiva el TipoCom. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getTipoComDAO().update(this);
	}
	
	/**
	 * Valida la activacion del TipoCom
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del TipoCom
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
