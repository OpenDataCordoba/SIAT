//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Vencimiento;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanVen
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planVen")
public class PlanVen extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idVencimiento") 
	private Vencimiento vencimiento;
	
	@Column(name = "cuotaHasta")
	private Integer cuotaHasta;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public PlanVen(){
		super();
		// Seteo de valores default			
	}
	
	public PlanVen(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanVen getById(Long id) {
		return (PlanVen) GdeDAOFactory.getPlanVenDAO().getById(id);
	}
	
	public static PlanVen getByIdNull(Long id) {
		return (PlanVen) GdeDAOFactory.getPlanVenDAO().getByIdNull(id);
	}
	
	public static List<PlanVen> getList() {
		return (List<PlanVen>) GdeDAOFactory.getPlanVenDAO().getList();
	}
	
	public static List<PlanVen> getListActivos() {			
		return (List<PlanVen>) GdeDAOFactory.getPlanVenDAO().getListActiva();
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

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Vencimiento getVencimiento() {
		return vencimiento;
	}

	public void setVencimiento(Vencimiento vencimiento) {
		this.vencimiento = vencimiento;
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
		if (getPlan().getEsManual()!=1 && getPlan().getListPlanVen().size()==1 && getPlan().getEstado()==1){
			addRecoverableError(GdeError.PLANVEN_UNICO);
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
		
		if (getVencimiento() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.VENCIMIENTO_LABEL);
		}
		
		if (getCuotaHasta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANVEN_CUOTAHASTA);
		}else if(getCuotaHasta()<0)
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLANVEN_CUOTAHASTA);
		
		if (getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANVEN_FECHADESDE);
		}
		
		if (getFechaHasta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANVEN_FECHAHASTA);
		}
		
        // valida el rango de fechas ingresado
        if(getFechaDesde()!=null && getFechaHasta()!=null && DateUtil.isDateAfter(getFechaDesde(), getFechaHasta()))
        	addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANINTFIN_FECHADESDE, GdeError.PLANINTFIN_FECHAHASTA);

		if (getPlan().getEstado()==1 && getPlan().getEsManual()!=1){
			Integer cantMaxCuoPlanVen=getCuotaHasta();
			for (PlanVen planVen: getPlan().getListPlanVen()){
				if (planVen.getCuotaHasta()>cantMaxCuoPlanVen && planVen.getId()!=getId()){
					cantMaxCuoPlanVen = planVen.getCuotaHasta();
				}
			}
			if (getPlan().getCanMaxCuo() > cantMaxCuoPlanVen){
				addRecoverableError(GdeError.PLAN_LISTPLANVENCUOMAXMOD);
			}
		}
		
		if (hasError()) {
			return false;
		}
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANVEN_FECHADESDE, GdeError.PLANVEN_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANVEN_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanVenDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanVen. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanVenDAO().update(this);
	}

	/**
	 * Desactiva el PlanVen. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanVenDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanVen
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanVen
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
