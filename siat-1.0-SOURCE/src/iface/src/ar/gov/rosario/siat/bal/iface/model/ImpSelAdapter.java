//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.bal.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.bal.iface.util.BalSecurityConstants;
import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;

/**
 * Adapter del SelladoImporte
 * 
 * @author tecso
 */
public class ImpSelAdapter extends SiatAdapterModel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String NAME = "impSelAdapterVO";
	
    private ImpSelVO impSel = new ImpSelVO();
    
    private List<TipoSelladoVO> listTipoSellado= new ArrayList<TipoSelladoVO>();
        
    // Constructores
    public ImpSelAdapter(){
    	super(BalSecurityConstants.ABM_IMPSEL);
    }
    
    //  Getters y Setters
	public ImpSelVO getImpSel() {
		return impSel;
	}

	public void setImpSel(ImpSelVO impSelVO) {
		this.impSel = impSelVO;
	}

	public List<TipoSelladoVO> getListTipoSellado() {
		return listTipoSellado;
	}

	public void setListTipoSellado(List<TipoSelladoVO> listTipoSellado) {
		this.listTipoSellado = listTipoSellado;
	}

		
	// View getters
}
