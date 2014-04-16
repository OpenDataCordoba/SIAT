//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Gestion de Cobranza
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_gesCob")
public class GesCob extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCobranza")
	private Cobranza cobranza;
	
	@Column(name="fecha")
	private Date fecha;
	
	@Column(name="observacion")
	private String observacion;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEstadoCobranza")
	private EstadoCobranza estadoCobranza;
	
	@Column(name="fechaControl")
	private Date fechaControl;
	

	//<#Propiedades#>
	
	// Constructores
	public GesCob(){
		super();
	}
	
	
	// Metodos de Clase
	public static GesCob getById(Long id) {
		return (GesCob) GdeDAOFactory.getGesCobDAO().getById(id);
	}
	
	public static GesCob getByIdNull(Long id) {
		return (GesCob) GdeDAOFactory.getGesCobDAO().getByIdNull(id);
	}
	
	public static List<GesCob> getList() {
		return (List<GesCob>) GdeDAOFactory.getGesCobDAO().getList();
	}
	
	public static List<GesCob> getListActivos() {			
		return (List<GesCob>) GdeDAOFactory.getGesCobDAO().getListActiva();
	}
	
	
	// Getters y setters

	public Cobranza getCobranza() {
		return cobranza;
	}


	public void setCobranza(Cobranza cobranza) {
		this.cobranza = cobranza;
	}


	public Date getFecha() {
		return fecha;
	}


	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}


	public String getObservacion() {
		return observacion;
	}


	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}


	public EstadoCobranza getEstadoCobranza() {
		return estadoCobranza;
	}


	public void setEstadoCobranza(EstadoCobranza estadoCobranza) {
		this.estadoCobranza = estadoCobranza;
	}


	public Date getFechaControl() {
		return fechaControl;
	}


	public void setFechaControl(Date fechaControl) {
		this.fechaControl = fechaControl;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		/*/	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesEstadoConCuo())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.ESTADOCONCUO_DESESTADOCONCUO);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("codEstadoConCuo");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, GdeError.ESTADOCONCUO_CODESTADOCONCUO);			
		}*/
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el EstadoConCuo. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getEstadoAjusteDAO().update(this);
	}

	/**
	 * Desactiva el EstadoConCuo. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getEstadoAjusteDAO().update(this);
	}
	
	/**
	 * Valida la activacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del EstadoConCuo
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	//<#MetodosBeanDetalle#>
}
