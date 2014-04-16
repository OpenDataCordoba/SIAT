//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.frm.buss.bean.Formulario;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a RecEmi
 * Representa las caracteristicas de la emision para el Recurso.
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_recEmi")
public class RecEmi extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)  
    @JoinColumn(name="idTipoEmision")
 	private TipoEmision tipoEmision;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)  
    @JoinColumn(name="idPeriodoDeuda")
	private PeriodoDeuda periodoDeuda;
	
	@Column(name="canPerAEmi")
	private Long canPerAEmi;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)  
    @JoinColumn(name="idForVen1")
	private Vencimiento forVen;
	
	@ManyToOne(fetch=FetchType.LAZY)  
    @JoinColumn(name="idAtributoEmision")
	private Atributo atributoEmision;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)  
	@JoinColumn(name="idForEmi")
	private Formulario formulario;

	@Column(name = "generaNotificacion")
	private Integer generaNotificacion;

	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	// Constructores
	public RecEmi(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	
	public TipoEmision getTipoEmision(){
		return tipoEmision;
	}
	
	public void setTipoEmision(TipoEmision tipoEmision){
		this.tipoEmision = tipoEmision;
	}
	
	public PeriodoDeuda getPeriodoDeuda(){
		return periodoDeuda;
	}
	
	public void setPeriodoDeuda(PeriodoDeuda periodoDeuda){
		this.periodoDeuda = periodoDeuda;
	}
	
	public Long getCanPerAEmi(){
		return canPerAEmi;
	}
	
	public void setCanPerAEmi(Long canPerAEmi){
		this.canPerAEmi = canPerAEmi;
	}
	
	public Vencimiento getForVen(){
		return forVen;
	}
	
	public void setForVen(Vencimiento forVen){
		this.forVen = forVen;
	}
	
	public Atributo getAtributoEmision(){
		return atributoEmision;
	}
	
	public void setAtributoEmision(Atributo atributoEmision){
		this.atributoEmision = atributoEmision;
	}
	
	public Formulario getFormulario(){
		return formulario;
	}
	
	public void setFormulario(Formulario formulario){
		this.formulario = formulario;
	}
	
	public Integer getGeneraNotificacion(){
		return generaNotificacion;
	}
	
	public void setGeneraNotificacion(Integer generaNotificacion){
		this.generaNotificacion = generaNotificacion;
	}
	
	public Date getFechaDesde(){
		return fechaDesde;
	}
	
	public void setFechaDesde(Date fechaDesde){
		this.fechaDesde = fechaDesde;
	}
	
	public Date getFechaHasta(){
		return fechaHasta;
	}
	
	public void setFechaHasta(Date fechaHasta){
		this.fechaHasta = fechaHasta;
	}
	
	// Metodos de Clase
	public static RecEmi getById(Long id) {
		return (RecEmi) DefDAOFactory.getRecEmiDAO().getById(id);  
	}
	
	public static RecEmi getByIdNull(Long id) {
		return (RecEmi) DefDAOFactory.getRecEmiDAO().getByIdNull(id);
	}
	
	public static List<RecEmi> getList() {
		return (List<RecEmi>) DefDAOFactory.getRecEmiDAO().getList();
	}
	
	public static List<RecEmi> getListActivos() {			
		return (List<RecEmi>) DefDAOFactory.getRecEmiDAO().getListActiva();
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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones de Requeridos
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECEMI_RECURSO);
		}
		
		if (getTipoEmision() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECEMI_TIPOEMISION);
		}
		if (getPeriodoDeuda() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECEMI_PERIODODEUDA);
		}

		if (getFormulario() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECEMI_FOREMI);
		}
		
		if (getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECEMI_FECHADESDE);
		}
		if(getGeneraNotificacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECEMI_GENERANOTIFICACION);
		}
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(getFechaDesde()!= null && getFechaHasta()!= null &&
				DateUtil.isDateBefore(this.fechaHasta, this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECEMI_FECHADESDE, DefError.RECEMI_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(getFechaDesde() != null && !DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECEMI_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
		}
		
		if (hasError()) {
			return false;
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
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;
	}
}
