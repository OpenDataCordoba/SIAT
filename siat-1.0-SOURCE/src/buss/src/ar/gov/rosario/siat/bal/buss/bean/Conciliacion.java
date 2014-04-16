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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Datos de Proceso de Conciliación diario incluidos en el Envio Osiris.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_conciliacion")
public class Conciliacion extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="idEnvioOsiris") 
	private EnvioOsiris envioOsiris;

	@Column(name = "idEnvioAfip")
	private Long idEnvioAfip;

	@Column(name = "banco")
	private Long banco;
	
	@Column(name = "nroCierreBanco")
	private Long nroCierreBanco;
	
	@Column(name = "fechaConciliacion")
	private Date fechaConciliacion;
		
	@Column(name = "cantNota")
	private Long cantNota;
	
	@Column(name = "totalConciliado")
	private Double totalConciliado;
	
	@OneToMany(mappedBy="conciliacion",fetch=FetchType.LAZY)
	@JoinColumn(name="idConciliacion")
	private List<NotaConc> listNotaConc;
	
	// Constructores
	public Conciliacion(){
		super();		
	}
	
	public Conciliacion(Long id){
		super();
		setId(id);
	}

	// Getters Y Setters
	public Long getBanco() {
		return banco;
	}

	public void setBanco(Long banco) {
		this.banco = banco;
	}

	public Long getCantNota() {
		return cantNota;
	}

	public void setCantNota(Long cantNota) {
		this.cantNota = cantNota;
	}

	public EnvioOsiris getEnvioOsiris() {
		return envioOsiris;
	}

	public void setEnvioOsiris(EnvioOsiris envioOsiris) {
		this.envioOsiris = envioOsiris;
	}

	public Date getFechaConciliacion() {
		return fechaConciliacion;
	}

	public void setFechaConciliacion(Date fechaConciliacion) {
		this.fechaConciliacion = fechaConciliacion;
	}

	public Long getIdEnvioAfip() {
		return idEnvioAfip;
	}

	public void setIdEnvioAfip(Long idEnvioAfip) {
		this.idEnvioAfip = idEnvioAfip;
	}

	public Long getNroCierreBanco() {
		return nroCierreBanco;
	}

	public void setNroCierreBanco(Long nroCierreBanco) {
		this.nroCierreBanco = nroCierreBanco;
	}

	public Double getTotalConciliado() {
		return totalConciliado;
	}

	public void setTotalConciliado(Double totalConciliado) {
		this.totalConciliado = totalConciliado;
	}
	
	public List<NotaConc> getListNotaConc() {
		return listNotaConc;
	}

	public void setListNotaConc(List<NotaConc> listNotaConc) {
		this.listNotaConc = listNotaConc;
	}

	// Metodos de Clase
	public static Conciliacion getById(Long id) {
		return (Conciliacion) BalDAOFactory.getConciliacionDAO().getById(id);
	}
	
	public static Conciliacion getByIdNull(Long id) {
		return (Conciliacion) BalDAOFactory.getConciliacionDAO().getByIdNull(id);
	}
	
	public static List<Conciliacion> getList() {
		return (ArrayList<Conciliacion>) BalDAOFactory.getConciliacionDAO().getList();
	}
	
	public static List<Conciliacion> getListActivos() {			
		return (ArrayList<Conciliacion>) BalDAOFactory.getConciliacionDAO().getListActiva();
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
	 * Activa el Conciliacion. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		BalDAOFactory.getConciliacionDAO().update(this);
	}

	/**
	 * Desactiva el Conciliacion. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		BalDAOFactory.getConciliacionDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Conciliacion
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Conciliacion
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
}
