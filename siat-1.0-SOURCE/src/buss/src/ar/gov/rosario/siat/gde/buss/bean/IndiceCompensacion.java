//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a IndiceCompensacion
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_indiceComp")
public class IndiceCompensacion extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "indice")
	private Double indice;
	
	
	@Column(name="fechaDesde")
	private Date fechaDesde;

	@Column(name="fechaHasta")
	private Date fechaHasta;
	

	// Constructores
	public IndiceCompensacion(){
		super();
	}
	
	public IndiceCompensacion(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static IndiceCompensacion getById(Long id) {
		return (IndiceCompensacion) GdeDAOFactory.getIndiceCompensacionDAO().getById(id);
	}
	
	public static IndiceCompensacion getByIdNull(Long id) {
		return (IndiceCompensacion) GdeDAOFactory.getIndiceCompensacionDAO().getByIdNull(id);
	}
	
	public static List<IndiceCompensacion> getList() {
		return (ArrayList<IndiceCompensacion>) GdeDAOFactory.getIndiceCompensacionDAO().getList();
	}
	
	public static List<IndiceCompensacion> getListActivos() {			
		return (ArrayList<IndiceCompensacion>) GdeDAOFactory.getIndiceCompensacionDAO().getListActiva();
	}
	
	public static IndiceCompensacion getVigente(Date fecha){
		return GdeDAOFactory.getIndiceCompensacionDAO().getVigente(fecha);
	}
	
	// Getters y setters
	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}

	public Double getIndice() {
		return indice;
	}

	public void setIndice(Double indice) {
		this.indice = indice;
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
		/*if (StringUtil.isNullOrEmpty(getCod${Bean}())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ExeError.${BEAN}_COD${BEAN} );
		}*/
		
		
		if (hasError()) {
			return false;
		}
				
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el IndiceCompensacion. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getIndiceCompensacionDAO().update(this);
	}

	/**
	 * Desactiva el IndiceCompensacion. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getIndiceCompensacionDAO().update(this);
	}
	
	/**
	 * Valida la activacion del IndiceCompensacion
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	/**
	 * Valida la desactivacion del IndiceCompensacion
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
