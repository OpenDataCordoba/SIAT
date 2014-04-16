//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.rec.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.exe.iface.model.ExencionVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;

/**
 * Adapter del ObraFormaPago
 * 
 * @author tecso
 */
public class ObraFormaPagoAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;

	public static final String NAME = "obraFormaPagoAdapterVO";
	
    private ObraFormaPagoVO obraFormaPago = new ObraFormaPagoVO();
    
    private List<SiNo>           listSiNo = new ArrayList<SiNo>();
    private List<ExencionVO>     listExencion = new ArrayList<ExencionVO>();    
    
    // Constructores
    public ObraFormaPagoAdapter(){
    	super(RecSecurityConstants.ABM_OBRA_FORMAPAGO);
    }
    
    public ObraFormaPagoAdapter(ObraVO obra){
    	super(RecSecurityConstants.ABM_OBRA_FORMAPAGO);
    	this.obraFormaPago = new ObraFormaPagoVO(obra);    	
    }
    
    //  Getters y Setters
	public ObraFormaPagoVO getObraFormaPago() {
		return obraFormaPago;
	}

	public void setObraFormaPago(ObraFormaPagoVO obraFormaPagoVO) {
		this.obraFormaPago = obraFormaPagoVO;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}

	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public List<ExencionVO> getListExencion() {
		return listExencion;
	}

	public void setListExencion(List<ExencionVO> listExencion) {
		this.listExencion = listExencion;
	}

}
