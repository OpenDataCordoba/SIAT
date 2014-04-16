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
 * Bean correspondiente a RecAtrVal
 * Representa atributos propios del Recurso que pueden ser utilizados en las formulas de calculo.
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recAtrVal")
public class RecAtrVal extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne()  
    @JoinColumn(name="idAtributo")
	private Atributo atributo;
	
	@Column(name = "valor")
	private String valor;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
		
	// Constructores
	public RecAtrVal(){
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
	public String getValor(){
		return valor;
	}
	public void setValor(String valor){
		this.valor = valor;
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
	public static RecAtrVal getById(Long id) {
		return (RecAtrVal) DefDAOFactory.getRecAtrValDAO().getById(id);  
	}
	
	public static RecAtrVal getByIdNull(Long id) {
		return (RecAtrVal) DefDAOFactory.getRecAtrValDAO().getByIdNull(id);
	}
	
	public static RecAtrVal getAbiertoByIdRecAtrVal(Long idRecurso, Long idAtributo) {
		return (RecAtrVal) DefDAOFactory.getRecAtrValDAO().getAbiertoByIdRecAtrVal(idAtributo, idRecurso);
	}
	
	public static List<RecAtrVal> getList() {
		return (List<RecAtrVal>) DefDAOFactory.getRecAtrValDAO().getList();
	}
	
	public static List<RecAtrVal> getListActivos() {			
		return (List<RecAtrVal>) DefDAOFactory.getRecAtrValDAO().getListActiva();
	}
	
	public static List<RecAtrVal> getListByIdRecurso(Long id){
		return (List<RecAtrVal>) DefDAOFactory.getRecAtrValDAO().getListByIdRecurso(id);
	}
	
	public static List<RecAtrVal> getListByIdRecAtr(Long idRecurso, Long idAtributo){
		return (List<RecAtrVal>) DefDAOFactory.getRecAtrValDAO().getListByIdRecAtr(idRecurso, idAtributo);
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRVAL_RECURSO);
		}
		if (getAtributo()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRVAL_ATRIBUTO);
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECATRVAL_FECHADESDE);
		}
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECATRVAL_FECHADESDE, DefError.RECATRVAL_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECATRVAL_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
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
