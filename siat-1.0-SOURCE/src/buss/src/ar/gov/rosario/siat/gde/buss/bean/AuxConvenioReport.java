//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import ar.gov.rosario.siat.gde.iface.model.EstadoConvenioVO;
import ar.gov.rosario.siat.gde.iface.model.PlanVO;
import ar.gov.rosario.siat.gde.iface.model.ProcuradorVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;
import coop.tecso.demoda.iface.model.TipoReporte;

/**
 * Clase Auxiliar para la Generacion del Reporte de Convenios
 * 
 * @author Tecso
 *
 */
public class AuxConvenioReport extends Common {
	
	private static final long serialVersionUID = 1L;

	// Convenio utilizado para cargarle los datos de los filtros de busqueda:
	// 		convenio.plan
	// 		convenio.viaDeuda
	// 		convenio.procurador
	// 		convenio.estadoConvenio
	private Convenio convenio = new Convenio();
	
	private List<Recurso> listRecurso;
	private Recurso recurso = new Recurso();
	private List<Plan> listPlan;
	private Date   fechaConvenioDesde;
	private String fechaConvenioDesdeView;
	private Date   fechaConvenioHasta;
	private String fechaConvenioHastaView;
	private List<ViaDeuda> listViaDeuda;
	
	private List<Procurador> listProcurador;
	private Integer cuotaDesde; 
	private String  cuotaDesdeView;
	private Integer cuotaHasta;
	private String  cuotaHastaView;

	private List<EstadoConvenio> listEstadoConvenio;
	private List<TipoReporte> listTipoReporte;
	private TipoReporte tipoReporte = TipoReporte.OpcionNula;
	private String idTipoReporte = null;
	
	private Long idProcuradorSesion = null; // TODO Ver si hay que pasarlo!!
	
	private String userId;
	private String userName;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	// Constructores
	public AuxConvenioReport() {       
	}
	// Getters Y Setters
	
	public Convenio getConvenio() {
		return convenio;
	}

	public void setConvenio(Convenio convenio) {
		this.convenio = convenio;
	}

	public Integer getCuotaDesde() {
		return cuotaDesde;
	}

	public void setCuotaDesde(Integer cuotaDesde) {
		this.cuotaDesde = cuotaDesde;
	}

	public String getCuotaDesdeView() {
		return cuotaDesdeView;
	}

	public void setCuotaDesdeView(String cuotaDesdeView) {
		this.cuotaDesdeView = cuotaDesdeView;
	}

	public Integer getCuotaHasta() {
		return cuotaHasta;
	}

	public void setCuotaHasta(Integer cuotaHasta) {
		this.cuotaHasta = cuotaHasta;
	}

	public String getCuotaHastaView() {
		return cuotaHastaView;
	}

	public void setCuotaHastaView(String cuotaHastaView) {
		this.cuotaHastaView = cuotaHastaView;
	}

	public Date getFechaConvenioDesde() {
		return fechaConvenioDesde;
	}

	public void setFechaConvenioDesde(Date fechaConvenioDesde) {
		this.fechaConvenioDesde = fechaConvenioDesde;
	}

	public String getFechaConvenioDesdeView() {
		return fechaConvenioDesdeView;
	}

	public void setFechaConvenioDesdeView(String fechaConvenioDesdeView) {
		this.fechaConvenioDesdeView = fechaConvenioDesdeView;
	}

	public Date getFechaConvenioHasta() {
		return fechaConvenioHasta;
	}

	public void setFechaConvenioHasta(Date fechaConvenioHasta) {
		this.fechaConvenioHasta = fechaConvenioHasta;
	}

	public String getFechaConvenioHastaView() {
		return fechaConvenioHastaView;
	}

	public void setFechaConvenioHastaView(String fechaConvenioHastaView) {
		this.fechaConvenioHastaView = fechaConvenioHastaView;
	}

	public Long getIdProcuradorSesion() {
		return idProcuradorSesion;
	}

	public void setIdProcuradorSesion(Long idProcuradorSesion) {
		this.idProcuradorSesion = idProcuradorSesion;
	}

	public String getIdTipoReporte() {
		return idTipoReporte;
	}

	public void setIdTipoReporte(String idTipoReporte) {
		this.idTipoReporte = idTipoReporte;
	}

	public List<EstadoConvenio> getListEstadoConvenio() {
		return listEstadoConvenio;
	}

	public void setListEstadoConvenio(List<EstadoConvenio> listEstadoConvenio) {
		this.listEstadoConvenio = listEstadoConvenio;
	}

	public List<Plan> getListPlan() {
		return listPlan;
	}

	public void setListPlan(List<Plan> listPlan) {
		this.listPlan = listPlan;
	}

	public List<Procurador> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<Procurador> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<Recurso> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<Recurso> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<TipoReporte> getListTipoReporte() {
		return listTipoReporte;
	}

	public void setListTipoReporte(List<TipoReporte> listTipoReporte) {
		this.listTipoReporte = listTipoReporte;
	}

	public List<ViaDeuda> getListViaDeuda() {
		return listViaDeuda;
	}

	public void setListViaDeuda(List<ViaDeuda> listViaDeuda) {
		this.listViaDeuda = listViaDeuda;
	}

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}

	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}

	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}

	public TipoReporte getTipoReporte() {
		return tipoReporte;
	}

	public void setTipoReporte(TipoReporte tipoReporte) {
		this.tipoReporte = tipoReporte;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	//	 Metodos auxiliares utilizados para determinar si se selecciono:
	// recurso, categoria
	public boolean getPlanSeleccionado(){
		PlanVO planVO = new PlanVO();
		if(this.getConvenio() != null && this.getConvenio().getPlan() != null)
			planVO.setId(this.getConvenio().getPlan().getId());
		else
			planVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(planVO);
	}
	public boolean getViaDeudaSeleccionada(){
		ViaDeudaVO viaDeudaVO = new ViaDeudaVO();
		if(this.getConvenio() != null && this.getConvenio().getViaDeuda()!=null)
			viaDeudaVO.setId(this.getConvenio().getViaDeuda().getId());
		else
			viaDeudaVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(viaDeudaVO);
	}
	
	public boolean getProcuradorSeleccionado(){
		ProcuradorVO procuradorVO = new ProcuradorVO();
		if(this.getConvenio() != null && this.getConvenio().getProcurador() != null)
			procuradorVO.setId(this.getConvenio().getProcurador().getId());
		else
			procuradorVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(procuradorVO);
	}
	public boolean getCuotaDesdeSeleccionada(){
		return this.getCuotaDesde() != null;
	}
	public boolean getCuotaHastaSeleccionada(){
		return this.getCuotaHasta() != null;
	}
	public boolean getEstadoConvenioSeleccionado(){
		EstadoConvenioVO estadoConvenioVO = new EstadoConvenioVO();
		if(this.getConvenio() != null && this.getConvenio().getEstadoConvenio() != null)
			estadoConvenioVO.setId(this.getConvenio().getEstadoConvenio().getId());
		else
			estadoConvenioVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(estadoConvenioVO);
	}
	
}
