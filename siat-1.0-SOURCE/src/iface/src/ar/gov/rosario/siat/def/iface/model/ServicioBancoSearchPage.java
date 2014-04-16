//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Servicio Banco
 * @author tecso
 *
 */
public class ServicioBancoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "servicioBancoSearchPageVO";
	
	private ServicioBancoVO servicioBanco = new ServicioBancoVO(); 
	
	List<ServicioBancoVO> listServicioBanco = new ArrayList<ServicioBancoVO>(); 
	
	public ServicioBancoSearchPage(){
		super(DefSecurityConstants.ABM_SERVICIO_BANCO);
	}
	
	// Getters y Setters
	
	public ServicioBancoVO getServicioBanco(){
		return servicioBanco;
	}
	public void setServicioBanco(ServicioBancoVO servicioBanco){
		this.servicioBanco = servicioBanco;
	}
	public List<ServicioBancoVO> getListServicioBanco(){
		return listServicioBanco;
	}
	public void setListServicioBanco(List<ServicioBancoVO> listServicioBanco){
		this.listServicioBanco = listServicioBanco;
	}

	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportTitle("Reporte de Servicios de Banco");
		 report.setReportBeanName("ServiciosBanco");
		 report.setReportFormat(format);
		 // nombre del archivo a generar en base al class del SearchPage id usuario y formato pasado
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // codigo
		 report.addReportFiltro("Código", this.getServicioBanco().getCodServicioBanco());
		 // descripcion
		 report.addReportFiltro("Descripción", this.getServicioBanco().getDesServicioBanco());

	     // Order by
		 report.setReportOrderBy("codServicioBanco ASC");
	     
	     // PageModel de ServicioBanco
		 ReportVO pmSB = new ReportVO();
	     pmSB.addReportDato("Código Sevicio Banco", "codServicioBanco");
	     pmSB.addReportDato("Descripción Sevicio Banco", "desServicioBanco");
	     pmSB.addReportDato("Estado", "estadoView");
	     
	     ReportTableVO rtRecursos = new ReportTableVO("Recursos");
	     rtRecursos.setTitulo("Recursos asociados al Servicio Banco");
	     rtRecursos.setReportMetodo("listSerBanRec");  // metodo a ejecutar para llenar el rtRecursos
	     
	     rtRecursos.addReportColumn("Recurso", "recurso.desRecurso");
	     rtRecursos.addReportColumn("Fecha Desde", "fechaDesde");
	     rtRecursos.addReportColumn("Fecha Hasta", "fechaHasta");
	     pmSB.getReportListTable().add(rtRecursos);

	     ReportTableVO rtDescGen = new ReportTableVO("DescuentosGenerales"); 
	     rtDescGen.setTitulo("Descuentos Generales asociados al Servicio Banco");
	     rtDescGen.setReportMetodo("listSerBanDesGen");  // metodo a ejecutar para llenar la tabla
	     rtDescGen.addReportColumn("Descuento General", "desGen.desDesGen");
	     rtDescGen.addReportColumn("Porcentaje Descuento", "porDes");
	     rtDescGen.addReportColumn("Fecha Desde", "fechaDesde");
	     rtDescGen.addReportColumn("Fecha Hasta", "fechaHasta");
	     
	     pmSB.getReportListTable().add(rtDescGen); 
	     
	     report.getListReport().add(pmSB);
	}

}
