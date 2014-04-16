//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import ar.gov.rosario.siat.gde.buss.dao.GdeDAOFactory;
import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.buss.bean.BaseBO;
import coop.tecso.demoda.iface.model.Estado;


/**
 * Bean correspondiente a Tramite
 * 
 * @author tecso
 */
@Entity
@Table(name = "gde_ctrlInfDeu")
public class CtrlInfDeu extends BaseBO {
	
	private static final long serialVersionUID = 1L;
	
	@Column(name = "nroTramite")
	private Integer nroTramite;
	
	@Column(name = "nroRecibo")
	private Long nroRecibo;
	
	@Column(name = "anioRecibo")
	private Long anioRecibo;
	
	@Column(name="codId") 
	private String codId;
	
	@ManyToOne(fetch=FetchType.LAZY) 
	@JoinColumn(name="idCuenta") 
	private Cuenta cuenta;
	
	@Column(name = "fechaHoraGen")
	private Date fechaHoraGen;	// Generacion
	
	@Column(name = "fechaHoraImp")
	private Date fechaHoraImp;  // Impresion
	
	@Column(name = "nroLiquidacion")
	private String nroLiquidacion;
	
	@Column(name = "observacion")
	private String observacion;


	// Constructores
	public CtrlInfDeu(){
		super();
		// Seteo de valores default			
	}
	
	public CtrlInfDeu(Long id){
		super();
		setId(id);
	}
	
	// Metodos de Clase
	public static CtrlInfDeu getById(Long id) {
		return (CtrlInfDeu) GdeDAOFactory.getCtrlInfDeuDAO().getById(id);
	}
	
	public static CtrlInfDeu getByIdNull(Long id) {
		return (CtrlInfDeu) GdeDAOFactory.getCtrlInfDeuDAO().getByIdNull(id);
	}
	
	public static List<CtrlInfDeu> getList() {
		return (List<CtrlInfDeu>) GdeDAOFactory.getCtrlInfDeuDAO().getList();
	}
	
	public static List<CtrlInfDeu> getListActivos() {			
		return (List<CtrlInfDeu>) GdeDAOFactory.getCtrlInfDeuDAO().getListActiva();
	}
	
	public static CtrlInfDeu getByNroYAnio(Long nroRecibo, Long anioRecibo) throws Exception{
		return (CtrlInfDeu) GdeDAOFactory.getCtrlInfDeuDAO().getByNroYAnio(nroRecibo, anioRecibo);
	}
	
	// Getters y setters
	public Long getAnioRecibo() {
		return anioRecibo;
	}

	public void setAnioRecibo(Long anioRecibo) {
		this.anioRecibo = anioRecibo;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public String getNroLiquidacion() {
		return nroLiquidacion;
	}

	public void setNroLiquidacion(String nroLiquidacion) {
		this.nroLiquidacion = nroLiquidacion;
	}

	public Long getNroRecibo() {
		return nroRecibo;
	}

	public void setNroRecibo(Long nroRecibo) {
		this.nroRecibo = nroRecibo;
	}

	public Integer getNroTramite() {
		return nroTramite;
	}

	public void setNroTramite(Integer nroTramite) {
		this.nroTramite = nroTramite;
	}

	public String getObservacion() {
		return observacion;
	}

	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	
	public String getCodId() {
		return codId;
	}

	public void setCodId(String codId) {
		this.codId = codId;
	}

	public Date getFechaHoraGen() {
		return fechaHoraGen;
	}

	public void setFechaHoraGen(Date fechaHoraGen) {
		this.fechaHoraGen = fechaHoraGen;
	}

	public Date getFechaHoraImp() {
		return fechaHoraImp;
	}

	public void setFechaHoraImp(Date fechaHoraImp) {
		this.fechaHoraImp = fechaHoraImp;
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
	
		//<#ValidateDelete#>
		
		if (hasError()) {
			return false;
		}

		return true;
	}
	
	private boolean validate() throws Exception {
		
		//	Validaciones        
		
		// Validaciones de unique
		
		return true;
	}
	
	// Metodos de negocio
	
	/**
	 * Activa el Tramite. Previamente valida la activacion. 
	 *
	 */
	public void activar(){
		if(!this.validateActivar()){
			return;
		}
		this.setEstado(Estado.ACTIVO.getId());
		GdeDAOFactory.getCtrlInfDeuDAO().update(this);
	}

	/**
	 * Desactiva el Tramite. Previamente valida la desactivacion. 
	 *
	 */
	public void desactivar(){
		if(!this.validateDesactivar()){
			return;
		}
		this.setEstado(Estado.INACTIVO.getId());
		GdeDAOFactory.getCtrlInfDeuDAO().update(this);
	}
	
	/**
	 * Valida la activacion del Tramite
	 * @return boolean
	 */
	private boolean validateActivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
	
	/**
	 * Valida la desactivacion del Tramite
	 * @return boolean
	 */
	private boolean validateDesactivar(){
		//limpiamos la lista de errores
		clearError();
		
		//Validaciones 
		return true;
	}
		
}
