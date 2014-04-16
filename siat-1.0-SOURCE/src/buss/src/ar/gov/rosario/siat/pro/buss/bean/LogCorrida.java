//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a LogCorrida
 * 
 * @author tecso
 */
@Entity
@Table(name = "pro_logcorrida")
public class LogCorrida extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(optional=false)
    @JoinColumn(name="idCorrida") 
	private Corrida corrida;
	
	@Column(name = "paso")
	private Long paso;
	
	@Column(name = "log")
	private String log;
	
	
	// Constructores
	public LogCorrida(){
		super();
	}
	
	/**
	 * Constructor Sobrecargado
	 *
	 * @param id
	 * @param corrida
	 * @param nivel
	 * @param log
	 */
	public LogCorrida(Corrida corrida, Long paso, String log) {
		this.corrida = corrida;
		this.paso = paso;
		this.log = log;
	}
	
	
	// Getters y setters
	public Corrida getCorrida() {
		return corrida;
	}

	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}

	public String getLog() {
		return log;
	}

	public void setLog(String log) {
		this.log = log;
	}

	public Long getPaso() {
		return paso;
	}

	public void setPaso(Long paso) {
		this.paso = paso;
	}

	
	// Metodos de Clase
	public static LogCorrida getById(Long id) {
		return (LogCorrida) ProDAOFactory.getLogCorridaDAO().getById(id);
	}
	
	public static LogCorrida getByIdNull(Long id) {
		return (LogCorrida) ProDAOFactory.getLogCorridaDAO().getByIdNull(id);
	}
	
	public static List<LogCorrida> getList() {
		return (ArrayList<LogCorrida>) ProDAOFactory.getLogCorridaDAO().getList();
	}
	
	public static List<LogCorrida> getListActivos() {			
		return (ArrayList<LogCorrida>) ProDAOFactory.getLogCorridaDAO().getListActiva();
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
	
		if (hasError()) {
			return false;
		}
		
		// Validaciones de unique
	
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el LogCorrida. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		ProDAOFactory.getLogCorridaDAO().update(this);
	}

	/**
	 * Desactiva el LogCorrida. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		ProDAOFactory.getLogCorridaDAO().update(this);
	}
	
	/**
	 * Valida la activacion del LogCorrida
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del LogCorrida
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
}
