//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Balance
 * @author tecso
 *
 */
public class BalanceSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	private BalanceVO balance = new BalanceVO();
	
	public static final String NAME = "balanceSearchPageVO";

	private Date fechaBalanceDesde;
	private Date fechaBalanceHasta;	
	private String fechaBalanceDesdeView = "";
	private String fechaBalanceHastaView = "";
	private EstadoCorridaVO estadoCorrida = new EstadoCorridaVO();
	
	private List<BalanceVO> listBalance = new ArrayList<BalanceVO>();
	private List<EjercicioVO> listEjercicio = new ArrayList<EjercicioVO>();
	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	private Boolean admProcesoBussEnabled      = true;
		
	public BalanceSearchPage(){
		super(BalSecurityConstants.ABM_BALANCE);
	}

	// Getters y Setters
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public EstadoCorridaVO getEstadoCorrida() {
		return estadoCorrida;
	}
	public void setEstadoCorrida(EstadoCorridaVO estadoCorrida) {
		this.estadoCorrida = estadoCorrida;
	}
	public Date getFechaBalanceDesde() {
		return fechaBalanceDesde;
		
	}
	public void setFechaBalanceDesde(Date fechaBalanceDesde) {
		this.fechaBalanceDesde = fechaBalanceDesde;
		this.fechaBalanceDesdeView = DateUtil.formatDate(fechaBalanceDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBalanceDesdeView() {
		return fechaBalanceDesdeView;
	}
	public void setFechaBalanceDesdeView(String fechaBalanceDesdeView) {
		this.fechaBalanceDesdeView = fechaBalanceDesdeView;
	}
	public Date getFechaBalanceHasta() {
		return fechaBalanceHasta;
	}
	public void setFechaBalanceHasta(Date fechaBalanceHasta) {
		this.fechaBalanceHasta = fechaBalanceHasta;
	}
	public String getFechaBalanceHastaView() {
		return fechaBalanceHastaView;
	}
	public void setFechaBalanceHastaView(String fechaBalanceHastaView) {
		this.fechaBalanceHastaView = fechaBalanceHastaView;
		this.fechaBalanceHastaView = DateUtil.formatDate(fechaBalanceHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public List<BalanceVO> getListBalance() {
		return listBalance;
	}
	public void setListBalance(List<BalanceVO> listBalance) {
		this.listBalance = listBalance;
	}
	public List<EjercicioVO> getListEjercicio() {
		return listEjercicio;
	}
	public void setListEjercicio(List<EjercicioVO> listEjercicio) {
		this.listEjercicio = listEjercicio;
	}
	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}
	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}
	
	//	Flags Seguridad
	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}

	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	
	public String getAdmProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmProcesoBussEnabled(), 
				BalSecurityConstants.ABM_BALANCE, BalSecurityConstants.ABM_BALANCE_ADM_PROCESO);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); 
		report.setReportTitle("Reporte de Balance");
		report.setReportBeanName("Balance");
		report.setReportFormat(format);
	     // nombre del archivo 
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // Numero
		 // Fecha de Balance Desde y Hasta
		 report.addReportFiltro("Fecha Balance Desde", this.getFechaBalanceDesdeView());
		 report.addReportFiltro("Fecha Balance Hasta", this.getFechaBalanceHastaView());
		 // Ejercicio
		 String desEjercicio = "No seleccionado";
		 EjercicioVO ejercicioVO = (EjercicioVO) ModelUtil.getBussImageModelByIdForList(
				 this.getBalance().getEjercicio().getId(), 
				 this.getListEjercicio());
		 if (ejercicioVO != null){
			 desEjercicio = ejercicioVO.getDesEjercicio();
		 }	
		 report.addReportFiltro("Ejercicio", desEjercicio);
		 
		 // Order by
		 report.setReportOrderBy("fechaBalance DESC, id DESC");
		 
		 ReportTableVO rtBalance = new ReportTableVO("Balance");
		 rtBalance.setTitulo("Listado de Balance");

		 rtBalance.addReportColumn("Número", "id");
		 rtBalance.addReportColumn("Fecha de Balance", "fechaBalance");
		 rtBalance.addReportColumn("Ejercicio", "ejercicio.desEjercicio");
		 rtBalance.addReportColumn("Fecha Desde", "fechaDesde");
		 rtBalance.addReportColumn("Fecha Hasta", "fechaHasta");

		 report.getReportListTable().add(rtBalance);
	}
}
