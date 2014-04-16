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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.util.BaseError;
import ar.gov.rosario.siat.pro.buss.dao.ProDAOFactory;
import ar.gov.rosario.siat.pro.iface.model.CorridaVO;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import ar.gov.rosario.siat.pro.iface.util.ProError;
import coop.tecso.adpcore.AdpRun;
import coop.tecso.demoda.buss.bean.BaseBO;

/**
 * Bean correspondiente a Corrida
 * 
 * @author tecso
 */

@Entity
@Table(name = "pro_corrida")
public class Corrida extends BaseBO {

	private static final long serialVersionUID = 1L;
	
	private static Logger log = Logger.getLogger(Corrida.class);

	public static final String NAME = "corrida";
	
	@Column(name = "desCorrida")
	private String desCorrida;   // VARCHAR(255) NOT NULL

	@ManyToOne(optional=false)  
    @JoinColumn(name="idProceso")
	private Proceso proceso;
	
	@Column(name = "fechaInicio")
	private Date fechaInicio;   // DATETIME YEAR TO SECOND
	
	@Column(name = "fechaFin")
	private Date fechaFin;   // DATETIME YEAR TO SECOND
	
	@Column(name = "fechaUltResume")
	private Date fechaUltResume;   // DATETIME YEAR TO SECOND

	@ManyToOne(optional=false)  
    @JoinColumn(name="idEstadoCorrida")
    private EstadoCorrida estadoCorrida;
	
	@Column(name = "mensajeEstado")
	private String mensajeEstado;   // VARCHAR(100)
	
	@Column(name = "observacion")
	private String observacion;   // VARCHAR(255)

	@Column(name = "pasoActual")
	private Integer pasoActual;  // NOT NULL
	
	@Column(name = "nodoOwner")
	private String nodoOwner;
	
	@OneToMany()
	@JoinColumn(name="idCorrida")
	private List<LogCorrida> listLogCorrida;
	
	// Constructores
	public Corrida(){
		super();
	}

	// Getters y Setters
	public String getDesCorrida() {
		return desCorrida;
	}
	public void setDesCorrida(String desCorrida) {
		this.desCorrida = desCorrida;
	}
	public EstadoCorrida getEstadoCorrida() {
		return estadoCorrida;
	}

