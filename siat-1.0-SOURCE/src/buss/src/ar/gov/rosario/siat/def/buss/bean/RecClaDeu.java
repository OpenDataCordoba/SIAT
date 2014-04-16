//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
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
import ar.gov.rosario.siat.gde.buss.bean.Plan;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a RecClaDeu
 * Representa las diversas deudas que pueden generarse para un determinado Recurso.
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recClaDeu")
public class RecClaDeu extends BaseBO {

	private static final long serialVersionUID = 1L;
	public static final long ID_ORIGINAL = 1L;
	public static final long ID_RETROACTIVA = 2L;

	public static final long ID_CUOTA_PAV_CDM = 3L;
	public static final long ID_CUOTA_GAS_CDM = 21L;
	
	public static final long ID_DDJJ_ORIGINAL = 5L;
	public static final long ID_DDJJ_RECTIFICATIVA_DREI = 6L;
	public static final long ID_RETENCIONES = 12;
	public static final long ID_SALDOS_CONVENIOS = 11L;
	public static final long ID_MULTA = 20L;
	public static final long ID_AJUSTE_FISCAL_DREI = 7L;
	public static final long ID_AJUSTE_FISCAL_ETUR = 19L;
	public static final long ID_AJUSTE_FISCAL_DREI_HISTORICO = 13L;
	public static final long ID_AJUSTE_FISCAL_DREI_ACCESORIO = 14L;
	public static final long ID_AJUSTE_FISCAL_ETUR_HISTORICO = 24L;
	public static final long ID_AJUSTE_FISCAL_ETUR_ACCESORIO = 25L;
	public static final long ID_ORIGINAL_DERPUB=23L;
	public static final long ID_DDJJ_RECTIFICATIVA_ETUR = 18L;
	public static final long ID_DDJJ_RECTIFICATIVA_DERPUB=94L;
	public static final long ID_AJUSTE_VERIFICADO_DREI=99L;
	public static final long ID_AJUSTE_VERIFICADO_ETUR=101L;
	
	public static final long ID_DDJJ_ORIGINAL_ETUR = 17L;
	
	public static final String ABR_ORIG ="Orig";
	public static final String ABR_RECTIF = "Rectif";
	public static final String ABR_MULTA = "MultOmisDJ";
	public static final String ABR_RS = "RS";
	public static final String ABR_IRS = "IRS";
	public static final String ABR_DIFRS = "DifRS";
	
	public static final String ABR_AF = "AF";
	public static final String ABR_AV = "AV";

	
	@ManyToOne(fetch=FetchType.LAZY)  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name = "desClaDeu")
	private String desClaDeu;
	
	@Column(name = "abrClaDeu")
	private String abrClaDeu;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name="esOriginal")
	private Integer esOriginal;
		
	@Column(name="actualizaDeuda")
	private Integer actualizaDeuda;
	
	// Constructores
	public RecClaDeu(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	public String getDesClaDeu(){
		return desClaDeu;
	}
	public void setDesClaDeu(String desClaDeu){
		this.desClaDeu = desClaDeu;
	}
	public String getAbrClaDeu(){
		return abrClaDeu;
	}
	public void setAbrClaDeu(String abrClaDeu){
		this.abrClaDeu = abrClaDeu;
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
	
	public Integer getEsOriginal() {
		return esOriginal;
	}
	public void setEsOriginal(Integer esOriginal) {
		this.esOriginal = esOriginal;
	}
	
	public Integer getActualizaDeuda() {
		return actualizaDeuda;
	}
	public void setActualizaDeuda(Integer actualizaDeuda) {
		this.actualizaDeuda = actualizaDeuda;
	}
	
	
	// Metodos de Clase
	public static RecClaDeu getById(Long id) {
		return (RecClaDeu) DefDAOFactory.getRecClaDeuDAO().getById(id);  
	}
	
	public static RecClaDeu getByIdNull(Long id) {
		return (RecClaDeu) DefDAOFactory.getRecClaDeuDAO().getByIdNull(id);
	}
	
	public static RecClaDeu getByRecursoAndAbrClaDeu(Recurso recurso, String abrClaDeu) {
		return (RecClaDeu) DefDAOFactory.getRecClaDeuDAO().getByRecursoAndAbrClaDeu(recurso,abrClaDeu);
	}
	
	public static List<RecClaDeu> getList() {
		return (ArrayList<RecClaDeu>) DefDAOFactory.getRecClaDeuDAO().getList();
	}
	
	public static List<RecClaDeu> getListActivos() {			
		return (ArrayList<RecClaDeu>) DefDAOFactory.getRecClaDeuDAO().getListActiva();
	}

	public static List<RecClaDeu> getListByIdRecurso(Long Idrecurso) {
		return (List<RecClaDeu>) DefDAOFactory.getRecClaDeuDAO().getListByIdRecurso(Idrecurso);
	}
	
	public static List<RecClaDeu> getListByPlan (Plan plan){
		return DefDAOFactory.getRecClaDeuDAO().getListByPlan(plan);
	}
		
	public static List<RecClaDeu> getListByIdRecurso(Long Idrecurso, List<Long> listIdExcluidos) {
		return (List<RecClaDeu>) DefDAOFactory.getRecClaDeuDAO().getListByIdRecurso(Idrecurso, listIdExcluidos);
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
		
		//UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		if (getRecurso()==null) { 
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_RECURSO);
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_FECHADESDE);
		}
		if (StringUtil.isNullOrEmpty(getDesClaDeu())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_DESCLADEU);
		}
		/*
		 * La abreviatura puede ser vacia
		}*/
		
		if (getEsOriginal() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_ESORIGINAL);
		}
		if (getActualizaDeuda() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCLADEU_ACTUALIZADEUDA);
		}
		
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, DefError.RECCLADEU_FECHADESDE, DefError.RECCLADEU_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECCLADEU_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
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

	// Metodos de negocio
	

}
