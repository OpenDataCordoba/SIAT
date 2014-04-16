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
import ar.gov.rosario.siat.rec.iface.model.ObraVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Adapter del Proceso de Emision de CdM
 * 
 * @author tecso
 */
public class ProcesoEmisionCdMAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procesoEmisionCdMAdapterVO";
	public static final String ENC_NAME = "emisionCdMAdapterVO";
	
    private EmisionVO emision = new EmisionVO();

    private ObraVO obra = new ObraVO();
	private Date fechaVencimiento; 

	private PasoCorridaVO pasoCorrida1 = new PasoCorridaVO();
	private PasoCorridaVO pasoCorrida2 = new PasoCorridaVO();
	
	private List<FileCorridaVO> listFileCorrida1 = new ArrayList<FileCorridaVO>();
	private List<FileCorridaVO> listFileCorrida2 = new ArrayList<FileCorridaVO>();

	private String paramPaso = "";

	// fecha de vencimiento de view
	private String fechaVencimientoView = "";
	
	//View Flags
	private boolean activarEnabled = false;
	private boolean cancelarEnabled = false;
	private boolean reiniciarEnabled = false;
	private boolean refrescarEnabled = true;
	private boolean verLogsEnabled = false;
	private boolean verReportesPaso1 = false;
	private boolean verReportesPaso2 = false;
	
	// Constructores
	public ProcesoEmisionCdMAdapter(){
		super(EmiSecurityConstants.ABM_EMISIONMAS);
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

	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obra) {
		this.obra = obra;
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

	public boolean isVerReportesPaso1() {
		return verReportesPaso1;
	}

	public void setVerReportesPaso1(boolean verReportesPaso1) {
		this.verReportesPaso1 = verReportesPaso1;
	}

	public boolean isVerLogsEnabled() {
		return verLogsEnabled;
	}

	public void setVerLogsEnabled(boolean verLogsEnabled) {
		this.verLogsEnabled = verLogsEnabled;
	}

	public boolean isVerReportesPaso2() {
		return verReportesPaso2;
	}

	public void setVerReportesPaso2(boolean verReportesPaso2) {
		this.verReportesPaso2 = verReportesPaso2;
	}

}
