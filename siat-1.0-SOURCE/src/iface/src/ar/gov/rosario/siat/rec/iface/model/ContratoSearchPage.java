//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Contrato
 * 
 * @author Tecso
 *
 */
public class ContratoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "contratoSearchPageVO";
	
	private ContratoVO contrato= new ContratoVO();
	
    private List<RecursoVO>	listRecurso = new ArrayList<RecursoVO>();
    private List<TipoContratoVO> listTipoContrato = new ArrayList<TipoContratoVO>();    
	
	
	// Constructores
	public ContratoSearchPage() {       
       super(RecSecurityConstants.ABM_CONTRATO);        
    }
	
	// Getters y Setters
	public ContratoVO getContrato() {
		return contrato;
	}

	public void setContrato(ContratoVO contrato) {
		this.contrato = contrato;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoContratoVO> getListTipoContrato() {
		return listTipoContrato;
	}

	public void setListTipoContrato(List<TipoContratoVO> listTipoContrato) {
		this.listTipoContrato = listTipoContrato;
	}

	// View getters
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Consulta de Contrato");
		report.setReportBeanName("contrato");
		report.setReportFileName(this.getClass().getName());
		 
		// recurso
		String desRecurso = "";
			
		RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
			 this.getContrato().getRecurso().getId(),
			 this.getListRecurso());
		
		if (recursoVO != null){
			desRecurso = recursoVO.getDesRecurso();
		}
		report.addReportFiltro("Recurso", desRecurso);
		  
		// Tipo de Contrato
		String desTipoContrato = "";
			
		TipoContratoVO tipoContratoVO = (TipoContratoVO) ModelUtil.getBussImageModelByIdForList(
			 this.getContrato().getTipoContrato().getId(),
			 this.getListTipoContrato());
		
		if (tipoContratoVO != null){
		 desTipoContrato = tipoContratoVO.getDescripcion();
		}
		
		report.addReportFiltro("Tipo de Contrato", desTipoContrato);
		 
		// Número
		report.addReportFiltro("N\u00FAmero", this.getContrato().getNumero());
		 
		// Descripción
		report.addReportFiltro("Descripci\u00F3n", this.getContrato().getDescripcion());
	
		// Order by
		report.setReportOrderBy("contrato.recurso.desRecurso, contrato.tipoContrato, contrato.numero");
	     
		ReportTableVO rtCon = new ReportTableVO("rtCon");
		rtCon.setTitulo("Listado de Contratos");
	   
	    // carga de columnas
	    rtCon.addReportColumn("Recurso", "recurso.desRecurso");
	    rtCon.addReportColumn("Tipo de Contrato", "tipoContrato.descripcion");
	    rtCon.addReportColumn("N\u00FAmero", "numero");
	    rtCon.addReportColumn("Descripci\u00F3n", "descripcion", 40);
	    rtCon.addReportColumn("Importe", "importeForReport");
	    rtCon.addReportColumn("Estado", "estadoView");
	    report.getReportListTable().add(rtCon);
	}
}
