//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter de Vencimiento
 * 
 * @author tecso
 */
public class VencimientoAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "vencimientoAdapterVO";
	
	private VencimientoVO vencimiento = new VencimientoVO();
	private List<SiNo> listSiNo= new ArrayList<SiNo>();
	
	public VencimientoAdapter(){
		super(DefSecurityConstants.ABM_VENCIMIENTO);
	}
	
	//Getters y Setters
	
	public VencimientoVO getVencimiento(){
		return vencimiento;
	}
	
	public void setVencimiento(VencimientoVO vencimiento){
		this.vencimiento = vencimiento;
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
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Vencimientos");
		 report.setReportBeanName("vencimiento");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosVec = new ReportVO();
		 reportDatosVec.setReportTitle("Datos de Vencimiento");
		 
		// carga de datos
			
	     //D�a                                
		 reportDatosVec.addReportDato("D�a", "dia");
		//Mes                                
		 reportDatosVec.addReportDato("Mes", "mes");
		 //H�bil
		 reportDatosVec.addReportDato("H�bil", "esHabil", SiNo.class);
	     //Cant. D�as
		 reportDatosVec.addReportDato("Cant. D�as", "cantDias");
	     //Cant. Mes
		 reportDatosVec.addReportDato("Cant. Mes", "cantMes");
	     //Cant. Semana
		 reportDatosVec.addReportDato("Cant. Semana", "cantSemana");
		//1er d�a Semana
		 reportDatosVec.addReportDato("1er d�a Semana", "primeroSemana", SiNo.class);
		//Ult. d�a Semana
		 reportDatosVec.addReportDato("Ult. d�a Semana", "ultimoSemana", SiNo.class);
		//Ultimo
		 reportDatosVec.addReportDato("Ultimo", "esUltimo", SiNo.class);
		//Descripci�n
		 reportDatosVec.addReportDato("Descripci�n", "desVencimiento");
	   
		 report.getListReport().add(reportDatosVec);
	

	}

}
