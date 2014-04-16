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
 * Bean correspondiente al Estado de los Archivos de Transacciones
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estadoArc")
public class EstadoArc extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final Long ID_RECIBIDO = 1L;
	public static final Long ID_ACEPTADO = 2L;
	public static final Long ID_ANULADO = 3L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	//Constructores 
	public EstadoArc(){
		super();
	}

	// Getters Y Setters 
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	
	// Metodos de clase	
	public static EstadoArc getById(Long id) {
		return (EstadoArc) BalDAOFactory.getEstadoArcDAO().getById(id);
	}
	
	public static EstadoArc getByIdNull(Long id) {
		return (EstadoArc) BalDAOFactory.getEstadoArcDAO().getByIdNull(id);
	}
		
	public static List<EstadoArc> getList() {
		return (ArrayList<EstadoArc>) BalDAOFactory.getEstadoArcDAO().getList();
	}
	
	public static List<EstadoArc> getListActivos() {			
		return (ArrayList<EstadoArc>) BalDAOFactory.getEstadoArcDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesEstadoArc())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_DESDISPAR);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.DISPAR_RECURSO);
		}*/
		
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
		
		/*if (GenericDAO.hasReference(this, EstadoArcDet.class, "disPar")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.DISPAR_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, EstadoArcRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, EstadoArcPla.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARPLA_LABEL);
		}*/
		
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}

}
