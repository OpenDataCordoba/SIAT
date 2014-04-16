//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del UsoExpediente
 * 
 * @author tecso
 */
public class UsoExpedienteAdapter extends SiatAdapterModel{
	
	public static final String NAME = "usoExpedienteAdapterVO";
	
    private UsoExpedienteVO usoExpediente = new UsoExpedienteVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public UsoExpedienteAdapter(){
    	super(CasSecurityConstants.ABM_USOEXPEDIENTE);
    }
    
    //  Getters y Setters
	public UsoExpedienteVO getUsoExpediente() {
		return usoExpediente;
	}

	public void setUsoExpediente(UsoExpedienteVO usoExpedienteVO) {
		this.usoExpediente = usoExpedienteVO;
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
		 report.setReportTitle("Consulta de Uso de Expedientes");
		 report.setReportBeanName("UsoExpediente");
		 report.setReportFileName(this.getClass().getName());
		 
	     ReportVO rtUsoExp = new ReportVO();
	     rtUsoExp.setReportTitle("Listado de Consulta de Uso de Expedientes");
	    
	     // carga de columnas
	     rtUsoExp.addReportDato("Fecha", "fechaAccion");
	     rtUsoExp.addReportDato("Sistema", "sistemaOrigen.desSistemaOrigen");
	     rtUsoExp.addReportDato("Número", "numero");
	     rtUsoExp.addReportDato("Recurso", "cuenta.recurso.desRecurso");
	     rtUsoExp.addReportDato("Cuenta", "cuenta.id");
	     rtUsoExp.addReportDato("Descripcion", "descripcion");
	    
	     report.getListReport().add(rtUsoExp);

	    }
	
	
}