	public void setEstadoCorrida(EstadoCorrida estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}
	public Date getFechaFin() {
		return fechaFin;
	}
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}
	public Date getFechaInicio() {
		return fechaInicio;
	}
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}
	public Date getFechaUltResume() {
		return fechaUltResume;
	}
	public void setFechaUltResume(Date fechaUltResume) {
		this.fechaUltResume = fechaUltResume;
	}
	public String getMensajeEstado() {
		return mensajeEstado;
	}
	public void setMensajeEstado(String mensajeEstado) {
		this.mensajeEstado = mensajeEstado;
	}
	public String getObservacion() {
		return observacion;
	}
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}
	public Integer getPasoActual() {
		return pasoActual;
	}
	public void setPasoActual(Integer pasoActual) {
		this.pasoActual = pasoActual;
	}
	public Proceso getProceso() {
		return proceso;
	}
	public void setProceso(Proceso proceso) {
		this.proceso = proceso;
	}
	
	public String getNodoOwner() {
		return nodoOwner;
	}

	public void setNodoOwner(String nodoOwner) {
		this.nodoOwner = nodoOwner;
	}

	public List<LogCorrida> getListLogCorrida() {
		return listLogCorrida;
	}
	public void setListLogCorrida(List<LogCorrida> listLogCorrida) {
		this.listLogCorrida = listLogCorrida;
	}
	// Metodos de Clase
	public static Corrida getById(Long id) {
		return (Corrida) ProDAOFactory.getCorridaDAO().getById(id);  
	}
	
	public static Corrida getByIdNull(Long id) {
		return (Corrida) ProDAOFactory.getCorridaDAO().getByIdNull(id);
	}
	
	public static List<Corrida> getList() {
		return (ArrayList<Corrida>) ProDAOFactory.getCorridaDAO().getList();
	}
	
	public static List<Corrida> getListActivos() {			
		return (ArrayList<Corrida>) ProDAOFactory.getCorridaDAO().getListActiva();
	}
	
	public static List<Corrida> getListByCodProAndFecIni(String codProceso,Date fechaInicio) {			
		return (ArrayList<Corrida>) ProDAOFactory.getCorridaDAO().getListByCodProAfterFecIni(codProceso,fechaInicio);
	}

	// Metodos de Instancia

	// Validaciones
	public boolean validate() throws Exception{
		
		//limpiamos la lista de errores
		clearError();

		//Validaciones de Requeridos	
		if(getProceso()==null){
			addRecoverableError(BaseError.MSG_CAMPO_REQUERIDO, ProError.CORRIDA_PROCESO);
		}
		
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
	
	// Metodos de negocio

	/**
	 * Obtiene el PasoCorrida para la Corrida actual y para el paso
	 * @param  paso
	 * @return PasoCorrida
	 */
	public PasoCorrida getPasoCorrida(Integer paso){
		return ProDAOFactory.getPasoCorridaDAO().getByCorridaPaso(this, paso);
	}

	/**
	 * Obtiene el PasoCorrida para la Corrida actual y para el paso actual
	 * @return PasoCorrida
	 */
	public PasoCorrida getPasoCorridaActual(){
		return this.getPasoCorrida(this.getPasoActual());
	}
	
	//	 Administracion de FileCorrida
	public FileCorrida createFileCorrida(FileCorrida fileCorrida) throws Exception {
		
		// Validaciones de negocio
		if (!fileCorrida.validateCreate()) {
			return fileCorrida;
		}

		ProDAOFactory.getFileCorridaDAO().update(fileCorrida);
		
		return fileCorrida;
	}	

	public FileCorrida updateFileCorrida(FileCorrida fileCorrida) throws Exception {
		
		// Validaciones de negocio
		if (!fileCorrida.validateUpdate()) {
			return fileCorrida;
		}

		ProDAOFactory.getFileCorridaDAO().update(fileCorrida);
		
		return fileCorrida;
	}	

	public FileCorrida deleteFileCorrida(FileCorrida fileCorrida) throws Exception {
		
		// Validaciones de negocio
		if (!fileCorrida.validateDelete()) {
			return fileCorrida;
		}
				
		ProDAOFactory.getFileCorridaDAO().delete(fileCorrida);
		
		return fileCorrida;
	}	

	/**
	 * Crea un Log de la corrida
	 * @param  logCorrida
	 * @return LogCorrida
	 * @throws Exception
	 */
	public LogCorrida createLogCorrida(LogCorrida logCorrida) throws Exception {
		
		// Validaciones de negocio
		if (!logCorrida.validateCreate()) {
			return logCorrida;
		}
		
		ProDAOFactory.getLogCorridaDAO().update(logCorrida);
		
		return logCorrida;
	}	
	
	
	
	/**
	 * Crea un FileCorrida asociado a la corrida y al paso actual con el archivo indicado.
	 * 
	 * @param nombre
	 * @param descripcion
	 * @param fileName
	 */
	public void addOutputFile(String nombre, String descripcion, String fileName) throws Exception{
		FileCorrida fileCorrida = new FileCorrida(this,this.getPasoActual(),nombre,descripcion,fileName);
		this.createFileCorrida(fileCorrida);
	}
	
	public void addOutputFileOrdenada(String nombre, String descripcion, String fileName, Long orden) throws Exception{
		FileCorrida fileCorrida = new FileCorrida(this,this.getPasoActual(),nombre,descripcion,fileName);
		fileCorrida.setOrden(orden);
		this.createFileCorrida(fileCorrida);
	}
	
	public void addOutputFile(String nombre, String descripcion, String fileName, Long ctdRegistros) throws Exception{
		FileCorrida fileCorrida = new FileCorrida(this,this.getPasoActual(),nombre,descripcion,fileName, ctdRegistros);
		this.createFileCorrida(fileCorrida);
	}

	public void addOutputFileByPaso(Integer paso, String nombre, String descripcion, String fileName) throws Exception{
		FileCorrida fileCorrida = new FileCorrida(this,paso,nombre,descripcion,fileName);
		this.createFileCorrida(fileCorrida);
	}

	
	/**
	 * Elimina los FileCorrida asociados a la Corrida y al Paso
	 * @param  paso
	 * @return int
	 * @throws Exception
	 */
	public int deleteListFileCorridaByPaso(Integer paso) throws Exception{
		return ProDAOFactory.getFileCorridaDAO().deleteByCorridaYPaso(this, paso);
	}

	
	/**
	 * Retrocede un paso la corrida.
	 * Crea un nuevo LogCorrida.
	 * Si no encuentra un pasoCorrida anterior,devuelve null y carga un error en la corrida.
	 * Usando Adp crea el LogCorrida.
	 * @return PasoCorrida a la que retrocedio
	 */
	public PasoCorrida retrocederPaso() throws Exception{
		
		Integer pasoActual = this.getPasoActual();
		PasoCorrida pasoCorridaAnterior = this.getPasoCorrida(this.getPasoActual() - 1);
		if (pasoCorridaAnterior == null){
			log.error("Paso corrida anterior al paso actual no encontrado. PasoActual = " + pasoActual );
			addRecoverableError(ProError.PASO_CORRIDA_NO_ENCONTRADO);
			return null;
		}

		String log   = "Retroceso al paso anterior";
		AdpRun.getRun(this.getId()).logError(log);

		//Long   nivel = Long.valueOf(pasoActual);
		// Generamos el log de corrida a traves de Adp y no directamente por hibernate.
		/*
		LogCorrida logCorrida = new LogCorrida(this, nivel, log);
		
		logCorrida = this.createLogCorrida(logCorrida);
		
		if(logCorrida.hasError()){
			logCorrida.addErrorMessages(this);
			return null;
		}
		*/
		// retrocedo al paso de corrida anterior
		this.setPasoActual(pasoCorridaAnterior.getPaso());
		
		return pasoCorridaAnterior;
	}
	
	public void reiniciar() throws Exception{
		
		/*
		Integer pasoActual = this.getPasoActual();
		PasoCorrida primerPasoCorrida = this.getPasoCorrida(PasoCorrida.PASO_UNO);
		if (primerPasoCorrida == null){
			log.error("Primer Paso corrida del paso actual no encontrado. PasoActual = " + pasoActual );
			addRecoverableError(ProError.PASO_CORRIDA_NO_ENCONTRADO);
			return null;
		}
		 */
		AdpRun.getRun(this.getId()).reset(true);
		
		// setear el paso actual al primer pasoCorrida
		//this.setPasoActual(PasoCorrida.PASO_UNO);
		
		// paso a estado en preparacion, el estado de la corrida del primer paso
		//primerPasoCorrida.setEstadoCorrida(EstadoCorrida.getById(EstadoCorrida.ID_EN_PREPARACION));
		//primerPasoCorrida.setObservacion("");
		
		//this.deletePasoCorridaDesdePaso(PasoCorrida.PASO_DOS);
		
		//ProDAOFactory.getPasoCorridaDAO().update(primerPasoCorrida);
		//ProDAOFactory.getCorridaDAO().update(this);
		
	}


	/**
	 * Hace el toVO(0, false) y setea el estado con toVO(0, false)
	 * @return
	 * @throws Exception
	 */
	public CorridaVO toVOWithDesEstado() throws Exception{
		CorridaVO corridaVO = (CorridaVO) this.toVO(0, false);
		corridaVO.setEstadoCorrida((EstadoCorridaVO) this.estadoCorrida.toVO(0, false));
		
		return corridaVO;
	}
	
	/* no usar desde aca, llamar a adp
	public int deletePasoCorridaDesdePaso (Integer paso){

		return ProDAOFactory.getPasoCorridaDAO().deleteByCorridaDesdePaso(this, paso);
	}
	*/
	
	public void setearBussinessFlags(CorridaVO	corridaVO) {

		long estadoActual = this.getEstadoCorrida().getId().longValue();
		Integer pasoActual = this.getPasoActual();
		// activar
		Boolean activarBussEnabled = (estadoActual == EstadoCorrida.ID_EN_PREPARACION ||
				estadoActual == EstadoCorrida.ID_EN_ESPERA_CONTINUAR || 
				estadoActual == EstadoCorrida.ID_PROCESADO_CON_ERROR || 
				estadoActual == EstadoCorrida.ID_ABORTADO_POR_EXCEPCION);
		corridaVO.setActivarBussEnabled(activarBussEnabled);

		// reiniciar
		Boolean reiniciarBussEnabled = (estadoActual == EstadoCorrida.ID_EN_ESPERA_CONTINUAR || 
				estadoActual == EstadoCorrida.ID_PROCESADO_CON_ERROR || 
				estadoActual == EstadoCorrida.ID_ABORTADO_POR_EXCEPCION);
		corridaVO.setReiniciarBussEnabled(reiniciarBussEnabled);
		
		// refrescar
		corridaVO.setRefrescarBussEnabled(Boolean.TRUE);

		// cancelar
		Boolean cancelarBussEnabled = (estadoActual == EstadoCorrida.ID_EN_ESPERA_COMENZAR);
		corridaVO.setCancelarBussEnabled(cancelarBussEnabled);

		// siguientePaso
		Boolean siguientePasoBussEnabled = corridaVO.getSiguientePasoBussEnabled();
		if(siguientePasoBussEnabled != null){
			siguientePasoBussEnabled = (estadoActual == EstadoCorrida.ID_EN_ESPERA_CONTINUAR);
			corridaVO.setSiguientePasoBussEnabled(siguientePasoBussEnabled);
		}
		
		// retrocederPaso
		Boolean retrocederPasoBussEnabled = corridaVO.getRetrocederPasoBussEnabled();
		if(retrocederPasoBussEnabled != null){
			retrocederPasoBussEnabled = ((estadoActual == EstadoCorrida.ID_EN_ESPERA_CONTINUAR || 
					estadoActual == EstadoCorrida.ID_PROCESADO_CON_ERROR) && pasoActual > 1);
			corridaVO.setRetrocederPasoBussEnabled(retrocederPasoBussEnabled);
		}

		// modificar encabezado
		Boolean modificarEncabezadoBussEnabled = (estadoActual == EstadoCorrida.ID_EN_PREPARACION);
		corridaVO.setModificarEncabezadoBussEnabled(modificarEncabezadoBussEnabled);
	}

	/**
	 * Borra la lista de FileCorrida asociada a la Corrida y al nombre
	 * @param  nombre
	 * @return int
	 * @throws Exception
	 */
	public int deleteListFileCorrida(String nombre) throws Exception {
		return ProDAOFactory.getFileCorridaDAO().deleteAllByCorridaNombre(this, nombre);
	}	

	public List<FileCorrida> getListFileCorrida(String nombre) throws Exception {
		return ProDAOFactory.getFileCorridaDAO().getListByCorridaNombre(this, nombre);
	}	

	
}
