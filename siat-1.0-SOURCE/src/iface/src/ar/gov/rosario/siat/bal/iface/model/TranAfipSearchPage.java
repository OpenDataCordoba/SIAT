//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.afi.iface.util.AfiSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TranAfip
 * 
 * @author Tecso
 *
 */
public class TranAfipSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tranAfipSearchPageVO";
	
	private TranAfipVO tranAfip= new TranAfipVO();
	
	// Constructores
	public TranAfipSearchPage() {       
       super(AfiSecurityConstants.ABM_TRANAFIP);        
    }
	
	// Getters y Setters
	public TranAfipVO getTranAfip() {
		return tranAfip;
	}
	public void setTranAfip(TranAfipVO tranAfip) {
		this.tranAfip = tranAfip;
	}           

    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de TranAfip");
		report.setReportBeanName("TranAfip");
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

		//Código
//		report.addReportFiltro("Código", this.getTranAfip().getCodTranAfip());
       //Descripción
//		report.addReportFiltro("Descripción", this.getTranAfip().getDesTranAfip());
		

		ReportTableVO rtTranAfip = new ReportTableVO("rtTranAfip");
		rtTranAfip.setTitulo("B\u00FAsqueda de TranAfip");

		// carga de columnas
		rtTranAfip.addReportColumn("Código","codTranAfip");
		rtTranAfip.addReportColumn("Descripción", "desTranAfip");
		
		 
	    report.getReportListTable().add(rtTranAfip);

	}
	// View getters
}
