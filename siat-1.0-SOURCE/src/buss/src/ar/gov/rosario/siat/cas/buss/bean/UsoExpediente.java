//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import ar.gov.rosario.siat.cas.buss.dao.CasDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a UsoExpediente
 * 
 * @author tecso
 */
@Entity
@Table(name = "cas_usoExpediente")

public class UsoExpediente extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "numero")
	private String numero;
	
	@Column(name = "idCaso")
	private String idCaso;
	
	@Column(name = "fechaAccion")
	private Date fechaAccion;

	@Column(name = "descripcion")
	private String descripcion;

	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idSistemaOrigen") 
	private SistemaOrigen sistemaOrigen;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idAccionExp") 
	private AccionExp accionExp;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	// Constructores
	public UsoExpediente(){
		super();
		// Seteo de valores default			
	}
	
	public UsoExpediente(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static UsoExpediente getById(Long id) {
		return (UsoExpediente) CasDAOFactory.getUsoExpedienteDAO().getById(id);
	}
	
	public static UsoExpediente getByIdNull(Long id) {
		return (UsoExpediente) CasDAOFactory.getUsoExpedienteDAO().getByIdNull(id);
	}
	
	public static List<UsoExpediente> getList() {
		return (List<UsoExpediente>) CasDAOFactory.getUsoExpedienteDAO().getList();
	}
	
	public static List<UsoExpediente> getListActivos() {			
		return (List<UsoExpediente>) CasDAOFactory.getUsoExpedienteDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getNumero(){
		return this.numero;
	}
	public void setNumero(String numero){
		this.numero = numero;
	}
	
	public String getIdCaso() {
		return idCaso;
	}
	public void setIdCaso(String idCaso) {
		this.idCaso = idCaso;
	}
	
	public Date getFechaAccion(){
		return this.fechaAccion;
	}
	public void setFechaAccion(Date fechaAccion){
		this.fechaAccion = fechaAccion;
	}

	public String getDescripcion(){
		return this.descripcion;
	}
	public void setDescripcion(String desc) {
		this.descripcion = desc;
	}

	public SistemaOrigen getSistemaOrigen(){
		return this.sistemaOrigen;
	}
	public void setSistemaOrigen(SistemaOrigen so){
		this.sistemaOrigen = so;
	}
	
	public AccionExp getAccionExp(){
		return this.accionExp;
	}
	public void setAccionExp(AccionExp ae){
		this.accionExp = ae;
	}
	
	public Cuenta getCuenta(){
		return this.cuenta;
	}
	public void setCuenta(Cuenta cuenta){
		this.cuenta = cuenta;
	}
	@Transient
	public String getDescReport(){
		return this.getSistemaOrigen().getDesSistemaOrigen()+"-"+this.getNumero();
		//.toString()+"/"+this.getAnio().toString()
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
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el UsoExpediente. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CasDAOFactory.getUsoExpedienteDAO().update(this);
	}

	/**
	 * Desactiva el UsoExpediente. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CasDAOFactory.getUsoExpedienteDAO().update(this);
	}
	
	/**
	 * Valida la activacion del UsoExpediente
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del UsoExpediente
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
