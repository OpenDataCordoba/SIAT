//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;


/**
 * Search Page de Repartidor
 * @author tecso
 *
 */
public class RepartidorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "repartidorSearchPageVO";
	
	private RepartidorVO repartidor = new RepartidorVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<TipoRepartidorVO> listTipoRepartidor = new ArrayList<TipoRepartidorVO>();
	
	private List<RepartidorVO> listRepartidor = new ArrayList<RepartidorVO>();
	
	// Flags
	private int paramRecurso = 0;
	
	public RepartidorSearchPage(){
		super(PadSecurityConstants.ABM_REPARTIDOR);
	}

	// Getters y Setters
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<RepartidorVO> getListRepartidor() {
		return listRepartidor;
	}
	public void setListRepartidor(List<RepartidorVO> listRepartidor) {
		this.listRepartidor = listRepartidor;
	}
	public List<TipoRepartidorVO> getListTipoRepartidor() {
		return listTipoRepartidor;
	}
	public void setListTipoRepartidor(List<TipoRepartidorVO> listTipoRepartidor) {
		this.listTipoRepartidor = listTipoRepartidor;
	}
	public RepartidorVO getRepartidor() {
		return repartidor;
	}
	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}
	public int getParamRecurso() {
		return paramRecurso;
	}
	public void setParamRecurso(int paramRecurso) {
		this.paramRecurso = paramRecurso;
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Repartidores");
		 report.setReportBeanName("repartidor");
		 report.setReportFileName(this.getClass().getName());
		 
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getRepartidor().getRecurso().getId(),
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
			     
        // Tipo de Repartidor
		 String desTipoRepartidor = "";
			
		 TipoRepartidorVO tipoRepartidorVO = (TipoRepartidorVO) ModelUtil.getBussImageModelByIdForList(
				 this.getRepartidor().getTipoRepartidor().getId(),
				 this.getListTipoRepartidor());
		 if (tipoRepartidorVO != null){
			 desTipoRepartidor = tipoRepartidorVO.getDesTipoRepartidor();
		 }
		 report.addReportFiltro("Tipo de Repartidor", desTipoRepartidor);
		 
	
	     // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtRep = new ReportTableVO("rtRep");
	     rtRep.setTitulo("Listado de Repartidores");
	   
	     // carga de columnas
	     rtRep.addReportColumn("Recurso", "recurso.desRecurso");
	     rtRep.addReportColumn("Tipo de Repartidor", "tipoRepartidor.desTipoRepartidor");
	     rtRep.addReportColumn("Apellido y Nombre", "desRepartidor");
	   	 rtRep.addReportColumn("Broche", "broche.id");
	     rtRep.addReportColumn("Estado", "estadoView");
	     report.getReportListTable().add(rtRep);

	   }
}
