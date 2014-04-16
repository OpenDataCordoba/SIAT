//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package coop.tecso.adpcore;

import java.io.File;

/**
 * Clase de Procesos de ADP. Desde esta clase se pueden instanciar y obtener
 * procesos definidos en ADP. Luego se puede ejecutar.
 *
 * @author Coop. Tecso Ltda.
 */
public class AdpProcess {

	public static final long TIPO_MANUAL = 1;
	public static final long TIPO_ARRIBODIR = 2;
	public static final long TIPO_PERIODIC = 3;

	private Long id = 0L;
	private String codProceso = "";
	private String desProceso  = "";
	private Boolean esAsincronico = true;
	private Long tipoEjecucion = 0L;
	private String directorioInput = "";
	private Long cantPasos = 0L;
	private Long tipoProgEjecucion = 0L;
	private String classForName = "";
	private String spValidate = "";
	private String spExecute = "";
	private String spResume = "";
	private String spCancel = "";
	private String ejecNodo = "";
	private Long locked = 0L;
	
	private String cronExpression = "";
	
	public AdpProcess() {
	}
	
	/**
	 * @return the cantPasos
	 */
	public Long getCantPasos() {
		return cantPasos;
	}

	/**
	 * @param cantPasos the cantPasos to set
	 */
	public void setCantPasos(Long cantPasos) {
		this.cantPasos = cantPasos;
	}

	/**
	 * @return the classForName
	 */
	public String getClassForName() {
		return classForName;
	}

	/**
	 * @param classForName the classForName to set
	 */
	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}

	/**
	 * @return the codProceso
	 */
	public String getCodProceso() {
		return codProceso;
	}

	/**
	 * @param codProceso the codProceso to set
	 */
	public void setCodProceso(String codProceso) {
		this.codProceso = codProceso;
	}

	/**
	 * @return the desProceso
	 */
	public String getDesProceso() {
		return desProceso;
	}

	/**
	 * @param desProceso the desProceso to set
	 */
	public void setDesProceso(String desProceso) {
		this.desProceso = desProceso;
	}

	/**
	 * @return the directorioInput
	 */
	public String getDirectorioInput() {
		return directorioInput;
	}

	/**
	 * @param directorioInput the directorioInput to set
	 */
	public void setDirectorioInput(String directorioInput) {
		this.directorioInput = directorioInput;
	}

	/**
	 * @return the esAsincronico
	 */
	public Boolean getEsAsincronico() {
		return esAsincronico;
	}

	/**
	 * @param esAsincronico the esAsincronico to set
	 */
	public void setEsAsincronico(Boolean esAsincronico) {
		this.esAsincronico = esAsincronico;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the spCancel
	 */
	public String getSpCancel() {
		return spCancel;
	}

	/**
	 * @param spCancel the spCancel to set
	 */
	public void setSpCancel(String spCancel) {
		this.spCancel = spCancel;
	}

	/**
	 * @return the spExecute
	 */
	public String getSpExecute() {
		return spExecute;
	}

	/**
	 * @param spExecute the spExecute to set
	 */
	public void setSpExecute(String spExecute) {
		this.spExecute = spExecute;
	}

	/**
	 * @return the spResume
	 */
	public String getSpResume() {
		return spResume;
	}

	/**
	 * @param spResume the spResume to set
	 */
	public void setSpResume(String spResume) {
		this.spResume = spResume;
	}

	/**
	 * @return the spValidate
	 */
	public String getSpValidate() {
		return spValidate;
	}

	/**
	 * @param spValidate the spValidate to set
	 */
	public void setSpValidate(String spValidate) {
		this.spValidate = spValidate;
	}

	/**
	 * @return the tipoEjecucion
	 */
	public Long getTipoEjecucion() {
		return tipoEjecucion;
	}

	/**
	 * @param tipoEjecucion the tipoEjecucion to set
	 */
	public void setTipoEjecucion(Long tipoEjecucion) {
		this.tipoEjecucion = tipoEjecucion;
	}

	/**
	 * @return the tipoProgEjecucion
	 */
	public Long getTipoProgEjecucion() {
		return tipoProgEjecucion;
	}

	/**
	 * @param tipoProgEjecucion the tipoProgEjecucion to set
	 */
	public void setTipoProgEjecucion(Long tipoProgEjecucion) {
		this.tipoProgEjecucion = tipoProgEjecucion;
	}

	/**
	 * @return el estado de lockeo (0 no esta lockeado, 1 esta lockeado)
	 */
	public boolean isLocked() {
		return locked != 0;
	}

	/**
	 * @param lock el estado de lockeo (0 no esta lockeado, 1 esta lockeado)
	 */
	public void setLocked(Long lock) {
		this.locked = lock;
	}

	public String infoString() {
		String sf = "id=%d;codProceso=%s;desProceso=%s;esAsincronico=%B;tipoEjecucion=%d;";
		sf+="directorioInput=%s;cantPasos=%d;tipoProgEjecucion=%d;classForName=%s;";
		sf+="spValidate=%s;spExecute=%s;spResume=%s;spCancel=%s";
		String ret = String.format(sf, this.getId(),
				this.getCodProceso(),
				this.getDesProceso(),
				this.getEsAsincronico(),
				this.getTipoEjecucion(),
				this.getDirectorioInput(),
				this.getCantPasos(),
				this.getTipoProgEjecucion(),
				this.getClassForName(),
				this.getSpValidate(),
				this.getSpExecute(),
				this.getSpResume(),
				this.getSpCancel());
		return ret;
	}

	/**
	 * Obtiene el directorio de input calculado a partir del directorioInput y el subdir 
	 * de corrida pasado en el parametro.
	 * @param subdir Subdirectorio de corrida: recibido, procesando, etc
	 */
	public String getInputFilePath(AdpRunDirEnum subdir) {
		File f = new File(directorioInput + "/" +  subdir.dirName());
		return f.getAbsolutePath();
	}

	/**
	 * Lista de nodos separada por comas que puede ejecutar este proceso
	 * @return
	 */
	public String getEjecNodo() {
		return ejecNodo;
	}
	/**
	 * Lista de nodos separada por comas que puede ejecutar este proceso
	*/
	public void setEjecNodo(String ejecNodo) {
		this.ejecNodo = ejecNodo;
	}
	
	/**
	 * @return the cronExpression
	 */
	public String getCronExpression() {
		return cronExpression;
	}

	/**
	 * @param cronExpression the cronExpression to set
	 */
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
}
