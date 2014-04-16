//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.demoda.iface.helper.StringUtil;

/**
 * Adapter del Proceso de Emision Masiva
 * 
 * @author tecso
 */
public class ProcesoEmisionExternaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoEmisionExternaAdapterVO";

	private EmisionVO emision = new EmisionVO();

	private Integer anio;
	
	private Integer periodo;
		
	private String anioView = "";
	
	private String periodoView = "";
		
	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();

	private PasoCorridaVO pasoCorrida2 = new PasoCorridaVO();
	// Paso 3 para emision ALFAX
	private PasoCorridaVO pasoCorrida3 = new PasoCorridaVO();
	
	// Archivos de salida para emision ALFAX
	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();
	
	private String paramPaso = "";

	// View Flags
	private boolean activarEnabled     = false;
	private boolean reprogramarEnabled = false;
	private boolean cancelarEnabled    = false;
	private boolean reiniciarEnabled   = false;
	private boolean refrescarEnabled   = true;
	private boolean verLogsEnabled 	   = false;
	
	// Constructores
	public ProcesoEmisionExternaAdapter(){
		super(EmiSecurityConstants.ABM_EMISION_EXTERNA);
	}

	 //  Getters y Setters
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

	public String getAnioView() {
		return anioView;
	}

	public void setAnioView(String anioView) {
		this.anioView = anioView;
	}

	public String getPeriodoView() {
		return periodoView;
	}

	public void setPeriodoView(String periodoDesdeView) {
		this.periodoView = periodoDesdeView;
	}

	public List<FileCorridaVO> getListFileCorrida1() {
		return listFileCorrida1;
	}

	public void setListFileCorrida1(List<FileCorridaVO> listFileCorrida1) {
		this.listFileCorrida1 = listFileCorrida1;
	}
	
	public PasoCorridaVO getPasoCorrida1() {
		return pasoCorrida1;
	}
	
	public void setPasoCorrida1(PasoCorridaVO pasoCorrida1) {
		this.pasoCorrida1 = pasoCorrida1;
	}
	
	public PasoCorridaVO getPasoCorrida2() {
		return pasoCorrida2;
	}
	
	public void setPasoCorrida2(PasoCorridaVO pasoCorrida2) {
		this.pasoCorrida2 = pasoCorrida2;
	}
	
	public PasoCorridaVO getPasoCorrida3() {
		return pasoCorrida3;
	}
	
	public void setPasoCorrida3(PasoCorridaVO pasoCorrida3) {
		this.pasoCorrida3 = pasoCorrida3;
	}
	
	public String getParamPaso() {
		return paramPaso;
	}
	
	public void setParamPaso(String paramPaso) {
		this.paramPaso = paramPaso;
	}
	
	public boolean isActivarEnabled() {
		return activarEnabled;
	}
	
	public void setActivarEnabled(boolean activarEnabled) {
		this.activarEnabled = activarEnabled;
	}
	
	public boolean isCancelarEnabled() {
		return cancelarEnabled;
	}
	
	public void setCancelarEnabled(boolean cancelarEnabled) {
		this.cancelarEnabled = cancelarEnabled;
	}
	
	public boolean isReprogramarEnabled() {
		return reprogramarEnabled;
	}
	
	public void setReprogramarEnabled(boolean reprogramarEnabled) {
		this.reprogramarEnabled = reprogramarEnabled;
	}
	
	public boolean isReiniciarEnabled() {
		return reiniciarEnabled;
	}
	
	public void setReiniciarEnabled(boolean reiniciarEnabled) {
		this.reiniciarEnabled = reiniciarEnabled;
	}

	public boolean isRefrescarEnabled() {
		return refrescarEnabled;
	}

	public void setRefrescarEnabled(boolean refrescarEnabled) {
		this.refrescarEnabled = refrescarEnabled;
	}

	public boolean isVerLogsEnabled() {
		return verLogsEnabled;
	}

	public void setVerLogsEnabled(boolean verLogsEnabled) {
		this.verLogsEnabled = verLogsEnabled;
	}

	public Integer getPeriodo() {
		return periodo;
	}
	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
		this.periodoView = StringUtil.formatInteger(periodo);
	}
	
	
}
