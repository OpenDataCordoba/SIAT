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
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
*
* SearchPage del MovBan
* 
* @author Tecso
*
*/
public class MovBanSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "movBanSearchPageVO";
	
	private MovBanVO movBan= new MovBanVO();
	
	private Date fechaDesde;
	private Date fechaHasta;	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	private List<SiNo> listSiNo = new ArrayList<SiNo>();
	
	private BalanceVO balance = new BalanceVO();    
	
	private Boolean conciliarBussEnabled   = true;
	private Boolean anularBussEnabled   = true;
	
	// Constructores
	public MovBanSearchPage() {       
       super(BalSecurityConstants.ABM_MOVBAN);        
    }
	
	// Getters y Setters
	public MovBanVO getMovBan() {
		return movBan;
	}
	public void setMovBan(MovBanVO movBan) {
		this.movBan = movBan;
	}
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format){
		
		ReportVO report = this.getReport(); 
		report.setReportFormat(format);	
		report.setReportBeanName("MovBan");
		report.setReportFileName(this.getClass().getName());	
		
		 // carga de filtros
		 // Fecha de Acreditacion Desde y Hasta
		 report.addReportFiltro("Fecha Acredicatión Desde", this.getFechaDesdeView());
		 report.addReportFiltro("Fecha Acredicatión Hasta", this.getFechaHastaView());

		 // Banco Adm.
		 report.addReportFiltro("Banco Adm.", this.getMovBan().getBancoAdmView());			 
		 
		 // Conciliado
		 String desConciliado = this.getMovBan().getConciliado().getValue();
		 report.addReportFiltro("Conciliado", desConciliado);
		 
		 // Order by
		 report.setReportOrderBy("t.fechaAcredit DESC, t.bancoAdm");
		 
		 ReportTableVO rtMovBan = new ReportTableVO("MovBan");
		 rtMovBan.setTitulo("Listado de Movimientos Bancarios");

		 // Fecha de Acreditación  	Banco Adm.  	Total Débito  	Total Crédito  	Conciliado
		 rtMovBan.addReportColumn("Fecha de Acreditación", "fechaAcredit");
		 rtMovBan.addReportColumn("Banco Adm.", "bancoAdm");
		 rtMovBan.addReportColumn("Total Débito", "totalDebito");
		 rtMovBan.addReportColumn("Total Crédito", "totalCredito");
		 rtMovBan.addReportColumn("Conciliado", "conciliado");

		 report.getReportListTable().add(rtMovBan);
	}

	// Flags Seguridad
	
	public Boolean getConciliarBussEnabled() {
		return conciliarBussEnabled;
	}

	public void setConciliarBussEnabled(Boolean conciliarBussEnabled) {
		this.conciliarBussEnabled = conciliarBussEnabled;
	}
	
	public String getConciliarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getConciliarBussEnabled(), 
				BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.CONCILIAR);
	}
	
	public Boolean getAnularBussEnabled() {
		return anularBussEnabled;
	}

	public void setAnularBussEnabled(Boolean anularBussEnabled) {
		this.anularBussEnabled = anularBussEnabled;
	}
	
	public String getAnularEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAnularBussEnabled(), 
				BalSecurityConstants.ABM_MOVBAN, BaseSecurityConstants.ANULAR);
	}
}
