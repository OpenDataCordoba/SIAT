//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del Procurador
 * 
 * @author tecso
 */
public class ProcuradorAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "procuradorAdapterVO";
	
	public static final String ENC_NAME = "encProcuradorAdapterVO";
	
    private ProcuradorVO procurador = new ProcuradorVO();
    
    private List<TipoProcuradorVO> listTipoProcurador = new ArrayList<TipoProcuradorVO>();
    
    // Constructores
    public ProcuradorAdapter(){
    	super(GdeSecurityConstants.ABM_PROCURADOR);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_PROCURADOR_ENC;
    }
    
    //  Getters y Setters
	public ProcuradorVO getProcurador() {
		return procurador;
	}

	public void setProcurador(ProcuradorVO procuradorVO) {
		this.procurador = procuradorVO;
	}

	public List<TipoProcuradorVO> getListTipoProcurador() {
		return listTipoProcurador;
	}

	public void setListTipoProcurador(List<TipoProcuradorVO> listTipoProcurador) {
		this.listTipoProcurador = listTipoProcurador;
	}

	public String getName(){
		return NAME;
	}
	
		
	public void prepareReport(Long format) {
		 
		 ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Procurador");
		 report.setReportBeanName("procurador");
		 report.setReportFileName(this.getClass().getName());
		 
		// carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosPro = new ReportVO();
		 reportDatosPro.setReportTitle("Datos de Procurador");
		 // carga de datos
		
	     //Descripción                                
		 reportDatosPro.addReportDato("Descripción", "descripcion");
	     //Domicilio
		 reportDatosPro.addReportDato("Domicilio", "domicilio");
		 //Teléfono
		 reportDatosPro.addReportDato("Teléfono", "telefono");
	     //Horario de Atención
		 reportDatosPro.addReportDato("Horario de Atención", "horarioAtencion");
	     //Observación
		 reportDatosPro.addReportDato("Observación", "observacion");
	     //Tipo
		 reportDatosPro.addReportDato("Tipo", "tipoProcurador.desTipoProcurador");
	   
		 report.getListReport().add(reportDatosPro);
	
		 // Formas de Pago de Obra
	/*	 ReportTableVO rtObraFormaPago = new ReportTableVO("ObraFormaPago");
	     rtObraFormaPago.setTitulo("Listado de Formas de Pago de la Obra");
	     rtObraFormaPago.setReportMetodo("listObraFormaPago");
	     report.getReportListTable().add(rtObraFormaPago);*/
	}

}
