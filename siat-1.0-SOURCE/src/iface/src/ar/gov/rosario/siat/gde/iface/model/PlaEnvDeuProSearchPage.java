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
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del PlaEnvDeuPro
 * 
 * @author Tecso
 *
 */
public class PlaEnvDeuProSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "plaEnvDeuProSearchPageVO";
	
	private PlaEnvDeuProVO plaEnvDeuPro= new PlaEnvDeuProVO();
	private Date fechaEnvioDesde;
	private Date fechaEnvioHasta;
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	private List<ProcuradorVO> listProcurador = new ArrayList<ProcuradorVO>();
	private List<EstPlaEnvDeuPrVO> listEstPlaEnvDeuPrVO = new ArrayList<EstPlaEnvDeuPrVO>();
	 
	
	private String fechaEnvioDesdeView = "";
	private String fechaEnvioHastaView = "";

	// flags de permisos
	private boolean recomponerPlanillaEnabled = true;
	private boolean habilitarPlanillaEnabled = true; 

	// Constructores
	public PlaEnvDeuProSearchPage() {       
       super(GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS);        
    }
	
	// Getters y Setters
	public PlaEnvDeuProVO getPlaEnvDeuPro() {
		return plaEnvDeuPro;
	}
	public void setPlaEnvDeuPro(PlaEnvDeuProVO plaEnvDeuPro) {
		this.plaEnvDeuPro = plaEnvDeuPro;
	}

	public Date getFechaEnvioDesde() {
		return fechaEnvioDesde;
	}

	public void setFechaEnvioDesde(Date fechaDesde) {
		this.fechaEnvioDesde = fechaDesde;
		this.fechaEnvioDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}

	public Date getFechaEnvioHasta() {
		return fechaEnvioHasta;
	}

	public void setFechaEnvioHasta(Date fechaHasta) {
		this.fechaEnvioHasta = fechaHasta;
		this.fechaEnvioHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ProcuradorVO> getListProcurador() {
		return listProcurador;
	}

	public void setListProcurador(List<ProcuradorVO> listProcurador) {
		this.listProcurador = listProcurador;
	}

	public List<EstPlaEnvDeuPrVO> getListEstPlaEnvDeuPrVO() {
		return listEstPlaEnvDeuPrVO;
	}

	public void setListEstPlaEnvDeuPrVO(List<EstPlaEnvDeuPrVO> listEstPlaEnvDeuPrVO) {
		this.listEstPlaEnvDeuPrVO = listEstPlaEnvDeuPrVO;
	}

	public String getRecomponerPlanillaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(recomponerPlanillaEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_RECOMPONER_PLANILLA);
	}

	public void setRecomponerPlanillaEnabled(boolean recomponerPlanillaEnabled) {
		this.recomponerPlanillaEnabled = recomponerPlanillaEnabled;
	}

	public String getHabilitarPlanillaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(habilitarPlanillaEnabled, GdeSecurityConstants.ADM_PLANILLA_DEU_PRO_MAS, GdeSecurityConstants.MTD_HABILITAR_PLANILLA);		
	}

	public void setHabilitarPlanillaBussEnabled(boolean habilitarPlanillaEnabled) {
		this.habilitarPlanillaEnabled = habilitarPlanillaEnabled;
	}
		
	// View getters
	public void setFechaEnvioDesdeView(String fechaDesdeView) {
		this.fechaEnvioDesdeView = fechaDesdeView;
	}
	public String getFechaEnvioDesdeView() {
		return fechaEnvioDesdeView;
	}

	public void setFechaEnvioHastaView(String fechaHastaView) {
		this.fechaEnvioHastaView = fechaHastaView;
	}
	public String getFechaEnvioHastaView() {
		return fechaEnvioHastaView;
	}





}
