//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Plan
 * 
 * @author Tecso
 *
 */
public class PlanSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planSearchPageVO";
	
	private PlanVO plan= new PlanVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	
	// Constructores
	public PlanSearchPage() {       
       super(GdeSecurityConstants.ABM_PLAN);        
    }
	
	// Getters y Setters
	public PlanVO getPlan() {
		return plan;
	}
	public void setPlan(PlanVO plan) {
		this.plan = plan;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	
	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Planes de Pagos");
		 report.setReportBeanName("Plan");
		 report.setReportFileName(this.getClass().getName());
		 
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getPlan().getRecurso().getId(), 
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 
		// Via deuda
		 String desViaDeuda = "";
		 
		 ViaDeudaVO viaDeudaVO = (ViaDeudaVO)ModelUtil.getBussImageModelByIdForList(
				 this.getPlan().getViaDeuda().getId(), 
				 this.getListViaDeuda());
		 if (viaDeudaVO != null){
			 desViaDeuda = viaDeudaVO.getDesViaDeuda();
		 }

		 // carga de filtros
		 report.addReportFiltro("Recurso", desRecurso);
		 report.addReportFiltro("Via Deuda", desViaDeuda);
		 report.addReportFiltro("Desc.Plan", this.getPlan().getDesPlan());
		 
		 // Order by
		 report.setReportOrderBy("desPlan ASC");
	     
	     ReportTableVO rtPlanPago = new ReportTableVO("rtPlanPago");
	     rtPlanPago.setTitulo("Listado de Planes de Pago");
	    
	     // carga de columnas
	     rtPlanPago.addReportColumn("Recurso", "desRecursos");
	     rtPlanPago.addReportColumn("Descripción", "desPlan");
	     rtPlanPago.addReportColumn("Via Deuda", "viaDeuda.desViaDeuda");
	     rtPlanPago.addReportColumn("Fec.Venc.Desde","fecVenDeuDes");
	     rtPlanPago.addReportColumn("Fec.Venc.Hasta","fecVenDeuHas");
	     rtPlanPago.addReportColumn("Cant.Cuotas","canMaxCuo");
	     rtPlanPago.addReportColumn("Estado", "estadoView");
	    
	     report.getReportListTable().add(rtPlanPago);

	    }
	

	
	// View getters
}
