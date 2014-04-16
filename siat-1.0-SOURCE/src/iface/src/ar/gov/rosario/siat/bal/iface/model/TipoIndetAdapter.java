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
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del TipoIndet
 * 
 * @author tecso
 */
public class TipoIndetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoIndetAdapterVO";
	
    private TipoIndetVO tipoIndet = new TipoIndetVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public TipoIndetAdapter(){
    	super(BalSecurityConstants.ABM_TIPOINDET);
    }
    
    //  Getters y Setters
	public TipoIndetVO getTipoIndet() {
		return tipoIndet;
	}

	public void setTipoIndet(TipoIndetVO tipoIndetVO) {
		this.tipoIndet = tipoIndetVO;
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
		 report.setReportTitle("Reporte de TipoIndet");     
		 report.setReportBeanName("TipoIndet");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportTipoIndet = new ReportVO();
		 reportTipoIndet.setReportTitle("Datos del TipoIndet");
		 // carga de datos
	     
	     //Codigo
		 reportTipoIndet.addReportDato("C\u00F3digo", "codTipoIndet");
		 //Descripcion
		 reportTipoIndet.addReportDato("Descripci\u00F3n", "desTipoIndet");
		 //Codigo MR
		 reportTipoIndet.addReportDato("C\u00F3digo MR", "codIndetMR");

		 
		 report.getListReport().add(reportTipoIndet);
	
	}
	
	// View getters
}