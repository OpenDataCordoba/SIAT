//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.TipoReporte;

/**
 * Report del Deuda Anulada
 * 
 * @author Tecso
 *
 */
public class DeudaAnuladaReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "deudaAnuladaReportVO";
	public static final long CTD_MAX_REG = 8000;
	
	// 	Filtros
	private RecursoVO recurso = new RecursoVO();
	private ViaDeudaVO viaDeuda = new ViaDeudaVO();
	private Date   fechaDesde;
	private Date   fechaHasta;
	
	private String fechaDesdeView;
	private String fechaHastaView;

	
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ViaDeudaVO> listViaDeuda = new ArrayList<ViaDeudaVO>();

	private List<TipoReporte> listTipoReporte = new ArrayList<TipoReporte>();
	private TipoReporte tipoReporte = TipoReporte.OpcionNula;
	private String idTipoReporte = null;
	
	
	private PlanillaVO reporteGenerado = new PlanillaVO();

	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";
	
	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;

	// bandera utilizada por el jsp para visualizar o no el combo procurador
	private boolean visualizarComboProcurador = false;
	
	// Constructores
	public DeudaAnuladaReport() {       
       super(GdeSecurityConstants.DEUDA_ANULADA_REPORT);        
    }

	// Getters y Setters	
	public ViaDeudaVO getViaDeuda() {
		return viaDeuda;
	}

	public void setViaDeuda(ViaDeudaVO viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public String getFechaDesdeView() {
		return fechaDesdeView;
	}

	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}

	public String getFechaHastaView() {
		return fechaHastaView;
	}

	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public List<TipoReporte> getListTipoReporte() {
		return listTipoReporte;
	}
	public void setListTipoReporte(List<TipoReporte> listTipoReporte) {
		this.listTipoReporte = listTipoReporte;
	}
	public List<ViaDeudaVO> getListViaDeuda() {
		return listViaDeuda;
	}
	public void setListViaDeuda(List<ViaDeudaVO> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}
	public RecursoVO getRecurso() {
		return recurso;
	}
	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}
	public TipoReporte getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(TipoReporte tipoReporte) {
		this.tipoReporte = tipoReporte;
	}

	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
	public String getIdTipoReporte() {
		return idTipoReporte;
	}
	public void setIdTipoReporte(String idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}

	public boolean getVisualizarComboProcurador() {
		return visualizarComboProcurador;
	}
	public void setVisualizarComboProcurador(boolean visualizarComboProcurador) {
		this.visualizarComboProcurador = visualizarComboProcurador;
	}
	
	public String getDesRunningRun() {
		return desRunningRun;
	}

	public void setDesRunningRun(String desRunningRun) {
		this.desRunningRun = desRunningRun;
	}

	public String getEstRunningRun() {
		return estRunningRun;
	}

	public void setEstRunningRun(String estRunningRun) {
		this.estRunningRun = estRunningRun;
	}

	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}

	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}

	public boolean isProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}
	
	public String getDesErrorRun() {
		return desErrorRun;
	}

	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	public String getEstErrorRun() {
		return estErrorRun;
	}

	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}

	
	// View getters
	
}
