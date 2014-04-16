//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import ar.gov.rosario.siat.cas.buss.bean.TipoSolicitud;
import ar.gov.rosario.siat.gde.buss.bean.Convenio;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a una tabla Auxiliar de Convenios.
 * Contiene los Convenio para los cuales se debe enviar una Solicitud de Revisión de Caso a Balance durante el asentamiento.
 * @author tecso
 */
@Entity
@Table(name = "bal_auxConvenio")
public class AuxConvenio extends BaseBO {

	private static final long serialVersionUID = 1L;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idAsentamiento") 
	private Asentamiento asentamiento;
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idConvenio") 
	private Convenio  convenio;

	@ManyToOne(optional=false, fetch=FetchType.LAZY)
    @JoinColumn(name="idTipoSolicitud") 
	private TipoSolicitud tipoSolicitud;

	@Column(name = "observacion")
	private String observacion;
	
	//Constructores 
	public AuxConvenio(){
		super();
	}

	// Getters y Setters
	public Asentamiento getAsentamiento() {
		return asentamiento;
	}
	public void setAsentamiento(Asentamiento asentamiento) {
		this.asentamiento = asentamiento;
	}
	public Convenio getConvenio() {
		return convenio;
	}
	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public TipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}
	public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}

	// Metodos de clase	
	public static AuxConvenio getById(Long id) {
		return (AuxConvenio) BalDAOFactory.getAuxConvenioDAO().getById(id);
	}
	
	public static AuxConvenio getByIdNull(Long id) {
		return (AuxConvenio) BalDAOFactory.getAuxConvenioDAO().getByIdNull(id);
	}
	public static AuxConvenio getByAsentamientoYConvenioYTipoSol(Asentamiento asentamiento, Convenio convenio, TipoSolicitud tipoSolicitud) throws Exception {
		return (AuxConvenio) BalDAOFactory.getAuxConvenioDAO().getByAsentamientoYConvenio(asentamiento, convenio, tipoSolicitud);
	}

	public static List<AuxConvenio> getList() {
		return (ArrayList<AuxConvenio>) BalDAOFactory.getAuxConvenioDAO().getList();
	}
	
	public static List<AuxConvenio> getListByAsentamiento(Asentamiento asentamiento) throws Exception {
		return (ArrayList<AuxConvenio>) BalDAOFactory.getAuxConvenioDAO().getListByAsentamiento(asentamiento);
	}
	
	public static List<AuxConvenio> getListActivos() {			
		return (ArrayList<AuxConvenio>) BalDAOFactory.getAuxConvenioDAO().getListActiva();
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
		
		//Validaciones de VO

		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
	
}
