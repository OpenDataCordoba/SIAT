//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

public class TipObjImpSearchPage extends SiatPageModel {
	
	private static final long serialVersionUID = 1L;		
	public static final String NAME = "tipObjImpSearchPageVO";
		
	private TipObjImpVO tipObjImp = new TipObjImpVO();
	
	private List<SiNo>  listSiNo = new ArrayList<SiNo>();
	
	
	private Boolean admAreaOrigenBussEnabled      = true;
	
	public TipObjImpSearchPage() {
		super(DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE);
		this.tipObjImp.setEsSiat(SiNo.OpcionTodo);
    }

	// Getters y Setters	
	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}
	public TipObjImpVO getTipObjImp() {
		return tipObjImp;
	}

	public void setTipObjImp(TipObjImpVO tipObjImp) {
		this.tipObjImp = tipObjImp;
	}

	
	// Flags Seguridad


	public Boolean getAdmAreaOrigenBussEnabled() {
		return admAreaOrigenBussEnabled;
	}

	public void setAdmAreaOrigenBussEnabled(Boolean admAreaOrigenBussEnabled) {
		this.admAreaOrigenBussEnabled = admAreaOrigenBussEnabled;
	}
	
	public String getAdmAreaOrigenEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmAreaOrigenBussEnabled(), 
				DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE,	DefSecurityConstants.ABM_TIPO_OBJETO_IMPONIBLE_ADM_AREA_ORIGEN);
	}
	
	// Getters y Setters para struts 

	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 // seteo apaisado
		 report.setPageHeight(ReportVO.PAGE_WIDTH);
		 report.setPageWidth(ReportVO.PAGE_HEIGHT);
		 
		 report.setReportTitle("Reporte de Tipos de Objetos Imponibles");
		 report.setReportBeanName("TiposObjetosImponibles");
		 report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros
		 report.addReportFiltro("Código", this.getTipObjImp().getCodTipObjImp());
		 report.addReportFiltro("Descripción", this.getTipObjImp().getDesTipObjImp());
		 SiNo esSiat = SiNo.getById(this.getTipObjImp().getEsSiat().getId());
		 report.addReportFiltro("Es SIAT", esSiat.getValue());
		 
		 // Order by
		 report.setReportOrderBy("codtipobjimp ASC");
		 
	     // PageModel de TipObjImp
		 ReportVO pmTOI = new ReportVO();
	     // carga de datos por cada tipObjImp
	     pmTOI.addReportDato("Código Tipo Objeto Imponible", "codTipObjImp");
	     pmTOI.addReportDato("Descripción  Tipo Objeto Imponible", "desTipObjImp");
	     pmTOI.addReportDato("Es SIAT", "esSiatView");
	     pmTOI.addReportDato("Fecha Alta", "fechaAlta");
	     pmTOI.addReportDato("Fecha Baja", "fechaBaja");
	     pmTOI.addReportDato("Estado", "estadoView");

	     ReportTableVO rtTipObjImpAtr = new ReportTableVO("TipObjImpAtr");
	     rtTipObjImpAtr.setTitulo("Listado de Atributos del Tipo de Objeto Imponible");
	     rtTipObjImpAtr.setReportMetodo("listTipObjImpAtr");  // metodo a ejecutar para llenar la tabla de TipObjImpAtr
		 // carga de columnas
	     rtTipObjImpAtr.addReportColumn("Código", "atributo.codAtributo");
	     rtTipObjImpAtr.addReportColumn("Descripción", "atributo.desAtributo");
	     rtTipObjImpAtr.addReportColumn("Fecha Alta", "fechaDesde");
	     rtTipObjImpAtr.addReportColumn("Fecha Baja", "fechaHasta");
	     
	     // columnas con subseldas
	     String[] metodosGenerales = {"esMultivalorReport", "poseeVigenciaReport", "esClaveReport", 
	    		 "esClaveFuncionalReport", "esDomicilioEnvioReport", "esUbicacionReport", "valorDefectoReport"};
	     rtTipObjImpAtr.addReportColumn("Caract. Generales", metodosGenerales);
	     
	     String[] metodosVisBus = {"esVisConDeuReport", "esAtributoBusReport", 
	    		 "esAtriBusMasivaReport", "admBusPorRanReport" };
	     rtTipObjImpAtr.addReportColumn("Caract. Visualización y Búsqueda", metodosVisBus);

	     String[] metodosActObjImp = {"esAtributoSIATReport", "posColIntReport", "posColIntHasReport" };
	     rtTipObjImpAtr.addReportColumn("Caract. Actualización Obj. Imp.", metodosActObjImp);
	     pmTOI.getReportListTable().add(rtTipObjImpAtr);

	     report.getListReport().add(pmTOI);
	}
	
}
