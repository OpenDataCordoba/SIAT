//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.buss.bean;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.cyq.buss.dao.CyqDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a MotivoBaja
 * 
 * @author tecso
 */
@Entity
@Table(name = "cyq_motivoBaja")
public class MotivoBaja extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	public static final long ID_CONVERSION_CONCURSO_QUIEBRA = 4L;
	public static final long ID_CONVERSION_QUIEBRA_CONCURSO = 5L;
		
	@Column(name = "desMotivoBaja")
	private String desMotivoBaja;
	
	@Column(name = "devuelveDeuda")
	private Integer devuelveDeuda;
	
	@Column(name = "tipo")
	private String tipo;


	//<#Propiedades#>
	
	// Constructores
	public MotivoBaja(){
		super();
		// Seteo de valores default			
	}
	
	public MotivoBaja(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static MotivoBaja getById(Long id) {
		return (MotivoBaja) CyqDAOFactory.getMotivoBajaDAO().getById(id);
	}
	
	public static MotivoBaja getByIdNull(Long id) {
		return (MotivoBaja) CyqDAOFactory.getMotivoBajaDAO().getByIdNull(id);
	}
	
	public static List<MotivoBaja> getList() {
		return (List<MotivoBaja>) CyqDAOFactory.getMotivoBajaDAO().getList();
	}
	
	public static List<MotivoBaja> getListActivos() {			
		return (List<MotivoBaja>) CyqDAOFactory.getMotivoBajaDAO().getListActiva();
	}
	
	public static List<MotivoBaja> getListEstados() throws Exception {			
		return (List<MotivoBaja>) CyqDAOFactory.getMotivoBajaDAO().getListEstados();
	}
	
	// Getters y setters
	public String getDesMotivoBaja() {
		return desMotivoBaja;
	}

	public void setDesMotivoBaja(String desMotivoBaja) {
		this.desMotivoBaja = desMotivoBaja;
	}

	public Integer getDevuelveDeuda() {
		return devuelveDeuda;
	}

	public void setDevuelveDeuda(Integer devuelveDeuda) {
		this.devuelveDeuda = devuelveDeuda;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
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
	
		//<#ValidateDelete#>
		
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
	 * Activa el MotivoBaja. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		CyqDAOFactory.getMotivoBajaDAO().update(this);
	}

	/**
	 * Desactiva el MotivoBaja. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		CyqDAOFactory.getMotivoBajaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del MotivoBaja
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del MotivoBaja
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}


	
	//<#MetodosBeanDetalle#>
}
