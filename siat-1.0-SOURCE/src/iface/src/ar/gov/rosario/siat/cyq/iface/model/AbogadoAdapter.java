//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Abogado
 * 
 * @author tecso
 */
public class AbogadoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "abogadoAdapterVO";
	
    private AbogadoVO abogado = new AbogadoVO();
    
    private List<SiNo>  listSiNo = new ArrayList<SiNo>();
    private List<JuzgadoVO>  listJuzgado = new ArrayList<JuzgadoVO>();

    
    // Constructores
    public AbogadoAdapter(){
    	super(CyqSecurityConstants.ABM_ABOGADO);
    }
    
    //  Getters y Setters
	public AbogadoVO getAbogado() {
		return abogado;
	}

	public void setAbogado(AbogadoVO abogadoVO) {
		this.abogado = abogadoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<JuzgadoVO> getListJuzgado() {
		return listJuzgado;
	}

	public void setListJuzgado(List<JuzgadoVO> listJuzgado) {
		this.listJuzgado = listJuzgado;
	}
		
	  
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Abogado");
		 report.setReportBeanName("Abogado");
		 report.setReportFileName(this.getClass().getName());
	
		
		 // Planillas de Abogado
		 ReportVO rtAbogado = new ReportVO();
		 rtAbogado.setReportTitle("Datos del Abogado");
	    
	     // carga de columnas
	     // Descripción
		 rtAbogado.addReportDato("Desc.Abogado", "descripcion");
		 rtAbogado.addReportDato("Domicilio", "domicilio");
		 rtAbogado.addReportDato("Telefono", "telefono");
		 rtAbogado.addReportDato("Juzgado", "juzgado.desJuzgado");
			 
	     report.getListReport().add(rtAbogado);
	 
	 	}

}
