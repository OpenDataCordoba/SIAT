//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Reingreso de Indeterminados incluido en Balance.
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_reingreso")
public class Reingreso extends BaseBO {

	private static final long serialVersionUID = 1L;
		
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idBalance") 
	private Balance balance;
	
	@Column(name = "nroReingreso")
	private Long nroReingreso;

	@Column(name = "importePago")
	private Double importePago;
	
	//Constructores 
	public Reingreso(){
		super();
	}

	// Getters Y Setters
	public Balance getBalance() {
		return balance;
	}
	public void setBalance(Balance balance) {
		this.balance = balance;
	}
	public Double getImportePago() {
		return importePago;
	}
	public void setImportePago(Double importePago) {
		this.importePago = importePago;
	}
	public Long getNroReingreso() {
		return nroReingreso;
	}
	public void setNroReingreso(Long nroReingreso) {
		this.nroReingreso = nroReingreso;
	}
	
	// Metodos de clase	
	public static Reingreso getById(Long id) {
		return (Reingreso) BalDAOFactory.getReingresoDAO().getById(id);
	}
	
	public static Reingreso getByIdNull(Long id) {
		return (Reingreso) BalDAOFactory.getReingresoDAO().getByIdNull(id);
	}
	
	public static Reingreso getByIdNroReingresoYBalance(Long nroReing, Balance balance) {
		return (Reingreso) BalDAOFactory.getReingresoDAO().getByIdNroReingresoYBalance(nroReing, balance);
	}
	
	
	public static List<Reingreso> getList() {
		return (ArrayList<Reingreso>) BalDAOFactory.getReingresoDAO().getList();
	}
	
	public static List<Reingreso> getListActivos() {			
		return (ArrayList<Reingreso>) BalDAOFactory.getReingresoDAO().getListActiva();
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
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

	
}
