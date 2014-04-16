//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del ParCueBan
 * 
 * @author tecso
 */
public class ParCueBanAdapter extends SiatAdapterModel{
	
	public static final String NAME = "parCueBanAdapterVO";
	
    private ParCueBanVO parCueBan = new ParCueBanVO();
    
    private List<CuentaBancoVO>  listCuentaBanco = new ArrayList<CuentaBancoVO>();
    
    // Constructores
    public ParCueBanAdapter(){
    	super(BalSecurityConstants.ABM_PARCUEBAN);
    }
    
    //  Getters y Setters
	public ParCueBanVO getParCueBan() {
		return parCueBan;
	}

	public void setParCueBan(ParCueBanVO parCueBanVO) {
		this.parCueBan = parCueBanVO;
	}

	
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Partida/Cuenta");     
		 report.setReportBeanName("ParCueBan");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportParCueBan = new ReportVO();
		 reportParCueBan.setReportTitle("Datos de la Partida/Cuenta");
		 // carga de datos
	     
	     //Fecha Desde
		 reportParCueBan.addReportDato("Fecha Desde", "fechaDesde");
		 //Fecha Hasta
		 reportParCueBan.addReportDato("Fecha Hasta", "fechaHasta");
		 //Nro Cuenta Bancaria
		 reportParCueBan.addReportDato("Nro Cuenta Bancaria", "cuentaBanco.nroCuenta");
	     
	     report.getListReport().add(reportParCueBan);
	
	}
	// View getters

	public List<CuentaBancoVO> getListCuentaBanco() {
		return listCuentaBanco;
	}

	public void setListCuentaBanco(List<CuentaBancoVO> listCuentaBanco) {
		this.listCuentaBanco = listCuentaBanco;
	}
}
