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
import ar.gov.rosario.siat.gde.iface.model.EstConDeuVO;
import ar.gov.rosario.siat.gde.iface.model.HistEstConDeuVO;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente al historico del estado de la constancia de deuda.
 * 
 * @author tecso
 */

@Entity
@Table(name = "gde_histEstConDeu")
public class HistEstConDeu extends BaseBO {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "hisEstConDeu";

	public static final String LOG_CREADA_MANUAL ="Creación manual de Constancia";
	
	public static final String LOG_EMITIDA="Creacion de la constancia de deuda";

	public static final String LOG_MODIFICADA = "Modificación de la constancia de deuda";
	
	public static final String LOG_MODIFICADA_ASPECTOS_FORMALES = "Modificación Aspectos Formales de Constancia";
	
	public static final String LOG_MODIFICACION_QUANTUM_CONSTANCIA="Modificación Quantum de la Constancia";
	
	public static final String LOG_MODIFICACION_OBS_QUANTUM_CONSTANCIA="Modificación Observación del Quantum de la Constancia";
	
	public static final String LOG_HABILITADA ="Habilitación de Constancia";
	
	public static final String LOG_HABILITADA_POR_HABILITACION_DE_PLANILLA ="Habilitación de la Constancia por habilitación de Planilla de envío";

	public static final String LOG_MODIFICACION_LEYENDA = "Modificación Leyenda de Constancia";
	
	public static final String LOG_RECOMPUESTA = "Reimpresión de Constancia";
	
	public static final String LOG_ANULADA = "Anulación de la constancia de deuda";
	
	public static final String LOG_ANULADA_MANUAL = "Anulación Manual de la constancia de deuda";
	
	
	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idEstConDeu") 
	private EstConDeu estConDeu; // NOT NULL

	@ManyToOne(optional=false, fetch=FetchType.LAZY) 
	@JoinColumn(name="idConstanciaDeu") 
	private ConstanciaDeu constanciaDeu;           // NOT NULL
	
	@Column(name = "fechaDesde")
	private Date fechaDesde;     // DATETIME YEAR TO DAY
	
	@Column(name = "logEstado")
	private String logEstado;     // VARCHAR(255)

	// Constructores
	public HistEstConDeu(){
		super();
	}
	
	// Getters y Setters
	public ConstanciaDeu getConstanciaDeu() {
		return constanciaDeu;
	}
	public void setConstanciaDeu(ConstanciaDeu constanciaDeu) {
		this.constanciaDeu = constanciaDeu;
	}
	public EstConDeu getEstConDeu() {
		return estConDeu;
	}
	public void setEstConDeu(EstConDeu estConDeu) {
		this.estConDeu = estConDeu;
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

	// Metodos de Clase
	public static HistEstConDeu getById(Long id) {
		return (HistEstConDeu) GdeDAOFactory.getHistEstConDeuDAO().getById(id);  
	}
	 
	public static HistEstConDeu getByIdNull(Long id) {
		return (HistEstConDeu) GdeDAOFactory.getHistEstConDeuDAO().getByIdNull(id);
	}
	
	public static List<HistEstConDeu> getList() {
		return (ArrayList<HistEstConDeu>) GdeDAOFactory.getHistEstConDeuDAO().getList();
	}
	
	public static List<HistEstConDeu> getListActivos() {			
		return (ArrayList<HistEstConDeu>) GdeDAOFactory.getHistEstConDeuDAO().getListActiva();
	}

	/**
	 * Devuelve el log que se graba en la tabla de historicos para el estado pasado como parametro
	 * @param idEstado
	 * @return Si no existe el estado devuelve una cadena vacia
	 */
	public static String getLogEstado(long idEstado){
		if(idEstado==EstConDeu.ID_EMITIDA)
			return LOG_EMITIDA;	
		if(idEstado==EstConDeu.ID_MODIFICADA)
			return LOG_MODIFICADA;
		if(idEstado==EstConDeu.ID_CREADA)
			return LOG_CREADA_MANUAL;
		if(idEstado==EstConDeu.ID_ANULADA)
			return LOG_ANULADA;
		
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
	public HistEstConDeuVO toVOforView() throws Exception{
		HistEstConDeuVO histEstConDeuVO = (HistEstConDeuVO) this.toVO(0, false);
		histEstConDeuVO.setEstConDeu((EstConDeuVO) estConDeu.toVO(0, false));
		
		return histEstConDeuVO;
	}
}
