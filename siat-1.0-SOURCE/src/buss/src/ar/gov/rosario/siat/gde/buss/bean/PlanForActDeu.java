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
import coop.tecso.demoda.iface.model.SiNo;


/**
 * Bean correspondiente a PlanForActDeu
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planForActDeu")
public class PlanForActDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "fecVenDeuDes")
	private Date fecVenDeuDes;
	
	@Column(name = "esComun")
	private Integer esComun;
	
	@Column(name = "porcentaje")
	private Double porcentaje;
	
	@Column(name = "className")
	private String className;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	//<#Propiedades#>
	
	// Constructores
	public PlanForActDeu(){
		super();
		// Seteo de valores default			
	}
	
	public PlanForActDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanForActDeu getById(Long id) {
		return (PlanForActDeu) GdeDAOFactory.getPlanForActDeuDAO().getById(id);
	}
	
	public static PlanForActDeu getByIdNull(Long id) {
		return (PlanForActDeu) GdeDAOFactory.getPlanForActDeuDAO().getByIdNull(id);
	}
	
	public static List<PlanForActDeu> getList() {
		return (List<PlanForActDeu>) GdeDAOFactory.getPlanForActDeuDAO().getList();
	}
	
	public static List<PlanForActDeu> getListActivos() {			
		return (List<PlanForActDeu>) GdeDAOFactory.getPlanForActDeuDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Integer getEsComun() {
		return esComun;
	}

	public void setEsComun(Integer esComun) {
		this.esComun = esComun;
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

	public Date getFecVenDeuDes() {
		return fecVenDeuDes;
	}

	public void setFecVenDeuDes(Date fecVenDeuDes) {
		this.fecVenDeuDes = fecVenDeuDes;
	}

	public Plan getPlan() {
		return plan;
	}

	public void setPlan(Plan plan) {
		this.plan = plan;
	}

	public Double getPorcentaje() {
		return porcentaje;
	}
	
	public void setPorcentaje(Double porcentaje) {
		this.porcentaje = porcentaje;
	}
	
	public String getClassName() {
		return className;
	}
	public void setClassName(String className) {
		this.className = className;
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
		}
		
		if (getEsComun() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANFORACTDEU_ESCOMUN);
		}else if(getEsComun().equals(SiNo.SI.getId())){
			if(getPorcentaje()==null)
				addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANFORACTDEU_PORCENTAJE);
		}
				
		if (getFecVenDeuDes() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANFORACTDEU_FECVENDEUDES);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANFORACTDEU_FECHADESDE);
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANFORACTDEU_FECHAHASTA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Si es Comun debe ingresar porcentaje.
		if ( getEsComun().intValue() == 1 && getPorcentaje() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,  GdeError.PLANFORACTDEU_PORCENTAJE);
		}
		
		// Si No es Comun, el nombre de la clase es requerida 
		if (getEsComun().intValue() == 0 && StringUtil.isNullOrEmpty(getClassName()) ){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANFORACTDEU_CLASSNAME);
		}
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(getFechaDesde(), getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, GdeError.PLANFORACTDEU_FECHADESDE, GdeError.PLANFORACTDEU_FECHAHASTA);
		}
		
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Plan
		if(!DateUtil.isDateBeforeOrEqual(getPlan().getFechaAlta(), getFechaDesde())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, GdeError.PLANFORACTDEU_FECHADESDE, GdeError.PLAN_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
		}
		
		if (GdeDAOFactory.getPlanForActDeuDAO().chkSolapaVigenciayValor(this)){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanForActDeu. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanForActDeuDAO().update(this);
	}

	/**
	 * Desactiva el PlanForActDeu. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanForActDeuDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanForActDeu
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanForActDeu
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
