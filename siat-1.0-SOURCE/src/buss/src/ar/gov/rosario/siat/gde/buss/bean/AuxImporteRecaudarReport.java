//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Importes a Recaudar de Planes
 * 
 * @author Tecso
 *
 */
public class AuxImporteRecaudarReport extends Common {
	
	private static final long serialVersionUID = 1L;
	
	private Plan plan;
	private Recurso recurso;
	
	private Date fechaVencimientoDesde;
	
	private Date fechaVencimientoHasta;
	
	private String fechaVencimientoDesdeView = "";
	private String fechaVencimientoHastaView = "";

	private List<Recurso> listRecurso;
	private List<Plan> listPlan;
	
	private PlanillaVO reporteGenerado = new PlanillaVO();

	private String userId;
	private String userName;

	private Procurador procurador;

	//	 Constructores
	public AuxImporteRecaudarReport() { 
	}

	// Getters Y Setters
	public Date getFechaVencimientoDesde() {
		return fechaVencimientoDesde;
	}
	public void setFechaVencimientoDesde(Date fechaVencimientoDesde) {
		this.fechaVencimientoDesde = fechaVencimientoDesde;
		this.fechaVencimientoDesdeView = DateUtil.formatDate(fechaVencimientoDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoDesdeView() {
		return fechaVencimientoDesdeView;
	}
	public void setFechaVencimientoDesdeView(String fechaVencimientoDesdeView) {
		this.fechaVencimientoDesdeView = fechaVencimientoDesdeView;
	}
	public Date getFechaVencimientoHasta() {
		return fechaVencimientoHasta;
	}
	public void setFechaVencimientoHasta(Date fechaVencimientoHasta) {
		this.fechaVencimientoHasta = fechaVencimientoHasta;
		this.fechaVencimientoHastaView = DateUtil.formatDate(fechaVencimientoHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaVencimientoHastaView() {
		return fechaVencimientoHastaView;
	}
	public void setFechaVencimientoHastaView(String fechaVencimientoHastaView) {
		this.fechaVencimientoHastaView = fechaVencimientoHastaView;
	}
	public List<Plan> getListPlan() {
		return listPlan;
	}
	public void setListPlan(List<Plan> listPlan) {
		this.listPlan = listPlan;
	}
	public List<Recurso> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<Recurso> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public Plan getPlan() {
		return plan;
	}
	public void setPlan(Plan plan) {
		this.plan = plan;
	}
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
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

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Procurador getProcurador() {
		return procurador;
	}
	

}
