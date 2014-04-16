//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.buss.bean;

import java.util.Date;

import ar.gov.rosario.siat.def.buss.bean.ViaDeuda;
import ar.gov.rosario.siat.def.iface.model.ViaDeudaVO;
import coop.tecso.demoda.iface.helper.ModelUtil;
import coop.tecso.demoda.iface.model.Common;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Clase Auxiliar para la Generacion del Reporte de Convenios Formalizados
 * 
 * @author Tecso
 *
 */
public class AuxConvenioFormReport extends Common {
	
	private static final long serialVersionUID = 1L;

	private ViaDeuda viaDeuda;
	
	private Date   fechaConvenioDesde;
	private Date   fechaConvenioHasta;
	private String fechaConvenioDesdeView;
	private String fechaConvenioHastaView;
		
	private PlanillaVO reporteGenerado = new PlanillaVO();

	private String userId;
	private String userName;
	
	private Procurador procurador;
	
	//	 Constructores
	public AuxConvenioFormReport() { 
	}

	// Getters Y Setters
	
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
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
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
	public void setProcurador(Procurador procuradorId) {
		this.procurador = procuradorId;
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

	// Metodos auxiliares utilizados para determinar si se selecciono:
	public boolean getViaDeudaSeleccionada(){
		ViaDeudaVO viaDeudaVO = new ViaDeudaVO();
		if(this.getViaDeuda()!=null)
			viaDeudaVO.setId(this.getViaDeuda().getId());
		else
			viaDeudaVO.setId(-1L);
		return !ModelUtil.isNullOrEmpty(viaDeudaVO);
	}

	
}
