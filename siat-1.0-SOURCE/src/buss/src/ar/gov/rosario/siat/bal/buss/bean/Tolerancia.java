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
import ar.gov.rosario.siat.def.buss.bean.ServicioBanco;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tolerancia
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tolerancia")
public class Tolerancia extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Double VALOR_FIJO = 0.5;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idServicioBanco") 
	private ServicioBanco servicioBanco;
	
	@Column(name = "toleranciaPartida")
	private Double toleranciaPartida;
	
	@Column(name = "toleranciaSaldo")
	private Double toleranciaSaldo;
	
	@Column(name = "toleranciaDifer")
	private Double toleranciaDifer;
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;
	
	@Column(name = "fechaHasta")
	private Date fechaHasta;
	
	//Constructores 
	public Tolerancia(){
		super();
	}

	// Getters y Setters
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
	public ServicioBanco getServicioBanco() {
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBanco servicioBanco) {
		this.servicioBanco = servicioBanco;
	}
	public Double getToleranciaDifer() {
		return toleranciaDifer;
	}
	public void setToleranciaDifer(Double toleranciaDifer) {
		this.toleranciaDifer = toleranciaDifer;
	}
	public Double getToleranciaPartida() {
		return toleranciaPartida;
	}
	public void setToleranciaPartida(Double toleranciaPartida) {
		this.toleranciaPartida = toleranciaPartida;
	}
	public Double getToleranciaSaldo() {
		return toleranciaSaldo;
	}
	public void setToleranciaSaldo(Double toleranciaSaldo) {
		this.toleranciaSaldo = toleranciaSaldo;
	}
	
	// Metodos de clase	
	public static Tolerancia getById(Long id) {
		return (Tolerancia) BalDAOFactory.getToleranciaDAO().getById(id);
	}
	
	public static Tolerancia getByIdNull(Long id) {
		return (Tolerancia) BalDAOFactory.getToleranciaDAO().getByIdNull(id);
	}

	public static Tolerancia getBySerBanYFecha(ServicioBanco servicioBanco, Date fecha) throws Exception{
		return (Tolerancia) BalDAOFactory.getToleranciaDAO().getBySerBanYFecha(servicioBanco, fecha);
	}

	public static List<Tolerancia> getList() {
		return (ArrayList<Tolerancia>) BalDAOFactory.getToleranciaDAO().getList();
	}
	
	public static List<Tolerancia> getListActivos() {			
		return (ArrayList<Tolerancia>) BalDAOFactory.getToleranciaDAO().getListActiva();
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
		//Validaciones de Negocio
				
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
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	

}
