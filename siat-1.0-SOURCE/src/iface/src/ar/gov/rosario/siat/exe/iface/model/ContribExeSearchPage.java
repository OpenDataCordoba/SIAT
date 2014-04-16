//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage de la Exencion de contribuyente
 * 
 * @author Tecso
 *
 */
public class ContribExeSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "contribExeSearchPageVO";

	private ContribExeVO contribExe = new ContribExeVO();

	private List<ExencionVO> listExencion = new ArrayList<ExencionVO>();

	private Date 	fechaDesde; 
	private Date 	fechaHasta; 

	private String 	fechaDesdeView = "";
	private String 	fechaHastaView = "";
	
	// Constructor
	public ContribExeSearchPage() {
       super(ExeSecurityConstants.ABM_CONTRIBEXE);        
    }

	// Getters y Setters	
	public ContribExeVO getContribExe() {
		return contribExe;
	}

	public void setContribExe(ContribExeVO contribExe) {
		this.contribExe = contribExe;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}
	
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	//	 View getters
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Sujetos Exentos");
		 report.setReportBeanName("ContribExe");
		 report.setReportFileName(this.getClass().getName());
		
		 // Descripción
		
		 report.addReportFiltro("Descripción", this.getContribExe().getDesContribExe());
		 
		 // Exención/Caso Social/Otro
		 String desExencion = "";
			
		 ExencionVO exencionVO = (ExencionVO) ModelUtil.getBussImageModelByIdForList(
				 this.getContribExe().getExencion().getId(),
				 this.getListExencion());
		 if (exencionVO != null){
			 desExencion = exencionVO.getDesExencion();
		 }
		 report.addReportFiltro("Exención/Caso Social/Otro", desExencion);
		 
		 // Fecha Desde
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
	     
		// Fecha Hasta
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
	     
		 
	
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtSuj = new ReportTableVO("rtSuj");
	     rtSuj.setTitulo("Listado de Sujetos de Exentos");
	   
	     // carga de columnas
	     rtSuj.addReportColumn("Descripción", "desContribExe");
	     rtSuj.addReportColumn("Persona", "personaFromGeneral.represent");
	     rtSuj.addReportColumn("Exención/Caso Social/Otro", "exencion.desExencion");
	     rtSuj.addReportColumn("Vigencia", "desVigencia");
	     report.getReportListTable().add(rtSuj);

	    }
	
}
