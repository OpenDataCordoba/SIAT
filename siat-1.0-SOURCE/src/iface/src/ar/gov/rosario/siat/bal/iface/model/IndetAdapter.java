//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Indet
 * 
 * @author tecso
 */
public class IndetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "indetAdapterVO";
	
    private IndetVO indet = new IndetVO();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
    private RecursoVO recurso = new RecursoVO();
    private CuentaVO cuenta = new CuentaVO();
    private SaldoAFavorVO saldoAFavor = new SaldoAFavorVO();
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    
    
	// Flags
	private boolean paramReclamo = false;
	
    // Constructores
    public IndetAdapter(){
    	super(BalSecurityConstants.ABM_INDET);
    }
    
    //  Getters y Setters
	public IndetVO getIndet() {
		return indet;
	}
	public void setIndet(IndetVO indetVO) {
		this.indet = indetVO;
	}
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	public boolean isParamReclamo() {
		return paramReclamo;
	}
	public void setParamReclamo(boolean paramReclamo) {
		this.paramReclamo = paramReclamo;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public SaldoAFavorVO getSaldoAFavor() {
		return saldoAFavor;
	}
	public void setSaldoAFavor(SaldoAFavorVO saldoAFavor) {
		this.saldoAFavor = saldoAFavor;
	}

	public String getName(){
		return NAME;
	}
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Indet");     
		 report.setReportBeanName("Indet");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportIndet = new ReportVO();
		 reportIndet.setReportTitle("Datos del Indet");
		 // carga de datos
	     
	     //Código
		 reportIndet.addReportDato("Código", "codIndet");
		 //Descripción
		 reportIndet.addReportDato("Descripción", "desIndet");
	     
		 report.getListReport().add(reportIndet);
	
	}
}