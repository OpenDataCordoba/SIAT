//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatParam;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Obra
 * 
 * @author tecso
 */
public class ObraAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "obraAdapterVO";
	public static final String ENC_NAME = "encObraAdapterVO";
	public static final String DESASIGNAR_REPARTIDOR = "DESASIGNAR REPARTIDOR";	
	
    private ObraVO obra = new ObraVO();
    
    private List<TipoObraVO>    listTipoObra   = new ArrayList<TipoObraVO>();
    private List<SiNo>          listSiNo = new ArrayList<SiNo>();
    private List<EstadoObraVO>  listEstadoObra = new ArrayList<EstadoObraVO>(); 
	private List<RecursoVO>     listRecurso      = new ArrayList<RecursoVO>();    
    
	// Reporte: Informe de Obra
    private ReportVO infObrReport = new ReportVO();
	
    // Constructores
    public ObraAdapter(){
    	super(RecSecurityConstants.ABM_OBRA);
    	ACCION_MODIFICAR_ENCABEZADO = RecSecurityConstants.ABM_OBRA_ENC;
    }

    //  Getters y Setters
	public ObraVO getObra() {
		return obra;
	}

	public void setObra(ObraVO obraVO) {
		this.obra = obraVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public List<TipoObraVO> getListTipoObra() {
		return listTipoObra;
	}

	public void setListTipoObra(List<TipoObraVO> listTipoObra) {
		this.listTipoObra = listTipoObra;
	}

	public List<EstadoObraVO> getListEstadoObra() {
		return listEstadoObra;
	}

	public void setListEstadoObra(List<EstadoObraVO> listEstadoObra) {
		this.listEstadoObra = listEstadoObra;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public ReportVO getInfObrReport() {
		return infObrReport;
	}

	public void setInfObrReport(ReportVO infObrReport) {
		this.infObrReport = infObrReport;
	}

	// Getter para los permisos
	// ---> Para Obra Forma Pago
	public String getVerObraFormaPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.VER);
	}
	
	public String getModificarObraFormaPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarObraFormaPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarObraFormaPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.AGREGAR);
	}

	public String getActivarObraFormaPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.ACTIVAR);
	}

	public String getDesactivarObraFormaPagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_FORMAPAGO, BaseSecurityConstants.DESACTIVAR);
	}
	// ---> Para Obra Forma Pago

	// ---> Para Planilla Cuadra
	public String getVerPlanillaCuadraEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, BaseSecurityConstants.VER);
	}

	public String getModificarPlanillaCuadraEnabled() {
		// Permitimos modificar la planilla solo si es 
		// una obra de costo especial
	//	if (getObra().getEsCostoEsp().equals(SiNo.SI))
			return SiatBussImageModel.hasEnabledFlag
				(RecSecurityConstants.ABM_PLANILLACUADRA, BaseSecurityConstants.MODIFICAR);

	//	return SiatBussImageModel.DISABLED;
	}
	
	public String getEliminarPlanillaCuadraEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getCambiarEstadoPlanillaCuadraEnabled() {
		return SiatBussImageModel.hasEnabledFlag(RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, 
			RecSecurityConstants.MTD_OBRA_PLANILLACUADRA_CAMBIAR_ESTADO);
	}

	public String getAgregarPlanillaCuadraEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRA_PLANILLACUADRA, BaseSecurityConstants.AGREGAR);
	}
	// <--- Para Planilla Cuadra
	
	// ---> Para ObrRepVen
	public String getAgregarObrRepVenEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_OBRREPVEN, BaseSecurityConstants.AGREGAR);
	}
	// <--- Para ObrRepVen

	// ---> Para asignar repartidor a planillas
	public String getAsignarRepartidorEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getObra().getAsignarRepartidorBussEnabled()
			,RecSecurityConstants.ABM_OBRA, RecSecurityConstants.MTD_OBRA_ASIGNAR_REPARTIDOR);
	}

	// ---> Para asignar repartidor a planillas	

	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Reporte de Obra");
		report.setReportBeanName("Obra");
		report.setReportFileName(this.getClass().getName());
		 
		ReportVO reportDatosObra = new ReportVO();
		reportDatosObra.setReportTitle("Datos de la Obra");
		// carga de datos
	    //Recurso
		reportDatosObra.addReportDato("Recurso", "recurso.desRecurso");
	    //Numero
		reportDatosObra.addReportDato("N\u00FAmero", "numeroObra");
	    //Descripcion
		reportDatosObra.addReportDato("Descripci\u00F3n", "desObra");
		//Permite Cambio de Plan Mayor
		reportDatosObra.addReportDato("Permite Cambio de Plan Mayor", "permiteCamPlaMayView");
		//Es por Valuacion
		reportDatosObra.addReportDato("Es por Valuaci\u00F3n", "esPorValuacionView");
		//Es Obra de Costo Especifico
		reportDatosObra.addReportDato("Es Obra de Costo Espec\u00EDfico", "esCostoEspView");
		if (getObra().getEsCostoEsp().equals(SiNo.SI)) {
			//Es Obra de Costo Especifico
			reportDatosObra.addReportDato("Costo Espec\u00EDfico", "costoEspView");
		}
		//Estado
		reportDatosObra.addReportDato("Estado Obra", "estadoObra.desEstadoObra");
	     
		report.getListReport().add(reportDatosObra);
		 
		ReportVO reportLeyenda = new ReportVO();
		reportLeyenda.setReportTitle("Leyendas de la Obra");
		 
		// Plan Contado
		reportLeyenda.addReportDato("Plan Contado", "leyConReport");
		// Primera Cuota (Plan Largo):
		reportLeyenda.addReportDato("Primera Cuota (Plan Largo)", "leyPriCuoReport");
		//	Cuotas Restantes (Plan Largo): 	
		reportLeyenda.addReportDato("Cuotas Restantes (Plan Largo)", "leyResCuoReport");
		//	Cambio de Plan:
		reportLeyenda.addReportDato("Cambio de Plan", "leyCamPlaReport");
		 
		report.getListReport().add(reportLeyenda);
		 

		// Historico de Cambios de Estados
		ReportTableVO rtHisEstadoObra = new ReportTableVO("HisEstadoObra");
		rtHisEstadoObra.setTitulo("Hist\u00F3rico de Cambios de Estados");

		rtHisEstadoObra.setReportMetodo("listHisEstadoObra");
		
		// Fecha
		rtHisEstadoObra.addReportColumn("Fecha", "fechaEstado");
		// Descripcion
		rtHisEstadoObra.addReportColumn("Evento", "descripcion");
		// Estado
		rtHisEstadoObra.addReportColumn("Estado", "estadoObra.desEstadoObra");

		report.getReportListTable().add(rtHisEstadoObra);
		
		// Formas de Pago de Obra
		ReportTableVO rtObraFormaPago = new ReportTableVO("ObraFormaPago");
		rtObraFormaPago.setTitulo("Listado de Formas de Pago de la Obra");
		rtObraFormaPago.setReportMetodo("listObraFormaPago");
	     
		// carga de columnas
	    // Fecha Desde
	    rtObraFormaPago.addReportColumn("Fecha Desde", "fechaDesde");
	    // Fecha Hasta
	    rtObraFormaPago.addReportColumn("FechaHasta", "fechaHasta");
	    // Cantidad de Cuotas
	    rtObraFormaPago.addReportColumn("Cantidad de Cuotas", "cantCuotas");
	    // Monto Mínimo Cuota
	    rtObraFormaPago.addReportColumn("Monto M\u00EDnimo Cuota", "montoMinimoCuota");
	    // Descuento
	    rtObraFormaPago.addReportColumn("Descuento", "descuento");
	    // Interés Financiero
	    rtObraFormaPago.addReportColumn("Inter\u00E9s Financiero", "interesFinanciero");
	    // Es Especial
	    rtObraFormaPago.addReportColumn("Es Especial", "esEspecialView");
	         
	    report.getReportListTable().add(rtObraFormaPago);
		 
		// Planillas de la Obra
	    ReportTableVO rtPlanillaCuadra = new ReportTableVO("PlanillaCuadra");
	    rtPlanillaCuadra.setTitulo("Listado Planillas de la Obra");
	    rtPlanillaCuadra.setReportMetodo("listPlanillaCuadra");
	     
		// carga de columnas
	    // Número
	    rtPlanillaCuadra.addReportColumn("N\u00FAmero", "id");
	    // Nro. Cuadra
	    rtPlanillaCuadra.addReportColumn("Nro. Cuadra", "numeroCuadra");
	    // Tipo Obra
	    rtPlanillaCuadra.addReportColumn("Tipo Obra", "tipoObra.desTipoObra");
	    // Descripción
	    rtPlanillaCuadra.addReportColumn("Descripci\u00F3n", "descripcion");
	    // Repartidor
	    rtPlanillaCuadra.addReportColumn("Repartidor", "repartidor.desRepartidor");
	    // Estado
	    rtPlanillaCuadra.addReportColumn("Estado", "estPlaCua.desEstPlaCua");
	     
	    report.getReportListTable().add(rtPlanillaCuadra);
	}

	
	// Preparacion del Informe de Obra
	public void prepareInfObrReport(Long format) throws Exception {
		 
		ReportVO infObrReport = this.getInfObrReport(); 
		
		infObrReport.setReportFormat(1L); //  PDF	
		infObrReport.setReportFileSharePath(SiatParam.getString("FileSharePath"));
		infObrReport.setReportTitle("Reporte de Obra");
		infObrReport.setReportBeanName("Obra");
		infObrReport.setReportFileName("InfObraReport");
		infObrReport.setReportFileNamePdf("InformeDeObra.pdf");
		
		// Datos de la Obra 
		ReportVO datosObra = new ReportVO();
		datosObra.setReportTitle("Datos de la Obra");

	    //Recurso
		datosObra.addReportDato("Recurso", "recurso.desRecurso");
	    //Numero
		datosObra.addReportDato("N\u00FAmero", "numeroObra");
	    //Descripcion
		datosObra.addReportDato("Descripci\u00F3n", "desObra");
		//Permite Cambio de Plan Mayor
		datosObra.addReportDato("Permite Cambio de Plan Mayor", "permiteCamPlaMayView");
		//Es por Valuacion
		datosObra.addReportDato("Es por Valuaci\u00F3n", "esPorValuacionView");
		//Es Obra de Costo Especifico
		datosObra.addReportDato("Es de Costo Espec\u00EDfico", "esCostoEspView");
		if (getObra().getEsCostoEsp().equals(SiNo.SI)) {
			//Es Obra de Costo Especifico
			datosObra.addReportDato("Costo Espec\u00EDfico", "costoEspView");
		}
		//Estado
		datosObra.addReportDato("Estado Obra", "estadoObra.desEstadoObra");
	     
		infObrReport.getListReport().add(datosObra);
		 
		// Datos de las cuadras ejecutadas
		ReportVO cuadEjeObra = new ReportVO();
		cuadEjeObra.addReportDato("Tramos Ejecutados","cuaEjecForReporte");
		
		infObrReport.getListReport().add(cuadEjeObra);
		
		// Datos de las Planillas
	    ReportTableVO rtPlanillaCuadra = new ReportTableVO("PlanillaCuadra");
	    rtPlanillaCuadra.setTitulo("Listado Planillas de la Obra");
	    rtPlanillaCuadra.setReportMetodo("listPlanillaCuadra");
	     
	    // Número
	    rtPlanillaCuadra.addReportColumn("N\u00FAmero", "id");
	    // Nro. Cuadra
	    rtPlanillaCuadra.addReportColumn("Nro. Cuadra", "numeroCuadra");
	    // Tipo Obra
	    rtPlanillaCuadra.addReportColumn("Tipo Obra", "tipoObra.desTipoObra");
	    // Descripción
	    rtPlanillaCuadra.addReportColumn("Descripci\u00F3n", "descripcion");
	    // Repartidor
	    rtPlanillaCuadra.addReportColumn("Repartidor", "repartidor.desRepartidor");
	    // Estado
	    rtPlanillaCuadra.addReportColumn("Estado", "estPlaCua.desEstPlaCua");
	     // Cantidad de Cuentas
	    rtPlanillaCuadra.addReportColumn("Cuentas", "totalCuentasForReport");
	    // Monto Total
	    rtPlanillaCuadra.addReportColumn("Monto", "montoTotalForReport");
	    
	    infObrReport.getReportListTable().add(rtPlanillaCuadra);
	    
		// Totales
	    ReportTableVO totales = new ReportTableVO("Totales");
	    totales.setTitulo("Totales");

	    // Cantidad de Cuentas
	    totales.addReportColumn("Cant. de Cuentas", "totalCuentasForReport");
	    // Monto Total
	    totales.addReportColumn("Monto Total", "montoTotalForReport");

		infObrReport.getReportListTable().add(totales);

	}

}
