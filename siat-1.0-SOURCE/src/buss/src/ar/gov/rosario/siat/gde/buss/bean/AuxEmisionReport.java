//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import ar.gov.rosario.siat.def.buss.bean.RecClaDeu;
import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class AuxEmisionReport extends Common{
	
	private static final long serialVersionUID = 1L;

	private Recurso recurso;
	private RecClaDeu recclaDeu; 
	private Date   fechaDesde;
	private Date   fechaHasta;
	private String fechaDesdeView;
	private String fechaHastaView;

	private String userName;
	private String userId;

	private Long totalCantCuentas;
	private Double totalImporteDeudas;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	//	 Constructores
	public AuxEmisionReport() {       
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

	public RecClaDeu getRecclaDeu() {
		return recclaDeu;
	}

	public void setRecclaDeu(RecClaDeu recclaDeu) {
		this.recclaDeu = recclaDeu;
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

	
}
