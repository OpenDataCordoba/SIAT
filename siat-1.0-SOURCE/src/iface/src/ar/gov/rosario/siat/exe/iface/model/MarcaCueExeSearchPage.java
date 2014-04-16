//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.exe.iface.model;
    
import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.exe.iface.util.ExeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del CueExe
 * 
 * @author Tecso
 *
 */
public class MarcaCueExeSearchPage extends SiatPageModel {
	         
	private static final long serialVersionUID = 1L;

	public static final String NAME = "marcaCueExeSearchPageVO";
	
	
	private CueExeVO cueExe= new CueExeVO();
	
	private EstadoCueExeVO estadoACambiar = new EstadoCueExeVO();
	
	private String[] idsSelected;
	
	
    private List<RecursoVO> 	 listRecurso = new ArrayList<RecursoVO>();	
    private List<ExencionVO>	 listExencion  = new ArrayList<ExencionVO>();
    private List<EstadoCueExeVO> listEstadoCueExe = new ArrayList<EstadoCueExeVO>();
    private List<EstadoCueExeVO> listEstadoAMarcar = new ArrayList<EstadoCueExeVO>();
    
    // Bandera para consulta estado en historico o no en en historico
    private Boolean estadoEnHistorico = true;
    private boolean modoVer = false;
    
    private boolean disableCombo =false;
    private boolean conExencionPreseteada = false;
    
    
	// Constructor
	public MarcaCueExeSearchPage() {       
       super(ExeSecurityConstants.ABM_CUEEXE);        
    }
	
	// Getters y Setters
	public CueExeVO getCueExe() {
		return cueExe;
	}
	public void setCueExe(CueExeVO cueExe) {
		this.cueExe = cueExe;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}
	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

	public List<EstadoCueExeVO> getListEstadoCueExe() {
		return listEstadoCueExe;
	}
	public void setListEstadoCueExe(List<EstadoCueExeVO> listEstadoCueExe) {
		this.listEstadoCueExe = listEstadoCueExe;
	}

	public Boolean getEstadoEnHistorico() {
		return estadoEnHistorico;
	}
	public void setEstadoEnHistorico(Boolean estadoEnHistorico) {
		this.estadoEnHistorico = estadoEnHistorico;
	}

	public boolean isModoVer() {
		return modoVer;
	}
	public void setModoVer(boolean modoVer) {
		this.modoVer = modoVer;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("B\u00FAsqueda de Exenciones");
		 report.setReportBeanName("CueExe");
		 report.setReportFileName(this.getClass().getName());
		 
		 
		 
		 // carga de filtros
		 
		// Recurso
		 String desRecurso = "";
			
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCueExe().getRecurso().getId(),
				 this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 
		 // Exención/Caso Social/Otro
		 String desExencion = "";
			
		 ExencionVO exencionVO = (ExencionVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCueExe().getExencion().getId(),
				 this.getListExencion());
		 if (exencionVO != null){
			 desExencion = exencionVO.getDesExencion();
		 }
		 
		 report.addReportFiltro("Exención/Caso Social/Otro", desExencion);
		 
         //Cuenta
		 report.addReportFiltro("N\u00FAmero Cuenta", this.getCueExe().getCuenta().getNumeroCuenta());
		 
		 //Fecha Desde
		 report.addReportFiltro("Fecha Desde", this.getCueExe().getFechaDesdeView());
			
		 //Fecha Hasta
		 report.addReportFiltro("Fecha Hasta", this.getCueExe().getFechaHastaView());
		 
		// Estado
		 String desEstado = "";
			
		 EstadoCueExeVO estadoVO = (EstadoCueExeVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCueExe().getEstadoCueExe().getId(),
				 this.getListEstadoCueExe());
		 if (estadoVO != null){
			 desEstado = estadoVO.getDesEstadoCueExe();
		 }
		 		 
		 report.addReportFiltro("Estado", desEstado);
		 
		 System.out.println("this.getEstadoEnHistorico()= "+this.getEstadoEnHistorico().toString());

		 if(this.getEstadoEnHistorico()) {
			 // Estado en Historico
			 String desEstadoHis = "";

			 EstadoCueExeVO hisEstCueExeVO = (EstadoCueExeVO) ModelUtil.getBussImageModelByIdForList(
					 this.getCueExe().getHisEstCueExe().getEstadoCueExe().getId(),
					 this.getListEstadoCueExe());
			 if (hisEstCueExeVO != null){
				 desEstadoHis = hisEstCueExeVO.getDesEstadoCueExe();
			 }
			 report.addReportFiltro("Estado en Historico", desEstadoHis);
		 }


		 // Order by
		 //report.setReportOrderBy("desTipoObra ASC");
	     
	     ReportTableVO rtExe = new ReportTableVO("rtExe");
	     rtExe.setTitulo("B\u00FAsqueda de Exenciones");
	   
	     // carga de columnas 
	     rtExe.addReportColumn("Fecha Solicitud","fechaSolicitud");
	     rtExe.addReportColumn("Recurso", "cuenta.recurso.desRecurso");
	     rtExe.addReportColumn("N\u00FAmero cuenta", "cuenta.numeroCuenta");
	     rtExe.addReportColumn("Tipo de Sujeto", "tipoSujeto.desTipoSujeto");
	     rtExe.addReportColumn("Fecha Desde", "fechaDesde");
	     rtExe.addReportColumn("Fecha Hasta", "fechaHasta");
	     rtExe.addReportColumn("Exención/Caso Social/Otro", "exencion.desExencion");
	     rtExe.addReportColumn("Estado", "estadoCueExe.desEstadoCueExe");
	     report.getReportListTable().add(rtExe);

	    }

	public boolean getDisableCombo() {
		return disableCombo;
	}

	public void setDisableCombo(boolean disableCombo) {
		this.disableCombo = disableCombo;
	}

	public boolean getConExencionPreseteada() {
		return conExencionPreseteada;
	}

	public void setConExencionPreseteada(boolean conExencionPreseteada) {
		this.conExencionPreseteada = conExencionPreseteada;
	}

	public String[] getIdsSelected() {
		return idsSelected;
	}

	public void setIdsSelected(String[] idsSelected) {
		this.idsSelected = idsSelected;
	}

	public List<EstadoCueExeVO> getListEstadoAMarcar() {
		return listEstadoAMarcar;
	}

	public void setListEstadoAMarcar(List<EstadoCueExeVO> listEstadoAMarcar) {
		this.listEstadoAMarcar = listEstadoAMarcar;
	}

	public EstadoCueExeVO getEstadoACambiar() {
		return estadoACambiar;
	}

	public void setEstadoACambiar(EstadoCueExeVO estadoACambiar) {
		this.estadoACambiar = estadoACambiar;
		
	}

	public String getCantResult(){
		return StringUtil.formatInteger(getListResult().size());
	}


    
}
