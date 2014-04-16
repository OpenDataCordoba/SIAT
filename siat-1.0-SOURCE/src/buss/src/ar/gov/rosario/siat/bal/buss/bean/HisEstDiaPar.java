//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.bal.buss.dao.BalDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean que representa el Historico de cambio de Estados de DiarioPartida.
 * 
 * @author Tecso
 *
 */
@Entity
@Table(name = "bal_hisEstDiaPar")
public class HisEstDiaPar extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idDiarioPartida") 
	private DiarioPartida diarioPartida;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstDiaPar") 
	private EstDiaPar estDiaPar;

	@Column(name = "fecha")
	private Date fecha;
	
	@Column(name = "logCambios")
	private String logCambios;
	
	@Column(name = "observaciones")
	private String observaciones;

	// Constructores
	public HisEstDiaPar(){
		super();	
	}
	
	public HisEstDiaPar(Long id){
		super();
		setId(id);
	}
	
	// Getters y Setters
	
	public DiarioPartida getDiarioPartida() {
		return diarioPartida;
	}

	public void setDiarioPartida(DiarioPartida diarioPartida) {
		this.diarioPartida = diarioPartida;
	}

	public EstDiaPar getEstDiaPar() {
		return estDiaPar;
	}

	public void setEstDiaPar(EstDiaPar estDiaPar) {
		this.estDiaPar = estDiaPar;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getLogCambios() {
		return logCambios;
	}

	public void setLogCambios(String logCambios) {
		this.logCambios = logCambios;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}


	// Metodos de clase	
	public static HisEstDiaPar getById(Long id) {
		return (HisEstDiaPar) BalDAOFactory.getHisEstDiaParDAO().getById(id);
	}
	
	public static HisEstDiaPar getByIdNull(Long id) {
		return (HisEstDiaPar) BalDAOFactory.getHisEstDiaParDAO().getByIdNull(id);
	}
	
	public static List<HisEstDiaPar> getList() {
		return (ArrayList<HisEstDiaPar>) BalDAOFactory.getHisEstDiaParDAO().getList();
	}
	
	public static List<HisEstDiaPar> getListActivos() {			
		return (ArrayList<HisEstDiaPar>) BalDAOFactory.getHisEstDiaParDAO().getListActiva();
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
		/*if(StringUtil.isNullOrEmpty(getDesHisEstDiaPar())){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPORIMOV_DESTIPORIMOV);
		}
		if(getRecurso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, BalError.TIPORIMOV_RECURSO);
		}*/
		
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
		
		/*if (GenericDAO.hasReference(this, DiarioPartida.class, "hisEstDiaPar")) {
			addRecoverableError(BaseError.MSG_ELIMINAR_REGISTROS_ASOCIADOS, 
				BalError.TIPORIMOV_LABEL , BalError.DIARIOPARTIDA_LABEL);
		}
		 */
		
		if (hasError()) {
			return false;
		}

		// Validaciones de Negocio
				
		return true;
	}
}
