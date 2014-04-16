//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.buss.bean;

import java.util.ArrayList;
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
 * Bean correspondiente a HisEstPlaCua
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_hisEstPlaCua")
public class HisEstPlaCua extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne()
	@JoinColumn(name="idEstPlaCua")
	private EstPlaCua estPlaCua; 

	@ManyToOne()
	@JoinColumn(name="idPlanillaCuadra")
	private PlanillaCuadra planillaCuadra;

	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "fechaEstado")
	private Date fechaEstado;

	// Constructores
	public HisEstPlaCua(){
		super();
	}
	
	public HisEstPlaCua(EstPlaCua estPlaCua, PlanillaCuadra planillaCuadra, String descripcion){
		super();
		this.setEstPlaCua(estPlaCua);
		this.setPlanillaCuadra(planillaCuadra);
		this.setDescripcion(descripcion);
		this.setFechaEstado(new Date());
	}

	// Metodos de Clase
	public static HisEstPlaCua getById(Long id) {
		return (HisEstPlaCua) RecDAOFactory.getHisEstPlaCuaDAO().getById(id);
	}
	
	public static HisEstPlaCua getByIdNull(Long id) {
		return (HisEstPlaCua) RecDAOFactory.getHisEstPlaCuaDAO().getByIdNull(id);
	}
	
	public static List<HisEstPlaCua> getList() {
		return (ArrayList<HisEstPlaCua>) RecDAOFactory.getHisEstPlaCuaDAO().getList();
	}
	
	public static List<HisEstPlaCua> getListActivos() {			
		return (ArrayList<HisEstPlaCua>) RecDAOFactory.getHisEstPlaCuaDAO().getListActiva();
	}
	
	public static HisEstPlaCua getLastHisEstPlaCua(Long idPlanillaCuadra) {
		return (HisEstPlaCua) RecDAOFactory.getHisEstPlaCuaDAO().getLastHisEstPlaCua(idPlanillaCuadra);
	}
	
	// Getters y setters
	public EstPlaCua getEstPlaCua() {
		return estPlaCua;
	}

	public void setEstPlaCua(EstPlaCua estPlaCua) {
		this.estPlaCua = estPlaCua;
	}

	public PlanillaCuadra getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadra planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getPlanillaCuadra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.PLANILLACUADRA_LABEL);
		}

		if (getEstPlaCua() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.ESTPLACUA_LABEL);
		}

		if (getFechaEstado() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.HISESTPLACUA_FECHAESTADO);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el HisEstPlaCua. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RecDAOFactory.getHisEstPlaCuaDAO().update(this);
	}

	/**
	 * Desactiva el HisEstPlaCua. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RecDAOFactory.getHisEstPlaCuaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del HisEstPlaCua
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del HisEstPlaCua
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
