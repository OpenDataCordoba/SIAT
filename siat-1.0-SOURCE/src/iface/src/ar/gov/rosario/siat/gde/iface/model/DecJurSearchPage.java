//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * SearchPage del Convenio
 * 
 * @author Tecso
 *
 */
public class DecJurSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "decJurSearchPageVO";
	
	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();

	private DecJurVO decJur = new DecJurVO();
	private RecursoVO recurso = new RecursoVO();
	private Integer periodoDesde;
	private Integer anioDesde;
	
	private Integer periodoHasta;
	private Integer anioHasta;
	
	private boolean modoVer = false;
	
	private Boolean vueltaAtrasBussEnabled  = true;
	
	// Constructores
	public DecJurSearchPage() {       
       super(GdeSecurityConstants.ABM_DECJUR);        
    }
	
	// Getters y Setters

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public RecursoVO getRecurso() {
		return recurso;
	}

	public void setRecurso(RecursoVO recurso) {
		this.recurso = recurso;
	}

	
	
	public Integer getPeriodoDesde() {
		return periodoDesde;
	}

	public void setPeriodoDesde(Integer periodoDesde) {
		this.periodoDesde = periodoDesde;
	}

	public Integer getAnioDesde() {
		return anioDesde;
	}

	public void setAnioDesde(Integer anioDesde) {
		this.anioDesde = anioDesde;
	}

	public Integer getPeriodoHasta() {
		return periodoHasta;
	}

	public void setPeriodoHasta(Integer periodoHasta) {
		this.periodoHasta = periodoHasta;
	}

	public Integer getAnioHasta() {
		return anioHasta;
	}

	public void setAnioHasta(Integer anioHasta) {
		this.anioHasta = anioHasta;
	}

	public DecJurVO getDecJur() {
		return decJur;
	}

	public void setDecJur(DecJurVO decJur) {
		this.decJur = decJur;
	}



	// View getters
	public boolean isModoVer() {
		return modoVer;
	}	
	public void setModoVer(boolean modoVer) {
		this.modoVer = modoVer;
	}

	// Flags Seguridad
	public Boolean getVueltaAtrasBussEnabled() {
		return vueltaAtrasBussEnabled;
	}

	public void setVueltaAtrasBussEnabled(Boolean vueltaAtrasBussEnabled) {
		this.vueltaAtrasBussEnabled = vueltaAtrasBussEnabled;
	}
	
	public String getVueltaAtrasEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getVueltaAtrasBussEnabled(), 
				GdeSecurityConstants.ABM_DECJUR, GdeSecurityConstants.ABM_DECJUR_VUELTA_ATRAS);
	}
	
}
