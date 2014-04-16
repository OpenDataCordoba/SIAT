//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Juzgado
 * 
 * @author tecso
 */
public class JuzgadoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "juzgadoAdapterVO";
	
    private JuzgadoVO juzgado = new JuzgadoVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
       
    // Constructores
    public JuzgadoAdapter(){
    	super(CyqSecurityConstants.ABM_JUZGADO);
    }
    
    //  Getters y Setters
	public JuzgadoVO getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(JuzgadoVO juzgadoVO) {
		this.juzgado = juzgadoVO;
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
		 report.setReportTitle("Reporte de Juzgado");
		 report.setReportBeanName("Juzgado");
		 report.setReportFileName(this.getClass().getName());
	
		
		 // Planillas de Juzgado
		 ReportVO rtJuzgado = new ReportVO();
		 rtJuzgado.setReportTitle("Datos del Juzgado");
	    
	     // carga de columnas
	     // Descripción
		 rtJuzgado.addReportDato("Descripción", "desJuzgado");
		 
	     report.getListReport().add(rtJuzgado);
	     
	 			 //Abogados
				ReportTableVO rtAbogado = new ReportTableVO("Abogado");
				rtAbogado.setTitulo("Abogados del Juzgado");
				rtAbogado.setReportMetodo("listAbogado");
						     	 
				// carga de columnas
				//Desc.Abogado
				rtAbogado.addReportColumn("Desc.Abogado", "descripcion");
				//Domicilio
				rtAbogado.addReportColumn("Domicilio", "domicilio");
				//Telefono
				rtAbogado.addReportColumn("Telefono", "telefono");
			
				report.getReportListTable().add(rtAbogado);
	 	}
}
