//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del UsoExpediente
 * 
 * @author Tecso
 *
 */
public class UsoExpedienteSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "usoExpedienteSearchPageVO";
	
	private UsoExpedienteVO usoExpediente= new UsoExpedienteVO();
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<SistemaOrigenVO> listSistemaOrigen = new ArrayList<SistemaOrigenVO>();
	
	
	private Date 	fechaDesde; 
	private Date 	fechaHasta;
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";


	// Constructores
	public UsoExpedienteSearchPage() {       
       super(CasSecurityConstants.ABM_USOEXPEDIENTE);        
    }
	
	// Getters y Setters
	public UsoExpedienteVO getUsoExpediente() {
		return usoExpediente;
	}
	public void setUsoExpediente(UsoExpedienteVO usoExpediente) {
		this.usoExpediente = usoExpediente;
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
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	// View getters
	public void setFechaDesdeView(String 	fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public String getFechaDesdeView() {
		return 	fechaDesdeView;
	}

	public void setFechaHastaView(String 	fechaHastaView) {
		this.fechaHastaView = 	fechaHastaView;
	}
	public String getFechaHastaView() {
		return 	fechaHastaView;
	}
	
	public List<SistemaOrigenVO> getListSistemaOrigen() {
		return listSistemaOrigen;
	}
	public void setListSistemaOrigen(List<SistemaOrigenVO> listSistemaOrigen) {
		this.listSistemaOrigen = listSistemaOrigen;
	}

	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Consulta de Uso de Expedientes");
		 report.setReportBeanName("UsoExpediente");
		 report.setReportFileName(this.getClass().getName());
		 
		 // recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getUsoExpediente().getCuenta().getRecurso().getId(), 
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
		 //Sistema origen
		 String desSistemaOrigen= "";
			
		 SistemaOrigenVO sistemaOrigenVO = CasoCache.getInstance().obtenerSistemaOrigenById(this.getUsoExpediente().getCaso().getSistemaOrigen().getId());
			 
		 if (sistemaOrigenVO != null){
			 desSistemaOrigen = sistemaOrigenVO.getDesSistemaOrigen();
		 }
		 report.addReportFiltro("Sistema Origen", desSistemaOrigen);
			
		 
		 // carga de filtros
		 report.addReportFiltro("Nro.Cuenta", this.getUsoExpediente().getCuenta().getNumeroCuenta());
	
		 report.addReportFiltro("Número", this.getUsoExpediente().getNumero());
			
		 report.addReportFiltro("Fecha Desde", this.getFechaDesdeView());
			
		 report.addReportFiltro("Fecha Hasta", this.getFechaHastaView());
			
		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtUsoExp = new ReportTableVO("rtUsoExp");
	     rtUsoExp.setTitulo("Listado de Consulta de Uso de Expedientes");
	   
	     // carga de columnas
	     rtUsoExp.addReportColumn("Fecha", "fechaAccion");
	     rtUsoExp.addReportColumn("Expediente", "descReport");
	     rtUsoExp.addReportColumn("Recurso", "cuenta.recurso.codRecurso");
	     rtUsoExp.addReportColumn("Cuenta", "cuenta.numeroCuenta");
		 rtUsoExp.addReportColumn("Descripción", "descripcion");
	    
	     report.getReportListTable().add(rtUsoExp);

	    }
	

}
