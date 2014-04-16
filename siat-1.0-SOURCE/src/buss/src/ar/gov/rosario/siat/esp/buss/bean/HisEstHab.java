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

import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean corresponiente al registro Historico de Estados de la Habilitacion.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_hisEstHab")
public class HisEstHab extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idHabilitacion") 
	private Habilitacion habilitacion;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstHab") 
	private EstHab estHab;

	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "logCambios")
	private String logCambios;
	
	@Column(name = "observaciones")
	private String observaciones;

	// Constructores
	public HisEstHab(){
		super();	
	}
	
	public HisEstHab(Long id){
		super();
		setId(id);
	}
	
	// Getters y Setters
	
	public Habilitacion getHabilitacion() {
		return habilitacion;
	}

	public void setHabilitacion(Habilitacion habilitacion) {
		this.habilitacion = habilitacion;
	}

	public EstHab getEstHab() {
		return estHab;
	}

	public void setEstHab(EstHab estHab) {
		this.estHab = estHab;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getLogCambios() {
		return logCambios;
	}

	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	// Metodos de clase	
	public static HisEstHab getById(Long id) {
		return (HisEstHab) EspDAOFactory.getHisEstHabDAO().getById(id);
	}
	
	public static HisEstHab getByIdNull(Long id) {
		return (HisEstHab) EspDAOFactory.getHisEstHabDAO().getByIdNull(id);
	}
	
	public static List<HisEstHab> getList() {
		return (ArrayList<HisEstHab>) EspDAOFactory.getHisEstHabDAO().getList();
	}
	
	public static List<HisEstHab> getListActivos() {			
		return (ArrayList<HisEstHab>) EspDAOFactory.getHisEstHabDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesHisEstHab())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPORIMOV_DESTIPORIMOV);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.TIPORIMOV_RECURSO);
		}*/
		
		if (hasError()) {
			return false;
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
		
		/*if (GenericDAO.hasReference(this, Hab.class, "hisEstHab")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				EspError.TIPORIMOV_LABEL , EspError.DIARIOPARTIDA_LABEL);
		}
		 */
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}


}
