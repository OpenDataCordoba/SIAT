//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.ef.buss.dao.EfDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a OrigenOrden
 * 
 * @author tecso
 */
@Entity
@Table(name = "ef_origenOrden")
public class OrigenOrden extends BaseBO {
	
	private static final long serialVersionUID = 1L;

	public static final Long ID_OPERATIVOS = 1L;
	public static final Long ID_TIPO_PROC_JUDICIAL = 2L;
	public static final Long ID_TIPO_ORDEN_RELACIONADA = 3L;
	public static final Long ID_TIPO_CONTRBUYENTE=4L;
	public static final Long ID_TIPO_SOLICITADO_DFT=5L;
	public static final Long ID_TIPO_DGEF=6L;
	public static final Long ID_TIPO_CIERRE_NEGOCIO=7L;
	public static final Long ID_TIPO_DEVOLUCION=8L;
	public static final Long ID_TIPO_COMPENSACION=9L;
	
	public static final Long[]IDs_SELECCION_CONTRIBUYENTE={ID_TIPO_CONTRBUYENTE,ID_TIPO_SOLICITADO_DFT,ID_TIPO_DGEF,
		ID_TIPO_CIERRE_NEGOCIO,ID_TIPO_DEVOLUCION,ID_TIPO_COMPENSACION};
	
	@Column(name = "desOrigen")
	private String desOrigen;
	
	@Column(name="esProJud")
	private Integer esProJud;
	
	
	// Constructores
	public OrigenOrden(){
		super();
	}
	
	public OrigenOrden(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static OrigenOrden getById(Long id) {
		return (OrigenOrden) EfDAOFactory.getOrigenOrdenDAO().getById(id);
	}
	
	public static OrigenOrden getByIdNull(Long id) {
		return (OrigenOrden) EfDAOFactory.getOrigenOrdenDAO().getByIdNull(id);
	}
	
	public static List<OrigenOrden> getList() {
		return (List<OrigenOrden>) EfDAOFactory.getOrigenOrdenDAO().getList();
	}
	
	public static List<OrigenOrden> getListActivos() {			
		return (List<OrigenOrden>) EfDAOFactory.getOrigenOrdenDAO().getListActiva();
	}
	
	public static List<OrigenOrden> getListProJud() {
		return (List<OrigenOrden>) EfDAOFactory.getOrigenOrdenDAO().getListProJud();
	}
	
	public static List<OrigenOrden> getListNotOperativoActivos(){
		return EfDAOFactory.getOrigenOrdenDAO().getListNotOperativoActivos();
	}

	// Getters y setters
	public String getDesOrigen() {
		return desOrigen;
	}

	public void setDesOrigen(String desOrigenOrden) {
		this.desOrigen = desOrigenOrden;
	}
	
	
	public Integer getEsProJud() {
		return esProJud;
	}

	public void setEsProJud(Integer esProJud) {
		this.esProJud = esProJud;
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
		//limpiamos la lista de errores
		clearError();
	
		/*Ejemplo:
		if (GenericDAO.hasReference(this, BeanRelacionado .class, " bean ")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							EfError.ORIGENORDEN_LABEL, EfError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		if (hasError()) {
			return false;
		}
		
	
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el OrigenOrden. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		EfDAOFactory.getOrigenOrdenDAO().update(this);
	}

	/**
	 * Desactiva el OrigenOrden. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		EfDAOFactory.getOrigenOrdenDAO().update(this);
	}
	
	/**
	 * Valida la activacion del OrigenOrden
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del OrigenOrden
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
