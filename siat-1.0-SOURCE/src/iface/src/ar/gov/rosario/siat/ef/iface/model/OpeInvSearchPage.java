//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del OpeInv
 * 
 * @author Tecso
 *
 */
public class OpeInvSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "opeInvSearchPageVO";
	
	private OpeInvVO opeInv= new OpeInvVO();
	
	private List<PlanFiscalVO> listPlanFiscal = new ArrayList<PlanFiscalVO>();
	private List<EstOpeInvVO> listEstOpeInv = new ArrayList<EstOpeInvVO>();
	
	// Flags
	private boolean contribuyenteBussEnabled = true;
	
	// Constructores
	public OpeInvSearchPage() {       
       super(EfSecurityConstants.ABM_OPEINV);        
    }
	
	// Getters y Setters
	public OpeInvVO getOpeInv() {
		return opeInv;
	}
	public void setOpeInv(OpeInvVO opeInv) {
		this.opeInv = opeInv;
	}

	public List<PlanFiscalVO> getListPlanFiscal() {
		return listPlanFiscal;
	}

	public void setListPlanFiscal(List<PlanFiscalVO> listPlan) {
		this.listPlanFiscal = listPlan;
	}

	public List<EstOpeInvVO> getListEstOpeInv() {
		return listEstOpeInv;
	}

	public void setListEstOpeInv(List<EstOpeInvVO> listEstOpeInv) {
		this.listEstOpeInv = listEstOpeInv;
	}


	public String getContribuyenteBussEnabled() {
		return SiatBussImageModel.hasEnabledFlag(contribuyenteBussEnabled, EfSecurityConstants.ADM_OPEINVCON, EfSecurityConstants.ADM_OPEINVCON_BUSCAR);
	}

	public void setContribuyenteBussEnabled(boolean value) {
		contribuyenteBussEnabled= value;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Operativos de Investigación");
		 report.setReportBeanName("OpeInv");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		
		 // Plan Fiscal
		 String desPlanFiscal = "";
			
		 PlanFiscalVO planFiscalVO = (PlanFiscalVO) ModelUtil.getBussImageModelByIdForList(
				 this.getOpeInv().getPlanFiscal().getId(),
				 this.getListPlanFiscal());
		 if (planFiscalVO != null){
			 desPlanFiscal = planFiscalVO.getDesPlanFiscal();
		 }
		
			 report.addReportFiltro("Plan Fiscal", desPlanFiscal);
		 
		 // Estado del Operativo
		 String desEstadoOpe = "";
			
		 EstOpeInvVO estOpeInvVO = (EstOpeInvVO) ModelUtil.getBussImageModelByIdForList(
				 this.getOpeInv().getEstOpeInv().getId(),
				 this.getListEstOpeInv());
		 if (estOpeInvVO != null){
			 desEstadoOpe = estOpeInvVO.getDesEstOpeInv();
		 }
		 report.addReportFiltro("Estado del Operativo", desEstadoOpe);
		 
		
        
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtOpe = new ReportTableVO("rtOpe");
	     rtOpe.setTitulo("Listado de Operativos de Investigación");
	   
	     // carga de columnas
	     rtOpe.addReportColumn("Descripción", "desOpeInv");
	     rtOpe.addReportColumn("Plan Fiscal", "planFiscal.desPlanFiscal");
	     rtOpe.addReportColumn("Fecha de Inicio", "fechaInicio");
	     rtOpe.addReportColumn("Estado", "estOpeInv.desEstOpeInv");
	     report.getReportListTable().add(rtOpe);

	    }
}
