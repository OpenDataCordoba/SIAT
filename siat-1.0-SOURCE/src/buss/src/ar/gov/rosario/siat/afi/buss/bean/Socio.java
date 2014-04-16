//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.afi.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.afi.buss.dao.AfiDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Socio - Contiene los datos de los socios 
 * y firmantes registrados en Formulario de Declaración Jurada proveniente de AFIP.
 * 
 * @author tecso
 */
@Entity
@Table(name = "afi_socio")
public class Socio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch = FetchType.LAZY, optional = true)
	@JoinColumn(name="idForDecJur")
	private ForDecJur forDecJur;
	
	@Column(name = "solofirmante")
	private Integer soloFirmante;
	
	@Column(name = "idpersona")
	private Long idPersona;
	
	@Column(name = "apellido")
	private String apellido;
	
	@Column(name = "apellidomaterno")
	private String apellidoMaterno;
	
	@Column(name = "nombre")
	private String nombre;
	  
	@Column(name = "encaracterde")
	private Integer enCaracterDe;
	
	@Column(name = "tipodocumento")	  
	private Integer tipoDocumento;
	   
	@Column(name = "nrodocumento")   
	private String nroDocumento;
	  
	@Column(name = "cuit")
	private String cuit;		
	
	@ManyToOne(fetch = FetchType.LAZY, optional= true)
	@JoinColumn(name="iddatosdomicilio")
	private DatosDomicilio datosDomicilio;
	
	
	// Constructores
	public Socio(){
		super();
		// Seteo de valores default			
	}
	
	public Socio(Long id){
		super();
		setId(id);
	}
	
	public void setForDecJur(ForDecJur forDecJur) {
		this.forDecJur = forDecJur;
	}

	public ForDecJur getForDecJur() {
		return forDecJur;
	}

	// Getters y Setters
	public Integer getSoloFirmante() {
		return soloFirmante;
	}

	public void setSoloFirmante(Integer soloFirmante) {
		this.soloFirmante = soloFirmante;
	}

	public Long getIdPersona() {
		return idPersona;
	}

	public void setIdPersona(Long idPersona) {
		this.idPersona = idPersona;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getApellidoMaterno() {
		return apellidoMaterno;
	}

	public void setApellidoMaterno(String apellidoMaterno) {
		this.apellidoMaterno = apellidoMaterno;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Integer getEnCaracterDe() {
		return enCaracterDe;
	}

	public void setEnCaracterDe(Integer enCaracterDe) {
		this.enCaracterDe = enCaracterDe;
	}

	public Integer getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(Integer tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public String getNroDocumento() {
		return nroDocumento;
	}

	public void setNroDocumento(String nroDocumento) {
		this.nroDocumento = nroDocumento;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

	public void setDatosDomicilio(DatosDomicilio datosDomicilio) {
		this.datosDomicilio = datosDomicilio;
	}

	public DatosDomicilio getDatosDomicilio() {
		return datosDomicilio;
	}

	// Metodos de Clase
	public static Socio getById(Long id) {
		return (Socio) AfiDAOFactory.getSocioDAO().getById(id);
	}
	
	public static Socio getByIdNull(Long id) {
		return (Socio) AfiDAOFactory.getSocioDAO().getByIdNull(id);
	}
	
	public static List<Socio> getList() {
		return (ArrayList<Socio>) AfiDAOFactory.getSocioDAO().getList();
	}
	
	public static List<Socio> getListActivos() {			
		return (ArrayList<Socio>) AfiDAOFactory.getSocioDAO().getListActiva();
	}
	
	
	// Getters y setters
	
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
		
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Socio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		AfiDAOFactory.getSocioDAO().update(this);
	}

	/**
	 * Desactiva el Socio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		AfiDAOFactory.getSocioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Socio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Socio
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	
}

	