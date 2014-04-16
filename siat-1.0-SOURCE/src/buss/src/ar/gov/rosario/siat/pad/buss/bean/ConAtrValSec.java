//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.def.buss.bean.ConAtr;
import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a ConAtrValSec - 
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_conAtrValSec")
public class ConAtrValSec extends BaseBO {
	
private static final long serialVersionUID = 1L;
	
@ManyToOne(fetch=FetchType.LAZY) 
@JoinColumn(name="idConAtr") 
private ConAtr conAtr;

@Column(name = "valorDesde")
private Integer valorDesde;

@Column(name = "valorHasta")
private Integer valorHasta;

@Column(name = "sector")
private String sector;



	//<#Propiedades#>
	
	// Constructores
	public ConAtrValSec(){
		super();
		// Seteo de valores default			
	}
	
	public ConAtrValSec(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static ConAtrValSec getById(Long id) {
		return (ConAtrValSec) PadDAOFactory.getConAtrValSecDAO().getById(id);
	}
	
	public static ConAtrValSec getByIdNull(Long id) {
		return (ConAtrValSec) PadDAOFactory.getConAtrValSecDAO().getByIdNull(id);
	}
	
	public static List<ConAtrValSec> getList() {
		return (ArrayList<ConAtrValSec>) PadDAOFactory.getConAtrValSecDAO().getList();
	}
	
	public static List<ConAtrValSec> getListActivos() {			
		return (ArrayList<ConAtrValSec>) PadDAOFactory.getConAtrValSecDAO().getListActiva();
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	public boolean validate() throws Exception {
		return true;
	}
	
	// Metodos de negocio


	public ConAtr getConAtr() {
		return conAtr;
	}

	public void setConAtr(ConAtr conAtr) {
		this.conAtr = conAtr;
	}

	public Integer getValorDesde() {
		return valorDesde;
	}

	public void setValorDesde(Integer valorDesde) {
		this.valorDesde = valorDesde;
	}

	public Integer getValorHasta() {
		return valorHasta;
	}

	public void setValorHasta(Integer valorHasta) {
		this.valorHasta = valorHasta;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	/**
	 * Activa el ConAtrValSec. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		PadDAOFactory.getConAtrValSecDAO().update(this);
	}

	/**
	 * Desactiva el ConAtrValSec. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		PadDAOFactory.getConAtrValSecDAO().update(this);
	}
	
	/**
	 * Valida la activacion del ConAtrValSec
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del ConAtrValSec
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
