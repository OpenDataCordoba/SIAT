//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.ef.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.base.iface.model.SiatBussImageModel;
import ar.gov.rosario.siat.ef.iface.util.EfSecurityConstants;

/**
 * Adapter del OpeInv
 * 
 * @author tecso
 */
public class ActaInvAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "actaInvAdapterVO";
	
    private OpeInvConVO opeInvCon = new OpeInvConVO();
    
    private List<EstadoOpeInvConVO> listEstadoOpeInvCon= new ArrayList<EstadoOpeInvConVO>(); 
    
    
    private boolean pedidoAprobacionBussEnabled = true;
    
    // Constructores
    public ActaInvAdapter(){
    	super(EfSecurityConstants.ABM_ACTAINV);
    }


    //  Getters y Setters
	public OpeInvConVO getOpeInvCon() {
		return opeInvCon;
	}


	public void setOpeInvCon(OpeInvConVO opeInvCon) {
		this.opeInvCon = opeInvCon;
	}


	public List<EstadoOpeInvConVO> getListEstadoOpeInvCon() {
		return listEstadoOpeInvCon;
	}


	public void setListEstadoOpeInvCon(List<EstadoOpeInvConVO> listEstadoOpeInvCon) {
		this.listEstadoOpeInvCon = listEstadoOpeInvCon;
	}


	public String getPedidoAprobacionEnabled() {
		return SiatBussImageModel.hasEnabledFlag(EfSecurityConstants.ABM_ACTAINV, EfSecurityConstants.ACT_PEDIDO_APROBACION);
	}


	public boolean getPedidoAprobacionBussEnabled() {
		return pedidoAprobacionBussEnabled;
	}


	public void setPedidoAprobacionBussEnabled(boolean pedidoAprobacionBussEnabled) {
		this.pedidoAprobacionBussEnabled = pedidoAprobacionBussEnabled;
	}
    
	

	// View getters
}
