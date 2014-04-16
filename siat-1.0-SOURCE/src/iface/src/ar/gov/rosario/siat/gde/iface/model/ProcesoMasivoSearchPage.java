//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;
import ar.gov.rosario.siat.pro.iface.model.EstadoCorridaVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Search Page de Proceso Masivo de Deudas
 * @author tecso
 *
 */
public class ProcesoMasivoSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "procesoMasivoSearchPageVO";

	private ProcesoMasivoVO procesoMasivo = new ProcesoMasivoVO();
	
	private Date 	fechaEnvioDesde; 
	private Date 	fechaEnvioHasta; 

	private String 	fechaEnvioDesdeView = "";
	private String 	fechaEnvioHastaView = "";
	
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();

	private List<EstadoCorridaVO> listEstadoCorrida = new ArrayList<EstadoCorridaVO>();
	
	private Boolean admProcesoBussEnabled      = true;
	
	public ProcesoMasivoSearchPage(){
		super(GdeSecurityConstants.ABM_PROCESO_MASIVO);
	}
	
	public ProcesoMasivoSearchPage(TipProMasVO tipProMas){
		this();
		this.procesoMasivo.setTipProMas(tipProMas);
	}


	// Getters y Setters	
	public ProcesoMasivoVO getProcesoMasivo() {
		return procesoMasivo;
	}
	public void setProcesoMasivo(ProcesoMasivoVO procesoMasivo) {
		this.procesoMasivo = procesoMasivo;
	}
	public Date getFechaEnvioDesde() {
		return fechaEnvioDesde;
	}
	public void setFechaEnvioDesde(Date fechaEnvioDesde) {
		this.fechaEnvioDesde = fechaEnvioDesde;
		this.fechaEnvioDesdeView = DateUtil.formatDate(fechaEnvioDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public Date getFechaEnvioHasta() {
		return fechaEnvioHasta;
	}
	public void setFechaEnvioHasta(Date fechaEnvioHasta) {
		this.fechaEnvioHasta = fechaEnvioHasta;
		this.fechaEnvioHastaView = DateUtil.formatDate(fechaEnvioHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}
	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}
	public String getFechaEnvioDesdeView() {
		return fechaEnvioDesdeView;
	}
	public void setFechaEnvioDesdeView(String fechaEnvioDesdeView) {
		this.fechaEnvioDesdeView = fechaEnvioDesdeView;
	}
	public String getFechaEnvioHastaView() {
		return fechaEnvioHastaView;
	}
	public void setFechaEnvioHastaView(String fechaEnvioHastaView) {
		this.fechaEnvioHastaView = fechaEnvioHastaView;
	}
	public List<EstadoCorridaVO> getListEstadoCorrida() {
		return listEstadoCorrida;
	}
	public void setListEstadoCorrida(List<EstadoCorridaVO> listEstadoCorrida) {
		this.listEstadoCorrida = listEstadoCorrida;
	}

	// Flags Seguridad


	public Boolean getAdmProcesoBussEnabled() {
		return admProcesoBussEnabled;
	}
	public void setAdmProcesoBussEnabled(Boolean admProcesoBussEnabled) {
		this.admProcesoBussEnabled = admProcesoBussEnabled;
	}
	
	public String getAdmProcesoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getAdmProcesoBussEnabled(),
				GdeSecurityConstants.ABM_PROCESO_MASIVO,	GdeSecurityConstants.MTD_ADM_PROCESO);
	}

}
