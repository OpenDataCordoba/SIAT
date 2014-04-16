//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a HisEstObra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_hisEstadoObra")
public class HisEstadoObra extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne()
	@JoinColumn(name="idEstadoObra")
	private EstadoObra estadoObra; 

	@ManyToOne()
	@JoinColumn(name="idObra")
	private Obra obra;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "fechaEstado")
	private Date fechaEstado;

	// Constructores
	public HisEstadoObra(){
		super();
	}
	
	public HisEstadoObra(EstadoObra estadoObra, Obra obra, String descripcion){
		super();
		this.setEstadoObra(estadoObra);
		this.setObra(obra);
		this.setDescripcion(descripcion);
		this.setFechaEstado(new Date());
	}

	// Metodos de Clase
	public static HisEstadoObra getById(Long id) {
		return (HisEstadoObra) RecDAOFactory.getHisEstadoObraDAO().getById(id);
	}
	
	public static HisEstadoObra getByIdNull(Long id) {
		return (HisEstadoObra) RecDAOFactory.getHisEstadoObraDAO().getByIdNull(id);
	}
	
	public static List<HisEstadoObra> getList() {
		return (List<HisEstadoObra>) RecDAOFactory.getHisEstadoObraDAO().getList();
	}
	
	public static List<HisEstadoObra> getListActivos() {			
		return (List<HisEstadoObra>) RecDAOFactory.getHisEstadoObraDAO().getListActiva();
	}

	// Getters y setters
	public EstadoObra getEstadoObra() {
		return estadoObra;
	}

	public void setEstadoObra(EstadoObra estadoObra) {
		this.estadoObra = estadoObra;
	}

	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Date getFechaEstado() {
		return fechaEstado;
	}

	public void setFechaEstado(Date fechaEstado) {
		this.fechaEstado = fechaEstado;
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
							RecError.${BEAN}_LABEL, RecError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_LABEL);
		}

		if (getEstadoObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ESTADOOBRA_LABEL);
		}

		if (getFechaEstado() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.HISESTADOOBRA_FECHAESTADO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el HisEstadoObra. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getHisEstadoObraDAO().update(this);
	}

	/**
	 * Desactiva el HisEstadoObra. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getHisEstadoObraDAO().update(this);
	}
	
	/**
	 * Valida la activacion del HisEstadoObra
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del HisEstadoObra
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
