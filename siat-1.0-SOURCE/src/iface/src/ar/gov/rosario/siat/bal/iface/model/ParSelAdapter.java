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
 * Adapter del ParSel
 * 
 * @author tecso
 */
public class ParSelAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "parSelAdapterVO";
	
    private ParSelVO parSel = new ParSelVO();
    
    private List<PartidaVO> listPartida = new ArrayList<PartidaVO>();
    private List<TipoDistribVO> listTipoDistrib = new ArrayList<TipoDistribVO>();
    
    // Constructores
    public ParSelAdapter(){
    	super(BalSecurityConstants.ABM_PARSEL);
    }
    
    //  Getters y Setters
	public ParSelVO getParSel() {
		return parSel;
	}

	public void setParSel(ParSelVO parSelVO) {
		this.parSel = parSelVO;
	}

	public List<PartidaVO> getListPartida() {
		return listPartida;
	}

	public void setListPartida(List<PartidaVO> listPartida) {
		this.listPartida = listPartida;
	}

	public List<TipoDistribVO> getListTipoDistrib() {
		return listTipoDistrib;
	}

	public void setListTipoDistrib(List<TipoDistribVO> listTipoDistrib) {
		this.listTipoDistrib = listTipoDistrib;
	}

	// View getters
}
