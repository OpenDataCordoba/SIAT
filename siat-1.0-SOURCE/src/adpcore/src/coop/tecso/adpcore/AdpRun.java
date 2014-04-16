//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import coop.tecso.adpcore.engine.AdpDao;
import coop.tecso.adpcore.engine.AdpLog;
import coop.tecso.adpcore.engine.AdpParameter;

/**
 * Clase de una Corrida de Procesos de ADP. Desde esta clase se pueden 
 * modificar datos asociados a la corrida, asi como su estado estado,
 * y manipular la informacion de logs.
 *
 * @author Coop. Tecso Ltda.
 */
public class AdpRun {
	
	private static Logger log = Logger.getLogger(AdpRun.class);
	protected long lastChangeMessageTime = 0;

	protected Long id = 0L;
	protected Long processId = 0L;

	protected Date fechaInicio = new Date();
	protected Date fechaFin = new Date();
	protected Date fechaUltResume  = new Date();
	protected Long idEstadoCorrida = 0L;
	protected String mensajeEstado = "";
	protected String observacion = "";
	protected Long pasoActual = 0L;
	protected String desCorrida = "";
	protected String usuario = "";
	
	protected AdpProcess process = null;
	protected Map<String, AdpParameter> parameters = null;
	protected AdpLog adplog = new AdpLog(0, "/tmp/adpdefault.log");
	protected BufferedReader inputFileReader = null;
	protected Object worker = null;
	
	// Contador de registros procesados
	private Long regCounter = 0L;
	// Cantidad Total de registros a Procesar
	private Long totalRegs	= 0L; 
	
	// Manejo de Errores de la Corrida
 	private StringBuilder errorBuffer   = new StringBuilder();
	int totalErrors = 0;
	String errorMarker = "\t(!)";

	// Manejo de Warnings de la Corrida
	private StringBuilder warningBuffer = new StringBuilder();
	int totalWarnings = 0;
	String warningMarker = "\t(*)";

	public AdpRun() throws Exception {
		this.process = null;
		this.setIdEstadoCorrida(AdpRunState.VOLATIL.id());
		this.parameters = Collections.synchronizedMap(new HashMap<String, AdpParameter>());
	}
	
