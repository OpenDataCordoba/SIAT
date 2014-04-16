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
 * Bean correspondiente a RecAli
 * Indica los valores de alicuotas de un recurso autoliquidable
 * 
 * @author tecso
 */

@Entity
@Table(name = "def_recAli")
public class RecAli extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;
	
	@ManyToOne()  
    @JoinColumn(name="idRecTipAli")
	private RecTipAli recTipAli;
	
	@Column(name="alicuota")
	private Double alicuota;
	
	@Column(name="observacion")
	private String observacion;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;
	
	@Column(name="fechaHasta")
	private Date fechaHasta;
	
	
	
	// Constructores
	public RecAli(){
		super();
	}
	// Getters y Setters
	public Recurso getRecurso(){
		return recurso;
	}
	public void setRecurso(Recurso recurso){
		this.recurso = recurso;
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
	
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public RecTipAli getRecTipAli() {
		return recTipAli;
	}
	public void setRecTipAli(RecTipAli recTipAli) {
		this.recTipAli = recTipAli;
	}
	public Double getAlicuota() {
		return alicuota;
	}
	public void setAlicuota(Double alicuota) {
		this.alicuota = alicuota;
	}
	// Metodos de Clase
	public static RecAli getById(Long id) {
		return (RecAli) DefDAOFactory.getRecAliDAO().getById(id);  
	}
	
	public static RecAli getByIdNull(Long id) {
		return (RecAli) DefDAOFactory.getRecAliDAO().getByIdNull(id);
	}
	
	public static List<RecAli> getList() {
		return (List<RecAli>) DefDAOFactory.getRecAliDAO().getList();
	}
	
	public static List<RecAli> getListActivos() {			
		return (List<RecAli>) DefDAOFactory.getRecAliDAO().getListActiva();
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
		if (getRecurso()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_RECURSO);
		}
		if (getRecTipAli()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECALI_RECTIPALI);
		}
		
		if (getAlicuota()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECALI_ALICUOTA);
		}
		
		if (getFechaDesde()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECCONADEC_FECHADESDE);
		}
	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		if(!DateUtil.isDateBefore(this.fechaDesde, this.fechaHasta)){
			addRecoverableError(BaseError.MSG_VALORMAYORQUE, DefError.RECCONADEC_FECHADESDE, DefError.RECCONADEC_FECHAHASTA);
		}
		// Valida que la Fecha Desde no sea menor que la fecha Alta del Recurso
		if(!DateUtil.isDateBeforeOrEqual(this.getRecurso().getFechaAlta(), this.fechaDesde)){
			addRecoverableError(BaseError.MSG_VALORMENORQUE, DefError.RECCONADEC_FECHADESDE, DefError.RECCONADEC_FECHAHASTA);
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
