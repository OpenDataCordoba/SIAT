//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.seg.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.seg.buss.dao.SegDAOFactory;
import ar.gov.rosario.siat.seg.iface.util.SegError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.buss.dao.UniqueMap;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Oficina de trabajo
 * 
 * @author tecso
 */
@Entity
@Table(name = "seg_oficina")
public class Oficina extends BaseBO {
	
	private static final long serialVersionUID = 1L;
		
	@Column(name = "desOficina")
	private String desOficina;
	
	@ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="idArea") 
	private Area area;
	
	// Constructores
	public Oficina(){
		super();
	}
	
	
	public Oficina(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static Oficina getById(Long id) {
		return (Oficina) SegDAOFactory.getOficinaDAO().getById(id);
	}
	
	public static Oficina getByIdNull(Long id) {
		return (Oficina) SegDAOFactory.getOficinaDAO().getByIdNull(id);
	}
	
	public static List<Oficina> getList() {
		return (List<Oficina>) SegDAOFactory.getOficinaDAO().getList();
	}
	
	public static List<Oficina> getListActivos() {			
		return (List<Oficina>) SegDAOFactory.getOficinaDAO().getListActiva();
	}
	
	
	// Getters y setters
	public String getDesOficina() {
		return desOficina;
	}

	public void setDesOficina(String desOficina) {
		this.desOficina = desOficina;
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
							SegError.Oficina_LABEL, SegError. BEAN_RELACIONADO _LABEL );
		}*/
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		if (StringUtil.isNullOrEmpty(getDesOficina())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, SegError.OFICINA_DESOFICINA);
		}
		
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
		UniqueMap uniqueMap = new UniqueMap();
		uniqueMap.addString("desOficina");
		if(!GenericDAO.checkIsUnique(this, uniqueMap)) {
			addRecoverableError(BaseError.MSG_CAMPO_UNICO, SegError.OFICINA_DESOFICINA);
		}
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Oficina. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		SegDAOFactory.getOficinaDAO().update(this);
	}

	/**
	 * Desactiva el Oficina. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		SegDAOFactory.getOficinaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Oficina
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Oficina
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}
	
	public String getDesOficinaReport() {
		return desOficina + " (" + getEstadoView() + ")";
	}

}
