//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.dao.DefDAOFactory;
import ar.gov.rosario.siat.def.iface.util.DefError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Categoria
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_categoria")
public class Categoria extends BaseBO {
	private static final long serialVersionUID = 1L;
	
	public static final Long ID_CDM = 17L;    // Categoria Contribucion de Mejoras
	public static final Long ID_TGI = 1L;     // Categoria Tasa General de Inmuebles
	public static final Long ID_ESP_PUB = 4L; // Categoria Espectaculos Publicos
	public static final Long ID_DREI = 2L; // Categoria Drei
	public static final Long ID_ETUR = 16L; // Etur 
	
	@Column(name = "desCategoria") 
	private String desCategoria;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipo")
	private Tipo tipo;

	// Constructores	
	public Categoria(){
		super();
	}

	//	Getters y setters
	public String getDesCategoria() {
		return desCategoria;
	}
	public void setDesCategoria(String desCategoria) {
		this.desCategoria = desCategoria;
	}
	public Tipo getTipo() {
		return tipo;
	}
	public void setTipo(Tipo tipo) {
		this.tipo = tipo;
	}
	
	// Metodos de clase	
	public static Categoria getById(Long id) {
		return (Categoria) DefDAOFactory.getCategoriaDAO().getById(id);
	}
	
	public static Categoria getByIdNull(Long id) {
		return (Categoria) DefDAOFactory.getCategoriaDAO().getByIdNull(id);
	}
	
	public static List<Categoria> getList() {
		return (ArrayList<Categoria>) DefDAOFactory.getCategoriaDAO().getList();
	}
	
	public static List<Categoria> getListActivos() {			
		return (ArrayList<Categoria>) DefDAOFactory.getCategoriaDAO().getListActiva();
	}
	
	public static List<Categoria> getListActivosByIdTipo(Long id) {			
		return (List<Categoria>) DefDAOFactory.getCategoriaDAO().getListActivosByIdTipo(id);
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
		if (StringUtil.isNullOrEmpty(getDesCategoria())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CATEGORIA_DESCATEGORIA);
		}
		if(getTipo()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.CATEGORIA_TIPO);
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
		
		if(GenericDAO.hasReference(this, Recurso.class, "categoria")){
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, DefError.CATEGORIA_LABEL, DefError.RECURSO_LABEL);
		}
		if (hasError()) {
			return false;
		}
		return true;
	}

//	 Metodos de negocio

	/**
	 * Activa la Categoria. Previamente valida la activacion 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getCategoriaDAO().update(this);
	}

	/**
	 * Desactiva la Categoria. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getCategoriaDAO().update(this);
	}
	
	/**
	 * Valida la activacion de la Categoria
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion de la Categoria
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	public Boolean getEsCategoriaCdM(){
		return (Categoria.ID_CDM == this.getId());
 	}
}
