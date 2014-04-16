//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del CueExcSel
 * 
 * @author Tecso
 *
 */
public class CueExcSelSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cueExcSelSearchPageVO";
	
	private CueExcSelVO cueExcSel= new CueExcSelVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<AreaVO> listArea  = new ArrayList<AreaVO>(); 
	
	
	// Constructores
	public CueExcSelSearchPage() {       
       super(PadSecurityConstants.ABM_CUEEXCSEL);        
    }
	
	// Getters y Setters
	public CueExcSelVO getCueExcSel() {
		return cueExcSel;
	}
	public void setCueExcSel(CueExcSelVO cueExcSel) {
		this.cueExcSel = cueExcSel;
	}

	// View getters
	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Exención/Caso Social/Otro");
		 report.setReportBeanName("CueExcSel");
		 report.setReportFileName(this.getClass().getName());
		 
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCueExcSel().getCuenta().getRecurso().getId(),
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
		 // carga de filtros
		 report.addReportFiltro("Cuenta", this.getCueExcSel().getCuenta().getNumeroCuenta());
	     
        // Area
		 String desArea = "";
			
		 AreaVO areaVO = (AreaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCueExcSel().getArea().getId(),
				 this.getListArea());
		 if (areaVO != null){
			 desArea = areaVO.getDesArea();
		 }
		 report.addReportFiltro("Area", desArea);
		 
	
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtExe = new ReportTableVO("rtExe");
	     rtExe.setTitulo("Listado de Exención/Caso Social/Otro");
	   
	     // carga de columnas
	     rtExe.addReportColumn("Recurso", "cuenta.recurso.desRecurso");
	     rtExe.addReportColumn("Cuenta", "cuenta.numeroCuenta");
	     rtExe.addReportColumn("Area", "area.desArea");
	     rtExe.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtExe);

	    }
}
