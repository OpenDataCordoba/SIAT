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
import ar.gov.rosario.siat.def.buss.bean.Atributo;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanAtrVal
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planAtrVal")
public class PlanAtrVal extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idAtributo") 
	private Atributo atributo;
	
	@Column(name = "valor")
	private String valor;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public PlanAtrVal(){
		super();
		// Seteo de valores default			
	}
	
	public PlanAtrVal(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanAtrVal getById(Long id) {
		return (PlanAtrVal) GdeDAOFactory.getPlanAtrValDAO().getById(id);
	}
	
	public static PlanAtrVal getByIdNull(Long id) {
		return (PlanAtrVal) GdeDAOFactory.getPlanAtrValDAO().getByIdNull(id);
	}
	
	public static List<PlanAtrVal> getList() {
		return (List<PlanAtrVal>) GdeDAOFactory.getPlanAtrValDAO().getList();
	}
	
	public static List<PlanAtrVal> getListActivos() {			
		return (List<PlanAtrVal>) GdeDAOFactory.getPlanAtrValDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Atributo getAtributo() {
		return atributo;
	}

	public void setAtributo(Atributo atributo) {
		this.atributo = atributo;
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

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
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
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if ( getPlan() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
		}
		
		if (getAtributo() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.ATRIBUTO_LABEL);
		}
		
		if (getValor() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANATRVAL_VALOR);			
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANATRVAL_FECHADESDE);			
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANATRVAL_FECHAHASTA);			
		}
		
		if (hasError()) {
			return false;
		}
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANATRVAL_FECHADESDE, GdeError.PLANATRVAL_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANATRVAL_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanAtrValDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanAtrVal. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanAtrValDAO().update(this);
	}

	/**
	 * Desactiva el PlanAtrVal. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanAtrValDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanAtrVal
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanAtrVal
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
