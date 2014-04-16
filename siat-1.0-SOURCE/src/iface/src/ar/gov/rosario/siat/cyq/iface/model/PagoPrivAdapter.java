//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.cyq.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.cyq.iface.util.CyqSecurityConstants;
import coop.tecso.demoda.iface.model.SiNo;
import coop.tecso.demoda.iface.model.TipoCancelacion;

/**
 * Adapter del PagoPriv
 * 
 * @author tecso
 */
public class PagoPrivAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "pagoPrivAdapterVO";
	
    private PagoPrivVO pagoPriv = new PagoPrivVO();
    
    private List<TipoCancelacion> listTipoCancelacion = new ArrayList<TipoCancelacion>();
    
    private List<SiNo> listSiNo = new ArrayList<SiNo>();
    
    private SiNo cancelaDeuda = SiNo.OpcionSelecionar;
    
    // Constructores
    public PagoPrivAdapter(){
    	super(CyqSecurityConstants.ABM_PAGOPRIV);
    }
    
    //  Getters y Setters
	public PagoPrivVO getPagoPriv() {
		return pagoPriv;
	}

	public void setPagoPriv(PagoPrivVO pagoPrivVO) {
		this.pagoPriv = pagoPrivVO;
	}

	public String getName(){
		return NAME;
	}

	public List<SiNo> getListSiNo() {
		return listSiNo;
	}
	public void setListSiNo(List<SiNo> listSiNo) {
		this.listSiNo = listSiNo;
	}

	public SiNo getCancelaDeuda() {
		return cancelaDeuda;
	}
	public void setCancelaDeuda(SiNo cancelaDeuda) {
		this.cancelaDeuda = cancelaDeuda;
	}

	public List<TipoCancelacion> getListTipoCancelacion() {
		return listTipoCancelacion;
	}
	public void setListTipoCancelacion(List<TipoCancelacion> listTipoCancelacion) {
		this.listTipoCancelacion = listTipoCancelacion;
	}
	
	// View getters
}
