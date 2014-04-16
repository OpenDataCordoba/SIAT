//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TipoIndet
 * 
 * @author Tecso
 *
 */
public class TipoIndetSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoIndetSearchPageVO";
	
	private TipoIndetVO tipoIndet= new TipoIndetVO();
	
	// Constructores
	public TipoIndetSearchPage() {       
       super(BalSecurityConstants.ABM_TIPOINDET);        
    }
	
	// Getters y Setters
	public TipoIndetVO getTipoIndet() {
		return tipoIndet;
	}
	public void setTipoIndet(TipoIndetVO tipoIndet) {
		this.tipoIndet = tipoIndet;
	}           

	public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de TipoIndet");
		report.setReportBeanName("TipoIndet");
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

		//Codigo
		report.addReportFiltro("C\u00F3digo", this.getTipoIndet().getCodTipoIndet());
        //Descripcion
		report.addReportFiltro("Descripci\u00F3n", this.getTipoIndet().getDesTipoIndet());
		//Codigo MR
		report.addReportFiltro("C\u00F3digo Indet MR", this.getTipoIndet().getCodIndetMR());
		

		ReportTableVO rtTipoIndet = new ReportTableVO("rtTipoIndet");
		rtTipoIndet.setTitulo("B\u00FAsqueda de TipoIndet");

		// carga de columnas
		rtTipoIndet.addReportColumn("C\u00F3digo","codTipoIndet");
		rtTipoIndet.addReportColumn("Descripci\u00F3n", "desTipoIndet");
		rtTipoIndet.addReportColumn("C\u00F3d. Indet. MR", "codIndetMR");
		
	    report.getReportListTable().add(rtTipoIndet);

	}

}
