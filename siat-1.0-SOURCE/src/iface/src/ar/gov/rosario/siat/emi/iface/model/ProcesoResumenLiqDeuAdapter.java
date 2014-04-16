//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.emi.iface.util.EmiSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.FileCorridaVO;
import ar.gov.rosario.siat.pro.iface.model.PasoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del Proceso de Emision de Tgi
 * 
 * @author tecso
 */
public class ProcesoResumenLiqDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoResumenLiqDeuAdapterVO";
	public static final String ENC_NAME = "procesoResumenLiqDeuEncAdapterVO";
	
    private EmisionVO emision = new EmisionVO();

    private Date fechaVencimiento; 

	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();
	
	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();

	private String paramPaso = "";

	// fecha de vencimiento de view
	private String fechaVencimientoView = "";
	
	//View Flags
	private boolean activarEnabled     = false;
	private boolean reprogramarEnabled = false;
	private boolean cancelarEnabled  = false;
	private boolean reiniciarEnabled = false;
	private boolean modificarEncEmiEnabled = false;
	
	// Constructores
	public ProcesoResumenLiqDeuAdapter(){
		super(EmiSecurityConstants.ABM_RESLIQDEU);
	}

	 //  Getters y Setters
	public EmisionVO getEmision() { 
		return emision;
	}
	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}
	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
		this.fechaVencimientoView = DateUtil.formatDate(fechaVencimiento, DateUtil.ddSMMSYYYY_MASK);
	}
	
	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}
	public String getFechaVencimientoView() {
		return fechaVencimientoView;
	}
	public void setFechaVencimientoView(String fechaVencimientoView) {
		this.fechaVencimientoView = fechaVencimientoView;
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
	public boolean isModificarEncEmiEnabled() {
		return modificarEncEmiEnabled;
	}
	public void setModificarEncEmiEnabled(boolean modificarEncEmiEnabled) {
		this.modificarEncEmiEnabled = modificarEncEmiEnabled;
	}

}
