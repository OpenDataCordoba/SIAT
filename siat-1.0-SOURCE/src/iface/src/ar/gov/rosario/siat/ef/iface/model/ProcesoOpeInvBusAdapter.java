//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;

/**
 * Adapter del ProcesoEmision
 * 
 * @author tecso
 */
public class ProcesoOpeInvBusAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoOpeInvBusAdapterVO";
	public static final String ENC_NAME = "OpeInvBusAdapterVO";
	
	private OpeInvBusVO opeInvBusVO; 
	
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
	private boolean modficarEncOpeInvBusEnabled = false;
	
	// Constructores
	public ProcesoOpeInvBusAdapter(){
		super(GdeSecurityConstants.ABM_LIQCOM);
		ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_LIQCOM;
	}

	 //  Getters y Setters
	
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

	public boolean getModficarEncOpeInvBusEnabled() {
		return modficarEncOpeInvBusEnabled;
	}

	public void setModficarEncOpeInvBusEnabled(boolean modficarEncOpeInvBusEnabled) {
		this.modficarEncOpeInvBusEnabled = modficarEncOpeInvBusEnabled;
	}

	public OpeInvBusVO getOpeInvBus() {
		return opeInvBusVO;
	}

	public void setOpeInvBus(OpeInvBusVO opeInvBusVO) {
		this.opeInvBusVO = opeInvBusVO;
	}

}
