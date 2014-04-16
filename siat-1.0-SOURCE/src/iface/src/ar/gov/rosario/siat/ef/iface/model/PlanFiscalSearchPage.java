//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del PlanFiscal
 * 
 * @author Tecso
 *
 */
public class PlanFiscalSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planFiscalSearchPageVO";
	
	private PlanFiscalVO planFiscal= new PlanFiscalVO();
	
	private List<EstadoPlanFisVO> listEstadoPlanFis = new ArrayList<EstadoPlanFisVO>();
	
	// Constructores
	public PlanFiscalSearchPage() {       
       super(EfSecurityConstants.ADM_PLANFISCAL);        
    }
	
	// Getters y Setters
	public PlanFiscalVO getPlanFiscal() {
		return planFiscal;
	}
	public void setPlanFiscal(PlanFiscalVO planFiscal) {
		this.planFiscal = planFiscal;
	}

	public List<EstadoPlanFisVO> getListEstadoPlanFis() {
		return listEstadoPlanFis;
	}

	public void setListEstadoPlanFis(List<EstadoPlanFisVO> listEstadoPlanfis) {
		this.listEstadoPlanFis = listEstadoPlanfis;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Planes Fiscales");
		 report.setReportBeanName("PlanFiscal");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 
		 report.addReportFiltro("Descripción", this.getPlanFiscal().getDesPlanFiscal());
		 
		 // Estado Plan
		 String desEstadoPlan = "";
			
		 EstadoPlanFisVO estadoPlanVO = (EstadoPlanFisVO) ModelUtil.getBussImageModelByIdForList(
				 this.getPlanFiscal().getEstadoPlanFis().getId(),
				 this.getListEstadoPlanFis());
		 if (estadoPlanVO != null){
			 desEstadoPlan = estadoPlanVO.getDesEstadoPlanFis();
		 }
		 report.addReportFiltro("Estado Plan", desEstadoPlan);
		 
		 // Fecha Desde
		 report.addReportFiltro("Fecha Desde", this.getPlanFiscal().getFechaDesdeView());
	     
		// Fecha Hasta
		 report.addReportFiltro("Fecha Hasta", this.getPlanFiscal().getFechaHastaView());
	     
        
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtPla = new ReportTableVO("rtPla");
	     rtPla.setTitulo("Listado de Planes Fiscales");
	   
	     // carga de columnas
	     rtPla.addReportColumn("Descripción", "desPlanFiscal");
	     rtPla.addReportColumn("Fecha Desde", "fechaDesde");
	     rtPla.addReportColumn("Fecha Hasta", "fechaHasta");
	     rtPla.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtPla);

	    }
}
