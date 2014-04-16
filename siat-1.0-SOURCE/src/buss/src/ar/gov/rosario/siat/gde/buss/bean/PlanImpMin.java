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
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanImpMin
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planImpMin")
public class PlanImpMin extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "cantidadCuotas")
	private Integer cantidadCuotas;
	
	@Column(name = "impMinCuo")
	private Double impMinCuo;
	
	@Column(name = "impMinDeu")
	private Double impMinDeu;

	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	// Constructores
	public PlanImpMin(){
		super();
		// Seteo de valores default			
	}
	
	public PlanImpMin(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanImpMin getById(Long id) {
		return (PlanImpMin) GdeDAOFactory.getPlanImpMinDAO().getById(id);
	}
	
	public static PlanImpMin getByIdNull(Long id) {
		return (PlanImpMin) GdeDAOFactory.getPlanImpMinDAO().getByIdNull(id);
	}
	
	public static List<PlanImpMin> getList() {
		return (List<PlanImpMin>) GdeDAOFactory.getPlanImpMinDAO().getList();
	}
	
	public static List<PlanImpMin> getListActivos() {			
		return (List<PlanImpMin>) GdeDAOFactory.getPlanImpMinDAO().getListActiva();
	}
		
	// Getters y setters
	public Integer getCantidadCuotas() {
		return cantidadCuotas;
	}

	public void setCantidadCuotas(Integer cantidadCuotas) {
		this.cantidadCuotas = cantidadCuotas;
	}

	public Double getImpMinCuo() {
		return impMinCuo;
	}

	public void setImpMinCuo(Double impMinCuo) {
		this.impMinCuo = impMinCuo;
	}

	public Double getImpMinDeu() {
		return impMinDeu;
	}

	public void setImpMinDeu(Double impMinDeu) {
		this.impMinDeu = impMinDeu;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
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

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		//<#ValidateDelete#>
		if (getPlan().getListPlanImpMin().size()==1 && getPlan().getEsManual()!=1 && getPlan().getEstado()==1){
			addRecoverableError(GdeError.PLANIMPMIN_UNICOPLANIMPMIN);
		}
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//		Validaciones        
		if (getPlan() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
		}
		
		if (getCantidadCuotas() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANIMPMIN_CANTIDADCUOTAS);
		}else if(getCantidadCuotas()<0)
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLANIMPMIN_CANTIDADCUOTAS);
		
		if (getImpMinDeu() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANIMPMIN_IMPMINDEU);
		}else if(getImpMinDeu()<0)
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLANIMPMIN_IMPMINDEU);
		
		if (getImpMinCuo() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANIMPMIN_IMPMINCUO);
		}else if(getImpMinCuo()<0)
			addRecoverableError(BaseError.MSG_RANGO_INVALIDO, GdeError.PLANIMPMIN_IMPMINCUO);
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANINTFIN_FECHADESDE);
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANINTFIN_FECHAHASTA);
		}
		
        // valida el rango de fechas ingresado
        if(getFechaDesde()!=null && getFechaHasta()!=null && DateUtil.isDateAfter(getFechaDesde(), getFechaHasta()))
        	addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANINTFIN_FECHADESDE, GdeError.PLANINTFIN_FECHAHASTA);

		if (hasError()) {
			return false;
		}
		
		if ( getCantidadCuotas() != null && getPlan().getCanMaxCuo() != null) {
			if (getPlan().getCanMaxCuo().intValue() < getCantidadCuotas().intValue())
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANIMPMIN_CANTIDADCUOTAS, 
						GdeError.PLAN_CANMAXCUO_REF );
		}
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANIMPMIN_FECHADESDE, GdeError.PLANIMPMIN_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANIMPMIN_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanImpMinDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanImpMin. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanImpMinDAO().update(this);
	}

	/**
	 * Desactiva el PlanImpMin. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanImpMinDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanImpMin
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanImpMin
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
