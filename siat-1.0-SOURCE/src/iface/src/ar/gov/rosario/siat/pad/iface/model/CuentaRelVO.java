//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import coop.tecso.demoda.iface.helper.DateUtil;

public class CuentaRelVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

    private CuentaVO cuentaOrigen = new CuentaVO();
    private CuentaVO cuentaDestino = new CuentaVO();
	private Date fechaDesde;
	private Date fechaHasta;
	
	private String fechaDesdeView   = "";
	private String fechaHastaView   = "";

	// Constructores
	public CuentaRelVO() {
		super();
	}

	// Getters y Setters
	public CuentaVO getCuentaDestino() {
		return cuentaDestino;
	}
	public void setCuentaDestino(CuentaVO cuentaDestino) {
		this.cuentaDestino = cuentaDestino;
	}
	public CuentaVO getCuentaOrigen() {
		return cuentaOrigen;
	}
	public void setCuentaOrigen(CuentaVO cuentaOrigen) {
		this.cuentaOrigen = cuentaOrigen;
	}
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
	
	
}
