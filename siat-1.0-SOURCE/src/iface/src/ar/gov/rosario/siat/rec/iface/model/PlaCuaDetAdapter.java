//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del PlaCuaDet
 * 
 * @author tecso
 */
public class PlaCuaDetAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "plaCuaDetAdapterVO";
	
    private PlaCuaDetVO plaCuaDet = new PlaCuaDetVO();

    private List<UsoCdMVO> listUsoCdM	= new ArrayList<UsoCdMVO>();

    // Constructores
    public PlaCuaDetAdapter(){
    	super(RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE);
    }
    
    //  Getters y Setters
	public PlaCuaDetVO getPlaCuaDet() {
		return plaCuaDet;
	}

	public void setPlaCuaDet(PlaCuaDetVO plaCuaDetVO) {
		this.plaCuaDet = plaCuaDetVO;
	}

	public List<UsoCdMVO> getListUsoCdM() {
		return listUsoCdM;
	}

	public void setListUsoCdM(List<UsoCdMVO> listUsoCdm) {
		this.listUsoCdM = listUsoCdm;
	}
	
	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);
		report.setReportTitle("Reporte de Detalle de Planilla de Cuadra");
		report.setReportBeanName("plaCuaDet");
		report.setReportFileName(this.getClass().getName());

		ReportVO reportDatosPla = new ReportVO();
		reportDatosPla.setReportTitle("Datos de la Planilla");
		// carga de datos

		// Numero
		reportDatosPla.addReportDato("N\u00FAmero", "planillaCuadra.id");
		// Recurso
		reportDatosPla.addReportDato("Recurso", "planillaCuadra.recurso.desRecurso");
		// Contrato
		reportDatosPla.addReportDato("Contrato", "planillaCuadra.contrato.descripcion");
		// Tipo Obra
		reportDatosPla.addReportDato("Tipo Obra", "planillaCuadra.tipoObra.desTipoObra");
		// Fecha de Carga
		reportDatosPla.addReportDato("Fecha de Ejecuci\u00F3n", "planillaCuadra.fechaCarga");
		// Descripcion
		reportDatosPla.addReportDato("Descripci\u00F3n", "planillaCuadra.descripcion");
		// Costo por cuadra
		reportDatosPla.addReportDato("Costo", "planillaCuadra.costoCuadra");
		// Calle Principal
		reportDatosPla.addReportDato("Calle Principal", "planillaCuadra.codCallePpal");
		// Calle Desde
		reportDatosPla.addReportDato("Calle Desde", "planillaCuadra.codCalleDesde");
		// Calle Hasta
		reportDatosPla.addReportDato("Calle Hasta", "planillaCuadra.codCalleHasta");
		// Observacion
		reportDatosPla.addReportDato("Observaci\u00F3n", "planillaCuadra.observacion");
		// Estado
		reportDatosPla.addReportDato("Estado", "planillaCuadra.estPlaCua.desEstPlaCua");

		report.getListReport().add(reportDatosPla);
	
		// Datos del detalle de la Planilla
		ReportVO reportDatosPlaCuaDet = new ReportVO();
		reportDatosPlaCuaDet.setReportTitle("Datos del Detalle de la Planilla");
	
		if (!this.getPlaCuaDet().getIsCarpeta()) {
			// Catastral
			reportDatosPlaCuaDet.addReportDato("Catastral", "cuentaTGI.objImp.claveFuncional");
			// Cuenta TGI
			reportDatosPlaCuaDet.addReportDato("Cuenta TGI", "cuentaTGI.numeroCuenta");
			// Valuacion
			reportDatosPlaCuaDet.addReportDato("Valuaci\u00F3n", "valuacionTerreno");
			// Uso Catastro
			reportDatosPlaCuaDet.addReportDato("Uso Catastro", "usoCatastro");
			// Uso CdM
			reportDatosPlaCuaDet.addReportDato("Uso CdM", "usoCdM.desUsoCdM");
		}
	
		if (this.getPlaCuaDet().getIsCarpeta())
			// Agrupador
			reportDatosPlaCuaDet.addReportDato("Carpeta", "agrupador");
	
		// Mts. Lineales de Frente
		reportDatosPlaCuaDet.addReportDato("Mts. Lineales de Frente", "cantidadMetros");
		// U.T.
		reportDatosPlaCuaDet.addReportDato("U.T.", "cantidadUnidades");

		report.getListReport().add(reportDatosPlaCuaDet);
	
		// Si es una carpeta, mostramos las unidades funcionales
		if (this.getPlaCuaDet().getIsCarpeta()) {
			ReportTableVO reportUniFun = new ReportTableVO("PlaCuaDet");
			reportUniFun.setTitulo("Unidades Funcionales");
			reportUniFun.setReportMetodo("listPlaCuaDetHijos");
			
			// Catastral
			reportUniFun.addReportColumn("Catastral", "cuentaTGI.objImp.claveFuncional",30);
			// Cuenta TGI
			reportUniFun.addReportColumn("Cuenta TGI", "cuentaTGI.numeroCuenta");
			// Porc. PH
			reportUniFun.addReportColumn("Porc. PH", "porcPH");
			// Mts. Lineales de Frente
			reportUniFun.addReportColumn("Mts. L. F.", "cantidadMetros");
			// U.T.
			reportUniFun.addReportColumn("U.T.", "cantidadUnidades");
			// Valuacion
			reportUniFun.addReportColumn("Valuaci\u00F3n", "valuacionTerreno");
			// Uso CdM
			reportUniFun.addReportColumn("Uso CdM", "usoCdM.desUsoCdM");

			report.getReportListTable().add(reportUniFun);
		}
		
	}
}