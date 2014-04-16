//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.buss.dao.GenericDAO;
import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Juzgados
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_juzgado")
public class Juzgado extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "desJuzgado")
	private String desJuzgado;

	@OneToMany()
	@JoinColumn(name="idJuzgado")
	private List<Abogado> listAbogado;

	// Constructores
	public Juzgado(){
		super();
	}
	// Getters Y Setters
	
	public String getDesJuzgado() {
		return desJuzgado;
	}
	public void setDesJuzgado(String desJuzgado) {
		this.desJuzgado = desJuzgado;
	}

	// Metodos de Clase
	public static Juzgado getById(Long id) {
		return (Juzgado) CyqDAOFactory.getJuzgadoDAO().getById(id);
	}
	
	public static Juzgado getByIdNull(Long id) {
		return (Juzgado) CyqDAOFactory.getJuzgadoDAO().getByIdNull(id);
	}
	
	public static List<Juzgado> getList() {
		return (ArrayList<Juzgado>) CyqDAOFactory.getJuzgadoDAO().getList();
	}
	
	public static List<Juzgado> getListActivos() {			
		return (ArrayList<Juzgado>) CyqDAOFactory.getJuzgadoDAO().getListActiva();
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
		if (GenericDAO.hasReference(this, Procedimiento.class, "juzgado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							CyqError.JUZGADO_LABEL, CyqError.PROCEDIMIENTO_LABEL);
		}
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		if (StringUtil.isNullOrEmpty(getDesJuzgado())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.JUZGADO_DESJUZGADO_LABEL );
		}
		
		//	Validaciones		
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	

	/**
	 * Activa el Juzgado. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getJuzgadoDAO().update(this);
	}


	/**
	 * Desactiva el Juzgado. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getJuzgadoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Juzgado
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Juzgado
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	public List<Abogado> getListAbogado() {
		return listAbogado;
	}
	public void setListAbogado(List<Abogado> listAbogado) {
		this.listAbogado = listAbogado;
	}
}
