//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del EmiMat
 * 
 * @author Tecso
 *
 */
public class EmiMatSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "emiMatSearchPageVO";
	
	private EmiMatVO emiMat= new EmiMatVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Constructores
	public EmiMatSearchPage() {       
       super(DefSecurityConstants.ABM_EMIMAT);        
    }
	
	// Getters y Setters
	public EmiMatVO getEmiMat() {
		return emiMat;
	}
	public void setEmiMat(EmiMatVO emiMat) {
		this.emiMat = emiMat;
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

		ReportVO report = this.getReport();
		report.setReportFormat(format);	
		report.setReportTitle("Listado de Tablas de Par\u00E1metros de Emisi\u00F3n");
		report.setReportBeanName("EmiMat");
		report.setReportFileName(this.getClass().getName());

		// Código
		report.addReportFiltro("Código", 
				this.getEmiMat().getCodEmiMat());
		// Recurso
	    String desRecurso = "";
		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
			 this.getEmiMat().getRecurso().getId(), 
			 this.getListRecurso());
	    
	    if (recursoVO != null){
	    	desRecurso = recursoVO.getDesRecurso();
		}
	    report.addReportFiltro("Recurso", desRecurso);
	    
	    // Order by
	    report.setReportOrderBy("recurso.desRecurso, codEmiMat");

		ReportTableVO rtEmiMat = new ReportTableVO("rtEmiMat");
		rtEmiMat.setTitulo("B\u00FAsqueda de Tablas");

		// Carga de columnas
		rtEmiMat.addReportColumn("Código","codEmiMat");
		rtEmiMat.addReportColumn("Recurso", "recurso.desRecurso");
		rtEmiMat.addReportColumn("Estado", "estadoView");
		
		report.getReportListTable().add(rtEmiMat);

	}
}
