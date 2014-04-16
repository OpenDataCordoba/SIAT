//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter del Sellado
 * 
 * @author tecso
 */
public class SelladoAdapter extends SiatAdapterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "selladoAdapterVO";

	public static final String ENC_NAME = "encSelladoAdapterVO";
	
    private SelladoVO sellado = new SelladoVO();
    private TipoSelladoVO tipoSellado = new TipoSelladoVO();
    
    // Constructores
    public SelladoAdapter(){
    	super(BalSecurityConstants.ABM_SELLADO);
    	ACCION_MODIFICAR_ENCABEZADO = BalSecurityConstants.ABM_SELLADO_ENC;
    }
    
    //  Getters y Setters
	public SelladoVO getSellado() {
		return sellado;
	}

	public void setSellado(SelladoVO selladoVO) {
		this.sellado = selladoVO;
	}

	public TipoSelladoVO getTipoSellado() {
		return tipoSellado;
	}

	public void setTipoSellado(TipoSelladoVO tipoSellado) {
		this.tipoSellado = tipoSellado;
	}
	
	// View getters
}
