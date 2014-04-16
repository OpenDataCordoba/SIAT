//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;

/**
 * Adapter del CodEmi
 * 
 * @author tecso
 */
public class CodEmiAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME 	= "codEmiAdapterVO";
	public static final String ENC_NAME = "encCodEmiAdapterVO";
	
    private CodEmiVO codEmi = new CodEmiVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<TipCodEmiVO> listTipCodEmi = new ArrayList<TipCodEmiVO>();

	// View Flag
	private boolean seleccionarRecursoEnabled = true;

	// Tab para Test del codigo
    private CuentaVO cuenta = new CuentaVO();
    
    private Integer anio;
    
    private Integer periodo;
    
    private String anioView = "";
    
    private String periodoView = "";
    
    private String evalOutput = "";

    // Tab seleccionado 
    private String currentTab = "editar";
    
    // Constructores
    public CodEmiAdapter(){
    	super(DefSecurityConstants.ABM_CODEMI);
    	ACCION_MODIFICAR_ENCABEZADO = DefSecurityConstants.ABM_CODEMI_ENC;
    }
    
    //  Getters y Setters
	public CodEmiVO getCodEmi() {
		return codEmi;
	}

	public void setCodEmi(CodEmiVO codEmiVO) {
		this.codEmi = codEmiVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipCodEmiVO> getListTipCodEmi() {
		return listTipCodEmi;
	}

	public void setListTipCodEmi(List<TipCodEmiVO> listTipCodEmi) {
		this.listTipCodEmi = listTipCodEmi;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	
	public boolean isSeleccionarRecursoEnabled() {
		return seleccionarRecursoEnabled;
	}

	public void setSeleccionarRecursoEnabled(boolean seleccionarRecursoEnabled) {
		this.seleccionarRecursoEnabled = seleccionarRecursoEnabled;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Integer getPeriodo() {
		return periodo;
	}

	public void setPeriodo(Integer periodo) {
		this.periodo = periodo;
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

	public String getEvalOutput() {
		return evalOutput;
	}

	public void setEvalOutput(String evalOutput) {
		this.evalOutput = evalOutput;
	}

	public String getCurrentTab() {
		return currentTab;
	}

	public void setCurrentTab(String currentTab) {
		this.currentTab = currentTab;
	}

}