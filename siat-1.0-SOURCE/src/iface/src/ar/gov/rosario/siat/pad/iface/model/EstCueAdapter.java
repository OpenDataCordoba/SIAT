//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del EstCue
 * 
 * @author tecso
 */
public class EstCueAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "estCueAdapterVO";
	
	private EstCueVO estCue = new EstCueVO();
	
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public EstCueAdapter(){
    	super(PadSecurityConstants.ABM_ESTADOCUENTA);
    }
    
    //  Getters y Setters

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	
	public EstCueVO getEstCue() {
		return estCue;
	}

	public void setEstCue(EstCueVO estCue) {
		this.estCue = estCue;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Estado de Cuenta");     
		 report.setReportBeanName("EstCue");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		// Order by: no 
		 
		 ReportVO reportEstCue = new ReportVO();
		 reportEstCue.setReportTitle("Datos del Estado de Cuenta");
		 // carga de datos
	     
		 //Descripción
		 reportEstCue.addReportDato("Id", "id");
		 reportEstCue.addReportDato("Descripción", "descripcion");
	     
		 report.getListReport().add(reportEstCue);
	
	}
	
}