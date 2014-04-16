//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import ar.gov.rosario.siat.pad.buss.bean.Cuenta;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class AuxGesJudReport extends Common{
	
	private static final long serialVersionUID = 1L;
	
	private String desRecurso ="";
	private TipoJuzgado tipoJuzgado = new TipoJuzgado();
	private Procurador procurador;
	private Cuenta cuenta;
	private String strEventosOpe = "";

	
	private Date   fechaDesde;
	private Date   fechaHasta;
	private String fechaDesdeView;
	private String fechaHastaView;

	private String userName;
	private String userId;
	
	private Double totalImpDeudasEncontradas;
	private Long totalCantDeudasEncontradas;
	private Double totalImpDeudasEnviadas;
	private Long totalCantDeudasEnviadas;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	//	 Constructores
	public AuxGesJudReport() {       
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

	public String getStrEventosOpe() {
		return strEventosOpe;
	}

	public void setStrEventosOpe(String strEventosOpe) {
		this.strEventosOpe = strEventosOpe;
	}

	public String getDesRecurso() {
		return desRecurso;
	}

	public void setDesRecurso(String desRecurso) {
		this.desRecurso = desRecurso;
	}

	public TipoJuzgado getTipoJuzgado() {
		return tipoJuzgado;
	}

	public void setTipoJuzgado(TipoJuzgado tipoJuzgado) {
		this.tipoJuzgado = tipoJuzgado;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Cuenta getCuenta() {
		return cuenta;
	}

	public void setCuenta(Cuenta cuenta) {
		this.cuenta = cuenta;
	}

	public Double getTotalImpDeudasEncontradas() {
		return totalImpDeudasEncontradas;
	}

	public void setTotalImpDeudasEncontradas(Double totalImpDeudasEncontradas) {
		this.totalImpDeudasEncontradas = totalImpDeudasEncontradas;
	}

	public Long getTotalCantDeudasEncontradas() {
		return totalCantDeudasEncontradas;
	}

	public void setTotalCantDeudasEncontradas(Long totalCantDeudasEncontradas) {
		this.totalCantDeudasEncontradas = totalCantDeudasEncontradas;
	}

	public Double getTotalImpDeudasEnviadas() {
		return totalImpDeudasEnviadas;
	}

	public void setTotalImpDeudasEnviadas(Double totalImpDeudasEnviadas) {
		this.totalImpDeudasEnviadas = totalImpDeudasEnviadas;
	}

	public Long getTotalCantDeudasEnviadas() {
		return totalCantDeudasEnviadas;
	}

	public void setTotalCantDeudasEnviadas(Long totalCantDeudasEnviadas) {
		this.totalCantDeudasEnviadas = totalCantDeudasEnviadas;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	
}
