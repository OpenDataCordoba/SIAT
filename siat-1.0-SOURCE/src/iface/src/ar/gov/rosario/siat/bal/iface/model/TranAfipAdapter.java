//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.base.iface.util.BaseSecurityConstants;

/**
 * Adapter del TranAfip
 * 
 * @author tecso
 */
public class TranAfipAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "tranAfipAdapterVO";
	
    private TranAfipVO tranAfip = new TranAfipVO();   
    
    // Flags
    private Boolean eliminarDetallePagoBussEnabled    = true;
	private Boolean eliminarDetalleDJBussEnabled    = true;
   
	// Constructores
    public TranAfipAdapter(){
    	super(BalSecurityConstants.ABM_TRANAFIP);
    }
    
    //  Getters y Setters
	public TranAfipVO getTranAfip() {
		return tranAfip;
	}

	public void setTranAfip(TranAfipVO tranAfipVO) {
		this.tranAfip = tranAfipVO;
	}
	
	public String getName(){
		return NAME;
	}
	
	public Boolean getEliminarDetallePagoBussEnabled() {
		return eliminarDetallePagoBussEnabled;
	}

	public void setEliminarDetallePagoBussEnabled(Boolean eliminarDetallePagoBussEnabled) {
		this.eliminarDetallePagoBussEnabled = eliminarDetallePagoBussEnabled;
	}

	public Boolean getEliminarDetalleDJBussEnabled() {
		return eliminarDetalleDJBussEnabled;
	}

	public void setEliminarDetalleDJBussEnabled(Boolean eliminarDetalleDJBussEnabled) {
		this.eliminarDetalleDJBussEnabled = eliminarDetalleDJBussEnabled;
	}

	public String getEliminarDetalleDJEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEliminarDetalleDJBussEnabled(),
				BalSecurityConstants.ABM_DETALLEDJ, BaseSecurityConstants.ELIMINAR);
	}
	
	public String getEliminarDetallePagoEnabled() {
		return SiatBussImageModel.hasEnabledFlag(this.getEliminarDetallePagoBussEnabled(),
				BalSecurityConstants.ABM_DETALLEPAGO, BaseSecurityConstants.ELIMINAR);
	}
			
}