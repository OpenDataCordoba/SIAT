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
 * Representa los tipos de compensaciones sobre deudas o cuotas
 * 
 * @author tecso
 */
@Entity
@Table(name = "bal_tipoComDeu")
public class TipoComDeu extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final long ID_CANCELACION_TOTAL = 1L;
    public static final long ID_PAGO_PARCIAL = 2L;
    public static final long ID_CANCELACION_POR_MENOS = 3L;
   
	@Column(name = "descripcion")
	private String descripcion;

	//Constructores 
	public TipoComDeu(){
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
	public static TipoComDeu getById(Long id) {
		return (TipoComDeu) BalDAOFactory.getTipoComDeuDAO().getById(id);
	}
	
	public static TipoComDeu getByIdNull(Long id) {
		return (TipoComDeu) BalDAOFactory.getTipoComDeuDAO().getByIdNull(id);
	}
		
	public static List<TipoComDeu> getList() {
		return (ArrayList<TipoComDeu>) BalDAOFactory.getTipoComDeuDAO().getList();
	}
	
	public static List<TipoComDeu> getListActivos() {			
		return (ArrayList<TipoComDeu>) BalDAOFactory.getTipoComDeuDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesTipoComDeu())){
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
		
		/*if (GenericDAO.hasReference(this, TranArc.class, "tipoComDeu")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.COMDEU_LABEL , BalError.DISPARDET_LABEL);
		}

		if (GenericDAO.hasReference(this, TipoComDeuRec.class, "disPar")) {
		addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
			BalError.DISPAR_LABEL , BalError.DISPARREC_LABEL);
		}
		
		if (GenericDAO.hasReference(this, TipoComDeuPla.class, "disPar")) {
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
