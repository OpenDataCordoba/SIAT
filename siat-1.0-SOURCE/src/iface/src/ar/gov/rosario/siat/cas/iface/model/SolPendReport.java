//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cas.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.cas.iface.util.CasSecurityConstants;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Solicitudes Pendientes
 * 
 * @author Andrei
 *
 */
public class SolPendReport extends SiatPageModel {
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "solPendReportVO";
	
	private TipoSolicitudVO tipoSolicitud = new TipoSolicitudVO();
	private AreaVO area = new AreaVO();
	
	private Date   fechaEmiDesde;
	private Date   fechaEmiHasta;
	private String fechaEmiDesdeView = "";
	private String fechaEmiHastaView = "";

	
	private String userName;

	private Long totalCantCuentas;
	private Double totalImporteDeudas;
	
	private List<TipoSolicitudVO> listTipoSolicitud = new ArrayList<TipoSolicitudVO>();
	private List<AreaVO> listArea = new ArrayList<AreaVO>();
	
	private PlanillaVO reporteGenerado = new PlanillaVO();	
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;

	//	 Constructores
	public SolPendReport() {
		super(CasSecurityConstants.SOLPEND_REPORT);
	}

	// Getters y Setters
	public Date getFechaEmiDesde() {
		return fechaEmiDesde;
	}
	public void setFechaEmiDesde(Date fechaDesde) {
		this.fechaEmiDesde = fechaDesde;
		this.fechaEmiDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaEmiHasta() {
		return fechaEmiHasta;
	}
	public void setFechaEmiHasta(Date fechaHasta) {
		this.fechaEmiHasta = fechaHasta;
		this.fechaEmiHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaEmiDesdeView() {
		return fechaEmiDesdeView;
	}
	public void setFechaEmiDesdeView(String fechaDesdeView) {
		this.fechaEmiDesdeView = fechaDesdeView;
	}
	public String getFechaEmiHastaView() {
		return fechaEmiHastaView;
	}
	public void setFechaEmiHastaView(String fechaHastaView) {
		this.fechaEmiHastaView = fechaHastaView;
	}
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Long getTotalCantCuentas() {
		return totalCantCuentas;
	}

	public void setTotalCantCuentas(Long totalCantCuentas) {
		this.totalCantCuentas = totalCantCuentas;
	}

	public Double getTotalImporteDeudas() {
		return totalImporteDeudas;
	}

	public void setTotalImporteDeudas(Double totalImporteDeudas) {
		this.totalImporteDeudas = totalImporteDeudas;
	}

	public TipoSolicitudVO getTipoSolicitud() {
		return tipoSolicitud;
	}

	public void setTipoSolicitud(TipoSolicitudVO tipoSolicitud) {
		this.tipoSolicitud = tipoSolicitud;
	}
	
	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public List<TipoSolicitudVO> getListTipoSolicitud() {
		return listTipoSolicitud;
	}

	public void setListTipoSolicitud(List<TipoSolicitudVO> listTipoSolicitud) {
		this.listTipoSolicitud = listTipoSolicitud;
	}

	public void setListArea(List<AreaVO> listArea) {
		this.listArea = listArea;
	}

	public List<AreaVO> getListArea() {
		return listArea;
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

	public String getDesErrorRun() {
		return desErrorRun;
	}

	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}

	public String getEstErrorRun() {
		return estErrorRun;
	}

	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}

	public boolean isProcesando() {
		return procesando;
	}

	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
	}

	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}

	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}


}
