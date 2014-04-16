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
 * Adapter del TipoCob
 * 
 * @author tecso
 */
public class TipoCobAdapter extends SiatAdapterModel {
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tipoCobAdapterVO";
	
    private TipoCobVO tipoCob = new TipoCobVO();
    
	private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
    
    // Constructores
    public TipoCobAdapter(){
    	super(BalSecurityConstants.ABM_TIPOCOB);
    }
    
    //  Getters y Setters
	public TipoCobVO getTipoCob() {
		return tipoCob;
	}

	public void setTipoCob(TipoCobVO tipoCobVO) {
		this.tipoCob = tipoCobVO;
	}

	public List<PartidaVO> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}

	public String getName(){
		return NAME;
	}
			
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de TipoCob");     
		 report.setReportBeanName("TipoCob");
		 report.setReportFileName(this.getClass().getName());
		 
		 ReportVO reportTipoCob = new ReportVO();
		 reportTipoCob.setReportTitle("Datos del TipoCob");
		 // carga de datos
	     
		 //Descripción
		 reportTipoCob.addReportDato("Descripción", "desTipoCob");
	     
		 report.getListReport().add(reportTipoCob);
	}
	
}
