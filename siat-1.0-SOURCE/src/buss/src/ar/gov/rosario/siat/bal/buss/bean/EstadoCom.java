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
 * Bean correspondiente a Estados de las Compensaciones 
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_estadoCom")
public class EstadoCom extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final long ID_CREADO = 1L;
    public static final long ID_EN_FOLIO = 2L;
    public static final long ID_ASENTADO = 3L;
    public static final long ID_LISTA_PARA_FOLIO = 4L;
    public static final long ID_EN_BALANCE = 5L;
    
	@Column(name = "descripcion")
	private String descripcion;

	@Column(name = "tipo")
	private String tipo;

	@Column(name = "transiciones")
	private String transiciones;
	
	//Constructores 
	public EstadoCom(){
		super();
	}

	// Getters Y Setters
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getTipo() {
		return tipo;
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getTransiciones() {
		return transiciones;
	}
	public void setTransiciones(String transiciones) {
		this.transiciones = transiciones;
	}
	
	// Metodos de clase	
	public static EstadoCom getById(Long id) {
		return (EstadoCom) BalDAOFactory.getEstadoComDAO().getById(id);
	}
	
	public static EstadoCom getByIdNull(Long id) {
		return (EstadoCom) BalDAOFactory.getEstadoComDAO().getByIdNull(id);
	}
		
	public static List<EstadoCom> getList() {
		return (ArrayList<EstadoCom>) BalDAOFactory.getEstadoComDAO().getList();
	}
	
	public static List<EstadoCom> getListActivos() {			
		return (ArrayList<EstadoCom>) BalDAOFactory.getEstadoComDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesEstadoCom())){
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
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "estadoCom")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.COMDEU_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, EstadoComRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, EstadoComPla.class, "disPar")) {
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
