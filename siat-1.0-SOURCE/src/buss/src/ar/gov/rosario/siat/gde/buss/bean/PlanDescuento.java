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
 * Bean correspondiente a PlanDescuento
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planDescuento")
public class PlanDescuento extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "cantidadCuotasPlan")
	private Integer cantidadCuotasPlan;
	
	@Column(name = "porDesCap")
	private Double porDesCap;
	
	@Column(name = "porDesAct")
	private Double porDesAct;
	
	@Column(name = "porDesInt")
	private Double porDesInt;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name= "aplTotImp")
	private Integer aplTotImp;


	//<#Propiedades#>
	
	// Constructores
	public PlanDescuento(){
		super();
		// Seteo de valores default	
		// propiedad_ejemplo = valorDefault;
	}
	
	public PlanDescuento(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanDescuento getById(Long id) {
		return (PlanDescuento) GdeDAOFactory.getPlanDescuentoDAO().getById(id);
	}
	
	public static PlanDescuento getByIdNull(Long id) {
		return (PlanDescuento) GdeDAOFactory.getPlanDescuentoDAO().getByIdNull(id);
	}
	
	public static List<PlanDescuento> getList() {
		return (List<PlanDescuento>) GdeDAOFactory.getPlanDescuentoDAO().getList();
	}
	
	public static List<PlanDescuento> getListActivos() {			
		return (List<PlanDescuento>) GdeDAOFactory.getPlanDescuentoDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Integer getCantidadCuotasPlan() {
		return cantidadCuotasPlan;
	}

	public void setCantidadCuotasPlan(Integer cantidadCuotasPlan) {
		this.cantidadCuotasPlan = cantidadCuotasPlan;
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

	public Double getPorDesAct() {
		return porDesAct;
	}

	public void setPorDesAct(Double porDesAct) {
		this.porDesAct = porDesAct;
	}

	public Double getPorDesCap() {
		return porDesCap;
	}

	public void setPorDesCap(Double porDesCap) {
		this.porDesCap = porDesCap;
	}

	public Double getPorDesInt() {
		return porDesInt;
	}

	public void setPorDesInt(Double porDesInt) {
		this.porDesInt = porDesInt;
	}

	public Integer getAplTotImp() {
		return aplTotImp;
	}

	public void setAplTotImp(Integer aplTotImp) {
		this.aplTotImp = aplTotImp;
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
		
		// Validaciones de negocio
		if (!this.validate()) {
			return false;
		}


		
		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		// Convenio
		if (GenericDAO.hasReference(this, Convenio.class, "planDescuento")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							GdeError.PLANDESCUENTO_LABEL, GdeError.CONVENIO_LABEL );
		}
		if (getPlan().getEsManual()!=1 && getPlan().getListPlanDescuento().size()==1 && getPlan().getEstado()==1 && !hasError()){
			addRecoverableError(GdeError.PLANDESCUENTO_UNICO);
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
		
		if (getCantidadCuotasPlan() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANDESCUENTO_CANTIDADCUOTASPLAN);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANDESCUENTO_FECHADESDE);
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANDESCUENTO_FECHAHASTA);
		}
		
		if ( getCantidadCuotasPlan() != null && getPlan().getCanMaxCuo() != null){
			if (getPlan().getCanMaxCuo().intValue() < getCantidadCuotasPlan().intValue())
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, GdeError.PLANDESCUENTO_CANTIDADCUOTASPLAN, 
						GdeError.PLAN_CANMAXCUO_REF );			
		}
		
		if (hasError()) {
			return false;
		}
		
		if (getCantidadCuotasPlan() != null && getPorDesCap() == null && getPorDesAct() == null && getPorDesInt() == null ){
			addRecoverableError(GdeError.PLANDESCUENTO_UNADESCUENTO_REQUERIDO);
		}
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANDESCUENTO_FECHADESDE, GdeError.PLANDESCUENTO_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANDESCUENTO_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validacion del formato de porcentaje
		if (getPorDesCap() != null && (getPorDesCap().doubleValue() < 0 || getPorDesCap().doubleValue() > 1)){
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLANVEN_PORDESCAP);
		}

		if (getPorDesAct() != null && (getPorDesAct().doubleValue() < 0 || getPorDesAct().doubleValue() > 1)){
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLANVEN_PORDESACT);
		}
		
		if (getPorDesInt() != null && (getPorDesInt().doubleValue() < 0 || getPorDesInt().doubleValue() > 1)){
			addRecoverableError(BaseError.MSG_FORMATO_CAMPO_PORCENTAJE_INVALIDO, GdeError.PLANVEN_PORDESINT);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanDescuentoDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
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
