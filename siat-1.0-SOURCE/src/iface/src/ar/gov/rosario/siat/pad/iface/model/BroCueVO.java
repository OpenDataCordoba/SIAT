//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import java.util.Date;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.cas.iface.model.CasoVO;
import coop.tecso.demoda.iface.helper.DateUtil;

public class BroCueVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	private BrocheVO broche = new BrocheVO();
	private CuentaVO cuenta = new CuentaVO();
	private Date fechaAlta;
	private Date fechaBaja;
	private CasoVO caso = new CasoVO();
	
	private String fechaAltaView = "";
	private String fechaBajaView = "";
	
	// Constructores
	
	public BroCueVO(){
		super();
	}

	//Getters y Setters
	
	public BrocheVO getBroche() {
		return broche;
	}
	public void setBroche(BrocheVO broche) {
		this.broche = broche;
	}
	public CuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	public Date getFechaAlta() {
		return fechaAlta;
	}
	public void setFechaAlta(Date fechaAlta) {
		this.fechaAlta = fechaAlta;
		this.fechaAltaView = DateUtil.formatDate(fechaAlta, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaAltaView() {
		return fechaAltaView;
	}
	public void setFechaAltaView(String fechaAltaView) {
		this.fechaAltaView = fechaAltaView;
	}
	public Date getFechaBaja() {
		return fechaBaja;
	}
	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
		this.fechaBajaView = DateUtil.formatDate(fechaBaja, DateUtil.ddSMMSYYYY_MASK);
	}
	public String getFechaBajaView() {
		return fechaBajaView;
	}
	public void setFechaBajaView(String fechaBajaView) {
		this.fechaBajaView = fechaBajaView;
	}

	
	public CasoVO getCaso() {
		return caso;
	}

	public void setCaso(CasoVO caso) {
		this.caso = caso;
	}
		
}
