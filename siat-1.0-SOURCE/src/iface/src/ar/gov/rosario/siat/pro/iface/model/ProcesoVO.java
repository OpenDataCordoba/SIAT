//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pro.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Son los tipos de procesos del SIAT.
 * @author tecso
 *
 */
public class ProcesoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procesoVO";
	
	private String codProceso;
	private String desProceso;
	private SiNo esAsincronico = SiNo.OpcionSelecionar;
	private TipoEjecucionVO tipoEjecucion = new TipoEjecucionVO();
	private String directorioInput;
	private Long cantPasos;
	private String cantPasosView;
	private TipoProgEjecVO tipoProgEjec = new TipoProgEjecVO();
	private String classForName;
	private String spValidate;
	private String spExecute;
	private String spResume;
	private String spCancel;
	private String ejecNodo="";
	private SiNo locked = SiNo.OpcionSelecionar;
	private String cronExpression;

	// Constructores
	public ProcesoVO(){
		super();
	}
	public ProcesoVO(int id, String desProceso) {
		super(id);
		setDesProceso(desProceso);
	}
	
	// Getters y Setters
	public String getCodProceso(){
		return codProceso;
	}
	public void setCodProceso(String codProceso){
		this.codProceso = codProceso;
	}
	public String getDesProceso(){
		return desProceso;
	}
	public void setDesProceso(String desProceso){
		this.desProceso = desProceso;
	}
	public SiNo getEsAsincronico(){
		return esAsincronico;
	}
	public void setEsAsincronico(SiNo esAsincronico){
		this.esAsincronico = esAsincronico;
	}
	public TipoEjecucionVO getTipoEjecucion(){
		return tipoEjecucion;
	}
	public void setTipoEjecucion(TipoEjecucionVO tipoEjecucion){
		this.tipoEjecucion = tipoEjecucion;
	}
	public String getDirectorioInput(){
		return directorioInput;
	}
	public void setDirectorioInput(String directorioInput){
		this.directorioInput = directorioInput;
	}
	public Long getCantPasos(){
		return cantPasos;
	}
	public void setCantPasos(Long cantPasos){
		this.cantPasos = cantPasos;
		this.cantPasosView = StringUtil.formatLong(cantPasos);
	}
	public String getCantPasosView(){
		return cantPasosView;
	}
	public void setCantPasosView(String cantPasosView){
	}
	public TipoProgEjecVO getTipoProgEjec(){
		return tipoProgEjec;
	}
	public void setTipoProgEjec(TipoProgEjecVO tipoProgEjec){
		this.tipoProgEjec = tipoProgEjec;
	}
	public String getClassForName(){
		return classForName;
	}
	public void setClasForName(String classForName){
		this.classForName = classForName;
	}
	public String getSpValidate(){
		return spValidate;
	}
	public void setSpValidate(String spValidate){
		this.spValidate = spValidate;
	}
	public String getSpExecute(){
		return spExecute;
	}
	public void setSpExecute(String spExecute){
		this.spExecute = spExecute;
	}
	public String getSpResume(){
		return spResume;
	}
	public void setSpResume(String spResume){
		this.spResume = spResume;
	}
	public String getSpCancel(){
		return spCancel;
	}
	public void setSpCancel(String spCancel){
		this.spCancel = spCancel;
	}

	public SiNo getLocked() {
		return locked;
	}
	public void setLocked(SiNo locked) {
		this.locked = locked;
	}
	
	public String getEjecNodo() {
		return ejecNodo;
	}
	public void setEjecNodo(String ejecNodo) {
		this.ejecNodo = ejecNodo;
	}
	public void setClassForName(String classForName) {
		this.classForName = classForName;
	}
	// View Getters
	/**
	 * Devuelve el nombre corto de la clase, es decir, sin el paquete
	 */
	public String getClassForNameView(){
		if(!StringUtil.isNullOrEmpty(classForName) && classForName.lastIndexOf(".")>0){
			return classForName.substring(classForName.lastIndexOf(".")+1);
		}
		return "";
	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}
	public String getCronExpression() {
		return cronExpression;
	}
}
