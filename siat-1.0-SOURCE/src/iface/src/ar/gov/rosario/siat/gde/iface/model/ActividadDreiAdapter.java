//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.gde.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.RecConADecVO;
import ar.gov.rosario.siat.rec.iface.util.RecSecurityConstants;

/**
 * Adapter para Actividades de Drei
 * 
 * @author tecso
 */
public class  ActividadDreiAdapter extends SiatAdapterModel{
	
	public static final String NAME = "actividadDreiAdapterVO";
	
	private RecConADecVO recConADec= new RecConADecVO();
    
    private List<RecConADecVO>listActividades = new ArrayList<RecConADecVO>();
   
    
    // Constructores
    public ActividadDreiAdapter(){
    	super(RecSecurityConstants.ABM_NOVEDADRS);
    }

    //  Getters y Setters

	
	public RecConADecVO getRecConADec() {
		return recConADec;
	}


	public void setRecConADec(RecConADecVO recConADec) {
		this.recConADec = recConADec;
	}


	public List<RecConADecVO> getListActividades() {
		return listActividades;
	}


	public void setListActividades(List<RecConADecVO> listActividades) {
		this.listActividades = listActividades;
	}




	

	// View getters
}
