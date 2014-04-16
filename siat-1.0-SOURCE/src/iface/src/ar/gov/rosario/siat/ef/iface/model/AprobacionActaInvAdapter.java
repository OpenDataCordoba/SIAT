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
import ar.gov.rosario.siat.pad.iface.model.TipObjImpDefinition;

/**
 * Adapter del OpeInv
 * 
 * @author tecso
 */
public class AprobacionActaInvAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "aprobacionActaInvAdapterVO";
	
    private OpeInvConVO opeInvCon = new OpeInvConVO();
    
    TipObjImpDefinition definition4Cuenta = new TipObjImpDefinition();
    
    private List<EstadoOpeInvConVO> listEstadoOpeInvCon= new ArrayList<EstadoOpeInvConVO>(); 
    
    private List<EstadoActaVO> listEstadoActa = new ArrayList<EstadoActaVO>();
    
    
    // Constructores
    public AprobacionActaInvAdapter(){
    	super(EfSecurityConstants.ADM_APROBACIONACTAINV);
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


	public String getCambiarEstadoActaEnabled() {
		return SiatBussImageModel.hasEnabledFlag(EfSecurityConstants.ADM_APROBACIONACTAINV, EfSecurityConstants.ACT_CAMBIAR_ESTADO_ACTA);
	}


	public List<EstadoActaVO> getListEstadoActa() {
		return listEstadoActa;
	}


	public void setListEstadoActa(List<EstadoActaVO> listEstadoActaVO) {
		this.listEstadoActa = listEstadoActaVO;
	}


	public TipObjImpDefinition getDefinition4Cuenta() {
		return definition4Cuenta;
	}


	public void setDefinition4Cuenta(TipObjImpDefinition definition4Cuenta) {
		this.definition4Cuenta = definition4Cuenta;
	}

	
	// View getters
}
