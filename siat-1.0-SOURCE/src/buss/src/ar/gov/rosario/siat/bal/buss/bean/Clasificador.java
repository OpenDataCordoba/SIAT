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
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Representa los Clasificadores para los reportes de Balance.
 * <p>
 * Clasificadores existentes actualmente:
 * </p>
 * <p>- Económico</p>
 * <p>- Procedencia</p>
 * <p>- Rubro</p>
 *
 * @author tecso
 */
@Entity
@Table(name = "bal_clasificador")
public class Clasificador extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final Long ID_CLA_RUBRO = 1L;
	public static final Long ID_CLA_PROC = 3L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "cantNivel")
	private Integer cantNivel;
	
	// Constructores
	public Clasificador(){
		super();
	}

	// Getters y Setters
	public Integer getCantNivel() {
		return cantNivel;
	}
	public void setCantNivel(Integer cantNivel) {
		this.cantNivel = cantNivel;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
	// Metodos de Clase
	public static Clasificador getById(Long id) {
		return (Clasificador) BalDAOFactory.getClasificadorDAO().getById(id);
	}
	
	public static Clasificador getByIdNull(Long id) {
		return (Clasificador) BalDAOFactory.getClasificadorDAO().getByIdNull(id);
	}
	
	public static List<Clasificador> getList() {
		return (ArrayList<Clasificador>) BalDAOFactory.getClasificadorDAO().getList();
	}
	
	public static List<Clasificador> getListActivos() {			
		return (ArrayList<Clasificador>) BalDAOFactory.getClasificadorDAO().getListActiva();
	}
	
	public static List<Clasificador> getListActivosExcluyendoId(Long idClasificador) {			
		return (ArrayList<Clasificador>) BalDAOFactory.getClasificadorDAO().getListActivosExcluyendoId(idClasificador);
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
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.CLASIFICADOR_DESCRIPCION);
		}
				
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
		
		if (GenericDAO.hasReference(this, Nodo.class, "clasificador")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					BalError.CLASIFICADOR_LABEL , BalError.NODO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
}
