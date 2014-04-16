//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import ar.gov.rosario.siat.ef.iface.util.EfError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a InsSup
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_inssup")
public class InsSup extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
@ManyToOne(fetch=FetchType.LAZY) 
@JoinColumn(name="idInspector") 
private Inspector inspector;


@ManyToOne(fetch=FetchType.LAZY) 
@JoinColumn(name="idSupervisor") 
private Supervisor supervisor;

@Column(name = "fechaDesde")
private Date fechaDesde;

@Column(name = "fechaHasta")
private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public InsSup(){
		super();
		// Seteo de valores default			
	}
	
	public InsSup(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static InsSup getById(Long id) {
		return (InsSup) EfDAOFactory.getInsSupDAO().getById(id);
	}
	
	public static InsSup getByIdNull(Long id) {
		return (InsSup) EfDAOFactory.getInsSupDAO().getByIdNull(id);
	}
	
	public static List<InsSup> getList() {
		return (ArrayList<InsSup>) EfDAOFactory.getInsSupDAO().getList();
	}
	
	public static List<InsSup> getListActivos() {			
		return (ArrayList<InsSup>) EfDAOFactory.getInsSupDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
		
		//	Validaciones 
		if (this.getSupervisor()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.INSSUP_SUPERVISOR );
		}
		
		if(fechaDesde==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EfError.SUPERVISOR_FECHADESDE_LABEL);
		}
		
		if(fechaDesde!=null && fechaHasta!=null && DateUtil.isDateBefore(fechaHasta, fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, EfError.SUPERVISOR_FECHADESDE_LABEL, 
					EfError.SUPERVISOR_FECHAHASTA_LABEL);
		}
		
		if(!hasError()){
			if(EfDAOFactory.getInsSupDAO().validateAcoplamientoInsSup(this)){
				addRecoverableError(EfError.MSG_SOLAPAMIENTO_PERIODO);
			}
		}

		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el InsSup. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getInsSupDAO().update(this);
	}

	/**
	 * Desactiva el InsSup. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getInsSupDAO().update(this);
	}
	
	/**
	 * Valida la activacion del InsSup
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del InsSup
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}


	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Inspector getInspector() {
		return inspector;
	}

	public void setInspector(Inspector inspector) {
		this.inspector = inspector;
	}

	public Supervisor getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(Supervisor supervisor) {
		this.supervisor = supervisor;
	}
	
	
}
