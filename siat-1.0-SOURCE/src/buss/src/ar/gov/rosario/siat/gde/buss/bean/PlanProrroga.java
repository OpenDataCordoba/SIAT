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
 * Bean correspondiente a PlanProrroga
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_planProrroga")
public class PlanProrroga extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "desPlanProrroga")
	private String desPlanProrroga;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlan") 
	private Plan plan;
	
	@Column(name = "fecVto")
	private Date fecVto;
	
	@Column(name = "fecVtoNue")
	private Date fecVtoNue;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
    @Column(name="idCaso") 
	private String idCaso;

	
	// Constructores
	public PlanProrroga(){
		super();
		// Seteo de valores default			
	}
	
	public PlanProrroga(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static PlanProrroga getById(Long id) {
		return (PlanProrroga) GdeDAOFactory.getPlanProrrogaDAO().getById(id);
	}
	
	public static PlanProrroga getByIdNull(Long id) {
		return (PlanProrroga) GdeDAOFactory.getPlanProrrogaDAO().getByIdNull(id);
	}
	
	public static List<PlanProrroga> getList() {
		return (List<PlanProrroga>) GdeDAOFactory.getPlanProrrogaDAO().getList();
	}
	
	public static List<PlanProrroga> getListActivos() {			
		return (List<PlanProrroga>) GdeDAOFactory.getPlanProrrogaDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}

	public String getDesPlanProrroga() {
		return desPlanProrroga;
	}

	public void setDesPlanProrroga(String desPlanProrroga) {
		this.desPlanProrroga = desPlanProrroga;
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

	public Date getFecVto() {
		return fecVto;
	}

	public void setFecVto(Date fecVto) {
		this.fecVto = fecVto;
	}

	public Date getFecVtoNue() {
		return fecVtoNue;
	}

	public void setFecVtoNue(Date fecVtoNue) {
		this.fecVtoNue = fecVtoNue;
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
		if (StringUtil.isNullOrEmpty(getDesPlanProrroga())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANPRORROGA_DESPLANPRORROGA);
		}

		if (plan == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLAN_LABEL);
		}
		
		if (getFecVto() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANPRORROGA_FECVTO);
		}
		
		if (getFecVtoNue() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANPRORROGA_FECVTONUE);
		}
		
		if (getFechaDesde() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANPRORROGA_FECHADESDE);
		}
		
		if (getFechaHasta() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.PLANPRORROGA_FECHAHASTA);
		}
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el PlanProrroga. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getPlanProrrogaDAO().update(this);
	}

	/**
	 * Desactiva el PlanProrroga. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getPlanProrrogaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del PlanProrroga
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del PlanProrroga
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
	@Override
	public String infoString() {
		String ret =" Prorroga";
		
		if(plan!=null){
			ret+=" - Para el Plan: "+plan.getDesPlan();
		}
		
		if(fecVto!=null){
			ret+=" - Fecha Vto: "+DateUtil.formatDate(fecVto, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fecVtoNue!=null){
			ret+=" - Fecha Vto. Nueva: "+DateUtil.formatDate(fecVtoNue, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaDesde!=null){
			ret+=" - Fecha Desde: "+DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaHasta!=null){
			ret+=" - Fecha Hasta: "+DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		
		return ret;
	}
}
