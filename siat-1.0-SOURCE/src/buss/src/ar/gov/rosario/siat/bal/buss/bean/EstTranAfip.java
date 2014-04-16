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
 * Bean correspondiente a estado de transacciones de Osiris 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estTranAfip")
public class EstTranAfip extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_PENDIENTE=1;
	public static final long ID_PROCESADO_OK=2;
	public static final long ID_PROCESADO_ERROR=3;
	public static final long ID_ANULADA=4;
	public static final long ID_CON_ERROR=5;
	public static final long ID_PROCESADO_PARCIAL=2;

	@Column(name="descripcion")
	private String descripcion;
		
	// Constructores
	public EstTranAfip(){
		super();
	}
	
	//Metodos de clase
	public static EstTranAfip getById(Long id) {
		return (EstTranAfip) BalDAOFactory.getEstTranAfipDAO().getById(id);  
	}
	
	public static EstTranAfip getByIdNull(Long id) {
		return (EstTranAfip) BalDAOFactory.getEstTranAfipDAO().getByIdNull(id);
	}
	
	public static List<EstTranAfip> getList() {
		return (List<EstTranAfip>) BalDAOFactory.getEstTranAfipDAO().getList();
	}
	
	public static List<EstTranAfip> getListActivos() {			
		return (List<EstTranAfip>) BalDAOFactory.getEstTranAfipDAO().getListActiva();
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
		clearError();

		if (!this.validate()) {
			return false;
		}

		// Validaciones de Negocio

		return true;
	}

	public boolean validateDelete() {
		// limpiamos la lista de errores
		clearError();

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
}
