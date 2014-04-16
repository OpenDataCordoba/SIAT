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
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al historico del estado de la planilla de envio de deuda a procuradores.
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_hisEstPlaEnvDP")
public class HistEstPlaEnvDP extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	public static final String LOG_ESTADO_EMITIDA="Creacion de la Planilla de Deuda de Envio a Procuradores";
	public static final String LOG_ESTADO_MODIFICADA = "Modificación Datos Planilla de Envío de Deuda";	
	public static final String LOG_MODIFICADA_POR_MODIF_CONSTANCIA = "Modificación Datos Planilla por modificación de constancias asociadas";
	public static final String LOG_ESTADO_RECOMPUESTA = "Reimpresión de Planilla de Envío de Deuda";
	public static final String LOG_ESTADO_HABILITADA = "Habilitación de Planilla de Envío de Deuda";
	public static final String LOG_ESTADO_ANULADA = "Anulación de Planilla de Envío de Deuda";
	
	public static final String NAME = "histEstPlaEnvDP";

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstPlaEnvDeuPr") 
	private EstPlaEnvDeuPr estPlaEnvDeuPr; // NOT NULL
	
	// INTEGER NOT NULL,
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idPlaEnvDeuPro") 
	private PlaEnvDeuPro plaEnvDeuPro;           // NOT NULL
	
	//fechaDesde DATETIME YEAR TO DAY NOT NULL,
	@Column(name = "fechaDesde")
	private Date fechaDesde;     // DATETIME YEAR TO DAY NOT NULL,

	@Column(name = "logEstado")
	private String logEstado;     // VARCHAR(255)

	// Constructores
	public HistEstPlaEnvDP(){
		super();
	}
	
	// Getters y Setters
	public EstPlaEnvDeuPr getEstPlaEnvDeuPr() {
		return estPlaEnvDeuPr;
	}
	public void setEstPlaEnvDeuPr(EstPlaEnvDeuPr estPlaEnvDeuPr) {
		this.estPlaEnvDeuPr = estPlaEnvDeuPr;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}
	public String getLogEstado() {
		return logEstado;
	}
	public void setLogEstado(String logEstado) {
		this.logEstado = logEstado;
	}
	public PlaEnvDeuPro getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}
	public void setPlaEnvDeuPro(PlaEnvDeuPro plaEnvDeuPro) {
		this.plaEnvDeuPro = plaEnvDeuPro;
	}

	// Metodos de Clase
	public static HistEstPlaEnvDP getById(Long id) {
		return (HistEstPlaEnvDP) GdeDAOFactory.getHistEstPlaEnvDPDAO().getById(id);  
	}
	 
	public static HistEstPlaEnvDP getByIdNull(Long id) {
		return (HistEstPlaEnvDP) GdeDAOFactory.getHistEstPlaEnvDPDAO().getByIdNull(id);
	}
	
	public static List<HistEstPlaEnvDP> getList() {
		return (ArrayList<HistEstPlaEnvDP>) GdeDAOFactory.getHistEstPlaEnvDPDAO().getList();
	}
	
	public static List<HistEstPlaEnvDP> getListActivos() {			
		return (ArrayList<HistEstPlaEnvDP>) GdeDAOFactory.getHistEstPlaEnvDPDAO().getListActiva();
	}

	/**
	 * Devuelve el log que se graba en la tabla de historicos para el estado pasado como parametro
	 * @param idEstado
	 * @return Si no existe el estado devuelve una cadena vacia
	 */
	public static String getLogEstado(long idEstado){
		if(idEstado==EstPlaEnvDeuPr.ID_EMITIDA)
			return LOG_ESTADO_EMITIDA;	
		if(idEstado==EstPlaEnvDeuPr.ID_MODIFICADA)
			return LOG_ESTADO_MODIFICADA;
		if(idEstado==EstPlaEnvDeuPr.ID_RECOMPUESTA)
			return LOG_ESTADO_RECOMPUESTA;
		if(idEstado==EstPlaEnvDeuPr.ID_HABILITADA)
			return LOG_ESTADO_HABILITADA;
		if(idEstado==EstPlaEnvDeuPr.ID_ANULADA)
			return LOG_ESTADO_ANULADA;
		
		return "";
	}
	
	// Metodos de Instancia

	// Validaciones
	public boolean validateCreate() throws Exception {
		//limpiamos la lista de errores
		clearError();

		if (!this.validate()) {
			return false;
		}
		
		// Validaciones de Negocio

		return true;
	}

	public boolean validateUpdate() throws Exception {
		//limpiamos la lista de errores
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

		// validaciones comunes 

		if (hasError()) {
			return false;
		}

		return true;
	}
	

	// Metodos de negocio
	
}
