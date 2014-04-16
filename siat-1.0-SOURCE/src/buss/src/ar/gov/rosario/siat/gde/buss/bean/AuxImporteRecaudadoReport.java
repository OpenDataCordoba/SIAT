//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import ar.gov.rosario.siat.def.buss.bean.Recurso;
import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Importes Recaudados por Planes
 * 
 * @author Tecso
 *
 */
public class AuxImporteRecaudadoReport extends Common {
	
	private static final long serialVersionUID = 1L;
	
	private Plan plan;
	private Recurso recurso;
	private Procurador procurador; 
	
	private ViaDeuda viaDeuda;
	
	private Integer tipoReporte=0;
	
	private String viaDeudaEnabled="true";
	
	private Date fechaPagoDesde;
	
	private Date fechaPagoHasta;
	
	private String fechaPagoDesdeView = "";
	private String fechaPagoHastaView = "";

	//private List<Recurso> listRecurso = new ArrayList<Recurso>();
	//private List<Plan> listPlan = new ArrayList<Plan>();
	//private List<ViaDeuda> listViaDeuda = new ArrayList<ViaDeuda>();

	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	private String userId;
	private String userName;

	//	 Constructores
	public AuxImporteRecaudadoReport() { 
	}

	// Getters Y Setters
	public Date getFechaPagoDesde() {
		return fechaPagoDesde;
	}
	public void setFechaPagoDesde(Date fechaPagoDesde) {
		this.fechaPagoDesde = fechaPagoDesde;
	}
	public String getFechaPagoDesdeView() {
		return fechaPagoDesdeView;
	}
	public void setFechaPagoDesdeView(String fechaPagoDesdeView) {
		this.fechaPagoDesdeView = fechaPagoDesdeView;
	}
	public Date getFechaPagoHasta() {
		return fechaPagoHasta;
	}
	public void setFechaPagoHasta(Date fechaPagoHasta) {
		this.fechaPagoHasta = fechaPagoHasta;
	}
	public String getFechaPagoHastaView() {
		return fechaPagoHastaView;
	}
	public void setFechaPagoHastaView(String fechaPagoHastaView) {
		this.fechaPagoHastaView = fechaPagoHastaView;
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
	public Integer getTipoReporte() {
		return tipoReporte;
	}
	public void setTipoReporte(Integer tipoReporte) {
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
	public String getViaDeudaEnabled() {
		return viaDeudaEnabled;
	}
	public void setViaDeudaEnabled(String viaDeudaEnabled) {
		this.viaDeudaEnabled = viaDeudaEnabled;
	}
	public Recurso getRecurso() {
		return recurso;
	}
	public void setRecurso(Recurso recurso) {
		this.recurso = recurso;
	}
	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public ViaDeuda getViaDeuda() {
		return viaDeuda;
	}
	public void setViaDeuda(ViaDeuda viaDeuda) {
		this.viaDeuda = viaDeuda;
	}

}
