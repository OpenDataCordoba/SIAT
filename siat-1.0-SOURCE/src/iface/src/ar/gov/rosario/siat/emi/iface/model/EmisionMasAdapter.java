//Copyright (c) 2011 Municipalidad de Rosario and Coop. de Trabajo Tecso Ltda.
//This file is part of SIAT. SIAT is licensed under the terms
//of the GNU General Public License, version 3.
//See terms in COPYING file or <http://www.gnu.org/licenses/gpl.txt>

package ar.gov.rosario.siat.emi.iface.model;

import java.util.ArrayList;
import java.util.List;

import ar.gov.rosario.siat.base.iface.model.SiatAdapterModel;
import ar.gov.rosario.siat.def.iface.model.GenericAtrDefinition;
import ar.gov.rosario.siat.def.iface.model.RecursoVO;

/**
 * Adapter de Emision Masiva
 * 
 * @author tecso
 */
public class EmisionMasAdapter extends SiatAdapterModel {
 
	private static final long serialVersionUID = 1L;
	
	public static final String NAME = "emisionMasAdapterVO";
	
	private EmisionVO emision = new EmisionVO();

	private List<RecursoVO> listRecurso = new ArrayList<RecursoVO>();
	
	// Contiene el dominio del atributo de segmentacion
	private GenericAtrDefinition genericAtrDefinition = new GenericAtrDefinition();

	private boolean selectAtrValEnabled = false;
	
	// Constructores
    public EmisionMasAdapter(){
    	super();
    }

    //Getters y Setters
	public EmisionVO getEmision() {
		return emision;
	}

	public void setEmision(EmisionVO emision) {
		this.emision = emision;
	}

	public List<RecursoVO> getListRecurso() {
		return listRecurso;
	}

	public void setListRecurso(List<RecursoVO> listRecurso) {
		this.listRecurso = listRecurso;
	}

	public GenericAtrDefinition getGenericAtrDefinition() {
		return genericAtrDefinition;
	}

	public void setGenericAtrDefinition(GenericAtrDefinition genericAtrDefinition) {
		this.genericAtrDefinition = genericAtrDefinition;
	}

	public boolean isSelectAtrValEnabled() {
		return selectAtrValEnabled;
	}

	public void setSelectAtrValEnabled(boolean selectAtrValEnabled) {
		this.selectAtrValEnabled = selectAtrValEnabled;
	}
	
}


	

