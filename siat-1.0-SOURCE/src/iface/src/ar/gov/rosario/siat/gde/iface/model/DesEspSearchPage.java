//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.TipoDeudaVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del DesEsp
 * 
 * @author Tecso
 *
 */
public class DesEspSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "desEspSearchPageVO";
	
	private DesEspVO desEsp= new DesEspVO();
	
	private List<RecursoVO> listRecurso= new ArrayList<RecursoVO>();
	
	private List<TipoDeudaVO> listTipoDeuda;
	
	private List<ViaDeudaVO> listViaDeuda;
	
	// Constructores
	public DesEspSearchPage() {       
       super(GdeSecurityConstants.ABM_DESESP);        
    }
	
	// Getters y Setters
	public DesEspVO getDesEsp() {
		return desEsp;
	}
	public void setDesEsp(DesEspVO desEsp) {
		this.desEsp = desEsp;
	}


	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List getListTipoDeuda() {
		return listTipoDeuda;
	}

	public void setListTipoDeuda(List<TipoDeudaVO> listTipoDeuda) {
		this.listTipoDeuda = listTipoDeuda;
	}

	public List getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}
	
	public String getName(){
		return NAME;
	}
		
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Descuento Especial");
		 report.setReportBeanName("DesEsp");
		 report.setReportFileName(this.getClass().getName());
		 
	     ReportTableVO rtDesEsp = new ReportTableVO("rtDesEsp");
	     rtDesEsp.setTitulo("Listado de Consulta de Descuento Especial");
	    
	     // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getDesEsp().getRecurso().getId(), 
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
		 //Tipo Deuda
		 String desTipoDeuda= "";
			
		 TipoDeudaVO tipoDeudaVO = (TipoDeudaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getDesEsp().getTipoDeuda().getId(),
				 this.getListTipoDeuda());
		 if (tipoDeudaVO != null){
			 desTipoDeuda = tipoDeudaVO.getDesTipoDeuda();
		 }
		 
		 report.addReportFiltro("Tipo de Deuda", desTipoDeuda);
		 
		 //VÃ­a Deuda
		 String desViaDeuda= "";
			
		 ViaDeudaVO viaDeudaVO = (ViaDeudaVO) ModelUtil.getBussImageModelByIdForList(
				 this.getDesEsp().getViaDeuda().getId(),
				 this.getListViaDeuda());
		 if (viaDeudaVO != null){
			 desViaDeuda = viaDeudaVO.getDesViaDeuda();
		 }
		 
		 report.addReportFiltro("Vía Deuda", desViaDeuda);
	
		 report.addReportFiltro("Descripción", this.getDesEsp().getDesDesEsp());
		 		 
	     // carga de columnas
	     rtDesEsp.addReportColumn("Descripción", "desDesEsp");
	     rtDesEsp.addReportColumn("Recurso", "recurso.desRecurso");
	     rtDesEsp.addReportColumn("Tipo de Deuda", "tipoDeuda.desTipoDeuda");
	     rtDesEsp.addReportColumn("Vía Deuda", "viaDeuda.desViaDeuda");
	     rtDesEsp.addReportColumn("Estado", "estadoView");
	    
	     report.getReportListTable().add(rtDesEsp);

	    }
	
}
