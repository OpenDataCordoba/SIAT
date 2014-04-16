//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.gde.iface.util.GdeSecurityConstants;

/**
 * Adapter del GesJudEvento
 * 
 * @author tecso
 */
public class GesJudEventoAdapter extends SiatAdapterModel{
	
	public static final String NAME = "gesJudEventoAdapterVO";
	
    private GesJudEventoVO gesJudEvento = new GesJudEventoVO();
    
    private List<EventoVO>   listEvento = new ArrayList<EventoVO>();
    
    // Constructores
    public GesJudEventoAdapter(){
    	super(GdeSecurityConstants.ABM_GESJUDEVENTO);
    }
    
    //  Getters y Setters
	public GesJudEventoVO getGesJudEvento() {
		return gesJudEvento;
	}

	public void setGesJudEvento(GesJudEventoVO gesJudEventoVO) {
		this.gesJudEvento = gesJudEventoVO;
	}

	public List<EventoVO> getListEvento() {
		return listEvento;
	}

	public void setListEvento(List<EventoVO> listEvento) {
		this.listEvento = listEvento;
	}

	
	// View getters
}
