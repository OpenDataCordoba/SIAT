//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.model.IntegerVO;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.ReportVO;

/**
 * Value Object del Saldo Por Caducidad Masivo de Convenios
 * @author tecso
 *
 */
public class SalPorCadMasivoSelAdapter extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "salPorCadMasivoSelAdapterVO";

	private SaldoPorCaducidadVO    saldoPorCaducidad  = new SaldoPorCaducidadVO();
	
	private Integer estadoProcesoConvenio = -1;
	private List<IntegerVO> listEstadoProcesoConvenio = new ArrayList<IntegerVO>(); 
	
	private String act="";
	
	private Boolean filtroHabilitado = false;
	
	private Boolean paraImprimir = false;
	
	// Constructores
	public SalPorCadMasivoSelAdapter() {       
       super(GdeSecurityConstants.ABM_SALDOPORCADUCIDAD);        
    }
	
	// Getters y Setters

	public SaldoPorCaducidadVO getSaldoPorCaducidad() {
		return saldoPorCaducidad;
	}

	public void setSaldoPorCaducidad(SaldoPorCaducidadVO saldoPorCaducidad) {
		this.saldoPorCaducidad = saldoPorCaducidad;
	}

	public String getAct() {
		return act;
	}

	public void setAct(String act) {
		this.act = act;
	}

	public Integer getEstadoProcesoConvenio() {
		return estadoProcesoConvenio;
	}

	public void setEstadoProcesoConvenio(Integer estadoProcesoConvenio) {
		this.estadoProcesoConvenio = estadoProcesoConvenio;
	}

	public List<IntegerVO> getListEstadoProcesoConvenio() {
		return listEstadoProcesoConvenio;
	}

	public void setListEstadoProcesoConvenio(
			List<IntegerVO> listEstadoProcesoConvenio) {
		this.listEstadoProcesoConvenio = listEstadoProcesoConvenio;
	}

	public Boolean getFiltroHabilitado() {
		return filtroHabilitado;
	}

	public void setFiltroHabilitado(Boolean filtroHabilitado) {
		this.filtroHabilitado = filtroHabilitado;
	}
	
	public Boolean getParaImprimir() {
		return paraImprimir;
	}

	public void setParaImprimir(Boolean paraImprimir) {
		this.paraImprimir = paraImprimir;
	}

	public void prepareReport(Long format) {	 
		 ReportVO report = this.getReport();
		 report.setReportFormat(format);	
		 report.setReportTitle("Selección Almacenada de Planes para Saldo por Caducidad");     
		 report.setReportBeanName("Convenio");
		 report.setReportFileName(this.getClass().getName());
	}
}
