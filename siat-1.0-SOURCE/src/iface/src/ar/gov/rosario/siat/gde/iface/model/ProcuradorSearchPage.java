//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del Procurador
 * 
 * @author Tecso
 *
 */
public class ProcuradorSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procuradorSearchPageVO";
	
	private ProcuradorVO procurador= new ProcuradorVO();
	
	private List<TipoProcuradorVO> listTipoProcurador = new ArrayList<TipoProcuradorVO>();
	
	// Constructores
	public ProcuradorSearchPage() {       
       super(GdeSecurityConstants.ABM_PROCURADOR);        
    }
	
	// Getters y Setters
	public ProcuradorVO getProcurador() {
		return procurador;
	}
	public void setProcurador(ProcuradorVO procurador) {
		this.procurador = procurador;
	}

	public List<TipoProcuradorVO> getListTipoProcurador() {
		return listTipoProcurador;
	}

	public void setListTipoProcurador(List<TipoProcuradorVO> listTipoProcurador) {
		this.listTipoProcurador = listTipoProcurador;
	}

	// View getters
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		
		ReportVO reportVO = this.getReport();
		
		 reportVO.setReportTitle("Reporte de Procuradores del Siat");
		 reportVO.setReportBeanName("Procuradores");
		 reportVO.setReportFormat(format);
		 // nombre del archivo a generar en base al class del SearchPage id usuario y formato pasado
		 reportVO.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // descripcion
		 reportVO.addReportFiltro("Descripción Procurador", this.getProcurador().getDescripcion());
		 // domicilio
		 reportVO.addReportFiltro("Domicilio Procurador", this.getProcurador().getDomicilio());
		 // tipo de procurador
		 String desTipoProcurador = "";
		 
		 TipoProcuradorVO tipoProcuradorVO = (TipoProcuradorVO) ModelUtil.getBussImageModelByIdForList(
				 this.getProcurador().getTipoProcurador().getId(), 
				 this.getListTipoProcurador());
		 if (tipoProcuradorVO != null){
			 desTipoProcurador = tipoProcuradorVO.getDesTipoProcurador();
		 }
		 reportVO.addReportFiltro("Tipo Procurador", desTipoProcurador);

	     // Order by
		 reportVO.setReportOrderBy("descripcion ASC");
	     
	     // PageModel de Procurador, contiene un PageModel de ProRec
		 ReportVO repProcurador = new ReportVO();
		 repProcurador.addReportDato("Procurador", "descripcion");
	     
	     ReportVO repProRec = new ReportVO();   // proRec de cada procurador
	     repProRec.setReportTitle("Recurso: FechaDesde: Fecha Hasta");
	     repProRec.setReportMetodo("listProRec");   // metodo a ejecutar para llenar el pmProRec
	     //pmProRec.addReportDato("Procurador", "procurador.descripcion");
	     repProRec.addReportDato("Recurso", "recurso.desRecurso");  
	     repProRec.addReportDato("Fecha Desde", "fechaDesde");
	     repProRec.addReportDato("Fecha Hasta", "fechaHasta");
	     
	     ReportTableVO rtComisiones = new ReportTableVO("Comisiones");
	     rtComisiones.setTitulo("Comisiones asociadas al Procurador y al Recurso");
	     rtComisiones.setReportMetodo("listProRecCom");  // metodo a ejecutar para llenar el pmComisiones
	     
	     rtComisiones.addReportColumn("Fec Vto Desde", "fecVtoDeuDes");
	     rtComisiones.addReportColumn("Fec Vto Hasta", "fecVtoDeuHas");
	     rtComisiones.addReportColumn("Porcentaje Comisión", "porcentajeComision");	     
	     rtComisiones.addReportColumn("Fecha desde", "fechaDesde");
	     rtComisiones.addReportColumn("Fecha hasta", "fechaHasta");	     
	     repProRec.getReportListTable().add(rtComisiones);

	     ReportTableVO rtManzanero = new ReportTableVO("Manzanero");  // manzaneros de un proRec
	     rtManzanero.setTitulo("Manzanero asociado al Procurador y al Recurso");
	     rtManzanero.setReportMetodo("listProRecDesHas");  // metodo a ejecutar para llenar la tabla de manzanero
	     rtManzanero.addReportColumn("Desde", "desde");
	     rtManzanero.addReportColumn("Hasta", "hasta");
	     rtManzanero.addReportColumn("Fecha Desde", "fechaDesde");
	     rtManzanero.addReportColumn("Fecha Hasta", "fechaHasta");
	     
	     repProRec.getReportListTable().add(rtManzanero); 
	     
	     repProcurador.getListReport().add(repProRec);
	     reportVO.getListReport().add(repProcurador);
	}

}
