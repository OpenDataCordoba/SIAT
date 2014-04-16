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
import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.model.PlanClaDeuVO;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanClaDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planClaDeu")
public class PlanClaDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idRecClaDeu") 
	private RecClaDeu recClaDeu;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public PlanClaDeu(){
		super();
		// Seteo de valores default			
	}
	
	public PlanClaDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanClaDeu getById(Long id) {
		return (PlanClaDeu) GdeDAOFactory.getPlanClaDeuDAO().getById(id);
	}
	
	public static PlanClaDeu getByIdNull(Long id) {
		return (PlanClaDeu) GdeDAOFactory.getPlanClaDeuDAO().getByIdNull(id);
	}
	
	public static List<PlanClaDeu> getList() {
		return (List<PlanClaDeu>) GdeDAOFactory.getPlanClaDeuDAO().getList();
	}
	
	public static List<PlanClaDeu> getListActivos() {			
		return (List<PlanClaDeu>) GdeDAOFactory.getPlanClaDeuDAO().getListActiva();
	}
	
	
	// Getters y setters
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

	public RecClaDeu getRecClaDeu() {
		return recClaDeu;
	}
	public void setRecClaDeu(RecClaDeu recClaDeu) {
		this.recClaDeu = recClaDeu;
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
		if (getPlan().getEsManual()!=1 && getPlan().getListPlanClaDeu().size()==1 && getPlan().getEstado()==1){
			addRecoverableError(GdeError.PLANCLADEU_UNICOPLANCLADEU);
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
		
		if (getRecClaDeu() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_LABEL);
		}
		
		if (getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANCLADEU_FECHADESDE);
		}
		
		if (getFechaHasta() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANCLADEU_FECHAHASTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		//	Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANCLADEU_FECHADESDE, GdeError.PLANCLADEU_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANCLADEU_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanClaDeuDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanClaDeu. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanClaDeuDAO().update(this);
	}

	/**
	 * Desactiva el PlanClaDeu. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanClaDeuDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanClaDeu
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanClaDeu
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
	
	public PlanClaDeuVO toVOForSearch() throws Exception {
		
		PlanClaDeuVO planClaDeuVO = (PlanClaDeuVO) this.toVO(0); 
		
		planClaDeuVO.setRecClaDeu((RecClaDeuVO) this.getRecClaDeu().toVO(0));
		
		return planClaDeuVO;
	}
}

