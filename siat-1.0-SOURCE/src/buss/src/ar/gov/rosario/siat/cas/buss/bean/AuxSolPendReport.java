//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.cas.iface.model.TipoSolicitudVO;
import ar.gov.rosario.siat.def.buss.bean.Area;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte Solicitudes Pendientes
 * @author Andrei
 *
 */
public class AuxSolPendReport extends Common {
	
	private static final long serialVersionUID = 1L;

	private List<TipoSolicitudVO> listTipoSolicitud = new ArrayList<TipoSolicitudVO>();
	private List<AreaVO> listArea = new ArrayList<AreaVO>();
	
	private Recurso recurso;
	private Area area;
	private TipoSolicitud tipoSolicitud;
	
	private Date   fechaDesde;
	private Date   fechaHasta;
	private String fechaDesdeView;
	private String fechaHastaView;

	private String userName;
	private String userId;

	private Long totalTipoSolicitudes;
	private Long totalIngresadas;
	private Long totalPendientes;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	//	 Constructores
	public AuxSolPendReport() {       
	}

	// Getters y Setters
	
	public List<TipoSolicitudVO> getListTipoSolicitud() {
		return listTipoSolicitud;
	}

	public void setListTipoSolicitud(List<TipoSolicitudVO> listTipoSolicitud) {
		this.listTipoSolicitud = listTipoSolicitud;
	}

	public List<AreaVO> getListArea() {
		return listArea;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}
	
	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public TipoSolicitud getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitud tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
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

	public Long getTotalTipoSolicitudes() {
		return totalTipoSolicitudes;
	}

	public void setTotalTipoSolicitudes(Long totalTipoSolicitudes) {
		this.totalTipoSolicitudes = totalTipoSolicitudes;
	}

	public Long getTotalIngresadas() {
		return totalIngresadas;
	}

	public void setTotalIngresadas(Long totalIngresadas) {
		this.totalIngresadas = totalIngresadas;
	}

	public Long getTotalPendientes() {
		return totalPendientes;
	}

	public void setTotalPendientes(Long totalPendientes) {
		this.totalPendientes = totalPendientes;
	}
	
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
}
