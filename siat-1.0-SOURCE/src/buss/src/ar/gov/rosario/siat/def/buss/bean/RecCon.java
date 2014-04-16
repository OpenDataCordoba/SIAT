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

import ar.gov.rosario.siat.bal.buss.bean.DisParDet;
import ar.gov.rosario.siat.bal.buss.bean.OtrIngTesRecCon;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.model.RecConVO;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.exe.buss.bean.ExeRecCon;
import ar.gov.rosario.siat.exe.iface.util.ExeError;
import ar.gov.rosario.siat.gde.buss.bean.DeuAdmRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuAnuRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuCanRecCon;
import ar.gov.rosario.siat.gde.buss.bean.DeuJudRecCon;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a RecCon
 * Representa los conceptos que componen un determinado Recurso
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recCon")
public class RecCon extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	// Constantes para los conceptos de la deuda CDM
	// TODO: pasar a Con*
	public static final String COD_CAPITAL_GAS = "CAP_GAS";
	public static final String COD_INTERES_GAS = "INT_GAS";
	public static final String COD_CAPITAL_PAV = "CAP_PAV";
	public static final String COD_INTERES_PAV = "INT_PAV";
	public static final String COD_DER_DREi = "DER";
	public static final String COD_INDEX_ENVIO="5";
	
	public static final String COD_CON1 = "CON1";
	public static final String COD_CON2 = "CON2";
	public static final String COD_CON3 = "CON3";
	public static final String COD_CON4 = "CON4";
	
	@ManyToOne(fetch=FetchType.LAZY)  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@Column(name = "codRecCon")
	private String codRecCon;
	
	@Column(name = "desRecCon")
	private String desRecCon;
	
	@Column(name = "abrRecCon")
	private String abrRecCon;
		
	@Column(name = "porcentaje")
	private Double porcentaje;
	
	@Column(name = "incrementa")
	private Integer incrementa;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "esVisible")
	private Integer esVisible;
	
	@Column(name = "ordenVisualizacion")
	private Long ordenVisualizacion;
	
	// Constructores
	public RecCon(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	public String getCodRecCon(){
		return codRecCon;
	}
	public void setCodRecCon(String codRecCon){
		this.codRecCon = codRecCon;
	}
	public String getDesRecCon(){
		return desRecCon;
	}
	public void setDesRecCon(String desRecCon){
		this.desRecCon = desRecCon;
	}
	public String getAbrRecCon(){
		return abrRecCon;
	}
	public void setAbrRecCon(String abrRecCon){
		this.abrRecCon = abrRecCon;
	}
	public Double getPorcentaje(){
		return porcentaje;
	}
	public void setPorcentaje(Double porcentaje){
		this.porcentaje = porcentaje;
	}
	public Integer getIncrementa(){
		return incrementa;
	}
	public void setIncrementa(Integer incrementa){
		this.incrementa = incrementa;
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
	public Integer getEsVisible(){
		return esVisible;
	}
	public void setEsVisible(Integer esVisible){
		this.esVisible = esVisible;
	}
	public Long getOrdenVisualizacion(){
		return ordenVisualizacion;
	}
	public void setOrdenVisualizacion(Long ordenVisualizacion){
		this.ordenVisualizacion = ordenVisualizacion;
	}
	
	// Metodos de Clase
	public static RecCon getById(Long id) {
		return (RecCon) DefDAOFactory.getRecConDAO().getById(id);  
	}
	
	public static RecCon getByIdRecursoAndCodigo(Long idRecurso, String codigo) throws Exception {
		return (RecCon) DefDAOFactory.getRecConDAO().getByIdRecursoAndCodigo(idRecurso,codigo);
	}
		
	public static RecCon getByIdRecursoAndOrden(Long idRecurso, int nroOrden) throws Exception {
		return (RecCon) DefDAOFactory.getRecConDAO().getByIdRecursoAndOrden(idRecurso, nroOrden);
	}
	
	public static RecCon getByIdNull(Long id) {
		return (RecCon) DefDAOFactory.getRecConDAO().getByIdNull(id);
	}
	
	public static List<RecCon> getList() {
		return (ArrayList<RecCon>) DefDAOFactory.getRecConDAO().getList();
	}
	
	public static List<RecCon> getListActivos() {			
		return (ArrayList<RecCon>) DefDAOFactory.getRecConDAO().getListActiva();
	}
	
	public static List<RecCon> getListVigentesByIdRecurso(Long id, List<RecConVO> listVOExcluidos, Date fecha) {			
		return (List<RecCon>) DefDAOFactory.getRecConDAO().getListVigentesByIdRecurso(id, listVOExcluidos, fecha);
	}
	
	public static List<RecCon> getListActivosByIdRecurso(Long idRecurso) {			
		return (List<RecCon>) DefDAOFactory.getRecConDAO().getListRecConOrderByVisualizacion(idRecurso);
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
		
		UniqueMap uniqueMap = new UniqueMap();

		//Validaciones de Requeridos
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_RECURSO);
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_FECHADESDE);
		}
		if (StringUtil.isNullOrEmpty(getCodRecCon())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_CODRECCON);
		}
		if (StringUtil.isNullOrEmpty(getDesRecCon())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_DESRECCON);
		}
		if (StringUtil.isNullOrEmpty(getAbrRecCon())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_ABRRECCON);
		}
		if(getIncrementa()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_INCREMENTA);
		}
		if(getEsVisible()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_ESVISIBLE);
		}
		if (getPorcentaje()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_PORCENTAJE);
		}
		if (getOrdenVisualizacion()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCON_ORDENVISUALIZACION);
		}
			
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		uniqueMap.addString("codRecCon");
		uniqueMap.addLong("ordenVisualizacion");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)){
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, DefError.RECCON_CODRECCON);
		}
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor o igual que la fecha Hasta
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORIGUALQUE, DefError.RECCON_FECHADESDE, DefError.RECCON_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECCON_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
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
		
		if (GenericDAO.hasReference(this, ExeRecCon.class, "recCon")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECCON_LABEL , ExeError.EXERECCON_LABEL);
		}
		if (GenericDAO.hasReference(this, DeuAdmRecCon.class, "recCon")
			||GenericDAO.hasReference(this, DeuJudRecCon.class, "recCon")			
			||GenericDAO.hasReference(this, DeuAnuRecCon.class, "recCon")
			||GenericDAO.hasReference(this, DeuCanRecCon.class, "recCon")) {
				addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
						DefError.RECCON_LABEL , GdeError.DEURECCON_LABEL);
		}
		if (GenericDAO.hasReference(this, DisParDet.class, "recCon")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECCON_LABEL , BalError.DISPARDET_LABEL);
		}
		if (GenericDAO.hasReference(this, OtrIngTesRecCon.class, "recCon")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				DefError.RECCON_LABEL , BalError.OTRINGTESRECCON_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio
}
