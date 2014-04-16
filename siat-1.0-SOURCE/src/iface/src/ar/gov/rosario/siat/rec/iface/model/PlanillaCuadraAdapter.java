//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.CommonView;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Adapter del PlanillaCuadra
 * 
 * @author tecso
 */
public class PlanillaCuadraAdapter extends SiatAdapterModel {

	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "planillaCuadraAdapterVO";
	public static final String ENC_NAME = "encPlanillaCuadraAdapterVO";

	// codigos utilizados para identificar la calles a buscar
	public static final String CALLE_PPAL  = "1";
	public static final String CALLE_DESDE = "2";
	public static final String CALLE_HASTA = "3";	

    private PlanillaCuadraVO planillaCuadra = new PlanillaCuadraVO();
   
    private List<RecursoVO>  listRecurso      = new ArrayList<RecursoVO>();
	private List<ContratoVO> listContrato     = new ArrayList<ContratoVO>();
	private List<TipoObraVO> listTipoObra     = new ArrayList<TipoObraVO>();
	private List<EstPlaCuaVO> listEstPlaCua  = new ArrayList<EstPlaCuaVO>();

	// String que tiene la calle seleccionada para buscar, 
	// puede ser la principal la desde o la hasta
	private String calle;
	private String idCalleSeleccionada;	

    // Constructores
    public PlanillaCuadraAdapter(){
    	super(RecSecurityConstants.ABM_PLANILLACUADRA);
    	ACCION_MODIFICAR_ENCABEZADO = RecSecurityConstants.ABM_PLANILLACUADRA_ENC;
    }
    
    //  Getters y Setters
	public PlanillaCuadraVO getPlanillaCuadra() {
		return planillaCuadra;
	}

	public void setPlanillaCuadra(PlanillaCuadraVO planillaCuadraVO) {
		this.planillaCuadra = planillaCuadraVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ContratoVO> getListContrato() {
		return listContrato;
	}

	public void setListContrato(List<ContratoVO> listContrato) {
		this.listContrato = listContrato;
	}

	public List<TipoObraVO> getListTipoObra() {
		return listTipoObra;
	}

	public void setListTipoObra(List<TipoObraVO> listTipoObra) {
		this.listTipoObra = listTipoObra;
	}

	public List<EstPlaCuaVO> getListEstPlaCua() {
		return listEstPlaCua;
	}

	public void setListEstPlaCua(List<EstPlaCuaVO> listEstPlaCua) {
		this.listEstPlaCua = listEstPlaCua;
	}

	public String getCalle() {
		return calle;
	}

	public void setCalle(String calle) {
		this.calle = calle;
	}

	public String getIdCalleSeleccionada() {
		return idCalleSeleccionada;
	}

	public void setIdCalleSeleccionada(String idCalleSeleccionada) {
		this.idCalleSeleccionada = idCalleSeleccionada;
	}

	// Permisos para DetPlaCua
	public String getVerPlaCuaDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlaCuaDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlaCuaDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlaCuaDetEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_PLANILLACUADRA_DETALLE, BaseSecurityConstants.AGREGAR);
	}
	
	public String getModificarNumeroCuadraEnabledEnabled() {
		if (!ModelUtil.isNullOrEmpty(getPlanillaCuadra().getObra())) {
			return CommonView.ENABLED;
		}
		
		return CommonView.DISABLED;
	}
	public String getName(){
		return NAME;
	}

	public void prepareReport(Long format) {

		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);
		report.setReportTitle("Reporte de Planilla de Cuadra Ejecutada");
		report.setReportBeanName("planillaCuadra");
		report.setReportFileName(this.getClass().getName());

		ReportVO reportDatosPla = new ReportVO();
		reportDatosPla.setReportTitle("Datos de la Planilla");
		// carga de datos

		// Numero
		reportDatosPla.addReportDato("N\u00FAmero", "id");
		// Recurso
		reportDatosPla.addReportDato("Recurso", "recurso.desRecurso");
		// Contrato
		reportDatosPla.addReportDato("Contrato", "contrato.descripcion");
		// Tipo Obra
		reportDatosPla.addReportDato("Tipo Obra", "tipoObra.desTipoObra");
		// Fecha de Carga
		reportDatosPla.addReportDato("Fecha de Ejecuci\u00F3n", "fechaCarga");
		// Descripcion
		reportDatosPla.addReportDato("Descripci\u00F3n", "descripcion");
		// Costo por cuadra
		reportDatosPla.addReportDato("Costo", "costoCuadra");
		// Calle Principal
		reportDatosPla.addReportDato("Calle Principal", "callePpal.nombreCalle");
		// Calle Desde
		reportDatosPla.addReportDato("Calle Desde", "calleDesde.nombreCalle");
		// Calle Hasta
		reportDatosPla.addReportDato("Calle Hasta", "calleHasta.nombreCalle");
		// Observacion
		reportDatosPla.addReportDato("Observaci\u00F3n", "observacion");
		// Estado
		reportDatosPla.addReportDato("Estado", "estPlaCua.desEstPlaCua");

		report.getListReport().add(reportDatosPla);

		// Historico de Cambios de Estados
		ReportTableVO rtHisEstPlaCua = new ReportTableVO("HisEstPlaCua");
		rtHisEstPlaCua.setTitulo("Hist\u00F3rico de Cambios de Estados");

		rtHisEstPlaCua.setReportMetodo("listHisEstPlaCua");

		// Fecha
		rtHisEstPlaCua.addReportColumn("Fecha", "fechaEstado");
		// Descripcion
		rtHisEstPlaCua.addReportColumn("Evento", "descripcion");
		// Estado
		rtHisEstPlaCua.addReportColumn("Estado", "estPlaCua.desEstPlaCua");

		report.getReportListTable().add(rtHisEstPlaCua);

		// Detalles de la Planilla
		ReportTableVO rtPlaCuaDet = new ReportTableVO("PlaCuaDet");
		rtPlaCuaDet.setTitulo("Detalles de la Planilla");

		rtPlaCuaDet.setReportMetodo("listPlaCuaDet");

		rtPlaCuaDet.addReportColumn("Tipo", "tipPlaCuaDet.desTipPlaCuaDet", 16);
		rtPlaCuaDet.addReportColumn("Catastral","cuentaTGI.objImp.claveFuncional");
		rtPlaCuaDet.addReportColumn("Cuenta TGI", "cuentaTGI.numeroCuenta");
		rtPlaCuaDet.addReportColumn("Ubicaci\u00F3n", "ubicacionFinca",30 );
		rtPlaCuaDet.addReportColumn("Porc. PH", "porcPH", 10);
		rtPlaCuaDet.addReportColumn("Valuaci\u00F3n", "valuacionTerreno", 18);
		rtPlaCuaDet.addReportColumn("Estado", "estPlaCuaDet.desEstPlaCuaDet",16);

		report.getReportListTable().add(rtPlaCuaDet);
	}

}