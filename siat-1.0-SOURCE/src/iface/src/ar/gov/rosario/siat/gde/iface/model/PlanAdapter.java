//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.SistemaVO;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.AtributoVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del Plan
 * 
 * @author tecso
 */
public class PlanAdapter extends SiatAdapterModel{
	
	public static final String NAME = "planAdapterVO";
	public static final String ENC_NAME = "encPlanAdapterVO";
	
    private PlanVO plan = new PlanVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();
	private List<TipoDeudaPlanVO> listTipoDeudaPlan = new ArrayList<TipoDeudaPlanVO>();
	private List<SistemaVO> listSistema = new ArrayList<SistemaVO>();
	private List<AtributoVO> listAtributo = new ArrayList<AtributoVO>();
	private List<RecursoVO> listPlanRecurso = new ArrayList<RecursoVO>();

	
	
    // Constructores
    public PlanAdapter(){
    	super(GdeSecurityConstants.ABM_PLAN);
    	ACCION_MODIFICAR_ENCABEZADO = GdeSecurityConstants.ABM_PLAN_ENC;
    }
    
    //  Getters y Setters
	public PlanVO getPlan() {
		return plan;
	}

	public void setPlan(PlanVO planVO) {
		this.plan = planVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoDeudaPlanVO> getListTipoDeudaPlan() {
		return listTipoDeudaPlan;
	}

	public void setListTipoDeudaPlan(List<TipoDeudaPlanVO> listTipoDeudaPlan) {
		this.listTipoDeudaPlan = listTipoDeudaPlan;
	}

	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public List<SistemaVO> getListSistema() {
		return listSistema;
	}

	public void setListSistema(List<SistemaVO> listSistema) {
		this.listSistema = listSistema;
	}

	
	public List<AtributoVO> getListAtributo() {
		return listAtributo;
	}

	public void setListAtributo(List<AtributoVO> listAtributo) {
		this.listAtributo = listAtributo;
	}
	
	
	public List<RecursoVO> getListPlanRecurso() {
		return listPlanRecurso;
	}

	public void setListPlanRecurso(List<RecursoVO> listPlanRecurso) {
		this.listPlanRecurso = listPlanRecurso;
	}

	// Permisos para PlanClaDeu
	public String getVerPlanClaDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanClaDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanClaDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanClaDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANCLADEU, BaseSecurityConstants.AGREGAR);
	}	
	
	// Permisos para PlanMotCad
	public String getVerPlanMotCadEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanMotCadEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanMotCadEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanMotCadEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANMOTCAD, BaseSecurityConstants.AGREGAR);
	}
	
	
	// Permisos para PlanForActDeu
	public String getVerPlanForActDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanForActDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanForActDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanForActDeuEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANFORACTDEU, BaseSecurityConstants.AGREGAR);
	}


	// Permisos para PlanDescuento
	public String getVerPlanDescuentoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanDescuentoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanDescuentoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanDescuentoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANDESCUENTO, BaseSecurityConstants.AGREGAR);
	}
	
	
	// Permisos para PlanIntFin
	public String getVerPlanIntFinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanIntFinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanIntFinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanIntFinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANINTFIN, BaseSecurityConstants.AGREGAR);
	}

	// Permisos para PlanVen
	public String getVerPlanVenEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanVenEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanVenEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanVenEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANVEN, BaseSecurityConstants.AGREGAR);
	}
	
	// Permisos para PlanAtrVal
	public String getVerPlanAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanAtrValEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANATRVAL, BaseSecurityConstants.AGREGAR);
	}
	
	// Permisos para PlanExe
	public String getVerPlanExeEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanExeEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanExeEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanExeEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANEXE, BaseSecurityConstants.AGREGAR);
	}
	
	// Permisos para PlanProrroga
	public String getVerPlanProrrogaEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanProrrogaEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanProrrogaEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanProrrogaEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ADM_PLANPRORROGA, BaseSecurityConstants.AGREGAR);
	}

	
	// Permisos para PlanImpMin
	public String getVerPlanImpMinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanImpMinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanImpMinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanImpMinEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANIMPMIN, BaseSecurityConstants.AGREGAR);
	}
	
	// Permisos para PlanRecurso
	public String getVerPlanRecursoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.VER);
	}
	
	public String getModificarPlanRecursoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.MODIFICAR);
	}

	public String getEliminarPlanRecursoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.ELIMINAR);
	}

	public String getAgregarPlanRecursoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(GdeSecurityConstants.ABM_PLANRECURSO, BaseSecurityConstants.AGREGAR);
	}
	
	public String getName(){
		return NAME;
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		 report.setReportFormat(format);	
		 report.setReportTitle("Reporte de Plan");
		 report.setReportBeanName("plan");
		 report.setReportFileName(this.getClass().getName());
	
		
		 // Planillas del Plan
		 ReportVO rtPlanPago = new ReportVO();
		 rtPlanPago.setReportTitle("Datos del Plan de Pago");
	    
	     // carga de columnas
	     // Descripción
		 rtPlanPago.addReportDato("Descripción", "desPlan");
		 //Ordenanza
		 rtPlanPago.addReportDato("Ordenanza", "ordenanza");
	     //Via Deuda
		 rtPlanPago.addReportDato("Via de la Deuda", "viaDeuda.desViaDeuda");
		 //Sistema
		 rtPlanPago.addReportDato("Sistema","sistema.desSistema");
		 //Fecha Alta
		 rtPlanPago.addReportDato("Fecha Alta","fechaAlta");
		 //Es Manual
		 rtPlanPago.addReportDato("Es Manual","esManual",SiNo.class);
	     // Fec.Venc.Desde
		 rtPlanPago.addReportDato("Fecha Vto. Deuda Desde","fecVenDeuDes");
	     // Fec.Venc.Hasta
		 rtPlanPago.addReportDato("Fecha Vto. Deuda Hasta","fecVenDeuHas");
		 //Aplica Total Impago
		 rtPlanPago.addReportDato("Aplica Total Impago","aplicaTotalImpago", SiNo.class);
		//Cant. Máx. Cuota
		 rtPlanPago.addReportDato("Cant. Máx. Cuota","canMaxCuo");
		 //Cant. Mín. Periodos Adeudados
		 rtPlanPago.addReportDato("Cant. Mín. Periodos Adeudados","canMinPer");
		 //Cant. Mín. Cuota p/cuota saldo
		 rtPlanPago.addReportDato("Cant. Mín. Cuota p/cuota saldo","canCuoAImpEnForm");
	     //Cant. Cuotas a Impr. en Formalización
		 rtPlanPago.addReportDato("Cant. Cuotas a Impr. en Formalización","cuoDesParaRec"); 
		 //Cuota Desde para Reconfección
		 rtPlanPago.addReportDato("Cuota Desde para Reconfección","cuoDesParaRec"); 
		//Posee Actualización Especial
		 rtPlanPago.addReportDato("Posee Actualización Especial","poseeActEsp",SiNo.class);
	     // Tipo Deuda Incorporada
		 rtPlanPago.addReportDato("Tipo Deuda Incorporada", "tipoDeudaPlan.desTipoDeudaPlan");
		 //Aplicar Pago a Cuenta
		 rtPlanPago.addReportDato("Aplicar Pago a Cuenta", "aplicaPagCue", SiNo.class);
		// Nombre Secuencia Para Generar Nro. Convenio
		 rtPlanPago.addReportDato("Nombre Secuencia Para Generar Nro. Convenio", "nameSequence");
		// Formulario de Formalización
		 rtPlanPago.addReportDato("Formulario de Formalización", "formulario.codFormulario");
		 //Leyenda del Formulario
		 rtPlanPago.addReportDato("Leyenda del Formulario", "leyendaForm");
	     //Caso
		 rtPlanPago.addReportDato("Caso", "casoView");
		 //Leyenda Plan
		 rtPlanPago.addReportDato("Leyenda Plan", "leyendaPlan");
		//Link Normativa
		 rtPlanPago.addReportDato("Link Normativa", "linkNormativa");
		// Fecha Baja
		 rtPlanPago.addReportDato("Fecha Baja", "fechaBaja");
		// Fecha Baja
		 rtPlanPago.addReportDato("Fecha Baja", "fechaBaja");
		 // Estado
		 rtPlanPago.addReportDato("Estado", "estadoView");
		
		 
	     report.getListReport().add(rtPlanPago);
	     
	 	     //Recursos por plan
	     	 ReportTableVO rtPlanRecurso = new ReportTableVO("planRecurso");
	     	 rtPlanRecurso.setTitulo("Listado de Recursos del plan");
	     	 rtPlanRecurso.setReportMetodo("listPlanRecurso");
	     	 
	     	// carga de columnas
		     // Fecha Desde
	     	rtPlanRecurso.addReportColumn("Fecha Desde", "fechaDesde");
	     	 // Fecha Hasta
	     	rtPlanRecurso.addReportColumn("Fecha Hasta", "fechaHasta");
	     	 // Recurso
	     	rtPlanRecurso.addReportColumn("Recurso", "recurso.desRecurso");
	     	 
	 	     report.getReportListTable().add(rtPlanRecurso);
	 	     

	 	     //Clasificación de Deuda
	     	 ReportTableVO rtPlanClaDeu = new ReportTableVO("planClaDeu");
	     	 rtPlanClaDeu.setTitulo("Clasificación de Deuda");
	         rtPlanClaDeu.setReportMetodo("listPlanClaDeu");
	     	 
	     	// carga de columnas
		     //Fecha Desde
	         rtPlanClaDeu.addReportColumn("Fecha Desde", "fechaDesde");
	     	 //Fecha Hasta
	         rtPlanClaDeu.addReportColumn("Fecha Hasta", "fechaHasta");
	     	 //Descripción
	         rtPlanClaDeu.addReportColumn("Descripción", "recClaDeu.desClaDeu");
	     	 
	 	     report.getReportListTable().add(rtPlanClaDeu);
	 	     
	 	    //Motivos de Caducidad
	     	 ReportTableVO rtPlanMotCad = new ReportTableVO("planMotCad");
	     	rtPlanMotCad.setTitulo("Clasificación de Deuda");
	     	rtPlanMotCad.setReportMetodo("listPlanMotCad");
	     	 
	     	// carga de columnas
		     //Fecha Desde
	     	rtPlanMotCad.addReportColumn("Fecha Desde", "fechaDesde");
	     	 //Fecha Hasta
	     	rtPlanMotCad.addReportColumn("Fecha Hasta", "fechaHasta");
	     	 //Descripción
	     	rtPlanMotCad.addReportColumn("Descripción", "desPlanMotCad");
	         //Es Especial
	     	rtPlanMotCad.addReportColumn("Es Especial", "esEspecial", SiNo.class);
	     	 
	 	     report.getReportListTable().add(rtPlanMotCad);
	 	     
	 	    //Motivos de Caducidad
	     	 ReportTableVO rtPlanForActDeu = new ReportTableVO("planForActDeu");
	     	rtPlanForActDeu.setTitulo("Formas de Actualización");
	     	rtPlanForActDeu.setReportMetodo("listPlanForActDeu");
	     	 
	     	// carga de columnas
		     //Fecha Desde
	     	rtPlanForActDeu.addReportColumn("Fecha Desde", "fechaDesde");
	     	 //Fecha Hasta
	     	rtPlanForActDeu.addReportColumn("Fecha Hasta", "fechaHasta");
	     	 //Descripción
	     	rtPlanForActDeu.addReportColumn("Descripción", "fecVenDeuDes");
	         //Es Común
	     	rtPlanForActDeu.addReportColumn("Es Común", "esComun");
	     	 //Porcentaje
	     	rtPlanForActDeu.addReportColumn("Porcentaje", "porcentaje");
	     	
	     	report.getReportListTable().add(rtPlanForActDeu);
	     	
	     	  //Importes Mínimos
	     	 ReportTableVO rtPlanImpMin = new ReportTableVO("planImpMin");
	     	rtPlanImpMin.setTitulo("Importes Mínimos");
	     	rtPlanImpMin.setReportMetodo("listPlanImpMin");
	     	 
	     	// carga de columnas
		     //Fecha Desde
	     	rtPlanImpMin.addReportColumn("Fecha Desde", "fechaDesde");
	     	 //Fecha Hasta
	     	rtPlanImpMin.addReportColumn("Fecha Hasta", "fechaHasta");
	     	 //Descripción
	     	rtPlanImpMin.addReportColumn("Cantidad de Cuotas", "cantidadCuotas");
	         //Importe Mínimo Deuda
	     	rtPlanImpMin.addReportColumn("Importe Mínimo Deuda", "impMinDeu");
	     	 //Importe Mínimo Cuota
	     	rtPlanImpMin.addReportColumn("Importe Mínimo Cuota", "impMinCuo");
	     	
	     	report.getReportListTable().add(rtPlanImpMin);
	     	
	    	  //Descuentos
	     	 ReportTableVO rtPlanDescuento = new ReportTableVO("PlanDescuento");
	     	rtPlanDescuento.setTitulo("Descuentos");
	     	rtPlanDescuento.setReportMetodo("listPlanDescuento");
	     	 
	     	// carga de columnas
		     //Fecha Desde
	     	rtPlanDescuento.addReportColumn("Fecha Desde", "fechaDesde");
	     	 //Fecha Hasta
	     	rtPlanDescuento.addReportColumn("Fecha Hasta", "fechaHasta");
	     	 //Cantidad de Cuotas
	     	rtPlanDescuento.addReportColumn("Cantidad de Cuotas", "cantidadCuotasPlan");
	         //% Descuento Capital
	     	rtPlanDescuento.addReportColumn("% Descuento Capital", "porDesCap");
	     	 //% Descuento Actualización
	     	rtPlanDescuento.addReportColumn("% Descuento Actualización", "porDesAct");
	     	 //% Descuento Actualización
	     	rtPlanDescuento.addReportColumn("% Descuento Actualización", "porDesAct");
	     	 //% Descuento Actualización
	     	rtPlanDescuento.addReportColumn("% Descuento Actualización", "porDesAct");
	     	
	     	
	     	report.getReportListTable().add(rtPlanDescuento);
	     	
	     	//Interés Financiero
	     	
	     	 ReportTableVO rtPlanIntFin = new ReportTableVO("PlanIntFin");
	     	 	rtPlanIntFin.setTitulo("Interés Financiero");
	     		rtPlanIntFin.setReportMetodo("listPlanIntFin");
		     	 
		     	// carga de columnas
			     //Fecha Desde
	     		rtPlanIntFin.addReportColumn("Fecha Desde", "fechaDesde");
		     	 //Fecha Hasta
	     		rtPlanIntFin.addReportColumn("Fecha Hasta", "fechaHasta");
		     	 //Cuota Hasta
	     		rtPlanIntFin.addReportColumn("Cuota Hasta", "cuotaHasta");
		         //Interes
	     		rtPlanIntFin.addReportColumn("Interes", "interes");
		     	
		     	
		     	report.getReportListTable().add(rtPlanIntFin);
		     	
		     	//Vencimientos
		     	ReportTableVO rtPlanVen = new ReportTableVO("PlanVen");
		     	 rtPlanVen.setTitulo("Vencimientos");
		         rtPlanVen.setReportMetodo("listPlanVen");
			     	 
			     	// carga de columnas
				     //Fecha Desde
		         rtPlanVen.addReportColumn("Fecha Desde", "fechaDesde");
			     	 //Fecha Hasta
		         rtPlanVen.addReportColumn("Fecha Hasta", "fechaHasta");
			     	 //Cuota Hasta
		         rtPlanVen.addReportColumn("Cuota Hasta", "cuotaHasta");
			         //Vencimiento
		         rtPlanVen.addReportColumn("Vencimiento", "vencimiento.desVencimiento");
			     	
			     report.getReportListTable().add(rtPlanVen);
			     
			 	 //Atributos Valorizados del Plan
				ReportTableVO rtPlanAtrVal = new ReportTableVO("PlanAtrVal");
				rtPlanAtrVal.setTitulo("Atributos Valorizados del Plan");
				rtPlanAtrVal.setReportMetodo("listPlanAtrVal");
						     	 
				// carga de columnas
				//Fecha Desde
				rtPlanAtrVal.addReportColumn("Fecha Desde", "fechaDesde");
				//Fecha Hasta
				rtPlanAtrVal.addReportColumn("Fecha Hasta", "fechaHasta");
			
				report.getReportListTable().add(rtPlanAtrVal);
				
				 //Exenciones
				ReportTableVO rtPlanExe = new ReportTableVO("PlanExe");
				rtPlanExe.setTitulo("Exenciones");
				rtPlanExe.setReportMetodo("listPlanExe");
						     	 
				// carga de columnas
				//Fecha Desde
				rtPlanExe.addReportColumn("Fecha Desde", "fechaDesde");
				//Fecha Hasta
				rtPlanExe.addReportColumn("Fecha Hasta", "fechaHasta");
				//Exención Caso Social/Otro
				rtPlanExe.addReportColumn("Exención Caso Social/Otro", "exencion.desExencion");
			
				report.getReportListTable().add(rtPlanExe);
	 	}
	
}
