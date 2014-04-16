//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>


package ar.gov.rosario.siat.def.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.pad.buss.bean.RecAtrCueV;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a RecAtrCue
 * Son los atributos que deberan ser valorizados cada vez que se relacione el Recurso con una Cuenta
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recAtrCue")
public class RecAtrCue extends BaseBO {
	
	public static final Long ID_REGIMEN_DREI=2L;
	public static final Long ID_REGIMEN_ETUR=4L;

	public static final Long ID_CUMUR_DREI=3L;
	public static final Long ID_CUMUR_ETUR=5L;

	public static final Long ID_CATEGORIARS_DREI=6L;
	public static final Long ID_CATEGORIARS_ETUR=7L;

	public static final Long ID_PERIODOINI_DREI=90L;
	public static final Long ID_PERIODOINI_ETUR=91L;
	
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne()  
    @JoinColumn(name="idAtributo")
	private Atributo atributo;
	
	@Column(name = "valorDefecto")
	private String valorDefecto;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "poseeVigencia")
	private Integer poseeVigencia;
	
	@Column(name = "esVisConDeu")
	private Integer esVisConDeu;
	
	@Column(name = "esVisRec")
	private Integer esVisRec;

	@Column(name = "esRequerido")
	private Integer esRequerido;
	
	// Constructores
	public RecAtrCue(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
	}
	
	public Atributo getAtributo(){
		return atributo;
	}
	
	public void setAtributo(Atributo atributo){
		this.atributo = atributo;
	}
	
	public String getValorDefecto(){
		return valorDefecto;
	}
	
	public void setValorDefecto(String valorDefecto){
		this.valorDefecto = valorDefecto;
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
	
	public Integer getPoseeVigencia() {
		return poseeVigencia;
	}
	
	public void setPoseeVigencia(Integer poseeVigencia) {
		this.poseeVigencia = poseeVigencia;
	}
	
	public Integer getEsVisConDeu() {
		return esVisConDeu;
	}
	
	public void setEsVisConDeu(Integer esVisConDeu) {
		this.esVisConDeu = esVisConDeu;
	}
	
	public Integer getEsVisRec() {
		return esVisRec;
	}
	
	public void setEsVisRec(Integer esVisRec) {
		this.esVisRec = esVisRec;
	}
	
	public Integer getEsRequerido() {
		return esRequerido;
	}
	
	public void setEsRequerido(Integer esRequerido) {
		this.esRequerido = esRequerido;
	}
	
	
	// Metodos de Clase
	public static RecAtrCue getById(Long id) {
		return (RecAtrCue) DefDAOFactory.getRecAtrCueDAO().getById(id);  
	}
	
	public static RecAtrCue getByIdNull(Long id) {
		return (RecAtrCue) DefDAOFactory.getRecAtrCueDAO().getByIdNull(id);
	}
	
	public static RecAtrCue getAbiertoByIdRecAtrCue(Long idRecurso, Long idAtributo) {
		return (RecAtrCue) DefDAOFactory.getRecAtrCueDAO().getAbiertoByIdRecAtrCue(idAtributo, idRecurso);
	}
	
	public static List<RecAtrCue> getList() {
		return (List<RecAtrCue>) DefDAOFactory.getRecAtrCueDAO().getList();
	}
	
	public static List<RecAtrCue> getListActivos() {			
		return (List<RecAtrCue>) DefDAOFactory.getRecAtrCueDAO().getListActiva();
	}
	
	public static List<RecAtrCue> getListByIdRecurso(Long id){
		return (List<RecAtrCue>) DefDAOFactory.getRecAtrCueDAO().getListByIdRecurso(id);
	}
	
	public static List<RecAtrCue> getListByIdRecAtr(Long idRecurso, Long idAtributo){
		return (List<RecAtrCue>) DefDAOFactory.getRecAtrCueDAO().getListByIdRecAtr(idRecurso, idAtributo);
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_RECURSO);
		}
		if (getAtributo()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ATRIBUTO);
		}
		/*if (getEsRequerido() != null && getEsRequerido().intValue() == 1 && StringUtil.isNullOrEmpty(getValorDefecto())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_VALORDEFECTO);
		}*/
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_FECHADESDE);
		}
		
		if (getEsVisConDeu() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ESVISCONDEU);
		}

		if (getEsVisRec() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUE_ESVISREC);
		}
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECATRCUE_FECHADESDE, DefError.RECATRCUE_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECATRCUE_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
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
		if (GenericDAO.hasReference(this, RecAtrCueV.class, "recAtrCue")) {
			addRecoverableError(DefError.MSG_ELIMINAR_RECATRCUE_CUENTASVALORIZADAS);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio


}
