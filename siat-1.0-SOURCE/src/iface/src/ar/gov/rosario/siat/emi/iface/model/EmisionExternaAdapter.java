//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;


import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del ConstanciaDeu
 * 
 * @author tecso
 */
public class EmisionExternaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(EmisionExternaAdapter.class);
	
	public static final String NAME = "emisionExternaAdapterVO";

	private String fileName = "";
	private byte[] fileData;
	private List<FilaEmisionExterna> listFilas = new ArrayList<FilaEmisionExterna>();
	private Integer cantLineas = 0;
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private StringBuilder errors = new StringBuilder(); 
	
	private EmisionVO emision = new EmisionVO();
	private Integer anio;
	private Integer periodo;
	private String  anioView = "";
	private String  periodoView = "";
		
	// Flags
	private boolean verBotonCargarEnabled = true;
	private boolean verBloqueValidarEnabled = false;
	private boolean verBotonValidarEnabled 	= false;
	private boolean verResultadoValidacionEnabled = false;
	private boolean verBotonContinuarBussEnabled = false;
	private boolean resultadoValidacion = false;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}

	public Integer getCantLineas() {
		return cantLineas;
	}

	public void setCantLineas(Integer cantLineas) {
		this.cantLineas = cantLineas;
	}

	public List<FilaEmisionExterna> getListFilas() {
		return listFilas;
	}
	public void setListFilas(List<FilaEmisionExterna> listFilas) {
		this.listFilas = listFilas;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	/**
	 * Devuelve la ruta completa de la carpeta de trabajo tmp para el archivo.<br>
	 * 
	 * @return
	 */
	public String getPathTmp(){
		return "/mnt/publico/tmp/" + getFileName();
	}
	
	public String getCantEventosGrabar(){
		return (listFilas!=null?String.valueOf(listFilas.size()):"0");
	}
	
	// flags getters
	public boolean isVerBotonValidarEnabled() {
		return verBotonValidarEnabled;
	}
	public void setVerBotonValidarEnabled(boolean verBotonValidarEnabled) {
		this.verBotonValidarEnabled = verBotonValidarEnabled;
	}

	public boolean isVerBotonCargarEnabled() {
		return verBotonCargarEnabled;
	}
	public void setVerBotonCargarEnabled(boolean verBotonCargarEnabled) {
		this.verBotonCargarEnabled = verBotonCargarEnabled;
	}

	public boolean isVerResultadoValidacionEnabled() {
		return verResultadoValidacionEnabled;
	}
	public void setVerResultadoValidacionEnabled(boolean verResultadoValidacionEnabled) {
		this.verResultadoValidacionEnabled = verResultadoValidacionEnabled;
	}

	public boolean isResultadoValidacion() {
		return resultadoValidacion;
	}
	public void setResultadoValidacion(boolean resultadoValidacion) {
		this.resultadoValidacion = resultadoValidacion;
	}
	
	public boolean isVerBloqueValidarEnabled() {
		return verBloqueValidarEnabled;
	}
	public void setVerBloqueValidarEnabled(boolean verBloqueValidarEnabled) {
		this.verBloqueValidarEnabled = verBloqueValidarEnabled;
	}

	public void setVerBotonContinuarBussEnabled(boolean verBotonContinuarBussEnabled) {
		this.verBotonContinuarBussEnabled = verBotonContinuarBussEnabled;
	}

	public EmisionVO getEmision() {
		return emision;
	}
	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}
 
	public Integer getAnio() {
		return anio;
	}
	public void setAnio(Integer anio) {
		this.anio = anio;
		this.anioView = StringUtil.formatInteger(anio);
	}

	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}

	public String getAnioView() {
		return anioView;
	}
	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoView() {
		return periodoView;
	}
	public void setPeriodoView(String periodoView) {
		this.periodoView = periodoView;
	}

	public StringBuilder getErrors() {
		return errors;
	}
	public void setErrors(StringBuilder errors) {
		this.errors = errors;
	}

	public String getVerBotonContinuarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(verBotonContinuarBussEnabled , EmiSecurityConstants.ADM_EMISION_EXTERNA, EmiSecurityConstants.MTD_CONTINUAR);
	}
}
