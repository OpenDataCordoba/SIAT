//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.pad.iface.model.RepartidorVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * SearchPage del PlanillaCuadra
 * 
 * @author Tecso
 *
 */
public class PlanillaCuadraSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "planillaCuadraSearchPageVO";
	public static final String ISMULTISELECT = "isMultiselect";
	public static final String LISTID_PLANILLACUADRA = "listIdPlanillaCuadra";
	public static final String ID_RECURSO = "idRecurso";	

	private PlanillaCuadraVO planillaCuadra= new PlanillaCuadraVO();
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ContratoVO> listContrato = new ArrayList<ContratoVO>();
	private List<TipoObraVO> listTipoObra = new ArrayList<TipoObraVO>();
	private List<EstPlaCuaVO> listEstPlaCua = new ArrayList<EstPlaCuaVO>();	
	
	// lista para elegir el repartido a asignar
	private List<RepartidorVO> listRepartidor = new ArrayList<RepartidorVO>();
	
	// es el repartidor seleccionado, si es -2
	// significa que hay que borrar el repartidor
	private RepartidorVO repartidor = new RepartidorVO();
	
	// id seleccionados, usados para seleccion multiple
	private String[] listId;
	// bandera usada para saber si se habilita el multiselect
	private Boolean isMultiselect = false;
	// planillas que seran deshabilitadas en la busqueda
	private List<Long> listIdPlanillaCuadra = new ArrayList<Long>();
	
	private Boolean seleccionRecursoBussEnabled = true;

	// Constructores
	public PlanillaCuadraSearchPage() {       
       super(RecSecurityConstants.ABM_PLANILLACUADRA);
    }

	// Getters y Setters
	public PlanillaCuadraVO getPlanillaCuadra() {
		return planillaCuadra;
	}
	public void setPlanillaCuadra(PlanillaCuadraVO planillaCuadra) {
		this.planillaCuadra = planillaCuadra;
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
	
	public String[] getListId() {
		return listId;
	}

	public void setListId(String[] listId) {
		this.listId = listId;
	}
	
	public Boolean getIsMultiselect() {
		return isMultiselect;
	}

	public void setIsMultiselect(Boolean isMultiselect) {
		this.isMultiselect = isMultiselect;
	}

	public List<Long> getListIdPlanillaCuadra() {
		return listIdPlanillaCuadra;
	}

	public void setListIdPlanillaCuadra(List<Long> listIdPlanillaCuadra) {
		this.listIdPlanillaCuadra = listIdPlanillaCuadra;
	}

	public void setMultiselect(boolean isMultiselect) {
		this.isMultiselect = isMultiselect;
	}

	public List<RepartidorVO> getListRepartidor() {
		return listRepartidor;
	}

	public void setListRepartidor(List<RepartidorVO> listRepartidor) {
		this.listRepartidor = listRepartidor;
	}

	public RepartidorVO getRepartidor() {
		return repartidor;
	}

	public void setRepartidor(RepartidorVO repartidor) {
		this.repartidor = repartidor;
	}
	
	public Boolean getSeleccionRecursoBussEnabled() {
		return seleccionRecursoBussEnabled;
	}

	public void setSeleccionRecursoBussEnabled(Boolean seleccionRecursoBussEnabled) {
		this.seleccionRecursoBussEnabled = seleccionRecursoBussEnabled;
	}

	public boolean hasItemsSelected() {
		boolean hasItemsSelected = true;
		
		if (listId == null || listId.length == 0) {
			hasItemsSelected = false;
		}
		
		return hasItemsSelected;
	}

	public String getInformarCatastralesEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_PLANILLACUADRA, RecSecurityConstants.MTD_PLANILLACUADRA_INFORMAR_CATASTRALES);
	}

	public String getCambiarEstadoEnabled() {
		return SiatBussImageModel.hasEnabledFlag
			(RecSecurityConstants.ABM_PLANILLACUADRA, RecSecurityConstants.MTD_PLANILLACUADRA_CAMBIAR_ESTADO);

	}

	public String getName(){
		return NAME;
	}
		
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); // no instanciar una nueva
		report.setReportFormat(format);	
		report.setReportTitle("Consulta de Planilla de Cuadras Ejecutadas");
		report.setReportBeanName("PlanillaCuadra");
		report.setReportFileName(this.getClass().getName());
		 
	    ReportTableVO rtPlaCua = new ReportTableVO("rtPlaCua");
	    rtPlaCua.setTitulo("Listado de Planilla de Cuadras Ejecutadas");
	    
	    // Recurso
	    String desRecurso = "";
			
	    RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(
			 this.getPlanillaCuadra().getRecurso().getId(), 
			 this.getListRecurso());
	    
	    if (recursoVO != null){
	    	desRecurso = recursoVO.getDesRecurso();
		}
	    
	    report.addReportFiltro("Recurso", desRecurso);
		 
	    // Contrato
	    String desContrato = "";
			
	    ContratoVO contratoVO = (ContratoVO) ModelUtil.getBussImageModelByIdForList(
			 this.getPlanillaCuadra().getContrato().getId(), 
			 this.getListContrato());
	   
	    if (contratoVO != null){
		 desContrato = contratoVO.getDescripcion();
	    }
		
	    report.addReportFiltro("Contrato", desContrato);
		 
	    // Tipo Obra
	    String desTipoObra = "";
			
		TipoObraVO tipoObraVO = (TipoObraVO) ModelUtil.getBussImageModelByIdForList(
			 this.getPlanillaCuadra().getTipoObra().getId(), 
			 this.getListTipoObra());
		if (tipoObraVO != null){
			desTipoObra = tipoObraVO.getDesTipoObra();
		}
		
		report.addReportFiltro("Tipo Obra", desTipoObra);
		 
		//Estado de Planilla de Cuadra
		String desPlanillaCuadra = "";
			
		EstPlaCuaVO estPlaCuaVO = (EstPlaCuaVO) ModelUtil.getBussImageModelByIdForList(
			 this.getPlanillaCuadra().getEstPlaCua().getId(), 
			 this.getListEstPlaCua());
		
		if (estPlaCuaVO != null){
			desPlanillaCuadra = estPlaCuaVO.getDesEstPlaCua();
		}
		 
		report.addReportFiltro("Estado", desPlanillaCuadra);
		report.addReportFiltro("N\u00FAmero", this.getPlanillaCuadra().getIdView());
		report.addReportFiltro("Descripci\u00F3n", this.getPlanillaCuadra().getDescripcion());
		report.addReportFiltro("Calle Principal", this.getPlanillaCuadra().getCallePpal().getNombreCalle());
			 
		// carga de columnas
		rtPlaCua.addReportColumn("N\u00FAmero", "id");
		rtPlaCua.addReportColumn("Tipo Obra", "tipoObra.desTipoObra");
		rtPlaCua.addReportColumn("Descripci\u00F3n", "descripcion");
		rtPlaCua.addReportColumn("Estado", "estPlaCua.desEstPlaCua");
	    
		report.getReportListTable().add(rtPlaCua);

	}

}
