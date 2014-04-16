//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Tipo de Origenes de Movimientos (Origenes para DiarioPartida)
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipOriMov")
public class TipOriMov extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final long ID_ASENTAMIENTO = 1L;
	public static final long ID_AJUSTES = 2L;
	public static final long ID_INGRESO_NO_TRIBUTARIO = 3L;
	public static final long ID_OTRO = 4L;
	public static final long ID_ASEDEL = 5L;
    
	@Column(name = "descripcion")
	private String descripcion;
	
	//Constructores 
	public TipOriMov(){
		super();
	}

	// Getters y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	// Metodos de clase	
	public static TipOriMov getById(Long id) {
		return (TipOriMov) BalDAOFactory.getTipOriMovDAO().getById(id);
	}
	
	public static TipOriMov getByIdNull(Long id) {
		return (TipOriMov) BalDAOFactory.getTipOriMovDAO().getByIdNull(id);
	}
	
	public static List<TipOriMov> getList() {
		return (ArrayList<TipOriMov>) BalDAOFactory.getTipOriMovDAO().getList();
	}
	
	public static List<TipOriMov> getListActivos() {			
		return (ArrayList<TipOriMov>) BalDAOFactory.getTipOriMovDAO().getListActiva();
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
		
		/*if (GenericDAO.hasReference(this, DiarioPartida.class, "tipOriMov")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.TIPORIMOV_LABEL , BalError.DIARIOPARTIDA_LABEL);
		}
		 */
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
