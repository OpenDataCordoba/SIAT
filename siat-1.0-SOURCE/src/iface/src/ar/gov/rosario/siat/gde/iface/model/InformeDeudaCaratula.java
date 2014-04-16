//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import coop.tecso.demoda.iface.model.BussImageModel;


public class InformeDeudaCaratula extends BussImageModel {

	private static final long serialVersionUID = 1L;
	
	private String desTipoTramite = "";
	private String nroLiquidacion = "";
	private String nroRecibo = "";
	private LiqCuentaVO cuenta = new LiqCuentaVO();


	// Getters y Setters
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}

	public String getNroLiquidacion() {
		return nroLiquidacion;
	}
	public void setNroLiquidacion(String nroLiquidacion) {
		this.nroLiquidacion = nroLiquidacion;
	}
	public String getNroRecibo() {
		return nroRecibo;
	}
	public void setNroRecibo(String nroRecibo) {
		this.nroRecibo = nroRecibo;
	}
	public String getDesTipoTramite() {
		return desTipoTramite;
	}
	public void setDesTipoTramite(String desTipoTramite) {
		this.desTipoTramite = desTipoTramite;
	}
	
	public boolean getIsNroRecibo(){
		if (nroRecibo.endsWith("- 0")){
			return false;			
		} else {
			return true;
		}
	}
	
	public String getCodRefPag(){
		return nroRecibo.split("-")[0];
	}
	
}
