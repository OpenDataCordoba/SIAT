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
import coop.tecso.demoda.buss.bean.BaseBO;


/**
 * Bean correspondiente a AccionSellado
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_accionSellado")
public class AccionSellado extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idSellado")
	private Sellado sellado;

	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idAccion")
    private Accion accion;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idRecurso")
    private Recurso recurso;
	
	@Column(name = "esEspecial")
	private Integer esEspecial;
	
	@Column(name = "cantPeriodos")
	private Integer cantPeriodos;
	
	
	@Column(name = "classForName")
	private String classForName;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	//<#Propiedades#>
	
	// Constructores
	public AccionSellado(){
		super();
		// Seteo de valores default			
	}
	
	public AccionSellado(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static AccionSellado getById(Long id) {
		return (AccionSellado) BalDAOFactory.getAccionSelladoDAO().getById(id);
	}
	
	public static AccionSellado getByIdNull(Long id) {
		return (AccionSellado) BalDAOFactory.getAccionSelladoDAO().getByIdNull(id);
	}
	
	public static List<AccionSellado> getList() {
		return (ArrayList<AccionSellado>) BalDAOFactory.getAccionSelladoDAO().getList();
	}
	
	public static List<AccionSellado> getListActivos() {			
		return (ArrayList<AccionSellado>) BalDAOFactory.getAccionSelladoDAO().getListActiva();
	}
	
	public static Boolean existe(Long idSellado, Long idAccion, Date fechaDesde, Date fechaHasta, Long idRecurso){
	return BalDAOFactory.getAccionSelladoDAO().existeAccionSellado(idSellado, idAccion, fechaDesde, fechaHasta, idRecurso);
    }  
	
	//	 Getters y setters
	public Accion getAccion() {
		return accion;
	}

	public void setAccion(Accion accion) {
		this.accion = accion;
	}

	public Integer getCantPeriodos() {
		return cantPeriodos;
	}

	public void setCantPeriodos(Integer cantPeriodos) {
		this.cantPeriodos = cantPeriodos;
	}

	public String getClassForName() {
		return classForName;
	}

	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}

	public Integer getEsEspecial() {
		return esEspecial;
	}

	public void setEsEspecial(Integer esEspecial) {
		this.esEspecial = esEspecial;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Sellado getSellado() {
		return sellado;
	}

	public void setSellado(Sellado sellado) {
		this.sellado = sellado;
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

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		if(existe(getSellado().getId(), getAccion().getId(), getFechaDesde(), getFechaHasta(),getRecurso().getId())){
			addRecoverableError(BaseError.MSG_SOLAPAMIENTO_VIGENCIAYVALOR);
		}
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
		
		if (getAccion() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.ACCIONSELLADO_ACCION_LABEL);
		}
		
		if (getEsEspecial() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.ACCIONSELLADO_ESESPECIAL_LABEL);
		}
		
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,
					BalError.ACCIONSELLADO_FECHADESDE_LABEL);
		}
		
		if(getFechaDesde()!=null && getFechaHasta()!=null && getFechaDesde().after(getFechaHasta())){
			addRecoverableError(BaseError.MSG_VALORMENORQUE,BalError.IMPSEL_FECHAHASTA_LABEL, BalError.IMPSEL_FECHADESDE_LABEL);
		}
         
				
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unicidad

		return true;
	}
	
}
