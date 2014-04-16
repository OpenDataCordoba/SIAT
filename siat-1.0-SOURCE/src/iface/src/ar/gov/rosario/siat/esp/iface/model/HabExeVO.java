//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.esp.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import coop.tecso.demoda.iface.helper.DateUtil;

/**
 * Bean corresponiente a las Exeniones de la habilitacion
 * 
 * @author tecso
 */
public class HabExeVO extends SiatBussImageModel{

	private static final long serialVersionUID = 1L;
	
	private Date fechaDesde;
	private Date fechaHasta;
	private HabilitacionVO habilitacion = new HabilitacionVO();
	private ExencionVO exencion = new ExencionVO();
	
	private String fechaDesdeView = "";
	private String fechaHastaView = "";
	
	//Constructores 
	public HabExeVO(){
		super();
	}
	
	// Getters Y Setters
	
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

	public HabilitacionVO getHabilitacion() {
		return habilitacion;
	}

	public void setHabilitacion(HabilitacionVO habilitacion) {
		this.habilitacion = habilitacion;
	}

	public ExencionVO getExencion() {
		return exencion;
	}

	public void setExencion(ExencionVO exencion) {
		this.exencion = exencion;
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
	
		
}

