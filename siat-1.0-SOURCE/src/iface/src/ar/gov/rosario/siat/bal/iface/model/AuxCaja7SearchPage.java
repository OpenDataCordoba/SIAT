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
import coop.tecso.demoda.iface.model.Estado;
import coop.tecso.demoda.iface.model.ReportVO;

/**
*
* SearchPage del AuxCaja7
* 
* @author Tecso
*
*/
public class AuxCaja7SearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "auxCaja7SearchPageVO";
	
	private AuxCaja7VO auxCaja7= new AuxCaja7VO();
	
	private Date fechaDesde;
	private Date fechaHasta;	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	private List<Estado> listEstado = new ArrayList<Estado>();
	
	private String[] listIdAuxCaja7Selected;
	private BalanceVO balance = new BalanceVO();    
	
	// Constructores
	public AuxCaja7SearchPage() {       
       super(BalSecurityConstants.ABM_AUXCAJA7);        
    }
	
	// Getters y Setters
	public AuxCaja7VO getAuxCaja7() {
		return auxCaja7;
	}
	public void setAuxCaja7(AuxCaja7VO auxCaja7) {
		this.auxCaja7 = auxCaja7;
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
	public String[] getListIdAuxCaja7Selected() {
		return listIdAuxCaja7Selected;
	}
	public void setListIdAuxCaja7Selected(String[] listIdAuxCaja7Selected) {
		this.listIdAuxCaja7Selected = listIdAuxCaja7Selected;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public List<Estado> getListEstado() {
		return listEstado;
	}
	public void setListEstado(List<Estado> listEstado) {
		this.listEstado = listEstado;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format){
		
		ReportVO report = this.getReport(); 
		report.setReportFormat(format);	
		report.setReportBeanName("AuxCaja7");
		report.setReportFileName(this.getClass().getName());	
	}

}
