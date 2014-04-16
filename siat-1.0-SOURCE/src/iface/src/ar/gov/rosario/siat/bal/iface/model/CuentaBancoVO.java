//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.def.iface.model.AreaVO;
import ar.gov.rosario.siat.def.iface.model.BancoVO;


/**
 * Value Object del CuentaBanco
 * @author tecso
 *
 */
public class CuentaBancoVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "cuentaBancoVO";
	
	private String nroCuenta;
	private BancoVO banco= new BancoVO();
	private AreaVO area=new AreaVO();
	private TipCueBanVO tipCueBan=new TipCueBanVO();
	private String observaciones;
	
	// Buss Flags
	
	
	// View Constants
	
	
	// Constructores
	public CuentaBancoVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public CuentaBancoVO(int id, String desc) {
		super();
		setId(new Long(id));
		setNroCuenta(desc);
	}
	
	
	public String getDesCuentaBanco() {
		if ( this.getBanco().getDesBanco() != null)
			return this.getBanco().getDesBanco() + " Cta: " + this.getNroCuenta();
		else
			 return this.getNroCuenta();
	}
	
	
	
	// Getters y Setters
	
	public String getNroCuenta() {
		return nroCuenta;
	}

	public void setNroCuenta(String nroCuenta) {
		this.nroCuenta = nroCuenta;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public BancoVO getBanco() {
		return banco;
	}

	public void setBanco(BancoVO banco) {
		this.banco = banco;
	}

	public AreaVO getArea() {
		return area;
	}

	public void setArea(AreaVO area) {
		this.area = area;
	}

	public TipCueBanVO getTipCueBan() {
		return tipCueBan;
	}

	public void setTipCueBan(TipCueBanVO tipCueBan) {
		this.tipCueBan = tipCueBan;
	}

	// Buss flags getters y setters
	
	
	// View flags getters
	
	
	// View getters
}
