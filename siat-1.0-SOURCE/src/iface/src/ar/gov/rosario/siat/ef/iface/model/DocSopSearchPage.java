//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del DocSop
 * 
 * @author Tecso
 *
 */
public class DocSopSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "docSopSearchPageVO";
	
	private DocSopVO docSop= new DocSopVO();
	
	// Constructores
	public DocSopSearchPage() {       
       super(EfSecurityConstants.ABM_DOCSOP);        
    }
	
	// Getters y Setters
	public DocSopVO getDocSop() {
		return docSop;
	}
	public void setDocSop(DocSopVO docSop) {
		this.docSop = docSop;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de DocSop");
		report.setReportBeanName("DocSop");
		report.setReportFileName(this.getClass().getName());

        /* Codigo de ejemplo para mostrar filtros de Combos en los imprimir
		String desRecurso = "";

		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				this.getReclamo().getRecurso().getId(),
				this.getListRecurso());
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);*/

		//C�digo
		//report.addReportFiltro("C�digo", this.getDocSop().getCodDocSop());
       //Descripci�n
		report.addReportFiltro("Descripci�n", this.getDocSop().getDesDocSop());
		

		ReportTableVO rtDocSop = new ReportTableVO("rtDocSop");
		rtDocSop.setTitulo("B\u00FAsqueda de DocSop");

		// carga de columnas
		rtDocSop.addReportColumn("C�digo","codDocSop");
		rtDocSop.addReportColumn("Descripci�n", "desDocSop");
		
		 
	    report.getReportListTable().add(rtDocSop);

	}
	// View getters
}
