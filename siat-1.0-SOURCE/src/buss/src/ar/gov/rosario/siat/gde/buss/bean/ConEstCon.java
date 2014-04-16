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
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a ConEstCon
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_conestcon")
public class ConEstCon extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstadoConvenio") 
	private EstadoConvenio estadoConvenio;
	
	@Column(name = "fechaConEstCon")
	private Date fechaConEstCon;
	
    @Column(name="idCaso") 
	private String idCaso;

	@Column(name = "observacion")
	private String observacion;

	// Constructores
	public ConEstCon(){
		super();
	}
	
	public ConEstCon(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ConEstCon getById(Long id) {
		return (ConEstCon) GdeDAOFactory.getConEstConDAO().getById(id);
	}
	
	public static ConEstCon getByIdNull(Long id) {
		return (ConEstCon) GdeDAOFactory.getConEstConDAO().getByIdNull(id);
	}
	
	public static List<ConEstCon> getList() {
		return (List<ConEstCon>) GdeDAOFactory.getConEstConDAO().getList();
	}
	
	public static List<ConEstCon> getListActivos() {			
		return (List<ConEstCon>) GdeDAOFactory.getConEstConDAO().getListActiva();
	}
	
	
	// Getters y setters
	public Convenio getConvenio(){
		return convenio;
	}
	public void setConvenio(Convenio convenio){
		this.convenio = convenio;		
	}
	 
	public EstadoConvenio getEstadoConvenio(){
		return estadoConvenio;
	}
	public void setEstadoConvenio(EstadoConvenio estadoConvenio){
		this.estadoConvenio = estadoConvenio;		
	}
	
	public Date getFechaConEstCon(){
		return fechaConEstCon;
	}
	public void setFechaConEstCon(Date fechaConEstCon){
		this.fechaConEstCon = fechaConEstCon;
	}
	 
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public String getObservacion(){
		return observacion;
	}
	public void setObservacion(String obs){
		this.observacion = obs;
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
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el ConEstCon. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getConEstConDAO().update(this);
	}

	/**
	 * Desactiva el ConEstCon. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getConEstConDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ConEstCon
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ConEstCon
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	@Override
	public String infoString() {
		String ret= "Historico de estado de Convenios";
		
		if(convenio!=null){
			ret+=" - Convenio: "+convenio.getNroConvenio();
			
			if(convenio.getCuenta() != null){
				ret+=" - Cuenta: " + convenio.getCuenta().getNumeroCuenta();
			}
		}
		
		if(estadoConvenio!=null){
			ret+=" - Estado Convenio: "+estadoConvenio.getDesEstadoConvenio();
		}
		
		if(fechaConEstCon!=null){
			ret+=" - Fecha: "+DateUtil.formatDate(fechaConEstCon, DateUtil.ddSMMSYYYY_MASK);
		}
		
		if(observacion!=null){
			ret+=" - Observacion: " + observacion;
		}
		
		if(idCaso!=null){
			ret+=" - Caso: "+idCaso;
		}
		return ret;
	}
	
}
