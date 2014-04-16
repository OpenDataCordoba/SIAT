//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.util.DefError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Estado;

/**
 * Bean correspondiente a AgeRet
 * Representa los agentes de retención
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_ageret")
public class AgeRet extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne()  
    @JoinColumn(name="idrecurso")
	private Recurso recurso;
		
	@Column(name="desAgeRet")
	private String desAgeRet;

	@Column(name="cuit")
	private String cuit;
	
	@Column(name="fechaDesde")
	private Date fechaDesde;

	@Column(name="fechaHasta")
	private Date fechaHasta;

	
	// Constructores
	public AgeRet(){
		super();
	}
	
	// Getters y Setters
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public String getDesAgeRet() {
		return desAgeRet;
	}

	public void setDesAgeRet(String desAgeRet) {
		this.desAgeRet = desAgeRet;
	}

	public String getCuit() {
		return cuit;
	}

	public void setCuit(String cuit) {
		this.cuit = cuit;
	}

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
	
	
	// Metodos de Clase
	public static AgeRet getById(Long id) {
		return (AgeRet) GdeDAOFactory.getAgeRetDAO().getById(id);  
	}

	public static AgeRet getByIdNull(Long id) {
		return (AgeRet) GdeDAOFactory.getAgeRetDAO().getByIdNull(id);
	}
	
	public static List<AgeRet> getList() {
		return (List<AgeRet>) GdeDAOFactory.getAgeRetDAO().getList();
	}
	
	public static List<AgeRet> getListActivos() {			
		return (List<AgeRet>) GdeDAOFactory.getAgeRetDAO().getListActiva();
	}
	
	public static List<AgeRet> getListActivosByRecurso(Recurso recurso) {			
		return (List<AgeRet>) GdeDAOFactory.getAgeRetDAO().getListActivosByRecurso(recurso);
	}
	
	public static AgeRet getByCuitYRecurso(String cuit, Long idRecurso) {
		return (AgeRet) GdeDAOFactory.getAgeRetDAO().getByCuitYRecurso(cuit, idRecurso);
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

	private boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos
		if(recurso==null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, DefError.RECURSO_LABEL);
		}
		if (StringUtil.isNullOrEmpty(getDesAgeRet())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.AGERET_DESAGERET);
		}
		if (StringUtil.isNullOrEmpty(getCuit())) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.AGERET_CUIT);
		}
		if (fechaDesde== null) {
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO,GdeError.AGERET_FECHADESDE);
		}

	
		if (hasError()) {
			return false;
		}
		// Validaciones de Unicidad
		
		// Otras Validaciones
		
		
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

		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
		
		return true;
	}

	// Metodos de negocio


	/**
	 * Activa el AgeRet. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getAgeRetDAO().update(this);
	}

	/**
	 * Desactiva el AgeRet. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getAgeRetDAO().update(this);
	}
	
	/**
	 * Valida la activacion del AgeRet
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del AgeRet
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}

	
}
