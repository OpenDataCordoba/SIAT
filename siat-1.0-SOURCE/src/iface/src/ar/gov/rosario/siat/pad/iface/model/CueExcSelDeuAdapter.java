//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.pad.iface.model;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.model.LiqDeudaAdapter;
import ar.gov.rosario.siat.pad.iface.util.PadSecurityConstants;

/**
 * Adapter del CueExcSelDeu
 * 
 * @author tecso
 */
public class CueExcSelDeuAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "cueExcSelDeuAdapterVO";
	
    private CueExcSelDeuVO cueExcSelDeu = new CueExcSelDeuVO();
    
    private LiqDeudaAdapter liqDeudaAdapter = new LiqDeudaAdapter();
    
    private String[] listIdDeudaSelected;
    
    // Constructores
    public CueExcSelDeuAdapter(){
    	super(PadSecurityConstants.ABM_CUEEXCSELDEU);
    }
    
    //  Getters y Setters
	public CueExcSelDeuVO getCueExcSelDeu() {
		return cueExcSelDeu;
	}

	public void setCueExcSelDeu(CueExcSelDeuVO cueExcSelDeuVO) {
		this.cueExcSelDeu = cueExcSelDeuVO;
	}

	public LiqDeudaAdapter getLiqDeudaAdapter() {
		return liqDeudaAdapter;
	}

	public void setLiqDeudaAdapter(LiqDeudaAdapter liqDeudaAdapter) {
		this.liqDeudaAdapter = liqDeudaAdapter;
	}

	public String[] getListIdDeudaSelected() {
		return listIdDeudaSelected;
	}

	public void setListIdDeudaSelected(String[] listIdDeudaSelected) {
		this.listIdDeudaSelected = listIdDeudaSelected;
	}
	
	// View getters
}
