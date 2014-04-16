//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import coop.tecso.demoda.iface.helper.DateUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Recaudacion por Recurso
 * 
 * @author Tecso
 *
 */
public class AuxDeudaProcuradorReport extends Common{
	
	private static final long serialVersionUID = 1L;
	
	private Procurador procurador;	
	private Date   fechaDesde;
	private Date   fechaHasta;
	private String fechaDesdeView;
	private String fechaHastaView;
	
	private Integer tipoReporte;
	
	private String userId;
	private String userName;

	
	private List<PlanillaVO> listReportesGenerado = new ArrayList<PlanillaVO>(); 
	
	//	 Constructores
	public AuxDeudaProcuradorReport() {       
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

	
	public List<PlanillaVO> getListReportesGenerado() {
		return listReportesGenerado;
	}

	public void setListReportesGenerado(List<PlanillaVO> listReportesGenerado) {
		this.listReportesGenerado = listReportesGenerado;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Procurador getProcurador() {
		return procurador;
	}

	public void setProcurador(Procurador procurador) {
		this.procurador = procurador;
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

}
