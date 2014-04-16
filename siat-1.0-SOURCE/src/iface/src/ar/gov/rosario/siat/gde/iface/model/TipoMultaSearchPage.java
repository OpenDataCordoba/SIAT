//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecClaDeuVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del TipoMulta
 * 
 * @author Tecso
 *
 */
public class TipoMultaSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "tipoMultaSearchPageVO";
	
	private TipoMultaVO tipoMulta= new TipoMultaVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	private List<RecClaDeuVO> listRecClaDeu = new ArrayList<RecClaDeuVO>();
	
	// Constructores
	public TipoMultaSearchPage() {       
       super(GdeSecurityConstants.ABM_TIPOMULTA);        
    }
	
	// Getters y Setters
	public TipoMultaVO getTipoMulta() {
		return tipoMulta;
	}
	public void setTipoMulta(TipoMultaVO tipoMulta) {
		this.tipoMulta = tipoMulta;
	}           

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<RecClaDeuVO> getListRecClaDeu() {
		return listRecClaDeu;
	}

	public void setListRecClaDeu(List<RecClaDeuVO> listRecClaDeu) {
		this.listRecClaDeu = listRecClaDeu;
	}
	
    public String getName(){    
		return NAME;
	}
	
	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Listados de Tipo de Multa");
		report.setReportBeanName("TipoMulta");
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

       //Descripción
		report.addReportFiltro("Descripción", this.getTipoMulta().getDesTipoMulta());
		

		ReportTableVO rtTipoMulta = new ReportTableVO("rtTipoMulta");
		rtTipoMulta.setTitulo("B\u00FAsqueda de Tipo Multa");

		// carga de columnas
		rtTipoMulta.addReportColumn("Descripción", "desTipoMulta");
		
		 
	    report.getReportListTable().add(rtTipoMulta);

	}
	// View getters
}
