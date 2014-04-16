//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Emision
 * 
 * @author Tecso
 *
 */
public class DeudaProcuradorReport extends SiatPageModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "deudaProcuradorReportVO";
	
	private ProcuradorVO procurador = new ProcuradorVO();
	private Date   fechaVtoDesde;
	private Date   fechaVtoHasta;
	private String fechaVtoDesdeView = "";
	private String fechaVtoHastaView = "";
	private Integer tipoReporte;
	
	private String userName;
	private String tituloReportes ="";
	
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	
	private List<PlanillaVO> listReporteGenerado = new ArrayList<PlanillaVO>();
	
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;

	//	 Constructores
	public DeudaProcuradorReport() {
		super(GdeSecurityConstants.DEUDA_PROCURADOR_REPORT);
	}

	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	// Getters y Setters
	public Date getFechaVtoDesde() {
		return fechaVtoDesde;
	}
	public void setFechaVtoDesde(Date fechaDesde) {
		this.fechaVtoDesde = fechaDesde;
		this.fechaVtoDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaVtoHasta() {
		return fechaVtoHasta;
	}
	public void setFechaVtoHasta(Date fechaHasta) {
		this.fechaVtoHasta = fechaHasta;
		this.fechaVtoHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVtoDesdeView() {
		return fechaVtoDesdeView;
	}
	public void setFechaVtoDesdeView(String fechaDesdeView) {
		this.fechaVtoDesdeView = fechaDesdeView;
	}
	public String getFechaVtoHastaView() {
		return fechaVtoHastaView;
	}
	public void setFechaVtoHastaView(String fechaHastaView) {
		this.fechaVtoHastaView = fechaHastaView;
	}

	public List<PlanillaVO> getListReporteGenerado() {
		return listReporteGenerado;
	}

	public void setListReporteGenerado(List<PlanillaVO> listReporteGenerado) {
		this.listReporteGenerado = listReporteGenerado;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	
	public Integer getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(Integer tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public String getDesRunningRun() {
		return desRunningRun;
	}

	public void setDesRunningRun(String desRunningRun) {
		this.desRunningRun = desRunningRun;
	}

	public String getEstRunningRun() {
		return estRunningRun;
	}

	public void setEstRunningRun(String estRunningRun) {
		this.estRunningRun = estRunningRun;
	}

	public String getDesErrorRun() {
		return desErrorRun;
	}

	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}

	public String getEstErrorRun() {
		return estErrorRun;
	}

	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}

	public boolean isProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}

	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}

	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getTituloReportes() {
		return tituloReportes;
	}

	public void setTituloReportes(String tituloReportes) {
		this.tituloReportes = tituloReportes;
	}

	
}
