//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.model.SiatPageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;
import ar.gov.rosario.siat.esp.iface.util.EspSecurityConstants;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Search Page de HabExe
 * @author tecso
 *
 */
public class HabExeSearchPage extends SiatPageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "habExeSearchPageVO";

	private HabExeVO habExe = new HabExeVO();

	private Date fechaDesde;
	private Date fechaHasta;
	
	private String fechaDesdeView;
	private String fechaHastaView;
	
	
	public HabExeSearchPage(){
		super(EspSecurityConstants.ABM_HABEXE);
	}

	// Getters & Setters
	public HabExeVO getHabExe() {
		return habExe;
	}
	public void setHabExe(HabExeVO habExe) {
		this.habExe = habExe;
	}

	public String getName(){
		return NAME;
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

	public String getAgregarEntVenEnabled(){
		return SiatBussImageModel.hasEnabledFlag
		(EspSecurityConstants.ABM_ENTVEN, BaseSecurityConstants.AGREGAR);
	}
}
