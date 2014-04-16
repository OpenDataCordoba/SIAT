//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.bean.Partida;
import ar.gov.rosario.siat.bal.iface.util.BalError;
import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.esp.buss.dao.EspDAOFactory;
import ar.gov.rosario.siat.esp.iface.util.EspError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Bean corresponiente a los Valores Cargados. Para el Tipo de Habilitacion Interna donde no se relaciona la habilitacion
 * a una cuenta.
 * 
 * @author tecso
 */
@Entity
@Table(name = "esp_valoresCargados")
public class ValoresCargados extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@Column(name = "descripcion")
	private String descripcion;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idPartida") 
	private Partida partida;

	//Constructores 
	public ValoresCargados(){
		super();
	}

	// Getters Y Setters 

	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public Partida getPartida() {
		return partida;
	}
	public void setPartida(Partida partida) {
		this.partida = partida;
	}	
	
	// Metodos de clase	
	public static ValoresCargados getById(Long id) {
		return (ValoresCargados) EspDAOFactory.getValoresCargadosDAO().getById(id);
	}
	
	public static ValoresCargados getByIdNull(Long id) {
		return (ValoresCargados) EspDAOFactory.getValoresCargadosDAO().getByIdNull(id);
	}
	
	public static List<ValoresCargados> getList() {
		return (ArrayList<ValoresCargados>) EspDAOFactory.getValoresCargadosDAO().getList();
	}
	
	public static List<ValoresCargados> getListActivos() {			
		return (ArrayList<ValoresCargados>) EspDAOFactory.getValoresCargadosDAO().getListActiva();
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
		if(StringUtil.isNullOrEmpty(getDescripcion())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, EspError.VALORESCARGADOS_DESCRIPCION);
		}
		if(getPartida()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.PARTIDA_LABEL);
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
		
		if (GenericDAO.hasReference(this, Habilitacion.class, "valoresCargados")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
					EspError.VALORESCARGADOS_LABEL , EspError.HABILITACION_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}


}
