//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Vigencia;

/**
 * Bean correspondiente a la Valorizacion de Atributos del Contribuyente
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_conAtrVal")
public class ConAtrVal extends BaseBO {

	// Propiedades
	private static final long serialVersionUID = 1L;
	
	public static final Long CONTRIBUYENTE_CER = 3L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idContribuyente")
	private Contribuyente contribuyente;
	
	@Column(name="idContribuyente", insertable= false, updatable = false)
	private Long idContribuyente;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
	@JoinColumn(name="idConAtr")
	private ConAtr conAtr;
	
	@Column(name="idConAtr", insertable= false, updatable = false)
	private Long idConAtr;
	
	@Column(name="valor")
	private String valor;

	@Column(name="fechaDesde")
	private Date fechaDesde;

	@Column(name="fechaHasta")
	private Date fechaHasta;

    @Column(name="idCaso") 
	private String idCaso;

	// Constructores
	public ConAtrVal(){
		super();
	}
	
	// Getters y setters
	public Contribuyente getContribuyente() {
		return contribuyente;
	}
	public void setContribuyente(Contribuyente contribuyente) {
		this.contribuyente = contribuyente;
	}
	public ConAtr getConAtr() {
		return conAtr;
	}
	public void setConAtr(ConAtr conAtr) {
		this.conAtr = conAtr;
	}
	public String getValor() {
		return valor;
	}
	public void setValor(String valor) {
		this.valor = valor;
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
	
	public Long getIdConAtr() {
		return idConAtr;
	}
	public void setIdConAtr(Long idConAtr) {
		this.idConAtr = idConAtr;
	}

	public Long getIdContribuyente() {
		return idContribuyente;
	}
	public void setIdContribuyente(Long idContribuyente) {
		this.idContribuyente = idContribuyente;
	}

	// Metodos de clase
	public static ConAtrVal getById(Long id) {
		return (ConAtrVal) PadDAOFactory.getConAtrValDAO().getById(id);
	}
	
	/** Recupera el valor vigente para el contribuyente y el 
	 *  atributo pasado como parametros.
	 * 
	 * @param idContribuyente
	 * @param idConAtr
	 * @return
	 */
	public static ConAtrVal getVigente(Long idContribuyente, Long idConAtr) {
		return PadDAOFactory.getConAtrValDAO().getConAtrValVigente(idContribuyente, idConAtr);
	}

	/** 
	 * Recupera la lista de ConAtrVal para un determinado contribuyente
	 * 	y ConAtr 
	 *  atributo pasado como parametros.
	 * 
	 * @param idContribuyente
	 * @param idConAtr
	 * @return
	 */
	public static List<ConAtrVal> getList(Long idContribuyente, Long idConAtr) {
		return PadDAOFactory.getConAtrValDAO().getListConAtrVal(idContribuyente, idConAtr);
	}
	
	/**
	 * - Utilizado por el metodo getUnionConAtrVal para la liquidacion de la deuda
	 * - Recupera la lista de ConAtrVal para una lista de ids de contribuyente 
	 * 	y una lista de ConAtr obtenidos mediante getDefinitionForWeb()
	 * 
	 * @author Cristian
	 * @param idContribuyente
	 * @param idConAtr
	 * @return List<ConAtrVal>
	 */
	public static List<ConAtrVal> getListByIdsContribuyentesYIdsConAtr(Long[] listIdsContrib, Long[] listIdsConAtr) {
		return PadDAOFactory.getConAtrValDAO().getListByIdsContribuyentesYIdsConAtr(listIdsContrib, listIdsConAtr);
	}
	
	// Metodos de Instancia
	// Validaciones
	
	/**
	 * Valida la creacion
	 * @author Ivan
	 */
	public boolean validateCreate() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	/**
	 * Valida la actualizacion
	 * @author Ivan
	 */
	public boolean validateUpdate() {
		//limpiamos la lista de errores
		clearError();

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		return true;		
	}
	/**
	 * Valida la eliminacion
	 * @author Ivan
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
	
		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de negocio
	/** Si la fecha desde es menor o igual a la actual y la fecha hasta es nula
	 *  el atrVal esta vigente.
	 *  Si la fecha actual esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */

    public Integer getVigencia() {

    	return this.getVigenciaForDate(new Date());

    }
    
  

	/** Si la fecha desde es menor o igual a la fecha a validar 
	 *  y la fecha hasta es nula el atrVal esta vigente.
	 *  Si la fecha a validar esta entre la fecha Desde y Hasta el atrVal
	 *  esta vigente.
	 *  Sino esta no vigente.
	 *  
	 */
    public Integer getVigenciaForDate(Date dateToValidate) {

    	Date fechaHastaValue = this.getFechaHasta();
    	Date fechaDesdeValue = this.getFechaDesde();

    	Integer vigencia = Vigencia.NOVIGENTE.getId();
    	
    	// fecha hasta nula
    	if (fechaHastaValue == null) {
    		if ( DateUtil.isDateBeforeOrEqual(fechaDesdeValue, dateToValidate) )  {
    			vigencia = Vigencia.VIGENTE.getId();
    		}
    	}

    	// fecha hasta existente, y no son iguales la fecha desde y hasta
    	if (fechaHastaValue != null && !this.isFecDesdeEqualFecHasta() ) {
    		if ( DateUtil.isDateBeforeOrEqual(fechaDesdeValue, dateToValidate)
    			&& DateUtil.isDateAfterOrEqual(fechaHastaValue, dateToValidate) )  {

    			vigencia = Vigencia.VIGENTE.getId();

    		}
    		
    	}
    	
    	return vigencia;
    }
    
    /** Devuelve true si la fecha desde y hasta son iguales
     * 
     * @return
     */
    private boolean isFecDesdeEqualFecHasta() {
    	if(DateUtil.isDateEqual(this.getFechaDesde(), this.getFechaHasta()) ) {
    		return true;
    	} else {
    		return false;
    	}
    }
	
}
