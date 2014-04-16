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
import ar.gov.rosario.siat.pad.buss.bean.Repartidor;
import ar.gov.rosario.siat.pad.iface.util.PadError;
import ar.gov.rosario.siat.rec.buss.dao.RecDAOFactory;
import ar.gov.rosario.siat.rec.iface.util.RecError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente al Historico de
 * repartidores de la planilla cuadra
 * 
 * @author tecso
 */
@Entity
@Table(name = "cdm_hisObrRepPla")
public class HisObrRepPla extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne()
	@JoinColumn(name="idObra")
	private Obra obra; 

	@ManyToOne()
	@JoinColumn(name="idPlanillaCuadra")
	private PlanillaCuadra planillaCuadra;
	
	@ManyToOne()
	@JoinColumn(name="idRepartidor")
	private Repartidor repartidor;

	@Column(name = "fechaAsignacion")
	private Date fechaAsignacion;

	// Constructores
	public HisObrRepPla(){
		super();
	}
	
	public HisObrRepPla(PlanillaCuadra planillaCuadra, Repartidor repartidor){
		super();
		this.setPlanillaCuadra(planillaCuadra);
		this.setObra(planillaCuadra.getObra());
		this.setRepartidor(repartidor);
		this.setFechaAsignacion(new Date());
	}

	// Metodos de Clase
	public static HisObrRepPla getById(Long id) {
		return (HisObrRepPla) RecDAOFactory.getHisObrRepPlaDAO().getById(id);
	}
	
	public static HisObrRepPla getByIdNull(Long id) {
		return (HisObrRepPla) RecDAOFactory.getHisObrRepPlaDAO().getByIdNull(id);
	}
	
	public static List<HisObrRepPla> getList() {
		return (ArrayList<HisObrRepPla>) RecDAOFactory.getHisObrRepPlaDAO().getList();
	}
	
	public static List<HisObrRepPla> getListActivos() {			
		return (ArrayList<HisObrRepPla>) RecDAOFactory.getHisObrRepPlaDAO().getListActiva();
	}

	// Getters y setters
	public Obra getObra() {
		return obra;
	}

	public void setObra(Obra obra) {
		this.obra = obra;
	}

	public PlanillaCuadra getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadra planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
	}

	public Repartidor getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(Repartidor repartidor) {
		this.repartidor = repartidor;
	}

	public Date getFechaAsignacion() {
		return fechaAsignacion;
	}

	public void setFechaAsignacion(Date fechaAsignacion) {
		this.fechaAsignacion = fechaAsignacion;
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
		if (getPlanillaCuadra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.PLANILLACUADRA_LABEL);
		}

		if (getObra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.OBRA_LABEL);
		}
		
		if (getRepartidor() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, PadError.REPARTIDOR_LABEL);
		}

		if (getFechaAsignacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RecError.HISOBRREPPLA_FECHAASIGNACION);
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
