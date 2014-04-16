//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter del DetallePago
 * 
 * @author tecso
 */
public class DetallePagoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "detallePagoAdapterVO";
	
    private DetallePagoVO detallePago = new DetallePagoVO();    
   
    
    // Constructores
    public DetallePagoAdapter(){
    	super(BalSecurityConstants.ABM_DETALLEPAGO);
    }
    
    //  Getters y Setters
	public DetallePagoVO getDetallePago() {
		return detallePago;
	}

	public void setDetallePago(DetallePagoVO detallePagoVO) {
		this.detallePago = detallePagoVO;
	}
	
	public String getName(){
		return NAME;
	}
	

}