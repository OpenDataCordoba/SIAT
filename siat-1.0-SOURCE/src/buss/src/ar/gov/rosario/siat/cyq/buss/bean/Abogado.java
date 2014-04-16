//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

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
import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import ar.gov.rosario.siat.cyq.iface.util.CyqError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a Abogado.
 * Son los abogados que se pueden asociar a cada juzgado.
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_abogado")
public class Abogado extends BaseBO {

	private static final long serialVersionUID = 1L;

	@Column(name = "descripcion")
	private String descripcion;
	
	@Column(name = "domicilio")
	private String domicilio;
	
	@Column(name = "telefono")
	private String telefono;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name = "idJuzgado")
	private Juzgado juzgado;
	
	

	// Constructores
	public Abogado(){
		super();
	}
	
	// Metodos de Clase
	public static Abogado getById(Long id) {
		return (Abogado) CyqDAOFactory.getAbogadoDAO().getById(id);
	}
	
	public static Abogado getByIdNull(Long id) {
		return (Abogado) CyqDAOFactory.getAbogadoDAO().getByIdNull(id);
	}
	
	public static List<Abogado> getList() {
		return (ArrayList<Abogado>) CyqDAOFactory.getAbogadoDAO().getList();
	}
	
	public static List<Abogado> getListActivos() {			
		return (ArrayList<Abogado>) CyqDAOFactory.getAbogadoDAO().getListActiva();
	}
	
// Metodos de negocio
	
	/**
	 * Activa el Abogado. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getAbogadoDAO().update(this);
	}

	/**
	 * Desactiva el Abogado. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getAbogadoDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Abogado
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Abogado
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
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

	
		//Procedimiento
		
		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio
		
		return true;		
	}

	public boolean validateDelete() {
		//limpiamos la lista de errores
		clearError();
		
		
		//Procedimiento
		if (GenericDAO.hasReference(this, Procedimiento.class, "abogado")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS,
							CyqError.ABOGADO_LABEL, CyqError.PROCEDIMIENTO_LABEL);
		}
		
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		// Validaciones        
		if (StringUtil.isNullOrEmpty(getDescripcion())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.ABOGADO_DESABOGADO_LABEL );
		}
		
		/*if (getJuzgado()==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, CyqError.JUZGADO_LABEL );
		}*/
		// Validaciones		
		
		if (hasError()) {
			return false;
		}
		
		return true;
	}
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getDomicilio() {
		return domicilio;
	}
	public void setDomicilio(String domicilio) {
		this.domicilio = domicilio;
	}
	public String getTelefono() {
		return telefono;
	}
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public Juzgado getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(Juzgado juzgado) {
		this.juzgado = juzgado;
	}

	
		
}
