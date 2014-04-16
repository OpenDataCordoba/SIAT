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
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanMotCad
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planMotCad")
public class PlanMotCad extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "desPlanMotCad")
	private String desPlanMotCad;
	
	@Column(name = "esEspecial")
	private Integer esEspecial;
	
	@Column(name = "cantCuoCon")
	private Integer cantCuoCon;
	
	@Column(name = "cantCuoAlt")
	private Integer cantCuoAlt;
	
	@Column(name = "cantDias")
	private Integer cantDias;
	
	@Column(name = "className")
	private String className;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	

	//<#Propiedades#>
	
	// Constructores
	public PlanMotCad(){
		super();
		// Seteo de valores default			
	}
	
	public PlanMotCad(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanMotCad getById(Long id) {
		return (PlanMotCad) GdeDAOFactory.getPlanMotCadDAO().getById(id);
	}
	
	public static PlanMotCad getByIdNull(Long id) {
		return (PlanMotCad) GdeDAOFactory.getPlanMotCadDAO().getByIdNull(id);
	}
	
	public static List<PlanMotCad> getList() {
		return (List<PlanMotCad>) GdeDAOFactory.getPlanMotCadDAO().getList();
	}
	
	public static List<PlanMotCad> getListActivos() {			
		return (List<PlanMotCad>) GdeDAOFactory.getPlanMotCadDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Integer getCantCuoAlt() {
		return cantCuoAlt;
	}

	public void setCantCuoAlt(Integer cantCuoAlt) {
		this.cantCuoAlt = cantCuoAlt;
	}

	public Integer getCantCuoCon() {
		return cantCuoCon;
	}

	public void setCantCuoCon(Integer cantCuoCon) {
		this.cantCuoCon = cantCuoCon;
	}

	public Integer getCantDias() {
		return cantDias;
	}

	public void setCantDias(Integer cantDias) {
		this.cantDias = cantDias;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDesPlanMotCad() {
		return desPlanMotCad;
	}

	public void setDesPlanMotCad(String desPlanMotCad) {
		this.desPlanMotCad = desPlanMotCad;
	}

	public Integer getEsEspecial() {
		return esEspecial;
	}

	public void setEsEspecial(Integer esEspecial) {
		this.esEspecial = esEspecial;
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
		if (getPlan().getEsManual()!=1 && getPlan().getListPlanMotCad().size()==1 && getPlan().getEstado()==1){
			addRecoverableError(GdeError.PLANMOTCAD_UNICOPLANMOTCAD);
		}
		//<#ValidateDelete#>
		
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
		
		if (StringUtil.isNullOrEmpty(getDesPlanMotCad())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANMOTCAD_DESPLANMOTCAD);
		}
		
		if (getEsEspecial() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANMOTCAD_ESESPECIAL);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANMOTCAD_FECHADESDE);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANMOTCAD_FECHAHASTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Si NO especial, al menos una de las cantidades es requerida.
		if ( getEsEspecial().intValue() == 0 && getCantCuoCon() == null && getCantCuoAlt() == null && getCantDias() == null){
			addRecoverableError(GdeError.PLANMOTCAD_UNACANTIDAD_REQUERIDA);
		}
		
		// Si es Especial, el nombre de la clase es requerida 
		if (getEsEspecial().intValue() == 1 && StringUtil.isNullOrEmpty(getClassName()) ){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANMOTCAD_CLASSNAME);
		}
				
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANMOTCAD_FECHADESDE, GdeError.PLANMOTCAD_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANMOTCAD_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
				
		if (hasError()) {
			return false;
		}
		
		// Si NO especial, ningunas de las dos cantidades de cuotas pueden ser mayores a la la cantidad maxima de cuotas del plan
		if ( getEsEspecial().intValue() == 0 && getCantCuoCon() != null && getPlan().getCanMaxCuo() != null) {
			if (getPlan().getCanMaxCuo().intValue() < getCantCuoCon().intValue())
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANMOTCAD_CANTCUOCON, GdeError.PLAN_CANMAXCUO_REF );		
		}
		
		if ( getEsEspecial().intValue() == 0 && getCantCuoAlt() != null && getPlan().getCanMaxCuo() != null){
			if (getPlan().getCanMaxCuo().intValue() < getCantCuoAlt().intValue())
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANMOTCAD_CANTCUOALT, GdeError.PLAN_CANMAXCUO_REF );			
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanMotCadDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanMotCad. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanMotCadDAO().update(this);
	}

	/**
	 * Desactiva el PlanMotCad. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanMotCadDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanMotCad
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanMotCad
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
