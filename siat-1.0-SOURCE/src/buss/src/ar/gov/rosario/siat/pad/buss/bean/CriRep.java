//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.pad.buss.dao.PadDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Criterio de Reparto
 * 
 * @author tecso
 */
@Entity
@Table(name = "pad_criRep")
public class CriRep extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "codCriRep")
	private String codCriRep;
	
	@Column(name = "desCriRep")
	private String desCriRep;
	
	// Constructores
	
	public CriRep(){
		super();
	}

	// Getters y Setters
	
	public String getCodCriRep() {
		return codCriRep;
	}
	public void setCodCriRep(String codCriRep) {
		this.codCriRep = codCriRep;
	}
	public String getDesCriRep() {
		return desCriRep;
	}
	public void setDesCriRep(String desCriRep) {
		this.desCriRep = desCriRep;
	}

	// Metodos de clase	
	public static CriRep getById(Long id) {
		return (CriRep) PadDAOFactory.getCriRepDAO().getById(id);
	}
	
	public static CriRep getByIdNull(Long id) {
		return (CriRep) PadDAOFactory.getCriRepDAO().getByIdNull(id);
	}
	
	public static List<CriRep> getList() {
		return (ArrayList<CriRep>) PadDAOFactory.getCriRepDAO().getList();
	}
	
	public static List<CriRep> getListActivos() {			
		return (ArrayList<CriRep>) PadDAOFactory.getCriRepDAO().getListActiva();
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
