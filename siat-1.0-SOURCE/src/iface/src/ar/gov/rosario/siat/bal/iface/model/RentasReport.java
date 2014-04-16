//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

public class RentasReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "rentasReportVO";
	
	private List<PlanillaVO> listReporteGenerado = new ArrayList<PlanillaVO>();
	private String tituloReporte = "";
		
	private Date fechaDesde;
	private Date fechaHasta;
	private Long nroBalance;
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	private String nroBalanceView = "";
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	// Constructores
	public RentasReport() {       
       super(BalSecurityConstants.CONSULTAR_RENTAS);        
    }

	// Getters y Setters
	public String getDesErrorRun() {
		return desErrorRun;
	}
	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}
	public String getDesRunningRun() {
		return desRunningRun;
	}
	public void setDesRunningRun(String desRunningRun) {
		this.desRunningRun = desRunningRun;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getEstErrorRun() {
		return estErrorRun;
	}
	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}
	public String getEstRunningRun() {
		return estRunningRun;
	}
	public void setEstRunningRun(String estRunningRun) {
		this.estRunningRun = estRunningRun;
	}
	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}
	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public boolean isProcesando() {
		return procesando;
	}
	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public List<PlanillaVO> getListReporteGenerado() {
		return listReporteGenerado;
	}
	public void setListReporteGenerado(List<PlanillaVO> listReporteGenerado) {
		this.listReporteGenerado = listReporteGenerado;
	}
	public String getTituloReporte() {
		return tituloReporte;
	}
	public void setTituloReporte(String tituloReporte) {
		this.tituloReporte = tituloReporte;
	}
	public Long getNroBalance() {
		return nroBalance;
	}
	public void setNroBalance(Long nroBalance) {
		this.nroBalance = nroBalance;
		this.nroBalanceView = StringUtil.formatLong(nroBalance);
	}
	public String getNroBalanceView() {
		return nroBalanceView;
	}
	public void setNroBalanceView(String nroBalanceView) {
		this.nroBalanceView = nroBalanceView;
	}


}
