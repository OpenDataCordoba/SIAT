//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>


package ar.gov.rosario.siat.def.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.util.DefSecurityConstants;

/**
 * Adapter del Calendario
 * 
 * @author tecso
 */
public class CalendarioAdapter extends SiatAdapterModel{
	
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "calendarioAdapterVO";
	
    private CalendarioVO calendario = new CalendarioVO();
    
    private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
    private List<ZonaVO> listZona	    = new ArrayList<ZonaVO>();
     
    // Constructores
    public CalendarioAdapter(){
    	super(DefSecurityConstants.ABM_CALENDARIO);
    }
    
    //  Getters y Setters
	public CalendarioVO getCalendario() {
		return calendario;
	}

	public void setCalendario(CalendarioVO calendarioVO) {
		this.calendario = calendarioVO;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public List<ZonaVO> getListZona() {
		return listZona;
	}

	public void setListZona(List<ZonaVO> listZona) {
		this.listZona = listZona;
	}

	// View getters
}