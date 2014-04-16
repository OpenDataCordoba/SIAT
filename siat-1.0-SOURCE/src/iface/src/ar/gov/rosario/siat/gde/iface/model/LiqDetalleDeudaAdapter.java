//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del LiqDetalleDeuda
 * 
 * @author tecso
 */
public class LiqDetalleDeudaAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "LiqDetalleDeudaAdapterVO";
	
    private LiqDetalleDeudaVO liqDetalleDeuda = new LiqDetalleDeudaVO();
    // Cuenta Filtro
    private LiqCuentaVO cuenta = new LiqCuentaVO();
    
    // Bandera para mostrar o no el link a ver declaraciones juradas
    private boolean poseeDecJur = false;
    
    // Bandera para mostrar o no los atributos de emision
    private boolean mostrarAtributosEmision = false;
    
    // Bandera para permitir o no la accion de modificar deuda para recursos autoliquidables
    private boolean modificarDeudaBussEnabled = false;
    
    // Constructores
    public LiqDetalleDeudaAdapter(){
    	super(GdeSecurityConstants.ABM_LIQDETALLEDEUDA);
    }
    
    //  Getters y Setters
	public LiqDetalleDeudaVO getLiqDetalleDeuda() {
		return liqDetalleDeuda;
	}

	public void setLiqDetalleDeuda(LiqDetalleDeudaVO LiqDetalleDeudaVO) {
		this.liqDetalleDeuda = LiqDetalleDeudaVO;
	}
	
	public LiqCuentaVO getCuenta() {
		return cuenta;
	}
	public void setCuenta(LiqCuentaVO cuenta) {
		this.cuenta = cuenta;
	}
	// View getters

	public boolean isPoseeDecJur() {
		return poseeDecJur;
	}
	public void setPoseeDecJur(boolean poseeDecJur) {
		this.poseeDecJur = poseeDecJur;
	}

	public boolean isMostrarAtributosEmision() {
		return mostrarAtributosEmision;
	}

	public void setMostrarAtributosEmision(boolean mostrarAtributosEmision) {
		this.mostrarAtributosEmision = mostrarAtributosEmision;
	}

	public boolean getModificarDeudaBussEnabled() {
		return modificarDeudaBussEnabled;
	}

	public void setModificarDeudaBussEnabled(boolean modificarDeudaBussEnabled) {
		this.modificarDeudaBussEnabled = modificarDeudaBussEnabled;
	}
	
	public String getModificarDeudaEnabled() {
		return this.getModificarDeudaBussEnabled() ? ENABLED : DISABLED;
	}
	
	
}
