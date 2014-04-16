//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Vencimientos
 * @author tecso
 *
 */
public class VencimientoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "vencimientoSearchPageVO";
	
	private VencimientoVO vencimiento = new VencimientoVO();
	
	private List<VencimientoVO> listVencimiento = new ArrayList<VencimientoVO>();
	
	public VencimientoSearchPage(){
		super(DefSecurityConstants.ABM_VENCIMIENTO);
	}

	// Getters y Setters
	
	public VencimientoVO getVencimiento(){
		return vencimiento;
	}
	public void setVencimiento(VencimientoVO vencimiento){
		this.vencimiento = vencimiento;
	}
	public List<VencimientoVO> getListVencimiento(){
		return listVencimiento;
	}
	public void setListVencimiento(List<VencimientoVO> listVencimiento){
		this.listVencimiento = listVencimiento;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Vencimientos");
		 report.setReportBeanName("Vencimientos");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 report.addReportFiltro("Descripción", this.getVencimiento().getDesVencimiento());
		 
		 // Order by
		 report.setReportOrderBy("desVencimiento ASC");
		 
	     ReportTableVO rtVencimiento = new ReportTableVO("Vencimiento");
	     rtVencimiento.setTitulo("Listado de Vencimientos");
	     
		 // carga de columnas
	     rtVencimiento.addReportColumn("Descripción", "desVencimiento").setWidth(60);
	     rtVencimiento.addReportColumn("Día", "dia");
	     rtVencimiento.addReportColumn("Mes", "mes");
	     rtVencimiento.addReportColumn("Hábil", "esHabilView");
	     rtVencimiento.addReportColumn("Ctd. Días", "cantDias");
	     rtVencimiento.addReportColumn("Ctd. Mes", "cantMes");
	     rtVencimiento.addReportColumn("Ctd. Sem.", "cantSemana");
	     rtVencimiento.addReportColumn("1er. Día Sem.", "primeroSemanaView");
	     rtVencimiento.addReportColumn("Ult. Día Sem.", "ultimoSemanaView");
	     rtVencimiento.addReportColumn("Ultimo", "esUltimoView");
	     rtVencimiento.addReportColumn("Estado", "estadoView");
	     
	     report.getReportListTable().add(rtVencimiento);
	}

}
