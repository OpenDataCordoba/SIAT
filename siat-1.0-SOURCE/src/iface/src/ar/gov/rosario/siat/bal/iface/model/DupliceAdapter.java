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
import ar.gov.rosario.siat.gde.iface.model.LiqCuotaVO;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaVO;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.TipoBoleta;

/**
 * Adapter del Duplice
 * 
 * @author tecso
 */
public class DupliceAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "dupliceAdapterVO";
	
    private IndetVO duplice = new IndetVO();
        
	private RecursoVO recurso = new RecursoVO();
	private CuentaVO cuenta = new CuentaVO();
	private SaldoAFavorVO saldoAFavor = new SaldoAFavorVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();

	List<LiqDeudaVO> listDeuda = new ArrayList<LiqDeudaVO>();
	private List<LiqCuotaVO> listCuota = new ArrayList<LiqCuotaVO>();
	
	private List<TipoBoleta> listTipoBoleta = new ArrayList<TipoBoleta>();
	  
	private TipoBoleta tipoBoleta = TipoBoleta.TIPODEUDA;
	private String idAImputar = "";

	private Boolean activarScroll = false; 
	
	private String actForParamCuenta = "";
	
	// Constructores
    public DupliceAdapter(){
    	super(BalSecurityConstants.ABM_DUPLICE);
    }
    
    //  Getters y Setters
	public IndetVO getDuplice() {
		return duplice;
	}
	public void setDuplice(IndetVO duplice) {
		this.duplice = duplice;
	} 
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public List<LiqCuotaVO> getListCuota() {
		return listCuota;
	}
	public void setListCuota(List<LiqCuotaVO> listCuota) {
		this.listCuota = listCuota;
	}
	public List<LiqDeudaVO> getListDeuda() {
		return listDeuda;
	}
	public void setListDeuda(List<LiqDeudaVO> listDeuda) {
		this.listDeuda = listDeuda;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoBoleta> getListTipoBoleta() {
		return listTipoBoleta;
	}
	public void setListTipoBoleta(List<TipoBoleta> listTipoBoleta) {
		this.listTipoBoleta = listTipoBoleta;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public TipoBoleta getTipoBoleta() {
		return tipoBoleta;
	}
	public void setTipoBoleta(TipoBoleta tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
	}
	public String getIdAImputar() {
		return idAImputar;
	}
	public void setIdAImputar(String idAImputar) {
		this.idAImputar = idAImputar;
	}
	public Boolean getActivarScroll() {
		return activarScroll;
	}
	public void setActivarScroll(Boolean activarScroll) {
		this.activarScroll = activarScroll;
	}
	public SaldoAFavorVO getSaldoAFavor() {
		return saldoAFavor;
	}
	public void setSaldoAFavor(SaldoAFavorVO saldoAFavor) {
		this.saldoAFavor = saldoAFavor;
	}
	public String getActForParamCuenta() {
		return actForParamCuenta;
	}
	public void setActForParamCuenta(String actForParamCuenta) {
		this.actForParamCuenta = actForParamCuenta;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Duplice");     
		 report.setReportBeanName("Indet");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportIndet = new ReportVO();
		 reportIndet.setReportTitle("Datos del Duplice");
		 // carga de datos
	     
	     //Código
		 reportIndet.addReportDato("Código", "codIndet");
		 //Descripción
		 reportIndet.addReportDato("Descripción", "desIndet");
	     
		 report.getListReport().add(reportIndet);
	
	}
}
