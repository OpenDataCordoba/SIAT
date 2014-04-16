//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * SearchPage del Reingresos de la base de Indeterminados
 * 
 * @author Tecso
 *
 */
public class IndetReingSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "indetReingSearchPageVO";
	
	private IndetVO indetReing= new IndetVO();
	
    private Date fechaDesde;
    private Date fechaHasta;

    private String fechaDesdeView = "";
    private String fechaHastaView = "";
    
    private Boolean vueltaAtrasEnabled = true;
    
	// Constructores
	public IndetReingSearchPage() {       
       super(BalSecurityConstants.ABM_INDETREING);
    }
	
	// Getters y Setters
	public IndetVO getIndetReing() {
		return indetReing;
	}
	public void setIndetReing(IndetVO indetReing) {
		this.indetReing = indetReing;
	}           
	public Date getFechaDesde() {
		return fechaDesde;
	}
	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
		this.fechaDesdeView = DateUtil.formatDate(fechaDesde, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaDesdeView() {
		return fechaDesdeView;
	}
	public void setFechaDesdeView(String fechaDesdeView) {
		this.fechaDesdeView = fechaDesdeView;
	}
	public Date getFechaHasta() {
		return fechaHasta;
	}
	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
		this.fechaHastaView = DateUtil.formatDate(fechaHasta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaHastaView() {
		return fechaHastaView;
	}
	public void setFechaHastaView(String fechaHastaView) {
		this.fechaHastaView = fechaHastaView;
	}
	
	public String getName(){    
		return NAME;
	}
	
	public Boolean getVueltaAtrasBussEnabled() {
		return vueltaAtrasEnabled;
	}
	public void setVueltaAtrasBussEnabled(Boolean vueltaAtrasEnabled) {
		this.vueltaAtrasEnabled = vueltaAtrasEnabled;
	}
	public String getVueltaAtrasEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getVueltaAtrasBussEnabled(), 
				BalSecurityConstants.ABM_INDETREING, BaseSecurityConstants.VUELTA_ATRAS);
	}
}
