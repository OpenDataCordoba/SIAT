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

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al auxiliar para aplicar pagos a cuenta
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_resDet")
public class ResDet extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idRescate")
	private Rescate rescate;
	
	@ManyToOne(optional=false ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idConvenio")
	private Convenio convenio;
	
	@ManyToOne(optional=true ,fetch=FetchType.LAZY) 
	@JoinColumn(name="idMotExc") 
	private MotExc motExc;
	
	@Column(name="observacion") 
	private String observacion;
	
	//Metodos de Clase
	
	public static ResDet getById(Long id){
		ResDet resDet = (ResDet) GdeDAOFactory.getResDetDAO().getById(id);
		return resDet;
	}
	
	// Constructores
	public ResDet(){
		super();
	}


	public Rescate getRescate() {
		return rescate;
	}


	public void setRescate(Rescate rescate) {
		this.rescate = rescate;
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

	
	// Getters Y Setters
	
	
	
}