	/**
	 * Obtiene el Id de corrida
	 * @return id de corrida.
	 */
	public Long getId() {
		return this.id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
		
	/**
	 * @return the processId
	 */
	public Long getProcessId() {
		return processId;
	}

	/**
	 * @param processId the processId to set
	 */
	public void setProcessId(Long processId) {
		this.processId = processId;
	}

	/**
	 * @return the fechaFin
	 */
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * @param fechaFin the fechaFin to set
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return the fechaInicio
	 */
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * retorna el estado actual de la corrida
	 */
	public AdpRunState getCurrentState() {
		return AdpRunState.getById(this.idEstadoCorrida);
	}

	/**
	 * @param fechaInicio the fechaInicio to set
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return the fechaUltResume
	 */
	public Date getFechaUltResume() {
		return fechaUltResume;
	}

	/**
	 * @param fechaUltResume the fechaUltResume to set
	 */
	public void setFechaUltResume(Date fechaUltResume) {
		this.fechaUltResume = fechaUltResume;
	}

	/**
	 * Obtiene el estado de la corrida
	 * @return idEstadoCorrida.
	 */
	public Long getIdEstadoCorrida() {
		return idEstadoCorrida;
	}

	/**
	 * Modifica el estado de la corrida.
	 * NO UTILIZE ESTE METOD. Para cambiar el estado de una corrida usar: changeState()
	 * @throws IllegalStateException si se intenta pasar a un estado no permitido.
	 * @param idEstadoCorrida the idEstadoCorrida to set
	 */
	public void setIdEstadoCorrida(Long idEstadoCorrida) throws Exception {
		this.idEstadoCorrida = idEstadoCorrida;
	}

	/**
	 * @return the mensajeEstado
	 */
	public String getMensajeEstado() {
		return mensajeEstado;
	}

	/**
	 * @param mensajeEstado the mensajeEstado to set
	 */
	public void setMensajeEstado(String mensajeEstado) {
		this.mensajeEstado = mensajeEstado;
	}

	/**
	 * Obtiene el Mensaje de Estado de la corrida.
	 * Este es un mensaje descriptivo de estado actual de la corrida.
	 * @return the observacion
	 */
	public String getObservacion() {
		return observacion;
	}

	/**
	 * Modifica el Mensaje de Estado de la corrida.
	 * Este es un mensaje descriptivo de estado actual de la corrida.
	 * @param observacion the observacion to set
	 */
	public void setObservacion(String observacion) {
		this.observacion = observacion;
	}

	/**
	 * Obtiene el valor de numero de Paso.
	 * @return numero de paso.
	 */
	public Long getPasoActual() {
		return this.pasoActual;
	}

	/**
	 * Modifica el numero de paso de la corrida
	 */
	public void setPasoActual(Long value) {
		this.pasoActual = value;
	}
	
   	/**
	 * @return the desCorrida
	 */
	public String getDesCorrida() {
		return desCorrida;
	}

	/**
	 * @param desCorrida the desCorrida to set
	 */
	public void setDesCorrida(String desCorrida) {
		this.desCorrida = desCorrida;
	}

   	/**
	 * @return the usuario
	 */
	public String getUsuario() {
		return this.usuario;
	}

	/**
	 * @param usuario the usuario to set
	 */
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	
	/**
	 * Obtiene el valor de un parametro asociada al proceso.
	 * @return valor del parametro. Si el parametro no existe lanza una excepcion.
	 */
	public String getParameter(String key) {
		if (!this.parameters.containsKey(key)) {
			log.debug("El parametro '" + key + "' no existe en los parametros del proceso " + this.getProcess().getCodProceso() 
						+ ". Verifique mayusculas y minusculas y que el parametro pertenezca al proceso.");
			return null;
		}
		return this.parameters.get(key).getValue();
	}

	/**
	 * Obtiene el parametro asociada al proceso.
	 * @return parametro. Si el parametro no existe lanza una excepcion.
	 */
	public AdpParameter getAdpParameter(String key) {
		return this.parameters.get(key);
	}

	/**
	 * Coloca o modifica el valor de un parametro asociado al proceso.
	 * Si el parametro lo crea y le asocia el valor.
	 * Si existe actualiza el valor en la base de datos.
	 */
	public void putParameter(String key, String value) throws Exception {		
		AdpParameter p = this.parameters.get(key);
		if (p == null) {
			p = new AdpParameter();
			p.setKey(key);
			this.parameters.put(key, p);
		}
		p.setTemporal(false);
		p.setValue(value);
		AdpDao.updateParameterValue(this.id, p);
	}

	
	/**
	 * Elimina todos los parametros temporales asociados a la corrida.
	 * @throws Exception
	 */
	public void putTempParameter(String key, String value) throws Exception {
		AdpParameter p = this.parameters.get(key);
		if (p == null) {
			p = new AdpParameter();
			p.setKey(key);
		}
		p.setTemporal(true);
		p.setValue(value);
		AdpDao.updateParameterValue(this.id, p);
	}
	
	/**
	 * Elimina todos los parametros temporales asociados a la corrida.
	 * @throws Exception
	 */
	public void clearTempParameters() throws Exception {
		AdpDao.deleteTempParameters(this.id);
		this.parameters.clear();
		AdpDao.loadRunParameters(this);
	}
	
	/**
	 * Coloca o modifica el valor de un parametro asociado al proceso.
	 * Si el parametro no existe lo añade al grup de parametros, si existe
	 * actualiza el valor.
	 */
	public void loadParameter(String key, AdpParameter param) {
		this.parameters.put(key, param);
	}
	
	/**
	 * Setea la marca de si un proceso esta lockeado o no.
	 * Cuando un proceso esta lockeado, ADP no inicia ninguna corrida
	 * ya sea por arribo de archivo o por proceso programado a determinada hora.
	 * @return Instancia del proceso.
	 * @throws IllegalArgumentException Si runId no corresponde a una corrida existente
	 */
	static public void lockProcess(String codProceso) throws Exception {
		AdpDao.updateLock(codProceso, 1L);
	}

	/**
	 * De Setea la marca de si un proceso esta lockeado o no.
	 * Cuando un proceso esta lockeado, ADP no inicia ninguna corrida
	 * ya sea por arribo de archivo o por proceso programado a determinada hora.
	 * @return Instancia del proceso.
	 * @throws IllegalArgumentException Si runId no corresponde a una corrida existente
	 */
	static public void unLockProcess(String codProceso) throws Exception {
		AdpDao.updateLock(codProceso, 0L);
	}


	/**
	 * Obtiene una corrida a partir de su id de corrida
	 * @return Instancia del proceso.
	 * @throws IllegalArgumentException Si runId no corresponde a una corrida existente
	 */
	static public AdpRun getRun(Long runId) throws Exception {
		AdpRun run = new AdpRun();
		run.initExistentRun(runId);
		return run;
	}

	/**
	 * Inicializa un corrida existente desde su runId
	 * @param runId
	 * @throws Exception
	 */
	private void initExistentRun(Long runId) throws Exception {
		AdpEngine.loadRunData(runId, this);
		this.adplog = new AdpLog(runId, null);
		this.process = new AdpProcess();
		AdpEngine.loadProcessDefData(this.processId, this.process);
	}

	/**
	 * Borra una corrida de la db siempre que la integridad referencial lo permita.
	 * @return true si se elimino corerctamente.
	 * @throws IllegalArgumentException Si runId no corresponde a una corrida existente
	 */
	static public boolean deleteRun(Long runId) throws Exception {
		return AdpDao.deleteRun(runId) != 0; 
	}

	/**
	 * Borra los archivos fisicos de una corrida
	 * @return true si se elimino corerctamente.
	 * @throws IllegalArgumentException Si runId no corresponde a una corrida existente
	 */
	static public void deleteFiles(Long runId) throws Exception {
		AdpDao.deleteFiles(runId); 
	}

	/**
	 * Crea una intancia de la clase AdpProcess para el proceso 'processName' 
	 * del argumento, el argumento es case sensitive. NOTA: Este metodo solo 
	 * instancia la clase. Para crear la corrida en la DB y dejarla en estado 
	 * 'En Preparación', se debe ejecutar el metodo 'create()'
	 * @return Instancia del proceso.
	 * @throws IllegalArgumentException Si processName no existente
	 */
	static public AdpRun newRun(String processName, String description) throws Exception {
		Long id = AdpEngine.getProcessId(processName);
		return newRun(id, description);
	}
	
	/**
	 * Crea una intancia de AdpRun para el proceso 'processId' del argumento.<br>
	 * NOTA: Este metodo solo instancia la clase. Para crear la corrida en la DB 
	 * y dejarla en estado 'En Preparación', se debe ejecutar el metodo 'create()'
	 * Mientras tanto la corrida se encuentra en estado VOLATIL
	 * @return Instacia del proceso.
	 * @throws IllegalArgumentException Si processId no existente
	 */
	static public AdpRun newRun(Long processId, String description) throws Exception {
		AdpRun run = new AdpRun();
		run.setDesCorrida(description);
		run.initNewRun(processId);
		return run;
	}

	/**
	 * Cambia el mensaje de estado de una corrida
	 */
	public static boolean changeRunMessage(String msg, int interval) throws Exception {
		AdpRun run = AdpRun.currentRun();
		if (run != null) {
			return run.changeMessage(msg, interval);
		}
		return false;
	}

	/**
	 * Genera un log en el archivo de logs
	 */
	public static void logRun(String msg) throws Exception {
		AdpRun run = AdpRun.currentRun();
		if (run != null) {
			run.logDebug(msg);
		}
	}

	/**
	 * Genera un log en el archivo de logs
	 */
	static public void logRun(String msg, Throwable t) throws Exception {
		AdpRun run = AdpRun.currentRun();
		if (run != null) {
			run.logDebug(msg, t);
		}
	}

	/**
	 * Inicializa una corrida que no existe en la DB
	 * @param processId
	 * @throws Exception
	 */
	private void initNewRun(Long processId) throws Exception {
		this.setProcessId(processId);
		this.process = new AdpProcess();
		AdpEngine.loadProcessDefData(processId, this.process);
		AdpEngine.loadRunData(0L, this);
	}

	/**
	 * Da de alta una corrida de proceso en la DB, y la deja en estado En Preparación.
	 */
	public void create() throws Exception {
		AdpEngine.createRun(this, parameters);
		this.adplog = new AdpLog(this.getId(), null);
		changeState(AdpRunState.PREPARACION, "", true, null);
	}
	
	/**
	 * Borra el archivo de log que se espera este en el sub directorio de corrida where
	 * @param where
	 */
	private void removeLogFile(AdpRunDirEnum where) {
		File file = new File(getLogFilename(where));
		file.delete();
	}

	/**
	 * Borra el archivo de input que se espera este en el sub directorio de corrida where
	 * @param where
	 */
	private void removeInputFile(AdpRunDirEnum where) {
		File file = new File(getInputFilename(where));
		file.delete();
	}

	/**
	 * Inicia una corrida para ejecución.
	 * La corrida puede estar en estado "En Preparacion", "En EsperaContinuar", 
	 * o puede ser una corrida que aun no existe.<br>
	 * El paramettro scheduleDate, es la fecha y hora en la que la corrida debe 
	 * comenzar. Si es null, o con una fecha menor a la acutal, o es un proceso de tipo AdpProcess.TIPO_ARRIBO
	 * la corrida iniciara inmediatamante.
     * @throws IllegalStateException si el proceso no es encuentra en alguno de los 
	 * estado de arriba.
	 */
	public boolean execute(Date scheduleDate) throws Exception {
		//los procesos que son por arribo de arrchivo son invocados inmediatamente.
		if (process.getTipoEjecucion() == AdpProcess.TIPO_ARRIBODIR) {
			log.debug(this.toString() + " Iniciando proceso tipo arribo.");
			this.fechaInicio = new Date();
			AdpEngine.executeFileArribe(this);
			return true;
		} else {
			log.debug(this.toString() + " Iniciando proceso tipo Manual.");
			this.fechaInicio = scheduleDate == null ? new Date() : scheduleDate;
			return this.changeState(AdpRunState.ESPERA_COMENZAR, null, false);
		}
	}

	/**
	 * Inicia una corrida para ejecución y  espera a que termine.
	 * La corrida puede estar en estado "En Preparacion", "En EsperaContinuar", 
	 * o puede ser una corrida que aun no existe.<br>
	 * El paramettro scheduleDate, es la fecha y hora en la que la corrida debe 
	 * comenzar. Si es null, o con una fecha menor a la acutal, la corrida iniciara 
	 * inmediatamante.
     * @throws IllegalStateException si el proceso no es encuentra en alguno de los 
	 * estado de arriba.
	 */
	public void executeAndWait() throws Exception {
		this.fechaInicio = null;
		this.changeState(AdpRunState.ESPERA_COMENZAR, null, false);
		AdpEngine.executeScheduled(this);
	}

	/**
	 * Cancela los schedule date si hubiese. (Desprograma la corrida)
	 */
	public boolean cancel() throws Exception {
		return changeState(AdpRunState.CANCEL, "Cancelado: " + AdpUtil.formatDate(new Date()), false);	
	}
	
	/**
	 * Reinicializa la corrida. La pasa a estado en Preparacion.
	 */
	public boolean reset() throws Exception {
		return this.reset(true);
	}
	
	
	/**
	 * Reinicializa la corrida. La pasa a estado en Preparacion.
	 */
	public boolean reset(boolean reiniciarPaso) throws Exception {
		if(reiniciarPaso)
			this.pasoActual = 1L;
		removeLogFile(AdpRunDirEnum.LOGS);
		return changeState(AdpRunState.PREPARACION, "Reiniciado el: " + AdpUtil.formatDate(new Date()), false);
	}
	
	public String infoString() {
		String sf = "id=%d;idEstadoCorrida=%s,mensaje='%s'";
		String ret = String.format(sf, this.getId(), this.getIdEstadoCorrida(),this.getMensajeEstado());
		return ret;
	}

	/**
	 * Obtiene el proceso de la corrida
	 * @return el proceso
	 */
	public AdpProcess getProcess() {
		return this.process;
	}

	/**
	 * Obtiene el nombre solo del archivo que arribo.
	 */
	public String getInputFilename() {
		String result = "";
		try {
			String infile = parameters.get(AdpParameter.INPUTFILEPATH).getValue();
			File f = new File(infile);
			result = f.getName();
		} catch (Exception e) {
		}
		return result;
	}

	/**
	 * Calcula el path absoluto del input filename
	 * @param where indica el subdirectorio de corrida y se utiliza para obtener 
	 *  el path completo del archivo de entrada utilizando el directorio where
	 * @return path absoluto del archivo de entrada como si estuviese en where.
	 *         retorna "" si la corrida no posee el parametro INPUTFILEPATH
	 */
	public String getInputFilename(AdpRunDirEnum where) {		
		String filename = getInputFilename();
		String path = process.getInputFilePath(where) + "/" + filename;
		File f = new File(path);
		return f.getAbsolutePath();
	}

	/**
	 * Obtiene el directorio calculado a partir del directorio del proceso
	 * y el subdir de corrida pasado en el parametro.
	 * @param subdir Subdirectorio de corrida: recibido, procesando, etc
	 * @return Nombre absoluto del directorio. NOTA: El nombre de dir NO posee caracter '/'
	 */
	public String getProcessDir(AdpRunDirEnum subdir) {
		return process.getInputFilePath(subdir);
	}
	
	/**
	 * Calcula el path absoluto del log filename
	 * @param where indica el subdirectorio de corrida y se utiliza para obtener 
	 *  el path completo del archivo de entrada utilizando el directorio where
	 * @return path absoluto del archivo de entrada como si estuviese en where
	 *         "" Si la corrida no es una corrida iniciado por arribo
	 */
	protected String getLogFilename(AdpRunDirEnum where) {		
		String filename = null;
		if(getInputFilename() != null && !"".equals(getInputFilename())){
			filename = getInputFilename() + "." + this.id + ".adplog";			
		}else{
			filename = process.getCodProceso() + "." + this.id + ".adplog";
		}
		String path = process.getInputFilePath(where) + "/" + filename;
		File f = new File(path);
		return f.getAbsolutePath();
	}


	/**
	 * Mueve el archivo del parametro de un subdirectorio de corrida a otro
	 * @param from subdir de corrida actual del archivo de input
	 * @param to subdir destino al que se quiere mover
	 */
	public void moveFile(String filename, AdpRunDirEnum from, AdpRunDirEnum to) {
		String di = process.getDirectorioInput();
		if (di == null || di.equals("null") || di.equals("")) {
			return;
		}

		String path = process.getInputFilePath(from) + "/" + filename; //directorio del proceso + subdir proc + filename
		File fileFrom = new File(path);
		File dirTo = new File(process.getInputFilePath(to));
		
		//si no existe intentamos crearlo
		if (!dirTo.isDirectory()) {
			dirTo.mkdir(); 
		}

		// cerramos el file reader antes de moverlo
		try { this.inputFileReader.close(); } catch (Exception e) {	}
		this.inputFileReader = null;

		fileFrom.renameTo(new File(dirTo, fileFrom.getName()));
	}

	/**
	 * Mueve el archivo de input de un subdirectorio de corrida a otro
	 * @param from subdir de corrida actual del archivo de input
	 * @param to subdir destino al que se quiere mover
	 */
	public void moveInputFile(AdpRunDirEnum from, AdpRunDirEnum to) {
		String di = process.getDirectorioInput();
		if (di == null || di.equals("null") || di.equals("")) {
			return;
		}

		File fileFrom = new File(getInputFilename(from));		
		File dirTo = new File(process.getInputFilePath(to));
		
		//si no existe intentamos crearlo
		if (!dirTo.isDirectory()) {
			dirTo.mkdir(); 
		}

		// cerramos el file reader antes de moverlo
		try { this.inputFileReader.close(); } catch (Exception e) {	}
		this.inputFileReader = null;

		fileFrom.renameTo(new File(dirTo, fileFrom.getName()));
	}

	/**
	 * Mueve el archivo de input de un subdirectorio de corrida a otro y lo renombra agregando el sufijo pasado.
	 * @param from subdir de corrida actual del archivo de input
	 * @param to subdir destino al que se quiere mover
	 * @param sufix que se le agrega al final del nombre
	 */
	public void moveInputFileSufix(AdpRunDirEnum from, AdpRunDirEnum to, String sufix) {
		String di = process.getDirectorioInput();
		if (di == null || di.equals("null") || di.equals("")) {
			return;
		}

		File fileFrom = new File(getInputFilename(from));		
		File dirTo = new File(process.getInputFilePath(to));
		
		//si no existe intentamos crearlo
		if (!dirTo.isDirectory()) {
			dirTo.mkdir(); 
		}

		// cerramos el file reader antes de moverlo
		try { this.inputFileReader.close(); } catch (Exception e) {	}
		this.inputFileReader = null;

		fileFrom.renameTo(new File(dirTo, fileFrom.getName()+sufix));
	}
	
	/**
	 * Copia el archivo de input de un subdirectorio de corrida a otro y lo renombra agregando el sufijo pasado.
	 * @param from subdir de corrida actual del archivo de input
	 * @param to subdir destino al que se quiere mover
	 * @param sufix que se le agrega al final del nombre
	 */
	public boolean copyInputFileSufix(AdpRunDirEnum from, AdpRunDirEnum to, String sufix) {
		String di = process.getDirectorioInput();
		if (di == null || di.equals("null") || di.equals("")) {
			return false;
		}

		File fileFrom = new File(getInputFilename(from));		
		File dirTo = new File(process.getInputFilePath(to));
		
		//si no existe intentamos crearlo
		if (!dirTo.isDirectory()) {
			dirTo.mkdir(); 
		}
		
		// cerramos el file reader antes de moverlo
		try { this.inputFileReader.close(); } catch (Exception e) {	}
		this.inputFileReader = null;
		try { 
			AdpRun.copyFile(fileFrom, new File(dirTo, fileFrom.getName()+sufix)); 
			return true;
		} catch (Exception e){
			return false;
		}
	}
	
	/**
	 * Mueve el archivo de log de un subdirectorio de corrida a otro
	 * @param from subdir de corrida actual del archivo de log
	 * @param to subdir destino al que se quiere mover
	 */
	public void moveLogFile(AdpRunDirEnum from, AdpRunDirEnum to) {
		String di = process.getDirectorioInput();
		if (di == null || di.equals("null") || di.equals("")) {
			return;
		}

		File fileFrom = new File(getLogFilename(from));
		File dirTo = new File(process.getInputFilePath(to));
		log.debug(this.toString() + ": Moviendo log file from:" + fileFrom.getPath() + " to: " + dirTo.getPath());
		
		//si no existe intentamos crearlo
		if (!dirTo.isDirectory()) {
			dirTo.mkdir(); 
		}

		// cerramos el log file
		adplog.close();
		fileFrom.renameTo(new File(dirTo, fileFrom.getName()));

		String logFilename = getLogFilename(to);
		log.debug(this.toString() + ": Re-Abriendo log file en:" + logFilename);
		this.adplog = new AdpLog(this.id, logFilename);
	}

	/**
	 * Obtiene un redear con buffer para parcear el archivo de input.
	 * Para obtener cada linea usar readline()
	 * @param where subdir de corrida donde se encuentra el archivo
	 * @return
	 * @throws Exception
	 */
	public BufferedReader getInputFileReader(AdpRunDirEnum where) throws Exception {
		if (this.inputFileReader == null) {
			String filename = getInputFilename(where);
			this.inputFileReader = new BufferedReader(new FileReader(filename));
		}
        return this.inputFileReader;
	}
	

	/**
	 * Cambia el estado a FIN_OK e incrementa el numero de paso
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 */
	public boolean changeStateFinOk(String message) {
		return changeState(AdpRunState.FIN_OK, message, true);
	}

	/**
	 * Cambia el estado a FIN_ADVERT e incrementa el numero de paso
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 */
	public boolean changeStateFinAdvertencia(String message) {
		return changeState(AdpRunState.FIN_ADVERT, message, true);
	}

	/**
	 * Cambia el estado a Espera contunar e incrementa el numero de paso
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 */
	public boolean changeStateEsperaContinuar(String message) {
		return changeState(AdpRunState.ESPERA_CONTINUAR, message, true);
	}
	
	/**
	 * Cambia el estado a Error
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 */
	public boolean changeStateError(String message, Throwable e) {
		return changeState(AdpRunState.FIN_ERROR, message, false, e);
	}

	/**
	 * Cambia el estado a Error
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 */
	public boolean changeStateError(String message) {
		return changeState(AdpRunState.FIN_ERROR, message, false, null);
	}

	/**
	 * Cambia el estado de una tarea con todo lo que ello implica.
	 * @param state estado al cual pasar la corrida
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 * @param changeStep true si se desea que al cambiar el estado tambien se incremente el nuemero de paso
	 */
	public boolean changeState(AdpRunState state, String message, boolean incrementStep) {
		return changeState(state, message, incrementStep, null);
	}
		
	/**
	 * Cambia el estado de una tarea con todo lo que ello implica.
	 * @param state estado al cual pasar la corrida
	 * @param message mensaje de observacion de la corrida. si es null, no lo modifica.
	 * @param changeStep true si se desea que al cambiar el estado tambien se incremente el nuemero de paso
	 * @param e null o exception a logear en adplog
	 */
	public boolean changeState(AdpRunState state, String message, boolean incrementStep, Throwable e) {
		boolean cambioValido = false;
		AdpRunState estadoEjecucionPaso = null;

		//CANCEL: Desprograma la corrida, solo valido si esta en ESPERA_COMENZAR
		if (isStateIn(state, AdpRunState.CANCEL) && isStateIn(idEstadoCorrida, AdpRunState.ESPERA_COMENZAR)) {
			state = AdpRunState.ESPERA_CONTINUAR;
			estadoEjecucionPaso = AdpRunState.ESPERA_CONTINUAR;
			cambioValido = true;
		//MSG: Cambia el mensaje de descripcion
		} else if (isStateIn(state, AdpRunState.MSG)) {
			state = getCurrentState();
			estadoEjecucionPaso = getCurrentState();
			cambioValido = true;			
		//STEP: Cambio al siguiente paso
		} else if (isStateIn(state, AdpRunState.STEP)) {
			state = AdpRunState.ESPERA_CONTINUAR;
			estadoEjecucionPaso = AdpRunState.FIN_OK;
			cambioValido = true;			
		//PREPARACION:
		} else if (isStateIn(state, AdpRunState.PREPARACION) && isStateIn(idEstadoCorrida, AdpRunState.VOLATIL)) {
			log.info(this.toString() + " Cambio estado a: " + state.toString() + "(Desde VOLATIL)");
			this.fechaInicio = new Date();
			this.fechaFin = null;
			cambioValido = true;
			estadoEjecucionPaso = AdpRunState.PREPARACION;

		// Vuelta a Cambio a EnPreparacion: solo valido sino estoy en Procesando
		} else if (isStateIn(state, AdpRunState.PREPARACION) && !isStateIn(idEstadoCorrida, AdpRunState.PROCESANDO)) {
			log.debug(this.toString() + " Cambio estado a: " + state.toString() + "(Reset)");
			this.fechaInicio = null;
			this.fechaFin = null;
			
			//actualizamos fecha finalizacion
			estadoEjecucionPaso = AdpRunState.PREPARACION;
			cambioValido = true;
		
		// Cambio a Procesando
		} else if (isStateIn(state, AdpRunState.PROCESANDO)) {
			log.info(this.toString() + " Cambio estado a: " + state.toString());
			logDebug("Comienza corrida: " + this.toString());
			logDebug("    Datos de corrida: " + this.infoString());

			//actualizamos fecha finalizacion
			this.fechaFin = null;
			
			//movemos archivos
			moveInputFile(AdpRunDirEnum.RECIBIDO, AdpRunDirEnum.PROCESANDO);
			//moveLogFile(AdpRunDirEnum.RECIBIDO , AdpRunDirEnum.PROCESANDO);
			estadoEjecucionPaso = AdpRunState.PROCESANDO; //dejamos intacto el pasoCorrida
			cambioValido = true;
		
		// Cambio a finok o espera continuar
		} else if (this.idEstadoCorrida == AdpRunState.PROCESANDO.id() 
				&& isStateIn(state, AdpRunState.FIN_OK, AdpRunState.ESPERA_CONTINUAR, AdpRunState.FIN_ADVERT)) {
			log.info(this.toString() + " Cambio estado a: " + state.toString());
			//Movemos archivos. TODO: aqui tendriamos que borrarlos segun algun config de adp
			moveInputFile(AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);
			//moveLogFile(AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_OK);

			//actualizamos fecha finalizacion
			this.fechaFin = new Date();
			if (state.equals(AdpRunState.FIN_ADVERT)) {
				estadoEjecucionPaso = AdpRunState.FIN_ADVERT;
			} else {
				estadoEjecucionPaso = AdpRunState.FIN_OK;
			}
			cambioValido = true;
		
		// Si estoy en procesando e intento pasar a error a excepcion
		} else if (this.idEstadoCorrida == AdpRunState.PROCESANDO.id()
				&& isStateIn(state, AdpRunState.FIN_ERROR, AdpRunState.ABORT_EXCEP)) {
			
			log.info(this.toString() + ": Cambio estado a: " + state.toString() + "\n" + message, e);
			if (e != null) {
				logError("Ocurrio una excepcion durante esta corrida. ", e);
			}
			
			//Movemos archivos.
			moveInputFile(AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);
			//moveLogFile(AdpRunDirEnum.PROCESANDO, AdpRunDirEnum.PROCESADO_ERROR);

			//actualizamos fecha finalizacion
			this.fechaFin = new Date();
			estadoEjecucionPaso = AdpRunState.FIN_ERROR;
			cambioValido = true;
			
		// cambio a en espera comenzar: solo valido desde EnPreparacion y EnEsperaContinuar y En EsperaComenzar, En error y AbortadoExce
		} else if (isStateIn(idEstadoCorrida, AdpRunState.PREPARACION, 
							 AdpRunState.ESPERA_CONTINUAR, 
							 AdpRunState.ESPERA_COMENZAR, 
							 AdpRunState.FIN_ERROR,
							 AdpRunState.ABORT_EXCEP) 
				   && isStateIn(state, AdpRunState.ESPERA_COMENZAR)) {
			log.debug(this.toString() + " Cambio estado a: " + state.toString());

			//actualizamos fecha finalizacion
			estadoEjecucionPaso = AdpRunState.ESPERA_COMENZAR;
			if (message == null) {
				message = "";
			}
			cambioValido = true;
		// cambio a estado Sin Procesar: solo valido desde Procesando -issue #7805
		} else if(isStateIn(idEstadoCorrida, AdpRunState.PROCESANDO) 
				&& isStateIn(state, AdpRunState.SIN_PROCESAR)){
			log.info(this.toString() + " Cambio estado a: " + state.toString() + " Desde: PROCESANDO");
			this.fechaFin = new Date();
			cambioValido = true;
			estadoEjecucionPaso = AdpRunState.SIN_PROCESAR;
		}else{
			log.info(this.toString() + " Cambio estado invalido. Fallo pasaje a:" + state.toString() + " idEstadoCorrida actual:" + idEstadoCorrida);
			return false;
		}

		if (cambioValido) {
			//cambiamos estado
			this.idEstadoCorrida = state.id();

			//actualizamos mensaje
			if (message !=  null) {
				this.mensajeEstado = message;
			}
							
			//actualizamo info del cambio de estado en las pasosCorrida
			//que viene a ser una especie de foto de la terminacion del proceso
			//la actualizamos solo cuando salimos del EnPreparacion o cuando cuando nos envian un mensaje.
			if (this.pasoActual > 0 || isStateIn(state, AdpRunState.MSG) ) {
				try { AdpDao.updatePasoCorrida(this, estadoEjecucionPaso.id()); } catch (Exception exp) { log.error("Error durante cambio de estado: No se pudo cambiar pasoCorrida", exp); }
			}
			
			//verificamos si incrementamos numero paso
			if (incrementStep) {
				this.pasoActual++;
			}

			if (e == null) {
				log.info("AdpRun:" + this + ": cambio de estado: " + state + "-" + message + " paso:" + pasoActual);
			} else {
				log.info("AdpRun:" + this + ": cambio de estado: " + state + "-" + message + " paso:" + pasoActual, e);
			}
			try { AdpDao.updateSate(this); } catch (Exception exp) { log.error("Error durante cambio de estado: No se pudo cambiar estado de la corrida.", exp); }
		}
		return true;
	}
	
	private boolean isStateIn(AdpRunState state, AdpRunState ... args) {
		for (AdpRunState obj : args) {
			if (obj.id() == state.id())
				return true;
		}
		return false;
	}

	private boolean isStateIn(long idState, AdpRunState ... args) {
		for (AdpRunState obj : args) {
			if (obj.id() == idState)
				return true;
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String ret = this.process.getCodProceso() + "@" + AdpEngine.nodeName() + "(corrida:" + this.id + ")";
		return ret;
	}
	
	/**
	 * Loguea un error en la base de datos en las tablas asociadas a la corrida 
	 * y en el archivo de log de la corrida.
	 * @param msg mensaje a loguear
	 * @throws Exception
	 */
	public void logError(String msg) {
		logError(msg, null);
	}
	
	/**
	 * Loguea un mensaje en el archivo de log de la corrida.
	 * @param msg mensaje a loguear
	 * @throws Exception
	 */
	public synchronized void logDebug(String msg) {
		logDebug(msg, null);
	}

	/**
	 * Loguea un error en la base de datos en las tablas asociadas a la corrida 
	 * y en el archivo de log de la corrida.
	 * @param msg mensaje a loguear
	 * @param e agrega el stacktrace de la exception
	 * @throws Exception
	 */
	public void logError(String msg, Throwable e) {
		this.adplog.log(this.pasoActual, AdpLog.FILE, msg, e);
	}

	/**
	 * Loguea un mensaje en el archivo de log de la corrida.
	 * @param msg mensaje a loguear
	 * @param e agrega el stacktrace de la exception
	 * @throws Exception
	 */
	public synchronized void logDebug(String msg, Throwable e) {
		this.adplog.log(this.pasoActual, AdpLog.FILE, msg, e);		
	}

	/**
	 * Util para leer una linea, retorna null, si estas al final del archivo o si no pudo
	 * leerla por una exception.
	 * @param is
	 * @return
	 */
	static public String readLine(BufferedReader is) {
		try {
			return is.readLine();
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Modifica el valor del parametro del InputFilePath asociado a la corrida
	 * @param filename nombre del archivo (sin paths). 
	 * 		  Este archivo sera ubicado en el directorio entrada/ del proceso (AdpRunDirEnum.ENTRADA)
	 * @return
	 */
	public void setInputFilename(String filename) throws Exception {
		String path = process.getInputFilePath(AdpRunDirEnum.ENTRADA) + "/" + filename;
		File f = new File(path);
		path = f.getAbsolutePath();		
		this.putParameter(AdpParameter.INPUTFILEPATH, path);
	}


	/* UserContext TLS Helper */
    private static ThreadLocal<AdpRun> adpRunTls = new ThreadLocal<AdpRun>();
    public static AdpRun currentRun() throws Exception {    	
    	AdpRun run  = adpRunTls.get();
        return run;  
    }

    protected static void setCurrentRun(AdpRun run) {
		adpRunTls.set(run);
    }

	/**
	 * Cambia el Mensaje de estado de la corrida y del pasoCorrida
	 * Este mensaje es una cadena informativa que comenta sobre el estado / paso actual de la corrida.
	 * Pej: El paso 2 final Ok ahora puede revisar los reportes.
	 */
	public void changeMessage(String msg) throws Exception {
		changeState(AdpRunState.MSG, msg, false, null);
	}

	/**
	 * Cambia el Mensaje de estado de la corrida y del pasoCorrida, invoca a void changeMessage(String).
	 * El parametro interval, determina si se ejecuta o no el cambio de mensaje.
	 * Solo cambia si han transcurridos interval segundos desde la ultima llamada a este metodo.
	 * Si interval es 0, se actualiza siempre.
	 */
	public boolean changeMessage(String msg, int interval) throws Exception {
		long time = new Date().getTime() / 1000;
		if (interval <=  0 || time > lastChangeMessageTime + interval) {
			lastChangeMessageTime = time;
			changeState(AdpRunState.MSG, msg, false, null);
			return true;
		}
		return false;
	}

	/**
	 * Cambia la descripcion de la corrida.
	 * Este es un mensaje que se refiere mas a toda la corrida.
	 * Pej: Envio judicial nro.938472 creado el sarasa...
	 */
	public void changeDesCorrida(String des) throws Exception {
		AdpDao.updateDescription(this.id, des);
	}

	/**
	 * Cambia la descripcion de la corrida.
	 * Este es un mensaje que se refiere mas a toda la corrida.
	 * Pej: Envio judicial nro.938472 creado el sarasa...
	 * @deprecated
	 */
	public void xxchangeObservacion(String obs) throws Exception {
		AdpDao.updateObs(this.id, obs);
	}
	
	/**
	 * retorna una copia plana del mapa de parametros interno de adpRun 
	 * @return
	 */
	public Map<String, String> getParameters() {
		Map<String, String> m = new HashMap<String, String>();
		for(String key : this.parameters.keySet()) {
			m.put(key, this.parameters.get(key).getValue());
		}
		return m;
	}


	/**
	 *  Retorna el nombre del archivo de log con el path de la carpeta donde se encuentra segun el estado de la corrida.
	 *  
	 */
	public String getLogFileName() {
		String logFileName = null;
		
		logFileName = this.getLogFilename(AdpRunDirEnum.LOGS);

		return logFileName;
	}

	public Object getWorker(){
		return this.worker;
	}
	
	/**
	 * Obtiene la ultima corrida terminada ok para el proceso con codigo igual al pasado como parametro.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getLastEndOk(String codProceso) throws Exception {		
		
		Long runId = AdpDao.getLastRunEndOkIdByCodProcess(codProceso);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}
	
	/**
	 * Obtiene la ultima corrida terminada ok para el proceso con codigo igual al pasado como parametro.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getLastEndOk(String codProceso, String userName) throws Exception {		
		
		Long runId = AdpDao.getLastRunEndOkIdByCodProcess(codProceso, userName);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}

	/**
	 * Obtiene la corrida que este en procesando o por comenzar para el proceso con codigo igual al pasado como parametro.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getRunning(String codProceso) throws Exception {		
		
		Long runId = AdpDao.getRunningRunIdByCodProcess(codProceso);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}

	/**
	 * Obtiene la ultima corrida para el proceso con codigo igual al pasado como parametro.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getLastRun(String codProceso, String userName) throws Exception {
		Long runId = AdpDao.getLastRunIdByCodProcess(codProceso, userName);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}

	/**
	 * Obtiene la corrida que este en procesando o por comenzar para el proceso con codigo igual al pasado como parametro 
	 * y el usuario tambien indicado.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getRunning(String codProceso, String userName) throws Exception {		
		
		Long runId = AdpDao.getRunningRunIdByCodProcess(codProceso, userName);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}
	
	/**
	 * Eliminar todas las corridas anteriores para el proceso con codigo igual al pasado como parametro.
	 * @return true o false
	 */
	static public boolean cleanOldExceptLastOk(String codProceso) throws Exception {		
		
		Long lastRunId = AdpDao.getLastRunEndOkIdByCodProcess(codProceso);
		if(lastRunId == null)
			return false;
		List<Long> listOldestRunId =  AdpDao.getListOldestRunId(codProceso, lastRunId);
		for(Long runId: listOldestRunId){
			if(runId != null && runId != 0){
				if(!AdpRun.deleteRun(runId))
					return false;				
			}
		}
		
		return true;
	}
	
	/**
	 * Eliminar todas las corridas anteriores para el proceso menos esta.
	 * @return true o false
	 */
	public boolean cleanOld() throws Exception {		
		Long exceptRunId = this.getId();
		List<Long> listOldestRunId =  AdpDao.getListOldestRunId(this.getProcess().getCodProceso(), exceptRunId);
		for(Long runId: listOldestRunId){
			if(runId != null && runId != 0){
				
				if(!AdpRun.deleteRun(runId))
					return false;			
			}
		}
		
		return true;
	}
	
	
	public boolean cleanOld(String userName) throws Exception {		
		Long exceptRunId = this.getId();
		List<Long> listOldestRunId =  AdpDao.getListOldestRunId(this.getProcess().getCodProceso(), exceptRunId, userName);
		for(Long runId: listOldestRunId){
			if(runId != null && runId != 0){
				
				if(!AdpRun.deleteRun(runId))
					return false;			
			}
		}
		
		return true;
	}
	
	/**
	 * Devuelve una lista con los Ids de las Corridas del Proceso exceptuando la actual.
	 * @return lista de ids
	 */
	public List<Long> getListOldRunId() throws Exception {		
		Long exceptRunId = this.getId();
		return AdpDao.getListOldestRunId(this.getProcess().getCodProceso(), exceptRunId);
	}
	
	/**
	 * Devuelve una lista con los Ids de las Corridas del Proceso para el Usuario indicado exceptuando la actual.
	 * @return lista de ids
	 */
	public List<Long> getListOldRunId(String userName) throws Exception {		
		Long exceptRunId = this.getId();
		return AdpDao.getListOldestRunId(this.getProcess().getCodProceso(), exceptRunId, userName);
	}
	
	/**
	 * Obtiene la ultima corrida terminada para el proceso con codigo igual al pasado como parametro.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getLastEndWrong(String codProceso) throws Exception {		
		
		Long runId = AdpDao.getLastRunEndWrongIdByCodProcess(codProceso);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}
	
	/**
	 * Obtiene la ultima corrida terminada para el proceso con codigo igual al pasado como parametro y para el usuario indicado.
	 * @return idRun y null si no se encontro ninguno
	 */
	static public AdpRun getLastEndWrong(String codProceso, String userName) throws Exception {		
		
		Long runId = AdpDao.getLastRunEndWrongIdByCodProcess(codProceso, userName);
		if(runId == null || runId == 0)
			return null;
		
		return AdpRun.getRun(runId);
	}
	
	/**
	 * Borra todos archivos que hay dentro de un directorio.
	 * Esta funcion NO es recursiva a proposito. Y NO borra directorios.
	 */
	static public void deleteDirFiles(File path) {
		if (path.exists()) {
			File[] files = path.listFiles();
			for(int i=0; i<files.length; i++) {
				if(files[i].isFile()) {
					files[i].delete();
				}
			}
		}
	}
	
	// Copia src file a dst file.
    // Si dst no existe es creado
	// Si dst es un directorio copia el archivo manteniendo el nombre
	static public void copyFile(File src, File dst) throws IOException {
	        InputStream in = new FileInputStream(src);
	        if (dst.isDirectory()) {
	        	dst = new File(dst, src.getName());
	        }
	        OutputStream out = new FileOutputStream(dst);
	    
	        // Transfer bytes from in to out
	        byte[] buf = new byte[1024];
	        int len;
	        while ((len = in.read(buf)) > 0) {
	            out.write(buf, 0, len);
	        }
	        in.close();
	        out.close();
	    }

	/**
	 * Agrega el mensaje errorMsg al buffer de errores
	 */
	public synchronized void addError(String errorMsg) {
		this.totalErrors++;
		this.errorBuffer.append(errorMarker + "Error " + 
				this.totalErrors + ": " + errorMsg + '\n');
	}

	/**
	 * Agrega el mensaje errorMsg al buffer de errores
	 */
	public synchronized void addError(String errorMsg, Exception e) {
		String strException = "";
		if (e != null) {
	    	StringWriter result = new StringWriter();
	    	PrintWriter printWriter = new PrintWriter(result);
	    	e.printStackTrace(printWriter);
	    	strException = result.toString();
	    	strException = strException.replace('\n', '-');
	    }
		this.totalErrors++;
		this.errorBuffer.append(errorMarker + "Error " + 
				this.totalErrors + ": " + errorMsg + ".Causa: " + strException + '\n');
	}

	
	/**
	 * Retorna true si hay errores
	 */
	public synchronized boolean hasErrors() {
		return this.totalErrors > 0;
	}

	/**
	 * Vuelca el buffer de errores en el archivo de logs 
	 * de la Corrida
	 */
	public synchronized void dumpErrorBuffer() {
		this.errorBuffer.append(errorMarker + "Total de Errores: " + this.totalErrors);
		logDebug(this.errorBuffer.toString());
	}

	/**
	 * Agrega el mensaje warningMsg al buffer de warnings
	 */
	public synchronized void addWarning(String warningMsg, Exception e) {
		String strException = "";
		if (e != null) {
	    	StringWriter result = new StringWriter();
	    	PrintWriter printWriter = new PrintWriter(result);
	    	e.printStackTrace(printWriter);
	    	strException = result.toString();
	    	strException = strException.replace('\n', '-');
	    }
		this.totalWarnings++;
		this.warningBuffer.append(warningMarker + "Advertencia " + 
				this.totalWarnings + ": " + warningMsg + ".Causa: " + strException + '\n');
	}

	/**
	 * Agrega el mensaje warningMsg al buffer de warnings
	 */
	public synchronized void addWarning(String warningMsg) {
		this.totalWarnings++;
		this.warningBuffer.append(warningMarker + "Advertencia " + 
				this.totalWarnings + ": " + warningMsg + '\n');
	}
	
	/**
	 * Retorna true si hay warnings 
	 */
	public synchronized boolean hasWarnings() {
		return this.totalWarnings > 0;
	}

	/**
	 * Vuelca el buffer de warnings en el archivo de logs
	 * de la Corrida
	 */
	public synchronized void dumpWarningBuffer() {
		this.warningBuffer.append(warningMarker + "Total de Advertencias: " + this.totalWarnings);
		logDebug(this.warningBuffer.toString());
	}
	
	/**
	 * Setter de la cantidad de registros a procesar 
	 */
	public synchronized void setTotalRegs(Long totalRegs) {
		this.totalRegs = totalRegs;
	}

	/**
	 * Getter de la cantidad de registros a procesar 
	 */
	public synchronized Long getTotalRegs() {
		return this.totalRegs;
	}
	
	/**
	 * Incrementa en una la cantidad de registros procesados
	 */
	public synchronized void incRegCounter() {
		this.regCounter++;
	}

	/**
	 * Getter de la cantidad de registros procesados 
	 */
	public synchronized Long getRegCounter() {
		return this.regCounter;
	}

	/**
	 * Retorna el pocentaje de registros procesados
	 */
	public synchronized Long getRunStatus() {
		return (totalRegs > 0L) ? (regCounter*100L/totalRegs) : 0L; 
	}
	
	/**
	 * Cambia el mensaje de status de la corrida
	 * y ademas lo loguea en el archivo de logs 
	 */
	public synchronized void changeStatusMsg (String msg) throws Exception {
		changeMessage(msg);
		logDebug("Cambiando mensaje de status a: " + msg);
	} 

	/**
	 * Si puede (no tiene errores en los buffers), cambia el estado de una tarea 
	 * con todo lo que ello implica. Ademas, si tiene warnings, hace un dump del 
	 * buffer de warnings e informa al usuario.
	 * En caso de no poder, termina en el estado FIN_ERROR.
	 *  
	 * @param state:   estado al cual pasar la corrida
	 * @param message: mensaje de observacion de la corrida.
	 */
	public boolean changeState(AdpRunState state, String message) {
		Long pasoActual = getPasoActual();
		String infoMsg = message;
		
		if (hasWarnings()) {
			// Mensaje de advertencia por defecto
			infoMsg = "El paso termino con advertencias. " + 
					  "Consulte el log";
			// Volcamos el buffer de warnings
			dumpWarningBuffer();
		}
		
		if (hasErrors() || state.equals(AdpRunState.FIN_ERROR)) {
			// Mensaje de error por defecto
			String infoError = "Ocurrieron errores durante la ejecucion del " + 
							   "paso " + pasoActual + ". Consulte los logs.";
			// Volcamos el buffer de errores
			dumpErrorBuffer();
			return changeState(AdpRunState.FIN_ERROR, infoError,false);
		}

		return changeState(state, infoMsg, !(state.equals(AdpRunState.FIN_OK)), null);	
	}

	public Writer getLogWriter() {
		return adplog.getWriter();
	}
	
}
