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
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a RecursoArea
 * 
 * @author tecso
 */
@Entity
@Table(name = "def_recursoarea")
public class RecursoArea extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY)  
    @JoinColumn(name="idArea")
	private Area area;
	
	@ManyToOne(fetch=FetchType.LAZY)  
    @JoinColumn(name="idRecurso")
	private Recurso recurso;	
	
	@Column(name="perCreaEmi")
	private Integer perCreaEmi; // Permite Creacion de Cuenta y Emision de Deuda.

	
	// Constructores
	public RecursoArea(){
		super();
		// Seteo de valores default			
	}
	
	public RecursoArea(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static RecursoArea getById(Long id) {
		return (RecursoArea) DefDAOFactory.getRecursoAreaDAO().getById(id);
	}
	
	public static RecursoArea getByIdNull(Long id) {
		return (RecursoArea) DefDAOFactory.getRecursoAreaDAO().getByIdNull(id);
	}
	
	public static RecursoArea getByRecursoArea(Long idRecurso, Long idArea) throws Exception {
		return (RecursoArea) DefDAOFactory.getRecursoAreaDAO().getByRecursoArea(idRecurso, idArea);
	}
	
	public static List<RecursoArea> getList() {
		return (ArrayList<RecursoArea>) DefDAOFactory.getRecursoAreaDAO().getList();
	}
	
	public static List<RecursoArea> getListActivos() {			
		return (ArrayList<RecursoArea>) DefDAOFactory.getRecursoAreaDAO().getListActiva();
	}
	
	public static List<RecursoArea> getListByAreaActivos(Long idArea) throws Exception {			
		return (ArrayList<RecursoArea>) DefDAOFactory.getRecursoAreaDAO().getListByAreaActivos(idArea);
	}
	
	// Getters y setters
	public Area getArea() {
		return area;
	}
	public void setArea(Area area) {
		this.area = area;
	}

	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public Integer getPerCreaEmi() {
		return perCreaEmi;
	}
	public void setPerCreaEmi(Integer perCreaEmi) {
		this.perCreaEmi = perCreaEmi;
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
	
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (area == null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.AREA_LABEL);
		}
		if (recurso == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		if (perCreaEmi == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSOAREA_PERCREAEMI);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addEntity("recurso");
		uniqueMap.addEntity("area");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(DefError.MSG_RECURSOAREA_UNIQUE);			
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el RecursoArea. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		DefDAOFactory.getRecursoAreaDAO().update(this);
	}

	/**
	 * Desactiva el RecursoArea. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		DefDAOFactory.getRecursoAreaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del RecursoArea
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del RecursoArea
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

}
//<template>
	