//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.helper.StringUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class AuxContribuyenteCerReport extends Common{
	
	private static final long serialVersionUID = 1L;
	private HashMap<Long, Map<Integer, Double>> listadoResultado= null;
	private Recurso recurso;
	private Date     fechaDesde;
	private Date     fechaHasta;
	private Integer  periodoDesde;
	private Integer  periodoHasta;
	private Integer  anioDesde;
	private Integer  anioHasta;
	private Date   	 fechaReporte;
	private String   fechaReporteView="";
	private String   fechaDesdeView="";
	private String   fechaHastaView="";
	private String   periodoDesdeView="";
	private String   periodoHastaView="";
	private String   anioDesdeView="";
	private String   anioHastaView="";

	private String userName;
	private String userId;
	
	private Long totalCantCuentas;
	private Double totalImporteDeudas;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	//	 Constructores
	public AuxContribuyenteCerReport() {       
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

	public Recurso getRecurso() {
		return recurso;
	}

	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
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

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFechaReporteView() {
		return fechaReporteView;
	}

	public void setFechaReporteView(String fechaReporteView) {
		this.fechaReporteView = fechaReporteView;
	}

	public Date getFechaReporte() {
		return fechaReporte;
	}

	public void setFechaReporte(Date fechaReporte) {
		this.fechaReporte = fechaReporte;
		this.fechaReporteView = DateUtil.formatDate(fechaReporte, DateUtil.ddSMMSYYYY_MASK);

	}

	
	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public String getPeriodoDesdeView() {
		return periodoDesdeView;
	}

	public void setPeriodoDesdeView(String periodoDesdeView) {
		this.periodoDesdeView = periodoDesdeView;
	}

	public String getPeridodoHastaView() {
		return periodoHastaView;
	}

	public void setPeridodoHastaView(String peridodoHastaView) {
		this.periodoHastaView = peridodoHastaView;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
		this.anioDesdeView = StringUtil.formatInteger(anioDesde);
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
		this.anioHastaView = StringUtil.formatInteger(anioHasta);
	}

	public String getAnioDesdeView() {
		return anioDesdeView;
	}

	public void setAnioDesdeView(String anioDesdeView) {
		this.anioDesdeView = anioDesdeView;
	}

	public String getAnioHastaView() {
		return anioHastaView;
	}

	public void setAnioHastaView(String anioHastaView) {
		this.anioHastaView = anioHastaView;
		
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
		this.periodoDesdeView = StringUtil.formatInteger(periodoDesde);
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
		this.periodoHastaView = StringUtil.formatInteger(periodoHasta);
	}

   public String getPeriodoHastaView() {
		return periodoHastaView;
	}

	public void setPeriodoHastaView(String periodoHastaView) {
		this.periodoHastaView = periodoHastaView;
	}

	public HashMap<Long, Map<Integer, Double>> getListadoResultado() {
		return listadoResultado;
	}

	public void setListadoResultado(HashMap<Long, Map<Integer, Double>> listadoResultado) {
		this.listadoResultado = listadoResultado;
	}

	
}
