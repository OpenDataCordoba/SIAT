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
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean correspondiente a RecGenCueAtrVa
 * Indica los valores de un atributo del objeto imponible para los cuales debe generarse la cuenta del recurso indicado.
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recGenCueAtrVa")
public class RecGenCueAtrVa extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne()  
    @JoinColumn(name="idAtributo")
	private Atributo atributo;
	
	@Column(name = "strValor")
	private String strValor;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@Column(name="esMultivalor")
	private Integer esMultivalor;
	
	// Constructores
	public RecGenCueAtrVa(){
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
	public String getStrValor(){
		return strValor;
	}
	public void setStrValor(String strValor){
		this.strValor = strValor;
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
	
	public Integer getEsMultivalor() {
		return esMultivalor;
	}
	public void setEsMultivalor(Integer esMultivalor) {
		this.esMultivalor = esMultivalor;
	}
	
	
	// Metodos de Clase
	public static RecGenCueAtrVa getById(Long id) {
		return (RecGenCueAtrVa) DefDAOFactory.getRecGenCueAtrVaDAO().getById(id);  
	}
	
	public static RecGenCueAtrVa getByIdNull(Long id) {
		return (RecGenCueAtrVa) DefDAOFactory.getRecGenCueAtrVaDAO().getByIdNull(id);
	}
	
	public static RecGenCueAtrVa getAbiertoByIdRecGenCueAtrVa(Long idRecurso, Long idAtributo) {
		return (RecGenCueAtrVa) DefDAOFactory.getRecGenCueAtrVaDAO().getAbiertoByIdRecGenCueAtrVa(idAtributo, idRecurso);
	}
	
	public static List<RecGenCueAtrVa> getList() {
		return (List<RecGenCueAtrVa>) DefDAOFactory.getRecGenCueAtrVaDAO().getList();
	}
	
	public static List<RecGenCueAtrVa> getListActivos() {			
		return (List<RecGenCueAtrVa>) DefDAOFactory.getRecGenCueAtrVaDAO().getListActiva();
	}
	
	public static List<RecGenCueAtrVa> getListByIdRecurso(Long id){
		return (List<RecGenCueAtrVa>) DefDAOFactory.getRecGenCueAtrVaDAO().getListByIdRecurso(id);
	}
	
	public static List<RecGenCueAtrVa> getListByIdRecAtr(Long idRecurso, Long idAtributo){
		return (List<RecGenCueAtrVa>) DefDAOFactory.getRecGenCueAtrVaDAO().getListByIdRecAtr(idRecurso, idAtributo);
	}
	
	
	public static List<RecGenCueAtrVa> getListByIdRecAtrValor(Long idRecurso, Long idAtributo, String strValor){
		return (List<RecGenCueAtrVa>) DefDAOFactory.getRecGenCueAtrVaDAO().getListByIdRecAtrValor(idRecurso, idAtributo, strValor);
	}
	
	public static List<Atributo> getListAtributoByIdRecurso(Long id){
		return (List<Atributo>) DefDAOFactory.getRecGenCueAtrVaDAO().getListAtributoByIdRecurso(id);
	}
	
	public static List<RecGenCueAtrVa> getListByStrValor(String codRubro) {
		return (List<RecGenCueAtrVa>) DefDAOFactory.getRecGenCueAtrVaDAO().getListByStrValor(codRubro);
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
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECGENCUEATRVA_RECURSO);
		}
		if (getAtributo()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECGENCUEATRVA_ATRIBUTO);
		}
		if (StringUtil.isNullOrEmpty(getStrValor())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECGENCUEATRVA_STRVALOR);
		}
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECGENCUEATRVA_FECHADESDE);
		}
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECGENCUEATRVA_FECHADESDE, DefError.RECGENCUEATRVA_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECGENCUEATRVA_FECHADESDE, DefError.RECURSO_FECHAALTA_REF);
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
