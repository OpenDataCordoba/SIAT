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

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean correspondiente a RecAtrCueEmi
 * Indica cuales son los atributos que se deben valorizar al momento de la emision.
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recAtrCueEmi")
public class RecAtrCueEmi extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne()  
    @JoinColumn(name="idAtributo")
	private Atributo atributo;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name = "esVisConDeu")
	private Integer esVisConDeu;

	@Column(name = "esVisRec")
	private Integer esVisRec;

	// Constructores
	public RecAtrCueEmi(){
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
	
	// Metodos de Clase
	public static RecAtrCueEmi getById(Long id) {
		return (RecAtrCueEmi) DefDAOFactory.getRecAtrCueEmiDAO().getById(id);  
	}
	
	public static RecAtrCueEmi getByIdNull(Long id) {
		return (RecAtrCueEmi) DefDAOFactory.getRecAtrCueEmiDAO().getByIdNull(id);
	}
	
	public static List<RecAtrCueEmi> getList() {
		return (List<RecAtrCueEmi>) DefDAOFactory.getRecAtrCueEmiDAO().getList();
	}
	
	public static List<RecAtrCueEmi> getListActivos() {			
		return (List<RecAtrCueEmi>) DefDAOFactory.getRecAtrCueEmiDAO().getListActiva();
	}

	public static List<RecAtrCueEmi> getListByIdRecurso(Long id){
		return (List<RecAtrCueEmi>) DefDAOFactory.getRecAtrCueEmiDAO().getListByIdRecurso(id);
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
		if (getRecurso() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUEEMI_RECURSO);
		}
		
		if (getAtributo() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUEEMI_ATRIBUTO);
		}

		if (getFechaDesde() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUEEMI_FECHADESDE);
		}
	
		if (getEsVisConDeu() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUEEMI_ESVISCONDEU);
		}

		if (getEsVisRec() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRCUEEMI_ESVISREC);
		}

		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECATRCUEEMI_FECHADESDE, DefError.RECATRCUEEMI_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECATRCUEEMI_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
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
