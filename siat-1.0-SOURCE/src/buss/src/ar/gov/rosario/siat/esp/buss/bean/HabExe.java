//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

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
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import ar.gov.rosario.siat.exe.buss.bean.Exencion;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean corresponiente a las Exeniones de la habilitacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_habExe")
public class HabExe extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idHabilitacion")
	private Habilitacion habilitacion;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idExencion") 
	private Exencion exencion;
	
	
	//Constructores 
	public HabExe(){
		super();
	}
	
	// Getters Y Setters
	
	public static HabExe getById(Long id) {
		return (HabExe) EspDAOFactory.getHabExeDAO().getById(id);
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

	public Habilitacion getHabilitacion() {
		return habilitacion;
	}

	public void setHabilitacion(Habilitacion habilitacion) {
		this.habilitacion = habilitacion;
	}

	public Exencion getExencion() {
		return exencion;
	}

	public void setExencion(Exencion exencion) {
		this.exencion = exencion;
	}

	// Metodos de clase
	public static HabExe getByIdNull(Long id) {
		return (HabExe) EspDAOFactory.getHabExeDAO().getByIdNull(id);
	}
	
	public static List<HabExe> getList() {
		return (ArrayList<HabExe>) EspDAOFactory.getHabExeDAO().getList();
	}
	
	public static List<HabExe> getListActivos() {			
		return (ArrayList<HabExe>) EspDAOFactory.getHabExeDAO().getListActiva();
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

	/**
	 * Validaciones comunes de creacion y actualizacion
	 * 
	 * @return boolean
	 * @throws Exception
	 */
	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(getHabilitacion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABILITACION_LABEL);
		}
		
		if(getExencion() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.HABEXE_LABEL);
		}
		
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		if(this.fechaDesde!=null && this.fechaHasta!=null){
			if(!DateUtil.isDateBeforeOrEqual(this.fechaDesde, this.fechaHasta)){
				addRecoverableError(BaseError.MSG_VALORMAYORQUE, EspError.HABILITACION_FECEVEDES, EspError.HABILITACION_FECEVEHAS);
			}			
		}
		
		if (hasError()) {
			return false;
		}
		// Valida que la Fecha Desde no sea mayor que la fecha Hasta
		
		
		return !hasError();
	}
	
	
	/**
	 * Valida la eliminacion
	 * @author
	 */
	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}	
}

