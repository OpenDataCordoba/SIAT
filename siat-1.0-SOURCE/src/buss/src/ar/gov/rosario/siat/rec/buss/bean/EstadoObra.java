//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a EstadoObra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_estadoObra")
public class EstadoObra extends BaseBO {
		
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_CREADA = 1L; 
	public static final Long ID_A_EMITIR = 2L;
	public static final Long ID_SUSPENDIDA = 3L;
	public static final Long ID_ACTIVA = 4L;
	public static final Long ID_ANULADA = 5L;
	public static final Long ID_CORREGIDA = 6L;
	public static final Long ID_REEMITIDA = 7L;
	public static final Long ID_NO_IMPRIMIR = 8L;
	
	@Column(name = "desEstadoObra")
	private String desEstadoObra;
	
	@Column(name = "transiciones")	
	private String transiciones; // las transiciones posibles del estado, separadas por como (1,2,...)
	
	// Constructores
	public EstadoObra(){
		super();
	}
	
	public EstadoObra(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static EstadoObra getById(Long id) {
		return (EstadoObra) RecDAOFactory.getEstadoObraDAO().getById(id);
	}
	
	public static List<EstadoObra> getList() {
		return (ArrayList<EstadoObra>) RecDAOFactory.getEstadoObraDAO().getList();
	}
	
	public static List<EstadoObra> getListActivos() {			
		return (ArrayList<EstadoObra>) RecDAOFactory.getEstadoObraDAO().getListActiva();
	}
	
	public static EstadoObra getByIdNull(Long id) {
		return (EstadoObra) RecDAOFactory.getEstadoObraDAO().getByIdNull(id);
	}
	
	// Getters y setters
	public String getDesEstadoObra() {
		return desEstadoObra;
	}

	public void setDesEstadoObra(String desEstadoObra) {
		this.desEstadoObra = desEstadoObra;
	}
	
	public String getTransiciones() {
		return transiciones;
	}

	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}

	// Validaciones 
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		/*Ejemplo:
		if (SiatHibernateUtil.hasReference(this, BeanRelacionado1.class, "estadoObra")) {
			addRecoverableError(RecError.ATRIBUTO_ BEANRELACIONADO1 _HASREF);
		}
		if (SiatHibernateUtil.hasReference(this, BeanRelacionado2.class, "estadoObra")) {
			addRecoverableError(RecError.ATRIBUTO_ BEANRELACIONADO2 _HASREF);
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/** Obtine una lista de todos
	 *  los estados a los cuales se puede dirigir
	 *  el estado actual
	 * 
	 */
	public List<EstadoObra> getListEstadoObraDestino() {
		List<EstadoObra> listEstadoObra = new ArrayList<EstadoObra>(); 

		String transiciones = this.getTransiciones();

		if ( !StringUtil.isNullOrEmpty(transiciones) ) {
			listEstadoObra = (ArrayList<EstadoObra>) RecDAOFactory.getEstadoObraDAO().getListActivaByIds(transiciones);
		}
		
		return listEstadoObra;

	}
	
	
	/**
	 * Activa el EstadoObra. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getEstadoObraDAO().update(this);
	}

	/**
	 * Desactiva el EstadoObra. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getEstadoObraDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoObra
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoObra
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
