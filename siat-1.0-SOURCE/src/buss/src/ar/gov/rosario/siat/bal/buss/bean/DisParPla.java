//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a Asociacion de Distribuidor de Partida con Plan para determinado 
 * valor del Atributo de Asentamiento (DisParPla)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_disParPla")
public class DisParPla extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idDisPar") 
	private DisPar disPar;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idPlan") 
	private Plan plan;

	@Column(name = "valor")
	private String valor;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
    @Column(name="idCaso") 
	private String idCaso;



	//Constructores 
	
	public DisParPla(){
		super();
	}

	// Getters y Setters
	
	public DisPar getDisPar() {
		return disPar;
	}
	public void setDisPar(DisPar disPar) {
		this.disPar = disPar;
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
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
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

	// Metodos de clase	
	public static DisParPla getById(Long id) {
		return (DisParPla) BalDAOFactory.getDisParPlaDAO().getById(id);
	}
	
	public static DisParPla getByIdNull(Long id) {
		return (DisParPla) BalDAOFactory.getDisParPlaDAO().getByIdNull(id);
	}
	
	public static List<DisParPla> getList() {
		return (ArrayList<DisParPla>) BalDAOFactory.getDisParPlaDAO().getList();
	}
	
	public static List<DisParPla> getListActivos() {			
		return (ArrayList<DisParPla>) BalDAOFactory.getDisParPlaDAO().getListActiva();
	}
	
	public static List<DisParPla> getListByPlanFecha(Plan plan, Date fechaEjecucion) {
		return (ArrayList<DisParPla>) BalDAOFactory.getDisParPlaDAO().getListByPlanFecha(plan,fechaEjecucion);
	}

	public static List<DisParPla> getListByFecha(Date fechaEjecucion) {
		return (ArrayList<DisParPla>) BalDAOFactory.getDisParPlaDAO().getListByFecha(fechaEjecucion);
	}
	
	public static List<DisParPla> getListByPlanRecursoFechaAtrVal(Plan plan, Recurso recurso, Date fechaEjecucion, String atrVal) {
		return (ArrayList<DisParPla>) BalDAOFactory.getDisParPlaDAO().getListByPlanRecursoFechaAtrVal(plan, recurso, fechaEjecucion, atrVal);
	}
	
	// Metodos de Instancia
	// Validaciones
	/**
	 * Valida la creacion
	 * @author
	 */
	public boolean validateCreate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();
		//Validaciones de Negocio
				
		if (hasError()) {
			return false;
		}
		return !hasError();
	}
	
	/**
	 * Valida la actualizacion
	 * @author
	 */
	public boolean validateUpdate() throws Exception{
		//limpiamos la lista de errores
		clearError();
		
		this.validate();

		if (hasError()) {
			return false;
		}
		return !hasError();
	}

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		// Validaciones de Requeridos	
		if(getPlan()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARPLA_PLAN);
		}
		if(getFechaDesde()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPARPLA_FECHADESDE);
		}
		
		if (hasError()) {
			return false;
		}

		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaHasta!=null){
			if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, BalError.DISPARPLA_FECHADESDE, BalError.DISPARPLA_FECHAHASTA);
			}			
		}

		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
	/**
	 * Obtiene la definicion con su valor del Atributo de Plan para DisParPla.
	 * <p>Este metodo sirve para obtener el atributo de Plan y su valor para DisParPla.
	 * @return el genericAtrDefinition cargado con el atributo y su valor.
	 * @throws Exception 
	 */
	public GenericAtrDefinition getDefinitionValue() throws Exception {		

		GenericAtrDefinition genericAtrDefinition = this.getDisPar().getRecurso().getAtributoAse().getDefinition();
		genericAtrDefinition.addValor(this.getValor());
		genericAtrDefinition.setEsRequerido(true);
			
		return genericAtrDefinition;
	}

	@Override
	public String infoString() {
		String ret ="Asociacion de Distribuidor de Partida con Plan";
		
		if(disPar!=null){
			ret += "  - para el Distribuidor de Partida: "+disPar.getDesDisPar();
			if(disPar.getRecurso()!=null){
				ret += " - del recurso: "+disPar.getRecurso().getDesRecurso();
			}
		}

		if(plan!=null){
			ret +=" - para el plan: "+plan.getDesPlan();
		}
		
		if(valor!=null){
			ret +=" - con valor: "+valor;
		}
		
		if(fechaDesde!=null){
			ret +=" - con Fecha Desde: "+DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(fechaHasta!=null){
			ret +=" - con Fecha Hasta: "+DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(idCaso!=null){
			ret += " - para el Caso: "+idCaso;
		}
		
		return ret;
	}
}
