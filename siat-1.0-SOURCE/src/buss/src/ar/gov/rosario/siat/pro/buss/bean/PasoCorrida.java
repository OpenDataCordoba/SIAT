//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a un Paso de una Corrida
 * 
 * @author tecso
 */

@Entity
@Table(name = "pro_pasoCorrida")
public class PasoCorrida extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "pasoCorrida";
	
	public static final int PASO_UNO  = 1;
	public static final int PASO_DOS  = 2;
	public static final int PASO_TRES = 3;
	public static final int PASO_CUATRO = 4;
	
	@ManyToOne(optional=false)  
	@JoinColumn(name="idCorrida")
	private Corrida corrida;
	
	@Column(name = "paso")
	private Integer paso;   // INTEGER NOT NULL
	 
	@Column(name = "fechaCorrida")
	private Date fechaCorrida;   // DATETIME HOUR TO MINUTE NOT NULL
	
	@ManyToOne(optional=false)  
	@JoinColumn(name="idEstadoCorrida") // NOT NULL
	private EstadoCorrida estadoCorrida;
	
	@Column(name = "observacion")
	private String observacion;   // VARCHAR(255)

	// Constructores
	public PasoCorrida(){
		super();
	}
	// Getters y Setters
	public Corrida getCorrida() {
		return corrida;
	}
	public void setCorrida(Corrida corrida) {
		this.corrida = corrida;
	}
	public EstadoCorrida getEstadoCorrida() {
		return estadoCorrida;
	}
	public void setEstadoCorrida(EstadoCorrida estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}
	public Date getFechaCorrida() {
		return fechaCorrida;
	}
	public void setFechaCorrida(Date fechaCorrida) {
		this.fechaCorrida = fechaCorrida;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Integer getPaso() {
		return paso;
	}
	public void setPaso(Integer paso) {
		this.paso = paso;
	}
	
	
	// Metodos de Clase
	public static PasoCorrida getById(Long id) {
		return (PasoCorrida) ProDAOFactory.getPasoCorridaDAO().getById(id);  
	}
	
	public static PasoCorrida getByIdNull(Long id) {
		return (PasoCorrida) ProDAOFactory.getPasoCorridaDAO().getByIdNull(id);
	}
	
	public static List<PasoCorrida> getList() {
		return (ArrayList<PasoCorrida>) ProDAOFactory.getPasoCorridaDAO().getList();
	}
	
	public static List<PasoCorrida> getListActivos() {			
		return (ArrayList<PasoCorrida>) ProDAOFactory.getPasoCorridaDAO().getListActiva();
	}

	// Metodos de Instancia

	// Validaciones
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

}
