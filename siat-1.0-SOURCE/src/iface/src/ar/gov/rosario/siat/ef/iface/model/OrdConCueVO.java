//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.pad.iface.model.CuentaVO;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Value Object del OrdConCue
 * @author tecso
 *
 */
public class OrdConCueVO extends SiatBussImageModel {

	private static final long serialVersionUID = 1L;

	public static final String NAME = "ordConCueVO";
	
	private OrdenControlVO ordenControlVO=new OrdenControlVO();

	private CuentaVO cuenta=new CuentaVO();
	
	private Integer fiscalizar;
	

	// Buss Flags
	private boolean liquidacionDeudaBussEnabled = true;
	private boolean estadoCuentaBussEnabled=true;
	
	// View Constants
	
	// Constructores
	public OrdConCueVO() {
		super();
	}
	
	// Constructor para utilizar este VO en un combo como valor SELECCIONAR, TODOS, etc.
	public OrdConCueVO(int id, String desc) {
		super();
		setId(new Long(id));
		setCuenta(new CuentaVO());
		getCuenta().setNumeroCuenta(desc);
	}
	
	// Getters y Setters
	public OrdenControlVO getOrdenControlVO() {
		return ordenControlVO;
	}

	public void setOrdenControlVO(OrdenControlVO ordenControlVO) {
		this.ordenControlVO = ordenControlVO;
	}

	public CuentaVO getCuenta() {
		return cuenta;
	}

	public void setCuenta(CuentaVO cuenta) {
		this.cuenta = cuenta;
	}


	public Integer getFiscalizar() {
		return fiscalizar;
	}

	public void setFiscalizar(Integer fiscalizar) {
		this.fiscalizar = fiscalizar;
	}

	// Buss flags getters y setters
	public boolean getLiquidacionDeudaBussEnabled() {
		return liquidacionDeudaBussEnabled;
	}
	
	public void setLiquidacionDeudaBussEnabled(boolean liquidacionDeudaBussEnabled) {
		this.liquidacionDeudaBussEnabled = liquidacionDeudaBussEnabled;
	}
	
	public boolean getEstadoCuentaBussEnabled() {
		return estadoCuentaBussEnabled;
	}
	
	public void setEstadoCuentaBussEnabled(boolean estadoCuentaBussEnabled) {
		this.estadoCuentaBussEnabled = estadoCuentaBussEnabled;
	}

	public String getFiscalizarView() {
		if(fiscalizar!=null && fiscalizar.intValue()== SiNo.SI.getId().intValue())
			return SiNo.SI.getValue();
		
		if(fiscalizar==null)
			return "";
		
		return SiNo.NO.getValue();
	}

	
	
	
	// View flags getters
	
	
	// View getters
	
}
