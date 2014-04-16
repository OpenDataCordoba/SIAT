//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.fra.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.fra.iface.util.FraSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Frase
 * 
 * @author tecso
 */
public class FraseAdapter extends SiatAdapterModel{
	
	public static final String NAME = "fraseAdapterVO";
	
    private FraseVO frase = new FraseVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    
    // Constructores
    public FraseAdapter(){
    	super(FraSecurityConstants.ABM_FRASE);
    }
    
    //  Getters y Setters
	public FraseVO getFrase() {
		return frase;
	}

	public void setFrase(FraseVO fraseVO) {
		this.frase = fraseVO;
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
		 report.setReportTitle("Reporte de Frases");
		 report.setReportBeanName("frase");
		 report.setReportFileName(this.getClass().getName());
		 
		
		 // carga de filtros: ninguno
		 // Order by: no 
		 
		 ReportVO reportDatosExe = new ReportVO();
		 reportDatosExe.setReportTitle("Datos de Frases");
		 // carga de datos
	     //Fecha Solicitud
		 reportDatosExe.addReportDato("Modulo", "modulo");
	     //Recurso
		 reportDatosExe.addReportDato("Pagina", "pagina");
	     //Exención/Caso Social/Otro
		 reportDatosExe.addReportDato("Etiqueta", "etiqueta");
	     //Número Cuenta
		 reportDatosExe.addReportDato("Valor Publicado", "valorPublico");
	     //Solicitante
		 reportDatosExe.addReportDato("Valor no publicado", "valorPrivado");
	     //Observación
		 reportDatosExe.addReportDato("Descripción", "desFrase");
		
	     
		 report.getListReport().add(reportDatosExe);
	
		 // Formas de Pago de Obra
	/*	 ReportTableVO rtObraFormaPago = new ReportTableVO("ObraFormaPago");
	     rtObraFormaPago.setTitulo("Listado de Formas de Pago de la Obra");
	     rtObraFormaPago.setReportMetodo("listObraFormaPago");
	    
		
	     report.getReportListTable().add(rtObraFormaPago);*/
	}

	
	
}