//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rod.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.rod.buss.dao.RodDAOFactory;
import ar.gov.rosario.siat.rod.iface.util.RodError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a HisEstTra
 * 
 * @author tecso
 */
@Entity
@Table(name = "rod_hisEstTra")
public class HisEstTra extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne()
	@JoinColumn(name="idEstTra")
	private EstadoTramiteRA estTra; 

	@ManyToOne()
	@JoinColumn(name="idTramite")
	private TramiteRA tramiteRA;

	@Column(name = "logCambios")
	private String logCambios;

	@Column(name = "fecha")
	private Date fecha;

	// Constructores
	public HisEstTra(){
		super();
	}
	
	public HisEstTra(EstadoTramiteRA estTra, TramiteRA tramiteRA, String logCambios){
		super();
		this.setEstTra(estTra);
		this.setTramiteRA(tramiteRA);
		this.setLogCambios(logCambios);
		this.setFecha(new Date());
	}

	// Metodos de Clase
	public static HisEstTra getById(Long id) {
		return (HisEstTra) RodDAOFactory.getHisEstTraDAO().getById(id);
	}
	
	public static HisEstTra getByIdNull(Long id) {
		return (HisEstTra) RodDAOFactory.getHisEstTraDAO().getByIdNull(id);
	}
	
	public static List<HisEstTra> getList() {
		return (ArrayList<HisEstTra>) RodDAOFactory.getHisEstTraDAO().getList();
	}
	
	public static List<HisEstTra> getListActivos() {			
		return (ArrayList<HisEstTra>) RodDAOFactory.getHisEstTraDAO().getListActiva();
	}
	
	public static HisEstTra getLastHisEstTra(Long idEstadoTramiteRA) {
		return (HisEstTra) RodDAOFactory.getHisEstTraDAO().getLastHisEstTra(idEstadoTramiteRA);
	}

	// Getters y setters
	
	public EstadoTramiteRA getEstTra() {
		return estTra;
	}

	public void setEstTra(EstadoTramiteRA estTra) {
		this.estTra = estTra;
	}

	public TramiteRA getTramiteRA() {
		return tramiteRA;
	}

	public void setTramiteRA(TramiteRA tramiteRA) {
		this.tramiteRA = tramiteRA;
	}

	public String getLogCambios() {
		return logCambios;
	}

	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}



	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();

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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (getTramiteRA() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RodError.TRAMITERA_LABEL);
		}

		if (getEstTra().isEstado() && getEstTra() == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RodError.ESTADOTRAMITE_LABEL);
		}

		if (getFecha() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, RodError.HISESTTRA_FECHA);
		}
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el HisEstTra. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		RodDAOFactory.getHisEstTraDAO().update(this);
	}

	/**
	 * Desactiva el HisEstTra. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		RodDAOFactory.getHisEstTraDAO().update(this);
	}
	
	/**
	 * Valida la activacion del HisEstTra
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del HisEstTra
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
