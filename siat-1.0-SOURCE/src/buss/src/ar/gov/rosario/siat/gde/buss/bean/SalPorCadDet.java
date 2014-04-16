//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.gde.iface.util.GdeError;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Rescate
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_salPorCadDet")
public class SalPorCadDet extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idSalPorCad") 
	private SalPorCad salPorCad;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idConvenio") 
	private Convenio convenio;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idMotExc") 
	private MotExc motExc;

	@Column(name = "observacion")
	private String observacion;
	
	@Column(name="procesado")
	private Integer procesado;
	
	
	// Constructores
	public SalPorCadDet(){
		super();
	}

	// Getters Y Setters
	public SalPorCad getSalPorCad() {
		return salPorCad;
	}

	public void setSalPorCad(SalPorCad salPorCad) {
		this.salPorCad = salPorCad;
	}

	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public MotExc getMotExc() {
		return motExc;
	}

	public void setMotExc(MotExc motExc) {
		this.motExc = motExc;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public Integer getProcesado() {
		return procesado;
	}

	public void setProcesado(Integer procesado) {
		this.procesado = procesado;
	}

	// Validaciones 
	public boolean validateCreate() throws Exception {
		// limpiamos la lista de errores
		clearError();
		
		// recurso
		if(this.getConvenio() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SALPORCADDET_CONVENIO);
		}
		// plan
		if(this.getSalPorCad() == null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, GdeError.SALPORCADDET_SALPORCAD);
		}
		

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
	
		if (hasError()) {
			return false;
		}
		
		
		// Validaciones de unique
		return !hasError();
	}
	
	public void update ()throws Exception{
		if (validate())GdeDAOFactory.getSalPorCadDetDAO().update(this);
		
	}


}
