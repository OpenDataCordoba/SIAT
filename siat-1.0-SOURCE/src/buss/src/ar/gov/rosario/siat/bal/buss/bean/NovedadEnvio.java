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
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Novedades relacionadas con transacciones previamente informadas por el banco.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_novedadEnvio")
public class NovedadEnvio extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idCierreBanco") 
	private CierreBanco cierreBanco;

	@Column(name = "idTransaccionAfip")
	private Long idTransaccionAfip;

	@Column(name = "formaPago")
	private Integer formaPago;

	@Column(name = "sucursal")
	private Long sucursal;
	
	@Column(name = "tipoSucursal")
	private Integer tipoSucursal;
	
	@Column(name = "tipoOperacion")
	private Integer tipoOperacion;
		
	@Column(name = "aceptada")
	private Integer aceptada;
	
	@Column(name = "deOficio")
	private Integer deOficio;
	
	@Column(name = "fechaNovedad")
	private Date fechaNovedad;

	@Column(name = "fechaRegistro")
	private Date fechaRegistro;

	// Constructores
	public NovedadEnvio(){
		super();		
	}
	
	public NovedadEnvio(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public Integer getAceptada() {
		return aceptada;
	}

	public void setAceptada(Integer aceptada) {
		this.aceptada = aceptada;
	}

	public CierreBanco getCierreBanco() {
		return cierreBanco;
	}

	public void setCierreBanco(CierreBanco cierreBanco) {
		this.cierreBanco = cierreBanco;
	}

	public Integer getDeOficio() {
		return deOficio;
	}

	public void setDeOficio(Integer deOficio) {
		this.deOficio = deOficio;
	}

	public Date getFechaNovedad() {
		return fechaNovedad;
	}

	public void setFechaNovedad(Date fechaNovedad) {
		this.fechaNovedad = fechaNovedad;
	}

	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public Integer getFormaPago() {
		return formaPago;
	}

	public void setFormaPago(Integer formaPago) {
		this.formaPago = formaPago;
	}

	public Long getSucursal() {
		return sucursal;
	}

	public void setSucursal(Long sucursal) {
		this.sucursal = sucursal;
	}

	public Integer getTipoOperacion() {
		return tipoOperacion;
	}

	public void setTipoOperacion(Integer tipoOperacion) {
		this.tipoOperacion = tipoOperacion;
	}

	public Integer getTipoSucursal() {
		return tipoSucursal;
	}

	public void setTipoSucursal(Integer tipoSucursal) {
		this.tipoSucursal = tipoSucursal;
	}
	
	public Long getIdTransaccionAfip() {
		return idTransaccionAfip;
	}

	public void setIdTransaccionAfip(Long idTransaccionAfip) {
		this.idTransaccionAfip = idTransaccionAfip;
	}

	// Metodos de Clase
	public static NovedadEnvio getById(Long id) {
		return (NovedadEnvio) BalDAOFactory.getNovedadEnvioDAO().getById(id);
	}
	
	public static NovedadEnvio getByIdNull(Long id) {
		return (NovedadEnvio) BalDAOFactory.getNovedadEnvioDAO().getByIdNull(id);
	}
	
	public static List<NovedadEnvio> getList() {
		return (ArrayList<NovedadEnvio>) BalDAOFactory.getNovedadEnvioDAO().getList();
	}
	
	public static List<NovedadEnvio> getListActivos() {			
		return (ArrayList<NovedadEnvio>) BalDAOFactory.getNovedadEnvioDAO().getListActiva();
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
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el NovedadEnvio. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getNovedadEnvioDAO().update(this);
	}

	/**
	 * Desactiva el NovedadEnvio. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getNovedadEnvioDAO().update(this);
	}
	
	/**
	 * Valida la activacion del NovedadEnvio
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del NovedadEnvio
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
}
