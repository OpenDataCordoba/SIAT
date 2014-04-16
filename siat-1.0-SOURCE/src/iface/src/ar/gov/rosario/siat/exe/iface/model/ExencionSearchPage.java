//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Exencion
 * 
 * @author Tecso
 *
 */
public class ExencionSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "exencionSearchPageVO";
	
	private ExencionVO exencion= new ExencionVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructor
	public ExencionSearchPage() {       
       super(ExeSecurityConstants.ABM_EXENCION);        
    }
	
	// Getters y Setters
	public ExencionVO getExencion() {
		return exencion;
	}
	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
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
		 report.setReportBeanName("Exencion");
		 report.setReportFileName(this.getClass().getName());
		 
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getExencion().getRecurso().getId(),
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
		 // carga de filtros
		 report.addReportFiltro("Código", this.getExencion().getCodExencion());
	
		 report.addReportFiltro("Descripción", this.getExencion().getDesExencion());
	
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtExe = new ReportTableVO("rtExe");
	     rtExe.setTitulo("Listado de Exención/Caso Social/Otro");
	   
	     // carga de columnas
	     rtExe.addReportColumn("Código", "codExencion");
	     rtExe.addReportColumn("Descripción", "desExencion");
	     rtExe.addReportColumn("Recurso", "recurso.desRecurso");
	     rtExe.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtExe);

	    }
		
}
