//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanRecurso
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planRecurso")
public class PlanRecurso extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(optional=false)
	@JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	


	//<#Propiedades#>
	
	// Constructores
	public PlanRecurso(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public PlanRecurso(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanRecurso getById(Long id) {
		return (PlanRecurso) GdeDAOFactory.getPlanRecursoDAO().getById(id);
	}
	
	
	
	// Getters y setters
	
	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
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
		
		if (DateUtil.isDateBefore(GdeDAOFactory.getPlanRecursoDAO().getMinFechaFormalizacion(this),getFechaDesde())){
			addRecoverableError(GdeError.PLANRECURSO_FECHADESDEMAYOR);
		}
		
		if (getFechaHasta() !=null && DateUtil.isDateAfter(GdeDAOFactory.getPlanRecursoDAO().getMaxFechaFormalizacion(this),getFechaHasta())){
			addRecoverableError(GdeError.PLANRECURSO_FECHAHASTAMENOR);
		}
		
		// Validaciones de negocio
		if (!this.validate() || this.hasError()) {
			return false;
		}


		
		return true;		
	}

	public boolean validateDelete() throws Exception{
		//limpiamos la lista de errores
		clearError();
	
		// Convenio
		if (GdeDAOFactory.getPlanRecursoDAO().estaReferenciado(this)) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLANRECURSO_LABEL, GdeError.CONVENIO_LABEL );
		}
		if (getPlan().getListPlanRecurso().size()==1 && getPlan().getEstado()==1 && !hasError()){
			addRecoverableError(GdeError.PLANRECURSO_UNICO);
		}
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getPlan() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
		}
		
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANDESCUENTO_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}
		
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANDESCUENTO_FECHADESDE, GdeError.PLANDESCUENTO_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANDESCUENTO_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if(GdeDAOFactory.getPlanRecursoDAO().esRecursoDuplicado(this)){
			addRecoverableError(GdeError.PLANRECURSO_DUPLICADO);
		}
		
		if (hasError()) {
			return false;
		}
		

		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanDescuento. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanDescuentoDAO().update(this);
	}

	/**
	 * Desactiva el PlanDescuento. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanDescuentoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanDescuento
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanDescuento
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Implementacion del metodo de la superclase
	 * @return
	 */
	public Date getFechaInicioVig() {
		return getFechaDesde();
	}

	/**
	 * Implementacion del metodo de la superclase
	 * @return
	 */
	public Date getFechaFinVig() {
		return getFechaHasta();
	}
	



}
