//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del SaldoAFavor
 * 
 * @author tecso
 */
public class SaldoAFavorAdapter extends SiatAdapterModel{
	
	public static final String NAME = "saldoAFavorAdapterVO";
	
    private SaldoAFavorVO saldoAFavor = new SaldoAFavorVO();
    
    private List<TipoOrigenVO> listTipoOrigen = new ArrayList<TipoOrigenVO>();
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    private List<AreaVO> listArea = new ArrayList<AreaVO>();
    private List<CuentaBancoVO> listCuentaBanco = new ArrayList<CuentaBancoVO>();
    
	// Constructores
    public SaldoAFavorAdapter(){
    	super(BalSecurityConstants.ABM_SALDOAFAVOR);
    }
    
    //  Getters y Setters
	public SaldoAFavorVO getSaldoAFavor() {
		return saldoAFavor;
	}

	public void setSaldoAFavor(SaldoAFavorVO saldoAFavorVO) {
		this.saldoAFavor = saldoAFavorVO;
	}

	public List<TipoOrigenVO> getListTipoOrigen() {
		return listTipoOrigen;
	}

	public void setListTipoOrigen(List<TipoOrigenVO> listTipoOrigen) {
		this.listTipoOrigen = listTipoOrigen;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public String getName(){
		return NAME;
	}
	
	
	
		
	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<CuentaBancoVO> getListCuentaBanco() {
		return listCuentaBanco;
	}

	public void setListCuentaBanco(List<CuentaBancoVO> listCuentaBanco) {
		this.listCuentaBanco = listCuentaBanco;
	}

	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Saldo a Favor");
		 report.setReportBeanName("SaldoAFavor");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportSaldo = new ReportVO();
		 reportSaldo.setReportTitle("Datos del Saldo a Favor");
		 // carga de datos
	     
	     //Recurso
		 reportSaldo.addReportDato("Recurso", "cuenta.recurso.desRecurso");
		 //Cuenta
		 reportSaldo.addReportDato("Cuenta", "cuenta.numeroCuenta");
	     //Tipo Origen
		 reportSaldo.addReportDato("Tipo Origen", "tipoOrigen.desTipoOrigen");
	     //Origen
		 reportSaldo.addReportDato("Origen", "idOrigen");
	     //Fecha Generación
		 reportSaldo.addReportDato("Fecha Generación", "fechaGeneracion");
		//IImporte
		 reportSaldo.addReportDato("Importe", "importe");
		//Saldo
		 reportSaldo.addReportDato("Saldo", "saldo");
		//Caso
		 reportSaldo.addReportDato("Caso", "idCaso");
		 		 
		 report.getListReport().add(reportSaldo);
	
	}
}
