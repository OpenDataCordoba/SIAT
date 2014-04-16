//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;

/**
 * Adapter del Proceso de Anulacion de Obra de CdM
 * 
 * @author tecso
 */
public class ProcesoAnulacionObraAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoAnulacionObraAdapterVO";
	
	private AnulacionObraVO anulacionObra = new AnulacionObraVO();

	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida2 = new PasoCorridaVO();
	
	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida2 = new ArrayList<FileCorridaVO>();

	private String paramPaso = "";

	//View Flags
	private boolean activarEnabled = false;
	private boolean reprogramarEnabled = false;
	private boolean cancelarEnabled = false;
	private boolean reiniciarEnabled = false;
	private boolean refrescarEnabled = true;

	// Constructores
	public ProcesoAnulacionObraAdapter(){
		super(RecSecurityConstants.ABM_ANULACIONOBRA);
	}

	 //  Getters y Setters
	public AnulacionObraVO getAnulacionObra() {
		return anulacionObra;
	}

	public void setAnulacionObra(AnulacionObraVO anulacionObra) {
		this.anulacionObra = anulacionObra;
	}
	
	public List<FileCorridaVO> getListFileCorrida1() {
		return listFileCorrida1;
	}

	public void setListFileCorrida1(List<FileCorridaVO> listFileCorrida1) {
		this.listFileCorrida1 = listFileCorrida1;
	}

	public List<FileCorridaVO> getListFileCorrida2() {
		return listFileCorrida2;
	}

	public void setListFileCorrida2(List<FileCorridaVO> listFileCorrida2) {
		this.listFileCorrida2 = listFileCorrida2;
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
}
