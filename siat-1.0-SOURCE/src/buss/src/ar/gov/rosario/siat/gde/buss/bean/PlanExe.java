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
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a PlanExe
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planExe")
public class PlanExe extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idExencion") 
	private Exencion exencion;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;

	//<#Propiedades#>
	
	// Constructores
	public PlanExe(){
		super();
		// Seteo de valores default			
	}
	
	public PlanExe(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanExe getById(Long id) {
		return (PlanExe) GdeDAOFactory.getPlanExeDAO().getById(id);
	}
	
	public static PlanExe getByIdNull(Long id) {
		return (PlanExe) GdeDAOFactory.getPlanExeDAO().getByIdNull(id);
	}
	
	public static List<PlanExe> getList() {
		return (List<PlanExe>) GdeDAOFactory.getPlanExeDAO().getList();
	}
	
	public static List<PlanExe> getListActivos() {			
		return (List<PlanExe>) GdeDAOFactory.getPlanExeDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getPlan() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL );
		}
		
		if (getExencion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.EXENCION_LABEL);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANEXE_FECHADESDE);			
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANEXE_FECHAHASTA);			
		}
		
		if (hasError()) {
			return false;
		}
		
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANEXE_FECHADESDE, GdeError.PLANEXE_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANEXE_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanExeDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanExe. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanExeDAO().update(this);
	}

	/**
	 * Desactiva el PlanExe. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanExeDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanExe
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanExe
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
