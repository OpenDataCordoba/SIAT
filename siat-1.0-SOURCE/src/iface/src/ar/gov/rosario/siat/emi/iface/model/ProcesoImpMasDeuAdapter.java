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

/**
 * Adapter del Proceso de Impresion Masiva de Deuda
 * 
 * @author tecso
 */
public class ProcesoImpMasDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoImpMasDeuAdapterVO";
	
    private ImpMasDeuVO impMasDeu = new ImpMasDeuVO();
    
	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();

	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();
	
	private String paramPaso = "";
	
	//View Flags
	private boolean activarEnabled  = false;

	private boolean cancelarEnabled = false;
	
	private boolean reiniciarEnabled = false;
	
	private boolean refrescarEnabled = true;
	
	private boolean verLogsEnabled = false;
	
	private boolean verFileList = false;
	
	private boolean selectAtrValEnabled = false;

    // Constructores
    public ProcesoImpMasDeuAdapter(){
    	super(EmiSecurityConstants.ABM_IMPMASDEU);
    }
    
    //  Getters y Setters
	public ImpMasDeuVO getImpMasDeu() {
		return impMasDeu;
	}

	public void setImpMasDeu(ImpMasDeuVO impMasDeuVO) {
		this.impMasDeu = impMasDeuVO;
	}

	public PasoCorridaVO getPasoCorrida1() {
		return pasoCorrida1;
	}

	public void setPasoCorrida1(PasoCorridaVO pasoCorrida1) {
		this.pasoCorrida1 = pasoCorrida1;
	}

	public List<FileCorridaVO> getListFileCorrida1() {
		return listFileCorrida1;
	}

	public void setListFileCorrida1(List<FileCorridaVO> listFileCorrida1) {
		this.listFileCorrida1 = listFileCorrida1;
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

	public boolean isVerFileList() {
		return verFileList;
	}

	public void setVerFileList(boolean verFileList) {
		this.verFileList = verFileList;
	}

	public boolean isSelectAtrValEnabled() {
		return selectAtrValEnabled;
	}

	public void setSelectAtrValEnabled(boolean selectAtrValEnabled) {
		this.selectAtrValEnabled = selectAtrValEnabled;
	}

}
