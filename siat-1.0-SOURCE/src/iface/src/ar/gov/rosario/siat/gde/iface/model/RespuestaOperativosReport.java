//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import coop.tecso.demoda.iface.model.PlanillaVO;

/**
 * Reporte del Respuesta Operativos
 * 
 * @author Tecso
 *
 */
public class RespuestaOperativosReport extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "respuestaOperativosReportVO";

	private PlanillaVO reporteGenerado = new PlanillaVO();
	
	private List<TipProMasVO> listTipProMas = new ArrayList<TipProMasVO>();
	private TipProMasVO tipProMas = new TipProMasVO();

	private List<ProcesoMasivoVO> listProcesoMasivo = new ArrayList<ProcesoMasivoVO>();
	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
	
	private String desRunningRun = "";
	private String estRunningRun = "";
	private String desErrorRun = "";
	private String estErrorRun = "";

	//Flags
	private boolean procesando = false;
	private boolean existeReporteGenerado = false;
	private boolean error = false;
	
	// Constructores
	public RespuestaOperativosReport() {       
       super(GdeSecurityConstants.CONSULTAR_RESPUESTA_OPERATIVOS);        
    }

	// Getters y Setters
	public PlanillaVO getReporteGenerado() {
		return reporteGenerado;
	}
	public void setReporteGenerado(PlanillaVO reporteGenerado) {
		this.reporteGenerado = reporteGenerado;
	}
	public List<ProcesoMasivoVO> getListProcesoMasivo() {
		return listProcesoMasivo;
	}
	public void setListProcesoMasivo(List<ProcesoMasivoVO> listProcesoMasivo) {
		this.listProcesoMasivo = listProcesoMasivo;
	}
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public List<TipProMasVO> getListTipProMas() {
		return listTipProMas;
	}
	public void setListTipProMas(List<TipProMasVO> listTipProMas) {
		this.listTipProMas = listTipProMas;
	}
	public TipProMasVO getTipProMas() {
		return tipProMas;
	}
	public void setTipProMas(TipProMasVO tipProMas) {
		this.tipProMas = tipProMas;
	}
	public boolean isProcesando() {
		return procesando;
	}
	public void setProcesando(boolean procesando) {
		this.procesando = procesando;
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
	public boolean isExisteReporteGenerado() {
		return existeReporteGenerado;
	}
	public void setExisteReporteGenerado(boolean existeReporteGenerado) {
		this.existeReporteGenerado = existeReporteGenerado;
	}
	public String getDesErrorRun() {
		return desErrorRun;
	}
	public void setDesErrorRun(String desErrorRun) {
		this.desErrorRun = desErrorRun;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getEstErrorRun() {
		return estErrorRun;
	}
	public void setEstErrorRun(String estErrorRun) {
		this.estErrorRun = estErrorRun;
	}
	
}
