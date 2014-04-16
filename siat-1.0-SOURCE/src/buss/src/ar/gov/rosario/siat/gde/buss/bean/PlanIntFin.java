//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanIntFin
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planIntFin")
public class PlanIntFin extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "cuotaHasta")
	private Integer cuotaHasta;
	
	@Column(name = "interes")
	private Double interes;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public PlanIntFin(){
		super();
	}
	
	public PlanIntFin(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanIntFin getById(Long id) {
		return (PlanIntFin) GdeDAOFactory.getPlanIntFinDAO().getById(id);
	}
	
	public static PlanIntFin getByIdNull(Long id) {
		return (PlanIntFin) GdeDAOFactory.getPlanIntFinDAO().getByIdNull(id);
	}
	
	public static List<PlanIntFin> getList() {
		return (List<PlanIntFin>) GdeDAOFactory.getPlanIntFinDAO().getList();
	}
	
	public static List<PlanIntFin> getListActivos() {			
		return (List<PlanIntFin>) GdeDAOFactory.getPlanIntFinDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Integer getCuotaHasta() {
		return cuotaHasta;
	}

	public void setCuotaHasta(Integer cuotaHasta) {
		this.cuotaHasta = cuotaHasta;
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

	public Double getInteres() {
		return interes;
	}

	public void setInteres(Double interes) {
		this.interes = interes;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
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
	
		// Convenio
		if (GenericDAO.hasReference(this, Convenio.class, "planIntFin")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLANINTFIN_LABEL, GdeError.CONVENIO_LABEL );
		}
		if (getPlan().getEsManual()!=1 && getPlan().getListPlanIntFin().size()==1 && getPlan().getEstado()==1 && !hasError()){
			addRecoverableError(GdeError.PLANINTFIN_UNICO);
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
		
		if (getCuotaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANINTFIN_CUOTAHASTA);
		}
		
		if (getInteres() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANINTFIN_INTERES);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANINTFIN_FECHADESDE);
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANINTFIN_FECHAHASTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validacion del formato de porcentaje
		if (getInteres().doubleValue() < 0 || getInteres().doubleValue() > 1){
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLANINTFIN_INTERES);
		}
		
		if (hasError()) {
			return false;
		}
		
		if ( getCuotaHasta() != null && getPlan().getCanMaxCuo() != null) {
			if (getPlan().getCanMaxCuo().intValue() < getCuotaHasta().intValue())
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANINTFIN_CUOTAHASTA, 
						GdeError.PLAN_CANMAXCUO_REF );			
		}
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANINTFIN_FECHADESDE, GdeError.PLANINTFIN_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANINTFIN_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanIntFinDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanIntFin. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanIntFinDAO().update(this);
	}

	/**
	 * Desactiva el PlanIntFin. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanIntFinDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanIntFin
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanIntFin
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
