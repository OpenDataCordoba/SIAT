//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.ReportTableVO;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Search Page de Compensacion
 * @author tecso
 *
 */
public class CompensacionSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	private CompensacionVO compensacion = new CompensacionVO();
	
	public static final String NAME = "compensacionSearchPageVO";

	private RecursoVO recurso = new RecursoVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<EstadoComVO> listEstadoCom = new ArrayList<EstadoComVO>();
	
	private Boolean enviarBussEnabled   = true;
	private Boolean devolverBussEnabled   = true;
	
	private BalanceVO balance = new BalanceVO();    
	private String[] listIdCompensacionSelected;
	private Boolean paramSeleccionPorLista  = false;
	
	public CompensacionSearchPage(){
		super(BalSecurityConstants.ABM_COMPENSACION);
	}

	// Getters Y Setters
	public CompensacionVO getCompensacion() {
		return compensacion;
	}
	public void setCompensacion(CompensacionVO compensacion) {
		this.compensacion = compensacion;
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<EstadoComVO> getListEstadoCom() {
		return listEstadoCom;
	}
	public void setListEstadoCom(List<EstadoComVO> listEstadoCom) {
		this.listEstadoCom = listEstadoCom;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public String[] getListIdCompensacionSelected() {
		return listIdCompensacionSelected;
	}
	public void setListIdCompensacionSelected(String[] listIdCompensacionSelected) {
		this.listIdCompensacionSelected = listIdCompensacionSelected;
	}
	public BalanceVO getBalance() {
		return balance;
	}
	public void setBalance(BalanceVO balance) {
		this.balance = balance;
	}
	public Boolean getParamSeleccionPorLista() {
		return paramSeleccionPorLista;
	}
	public void setParamSeleccionPorLista(Boolean paramSeleccionPorLista) {
		this.paramSeleccionPorLista = paramSeleccionPorLista;
	}

	public String getName(){
		return NAME;
	}
	
	//	 Flags Seguridad
	public Boolean getEnviarBussEnabled() {
		return enviarBussEnabled;
	}

	public void setEnviarBussEnabled(Boolean enviarBussEnabled) {
		this.enviarBussEnabled = enviarBussEnabled;
	}
	
	public String getEnviarEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEnviarBussEnabled(), 
				BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.ENVIAR);
	}
	public Boolean getDevolverBussEnabled() {
		return devolverBussEnabled;
	}

	public void setDevolverBussEnabled(Boolean devolverBussEnabled) {
		this.devolverBussEnabled = devolverBussEnabled;
	}
	
	public String getDevolverEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getDevolverBussEnabled(), 
				BalSecurityConstants.ABM_COMPENSACION, BaseSecurityConstants.DEVOLVER);
	}
	
	public void prepareReport(Long format) {
		 
		ReportVO report = this.getReport(); 
		report.setReportTitle("Reporte de Compensaciones");
		report.setReportBeanName("Compensacion");
		report.setReportFormat(format);
	     // nombre del archivo 
		report.setReportFileName(this.getClass().getName());
		 
		 // carga de filtros:
		 // Recurso
		 String desRecurso = "No seleccionado";
		 RecursoVO recursoVO = (RecursoVO) ModelUtil.getBussImageModelByIdForList(this.getRecurso().getId(),this.getListRecurso());
		 if (recursoVO != null){
			 desRecurso = recursoVO.getDesRecurso();
		 }
		 report.addReportFiltro("Recurso", desRecurso);
		 // Numero
		 report.addReportFiltro("Número de Cuenta", this.getCompensacion().getCuenta().getNumeroCuenta());			 
		 
		 // Estado
		 String desEstado = "No seleccionado";
		 EstadoComVO estadoComVO = (EstadoComVO) ModelUtil.getBussImageModelByIdForList(
				 this.getCompensacion().getEstadoCom().getId(), 
				 this.getListEstadoCom());
		 if (estadoComVO != null){
			 desEstado = estadoComVO.getDescripcion();
		 }	
		 report.addReportFiltro("Estado", desEstado);
		 
		 // Order by
		 report.setReportOrderBy("cuenta.id, fechaAlta DESC, id DESC");
		 
		 ReportTableVO rtCompensacion = new ReportTableVO("Compensacion");
		 rtCompensacion.setTitulo("Listado de Compensaciones");

		 rtCompensacion.addReportColumn("Fecha de Alta", "fechaAlta");
		 rtCompensacion.addReportColumn("Descripción", "descripcion");
		 rtCompensacion.addReportColumn("Estado", "estadoCom.descripcion");

		 report.getReportListTable().add(rtCompensacion);
	}
	
}
