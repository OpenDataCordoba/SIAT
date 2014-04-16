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
import ar.gov.rosario.siat.def.iface.model.BancoVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del CuentaBanco
 * 
 * @author tecso
 */
public class CuentaBancoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "cuentaBancoAdapterVO";
	
    private CuentaBancoVO cuentaBanco = new CuentaBancoVO();
    
    private List<BancoVO> listBanco = new ArrayList<BancoVO>();
	private List<AreaVO> listArea = new ArrayList<AreaVO>();
	private List<TipCueBanVO> listTipCueBan = new ArrayList<TipCueBanVO>();

	
    
    public List<BancoVO> getListBanco() {
		return listBanco;
	}

	public void setListBanco(List<BancoVO> listBanco) {
		this.listBanco = listBanco;
	}

	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<TipCueBanVO> getListTipCueBan() {
		return listTipCueBan;
	}

	public void setListTipCueBan(List<TipCueBanVO> listTipCueBan) {
		this.listTipCueBan = listTipCueBan;
	}

	// Constructores
    public CuentaBancoAdapter(){
    	super(BalSecurityConstants.ABM_CUENTABANCO);
    }
    
    //  Getters y Setters
	public CuentaBancoVO getCuentaBanco() {
		return cuentaBanco;
	}

	public void setCuentaBanco(CuentaBancoVO cuentaBancoVO) {
		this.cuentaBanco = cuentaBancoVO;
	}

		
	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Cuenta Bancaria");     
		 report.setReportBeanName("CuentaBanco");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportCuentaBanco = new ReportVO();
		 reportCuentaBanco.setReportTitle("Datos de la Cuenta Bancaria");
		 // carga de datos
	     
	     //Nro. Cuenta
		 reportCuentaBanco.addReportDato("Nro. Cuenta", "nroCuenta");
		 //Banco
		 reportCuentaBanco.addReportDato("Banco", "banco.desBanco");
		 //Area
		 reportCuentaBanco.addReportDato("Area", "area.desArea");
		 //Tipo Cuenta Bancaria
		 reportCuentaBanco.addReportDato("Tipo Cuenta Bancaria", "tipCueBan.descripcion");
		 //Observaciones
		 reportCuentaBanco.addReportDato("Observaciones", "observaciones");
	     
		 report.getListReport().add(reportCuentaBanco);
	
	}
	
	// View getters
}
