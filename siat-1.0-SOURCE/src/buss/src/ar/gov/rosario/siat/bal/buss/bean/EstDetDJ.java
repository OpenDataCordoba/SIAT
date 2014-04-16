//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;


import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a  transacciones de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estDetDJ")
public class EstDetDJ extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_PENDIENTE=1;
	public static final long ID_PROCESADO_OK=2;
	public static final long ID_PROCESADO_ERROR=3;

	
	@Column(name="descripcion")
	private String descripcion;
	
	// Constructores
	public EstDetDJ(){
		super();
	}
		
	//Metodos de clase
	public static EstDetDJ getById(Long id) {
		return (EstDetDJ) BalDAOFactory.getEstDetDJDAO().getById(id);  
	}
	
	public static EstDetDJ getByIdNull(Long id) {
		return (EstDetDJ) BalDAOFactory.getEstDetDJDAO().getByIdNull(id);
	}
	
	public static List<EstDetDJ> getList() {
		return (List<EstDetDJ>) BalDAOFactory.getEstDetDJDAO().getList();
	}
	
	public static List<EstDetDJ> getListActivos() {			
		return (List<EstDetDJ>) BalDAOFactory.getEstDetDJDAO().getListActiva();
	}

	
	//Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}


	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
		//clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

		// <#ValidateDelete#>

		if (hasError()) {
			return false;
		}

		return true;
	}

	private boolean validate() throws Exception {

		// Validaciones

		if (hasError()) {
			return false;
		}

		return true;
	}

	// Metodos de Instancia


	
	
}
