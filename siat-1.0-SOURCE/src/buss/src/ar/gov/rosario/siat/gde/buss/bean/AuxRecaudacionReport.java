//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.CategoriaVO;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class AuxRecaudacionReport extends Common{
	
	private static final long serialVersionUID = 1L;
	
	private Recurso recurso = new Recurso();
	private ViaDeuda viaDeuda = new ViaDeuda();
	private List<Recurso> listRecurso = new ArrayList<Recurso>();
	
	private Date   fechaDesde;
	private Date   fechaHasta;
	private String fechaDesdeView;
	private String fechaHastaView;

	private Integer tipoFecha=0;
	
	private String userId;
	private String userName;

	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	//	 Constructores
	public AuxRecaudacionReport() {       
	}

	// Getters y Setters
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
	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
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
	public List<Recurso> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<Recurso> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public Integer getTipoFecha() {
		return tipoFecha;
	}

	public void setTipoFecha(Integer tipoFecha) {
		this.tipoFecha = tipoFecha;
	}

	// Metodos auxiliares utilizados para determinar si se selecciono:
	// recurso, categoria
	public boolean getRecursoSeleccionado(){
		RecursoVO recursoVO = new RecursoVO();
		if(this.getRecurso()!=null)
			recursoVO.setId(this.getRecurso().getId());
		else
			recursoVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(recursoVO);
	}
	public boolean getCategoriaSeleccionada(){
		CategoriaVO categoriaVO = new CategoriaVO();
		if(this.getRecurso()!=null && this.getRecurso().getCategoria()!=null)
			categoriaVO.setId(this.getRecurso().getCategoria().getId());
		else
			categoriaVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(categoriaVO);
	}
	public boolean getViaDeudaSeleccionada(){
		ViaDeudaVO viaDeudaVO = new ViaDeudaVO();
		if(this.getViaDeuda()!=null)
			viaDeudaVO.setId(this.getViaDeuda().getId());
		else
			viaDeudaVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(viaDeudaVO);
	}
	
}
